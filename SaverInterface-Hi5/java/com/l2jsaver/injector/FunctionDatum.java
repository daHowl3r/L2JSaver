/*
 * Authors: Issle, Howler, David
 * File: ClassDatum.java
 */
package com.l2jsaver.injector;

/**
 * @author Issle
 * 
 */
public class FunctionDatum {

	public static String classpath = "com.l2jsaver.injector.CallbackWrapper";
	public String declaration;
	public String returnType;
	public boolean isEnd;
	public boolean isType;
	
	public FunctionDatum(String declaration, String returnType, boolean isEnd, boolean isType)
	{
		this.declaration = declaration;
		this.returnType = returnType;
		this.isEnd = isEnd;
		this.isType = isType;
	}
	
	public String constructInjection()
	{
		String s ="";
		if(isEnd)
		{
			s = classpath+"."+declaration+";";
		}
		else
		{
			s+= "if(" + classpath+"."+declaration+")";
			if(isType)
				s+= "return "+returnType+";";
			else
				s+= "return "+classpath+"."+returnType+";";
		}
		
		return s;
	}
}
