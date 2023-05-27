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
package vwg.vw.km.application.service.logic;

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
 * @author hamdiABID
 * 
 */
import java.util.List;

import vwg.vw.km.application.implementation.NewsManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.NewsDTO;
import vwg.vw.km.integration.persistence.model.NewsModel;

public class NewsService extends BaseService<NewsDTO> {

	private NewsManager newsManager;

	public NewsManager getNewsManager() {
		return newsManager;
	}

	public void setNewsManager(NewsManager newsManager) {
		this.newsManager = newsManager;
	}

	/**
	 * return list of all news associated to given work area
	 * 
	 * @param newsDTO
	 * @return
	 */
	public NewsDTO loadListNews(NewsDTO newsDTO) {

		List<NewsModel> newsList = newsManager.getObjects();
		newsDTO.setNewsList(newsList);

		return newsDTO;
	}

	/**
	 * 
	 * 
	 * @param newsDTO
	 * @return list of news sorted by latest date
	 */

	public NewsDTO loadListNewsSortedByLatestDate(NewsDTO newsDTO) {

		List<NewsModel> newsList = newsManager.getListNewsSortedByLatestDate();
		newsDTO.setNewsList(newsList);

		return newsDTO;
	}

	/**
	 * return list of news which are active and valid, then the list is sorted by date
	 * 
	 * @param newsDTO
	 * @return
	 */
	public NewsDTO loadListNewsActivAndSortedByLatestDate(NewsDTO newsDTO) {

		List<NewsModel> newsList = newsManager.getListNewsActivAndSortedByLatestDate();
		newsDTO.setNewsList(newsList);

		return newsDTO;
	}

	/**
	 * load requested user object in DTO for Admin purpose
	 * 
	 * @param userDTO
	 * @return
	 */
	public NewsDTO loadNewsDTO(NewsDTO newsDTO) {

		NewsModel news = newsManager.getObject(newsDTO.getNewsId());
		if (news != null) {
			newsDTO.setNews(news);
		}
		return newsDTO;
	}

	/**
	 * save current user in DTO
	 * 
	 * @param userDTO
	 * @return
	 */
	public NewsDTO save(NewsDTO newsDTO) {

		NewsModel news = newsDTO.getNews();
		newsManager.saveObject(news);

		return newsDTO;
	}

	/**
	 * delete current news in DTO
	 * 
	 * @param newsDTO
	 * @return
	 */
	public NewsDTO delete(NewsDTO newsDTO) {

		newsManager.removeObject(newsDTO.getNewsId());
		return newsDTO;
	}

	/**
	 * @see vwg.vw.km.application.service.base.BaseService#loadDTOForListBySearchString(vwg.vw.km.application.service.dto.base.BaseDTO)
	 */
	public NewsDTO loadDTOForListBySearchString(NewsDTO newsDTO) {
		newsDTO.setNewsList(newsManager.getNewsBySearchString(newsDTO.getSearchString()));
		newsDTO.setNoSearchResult(newsDTO.isNoNews());
		return newsDTO;
	}

}
