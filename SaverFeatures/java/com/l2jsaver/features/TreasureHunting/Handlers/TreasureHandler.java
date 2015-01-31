package com.l2jsaver.features.TreasureHunting.Handlers;

import com.l2jsaver.features.TreasureHunting.Controller.TreasureController;
import com.l2jsaver.features.TreasureHunting.Model.Treasure;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jsaver.interfaces.IVoicedCommandHandler;


public class TreasureHandler implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
	{
		"hide", "treasure"
	};
	
	
	public boolean useVoicedCommand(String command, IPlayer activeChar, String params)
	{
		if(command.equalsIgnoreCase("hide"))
		{
			if(params == null)
			{
				activeChar.sendMessage("Specify the amount you want to convert to a treasure. For example ?hide 5000");
				return false;
			}
			
			int count = 0;
			try{ count = Integer.parseInt(params);}catch(Exception e){activeChar.sendMessage("That is not a valid number.");return false;}
			
			if(count <=0 || count >= TreasureController.MAX_COUNT)
			{
				activeChar.sendMessage("Dont try to cheat ... ");
				return false;
			}
			TreasureController.getInstance().generateTreasure(activeChar, count);
		}
		else if(command.equalsIgnoreCase("treasure"))
		{
			Treasure t = TreasureController.getInstance().getTreasureByPlayer(activeChar.getObjectId());
			if(t != null)
			{
				activeChar.sendMessage("Your treasure has not been found by anyone yet and it is : "+String.valueOf(t.getItemCount()));
			}
			else 
			{
				activeChar.sendMessage("You do not have a treasure or your treasure has been found by some player.");
			}
			activeChar.sendMessage("There are "+TreasureController.getInstance().getTreasures().size() +" treasures hidden in the map.");
		}
		
		return false;
	}
	
	
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}
