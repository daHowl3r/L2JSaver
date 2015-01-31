package com.l2jsaver.managers;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.interfaces.INpcHandler;
import com.l2jsaver.models.Monster;
import com.l2jserver.gameserver.data.xml.impl.NpcData;
import com.l2jserver.gameserver.datatables.SpawnTable;
import com.l2jserver.gameserver.model.L2Spawn;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2MonsterInstance;
import com.l2jserver.gameserver.model.actor.templates.L2NpcTemplate;

public class NpcManager implements INpcHandler
{

	@Override
	public int spawn(int heading, int x, int y , int z, int id) 
	{
		L2NpcTemplate template1;
		template1 = NpcData.getInstance().getTemplate(id);
		int objectId = 0;
		L2Spawn spawn;
		try {
			spawn = new L2Spawn(template1);
		
			spawn.setX(x);
			spawn.setY(y);
			spawn.setZ(z+4);
			spawn.setHeading(heading);
			spawn.setRespawnDelay(20000);
			spawn.doSpawn();
			
			SpawnTable.getInstance().addNewSpawn(spawn, false);
			spawn.init();
			L2Npc _lastNpcSpawn = spawn.getLastSpawn();
			_lastNpcSpawn.setCurrentHp(_lastNpcSpawn.getMaxHp());
			_lastNpcSpawn.setTitle("Treasure Hunting");
			_lastNpcSpawn.isAggressive();
			_lastNpcSpawn.decayMe();
			_lastNpcSpawn.spawnMe(spawn.getLastSpawn().getX(), spawn.getLastSpawn().getY(), spawn.getLastSpawn().getZ());
			
			objectId = _lastNpcSpawn.getObjectId();
			
			if (_lastNpcSpawn instanceof L2MonsterInstance)
			{
				L2MonsterInstance mon = (L2MonsterInstance) _lastNpcSpawn;
				Monster _mon = new Monster(mon);
				
				PlayerController.getInstance().addMonster(_mon);
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return objectId;
	}

	@Override
	public void unSpawn(int objectId) 
	{
		
		L2Npc npc = (L2Npc) L2World.getInstance().findObject(objectId);
		
		if(npc == null)
			return;
		
		L2Spawn spawn = npc.getSpawn();
		if (spawn != null)
		{
			spawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(spawn, true);
		}
		
		if (npc instanceof L2MonsterInstance)
		{
			L2MonsterInstance mon = (L2MonsterInstance) npc;
			
			PlayerController.getInstance().getMonsters().remove(mon.getObjectId());
		}
		
		npc.deleteMe();
	}
	
	public static final NpcManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final NpcManager _instance = new NpcManager();
	}

}
