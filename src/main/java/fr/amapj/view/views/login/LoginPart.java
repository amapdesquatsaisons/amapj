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
 package fr.amapj.view.views.login;

import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.common.DebugUtil;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.FormPopup;
import fr.amapj.view.engine.ui.AmapUI;

public class LoginPart
{
	private PasswordManager passwordManager = new PasswordManager();
	
	public GridLayout loginLayout = new GridLayout(8, 8);
		
	private AmapUI ui;
	
	public LoginPart()
	{
		
	}
	
	public void buildLoginView(boolean exit,CssLayout root,AmapUI ui,String loginFromUrl,String passwordFromUrl,String sudo)
	{
		this.ui = ui;
		if (exit)
		{
			root.removeAllComponents();
		}
		

		loginLayout.removeAllComponents();
		loginLayout.setSizeFull();
		
		root.addComponent(loginLayout);
		
		
		// Bandeau du haut
		String nomAmap = new ParametresService().getParametres().nomAmap;
		String message  = "<h1>"+nomAmap+"</h1>";
		Panel bandeau = new Panel(message);
		bandeau.addStyleName(ChameleonTheme.PANEL_BORDERLESS);
		loginLayout.addComponent(bandeau,0,0,7,0);
		
		
		// Image sur la partie droite (panier)
		Resource res = new ThemeResource("img/panier.png");
		Image image = new Image(null, res);
		image.setSizeFull();
		loginLayout.addComponent(image,1,2,3,5);
		
		// Zone de saisie login/password sur la gauche	
		MyLoginForm myLoginForm = new MyLoginForm(loginFromUrl,passwordFromUrl,sudo);
		myLoginForm.setWidth("100%");
		loginLayout.addComponent(myLoginForm,5,2,6,5);
		
		Label l1 = new Label("Application fonctionnant avec AmapJ - ");
		Link link = new Link("Plus d'infos", new ExternalResource("http://amapj.fr"));
		link.setTargetName("_blank");
		
		HorizontalLayout hL = new HorizontalLayout();
		hL.addComponent(l1);
		hL.setComponentAlignment(l1, Alignment.MIDDLE_CENTER);
		hL.addComponent(link);
		hL.setComponentAlignment(link, Alignment.MIDDLE_CENTER);
		hL.setMargin(true);
		
		loginLayout.addComponent(hL,0,7,7,7);
		loginLayout.setComponentAlignment(hL, Alignment.BOTTOM_CENTER);
		
		
		// Si les deux champs ont été remplis on tente une validation automatique
		if ((passwordFromUrl!=null) && (loginFromUrl!=null))
		{
			myLoginForm.login(loginFromUrl, passwordFromUrl);
		}
		
	}

	

	/**
	 * Gestion de l'appui sur mot de passe perdu
	 */
	protected void handleLostPwd()
	{
		FormPopup.open(new PopupSaisieEmail());
	}
	
	
	/**
	 * Zone de saisie du password 
	 *
	 */
    public class MyLoginForm extends LoginForm 
    {
    	String loginFromUrl;
    	String passwordFromUrl;
    	String sudo;
    	
    	VerticalLayout layout;
    	TextField userNameField;
    	
		public MyLoginForm(String loginFromUrl, String passwordFromUrl,	String sudo) 
		{
			this.loginFromUrl = loginFromUrl;
			this.passwordFromUrl = passwordFromUrl;
			this.sudo = sudo;
		}

		@Override
        protected Component createContent(TextField userNameField, PasswordField passwordField, Button loginButton) 
        {
            layout = new VerticalLayout();
            layout.setSpacing(true);
            layout.setMargin(true);
            
            this.userNameField = userNameField;
            userNameField.setCaption("Adresse Email");
            userNameField.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
            userNameField.setWidth("100%");
            userNameField.setId("amapj.login.email");
    		if (loginFromUrl!=null)
    		{
    			userNameField.setValue(loginFromUrl);
    		}
    		layout.addComponent(userNameField);

    		passwordField.setCaption("Mot de passe");
    		passwordField.setStyleName(ChameleonTheme.TEXTFIELD_BIG);
    		passwordField.setWidth("100%");
    		passwordField.setId("amapj.login.password");
    		if (passwordFromUrl!=null)
    		{
    			passwordField.setValue(passwordFromUrl);
    		}
    		layout.addComponent(passwordField);
    		
    		if ((loginFromUrl==null) || (loginFromUrl.length()==0))
    		{
    			userNameField.focus();
    		}
    		else
    		{
    			passwordField.focus();
    		}

    		loginButton.setCaption("S'identifier");
    		loginButton.setId("amapj.login.signin");
    		if (sudo!=null)
    		{
    			loginButton.setCaption("SUDO");
    		}
    		
    		loginButton.addStyleName(ChameleonTheme.BUTTON_BIG);
    		loginButton.addStyleName(ChameleonTheme.BUTTON_DEFAULT);
    		layout.addComponent(loginButton);
    		layout.setComponentAlignment(loginButton, Alignment.BOTTOM_LEFT);
    		
    		
    		Button lostPwd = new Button("Mot de passe perdu");
    		lostPwd.addStyleName(ChameleonTheme.BUTTON_BIG);
    		lostPwd.addStyleName(ChameleonTheme.BUTTON_LINK);
    		layout.addComponent(lostPwd);
    		layout.setComponentAlignment(lostPwd, Alignment.BOTTOM_LEFT);
    		
    		
    		
    		lostPwd.addClickListener(new ClickListener()
    		{
    			@Override
    			public void buttonClick(ClickEvent event)
    			{
    				handleLostPwd();
    			}
    		});
            return layout;
        }

 
        @Override
        protected void login(String userName, String password) 
        {
        	String msg = passwordManager.checkUser(userName, password,sudo); 
    		
    		if ( msg == null)
    		{
    			// Si le mot de passe est correct : on passe à la vue principale
    			ui.buildMainView();
    		} 
    		else
    		{
    			
    			// Sinon on affiche le probleme
    			if (layout.getComponentCount() > 4)
    			{
    				// Remove the previous error message
    				layout.removeComponent(layout.getComponent(4));
    			}
    			// Add new error message
    			Label error = new Label(msg, ContentMode.HTML);
    			error.addStyleName(ChameleonTheme.LABEL_ERROR);
    			error.addStyleName(ChameleonTheme.LABEL_BIG);
    			error.setSizeUndefined();
    			layout.addComponent(error);
    			userNameField.focus();
    		}
        }
    }
	
	
}
