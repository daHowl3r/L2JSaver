/*
 * Authors: Issle, Howler, Matim
 * File: TreasureHuntingManager.java
 */
package com.l2jsaver.features.TreasureHunting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jsaver.abstracts.AbstractExtension;
import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.controllers.VoicedHandlerController;
import com.l2jsaver.features.TreasureHunting.Controller.TreasureController;
import com.l2jsaver.features.TreasureHunting.Handlers.TreasureHandler;
import com.l2jsaver.features.TreasureHunting.Model.Treasure;
import com.l2jsaver.interfaces.ILogger;
import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;

/**
 * @author Howler
 *
 */
public class TreasureHuntingManager extends AbstractExtension
{
	private ILogger _log = SaverController.getInstance().getLogger();
	private String MIGRATE_TH = "CREATE TABLE IF NOT EXISTS `TreasureHunting` (" +
								"`ownerId`  int(10) NOT NULL DEFAULT 0 ," +
								"`ownerName`  varchar(35) NOT NULL DEFAULT 'None' ," +
								"`itemCount`  bigint(20) NOT NULL ," +
								"`x`  mediumint(9) NOT NULL DEFAULT 0 ," +
								"`y`  mediumint(9) NOT NULL DEFAULT 0 ," +
								"`z`  mediumint(9) NOT NULL DEFAULT 0 ," +
								"PRIMARY KEY (`ownerId`) );";	
	
	private String RESTORE_TH = "SELECT ownerId, ownerName, itemCount, x, y, z FROM TreasureHunting";
	private String INSERT_TH = "INSERT INTO TreasureHunting(ownerId, ownerName, itemCount, x, y, z) VALUES (?,?,?,?,?,?)";
	private String REMOVE_TH  = "DELETE FROM TreasureHunting WHERE ownerId=?";
	private String UPDATE_TH = "UPDATE TreasureHunting SET ownerName=?, itemCount=?, x=?, y=?, z=? WHERE ownerId=?";
	
	public void migrate()
	{
		_log.info("[Treasure Hunter] Migrating Database");
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement statement = con.prepareStatement(MIGRATE_TH);
			statement.execute();
			
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try { con.close(); } catch (Exception e) {}
		}
	}
	
	public void restoreTh()
	{
		_log.info("[Treasure Hunter] Loading Data");
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement restore = con.prepareStatement(RESTORE_TH);
			
			ResultSet rset = restore.executeQuery();
			while(rset.next())
			{
				Treasure t = new Treasure(rset.getInt("ownerId"), rset.getInt("itemCount"), rset.getString("ownerName"));
				t.setX(rset.getInt("x"));
				t.setY(rset.getInt("y"));
				t.setZ(rset.getInt("z"));
				TreasureController.getInstance().addTreasure(TreasureController.getInstance().spawnNpc(0, t.getX(), t.getY(), t.getZ()), t.getOwnerId(), t);// 1 Should be object id of monster - call spawn on each loop. and then add it.
			}
			rset.close();
			restore.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	
	public void saveTh()
	{
		_log.info("[Treasure Hunter] Saving Data");
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement save = con.prepareStatement(UPDATE_TH);
			
			for (Treasure t : TreasureController.getInstance().getTreasures())
			{
				save.setString(1, t.getOwnerName());
				save.setInt(2, t.getItemCount());
				save.setInt(3, t.getX());
				save.setInt(4, t.getY());
				save.setInt(5, t.getZ());
				save.setInt(6, t.getOwnerId());
				
				save.execute();
			}
			
			save.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	
	public synchronized void insertTp(int ownerId, String name, int itemCount, int x, int y, int z)
	{
		
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement statement = con.prepareStatement(INSERT_TH);
			
			statement.setInt(1, ownerId);
			statement.setString(2, name);
			statement.setInt(3, itemCount);
			statement.setInt(4, x);
			statement.setInt(5, y);
			statement.setInt(6, z);
			
			
			statement.executeUpdate();
			statement.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	
	public synchronized void removeTp(int ownerId)
	{
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement remove = con.prepareStatement(REMOVE_TH);
			remove.setInt(1, ownerId);
			
			remove.execute();
			remove.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	private volatile static TreasureHuntingManager singleton;

	public TreasureHuntingManager() 
	{
		super(3, "Treasure Hunting");
		
		if (isDisabled())
			return;
		
		migrate();
		restoreTh();
		TreasureController.getInstance();
		VoicedHandlerController.getInstance().registerVoicedCommandHandler(TreasureHandler.getInstance());
	}

	public static TreasureHuntingManager getInstance() {
		if (singleton == null) {
			synchronized (TreasureHuntingManager.class) {
				if (singleton == null)
					singleton = new TreasureHuntingManager();
			}
		}
		return singleton;
	}

	@Override
	public void saveExtensionData() 
	{
		TreasureController.getInstance().onReload();
		VoicedHandlerController.getInstance().unregisterVoicedCommandHandler(TreasureHandler.getInstance());
	}
	
	@Override
	public void onNpcKill(IPlayer killer, IMonster killed) 
	{
		
		if(!TreasureController.getInstance().getMobs().containsKey(killed.getObjectId()))
			return;
		
		Treasure t = TreasureController.getInstance().getMobs().get(killed.getObjectId());
		
		killer.addItem("Treasure Hunting", TreasureController.getInstance().getItemId(), t.getItemCount(), true);
		if(!killer.getName().equals(t.getOwnerName()))
			SaverController.getInstance().getEventHandler().announceToAll(killer.getName()+" found the treasure of "+t.getOwnerName());
		TreasureController.getInstance().getMobs().remove(killed.getObjectId());
		TreasureController.getInstance().getPlayers().remove(t.getOwnerId());
		TreasureHuntingManager.getInstance().removeTp(t.getOwnerId());
		
	}
}
