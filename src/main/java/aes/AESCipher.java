package aes;

import java.util.Arrays;


public class AESCipher {

    /**
     *
     * @param message - An array containing the numerical codes of the message. Its length must be 128.
     * @return bitSetsMessage - The bit sets with the correct order for using AES.
     */
    public int[][] prepareMessage(int[] message) {
        int[][] matrix;
        int n = 4;
        if (!(message.length == 128 / 8)) {
            throw new RuntimeException("The message block length must be 128.");
        }

        matrix = AuxTransformations.convertToMatrix(message, n, n);
        matrix = AuxTransformations.transpose(matrix);
        return matrix;
    }

    public static void main(String[] args) {
        String message = "Aes Æs mÜÿ fáÇîl";
        int[] textArray = message.chars().toArray();

        AESCipher aesCipher = new AESCipher();
        int[][] preparedMessage = aesCipher.prepareMessage(textArray);

        System.out.println(Arrays.deepToString(preparedMessage));

    }

}
