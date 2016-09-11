package des;

import java.util.BitSet;
import java.util.stream.IntStream;

/**
 * Created by oscar on 2/09/16.
 */
public class KeyGenerator {

    // Subtract 1 to get the correct position
    private static final int[] PC1 = {
        57, 49, 41, 33, 25, 17,  9,
         1, 58, 50, 42, 34, 26, 18,
        10,  2, 59, 51, 43, 35, 27,
        19, 11,  3, 60, 52, 44, 36,
        63, 55, 47, 39, 31, 23, 15,
         7, 62, 54, 46, 38, 30, 22,
        14,  6, 61, 53, 45, 37, 29,
        21, 13,  5, 28, 20, 12,  4
    };

    // Subtract 1 to get the correct position
    private static final int[] PC2 = {
        14, 17, 11, 24,  1,  5,
         3, 28, 15,  6, 21, 10,
        23, 19, 12,  4, 26,  8,
        16,  7, 27, 20, 13,  2,
        41, 52, 31, 37, 47, 55,
        30, 40, 51, 45, 33, 48,
        44, 49, 39, 56, 34, 53,
        46, 42, 50, 36, 29, 32
    };

    public static BitSet[] generateKeys(BitSet initialKey) {
        BitSet[] subKeys = new BitSet[16];
        BitSet pc1Output = getPC1(initialKey);
        BitSet C_i = pc1Output.get(0, 28);
        BitSet D_i = pc1Output.get(28, 56);
        for (int i = 0; i < 16; ++i) {
            C_i = leftShift(C_i, i, 28);
            D_i = leftShift(D_i, i, 28);
            subKeys[i] = getPC2(concatenateBitStrings(C_i, D_i, 56));
        }
        return subKeys;
    }

    private static BitSet concatenateBitStrings(BitSet leftBitString, BitSet rightBitString, int n) {
        long left = (leftBitString.length() != 0) ? leftBitString.toLongArray()[0] : 0;
        long right = (rightBitString.length() != 0) ? rightBitString.toLongArray()[0] : 0;
        long concatenated = (left << n/2) | right;
        return BitSet.valueOf(new long[]{concatenated});
    }

    private static BitSet getPC1(BitSet key) {
        BitSet pc1 = new BitSet(56);
        for (int i = 0; i < PC1.length; ++i)
            pc1.set(55 - i, key.get(PC1[i] - 1));
        return pc1;
    }

    private static BitSet getPC2(BitSet subKey) {
        BitSet pc2 = new BitSet(48);
        for (int i = 0; i < PC2.length; ++i)
            pc2.set(47 - i, subKey.get(PC2[i] - 1));
        return pc2;
    }

    private static BitSet leftShift(BitSet bitString, int index, int n) {
        if (bitString.length() == 0)
            return bitString;
        BitSet shiftedBitString;
        long key = bitString.toLongArray()[0];
        long shiftedKey;
        switch (index) {
            case 1:
            case 2:
            case 9:
            case 16:
                shiftedKey = (key << n - 1) | (key >>> 1);
                shiftedBitString = BitSet.valueOf(new long[]{shiftedKey});
                break;
            default:
                shiftedKey = (key << n - 2) | (key >>> 2);
                shiftedBitString = BitSet.valueOf(new long[]{shiftedKey});
        }
        return shiftedBitString;
    }

    private static String convertBitSetToString(BitSet bitString, int n) {
        final StringBuilder buffer = new StringBuilder(n);
        IntStream.range(0, n)
                .map(i -> n - i - 1)
                .mapToObj(i -> bitString.get(i) ? '1' : '0')
                .forEach(buffer::append);
        return buffer.toString();
    }

    public static void main(String[] args) {
        long keyNumber = -1L;
        BitSet bitKey = BitSet.valueOf(new long[]{keyNumber});
        System.out.println(convertBitSetToString(leftShift(bitKey, 2, 64), 64));
        System.out.println(convertBitSetToString(leftShift(bitKey, 3, 64), 64));

        BitSet pc1 = getPC1(bitKey);
//        BitSet pc1 = getPC1(leftShift(bitKey, 2));
        System.out.println(convertBitSetToString(pc1, 56));
        System.out.println(convertBitSetToString(getPC2(pc1), 48));

        System.out.println("\nGenerated keys\n");
        BitSet[] keys = generateKeys(bitKey);
        for (BitSet key : keys)
            System.out.println(convertBitSetToString(key, 48));
    }

}
