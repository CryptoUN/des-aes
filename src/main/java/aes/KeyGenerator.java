package aes;

import java.util.BitSet;

public class KeyGenerator {


    public static BitSet[][] rotWord(BitSet[][] roundKey) {
/*
        BitSet temp = roundKey[0];
        for (int i = 0; i < roundKey.length - 1; i++) {
            roundKey[i] = roundKey[i + 1];
        }

        roundKey[roundKey.length - 1] = temp;
*/
        return roundKey;
    }





}
