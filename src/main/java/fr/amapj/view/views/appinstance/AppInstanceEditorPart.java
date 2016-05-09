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
 package fr.amapj.view.views.appinstance;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;

import fr.amapj.common.StringUtils;
import fr.amapj.model.models.saas.TypDbExemple;
import fr.amapj.service.services.appinstance.AppInstanceDTO;
import fr.amapj.service.services.appinstance.AppInstanceService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet uniquement de creer des instances
 * 
 *
 */
@SuppressWarnings("serial")
public class AppInstanceEditorPart extends WizardFormPopup
{

	private AppInstanceDTO dto;

	public enum Step
	{
		GENERAL , DEMO_DATA , URL_INFO;
	}

	/**
	 * 
	 */
	public AppInstanceEditorPart()
	{
		popupWidth = "80%";
		popupHeight = "60%";
			
		popupTitle = "Création d'une instance";
		this.dto = new AppInstanceDTO();
		
		item = new BeanItem<AppInstanceDTO>(this.dto);

	}
	
	

	private String guessDefault()
	{
		ParametresDTO param = new ParametresService().getParametres();
		String res = param.getUrl();
		res = res.replaceAll("master", "");
		res = res + dto.nomInstance;
		return res;
	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;

		case DEMO_DATA:
			addFieldDemo();
			break;
			
		case URL_INFO:
			addFieldUrlInfo();
			break;
			
			

		default:
			break;
		}
	}

	


	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales de l'instance");
		
		// Champ 1
		addTextField("Nom", "nomInstance");
		
		addTextField("Dbms", "dbms");
		
		addTextField("Db UserName (si externe)", "dbUserName");
		
		addTextField("Db Password (si externe)", "dbPassword");
		
		addComboEnumField("Base de départ", "typDbExemple");
	}
	
	private void addFieldDemo()
	{
		dto.url = guessDefault();
		
		// Titre
		setStepTitle("les informations pour la création de la base");
		
		if (dto.typDbExemple==TypDbExemple.BASE_EXEMPLE)
		{
			// 
			addTextField("Nom de l'AMAP qui sera affiché", "nomAmap");
			
			addTextField("Ville de l'AMAP", "villeAmap");
			
			addTextField("Url vue par les utilisateurs", "url");
			
			addDateField("Date de fin des inscriptions", "dateFinInscription");
			
			addDateField("Date de début des distributions", "dateDebut");
			
			addDateField("Date de fin des distributions", "dateFin");
			
			addTextField("Password des utilisateurs", "password");
		}
		else
		{
			// 
			addTextField("Nom de l'AMAP qui sera affiché", "nomAmap");
			
			addTextField("Ville de l'AMAP", "villeAmap");
			
			addTextField("Url vue par les utilisateurs", "url");
			
			addTextField("Nom de l'administrateur", "user1Nom");
			
			addTextField("Prénom de l'administrateur", "user1Prenom");
			
			addTextField("Email de l'administrateur", "user1Email");
						
			addTextField("Password de l'administrateur", "password");
		}
		
	}
	
	
	private void addFieldUrlInfo()
	{
		// Titre
		setStepTitle("le mail à envoyer");
		
		String str = null;
		
		if (dto.typDbExemple==TypDbExemple.BASE_EXEMPLE)
		{
			str = getInfoDemo();
		}
		else
		{
			str = getInfoProd();
		}
				 
		//
		addLabel(str, ContentMode.HTML);
		
	}



	private String getInfoProd()
	{
		String str = "<br/>Bonjour <br/>"+
					"votre AmapJ de production est accessible avec ce lien :<br/><br/>"+
					dto.url+"<br/><br/>"+
					"Il y a un seul utilisateur avec les caractéristiques suivantes :<br/><br/>"+
					"Nom: "+dto.user1Nom+"<br/>"+
					"Prenom: "+dto.user1Prenom+"<br/>"+
					"Email: "+dto.user1Email+"<br/>"+
					"Password: "+dto.password+"<br/><br/>"+
					"Cet utilisateur est ADMINISTRATEUR, merci de changer dès que possible le mot de passe.";

	return str;
	}



	private String getInfoDemo()
	{
		String str = "<br/>Bonjour <br/>"+
 					"vous pouvez tester le logiciel AmapJ avec ce lien :<br/><br/>"+
					dto.url+"<br/><br/>"+
					"Les instructions pour une visite guidée sont ici :<br/><br/>"+
					getUrl()+"<br/><br/>"+
					"Ce lien vous donnera les login et les mots de passe à utiliser.<br/>"+
					" Je vous  conseille de suivre pas à pas les indications de cette page, <br/>"+
					"cela vous  permettra  de comprendre le fonctionnement  du logiciel";

		return str;
	}



	private String getUrl()
	{
		return 
				  "http://amapj.fr/docs_utilisateur_visite_guidee.html?"+
				  "d1="+getDate(dto.dateFinInscription)+"&"+
				  "d2="+getDate(dto.dateFinInscription)+"&"+
				  "d3="+getDate(dto.dateDebut)+"&"+
				  "d4="+getDate(dto.dateFin)+"&"+
				  "pass="+dto.password;
	}
	
	private String getDate(Date date)
	{
		SimpleDateFormat df = new SimpleDateFormat("EEEEE dd MMMMM yyyy");	
		String str = df.format(date);
		str = str.replaceAll(" ", "_");
		return StringUtils.sansAccent(str);
	}



	@Override
	protected void performSauvegarder() throws OnSaveException
	{
		new AppInstanceService().create(dto);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}