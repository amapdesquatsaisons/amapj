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
 package fr.amapj.view.views.producteur.basicform;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.TextArea;

import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.service.services.producteur.ProdUtilisateurDTO;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.UniqueInDatabaseValidator;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class ProducteurEditorPart extends WizardFormPopup
{

	private ProducteurDTO producteurDTO;

	private boolean create;

	public enum Step
	{
		GENERAL, UTILISATEUR , REFERENTS ;
	}

	/**
	 * 
	 */
	public ProducteurEditorPart(boolean create,ProducteurDTO p)
	{
		this.create = create;
		
		setWidth(80);
		
		if (create)
		{
			popupTitle = "Création d'un producteur";
			this.producteurDTO = new ProducteurDTO();
		}
		else
		{
			popupTitle = "Modification d'un producteur";
			this.producteurDTO = p;
		}	
		
	
		
		item = new BeanItem<ProducteurDTO>(this.producteurDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;

		case UTILISATEUR:
			addFieldUtilisateur();
			break;
			
		case REFERENTS:
			addFieldReferents();
			break;

		default:
			break;
		}
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales du producteur");
		
		// Champ 1
		IValidator uniq = new UniqueInDatabaseValidator(Producteur.class,"nom",producteurDTO.id);
		addTextField("Nom", "nom",uniq);
		
		TextArea f =  addTextAeraField("Description", "description");
		f.setMaxLength(20480);
		f.setHeight(5, Unit.CM);

		// Champ 2
		addIntegerField("Délai en jours entre l'envoi du fichier par mail et la livraison", "delaiModifContrat");
		
		String str = 	"Exemple :<br>" +
						"Si les livraisons ont lieu le jeudi et si vous mettez 2 dans le champ précédent<br>"+
						"alors le producteur recevra le mail le mardi matin à 2h00 du matin<br>";
		
		addLabel(str, ContentMode.HTML);
		
		// Champ 3
		if (new EditionSpeService().ficheProducteurNeedEtiquette())
		{
			addSearcher("Type des étiquettes de distribution", "idEtiquette", SearcherList.ETIQUETTE ,null);
		}
		
		// Champ 4
		if (new EditionSpeService().ficheProducteurNeedEngagement())
		{
			// TODO addSearcher("Contrat d'engagement", "idEngagement", SearcherList.ENGAGEMENT ,null);
			
			// TODO addTextField("Identification du producteur sur le contrat d'engagement", "libContrat");
		}
		
		

	}

	private void addFieldUtilisateur()
	{
		// Titre
		setStepTitle("les noms des producteurs");
		
		addLabel("Vous pouvez laisser cette liste vide dans un premier temps", ContentMode.HTML);
		
		CollectionEditor<ProdUtilisateurDTO> f1 = new CollectionEditor<ProdUtilisateurDTO>("Liste des producteurs", (BeanItem) item, "utilisateurs", ProdUtilisateurDTO.class);
		f1.addSearcherColumn("idUtilisateur", "Nom du producteur",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		f1.addColumn("etatNotification","Notification par mail",FieldType.CHECK_BOX,true);
		binder.bind(f1, "utilisateurs");
		form.addComponent(f1);
		
	
	}
	
	private void addFieldReferents()
	{
		if (checkFieldUtilisateur()==false)
		{
			setBackOnlyMode();
			
			// Titre
			setErrorTitle("Il y a des erreurs dans la saisie des noms des producteurs");
			
			addLabel("Vous ne devez pas avoir de lignes vides",ContentMode.TEXT);
			
			addLabel("Vous devez bien choisir dans la liste des noms proposés",ContentMode.TEXT);
			
			return ;
		}
		
		
		// Titre
		setStepTitle("les noms des référents");
		
		addLabel("Vous pouvez laisser cette liste vide dans un premier temps", ContentMode.HTML);
		
		CollectionEditor<ProdUtilisateurDTO> f1 = new CollectionEditor<ProdUtilisateurDTO>("Liste des référents", (BeanItem) item, "referents", ProdUtilisateurDTO.class);
		f1.addSearcherColumn("idUtilisateur", "Nom des référents",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		binder.bind(f1, "referents");
		form.addComponent(f1);
	
	}
	
	
	private boolean checkFieldUtilisateur()
	{
		List<ProdUtilisateurDTO> us = producteurDTO.utilisateurs;
		for (ProdUtilisateurDTO lig : us)
		{
			if (lig.idUtilisateur==null)
			{
				return false;
			}
		}
		return true;
	}
	
	
	private boolean checkFieldReferents()
	{
		List<ProdUtilisateurDTO> us = producteurDTO.referents;
		for (ProdUtilisateurDTO lig : us)
		{
			if (lig.idUtilisateur==null)
			{
				return false;
			}
		}
		return true;
	}
	
	
	

	

	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		if (checkFieldReferents()==false)
		{
			List<String> ls = new ArrayList<String>();
			ls.add("Il y a des erreurs dans la saisie des noms des référents");
			ls.add("Vous ne devez pas avoir de lignes vides");
			ls.add("Vous devez bien choisir dans la liste des noms proposés");
			throw new OnSaveException(ls);
		}
		
		new ProducteurService().update(producteurDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
