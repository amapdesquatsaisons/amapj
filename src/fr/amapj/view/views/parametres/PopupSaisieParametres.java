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
 package fr.amapj.view.views.parametres;

import com.vaadin.data.util.BeanItem;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.RichTextArea;

import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;

/**
 * Permet à un utilisateur de mettre à jour ses coordonnées
 * 
 *
 */
@SuppressWarnings("serial")
public class PopupSaisieParametres extends WizardFormPopup
{

	private ParametresDTO dto;

	public enum Step
	{
		AMAP_INFO , MAIL_INFO , PERMANENCE_INFO , MAIL_PERIODIQUE , GESTION_COTISATION , ETIQUETTES ;
	}

	/**
	 * 
	 */
	public PopupSaisieParametres(ParametresDTO dto)
	{
		popupWidth = "80%";
		popupHeight = "60%";
		popupTitle = "Modification des paramètres";

		this.dto = dto;
		item = new BeanItem<ParametresDTO>(dto);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case AMAP_INFO:
			addFieldAmapInfo();
			break;

		case MAIL_INFO:
			addFieldMailInfo();
			break;
			
		case PERMANENCE_INFO:
			addFieldPermanenceInfo();
			break;
			
		case MAIL_PERIODIQUE:
			addFieldMailPeriodique();
			break;
			
		case GESTION_COTISATION:
			addFieldGestionCotisation();
			break;
			
		case ETIQUETTES:
			addFieldEtiquettes();
			break;
		
		}
	}
	
	

	private void addFieldAmapInfo()
	{
		// Titre
		setStepTitle("identification de l'AMAP");
		
		// 
		addTextField("Nom de l'AMAP", "nomAmap");
		
		addTextField("Ville de l'AMAP", "villeAmap");
		
	}
	
	private void addFieldMailInfo()
	{
		// Titre
		setStepTitle("information sur l'envoi des mails");
		
		addComboEnumField("Type du serveur de mail", "smtpType", new NotNullValidator());
		
		addTextField("Adresse mail qui enverra les messages", "sendingMailUsername");

		addPasswordTextField("Password de l'adresse mail qui enverra les messages", "sendingMailPassword");
		
		addTextField("URL de l'application vue par les utilisateurs", "url");
		
		addTextField("Adresse mail du destinataire des sauvegardes quotidiennes", "backupReceiver");
		
	}
	
	private void addFieldPermanenceInfo()
	{
		// Titre
		setStepTitle("module \"Gestion des permanences\"");
				
		// Champ 9
		addComboEnumField("Activation du module \"Gestion des permanences\"", "etatPlanningDistribution");
		
		addComboEnumField("Envoi d'un mail de rappel pour les permanences",  "envoiMailRappelPermanence");
		
		addIntegerField("Délai en jours entre la permanence et l'envoi du mail", "delaiMailRappelPermanence");
		
		addTextField("Titre du mail de rappel pour les permanences", "titreMailRappelPermanence");
		
		RichTextArea f =  addRichTextAeraField("Contenu du mail de rappel pour les permanences", "contenuMailRappelPermanence");
		f.setHeight(8, Unit.CM);

	}
	
	private void addFieldMailPeriodique()
	{
		// Titre
		setStepTitle("Envoi d'un mail périodique (tous les mois)");
				
		// Champ 9
		addComboEnumField("Activation de l'envoi d'un mail périodique (tous les mois)",  "envoiMailPeriodique");
		
		addIntegerField("Numéro du jour dans le mois où le mail sera envoyé", "numJourDansMois");
		
		addTextField("Titre du mail périodique", "titreMailPeriodique");
		
		RichTextArea f =  addRichTextAeraField("Contenu du mail périodique", "contenuMailPeriodique");
		f.setHeight(8, Unit.CM);

	}
	
	private void addFieldGestionCotisation()
	{
		// Titre
		setStepTitle("module \"Gestion des cotisations\"");
				
		// Champ 9
		addComboEnumField("Activation du module \"Gestion des cotisations\"",  "etatGestionCotisation");
		
	}
	
	private void addFieldEtiquettes()
	{
		// Titre
		setStepTitle("module \"Gestion des éditions spécifiques\"");
		
		String content =  	"Ce module vous permet d'activer plusieurs outils <br/><br/>"
					+ "1/la génération d'un planning mensuel<br/>"+
					"qui liste pour un mois complet les produits à livrer pour chaque amapien<br/>"+
					"Peut être utile si le nombre de produits n'est pas trop important<br/><br/>"+
					  "2/la génération d'étiquettes pour les producteurs";
		
		addLabel(content, ContentMode.HTML);
		
				
		// Champ 
		addComboEnumField("Activation du module \"Gestion des éditions spécifiques\"",  "etatEditionSpecifique");
		
	}

	

	@Override
	protected void performSauvegarder()
	{
		new ParametresService().update(dto);
	}


	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
}
