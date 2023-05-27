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
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.NewsDTO;
import vwg.vw.km.integration.persistence.model.NewsModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.StaticFolderNode;

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
 * @author hamdiABID $
 * @version
 */
public class NewsAdministrationNode extends StaticFolderNode {

	private NewsSearchResultNode newsSearchResultNode = new NewsSearchResultNode(treeBean, this);

	public NewsAdministrationNode(NavigationBean tree, FolderNode parent, boolean addSearchNode) {
		super(tree, parent, StaticNodeIdEnum.NEWS_ADMINISTRATION_NODE.value());

		if (treeBean.getUserFromSession().haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			setContextMenu(ContextMenuEnum.StaticFolderContextMenu);
		}
		setTemplateName("./administration/newsAdministration/newsList.xhtml");
		setAddObjectIcon("add-news");

		if (addSearchNode) {
			treeBean.getObjectTypeSearchResultNode().put(getObjectContentId(), newsSearchResultNode);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		childs.add(newsSearchResultNode);

		// load newsList
		NewsDTO d = new NewsDTO();
		d.setUserLogged(treeBean.getUserFromSession());
		d.setWorkAreaId(null);
		List<NewsModel> newsList = treeBean.getNewsService().loadListNews(d).getNewsList();

		setDto(d);
		for (NewsModel newsModel : newsList) {

			String newsLabel = newsModel.getTitle();
			NewsNode n = new NewsNode(treeBean, this, newsLabel, newsModel.getNewsId(), Boolean.FALSE);
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		return childs;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getNewObjectNodeInstance()
	 */
	@Override
	public NewsNode getNewObjectNodeInstance() {
		return new NewsNode(treeBean, this, treeBean.getMessage("news.newNews.tree.msge"), null, Boolean.FALSE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.NEWS_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		return treeBean.getMessage("action.news.list");
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
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.FolderNode#getCreateMessage()
	 */
	@Override
	protected String getCreateMessage() {
		return treeBean.getMessage("action.news.create");
	}
}
