package com.lsj.java;

import java.io.*;
import java.util.*;

public class Test {

	public static void main(String[] args) throws Exception{
		Properties p = new Properties();
		FileReader fr = new FileReader("classInfo.properties");
		p.load(fr);
		fr.close();
		
		String className = p.getProperty("className");
		Class c = Class.forName(className);
		Object o = c.newInstance();
		
		System.out.println(o);
		System.out.println(p);
		System.out.println("Done!");
	}
}
