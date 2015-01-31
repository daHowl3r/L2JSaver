package com.l2jsaver.features.RaidbossManager.Controllers;

import java.util.Collection;

import javolution.util.FastList;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.corefeatures.L2JFrame.L2JFrame;
import com.l2jsaver.corefeatures.L2JFrame.models.L2JAlign;
import com.l2jsaver.corefeatures.L2JFrame.models.L2JColor;
import com.l2jsaver.corefeatures.L2JFrame.models.derived.L2JTablePane;
import com.l2jsaver.corefeatures.L2JFrame.models.derived.PagingController;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JButton;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JButton.L2JButtonType;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JHorizontalPane;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JMiniBar;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JRootPane;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JText;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JVerticalPane;
import com.l2jsaver.features.RaidbossManager.RaidBossManager;
import com.l2jsaver.features.RaidbossManager.Windows.BossMain;
import com.l2jsaver.interfaces.IBoss;
import com.l2jsaver.interfaces.IPlayer;

public class RaidbossPagingController extends PagingController
{
	private String s = "RaidBoss Manager<br>"+
			"____________________________________________<br>";
	
	@Override
	public boolean conditionsOk(IPlayer arg0) 
	{
		return false;
	}

	@Override
	public L2JRootPane getNewWindow(int page, IPlayer player) 
	{
		L2JRootPane panel = new L2JRootPane("Raidboss Manager");
		new L2JText(s).toPanel(panel);
		L2JVerticalPane pane = new L2JVerticalPane().toPanel(panel);
		
		L2JHorizontalPane pl = new L2JHorizontalPane(L2JAlign.Center).toPanel(pane);
		new L2JText("Name", 100).toPanel(pl);
		new L2JText("Status", 45).toPanel(pl);
		new L2JText("HP", 50).toPanel(pl);
		new L2JText("View", 20).toPanel(pl);
		
		L2JTablePane info = new BossMain(getInfo(), 6, page, this);
		panel.addGraphic(info);
		panel.addGraphic(info.getNavigationMenu());
		return panel;
	}
	
	public Collection<L2JHorizontalPane> getInfo()
	{
		FastList<L2JHorizontalPane> panels = new FastList<L2JHorizontalPane>();
		
		for (int id : RaidBossManager.getInstance().getBossesId())
		{
			IBoss rb = PlayerController.getInstance().getBoss(id);
			
			if (rb == null)
				continue;
			
			L2JHorizontalPane panelh = new L2JHorizontalPane(L2JAlign.Center);
			String status = "";
			L2JColor color;
			if (rb.getCurrentHp() < 0.5)
			{
				status = "Dead";
				color = L2JColor.Red;
			}
			else if (rb.isInCombat())
			{
				status = "Fighting";
				color = L2JColor.Yellow;
			}
			else
			{
				status = "Alive";
				color = L2JColor.Green;
			}
			new L2JText(L2JColor.BufferLink, L2JAlign.Left, rb.getName(), 100).toPanel(panelh);
			new L2JText(color, L2JAlign.Center, status, 45).toPanel(panelh);
			L2JMiniBar hp = new L2JMiniBar(L2JColor.Grey, (int)rb.getCurrentHp(), (int)rb.getMaxHp(), L2JAlign.Center, 50);
			panelh.addGraphic(hp);
			if (!rb.isDead())
			{
				L2JButton b2 = new L2JButton("", L2JButtonType.QUESTION_MARK_ICON, RaidbossController.getInstance(), L2JAlign.Center);
				b2.addArgument("observBoss."+rb.getBossId());
				b2.toPanel(panelh);
			}
			else
			{
				new L2JText("").toPanel(panelh);
			}
			panels.add(panelh);
		}
		return panels;
	}
	
	public RaidbossPagingController()
	{
		L2JFrame.getInstance().registerListener(this);
	}
	
	private volatile static RaidbossPagingController singleton;
	
	public static RaidbossPagingController getInstance()
	{
		if (singleton == null)
		{
			synchronized(RaidbossPagingController.class)
			{
				if (singleton == null)
					singleton = new RaidbossPagingController();
			}
		}
		return singleton;
	}

}
