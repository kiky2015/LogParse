package com.dc.pin;


import java.io.ByteArrayOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**  
 *字符串 DESede(3DES) 加密  
 */ 

public class DES3Utils {  

	private static final String Algorithm = "DESede"; // 定义 加密算法,可用 DES,DESede,Blowfish  
	private static final String hexString = "0123456789ABCDEF";  

	/**  
	 *                                                     
	 * @param keybyte  加密密钥，长度为24字节  
	 * @param src     字节数组(根据给定的字节数组构造一个密钥。 )  
	 * @return  
	 */ 
	public static byte[] encryptMode(byte[] key, byte[] src) {  
		try {  
			if (key == null || key.length < 16 || src == null || src.length == 0) {
				return null;
			}
			
			byte[] keyBytes = new byte[24];
			System.arraycopy(key, 0, keyBytes, 0, 16);
			System.arraycopy(key, 0, keyBytes, 16, 8);
			
			// 根据给定的字节数组和算法构造一个密钥  
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);  
			// 加密  
			Cipher c1 = Cipher.getInstance(Algorithm);  
			c1.init(Cipher.ENCRYPT_MODE, deskey);  
			
			byte[] byRes = c1.doFinal(src);
			
			byte[] byResult = new byte[byRes.length-8];
			System.arraycopy(byRes, 0, byResult, 0, byResult.length);
			return byResult;  
		} catch (java.security.NoSuchAlgorithmException e1) {  
			e1.printStackTrace();  
		} catch (javax.crypto.NoSuchPaddingException e2) {  
			e2.printStackTrace();  
		} catch (java.lang.Exception e3) {  
			e3.printStackTrace();  
		}  

		return null;  
	}  

	/**  
	 *   
	 * @param keybyte 密钥  
	 * @param src       需要解密的数据  
	 * @return  
	 */ 
	public static byte[] decryptMode(byte[] key, byte[] src) {  
		try {  
			if (key == null || key.length < 16 || src == null || src.length < 16) {
				return null;
			}
			
			byte[] keyBytes = new byte[24];
			System.arraycopy(key, 0, keyBytes, 0, 16);
			System.arraycopy(key, 0, keyBytes, 16, 8);
			SecretKey deskey = new SecretKeySpec(keyBytes, Algorithm);  
			Cipher c1 = Cipher.getInstance("DESede/ECB/NOPadding");  
			c1.init(Cipher.DECRYPT_MODE, deskey); 
			return c1.doFinal(src);  
		} catch (java.security.NoSuchAlgorithmException e1) {  
			e1.printStackTrace();  
		} catch (javax.crypto.NoSuchPaddingException e2) {  
			e2.printStackTrace();  
		} catch (java.lang.Exception e3) {  
			e3.printStackTrace();  
		}  

		return null;  
	}  
	
//	public static byte[] decryptMode(byte[] keybyte, byte[] src) {  
//		try {  
//			// �����Կ  
//			byte[] kb = new byte[24];
//			if(keybyte.length == 16){
//				System.arraycopy(keybyte, 0, kb, 0, 16);
//				System.arraycopy(keybyte, 0, kb, 16, 8);
//			}else if(keybyte.length == 24){
//				System.arraycopy(keybyte, 0, kb, 0, 24);
//			}else{
//				return null;
//			}
//			SecretKey deskey = new SecretKeySpec(kb, Algorithm);  
//			// ����  
//			Cipher c1 = Cipher.getInstance("DESede/ECB/NOPadding");  
//			c1.init(Cipher.DECRYPT_MODE, deskey);  
//			return c1.doFinal(src);  
//		} catch (java.security.NoSuchAlgorithmException e1) {  
//			e1.printStackTrace();  
//		} catch (javax.crypto.NoSuchPaddingException e2) {  
//			e2.printStackTrace();  
//		} catch (java.lang.Exception e3) {  
//			e3.printStackTrace();  
//		}  
//		return null;  
//	}
	
	public static byte[] decryptMode(String key ,String src){

		if(key.length() == 16){
			key = key + key.substring(0,8);
		}
		byte[] keyBytes = StringToByteArray(key.substring(0, 24));
		byte[] decoded= decryptMode(keyBytes, StringToByteArray(src));
		if(decoded == null){
			return null;
		}
		return decoded;
	}
	
	public static char[] ByteToCharArray(byte[] bs){
		int bsl = bs.length;
		char[] charArray=new char[bsl];
		for(int i=0; i<bsl;i++){
			charArray[i]=(char) (((char)bs[i]) & 0x00FF);
		}
		return charArray;
	}
	
	public static byte[] StringToByteArray(String s){
		int sl = s.length();
		byte[] charArray=new byte[sl];
		for(int i=0; i<sl;i++){
			char charElement=s.charAt(i);
			charArray[i]=(byte)charElement;
		}
		return charArray;
	}

	/**  
	 * 字符串转为16进制  
	 * @param str  
	 * @return  
	 */ 

	public static String encode(String str)   
	{   
		//根据默认编码获取字节数组   
		byte[] bytes=str.getBytes();   
		StringBuilder sb=new StringBuilder(bytes.length*2);   

		//将字节数组中每个字节拆解成2位16进制整数   
		for(int i=0;i<bytes.length;i++)   
		{   
			sb.append(hexString.charAt((bytes[i]&0xf0)>>4));   
			sb.append(hexString.charAt((bytes[i]&0x0f)>>0));   
		}   

		return sb.toString();   
	}   

	/**  
	 *   
	 * @param bytes  
	 * @return  
	 * 将16进制数字解码成字符串,适用于所有字符（包括中文）   
	 */  
	public static String decode(String bytes)   
	{   
		ByteArrayOutputStream baos=new ByteArrayOutputStream(bytes.length()/2);   

		//将每2位16进制整数组装成一个字节   
		for(int i=0;i<bytes.length();i+=2)   
			baos.write((hexString.indexOf(bytes.charAt(i))<<4 |hexString.indexOf(bytes.charAt(i+1))));   

		return new String(baos.toByteArray());   
	}   

	// 转换成十六进制字符串  
	public static String byte2hex(byte[] b) {  
		String hs = "";  
		String stmp = "";  

		for (int n = 0; n < b.length; n++) {  
			stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
			if (stmp.length() == 1)  
				hs = hs + "0" + stmp;  
			else 
				hs = hs + stmp;  
			if (n < b.length - 1)  
				hs = hs + ":";  
		}  
		return hs.toUpperCase();  
	} 

	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
		StringBuffer sb = new StringBuffer();  
		for (int i = 0; i < buf.length; i++) {  
			String hex = Integer.toHexString(buf[i] & 0xFF);  
			if (hex.length() == 1) {  
				hex = '0' + hex;  
			}  
			sb.append(hex.toUpperCase());  
		}  
		return sb.toString();  
	}

	/**将16进制转换为二进制 
	 * @param hexStr 
	 * @return 
	 */  
	public static byte[] parseHexStr2Byte(String hexStr) {  
		if (hexStr.length() < 1)  
			return null;  
		byte[] result = new byte[hexStr.length()/2];  
		for (int i = 0;i< hexStr.length()/2; i++) {  
			int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
			int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
			result[i] = (byte) (high * 16 + low);  
		}  
		return result;  
	}    
} 
