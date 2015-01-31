package com.l2jsaver.models;

import com.l2jsaver.interfaces.IBoss;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;

public class Boss implements IBoss 
{
	private L2RaidBossInstance boss;
	
	public Boss(L2RaidBossInstance boss)
	{
		this.boss = boss;
	}
	
	@Override
	public int getBossId() 
	{
		return boss.getId();
	}

	@Override
	public double getCurrentHp() 
	{
		return boss.getCurrentHp();
	}

	@Override
	public double getMaxHp() 
	{
		return boss.getMaxHp();
	}

	@Override
	public String getName() 
	{
		return boss.getName();
	}

	@Override
	public int getObjectId() 
	{
		return boss.getObjectId();
	}

	@Override
	public int getX() 
	{
		return boss.getX();
	}

	@Override
	public int getY() 
	{
		return boss.getY();
	}

	@Override
	public int getZ() 
	{
		return boss.getZ();
	}

	@Override
	public boolean isDead() 
	{
		return boss.isDead();
	}

	@Override
	public boolean isInCombat() 
	{
		return boss.isInCombat();
	}

}
