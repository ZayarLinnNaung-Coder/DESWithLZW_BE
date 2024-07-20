package yamone.thesis.desWithLZW.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;

public class HexUtils {

//    public static void main(String[] args) {
//
//        String input = "\u0002õû\u009A½¿\u007F¢\u001Bù¿ó¢{«Ð 9\u001Aòéÿ\u008F\u0017";
//        System.out.println("input : " + input);
//
//        String hex = convertStringToHex(input);
//        System.out.println("hex : " + hex);
//    }

    public static String convertStringToHex(String str) {

        // display in uppercase
        char[] chars = Hex.encodeHex(str.getBytes(StandardCharsets.UTF_16LE), false);

        return String.valueOf(chars);
    }

    public static String convertHexToString(String hex) {

        String result = "";
        try {
            byte[] bytes = Hex.decodeHex(hex);
            result = new String(bytes, StandardCharsets.UTF_16LE);
        } catch (DecoderException e) {
            throw new IllegalArgumentException("Invalid Hex format!");
        }
        return result;
    }
}
