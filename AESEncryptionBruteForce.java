
import javax.crypto.Cipher;

import javax.crypto.spec.IvParameterSpec;

import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

import java.util.*;

import java.io.*;



public class AESEncryptionBruteForce {



    public static void main(String[] args) {

        String iV_hex = "aabbccddeeff00998877665544332211";

        String ciphertextHex = "764aa26b55a4da654df6b19e4bce00f4ed05e09346fb0e762583cb7da2ac93a2";

        String plaintext = "This is a top secret.";



        byte[] iv = Hex_String_To_Byte_Array(iV_hex);

        byte[] ciphertext = Hex_String_To_Byte_Array(ciphertextHex);



        try {

            List<String> words = Load_Dictionary("C:\\Users\\Lenovo\\Desktop/words.txt");

            String key = Key(words, plaintext, ciphertext, iv);

            if (key != null) {

                System.out.println("Encryption key found: " + key);

            } else {

                System.out.println("Encryption key not found.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }

    }



    private static List<String> Load_Dictionary(String filename) throws IOException {

        List<String> Words = new ArrayList<>();

        try (Scanner scanner = new Scanner(new File(filename))) {

            while (scanner.hasNextLine()) {

                Words.add(scanner.nextLine().trim());

            }

        }

        return Words;

    }



    private static String Key(List<String> words, String plaintext, byte[] ciphertext, byte[] iv) {

        Cipher cipher;

        try {

            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        } catch (Exception e) {

            e.printStackTrace();

            return null;

        }

        IvParameterSpec iV_Spec = new IvParameterSpec(iv);



        for (String word : words) {

            if (word.length() <= 16) {

                String paddedWord = Pad_to_length(word, 16);

                SecretKeySpec Key_Spec = new SecretKeySpec(paddedWord.getBytes(StandardCharsets.UTF_8), "AES");



                try {

                    cipher.init(Cipher.DECRYPT_MODE, Key_Spec, iV_Spec);

                    byte[] decrypted = cipher.doFinal(ciphertext);



                    if (new String(decrypted, StandardCharsets.UTF_8).startsWith(plaintext)) {

                        return word;

                    }

                } catch (Exception e) {

                    // If an exception is caught, it likely means the key was incorrect.

                    // Continue with the next word.

                }

            }

        }

        return null; // Return null if no key is found

    }



    private static String Pad_to_length(String word, int length) {

        StringBuilder sb = new StringBuilder(word);

        while (sb.length() < length) {

            sb.append("#");

        }

        return sb.toString();

    }



    public static byte[] Hex_String_To_Byte_Array(String s) {

        int len = s.length();

        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {

            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)

                                 + Character.digit(s.charAt(i + 1), 16));

        }

        return data;

    }

}