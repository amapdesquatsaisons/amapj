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

import fr.amapj.model.engine.Identifiable;
import fr.amapj.model.engine.Mdm;
import fr.amapj.service.services.searcher.SearcherService;


/**
 * Implementation basique du searcher 
 */
public class BasicSearcher implements SearcherDefinition 
{
	private String title;
	private Class<? extends Identifiable> clazz;
	private String propertyIdToDisplay;
	
	
	public BasicSearcher(String title, Class<? extends Identifiable> clazz, String propertyIdToDisplay)
	{
		this.title = title;
		this.clazz = clazz;
		this.propertyIdToDisplay = propertyIdToDisplay;
	}
	
	public BasicSearcher(String title, Class<? extends Identifiable> clazz, Mdm propertyIdToDisplay)
	{
		this(title,clazz,propertyIdToDisplay.prop());
	}
	
	
	public List<Identifiable> getAllElements(Object params)
	{
		return new SearcherService().getAllElements(clazz);
	}
	
	public String getPropertyId()
	{
		return propertyIdToDisplay;
	}
		
	public String toString(Identifiable identifiable)
	{
		return null;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Class getClazz()
	{
		return clazz;
	}

	@Override
	public boolean needParams()
	{
		return false;
	}

}
