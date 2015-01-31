package com.l2jsaver.features.Test;

import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.interfaces.IExtension;
import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;

public class Test implements IExtension
{
	public Test()
	{
		
	}

	@Override
	public void reloadExtension() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopExtension() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogin(IPlayer player) 
	{
		SaverController.getInstance().getEventHandler().announceToAll("Welcome " + player.getName() + " from Saver-Core");
	}

	@Override
	public void onLogout(IPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerKill(IPlayer playerA, IPlayer playerB) 
	{
		// TODO Auto-generated method stub
		
	}
	
	public static final Test getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final Test _instance = new Test();
	}

	@Override
	public void onNpcKill(IPlayer killer, IMonster killed) {
		// TODO Auto-generated method stub
		
	}

}
