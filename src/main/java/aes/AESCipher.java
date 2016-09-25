package aes;

import java.util.Arrays;
import java.util.List;

import static aes.AuxTransformations.*;


public class AESCipher {

    /**
     * Specifies the key schedule
     */
    public enum Type {
        AES128, AES192, AES256
    }

    /**
     *
     * @param message - An array containing the numerical codes of the message. Its length must be 128.
     * @return bitSetsMessage - The bit sets with the correct order for using AES.
     */
    public static int[][] prepareMessage(int[] message) {
        int[][] matrix;
        int n = 4;
        if (!(message.length == 128 / 8)) {
            throw new RuntimeException("The message block length must be 128.");
        }

        matrix = convertToMatrix(message, n, n);
        matrix = transpose(matrix);
        return matrix;
    }

    public static int[] encrypt(int[] m, int[] key, Type type) {

        int[][] message = prepareMessage(m);
        List<int[][]> roundKeys = KeyGenerator.generate(key, type);
        int[][] state = addRoundKey(message, roundKeys.get(0));

        for (int i = 1; i < roundKeys.size(); i++) {
            subBytes(state, false);
            shiftRows(state, false);

            if (i + 1 != roundKeys.size())
                state = mixColumns(state, false);

            addRoundKey(state, roundKeys.get(i));
        }

        return matrixToArray(transpose(state));
    }


    public static int[] decrypt(int[] c, int[] key, Type type) {

        int[][] ciphertext = prepareMessage(c);
        List<int[][]> roundKeys = KeyGenerator.generate(key, type);
        int[][] state = addRoundKey(ciphertext, roundKeys.get(roundKeys.size() - 1));

        shiftRows(state, true);
        subBytes(state, true);

        for (int i = roundKeys.size() - 2; i >= 0; i--) {

            if (i == 0) {
                addRoundKey(state, roundKeys.get(i));
                continue;
            }

            addRoundKey(state, roundKeys.get(i));
            state = mixColumns(state, true);
            shiftRows(state, true);
            subBytes(state, true);
        }

        return matrixToArray(transpose(state));
    }

    public static void main(String[] args) {
        String m = "Aes Æs mÜÿ fáÇîl";
        //int[] message = {0x41, 0x45, 0x53, 0x20, 0x65, 0x73, 0x20, 0x6d, 0x75, 0x79, 0x20, 0x66, 0x61, 0x63, 0x69, 0x6c};
        int[] key = {0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6, 0xab, 0xf7, 0x15, 0x88, 0x09, 0xcf, 0x4f, 0x3c};
        int[] message = m.chars().toArray();

        int[] ciphertext = encrypt(message, key, Type.AES128);
        int[] plaintext = decrypt(ciphertext, key, Type.AES128);

        System.out.println("message " + Arrays.deepToString(convertToMatrix(message, 4, 4)));
        System.out.println("cipher " + Arrays.toString(ciphertext));
        System.out.println("plaint " + Arrays.toString(plaintext));
    }
}
