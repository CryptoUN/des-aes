package aes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KeyGenerator {

    //Round constant
    private static final int[] rcon = {
            0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a,
            0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
    };

    public static int[] subWord(int[] word) {

        int row, col;
        for (int i = 0; i < word.length; i++) {
            row = word[i] >> 4;
            col = word[i] & 15;
            word[i] = AuxTransformations.getSBox()[row][col];
        }

        return word;
    }

    public static List<int[][]> generate(int[][] initialKey, AESCipher.Type type) {

        int n = 4, rounds = 10;

        switch (type) {
            case AES128:
                break;
            case AES192:
                n = 6;
                rounds = 12;
                break;
            case AES256:
                n = 8;
                rounds = 14;
        }

        ArrayList<int[][]> roundKeys = new ArrayList<>();

        for (int i = 0; i <= rounds; i++) {

            roundKeys.add(new int[4][4]);
            int[][] roundKey = roundKeys.get(i);

            if (i == 0) {
                for (int j = 0; j < 4; j++) {
                    for (int k = 0; k < 4; k++) {
                        roundKey[j][k] = initialKey[j][k];
                    }
                }
                continue;
            }

            int[][] prevKey = roundKeys.get(i - 1);

            roundKey[0] = subWord(AuxTransformations.rotateWord(prevKey[n - 1].clone(), 1));
            roundKey[0][0] ^= rcon[i];

            for (int j = 0; j < 4; j++) {
                for (int k = 0; k < 4; k++) {
                    roundKey[j][k] ^= prevKey[j][k];

                    if (j != 0) {
                        roundKey[j][k] ^= roundKey[j - 1][k];
                    }
                }
            }
        }

        for (int i = 0; i < roundKeys.size(); i++) {
            roundKeys.set(i, AuxTransformations.transpose(roundKeys.get(i)));
        }

        return roundKeys;
    }

    public static void main(String[] args) {

        int[] message = {0x41, 0x45, 0x53, 0x20, 0x65, 0x73, 0x20, 0x6d, 0x75, 0x79, 0x20, 0x66, 0x61, 0x63, 0x69, 0x6c};
        int[] key = {0x2b, 0x7e, 0x15, 0x16, 0x28, 0xae, 0xd2, 0xa6, 0xab, 0xf7, 0x15, 0x88, 0x09, 0xcf, 0x4f, 0x3c};

        AESCipher aesCipher = new AESCipher();

        int[][] keyMatrix = AuxTransformations.convertToMatrix(key, 4, 4);
        int[][] messageMatrix = aesCipher.prepareMessage(message);

        System.out.println("Message");
        System.out.println(Arrays.deepToString(messageMatrix));
        System.out.println("key");
        System.out.println(Arrays.deepToString(keyMatrix));
        System.out.println("rounds keys");
        List<int[][]> roundKeys = generate(keyMatrix, AESCipher.Type.AES128);

        for (int[][] r : roundKeys) {
            System.out.println(Arrays.deepToString(r));
        }
    }

}
