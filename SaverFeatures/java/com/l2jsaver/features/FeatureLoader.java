package com.l2jsaver.features;

import com.l2jsaver.controllers.ExtensionController;
import com.l2jsaver.features.KillingSpree.KillingSpreeController;
import com.l2jsaver.features.RaidbossManager.RaidBossManager;
import com.l2jsaver.features.Test.Test;
import com.l2jsaver.features.TreasureHunting.TreasureHuntingManager;

public class FeatureLoader 
{
	private ExtensionController ext;
	
	public FeatureLoader()
	{
		ext = ExtensionController.getInstance();
		
		ext.addExtension(0, Test.getInstance());
		ext.addExtension(1, TreasureHuntingManager.getInstance());
		ext.addExtension(2, KillingSpreeController.getInstance());
		ext.addExtension(3, RaidBossManager.getInstance());
	}
	
	public static final FeatureLoader getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final FeatureLoader _instance = new FeatureLoader();
	}
}
