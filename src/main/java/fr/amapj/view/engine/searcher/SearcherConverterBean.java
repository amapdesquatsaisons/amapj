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
 package fr.amapj.view.engine.searcher;

import java.util.List;
import java.util.Locale;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter;

import fr.amapj.service.services.searcher.SearcherBean;

public class SearcherConverterBean implements Converter
{

	private final BeanItemContainer<SearcherBean> container;

	public SearcherConverterBean(BeanItemContainer<SearcherBean> container)
	{
		this.container = container;
	}

	public Long convertToModel(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value!=null)
		{
			return ((SearcherBean) value).getId();
		}
		return null;
	}

	public SearcherBean convertToPresentation(Object value, Class targetType, Locale locale) throws com.vaadin.data.util.converter.Converter.ConversionException
	{
		if (value != null)
		{
			Long l = (Long) value;
			List<SearcherBean> ls = container.getItemIds();
			for (SearcherBean identifiable : ls)
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

	public Class<SearcherBean> getPresentationType()
	{
		return SearcherBean.class;
	}

	

}
