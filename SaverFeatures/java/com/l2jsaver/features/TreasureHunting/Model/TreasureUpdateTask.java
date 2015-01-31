/*
 * Authors: Issle, Howler, Matim
 * File: TreasureUpdateTask.java
 */
package com.l2jsaver.features.TreasureHunting.Model;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.features.TreasureHunting.Controller.TreasureController;
import com.l2jsaver.interfaces.IPlayer;


/**
 * @author Issle
 *
 */
public class TreasureUpdateTask implements Runnable{

	public static double multiplier = 1.001;
	@Override
	public void run() {
		
		TreasureController.antispam.clear();
		for(Treasure t: TreasureController.getInstance().getTreasures())
		{
			int playerId = t.getOwnerId();
			long count = t.getItemCount();
			
			count*=multiplier;
			count++;
			
			if(count <=0 || count >= TreasureController.MAX_COUNT)
				continue;
			
			t.setItemCount((int) count);
			
			IPlayer player = PlayerController.getInstance().getPlayers().get(playerId);
			if(player != null)
				player.sendMessage("Your treasure is now "+String.valueOf(count)+" Festival Adena");
			
			
		}
		
	}

}
