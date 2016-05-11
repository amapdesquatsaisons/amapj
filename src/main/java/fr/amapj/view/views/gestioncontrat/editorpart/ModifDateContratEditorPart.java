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
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class ModifDateContratEditorPart extends WizardFormPopup
{
	private ModeleContratDTO modeleContrat;

	public enum Step
	{
		CHOIX_FREQUENECE, DATE_LIVRAISON;
	}

	/**
	 * 
	 */
	public ModifDateContratEditorPart(Long id)
	{
		setWidth(80);
		popupTitle = "Modification des dates de livraison d'un contrat";
		
		// Chargement de l'objet  à modifier
		modeleContrat = new GestionContratService().loadModeleContrat(id);
		
		item = new BeanItem<ModeleContratDTO>(modeleContrat);

	}
	
	
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case CHOIX_FREQUENECE:
			addFieldChoixFrequence();
			break;

		case DATE_LIVRAISON:
			addFieldDateLivraison();
			break;

		default:
			break;
		}
	}

	private void addFieldChoixFrequence()
	{
		// Titre
		setStepTitle("frequence des livraisons");
		
		//
		addComboEnumField("Fréquence des livraisons", "frequence");
		
		//
		addLabel("Nota : si vous souhaitez modifier uniquement une date ou deux dans la liste et ne pas tout recalculer,<br/>"
				+ "merci de choisir \"Autre ...\" dans la liste déroulante ci dessus.", ContentMode.HTML);

	}

	private void addFieldDateLivraison()
	{
		// Titre
		setStepTitle("les dates de livraison");
		
		if (modeleContrat.frequence==FrequenceLivraison.UNE_SEULE_LIVRAISON)
		{
			addDateField("Date de la livraison", "dateDebut");
		}
		else if (modeleContrat.frequence==FrequenceLivraison.AUTRE)
		{
			//
			CollectionEditor<DateModeleContratDTO> f1 = new CollectionEditor<DateModeleContratDTO>("Liste des dates", (BeanItem) item, "dateLivs", DateModeleContratDTO.class);
			f1.addColumn("dateLiv", "Date",FieldType.DATE, null);
			binder.bind(f1, "dateLivs");
			form.addComponent(f1);
		}
		else
		{
			addDateField("Date de la première livraison", "dateDebut");
			addDateField("Date de la dernière livraison", "dateFin");
		}
		

	}


	@Override
	protected void performSauvegarder()
	{
		new GestionContratService().updateDateModeleContrat(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	/**
	 * Vérifie si il n'y a pas déjà des contrats signés, qui vont empecher de modifier les dates
	 */
	@Override
	protected String checkInitialCondition()
	{
		int nbInscrits = new GestionContratService().getNbInscrits(modeleContrat.id);
		if (nbInscrits!=0)
		{
			String str = "Vous ne pouvez plus modifier les dates de livraison de ce contrat<br/>"+
						 "car "+nbInscrits+" adhérents ont déjà souscrits à ce contrat<br/>."+
						 "Deux cas sont possibles :<br/><ul>"+
						 "<li>Soit vous pouvez supprimer les contrats signés par les adhérents, car ce sont des données de test, ou ils ne sont plus valables. Dans ce cas, vous allez dans \"Gestion des contrats signés\", puis vous cliquez sur le bouton \"Supprimer un contrat signé\".</li>"+
						 "<li>Soit une date a été réellement annulée suite à un problème avec le producteur par exemple. Dans ce cas, vous allez dans \"Gestion des contrats signés\", puis vous cliquez sur le bouton \"Autre\"."+
						 "Un assistant vous aidera à gérer le cas où une ou plusieurs livraisons sont annulées.</li>"+
						 "</ul>";
			return str;
		}
		
		return null;
	}
}
