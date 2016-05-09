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
 package fr.amapj.view.views.listeproducteurreferent;

import java.util.List;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.listeproducteurreferent.DetailProducteurDTO;
import fr.amapj.service.services.listeproducteurreferent.ListeProducteurReferentService;
import fr.amapj.service.services.producteur.ProdUtilisateurDTO;
import fr.amapj.service.services.utilisateur.util.UtilisateurUtil;


/**
 * Page permettant à l'utilisateur de visualiser tous les producteurs et les référents
 * 
 *  
 *
 */
public class ListeProducteurReferentView extends Panel implements View
{
	VerticalLayout layout = null;

	/**
	 * 
	 */
	@Override
	public void enter(ViewChangeEvent event)
	{
		setSizeFull();
		refresh();
	}

	
	/**
	 * Ajoute un label sur toute la largeur à la ligne indiquée
	 */
	private Label addLabel(VerticalLayout layout, String str)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf);
		return tf;
		
	}


	private void refresh()
	{
		List<DetailProducteurDTO> dtos = new ListeProducteurReferentService().getAllProducteurs();
		
		
		layout = new VerticalLayout();
		
		
		for (DetailProducteurDTO detailProducteurDTO : dtos)
		{
			// Le titre
			addLabel(layout,"Producteur : "+detailProducteurDTO.nom);
			
			String str = detailProducteurDTO.description;
			if (str!=null)
			{
				Label tf =new Label(str, ContentMode.HTML);
				tf.addStyleName(ChameleonTheme.LABEL_H4);
				layout.addComponent(tf);
			}
			
			str = formatUtilisateur(detailProducteurDTO.utilisateurs);
			layout.addComponent(new Label(str, ContentMode.HTML));
			
			str = formatReferent(detailProducteurDTO.referents);
			layout.addComponent(new Label(str, ContentMode.HTML));
		}
		
		// 
		
		layout.setMargin(true);
		layout.setSpacing(true);
		
		setContent(layout);
		addStyleName(ChameleonTheme.PANEL_BORDERLESS);
	}
	
	
	
	
	
	
	private String formatUtilisateur(List<ProdUtilisateurDTO> utilisateurs)
	{
		if (utilisateurs.size()==0)
		{
			return "";
		}
		
		String str = UtilisateurUtil.asStringPrenomFirst(utilisateurs, " et ");
		
		if (utilisateurs.size()==1)
		{
			return "Le producteur est "+str+"<br/>";
		}
		else
		{
			return "Les producteurs sont  "+str+"<br/>";
		}
	}
	
	
	private String formatReferent(List<ProdUtilisateurDTO> utilisateurs)
	{
		if (utilisateurs.size()==0)
		{
			return "";
		}
		
		String str = UtilisateurUtil.asStringPrenomFirst(utilisateurs, " et ");
		
		if (utilisateurs.size()==1)
		{
			return "Le référent est "+str+"<br/>";
		}
		else
		{
			return "Les référents sont "+str+"<br/>";
		}
	}
}
