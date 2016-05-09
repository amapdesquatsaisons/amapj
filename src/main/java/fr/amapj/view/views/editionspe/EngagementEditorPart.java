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
 package fr.amapj.view.views.editionspe;

import com.vaadin.data.util.BeanItem;
import com.vaadin.ui.TextArea;

import fr.amapj.model.models.editionspe.AbstractEditionSpeJson;
import fr.amapj.model.models.editionspe.TypEditionSpecifique;
import fr.amapj.model.models.editionspe.engagement.EngagementJson;
import fr.amapj.service.services.editionspe.EditionSpeDTO;
import fr.amapj.service.services.editionspe.EditionSpeService;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;

/**
 * Permet le paramétrage des engagements
 * 
 *
 */
@SuppressWarnings("serial")
public class EngagementEditorPart extends WizardFormPopup
{

	private EngagementJson etiquetteDTO;

	private boolean create;

	public enum Step
	{
		GENERAL, TEXT ;
	}

	/**
	 * 
	 */
	public EngagementEditorPart(boolean create,EditionSpeDTO p)
	{
		this.create = create;
		
		popupWidth = "80%";
		popupHeight = "60%";
		
		if (create)
		{
			popupTitle = "Création d'un modèle de contrat d'engagement";
			this.etiquetteDTO = new EngagementJson();
			this.etiquetteDTO.setTypEditionSpecifique(TypEditionSpecifique.ENGAGEMENT);
		}
		else
		{
			popupTitle = "Modification d'un modèle de contrat d'engagement";
			
			
			this.etiquetteDTO = (EngagementJson) AbstractEditionSpeJson.load(p);
			
		}	
		
	
		
		item = new BeanItem<EngagementJson>(this.etiquetteDTO);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case GENERAL:
			addFieldGeneral();
			break;

		case TEXT:
			addFieldText();
			break;
			
		
		default:
			break;
		}
	}

	private void addFieldGeneral()
	{
		// Titre
		setStepTitle("les informations générales");
		
		// Champ 1
		addTextField("Nom", "nom");
		
	}

	private void addFieldText()
	{
		// Titre
		setStepTitle("le contenu");
		
		
		/* TODO 
		CKEditorConfig config = new CKEditorConfig();
        config.useCompactTags();
        config.disableElementsPath();
        config.setResizeDir(CKEditorConfig.RESIZE_DIR.HORIZONTAL);
        config.disableSpellChecker();
        config.setToolbarCanCollapse(false);
        //config.addOpenESignFormsCustomToolbar();
        config.setWidth("100%");
        
        final CKEditorTextField ckEditorTextField = new CKEditorTextField(config);
        form.addComponent(ckEditorTextField);
        
        final String editorInitialValue = 
                "<p>Thanks TinyMCEEditor for getting us started on the CKEditor integration.</p><h1>Like TinyMCEEditor said, &quot;Vaadin rocks!&quot;</h1><h1>And CKEditor is no slouch either.</h1>";

        ckEditorTextField.setValue(editorInitialValue);
        //ckEditorTextField.setReadOnly(true);
        /*ckEditorTextField.addListener(new Property.ValueChangeListener() {

                public void valueChange(ValueChangeEvent event) {
                        getMainWindow().showNotification("CKEditor contents: " + event.getProperty().toString().replaceAll("<", "&lt;"));
                }
        });*/
		
		TextArea f =  addTextAeraField("Contenu ", "text");
		f.setHeight(8, Unit.CM);
		
	
	}
	

	@Override
	protected void performSauvegarder()
	{
		EditionSpeDTO editionSpeDTO = etiquetteDTO.save();
		new EditionSpeService().update(editionSpeDTO, create);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
}
