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
 package fr.amapj.model.models.fichierbase;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;

/**
 * Liste des référents d'un producteur
 *
 */
@Entity
public class ProducteurReferent implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;	
	
	@NotNull
	@ManyToOne
	private Producteur producteur;
	
	@NotNull
	@ManyToOne
	private Utilisateur referent;
	
	
	public enum P implements Mdm
	{ 
		ID("id") ,  REFERENT("referent") ,  PRODUCTEUR("producteur")  ;
	
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

	public Producteur getProducteur()
	{
		return producteur;
	}

	public void setProducteur(Producteur producteur)
	{
		this.producteur = producteur;
	}

	public Utilisateur getReferent()
	{
		return referent;
	}

	public void setReferent(Utilisateur referent)
	{
		this.referent = referent;
	}
	
	
}
