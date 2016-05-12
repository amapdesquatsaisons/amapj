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
 package fr.amapj.model.samples.query;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.engine.tools.TestTools;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.fichierbase.Utilisateur;

/**
 * Utilisation de requete HSQL
 * 
 */
public class SelectDbUtilisateurHsql
{

	/**
	 * Permet de lister simplement tous les utilisteurs
	 */
	@DbRead
	public void listAllUser()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}
	}
	
	/**
	 * Permet de lister tous les utilisteurs ayant pour nom AA
	 */
	@DbRead
	public void listUserWithNameAA()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u WHERE u.nom=:nom");
		q.setParameter("nom","nom_a");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}
	}
	
	
	
	
	
	/**
	 * Retourne la liste des utilisateurs ayant au moins un contrat 
	 */
	@DbRead
	public void listUserWithAContrat()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u WHERE EXISTS (select c from Contrat c where c.utilisateur = u)");
		//q.setParameter("mc","nom_a");
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			System.out.println("Utilisateur: Nom ="+u.getNom()+" Prenom ="+u.getPrenom());
		}

	}
	
	@DbRead
	public void complexrequest() throws ParseException
	{
		EntityManager em = TransactionHelper.getEm();
		
		//
		Long idUtilisateur = new Long(1052);
		
		//
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
		List<Date> dates = new ArrayList<>();
		dates.add(df.parse("12/06/14"));
		dates.add(df.parse("19/06/14"));
		dates.add(df.parse("26/06/14"));
		dates.add(df.parse("03/07/14"));
		
		//
		Query q = em.createQuery("select distinct(c.modeleContratDate.dateLiv) from ContratCell c WHERE " +
				"c.contrat.utilisateur=:u and " +
				"c.modeleContratDate.dateLiv in :dates " +
				"order by c.modeleContratDate.dateLiv");
		q.setParameter("u", em.find(Utilisateur.class, idUtilisateur));
		q.setParameter("dates", dates);
		
		
		List<Date> ds = q.getResultList();
		System.out.println("");
		for (Date date : ds)
		{
			System.out.println("Date: Nom ="+df.format(date));
		}
		
	}
	
	
	
	@DbRead
	public void tochar_request() 
	{
		EntityManager em = TransactionHelper.getEm();
		
		//
		Query q = em.createNativeQuery("select to_char(l.dateIn,'YYYY-MM-DD') , count(l.id) , count(distinct(l.idUtilisateur,l.dbName)) , sum(l.activityTime) from LogAccess l group by to_char(l.dateIn,'YYYY-MM-DD')");
		
		
		List<Object[]> ds = q.getResultList();
		System.out.println("");
		for (Object[] s : ds)
		{
		
			System.out.println("Date: Nom ="+s[0].getClass()+"=="+s[1].getClass()+"=="+s[3].getClass());
		}
		
	}
	
	
	

	public static void main(String[] args)
	{
		TestTools.init();
		
		SelectDbUtilisateurHsql selectUtilisateur = new SelectDbUtilisateurHsql();
		System.out.println("Requete dans la base avec HSQL..");
		selectUtilisateur.tochar_request();
		System.out.println("Fin de la requete");

	}

}
