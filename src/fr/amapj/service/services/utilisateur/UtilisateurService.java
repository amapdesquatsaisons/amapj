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
 package fr.amapj.service.services.utilisateur;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import fr.amapj.common.LongUtils;
import fr.amapj.model.engine.transaction.DbRead;
import fr.amapj.model.engine.transaction.DbWrite;
import fr.amapj.model.engine.transaction.TransactionHelper;
import fr.amapj.model.models.acces.RoleList;
import fr.amapj.model.models.fichierbase.EtatUtilisateur;
import fr.amapj.model.models.fichierbase.Utilisateur;
import fr.amapj.service.services.access.AccessManagementService;
import fr.amapj.service.services.authentification.PasswordManager;
import fr.amapj.service.services.mailer.MailerMessage;
import fr.amapj.service.services.mailer.MailerService;
import fr.amapj.service.services.parametres.ParametresDTO;
import fr.amapj.service.services.parametres.ParametresService;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailDTO;
import fr.amapj.service.services.utilisateur.envoimail.EnvoiMailUtilisateurDTO;
import fr.amapj.view.engine.popup.suppressionpopup.UnableToSuppressException;

/**
 * Permet la gestion des utilisateurs en masse
 * ou du changement de son état
 * 
 */
public class UtilisateurService
{
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES UTILISATEURS
	
	/**
	 * Permet de charger la liste de tous les utilisateurs
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<UtilisateurDTO> getAllUtilisateurs()
	{
		EntityManager em = TransactionHelper.getEm();
		
		List<UtilisateurDTO> res = new ArrayList<>();
		
		Query q = em.createQuery("select u from Utilisateur u order by u.nom,u.prenom");
			
		List<Utilisateur> us = q.getResultList();
		for (Utilisateur u : us)
		{
			UtilisateurDTO dto = createUtilisateurDto(em,u);
			res.add(dto);
		}
		
		return res;
		
	}

	
	public UtilisateurDTO createUtilisateurDto(EntityManager em, Utilisateur u)
	{
		UtilisateurDTO dto = new UtilisateurDTO();
		
		dto.id = u.getId();
		dto.nom = u.getNom();
		dto.prenom = u.getPrenom();
		dto.roles = new AccessManagementService().getRoleAsString(em,u);
		dto.email = u.getEmail();
		dto.etatUtilisateur = u.getEtatUtilisateur();
		
		dto.numTel1 = u.getNumTel1();
		dto.numTel2 = u.getNumTel2();
		dto.libAdr1 = u.getLibAdr1();
		dto.codePostal = u.getCodePostal();
		dto.ville = u.getVille();
		
		return dto;
	}


	// PARTIE MISE A JOUR DES UTILISATEURS

	
	@DbWrite
	public void updateUtilisateur(UtilisateurDTO dto)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur u = em.find(Utilisateur.class, dto.id);
		u.setNom(dto.nom);
		u.setPrenom(dto.prenom);
		u.setEmail(dto.email);
		
		u.setNumTel1(dto.numTel1);
		u.setNumTel2(dto.numTel2);
		u.setLibAdr1(dto.libAdr1);
		u.setCodePostal(dto.codePostal);
		u.setVille(dto.ville);
		
	}

	
	// PARTIE MISE A JOUR DE L ETAT D'UN UTILISATEUR

	
	@DbWrite
	public void updateEtat(EtatUtilisateur newValue, Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur u = em.find(Utilisateur.class, id);
		u.setEtatUtilisateur(newValue);
	}
	
	
	
	
	// PARTIE CREATION D UN UTILISATEUR

	/**
	 * Permet la création d'un utilisateur dans une transaction en ecriture
	 * 
	 * Retourne le mot de passe si celui ci doit être affiché à l'écran 
	 * 
	 */
	@DbWrite
	public String createNewUser(UtilisateurDTO utilisateurDTO,boolean generatePassword)
	{
		EntityManager em = TransactionHelper.getEm();
		
		// TODO faire les vérifications sur nom, prenom, email 
		
		String nom = utilisateurDTO.nom.trim();
		String prenom = utilisateurDTO.prenom.trim();
		String email = utilisateurDTO.email.trim().toLowerCase();
		
		Utilisateur u = new Utilisateur();
		u.setNom(nom);
		u.setPrenom(prenom);
		u.setEmail(email);
		u.setNumTel1(utilisateurDTO.numTel1);
		u.setNumTel2(utilisateurDTO.numTel2);
		u.setLibAdr1(utilisateurDTO.libAdr1);
		u.setCodePostal(utilisateurDTO.codePostal);
		u.setVille(utilisateurDTO.ville);

		em.persist(u);
		
		if (generatePassword==false)
		{
			return null;
		}

		String clearPassword = generatePassword();

		new PasswordManager().setUserPassword(u.getId(), clearPassword);
		
		
		ParametresDTO param = new ParametresService().getParametres();
		if (param.serviceMailActif==true)
		{
			String link = param.getUrl()+"?username="+u.getEmail();
		
			StringBuffer buf = new StringBuffer();
			buf.append("<h2>"+param.nomAmap+"</h2>");
			buf.append("<br/>");
			buf.append("Bonjour , voici vos identifiants pour vous connecter à l'application WEB de :"+param.nomAmap);
			buf.append("<br/>");
			buf.append("<br/>");
			buf.append("Adresse e mail : "+email);
			buf.append("<br/>");
			buf.append("<br/>");
			buf.append("Mot de passe : "+clearPassword);
			buf.append("<br/>");
			buf.append("<br/>");
			buf.append("<a href=\""+link+"\">Cliquez ici pour accéder à l'application</a>");
			buf.append("<br/>");
			buf.append("<br/>");
			buf.append("Merci de conserver ce lien pour pouvoir vous reconnecter plus tard.");
			buf.append("<br/>");
			buf.append("Si vous souhaitez changer votre mot de passe, vous pourrez le faire en vous " +
					"connectant dans l'application, puis en allant dans le menu \"Mon Compte\"");
			buf.append("<br/>");
			buf.append("<br/>");
			
			new MailerService().sendHtmlMail( new MailerMessage(email, "Bienvenue à "+param.nomAmap, buf.toString()));
			
			return null;
		}
		else
		{
			return clearPassword;
		}
	}

	/**
	 * Génère un mot de passe de 6 caractères majuscules
	 * 
	 * @return
	 */
	private String generatePassword()
	{

		int len = 6;
		byte[] buf = new byte[len];

		for (int i = 0; i < len; i++)
		{
			int random = ((int) (Math.random() * 26.0)) % 26;
			buf[i] = (byte) ('A' + random);
		}

		return new String(buf);

	}
	
	// PARTIE SUPPRESSION

	/**
	 * Permet de supprimer un utilisateur 
	 * Ceci est fait dans une transaction en ecriture
	 */
	@DbWrite
	public void deleteUtilisateur(Long id)
	{
		EntityManager em = TransactionHelper.getEm();
		
		Utilisateur u = em.find(Utilisateur.class, id);

		int r = countContrat(u,em);
		if (r>0)
		{
			throw new UnableToSuppressException("Cet utilisateur posséde "+r+" contrats.");
		}
		
		List<RoleList> rs = new AccessManagementService().getUserRole(u, em);
		checkRole(rs,RoleList.ADMIN);
		checkRole(rs,RoleList.TRESORIER);
		checkRole(rs,RoleList.PRODUCTEUR);
		checkRole(rs,RoleList.REFERENT);
		
		em.remove(u);
	}


	private void checkRole(List<RoleList> rls, RoleList rl)
	{
		if (rls.contains(rl))
		{
			throw new UnableToSuppressException("Cet utilisateur est "+rl+". Enlevez lui ce rôle avant de le supprimer.");
		}
		
	}


	private int countContrat(Utilisateur u, EntityManager em)
	{
		Query q = em.createQuery("select count(c) from Contrat c WHERE c.utilisateur=:u");
		q.setParameter("u", u);
			
		return LongUtils.toInt(q.getSingleResult());
	}
	
	
	
	// PARTIE REQUETAGE POUR AVOIR LA LISTE DES UTILISATEURS ACTIFS POUR LE SEARCHER
	
	/**
	 * Permet de charger la liste de tous les utilisateurs
	 * dans une transaction en lecture
	 */
	@DbRead
	public List<Utilisateur> getAllUtilisateursActif()
	{
		EntityManager em = TransactionHelper.getEm();
		
		Query q = em.createQuery("select u from Utilisateur u where u.etatUtilisateur=:etat order by u.nom,u.prenom");
		q.setParameter("etat", EtatUtilisateur.ACTIF);
		List<Utilisateur> us = q.getResultList();
		return us;
	}

	
	// INSERTION EN MASSE DES UTILISATEURS

	@DbWrite
	public void insertAllUtilisateurs(List<UtilisateurDTO> utilisateurs)
	{
		for (UtilisateurDTO utilisateurDTO : utilisateurs)
		{
			createNewUser(utilisateurDTO, false);
		}
	}
	
	// ENVOI DES E MAILS DE BIENVENUE

	/**
	 * TODO faire une transaction pour chaque mail / utilisateur 
	 * 
	 * Si on plante à l'envoi du dernier mail, ca roll back tous les mots de passe ...
	 * @return
	 */
	@DbRead
	public EnvoiMailDTO getEnvoiMailDTO()
	{
		EntityManager em = TransactionHelper.getEm();
		
		EnvoiMailDTO dto = new EnvoiMailDTO();
		
		dto.utilisateurs = new ArrayList<>();
		
		Query q = em.createQuery("select u from Utilisateur u where u.etatUtilisateur=:etat and u.password is null order by u.nom,u.prenom");
		q.setParameter("etat", EtatUtilisateur.ACTIF);
		List<Utilisateur> us = q.getResultList();
		
		for (Utilisateur u : us)
		{
			EnvoiMailUtilisateurDTO emu = new EnvoiMailUtilisateurDTO();
			emu.idUtilisateur = u.getId();
			emu.sendMail = true;
			dto.utilisateurs.add(emu);
		}
		
		return dto;
	}

	@DbWrite
	public void envoiEmailBienvenue(EnvoiMailDTO envoiMail)
	{
		EntityManager em = TransactionHelper.getEm();
		
		for (EnvoiMailUtilisateurDTO dto : envoiMail.utilisateurs)
		{
			Utilisateur utilisateur = em.find(Utilisateur.class, dto.idUtilisateur);
			String clearPassword = generatePassword();
			new PasswordManager().setUserPassword(utilisateur.getId(), clearPassword);
			
			sendEmail(em, utilisateur,  envoiMail.texteMail,clearPassword);
		}
		
	}
	
	
	
	private void sendEmail(EntityManager em, Utilisateur utilisateur, String texte, String clearPassword)
	{
		ParametresDTO param = new ParametresService().getParametres();
		
		//
		String email=utilisateur.getEmail();
		
		String subject = param.nomAmap+" - Bienvenue";
		String htmlContent = texte;
		
		// Mise en place des <br/>
		htmlContent = htmlContent.replaceAll("\r\n", "<br/>");
		htmlContent = htmlContent.replaceAll("\n\r", "<br/>");
		htmlContent = htmlContent.replaceAll("\n", "<br/>");
		htmlContent = htmlContent.replaceAll("\r", "<br/>");
		
		
		// Remplacement des zones de textes
		String link = param.getUrl()+"?username="+email;
		htmlContent = htmlContent.replaceAll("#LINK#", link);
		
		htmlContent = htmlContent.replaceAll("#PASSWORD#", clearPassword);
		htmlContent = htmlContent.replaceAll("#EMAIL#", utilisateur.getEmail());
		
		// Construction du message
		MailerMessage message  = new MailerMessage();
		message.setEmail(email);
		message.setTitle(subject);
		message.setContent(htmlContent);
		
		// Envoi du message
		new MailerService().sendHtmlMail(message);
		//System.out.println("titre="+message.getTitle());
		//System.out.println("email="+message.getEmail());
		//System.out.println("content="+message.getContent());
		
	}
	
	
	

}
