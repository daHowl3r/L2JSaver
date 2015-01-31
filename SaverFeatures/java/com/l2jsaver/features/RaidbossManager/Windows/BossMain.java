package com.l2jsaver.features.RaidbossManager.Windows;

import java.util.Collection;

import com.l2jsaver.corefeatures.L2JFrame.AbstractController;
import com.l2jsaver.corefeatures.L2JFrame.models.derived.L2JTablePane;
import com.l2jsaver.corefeatures.L2JFrame.models.primitive.L2JHorizontalPane;

public class BossMain extends L2JTablePane
{

	public BossMain(Collection<L2JHorizontalPane> list, int rowsPerPage, int page, AbstractController controller) 
	{
		super(list, rowsPerPage, page, controller);
	}
	
}
