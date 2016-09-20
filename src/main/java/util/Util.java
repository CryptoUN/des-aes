package util;

import java.util.BitSet;
import java.util.stream.IntStream;

public class Util {

    public static BitSet concatenateBitStrings(BitSet left, BitSet right, int n) {
        BitSet total = new BitSet(n);

        for(int i = 0; i < n/2 ; i++) {
            total.set(i, right.get(i));
            total.set(i + n/2, left.get(i));
        }

        return total;
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
