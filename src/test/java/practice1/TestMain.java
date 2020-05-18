package practice1;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.apache.commons.codec.binary.Hex.decodeHex;

public class TestMain {

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

    @Test(expected = CRC2Exception.class)
    public void failCountControlSumCRC2() throws CRC1Exception, CRC2Exception, MagicByteException {
        CreatePacket createPacket = new CreatePacket();
        PacketParser.packetParser(createPacket.createPacket());
    }

}
