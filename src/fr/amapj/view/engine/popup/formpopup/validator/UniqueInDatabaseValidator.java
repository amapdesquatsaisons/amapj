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
 package fr.amapj.view.engine.popup.formpopup.validator;

import fr.amapj.service.services.dbservice.DbService;


/**
 * Ne fonctionne que pour les zones de textes 
 *
 */
public class UniqueInDatabaseValidator implements IValidator
{
	
	private Class clazz;

    private String property;

 
	public UniqueInDatabaseValidator(Class clazz,String property)
	{
		super();
		this.clazz = clazz;
		this.property = property;
	}




	@Override
	public void performValidate(Object value,ValidatorHolder 	a)
	{
		String val = (String) value;
		if (val==null)
		{
			val="";
		}
		
		int nb = new DbService().count(clazz, property, val);
		if (nb>0)
		{
			a.addMessage("Le champ \""+a.title+"\" contient une valeur déjà utilisée. Merci de choisir une autre valeur.");
		}
	}




	@Override
	public boolean canCheckOnFly()
	{
		return true;
	}
	

}
