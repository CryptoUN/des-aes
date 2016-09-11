package aes;

import java.util.Arrays;
import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * Created by oscar on 11/09/16.
 */
public class AuxTransformations {

    /**
     * XORs the message and the key. WARNING: modifies the original message.
     * @param message
     * @param key
     * @return XORed message with key.
     */
    static BitSet[] addRoundKey(BitSet[] message, BitSet[] key) {
        for (int i = 0; i < message.length; i++) {
            message[i].xor(key[i]);
        }
        return message;
    }

    static BitSet[] convertToBitSet(long[] array) {
        BitSet[] bitSets = new BitSet[array.length];
        for (int i = 0; i < array.length; i++) {
            bitSets[i] = BitSet.valueOf(new long[]{array[i]});
        }
        return bitSets;
    }

    static long[] mergeIntoLong(int[][] matrix, int n) {
        long[] merged = new long[n];
        for (int i = 0; i < n; i++) {
            long section0 = matrix[i][3];
            long section1 = matrix[i][2];
            long section2 = matrix[i][1];
            long section3 = matrix[i][0];
            merged[i] = section3 << 24 | section2 << 16 | section1 << 8 | section0;
        }
        return merged;
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

    static String convertBitSetToString(BitSet bitString, int n) {
        final StringBuilder buffer = new StringBuilder(n);
        IntStream.range(0, n)
                .map(i -> n - i - 1)
                .mapToObj(i -> bitString.get(i) ? '1' : '0')
                .forEach(buffer::append);
        return buffer.toString();
    }

    public static void main(String[] args) {
        int[] originalArray = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        int[][] matrix = convertToMatrix(originalArray, 4, 4);
        System.out.println(Arrays.deepToString(matrix));

        int[][] transpose = transpose(matrix);
        System.out.println(Arrays.deepToString(transpose));

        long[] merged = mergeIntoLong(transpose, transpose.length);
        System.out.println(Arrays.toString(merged));
        System.out.println(convertBitSetToString(BitSet.valueOf(new long[]{transpose[0][0]}), 8) + "-"
                + convertBitSetToString(BitSet.valueOf(new long[]{transpose[0][1]}), 8) + "-"
                + convertBitSetToString(BitSet.valueOf(new long[]{transpose[0][2]}), 8) + "-"
                + convertBitSetToString(BitSet.valueOf(new long[]{transpose[0][3]}), 8));
        System.out.println(convertBitSetToString(BitSet.valueOf(new long[]{merged[0]}), 32));

        System.out.println("\nTesting addRoundKey\n");
        BitSet[] message = {
            BitSet.valueOf(new long[]{123}),
            BitSet.valueOf(new long[]{126}),
            BitSet.valueOf(new long[]{923}),
            BitSet.valueOf(new long[]{15423})
        };

        BitSet[] key = {
            BitSet.valueOf(new long[]{14875623}),
            BitSet.valueOf(new long[]{-126}),
            BitSet.valueOf(new long[]{94523}),
            BitSet.valueOf(new long[]{1547723})
        };

        BitSet[] result = addRoundKey(message, key);

        System.out.println();

        for (BitSet bitSet : result) {
            System.out.println(convertBitSetToString(bitSet, 32));
        }
    }

}
