package encoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public class App {

	public static void main(String[] args) throws IOException {
		
		String name = "I am 君山";
		
		
		show(name.getBytes());
		System.out.print("iso8859: ");show(name.getBytes("ISO-8859-1"));
		System.out.print("gb2312: ");show(name.getBytes("GB2312"));
		System.out.print("gbk: ");show(name.getBytes("GBK"));
		System.out.print("utf16: ");show(name.getBytes("UTF-16"));
		System.out.print("unicode: ");show(name.getBytes("unicode"));		//unicode等价于utf-8, char就是一个unicde格式的字符
		System.out.print("utf8: ");show(name.getBytes("UTF-8"));
		
		String str = new String(name.getBytes("ISO-8859-1"), "ISO-8859-1");
		System.out.println(str);
		/*
		OutputStream iso8859 = new FileOutputStream(new File("iso8859.html"));
		OutputStream gb2312 = new FileOutputStream(new File("gb2312.html"));
		OutputStream gbk = new FileOutputStream(new File("gbk.html"));
		OutputStream utf16 = new FileOutputStream(new File("utf16.html"));
		OutputStream utf8 = new FileOutputStream(new File("utf8.html"));
		
		iso8859.write(name.getBytes("ISO-8859-1"));iso8859.close();
		gb2312.write(name.getBytes("GB2312"));gb2312.close();
		gbk.write(name.getBytes("GBK"));gbk.close();
		utf16.write(name.getBytes("UTF-16"));utf16.close();
		utf8.write(name.getBytes("UTF-8"));utf8.close();
		*/
	}
	
	private static void show(byte[] bytes){
		for(byte b : bytes){
			System.out.print(Long.toHexString(Math.abs(b&0xFF))+" ");
		}
		System.out.println();
	}

}
