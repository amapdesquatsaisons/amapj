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
 package fr.amapj.view.views.editionspe;

import com.vaadin.data.util.BeanItem;

import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.editionspe.AbstractEditionSpeJson;
import fr.amapj.model.models.editionspe.TypEditionSpecifique;
import fr.amapj.model.models.editionspe.planningmensuel.ParametresProduitsJson;
import fr.amapj.model.models.editionspe.planningmensuel.PlanningMensuelJson;
import fr.amapj.service.services.editionspe.EditionSpeDTO;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet la saisie des paramétres du planning mensuel 
 * 
 *
 */
@SuppressWarnings("serial")
public class PlanningMensuelEditorPart extends WizardFormPopup
{

	private PlanningMensuelJson etiquetteDTO;

	private boolean create;

	public enum Step
	{
		GENERAL, COLONNES  ;
	}

	/**
	 * 
	 */
	public PlanningMensuelEditorPart(boolean create,EditionSpeDTO p)
	{
		this.create = create;
		
		popupWidth = "80%";
		popupHeight = "60%";
		
		if (create)
		{
			popupTitle = "Création d'un planning mensuel ou hebdomadaire";
			this.etiquetteDTO = new PlanningMensuelJson();
			this.etiquetteDTO.setTypEditionSpecifique(TypEditionSpecifique.PLANNING_MENSUEL);
		}
		else
		{
			popupTitle = "Modification d'un planning mensuel ou hebdomadaire";
			
			
			this.etiquetteDTO = (PlanningMensuelJson) AbstractEditionSpeJson.load(p);
			
		}	
			
		item = new BeanItem<PlanningMensuelJson>(this.etiquetteDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;

		case COLONNES:
			addFieldColonnes();
			break;
			
		default:
			break;
		}
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales du planning mensuel ou hebdomadaire");
		
		addTextField("Nom", "nom");
		
		addComboEnumField("Type de planning", "typPlanning", new NotNullValidator());
		
		
		addIntegerField("Largeur (en mm) de la colonne Nom", "lgColNom");
		
		addIntegerField("Largeur (en mm) de la colonne Prénom", "lgColPrenom");
		
		addIntegerField("Largeur (en mm) de la colonne Présence", "lgColPresence");
		
		addIntegerField("Largeur (en mm) de la colonne Tel", "lgColnumTel1");
		
		Enum[] enumsToExclude = new Enum[] { RoleList.MASTER };
		
		addComboEnumField("Accessible par ", "accessibleBy", enumsToExclude,new NotNullValidator());
		
			
	}

	private void addFieldColonnes()
	{
		// Titre
		setStepTitle("la description des colonnes");
		
		CollectionEditor<ParametresProduitsJson> f1 = new CollectionEditor<ParametresProduitsJson>("Liste des colonnes", (BeanItem) item, "parametresProduits", ParametresProduitsJson.class);
		f1.addSearcherColumn("idProduit","Produit",FieldType.SEARCHER,null,SearcherList.PRODUIT_ALL,null);
		f1.addColumn("titreColonne","Titre de la colonne",FieldType.STRING,"");
		f1.addColumn("largeurColonne","Largeur en mm",FieldType.INTEGER,20);
		binder.bind(f1, "parametresProduits");
		form.addComponent(f1);
		
	
	}
	
	

	@Override
	protected void performSauvegarder()
	{
		EditionSpeDTO editionSpeDTO = etiquetteDTO.save(); 
		new EditionSpeService().update(editionSpeDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
