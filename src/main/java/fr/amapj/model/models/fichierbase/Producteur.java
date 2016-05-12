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
 package fr.amapj.model.models.fichierbase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.editionspe.EditionSpecifique;
import fr.amapj.model.models.param.EtatModule;

@Entity
@Table( uniqueConstraints=
		{
		   @UniqueConstraint(columnNames={"nom"})
		})
public class Producteur implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	@Column(length = 100)
	private String nom;
	
	
	@Size(min = 1, max = 2048)
	@Column(length = 2048)
	private String description;
	
	@NotNull
	private int delaiModifContrat;
	
	/**
	 * Type etiquette utilisé par ce producteur
	 * null si il n'utilise pas d'étiquette  
	 */
	@ManyToOne
    private EditionSpecifique etiquette;
	
	/**
	 * Type engagement utilisé par ce producteur
	 * null si il n'utilise pas d'engagement  
	 */
	@ManyToOne
    private EditionSpecifique engagement;
	
	// Libelle qui sera utilisé sur le contrat
	@Size(min = 0, max = 255)
	@Column(length = 255)
	private String libContrat;
	

	public enum P implements Mdm
	{ 
		ID("id") ,  NOM("nom") , DESCRIPTION("description") , DELAIMODIFCONTRAT("delaiModifContrat") ;
	
		private String propertyId;   
	   
	    P(String propertyId) 
	    {
	        this.propertyId = propertyId;
	    }
	    public String prop() 
	    { 
	    	return propertyId; 
	    }
	} ;

	
	
	
	// Getters ans setters
	
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

	

	public int getDelaiModifContrat()
	{
		return delaiModifContrat;
	}

	public void setDelaiModifContrat(int delaiModifContrat)
	{
		this.delaiModifContrat = delaiModifContrat;
	}

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public EditionSpecifique getEtiquette()
	{
		return etiquette;
	}

	public void setEtiquette(EditionSpecifique etiquette)
	{
		this.etiquette = etiquette;
	}

	public EditionSpecifique getEngagement()
	{
		return engagement;
	}

	public void setEngagement(EditionSpecifique engagement)
	{
		this.engagement = engagement;
	}

	public String getLibContrat()
	{
		return libContrat;
	}

	public void setLibContrat(String libContrat)
	{
		this.libContrat = libContrat;
	}

	
	
	
	
}
