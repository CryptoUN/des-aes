package des;

import util.Util;

import java.util.*;

public class DES {

    private static final int[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    public static BitSet initialPermutation(BitSet message) {

        BitSet permMessage = new BitSet(64);

        for (int i = 0; i < IP.length; i++) {
            permMessage.set(63 - i, message.get(64 - IP[i]));
        }

        return permMessage;
    }

    public static BitSet finalPermutation(BitSet mi) {

        BitSet ciphertext = new BitSet(64);
        for (int i = 0; i < IP.length; i++) {
            ciphertext.set(64 - IP[i], mi.get(63 - i));
        }

        return ciphertext;
    }

    public static BitSet encrypt(BitSet message, BitSet key) {

        message = initialPermutation(message);
        BitSet [] subkeys = KeyGenerator.generateKeys(key);
        BitSet ciphertext = finalPermutation(Stage2.rounds(message, subkeys));

        return ciphertext;
    }

    public static BitSet decrypt(BitSet ciphertext, BitSet key) {
        ciphertext = initialPermutation(ciphertext);
        BitSet [] subkeys = KeyGenerator.generateKeys(key);
        List keys = Arrays.asList(subkeys);
        Collections.reverse(keys);
        subkeys = (BitSet [])keys.toArray();
        BitSet stage2 = Stage2.rounds(ciphertext, subkeys);
        BitSet plaintext = finalPermutation(stage2);

        return plaintext;
    }

    public static void main(String[] args) {

        BitSet initialKey = BitSet.valueOf("12'a´wdá".getBytes());
        BitSet message = BitSet.valueOf("éxïto".getBytes());

        System.out.println("key " + Util.convertBitSetToString(initialKey, 64));
        System.out.println("message "+Util.convertBitSetToString(message, 64));

        //Encrypts
        BitSet ciphertext = DES.encrypt(message, initialKey);
        String textCiphertext = Base64.getEncoder().encodeToString(ciphertext.toByteArray());

        System.out.println("ciphertext  "+Util.convertBitSetToString(ciphertext, 64));
        System.out.println("text cipher " + textCiphertext);

        //Decrypts using text version of ciphertext
        BitSet bitStringText_ciphertext = BitSet.valueOf(Base64.getDecoder().decode(textCiphertext));
        BitSet plaintext = DES.decrypt(bitStringText_ciphertext, initialKey);
        System.out.println("decrypted plaintext "+Util.convertBitSetToString(plaintext, 64));
        System.out.println("text plaintext " + new String(plaintext.toByteArray()));

        /*
        //Decrypts directly with BitSet
        plaintext = DES.decrypt(ciphertext, initialKey);
        System.out.println("decrypted plaintext "+Util.convertBitSetToString(plaintext, 64));
        System.out.println("text plaintext " + new String(plaintext.toByteArray()));
        */
    }
}
