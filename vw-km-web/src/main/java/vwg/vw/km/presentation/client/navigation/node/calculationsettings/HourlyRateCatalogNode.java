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
package vwg.vw.km.presentation.client.navigation.node.calculationsettings;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.HourlyRateCatalogDTO;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.ContextMenuEnum;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
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
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.36 $ $Date: 2014/01/20 13:20:49 $
 */
public class HourlyRateCatalogNode extends ObjectNode {

	public static final String CATALOG_ATTACHED_TO_USER_MESSAGE_ID = "hourlyRateCatalog.attachedToUser.message";

	public HourlyRateCatalogNode(NavigationBean tree, FolderNode parent, String text, Long hourlyRateCatalogId) {
		super(tree, parent, Boolean.FALSE);
		setMenuDisplayText(text);
		setMenuContentTitle(text);
		setFromBundle(Boolean.FALSE);
		setTemplateName("./hourlyRateCatalog/hourlyRateCatalog.xhtml");
		setPopupPageName("./hourlyRateCatalog/popup.xhtml");
		setNodeId(hourlyRateCatalogId);
		if (!treeBean.isVisitorUser()) {
			setContextMenu(ContextMenuEnum.CatalogObjectContextMenu);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public Long getObjectTypeId() {
		return EnumObjectTypeManager.HOURLY_RATE_CATALOG_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificNodeClicked()
	 */
	@Override
	public void specificNodeClicked() {
		refresh();
		// fill DTO ()
		HourlyRateCatalogDTO hourlyRateCatalogDTO = new HourlyRateCatalogDTO();
		hourlyRateCatalogDTO.setUserLogged(treeBean.getUserFromSession());
		setDto(treeBean.getHourlyRateCatalogService().loadDTOForDetail(hourlyRateCatalogDTO, getNodeId(),
				this.getParent().getNodeId()));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		specificNodeClicked();
		HourlyRateCatalogDTO dto = (HourlyRateCatalogDTO) getDto();
		treeBean.getHourlyRateCatalogService().loadIsCatalogAttachedToUser(dto);
		if (dto.getIsCatalogAttachedToUser()) {
			return treeBean.getMessage(CATALOG_ATTACHED_TO_USER_MESSAGE_ID);
		}
		treeBean.getHourlyRateCatalogService().delete(dto);
		return "";
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#specificCopyClicked(vwg.vw.km.presentation.client.navigation.node.ObjectNode)
	 */
	@Override
	public Node specificCopyClicked(Node node) {
		HourlyRateCatalogDTO hourlyRateCatalogDTO = (HourlyRateCatalogDTO) getDto();
		for (HourlyRateModel hModel : hourlyRateCatalogDTO.getHourlyRates()) {
			hModel.setLoaded(Boolean.FALSE);
		}
		String newDesignation = "Kopie_" + hourlyRateCatalogDTO.getHourlyRateCatalogModel().getDesignation();
		hourlyRateCatalogDTO.getHourlyRateCatalogModel().setDesignation(newDesignation);
		hourlyRateCatalogDTO.getHourlyRateCatalogModel().setLoaded(Boolean.FALSE);
		node.setMenuDisplayText(newDesignation);
		node.setDto(hourlyRateCatalogDTO);
		return node;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteMessage()
	 */
	@Override
	public String getDeleteMessage() {
		return treeBean.getMessage("hourlyRateCatalog.deleteConfirm");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.ObjectNode#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return treeBean.getMessage("action.hourlyRateCatalog.copy");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return treeBean.getMessage("action.hourlyRateCatalog.edit");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return treeBean.getMessage("action.hourlyRateCatalog.cut");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return treeBean.getMessage("action.hourlyRateCatalog.delete");
	}
}
