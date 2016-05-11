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
 package fr.amapj.view.engine.listpart;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.tools.TableItem;
import fr.amapj.view.engine.tools.TableTools;


/**
 * Ecran classique avec une liste d'elements 
 *
 */
@SuppressWarnings("serial")
abstract public class StandardListPart<T extends TableItem> extends VerticalLayout implements ComponentContainer , View ,  PopupListener
{

	private TextField searchField;

	private List<ButtonHandler> buttons = new ArrayList<ButtonHandler>();
	
	private String textFilter;

	private BeanItemContainer<T> mcInfos;

	protected Table cdesTable;
	
	private Class<T> beanClazz;

	public StandardListPart(Class<T> beanClazz)
	{
		this.beanClazz = beanClazz;
	}
	
	abstract protected String getTitle();
	
	abstract protected void drawButton();
	
	abstract protected void drawTable();
	
	abstract protected void addExtraComponent();
	
	abstract protected List<T> getLines();
	
	abstract protected String[] getSortInfos();
	
	abstract protected String[] getSearchInfos();
	
	
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		buildMainArea();
	}
	
	
	public void addButton(String label,ButtonType type, ListPartButtonListener listener)
	{
		Button newButton = new Button(label);
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				listener.handleButtonPressed();
			}
		});
		
		//
		ButtonHandler handler = new ButtonHandler();
		handler.button = newButton;
		handler.type = type;
		buttons.add(handler);
	}
	
	
	public void addSearchField(String label)
	{
		searchField = new TextField();
		searchField.setInputPrompt(label);
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				textFilter = event.getText();
				updateFilters();
			}
		});
	}
	
	

	private void buildMainArea()
	{
		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<T>(beanClazz);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		drawTable();
		

		cdesTable.setSelectable(true);
		cdesTable.setImmediate(true);

		// Activation au desactivation des boutons delete et edit
		cdesTable.addValueChangeListener(new Property.ValueChangeListener()
		{
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				buttonBarEditMode(event.getProperty().getValue() != null);	
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
		
		Label title2 = new Label("Liste des utilisateurs");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		drawButton();
		
		for (ButtonHandler handler : buttons) 
		{
			toolbar.addComponent(handler.button);	
		}
		
		if (searchField!=null)
		{
			toolbar.addComponent(searchField);
			toolbar.setWidth("100%");
			toolbar.setExpandRatio(searchField, 1);
			toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		}

		
	
		addComponent(title2);
		addComponent(toolbar);
		addExtraComponent();
		addComponent(cdesTable);
		setExpandRatio(cdesTable, 1);
		setSizeFull();
		setMargin(true);
		setSpacing(true);
		
		refreshTable();

	}



	private void updateFilters()
	{
		mcInfos.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			String[] searchInfos = getSearchInfos();
			Filter[] filters = new Filter[searchInfos.length];
			
			for (int i = 0; i < searchInfos.length; i++) 
			{
				String search = searchInfos[i];
				filters[i] = new Like(search, "%"+textFilter + "%", false);
			}
			Or or = new Or(filters);
			mcInfos.addContainerFilter(or);
		}
	}
	
	
	
	
	/**
	 * Permet de rafraichir la table
	 */
	public void refreshTable()
	{
		String[] sortColumns = getSortInfos(); 
		boolean[] sortAscending = new boolean[sortColumns.length];
		Arrays.fill(sortAscending,true);
		
		List<T> res = getLines();
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		buttonBarEditMode(enabled);		
	}

	
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}
	
	
	/**
	 * Permet d'activer ou de désactiver toute la barre des boutons
	 * 
	 */
	public void buttonBarFull(boolean enable)
	{
		for (ButtonHandler handler : buttons) 
		{
			handler.button.setEnabled(enable);
		}
	}
	
	/**
	 * Permet d'activer ou de désactiver les boutons de la barre 
	 * qui sont relatifs au mode édition, c'est à dire les boutons 
	 * Edit et Delete
	 */
	public void buttonBarEditMode(boolean enable)
	{
		for (ButtonHandler handler : buttons) 
		{
			if (handler.type==ButtonType.EDIT_MODE)
			{
				handler.button.setEnabled(enable);
			}
		}		
	}
	
	
	
}
