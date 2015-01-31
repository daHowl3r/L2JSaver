package com.l2jsaver.features.RaidbossManager;

import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.controllers.VoicedHandlerController;
import com.l2jsaver.features.RaidbossManager.Controllers.RaidbossController;
import com.l2jsaver.features.RaidbossManager.Controllers.RaidbossPagingController;
import com.l2jsaver.features.RaidbossManager.Handlers.RaidBossHandler;
import com.l2jsaver.interfaces.IExtension;
import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;

public class RaidBossManager implements IExtension
{
	private int[] bossesId = { 25283, 25286, 29095, 25450 };
	
	public RaidBossManager()
	{
		SaverController.getInstance().getLogger().info("Loading Raidboss Manager");
		VoicedHandlerController.getInstance().registerVoicedCommandHandler(new RaidBossHandler());
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
	public void onLogin(IPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout(IPlayer arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNpcKill(IPlayer arg0, IMonster arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPlayerKill(IPlayer arg0, IPlayer arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadExtension() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stopExtension() {
		// TODO Auto-generated method stub
		
	}

}
