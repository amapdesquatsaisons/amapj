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
 package fr.amapj.view.views.editionspe;

import java.util.List;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.editionspe.EditionSpeDTO;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.TableTools;


/**
 * Gestion des étiquettes
 *
 */
public class EditionSpeListPart extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button editButton;


	private String textFilter;

	private BeanItemContainer<EditionSpeDTO> mcInfos;

	private Table cdesTable;

	public EditionSpeListPart()
	{
	}
	
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		buildMainArea();
	}
	

	private void buildMainArea()
	{
		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<EditionSpeDTO>(EditionSpeDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom" , "typEditionSpecifique"});
		cdesTable.setColumnHeader("nom","Nom");
		cdesTable.setColumnHeader("typEditionSpecifique","Type de l'édition");
		

		cdesTable.setSelectable(true);
		cdesTable.setImmediate(true);

		// Activation au desactivation des boutons delete et edit
		cdesTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				setModificationsEnabled(event.getProperty().getValue() != null);
			}

			private void setModificationsEnabled(boolean b)
			{
				enableButtonBar(b);
			}
		});

		cdesTable.setSizeFull();

		cdesTable.addItemClickListener(new ItemClickListener()
		{
			@Override
			public void itemClick(ItemClickEvent event)
			{
				if (event.isDoubleClick())
				{
					cdesTable.select(event.getItemId());
				}
			}
		});

		HorizontalLayout toolbar = new HorizontalLayout();
		
		
		Label title2 = new Label("Liste des éditions spécifiques");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer une nouvelle édition spécifique");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
			
		editButton = new Button("Modifier");
		editButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleEditer();

			}
		});	
		

		deleteButton = new Button("Supprimer");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();

			}
		});
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par nom");
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});

		
		toolbar.addComponent(newButton);
		toolbar.addComponent(editButton);
		toolbar.addComponent(deleteButton);
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
	
		addComponent(title2);
		addComponent(toolbar);
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}
	
	

	private void handleAjouter()
	{
		ChoixEditionSpecifiqueEditorPart.open(new ChoixEditionSpecifiqueEditorPart(), this);
	}

	

	protected void handleEditer()
	{
		EditionSpeDTO dto = (EditionSpeDTO) cdesTable.getValue();
		ChoixEditionSpecifiqueEditorPart.openEditorPart(dto, this);
	}

	protected void handleSupprimer()
	{
		EditionSpeDTO dto = (EditionSpeDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer l'édition spécifique "+dto.nom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new EditionSpeService().delete(idItemToSuppress);
	}




	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			mcInfos.addContainerFilter(new Like("nom", textFilter + "%", false));
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "nom"  };
		boolean[] sortAscending = new boolean[] { true } ;
		
		List<EditionSpeDTO> res = new EditionSpeService().getAllEtiquettes();
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		enableButtonBar(enabled);		
	}
	

	
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
	
	
	private void enableButtonBar(boolean enable)
	{
		deleteButton.setEnabled(enable);
		editButton.setEnabled(enable);
	}

}
