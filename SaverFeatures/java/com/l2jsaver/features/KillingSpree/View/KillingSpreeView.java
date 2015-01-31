/*
 * Authors: Issle, Howler, David
 * File: KillingSpreeView.java
 */
package com.l2jsaver.features.KillingSpree.View;

import com.l2jsaver.corefeatures.L2JFrame.models.L2JAlign;
import com.l2jsaver.corefeatures.L2JFrame.models.L2JColor;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JRootPane;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JText;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JVerticalPane;
import com.l2jsaver.interfaces.IPlayer;

/**
 * @author Howler
 *
 */
public class KillingSpreeView extends L2JRootPane
{
	private L2JVerticalPane panel;
	
	public KillingSpreeView(IPlayer actor)
	{
		super();
		panel = new L2JVerticalPane();
		
		generateText(L2JColor.Blue, "Stat's for: " + actor.getName());
		generateText(L2JColor.Red, "Biggest KS: " + actor.getMaxKills());
		generateText(L2JColor.Red, "Total FB: " + actor.getFirstBloods());
		generateText(L2JColor.Green, "Pvp: " + actor.getPvpKills());
		generateText(L2JColor.Green, "PK: " + actor.getPkKills());
		
		addGraphic(panel);
	}
	
	public void generateText(L2JColor color, String text)
	{
		panel.addGraphic(new L2JText(color, L2JAlign.Center, text));
	}
}