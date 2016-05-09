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
 package fr.amapj.view.engine.searcher;

import java.util.List;

import fr.amapj.model.engine.Identifiable;


/**
 * Un searcher doit pouvoir :
 * - récupérer tous les élements
 * - les afficher 
 * - accepter des paramètres en entrée pour les requetes
 */
public interface SearcherDefinition 
{
	public String getTitle();
	
	
	/**
	 * Indique si ce searcher a besoin de paramètres ou non 
	 * @return
	 */
	public boolean needParams();
	
	
	public List<? extends Identifiable> getAllElements(Object params);
	
	/**
	 * Si cette méthode retourne une valeur non nulle, alors la méthode
	 * toString(Identifiable identifiable) n'est jamais appelée
	 * 
	 */
	public String getPropertyId();
	
	/**
	 * 
	 */
	public String toString(Identifiable identifiable);
	
	/**
	 * Classe des élements de la liste 
	 * Peut retourner null dans le cas ou getPropertyId() retourne une valeur non nulle
	 */
	public Class getClazz();
	

}
