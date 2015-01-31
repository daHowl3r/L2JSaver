package com.l2jsaver.features.RaidbossManager.Controllers;

import javolution.util.FastMap;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.corefeatures.L2JFrame.AbstractController;
import com.l2jsaver.corefeatures.L2JFrame.L2JFrame;
import com.l2jsaver.interfaces.IBoss;
import com.l2jsaver.interfaces.IPlayer;

public class RaidbossController extends AbstractController
{

	@Override
	public void execute(IPlayer player) 
	{
	}

	@Override
	public void execute(IPlayer activeChar, FastMap<Integer, String> arguments) 
	{
		String command = arguments.get(0);
		
		if (command.startsWith("observBoss"))
		{
			if (activeChar.isInJail())
				return;
			if (activeChar.isParalyzed())
				return;
			if (activeChar.isInOlympiadMode())
				return;
			if (activeChar.isInCombat())
				return;
			if (activeChar.getPvpFlag() > 0)
				return;
			if (!activeChar.isInPeaceZone())
				return;
			
			String _id = command.substring(11);
			int id = Integer.parseInt(_id);
			
			IBoss mainRb = PlayerController.getInstance().getBossById(id);
			activeChar.enterObserverMode(mainRb.getX(), mainRb.getY(), mainRb.getZ()+150);
		}
	}
	
	private volatile static RaidbossController singleton;
	
	
	public static RaidbossController getInstance()
	{
		synchronized(RaidbossController.class)
		{
			if (singleton == null)
				singleton = new RaidbossController();
		}
		return singleton;
	}
	
	public RaidbossController()
	{
		L2JFrame.getInstance().registerListener(this);
	}

}
