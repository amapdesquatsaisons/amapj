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
 package fr.amapj.service.services.listeadherents;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Permet d'afficher la liste des adherents
 * 
 *  
 *
 */
public class ListeAdherentsService
{

	public ListeAdherentsService()
	{

	}

	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES  ADHERENTS

	/**
	 *  Retourne la liste de tous les utilisateurs 
	 */
	@DbRead
	public List<Utilisateur> getAllUtilisateurs(boolean includeInactif)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q ;
		if (includeInactif)
		{
			q = em.createQuery("select u from Utilisateur u " +
					"order by u.nom,u.prenom");
		}
		else
		{
			q = em.createQuery("select u from Utilisateur u " +
					"where u.etatUtilisateur=:etat " +
					"order by u.nom,u.prenom");
				q.setParameter("etat", EtatUtilisateur.ACTIF);
		}
		
		List<Utilisateur> us = q.getResultList();
		return us;
	}
	
	/**
	 *  Retourne la liste de tous les emails des utilisateurs actifs 
	 */
	@DbRead
	public String getAllEmails()
	{
		StringBuffer buf = new StringBuffer();
		List<Utilisateur> us = getAllUtilisateurs(false);
		if (us.size()==0)
		{
			return "";
		}
		
	
		for (int i = 0; i < us.size()-1; i++)
		{
			Utilisateur u = us.get(i);
			buf.append(u.getEmail());
			buf.append(',');
		}
		
		Utilisateur u = us.get(us.size()-1);
		buf.append(u.getEmail());
		return buf.toString();
	}
	
	
	
	
}
