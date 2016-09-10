

import java.util.BitSet;
import java.util.List;

public class Stage2 {

    public BitSet round(BitSet key, BitSet mi) {

        BitSet li = new BitSet(32);
        BitSet ri = new BitSet(32);

        li = mi.get(32, 64);
        ri = mi.get(0, 32);

        innerF(ri, key);

        return mi;
    }

    public BitSet innerF(BitSet ri, BitSet key){

        //Do expansion
        ri.xor(key);

        //S-boxes
        //Permutation

        return ri;
    }

    public void rounds (BitSet m0, List<BitSet> keys) {

        for (int i = 0; i < 16; i++) {
            m0 = round(m0, keys.get(i));
        }
    }

    public static void main (String[] args)
    {

    }
}
