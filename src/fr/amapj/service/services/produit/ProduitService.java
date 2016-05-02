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
 package fr.amapj.service.services.produit;

import javax.persistence.EntityManager;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Produit;

/**
 * Permet la gestion des producteurs
 * 
 */
public class ProduitService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES PRODUCTEURS
	
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
}
