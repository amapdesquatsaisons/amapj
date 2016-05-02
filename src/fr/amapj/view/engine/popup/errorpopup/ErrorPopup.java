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
 package fr.amapj.view.engine.popup.errorpopup;

import javax.validation.ConstraintViolationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.common.StackUtils;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.session.SessionParameters;
import fr.amapj.view.engine.popup.corepopup.CorePopup;


/**
 * Popup pour l'affichage d'une vraie erreur
 *  
 *  Attention, l'erreur est indiquée dans les fichiers de logs
 *  
 *  Cette classe ne doit pas être utilisée pour afficher un message d'avertissement à l'utilisateur
 *  mais uniquement quand une vraie erreur de programmation est détectée
 *  
 */
@SuppressWarnings("serial")
public class ErrorPopup extends CorePopup
{

	private final static Logger logger = LogManager.getLogger();
	
	private String okButtonTitle = "OK";
	
	
	private String message;
	private Throwable throwable;
	
	public ErrorPopup(String message)
	{
		this(message,null);
	}
	
	public ErrorPopup(String message,Throwable throwable)
	{
		this.message = message;
		this.throwable = throwable;
		popupTitle = "Erreur";
	}
	
	public ErrorPopup(Throwable throwable)
	{
		this(null,throwable);
	}
	
	
	

	protected void createContent(VerticalLayout contentLayout)
	{

		// Message loggé 
		SessionParameters p = SessionManager.getSessionParameters();
		String debugMessage = null;
		if (p!=null)
		{
			debugMessage = p.userNom +" "+p.userPrenom+" a rencontré une erreur :"+message;
			p.incNbError();
		}
		else
		{
			debugMessage = "Pas d'utilisateur encore loggé. Erreur :"+message;
		}
		logger.info( debugMessage, throwable);
		
		if (throwable instanceof ConstraintViolationException)
		{
			ConstraintViolationException e = (ConstraintViolationException) throwable;
			logger.info("Autre information:"+StackUtils.getConstraints(e));
		}
		

		// Message utilisateur
		String msg = "Désolé, une erreur est survenue.<br/>";
		if (message!=null)
		{
			msg = msg+"Information supplémentaire:<br/>" + message+"<br/>";
		}
		msg = msg + "Veuillez cliquer sur OK pour continuer<br/>";
		
		

		// Construction de la zone de texte
		HorizontalLayout hlTexte = new HorizontalLayout();
		hlTexte.setMargin(true);
		hlTexte.setSpacing(true);
		hlTexte.setWidth("100%");
		
		
		Label textArea = new Label(msg,ContentMode.HTML);
		textArea.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
		textArea.setWidth("80%");
		
		hlTexte.addComponent(textArea);
		hlTexte.setExpandRatio(textArea, 1);
		hlTexte.setComponentAlignment(textArea, Alignment.MIDDLE_CENTER);
		
		contentLayout.addComponent(hlTexte);
	}
	
	protected void createButtonBar()
	{
		addDefaultButton(okButtonTitle, new Button.ClickListener()
		{

			@Override
			public void buttonClick(ClickEvent event)
			{
				handleOK();
			}
		});	
	}

	protected void handleOK()
	{
		close();
	}
	

	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(Throwable throwable)
	{
		open(new ErrorPopup(throwable));
	}
	
	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(String message,Throwable throwable)
	{
		open(new ErrorPopup(message,throwable));
	}

	/**
	 * Attention : cette méthode rend la main tout de suite, ca n'attend pas 
	 * le clic de l'opérateur 
	 */	
	static public void open(String message)
	{
		open(new ErrorPopup(message));
	}
}
