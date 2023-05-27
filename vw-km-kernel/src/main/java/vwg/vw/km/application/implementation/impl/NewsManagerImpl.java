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
import java.util.List;

import vwg.vw.km.application.implementation.NewsManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.integration.persistence.dao.NewsDAO;
import vwg.vw.km.integration.persistence.model.NewsModel;

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
 */
public class NewsManagerImpl extends DBManagerImpl<NewsModel, NewsDAO> implements NewsManager {

	private volatile static NewsManagerImpl manager;

	public static NewsManagerImpl get() {
		if (manager == null) {
			synchronized (NewsManagerImpl.class) {
				if (manager == null)
					manager = new NewsManagerImpl();
			}
		}
		return manager;
	}

	public Serializable getObjectPK(NewsModel model) {
		return model.getNewsId();
	}

	public List<NewsModel> getListNewsActivAndSortedByLatestDate() {
		return dao.getListNewsActivAndSortedByLatestDate();
	}

	public List<NewsModel> getListNewsSortedByLatestDate() {
		return dao.getListNewsSortedByLatestDate();
	}

	/**
	 * @see vwg.vw.km.application.implementation.NewsManager#getNewsBySearchString(String)
	 */
	public List<NewsModel> getNewsBySearchString(String searchString) {
		return dao.getNewsBySearchString(searchString);
	}

}
