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
 package fr.amapj.view.views.cotisation.bilan;

import com.vaadin.data.util.BeanItem;

import fr.amapj.service.services.gestioncotisation.GestionCotisationService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationDTO;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet
 * 
 *
 */
@SuppressWarnings("serial")
public class PeriodeCotisationEditorPart extends WizardFormPopup
{

	private PeriodeCotisationDTO dto;
	
	private boolean create;

	public enum Step
	{
		GENERAL , PAIEMENT;
	}

	/**
	 * 
	 */
	public PeriodeCotisationEditorPart(boolean create,PeriodeCotisationDTO p)
	{
		this.create = create;
		
		popupWidth = "80%";
		popupHeight = "60%";
	
		
		if (create)
		{
			popupTitle = "Création d'une période de cotisation";
			this.dto = new PeriodeCotisationDTO();
		}
		else
		{
			popupTitle = "Modification d'une période de cotisation";
			this.dto = p;
		}	
		
	
		item = new BeanItem<PeriodeCotisationDTO>(this.dto);
		

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;
			
		case PAIEMENT:
			addFieldPaiement();
			break;


		default:
			break;
		}
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales de la période de cotisation");
		
		// Champ 1
		addTextField("Nom de la période", "nom");
		
		addDateField("Date de début de la période","dateDebut");
		
		addDateField("Date de fin de la période","dateFin");
		
		
		
	}
	
	private void addFieldPaiement()
	{
		// Titre
		setStepTitle("les informations sur le paiement de l'adhésion");
		
		addCurrencyField("Montant minimum","montantMini",false);
		
		addCurrencyField("Montant conseillé","montantConseille",false);
		
		addTextField("Ordre du chèque","libCheque");
		
		addTextAeraField("Texte explicatif pour le paiement","textPaiement");
		
		addDateField("Date de début des inscription","dateDebutInscription");
		
		addDateField("Date de fin des inscription","dateFinInscription");
		
		addDateField("Date de remise des chèques","dateRemiseCheque");
		
		
		

	}

	

	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		new GestionCotisationService().createOrUpdate(dto);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
