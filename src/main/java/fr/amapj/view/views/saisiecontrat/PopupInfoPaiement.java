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
 package fr.amapj.view.views.saisiecontrat;



import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.mescontrats.InfoPaiementDTO;
import fr.amapj.service.services.mescontrats.MesContratsService;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.ModeSaisie;
import fr.amapj.view.views.saisiecontrat.SaisieContrat.SaisieContratData;

/**
 * Popup d'informations sur le paiement
 *  
 */
@SuppressWarnings("serial")
public class PopupInfoPaiement extends CorePopup
{
	private InfoPaiementDTO paiementDTO;
	
	private SaisieContratData data;
	
	
	public PopupInfoPaiement(SaisieContratData data)
	{
		super();
		this.data = data;
		this.paiementDTO = data.contratDTO.paiement;
		
		popupTitle = "Information sur les paiements pour le contrat "+data.contratDTO.nom;
		popupWidth ="50%";
	}

	protected void createContent(VerticalLayout contentLayout)
	{
		contentLayout.addComponent(new Label(paiementDTO.textPaiement));
		
		boolean readOnly = (data.modeSaisie == ModeSaisie.READ_ONLY); 
		
		if (readOnly==false)
		{
			String str = "<br/><br/>Veuillez maintenant cliquer sur Sauvegarder pour valider votre contrat, " +
				"ou sur Annuler si vous ne souhaitez pas conserver ce contrat<br/><br/>";
		
			contentLayout.addComponent(new Label(str,ContentMode.HTML));
		}
	}
	


	protected void createButtonBar()
	{
		if (data.modeSaisie == ModeSaisie.READ_ONLY)
		{
			Button ok = addDefaultButton("OK", new Button.ClickListener()
			{

				@Override
				public void buttonClick(ClickEvent event)
				{
					handleAnnuler();
				}
			});
		}
		else
		{
			Button saveButton = addDefaultButton("Sauvegarder", new Button.ClickListener()
			{
				@Override
				public void buttonClick(ClickEvent event)
				{
					handleSauvegarder();
				}
			});

			Button cancelButton = addButton("Annuler", new Button.ClickListener()
			{

				@Override
				public void buttonClick(ClickEvent event)
				{
					handleAnnuler();
				}
			});
		}

	}
	

	protected void handleAnnuler()
	{
		close();
	}
	
	
	public void handleSauvegarder()
	{
		if (data.modeSaisie!=ModeSaisie.FOR_TEST)
		{
			new MesContratsService().saveNewContrat(data.contratDTO,data.userId);
		}
		
		close();
	}
	
}