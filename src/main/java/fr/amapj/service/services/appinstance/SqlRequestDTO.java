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
 package fr.amapj.service.services.appinstance;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
public class SqlRequestDTO 
{
	public String requests;
	
	public List<String> verifiedRequests = new ArrayList<String>();
	
	public List<DataBaseResponseDTO> responses = new ArrayList<SqlRequestDTO.DataBaseResponseDTO>();
	
	public boolean success;

	public String getRequests()
	{
		return requests;
	}

	public void setRequests(String requests)
	{
		this.requests = requests;
	}

	
	static public class DataBaseResponseDTO
	{
		public String dbName;
		
		public boolean success;
		
		public List<ResponseDTO> responses= new ArrayList<SqlRequestDTO.ResponseDTO>();
	}
	
	static public class ResponseDTO
	{
		public int index;
		
		public String sqlRequest;
		
		public boolean success;
		
		public String sqlResponse;
	}
	
}
