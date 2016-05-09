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
 package fr.amapj.service.services.historiquecontrats;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.service.services.mescontrats.MesContratsService;

/**
 * Permet la gestion des modeles de contrat
 * 
 *  
 *
 */
public class HistoriqueContratsService
{
	private final static Logger logger = LogManager.getLogger();

	public HistoriqueContratsService()
	{

	}

	
	// PARTIE HISTORIQUE DES CONTRATS
	
	/**
	 * Retourne la liste historique des contrats pour l'utilisateur courant
	 */
	@DbRead
	public List<HistoriqueContratDTO> getHistoriqueContrats(Long userId)
	{
		EntityManager em = TransactionHelper.getEm();
		
		MesContratsService mesContratsService = new MesContratsService();
		GestionContratSigneService gestionContratSigneService = new GestionContratSigneService();

		List<HistoriqueContratDTO> res = new ArrayList<>();

		Utilisateur user = em.find(Utilisateur.class, userId);

		// On récupère la liste de tous les contrats de cet utilisateur
		Query q = em.createQuery("select c from Contrat c WHERE c.utilisateur=:u");
		q.setParameter("u",user);
		
		List<Contrat> contrats = q.getResultList();

		for (Contrat contrat : contrats)
		{
			// Si ce contrat est en historique
			if (mesContratsService.isHistorique(contrat,em)==true)
			{
				ModeleContrat mc = contrat.getModeleContrat();
	
				// Appel du service modele de contrat pour avoir toutes les infos
				// sur ce modele de contrat
				ModeleContratSummaryDTO summaryDTO = new GestionContratService().createModeleContratInfo(em, mc);
	
				HistoriqueContratDTO dto = new HistoriqueContratDTO();
				
				dto.nomContrat = contrat.getModeleContrat().getNom();
				dto.nomProducteur = contrat.getModeleContrat().getProducteur().getNom();
				dto.dateDebut = summaryDTO.dateDebut;
				dto.dateFin = summaryDTO.dateFin;
				dto.dateCreation = contrat.getDateCreation();
				dto.dateModification = contrat.getDateModification();
				dto.montant = gestionContratSigneService.getMontant(em, contrat);
				
				dto.idContrat = contrat.getId();
				dto.idModeleContrat = mc.getId();
				dto.idUtilisateur = userId;
	
				res.add(dto);
			}
		}
		
		return res;
	}
	
	
	

}
