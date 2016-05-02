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
 package fr.amapj.view.views.appinstance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import com.vaadin.ui.Notification;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.service.engine.appinitializer.AppInitializer;
import fr.amapj.service.services.appinstance.AppInstanceDTO;
import fr.amapj.service.services.appinstance.AppInstanceService;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableItem;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.engine.ui.AppConfiguration;


/**
 * Gestion des instances
 *
 */
public class AppInstanceBasicForm extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button startButton;
	private Button connectButton;
	private Button sqlButton;
	private Button saveButton;


	private String textFilter;

	private BeanItemContainer<AppInstanceDTO> mcInfos;

	private Table cdesTable;

	public AppInstanceBasicForm()
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
		mcInfos = new BeanItemContainer<AppInstanceDTO>(AppInstanceDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nomInstance", "dbms","dateCreation" ,"state" ,"nbUtilisateurs"});
		
		cdesTable.setColumnHeader("nomInstance","Nom");
		cdesTable.setColumnHeader("dbms","Dbms");
		cdesTable.setColumnHeader("dateCreation","Date de création");
		cdesTable.setColumnHeader("state","Etat");
		cdesTable.setColumnHeader("nbUtilisateurs","Nb utilisateurs");
		
		cdesTable.setConverter("dateCreation", new DateTimeToStringConverter());

		cdesTable.setSelectable(true);
		cdesTable.setMultiSelect(true);
		cdesTable.setImmediate(true);

		// Activation au desactivation des boutons delete et edit
		cdesTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				Set s = (Set) event.getProperty().getValue();
				setModificationsEnabled( s.size()>0);
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
		
		
		Label title2 = new Label("Liste des instances");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer une nouvelle instance");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		startButton = new Button("Changer l'état");
		startButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleStart();

			}
		});	
		
		connectButton = new Button("Se connecter ...");
		connectButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleConnect();

			}
		});	
		
		sqlButton = new Button("Requete SQL ...");
		sqlButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSql();

			}
		});	
		
		saveButton = new Button("Sauvegarder ...");
		saveButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSave();

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
		toolbar.addComponent(startButton);
		toolbar.addComponent(connectButton);
		toolbar.addComponent(sqlButton);
		toolbar.addComponent(saveButton);
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
		AppInstanceEditorPart.open(new AppInstanceEditorPart(), this);
	}
	
	private void handleStart()
	{
		List<AppInstanceDTO> dtos = getSelected();
		PopupEtatAppInstance.open(new PopupEtatAppInstance(dtos), this);
	}
	
	private void handleConnect()
	{
		List<AppInstanceDTO> dtos = getSelected();
		if (dtos.size()==1)
		{
			AppInstanceDTO dto = dtos.get(0);
			PopupConnectAppInstance.open(new PopupConnectAppInstance(dto), this);
		}
		else
		{
			Notification.show("Vous devez selectionner une et une seule instance");
		}
		
	}
	
	private void handleSql()
	{
		List<AppInstanceDTO> dtos = getSelected();
		PopupSqlAppInstance.open(new PopupSqlAppInstance(dtos), this);
	}
	
	private void handleSave()
	{
		List<AppInstanceDTO> dtos = getSelected();
		PopupSaveAppInstance.open(new PopupSaveAppInstance(dtos), this);
	}

	/**
	 * Retourne la liste des lignes selectionnées
	 * @return
	 */
	private List<AppInstanceDTO> getSelected()
	{
		List<AppInstanceDTO> res = new ArrayList<AppInstanceDTO>();
		Set s = (Set) cdesTable.getValue();
		res.addAll(s);
		return res;
	}


	protected void handleSupprimer()
	{
		List<AppInstanceDTO> dtos = getSelected();
		if (dtos.size()!=1)
		{
			Notification.show("Vous devez selectionner une et une seule instance");
			return ;
		}
		
		AppInstanceDTO dto = dtos.get(0);
		String text = "Etes vous sûr de vouloir supprimer l'instance "+dto.nomInstance+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new AppInstanceService().delete(idItemToSuppress);
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
		String[] sortColumns = new String[] { "nomInstance"  };
		boolean[] sortAscending = new boolean[] { true } ;
		
		List<AppInstanceDTO> res = new AppInstanceService().getAllInstances();
		boolean enabled = TableTools.updateTableMultiselect(cdesTable, res, sortColumns, sortAscending);
		
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
		startButton.setEnabled(enable);
		connectButton.setEnabled(enable);
		sqlButton.setEnabled(enable);
		saveButton.setEnabled(enable);
	}
	
	
	
}
