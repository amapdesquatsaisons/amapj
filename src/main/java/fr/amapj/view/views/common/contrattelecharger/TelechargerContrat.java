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
 package fr.amapj.view.views.common.contrattelecharger;

import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.service.services.excelgenerator.EGBilanCompletCheque;
import fr.amapj.service.services.excelgenerator.EGCollecteCheque;
import fr.amapj.service.services.excelgenerator.EGContratUtilisateur;
import fr.amapj.service.services.excelgenerator.EGFeuilleLivraison;
import fr.amapj.service.services.excelgenerator.EGLiasseContratUtilisateur;
import fr.amapj.service.services.excelgenerator.EGSyntheseContrat;
import fr.amapj.service.services.excelgenerator.EGUtilisateurContrat;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.pdfgenerator.PGEngagement;
import fr.amapj.service.services.producteur.ProducteurService;
import fr.amapj.view.engine.excelgenerator.TelechargerPopup;
import fr.amapj.view.engine.popup.PopupListener;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

public class TelechargerContrat
{

	/**
	 * Permet l'affichage d'un popup avec tous les fichiers à télécharger pour un modele de contrat
	 * 
	 * idContrat peut être null
	 */
	static public void displayPopupTelechargerContrat(Long idModeleContrat, Long idContrat,PopupListener listener)
	{
		TelechargerPopup popup = new TelechargerPopup("Télecharger ...");
		
		if (idContrat!=null)
		{
			popup.addGenerator(new EGContratUtilisateur(idContrat));
			popup.addSeparator();
		}
		
		popup.addGenerator(new EGFeuilleLivraison(idModeleContrat));
		
		// Ces documents sont utilisables uniquement si le modele de contrat gere les paiements 
		if (isWithPaiement(idModeleContrat))
		{
			popup.addGenerator(new EGCollecteCheque(idModeleContrat));
			popup.addGenerator(new EGBilanCompletCheque(idModeleContrat));
		}
		
		
		popup.addGenerator(new EGLiasseContratUtilisateur(idModeleContrat));
		popup.addGenerator(new EGUtilisateurContrat(idModeleContrat));
		popup.addGenerator(new EGSyntheseContrat(idModeleContrat));
		
		if (new EditionSpeService().needEngagement(idModeleContrat))
		{
			// TODO popup.addGenerator(new PGEngagement(idModeleContrat));
		}
				
		CorePopup.open(popup,listener);
	}

	private static boolean isWithPaiement(Long idModeleContrat) 
	{
		ModeleContratDTO dto = new GestionContratService().loadModeleContrat(idModeleContrat);
		return dto.gestionPaiement==GestionPaiement.GESTION_STANDARD;
	}
	
	
}
