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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.CollectionUtils;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.contrat.modele.ModeleContratDate;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.editionspe.AbstractEditionSpeJson;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.editionspe.etiquette.EtiquetteProducteurJson;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;


/**
 * Permet la generation des etiquettes de distribution au format excel
 * 
 *  
 *
 */
public class EGEtiquetteDistribution extends AbstractExcelGenerator
{
	
	private Long modeleContratDateId;
	
	public EGEtiquetteDistribution(Long modeleContratDateId)
	{
		this.modeleContratDateId = modeleContratDateId;
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd MMMMM yyyy");
		SimpleDateFormat df2 = new SimpleDateFormat("dd MMMMM");
		
		ModeleContratDate mcd = em.find(ModeleContratDate.class, modeleContratDateId);
		EditionSpecifique editionSpe = mcd.getModeleContrat().getProducteur().getEtiquette();
		EtiquetteProducteurJson etiquette = (EtiquetteProducteurJson) AbstractEditionSpeJson.load(editionSpe);
		
		// Recherche de toutes les quantités à livrer 
		List<ContratCellNumber> cells = getContratCell(em);
		
		
		
		// Il y a nbCol colonnes
		int nbCol =  etiquette.getNbColonne();
		et.addSheet("Etiquettes-"+df2.format(mcd.getDateLiv()), nbCol, 10);
		for (int i = 0; i < nbCol; i++)
		{
			et.setColumnWidthInMm(i, etiquette.getLargeurColonnes().get(i).getLargeur());	
		}
		
		
		// Les marges sont aussi précisées dans Etiquette
		et.setMargin(etiquette.getMargeGauche(),etiquette.getMargeDroite(),etiquette.getMargeHaut(),etiquette.getMargeBas());
			
		// On découpe la liste en n sous listes contenant au maximum nbCol elements
		List<List<ContratCellNumber>> lines = CollectionUtils.cutInSubList(cells, nbCol);
		
		
		for (List<ContratCellNumber> line : lines)
		{
			processOneLine(line,et,df,etiquette);
		}	

	}
	
	static private class ContratCellNumber
	{
		public ContratCell cell;
		
		public int number;
		
		public int totalNumber;
	}

	private List<ContratCellNumber> getContratCell(EntityManager em)
	{
		ModeleContratDate mcDate = em.find(ModeleContratDate.class, modeleContratDateId);
		
		Query q = em.createQuery("select c from ContratCell c WHERE " +
				"c.modeleContratDate=:mcDate "+
				"order by c.contrat.utilisateur.nom , c.contrat.utilisateur.prenom");
		q.setParameter("mcDate", mcDate);
		
		List<ContratCellNumber> ret =new ArrayList<>();
		List<ContratCell> cells = q.getResultList();
		for (ContratCell cell : cells)
		{
			int qte =  cell.getQte();
			for (int i = 1; i <=qte; i++)
			{
				ContratCellNumber ccn = new ContratCellNumber();
				ccn.cell = cell;
				ccn.number = i;
				ccn.totalNumber = qte;
				
				ret.add(ccn);
			}
		}
		
		return ret;
	}

	private void processOneLine(List<ContratCellNumber> line, ExcelGeneratorTool et,SimpleDateFormat df, EtiquetteProducteurJson etiquette)
	{ 
		et.addRow();
		et.setRowHeigthInMm(etiquette.getHauteur());
		int index =0;
		for (ContratCellNumber cell : line)
		{
			String content = getContent(cell,df);
			et.setCell(index,content , et.grasCentreBordure);
			index++;
		}
	}



	private String getContent(ContratCellNumber ccn, SimpleDateFormat df)
	{
		ContratCell cell = ccn.cell;
		
		// Ligne 1 : la date 
		String str = df.format(cell.getModeleContratDate().getDateLiv()) +"\n";
		
		// Ligne 2 : nom et prénom
		Utilisateur u = cell.getContrat().getUtilisateur();
		str = str + u.getNom()+" "+u.getPrenom()+"\n";
		
		// Ligne 3 : le nom du produit 
		Produit p = cell.getModeleContratProduit().getProduit();
		str = str + p.getNom()+","+p.getConditionnement()+"\n";
		
		// Ligne 4 : les numéros d'ordre
		str = str + ccn.number+" / "+ccn.totalNumber+"\n";
		
		return str;
	}

	@Override
	public String getFileName(EntityManager em)
	{
		ModeleContratDate mcDate = em.find(ModeleContratDate.class, modeleContratDateId);
		ModeleContrat mc = mcDate.getModeleContrat();
		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			
		return "etiquette-distribution-"+mc.getNom()+"-"+df.format(mcDate.getDateLiv());
	}

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "les étiquettes de distributions";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}

	public static void main(String[] args) throws IOException
	{
		new EGEtiquetteDistribution(11L).test();
	}

}