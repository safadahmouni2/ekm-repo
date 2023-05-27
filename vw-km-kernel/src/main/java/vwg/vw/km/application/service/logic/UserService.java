/*
 * Copyright (c) VW All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by VW Software as part
 * of an VW Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of VW Software.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD INPRISE, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */
package vwg.vw.km.application.service.logic;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.32 $ $Date: 2020/08/21 07:58:11 $
 */
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.RandomString;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;

public class UserService extends BaseService<UserDTO> {

	private UserManager userManager;

	private FolderManager folderManager;

	private EnumObjectTypeManager enumObjectTypeManager;

	private MailService mailService;

	private final static int CHECKSUM_LENGTH = 14;

	private final Log log = LogManager.get().getLog(UserService.class);

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setEnumObjectTypeManager(EnumObjectTypeManager enumObjectTypeManager) {
		this.enumObjectTypeManager = enumObjectTypeManager;
	}

	public MailService getMailService() {
		return mailService;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}

	/**
	 * save current user in DTO
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO save(UserDTO userDTO) {
		UserModel user = userDTO.getUser();
		Long userId = user.getUserId();

		// US_10243940_Job_to_deactivate_UserAccount : when account become inactive by ADMIN, generate a activationCode
		// + send mail
		if (Boolean.FALSE.equals(user.getActive()) && user.getActivationCode() == null) {
			RandomString gen = new RandomString(CHECKSUM_LENGTH);
			user.setActivationCode(gen.nextString());
			user.setActivationCodeSentDate(BaseDateTime.getCurrentDateTime());
			sendMailToNotifyUser(user);
			log.info("send before save ");
		}

		userManager.saveObject(user);

		if (userId == null) {
			FolderModel elementFolder = new FolderModel();
			elementFolder.setDesignation("Element Ordner_" + user.getUserId());
			elementFolder.setObjectType(enumObjectTypeManager.getObject(1L));
			elementFolder.setCreatorRefId(user.getUserId());
			folderManager.saveObject(elementFolder);

			FolderModel componentFolder = new FolderModel();
			componentFolder.setDesignation("Baustein Ordner_" + user.getUserId());
			componentFolder.setCreatorRefId(user.getUserId());
			componentFolder.setObjectType(enumObjectTypeManager.getObject(EnumObjectTypeManager.COMPONENT_ID));
			folderManager.saveObject(componentFolder);
		}
		return userDTO;
	}

	/**
	 * delete current user in DTO
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO delete(UserDTO userDTO) {
		// do deactivate user + generate Activation code + send mail

		UserModel user = userDTO.getUser();
		user.setActive(Boolean.FALSE);
		RandomString gen = new RandomString(CHECKSUM_LENGTH);
		user.setActivationCode(gen.nextString());
		user.setActivationCodeSentDate(BaseDateTime.getCurrentDateTime());
		sendMailToNotifyUser(user);
		log.info("Email sent to user: account has been deactivated by delete action");

		userManager.saveObject(user);
		// userManager.removeObject(userDTO.getUserId());
		return userDTO;
	}

	/**
	 * return list of all users associated to given work area
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO loadUsers(UserDTO userDTO) {
		List<UserModel> list = userManager.getAdministratedUsers(userDTO.getWorkAreaId());
		// list.remove(userDTO.getUserLogged());
		userDTO.setUsers(list);
		return userDTO;
	}

	/**
	 * return list of all users associated to given work area
	 * 
	 * @param userDTO
	 * @return
	 */
	public int getActiveUsersCount() {
		int activeUsersCount = userManager.getActiveUsersCount();
		return activeUsersCount;
	}

	/**
	 * load requested user object in DTO for Admin purpose
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO loadUserDTO(UserDTO userDTO) {
		UserModel user = userManager.getObject(userDTO.getUserId());
		if (user != null) {
			userDTO.setUser(user);
		}
		return userDTO;
	}

	/**
	 * return User By given KUMS
	 * 
	 * @param kums
	 * @return
	 */
	public UserModel getUserByKUMSId(String kums) {
		return userManager.getUserByKUMSId(kums);
	}

	/**
	 * return Active User By given KUMS
	 * 
	 * @param kums
	 * @return
	 */
	public UserModel getActiveUserByKUMSId(String kums) {
		if (kums == null || "".equals(kums)) {
			return null;
		}
		UserModel user = getUserByKUMSId(kums);
		if (user != null && user.getActive()) {
			return user;
		}
		return null;
	}

	/**
	 * @see vwg.vw.km.application.service.base.BaseService#loadDTOForListBySearchString(vwg.vw.km.application.service.dto.base.BaseDTO)
	 */
	public UserDTO loadDTOForListBySearchString(UserDTO userDTO) {
		userDTO.setUsers(userManager.getUsersBySearchString(userDTO.getSearchString()));
		userDTO.setNoSearchResult(userDTO.isNoUsers());
		return userDTO;
	}

	/**
	 * return list of all working users
	 * 
	 * @param userDTO
	 * @return list of users Model
	 */
	public UserDTO loadWorkingUsers(UserDTO userDTO) {
		List<UserModel> list = userManager.getWorkingUsers();
		userDTO.setUsers(list);
		return userDTO;
	}

	/**
	 * return User By given activationCode
	 * 
	 * @param activationCode
	 * @return UserModel
	 */
	public UserModel getUserByActivationCode(String activationCode) {
		return userManager.getUserByActivationCode(activationCode);
	}

	/**
	 * send Email To Notify user when the ADMIN inactive his account
	 * 
	 * @param user
	 */
	public void sendMailToNotifyUser(UserModel user) {
		if (user.getEmail() != null) {
			try {
				mailService.sendMail(user);
				log.info("mail has been sent to " + user.getEmail());
			} catch (Exception e) {
				log.error("No email has been sent to this user", e);
			}
		} else {
			log.info("Mail could not be send because Emai address is  " + user.getEmail());
		}
	}

	/**
	 * Set the notification date when user receive the privacy notification
	 * 
	 * @param user
	 *            logged use
	 */
	public void saveNotificationDate(UserModel user) {
		log.info("Set the notification date for user: " + user.getUserId());
		user.setLastNotificationDate(BaseDateTime.getCurrentDateTime());
		userManager.saveObject(user);
	}

}
