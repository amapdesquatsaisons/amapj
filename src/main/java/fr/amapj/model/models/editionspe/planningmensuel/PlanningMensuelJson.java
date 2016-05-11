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
 package fr.amapj.model.models.editionspe.planningmensuel;

import java.util.ArrayList;
import java.util.List;

import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.editionspe.AbstractEditionSpeJson;

/**
 * Paramétrage de l'édition planning mensuel 
 *
 */
public class PlanningMensuelJson extends AbstractEditionSpeJson
{
	
	
	private List<ParametresProduitsJson> parametresProduits = new ArrayList<ParametresProduitsJson>();

	// Largeur en mm pour la colonne Nom
	private int lgColNom;
	
	// Largeur en mm pour la colonne Prenom
	private int lgColPrenom;

	// Largeur en mm pour la colonne Presence
	private int lgColPresence;

	// Largeur en mm pour la colonne Telephone
	private int lgColnumTel1;
	
	// Role pouvant accéder à ce planning 
	private RoleList accessibleBy = RoleList.ADHERENT;
	
	//
	private TypPlanning typPlanning = TypPlanning.MENSUEL;

	

	public List<ParametresProduitsJson> getParametresProduits()
	{
		return parametresProduits;
	}


	public void setParametresProduits(List<ParametresProduitsJson> parametresProduits)
	{
		this.parametresProduits = parametresProduits;
	}


	public int getLgColNom()
	{
		return lgColNom;
	}


	public void setLgColNom(int lgColNom)
	{
		this.lgColNom = lgColNom;
	}


	public int getLgColPrenom()
	{
		return lgColPrenom;
	}


	public void setLgColPrenom(int lgColPrenom)
	{
		this.lgColPrenom = lgColPrenom;
	}


	public int getLgColPresence()
	{
		return lgColPresence;
	}


	public void setLgColPresence(int lgColPresence)
	{
		this.lgColPresence = lgColPresence;
	}


	public int getLgColnumTel1()
	{
		return lgColnumTel1;
	}


	public void setLgColnumTel1(int lgColnumTel1)
	{
		this.lgColnumTel1 = lgColnumTel1;
	}


	public RoleList getAccessibleBy()
	{
		return accessibleBy;
	}


	public void setAccessibleBy(RoleList accessibleBy)
	{
		this.accessibleBy = accessibleBy;
	}


	public TypPlanning getTypPlanning()
	{
		return typPlanning;
	}


	public void setTypPlanning(TypPlanning typPlanning)
	{
		this.typPlanning = typPlanning;
	}


	
	
}
