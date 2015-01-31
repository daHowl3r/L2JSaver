package com.l2jsaver.managers;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ScheduledFuture;

import com.l2jsaver.interfaces.IEventHandler;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jserver.L2DatabaseFactory;
import com.l2jserver.gameserver.ThreadPoolManager;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.serverpackets.CreatureSay;
import com.l2jserver.gameserver.util.Broadcast;

public class EventManager implements IEventHandler 
{

	@Override
	public void announceToAll(String message) 
	{
		Broadcast.toAllOnlinePlayers(message);
	}

	@Override
	public void creatureSay(String message, String sender, int channel, IPlayer player) 
	{
		L2PcInstance _player = L2World.getInstance().getPlayer(player.getObjectId());
		_player.sendPacket(new CreatureSay(0, channel, sender, message));
	}
	
	public static final EventManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final EventManager _instance = new EventManager();
	}

	@Override
	public Connection getConnection()
	{
		try 
		{
			return L2DatabaseFactory.getInstance().getConnection();	
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void closeConnection(Connection con) 
	{
		try 
		{
			con.close();
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
	}

	@Override
	public ScheduledFuture<?> scheduleGeneralAtFixedRate(Runnable r, long init, long delay) 
	{
		return ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(r, init, delay);
	}

}
