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
 package fr.amapj.view.views.saisiepermanence;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.saisiepermanence.PermanenceService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Popup pour la planification des permanences
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupRappelPermanence extends WizardFormPopup
{

	public enum Step
	{
		INFO_GENERALES, SAISIE_TEXTE_MAIL;
	}

	/**
	 * 
	 */
	public PopupRappelPermanence()
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Envoi d'un mail de rappel des permanences";

		// 
		// Contruction de l'item
		item.addItemProperty("texte", new ObjectProperty<String>(getInitialText()));

	}
	
	

	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		case SAISIE_TEXTE_MAIL:
			addFieldTexteMail();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("le rappel de permanence");
		
		addLabel("Avec cet outil,  vous allez pouvoir envoyer un mail de rappel pour toutes les personnes de permanence dans le futur",ContentMode.HTML);

	}

	private void addFieldTexteMail()
	{
		// Titre
		setStepTitle("le texte du mail");
		
	
		//
		RichTextArea f =  addRichTextAeraField("Texte du mail", "texte");
		f.setHeight(10, Unit.CM);
		
		
		

	}

	

	@Override
	protected void performSauvegarder()
	{
		String texte = (String) item.getItemProperty("texte").getValue();
		new PermanenceService().performRappel(texte);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	private String getInitialText()
	{
		String lineSep="<br/>";
		
		ParametresDTO param = new ParametresService().getParametres();
		
		StringBuffer buf = new StringBuffer();
		buf.append("<h2>"+param.nomAmap+"</h2>");
		buf.append("Bonjour , ");
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append("vous trouverez ci joint le planning de distribution pour l'année à venir.");
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append("En ce qui vous concerne, vos dates de permanences sont :");
		buf.append(lineSep);
		buf.append("#DATES#");
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append("Nous comptons sur votre participation active !!!");
		buf.append(lineSep);
		buf.append("Si vous n'êtes pas disponible à une date, merci d'échanger avec un autre AMAPIEN");
		buf.append(lineSep);
		buf.append("Pour cela, prenez contact avec un autre amapien, une fois que vous avez son accord, merci de corriger le planning affiché à l'AMAP (en barrant au stylo)");
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append("Notez que vous pouvez désormais consulter le planning des permanences sur l'application WEB de l'AMAP");
		buf.append(lineSep);
		buf.append("#LINK#");
		buf.append(lineSep);
		buf.append(lineSep);
		buf.append("Bonne journée à tous !!");
		buf.append(lineSep);
		return buf.toString();
	}
	
	
}
