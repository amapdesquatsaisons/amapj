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
 package fr.amapj.view.views.gestioncontrat.editorpart;

import fr.amapj.model.engine.Mdm;

public enum FrequenceLivraison implements Mdm
{
	UNE_SEULE_LIVRAISON("Une seule livraison") ,
	UNE_FOIS_PAR_SEMAINE("Une fois par semaine") , 
	QUINZE_JOURS("Une fois tous les quinze jours") , 
	UNE_FOIS_PAR_MOIS("Une fois par mois") ,
	AUTRE ("Autre...") ;
	
	private String propertyId;   
	   
	FrequenceLivraison(String propertyId) 
    {
        this.propertyId = propertyId;
    }
    public String prop() 
    { 
    	return propertyId; 
    }
}
