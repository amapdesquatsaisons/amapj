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
 package fr.amapj.view.views.gestioncontrat.editorpart;

import java.util.Date;
import java.util.List;

import com.vaadin.data.util.BeanItem;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.TextField;

import fr.amapj.common.DateUtils;
import fr.amapj.common.DebugUtil;
import fr.amapj.model.models.contrat.modele.GestionPaiement;
import fr.amapj.model.models.contrat.modele.ModeleContrat;
import fr.amapj.model.models.fichierbase.Producteur;
import fr.amapj.service.services.gestioncontrat.DateModeleContratDTO;
import fr.amapj.service.services.gestioncontrat.GestionContratService;
import fr.amapj.service.services.gestioncontrat.LigneContratDTO;
import fr.amapj.service.services.gestioncontrat.ModeleContratDTO;
import fr.amapj.service.services.saisiepermanence.PermanenceUtilisateurDTO;
import fr.amapj.view.engine.collectioneditor.CollectionEditor;
import fr.amapj.view.engine.collectioneditor.FieldType;
import fr.amapj.view.engine.popup.formpopup.WizardFormPopup;
import fr.amapj.view.engine.popup.formpopup.validator.ValidatorHolder;
import fr.amapj.view.engine.popup.formpopup.validator.IValidator;
import fr.amapj.view.engine.popup.formpopup.validator.NotNullValidator;
import fr.amapj.view.engine.popup.formpopup.validator.StringLengthValidator;
import fr.amapj.view.engine.popup.formpopup.validator.UniqueInDatabaseValidator;
import fr.amapj.view.engine.searcher.Searcher;
import fr.amapj.view.views.searcher.SearcherList;

/**
 * Permet uniquement de creer des contrats
 * 
 *
 */
@SuppressWarnings("serial")
public class GestionContratEditorPart extends WizardFormPopup
{

	private ModeleContratDTO modeleContrat;

	private boolean creerAPartirDeMode;
	
	private Searcher prod;
	
	private List<Producteur> allowedProducteurs;

	public enum Step
	{
		INFO_GENERALES, DATE_LIVRAISON, CHOIX_PRODUITS, PAIEMENT;
	}

	/**
	 * 
	 */
	public GestionContratEditorPart(Long id,List<Producteur> allowedProducteurs)
	{
		this.allowedProducteurs = allowedProducteurs;
		
		setWidth(80);
		popupTitle = "Création d'un contrat";

		// Chargement de l'objet à créer
		// Si id est non null, alors on se sert de ce contenu pour précharger
		// les champs
		if (id == null)
		{
			modeleContrat = new ModeleContratDTO();
			modeleContrat.frequence = FrequenceLivraison.UNE_FOIS_PAR_SEMAINE;
			modeleContrat.gestionPaiement = GestionPaiement.NON_GERE;
			creerAPartirDeMode = false;
		}
		else
		{
			modeleContrat = new GestionContratService().loadModeleContrat(id);
			modeleContrat.nom = modeleContrat.nom + "(Copie)";
			modeleContrat.id = null;
			modeleContrat.dateLivs.clear();
			creerAPartirDeMode = true;
		}
		item = new BeanItem<ModeleContratDTO>(modeleContrat);

	}
	
	@Override
	protected void addFields(Enum step)
	{
		switch ( (Step) step)
		{
		case INFO_GENERALES:
			addFieldInfoGenerales();
			break;

		case DATE_LIVRAISON:
			addFieldDateLivraison();
			break;

		case CHOIX_PRODUITS:
			addFieldChoixProduits();
			break;

		case PAIEMENT:
			addFieldPaiement();
			break;

		default:
			break;
		}
	}

	private void addFieldInfoGenerales()
	{
		// Titre
		setStepTitle("les informations générales");
		
		// Liste des validators
		IValidator len_1_100 = new StringLengthValidator(1, 100);
		IValidator len_1_255 = new StringLengthValidator(1, 255);
		IValidator uniq = new UniqueInDatabaseValidator(ModeleContrat.class,"nom",null);
		IValidator notNull = new NotNullValidator();
		IValidator prodValidator = new ProducteurAvecProduitValidator();
		
		
		
		// Champ 1
		addTextField("Nom du contrat", "nom",len_1_100,uniq);


		// Champ 2
		addTextField("Description du contrat", "description",len_1_255);

		// Champ 3
		
		prod = addSearcher("Producteur", "producteur", SearcherList.PRODUCTEUR, allowedProducteurs,notNull,prodValidator);
		// On ne peut pas changer le producteur quand on crée à partir d'un
		// autre contrat
		if (creerAPartirDeMode == true)
		{
			prod.setEnabled(false);
		}

		// Champ 4
		addDateField("Date de fin des inscriptions", "dateFinInscription",notNull);

		//
		addComboEnumField("Fréquence des livraisons", "frequence",notNull);
		
		//
		addComboEnumField("Gestion des paiements", "gestionPaiement",notNull);

	}

	private void addFieldDateLivraison()
	{
		// Titre
		setStepTitle("les dates de livraison");
		
		// Liste des validators
		IValidator notNull = new NotNullValidator();

		
		
		if (modeleContrat.frequence==FrequenceLivraison.UNE_SEULE_LIVRAISON)
		{
			addDateField("Date de la livraison", "dateDebut",notNull);
		}
		else if (modeleContrat.frequence==FrequenceLivraison.AUTRE)
		{
			//
			CollectionEditor<DateModeleContratDTO> f1 = new CollectionEditor<DateModeleContratDTO>("Liste des dates", (BeanItem) item, "dateLivs", DateModeleContratDTO.class);
			f1.addColumn("dateLiv", "Date",FieldType.DATE, null);
			binder.bind(f1, "dateLivs");
			form.addComponent(f1);
		}
		else
		{
			addDateField("Date de la première livraison", "dateDebut",notNull);
			addDateField("Date de la dernière livraison", "dateFin",notNull);
		}
		

	}

	private void addFieldChoixProduits()
	{
		// Si liste vide
		Long idProducteur = (Long) prod.getConvertedValue();
		if (modeleContrat.produits.size()==0 && idProducteur!=null)
		{
			modeleContrat.produits.addAll(new GestionContratService().getInfoProduitModeleContrat(idProducteur));
		}
		
		
		// Titre
		setStepTitle("la liste des produits et des prix");
				
		// Champ 7
		CollectionEditor<LigneContratDTO> f1 = new CollectionEditor<LigneContratDTO>("Produits", (BeanItem) item, "produits", LigneContratDTO.class);
		f1.addSearcherColumn("produitId", "Nom du produit",FieldType.SEARCHER, null,SearcherList.PRODUIT,prod);
		f1.addColumn("prix", "Prix du produit", FieldType.CURRENCY, null);
		binder.bind(f1, "produits");
		form.addComponent(f1);

	}

	private void addFieldPaiement()
	{
		if (checkProduits()==false)
		{
			setBackOnlyMode();
			
			// Titre
			setErrorTitle("Il y a des erreurs dans la saisie des produits");
			
			addLabel("Il y a des produits non renseignés ou des prix non renseignés",ContentMode.TEXT);
			
			addLabel("Vous ne devez pas avoir de lignes vides non plus",ContentMode.TEXT);
			
			return ;
		}
		
		
		
		setStepTitle("les informations sur le paiement");
		
		// Liste des validators
		IValidator notNull = new NotNullValidator();
		IValidator len_0_2048 = new StringLengthValidator(0, 2048);
		IValidator len_0_255 = new StringLengthValidator(0, 255);

		
		if (modeleContrat.gestionPaiement==GestionPaiement.GESTION_STANDARD)
		{	
			addTextField("Ordre du chèque", "libCheque",len_0_255);
			
			if (modeleContrat.frequence==FrequenceLivraison.UNE_SEULE_LIVRAISON)
			{
				PopupDateField p = addDateField("Date de remise du chèque", "dateRemiseCheque",notNull);
				p.setValue(modeleContrat.dateDebut);
			}
			else
			{
				PopupDateField p = addDateField("Date de remise des chèques", "dateRemiseCheque",notNull);
				p.setValue(modeleContrat.dateFinInscription);
				
				p = addDateField("Date du premier paiement", "premierCheque",notNull);
				p.setValue(proposeDatePremierPaiement());
				
				p = addDateField("Date du dernier paiement", "dernierCheque",notNull);
				p.setValue(proposeDateDernierPaiement()); 
			}
		}
		else
		{
			TextField f = (TextField) addTextField("Texte affiché dans la fenêtre paiement", "textPaiement",len_0_2048);
			f.setMaxLength(2048);
			f.setHeight(5, Unit.CM);
		}
	}

	private boolean checkProduits()
	{
		List<LigneContratDTO> produits = modeleContrat.produits;
		for (LigneContratDTO lig : produits)
		{
			if (lig.prix==null)
			{
				return false;
			}
			if (lig.produitId==null)
			{
				return false;
			}
		}
		
		
		return true;
	}

	private Date proposeDatePremierPaiement()
	{
		if (modeleContrat.dateDebut!=null)
		{
			return DateUtils.firstDayInMonth(modeleContrat.dateDebut); 
		}
		
		if (modeleContrat.dateLivs.size()>0)
		{
			return DateUtils.firstDayInMonth(modeleContrat.dateLivs.get(0).dateLiv);
		}
		
		return null;
	}
	
	
	private Date proposeDateDernierPaiement()
	{
		if (modeleContrat.dateFin!=null)
		{
			return DateUtils.firstDayInMonth(modeleContrat.dateFin); 
		}
		
		if (modeleContrat.dateLivs.size()>0)
		{
			return DateUtils.firstDayInMonth(modeleContrat.dateLivs.get(modeleContrat.dateLivs.size()-1).dateLiv);
		}
		
		return null;
	}
	
	

	@Override
	protected void performSauvegarder()
	{
		new GestionContratService().saveNewModeleContrat(modeleContrat);
	}

	@Override
	protected Class getEnumClass()
	{
		return Step.class;
	}
	
	/**
	 * Validateur qui vérifie que le producteur posséde au moins un produit
	 * @author Nadege
	 *
	 */
	public class ProducteurAvecProduitValidator implements IValidator
	{

		@Override
		public void performValidate(Object value, ValidatorHolder a)
		{
			Producteur p = (Producteur) value;
			if (p!=null)
			{
				List<LigneContratDTO> ligs = new GestionContratService().getInfoProduitModeleContrat(p.getId());
				if (ligs.size()==0)
				{
					a.addMessage("Le producteur \""+p.getNom()+"\" ne posséde pas de produits.");
					a.addMessage("Pour pouvoir créer un contrat pour ce producteur");
					a.addMessage("Vous devez d'abord aller dans le menu \"Gestion des produits\",");
					a.addMessage("et indiquer la liste des produits faits par ce producteur.");
				}
			}
		}

		@Override
		public boolean canCheckOnFly()
		{
			return true;
		}
		
	}
	
	
}
