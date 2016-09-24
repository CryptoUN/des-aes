package aes;

import java.util.Arrays;


public class AuxTransformations {

    /**
     * XORs the message and the key. WARNING: modifies the original message.
     * @param message
     * @param key
     * @return XORed message with key.
     */
    static int[][] addRoundKey(int[][] message, int[][] key) {
        for (int i = 0; i < message.length; i++) {
            for (int j = 0; j < message.length; j++)
                message[i][j] ^= key[i][j];
        }
        return message;
    }

    static int[][] transpose(int[][] matrix) {
        int[][] transpose = new int[matrix[0].length][matrix.length];
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                transpose[i][j] = matrix[j][i];
            }
        }
        return transpose;
    }

    static int[][] convertToMatrix(int[] array, int n, int m) {
        int[][] matrix = new int[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix[i][j] = array[i * m + j];
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        int[] originalArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[][] matrix = convertToMatrix(originalArray, 4, 4);
        System.out.println(Arrays.deepToString(matrix));

        int[][] transpose = transpose(matrix);
        System.out.println(Arrays.deepToString(transpose));

        System.out.println("\nTesting addRoundKey\n");
        int[] message = {0x41, 0x45, 0x53, 0x20, 0x65, 0x73, 0x20, 0x6d, 0x75, 0x79, 0x20, 0x66, 0x61, 0x63, 0x69, 0x6c};
        int[] key = {0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6, 0xab, 0xf7, 0x15, 0x88, 0x09, 0xcf, 0x4f, 0x3c};
        AESCipher aesCipher = new AESCipher();

        int[][] result = addRoundKey(aesCipher.prepareMessage(message), convertToMatrix(key, 4, 4));

        System.out.println(Arrays.deepToString(result));
    }

}
