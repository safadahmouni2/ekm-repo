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

package vwg.vw.km.integration.persistence.dao.base;

import java.io.Serializable;
import java.util.List;

import vwg.vw.km.integration.persistence.model.base.BaseModel;

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
 * @param <T>
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.3 $ $Date: 2012/11/21 13:40:28 $
 */
public interface BaseDao<T extends BaseModel> {

	/**
	 * Generic method used to get all objects of a particular type. This is the same as lookup up all rows in a table.
	 * 
	 * @return List of populated objects
	 */
	public List<T> getObjects();

	/**
	 * Generic method to get an object based on class and identifier. An ObjectRetrievalFailureException Runtime
	 * Exception is thrown if nothing is found.
	 * 
	 * @param id
	 *            the identifier (primary key) of the class
	 * @return a populated object
	 * @see org.springframework.orm.ObjectRetrievalFailureException
	 */
	public T getObject(Serializable id);

	/**
	 * Generic method to save an object - handles both update and insert.
	 * 
	 * @param o
	 *            the object to save
	 */
	public Serializable saveObject(T o);

	/**
	 * Generic method to delete an object based on class and id
	 * 
	 * @param id
	 *            the identifier (primary key) of the class
	 */
	public void removeObject(Serializable id);

	/**
	 * Generic method to update an object - handles both update .
	 * 
	 * @param o
	 *            the object to save
	 */
	public void updateObject(T o);

	/**
	 * remove all data
	 * 
	 */
	public void removeAll();

	/**
	 * Remove this instance from the session cache. Changes to the instance will not be synchronized with the database
	 * 
	 * @param model
	 *            - a persistent instance
	 */
	public void evict(T model);
}
