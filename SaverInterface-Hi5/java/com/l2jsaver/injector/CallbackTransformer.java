package com.l2jsaver.injector;
/*
 * Authors: Issle, Howler, David
 * File: CallbackTransformer.java
 */

import java.io.ByteArrayInputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.LoaderClassPath;
import javolution.util.FastMap;

/**
 * @author Issle
 * @InitialAuthor SoulKeeper
 *
 */
public class CallbackTransformer implements ClassFileTransformer{

	public FastMap<String, HashMap<String,FunctionDatum>> data = new FastMap<String, HashMap<String,FunctionDatum>>();
	
	public void appendReplacement(String className, String method, String declaration, String returnType, boolean isEnd, boolean isType)
	{
		System.out.println("Appending:"+className+"."+method+"()");
		
		if(data.containsKey(className))
		{
			HashMap<String,FunctionDatum> datum = data.get(className);
			
			datum.put(method, new FunctionDatum(declaration, returnType, isEnd, isType));
		}
		else
		{
			HashMap<String,FunctionDatum> datum = new HashMap<String,FunctionDatum>();
			datum.put(method, new FunctionDatum(declaration, returnType, isEnd, isType));
			data.put(className, datum);
		}
	}
	
	public boolean hasClass(String className)
	{
		if(data.containsKey(className))
			return true;
		
		return false;
	}

	public HashMap<String, FunctionDatum> getClassMethods(String className) {

		return data.get(className);
		
	}
	
	public static void premain(String args, Instrumentation instrumentation)
	{
		CallbackTransformer.getInstance();
		CallbackWrapper.init();
		instrumentation.addTransformer(CallbackTransformer.getInstance(), false);
		
	}
	
	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
		ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException
	{
		try
		{
			return transformClass(loader, classfileBuffer, className, classBeingRedefined);
		}
		catch (Exception e)
		{
			return null;
		}
	}

	
	protected byte[] transformClass(ClassLoader loader, byte[] clazzBytes, String className, Class<?> classBeingRedefined) throws Exception
	{
		ClassPool cp = new ClassPool();
		cp.appendClassPath(new LoaderClassPath(loader));
		CtClass clazz = cp.makeClass(new ByteArrayInputStream(clazzBytes));
		
		Set<CtMethod> methdosToEnhance = new HashSet<CtMethod>();
		Set<CtConstructor> constructorsToEnhance = new HashSet<CtConstructor>();
		
		HashMap<String,FunctionDatum> methods = getClassMethods(className);
		for (CtMethod method : clazz.getMethods())
		{
			if(methods.containsKey(method.getName()))
				methdosToEnhance.add(method);
		}
		
		for(CtConstructor constr : clazz.getConstructors())
		{
			if(methods.containsKey(constr.getName()))
				constructorsToEnhance.add(constr);
		}

		if(!methdosToEnhance.isEmpty() || !constructorsToEnhance.isEmpty())
		{
			if (!methdosToEnhance.isEmpty())
			{
				for (CtMethod method : methdosToEnhance)
				{
					try
					{
						FunctionDatum f = methods.get(method.getName());
						if(!f.isEnd)
							method.insertBefore("{ "+ f.constructInjection()+" }");
						else
							method.insertAfter("{ "+ f.constructInjection()+" }");
					}
					catch(Exception e)
					{
						System.out.println("Error in "+ method.getName());
						e.printStackTrace();
					}
				}

			}
			if(!constructorsToEnhance.isEmpty())
			{
				for (CtConstructor method : constructorsToEnhance)
				{
					try
					{
						FunctionDatum f = methods.get(method.getName());
						if(!f.isEnd)
							method.insertBefore("{ "+ f.constructInjection()+" }");
						else
							method.insertAfter("{ "+ f.constructInjection()+" }");
					}
					catch(Exception e)
					{
						System.out.println("Error in "+ method.getName());
						e.printStackTrace();
					}
				}
			}
			System.out.println("Recompiling class:"+ className);
			return clazz.toBytecode();
		}
		else
		{
			return null;
		}
	}
	
	private volatile static CallbackTransformer singleton;

	private CallbackTransformer() {
		data = new FastMap<String, HashMap<String,FunctionDatum>>();
		data.shared();
		
	}

	public static CallbackTransformer getInstance() {
		if (singleton == null) {
			synchronized (CallbackTransformer.class) {
				if (singleton == null)
					singleton = new CallbackTransformer();
			}
		}
		return singleton;
	}

}

