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
 package fr.amapj.service.services.excelgenerator;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.reel.Contrat;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.generator.excel.AbstractExcelGenerator;
import fr.amapj.service.engine.generator.excel.ExcelFormat;
import fr.amapj.service.engine.generator.excel.ExcelGeneratorTool;


/**
 * Permet l'impression de la liasse des contrat pour tous les utilisateurs d'un contrat
 * 
 *  
 *
 */
public class EGLiasseContratUtilisateur  extends AbstractExcelGenerator
{
	
	Long modeleContratId;
	
	public EGLiasseContratUtilisateur(Long modeleContratId)
	{
		this.modeleContratId = modeleContratId;
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{	
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		
		List<Contrat> contrats = getContrat(em, mc);
		
		for (Contrat contrat : contrats)
		{
			Utilisateur u = contrat.getUtilisateur();
			new EGContratUtilisateur(contrat.getId()).addOnePage(em, et, u.getNom()+" "+u.getPrenom());
		}
		
	}

	

	/**
	 * Retrouve la liste de tous les contrats pour ce modele, triés par nom prenom de l'utilisateur 
	 * 
	 */
	private List<Contrat> getContrat(EntityManager em, ModeleContrat mc)
	{
		// On récupère ensuite la liste de tous les contrats de ce modele de contrat, trié par nom d'utilisateur
		Query q = em.createQuery("select c from Contrat c WHERE c.modeleContrat=:mc order by c.utilisateur.nom, c.utilisateur.prenom");
		q.setParameter("mc",mc);
		List<Contrat> cs = q.getResultList();
		return cs;
	}

	@Override
	public String getFileName(EntityManager em)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		return "liasse-contrat-"+mc.getNom();
	}


	@Override
	public String getNameToDisplay(EntityManager em)
	{
		ModeleContrat mc = em.find(ModeleContrat.class, modeleContratId);
		return "la liasse des contrats signés";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}
	
	

	public static void main(String[] args) throws IOException
	{
		new EGLiasseContratUtilisateur(8342L).test(); 
	}

}
