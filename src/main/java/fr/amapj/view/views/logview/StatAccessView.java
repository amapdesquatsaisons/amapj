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

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.logview.LogViewService;
import fr.amapj.service.services.logview.StatAccessDTO;
import fr.amapj.view.engine.tools.DateToStringConverter;



/**
 * Page permettant de presenter les statistiques d'acces
 * 
 *  
 *
 */
@SuppressWarnings("serial")
public class StatAccessView extends VerticalLayout implements View
{

	private Table beanTable;
	BeanItemContainer<StatAccessDTO> listPartContainer;
	
	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{		
		listPartContainer = new BeanItemContainer<>(StatAccessDTO.class);
									
		// Bind it to a component
		beanTable = new Table("", listPartContainer);
		beanTable.setStyleName("big strong");
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("date" , "nbAcces" , "nbVisiteur" , "tempsTotal" );
		
		beanTable.setColumnHeader("date","Date");
		beanTable.setColumnHeader("nbAcces","Nb d'accès");
		beanTable.setColumnHeader("nbVisiteur","Nb de visiteurs différents");
		beanTable.setColumnHeader("tempsTotal","temps total en minutes");
				
		beanTable.setConverter("date", new DateToStringConverter());
		
		
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
				
		Button resfresh = new Button("Rafraichir");
		resfresh.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				refresh();
			}
		});
		
		
		Label title = new Label("Statistiques des accès");
		title.setSizeUndefined();
		title.addStyleName("h1");
		
		toolbar.addComponent(resfresh);
		
		toolbar.setWidth("100%");

		setMargin(true);
		setSpacing(true);
		
		addComponent(title);
		addComponent(toolbar);
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
		setSizeFull();
		
		refresh();
		
	}
	

	protected void refresh()
	{
		List<StatAccessDTO> us = new LogViewService().getStats();
		listPartContainer.removeAllItems();
		listPartContainer.addAll(us);
		
	}
	
	
	
	
	

}
