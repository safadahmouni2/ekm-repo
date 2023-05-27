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

package vwg.vw.km.presentation.client.calculationsettings;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import vwg.vw.km.application.service.dto.HourlyRateCatalogDTO;
import vwg.vw.km.application.service.logic.HourlyRateCatalogService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;

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
 * @author monam changed by $Author: abidh $
 * @version $Revision: 1.39 $ $Date: 2020/01/07 17:12:52 $
 */
public class HourlyRateCatalogDetailBean extends BaseDetailBean<HourlyRateCatalogDTO, HourlyRateCatalogService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5402725353703765914L;

	private final Log log = LogManager.get().getLog(HourlyRateCatalogDetailBean.class);

	public static final String HOURLY_RATE_EXIST_MESSAGE_ID = "hourlyratecatalog.exist.sameDesignation.message";

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#specificSave(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificSave(ActionEvent event) {
		if (!"ESCAPE_CONFIRMATION".equals(getRequestParameterMap().get("save_mode"))) {
			if (dto.getWorkAreaId() == null) {
				openConfirmWithoutWorkAtraPopup();
				return "CONFIRMATION";
			}
		}
		closeConfirmWithoutWorkAtraPopup(event);
		service.loadExistWorkAreaByDesignation(dto);
		if (dto.getExistHRCatalogByDesignation()) {
			return getMessage(HOURLY_RATE_EXIST_MESSAGE_ID);
		}
		HourlyRateCatalogModel hourlyRateCatalogModel = dto.getHourlyRateCatalogModel();

		
		if(hourlyRateCatalogModel.getDesignation() == null ){
			return getMessage("common.popup.UIInput.REQUIRED");
		}
		// US_10263942... To hide a costAttribute from katalog screen and save it value from an other
		List<HourlyRateModel> hourlyRateModels = dto.getHourlyRates();
		for (HourlyRateModel hourlyRateModel : hourlyRateModels) {
			if (hourlyRateModel.getCostAttribute().getCostAttributeTransferRefId() != null) {
				for (HourlyRateModel hRateModel : hourlyRateModels) {
					if ((hRateModel.getCostAttribute().getCostAttributeId()
							.equals(hourlyRateModel.getCostAttribute().getCostAttributeTransferRefId()))) {
						hourlyRateModel.setValue(hRateModel.getValue());
					}
				}
			}
		}

		log.info("Try to save " + hourlyRateCatalogModel);
		if (!hourlyRateCatalogModel.isLoaded()) {
			hourlyRateCatalogModel.setCreatorRefId(getUserFromSession().getUserId());
			hourlyRateCatalogModel.setCreationDate(BaseDateTime.getCurrentDateTime());
		}
		service.save(dto);

		return "";
	}

	// PTS_Problem-35624: overwrite the save method to Make possible to navigate in the tree when the user
	// choose to save or not a previous change for the hourlyRateCatalog bean
	@Override
	public void save(ActionEvent event) {
		String saveResult = specificSave(event);
		dto.setShowSaveChangesPopup(Boolean.FALSE);
		if ("".equals(saveResult) || "CONFIRMATION".equals(saveResult)) {
			dto.getPageChanges().clear();
			if (!("CONFIRMATION".equals(saveResult))) {
				afterSave(event);
				navigationTree.setDownActionDefintion(getSaveMessage());
			}
		} else {
			navigationTree.setGotoNode(null);
			navigationTree.openErrorPopup(saveResult);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getSaveMessage()
	 */
	@Override
	public String getSaveMessage() {
		return getMessage("action.hourlyRateCatalog.save");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return dto.getHourlyRateCatalogModel().getDesignation();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getHourlyRateCatalogModel().getHourlyRateCatalogId();
	}

	/**
	 * get work areas drop down
	 * 
	 * @return SelectItem[]
	 */
	public SelectItem[] getWorkAreaItems() {
		List<WorkAreaModel> list = dto.getWorkAreaModelList();
		SelectItem[] items = null;
		if (list != null) {
			items = new SelectItem[1 + list.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				WorkAreaModel wModel = list.get(i);
				items[i + 1] = new SelectItem(wModel.getWorkAreaId(), wModel.getDesignation());
			}
		}
		return items;
	}

	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.hourlyRateCatalog.reload");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.hourlyRateCatalog.search");
	}

	private boolean showWithoutWorkAtraConfirm = Boolean.FALSE;

	public boolean isShowWithoutWorkAtraConfirm() {
		return showWithoutWorkAtraConfirm;
	}

	public void setShowWithoutWorkAtraConfirm(boolean showWithoutWorkAtraConfirm) {
		this.showWithoutWorkAtraConfirm = showWithoutWorkAtraConfirm;
	}

	public void closeConfirmWithoutWorkAtraPopup(ActionEvent event) {
		setShowWithoutWorkAtraConfirm(Boolean.FALSE);
	}

	public void openConfirmWithoutWorkAtraPopup() {
		setShowWithoutWorkAtraConfirm(Boolean.TRUE);
	}

}
