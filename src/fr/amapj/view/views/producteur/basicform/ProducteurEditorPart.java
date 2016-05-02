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
 package fr.amapj.view.views.producteur.basicform;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.TextArea;

import fr.amapj.model.models.param.EtatModule;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.producteur.ProdUtilisateurDTO;
import fr.amapj.service.services.producteur.ProducteurDTO;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
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
		
		popupWidth = "80%";
		popupHeight = "60%";
		
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
		addTextField("Nom", "nom");
		
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
		// Titre
		setStepTitle("les noms des référents");
		
		addLabel("Vous pouvez laisser cette liste vide dans un premier temps", ContentMode.HTML);
		
		CollectionEditor<ProdUtilisateurDTO> f1 = new CollectionEditor<ProdUtilisateurDTO>("Liste des référents", (BeanItem) item, "referents", ProdUtilisateurDTO.class);
		f1.addSearcherColumn("idUtilisateur", "Nom des référents",FieldType.SEARCHER, null,SearcherList.UTILISATEUR_ACTIF,null);
		binder.bind(f1, "referents");
		form.addComponent(f1);
	
	}

	

	@Override
	protected void performSauvegarder()
	{
		new ProducteurService().update(producteurDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
