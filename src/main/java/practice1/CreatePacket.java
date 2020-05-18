package practice1;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class CreatePacket {


    private final static int TYPEID = 1;
    private final static int USERID = 2;
    private final static String MESSAGENEED = "Hello, nice to see you";

    public static String createMessage() {

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


        String response = Arrays.toString(bbMessage);

        String[] byteValues = response.substring(1, response.length() - 1).split(",");
        byte[] bytes = new byte[byteValues.length];

        for (int i = 0, len = bytes.length; i < len; i++) {
            bytes[i] = Byte.parseByte(byteValues[i].trim());
        }

        String str = new String(bytes);

        System.out.println("Create unique message ready: " + str);

        return str;


    }

    public static byte[] createPacket() {

        final String response = CreatePacket.createMessage();
        final byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        final byte[] header = new byte[]{
                0x13,
                0x0,
                0x0, 0x0, 0x0, 0x1, 0x1, 0x1, 0x1, 0x10,
                0x0, 0x0, 0x0, (byte) CreatePacket.createMessage().length()

        };

        return ByteBuffer.allocate(14 + 2 + CreatePacket.createMessage().length() + 2)
                .put(header)
                .putShort(CRC16.evaluateCRC(header, 0, header.length))
                .put(bytes)
                .putShort(CRC16.evaluateCRC(bytes, 0, header.length))
                .array();
    }


    public static void main(String[] args) {

        createMessage();
        
        //System.out.println("Length of all message: " + CreatePacket.createMessage().length());
        
        createPacket();
    }


}
