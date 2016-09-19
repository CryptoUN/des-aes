package util;

import java.util.BitSet;
import java.util.stream.IntStream;

public class Util {

    public static BitSet concatenateBitStrings(BitSet leftBitString, BitSet rightBitString, int n) {
        long left = (leftBitString.length() != 0) ? leftBitString.toLongArray()[0] : 0;
        long right = (rightBitString.length() != 0) ? rightBitString.toLongArray()[0] : 0;
        long concatenated = (left << n/2) | right;
        return BitSet.valueOf(new long[]{concatenated});
    }

    public static String convertBitSetToString(BitSet bitString, int n) {
        final StringBuilder buffer = new StringBuilder(n);
        IntStream.range(0, n)
                .map(i -> n - i - 1)
                .mapToObj(i -> bitString.get(i) ? '1' : '0')
                .forEach(buffer::append);
        return buffer.toString();
    }

    public static int bitSetToInt(BitSet bitSet) {
        int num = 0, i = 0;

        while (true) {
            i = bitSet.nextSetBit(i);
            if(i < 0) break;
            num |= (1 << i);
            i++;
        }

        num &= Integer.MAX_VALUE;
        return num;
    }

}
