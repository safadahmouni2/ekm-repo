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
package vwg.vw.km.presentation.client.login;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.UserModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : User login bean class
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.41 $ $Date: 2020/08/21 07:57:27 $
 */
public class UserLoginBean {

	public static final String LOGIN_SUCCESS = "loginSuccess";

	public static final String LOGIN_FAILURE = "loginFailure";

	private final Log log = LogManager.get().getLog(UserLoginBean.class);

	private UserService userService = null;

	private String login = null;

	private String password = null;

	private String externalLogoutUrl = null;

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setExternalLogoutUrl(String externalLogoutUrl) {
		this.externalLogoutUrl = externalLogoutUrl;
	}

	// ZS: PTS_Requirement-22198: The user manual is no longer set in the user session. It will be downloaded with
	// external link
	public UserLoginBean() {
	}

	/**
	 * login action:check success or failure
	 */
	public String doLogin() {
		if (log.isInfoEnabled()) {
			log.info("Try to login...");
		}
		UIComponent component = FacesContext.getCurrentInstance().getViewRoot().findComponent("loginForm:loginError");
		UserModel user = userService.getActiveUserByKUMSId(getLogin());
		if (user != null) {
			BaseDateTime lastNotificationDate = user.getLastNotificationDate();
			log.info("User was notified:   " + lastNotificationDate);
			if (lastNotificationDate == null || lastNotificationDate.isNull()) {
				log.info("User was not notified before ");
				user.setToBeNotified(true);
			} else {
				String notificationYear = lastNotificationDate.getString(BaseDateTime.YYYY);
				String currentYear = BaseDateTime.getCurrentDateTime().getCurrentYear();
				if (!notificationYear.equals(currentYear)) {
					user.setToBeNotified(true);
				}
			}

			setSessionLoginParam(user);
			/**
			 * PTS requirement 22211: ABA :Benachrichtigungsfunktion Executed when the user login in testSystem Save
			 * LOGIN DateTime to use it as input to get last changes from previous login
			 */
			// Get the last login time saved in DB
			user.setPreviousLoginTime(user.getLastLogInTime());
			if (log.isInfoEnabled()) {
				log.info("Logged user is : " + user);
				log.info("Previous LOGIN Time:  " + user.getLastLogInTime() + " ---- Current LOGIN Time :"
						+ BaseDateTime.getCurrentDateTime());
			}
			// Save the current login time in DB
			user.setLastLogInTime(BaseDateTime.getCurrentDateTime());
			UserDTO dto = new UserDTO();
			dto.setUser(user);
			userService.save(dto);
			return LOGIN_SUCCESS;
		} else {
			if (log.isInfoEnabled()) {
				log.info("Login failure");
			}
			FacesContext facesContext = FacesContext.getCurrentInstance();
			FacesMessage message = new FacesMessage("Bitte prüfen Sie Ihre Zugangsdaten!");
			facesContext.addMessage(component.getClientId(facesContext), message);
			return LOGIN_FAILURE;
		}
	}

	/**
	 * logout action
	 */
	public String doLogout() throws IOException {
		UserModel user = getLoggedUser();
		if (log.isInfoEnabled()) {
			log.info("User: " + user.getFullName() + " Try to logout...");
		}
		ExternalContext ectx = FacesContext.getCurrentInstance().getExternalContext();
		invalidateSession(ectx);
		// if (externalLogoutUrl != null && !"".equals(externalLogoutUrl.trim())
		// && !"NO".equals(externalLogoutUrl.toUpperCase())) {
		// ectx.redirect(externalLogoutUrl);
		// return null;
		// }
		// ectx.redirect(ectx.getRequestContextPath()+ "/pages/login.jsf");
		// return LOGIN_FAILURE;
		// this param will be used
		// <context-param>
		// <param-name>org.icefaces.sessionExpiredRedirectURI</param-name>
		// <param-value>/pages/login.jsf</param-value>
		// </context-param>
		return null;
	}

	/**
	 * invalidate session
	 */
	private void invalidateSession(ExternalContext ectx) {
		HttpSession httpSession = null;
		// make sure we have a valid facesContext
		if (ectx != null) {
			ectx.invalidateSession();
			httpSession = (HttpSession) ectx.getSession(false);
		}
		// finally make sure the session is not null before trying to set the
		// login
		if (httpSession != null) {
			httpSession.invalidate();
		}
	}

	/**
	 * set current user in session as attribute
	 * 
	 * @param user
	 *            logged
	 */
	private void setSessionLoginParam(UserModel user) {
		HttpSession httpSession = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		// make sure we have a valid facesContext
		if (facesContext != null) {
			httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		// finally make sure the session is not null before trying to set the
		// login
		if (httpSession != null) {
			httpSession.setAttribute(UserModel.class.getName(), user);
		}
	}

	/**
	 * return the logged user from the session
	 * 
	 * @return logged user
	 */
	private UserModel getLoggedUser() {
		HttpSession httpSession = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		// make sure we have a valid facesContext
		if (facesContext != null) {
			httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		// finally make sure the session is not null before trying to set the
		// login
		if (httpSession != null) {
			return (UserModel) httpSession.getAttribute(UserModel.class.getName());
		}
		return null;
	}
}
