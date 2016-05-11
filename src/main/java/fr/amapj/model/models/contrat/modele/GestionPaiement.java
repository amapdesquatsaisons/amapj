/*
 *  Copyright 2013-2016 Emmanuel BRUN (contact@amapj.fr)
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
 /**
 * Permet de definir l'état d'un modele de contrat
 * 
 * 
 */
package fr.amapj.model.models.contrat.modele;

import fr.amapj.model.engine.Mdm;

public enum GestionPaiement implements Mdm
{
	// Pas de gestion des paiements
	NON_GERE("Pas de gestion des paiements"),
	
	// gestion standard
	GESTION_STANDARD("Gestion standard") ;
	
	private String propertyId;   
	   
	GestionPaiement(String propertyId) 
    {
        this.propertyId = propertyId;
    }
	
    public String prop() 
    { 
    	return propertyId; 
    }

}
