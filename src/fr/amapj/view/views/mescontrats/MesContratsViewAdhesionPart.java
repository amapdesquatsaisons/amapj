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
 package fr.amapj.view.views.mescontrats;

import java.text.SimpleDateFormat;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ChameleonTheme;

import fr.amapj.service.services.gestioncotisation.GestionCotisationService;
import fr.amapj.service.services.gestioncotisation.PeriodeCotisationDTO;
import fr.amapj.service.services.mescontrats.AdhesionDTO;
import fr.amapj.service.services.mescontrats.MesContratsDTO;
import fr.amapj.view.engine.popup.suppressionpopup.PopupSuppressionListener;
import fr.amapj.view.engine.popup.suppressionpopup.SuppressionPopup;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;


/**
 * Page permettant à l'utilisateur de gérer ses contrats
 * 
 *  
 *
 */
public class MesContratsViewAdhesionPart implements PopupSuppressionListener
{
	
	SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");
	private MesContratsView view;

	/**
	 * 
	 */
	public MesContratsViewAdhesionPart(MesContratsView view)
	{
		this.view = view;
	}

	
	/**
	 * Ajoute un label sur toute la largeur à la ligne indiquée
	 */
	private Label addLabel(GridLayout layout, String str,int row1)
	{
		Label tf = new Label(str);
		tf.addStyleName(ChameleonTheme.LABEL_H1);
		layout.addComponent(tf, 0, row1, 3, row1);
		return tf;
		
	}
	


	private Button addButtonAdhesionAdherer(String str)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAdhesionAdherer();
			}
		});
		return b;
	}
	
	private void handleAdhesionAdherer()
	{
		PopupAdhesion adhesion = new PopupAdhesion(view.mesContratsDTO.adhesionDTO,true);
		PopupAdhesion.open(adhesion, this);
	}
	


	private Button addButtonAdhesionVoir(String str)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAdhesionVoir();
			}
		});
		return b;
	}
	
	private void handleAdhesionVoir()
	{
		PopupAdhesion adhesion = new PopupAdhesion(view.mesContratsDTO.adhesionDTO,false);
		PopupAdhesion.open(adhesion, this);
	}
	
	
	private Button addButtonAdhesionSupprimer(String str)
	{
		Button b = new Button(str);
		b.addStyleName(ChameleonTheme.BUTTON_BIG);
		b.addClickListener(new ClickListener()
		{
			
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAdhesionSupprimer();
			}
		});
		return b;
	}
	
	private void handleAdhesionSupprimer()
	{
		String text = "Etes vous sûr de vouloir supprimer votre adhésion?";
		Long idAdhesion = view.mesContratsDTO.adhesionDTO.periodeCotisationUtilisateurDTO.id;
		SuppressionPopup confirmPopup = new SuppressionPopup(text,idAdhesion);
		SuppressionPopup.open(confirmPopup, this);		
	}
	
	@Override
	public void deleteItem(Long idItemToSuppress) throws UnableToSuppressException
	{
		new GestionCotisationService().deleteAdhesion(idItemToSuppress);   
	}
	



	public int addAhesionInfo(GridLayout layout,int index)
	{
		MesContratsDTO mesContratsDTO = view.mesContratsDTO;
		
		
		
		// Information sur le renouvellement de l'adhésion
		if (mesContratsDTO.adhesionDTO.displayAdhesionTop())
		{
			// Le titre
			addLabel(layout,"Renouvellement de votre adhésion à l'AMAP",index);
			index++;
			
			String str = formatLibelleAdhesion(mesContratsDTO.adhesionDTO);
			
			layout.addComponent(new Label(str, ContentMode.HTML),0,index);
			
			if (mesContratsDTO.adhesionDTO.isCotisant())	
			{
				Button v = addButtonAdhesionVoir("Voir");
				layout.addComponent(v,1,index);
				layout.setComponentAlignment(v, Alignment.MIDDLE_CENTER);
				
				Button b = addButtonAdhesionAdherer("Modifier");
				layout.addComponent(b,2,index);
				layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
				
				b = addButtonAdhesionSupprimer("Supprimer");
				layout.addComponent(b,3,index);
				layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
				
			}
			else
			{
				Button b = addButtonAdhesionAdherer("Adhérer");
				layout.addComponent(b,1,index);
				layout.setComponentAlignment(b, Alignment.MIDDLE_CENTER);
			}
			
			index++;
			
		}
		
		return index;
		
	}
	
	
	


	private String formatLibelleAdhesion(AdhesionDTO adhesionDTO)
	{
		SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
		
		PeriodeCotisationDTO p = adhesionDTO.periodeCotisationDTO;
		// Ligne 0
		String str = "<h4>Adhésion pour "+p.nom+"</h4>";
		
		// Ligne 1
		
		
		//  
		if (adhesionDTO.isCotisant())
		{
			str = str+"Vous avez renouvelé votre adhésion à l'AMAP.<br/>Vous pouvez modifier votre choix  jusqu'au "+df.format(p.dateFinInscription)+ " minuit.";
		}
		else
		{
			str = str +"Il est temps d'adhérer pour la nouvelle saison !<br/>";
			
			str = str+"Cette adhésion couvre la période du "+df2.format(p.dateDebut)+" au "+df2.format(p.dateFin);
			
			str=str+"<br/>";
			str = str+"Vous avez jusqu'au  "+df.format(p.dateFinInscription)+ " minuit pour adhérer à l'AMAP.";
		}
		
		str=str+"<br/>";
		str=str+"<br/>";
		
		return str;
	}



	@Override
	public void onPopupClose()
	{
		view.refresh();
		
	}


}
