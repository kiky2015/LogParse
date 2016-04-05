import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Sort {
	public static void main(String[] args) {

		String fileName = "F://nick//github//P92LogParse//log.txt";
		if(args[0] != null || args[0] != "") {
			fileName = args[0].replace("\\", "//");
		}
		
		File log = new File(fileName);
		BufferedReader br = null;
		BufferedWriter bw = null;
		String lineRead = null;
		StringBuffer sb = new StringBuffer();
		String tmpStr = null;
		if(log.exists()) {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(log)));
				while((lineRead = br.readLine()) != null) {
					sb.append(lineRead);
				}
				br.close();
				tmpStr = sb.toString().replaceAll("\\s*", "");//将空格替换成""
				sb.delete(0, sb.length());
				String[] split = tmpStr.split(":");
				for(int i = 0; i < split.length; i++) {
					sb.append(AlaTAG(split[i]));
					sb.append("\n");
				}
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		File log1 = new File("F://nick//github//P92LogParse//log1.txt");
		if(log1.exists()) {
			log1.delete();
			try {
				log1.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(log1)));
			bw.write(sb.toString());
			bw.flush();
			bw.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(sb.toString());
	}
	
	public static String AlaTAG(String tag) {
		if(tag == null || tag.length() == 0) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		String[] split = null;
		char charAt = tag.charAt(0);
		//0表示为指令
		if(charAt == '0') {
			split = tag.split("E/");		//将指令与标识分开
			String parseTag = ParseTag(split[0]);	//解析指令
			sb.append(parseTag);
			for(int i = 1; i < split.length; i++) {
				sb.append("\n");
				sb.append(split[i]);
			}
		}else if(charAt == '(') {
			
		}else if(charAt == 'S' || charAt == 'E') {
			sb.append(tag);
		}
		return sb.toString();
	}
	
	public static String ParseTag(String tag) {
		StringBuffer sb = new StringBuffer();
		int index = 0;
		int step = 2;
		int length = 3;
		int datacount = 0;
		int count = 0;
		//02 00 02
		for(int i = 0; i < length; i++) {
			String substring = tag.substring(index,index+step);
			sb.append(substring + " ");
			index += step;
		}
		sb.append("\n");
		
		length = 2;
		//长度
		for(int i = 0; i < length; i++) {
			String substring = tag.substring(index,index+step);
			sb.append(substring + " ");
			index += step;
		}
		sb.append("\n");
		
		length = 7;
		//version 00 05 FF 00 00 01 31 
		for(int i = 0; i < length; i++) {
			String substring = tag.substring(index,index+step);
			sb.append(substring + " ");
			index += step;
		}
		sb.append("\n");
		
		length = 2;
		//长度
		for(int i = 0; i < length; i++) {
			String substString = tag.substring(index,index+step);
			sb.append(substString+" ");
			index+=step;
			if(i == 1) {
				String len = sb.substring(sb.length()-8, sb.length()).replace(" ", "");	//得到长度的字串
				String number = "";
				//将长度字串中非数据符号去掉
				for(int j = 0; j < len.length();j++) {
					if ((len.charAt(j) >= '0' && len.charAt(j) <= '9') ||
							(len.charAt(j) >= 'A' && len.charAt(j) <= 'F')) {
						number += len.charAt(j);
					}
				}
				//开始为0时，0去掉
				int k = 0;
				for(; k < number.length();) {
					if(number.charAt(k) == '0') {
						k++;
					}else {
						break;
					}
				}
				number = number.substring(k);
				datacount = Integer.parseInt(number, 16);
			}
		}
		sb.append("\n");
		
		boolean isNotEnd = true;
		//解析数据
		while(isNotEnd) {
			length = 4;
			for(int i = 0; i < length; i++) {
				String substring = tag.substring(index,index+step);
				sb.append(substring + " ");
				index += step;
				if(i == 3) {
					int len = Integer.parseInt(substring, 16);
					length += len;
				}
				count++;
				if(count + 1 > datacount) {
					isNotEnd = false;
					break;
				}
			}
			sb.append("\n");
		}
		
		length = 2;
		for(int i = 0; i < length; i++) {
			String substring = tag.substring(index,index+step);
			sb.append(substring + " ");
			index += step;
			count++;
		}
		sb.append("\n");
		return sb.toString();
	}
}
