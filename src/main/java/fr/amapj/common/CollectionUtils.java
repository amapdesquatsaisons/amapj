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
 package fr.amapj.common;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;




public class CollectionUtils
{
	
	/**
	 * Découpe la liste listIn en sous listes contenant au maximum nbElem
	 * 
	 * @param listIn
	 * @param nbElem
	 * @return
	 */
	static public <T> List<List<T>> cutInSubList(List<T> listIn,int nbElem)
	{
		if (nbElem<=0)
		{
			throw new AmapjRuntimeException("Impossible de découper une liste avec nbElem="+nbElem);
		}
		
		List<List<T>> res = new ArrayList<>();
		
		List<T> tmp = new ArrayList<>();
		int size = listIn.size();
		for (int i = 0; i < size; i++)
		{
			if ( (i!=0) && ((i%nbElem)==0) )
			{
				res.add(tmp);
				tmp = new ArrayList<>();
			}
			tmp.add(listIn.get(i));
		}
		
		if (tmp.size()!=0)
		{
			res.add(tmp);
		}
		
		return res;
	}
	
	
	static public interface ToString<T>
	{
		public String toString(T t);
	}
	
	/**
	 * Convertit une liste d'objet en une String 
	 * Exemple : ls = [ "Bob" , "Marc" , "Paul" ]
	 * 
	 *  asString(ls,",") =>  "Bob,Marc,Paul"
	 * 
	 */
	public static <T> String asString(List<T> ls,String sep,ToString<T> f)
	{
		if (ls.size()==0)
		{
			return "";
		}
		
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ls.size()-1; i++)
		{
			T l = ls.get(i);
			if (l!=null)
			{
				str.append(f.toString(l));
			}
			else
			{
				str.append("null");
			}
			str.append(sep);
		}
		
		T l = ls.get(ls.size()-1);
		if (l!=null)
		{
			str.append(f.toString(l));
		}
		else
		{
			str.append("null");
		}
		
		return str.toString();
	}
	
	/**
	 * Convertit une liste d'objet en une String 
	 * Exemple : ls = [ "Bob" , "Marc" , "Paul" ]
	 * 
	 *  asString(ls,",") =>  "Bob,Marc,Paul"
	 * 
	 */
	public static <T> String asString(List<T> ls,String sep)
	{
		ToString<T> f = new CollectionUtils.ToString<T>() 
		{
			public String toString(T t)
			{
				return t.toString();
			}
		};
		return asString(ls, sep,f);
	}
	
	
	/**
	 * Convertit une liste d'objet en une String 
	 * Exemple : ls = [ "Bob" , "Marc" , "Paul" ]
	 * 
	 *  asString(ls) =>  "(Bob,Marc,Paul)"
	 * 
	 */
	public static <T> String asStdString(List<T> ls,ToString<T> f)
	{
		return "("+asString(ls, ",", f)+")";
	}
	
	
	
	/**
	 * Convertit une liste d'objet en une String , EN CONSERVANT LE SEPARATEUR FINAL
	 * POUR LE DERNIER ELEMENT
	 *  
	 * Exemple : ls = [ "Bob" , "Marc" , "Paul" ]
	 * 
	 *  asString(ls,",") =>  "Bob,Marc,Paul,"
	 *  
	 *  Si la liste est vide, retourne la chaine vide
	 * 
	 */
	public static <T> String asStringFinalSep(List<T> ls,String sep,ToString<T> f)
	{
		if (ls.size()==0)
		{
			return "";
		}
		
		StringBuffer str = new StringBuffer();
		for (int i = 0; i < ls.size(); i++)
		{
			T l = ls.get(i);
			if (l!=null)
			{
				str.append(f.toString(l));
			}
			else
			{
				str.append("null");
			}
			str.append(sep);
		}	
		return str.toString();
	}
	
	
	
	
	
}
