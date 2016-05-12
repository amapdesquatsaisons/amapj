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
 package fr.amapj.view.views.parametres.paramecran;

import com.vaadin.data.util.BeanItem;

import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.param.paramecran.AbstractParamEcran;
import fr.amapj.model.models.param.paramecran.PEListeAdherent;
import fr.amapj.service.services.parametres.ParamEcranDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.menu.MenuList;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;

/**
 * Permet la saisie des paramètres de l'écran "liste des adhérents"
 * 
 *
 */
@SuppressWarnings("serial")
public class PEListeAdherentEditorPart extends WizardFormPopup
{

	private PEListeAdherent pe;

	private boolean create;
	
	final static private MenuList menu = MenuList.LISTE_ADHERENTS;

	public enum Step
	{
		GENERAL ;
	}

	/**
	 * 
	 */
	public PEListeAdherentEditorPart()
	{
		
		ParamEcranDTO p = new ParametresService().getParamEcran(menu);
		
		this.create = (p==null);
		
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Paramètrage de l'écran \""+menu.getTitle()+"\"";
		
		if (create)
		{
			
			this.pe = new PEListeAdherent();
			this.pe.setMenu(menu);
		}
		else
		{
			this.pe = (PEListeAdherent) AbstractParamEcran.load(p);	
		}	
		
	
		
		item = new BeanItem<PEListeAdherent>(this.pe);

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
		setStepTitle("les droits d'accès sur cet écran");
		
		Enum[] enumsToExclude = new Enum[] { RoleList.MASTER };
		
		
		addComboEnumField("L'écran en entier est visible par ", "canAccessEcran", enumsToExclude,new NotNullValidator());
		
		
		addComboEnumField("La colonne e-mail est visible par ", "canAccessEmail", enumsToExclude,new NotNullValidator());
		
		addComboEnumField("La colonne Tel1 est visible par ", "canAccessTel1", enumsToExclude,new NotNullValidator());
		
		addComboEnumField("La colonne Tel2 est visible par ", "canAccessTel2", enumsToExclude,new NotNullValidator());
		
		addComboEnumField("Les 3 colonnes Adr, Ville, CodePostal sont visibles par ", "canAccessAdress", enumsToExclude,new NotNullValidator());
		
	
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
