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
 package fr.amapj.service.services.mailer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;

import org.apache.poi.ss.usermodel.Workbook;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;

/**
 * Permet de stocker une piece jointe 
 * 
 *
 */
public class MailerAttachement
{

	private DataSource dataSource;
	
	private String name;
	
	
	public MailerAttachement()
	{
		
	}
	
	/**
	 * Permet de construire une pièce jointe à partir d'un fichier local
	 */
	public MailerAttachement(File file)
	{
		dataSource = new FileDataSource(file);
		name = file.getName();	
	}
	
	/**
	 * Permet de construire une pièce jointe à partir d'un fichier excel 
	 */	
	public MailerAttachement(AbstractExcelGenerator generator)
	{
		ExcelGeneratorService excelGeneratorService = new ExcelGeneratorService();

		Workbook workbook = excelGeneratorService.getFichierExcel(generator);
		FileInfoDTO fileInfo = excelGeneratorService.getFileInfo(generator);

		ByteArrayOutputStream imagebuffer = new ByteArrayOutputStream();

		try
		{
			workbook.write(imagebuffer);
		}
		catch (IOException e)
		{
			throw new RuntimeException("Erreur inattendue");
		}

		dataSource = new ByteArrayDataSource(imagebuffer.toByteArray(), "application/vnd.ms-excel");
		name = fileInfo.fileName;
	}
	

	
	
	
	
	
	public DataSource getDataSource()
	{
		return dataSource;
	}

	public void setDataSource(DataSource dataSource)
	{
		this.dataSource = dataSource;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}
	
	
	
	
	
	
	
}
