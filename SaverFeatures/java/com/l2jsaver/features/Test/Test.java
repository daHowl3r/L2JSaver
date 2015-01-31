package com.l2jsaver.features.Test;

import com.l2jsaver.abstracts.AbstractExtension;
import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.interfaces.IPlayer;

public class Test extends AbstractExtension
{
	public Test()
	{
		super(2, "Test");
		if (isDisabled())
			return;
	}

	@Override
	public void onLogin(IPlayer player) 
	{
		SaverController.getInstance().getEventHandler().announceToAll("Welcome " + player.getName() + " from Saver-Core");
	}
	
	public static final Test getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final Test _instance = new Test();
	}

}
