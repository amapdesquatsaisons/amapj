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
 package fr.amapj.view.engine.menu;
/**
 * Contient la liste des Menus disponibles dans l'application
 * 
 * Si le titre n'est pas précisé, alors c'est simplement le nom de l'entrée
 * 
 */
public enum MenuList
{ 	
	
	// Partie standard
	
	MES_CONTRATS("Mes contrats"),
	
	MES_LIVRAISONS("Mes livraisons"),
	
	MES_PAIEMENTS("Mes paiements"),
	
	MON_COMPTE("Mon compte"),
	
	LISTE_PRODUCTEUR_REFERENT("Producteurs / Référents"),
	
	LISTE_ADHERENTS("Liste des adhérents"),
	
	PLANNING_DISTRIBUTION("Planning des permanences"),
	
	HISTORIQUE_CONTRATS("Historique de mes contrats"),
	
	HISTORIQUE_PAIEMENTS("Historique de mes paiements"),
	
	
	// Partie producteur
	
	LIVRAISONS_PRODUCTEUR("Livraisons d'un producteur") ,
	
	CONTRATS_PRODUCTEUR("Contrats d'un producteur"),
	
	
	// Partie référents
	
	GESTION_CONTRAT("Gestion des contrats vierges"),
	
	GESTION_CONTRAT_SIGNES("Gestion des contrats signés"),
	
	RECEPTION_CHEQUES("Réception des chèques"),
	
	REMISE_PRODUCTEUR("Remise aux producteurs"),
	
	PRODUIT("Gestion des produits") ,
	
	SAISIE_PLANNING_DISTRIBUTION("Planification des permanences") ,
	
	// Partie trésorier
	
	UTILISATEUR("Gestion des utilisateurs") , 
	
	PRODUCTEUR("Gestion des producteurs") ,
	
	BILAN_COTISATION("Bilan des cotisations") ,
	
	RECEPTION_COTISATION("Réception des cotisations") ,
	
	IMPORT_DONNEES("Import des données"),
	
	LISTE_TRESORIER("Liste des trésoriers"),
	
	ETIQUETTE("Editions spécifiques"),
	
	
	// Partie admnistrateur
	
	PARAMETRES("Paramètres généraux"),
	
	LISTE_ADMIN("Liste des administrateurs"),
	
	MAINTENANCE("Maintenance"),
	
	ENVOI_MAIL("Envoyer un mail"),
	
	// Partie Saas

	LISTE_APP_INSTANCE("Liste des instances"),
	
	SUIVI_ACCES("Suivi accès"), 
	
	VISU_LOG("Visualisation des logs"),
	
	STAT_ACCES("Statistiques des accès")
	
	;


	
	
	
	private String title;   
	   
	MenuList(String title) 
    {
        this.title = title;
    }
    public String getTitle() 
    { 
    	if (title==null)
    	{
    		return name();
    	}
    	else
    	{
    		return title;
    	}
    	 
    }
	
}
