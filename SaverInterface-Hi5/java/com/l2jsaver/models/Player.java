package com.l2jsaver.models;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.interfaces.IAppearance;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jserver.gameserver.model.L2Object;
import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.instance.L2ItemInstance;
import com.l2jserver.gameserver.model.zone.ZoneId;
import com.l2jserver.gameserver.network.serverpackets.NpcHtmlMessage;
import com.l2jserver.gameserver.network.serverpackets.ShowBoard;

public class Player implements IPlayer 
{
	private String name;
	private String accountName;
	private int objectId;
	private String ip;
	private L2PcInstance player;
	private boolean isGM;
	private int firstBloods;
	private int maxKills;
	private int kills;
	private IAppearance appearance;
	
	public Player(L2PcInstance player)
	{
		this.player = player;
		this.name = player.getName();
		this.accountName = player.getAccountName();
		this.objectId = player.getObjectId();
		this.ip = player.getClient().getConnection().getInetAddress().getHostAddress();
		this.isGM = player.isGM();
		this.appearance = new Appearance(player);
	}

	@Override
	public String getName() 
	{
		return name;
	}

	@Override
	public String getAccountName() 
	{
		return accountName;
	}

	@Override
	public int getObjectId() 
	{
		return objectId;
	}

	@Override
	public String getIp() 
	{
		return ip;
	}

	@Override
	public boolean isGM() {
		return isGM;
	}

	@Override
	public void logout() 
	{
		player.logout();
	}

	@Override
	public void sendBoardMessage(String html) 
	{
		if (html.length() < 4090)
		{
			player.sendPacket(new ShowBoard(html, "101"));
			player.sendPacket(new ShowBoard(null, "102"));
			player.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, html.length()), "102"));
			player.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < 12270)
		{
			player.sendPacket(new ShowBoard(html.substring(0, 4090), "101"));
			player.sendPacket(new ShowBoard(html.substring(4090, 8180), "102"));
			player.sendPacket(new ShowBoard(html.substring(8180, html.length()), "103"));		
		}
	}

	@Override
	public void sendHtmlMessage(String html) 
	{
		NpcHtmlMessage message = new NpcHtmlMessage(5);
		message.setHtml(html);
		player.sendPacket(message);
	}

	@Override
	public void sendMessage(String message) 
	{
		player.sendMessage(message);
	}

	@Override
	public void addItem(String method, int id, int count, boolean message) 
	{
		player.addItem(method, id, count, null, message);
	}

	@Override
	public int getHeading() 
	{
		return player.getHeading();
	}

	@Override
	public void getItemByItemId(int arg0) 
	{
		
	}

	@Override
	public int getX() 
	{
		return player.getX();
	}

	@Override
	public int getY() 
	{
		return player.getY();
	}

	@Override
	public int getZ() 
	{
		return player.getZ();
	}

	@Override
	public void destroyItemById(String method, int id, int count, boolean message) 
	{
		L2ItemInstance item = player.getInventory().getItemByItemId(id);
		
		if (item == null || item.getCount() < count)
		{
			sendMessage("You don't have enough!");
			return;
		}
		
		player.destroyItemByItemId(method, id, count, null, message);
	}

	@Override
	public int getFirstBloods() 
	{
		return firstBloods;
	}

	@Override
	public int getKills() 
	{
		return kills;
	}

	@Override
	public int getMaxKills() 
	{
		return maxKills;
	}

	@Override
	public int getPkKills() 
	{
		return player.getPkKills();
	}

	@Override
	public int getPvpKills() 
	{
		return player.getPvpKills();
	}

	@Override
	public IPlayer getTarget() 
	{
		L2Object target = player.getTarget();
		
		if (target == null)
			return null;
		
		if (target instanceof L2PcInstance)
		{
			IPlayer p = PlayerController.getInstance().getPlayer(((L2PcInstance)target).getObjectId());
			return p;
		}
		else
			return null;
	}

	@Override
	public void increaseFirstBloods() 
	{
		firstBloods++;
	}

	@Override
	public void increaseKills() 
	{
		kills++;
	}

	@Override
	public void increaseMaxKills() 
	{
		maxKills++;
	}

	@Override
	public void resetKills() 
	{
		kills = 0;
	}

	@Override
	public void setFirstBloods(int num) 
	{
		firstBloods = num;
	}

	@Override
	public void setMaxKills(int num) 
	{
		maxKills = num;
	}

	@Override
	public void enterObserverMode(int x, int y, int z) 
	{
		Location loc = new Location(x, y, z);
		player.enterObserverMode(loc);
	}

	@Override
	public int getPvpFlag() 
	{
		return player.getPvpFlag();
	}

	@Override
	public boolean isInCombat() 
	{
		return player.isInCombat();
	}

	@Override
	public boolean isInJail() 
	{
		return player.isJailed();
	}

	@Override
	public boolean isInOlympiadMode() 
	{
		return player.isInOlympiadMode();
	}

	@Override
	public boolean isInPeaceZone() 
	{
		if (player.isInsideZone(ZoneId.PEACE))
			return true;
		
		return false;
	}

	@Override
	public boolean isParalyzed() 
	{
		return player.isParalyzed();
	}

	@Override
	public void broadcastStatusUpdate() 
	{
		player.broadcastStatusUpdate();
	}

	@Override
	public void broadcastTitleInfo() 
	{
		player.broadcastTitleInfo();
	}

	@Override
	public void broadcastUserInfo() 
	{
		player.broadcastUserInfo();
	}

	@Override
	public int getKarma() 
	{
		return player.getKarma();
	}

	@Override
	public int getLevel() 
	{
		return player.getLevel();
	}

	@Override
	public boolean isDead() 
	{
		return player.isDead();
	}

	@Override
	public boolean isHero() 
	{
		return player.isHero();
	}

	@Override
	public boolean isInDuel() 
	{
		return player.isInDuel();
	}

	@Override
	public boolean isInParty() 
	{
		return player.isInParty();
	}

	@Override
	public boolean isNoble() 
	{
		return player.isNoble();
	}

	@Override
	public boolean isOnline() 
	{
		return player.isOnline();
	}

	@Override
	public void setKarma(int val) 
	{
		player.setKarma(val);
	}

	@Override
	public void setTitle(String title) 
	{
		player.setTitle(title);
		broadcastTitleInfo();
	}

	@Override
	public IAppearance getAppearance() 
	{
		return appearance;
	}
}
