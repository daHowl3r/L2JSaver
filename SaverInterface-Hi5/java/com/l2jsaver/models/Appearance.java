package com.l2jsaver.models;

import com.l2jsaver.interfaces.IAppearance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class Appearance implements IAppearance 
{
	private L2PcInstance player;
	
	public Appearance(L2PcInstance player)
	{
		this.player = player;
	}
	
	@Override
	public void setNameColor(int val) 
	{
		player.getAppearance().setNameColor(val);
	}

	@Override
	public void setTitleColor(int val) 
	{
		player.getAppearance().setTitleColor(val);
	}
}
