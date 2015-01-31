package com.l2jsaver.models;

import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;

public class Monster implements IMonster 
{
	private L2MonsterInstance mon;
	
	public Monster(L2MonsterInstance mon)
	{
		this.mon = mon;
	}

	@Override
	public void doDie(IPlayer arg0) 
	{

	}

	@Override
	public int getObjectId() 
	{
		return mon.getObjectId();
	}

}
