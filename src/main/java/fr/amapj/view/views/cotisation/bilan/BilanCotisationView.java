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
 package fr.amapj.view.views.cotisation.bilan;

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
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.services.excelgenerator.EGBilanAdhesion;
import fr.amapj.service.services.gestioncotisation.GestionCotisationService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationDTO;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;


/**
 * Affichage de la liste des périodes de cotisation 
 *
 */
public class BilanCotisationView extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener
{

	private TextField searchField;

	private Button newButton;
	private Button deleteButton;
	private Button updateButton;
	private Button telechargerButton;


	private String textFilter;

	private BeanItemContainer<PeriodeCotisationDTO> mcInfos;

	private Table cdesTable;

	public BilanCotisationView()
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
		mcInfos = new BeanItemContainer<PeriodeCotisationDTO>(PeriodeCotisationDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nom", "nbAdhesion","mntTotalAdhesion" ,"nbPaiementDonnes" ,"nbPaiementARecuperer"});
		
		cdesTable.setColumnHeader("nom","Nom de la période");
		cdesTable.setColumnHeader("nbAdhesion","Nombre d'adhérents");
		cdesTable.setColumnAlignment("nbAdhesion",Align.RIGHT);
		cdesTable.setColumnHeader("mntTotalAdhesion","Montant total des adhésions (en €)");
		cdesTable.setColumnAlignment("mntTotalAdhesion",Align.RIGHT);
		cdesTable.setColumnHeader("nbPaiementDonnes","Nb de paiements réceptionnés");
		cdesTable.setColumnAlignment("nbPaiementDonnes",Align.RIGHT);
		cdesTable.setColumnHeader("nbPaiementARecuperer","Nb de paiements à récupérer");
		cdesTable.setColumnAlignment("nbPaiementARecuperer",Align.RIGHT);
		
		cdesTable.setConverter("mntTotalAdhesion", new CurrencyTextFieldConverter());
		

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
		
		
		Label title2 = new Label("Liste des périodes de cotisation");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Créer une période");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		updateButton = new Button("Modifier une période");
		updateButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleUpdate();

			}
		});	
		
		
		

		deleteButton = new Button("Supprimer une période");
		deleteButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSupprimer();

			}
		});
		
		
		telechargerButton = new Button("Télécharger ...");
		telechargerButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleTelecharger();

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
		toolbar.addComponent(updateButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(telechargerButton);
		
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
		PeriodeCotisationEditorPart.open(new PeriodeCotisationEditorPart(true,null), this);
	}
	
	private void handleUpdate()
	{
		PeriodeCotisationDTO dto = (PeriodeCotisationDTO) cdesTable.getValue();
		PeriodeCotisationEditorPart.open(new PeriodeCotisationEditorPart(false,dto), this);
	}	
	

	protected void handleSupprimer()
	{
		PeriodeCotisationDTO dto = (PeriodeCotisationDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer la période de cotisation "+dto.nom+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}

	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new GestionCotisationService().delete(idItemToSuppress);
	}

	
	private void handleTelecharger()
	{
		PeriodeCotisationDTO dto = (PeriodeCotisationDTO) cdesTable.getValue();
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGBilanAdhesion(dto.id));
		CorePopup.open(popup,this);
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
		
		List<PeriodeCotisationDTO> res = new GestionCotisationService().getAll();
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
		updateButton.setEnabled(enable);
		telechargerButton.setEnabled(enable);
	}
	
	
	
}
