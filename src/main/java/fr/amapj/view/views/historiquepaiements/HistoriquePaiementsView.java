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
 package fr.amapj.view.views.historiquepaiements;

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
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.mespaiements.MesPaiementsService;
import fr.amapj.service.services.mespaiements.PaiementHistoriqueDTO;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.template.BackOfficeView;
import fr.amapj.view.engine.tools.DateAsMonthToStringConverter;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;


/**
 * Page permettant de presenter la liste des paiements passés
 * 
 *  
 *
 */
public class HistoriquePaiementsView extends BackOfficeView
{

	private Table beanTable;
	
	private TextField searchField;

	private String textFilter;
	
	BeanItemContainer<PaiementHistoriqueDTO> listPartContainer;
	
	/**
	 * 
	 */
	@Override
	public void enterIn(ViewChangeEvent event)
	{
		listPartContainer = new BeanItemContainer<>(PaiementHistoriqueDTO.class);
		List<PaiementHistoriqueDTO> ps = new MesPaiementsService().getMesPaiements(SessionManager.getUserId()).paiementHistorique;
		listPartContainer.addAll(ps);
		
					
		// on trie par date prévue et nom du contrat
		listPartContainer.sort(new String[] { "datePrevu" , "nomContrat" }, new boolean[] { false, true });
			
		// Bind it to a component
		beanTable = createTable(listPartContainer);
		
		// Gestion de la liste des colonnes visibles
		beanTable.setVisibleColumns("nomProducteur", "nomContrat" , "datePrevu" , "dateReelle" , "montant");
		
		beanTable.setColumnHeader("nomProducteur","Producteur");
		beanTable.setColumnHeader("nomContrat","Contrat");
		beanTable.setColumnHeader("datePrevu","Mois de paiement prévu");
		beanTable.setColumnHeader("dateReelle","Date réelle de remise");
		beanTable.setColumnHeader("montant","Montant (en €)");
		beanTable.setColumnAlignment("montant",Align.RIGHT);
		
		beanTable.setConverter("datePrevu", new DateAsMonthToStringConverter());
		beanTable.setConverter("dateReelle", new DateToStringConverter());
		beanTable.setConverter("montant", new CurrencyTextFieldConverter());
		
	
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
		
	
		
		Label title = new Label("Liste des paiements passés");
		title.setSizeUndefined();
		title.addStyleName("stdlistpart-text-title");
		
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par le nom du contrat ou du producteur");
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

		
	}
	
	private void updateFilters()
	{
		
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like("nomProducteur", textFilter + "%", false), new Like("nomContrat", textFilter + "%", false));
			listPartContainer.addContainerFilter(or);
		}
	}

	
	

}
