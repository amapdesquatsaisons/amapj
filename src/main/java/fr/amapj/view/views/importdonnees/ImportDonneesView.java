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
 package fr.amapj.view.views.importdonnees;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.excelgenerator.EGListeAdherent;
import fr.amapj.service.services.excelgenerator.EGListeAdherent.Type;
import fr.amapj.service.services.excelgenerator.EGListeProduitProducteur;
import fr.amapj.view.engine.excelgenerator.LinkCreator;

public class ImportDonneesView extends Panel implements View
{

	//private final static Logger logger = Logger.getLogger(ImportDonneesView.class.getName());

	@Override
	public void enter(ViewChangeEvent event)
	{

		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);

		addLabelH1(layout, "Outil d'import des données en masse");
		addEmptyLine(layout);
		addLabel(layout, "Cet outil vous permet d'importer en masse les utilisateurs, les produits et les producteurs");
		//addLabel(layout, "Cet outil permet aussi de vider la base de données si nécessaire");
		addEmptyLine(layout);
		
		Panel utilisateurPanel = new Panel("Importations des utilisateurs");
		utilisateurPanel.setContent(getUtilisateurPanel());
		
		
		Panel produitPanel = new Panel("Importations des produits et des producteurs");
		produitPanel.setContent(getProduitPanel());
		
		layout.addComponent(utilisateurPanel);
		addEmptyLine(layout);
		layout.addComponent(produitPanel);
	

		setContent(layout);
		setSizeFull();
	}

	private Component getUtilisateurPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		addEmptyLine(layout);
		addLabel(layout, "Pour importer les utilisateurs en masse, vous devez remplir un fichier Excel à un certain format.");
		addLabel(layout, "Pour avoir un exemple du fichier à remplir, merci de cliquer sur ce lien :");
		layout.addComponent(LinkCreator.createLink(new EGListeAdherent(Type.EXAMPLE)));
		addEmptyLine(layout);
		
		addLabel(layout, "Une fois que votre fichier Excel est prêt, vous pouvez le charger dans l'application."
				+ " Pour cela, cliquez sur le bouton \"Charger les utilisateurs\", sélectionnez votre fichier, cliquez sur OK. Les utilisateurs seront alors automatiquement créés, sans mot de passe ");
		addEmptyLine(layout);
		//
		UtilisateurImporter utilisateurImporter = new UtilisateurImporter();
		Upload upload = new Upload(null, utilisateurImporter);
		upload.addSucceededListener(utilisateurImporter);
		upload.setStyleName("big");
		upload.setImmediate(true);
		upload.setButtonCaption("Charger les utilisateurs");

		layout.addComponent(upload);
		
		addEmptyLine(layout);
		
		return layout;
	}
	
	
	
	private Component getProduitPanel()
	{
		VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		
		addEmptyLine(layout);
		addLabel(layout, "Pour importer les produits et les producteurs en masse, vous devez remplir un fichier Excel à un certain format.");
		addLabel(layout, "Pour avoir un exemple du fichier à remplir, merci de cliquer sur ce lien :");
		layout.addComponent(LinkCreator.createLink(new EGListeProduitProducteur(fr.amapj.service.services.excelgenerator.EGListeProduitProducteur.Type.EXAMPLE)));
		addEmptyLine(layout);
		
		addLabel(layout, "Une fois que votre fichier Excel est prêt, vous pouvez le charger dans l'application."
				+ " Pour cela, cliquez sur le bouton \"Charger les produits et les producteurs\", sélectionnez votre fichier, cliquez sur OK. Les produits et les producteurs seront alors automatiquement créés.");
		
		//

		ProduitImporter produitImporter = new ProduitImporter();
		Upload upload = new Upload(null, produitImporter);
		upload.addSucceededListener(produitImporter);
		upload.setStyleName("big");
		upload.setImmediate(true);
		upload.setButtonCaption("Charger les produits et les producteurs");
		
		addEmptyLine(layout);
		layout.addComponent(upload);
		addEmptyLine(layout);
		
		return layout;
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
