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
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.backupdb.BackupDatabaseService;
import fr.amapj.service.services.maintenance.MaintenanceService;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

public class MaintenanceView extends VerticalLayout implements View
{

	private final static Logger logger = LogManager.getLogger();

	@Override
	public void enter(ViewChangeEvent event)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
		VerticalLayout layout = new VerticalLayout();

		addLabelH1(layout, "Maintenance du système");
		addLabel(layout, "");
		addLabel(layout, "Date et heure courante "+df.format(new Date()));
		addLabel(layout, "");
		
		addLabel(layout, "");
		addLabel(layout, "Version de l'application "+getVersion());
		addLabel(layout, "");
		
		
		
		
		Button b1 = new Button("Backup de la base et envoi par mail", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new BackupDatabaseService().backupDatabase();
			}
		});
		
		addLabel(layout, "");
		
		Button b2 = new Button("Remise à zéro du cache (obligatoire après requete SQL)", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				new MaintenanceService().resetDatabaseCache();
			}
		});
		
		
		
		Button b3 = new Button("Suppression complète d'un contrat vierge et des contrats associés", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupSuppressionTotaleContrat popup = new PopupSuppressionTotaleContrat();
				CorePopup.open(popup);
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
		
		Button b5 = new Button("Décalage dans le temps d'un contrat vierge et des contrats associés", new ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				PopupDecalageTemporelContrat popup = new PopupDecalageTemporelContrat();
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
		
		
		
		
		
				
		
		layout.addComponent(b1);
		layout.addComponent(b2);
		layout.addComponent(b3);
		layout.addComponent(b4);
		layout.addComponent(b5);
		layout.addComponent(b6);
		
		addComponent(layout);
		setSizeFull();
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
	
	
	
}
