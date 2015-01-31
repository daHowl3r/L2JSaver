package com.l2jsaver.features.RaidbossManager.Handlers;

import com.l2jsaver.corefeatures.L2JFrame.L2JFrame;
import com.l2jsaver.features.RaidbossManager.Controllers.RaidbossPagingController;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jsaver.interfaces.IVoicedCommandHandler;


public class RaidBossHandler implements IVoicedCommandHandler 
{
	private static final String[] VOICED_COMMANDS =
	{
		"rb"
	};
	
	@Override
	public boolean useVoicedCommand(String command, IPlayer activeChar, String target) 
	{
		L2JFrame.getInstance().send(activeChar, RaidbossPagingController.getInstance().getNewWindow(0, activeChar));
		return false;
	}

	@Override
	public String[] getVoicedCommandList() 
	{
		return VOICED_COMMANDS;
	}
	
	public static final RaidBossHandler getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final RaidBossHandler _instance = new RaidBossHandler();
	}

}