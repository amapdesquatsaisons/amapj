/*
 *  Copyright 2013-2014 AmapJ Team
 * 
 *  This file is part of AmapJ.
 *  
 *  AmapJ is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.

 *  AmapJ is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with AmapJ.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * 
 */
 package fr.amapj.view.engine.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.model.engine.dbms.DBMSConf;
import fr.amapj.model.engine.dbms.hsqlexternal.HsqlExternalDbmsConf;
import fr.amapj.model.engine.dbms.hsqlinternal.HsqlInternalDbmsConf;
import fr.amapj.service.engine.appinitializer.MockServletParameter;
import fr.amapj.service.engine.appinitializer.ServletParameter;
import fr.amapj.service.services.appinstance.AppInstanceDTO;

/**
 * Paramètres de configuration de l'application
 * 
 */
public class AppConfiguration
{
	private final static Logger logger = LogManager.getLogger();

	static private AppConfiguration mainInstance;

	static public AppConfiguration getConf()
	{
		if (mainInstance == null)
		{
			throw new RuntimeException("Vous devez d'abord charger les parametres avec la methode load");
		}
		return mainInstance;
	}

	/**
	 * Permet le chargement des parametres
	 */
	static public void load(ServletParameter param)
	{
		if (mainInstance != null)
		{
			throw new RuntimeException("Impossible de charger deux fois les parametres");
		}

		mainInstance = new AppConfiguration();
		mainInstance.loadInternal(param);
	}

	private AppConfiguration()
	{
	}

	// Est on est mode de test ?
	private String testMode;

	// Répertoire pour la sauvegarde de la base
	private String backupDirectory;
	
	// Nom de l'onglet dans le navigateur
	private String pageTitle;

	//
	private List<DBMSConf> dbmsConfs = new ArrayList<DBMSConf>();
	
	private AppInstanceDTO masterConf;

	private void loadInternal(ServletParameter param)
	{

		testMode = param.read("test");
		
		pageTitle = param.read("pageTitle", "AMAP");

		// TODO verifier que c'est bien un directory
		backupDirectory = param.read("database.backupdir");
		
		// TODO verifier que c'est bien un directory
		String logDir = param.read("logDir");
		AmapJLogManager.setLogDir(logDir);
		
		// Lecture des DBMS
		String dbmsList =  param.read("dbms");
		
		String[] dbmss = dbmsList.split(",");
		for (int i = 0; i < dbmss.length; i++)
		{
			String dbmsName = dbmss[i];
			DBMSConf dbmsConf = createDbmsConf(dbmsName,param);
			dbmsConf.load(param);
			dbmsConfs.add(dbmsConf);	
		}
		
		
		// Lecture de la base master
		masterConf = createMasterConf(param);
		
	}
	
	
	private AppInstanceDTO createMasterConf(ServletParameter param)
	{
		AppInstanceDTO dto = new AppInstanceDTO();
		
		dto.nomInstance = param.read("master.name");
		
		dto.dbUserName = param.read("master.user");
		
		dto.dbPassword = param.read("master.password");
		
		dto.dbms = param.read("master.dbms");
		
		return dto;
	}

	private DBMSConf createDbmsConf(String dbmsName, ServletParameter param)
	{
		DBMSConf res;
		
		String type =  param.read("dbms."+dbmsName+".type");
		
		if (type.equals("hsql_internal"))
		{
			res = new HsqlInternalDbmsConf(dbmsName);
		}
		else if (type.equals("hsql_external"))
		{
			res = new HsqlExternalDbmsConf(dbmsName);
		} 
		else
		{
			throw new AmapjRuntimeException("Le type <"+type+"> n'est pas reconnu ");
		}
		return res;
	}
	
	
	

	public String getTestMode()
	{
		return testMode;
	}

	public String getBackupDirectory()
	{
		return backupDirectory;
	}

	public String getPageTitle()
	{
		return pageTitle;
	}
	
	public List<DBMSConf> getDbmsConfs()
	{
		return dbmsConfs;
	}
	
	public AppInstanceDTO getMasterConf()
	{
		return masterConf;
	}

	/**
	 * Permet de créer une configuration pour les tests
	 * 
	 * ATTENTION : cette méthode doit être appelée uniquement par TestTools.init()
	 */
	public static void initializeForTesting()
	{
		mainInstance = new AppConfiguration();

		Properties prop = new Properties();
		
		prop.put("dbms", "he");
		prop.put("dbms.he.type", "hsql_external");
		prop.put("dbms.he.ip", "127.0.0.1");
		prop.put("dbms.he.port", "9001");
		 
		prop.put("master.dbms","he");
		prop.put("master.name","master");
		prop.put("master.user","SA");
		prop.put("master.password","");
		 
		prop.put("logDir","../logs/");

		MockServletParameter param = new MockServletParameter(prop);
		
		mainInstance.loadInternal(param);
		
		
	}
	
	
	/**
	 * Permet de créer une configuration pour les tests
	 * 
	 * ATTENTION : cette méthode doit être appelée uniquement par TestTools.init()
	 */
	public static void initializeForTestingInternalDb()
	{
		mainInstance = new AppConfiguration();

		Properties prop = new Properties();
		
		prop.put("dbms", "hi");
		prop.put("dbms.hi.type", "hsql_internal");
		prop.put("dbms.hi.port", "9001");
		prop.put("dbms.hi.dir", "c:/prive/dev/amapj/git/amapj-dev/amapj/db/data");
		
		 
		prop.put("master.dbms","hi");
		prop.put("master.name","master"); 
		
		prop.put("logDir","../logs/");
		


		MockServletParameter param = new MockServletParameter(prop);
		
		mainInstance.loadInternal(param);
		
		
	}

}
