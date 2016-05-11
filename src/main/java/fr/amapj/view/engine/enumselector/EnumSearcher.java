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
 package fr.amapj.view.engine.enumselector;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.AbstractSelect.ItemCaptionMode;
import com.vaadin.ui.ComboBox;

import fr.amapj.common.AmapjRuntimeException;

public class EnumSearcher 
{

	
	/**
	 * Permet de créer une combo box permettant de choisir parmi une liste de Enum
	 *  
	 * @param binder
	 * @param title
	 * @param enumeration donne la liste à afficher
	 * @param propertyId
	 * @return
	 */
	static public ComboBox createEnumSearcher(FieldGroup binder,String title,String propertyId,Enum...enumsToExcludes)
	{
		Class enumeration = binder.getItemDataSource().getItemProperty(propertyId).getType();
		if (enumeration.isEnum()==false)
		{
			throw new AmapjRuntimeException("Le champ "+title+" n'est pas de type enum");
		}
		
		
		List<EnumBean> beans = new ArrayList<>();
		EnumSet enums = EnumSet.allOf(enumeration);
		for (Object enum1 : enums)
		{
			Enum en = (Enum) enum1;
			if (isAllowed(en,enumsToExcludes))
			{
				beans.add(new EnumBean(en));
			}
		}
			
		
		BeanItemContainer<EnumBean> container = new BeanItemContainer(EnumBean.class, beans);
		
		
		ComboBox comboBox = new ComboBox(title,container);
		
		comboBox.setConverter(new ComboBoxEnumConverter(container));
		
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("lib");
			
		binder.bind(comboBox, propertyId);
		
		return comboBox;
	}
	
	
	private static boolean isAllowed(Enum en, Enum[] enumsToExcludes)
	{
		if (enumsToExcludes==null)
		{
			return true;
		}
		for (int i = 0; i < enumsToExcludes.length; i++)
		{
			Enum enum1 = enumsToExcludes[i];
			if (enum1.equals(en))
			{
				return false;
			}
		}
		return true;
	}


	/**
	 * Permet de créer une combo box permettant de choisir parmi une liste de Enum
	 *  
	 * @param binder
	 * @param title
	 * @param enumeration donne à la fois la liste à afficher et la valeur par défaut 
	 * @param propertyId
	 * @return
	 */
	static public ComboBox createEnumSearcher(String title,Enum enumeration)
	{
		List<EnumBean> beans = new ArrayList<>();
		EnumSet enums = EnumSet.allOf(enumeration.getDeclaringClass());
		for (Object enum1 : enums)
		{
			beans.add(new EnumBean( (Enum) enum1));
		}
			
		
		BeanItemContainer<EnumBean> container = new BeanItemContainer(EnumBean.class, beans);
	
		
		
		
		ComboBox comboBox = new ComboBox(title,container);
		
		comboBox.setConverter(new ComboBoxEnumConverter(container));
		
		comboBox.setItemCaptionMode(ItemCaptionMode.PROPERTY);
		comboBox.setItemCaptionPropertyId("lib");
		
		comboBox.setConvertedValue(enumeration);
		
		
		return comboBox;
	}
	
}
