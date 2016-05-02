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
 package fr.amapj.service.services.logview;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.amapj.common.AmapjRuntimeException;
import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.Call;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.NewTransaction;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.saas.LogAccess;
import fr.amapj.model.models.saas.TypLog;
import fr.amapj.service.services.appinstance.LogAccessDTO;
import fr.amapj.service.services.session.SessionManager;
import fr.amapj.view.engine.ui.AmapJLogManager;

/**
 * Permet d'afficher la liste des personnes connectées
 * 
 * 
 * 
 */
public class LogViewService
{
	private final static Logger logger = LogManager.getLogger();

	// PARTIE CONNEXION / DECONNEXION DES UTILISATEURS OU DES DEMONS

	/**
	 * Démarrage d'un acces pour un user ou un demon
	 * 
	 * @param nom
	 * @param prenom
	 * @param idUtilisateur
	 * @param ip
	 * @param browser
	 * @param dbName
	 * @return
	 */
	public LogAccessDTO saveAccess(final String nom, final String prenom, final Long idUtilisateur, final String ip, final String browser, final String dbName,final TypLog typLog,final boolean sudo)
	{
		return (LogAccessDTO) NewTransaction.writeInMaster(new Call()
		{
			@Override
			public Object executeInNewTransaction(EntityManager em)
			{
				// On crée l'object et on le rend persistant pour avoir son id
				LogAccess logAccess = new LogAccess();
				logAccess.setNom(nom);
				logAccess.setPrenom(prenom);
				logAccess.setIdUtilisateur(idUtilisateur);
				logAccess.setIp(ip);
				logAccess.setBrowser(browser);
				logAccess.setDbName(dbName);
				logAccess.setDateIn(new Date());
				logAccess.setTypLog(typLog);
				logAccess.setSudo((sudo==true) ? 1 : 0);
				em.persist(logAccess);

				// Gestion du logging ensuite
				String fileName = AmapJLogManager.createLogFileName(dbName,logAccess.getId(), logAccess.getDateIn(),typLog);
				logAccess.setLogFileName(fileName);
				String d = new SimpleDateFormat("dd/MM/yyyy").format(logAccess.getDateIn());
				
				if (typLog==TypLog.USER)
				{
					logger.info("Authentification réussie pour nom={} prenom={} id={} date={}", nom, prenom, idUtilisateur,d);
				}
				else
				{
					logger.info("Démarrage du démon nom={}  dbName={} date={}", nom, dbName,d);
				}

				//
				LogAccessDTO dto = createLogAccessDTO(logAccess);
				return dto;
			}
		});
	}

	/**
	 * 
	 * @param idLogAccess
	 */
	public void endAccess(final Long idLogAccess,final int nbError)
	{
		NewTransaction.writeInMaster(new Call()
		{
			@Override
			public Object executeInNewTransaction(EntityManager em)
			{
				LogAccess logAccess = em.find(LogAccess.class, idLogAccess);
				logAccess.setDateOut(new Date());
				int nbSec = (int) ((logAccess.getDateOut().getTime()-logAccess.getDateIn().getTime())/1000);
				logAccess.setActivityTime(nbSec);
				if (logAccess.getNbError()<nbError)
				{
					logAccess.setNbError(nbError);
				}

				return null;
			}
		});
	}

	private LogAccessDTO createLogAccessDTO(LogAccess logAccess)
	{
		LogAccessDTO dto = new LogAccessDTO();

		dto.browser = logAccess.getBrowser();
		dto.dateIn = logAccess.getDateIn();
		dto.dateOut = logAccess.getDateOut();
		dto.dbName = logAccess.getDbName();
		dto.id = logAccess.getId();
		dto.idUtilisateur = logAccess.getIdUtilisateur();
		dto.ip = logAccess.getIp();
		dto.logFileName = logAccess.getLogFileName();
		dto.nom = logAccess.getNom();
		dto.prenom = logAccess.getPrenom();
		dto.typLog = logAccess.getTypLog();
		dto.nbError = logAccess.getNbError();
		dto.sudo = (logAccess.getSudo()==0) ? "" : "SUDO";

		return dto;
	}
	
	// PARTIE VISUALISATION
	@DbRead
	public List<LogAccessDTO> getLogs()
	{
		List<LogAccessDTO> res = new ArrayList<LogAccessDTO>();
		
		EntityManager em = TransactionHelper.getEm();


		Query q = em.createQuery("select a from LogAccess a order by a.dateIn desc");

		List<LogAccess> ps = q.getResultList();
		for (LogAccess p : ps)
		{
			LogAccessDTO dto = createLogAccessDTO(p);
			if (dto.dateOut==null)
			{
				dto.nbError = SessionManager.getNbError(dto);
			}
			
			res.add(dto);
		}

		return res;
	}
	

	// STATISTIQUE SUR LES ACCES
	@DbRead
	public List<StatAccessDTO> getStats()
	{
		List<StatAccessDTO> res = new ArrayList<StatAccessDTO>();
		
		EntityManager em = TransactionHelper.getEm();

		//
		Query q = em.createNativeQuery("select to_char(l.dateIn,'YYYY-MM-DD') , count(l.id) , "
				+ "count(distinct(l.idUtilisateur,l.dbName)) , sum(l.activityTime) from LogAccess l "
				+ "where l.typLog = 'USER' "
				+ "group by to_char(l.dateIn,'YYYY-MM-DD') order by to_char(l.dateIn,'YYYY-MM-DD') desc");
		
		
		List<Object[]> ds = q.getResultList();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		
		for (Object[] s : ds)
		{
			
			try
			{
				StatAccessDTO dto = new StatAccessDTO();
				dto.date = df.parse((String) s[0]);
				dto.nbAcces = LongUtils.toInt(s[1]);
				dto.nbVisiteur = LongUtils.toInt(s[2]);
				dto.tempsTotal = LongUtils.toInt(s[3])/60;
				res.add(dto);
			} 
			catch (ParseException e)
			{
				throw new AmapjRuntimeException(e);
			}			
		}


		return res;
	}


}
