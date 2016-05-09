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
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.DateUtils;
import fr.amapj.common.LongUtils;
import fr.amapj.model.models.contrat.reel.ContratCell;
import fr.amapj.model.models.editionspe.AbstractEditionSpeJson;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.editionspe.planningmensuel.ParametresProduitsJson;
import fr.amapj.model.models.editionspe.planningmensuel.PlanningMensuelJson;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelFormat;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorTool;
import fr.amapj.service.services.saisiepermanence.PermanenceDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceService;


/**
 * Permet la generation d'un planning mensuel
 * 
 *  
 *
 */
public class EGPlanningMensuel extends AbstractExcelGenerator
{
	
	private Long editionSpecifiqueId;
	private Date month;
	private String suffix;
	
	public EGPlanningMensuel(Long editionSpecifiqueId,Date month,String suffix)
	{
		this.suffix = suffix;
		this.editionSpecifiqueId = editionSpecifiqueId;
		this.month = DateUtils.firstDayInMonth(month);
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		SimpleDateFormat df = new SimpleDateFormat("dd MMMMM");
		SimpleDateFormat df2 = new SimpleDateFormat("MMMMM yyyy");
		
		
		EditionSpecifique editionSpe = em.find(EditionSpecifique.class, editionSpecifiqueId);
		PlanningMensuelJson planningJson = (PlanningMensuelJson) AbstractEditionSpeJson.load(editionSpe);
		
		// Recherche de toutes les colonnes du document 
		Entete entete = getEntetePlanning(em,planningJson);
		
		// Recherche de tous les utilisateurs du document
		List<Utilisateur> utilisateurs = getUtilisateur(em);
		
		
		
		
		// Les colonnes en + sont le nom, prenom et telephone
		int nbCol =  entete.prodCols.size()+3;
		et.addSheet("Planning mensuel "+df2.format(month), nbCol, 25);
		et.setModePaysage();
		
		// Ecriture de la ligne des dates
		et.addRow();
		et.setCell(0, "DISTRIBUTIONS "+df2.format(month).toUpperCase(), et.grasCentreBordure);
		et.mergeCellsRight(0, 2);
		
		int index = 2;
		for (DateColonne dateCol : entete.dateCols)
		{
			et.setCell(index, df.format(dateCol.date), et.grasCentreBordure);
			et.mergeCellsRight(index, dateCol.nbColProduit);
			index = index + dateCol.nbColProduit;
		}
		
		et.setCell(index, "Téléphone portable ", et.grasCentreBordure);
		
		// Ecriture de la ligne des responsables de la distribution
		et.addRow();
		et.setRowHeigth(3);
		et.setCell(1, "Responsable de distribution", et.nongrasGaucheWrappe);
		
		index = 2;
		for (DateColonne dateCol : entete.dateCols)
		{
			et.setCell(index, dateCol.permanence, et.nonGrasCentreBordure);
			et.mergeCellsRight(index, dateCol.nbColProduit);
			index = index + dateCol.nbColProduit;
		}
		
		et.setCell(index, "", et.grasCentreBordure);
		
		// Ecriture de la ligne avec les noms des produits
		// et positionnement de toutes les largeurs
		et.addRow();
		et.setCell(0, "Nom", et.grasCentreBordure);
		et.setColumnWidthInMm(0, planningJson.getLgColNom());
		
		et.setCell(1, "Prénom", et.grasCentreBordure);
		et.setColumnWidthInMm(1, planningJson.getLgColPrenom());
		
		index = 2;
		for (ProduitColonne prodCol : entete.prodCols)
		{
			if (prodCol.idProduit!=null)
			{
				et.setCell(index, prodCol.nomColonne, et.grasCentreBordure);
				et.setColumnWidthInMm(index, prodCol.largeurColonne);
			}
			else
			{
				et.setCell(index, prodCol.nomColonne, et.grasCentreBordureColorPetit);
				et.setColumnWidthInMm(index, planningJson.getLgColPresence());
			}
			index++;
		}
		
		et.setCell(index, "", et.grasCentreBordure);
		et.mergeCellsUp(index, 3);
		et.setColumnWidthInMm(index, planningJson.getLgColnumTel1());
		
		//
		for (Utilisateur utilisateur : utilisateurs)
		{
			addRowUtilisateur(et,utilisateur,em,entete);
		}
		
	}
	
	

	private void addRowUtilisateur(ExcelGeneratorTool et, Utilisateur utilisateur, EntityManager em, Entete entete)
	{
		int[] qtes = getQte(utilisateur, em, entete.prodCols);
		
		et.addRow();
		et.setCell(0, utilisateur.getNom(), et.grasGaucheNonWrappeBordure);
		et.setCell(1, utilisateur.getPrenom(), et.nonGrasGaucheBordure);
		
		int index = 2;
		for (int i = 0; i < qtes.length; i++)
		{
			String str = qtes[i]>0 ? "X" : " ";
			ProduitColonne prodCol = entete.prodCols.get(i);
			
			if (prodCol.idProduit!=null)
			{
				et.setCell(index, str, et.grasCentreBordure);
			}
			else
			{
				// Colonne présence
				et.setCell(index, "", et.grasCentreBordureColor);
			}
				
			index++;
		}
		
		// Numéro de telephone
		et.setCell(index, utilisateur.getNumTel1(), et.nonGrasCentreBordure);
		
	}
	

	private int[] getQte(Utilisateur u,EntityManager em,List<ProduitColonne> prodCols)
	{
		Query q = em.createQuery("select c from ContratCell c WHERE "
				+ " c.modeleContratDate.dateLiv >= :d1 AND c.modeleContratDate.dateLiv<:d2 AND "
				+ "c.contrat.utilisateur=:u");
		
		Date d2 = DateUtils.addMonth(month,1);
		q.setParameter("d1",month);
		q.setParameter("d2",d2);
		q.setParameter("u",u);
		
		int[] res = new int[prodCols.size()];
		List<ContratCell> ccs = q.getResultList();
		for (ContratCell cc : ccs)
		{
			int index = findIndex(prodCols,cc);
			if (index!=-1)
			{
				res[index]=res[index]+cc.getQte();
			}
		}
		
		return res;
	}
	
	

	private int findIndex(List<ProduitColonne> prodCols, ContratCell cc)
	{
		int s = prodCols.size();
		for (int i = 0; i < s; i++)
		{
			ProduitColonne produitColonne= prodCols.get(i);
			
			if (	LongUtils.equals(produitColonne.idProduit,cc.getModeleContratProduit().getProduit().getId()) &&
					cc.getModeleContratDate().getDateLiv().equals(produitColonne.dateColonne.date))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Retourne la liste de tous les utilisateurs qui ont commandé au moins une fois dans le mois
	 * @param em
	 * @return
	 */
	private List<Utilisateur> getUtilisateur(EntityManager em)
	{
		Query q = em.createQuery("select distinct(c.contrat.utilisateur) from ContratCell c WHERE "
				+ " c.modeleContratDate.dateLiv >= :d1 AND c.modeleContratDate.dateLiv<:d2 "
				+ "ORDER BY c.contrat.utilisateur.nom , c.contrat.utilisateur.prenom");
		
		Date d2 = DateUtils.addMonth(month,1);
		q.setParameter("d1",month);
		q.setParameter("d2",d2);
		
		List<Utilisateur> us = q.getResultList();
		return us;
	}

	/**
	 * Calcul de l'entête du planning mensuel
	 * @param em
	 * @param planningJson
	 * @return
	 */
	private Entete getEntetePlanning(EntityManager em, PlanningMensuelJson planningJson)
	{
		Entete entete = new Entete();
		
		// Recherche des distributions dans ce mois
		List<PermanenceDTO> permanenceDTOs = getPermanence(em);
		
		// On recherche toutes les dates de livraisons sur ce mois, ordonnées par ordre croissant
		List<Date> dateLivs = getDateLivs(em);
		
		// Pour chaque date, on fait la liste des produits concernés
		for (Date dateLiv : dateLivs)
		{
			findProduits(em,entete,dateLiv,planningJson,permanenceDTOs);
		}
		
		// 
		return entete;
	}
	
	private List<PermanenceDTO> getPermanence(EntityManager em)
	{
		Date d2 = DateUtils.addMonth(month,1);
		return new PermanenceService().getAllDistributions(em, month, d2);
	}
	
	
	private List<Date> getDateLivs(EntityManager em)
	{
		Query q = em.createQuery("select distinct(mcd.dateLiv) from ModeleContratDate mcd WHERE mcd.dateLiv >= :d1 AND mcd.dateLiv<:d2 ORDER BY mcd.dateLiv");
		
		Date d2 = DateUtils.addMonth(month,1);
		q.setParameter("d1",month);
		q.setParameter("d2",d2);
		
		List<Date> us = q.getResultList();
		return us;
	}
	
	private void findProduits(EntityManager em, Entete entete, Date dateLiv, PlanningMensuelJson planningJson, List<PermanenceDTO> permanenceDTOs)
	{
		List<ProduitColonne> cols = computeProdCol(em,dateLiv,planningJson);
		
		// Si il n'y a pas de produit pour cette date, on retourne sans rien ajouter à l'entete
		if (cols.size()==0)
		{
			return ;
		}
		
		
		// Ajout de la colonne présence
		ProduitColonne prodCol = new ProduitColonne();
		prodCol.nomColonne = "Présence";
		cols.add(prodCol);
		
		//
		DateColonne dateCol = new DateColonne();
		dateCol.date = dateLiv;
		dateCol.nbColProduit = cols.size();
		dateCol.permanence = findPermanence(permanenceDTOs,dateLiv);
		
		//
		for (ProduitColonne produitColonne : cols)
		{
			produitColonne.dateColonne = dateCol;
		}
				
		entete.prodCols.addAll(cols);
		entete.dateCols.add(dateCol);
		
	}

	private List<ProduitColonne> computeProdCol(EntityManager em, Date dateLiv, PlanningMensuelJson planningJson)
	{
		List<ProduitColonne> res = new ArrayList<>();
		for (ParametresProduitsJson prodJson : planningJson.getParametresProduits())
		{
			Produit p = em.find(Produit.class, prodJson.getIdProduit());
			ProduitColonne prodCol = constructProdCol(em,p,dateLiv,prodJson);
			if (prodCol!=null)
			{
				res.add(prodCol);
			}
		}
		return res;
	}
	
	
	private ProduitColonne constructProdCol(EntityManager em, Produit p, Date dateLiv, ParametresProduitsJson prodJson)
	{
		Query q = em.createQuery("select count(c) from ContratCell c WHERE "
				+ " c.modeleContratProduit.produit=:p AND "
				+ " c.modeleContratDate.dateLiv=:d ");
		q.setParameter("p", p);
		q.setParameter("d", dateLiv);

		// Si pas de commande pour ce produit à cette date : on le supprime
		if(LongUtils.toInt(q.getSingleResult())==0)
		{
			return null;
		}
		ProduitColonne prodCol = new ProduitColonne();
		prodCol.idProduit = p.getId();
		prodCol.largeurColonne = prodJson.getLargeurColonne();
		prodCol.nomColonne = prodJson.getTitreColonne();
		
		return prodCol;
	}
	
	
	/**
	 * Calcul des informations de permanences
	 * @param permanenceDTOs
	 * @param dateLiv
	 * @return
	 */
	private String findPermanence(List<PermanenceDTO> permanenceDTOs, Date dateLiv)
	{
		for (PermanenceDTO permanenceDTO : permanenceDTOs)
		{
			if (permanenceDTO.datePermanence.equals(dateLiv))
			{
				return permanenceDTO.getUtilisateurs("\n");
			}
		}
		
		return "";
	}

	@Override
	public String getFileName(EntityManager em)
	{
		SimpleDateFormat df = new SimpleDateFormat("MMMMM-yyyy");
			
		return "planning-mensuel-"+df.format(month)+suffix;
	}

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		SimpleDateFormat df = new SimpleDateFormat("MMMMM yyyy");
		String str = "le planning mensuel de "+df.format(month);
		if ( (suffix!=null) && (suffix.length()>0) )
		{
			str = str +"("+suffix+")";
		}
		return str;
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}


	
	
	static public class ProduitColonne
	{
		public String nomColonne;
		
		public int largeurColonne;
		
		// est égal à null pour la colonne Présence
		public Long idProduit;
		
		public DateColonne dateColonne;
		

	}
	
	static public class DateColonne
	{
		public Date date;
		
		// Nombre de colonne produit pour cette date donnée
		public int nbColProduit;
		
		public String permanence;
	}
	
	
	static public class Entete
	{
		public List<ProduitColonne> prodCols = new ArrayList<>();
		
		public List<DateColonne> dateCols = new ArrayList<>();
		
	}
	
	
	public static void main(String[] args) throws IOException
	{
		Date d = DateUtils.addMonth(new Date(), 1);
		new EGPlanningMensuel(10301L,d,"").test();
	}
	

}