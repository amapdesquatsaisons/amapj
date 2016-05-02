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



import java.util.ArrayList;
import java.util.List;

import com.vaadin.server.StreamResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

import fr.amapj.service.engine.excelgenerator.AbstractExcelGenerator;
import fr.amapj.service.engine.excelgenerator.ExcelGeneratorService;
import fr.amapj.service.engine.excelgenerator.FileInfoDTO;
import fr.amapj.view.engine.popup.corepopup.CorePopup;

/**
 * Popup de telechargement
 *  
 */
@SuppressWarnings("serial")
public class TelechargerPopup extends CorePopup
{
	
	private List<AbstractExcelGenerator> generators = new ArrayList<>();
	
	// Mémorise l'index des generators qui sont suivis par un espace
	private List<Integer> separators = new ArrayList<>();
	
	public TelechargerPopup()
	{	
	}
	
	public void addGenerator(AbstractExcelGenerator generator)
	{
		generators.add(generator);
	}
	
	public void addSeparator()
	{
		separators.add(generators.size()-1);
	}
	
	

	protected void createContent(VerticalLayout contentLayout)
	{
		contentLayout.addComponent(new Label("Veuillez cliquer sur le lien du fichier que vous souhaitez télécharger<br/>",ContentMode.HTML));
		
		
		List<FileInfoDTO> fileInfoDTOs = new ExcelGeneratorService().getFileInfo(generators);
		int i=0;
		for (FileInfoDTO fileInfoDTO : fileInfoDTOs)
		{
			addLink(contentLayout,fileInfoDTO);
			
			if (separators.contains(i))
			{
				contentLayout.addComponent(new Label("----"));
			}
			i++;
		}	
	}
	
	private void addLink(VerticalLayout contentLayout, FileInfoDTO fileInfoDTO)
	{
		String titre = fileInfoDTO.nameToDisplay;
		String fileName = fileInfoDTO.fileName;
		String extension = fileInfoDTO.generator.getFormat().name().toLowerCase();
		Link extractFile = new Link(titre,new StreamResource(new ExcelResource(fileInfoDTO.generator), fileName+"."+extension));
		contentLayout.addComponent(extractFile);
	}

	protected void createButtonBar()
	{		
		addButton("Quitter", new Button.ClickListener()
		{
			@Override
			public void buttonClick(ClickEvent event)
			{
				handleAnnuler();
			}
		});
	}
	

	protected void handleAnnuler()
	{
		close();
	}
	
}
