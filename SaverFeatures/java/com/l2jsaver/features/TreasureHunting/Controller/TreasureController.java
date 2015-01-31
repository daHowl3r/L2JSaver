/*
 * Authors: Issle, Howler, Matim
 * File: TreasureController.java
 */
package com.l2jsaver.features.TreasureHunting.Controller;

import java.util.Collection;
import java.util.concurrent.Future;

import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.features.TreasureHunting.TreasureHuntingManager;
import com.l2jsaver.features.TreasureHunting.Model.Treasure;
import com.l2jsaver.features.TreasureHunting.Model.TreasureUpdateTask;
import com.l2jsaver.interfaces.INpcHandler;
import com.l2jsaver.interfaces.IPlayer;

import javolution.util.FastMap;

/**
 * @author Issle
 *
 */
public class TreasureController 
{
	
	public static final int MAX_COUNT = Integer.MAX_VALUE;
	private static final int ITEM_ID = 57;
	private static final int NPC_ID = 18265;
	private FastMap<Integer, Treasure> byMob = new FastMap<Integer,Treasure>().shared();
	private FastMap<Integer, Treasure> byPlayer = new FastMap<Integer,Treasure>().shared();
	public static FastMap<Integer,Integer> antispam = new FastMap<Integer,Integer>().shared();
	private Future<?> task;
	
	public Collection<Treasure> getTreasures()
	{
		return byPlayer.values();
	}
	
	public Treasure getTreasureByMob(int mobId)
	{
		return byMob.get(mobId);
	}
	
	public Treasure getTreasureByPlayer(int playerId)
	{
		return byPlayer.get(playerId);
	}
	
	public void addTreasure(int mobId, int playerId, Treasure t)
	{
		byMob.put(mobId, t);
		byPlayer.put(playerId,t);
	}
	
	public void spawnTreasures()
	{
		int count = 0;
		for(Treasure t: byMob.values())
		{
			spawnNpc(0, t.getX(), t.getY(), t.getZ());
			count++;
		}
		System.out.println("Spawned "+ count+ " treasures.");
	}
	
	public void generateTreasure(IPlayer player, int count)
	{
		if(byPlayer.containsKey(player.getObjectId()))
		{
			player.sendMessage("You have already spawned your treasure.");
			return;
		}
		
		if(antispam.containsKey(player.getObjectId()))
		{
			player.sendMessage("You are spawning treasures way too fast. Wait 5 minutes.");
			if(!player.isGM())
				return;
		}
		Treasure t = new Treasure(player.getObjectId(),count, player.getName());
		
		t.setItemCount(count);
		player.destroyItemById("Treasure Hunting", ITEM_ID, count, true);
		
		t.setX(player.getX());
		t.setY(player.getY());
		t.setZ(player.getZ());
		t.setOwnerName(player.getName());
		t.setOwnerId(player.getObjectId());
		addTreasure(spawnNpc(player.getHeading(),player.getX(), player.getY(), player.getZ()), player.getObjectId(),t);
		antispam.put(player.getObjectId(), 0);
		TreasureHuntingManager.getInstance().insertTp(player.getObjectId(), player.getName(), count, player.getX(), player.getY(), player.getZ());
	}
	
	public int spawnNpc(int heading, int x, int y , int z)
	{
		INpcHandler npc = SaverController.getInstance().getNpcHandler();
		return npc.spawn(heading, x, y, z, NPC_ID);
	}
	
	private volatile static TreasureController singleton;

	private TreasureController() 
	{
		task = SaverController.getInstance().getEventHandler().scheduleGeneralAtFixedRate(new TreasureUpdateTask(),0, 600000);
	}

	public static TreasureController getInstance() 
	{
		if (singleton == null) {
			synchronized (TreasureController.class) 
			{
				if (singleton == null)
					singleton = new TreasureController();
			}
		}
		return singleton;
	}
	
	public void onReload() 
	{
		TreasureHuntingManager.getInstance().saveTh();
		if(task != null)
			task.cancel(true);
		unSpawnNpcs();
	}
	
	public FastMap<Integer, Treasure> getMobs()
	{
		return byMob;
	}
	
	public FastMap<Integer, Treasure> getPlayers()
	{
		return byPlayer;
	}
	
	public int getItemId()
	{
		return ITEM_ID;
	}
	
	public void unSpawnNpcs()
	{
		int count =0;
		for (int objectId : byMob.keySet())
		{
			INpcHandler npc = SaverController.getInstance().getNpcHandler();
			npc.unSpawn(objectId);
		}
		System.out.println("Treasure hunting: Unspawned "+ count+" NPC chests.");
	}
}
