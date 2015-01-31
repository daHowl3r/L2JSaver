/*
 * Authors: Issle, Howler, David
 * File: CallbackWrapper.java
 */
package com.l2jsaver.injector;

import java.util.StringTokenizer;
import java.util.logging.Logger;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.controllers.VoicedHandlerController;
import com.l2jsaver.corefeatures.L2JFrame.L2JFrame;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jsaver.interfaces.IVoicedCommandHandler;
import com.l2jsaver.managers.InterfaceManager;
import com.l2jsaver.managers.LoggerManager;
import com.l2jsaver.managers.SaverAdapterManager;
import com.l2jsaver.models.Player;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.L2Summon;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.L2GameClient;
import com.l2jserver.gameserver.network.clientpackets.EnterWorld;
import com.l2jserver.gameserver.network.clientpackets.RequestBypassToServer;

/**
 * @author Issle
 * 
 * Put in here all the functions that will get
 * hooked in the core. Hooking will happen during
 * class loading by the Javaagent.
 *
 */
public class CallbackWrapper
{
	private static final Logger _log = Logger.getLogger(CallbackWrapper.class.getName());
	
	private static final String PcInstance = "net/sf/l2j/gameserver/model/actor/instance/L2PcInstance";
	private static final String GameServer = "net/sf/l2j/gameserver/GameServer";
	private static final String EnterWorld = "net/sf/l2j/gameserver/network/clientpackets/EnterWorld";
	private static final String Bypass = "net/sf/l2j/gameserver/network/clientpackets/RequestBypassToServer";
	private static final String MonsterInstance = "net/sf/l2j/gameserver/model/actor/instance/L2MonsterInstance";
	private static final String Shutd = "net/sf/l2j/gameserver/Shutdown";
	private static final String Chat = "net/sf/l2j/gameserver/handler/chathandlers/ChatAll";
	private static final String Community = "net/sf/l2j/gameserver/communitybbs/CommunityBoard";
	
	public static void init()
	{
		System.out.println("Injecting Saver Interface");
		
		CallbackTransformer.getInstance().appendReplacement(GameServer, "GameServer", "loadSaverComponents()", "", true, true);
		CallbackTransformer.getInstance().appendReplacement(EnterWorld, "runImpl", "onLogin(this)", "", true, true);
		CallbackTransformer.getInstance().appendReplacement(PcInstance, "logout", "onLogout(this)", "", false, true);
		CallbackTransformer.getInstance().appendReplacement(PcInstance, "doDie", "onPlayerKill(this, killer)", "", true, true);
		CallbackTransformer.getInstance().appendReplacement(Bypass, "runImpl", "onBypass(this, _command)", "", false, true);
		CallbackTransformer.getInstance().appendReplacement(Chat, "handleChat", "onVoicedCommand(activeChar, text)", "", false, true);
		CallbackTransformer.getInstance().appendReplacement(MonsterInstance, "doDie", "onNpcKill(this, killer)", "", true, true);
		CallbackTransformer.getInstance().appendReplacement(Shutd, "run", "onShutdown()", "", false, true);
		CallbackTransformer.getInstance().appendReplacement(Community, "handleCommands", "onBBSBypass(client, command)", "", false, true);
		
		System.out.println("Saver Injection Done");
		System.out.println("Interface: Interlude - Pack: aCis");
	}
	
	public static boolean loadSaverComponents()
	{
		printSection("L2JSaver Components");
		SaverAdapterManager.getInstance();
		return false;
	}
	
	public static boolean onShutdown()
	{
		InterfaceManager.getInstance().onShutdown();
		return false;
	}
	
	public static boolean onLogin(EnterWorld packet)
	{
		generatePlayerData(packet.getClient().getActiveChar());
		InterfaceManager.getInstance().onLogin(packet.getClient().getActiveChar());
		return false;
	}
	
	public static boolean onLogout(L2PcInstance player)
	{
		InterfaceManager.getInstance().onLogout(player);
		removePlayerData(player);
		return false;
	}
	
	public static boolean onBBSBypass(L2GameClient client, String command)
	{
		if (command.startsWith("_bbsFrame"))
		{
			IPlayer player = InterfaceManager.getInstance().getPlayer(client.getActiveChar());
			String cmd = command.split("-")[1];
			L2JFrame.getInstance().bypassToServer(player, cmd);
			return true;
		}
		
		return false;
	}
	
	public static boolean onBypass(RequestBypassToServer packet, String _command)
	{
		if (_command.startsWith("L2JFrame_"))
		{
			final L2PcInstance activeChar = packet.getClient().getActiveChar();
		
			if (activeChar == null)
				return false;
		
			if (!packet.getClient().getFloodProtectors().getServerBypass().tryPerformAction(_command))
				return false;
		
			IPlayer player = InterfaceManager.getInstance().getPlayer(activeChar);
		
			L2JFrame.getInstance().checkBypass(_command, player);
			return true;
		}
		return false;
	}
	
	public static boolean onNpcKill(L2MonsterInstance monster, L2Character player)
	{
		L2PcInstance play;
		
		if (player instanceof L2PcInstance)
			play = (L2PcInstance) player;
		else if (player instanceof L2Summon)
			play = ((L2Summon) player).getOwner();
		else
		{
			LoggerManager.getInstance().warn("onNpcKill() can't find player");
			return false;
		}
		
		InterfaceManager.getInstance().onNpcKill(play, monster);
			
		return false;
	}
	
	public static boolean onPlayerKill(L2PcInstance actor, L2Character killer)
	{
		InterfaceManager.getInstance().onPlayerKill(killer, actor);
		return false;
	}
	
	public static boolean onVoicedCommand(L2PcInstance _player, String _text)
	{
		boolean used = false;
		
		IPlayer player = PlayerController.getInstance().getPlayer(_player.getObjectId());
		if (_text.startsWith("?"))
		{	
			StringTokenizer st = new StringTokenizer(_text);
			IVoicedCommandHandler vch;
			String command = "";
			String params = "";
			
			if (st.countTokens() > 1)
			{
				command = st.nextToken().substring(1);
				params = _text.substring(command.length() + 2);
				vch = VoicedHandlerController.getInstance().getVoicedCommandHandler(command);
			}
			else	
			{
				command = _text.substring(1);
				vch = VoicedHandlerController.getInstance().getVoicedCommandHandler(command);
			}
			if (vch != null)
			{
				vch.useVoicedCommand(command, player, params);
				used = true;
			}
		}
		return used;
	}
	
	private static void generatePlayerData(L2PcInstance player)
	{
		Player p = new Player(player);
		
		if (PlayerController.getInstance().getPlayers().containsKey(player.getObjectId()))
			PlayerController.getInstance().getPlayers().remove(player.getObjectId());
		
		PlayerController.getInstance().addPlayer(p);
	}
	
	private static void removePlayerData(L2PcInstance player)
	{
		PlayerController.getInstance().getPlayers().remove(player.getObjectId());
	}
	
	public static void printSection(String s)
	{
		s = "=[ " + s + " ]";
		while (s.length() < 61)
		{
			s = "-" + s;
		}
		_log.info(s);
	}
}
