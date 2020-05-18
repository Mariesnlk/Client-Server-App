package practice1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CreatePacket {


    private final static int TYPEID = 1;
    private final static int USERID = 2;
    private final static String MESSAGENEED = "Hello, nice to see you";

    public static byte[] createMessage() {

//    public static void createMessage() {


        //encrypt message
        final String secretKey = "fifi!fifi!!";

        String originalString = CreatePacket.MESSAGENEED;
        String encryptedString = AES.encrypt(originalString, secretKey);
        System.out.println(originalString);
        System.out.println(encryptedString);
        //        String decryptedString = AES.decrypt(encryptedString, secretKey);
        //        System.out.println(decryptedString);


        // string to array byte
        final byte[] byteArrayString = encryptedString.getBytes(StandardCharsets.UTF_8);
        System.out.println("Convert string to byte array: " + Arrays.toString(byteArrayString));

        //create all message
        byte[] bbMessage = ByteBuffer.allocate(4 + 4 + byteArrayString.length)
                .putInt(CreatePacket.TYPEID)
                .putInt(CreatePacket.USERID)
                .put(byteArrayString).array();

        System.out.println("Create unique message ready: " + bbMessage);

        return bbMessage;


    }

    //public static void createPacket() {

    public static byte[] createPacket() {

        final byte[] bytes = CreatePacket.createMessage();
        System.out.println(bytes);

        final byte[] header = new byte[]{
                0x13,
                0x0,
                0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x0, 0x16,
                0x0, 0x0, 0x0, (byte) bytes.length

        };

       // byte[]


        return ByteBuffer.allocate(14 + 2 + bytes.length + 2)
                .put(header)
                .putShort(CRC16.evaluateCRC(header, 0, header.length))
                .put(bytes)
                .putShort(CRC16.evaluateCRC(bytes, 0, header.length))
                .array();
    }


    public static void main(String[] args) {

        // createMessage();
        createPacket();
    }


}
