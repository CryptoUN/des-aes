package des;

import java.io.UnsupportedEncodingException;
import java.util.BitSet;

public class DES {

    private static int [] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7};

    public static BitSet initialPermutation(BitSet message) {

        BitSet permMessage = new BitSet(message.length());

        for (int i = 0; i < IP.length; i++) {
            permMessage.set(i, message.get(IP[i] - 1));
        }

        return permMessage;
    }

    public static BitSet finalPermutation(BitSet mi) {

        BitSet ciphertext = new BitSet(mi.length());

        for (int i = 0; i < IP.length; i++) {
            ciphertext.set(IP[i] - 1, mi.get(i));
        }

        return ciphertext;
    }

    public static BitSet encrypt(BitSet message, BitSet key) {

        message = initialPermutation(message);
        BitSet [] subkeys = KeyGenerator.generateKeys(key);
        BitSet ciphertext = finalPermutation(Stage2.rounds(message, subkeys));

        return ciphertext;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        BitSet initialKey = BitSet.valueOf("0123456789abcdef".getBytes("UTF-8"));
        BitSet message = BitSet.valueOf("thisIsJustATest1".getBytes("UTF-8"));

        BitSet ciphertext = DES.encrypt(message, initialKey);
    }
}
