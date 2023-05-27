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
package vwg.vw.km.presentation.client.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.manager.SessionManager;
import vwg.vw.km.common.type.BaseDateTime;
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
 * @author Sebri Zouhaier changed by $Author: lassaad $
 * @version $Revision: 1.22 $ $Date: 2021/05/31 10:16:44 $
 */
public class BaseVKMFilter implements Filter {

	private final Log log = LogManager.get().getLog(BaseVKMFilter.class);

	private UserService userService = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		log.debug("VKM Application base filter started");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#destroy()
	 */
	@Override
	public void destroy() {
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse,
	 *      javax.servlet.FilterChain)
	 */
	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = (HttpServletRequest) req;
		long duration, starttime = System.currentTimeMillis();

		UserModel user = (UserModel) httpReq.getSession().getAttribute(UserModel.class.getName());

		// LBJ 2021-05-31 - Migration of Webseal to APM
		// --> evtl. use of http Header ‚X-Auth-User‘ instead of 'iv-user'
		String ivUser = httpReq.getHeader("iv-user");

		Enumeration<String> headerNames = httpReq.getHeaderNames();
		while (headerNames.hasMoreElements()) {
			String headerName = headerNames.nextElement();
			log.debug("header Name: " + headerName + " - value: " + httpReq.getHeader(headerName));
		}
		/**
		 * 26-11-2013: PTS-Ticket-34292:ABA Avoid the Exception display when requesting application
		 * using:app_url/pages/home.jsf If there is no logged user and request URL is app_url/pages/home.jsf : the user
		 * should be redirected to login.jsf page
		 */
		String fromURL = httpReq.getRequestURL().toString();
		HttpServletRequest request = (HttpServletRequest) req;
		String contextPath = request.getContextPath();
		String incomeURL = contextPath + "/pages/home.jsf";
		if (fromURL.endsWith(incomeURL) && user == null && (ivUser == null || "".equals(ivUser.trim()))) {
			if (log.isInfoEnabled()) {
				log.debug(" ###### No user logged ==> redirect to login page ###### ");
			}
			// No logged-in user found, so redirect to login page.
			HttpServletResponse response = (HttpServletResponse) res;
			response.sendRedirect(contextPath + "/pages/login.jsf");
		} else {
			if (user == null && ivUser != null && !"".equals(ivUser.trim())) {
				user = userService.getActiveUserByKUMSId(httpReq.getHeader("iv-user"));
				if (user == null) {
					log.warn("Given iv-user :" + ivUser + " is not supported by EKM System");
					// PTS_Problem 34837
					// You aren't returning after the forward when the user is not been supplied. It's a common
					// misconception among starters that the forward() method magically terminates the code execution
					// and jumps
					// out of the method somehow. This is thus not true. You have to return from the method and stop the
					// execution
					// of the remnant of the code yourself.
					//
					// You need to either add a return;
					httpReq.getRequestDispatcher("/NoValidIvUser.html").forward(req, res);
					return;
				}
				BaseDateTime lastNotificationDate = user.getLastNotificationDate();
				log.info("User was notified:   " + lastNotificationDate);
				if (lastNotificationDate == null || lastNotificationDate.isNull()) {
					log.debug("User was not notified before ");
					user.setToBeNotified(true);
				} else {
					String notificationYear = lastNotificationDate.getString(BaseDateTime.YYYY);
					String currentYear = BaseDateTime.getCurrentDateTime().getCurrentYear();
					if (!notificationYear.equals(currentYear)) {
						user.setToBeNotified(true);
					}
				}

				/**
				 * PTS requirement 22211: ABA :Benachrichtigungsfunktion Executed when the user is already logged with
				 * the SSO Save LOGIN DateTime to use it as input to get last changes from previous login
				 */
				// Get the last login time saved in DB
				user.setPreviousLoginTime(user.getLastLogInTime());
				if (log.isInfoEnabled()) {
					log.debug("Logged user is : " + user);
					log.debug("Previous LOGIN Time:  " + user.getLastLogInTime() + " ---- Current LOGIN Time :"
							+ BaseDateTime.getCurrentDateTime());
				}
				// Save the current login time in DB
				user.setLastLogInTime(BaseDateTime.getCurrentDateTime());
				UserDTO dto = new UserDTO();
				dto.setUser(user);
				userService.save(dto);
				log.debug("Given iv-user :" + ivUser + " is logged EKM System");
				httpReq.getSession().setAttribute(UserModel.class.getName(), user);
			}

			Long userId = 0L;
			if (user != null) {
				userId = user.getUserId();
			}

			Thread.currentThread().setName("__" + userId + "__" + httpReq.getSession().getId() + "__");
			if (log.isDebugEnabled()) {
				log.debug("STARTED  URL :" + httpReq.getHeader("iv-user") + ": " + httpReq.getRequestURL());
			}
			SessionManager.get().register(user);
			chain.doFilter(req, res);
			// after response returns, calculate duration and log it
			duration = System.currentTimeMillis() - starttime;
			if (log.isDebugEnabled()) {
				log.debug("DURATION URL :" + httpReq.getHeader("iv-user") + ": " + httpReq.getRequestURL() + " - "
						+ duration);
			}
		}

	}

}
