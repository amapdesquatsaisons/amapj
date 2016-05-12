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
 package fr.amapj.service.services.meslivraisons;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.amapj.service.services.saisiepermanence.PermanenceDTO;

/**
 * Informations sur les contrats d'un utilisateur
 *
 */
public class JourLivraisonsDTO
{
	public Date date;
	
	public List<ProducteurLivraisonsDTO> producteurs = new ArrayList<ProducteurLivraisonsDTO>();
	
	// Si non null : indique que l'utilisateur doit réaliser la distribution 	
	public PermanenceDTO distribution;

}
