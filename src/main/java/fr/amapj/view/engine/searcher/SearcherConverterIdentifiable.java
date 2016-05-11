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
 package fr.amapj.view.engine.searcher;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;

import fr.amapj.model.engine.Identifiable;

public class SearcherConverterIdentifiable implements Converter
{

	private final BeanItemContainer<Identifiable> container;

	public SearcherConverterIdentifiable(BeanItemContainer<Identifiable> container)
	{
		this.container = container;
	}

	public Long convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			return ((Identifiable) value).getId();
		}
		return null;
	}

	public Identifiable convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Long l = (Long) value;
			List<Identifiable> ls = container.getItemIds();
			for (Identifiable identifiable : ls)
			{
				if (identifiable.getId().equals(l))
				{
					return identifiable;
				}
			}
		}
		return null;
	}

	public Class<Long> getModelType()
	{
		return Long.class;
	}

	public Class<Identifiable> getPresentationType()
	{
		return Identifiable.class;
	}

	

}
