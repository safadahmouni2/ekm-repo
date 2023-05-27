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
package vwg.vw.km.presentation.client.navigation.node.costcomponent;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.Node;

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
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.21 $ $Date: 2013/11/28 15:45:51 $
 */
public class CostComponentVersionNode extends BranchNode {

	public CostComponentVersionNode(NavigationBean tree, BranchNode parent, String text, Long componentVersionId,
			boolean schowLastVersion, boolean isSearchNode, String owner, List<String> users) {
		super(tree, parent, componentVersionId);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setLeafIcon(makeIcon("es.gif"));
		setBranchContractedIcon(makeIcon("es.gif"));
		setBranchExpandedIcon(makeIcon("es.gif"));
		setOwnerImg(owner);
		setUsers(users);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {

		List<Node> childs = new ArrayList<Node>();
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setUserLogged(treeBean.getUserFromSession());

		treeBean.getCostComponentService().loadDTOForDetail(costComponentDTO, getNodeId(),
				this.getParent().getParent().getNodeId(), "VERSION", Boolean.TRUE);
		List<ComponentStandModel> componentStands = costComponentDTO.getVersionStandList(getNodeId());
		setDto(costComponentDTO);
		String standLabel = treeBean.getMessage("costcomponent.componentstand");
		boolean addOwnerImg = Boolean.TRUE;
		for (ComponentStandModel model : componentStands) {
			String owner = null;
			if (addOwnerImg) {
				owner = getOwnerImg();
				addOwnerImg = Boolean.FALSE;
			}
			Node n = new CostComponentVersionStandNode(treeBean, this,
					standLabel + " " + model.getNumber() + " "
							+ model.getStandDate().getString(BaseDateTime.DD_MM_YYYY, BaseDateTime.PNT),
					model.getId(), Boolean.TRUE, getSearchNode(), owner, model.getUsersImg());
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}

		ComponentModel component = costComponentDTO.getComponentVersion().getComponent();
		component.setUsers(treeBean.getCostComponentService().loadComponentVersionUsers(component));
		getParent().setUsers(component.getUsersImg());
		return childs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getObjectContentId()
	 */
	@Override
	public Long getObjectContentId() {
		return EnumObjectTypeManager.COMPONENT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.COMPONENT_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("costcomponent.deleteConfirm");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			treeBean.setGotoNode(this);
			return;
		}
		expandClicked(event);
		if (!this.expanded) {
			this.setExpanded(Boolean.TRUE);
		} else {
			this.setExpanded(Boolean.FALSE);
		}
		treeBean.setSelectedNodeObject(this);
		if (objectChildsCount > 0) {
			childs.get(0).nodeClicked(event);
			this.setExpanded(Boolean.TRUE);
			return;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.costComponent.delete");
	}

	@Override
	protected String getCopyMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getCutMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getPasteMessage()
	 */
	@Override
	protected String getPasteMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getCreateMessage()
	 */
	@Override
	protected String getCreateMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getNewFolderName()
	 */
	@Override
	public String getNewFolderName() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getListClickedMessage()
	 */
	@Override
	protected String getListClickedMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.BranchNode#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.costComponent.edit");
	}
}
