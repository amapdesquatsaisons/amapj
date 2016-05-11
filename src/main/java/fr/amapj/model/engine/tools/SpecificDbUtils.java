/*
 *  Copyright 2013-2015 AmapJ Team
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
 package fr.amapj.model.engine.tools;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.common.StackUtils;
import fr.amapj.model.engine.transaction.DataBaseInfo;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.service.services.appinstance.AppState;



/**
 * Utilitaires pour les autres bases
 * 
 */
public class SpecificDbUtils 
{
	private final static Logger logger = LogManager.getLogger();
	
	/**
	 * Permet d'executer une requete dans une base specifique
	 * 
	 * ATTENTION : ceci doit être executé / appelé  depuis du code en dehors de toute transaction !! 
	 * 
	 * @param deamonName
	 * @param deamon
	 */
	static public Object executeInSpecificDb(String dbName,SpecificDbImpl deamon)
	{
		DataBaseInfo dataBaseInfo = DbUtil.findDataBaseFromName(dbName);
		if (dataBaseInfo==null || dataBaseInfo.getState()!=AppState.ON)
		{
			throw new AmapjRuntimeException("La base est inconnue ou non active"); 
		}
		
		Object res = null;
	
		DbUtil.setDbForDeamonThread(dataBaseInfo);
		try
		{
			res = deamon.perform();
		}
		catch(Throwable t)
		{
			// Do noting - only log 
			logger.info("Erreur pour la base "+dataBaseInfo+"\n"+StackUtils.asString(t));
		}
		DbUtil.setDbForDeamonThread(null);
		
		return res;
	}
	
}
