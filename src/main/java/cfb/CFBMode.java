package cfb;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;

import util.BitsHandler;

public class CFBMode {
	
	public static final String I_ARRAY = "IArray";
	public static final String O_ARRAY = "OArray";
	public static final String T_ARRAY = "tArray";
	public static final String M_ARRAY = "mArray";
	public static final String C_ARRAY = "cArray";
	
	public static final String CIPHER_TEXT = "cipherText";
	public static final String PLAIN_TEXT = "plainText";
	public static final String IV = "iv";
	public static final String M_SIZE = "mSize";
	
	public static final int N_SIZE = 4;
	public static final int R_SIZE = 3;
	
	BitsHandler bH = new BitsHandler();
	
	public BitSet[] divideBits(BitSet m, int numberOfBits){
		int fillingSize = numberOfBits % R_SIZE == 0 ? 0 : R_SIZE - numberOfBits % R_SIZE;  
		
		BitSet filling = new BitSet(fillingSize);
		for(int i = 0; i < fillingSize; i++)
			filling.set(i, false);
		
		
		System.out.println("m: " + bH.convertBSToString(m, numberOfBits));
		BitSet completed = bH.concatenateBitStrings(m, filling, fillingSize);
		
		System.out.println("Size: " + numberOfBits);
		System.out.println("fillingSize: " + fillingSize);
		System.out.println("Completed: " + bH.convertBSToString(completed, numberOfBits + fillingSize));
		
		BitSet[] mArray = new BitSet[(numberOfBits + fillingSize) / R_SIZE];
		for(int i = 0; i < (numberOfBits + fillingSize) / R_SIZE; i++){
			mArray[i] = completed.get(i*R_SIZE, i * R_SIZE + R_SIZE);
			/*System.out.println("i: " + i);
			System.out.println("mArray[i]: " + bH.convertBSToString(mArray[i], R_SIZE));*/
		}
		
		return mArray;
	}
	
	public HashMap<String, String[]> getCipherTextData(BitSet[] mArray){
		int iterations = mArray.length;
		
		List<Integer> pi = generatePi();
		
		BitSet[] ivArray = new BitSet[ iterations ];
		BitSet[] oArray = new BitSet[ iterations ];
		BitSet[] tArray = new BitSet[ iterations ];
		BitSet[] cArray = new BitSet[ iterations ];
		
		System.out.println("\nCipher");
		System.out.println("j\tIj\tOj\ttj\tmj\tcj");
		
		int reverseIndex = iterations - 1;
		
		for( int j = 0; j < iterations; j++){
			ivArray[j] = j == 0 ? getIVj(j, null, null) : getIVj(j, cArray[j-1], ivArray[j-1]);
			
			oArray[j] = getOj(ivArray[j], pi);
			tArray[j] = getTj(oArray[j]);
			cArray[j] = getCj(tArray[j], mArray[reverseIndex - j]);
			
			System.out.println( (j+1) + "\t" + 
								bH.convertBSToString(ivArray[j], N_SIZE) + "\t" +
								bH.convertBSToString(oArray[j], N_SIZE) + "\t" + 
								bH.convertBSToString(tArray[j], R_SIZE) + "\t" +
								bH.convertBSToString(mArray[reverseIndex - j], R_SIZE) + "\t" +
								bH.convertBSToString(cArray[j], R_SIZE) + "\t");
		}
		
		int cTextLength = iterations * R_SIZE;
		BitSet cipherBS = new BitSet( cTextLength );
		for(int i = 0; i < cTextLength; i++)
			cipherBS.set(i, cArray[ (cTextLength - 1 - i) / R_SIZE ].get(i % R_SIZE));
		
		System.out.println("cipherBits: " + bH.convertBSToString(cipherBS, cTextLength));
		
		return getMapArrays(ivArray, oArray, tArray, mArray, cArray, true);
	}
	
	public HashMap<String, String> getCipherText(BitSet m, int numberOfBits){
		HashMap<String, String[]> mapData = getCipherTextData( divideBits(m, numberOfBits) );
		HashMap<String, String> map = new HashMap<>();
		
		String cipherText = "";
		String[] cipherArray = mapData.get(C_ARRAY);
		for(int i = cipherArray.length - 1; i >= 0; i--)
			cipherText += cipherArray[i];
		
		String iv = mapData.get(I_ARRAY)[0];
		
		map.put(CIPHER_TEXT, cipherText);
		map.put(IV, iv);
		map.put(M_SIZE, String.valueOf(numberOfBits));
		
		return map;
	}
	
	public HashMap<String, String[]> getDecipherTextData(BitSet[] cArray, BitSet iv){
		int iterations = cArray.length;
		
		List<Integer> pi = generatePi();
		
		BitSet[] ivArray = new BitSet[ iterations ];
		BitSet[] oArray = new BitSet[ iterations ];
		BitSet[] tArray = new BitSet[ iterations ];
		BitSet[] mArray = new BitSet[ iterations ];
		
		System.out.println("\nDecipher");
		System.out.println("j\tIj\tOj\ttj\tcj\tmj");
		
		int reverseIndex = iterations - 1;
		
		for( int j = 0; j < iterations; j++){
			ivArray[j] = j == 0 ? iv : getIVj(j, cArray[reverseIndex - (j-1)], ivArray[j-1]);
			
			oArray[j] = getOj(ivArray[j], pi);
			tArray[j] = getTj(oArray[j]);
			mArray[j] = getCj(tArray[j], cArray[reverseIndex - j]);
			
			System.out.println( (j+1) + "\t" + 
								bH.convertBSToString(ivArray[j], N_SIZE) + "\t" +
								bH.convertBSToString(oArray[j], N_SIZE) + "\t" + 
								bH.convertBSToString(tArray[j], R_SIZE) + "\t" +
								bH.convertBSToString(cArray[reverseIndex - j], R_SIZE) + "\t" +
								bH.convertBSToString(mArray[j], R_SIZE) + "\t");
		}
		
		int cTextLength = iterations * R_SIZE;
		BitSet plainBS = new BitSet( cTextLength );
		for(int i = 0; i < cTextLength; i++)
			plainBS.set(i, mArray[ (cTextLength - 1 - i) / R_SIZE ].get(i % R_SIZE));
		
		System.out.println("plainBits: " + bH.convertBSToString(plainBS, cTextLength));
		
		return getMapArrays(ivArray, oArray, tArray, mArray, cArray, false);
	}
	
	public String getDecipherText(BitSet m, BitSet iv, int numberOfBits, int plainSize){
		HashMap<String, String[]> mapData = getDecipherTextData( divideBits(m, numberOfBits), iv );
		
		String plainText = "";
		String[] plainArray = mapData.get(M_ARRAY);
		for(int i = plainArray.length - 1; i >= 0; i--)
			plainText += plainArray[i];
		
		plainText = plainText.substring(0, plainSize);
		
		return plainText;
	}
	
	private HashMap<String, String[]> getMapArrays(BitSet[] ivArray, BitSet[] oArray, BitSet[] tArray, BitSet[] mArray, BitSet[] cArray, boolean cipher){
		int dim = ivArray.length;
		String[] ivStringArray = new String[dim];
		String[] oStringArray = new String[dim];
		String[] tStringArray = new String[dim];
		String[] mStringArray = new String[dim];
		String[] cStringArray = new String[dim];
		
		for(int i = 0; i < dim; i++){
			ivStringArray[i] = bH.convertBSToString( ivArray[i], N_SIZE );
			oStringArray[i] = bH.convertBSToString( oArray[i], N_SIZE );
			tStringArray[i] = bH.convertBSToString( tArray[i], R_SIZE );
			if( cipher ){
				mStringArray[i] = bH.convertBSToString( mArray[i], R_SIZE );
				cStringArray[i] = bH.convertBSToString( cArray[dim - 1 - i], R_SIZE );
			}else{
				mStringArray[i] = bH.convertBSToString( mArray[dim - 1 - i], R_SIZE );
				cStringArray[i] = bH.convertBSToString( cArray[i], R_SIZE );
			}
			
		}
		
		HashMap<String, String[]> map = new HashMap<>();
		map.put(I_ARRAY, ivStringArray);
		map.put(O_ARRAY, oStringArray);
		map.put(T_ARRAY, tStringArray);
		map.put(M_ARRAY, mStringArray);
		map.put(C_ARRAY, cStringArray);
		
		return map;
	}
	
	private BitSet getIVj(int j, BitSet cjPrev, BitSet ivjPrev){
		if(j == 0)
			return generateIV();
		
		BitSet iv = new BitSet(N_SIZE);
		for(int i = 0; i < N_SIZE; i++){
			if( i < R_SIZE){
				try{
					iv.set( i, cjPrev.get(i) );
				}catch(IndexOutOfBoundsException e){
					iv.set( i, false);
				}
			}else{
				try{
					iv.set( i, ivjPrev.get(i - R_SIZE) );
				}catch(IndexOutOfBoundsException e){
					iv.set( i, false);
				}
			}
			
		}
		return iv;
	}
	
	private BitSet getOj(BitSet ivj, List<Integer> pi){
		int index = N_SIZE - 1;
		BitSet oj = new BitSet(N_SIZE);
		for(int i = 0; i < N_SIZE; i++){
			try{
				oj.set( index - i, ivj.get(index - pi.get(i)) );
			}catch(IndexOutOfBoundsException e){
				oj.set(N_SIZE - i - 1, false);
			}
		}
		return oj;
	}
	
	private BitSet getTj(BitSet oj){
		BitSet tj = new BitSet(R_SIZE);
		
		int index = N_SIZE - 1;
		int diference = N_SIZE - R_SIZE;
		for(int i = index; i >= diference; i--){
			try{
				tj.set(i - diference, oj.get(i) );
			}catch(IndexOutOfBoundsException e){
				tj.set(i - diference, false);
			}
		}
		return tj;
	}
	
	private BitSet getCj(BitSet tj, BitSet mj){
		BitSet cj = (BitSet) tj.clone();
		cj.xor(mj);
		return cj;
	}
	
	private BitSet generateIV(){
		BitSet iv = new BitSet(N_SIZE);
		/*for(int i = 0; i < N_SIZE; i++){
			iv.set( i, Math.round( Math.random() ) == 1 ? true: false);
		}*/
		iv.set(3);
		iv.set(1);
		//System.out.println("iv: " + bH.convertBSToString(iv, N_SIZE));
		return iv;
	}
	
	private List<Integer> generatePi(){
		//boolean rightOrder = false;
		//List<Integer> original = new ArrayList<>();
		List<Integer> pi = new ArrayList<>();
		/*for (int i = 0; i < N_SIZE; i++){
		    pi.add(i);
		    original.add(i);
		}
		
		while(!rightOrder){
			Collections.shuffle(pi);
			for (int i = 0; i < N_SIZE; i++){
				if( pi.get(i) != original.get(i))
					rightOrder = true;
			}
		}
		System.out.println("pi: " + pi.toString());*/
		pi.add(1);
		pi.add(2);
		pi.add(3);
		pi.add(0);
		return pi;
	}

}
