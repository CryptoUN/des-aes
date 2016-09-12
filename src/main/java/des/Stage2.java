
package des;

import java.util.BitSet;
import java.util.List;

public class Stage2 {

    public BitSet round(BitSet key, BitSet mi) {

        BitSet li = new BitSet(32);
        BitSet ri = new BitSet(32);

        li = mi.get(32, 64);
        ri = mi.get(0, 32);

        feistelFunction(ri, key);

        return mi;
    }

    public BitSet feistelFunction(BitSet ri, BitSet key){

        //Expansion
        int [] E = {32, 1, 2, 3, 4, 5,
                    4, 5, 6, 7, 8, 9,
                    8, 9, 10, 11, 12, 13,
                    12, 13, 14, 15, 16, 17,
                    16, 17, 18, 19, 20, 21,
                    20, 21, 22, 23, 24, 25,
                    24, 25, 26, 27, 28, 29,
                    28, 29, 30, 31, 32, 1};

        BitSet expandedRi = new BitSet(48);

        for(int i = 0; i < E.length; i++){
            expandedRi.set(i, ri.get(E[i]));
        }

        expandedRi.xor(key);

        BitSet subRi = new BitSet(32);
        //S-boxes

        BitSet permRi = new BitSet(32);
        //Permutation
        int [] P = {16, 7, 20, 21, 29, 12, 28, 17,
                    1, 15, 23, 26, 5, 18, 31, 10,
                    2, 8, 24, 14, 32, 27, 3, 9,
                    19, 13, 30, 6, 22, 11, 4, 25};

        for (int i = 0; i < P.length; i++) {
            permRi.set(i, subRi.get(P[i]));
        }

        return permRi;
    }

    public void rounds (BitSet m0, BitSet [] keys) {

        for (int i = 0; i < keys.length; i++) {
            m0 = round(m0, keys[i]);
        }
    }

    public static void main (String[] args)
    {
        KeyGenerator keyGenerator = new KeyGenerator();
        BitSet initialKey = BitSet.valueOf("0123456789abcdef".getBytes());
        BitSet partialMessage = BitSet.valueOf("thisIsJustATest1".getBytes());
        BitSet [] subkeys = keyGenerator.generateKeys(initialKey);

        Stage2 stage2 = new Stage2();

        stage2.rounds(partialMessage, subkeys);
    }
}
