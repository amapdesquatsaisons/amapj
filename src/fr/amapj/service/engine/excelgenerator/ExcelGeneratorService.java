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
 package fr.amapj.service.engine.excelgenerator;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.apache.poi.ss.usermodel.Workbook;

import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.TransactionHelper;


/**
 * Permet la gestion des extractions excels
 * 
 *  
 *
 */
public class ExcelGeneratorService
{
	/**
	 * Permet de générer le fichier Excel 
	 */
	@DbRead
	public Workbook  getFichierExcel(AbstractExcelGenerator generator)
	{
		EntityManager em = TransactionHelper.getEm();
	
		ExcelFormat format = generator.getFormat();
		ExcelGeneratorTool et = new ExcelGeneratorTool(format);
		generator.fillExcelFile(em, et);
		return et.getWb();
	}
	
	
	
	

	/**
	 * Permet de générer les informations sur le fichier 
	 */
	@DbRead
	public List<FileInfoDTO>  getFileInfo(List<AbstractExcelGenerator> generators)
	{
		EntityManager em = TransactionHelper.getEm();
		
		List<FileInfoDTO> res = new ArrayList<>();
		
		for (AbstractExcelGenerator generator : generators)
		{
			FileInfoDTO dto = new FileInfoDTO();
			dto.fileName = generator.getFileName(em);
			dto.nameToDisplay = generator.getNameToDisplay(em);
			dto.generator = generator;
			res.add(dto);
		}
		
		return res;
	}	
	
	/**
	 * Idem à la fonction précédente mais sur un seul fichier 
	 * 
	 * @param generator
	 * @return
	 */
	public FileInfoDTO  getFileInfo(AbstractExcelGenerator generator)
	{
		List<AbstractExcelGenerator> generators = new ArrayList<>();
		generators.add(generator);
		List<FileInfoDTO> res = getFileInfo(generators);
		return res.get(0);
	}
	

}
