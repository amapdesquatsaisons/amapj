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
 package fr.amapj.view.views.common.contratselector;

public interface IContratSelectorPart
{
	
	/**
	 * Permet d'activer ou de désactiver toute la barre des boutons
	 * 
	 */
	public void buttonBarFull(boolean enable);
	
	/**
	 * Permet d'activer ou de désactiver les boutons de la barre 
	 * qui sont relatifs au mode édition, c'est à dire les boutons 
	 * Edit et Delete
	 */
	public void buttonBarEditMode(boolean enable);
	
	public void refreshTable();
	
}
