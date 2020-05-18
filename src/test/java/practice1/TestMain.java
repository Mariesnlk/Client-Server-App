package practice1;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.apache.commons.codec.binary.Hex.decodeHex;

public class TestMain {

    @Test(expected = MagicByteException.class)
    public void failWhatValidMagicByte() throws DecoderException, MagicByteException, CRC1Exception, CRC2Exception {
        PacketParser.packetParser(decodeHex(("12")));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void workWhatValidMagicByte() throws DecoderException, MagicByteException, CRC1Exception, CRC2Exception {
        PacketParser.packetParser(decodeHex(("13")));
    }

    @Test(expected = CRC1Exception.class)
    public void failCountControlSumCRC1() throws DecoderException, CRC1Exception, CRC2Exception, MagicByteException {
        PacketParser.packetParser(decodeHex(("13 01 00000000000000000A 00000100 022C").replace(" ", "")));
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void workCountControlSumCRC1() throws CRC1Exception, CRC2Exception, MagicByteException {
        PacketParser.packetParser(new byte[]{
                0x13,
                0xB,
                0x0, 0x0, 0x1, 0x1, 0x0, 0x41, 0x15, 0x00,
                0x0, 0x0, 0x0, 0x04,
                0x0F, 0x06

        });
    }

    @Test(expected = CRC2Exception.class)
    public void failCountControlSumCRC2() throws CRC1Exception, CRC2Exception, MagicByteException {
        CreatePacket createPacket = new CreatePacket();
        PacketParser.packetParser(createPacket.createPacket());
    }

    @Test
    public void workSetKey() {
        final String secretKey = "fifi!fifi!!";
        AES.setKey(secretKey);

    }


    @Test(expected = NullPointerException.class)
    public void failSetKey() {
        final String secretKey = null;
        AES.setKey(secretKey);

    }

    @Test
    public void failEncrypt() {
        final String secretKey = "fi";
        String originalString = null;
        AES.encrypt(originalString, secretKey);
    }

    @Test
    public void failDecrypt(){
        final String secretKey = null;
        String originalString = "Hello my dear friend";
        AES.decrypt(originalString, secretKey);

    }


}
