package com.yan.stamped;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

import java.util.Arrays;

public class CardService extends HostApduService{
    private static final String TAG = "Stamped";
    NotificationManager NM;
    // AID for our loyalty card service.
    private static final String SAMPLE_LOYALTY_CARD_AID = "F285623147";
    // ISO-DEP command HEADER for selecting an AID.
    // Format: [Class | Instruction | Parameter 1 | Parameter 2]
    private static final String SELECT_APDU_HEADER = "00A40400";
    // "OK" status word sent in response to SELECT AID command (0x9000)
    private static final byte[] SELECT_OK_SW = HexStringToByteArray("9000");
    // "UNKNOWN" status word sent in response to invalid APDU command (0x0000)
    private static final byte[] UNKNOWN_CMD_SW = HexStringToByteArray("0000");
    private static final byte[] SELECT_APDU = BuildSelectApdu(SAMPLE_LOYALTY_CARD_AID);
    UserFunctions userfunctions = new UserFunctions();

    public void onDeactivated(int reason) { }

    public byte[] processCommandApdu(byte[] commandApdu, Bundle extras) {
        Log.i(TAG, "Received APDU: " + ByteArrayToHexString(commandApdu));
        // If the APDU matches the SELECT AID command for this service,
        // send the loyalty card account number, followed by a SELECT_OK status trailer (0x9000).
        if (Arrays.equals(SELECT_APDU, commandApdu)) {
            String account = userfunctions.getUserEmail(getApplicationContext())+userfunctions.getRewardMessage();
            byte[] accountBytes = account.getBytes();
            Log.i(TAG, "Sending account number: " + account);
            userfunctions.sync(getApplicationContext());
            userfunctions.dismissAllDialogs();
            if(userfunctions.getRewardMessage() != "") {
                userfunctions.setRewardMessage("");
            } else {
                Notify("New Stamp","Received New Stamp!","Click here to check it out");
            }
            return ConcatArrays(accountBytes, SELECT_OK_SW);
        } else {
            return UNKNOWN_CMD_SW;
        }
    }

    public static byte[] BuildSelectApdu(String aid) {
        // Format: [CLASS | INSTRUCTION | PARAMETER 1 | PARAMETER 2 | LENGTH | DATA]
        return HexStringToByteArray(SELECT_APDU_HEADER + String.format("%02X",
                aid.length() / 2) + aid);
    }

    public static String ByteArrayToHexString(byte[] bytes) {
        final char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        char[] hexChars = new char[bytes.length * 2]; // Each byte has two hex characters (nibbles)
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF; // Cast bytes[j] to int, treating as unsigned value
            hexChars[j * 2] = hexArray[v >>> 4]; // Select hex character from upper nibble
            hexChars[j * 2 + 1] = hexArray[v & 0x0F]; // Select hex character from lower nibble
        }
        return new String(hexChars);
    }

    public static byte[] HexStringToByteArray(String s) throws IllegalArgumentException {
        int len = s.length();
        if (len % 2 == 1) {
            throw new IllegalArgumentException("Hex string must have even number of characters");
        }
        byte[] data = new byte[len / 2]; // Allocate 1 byte per 2 hex characters
        for (int i = 0; i < len; i += 2) {
            // Convert each character into a integer (base-16), then bit-shift into place
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static byte[] ConcatArrays(byte[] first, byte[]... rest) {
        int totalLength = first.length;
        for (byte[] array : rest) {
            totalLength += array.length;
        }
        byte[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (byte[] array : rest) {
            System.arraycopy(array, 0, result, offset, array.length);
            offset += array.length;
        }
        return result;
    }

    @SuppressWarnings("deprecation")
    public void Notify(String title, String subject, String body) {
        NM=(NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Notification notify=new Notification(android.R.drawable.
                stat_notify_more,title,System.currentTimeMillis());
        final Intent intent = new Intent(this, Main.class);
        PendingIntent pending=PendingIntent.getActivity(
                getApplicationContext(),0, intent,0);
        notify.setLatestEventInfo(getApplicationContext(),subject,body,pending);
        NM.notify(0, notify);
    }

}
