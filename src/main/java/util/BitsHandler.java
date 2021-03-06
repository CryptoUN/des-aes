package util;

import java.math.BigInteger;
import java.util.BitSet;

import static java.nio.charset.StandardCharsets.*;

public class BitsHandler {
	
	public BitSet getBits(String text){
		System.out.println(text);
	    byte[] bytes = text.getBytes(ISO_8859_1);
	    for(int i = 0 ; i< bytes.length; i++)
	    	bytes[i] = (byte) (bytes[i] & 0xff);
	    
	    BitSet bitset = BitSet.valueOf(bytes);
	    
	    return bitset;
	}
	
	public String getBinaryText(BitSet bits){
		String binary = "";
		for(int i = 0; i < bits.length(); i++)
			binary += bits.get(bits.length() - i - 1) ? "1" : "0";
	    return binary;
	}
	
	public String getText(BitSet bits){
	    String binary = getBinaryText(bits);
		return new String(new BigInteger(binary, 2).toByteArray());
	}
	
	public String getBinaryFromText(String text){
	    return getBinaryText( getBits(text) );
	}
	
	public String getTextFromBinary(String binary){
		String textReverse = new String(new BigInteger(binary, 2).toByteArray(), ISO_8859_1);
		String text = "";
		for(int i = textReverse.length() - 1; i >= 0; i--)
			text += textReverse.charAt(i);
		
		return text.replace("\u0000", "");
	}
	
	public BitSet bsFromString(String s) {
		BitSet bs = new BitSet(s.length());
		for(int i = 0; i < s.length(); i++)
			bs.set(i, s.charAt(s.length() - 1 - i) == '1');
        return bs;
    }
    
    public BitSet getFilling(int n){
    	String filling = "";
    	for(int i = 0; i < n; i++)
    		filling += "0";
    	return bsFromString(filling);
    }
    
    public BitSet concatenateBitStrings(BitSet leftBitString, BitSet rightBitString, int n) {
    	int dim = leftBitString.length() + n;
        BitSet bs = new BitSet(dim);
        for(int i = 0; i < dim; i++){
        	if(i < n){
        		try{
        			bs.set(i, rightBitString.get(i));
        		}catch(IndexOutOfBoundsException e){
        			bs.set(i, false);
        		}
        	}
        	else
        		bs.set(i, leftBitString.get(i - n));
        }
        return bs;
    }
    
    public String convertBSToString(BitSet bits, int numberOfBits){
		String binary = "";
		for(int i = 0; i < numberOfBits; i++){
			try{
				binary += bits.get(numberOfBits - i - 1) ? "1" : "0";
			}catch(IndexOutOfBoundsException e){
				binary += "0";
			}
		}
	    return binary;
	}

	public int[] convertBitStringToIntArray(String bitString) {
        int blockSize = 8;
        int[] intArray = new int[bitString.length()/blockSize];
        String subsection;
        int start, end;
        for (int i = 0; i < intArray.length; ++i) {
            start = i * blockSize;
            end = start + blockSize;
            subsection = bitString.substring(start, end);
            intArray[i] = Integer.parseInt(subsection, 2);
        }
        return intArray;
    }

    public String convertIntArrayToBitString(int[] intArray) {
        int blockSize = 8;
        String element;
        StringBuilder bitString = new StringBuilder();
        for (int i = 0; i < intArray.length; ++i) {
            element = Integer.toBinaryString(intArray[i]);
            element = padZerosToLeft(element, blockSize);
            bitString.append(element.substring(element.length() - blockSize, element.length()));
        }
        return  bitString.toString();
    }

    private static String padZerosToLeft(String string, int desiredSize) {
        if (string.length() == desiredSize)
            return string;
        StringBuilder paddedString = new StringBuilder(string).reverse();
        for (int i = 0; i < desiredSize - string.length(); i++) {
            paddedString.append('0');
        }
        return paddedString.reverse().toString();
    }
	
}
