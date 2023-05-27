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
package vwg.vw.km.presentation.client.costcomponent;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
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
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.14 $ $Date: 2016/10/20 14:49:31 $
 */
public class CostElementAttachNode extends ElementNode {

	private ElementModel elementModel;

	public CostElementAttachNode(NavigationElementBean tree, ElementFolderNode parent, String text, Long costElementId,
			Boolean selected, ElementModel eModel) {
		super(tree, parent);
		setLeaf(Boolean.TRUE);
		setMenuDisplayText(text);
		setContextMenu(ContextMenuEnum.ElementToComponentAssignContextMenu);
		setDisplayContextMenu(true);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setLeafIcon(Node.makeIcon("e.gif"));
		setNodeId(costElementId);
		setSelected(selected);
		setElementModel(eModel);
		setOwnerImg(eModel.getOwnerImg());
	}

	/**
	 * Return the enumeration value of the ELEMENT_ID
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.ELEMENT_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.costcomponent.ElementNode#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (isSelected()) {
			treeBean.getElementsMap().remove(getElementModel().getElementId());
			setSelected(Boolean.FALSE);
		} else {
			treeBean.getElementsMap().put(getElementModel().getElementId(), this);
			setSelected(Boolean.TRUE);
		}
	}

	/**
	 * Display event when right click on an element in tree to remove the selection when assign by menuContext
	 */
	public void nodeRightClicked(Object event) {
		if (isSelected()) {
			setSelected(Boolean.FALSE);
		}
		treeBean.getElementsMap().put(getElementModel().getElementId(), this);
	}

	/**
	 * Getter of elementModel object
	 * 
	 * @return elementModel
	 */
	public ElementModel getElementModel() {
		return elementModel;
	}

	/**
	 * Setter of elementModel object
	 * 
	 * @param elementModel
	 */
	public void setElementModel(ElementModel elementModel) {
		this.elementModel = elementModel;
	}

	/**
	 * Check if the cost element is assigned to a cost component
	 */
	@Override
	public boolean isAssigned() {
		if (treeBean.getAssignedElementIds().contains(getNodeId())) {
			return true;
		} else {
			return false;
		}
	}
}
