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
 package fr.amapj.view.views.searcher;

import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.cotisation.PeriodeCotisation;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.editionspe.TypEditionSpecifique;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.view.engine.searcher.BasicSearcher;
import fr.amapj.view.engine.searcher.SearcherDefinition;


/**
 * Contient la liste de tous les searchers de l'application 
 *
 */
public class SearcherList 
{
	
	static public SearcherDefinition MODELE_CONTRAT = new BasicSearcher("Modéle de contrat", ModeleContrat.class, ModeleContrat.P.NOM);
	
	static public SearcherDefinition PRODUCTEUR = new BasicSearcher("Producteur", Producteur.class, Producteur.P.NOM);
	
	static public SearcherDefinition PERIODE_COTISATION = new BasicSearcher("Période de cotisation", PeriodeCotisation.class, "nom");
	
	static public SearcherDefinition ETIQUETTE = new SDEditionSpe(TypEditionSpecifique.ETIQUETTE_PRODUCTEUR);
	
	static public SearcherDefinition ENGAGEMENT = new SDEditionSpe(TypEditionSpecifique.ENGAGEMENT);
	
	// Searcher d'un produit lié à un producteur
	// Permet de lister tous les produits d'un producteur donné
	static public SearcherDefinition PRODUIT = new SDProduit();
	
	// Permet de lister tous les produits de la base, sous la forme Producteur - Produit 
	static public SearcherDefinition PRODUIT_ALL = new SDProduitAll();
	
	static public SearcherDefinition UTILISATEUR_SANS_CONTRAT = new SDUtilisateurSansContrat();
	
	static public SearcherDefinition UTILISATEUR_SANS_ADHESION = new SDUtilisateurSansAdhesion();
	
	static public SearcherDefinition UTILISATEUR_ACTIF = new SDUtilisateur();
	
	static public SearcherDefinition UTILISATEUR_TOUS = new SDUtilisateurTous();
		
}
