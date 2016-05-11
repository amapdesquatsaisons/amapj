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
 package fr.amapj.view.engine.popup.messagepopup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.ChameleonTheme;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup permettant d'afficher un message , avec 1 bouton OK
 *  
 */
@SuppressWarnings("serial")
public class MessagePopup extends CorePopup
{
	protected Button okButton;
	
	List<String> messages = new ArrayList<>();
	
	private ContentMode contentMode = ContentMode.TEXT;
	
	
	public MessagePopup(String title, List<String> strs)
	{
		popupTitle = title;
		messages.addAll(strs);
	}
	
	public MessagePopup(String title, String ... msgs)
	{
		this(title,ContentMode.TEXT,msgs);
	}
	
	public MessagePopup(String title, ContentMode contentMode,String ... msgs)
	{
		this.contentMode = contentMode;
		popupTitle = title;
		for (int i = 0; i < msgs.length; i++)
		{
			messages.add(msgs[i]);
		}
	}
	
	
	protected void createButtonBar()
	{		
		okButton = addDefaultButton("OK", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				close();
			}
		});
				
	
	}
	

	protected void createContent(VerticalLayout contentLayout)
	{
		for (String message : messages)
		{
			// Construction de la zone de texte
			HorizontalLayout hlTexte = new HorizontalLayout();
			hlTexte.setMargin(true);
			hlTexte.setSpacing(true);
			hlTexte.setWidth("100%");
			
			
			Label textArea = new Label(message,contentMode);
			textArea.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
			textArea.setWidth("80%");
			
			hlTexte.addComponent(textArea);
			hlTexte.setExpandRatio(textArea, 1);
			hlTexte.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
			
			contentLayout.addComponent(hlTexte);
		}
	}
	
}
