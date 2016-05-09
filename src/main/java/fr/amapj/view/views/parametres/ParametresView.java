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
 package fr.amapj.view.views.parametres;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.param.EtatModule;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.views.parametres.paramecran.PEListeAdherentEditorPart;


/**
 * Page permettant à l'administrateur de modifier les paramètres généraux
 *  
 *
 */
public class ParametresView extends VerticalLayout implements View, PopupListener
{

	ParametresDTO dto;
	
	TextField nomAmap;
	TextField villeAmap;
	
	TextField sendingMailUsername;
	TextField url;
	TextField backupReceiver;
	
	TextField modulePlanningDistribution;
	TextField moduleGestionCotisation;
	TextField moduleEditionSpecifique;

	

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{

		GridLayout layout = new GridLayout(3, 12);
		layout.setWidth("80%");
		layout.setColumnExpandRatio(0, 0);
		
		
		// Une ligne vide
		addEmptyLine(layout);
		
		// 
		nomAmap = addLine(layout,"Nom de l'AMAP:");
		villeAmap = addLine(layout,"Ville de l'AMAP:");
		addEmptyLine(layout);
		
		sendingMailUsername = addLine(layout,"Adresse mail qui enverra les messages");
		url = addLine(layout,"URL de l'application utilisée dans les mails");
		backupReceiver = addLine(layout,"Adresse mail du destinataire des sauvegardes quotidiennes");
		addEmptyLine(layout);
		
		modulePlanningDistribution = addLine(layout, "Activation du module Planning de distribution");
		moduleGestionCotisation = addLine(layout, "Activation du module Gestion des cotisations");
		moduleEditionSpecifique = addLine(layout, "Activation du module Etiquettes et autres éditions spécifiques");
		
		
		addLabel(layout, "...");
		addLabel(layout," ");
		addLabel(layout," ");	
		
		
		addButton(layout, "Changer les paramètres",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleChangerParam();
			}
		});
		
		addLabel(layout," ");
		addLabel(layout," ");	
		addEmptyLine(layout);
		
		final PopupListener listener = this;
		
		addButton(layout, "Ecran \"Liste des adhérents\"",new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				CorePopup.open(new PEListeAdherentEditorPart(),listener);
			}
		});
		
		
		
		refresh();
		
		layout.setMargin(true);
		layout.setSpacing(true);
		layout.setSizeFull();
		addComponent(layout);
		
		
	}



	private void addEmptyLine(GridLayout layout)
	{
		for(int i=0;i<3;i++)
		{
			Label tf = new Label("<br/>",ContentMode.HTML);
			layout.addComponent(tf);
		}
	}



	private TextField addLine(GridLayout layout, String label)
	{
		addLabel(layout,label);
		TextField tf = addTextField(layout);
		addLabel(layout," ");
		return tf;
	}



	
	private void handleChangerParam()
	{
		FormPopup.open(new PopupSaisieParametres(dto),this);
		
	}
	

	
	
	private Label addLabel(GridLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;
		
	}

	private TextField addTextField(GridLayout layout)
	{
		TextField tf = new TextField();
		tf.setWidth("100%");
		tf.setNullRepresentation("");
		tf.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		tf.setEnabled(false);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private void addButton(GridLayout layout, String str,ClickListener listener)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(listener);
		layout.addComponent(b);
		
	}

	@Override
	public void onPopupClose()
	{
		refresh();
	}

	private void refresh()
	{
		dto = new ParametresService().getParametres();
		
		nomAmap.setValue(dto.nomAmap);
		villeAmap.setValue(dto.villeAmap);
		
		sendingMailUsername.setValue(dto.sendingMailUsername);
		url.setValue(dto.url);
		backupReceiver.setValue(dto.backupReceiver);
		
		modulePlanningDistribution.setValue(dto.etatPlanningDistribution.toString());
		moduleGestionCotisation.setValue(dto.etatGestionCotisation.toString());
		moduleEditionSpecifique.setValue(dto.etatEditionSpecifique.toString());
					
	}

}
