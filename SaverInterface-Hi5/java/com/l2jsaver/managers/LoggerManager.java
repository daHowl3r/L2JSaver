package com.l2jsaver.managers;

import java.util.logging.Logger;

import com.l2jsaver.interfaces.ILogger;

public class LoggerManager implements ILogger 
{
	private static final Logger _log = Logger.getLogger("Saver");
	
	@Override
	public void fatal(String message) 
	{
		_log.severe(message);
	}

	@Override
	public void info(String message) 
	{
		_log.info(message);
	}

	@Override
	public void warn(String message) 
	{
		_log.warning(message);
	}
	
	public static final LoggerManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final LoggerManager _instance = new LoggerManager();
	}

}
