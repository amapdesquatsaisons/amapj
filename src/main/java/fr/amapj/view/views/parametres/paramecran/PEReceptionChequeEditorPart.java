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

import fr.amapj.model.models.param.paramecran.AbstractParamEcran;
import fr.amapj.model.models.param.paramecran.PEReceptionCheque;
import fr.amapj.service.services.parametres.ParamEcranDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.menu.MenuList;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.popup.formpopup.validator.StringLengthValidator;

/**
 * Permet la saisie des paramètres de l'écran "réception des chèques"
 * 
 *
 */
@SuppressWarnings("serial")
public class PEReceptionChequeEditorPart extends WizardFormPopup
{

	private PEReceptionCheque pe;

	private boolean create;
	
	final static private MenuList menu = MenuList.RECEPTION_CHEQUES;

	public enum Step
	{
		AVOIR , COMMENTAIRE ;
	}

	/**
	 * 
	 */
	public PEReceptionChequeEditorPart()
	{
		
		ParamEcranDTO p = new ParametresService().getParamEcran(menu);
		
		this.create = (p==null);
		
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Paramètrage de l'écran \""+menu.getTitle()+"\"";
		
		if (create)
		{
			
			this.pe = new PEReceptionCheque();
			this.pe.setMenu(menu);
		}
		else
		{
			this.pe = (PEReceptionCheque) AbstractParamEcran.load(p);	
		}	
		
	
		
		item = new BeanItem<PEReceptionCheque>(this.pe);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case AVOIR:
			addFieldAvoir();
			break;

		case COMMENTAIRE:
			addFieldCommentaire();
			break;


		default:
			break;
		}
	}

	private void addFieldAvoir()
	{
		// Titre
		setStepTitle("La saisie des avoirs");
		
		addComboEnumField("Autoriser la saisie d'un avoir négatif", "saisieAvoirNegatif", new NotNullValidator());
		
	}

	
	
	private void addFieldCommentaire()
	{
		// Titre
		setStepTitle("Autoriser la saisie de commentaires lors de la réception des chèques (exemple : saisir le nom de la banque, saisir le numéro des chèques)");
		
		// Liste des validators
		IValidator len_1_100 = new StringLengthValidator(1, 255);
		
		
		addComboEnumField("Saisie d'un commentaire 1", "saisieCommentaire1", new NotNullValidator());
		addTextField("Libellé du commentaire 1", "libSaisieCommentaire1",len_1_100);
		
		
		addComboEnumField("Saisie d'un commentaire 2", "saisieCommentaire2", new NotNullValidator());
		addTextField("Libellé du commentaire 2", "libSaisieCommentaire2",len_1_100);
		
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
