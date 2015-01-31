package com.l2jsaver.managers;

import java.util.logging.Logger;

import com.l2jsaver.controllers.ExtensionController;
import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.features.FeatureLoader;
import com.l2jsaver.injector.CallbackWrapper;
import com.l2jsaver.interfaces.IBoss;
import com.l2jsaver.models.Boss;
import com.l2jserver.gameserver.instancemanager.RaidBossSpawnManager;
import com.l2jserver.gameserver.model.actor.instance.L2RaidBossInstance;

public class SaverAdapterManager 
{
	private static final Logger _log = Logger.getLogger(CallbackWrapper.class.getName());
	
	public SaverAdapterManager()
	{
		load();
	}
	
	public final void load()
	{
		_log.info("Loading Player Controller");
		PlayerController.getInstance();
		_log.info("Loading Saver Controller");
		SaverController.getInstance().setEventHandler(EventManager.getInstance());
		SaverController.getInstance().setLogger(LoggerManager.getInstance());
		SaverController.getInstance().setNpcHandler(NpcManager.getInstance());
		_log.info("Loading Extension Controller");
		ExtensionController.getInstance();
		
		_log.info("Loading Features");
		FeatureLoader.getInstance();

		for (L2RaidBossInstance rb : RaidBossSpawnManager.getInstance().getBosses().values())
		{
			IBoss boss = new Boss(rb);
			
			PlayerController.getInstance().addBoss(boss);
		}
	}
	
	public static final SaverAdapterManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final SaverAdapterManager _instance = new SaverAdapterManager();
	}
}
