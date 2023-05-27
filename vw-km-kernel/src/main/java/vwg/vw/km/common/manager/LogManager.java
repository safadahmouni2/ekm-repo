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

import java.io.File;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;

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
 * @version $Revision: 1.7 $ $Date: 2022/01/19 09:37:04 $
 */
public class LogManager {

	/** the <code>manager</code> instance holder */
	private volatile static LogManager manager;

	/**
	 * private constructor only for internal use.
	 */
	private LogManager() {
		init();
	}

	/**
	 * @return the active instance of the <code>LogManager</code> or create and return a new one.
	 */
	public static LogManager get() {
		if (manager == null) {
			synchronized (LogManager.class) {
				if (manager == null) {
					manager = new LogManager();
				}
			}
		}
		return manager;
	}

	/**
	 * 
	 * @param c
	 *            the class name to log as Class
	 * @return Logger the logger instance
	 */
	public Log getLog(Class<?> c) {
		return new Log(org.apache.logging.log4j.LogManager.getLogger(c));
	}

	/**
	 * 
	 * @param c
	 *            the class name to log as String
	 * @return Logger the logger instance
	 */
	public Log getLog(String c) {
		return new Log(org.apache.logging.log4j.LogManager.getLogger(c));
	}

	/**
	 * 
	 * @param name
	 *            create a new logger for class name <tt>name</tt>
	 * @return Logger the logger instance
	 */
	public Logger makeNewLoggerInstance(String name) {
		return org.apache.logging.log4j.LogManager.getLogger(name);
	}

	/**
	 * You must call this static method to initialize global logging mechanism. Searches for properties file in current
	 * CLASSPATH.
	 */
	public void init() {

		try {
			System.out.println(
					"LogManager ... User's current working directory: user.dir=" + System.getProperty("user.dir"));
			System.out.println("LogManager ... Log directory: log_dir=" + System.getProperty("log_dir"));

			// Get log4j conf file for DEV/Tentun System
			URL url = this.getClass().getResource("/conf/log4j.xml");

			if (url == null) {
				throw new RuntimeException("LogManager: init(): log4j-config file not found: conf/log4j.xml");
			}
			File confFile = new File(url.getFile());
			// Load conf file for QA/Prod
			// @todo: use property to declare the file path
			if (!confFile.exists()) {
				System.out.println(url.getFile() + " File not found");
				confFile = new File("/appl/webappl/process_designer/appl/wl_config/log4j.xml");
			}
			System.out.println("LogManager: init(): log4j.config url : " + url);
			System.out.println("LogManager: init(): log4j.config file : " + confFile.getPath());

			Configurator.initialize(LogManager.class.getName(), LogManager.class.getClassLoader(), confFile.toURI());

			System.out.println("LogManager: init(): log4j configuration finished successfully!");
		} catch (Throwable e) {
			System.out.println("\nerror loading prop : " + e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

}
