package com.dc.pin;

import java.util.Locale;
public class PinEncrypt {
	
	public final static int MAX_LEN = 16;
	public final static char[] mChars = "0123456789ABCDEF".toCharArray();
	public final static String pinkey = "11111111111111111111111111111111";		//PIN密钥明文
	
	public static void main(String[] args) {
		
		String pin = "123456";
		String cardno = "2222222222222222";
		String pinEncryptData = getPin(pin, cardno);
		System.out.println("pin明文 : " + pin +" 。卡号 :" + cardno + " 。pinkey明文 : " + pinkey + " 。 pin密 文 :	" + pinEncryptData);
		
	}
	
	/**
	 * @param pin pin明文
	 * @param cardno 卡号
	 * @return
	 */
	public static String getPin(String pin, String cardno) {
		String pinEnc = "";
		String pinBlock = "";
		String panBlock = "";
		
		if(pin != null) {
			String len = Integer.toHexString(pin.length());
			if(len.length() < 2) {
				len = "0"+len;
			}
			pinBlock = len + pin;
			int pinBlocklen = pinBlock.length();
			for(int i = 0; i < MAX_LEN - pinBlocklen; i++) {
				pinBlock += "F";
			}
		}
		
		if(cardno != null && cardno.length() >= MAX_LEN) {
			int endIndex = cardno.length() - 1;
			int startIndex = endIndex - 12;
			panBlock = "0000" + cardno.substring(startIndex,endIndex);
		}
		
		String PAN_XOR_PIN = "";
		
		for(int i = 0; i < MAX_LEN; i++) {
			int pindata =  Integer.parseInt(pinBlock.charAt(i)+"", 16);
			int pandata = Integer.parseInt(panBlock.charAt(i)+"", 16);
			int xordata = pindata ^ pandata;
			String tmp = mChars[xordata] + "";
			PAN_XOR_PIN += tmp;
		}
		
		byte[] encData = hexStr2Bytes(PAN_XOR_PIN);  //将pin异或pan后的数据转成byte类型
		byte[] keydata = hexStr2Bytes(pinkey);		 //将pinkey明文转成byte类型
		byte[] pinEncryptMode = DES3Utils.encryptMode(keydata, encData);	//得到pin密文
		pinEnc = bytesToHexString(pinEncryptMode);
		
		return pinEnc;
	}
	
	/**
	 * 字符转byte
	 * @param src
	 * @return
	 */
	public static byte[] hexStr2Bytes(String src) {
		/* 对输入值进行规范化整理 */
		src = src.trim().replace(" ", "").toUpperCase(Locale.US);
		// 处理值初始化
		int m = 0, n = 0;
		int l = src.length() / 2; // 计算长度
		byte[] ret = new byte[l]; // 分配存储空间

		for (int i = 0; i < l; i++) {
			m = i * 2 + 1;
			n = m + 1;
			ret[i] = (byte) (Integer.decode("0x" + src.substring(i * 2, m)
					+ src.substring(m, n)) & 0xFF);
		}
		return ret;
	}
	
	/**
	 * byte转字串
	 * @param bArray
	 * @return
	 */
	public static final String bytesToHexString(byte[] bArray) {
		if(bArray == null){
			return "";
		}
		StringBuffer sb = new StringBuffer(bArray.length);
		String sTemp;
		for (int i = 0; i < bArray.length; i++) {
			sTemp = Integer.toHexString(0xFF & bArray[i]);
			if (sTemp.length() < 2)
				sb.append(0);
			sb.append(sTemp.toUpperCase());
		}
		return sb.toString();
	}
	
}
