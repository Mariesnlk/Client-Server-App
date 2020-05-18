package practice1;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class PacketParser {


    //прийняти масив байт і відправити
    public static void packetParser(final byte[] inputMessage) throws MagicByteException, CRC1Exception, CRC2Exception {

        if (inputMessage[0] != 0x13) {
            throw new MagicByteException("Not a magic byte");
        }

        final int uniqueClientId = inputMessage[1] & 0xff;//from byte to int convert to have only pos num
        System.out.println("Unique client id: " + uniqueClientId);

        final long packetId = ByteBuffer.wrap(inputMessage, 2, 8).order(ByteOrder.BIG_ENDIAN).getLong();
        System.out.println("Packet id: " + packetId);

        final int messageLength = ByteBuffer.wrap(inputMessage, 10, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        System.out.println("Message length: " + messageLength);

        final short crc1 = ByteBuffer.wrap(inputMessage, 14, 2).order(ByteOrder.BIG_ENDIAN).getShort();
        System.out.println("CRC1: " + crc1);

        final short crc1Actual = CRC16.evaluateCRC(inputMessage, 0, 14);//count control sum

        if (crc1 != crc1Actual) {
            throw new CRC1Exception("Input CRC1. Actual: " + crc1Actual + ", but was: " + crc1);
        }

        //our message
        byte[] message = new byte[messageLength];
        System.arraycopy(inputMessage, 16, message, 0, messageLength);
        System.out.println("Input message: " + new String(message, StandardCharsets.UTF_8));


//        final short crc2 = ByteBuffer.wrap(inputMessage, 16 + messageLength, 2).order(ByteOrder.BIG_ENDIAN).getShort();
//        System.out.println("CRC2: " + crc2);
//
//        final short crc2Actual = CRC16.evaluateCRC(message, 0, messageLength);//count control sum
//
//        if (crc2 != crc2Actual) {
//            throw new CRC2Exception("Input CRC2. Actual: " + crc2Actual + ", but was: " + crc2);
//        }

        //message parser
        final int typeID = ByteBuffer.wrap(message, 0, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        System.out.println("Type: " + typeID);

        final int userID = ByteBuffer.wrap(message, 4, 4).order(ByteOrder.BIG_ENDIAN).getInt();
        System.out.println("User id: " + userID);

        //our useful info
        byte[] usefulInfo = new byte[messageLength - 8];
        System.arraycopy(message, 8, usefulInfo, 0, messageLength - 8);
        System.out.println("Useful information: " + new String(usefulInfo, StandardCharsets.UTF_8));

        decryptMessage();

    }

    public static String decryptMessage() {
        //decrypt message
        final String secretKey = "fifi!fifi!!";

        String decryptedString = AES.decrypt(CreatePacket.encryptMessage(), secretKey);
        System.out.println("Decrypted unique message is: " + decryptedString);

        return decryptedString;

    }


    public static void main(String[] args) throws MagicByteException, CRC1Exception, CRC2Exception {

        CreatePacket createPacket = new CreatePacket();
        packetParser(createPacket.createPacket());
        
    }
}
