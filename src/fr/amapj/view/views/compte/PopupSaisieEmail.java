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
 package fr.amapj.view.views.compte;

import com.vaadin.data.util.ObjectProperty;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.moncompte.MonCompteService;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;

/**
 * Popup pour la saisie de la nouvelle adresse e mail
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieEmail extends FormPopup
{
		
	private UtilisateurDTO u;


	/**
	 * 
	 */
	public PopupSaisieEmail(UtilisateurDTO u)
	{
		popupTitle = "Changement de votre e-mail";
		this.u = u;
		
	}
	
	
	protected void addFields()
	{
		// Contruction de l'item
		item.addItemProperty("email", new ObjectProperty<String>(u.getEmail()));
		

		// Construction des champs
		addTextField("Votre nouvel e-mail", "email");
		
		
		
	}

	protected void performSauvegarder()
	{
		String newValue = (String) item.getItemProperty("email").getValue();
		new MonCompteService().setNewEmail(u.getId(),newValue);
	}

	
	

}
