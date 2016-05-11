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
 package fr.amapj.view.views.listeadherents;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGListeAdherent.Type;
import fr.amapj.service.services.listeadherents.ListeAdherentsService;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.parametres.paramecran.PEListeAdherentDTO;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.engine.template.BackOfficeView;


/**
 * Page permettant de presenter la liste des utilisateurs
 * 
 *  
 *
 */
public class ListeAdherentsView extends BackOfficeView
{

	private Table beanTable;
	
	private TextField searchField;

	private String textFilter;
	
	BeanItemContainer<Utilisateur> listPartContainer;
	
	private Button sendMailButton;
	
	/**
	 * 
	 */
	@Override
	public void enterIn(ViewChangeEvent event)
	{		
		// TODO code à factoriser
		PEListeAdherentDTO p = new ParametresService().getPEListeAdherentDTO();
		
		listPartContainer = new BeanItemContainer<>(Utilisateur.class);
		List<Utilisateur> us = new ListeAdherentsService().getAllUtilisateurs(false);
		listPartContainer.addAll(us);
		
					
		// on trie par nom puis prenom
		listPartContainer.sort(new String[] { "nom" , "prenom" }, new boolean[] { true, true });
			
		// Bind it to a component
		beanTable = createTable(listPartContainer);
		
		
		// Gestion de la liste des colonnes visibles
		List<String> cols = new ArrayList<>();
		cols.add("nom");
		cols.add("prenom");
		
		if (p.canAccessEmail)
		{	
			cols.add("email");
		}
		if (p.canAccessTel1)
		{	
			cols.add("numTel1");
		}
		if (p.canAccessTel2)
		{	
			cols.add("numTel2");
		}
		
		beanTable.setVisibleColumns(cols.toArray());
		
		beanTable.setColumnHeader("nom","Nom");
		beanTable.setColumnHeader("prenom","Prénom");
		beanTable.setColumnHeader("email","E mail");
		beanTable.setColumnHeader("numTel1","Numéro Tel 1");
		beanTable.setColumnHeader("numTel2","Numéro Tel 2");
		
		
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
		
	
		
		Label title = new Label("Liste des adhérents");
		title.setSizeUndefined();
		title.addStyleName("stdlistpart-text-title");	
		
		
		sendMailButton = new Button("Envoyer un mail à tous ...");
		sendMailButton.addClickListener(new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleSendMail();
			}
		});
		

		searchField = new TextField();
		searchField.setInputPrompt("Rechercher par le nom ou le prénom");
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
		
		if (p.canAccessEmail)
		{	
			toolbar.addComponent(sendMailButton);
		}
		
		// 
		toolbar.addComponent(LinkCreator.createLink(new EGListeAdherent(Type.STD,p)));
		toolbar.addComponent(searchField);
		toolbar.setWidth("100%");
		toolbar.setExpandRatio(searchField, 1);
		toolbar.setComponentAlignment(searchField, Alignment.TOP_RIGHT);
		toolbar.setSpacing(true);

		
		
		addComponent(title);
		addComponent(toolbar);
		
		addComponent(beanTable);
		setExpandRatio(beanTable, 1);
				
	}
	
	private void handleSendMail()
	{
		String mails = new ListeAdherentsService().getAllEmails();
		PopupCopyAllMail popup = new PopupCopyAllMail(mails);
		CorePopup.open(popup);
		
	}

	private void updateFilters()
	{
		
		listPartContainer.removeAllContainerFilters();
		if (textFilter != null && !textFilter.equals(""))
		{
			Or or = new Or(new Like(Utilisateur.P.NOM.prop(), textFilter + "%", false), new Like(Utilisateur.P.PRENOM.prop(), textFilter + "%", false));
			listPartContainer.addContainerFilter(or);
		}
	}
}
