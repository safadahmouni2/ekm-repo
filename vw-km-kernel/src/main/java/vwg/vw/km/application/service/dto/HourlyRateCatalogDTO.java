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
package vwg.vw.km.application.service.dto;

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.integration.persistence.model.CostAttributeCategoryModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;

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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.25 $ $Date: 2020/01/16 15:25:02 $
 */
public class HourlyRateCatalogDTO extends BaseDTO {

	private static final long serialVersionUID = -9171261575076710054L;

	private HourlyRateCatalogModel hourlyRateCatalogModel;

	private List<HourlyRateCatalogModel> hourlyRateCatalogs;

	private Long hourlyRateCatalogModelId;

	private List<HourlyRateModel> hourlyRates;

	private List<WorkAreaModel> WorkAreaModelList;

	private String workAreaId;

	private Boolean existHRCatalogByDesignation;

	private Boolean isCatalogAttachedToUser;

	public Boolean getExistHRCatalogByDesignation() {
		return existHRCatalogByDesignation;
	}

	public void setExistHRCatalogByDesignation(Boolean existHRCatalogByDesignation) {
		this.existHRCatalogByDesignation = existHRCatalogByDesignation;
	}

	public Boolean getIsCatalogAttachedToUser() {
		return isCatalogAttachedToUser;
	}

	public void setIsCatalogAttachedToUser(Boolean isCatalogAttachedToUser) {
		this.isCatalogAttachedToUser = isCatalogAttachedToUser;
	}

	public HourlyRateCatalogModel getHourlyRateCatalogModel() {
		return hourlyRateCatalogModel;
	}

	public void setHourlyRateCatalogModel(HourlyRateCatalogModel hourlyRateCatalogModel) {
		this.hourlyRateCatalogModel = hourlyRateCatalogModel;
	}

	public List<HourlyRateCatalogModel> getHourlyRateCatalogs() {
		return hourlyRateCatalogs;
	}

	public void setHourlyRateCatalogs(List<HourlyRateCatalogModel> hourlyRateCatalogs) {
		this.hourlyRateCatalogs = hourlyRateCatalogs;
	}

	public Long getHourlyRateCatalogModelId() {
		return hourlyRateCatalogModelId;
	}

	public void setHourlyRateCatalogModelId(Long hourlyRateCatalogModelId) {
		this.hourlyRateCatalogModelId = hourlyRateCatalogModelId;
	}

	public List<HourlyRateModel> getHourlyRates() {
		return hourlyRates;
	}

	public void setHourlyRates(List<HourlyRateModel> hourlyRates) {
		this.hourlyRates = hourlyRates;
	}

	public List<WorkAreaModel> getWorkAreaModelList() {
		return WorkAreaModelList;
	}

	public void setWorkAreaModelList(List<WorkAreaModel> workAreaModelList) {
		WorkAreaModelList = workAreaModelList;
	}

	public String getWorkAreaIdAsString() {
		return workAreaId;
	}

	public void setWorkAreaIdAsString(String workAreaId) {
		this.workAreaId = workAreaId;
	}

	public void setWorkAreaId(Long workAreaId) {
		if (workAreaId != null) {
			this.workAreaId = workAreaId.toString();
		}
	}

	public Long getWorkAreaId() {
		Long workAreaIdAsLong = null;
		if (this.workAreaId != null && !"".equals(this.workAreaId.trim())) {
			try {
				workAreaIdAsLong = Long.parseLong(this.workAreaId);
			} catch (NumberFormatException e) {
			}
		}
		return workAreaIdAsLong;
	}

	/**
	 * getGeneralMecanicHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getGeneralMecanicHourlyRates() {
		return getSpecificList(1L);
	}

	/**
	 * getConstructionHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getConstructionHourlyRates() {
		return getSpecificList(2L);
	}

	/**
	 * getRobotOfflineProgrammingHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getRobotOfflineProgrammingHourlyRates() {
		return getSpecificList(3L);
	}

	/**
	 * getProductionHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getProductionHourlyRates() {
		return getSpecificList(4L);
	}

	/**
	 * getMontageMecanicHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getMontageMecanicHourlyRates() {
		List<HourlyRateModel> returnList = getSpecificList(6L);
		returnList.addAll(getSpecificList(7L));
		return returnList;
	}

	/**
	 * getMecanicPurchasedPartsHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getMecanicPurchasedPartsHourlyRates() {
		return getSpecificList(5L);
	}

	/**
	 * getGeneralElectricHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getGeneralElectricHourlyRates() {
		return getSpecificList(8L);
	}

	/**
	 * getConstructionMchanicHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getConstructionMchanicHourlyRates() {
		List<HourlyRateModel> returnList = getSpecificList(9L);
		returnList.addAll(getSpecificList(10L));
		return returnList;
	}

	/**
	 * getElectricPurchasedPartsHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getElectricPurchasedPartsHourlyRates() {
		return getSpecificList(11L);
	}

	/**
	 * getElectricInstallationHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getElectricInstallationHourlyRates() {
		List<HourlyRateModel> returnList = getSpecificList(12L);
		returnList.addAll(getSpecificList(13L));
		return returnList;
	}

	/**
	 * getCommissioningHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getCommissioningHourlyRates() {
		return getSpecificList(14L);
	}

	/**
	 * getRobotOnlineProgrammingHourlyRates
	 * 
	 * @return
	 */
	public List<HourlyRateModel> getRobotOnlineProgrammingHourlyRates() {
		return getSpecificList(15L);
	}

	public List<HourlyRateModel> getElectricPurchaseFactorHourlyRates() {
		return getSpecificList(15L);
	}

	public List<HourlyRateModel> getMechanicPurchaseFactorHourlyRates() {
		return getSpecificList(5L);
	}

	/**
	 * return a list of hourly rate for given cost attribute categoy id
	 * 
	 * @param id
	 * @return
	 */
	public List<HourlyRateModel> getSpecificList(Long id) {
		List<HourlyRateModel> list = new ArrayList<HourlyRateModel>();
		for (HourlyRateModel hRateModel : hourlyRates) {
			CostAttributeCategoryModel costAttributeCategoryModel = hRateModel.getCostAttribute()
					.getCostAttributeCategory();
			// to hide the cost attribute if costattributeTransferRef is not null
			if (hRateModel.getCostAttribute().getCostAttributeTransferRefId() == null
					&& costAttributeCategoryModel.getCostAttributeCategoryId().compareTo(id) == 0) {
				list.add(hRateModel);
			}
		}
		return list;
	}

	public boolean isNoHourlyRateCatalogs() {
		if (hourlyRateCatalogs != null && !hourlyRateCatalogs.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Check if a user can perform an save/delete 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role
	 * BETRACHTER
	 * 
	 * @return true/false
	 */
	public boolean getUserHasWriteAccess() {
		UserModel loggedUser = getUserLogged();
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
}