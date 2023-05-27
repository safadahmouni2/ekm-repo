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
package vwg.vw.km.application.implementation.impl.base;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
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
 * @param <X>
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.8 $ $Date: 2011/08/17 06:59:41 $
 */
public abstract class DBManagerImpl<T extends BaseModel, X extends BaseDao<T>> implements BaseManager<T, X> {
	private final Log log = LogManager.get().getLog(DBManagerImpl.class);

	protected X dao = null;

	public void setDao(X dao) {
		this.dao = dao;
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObject(java.lang.Class, java.io.Serializable)
	 */
	public T getObject(Serializable id) {
		// avoid exception: java.lang.IllegalArgumentException: id to load is required for loading
		if (id == null) {
			log.info("the id object given to load " + getClass() + " is Null  : " + id);
			return null;
		}
		return (T) dao.getObject(id);
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjects(java.lang.Class)
	 */
	public List<T> getObjects() {
		return dao.getObjects();
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#removeObject(java.lang.Class, java.io.Serializable)
	 */
	public void removeObject(Serializable id) {
		dao.removeObject(id);
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#saveObject(java.lang.Object)
	 */
	public Serializable saveObject(T o) {
		o = beforeSave(o);
		if (!o.isLoaded()) {
			return dao.saveObject(o);
		} else {
			dao.updateObject(o);
			return getObjectPK(o);
		}

	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#saveObjects(java.util.List)
	 */
	public void saveObjects(List<T> list) {
		if (list != null) {
			for (Iterator<T> i = list.iterator(); i.hasNext();) {
				saveObject(i.next());
			}
		}
	}

	/**
	 * Method will be executed before saving the object. It should be overridden to allow specific setting (such as save
	 * dependend object ) before saving the object
	 * 
	 * @param baseModel
	 *            Object to be saved
	 * @return the modified object
	 */
	protected T beforeSave(T baseModel) {
		// nothing to do it should be overrided
		// Please dont do anything here
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#removeAll()
	 */
	public void removeAll() {
		dao.removeAll();
	}

}
