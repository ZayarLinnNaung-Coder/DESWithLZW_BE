package yamone.thesis.desWithLZW.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LZWUtilsWithSout {

    private static double MAX_TABLE_SIZE = Math.pow(2, 16);

    public static String compress(String text){

        double table_Size =  255;

        Map<String, Integer> TABLE = new HashMap<String, Integer>();

        for (int i = 0; i < 255 ; i++)
            TABLE.put("" + (char) i, i);

        String initString = "";

        List<Integer> encoded_values = new ArrayList<Integer>();

        for (char symbol : text.toCharArray()) {

            System.out.println(TABLE);
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

            System.out.println();

        }

        if (!initString.equals(""))
            encoded_values.add(TABLE.get(initString));

        StringBuilder encodedString = new StringBuilder();
        encoded_values.forEach(v -> encodedString.append((char) v.intValue()));

        return encodedString.toString();

    }

    public static String decompress(String compressString){
        System.out.println("***************************");
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
            System.out.println(TABLE);
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

    public static void main(String[] args) {
        String originalString = "...";
        String compress = compress(originalString);
        System.out.println(compress);
        System.out.format("Original %s, Compressed %s \n", originalString.length(), compress.length());

        String decompress = decompress("Here is thĂdemo vĀsion of LZW algorićm. ThĄăąćĂĠiginĝ stġng");
        System.out.println(decompress);
    }

}
