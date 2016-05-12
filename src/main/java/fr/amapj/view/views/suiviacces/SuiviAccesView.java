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
 package fr.amapj.view.views.suiviacces;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.suiviacces.ConnectedUserDTO;
import fr.amapj.service.services.suiviacces.SuiviAccesService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.template.BackOfficeView;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;



/**
 * Page permettant de presenter la liste des utilisateurs
 * 
 *  
 *
 */
public class SuiviAccesView extends BackOfficeView
{

	private Table beanTable;
	
	private TextField searchField;

	private String textFilter;
	
	BeanItemContainer<ConnectedUserDTO> listPartContainer;
	
	/**
	 * 
	 */
	@Override
	public void enterIn(ViewChangeEvent event)
	{
		listPartContainer = new BeanItemContainer<>(ConnectedUserDTO.class);
							
		// on trie par nom puis prenom
		listPartContainer.sort(new String[] { "nom" , "prenom"  }, new boolean[] { true, true });
			
		// Bind it to a component
		beanTable = createTable(listPartContainer);
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("nom" , "prenom" , "email" , "date" , "agent" ,"dbName");
		
		beanTable.setColumnHeader("nom","Nom");
		beanTable.setColumnHeader("prenom","Prénom");
		beanTable.setColumnHeader("email","E mail");
		beanTable.setColumnHeader("date","Date connexion");
		beanTable.setColumnHeader("agent","Browser");
		beanTable.setColumnHeader("dbName","Nom de la base");
		
		beanTable.setConverter("date", new DateTimeToStringConverter());
		
		
		beanTable.setSelectable(true);
		beanTable.setImmediate(true);

		beanTable.setSizeFull();

		beanTable.addItemClickListener(new ItemClickListener()
		{
			@Override
			public void itemClick(ItemClickEvent event)
			{
				if (event.isDoubleClick())
				{
					beanTable.select(event.getItemId());
				}
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout();
		
		
		Button sendMsg = new Button("Envoyer un message à tous");
		sendMsg.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				FormPopup.open(new PopupSaisieMessage());
			}
		});
		
		
		Button resfresh = new Button("Rafraichir");
		resfresh.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				refresh();
			}
		});
		
		toolbar.addComponent(sendMsg);
		toolbar.addComponent(resfresh);
	
		
		Label title = new Label("Liste des personnes connectées");
		title.setSizeUndefined();
		title.addStyleName("stdlistpart-text-title");	
		
		
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par le nom ou l'email");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("50%");
		
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
		addComponent(title);
		addComponent(toolbar);
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		
		
		refresh();
		
	}
	
	protected void refresh()
	{
		List<ConnectedUserDTO> us = new SuiviAccesService().getConnectedUser();
		listPartContainer.removeAllItems();
		listPartContainer.addAll(us);
		
	}

	private void updateFilters()
	{
		
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nom", textFilter + "%", false), new Like("prenom", textFilter + "%", false));
			listPartContainer.addContainerFilter(or);
		}
	}

	
	

}
