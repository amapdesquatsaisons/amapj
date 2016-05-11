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
 package fr.amapj.service.services.gestioncontrat;


import java.util.Date;

/**
 * Gestion des dates de livraison d'un modèle de contrat
 *
 */
public class DateModeleContratDTO
{
	public Date dateLiv;

	public Date getDateLiv()
	{
		return dateLiv;
	}

	public void setDateLiv(Date dateLiv)
	{
		this.dateLiv = dateLiv;
	}
	
	
	
	
}
