/*
 * Authors: Issle, Howler, David
 * File: KillingSpree.java
 */
package com.l2jsaver.features.KillingSpree.Handler;

import com.l2jsaver.corefeatures.L2JFrame.L2JFrame;
import com.l2jsaver.features.KillingSpree.View.KillingSpreeView;
import com.l2jsaver.interfaces.IPlayer;
import com.l2jsaver.interfaces.IVoicedCommandHandler;

/**
 * @author Howler
 *
 */
public class KillingSpree implements IVoicedCommandHandler
{
	private static final String[] VOICED_COMMANDS =
    {
  	  "killstat"
    };

	@Override
	public boolean useVoicedCommand(String command, IPlayer activeChar, String params)
	{
		IPlayer target = activeChar.getTarget();
		
		if (target != null)
			L2JFrame.getInstance().send(activeChar, new KillingSpreeView(target));
		else
			L2JFrame.getInstance().send(activeChar, new KillingSpreeView(activeChar));
		return false;
	}

	@Override
	public String[] getVoicedCommandList()
	{
		return VOICED_COMMANDS;
	}
}