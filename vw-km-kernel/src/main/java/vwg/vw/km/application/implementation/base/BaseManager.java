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
package vwg.vw.km.application.implementation.base;

import java.io.Serializable;
import java.util.List;

import vwg.vw.km.integration.persistence.dao.base.BaseDao;
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
 * @param <E>
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.4 $ $Date: 2011/08/17 06:59:41 $
 */
public interface BaseManager<T extends BaseModel, E extends BaseDao<T>> {

	/**
	 * Expose the setDao method for testing purposes
	 * 
	 * @param dao
	 */
	public void setDao(E dao);

	/**
	 * Generic method used to get a all objects of a particular type.
	 * 
	 * @return List of populated objects
	 */
	public List<T> getObjects();

	/**
	 * Generic method to get an object based on class and identifier.
	 * 
	 * @param id
	 *            the identifier (primary key) of the class
	 * @return a populated object
	 */
	public T getObject(Serializable id);

	/**
	 * Generic method to save an object.
	 * 
	 * @param o
	 *            the object to save
	 */
	public Serializable saveObject(T o);

	/**
	 * Generic method to save a list of object.
	 * 
	 * @param list
	 *            to save
	 */
	public void saveObjects(List<T> list);

	/**
	 * Generic method to delete an object based on class and id
	 * 
	 * @param id
	 *            the identifier of the class
	 */
	public void removeObject(Serializable id);

	/**
	 * 
	 * @param model
	 *            object
	 * @return identifier of object
	 */
	public Serializable getObjectPK(T model);

	/**
	 * remove all existing objects
	 */
	public void removeAll();
}
