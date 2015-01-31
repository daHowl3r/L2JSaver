package com.l2jsaver.managers;

import com.l2jsaver.controllers.ExtensionController;
import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

public class InterfaceManager 
{
	
	public void onPlayerKill(L2Character playerA, L2PcInstance playerB)
	{
		L2PcInstance player;
		
		if (playerA instanceof L2PcInstance)
			player = (L2PcInstance)playerA;
		else
			return;
		
		IPlayer _playerA = getPlayer(player);
		IPlayer _playerB = getPlayer(playerB);
		
		ExtensionController.getInstance().onKillPlayer(_playerA, _playerB);
	}
	
	public void onLogin(L2PcInstance player)
	{
		ExtensionController.getInstance().onLogin(getPlayer(player));
	}
	
	public void onLogout(L2PcInstance player)
	{
		ExtensionController.getInstance().onLogout(getPlayer(player));
	}
	
	public void onNpcKill(L2PcInstance player, L2MonsterInstance monster)
	{
		IPlayer play = PlayerController.getInstance().getPlayer(player.getObjectId());
		IMonster mon = PlayerController.getInstance().getMonster(monster.getObjectId());
		ExtensionController.getInstance().onNpcKill(play, mon);;
	}
	
	public void onShutdown()
	{
		ExtensionController.getInstance().onServerShutdown();
	}
	
	public IPlayer getPlayer(L2PcInstance player)
	{
		IPlayer _player = PlayerController.getInstance().getPlayers().get(player.getObjectId());
		return _player;
	}
	
	public static final InterfaceManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final InterfaceManager _instance = new InterfaceManager();
	}
}
