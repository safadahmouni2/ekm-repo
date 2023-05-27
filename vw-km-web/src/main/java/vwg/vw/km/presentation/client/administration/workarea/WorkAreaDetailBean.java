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

package vwg.vw.km.presentation.client.administration.workarea;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import vwg.vw.km.application.service.dto.WorkAreaDTO;
import vwg.vw.km.application.service.logic.WorkAreaService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;
import vwg.vw.km.presentation.client.navigation.node.Node;

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
 * @author zouhairs changed by $Author: saidi $
 * @version $Revision: 1.41 $ $Date: 2013/12/20 10:42:58 $
 */
public class WorkAreaDetailBean extends BaseDetailBean<WorkAreaDTO, WorkAreaService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6300130662179507334L;

	private final Log log = LogManager.get().getLog(WorkAreaDetailBean.class);

	public static final String WORK_AREA_EXIST_MESSAGE_ID = "workArea.exist.sameDesignation.message";

	public static final String WORK_AREA_ATTACHED_MESSAGE_ID = "workArea.attachedToUser.message";

	public static final String WORK_AREA_ATTACHED_TO_CATALOG_MESSAGE_ID = "workArea.attachedToCatalog.message";

	private SelectItem[] brandItems;

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#specificSave(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificSave(ActionEvent event) {
		log.info("Start saving : " + dto.getCurrentWorkArea());
		service.getExistWorkAreaByDesignation(dto);
		WorkAreaModel area = dto.getCurrentWorkArea();
		if (area.getWorkAreaId() == null && dto.getExistWorkAreaByDesignation()) {
			return getMessage(WORK_AREA_EXIST_MESSAGE_ID);
		}
		
		if(area.getDesignation() == null || area.getBrand() == null){
			return getMessage("common.popup.UIInput.REQUIRED");
		}
		service.save(dto);
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#afterSave(javax.faces.event.ActionEvent)
	 */
	@Override
	protected void afterSave(ActionEvent event) {
		Node actualNode = navigationTree.getSelectedNodeObject();
		actualNode.setMenuDisplayText(getDisplayText());
		actualNode.setNodeId(getNodeId());
		actualNode.setOwnerImg(dto.getCurrentWorkArea().getBrandIcon());
		actualNode.nodeClicked(event);
		// ZS: PTS_Problem-35155: Make possible to navigate in the tree when the user choose to save a previous change
		Node gotoNode = navigationTree.getGotoNode();
		if (gotoNode != null) {
			gotoNode.nodeClicked(event);
		}
		navigationTree.setGotoNode(null);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getSaveMessage()
	 */
	@Override
	public String getSaveMessage() {
		return getMessage("action.workArea.save");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.workArea.reload");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.workArea.search");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return dto.getCurrentWorkArea().getDesignation();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getCurrentWorkArea().getWorkAreaId();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		setBrandItems(fillBrandItemsFromDTO());
	}

	/**
	 * Fill the class items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillBrandItemsFromDTO() {
		List<BrandModel> brands = dto.getBrands();
		SelectItem[] items = null;
		if (brands != null) {
			items = new SelectItem[1 + brands.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (brands != null) {
			BrandModel brand;
			for (int i = 0; i < brands.size(); i++) {
				brand = brands.get(i);
				items[i + 1] = new SelectItem(brand.getBrandId(), brand.getBrandId());
			}
		}
		return items;
	}

	public SelectItem[] getBrandItems() {
		return brandItems;
	}

	public void setBrandItems(SelectItem[] brandItems) {
		this.brandItems = brandItems;
	}

	/**
	 * load Brand
	 * 
	 * @param event
	 */
	public void loadBrand(ValueChangeEvent event) {
		checkChanged(event);

		dto.setBrandId((String) event.getNewValue());
		service.loadBrand(dto);
		navigationTree.getSelectedNodeObject().setOwnerImg(dto.getCurrentWorkArea().getBrandIcon());
	}
}
