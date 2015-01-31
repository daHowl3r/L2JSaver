package com.l2jsaver.features.RaidbossManager;

import com.l2jsaver.abstracts.AbstractExtension;
import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.controllers.VoicedHandlerController;
import com.l2jsaver.features.RaidbossManager.Controllers.RaidbossController;
import com.l2jsaver.features.RaidbossManager.Controllers.RaidbossPagingController;
import com.l2jsaver.features.RaidbossManager.Handlers.RaidBossHandler;

public class RaidBossManager extends AbstractExtension
{
	private int[] bossesId = { 25283, 25286, 29095, 25450 };
	
	public RaidBossManager()
	{
		super(1, "RaidBoss Manager");
		
		if (isDisabled())
			return;
		
		SaverController.getInstance().getLogger().info("Loading Raidboss Manager");
		VoicedHandlerController.getInstance().registerVoicedCommandHandler(RaidBossHandler.getInstance());
		RaidbossController.getInstance();
		RaidbossPagingController.getInstance();
	}
	
	private static volatile RaidBossManager singleton;
	
	public static RaidBossManager getInstance()
	{
		if (singleton == null)
		{
			synchronized(RaidBossManager.class)
			{
				if (singleton == null)
					singleton = new RaidBossManager();
			}
		}
		return singleton;
	}
	
	public int[] getBossesId()
	{
		return bossesId;
	}

	@Override
	public void saveExtensionData() 
	{
		VoicedHandlerController.getInstance().unregisterVoicedCommandHandler(RaidBossHandler.getInstance());
	}

}
