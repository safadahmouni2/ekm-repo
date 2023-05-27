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
package vwg.vw.km.application.implementation;

import java.util.List;

import vwg.vw.km.application.implementation.base.BaseManager;
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
 * @version $Revision: 1.16 $ $Date: 2020/01/30 16:13:49 $
 */
public interface UserManager extends BaseManager<UserModel, UserDAO> {

	/**
	 * return user by kums id
	 * 
	 * @param kums
	 * @return
	 */
	public UserModel getUserByKUMSId(String kums);

	/**
	 * list of administrated users in given work area
	 * 
	 * @param workAreaId
	 * @return
	 */
	public List<UserModel> getAdministratedUsers(Long workAreaId);

	/**
	 * PTS requirement 22205:statistic page list of active users
	 * 
	 * @return activeUsersCount
	 */
	public int getActiveUsersCount();

	/**
	 * get Users By SearchString
	 * 
	 * @param searchString
	 * @return
	 */
	public List<UserModel> getUsersBySearchString(String searchString);

	/**
	 * return list of users that use this catalog
	 * 
	 * @param catalogId
	 *            id of the catalog to check
	 * @return list of users Model
	 */
	public List<UserModel> getUsersByCatalog(Long catalogId);

	/**
	 * list of working users to be used as folders
	 * 
	 * @return list of users Model
	 */
	public List<UserModel> getWorkingUsers();

	/**
	 * return list of users to be anonomyzed
	 * 
	 * @return list of users Model
	 */
	public List<UserModel> getUsersToBeAnonomyzedByBatch();

	/**
	 * return list of users to be deactivated
	 * 
	 * @return list of users Model
	 */
	public List<UserModel> getUsersToBeDeactivatedByBatch();

	/**
	 * return a user to be reactivate by activationCode
	 * 
	 * @return user Model
	 */
	public UserModel getUserByActivationCode(String searchString);
}
