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
 package fr.amapj.service.services.access;

import fr.amapj.view.engine.tools.TableItem;

/**
 * Permet la gestion des droits dans le fichier de base
 * 
 */
public class AdminTresorierDTO implements TableItem
{
	public Long id;
	
	public Long utilisateurId;
	

	public Long getId()
	{
		return id;
	}

	public void setId(Long id)
	{
		this.id = id;
	}

	public Long getUtilisateurId()
	{
		return utilisateurId;
	}

	public void setUtilisateurId(Long utilisateurId)
	{
		this.utilisateurId = utilisateurId;
	}

	
	
}