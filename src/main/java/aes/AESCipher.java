package aes;

import java.util.BitSet;

/**
 * Created by oscar on 11/09/16.
 */
public class AESCipher {

    /**
     *
     * @param message - An array containing the ASCII codes of the message. Its length must be 128, 192 or 256.
     * @return bitSetsMessage - The bit sets with the correct order for using AES.
     */
    public BitSet[] prepareMessage(int[] message) {
        int[][] asciiMatrix;
        int n, m = 4;
        if (message.length == 256 / 8) {
            n = 8;
        } else if (message.length == 192 / 8) {
            n = 6;
        } else if (message.length == 128 / 8) {
            n = 4;
        } else {
            throw new RuntimeException("The message block length must be either 128, 192 or 256.");
        }
        asciiMatrix = AuxTransformations.convertToMatrix(message, n, m);
        int[][] transposedMessage = AuxTransformations.transpose(asciiMatrix);
        long[] mergedMessage = AuxTransformations.mergeIntoLong(transposedMessage, n);
        return AuxTransformations.convertToBitSet(mergedMessage);
    }

    public BitSet[] doStepOne(BitSet[] input) {
        BitSet[] output = new BitSet[input.length];
        return output;
    }

    public static void main(String[] args) {
        String message = "aes es muy facil";
        int[] asciiArray = message.chars().toArray();

        AESCipher aesCipher = new AESCipher();
        BitSet[] preparedMessage = aesCipher.prepareMessage(asciiArray);
        for (BitSet bitSet : preparedMessage) {
            System.out.println(AuxTransformations.convertBitSetToString(bitSet, 32));
        }
    }

}
