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

import com.vaadin.server.StreamResource;
import com.vaadin.ui.Link;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;

/**
 * Outil pour créer facilement des liens 
 *  
 */
@SuppressWarnings("serial")
public class LinkCreator 
{
	
	static public Link createLink(AbstractExcelGenerator generator)
	{
		FileInfoDTO fileInfoDTO = new ExcelGeneratorService().getFileInfo(generator);
		
		String titre = fileInfoDTO.nameToDisplay;
		String fileName = fileInfoDTO.fileName;
		String extension = fileInfoDTO.generator.getFormat().name().toLowerCase();
		Link extractFile = new Link("Télécharger "+titre,new StreamResource(new ExcelResource(fileInfoDTO.generator), fileName+"."+extension));
		return extractFile;
	}
	
}