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
 package fr.amapj.view.engine.excelgenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Workbook;

import com.vaadin.server.StreamResource;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;


public class ExcelResource implements StreamResource.StreamSource
{

	
	AbstractExcelGenerator generator;

	public ExcelResource(AbstractExcelGenerator generator)
	{
		this.generator = generator;
	}

	@Override
	public InputStream getStream()
	{
		
		Workbook workbook = new ExcelGeneratorService().getFichierExcel(generator);
		
		ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();
	
		try
		{
			workbook.write(imagebuffer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Erreur inattendue");
		}
		return new ByteArrayInputStream(imagebuffer.toByteArray());
	}

}
