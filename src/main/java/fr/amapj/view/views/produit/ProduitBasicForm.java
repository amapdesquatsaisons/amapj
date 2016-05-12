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
 package fr.amapj.view.views.produit;

import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.filter.Like;
import com.vaadin.data.util.filter.Or;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.IdentifiableUtil;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.dbservice.DbService;
import fr.amapj.service.services.excelgenerator.EGListeProduitProducteur;
import fr.amapj.service.services.excelgenerator.EGListeProduitProducteur.Type;
import fr.amapj.service.services.produit.ProduitDTO;
import fr.amapj.service.services.produit.ProduitService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.basicform.BasicFormListPart;
import fr.amapj.view.engine.basicform.FormInfo;
import fr.amapj.view.engine.excelgenerator.LinkCreator;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

public class ProduitBasicForm extends BasicFormListPart
{
	private List<Producteur> allowedProducteurs;

	/*
	 * Partie vue Liste
	 */

	protected void createColumn()
	{
		allowedProducteurs = new AccessManagementService().getAccessLivraisonProducteur(SessionManager.getUserRoles(),SessionManager.getUserId()); 
		
		addColumn(Produit.P.NOM);
		addColumn(Produit.P.CONDITIONNEMENT);
		addColumn("producteur.nom", "Producteur");
		orderBy("producteur.nom", "nom");
	}

	protected String getListPartTitle()
	{
		return "Liste des produits";
	}

	protected String getListPartInputPrompt()
	{
		return "Rechercher par produit ou producteur";
	}

	protected void setFilter(BeanItemContainer container, String textFilter)
	{
		Or or = new Or(new Like("producteur.nom", textFilter + "%", false), new Like("nom", textFilter + "%", false));
		container.addContainerFilter(or);
	}

	/*
	 * Partie vue Form
	 */

	protected Class<? extends Identifiable> getClazz()
	{
		return Produit.class;
	}

	protected void createFormField(FormInfo formInfo)
	{
		// Champ 1
		addFieldText(formInfo, Produit.P.NOM);
		
		// Champ 2
		addFieldText(formInfo, Produit.P.CONDITIONNEMENT);

		
		// Champ 3
		Field f = addFieldSearcher(formInfo, "producteurId", "Producteur",new Searcher(SearcherList.PRODUCTEUR,"Producteur",allowedProducteurs));
		// Il n'est pas possible de modifier le producteur d'un produit existant
		if (formInfo.addMode==false)
		{
			f.setEnabled(false);
		}
		
	}
	
	protected String getEditorPartTitle(boolean addMode)
	{
		if (addMode==true)
		{
			return "Création d'un produit";
		}
		else
		{
			return "Modification d'un produit";
		}
	}
	
	protected Component getExtraComponent()
	{
		return LinkCreator.createLink(new EGListeProduitProducteur(Type.STD));
	}

	@Override
	protected Object loadDTO(Long id)
	{
		return new ProduitService().getProduitDto(id);
	}

	@Override
	protected void saveDTO(Object dto, boolean isNew)
	{
		new ProduitService().update( (ProduitDTO) dto, isNew);
		
	}
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new ProduitService().deleteProduit(idItemToSuppress);
	}
	
	
	@Override
	protected boolean isAccessAllowed(Long id)
	{
		ProduitDTO p =  new ProduitService().getProduitDto(id);
		return IdentifiableUtil.contains(allowedProducteurs, p.producteurId);
	}
	
	
}
