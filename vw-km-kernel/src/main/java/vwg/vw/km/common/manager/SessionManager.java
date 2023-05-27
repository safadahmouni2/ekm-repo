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
package vwg.vw.km.common.manager;

import java.util.HashMap;
import java.util.Map;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.8 $ $Date: 2018/11/13 14:18:12 $
 */
public class SessionManager {

	/**
	 * Comment for <code>log</code>
	 */
	private final Log log = LogManager.get().getLog(SessionManager.class);

	private static SessionManager sessionManager;

	private Map<Integer, Object> threads = null;

	public SessionManager() {
		super();
		threads = new HashMap<Integer, Object>();
	}

	public static SessionManager get() {
		if (sessionManager == null) {
			sessionManager = new SessionManager();
		}
		return sessionManager;
	}

	public void register(Object o1) {
		if (log.isDebugEnabled()) {
			log.debug("try to register: [" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName()
					+ "] ...");
		}
		threads.put(getThreadIdentificatorRoot(), o1);
		log.info("[" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName()
				+ "] registered., session=" + o1);

	}

	public void unregister() {
		if (log.isDebugEnabled()) {
			log.debug("try to unregister: [" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName()
					+ "] ...");
		}
		threads.remove(getThreadIdentificatorRoot());
		log.info("[" + Thread.currentThread().getId() + ", " + Thread.currentThread().getName() + "] unregistered.");

	}

	/**
	 * Returns a unique identificator of the thread using this manager
	 * 
	 * @return int the hashcode of the current thread
	 */
	private final Integer getThreadIdentificatorRoot() {
		return new Integer(Thread.currentThread().hashCode());
	}

	public Object getUserObject() {
		return threads.get(getThreadIdentificatorRoot());
	}

}
