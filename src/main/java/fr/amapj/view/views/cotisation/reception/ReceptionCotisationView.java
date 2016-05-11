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
 package fr.amapj.view.views.cotisation.reception;

import java.util.ArrayList;
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
import fr.amapj.service.services.gestioncotisation.BilanAdhesionDTO;
import fr.amapj.service.services.gestioncotisation.GestionCotisationService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationUtilisateurDTO;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.tools.DateToStringConverter;
import fr.amapj.view.engine.tools.TableTools;
import fr.amapj.view.engine.widgets.CurrencyTextFieldConverter;
import fr.amapj.view.views.cotisation.PeriodeCotisationSelectorPart;


/**
 * Gestion de la réception des cotisations
 *
 */
public class ReceptionCotisationView extends VerticalLayout implements ComponentContainer , View ,  PopupSuppressionListener 
{

	private TextField searchField;

	private Button newButton;
	private Button receptionMasseButton;
	private Button deleteButton;
	private Button updateButton;
	private Button telechargerButton;

	private PeriodeCotisationSelectorPart periodeSelector;
	
	private String textFilter;

	private BeanItemContainer<PeriodeCotisationUtilisateurDTO> mcInfos;

	private Table cdesTable;

	public ReceptionCotisationView()
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
		periodeSelector = new PeriodeCotisationSelectorPart(this);
		
		// Lecture dans la base de données
		mcInfos = new BeanItemContainer<PeriodeCotisationUtilisateurDTO>(PeriodeCotisationUtilisateurDTO.class);
			
		// Bind it to a component
		cdesTable = new Table("", mcInfos);
		cdesTable.setStyleName("big strong");
		
		
		// Titre des colonnes
		cdesTable.setVisibleColumns(new String[] { "nomUtilisateur", "prenomUtilisateur","dateAdhesion" ,"dateReceptionCheque" ,
				"montantAdhesion" ,"etatPaiementAdhesion","typePaiementAdhesion"});
		
		cdesTable.setColumnHeader("nomUtilisateur","Nom ");
		cdesTable.setColumnHeader("prenomUtilisateur","Prénom");
		cdesTable.setColumnHeader("dateAdhesion","Date de l'adhésion");
		cdesTable.setColumnHeader("dateReceptionCheque","Date réception chéque");
		
		cdesTable.setColumnHeader("montantAdhesion","Montant adhésion (en €)");
		cdesTable.setColumnAlignment("montantAdhesion",Align.RIGHT);
		
		cdesTable.setColumnHeader("etatPaiementAdhesion","Etat du paiement");
		cdesTable.setColumnHeader("typePaiementAdhesion","Type de paiement");
		
		
		cdesTable.setConverter("montantAdhesion", new CurrencyTextFieldConverter());
		cdesTable.setConverter("dateAdhesion", new DateToStringConverter());
		cdesTable.setConverter("dateReceptionCheque", new DateToStringConverter());

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
				buttonBarEditMode(b);		
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
		
		
		Label title2 = new Label("Réception des cotisations");
		title2.setSizeUndefined();
		title2.addStyleName("h1");	
		
		newButton = new Button("Ajouter une cotisation");
		newButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAjouter();
			}
		});
		
		receptionMasseButton = new Button("Réceptionner en masse les cotisations");
		receptionMasseButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleReceptionnerMasse();
			}
		});
		
		
		
		updateButton = new Button("Réceptionner / Modifier une cotisation");
		updateButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleUpdate();

			}
		});	
		
		
		

		deleteButton = new Button("Supprimer une cotisation");
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
		toolbar.addComponent(receptionMasseButton);
		toolbar.addComponent(updateButton);
		toolbar.addComponent(deleteButton);
		toolbar.addComponent(telechargerButton);
		
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);

		addComponent(title2);
		addComponent(periodeSelector.getChoixPeriodeComponent());
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
		Long idPeriodeCotisation = periodeSelector.getPeriodeId();
		PopupAjoutCotisation ajoutCotisation = new PopupAjoutCotisation(idPeriodeCotisation);
		PopupAjoutCotisation.open(ajoutCotisation, this);
	}
	
	
	private void handleReceptionnerMasse()
	{
		Long idPeriodeCotisation = periodeSelector.getPeriodeId();
		PopupReceptionMasseCotisation masseCotisation = new PopupReceptionMasseCotisation(idPeriodeCotisation);
		PopupReceptionMasseCotisation.open(masseCotisation, this);
	}
	
	
	
	private void handleUpdate()
	{
		PeriodeCotisationUtilisateurDTO dto = (PeriodeCotisationUtilisateurDTO) cdesTable.getValue();
		PopupModifCotisation modifCotisation = new PopupModifCotisation(dto);
		PopupModifCotisation.open(modifCotisation, this);
		
	}


	private void handleSupprimer()
	{
		PeriodeCotisationUtilisateurDTO dto = (PeriodeCotisationUtilisateurDTO) cdesTable.getValue();
		String text = "Etes vous sûr de vouloir supprimer la cotisation "+dto.nomUtilisateur+" "+dto.prenomUtilisateur+" ?";
		SuppressionPopup confirmPopup = new SuppressionPopup(text,dto.id);
		SuppressionPopup.open(confirmPopup, this);		
	}

	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new GestionCotisationService().deleteAdhesion(idItemToSuppress);
	}

	
	private void handleTelecharger()
	{
		Long idPeriode = periodeSelector.getPeriodeId();
		TelechargerPopup popup = new TelechargerPopup();
		popup.addGenerator(new EGBilanAdhesion(idPeriode));
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
		String[] sortColumns = new String[] { "nom"  ,"prenom"};
		boolean[] sortAscending = new boolean[] { true , true} ;
		
		Long idPeriode = periodeSelector.getPeriodeId();
		
		List<PeriodeCotisationUtilisateurDTO> res = new ArrayList<>();
		if (idPeriode!=null)
		{
			BilanAdhesionDTO bilan = new GestionCotisationService().loadBilanAdhesion(idPeriode);
			res = bilan.utilisateurDTOs;
		}
		boolean enabled = TableTools.updateTable(cdesTable, res, sortColumns, sortAscending);
		
		if (idPeriode!=null)
		{
			buttonBarFull(true);
			buttonBarEditMode(enabled);
		}
		else
		{
			buttonBarFull(false);
		}
	}
	

	
	
	@Override
	public void onPopupClose()
	{
		refreshTable();
		
	}


	
	public void buttonBarFull(boolean enable)
	{
		newButton.setEnabled(enable);
		receptionMasseButton.setEnabled(enable);
		telechargerButton.setEnabled(enable);
		buttonBarEditMode(enable);
	}


	
	public void buttonBarEditMode(boolean enable)
	{
		deleteButton.setEnabled(enable);
		updateButton.setEnabled(enable);
	}
	
	
	
}
