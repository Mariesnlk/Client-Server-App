package practice1;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.apache.commons.codec.binary.Hex.decodeHex;

public class TestMain {

//    @Test
//    public void trueWhenValidPacket() throws DecoderException, CRC1Exception, CRC2Exception, MagicByteException {
//        final String plainText = "My plain t";
//        final byte[] plainBytes = plainText.getBytes(StandardCharsets.UTF_8);
//        Main.packetParser(decodeHex(("13 00 0000101000000001 0000000A 4163" + Hex.encodeHexString(plainBytes) + " 0B3F").replace(" ", "")));
//
//    }


    @Test(expected = MagicByteException.class)
    public void failWhatValidMagicByte() throws DecoderException, MagicByteException, CRC1Exception, CRC2Exception {
        PacketParser.packetParser(decodeHex(("12")));
    }

    @Test
    public void workWhatValidMagicByte() throws DecoderException, MagicByteException, CRC1Exception, CRC2Exception {
        PacketParser.packetParser(decodeHex(("13")));
    }

    @Test(expected = CRC1Exception.class)
    public void failCountControlSumCRC1() throws DecoderException, CRC1Exception, CRC2Exception, MagicByteException {
        PacketParser.packetParser(decodeHex(("13 01 00000000000000000A 00000100 022C").replace(" ", "")));
    }

    @Test
    public void workCountControlSumCRC1() throws CRC1Exception, CRC2Exception, MagicByteException {
        PacketParser.packetParser(new byte[]{
                0x13,
                0xB,
                0x0, 0x0, 0x1, 0x1, 0x0, 0x41, 0x15, 0x00,
                0x0, 0x0, 0x0, 0x04,
                0x0F, 0x06

        });
    }


}
