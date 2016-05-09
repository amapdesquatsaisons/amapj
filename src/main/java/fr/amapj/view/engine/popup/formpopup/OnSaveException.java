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
 package fr.amapj.view.engine.popup.formpopup;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class OnSaveException extends Exception
{
	
	private List<String> errorMessage = new ArrayList<>();

	public OnSaveException()
	{
	}

	public OnSaveException(String message)
	{
		super(message);
		errorMessage.add(message);
	}
	
	public OnSaveException(List<String> messages)
	{
		super();
		errorMessage.addAll(messages);
	}
	

		
	public List<String> getAllMessages()
	{
		return errorMessage;
	}

}