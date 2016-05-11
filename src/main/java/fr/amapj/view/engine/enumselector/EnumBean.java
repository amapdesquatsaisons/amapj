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
 package fr.amapj.view.engine.enumselector;

import java.util.List;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.model.models.fichierbase.Produit;
import fr.amapj.service.services.searcher.SearcherService;

public class EnumBean 
{

	private Enum value;

	public EnumBean(Enum value)
	{
		this.value = value;
	}
	
	public Enum getValue()
	{
		return value;
	}
	
	
	public String getLib()
	{
		if (value instanceof Mdm)
		{
			return  ( (Mdm)value).prop();
		}
			
			
		return value.toString();
	}
	
	public void setLib(String lib)
	{
		//
	}
	
	
	
}
