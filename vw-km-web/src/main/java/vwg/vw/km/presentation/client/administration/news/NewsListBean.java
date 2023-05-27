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
package vwg.vw.km.presentation.client.administration.news;

import vwg.vw.km.application.service.dto.NewsDTO;
import vwg.vw.km.application.service.logic.NewsService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.presentation.client.base.BaseBean;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 */
public class NewsListBean extends BaseBean<NewsDTO, NewsService> {

	private static final long serialVersionUID = 3487994915234656427L;

	private final static Log log = LogManager.get().getLog(NewsListBean.class);

	private Long objectType;

	private int currentIndex;

	private int maxRange;

	public static String NEWS_DETAIL_MASK = "./administration/newsAdministration/newsDetail.xhtml";

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.news.search");
	}

	/**
	 * Used to Load News list without a clicked node on News verwalten
	 * 
	 * @param event
	 *            ActionEvent object
	 */
	public Boolean getLoadWithoutClickedNode() {
		log.info("loadWithoutClickedNode method started");
		NewsDTO d = new NewsDTO();
		service.loadListNewsActivAndSortedByLatestDate(d);
		setDto(d);
		log.info("loadWithoutClickedNode method executed");
		return true;
	}

	@Override
	public void specificLoadDTO() {
		if (!dto.isSearchMode()) {
			NewsDTO d = new NewsDTO();
			service.loadListNewsSortedByLatestDate(d);
			setDto(d);
		}
	}

	public Long getObjectType() {
		return objectType;
	}

	public void setObjectType(Long objectType) {
		this.objectType = objectType;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}
}
