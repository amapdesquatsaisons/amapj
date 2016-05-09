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
 package fr.amapj.service.services.excelgenerator;

import java.io.IOException;

import javax.persistence.EntityManager;

import fr.amapj.service.engine.generator.excel.AbstractExcelGenerator;
import fr.amapj.service.engine.generator.excel.ExcelFormat;
import fr.amapj.service.engine.generator.excel.ExcelGeneratorTool;


/**
 * Test pour le calcul des hauteurs de ligne
 * 
 *  
 *
 */
public class EGTest extends AbstractExcelGenerator
{
	
	
	public EGTest()
	{
	}
	
	@Override
	public void fillExcelFile(EntityManager em,ExcelGeneratorTool et)
	{
		
		// Calcul du nombre de colonnes :  Nom + prénom + 1 montant de l'avoir
		et.addSheet("Avoirs", 3, 20);
				

		// Création de la ligne titre des colonnes
		et.addRow();
		et.setCell(0,"Nom",et.grasCentreBordure);
		et.setCell(1,"Prénom",et.grasCentreBordure);
		et.setCell(2,"Montant avoir",et.grasCentreBordure);
		

		addRow(et);

		
		

		

	}

	private void addRow( ExcelGeneratorTool et)
	{
		et.addRow();
		
		String cellValue = "court"; 
		cellValue = "un tres long message qui a besoin de plusieurs lignes pour s'afficher";
		
		et.setCell(0,cellValue,et.grasCentreBordure);
		et.setCell(1,"yyy",et.grasCentreBordure);
		et.setCellPrix(2,35,et.prixCentreBordure);
		
		/*int width = et.currentRow.getSheet().getColumnWidth(0)/64;
		
		CalculateSize cal = new CalculateSize();
		String fontName = "Arial";
		int fontSize = 10;
		
		System.out.println("width = "+width);
		System.out.println("height = "+cal.getHeight(cellValue, width, fontName, fontSize));
		*/
	}


	
	
	
	

	@Override
	public String getFileName(EntityManager em)
	{
		return "essai";
	}

	@Override
	public String getNameToDisplay(EntityManager em)
	{
		return "essai";
	}
	
	@Override
	public ExcelFormat getFormat()
	{
		return ExcelFormat.XLS;
	}

	public static void main(String[] args) throws IOException
	{
		new EGTest().test();
	}

}
