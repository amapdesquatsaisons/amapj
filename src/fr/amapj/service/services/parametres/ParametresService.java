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
 package fr.amapj.service.services.parametres;

import javax.persistence.EntityManager;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.param.Parametres;

/**
 * 
 * 
 */
public class ParametresService
{
	
	static private Long ID_PARAM = new Long(1);
	
	// PARTIE REQUETAGE 
	
	/**
	 * Permet de charger les paramètres
	 */
	@DbRead
	public ParametresDTO getParametres()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Parametres param = em.find(Parametres.class, ID_PARAM);
		
		if (param==null)
		{
			throw new RuntimeException("Il faut insérer les paramètres généraux dans la base");
		}
		
		ParametresDTO dto = new ParametresDTO();
		dto.nomAmap = param.getNomAmap();
		dto.villeAmap = param.getVilleAmap();
		dto.smtpType = param.getSmtpType();
		dto.sendingMailUsername = param.getSendingMailUsername();
		dto.sendingMailPassword = param.getSendingMailPassword();
		dto.url = param.getUrl();
		dto.backupReceiver = param.getBackupReceiver();
		
		dto.etatPlanningDistribution = param.getEtatPlanningDistribution();
		dto.etatGestionCotisation = param.getEtatGestionCotisation();
		dto.delaiMailRappelPermanence = param.getDelaiMailRappelPermanence();
		dto.envoiMailRappelPermanence = param.getEnvoiMailRappelPermanence();
		dto.titreMailRappelPermanence = param.getTitreMailRappelPermanence();
		dto.contenuMailRappelPermanence = param.getContenuMailRappelPermanence();
		
		dto.envoiMailPeriodique = param.getEnvoiMailPeriodique();
		dto.numJourDansMois = param.getNumJourDansMois();
		dto.titreMailPeriodique = param.getTitreMailPeriodique();
		dto.contenuMailPeriodique = param.getContenuMailPeriodique();
		
		dto.etatEditionSpecifique = param.getEtatEditionSpecifique();
			
		// Champs calculés
		dto.serviceMailActif = false;
		if ((param.getSendingMailUsername()!=null) && (param.getSendingMailUsername().length()>0))
		{
			dto.serviceMailActif = true;
		}
		
		return dto;
		
	}




	// PARTIE MISE A JOUR 

	
	@DbWrite
	public void update(final ParametresDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Parametres param = em.find(Parametres.class, ID_PARAM);
		
		param.setNomAmap(dto.nomAmap);
		param.setVilleAmap(dto.villeAmap);
		param.setSmtpType(dto.smtpType);
		param.setSendingMailUsername(dto.sendingMailUsername);
		param.setSendingMailPassword(dto.sendingMailPassword);
		param.setUrl(dto.url);
		param.setBackupReceiver(dto.backupReceiver);
		
		param.setEtatPlanningDistribution(dto.etatPlanningDistribution);
		param.setEtatGestionCotisation(dto.etatGestionCotisation);
		param.setDelaiMailRappelPermanence(dto.delaiMailRappelPermanence);
		param.setEnvoiMailRappelPermanence(dto.envoiMailRappelPermanence);
		param.setTitreMailRappelPermanence(dto.titreMailRappelPermanence);
		param.setContenuMailRappelPermanence(dto.contenuMailRappelPermanence);
		
		param.setEnvoiMailPeriodique(dto.envoiMailPeriodique);
		param.setNumJourDansMois(dto.numJourDansMois);
		param.setTitreMailPeriodique(dto.titreMailPeriodique);
		param.setContenuMailPeriodique(dto.contenuMailPeriodique);
		
		param.setEtatEditionSpecifique(dto.etatEditionSpecifique);
		
		
	}

}
