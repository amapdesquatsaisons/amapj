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
 package fr.amapj.service.services.produit;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.CollectionUtils;
import fr.amapj.common.DebugUtil;
import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;

/**
 * Permet la gestion des producteurs
 * 
 */
public class ProduitService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES PRODUITS
	
	/**
	 * Permet de charger un produit
	 */
	@DbRead
	public ProduitDTO getProduitDto(Long id)
	{	
		EntityManager em = TransactionHelper.getEm();
		
		if (id==null)
		{
			return new ProduitDTO();
		}
		
		Produit p = em.find(Produit.class, id);

		ProduitDTO dto = new ProduitDTO();
		
		dto.id = p.getId();
		dto.nom = p.getNom();
		dto.conditionnement = p.getConditionnement();
		dto.producteurId = p.getProducteur().getId();
		
		return dto;
		
	}

	
	

	/**
	 * Mise à jour ou création d'un produit
	 * @param dto
	 * @param create
	 */
	@DbWrite
	public void update(ProduitDTO dto,boolean create)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Produit p;
		
		if (create)
		{
			p = new Produit();
		}
		else
		{
			p = em.find(Produit.class, dto.id);
		}
		
		p.setNom(dto.nom);
		p.setConditionnement(dto.conditionnement);
		p.setProducteur(em.find(Producteur.class, dto.producteurId));
		
		if (create)
		{
			em.persist(p);
		}
	}



	/**
	 * Permet la suppression d'un produit
	 */
	@DbWrite
	public void deleteProduit(Long idItemToSuppress) throws UnableToSuppressException
	{
		EntityManager em = TransactionHelper.getEm();
		Produit p = em.find(Produit.class, idItemToSuppress);
		
		// 
		verifContrat(p,em);
		
		em.remove(p);
	}
	
	
	private void verifContrat(Produit p, EntityManager em) throws UnableToSuppressException
	{
		Query q = em.createQuery("select distinct(c.modeleContrat) from ModeleContratProduit c WHERE c.produit=:p");
		q.setParameter("p", p);
			
		List<ModeleContrat> mcs = q.getResultList();

		if (mcs.size()>0)
		{
			String str = CollectionUtils.asStdString(mcs, new CollectionUtils.ToString<ModeleContrat>()
			{
				@Override
				public String toString(ModeleContrat t)
				{
					return t.getNom();
				}
			});
			throw new UnableToSuppressException("Cet produit est présent dans "+mcs.size()+" contrats : "+str);
		}
	}
	
	
}
