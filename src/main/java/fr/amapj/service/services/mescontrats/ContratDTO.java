/*
 *  Copyright 2013-2016 Emmanuel BRUN (contact@amapj.fr)
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
 package fr.amapj.service.services.mescontrats;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;

import fr.amapj.view.engine.grid.integergrid.IntegerGridParam;

/**
 * 
 *
 */
public class ContratDTO
{
	public Long contratId;
	
	public Long modeleContratId;
	
	public String nom;
	
	public String description;

	public String nomProducteur;
	
	public Date dateFinInscription;
	
	public Date dateDebut;
	
	public Date dateFin;

	public int nbLivraison;
	
	public int nbProduit;
	
	public boolean isNew;
	
	// Caractéristiques des lignes
	public List<ContratLigDTO> contratLigs = new ArrayList<ContratLigDTO>();

	// Caractéristiques des colonnes
	public List<ContratColDTO> contratColumns = new ArrayList<ContratColDTO>();
	
	// Contient les quantites qte[numero_ligne][numero_colonne]
	public int[][] qte;
	
	// Contient toutes les cases qui sont exclues de la saisies
	// Si excluded est null : toutes les cases sont autorisées
	// Si une case est egale a true : alors la case est exclue
	public boolean[][] excluded;
	
	
	// Caractéristiques du paiement
	public InfoPaiementDTO paiement;
	
	/**
	 * @return true si toutes les quantités sont à zéro
	 */
	public boolean isEmpty()
	{
		for (int i = 0; i < contratLigs.size(); i++)
		{
			for (int j = 0; j < contratColumns.size(); j++)
			{
				if (qte[i][j]!=0)
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Indique si le contrat est modifiable
	 */
	public boolean isModifiable()
	{
		Date d = DateUtils.addHours(dateFinInscription,23);
		d = DateUtils.addMinutes(d, 59);
		return  d.after(new Date());
	}
	
	/**
	 * Methode utilitaire de copie
	 * 
	 * @param param
	 * @param contratDTO
	 */
	public void fillParam(IntegerGridParam param)
	{
		param.nbCol = this.contratColumns.size();
		param.nbLig = this.contratLigs.size();
		param.qte = this.qte;
		param.excluded = this.excluded;
		
		// tableau des prix
		param.prix = new int[param.nbCol];
		for (int j = 0; j < param.nbCol; j++)
		{
			param.prix[j] = this.contratColumns.get(j).prix;
		}
	}


}
