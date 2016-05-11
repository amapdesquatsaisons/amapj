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
 package fr.amapj.service.services.velocity;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.EntityManager;

import org.apache.velocity.VelocityContext;

import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratSummaryDTO;
import fr.amapj.service.services.mespaiements.MesPaiementsService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;


public class VCBuilder
{
	
	static public void addAmap(VelocityContext ctx)
	{
		ParametresDTO param = new ParametresService().getParametres();
		VCPersonne amap = new VCPersonne();
		
		amap.nom = param.nomAmap;
		amap.ville = param.villeAmap;
				
		ctx.put("amap", amap);
	}
	
	
	static public void addAmapien(VelocityContext ctx,Utilisateur u)
	{
		VCAmapien amapien = new VCAmapien();
		amapien.load(u);
				
		ctx.put("amapien", amapien);
	}
	
	static public void addReferent(VelocityContext ctx,Utilisateur u)
	{
		VCAmapien amapien = new VCAmapien();
		amapien.load(u);
				
		ctx.put("referent", amapien);	
	}
	
	static public void addProducteur(VelocityContext ctx,Producteur p)
	{
		VCProducteur prod = new VCProducteur();
		
		prod.nom = p.getNom();
		prod.libContrat = p.getLibContrat();
		
		ctx.put("paysan", prod);
	}
	
	static public void addContrat(VelocityContext ctx,Contrat c,EntityManager em)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		
		VCContrat cc = new VCContrat();
		
		ModeleContratSummaryDTO sum = new GestionContratService().createModeleContratInfo(em, c.getModeleContrat());
		ModeleContratDTO dto = new GestionContratService().loadModeleContrat(c.getModeleContrat().getId());
		
		cc.nom = dto.nom;
		cc.description = dto.description;
		cc.dateDebut = df.format(dto.dateDebut);
		cc.dateFin = df.format(dto.dateFin);
		cc.saison = getSaison(dto.dateDebut,dto.dateFin);
		cc.nbLivraison = ""+sum.nbLivraison;
		cc.libCheque = dto.libCheque;
		cc.nbCheque = ""+new MesPaiementsService().getNbChequeContrat(c, em);
		
		// TODO cc.tableauDateProduit;
		// TODO cc.tableauCheque;
				
		ctx.put("contrat", cc);
	}

	private static String getSaison(Date dateDebut, Date dateFin)
	{
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy");
		String s1 = df2.format(dateDebut);
		String s2 = df2.format(dateFin);
		
		if (s1.equals(s2))
		{
			return s1;
		}
		return s1+"-"+s2;
	}
	
	
	static public void addDateInfo(VelocityContext ctx)
	{
		VCDate date = new VCDate();
		ctx.put("date", date);
	}
	
	
	
}
