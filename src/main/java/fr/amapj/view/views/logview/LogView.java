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
 package fr.amapj.view.views.logview;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Compare.GreaterOrEqual;
import com.vaadin.data.util.filter.Compare.Greater;
import com.vaadin.data.util.filter.Compare.Equal;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.DateField;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.saas.TypLog;
import fr.amapj.service.services.appinstance.LogAccessDTO;
import fr.amapj.service.services.logview.LogFileResource;
import fr.amapj.service.services.logview.LogViewService;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.tools.DateTimeToStringConverter;



/**
 * Page permettant de presenter la liste des utilisateurs
 * 
 *  
 *
 */
public class LogView extends VerticalLayout implements View, PopupListener
{

	private Table beanTable;
	BeanItemContainer<LogAccessDTO> listPartContainer;
	
	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{		
		listPartContainer = new BeanItemContainer<>(LogAccessDTO.class);
								
		// Bind it to a component
		beanTable = new Table("", listPartContainer);
		beanTable.setStyleName("big strong");
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("sudo" ,"nom" , "prenom" , "dbName" , "status" ,"typLog" , "ip" , "browser" , "dateIn" , "dateOut" , "nbError");
		
		beanTable.setColumnHeader("sudo","Sudo");
		beanTable.setColumnHeader("nom","Nom");
		beanTable.setColumnHeader("prenom","Prénom");
		beanTable.setColumnHeader("dbName","Base");
		beanTable.setColumnHeader("ip","Ip");
		beanTable.setColumnHeader("browser","Browser");
		beanTable.setColumnHeader("dateIn","Date connexion");
		beanTable.setColumnHeader("dateOut","Date déconnexion");
		beanTable.setColumnHeader("status","Etat");
		beanTable.setColumnHeader("typLog","Type");
		beanTable.setColumnHeader("nbError","Erreur");
		beanTable.setColumnAlignment("nbError",Align.CENTER);
		
		beanTable.setConverter("dateIn", new DateTimeToStringConverter());
		beanTable.setConverter("dateOut", new DateTimeToStringConverter());
		
		
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

		
		beanTable.addGeneratedColumn("Télécharger ...", new ColumnGenerator() 
		{ 
		    @Override
		    public Object generateCell(final Table source, final Object itemId, Object columnId) 
		    {
		    	LogAccessDTO dto = (LogAccessDTO) itemId;
		    	if(dto.logFileName==null)
		    	{
		    		Label l = new Label("Pas de fichier");
		    		return l;
		    	}
		    	LogFileResource logFileResource = new LogFileResource(dto.logFileName);
				
				Link extractFile = new Link("Télécharger le fichier de log",new StreamResource(logFileResource, dto.logFileName+".log"));
				return extractFile;
		    }
		});
		
		HorizontalLayout toolbar = new HorizontalLayout();
		
		
		Button resfresh = new Button("Rafraichir");
		resfresh.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				refresh();
			}
		});
		
		Button telechargerButton = new Button("Télécharger ...");
		telechargerButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleTelecharger();
			}
		});		
	
		
		
		Label title = new Label("Liste des logs");
		title.setSizeUndefined();
		title.addStyleName("h1");
		
		
		
		//
		TextField nomField = createFilterField("Nom","nom");
		TextField dbNameField = createFilterField("Base","dbName");
		TextField statusField = createFilterField("Etat","status");
		TextField typLogField = createTypLogField("Type","typLog");
		TextField ipField = createFilterField("Ip","ip");
		DateField dateInField = createDateFilterField("Date connexion","dateIn");
		TextField erreurField = createIntegerFilterField("Erreurs","nbError");
		
		
		toolbar.addComponent(resfresh);
		toolbar.addComponent(telechargerButton);
		
		toolbar.addComponent(nomField);
		toolbar.addComponent(dbNameField);
		toolbar.addComponent(statusField);
		toolbar.addComponent(typLogField);
		toolbar.addComponent(ipField);
		toolbar.addComponent(dateInField);
		toolbar.addComponent(erreurField);
		
		
		toolbar.addComponent(erreurField);
		
		toolbar.setWidth("100%");

		setMargin(true);
		setSpacing(true);
		
		addComponent(title);
		addComponent(toolbar);
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		setSizeFull();
		
		refresh();
		
		// Valeurs par défaut
		statusField.setValue("on");
		updateLikeFilter("on", "status");
		
		typLogField.setValue("user");
		updateTypLogFilter("user", "typLog");
		
	}
	
	private void handleTelecharger()
	{
		CorePopup.open(new TelechargerLogPopup(),this);
		
	}
	
	private DateField createDateFilterField(String inputPrompt, final String property)
	{
		DateField searchField = new DateField();
		searchField.addValueChangeListener(new ValueChangeListener()
		{
			
			@Override
			public void valueChange(ValueChangeEvent event)
			{
				updateGreaterFilter(event.getProperty().getValue(),property);
				
			}
		});
		
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("90%");
		searchField.setImmediate(true);
		
		return searchField;
	}
	

	private TextField createIntegerFilterField(String inputPrompt, final String property)
	{
		TextField searchField = new TextField();
		searchField.setInputPrompt(inputPrompt);
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				updateGreaterOrEqualIntegerFilter(event.getText(),property);
			}
		});
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("90%");
		searchField.setImmediate(true);
		
		return searchField;
	}
	
	private TextField createTypLogField(String inputPrompt, final String property)
	{
		TextField searchField = new TextField();
		searchField.setInputPrompt(inputPrompt);
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				updateTypLogFilter(event.getText(),property);
			}
		});
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("90%");
		searchField.setImmediate(true);
		
		return searchField;
	}
	

	



	private TextField createFilterField(String inputPrompt, final String property)
	{
		TextField searchField = new TextField();
		searchField.setInputPrompt(inputPrompt);
		searchField.addTextChangeListener(new TextChangeListener()
		{

			@Override
			public void textChange(TextChangeEvent event)
			{
				updateLikeFilter(event.getText(),property);				
			}

		});
		searchField.addStyleName(ChameleonTheme.TEXTFIELD_BIG);
		searchField.setWidth("90%");
		searchField.setImmediate(true);
		
		return searchField;
	}

	protected void refresh()
	{
		List<LogAccessDTO> us = new LogViewService().getLogs();
		listPartContainer.removeAllItems();
		listPartContainer.addAll(us);
		
	}
	
	
	/*
	 * Gestion des filtres
	 */
	
	private void updateGreaterFilter(Object value, String property)
	{
		Filter f = null;
		if (value != null)
		{
			f = new Greater(property, value);
		}
		updateFilter(f,property);
	}
	
	private void updateTypLogFilter(String textFilter,String property)
	{
		Filter f = null;	
		
		TypLog ref = null;
		
		if (textFilter!=null)
		{
			if (textFilter.toLowerCase().startsWith("u"))
			{
				ref = TypLog.USER;
			}
			if (textFilter.toLowerCase().startsWith("d"))
			{
				ref = TypLog.DEAMON;
			}
		}
		
		
		if (ref!=null)
		{
			f = new Equal(property, ref);
		}
		
		updateFilter(f,property);
		
	}
	
	
	private void updateGreaterOrEqualIntegerFilter(String textFilter,String property)
	{
		Filter f = null;	
		int nb = 0;
		
		try
		{
			nb = Integer.parseInt(textFilter);
		} 
		catch (NumberFormatException e)
		{
			// Do nothing
		}
		
		
		if (nb>0)
		{
			f = new GreaterOrEqual(property, nb);
		}
		
		updateFilter(f,property);
		
	}
	
	
	
	private void updateLikeFilter(String textFilter,String property)
	{
		Filter f = null;
		if (textFilter != null && !textFilter.equals(""))
		{
			f = new Like(property, textFilter + "%", false);
		}
		
		updateFilter(f,property);
		
	}
	
	
	
	
	private Map<String,Filter> filters = new HashMap<>();

	private void updateFilter(Filter newFilter,String filterName)
	{
		Filter f = filters.get(filterName);
		if (f!=null)
		{
			listPartContainer.removeContainerFilter(f);
			filters.remove(filterName);
		}
		
		if (newFilter!=null)
		{
			listPartContainer.addContainerFilter(newFilter);
			filters.put(filterName,newFilter);
		}
	}
	
	
	

	@Override
	public void onPopupClose()
	{
		refresh();
	}

	
	
	

}
