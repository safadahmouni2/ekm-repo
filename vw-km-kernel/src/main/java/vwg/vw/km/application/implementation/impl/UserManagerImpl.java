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
package vwg.vw.km.application.implementation.impl;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.SessionManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.UserDAO;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.27 $ $Date: 2020/02/11 11:19:12 $
 */
public class UserManagerImpl extends DBManagerImpl<UserModel, UserDAO> implements UserManager {

	// cached list of modified catalog and not loaded to user(load on demand)
	private Map<Long, Long> userModifiedDefaultCatalog = new HashMap<Long, Long>();

	public Map<Long, Long> getUserModifiedDefaultCatalog() {
		return userModifiedDefaultCatalog;
	}

	public void setUserModifiedDefaultCatalog(Map<Long, Long> userModifiedDefaultCatalog) {
		this.userModifiedDefaultCatalog = userModifiedDefaultCatalog;
	}

	private volatile static UserManagerImpl manager;

	public static UserManagerImpl get() {
		if (manager == null) {
			synchronized (UserManagerImpl.class) {
				if (manager == null)
					manager = new UserManagerImpl();
			}
		}
		return manager;
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(UserModel model) {
		return model.getUserId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#removeObject(Serializable)
	 */
	public void removeObject(Serializable id) {
		UserModel baseModel = getObject(id);
		baseModel.setActive(Boolean.FALSE);
		// baseModel.setWorkAreas(null);
		// baseModel.setDefaultStdSatzKatRefId(null);
		saveObject(baseModel);
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getUserByKUMSId(String)
	 */
	public UserModel getUserByKUMSId(String kums) {
		return dao.getUserByKUMSId(kums);
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getAdministratedUsers(Long)
	 */
	public List<UserModel> getAdministratedUsers(Long workAreaId) {
		return dao.getAdministratedUsers(workAreaId);
	}

	/**
	 * PTS requirement 22205:statistic page
	 * 
	 * @see vwg.vw.km.application.implementation.UserManager#getActiveUsers()
	 * 
	 *      list of active users count
	 */
	public int getActiveUsersCount() {
		return dao.getActiveUsersCount();
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getUsersBySearchString(String)
	 */
	public List<UserModel> getUsersBySearchString(String searchString) {
		return dao.getUsersBySearchString(searchString);
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected UserModel beforeSave(UserModel baseModel) {
		baseModel = super.beforeSave(baseModel);
		UserModel user = (UserModel) SessionManager.get().getUserObject();
		if (baseModel.getUserId() == null) {
			if (baseModel.getCreationDate() == null || baseModel.getCreationDate().isNull()) {
				baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
			}
			if (user != null && baseModel.getCreatorRefId() == null) {
				baseModel.setCreatorRefId(user.getUserId());
			}
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
			if (user != null) {
				baseModel.setModifierRefId(user.getUserId());
			}
		}
		if (baseModel.getActive() == null) {
			baseModel.setActive(Boolean.TRUE);
		}
		if (baseModel.getAnonymized() == null) {
			baseModel.setActive(Boolean.FALSE);
		}
		if (Boolean.FALSE.equals(baseModel.getActive())) {
			if (baseModel.getOldActive() == null || Boolean.TRUE.equals(baseModel.getOldActive())) {
				baseModel.setDeactivationDate(BaseDateTime.getCurrentDateTime());
			}
		} else {
			baseModel.setDeactivationDate(null);
			// US_10243940_Job_to_deactivate_UserAccount : when account become active by ADMIN, the validationCode and
			// SentDateCode must be null
			baseModel.setActivationCode(null);
			baseModel.setActivationCodeSentDate(null);
		}
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getUsersByCatalog(Long)
	 */
	public List<UserModel> getUsersByCatalog(Long catalogId) {
		return dao.getUsersByCatalog(catalogId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getWorkingUsers()
	 */
	public List<UserModel> getWorkingUsers() {
		return dao.getWorkingUsers();
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getUsersToBeAnonomyzedByBatch()
	 */
	public List<UserModel> getUsersToBeAnonomyzedByBatch() {
		return dao.getUsersToBeAnonomyzedByBatch();
	}

	/**
	 * @see vwg.vw.km.application.implementation.UserManager#getUsersToBeDeactivatedByBatch()
	 */
	public List<UserModel> getUsersToBeDeactivatedByBatch() {
		return dao.getUsersToBeDeactivatedByBatch();
	}

	/**
	 * 
	 */
	public UserModel getUserByActivationCode(String searchString) {
		return dao.getUserByActivationCode(searchString);
	}
}
