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
 package fr.amapj.model.engine.dbms;

import java.sql.SQLException;

import fr.amapj.service.services.appinstance.AppInstanceDTO;
import fr.amapj.service.services.appinstance.AppState;

public interface DBMS
{

	/**
	 * Réalise l'initialisation du DBMS , si nécessaire
	 */
	public void startDBMS();

	/**
	 * Permet le démarrage d'une nouvelle base de données
	 * 
	 */
	public void startOneBase(String dbName);

	/**
	 * Permet l'arret d'une base de données
	 * 
	 */
	public void stopOneBase(String dbName);

	/**
	 * Permet l'arret du DBMS  
	 */
	public void stopDBMS();
	
	
	/**
	 * Permet d'enregistrer une base de données dans DbUtil, avec l'état indiqué
	 */
	public void registerDb(AppInstanceDTO dto,AppState state);
	
	
	/**
	 * Permet la création d'une nouvelle base de données
	 * 
	 * Cette méthode doit :
	 * - créer la base 
	 * - la démarrer 
	 * - l'enregistrer au niveau de DbUtil
	 * - la remplir avec les données indiquées
	 * 
	 * En fin de cette méthode , la base est à l'état démarrée 
	 * 
	 */
	public void createOneBase(AppInstanceDTO appInstanceDTO);
	
	/**
	 * Permet l'execution d'une requete SQL d'update ou de modification du schéma sur la base indiquée
	 */
	public void executeSqlCommand(String sqlCommand,AppInstanceDTO dto) throws SQLException;



}