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
 package fr.amapj.service.services.appinstance;

import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.common.StringUtils;
import fr.amapj.model.engine.db.DbManager;
import fr.amapj.model.engine.dbms.DBMS;
import fr.amapj.model.engine.tools.SpecificDbImpl;
import fr.amapj.model.engine.tools.SpecificDbUtils;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DataBaseInfo;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbUtil;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.saas.AppInstance;
import fr.amapj.service.engine.appinitializer.AppInitializer;
import fr.amapj.service.services.appinstance.SqlRequestDTO.DataBaseResponseDTO;
import fr.amapj.service.services.appinstance.SqlRequestDTO.ResponseDTO;
import fr.amapj.service.services.mailer.MailerCounter;
import fr.amapj.service.services.mailer.MailerCounter.MailCount;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.service.services.suiviacces.ConnectedUserDTO;
import fr.amapj.service.services.utilisateur.UtilisateurDTO;
import fr.amapj.service.services.utilisateur.UtilisateurService;
import fr.amapj.view.engine.popup.formpopup.OnSaveException;
import fr.amapj.view.engine.ui.AppConfiguration;

/**
 * Permet la gestion des instances de l'application
 * 
 */
public class AppInstanceService
{
	
	private final static Logger logger = LogManager.getLogger();

	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES INSTANCES

	/**
	 * Permet de charger la liste de tous les instances
	 */
	@DbRead
	public List<AppInstanceDTO> getAllInstances(boolean withMaster)
	{
		EntityManager em = TransactionHelper.getEm();	
		
		List<AppInstanceDTO> res = new ArrayList<>();

		Query q = em.createQuery("select a from AppInstance a");

		List<AppInstance> ps = q.getResultList();
		List<ConnectedUserDTO> connected = SessionManager.getAllConnectedUser();
		for (AppInstance p : ps)
		{
			AppInstanceDTO dto = createAppInstanceDto(connected,p);
			res.add(dto);
		}
		
		// On ajoute ensuite la base master si besoin 
		if (withMaster)
		{
			AppInstanceDTO master = AppConfiguration.getConf().getMasterConf();
			addInfo(master,connected);
			res.add(master);
		}

		return res;

	}

	

	public AppInstanceDTO createAppInstanceDto(List<ConnectedUserDTO> connected, AppInstance a)
	{
		AppInstanceDTO dto = new AppInstanceDTO();
		
		dto.id = a.getId();
		dto.nomInstance = a.getNomInstance();
		dto.dateCreation = a.getDateCreation();
		dto.dbms = a.getDbms();
		dto.dbUserName = a.getDbUserName();
		dto.dbPassword = a.getDbPassword();		
		addInfo(dto, connected);
		return dto;
	}
	
	
	private void addInfo(AppInstanceDTO dto, List<ConnectedUserDTO> connected)
	{
		dto.state = getState(dto.getNomInstance());
		dto.nbUtilisateurs = getNbUtilisateurs(connected,dto.getNomInstance());
		dto.nbMails = MailerCounter.getNbMails(dto.getNomInstance());
		
	}


	private AppState getState(String nomInstance)
	{
		DataBaseInfo dataBaseInfo = DbUtil.findDataBaseFromName(nomInstance);
		if (dataBaseInfo == null)
		{
			return AppState.OFF;
		}
		return dataBaseInfo.getState();
	}
	
	private int getNbUtilisateurs(List<ConnectedUserDTO> connected, String nomInstance)
	{
		int res = 0;
		for (ConnectedUserDTO connectedUserDTO : connected)
		{
			if ((connectedUserDTO.isLogged==true) && (StringUtils.equals(connectedUserDTO.dbName,nomInstance)) )
			{
				res++;
			}
		}
		return res;
	}

	
	// CREATION D'UNE INSTANCE
	
	/**
	 * Permet de créer une instance 
	 * 
	 * Attention : ne pas mettre d'annotation dbWrite ou dbRead ici !!!
	 * 
	 * @param appInstanceDTO
	 * @throws OnSaveException
	 */
	public void create(final AppInstanceDTO appInstanceDTO) throws OnSaveException
	{
		// On vérifie que la base n'existe pas déjà
		if (DbUtil.findDataBaseFromName(appInstanceDTO.nomInstance)!=null)
		{
			throw new OnSaveException("La base existe déjà");
		}

		// On crée la base
		// Attention : ne pas créer de transaction ici, car on va ecrire dans la base de données fille
		AppInitializer.dbManager.createDataBase(appInstanceDTO);
		
		// On crée ensuite en base de données
		// On crée la transaction seulement ici dans le master
		NewTransaction.writeInMaster(new Call()
		{
			
			@Override
			public Object executeInNewTransaction(EntityManager em)
			{
				AppInstance a = new AppInstance();

				a.setNomInstance(appInstanceDTO.nomInstance);
				a.setDateCreation(new Date());
				a.setDbms(appInstanceDTO.dbms);
				a.setDbUserName(appInstanceDTO.dbUserName);
				a.setDbPassword(appInstanceDTO.dbPassword);
						
				em.persist(a);

				return null;
			}
		});
	}


	// PARTIE SUPPRESSION

	/**
	 * Permet de supprimer un instance Ceci est fait dans une transaction en ecriture
	 */
	@DbWrite
	public void delete(final Long id)
	{
		EntityManager em = TransactionHelper.getEm();

		AppInstance a = em.find(AppInstance.class, id);

		em.remove(a);
	}

	// PARTIE DEMARRAGE DE LA BASE

	public void setDbState(AppInstanceDTO dto)
	{
		AppState appState = dto.getState();
		DataBaseInfo dataBaseInfo = DbUtil.findDataBaseFromName(dto.nomInstance);

		// La base n'existe pas du tout
		if (dataBaseInfo == null)
		{
			throw new AmapjRuntimeException("Base non trouvée");
		}

		// Réalisation des opérations nécessaires
		switch (dataBaseInfo.getState())
		{
		case OFF:
			changeStateFromOff(dataBaseInfo,appState);
			break;

		case DATABASE_ONLY:
			changeStateFromDatabaseOnly(dataBaseInfo,appState);
			break;

		case ON:
			changeStateFromOn(dataBaseInfo,appState);
			break;

		default:
			throw new AmapjRuntimeException("Erreur de programmation");
		}
		
		// On mémorise son état correctement
		dataBaseInfo.setState(appState);
		
	}

	private void changeStateFromOn(DataBaseInfo dataBaseInfo, AppState appState)
	{
		switch (appState)
		{
		case ON:
		case DATABASE_ONLY:
			// Rien à faire
			break;

		case OFF:
			DBMS dbms = dataBaseInfo.getDbms();
			dbms.stopOneBase(dataBaseInfo.getDbName());
			break;
		
		default:
			throw new AmapjRuntimeException("Erreur de programmation");
		}

	}

	private void changeStateFromDatabaseOnly(DataBaseInfo dataBaseInfo, AppState appState)
	{
		switch (appState)
		{
		case ON:
		case DATABASE_ONLY:
			// Rien à faire
			break;

		case OFF:
			DBMS dbms = dataBaseInfo.getDbms();
			dbms.stopOneBase(dataBaseInfo.getDbName());
			break;
		
		default:
			throw new AmapjRuntimeException("Erreur de programmation");
		}

	}

	private void changeStateFromOff(DataBaseInfo dataBaseInfo, AppState appState)
	{
		switch (appState)
		{
		case ON:
		case DATABASE_ONLY:
			DBMS dbms = dataBaseInfo.getDbms();
			dbms.startOneBase(dataBaseInfo.getDbName());
			break;

		case OFF:
			// Rien à faire
			break;
		
		default:
			throw new AmapjRuntimeException("Erreur de programmation");
		}
		
	}	
	
	
	// PARTIE SUDO
	public List<SudoUtilisateurDTO> getSudoUtilisateurDto(AppInstanceDTO dto)
	{
		return (List<SudoUtilisateurDTO>) SpecificDbUtils.executeInSpecificDb(dto.nomInstance, new SpecificDbImpl()
		{
			@Override
			public Object perform()
			{
				return getSudoUtilisateurDto();
			}
		});
	}

	public List<SudoUtilisateurDTO> getSudoUtilisateurDto()
	{
		List<SudoUtilisateurDTO> res = new ArrayList<SudoUtilisateurDTO>();
		ParametresDTO param = new ParametresService().getParametres();
		
		List<UtilisateurDTO> utilisateurDTOs =  new UtilisateurService().getAllUtilisateurs();
		for (UtilisateurDTO utilisateur : utilisateurDTOs)
		{
			SudoUtilisateurDTO dto = new SudoUtilisateurDTO();
			dto.id = utilisateur.getId();
			dto.nom = utilisateur.getNom();
			dto.prenom = utilisateur.getPrenom();
			dto.roles = utilisateur.roles;
			dto.url = param.getUrl()+"?username="+utilisateur.getEmail();
			res.add(dto);
		}
		
		// TODO trier suivant la logique voulue
		
		return res;
	}

	public void executeSqlRequest(SqlRequestDTO selected,List<AppInstanceDTO>  appInstanceDTOs)
	{	
		// Chaque requete est executée dans une transaction indépendante
		// On s'arrête dès qu'une requete échoue 
		
		selected.success = false;
		
		for (AppInstanceDTO appInstanceDTO : appInstanceDTOs)
		{
			DataBaseResponseDTO dataBaseResponseDTO = new DataBaseResponseDTO();
			dataBaseResponseDTO.success = false;
			dataBaseResponseDTO.dbName = appInstanceDTO.nomInstance;
			selected.responses.add(dataBaseResponseDTO);
			
			int index = 1;
			for (String request : selected.verifiedRequests)
			{
				ResponseDTO res = executeOneSqlRequest(request,appInstanceDTO,index);
				dataBaseResponseDTO.responses.add(res);
				if (res.success==false)
				{
					return ;
				}
				index++;
			}
			dataBaseResponseDTO.success = true;
		}
		
		selected.success = true;
	}


	private ResponseDTO executeOneSqlRequest(String request, AppInstanceDTO dto,int index) 
	{
		ResponseDTO res = new ResponseDTO();
		res.index = index;
		res.sqlRequest = request;
		
		String msg = executeOneSqlRequestException(request, dto);
		
		if (msg==null)
		{	
			res.sqlResponse = "OK";
			res.success = true;
		}
		else
		{
			res.sqlResponse = "Erreur : "+msg;
			res.success = false;	
		}
		
		return res;
	}
	
	
	/**
	 * Permet d'excuter une requete SQL sur l'instance indiquée
	 * 
	 * Retourne null si tout est OK, sinon retourne le message de l'exception SQL
	 * 
	 * @param request
	 * @param dto
	 * @return
	 * @throws SQLException
	 */
	private String executeOneSqlRequestException(String request, AppInstanceDTO dto) 
	{
		DbManager dbManager = AppInitializer.dbManager;
		DBMS dbms = dbManager.getDbms(dto.getDbms());
		try
		{
			dbms.executeSqlCommand(request, dto);
			return null;
		} 
		catch (SQLException e)
		{
			return e.getMessage();
		}
	}

	/**
	 * Permet de sauvegarder toutes ces instances
	 * 
	 * @param appInstanceDTOs
	 * @return
	 */
	public List<String> saveInstance(List<AppInstanceDTO> appInstanceDTOs)
	{
		List<String> res = new ArrayList<String>();
		
		String backupDir = AppConfiguration.getConf().getBackupDirectory();
		if (backupDir==null)
		{
			throw new RuntimeException("Le répertoire de stockage des sauvegardes n'est pas défini");
		}
		
		for (AppInstanceDTO appInstanceDTO : appInstanceDTOs)
		{
			String msg = saveInstance(appInstanceDTO,backupDir);
			res.add(msg);
		}
		return res;
	}

	private String saveInstance(AppInstanceDTO appInstanceDTO, String backupDir)
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String fileName = backupDir+"/"+appInstanceDTO.nomInstance+"_"+df.format(new Date())+".tar.gz";
		
		String request = "BACKUP DATABASE TO '"+fileName+"' BLOCKING";
		
		String msg = executeOneSqlRequestException(request, appInstanceDTO);
		if (msg!=null)
		{
			return "Erreur pour "+appInstanceDTO.nomInstance+": "+msg;
		}
		
		
		File file= new File(fileName);
		if (file.canRead()==false)
		{
			return "Erreur pour "+appInstanceDTO.nomInstance+": le fichier n'est pas trouvé";
		}
		
		return "Succès pour "+appInstanceDTO.nomInstance;
	}

	


}
