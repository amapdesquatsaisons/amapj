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
 package fr.amapj.view.views.gestioncontratsignes;

import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.gestioncontratsigne.ContratSigneDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.popup.okcancelpopup.OKCancelPopup;
import fr.amapj.view.engine.tools.BaseUiTools;

/**
 * Popup pour la saisie des avoirs d'un utilisateur
 *  
 */
@SuppressWarnings("serial")
public class PopupSaisieAvoir extends OKCancelPopup
{
	
	private TextField textField;
	
	private ContratSigneDTO contratSigneDTO;
	
	/**
	 * 
	 */
	public PopupSaisieAvoir(ContratSigneDTO contratSigneDTO)
	{
		this.contratSigneDTO = contratSigneDTO;
		popupTitle = "Saisie de l'avoir";
	}
	
	
	@Override
	protected void createContent(VerticalLayout contentLayout)
	{
		FormLayout f = new FormLayout();
		
		
		textField = BaseUiTools.createCurrencyField("Montant de l'avoir",false);
		
		textField.setConvertedValue(new Integer(contratSigneDTO.mntAvoirInitial));
		

		textField.addStyleName("align-center");
		textField.addStyleName("big");
		
		
		String message = "<h3> Saisie d'un avoir  pour "+contratSigneDTO.prenomUtilisateur+" "+contratSigneDTO.nomUtilisateur+"</h3>";
		
		f.addComponent(new Label(message, ContentMode.HTML));
		f.addComponent(textField);
		contentLayout.addComponent(f);
		
		
		
	}

	protected boolean performSauvegarder()
	{
		int qte = 0;
		try
		{
			Integer val = (Integer) textField.getConvertedValue();
			
			if (val != null)
			{
				qte = val.intValue();
			}
		}
		catch (ConversionException e)
		{
			Notification.show("Erreur de saisie");
			return false;
		}
				
		new MesContratsService().saveAvoirInitial(contratSigneDTO.idContrat,qte);
		return true;
	}

	
}
