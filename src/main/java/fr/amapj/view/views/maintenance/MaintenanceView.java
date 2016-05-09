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
 package fr.amapj.view.views.maintenance;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.util.IOUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.backupdb.BackupDatabaseService;
import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGListeAdherent.Type;
import fr.amapj.service.services.mailer.MailerCounter;
import fr.amapj.service.services.maintenance.MaintenanceService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.corepopup.CorePopup;
import fr.amapj.view.views.importdonnees.UtilisateurImporter;

public class MaintenanceView extends Panel implements View
{

	private final static Logger logger = LogManager.getLogger();

	@Override
	public void enter(ViewChangeEvent event)
	{
		boolean adminFull = SessionManager.getSessionParameters().isAdminFull();
		
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		addLabelH1(layout, "Maintenance du système");
		addEmptyLine(layout);
		
		addLabel(layout, "Date et heure courante "+df.format(new Date()));
		addEmptyLine(layout);
		addLabel(layout, "Version de l'application : "+getVersion());
		addEmptyLine(layout);
		addLabel(layout, "Nombre d'emails envoyés aujourd'hui : "+MailerCounter.getNbMails());
		addEmptyLine(layout);
		
		
		Panel backupPanel = new Panel("Sauvegarde de la base et envoi par e mail");
		backupPanel.setContent(getBackupPanel());
		
		Panel suppressionPanel = new Panel("Suppression complète d'un contrat vierge et des contrats associés");
		suppressionPanel.setContent(getSuppressionPanel());
		
		Panel diversPanel = new Panel("Outils divers");
		diversPanel.setContent(getDiversPanel());
		
		layout.addComponent(backupPanel);
		addEmptyLine(layout);
		layout.addComponent(suppressionPanel);
		addEmptyLine(layout);
		
		//
		if (adminFull)
		{
			layout.addComponent(diversPanel);
		}
		
		setContent(layout);
		setSizeFull();
	}
	
	
	
	private Component getBackupPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		addEmptyLine(layout);
		addLabel(layout, "Cet outil vous permet de faire une sauvegarde de la base et de l'envoyer par mail à l'adresse paramétrée dans les paramètres généraux.");
		addLabel(layout, "Cet outil peut être utilisé avant de faire des modifications importantes sur la base.");
		addLabel(layout, "Cet outil permet aussi de vérifier que les sauvegardes fonctionnent bien.");
		

		addEmptyLine(layout);
		
		Button b1 = new Button("Backup de la base et envoi par mail", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new BackupDatabaseService().backupDatabase();
			}
		});
		
		layout.addComponent(b1);
				
		addEmptyLine(layout);
		
		return layout;
	}
	
	
	private Component getSuppressionPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		addEmptyLine(layout);
		addLabel(layout, "Cet outil vous permet de supprimer complètement un contrat vierge et tous les contrats signés associés.");
		addEmptyLine(layout);
		addLabel(layout, "ATTENTION !!! Les suppressions sont définitives !!! ATTENTION !!!!.");
		

		addEmptyLine(layout);
		

		Button b3 = new Button("Suppression complète d'un contrat ...", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupSuppressionTotaleContrat popup = new PopupSuppressionTotaleContrat();
				CorePopup.open(popup);
			}
		});
		
		layout.addComponent(b3);
				
		addEmptyLine(layout);
		
		return layout;
	}
	
	
	private Component getDiversPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		addEmptyLine(layout);
		addLabel(layout, "Outils divers réservés aux experts.");
		addEmptyLine(layout);
		addLabel(layout, "ATTENTION !!! Ne pas utiliser sur une base en production !!! ATTENTION !!!!.");
		

		addEmptyLine(layout);
		

		Button b2 = new Button("Remise à zéro du cache (obligatoire après requete SQL)", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new MaintenanceService().resetDatabaseCache();
			}
		});
		
		
		Button b4 = new Button("Positionner les dates pour la base démo", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupDateDemo popup = new PopupDateDemo();
				CorePopup.open(popup);
			}
		});
		
		
		Button b6 = new Button("Générer une erreur", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				String str = null;
				String s = 	str.trim();
			}
		});
		
		layout.addComponent(b2);
		addEmptyLine(layout);
		layout.addComponent(b4);
		addEmptyLine(layout);
		layout.addComponent(b6);
				
		addEmptyLine(layout);
		
		return layout;
	}
	
	
	/**
	 * Lit lenuméro de la version
	 * @return
	 */
	private String getVersion()
	{
		try
		{
			InputStream in = this.getClass().getResourceAsStream("/amapj_version.txt");
			byte[] bs = IOUtils.toByteArray(in);
			return new String(bs);
		} 
		catch (IOException e)
		{
			return "error";
		}
	}

	private Label addLabelH1(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}
	
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;
	}
	
	
	private Label addEmptyLine(VerticalLayout layout)
	{
		Label tf = new Label("<br/>",ContentMode.HTML);
		tf.addStyleName(ChameleonTheme.LABEL_BIG);
		layout.addComponent(tf);
		return tf;

	}
	
	
}
