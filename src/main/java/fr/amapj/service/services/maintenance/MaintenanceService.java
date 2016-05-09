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
 package fr.amapj.service.services.maintenance;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.DateUtils;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.modele.ModeleContratDatePaiement;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.mescontrats.MesContratsService;

/**
 * Permet la gestion des contrats
 * 
 *  
 *
 */
public class MaintenanceService
{
	public MaintenanceService()
	{

	}



	// PARTIE SUPPRESSION D'UN MODELE DE CONTRAT ET DE TOUS LES CONTRATS ASSOCIES

	/**
	 * Permet de supprimer un modele de contrat et TOUS les contrats associées
	 * Ceci est fait dans une transaction en ecriture  
	 */
	@DbWrite
	public void deleteModeleContratAndContrats(Long modeleContratId)
	{
		EntityManager em = TransactionHelper.getEm();
		
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		List<Contrat> cs = getAllContrats(em, mc);
		
		for (Contrat contrat : cs)
		{
			new MesContratsService().deleteContrat(contrat.getId());
		}
		
		new GestionContratService().deleteContrat(modeleContratId);
		
	}

	
	/**
	 * 
	 */
	private List<Contrat> getAllContrats(EntityManager em, ModeleContrat mc)
	{
		Query q = em.createQuery("select c from Contrat c WHERE c.modeleContrat=:mc");
		q.setParameter("mc",mc);
		List<Contrat> cs = q.getResultList();
		return cs;
	}
	

	
	/**
	 * Permet de vider le cache de la base
	 * Ceci est fait dans une transaction en ecriture  
	 * obligatoire après requete SQL manuelle
	 */
	@DbWrite
	public void resetDatabaseCache()
	{
		EntityManager em = TransactionHelper.getEm();
		em.getEntityManagerFactory().getCache().evictAll();
	}

	
		
	
}
