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
package vwg.vw.km.presentation.client.navigation.node.administration;

import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.NewsDTO;
import vwg.vw.km.integration.persistence.model.NewsModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.ObjectNode;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * 
 */
public class NewsNode extends ObjectNode {

	public NewsNode(NavigationBean tree, FolderNode parent, String text, Long newsId, boolean isSearchNode) {
		super(tree, parent, isSearchNode);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./administration/newsAdministration/newsDetail.xhtml");
		setPopupPageName("./administration/newsAdministration/popup.xhtml");
		setLeafIcon(makeIcon("news-icon.jpg"));
		setNodeId(newsId);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */
	@Override
	public void specificNodeClicked() {
		refresh();
		NewsDTO newsDTO = new NewsDTO();
		newsDTO.setUserLogged(treeBean.getUserFromSession());
		newsDTO.setNewsId(getNodeId());
		setDto(treeBean.getNewsService().loadNewsDTO(newsDTO));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.NEWS_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("newsAdministration.deleteConfirm");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.news.delete");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.news.edit");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		specificNodeClicked();
		NewsDTO dto = (NewsDTO) getDto();
		dto.setNewsId(dto.getNews().getNewsId());
		treeBean.getNewsService().delete(dto);

		// Refresh the news verwalten folder when deleting an object
		treeBean.getNewsAdministrationNode().expandClicked(event);
		// if the delete in search mode , folders must be refreshed

		NewsSearchResultNode searchNode = (NewsSearchResultNode) treeBean.getObjectTypeSearchResultNode()
				.get(EnumObjectTypeManager.NEWS_ID);
		if (searchNode.getDto() != null) {
			List<NewsModel> newsList = ((NewsDTO) searchNode.getDto()).getNewsList();
			for (NewsModel news : newsList) {
				if (news.getNewsId().equals(getNodeId())) {
					newsList.remove(news);
					((NewsDTO) searchNode.getDto()).setNewsList(newsList);
					searchNode.expandClicked(event);
					break;
				}
			}
		}

		return "";
	}
}
