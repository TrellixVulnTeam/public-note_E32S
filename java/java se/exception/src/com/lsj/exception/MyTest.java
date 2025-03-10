package com.lsj.exception;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MyTest {
	public static void m() throws FileNotFoundException{
		FileInputStream fis = new FileInputStream("ab.txt");
	}
	
	public static int m1(){
		int i = 0;
		
		try{
			i=10;
			return i;
		}finally{
			i++;
		}
	}
	
	public static void main(String[] args) {
		/*
		try{
			m();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}finally{
			System.out.println("final!");
		}
		
		System.out.println(m1());
		*/
		
		String username = "jack";
		
		try {
			new CustomerService().register(username);
		} catch (IllegaNameException e) {
			System.out.println(e.hashCode());
			e.printStackTrace();
		}
		
		System.out.println("done!");
	}

}

class IllegaNameException extends Exception{
	public IllegaNameException(){}
	public IllegaNameException(String msg){super(msg);}
}

class CustomerService{
	public void register(String name) throws IllegaNameException{
		if(name.length() < 6){
			//创建异常对象
			IllegaNameException e = new IllegaNameException("用户名不可少于6位");
			System.out.println(e.hashCode());
			throw e;
		}
	}
}