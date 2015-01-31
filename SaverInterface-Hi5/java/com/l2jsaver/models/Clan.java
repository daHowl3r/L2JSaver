package com.l2jsaver.models;

import javolution.util.FastList;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.interfaces.IClan;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

public class Clan implements IClan 
{
	private L2Clan clan;
	
	public Clan(L2Clan clan)
	{
		this.clan = clan;
	}

	@Override
	public void broadcastClanStatus() 
	{
		clan.broadcastClanStatus();
	}

	@Override
	public void broadcastToOnlineAllyMembers(String packe) 
	{
		L2GameServerPacket packet = null;
		clan.broadcastToOnlineAllyMembers(packet);
	}

	@Override
	public void broadcastToOnlineMembers(String packe) 
	{
		L2GameServerPacket packet = null;
		clan.broadcastToOnlineMembers(packet);
	}

	@Override
	public int getAllyId() 
	{
		return clan.getAllyId();
	}

	@Override
	public String getAllyName() 
	{
		return clan.getAllyName();
	}

	@Override
	public int getCastleId() 
	{
		return clan.getCastleId();
	}

	@Override
	public int getClanId() 
	{
		return clan.getId();
	}

	@Override
	public int getFortId() 
	{
		return clan.getFortId();
	}

	@Override
	public int getHideoutId() 
	{
		return clan.getHideoutId();
	}

	@Override
	public IPlayer getLeader() 
	{	
		L2PcInstance player = clan.getLeader().getPlayerInstance();
		IPlayer p = PlayerController.getInstance().getPlayer(player.getObjectId());
		return p;
	}

	@Override
	public int getLeaderId() 
	{
		return clan.getLeaderId();
	}

	@Override
	public String getLeaderName() 
	{
		return clan.getLeaderName();
	}

	@Override
	public int getLevel() 
	{
		return clan.getLevel();
	}

	@Override
	public String getName() 
	{
		return clan.getName();
	}

	@Override
	public FastList<IPlayer> getOnlineMembers() 
	{
		FastList<L2PcInstance> clannies = clan.getOnlineMembers(0);
		FastList<IPlayer> _clannies = new FastList<IPlayer>();
		
		for (L2PcInstance player : clannies)
		{
			IPlayer p = PlayerController.getInstance().getPlayer(player.getObjectId());
			_clannies.add(p);
		}
		
		return _clannies;
	}

	@Override
	public int getOnlineMembersCount() 
	{
		return clan.getOnlineMembersCount();
	}

	@Override
	public boolean isMember(int objectId) 
	{
		return clan.isMember(objectId);
	}

}
