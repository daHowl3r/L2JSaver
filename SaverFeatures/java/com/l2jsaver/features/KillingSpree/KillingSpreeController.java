package com.l2jsaver.features.KillingSpree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.l2jsaver.controllers.PlayerController;
import com.l2jsaver.controllers.SaverController;
import com.l2jsaver.controllers.VoicedHandlerController;
import com.l2jsaver.features.KillingSpree.Handler.KillingSpree;
import com.l2jsaver.interfaces.IExtension;
import com.l2jsaver.interfaces.ILogger;
import com.l2jsaver.interfaces.IMonster;
import com.l2jsaver.interfaces.IPlayer;

/**
 * @author Howler
 */
public class KillingSpreeController implements IExtension
{
	private static String CREATE  = "CREATE TABLE IF NOT EXISTS `killingSpree` (" +
									"`charId`  int(10) NOT NULL ," +
									"`bigKs`  smallint(5) NOT NULL DEFAULT 0 ," +
									"`timeFb`  smallint(5) NOT NULL DEFAULT 0, " +
									"PRIMARY KEY (`charId`)" +
									")ENGINE=MyISAM DEFAULT CHARSET=latin1;";
	
	private static String SAVE 	  = "UPDATE killingSpree SET bigKs=?,timeFb=? WHERE charId=?";
	private static String RESTORE = "SELECT bigKs, timeFb FROM killingSpree WHERE charId=?";
	private static String INSERT  = "INSERT INTO killingSpree(charId, bigKs, timeFb) VALUES (?,?,?)";

	private static String[] sayings;
	private static boolean firstBlood;
	private static ILogger _log = SaverController.getInstance().getLogger();
	
	private static boolean exist;
	
	public KillingSpreeController() 
	{
		createDb();
		VoicedHandlerController.getInstance().registerVoicedCommandHandler(new KillingSpree());
		
		firstBlood = true;
		sayings = new String[16];
		
		sayings[2]= " is on Double kill!";
		sayings[3]= " is on Triple kill!";
		sayings[5]= " is on Multi kill!";
		sayings[7]= " is Dominating!";
		sayings[9]= " is Unstoppable!";
		sayings[11]= " is WickedSick!";
		sayings[15]= " is God-Like!";
	}
	
	/**
	 * Create the Database for the killingspree, 
	 * so it can save some stat's for each player.
	 */
	private void createDb()
	{
		_log.info("[KillingSpree] Migrating the database");
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement statement = con.prepareStatement(CREATE);
			statement.execute();
			statement.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	
	/**
	 * Load the KillingSpree Table for each player so we can
	 * set their stat's to the that they had before the logout.
	 * @param actor
	 */
	public void LoadDb(IPlayer actor)
	{
		exist = false;
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement restore = con.prepareStatement(RESTORE);
			restore.setInt(1, actor.getObjectId());
			
			ResultSet rset = restore.executeQuery();
			while(rset.next())
			{
				actor.setMaxKills(rset.getInt("bigKs"));
				actor.setFirstBloods(rset.getInt("timeFb"));
				
				exist = true;
			}
			rset.close();
			restore.close();
			
			if (!exist)
				InsertDb(actor);
		}
		catch (Exception e)
		{
			_log.fatal("Failed loading characters killingspree values");
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
		
	/**
	 * Save the KillingSpree Database for each player stat,
	 * so we can use them when he will login again.
	 * @param actor
	 */
	public void SaveDb(IPlayer actor)
	{
		Connection con = null;
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement save = con.prepareStatement(SAVE);
			
			save.setInt(1, actor.getMaxKills());
			save.setInt(2, actor.getFirstBloods());
			save.setInt(3, actor.getObjectId());
			
			save.execute();
			save.close();
		}
		catch (Exception e)
		{
			_log.fatal("Failed store characters killingspree values " + e.getMessage());
		}
		finally
		{
			SaverController.getInstance().getEventHandler().closeConnection(con);
		}
	}
	
	// Insert no existing player in the database
	public void InsertDb(IPlayer actor)
	{
		Connection con = null;
		
		try
		{
			con = SaverController.getInstance().getEventHandler().getConnection();
			PreparedStatement statement = con.prepareStatement(INSERT);
			
			statement.setInt(1, actor.getObjectId());
			statement.setInt(2, 0);
			statement.setInt(3, 0);
			
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

	@Override
	public void reloadExtension() 
	{
		
	}

	@Override
	public void stopExtension() 
	{
		for (IPlayer p : PlayerController.getInstance().getPlayers().values())
		{
			SaveDb(p);
		}
	}

	@Override
	public void onLogin(IPlayer player) 
	{
		LoadDb(player);
	}

	@Override
	public void onLogout(IPlayer player) 
	{
		SaveDb(player);
	}

	@Override
	public void onPlayerKill(IPlayer playerA, IPlayer playerB) 
	{
		if(sayings == null)
		{
			System.out.println("You didnt initiate the data of killing spree, run load()");
			return;
		}
			
		// Removing Actor killing spree no matter if is pvp or not.
		playerB.resetKills();
			
		//if (actor.getPvpFlag() == 0)
		//return;
		// When server is fresh started first player kill will give him the first blood.
		if (firstBlood)
		{
			firstBlood = false;
			playerA.increaseFirstBloods();
			SaverController.getInstance().getEventHandler().announceToAll(playerA.getName() + " got first blood!");
		}
				
		// Increase Killer killing spree if is pvp.
		playerA.increaseKills();
		
		if (playerA.getKills() < sayings.length && sayings[playerA.getKills()] !=null)
			SaverController.getInstance().getEventHandler().announceToAll(playerA.getName() + sayings[playerA.getKills()]);
				
		// If totalKillingSpree smaller than kills then set as kills.
		if (playerA.getMaxKills() < playerA.getKills())
			playerA.setMaxKills(playerA.getKills());
	}

	@Override
	public void onNpcKill(IPlayer killer, IMonster killed) {
		// TODO Auto-generated method stub
		
	}
	
	public static final KillingSpreeController getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final KillingSpreeController _instance = new KillingSpreeController();
	}
}