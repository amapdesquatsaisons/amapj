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
 package fr.amapj.view.views.gestioncontrat.editorpart;

import com.vaadin.data.util.BeanItem;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.StringLengthValidator;
import fr.amapj.view.engine.popup.formpopup.validator.UniqueInDatabaseValidator;

/**
 * Permet de modifier l'entete du contrat, c'est à dire son nom
 * et la date limite d'inscription 
 */
@SuppressWarnings("serial")
public class ModifEnteteContratEditorPart extends FormPopup
{
	private ModeleContratDTO modeleContrat;
	
	/**
	 * 
	 */
	public ModifEnteteContratEditorPart(Long id)
	{
		popupTitle = "Modification d'un contrat";
		setWidth(80);
				
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);
		
	}
	
	protected void addFields()
	{
		IValidator uniq = new UniqueInDatabaseValidator(ModeleContrat.class,"nom",modeleContrat.id);
		
		IValidator len_1_100 = new StringLengthValidator(1, 100);
		IValidator len_1_255 = new StringLengthValidator(1, 255);
		
		// Champ 1
		addTextField("Nom du contrat", "nom",uniq,len_1_100);
		
		// Champ 2
		addTextField("Description du contrat", "description",len_1_255);
			
		// Champ 3
		addDateField("Date de fin des inscriptions","dateFinInscription");
	}
	
	


	protected void performSauvegarder()
	{
		// Sauvegarde du contrat
		new GestionContratService().updateEnteteModeleContrat(modeleContrat);
	}
	
}
