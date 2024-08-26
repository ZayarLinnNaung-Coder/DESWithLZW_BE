package yamone.thesis.desWithLZW.utils;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

public class DESUtils {
	
	public static int KEY_LENGTH = 64; 
	
	private static int[] PC1 = 
	{  
		57, 49, 41, 33, 25, 17,  9,
         1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
         7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
	};
	
	// First index is garbage value, loops operating on this should start with index = 1
	private static int[] KEY_SHIFTS = 
	{
		0,  1,  1,  2,  2,  2,  2,  2,  2,  1,  2,  2,  2,  2,  2,  2,  1
	};
	
	private static int[] PC2 = 
	{
		14, 17, 11, 24,  1,  5,
         3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8,
        16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
	};
	
	
	private static int[][] s1 = {
		{14, 4, 13,  1,  2, 15, 11,  8,  3, 10,  6, 12,  5,  9,  0,  7},
		{0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11,  9,  5,  3,  8},
		{4, 1, 14,  8, 13,  6, 2, 11, 15, 12,  9,  7,  3, 10,  5,  0},
		{15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
	 };

	private static int[][] s2 = {
			{15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10},
			{3, 13,  4, 7, 15,  2,  8, 14, 12,  0, 1, 10,  6,  9, 11,  5},
			{0, 14, 7, 11, 10,  4, 13,  1,  5,  8, 12,  6,  9,  3,  2, 15},
			{13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14,  9}
		 };
	
	private static int[][] s3 = {
			{10, 0, 9, 14, 6, 3, 15, 5,  1, 13, 12, 7, 11, 4, 2,  8},
			{13, 7, 0, 9, 3,  4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1},
			{13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14,  7},
			{1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12}
		 };
	
	private static int[][] s4 = {
			{7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15},
			{13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14,  9},
			{10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4},
			{3, 15, 0, 6, 10, 1, 13, 8, 9,  4, 5, 11, 12, 7, 2, 14}
		 };
	
	private static int[][] s5 = {
			{2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9},
			{14, 11, 2, 12,  4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6},
			{4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14},
			{11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3}
		  };
	
	private static int[][] s6 = {
			{12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11},
			{10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8},
			{9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6},
			{4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13}
		  };
	
	private static int[][] s7 = {
			{4, 11, 2, 14, 15,  0, 8, 13 , 3, 12, 9 , 7,  5, 10, 6, 1},
			{13 , 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6},
			{1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2},
			{6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12}
		  };
	
	private static int[][] s8 = {
			{13, 2, 8,  4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7},
			{1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6 ,11, 0, 14, 9, 2},
			{7, 11, 4, 1, 9, 12, 14, 2,  0, 6, 10 ,13, 15, 3, 5, 8},
			{2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6 ,11}
		};
	
	private static int[][][] s = {s1, s2, s3, s4, s5, s6, s7, s8};
	
	private static int[] g = 
	{
		32,  1,  2,  3,  4,  5,
		 4,  5,  6,  7,  8,  9, 
		 8,  9, 10, 11, 12, 13, 
		12, 13, 14, 15, 16, 17,
		16, 17, 18, 19, 20, 21, 
		20, 21, 22, 23, 24, 25, 
		24, 25, 26, 27, 28, 29, 
		28, 29, 30, 31, 32,  1
	};
		
	
	static int[] p = 
	{
		16,  7, 20, 21, 
		29, 12, 28, 17, 
		 1, 15, 23, 26, 
		 5, 18, 31, 10, 
		 2,  8, 24, 14, 
		32, 27,  3,  9, 
		19, 13, 30,  6, 
		22, 11,  4, 25
	};
	
	static int[] IP = 
	{
		 58, 50, 42, 34, 26, 18, 10 , 2,
         60, 52, 44, 36, 28, 20, 12, 4,
         62, 54, 46, 38, 30, 22, 14, 6,
         64, 56, 48, 40, 32, 24, 16, 8,
         57, 49, 41, 33, 25, 17, 9, 1,
         59, 51, 43, 35, 27, 19, 11, 3,
         61, 53, 45, 37, 29, 21, 13, 5,
         63, 55, 47, 39, 31, 23, 15, 7
	};
	
	static int[] IPi = 
	{
			40, 8, 48, 16, 56, 24, 64, 32,
	        39, 7, 47, 15, 55, 23, 63, 31,
	        38, 6, 46, 14, 54, 22, 62, 30,
	        37, 5, 45, 13, 53, 21, 61, 29,
	        36, 4, 44, 12, 52, 20, 60, 28,
	        35, 3, 43 ,11, 51, 19, 59, 27,
	        34, 2, 42, 10, 50, 18, 58, 26,
	        33, 1, 41, 9, 49, 17, 57, 25
	};
	
	private static long[] K = new long[17];
	
	public static String binToHex(String bin) {
		
		BigInteger b = new BigInteger(bin, 2);
		String ciphertext = b.toString(16);
		
		return ciphertext;
	}
	
	public static String hexToBin(String hex) {
		
		BigInteger b = new BigInteger(hex, 16);
		String bin = b.toString(2);
		
		return bin;
	}
	
	public static String binToUTF(String bin) {
		
		// Convert back to String
		byte[] ciphertextBytes = new byte[bin.length()/8];
		String ciphertext = null;
		for(int j = 0; j < ciphertextBytes.length; j++) {
	        String temp = bin.substring(0, 8);
	        byte b = (byte) Integer.parseInt(temp, 2);
	        ciphertextBytes[j] = b;
	        bin = bin.substring(8);
	    }
		
		try {
			ciphertext = new String(ciphertextBytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ciphertext.trim();
	}
	
	public static String utfToBin(String utf) {
		
		// Convert to binary
		byte[] bytes = null;
		try {
			bytes = utf.getBytes("utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String bin = "";
		for (int i = 0; i < bytes.length; i++) {
		     int value = bytes[i];
		     for (int j = 0; j < 8; j++)
		     {
		        bin += ((value & 128) == 0 ? 0 : 1);
		        value <<= 1;
		     }
		}
		return bin;
	}
	
	/**
	 * Encrypt a string message with the DES block cipher 
	 * @param key
	 * @param plaintext
	 * @return
	 */
	public static byte[] encryptFile(String key, byte[] fileBytes) throws Exception {

		String plaintext = Base64.getEncoder().encodeToString(fileBytes);

		// Build the key schedule
		buildKeySchedule(hash(key));
		
		String binPlaintext = plaintext;
		
		// Add padding if necessary
		int remainder = binPlaintext.length() % 64;
		if (remainder != 0) {
			for (int i = 0; i < (64 - remainder); i++)
				binPlaintext = "0" + binPlaintext;
		}
		
		// Separate binary plaintext into blocks
		String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
		int offset = 0;
		for (int i = 0; i < binPlaintextBlocks.length; i++) {
			binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
			offset += 64;
		}
		
		String[] binCiphertextBlocks = new String[binPlaintext.length()/64];
		
		// Encrypt the blocks
		for (int i = 0; i < binCiphertextBlocks.length; i++)
			try {
				binCiphertextBlocks[i] = encryptBlock(binPlaintextBlocks[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		// Build the ciphertext binary string from the blocks
		String binCiphertext = "";
		for (int i = 0; i < binCiphertextBlocks.length; i++) 
			binCiphertext += binCiphertextBlocks[i];
			
		// Destroy key schedule
		for (int i=0;i<K.length;i++)
			K[i] = 0;
		
		
		return Base64.getDecoder().decode(binCiphertext);
	}

	public static String encrypt(String key, String plaintext) throws Exception {

		// Build the key schedule
		buildKeySchedule(hash(key));

		String binPlaintext = plaintext;

		// Add padding if necessary
		int remainder = binPlaintext.length() % 64;
		if (remainder != 0) {
			for (int i = 0; i < (64 - remainder); i++)
				binPlaintext = "0" + binPlaintext;
		}

		// Separate binary plaintext into blocks
		String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
		int offset = 0;
		for (int i = 0; i < binPlaintextBlocks.length; i++) {
			binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
			offset += 64;
		}

		String[] binCiphertextBlocks = new String[binPlaintext.length()/64];

		// Encrypt the blocks
		for (int i = 0; i < binCiphertextBlocks.length; i++)
			try {
				binCiphertextBlocks[i] = encryptBlock(binPlaintextBlocks[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		// Build the ciphertext binary string from the blocks
		String binCiphertext = "";
		for (int i = 0; i < binCiphertextBlocks.length; i++)
			binCiphertext += binCiphertextBlocks[i];

		// Destroy key schedule
		for (int i=0;i<K.length;i++)
			K[i] = 0;


		return binCiphertext;
	}
	
	/**
	 * Decrypt a string message with the DES block cipher 
	 * @param key : String - the key to decrypt with
	 * @param plaintext : String - Hex string to decrypt
	 * @return Plaintext message string
	 */

	public static byte[] decryptFile(String key, byte[] fileBytes) throws Exception {

		String plaintextHexa = Base64.getEncoder().encodeToString(fileBytes);
		String plaintext = hexToBin(plaintextHexa);

		// Build the key schedule
		buildKeySchedule(hash(key));

		String binPlaintext = null;

		binPlaintext = plaintext;

		// Add padding if necessary
		int remainder = binPlaintext.length() % 64;
		if (remainder != 0) {
			for (int i = 0; i < (64 - remainder); i++)
				binPlaintext = "0" + binPlaintext;
		}

		// Separate binary plaintext into blocks
		String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
		int offset = 0;
		for (int i = 0; i < binPlaintextBlocks.length; i++) {
			binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
			offset += 64;
		}

		String[] binCiphertextBlocks = new String[binPlaintext.length()/64];

		// Encrypt the blocks
		for (int i = 0; i < binCiphertextBlocks.length; i++) {
			try {
				binCiphertextBlocks[i] = decryptBlock(binPlaintextBlocks[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// Build the ciphertext binary string from the blocks
		String binCiphertext = "";
		for (int i = 0; i < binCiphertextBlocks.length; i++)
			binCiphertext += binCiphertextBlocks[i];

		// Destroy key schedule
		for (int i=0;i<K.length;i++)
			K[i] = 0;

		return Base64.getDecoder().decode(binCiphertext);
	}

	public static String decrypt(String key, String plaintext) throws Exception {
		
		// Build the key schedule
		buildKeySchedule(hash(key));
		
		String binPlaintext = null;
		
		binPlaintext = plaintext;
		
		// Add padding if necessary
		int remainder = binPlaintext.length() % 64;
		if (remainder != 0) {
			for (int i = 0; i < (64 - remainder); i++)
				binPlaintext = "0" + binPlaintext;
		}
		
		// Separate binary plaintext into blocks
		String[] binPlaintextBlocks = new String[binPlaintext.length()/64];
		int offset = 0;
		for (int i = 0; i < binPlaintextBlocks.length; i++) {
			binPlaintextBlocks[i] = binPlaintext.substring(offset, offset+64);
			offset += 64;
		}
		
		String[] binCiphertextBlocks = new String[binPlaintext.length()/64];
		
		// Encrypt the blocks
		for (int i = 0; i < binCiphertextBlocks.length; i++) {
			try {
				binCiphertextBlocks[i] = decryptBlock(binPlaintextBlocks[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// Build the ciphertext binary string from the blocks
		String binCiphertext = "";
		for (int i = 0; i < binCiphertextBlocks.length; i++) 
			binCiphertext += binCiphertextBlocks[i];
			
		// Destroy key schedule
		for (int i=0;i<K.length;i++)
			K[i] = 0;
		
		return binCiphertext;
	}

	
	public static String encryptBlock(String plaintextBlock) throws Exception {
		int length = plaintextBlock.length();
		if (length != 64)
			throw new RuntimeException("Input block length is not 64 bits!");
		
		//Initial permutation
		String out = "";
		for (int i = 0; i < IP.length; i++) {
			out = out + plaintextBlock.charAt(IP[i] - 1);	
		}
			
		String mL = out.substring(0, 32);
		String mR = out.substring(32);
	
		for (int i = 0; i < 16; i++) {
			
			// 48-bit current key
			String curKey = Long.toBinaryString(K[i+1]);
			while(curKey.length() < 48)
				curKey = "0" + curKey;
			
			// Get 32-bit result from f with m1 and ki
			String fResult = f(mR, curKey);
			
			// XOR m0 and f
			long f = Long.parseLong(fResult, 2);
			long cmL = Long.parseLong(mL, 2);
			
			long m2 = cmL ^ f;
			String m2String = Long.toBinaryString(m2);
			
			while(m2String.length() < 32)
				m2String = "0" + m2String;
			
			mL = mR;
			mR = m2String;	
		}
		
		String in = mR + mL;
		String output = "";
		for (int i = 0; i < IPi.length; i++) {
			output = output + in.charAt(IPi[i] - 1);
		}
		
		return output;
	}
	
	public static String decryptBlock(String plaintextBlock) throws RuntimeException {
		int length = plaintextBlock.length();
		if (length != 64)
			throw new RuntimeException("Input block length is not 64 bits!");
		
		//Initial permutation
		String out = "";
		for (int i = 0; i < IP.length; i++) {
			out = out + plaintextBlock.charAt(IP[i] - 1);	
		}
			
		String mL = out.substring(0, 32);
		String mR = out.substring(32);
	
		for (int i = 16; i > 0; i--) {
			
			// 48-bit current key
			String curKey = Long.toBinaryString(K[i]);
			while(curKey.length() < 48)
				curKey = "0" + curKey;
			
			// Get 32-bit result from f with m1 and ki
			String fResult = f(mR, curKey);
			
			// XOR m0 and f
			long f = Long.parseLong(fResult, 2);
			long cmL = Long.parseLong(mL, 2);
			
			long m2 = cmL ^ f;
			String m2String = Long.toBinaryString(m2);
			
			while(m2String.length() < 32)
				m2String = "0" + m2String;
			
			mL = mR;
			mR = m2String;	
		}
		
		String in = mR + mL;
		String output = "";
		for (int i = 0; i < IPi.length; i++) {
			output = output + in.charAt(IPi[i] - 1);
		}
		
		return output;
	}

	/**
	 * Hash Function from user <b>sfussenegger</b> on stackoverflow 
	 * 
	 * @param string : String to hash
	 * @return 64-bit long hash value
	 * @source http://stackoverflow.com/questions/1660501/what-is-a-good-64bit-hash-function-in-java-for-textual-strings 
	 */
	
	// adapted from String.hashCode()
	public static long hash(String string) throws Exception {
		long h = 1125899906842597L; // prime
		int len = string.length();

		if(len != 8){
			throw new Exception("String must be 8 characters long!");
		}

		for (int i = 0; i < len; i++) {
			h = 31*h + string.charAt(i);
		}
		return h;
	}
	
	public static void buildKeySchedule(long key) {
		
		// Convert long value to 64bit binary string
		String binKey = Long.toBinaryString(key);
		
		// Add leading zeros if not at key length for ease of computations
		while (binKey.length() < 64) 
			binKey = "0" + binKey;
		
		// For the 56-bit permuted key 
		String binKey_PC1 = "";
		
		// Apply Permuted Choice 1 (64 -> 56 bit)
		for (int i = 0; i < PC1.length; i++)
			binKey_PC1 = binKey_PC1 + binKey.charAt(PC1[i]-1);
		
		String sL, sR;
		int iL, iR;
		
		// Split permuted string in half | 56/2 = 28
		sL = binKey_PC1.substring(0, 28);
		sR = binKey_PC1.substring(28);
		
		// Parse binary strings into integers for shifting
		iL = Integer.parseInt(sL, 2);
		iR = Integer.parseInt(sR, 2);
		
		// Build the keys (Start at index 1)
		for (int i = 1; i < K.length; i++) {
			
			// Perform left shifts according to key shift array
			iL = Integer.rotateLeft(iL, KEY_SHIFTS[i]);
			iR = Integer.rotateLeft(iR, KEY_SHIFTS[i]);
			
			// Merge the two halves
			long merged = ((long)iL << 28) + iR;
			
			// 56-bit merged
			String sMerged = Long.toBinaryString(merged);
			
			// Fix length if leading zeros absent
			while (sMerged.length() < 56)
				sMerged = "0" + sMerged;
			
			// For the 56-bit permuted key 
			String binKey_PC2 = "";
			
			// Apply Permuted Choice 2 (56 -> 48 bit)
			for (int j = 0; j < PC2.length; j++)
				binKey_PC2 = binKey_PC2 + sMerged.charAt(PC2[j]-1);
			
			// Set the 48-bit key
			K[i] = Long.parseLong(binKey_PC2, 2);
		}
	}
	
	
	/**
	 * Feistel function in DES algorithm specified in FIPS Pub 46
	 * @param mi : String - 32-bit message binary string
	 * @param key : String - 48-bit key binary string
	 * @return 32-bit output string
	 */
	public static String f(String mi, String key) {
		
		// Expansion function g (named E in fips pub 46)
		String gMi = "";
		for (int i = 0; i < g.length; i++) {
			gMi = gMi + mi.charAt(g[i] - 1);
		}
		
		long m =  Long.parseLong(gMi, 2);	
		long k = Long.parseLong(key, 2);
		
		// XOR expanded message block and key block (48 bits)
		Long result = m ^ k;
		
		String bin = Long.toBinaryString(result);
		// Making sure the string is 48 bits
		while (bin.length() < 48) {
			bin = "0" + bin;
		}
		
		// Split into eight 6-bit strings
		String[] sin = new String[8];
		for (int i = 0; i < 8; i++) {
			sin[i] = bin.substring(0, 6);
			bin = bin.substring(6);
		}
		
		
		// Do S-Box calculations
		String[] sout = new String[8];
		for (int i = 0 ; i < 8; i++) {
			int[][] curS = s[i];
			String cur = sin[i];
		
			// Get binary values
			int row = Integer.parseInt(cur.charAt(0) + "" + cur.charAt(5), 2); 
			int col = Integer.parseInt(cur.substring(1, 5), 2);
			
			// Do S-Box table lookup
			sout[i] = Integer.toBinaryString(curS[row][col]);
			
			// Make sure the string is 4 bits
			while(sout[i].length() < 4)
				sout[i] = "0" + sout[i];
			
		}
		
		// Merge S-Box outputs into one 32-bit string
		String merged = "";
		for (int i = 0; i < 8; i++) {
			merged = merged + sout[i];
		}
		
		// Apply Permutation P
		String mergedP = "";
		for (int i = 0; i < p.length; i++) {
			mergedP = mergedP + merged.charAt(p[i] - 1);
		}
		
		return mergedP;
	}
	
//	public static void main(String[] args) {
//
//		DESUtils des = new DESUtils();
//
//		boolean enc = false;
//		String key1 = "12345678", key2 = null, key3 = null;
//		String message = "9bcbcadcc8b22a2e313b21e04049f89e6b8fc71fa4c3040aeb9fbbd1ea8c887061edc4040d3c6b28c933c2098377588b3cbd3e5aa2ad17ae0f4923b875d57fc938032cbbbd5e145f55c51647957c0dc00702762d803696df83a121b9ec3666599d9457ec58049babf684c651a6df9e09a3fe5ffd786f443b23d2755df7c6a4313911117c317afa06878e26753ca32dcfe6263869946ddd4c5600b1e9eac21356c274775682d7fef2577ab02e87b8fc744e4e8289ef652be31ce584d50c3d62c73ff0dcfa574a22545d0602eb8edb9b73239fdff6217dfc3ca2bc801f9cd94cff6d8a4e840faac84f08cd9cb67b231843e958b4a3aa8a7cdc09db3f5d7f01dbafc08a3ed7221d55e8e4ade70fa4d51bd30b2600eec7eb3491b82aca6b32c3b426f4e153019a31aa64ea90ccf4edc5e6f5fa9ddc31bfc44fe161edc4040d3c6b28dd9e3aefbd4f5ccab87129b0e53ef36811d73116ee7c0136962044d0f17600860a09a2866e3081a2589e2e2d2956682f8d3ee939c58d207a79f7ef3283bc5dd867c8dc98c6c78003cfc2a569c3dbb6eaa1bb12b74c538f0996fae33a7ef0cee4873d56160a0ae90cb2ec9cd170c0497a9fe5ce8ee560f32e31f3fd6dd8aff9b625d6ec2040d6a8f5c8655073287569209029abcb8bcdc4391cadabbd7195a1d451d2f780fda87314c10f1c2b236f4bb02455e6b9c6a039b6cfa4a14ce7c21f84528de678b904ab841404d51ad52bf46566c55b1a7fce55aafabd30ea3f609fa0f5ec7e083dc2ebe45aeac0a6bce8f2012bc485bcfa64eb60b4471f82db0351464e52d96415bc1695ce8cc4c67f0ea4550355da79914949ecd70942149c35dddefb5de736eb58aff5ab07ca8a0fa832a5dcde748eef0d650529fe1941ad900f7ed9b568a7e90d0d66529678a52fee2497ae778109259ea4272208a33735b44fff74614022d584dd2575af39ee1f766a6e8b825037260132c24477d82cd37dad9ad79846d8fc657da2ebb0d85093e3f6450f9044d4e50c9f247c7cd048bc8951b03f8496cbe5afe95c5cff627d2f73b638b07bf1567d369db594e9103a0b826f588ae9f55177480c9eaa7115b6002641a98c7f6c9bdc81340465dbe6a83326c81b1209d9a8c86a89fe8c2f5a567ba95a4bff96a1c552c5bba389f42302eac47b8c84678f9fafe53d4df8437a8e2bfc2ef53a00ad760690e491f8731e5f7930b1c369b95d6220e3001387484ce80968d8c15fc40a7f3af2186a2ba7800d77838190dac32b9cc975143bcd5b5b6479f0f0c77b84624649aa2f1a9c38bfa1d936bd365ffcd3a381d8e9734fecc8c03d4f3d579cac9d777676721ad630f9bb15bec345efa59c79b003503daee6eb7c992bfea060985b22ed9bee57d15aa22a57cd11ec1e2eca522b4684971c86ec72f6467eb7894bed4f8cc59edcf6fb27dc372015dc22156374b1afb3a16f6d294c5aa71355e8b681bff4378ae491e0a86c96c0590562b16891ef70580d";
////		String message = "Contrary to popular belief, Lorem Ipsum is not simply random text. It has roots in a piece of classical Latin literature from 45 BC, making it over 2000 years old. Richard McClintock, a Latin professor at Hampden-Sydney College in Virginia, looked up one of the more obscure Latin words, consectetur, from a Lorem Ipsum passage, and going through the cites of the word in classical literature, discovered the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of \"de Finibus Bonorum et Malorum\" (The Extremes of Good and Evil) by Cicero, written in 45 BC. This book is a treatise on the theory of ethics, very popular during the Renaissance. The first line of Lorem Ipsum, \"Lorem ipsum dolor sit amet..\", comes from a line in section 1.10.32.\n" +
////				"\n" +
////				"The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from \"de Finibus Bonorum et Malorum\" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.";
//		String result = null;
//
//		if (enc) {
//			if (message == null) {
//				System.out.println("No message given to encrypt. Exiting..");
//				System.exit(0);
//			} else if (key1 == null) {
//				System.out.println("Improper use of key arguments. Exiting..");
//				System.exit(0);
//			}
//
//			if (key2 == null) {
//				if (key3 != null) {
//					System.out.println("Improper use of key arguments. Exiting..");
//					System.exit(0);
//				}
//				result = des.encrypt(key1, utfToBin(message));
//				System.out.println(binToHex(result));
//			} else {
//				if (key3 == null) {
//					System.out.println("Improper use of key arguments. Exiting..");
//					System.exit(0);
//				}
//				result = des.encrypt(key3, des.decrypt(key2, des.encrypt(key1, utfToBin(message))));
//				System.out.println(binToHex(result));
//			}
//		} else {
//			if (message == null) {
//				System.out.println("No data given to decrypt. Exiting..");
//				System.exit(0);
//			} else if (key1 == null) {
//				System.out.println("Improper use of key arguments. Exiting..");
//				System.exit(0);
//			}
//
//			if (key2 == null) {
//				if (key3 != null) {
//					System.out.println("Improper use of key arguments. Exiting..");
//					System.exit(0);
//				}
//				result = des.decrypt(key1, hexToBin(message));
//				System.out.println(binToUTF(result));
//			} else {
//				if (key3 == null) {
//					System.out.println("Improper use of key arguments. Exiting..");
//					System.exit(0);
//				}
//				result = des.decrypt(key1, encrypt(key2, des.decrypt(key3, hexToBin(message))));
//				System.out.println(binToUTF(result));
//			}
//		}
//
//	}

}