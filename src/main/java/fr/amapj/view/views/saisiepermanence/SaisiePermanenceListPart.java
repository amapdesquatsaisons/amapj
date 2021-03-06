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
 package fr.amapj.view.views.saisiepermanence;

import java.text.SimpleDateFormat;
import java.util.Date;
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

import fr.amapj.service.services.excelgenerator.EGPlanningPermanence;
import fr.amapj.service.services.saisiepermanence.PermanenceDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.template.BackOfficeView;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;


/**
 * Saisie des distributions
 *
 */
@SuppressWarnings("serial")
public class SaisiePermanenceListPart extends BackOfficeView implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button editButton;
	private Button planifButton;
	private Button rappelButton;
	private Button deleteListButton;
	

	private String textFilter;

	private BeanItemContainer<PermanenceDTO> mcInfos;

	private Table cdesTable;

	public SaisiePermanenceListPart()
	{
	}
	
	
	@Override
	public void enterIn(ViewChangeEvent event)
	{

		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<PermanenceDTO>(PermanenceDTO.class);
			
		// Bind it to a component
		cdesTable = createTable(mcInfos);
		
				
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "datePermanence", "utilisateurs" , "numeroSession"});
		cdesTable.setColumnHeader("datePermanence","Date");
		cdesTable.setColumnHeader("utilisateurs","Personnes de permanence");
		cdesTable.setColumnHeader("numeroSession","Numéro de la session");
		
		cdesTable.setConverter("datePermanence", new DateToStringConverter());
		

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
		toolbar.addStyleName("stdlistpart-hlayout-button");
		
		
		Label title2 = new Label("Planning des permanences");
		title2.setSizeUndefined();
		title2.addStyleName("stdlistpart-text-title");	
		
		newButton = new Button("Créer une nouvelle permanence");
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
		
		planifButton = new Button("Planifier");
		planifButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handlePlanification();
			}
		});
		
		rappelButton = new Button("Envoyer un rappel");
		rappelButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleRappel();
			}
		});
		
		deleteListButton = new Button("Supprimer plusieurs permanences");
		deleteListButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleDeleteList();
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
		toolbar.addComponent(planifButton);
		toolbar.addComponent(rappelButton);
		toolbar.addComponent(deleteListButton);
		
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		
	
		addComponent(title2);
		addComponent(toolbar);
		addComponent(LinkCreator.createLink(new EGPlanningPermanence(new Date())));
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}
	
	
	private void handlePlanification()
	{
		PopupPlanificationPermanence.open(new PopupPlanificationPermanence(),this);		
	}
	
	private void handleRappel()
	{
		PopupRappelPermanence.open(new PopupRappelPermanence(),this);		
	}
	
	

	private void handleAjouter()
	{
		PopupSaisiePermanence.open(new PopupSaisiePermanence(null), this);
	}

	

	protected void handleEditer()
	{
		PermanenceDTO dto = (PermanenceDTO) cdesTable.getValue();
		PopupSaisiePermanence.open(new PopupSaisiePermanence(dto), this);
	}

	protected void handleSupprimer()
	{
		PermanenceDTO dto = (PermanenceDTO) cdesTable.getValue();
		SimpleDateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");
		String text = "Etes vous sûr de vouloir supprimer la permanence du "+df1.format(dto.datePermanence)+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new PermanenceService().deleteDistribution(idItemToSuppress);
	}


	protected void handleDeleteList()
	{
		
		PopupDeletePermanence.open(new PopupDeletePermanence(), this);
	}



	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Like like = new Like("utilisateurs", "%" + textFilter + "%", false);
			mcInfos.addContainerFilter(like);
		}
	}
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = new String[] { "datePermanence" };
		boolean[] sortAscending = new boolean[] { true } ;
		
		List<PermanenceDTO> res = new PermanenceService().getAllDistributions();
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
