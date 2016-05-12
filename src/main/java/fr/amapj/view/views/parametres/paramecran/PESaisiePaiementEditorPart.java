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
 package fr.amapj.view.views.parametres.paramecran;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.model.models.param.paramecran.AbstractParamEcran;
import fr.amapj.model.models.param.paramecran.PESaisiePaiement;
import fr.amapj.service.services.parametres.ParamEcranDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.menu.MenuList;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;

/**
 * Permet la saisie des paramètres de l'écran "saisie des paiements par l'amapien"
 * 
 *
 */
@SuppressWarnings("serial")
public class PESaisiePaiementEditorPart extends WizardFormPopup
{

	private PESaisiePaiement pe;

	private boolean create;
	
	final static private MenuList menu = MenuList.OUT_SAISIE_PAIEMENT;

	public enum Step
	{
		GENERAL ;
	}

	/**
	 * 
	 */
	public PESaisiePaiementEditorPart()
	{
		
		ParamEcranDTO p = new ParametresService().getParamEcran(menu);
		
		this.create = (p==null);
		
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Paramètrage de l'écran \""+menu.getTitle()+"\"";
		
		if (create)
		{
			
			this.pe = new PESaisiePaiement();
			this.pe.setMenu(menu);
		}
		else
		{
			this.pe = (PESaisiePaiement) AbstractParamEcran.load(p);	
		}	
		
	
		
		item = new BeanItem<PESaisiePaiement>(this.pe);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;

		default:
			break;
		}
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("General");
		
		// TODO : à remettre apres le refactoring de PopupCurrencyVector
		//addComboEnumField("L'amapien peut modifier la proposition de paiement", "modificationPaiementPossible", new NotNullValidator());
		
		addComboEnumField(" Mode de calcul de la proposition de paiement", "modeCalculPaiement", new NotNullValidator());
		
		
		String msg = "Pour le calcul de la proposition pour les chèques, deux modes sont aujourd'hui disponibles :<br>"+	
					" <b>STANDARD:</b> Dans ce mode, les montants des chèques sont arrondis à l'euro, et sont quasi égaux.<br/>"
					+ "Exemple : la commande fait 100 € pour 3 mois, alors on propose 33 € pour le mois 1, puis 33 € pour le mois 2 puis 34 euros € pour le mois 3"
					+ "Ceci simplifie l'écriture des chèques<br/><br/>"+
					" <b>TOUS_EGAUX:</b> Dans ce mode, les montants des chèques sont strictement égaux, sans arrondi."
					+ "Exemple : la commande fait 20.40 € pour 2 mois, alors on propose 10.20 € pour le mois 1, puis 10.20 € pour le mois 2";
		
		addLabel(msg, ContentMode.HTML);
		
		//
		addCurrencyField("Montant minimum pour les chèques pour le calcul de la proposition", "montantChequeMiniCalculProposition", false);


		msg = "Ce montant minimum est utilisé pour le calcul de la proposition (mais pas pour la vérification si l'amapien change les montants) :<br>"+	
				" <b>Dans le mode STANDARD:</b> Si la commande fait 40 € pour 4 mois et le montant mini est 20€, alors on propose 20 € , 0 € , 20 € , 0€<br/><br/>"+
				" <b>Dans le mode TOUS_EGAUX:</b>Pas de prise en compte du montant minimum.";
	
		addLabel(msg, ContentMode.HTML);

		
		
	}


	

	@Override
	protected void performSauvegarder()
	{
		ParamEcranDTO peDTO = pe.save();
		new ParametresService().update(peDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
