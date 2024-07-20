package yamone.thesis.desWithLZW.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWUtils {

    private static final double MAX_TABLE_SIZE = Math.pow(2, 16);

    public static String compress(String text){

        double table_Size =  255;

        Map<String, Integer> TABLE = new HashMap<String, Integer>();

        for (int i = 0; i < 255 ; i++)
            TABLE.put("" + (char) i, i);

        String initString = "";

        List<Integer> encoded_values = new ArrayList<Integer>();

        for (char symbol : text.toCharArray()) {

            System.out.println("For symbol: " + symbol);

            String Str_Symbol = initString + symbol;
            System.out.println("Str_Symbol: " + Str_Symbol);

            if (TABLE.containsKey(Str_Symbol)){
                initString = Str_Symbol;
                System.out.println("Contains in TABLE");
                System.out.println("InitString is " + initString);
            }
            else {
                System.out.println("Not contains in TABLE");
                System.out.println("Encoded value is added -> " + TABLE.get(initString));

                encoded_values.add(TABLE.get(initString));

                if(table_Size < MAX_TABLE_SIZE)
                    TABLE.put(Str_Symbol, (int) table_Size++);
                initString = "" + symbol;
            }
        }

        if (!initString.equals(""))
            encoded_values.add(TABLE.get(initString));

        StringBuilder encodedString = new StringBuilder();
        encoded_values.forEach(v -> encodedString.append((char) v.intValue()));

        return encodedString.toString();

    }

    public static String decompress(String compressString){

        List<Integer> get_compress_values = new ArrayList<>();
        int table_Size = 255;

        for (int i = 0; i < compressString.length(); i++) {
            get_compress_values.add((int) compressString.charAt(i));
        }

        Map<Integer, String> TABLE = new HashMap<>();
        for (int i = 0; i < 255; i++)
            TABLE.put(i, "" + (char) i);

        String Encode_values = "" + (char) (int) get_compress_values.remove(0);

        StringBuffer decoded_values = new StringBuffer(Encode_values);

        String get_value_from_table = null;
        for (int check_key : get_compress_values) {

            if (TABLE.containsKey(check_key))
                get_value_from_table = TABLE.get(check_key);
            else if (check_key == table_Size)
                get_value_from_table = Encode_values + Encode_values.charAt(0);

            decoded_values.append(get_value_from_table);

            if(table_Size < MAX_TABLE_SIZE )
                TABLE.put(table_Size++, Encode_values + get_value_from_table.charAt(0));

            Encode_values = get_value_from_table;
        }

        return decoded_values.toString();
    }

    public static byte[] compressFile(byte[] fileBytes) throws IOException {
        // Read file content as string
        String content = Base64.getEncoder().encodeToString(fileBytes);
        // Compress the content
        String compressedContent = compress(content);
        // Write the compressed content to output file
        byte[] fileByte = compressedContent.getBytes(StandardCharsets.ISO_8859_1);
        return fileBytes;
    }

    public static byte[] decompressFile(byte[] fileBytes) throws IOException {
        // Read compressed content from file
        String compressedContent = Base64.getEncoder().encodeToString(fileBytes);
        // Decompress the content
        String decompressedContent = decompress(compressedContent);
        byte[] fileByte = decompressedContent.getBytes(StandardCharsets.ISO_8859_1);
        return fileBytes;
    }

    public static void main(String[] args) {
        String originalString = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
                "\n" +
                "The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.";
        String compress = compress(originalString);
        System.out.println(compress);
        System.out.format("Original %s, Compressed %s \n", originalString.length(), compress.length());

        String decompress = decompress(compress);
        System.out.println(decompress);
    }

}
