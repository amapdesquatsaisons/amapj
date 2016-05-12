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

import java.util.List;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.editionspe.TypEditionSpecifique;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.service.services.gestioncontratsigne.GestionContratSigneService;
import fr.amapj.view.engine.searcher.SearcherDefinition;

/**
 * Liste des étiquettes
 *
 */
public class SDEditionSpe implements SearcherDefinition
{
	
	private TypEditionSpecifique typEditionSpecifique;
	
	public SDEditionSpe(TypEditionSpecifique typEditionSpecifique)
	{
		this.typEditionSpecifique = typEditionSpecifique;
	}

	@Override
	public String getTitle()
	{
		switch (typEditionSpecifique)
		{
		case ETIQUETTE_PRODUCTEUR:
			return "Etiquette";
			
		case ENGAGEMENT:
			return "Contrat d'engagement";

		default:
			return "Edition spécifique";
		}
	}

	@Override
	public List<? extends Identifiable> getAllElements(Object params)
	{
		return  new EditionSpeService().getEtiquetteByType(typEditionSpecifique);
	}


	@Override
	public String toString(Identifiable identifiable)
	{
		EditionSpecifique spe = (EditionSpecifique) identifiable;
		return spe.getNom();
	}
	
	@Override
	public String getPropertyId()
	{
		return null;
	}

	@Override
	public Class getClazz()
	{
		return null;
	}

	@Override
	public boolean needParams()
	{
		return false;
	}

}
