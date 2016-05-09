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
 package fr.amapj.model.models.cotisation;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fr.amapj.model.engine.Identifiable;

@Entity
@Table( uniqueConstraints=
{
   @UniqueConstraint(columnNames={"nom"})
})
public class PeriodeCotisation implements Identifiable
{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Size(min = 1, max = 100)
	private String nom;
	
	@NotNull
	// Montant mini 
	private int montantMini;
	
	@NotNull
	// Montant conseillé
	private int montantConseille;
	
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateFinInscription;
	
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateDebut;
	
	@NotNull
	@Temporal(TemporalType.DATE)
	private Date dateFin;
	
	
	// Paiement
	
	@Size(min = 0, max = 2048)
	// Texte qui sera affiché pour le paiement
	private String textPaiement;
	
	// Libellé du chéque 
	@Size(min = 0, max = 100)
	private String libCheque;
	
	// Date de remise des chéques
	@Temporal(TemporalType.DATE)
	private Date dateRemiseCheque;

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

	public int getMontantMini()
	{
		return montantMini;
	}

	public void setMontantMini(int montantMini)
	{
		this.montantMini = montantMini;
	}

	public int getMontantConseille()
	{
		return montantConseille;
	}

	public void setMontantConseille(int montantConseille)
	{
		this.montantConseille = montantConseille;
	}


	public Date getDateFinInscription()
	{
		return dateFinInscription;
	}

	public void setDateFinInscription(Date dateFinInscription)
	{
		this.dateFinInscription = dateFinInscription;
	}

	public String getTextPaiement()
	{
		return textPaiement;
	}

	public void setTextPaiement(String textPaiement)
	{
		this.textPaiement = textPaiement;
	}

	public String getLibCheque()
	{
		return libCheque;
	}

	public void setLibCheque(String libCheque)
	{
		this.libCheque = libCheque;
	}

	public Date getDateRemiseCheque()
	{
		return dateRemiseCheque;
	}

	public void setDateRemiseCheque(Date dateRemiseCheque)
	{
		this.dateRemiseCheque = dateRemiseCheque;
	}

	public Date getDateDebut()
	{
		return dateDebut;
	}

	public void setDateDebut(Date dateDebut)
	{
		this.dateDebut = dateDebut;
	}

	public Date getDateFin()
	{
		return dateFin;
	}

	public void setDateFin(Date dateFin)
	{
		this.dateFin = dateFin;
	}
	
	
	

}