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
 package fr.amapj.model.models.editionspe;

import com.google.gson.Gson;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.models.editionspe.engagement.EngagementJson;
import fr.amapj.model.models.editionspe.etiquette.EtiquetteProducteurJson;
import fr.amapj.model.models.editionspe.planningmensuel.PlanningMensuelJson;
import fr.amapj.service.services.editionspe.EditionSpeDTO;


public class AbstractEditionSpeJson implements Identifiable
{

	transient private Long id;

	transient private String nom;
	
	transient private TypEditionSpecifique typEditionSpecifique;
	
	
	public AbstractEditionSpeJson()
	{
	}
	
	
	/**
	 * Permet de transformer un EditionSpeDTO en un objet JSON 
	 * @param p
	 * @return
	 */
	static public AbstractEditionSpeJson load(EditionSpeDTO p)
	{
		Class clazz = findClazz(p.typEditionSpecifique);
	
		
		AbstractEditionSpeJson etiquetteDTO = (AbstractEditionSpeJson) new Gson().fromJson(p.content, clazz);
		etiquetteDTO.setId(p.id);
		etiquetteDTO.setNom(p.nom);
		etiquetteDTO.setTypEditionSpecifique(p.typEditionSpecifique);
		
		return etiquetteDTO;
	}
	
	static public AbstractEditionSpeJson load(EditionSpecifique p)
	{
		Class clazz = findClazz(p.getTypEditionSpecifique());
	
		
		AbstractEditionSpeJson etiquetteDTO = (AbstractEditionSpeJson) new Gson().fromJson(p.getContent(), clazz);
		etiquetteDTO.setId(p.getId());
		etiquetteDTO.setNom(p.getNom());
		etiquetteDTO.setTypEditionSpecifique(p.getTypEditionSpecifique());
		
		return etiquetteDTO;
	}
	

	private static Class findClazz(TypEditionSpecifique typ)
	{
		switch (typ)
		{
		case ETIQUETTE_PRODUCTEUR:
			return EtiquetteProducteurJson.class;
			
		case PLANNING_MENSUEL:
			return PlanningMensuelJson.class;
			
		case ENGAGEMENT:
			return EngagementJson.class;
		
		default:
			throw new AmapjRuntimeException("Type non pris en compte");
		}
	}
	
	
	/**
	 * Permet de transformer l'objet courant en un objet EditionSpeDTO
	 * @return
	 */
	public EditionSpeDTO save()
	{
	
		EditionSpeDTO editionSpeDTO = new EditionSpeDTO();
		editionSpeDTO.id = id;
		editionSpeDTO.nom = nom;
		editionSpeDTO.typEditionSpecifique = typEditionSpecifique;
		Gson gson = new Gson();
		
		editionSpeDTO.content = gson.toJson(this);
		
		return editionSpeDTO;
	}
	
	
	

	public Long getId()
	{
		return id;
	}


	public void setId(Long id)
	{
		this.id = id;
	}


	public String getNom()
	{
		return nom;
	}


	public void setNom(String nom)
	{
		this.nom = nom;
	}


	
	
	public TypEditionSpecifique getTypEditionSpecifique()
	{
		return typEditionSpecifique;
	}


	public void setTypEditionSpecifique(TypEditionSpecifique typEditionSpecifique)
	{
		this.typEditionSpecifique = typEditionSpecifique;
	}

	
}
