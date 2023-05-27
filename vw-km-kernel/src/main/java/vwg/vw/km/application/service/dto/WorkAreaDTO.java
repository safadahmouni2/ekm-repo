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

import java.util.List;

import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.integration.persistence.model.BrandModel;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.16 $ $Date: 2013/09/30 14:14:53 $
 */
public class WorkAreaDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6510004914088025652L;

	private WorkAreaModel currentWorkArea;

	private List<WorkAreaModel> workAreas;

	private Long workAreaId;

	private Boolean existWorkAreaByDesignation;

	private Boolean isWorkAreaAttached;

	private Boolean isWorkAreaAttachedToCatalog;

	private List<BrandModel> brands;

	private String brandId;

	public WorkAreaDTO() {
		currentWorkArea = new WorkAreaModel();
	}

	public WorkAreaModel getCurrentWorkArea() {
		return currentWorkArea;
	}

	public void setCurrentWorkArea(WorkAreaModel currentWorkArea) {
		this.currentWorkArea = currentWorkArea;
	}

	public List<WorkAreaModel> getWorkAreas() {
		return workAreas;
	}

	public void setWorkAreas(List<WorkAreaModel> workAreas) {
		this.workAreas = workAreas;
	}

	public Long getWorkAreaId() {
		return workAreaId;
	}

	public void setWorkAreaId(Long workAreaId) {
		this.workAreaId = workAreaId;
	}

	public Boolean getExistWorkAreaByDesignation() {
		return existWorkAreaByDesignation;
	}

	public void setExistWorkAreaByDesignation(Boolean existWorkAreaByDesignation) {
		this.existWorkAreaByDesignation = existWorkAreaByDesignation;
	}

	public Boolean getIsWorkAreaAttached() {
		return isWorkAreaAttached;
	}

	public void setIsWorkAreaAttached(Boolean isWorkAreaAttached) {
		this.isWorkAreaAttached = isWorkAreaAttached;
	}

	public Boolean getIsWorkAreaAttachedToCatalog() {
		return isWorkAreaAttachedToCatalog;
	}

	public void setIsWorkAreaAttachedToCatalog(Boolean isWorkAreaAttachedToCatalog) {
		this.isWorkAreaAttachedToCatalog = isWorkAreaAttachedToCatalog;
	}

	public boolean isNoWorkAreas() {
		if (workAreas != null && !workAreas.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * @see vwg.vw.km.application.service.dto.base.BaseDTO#copyUtilAttributesForSearch()
	 */
	public WorkAreaDTO copyUtilAttributesForSearch() {
		WorkAreaDTO workAreaDTO = new WorkAreaDTO();
		workAreaDTO.setSearchString(getSearchString());
		workAreaDTO.setSearchStringResult(getSearchString());
		workAreaDTO.setSearchMode(Boolean.TRUE);
		workAreaDTO.setNoSearchResult(isNoSearchResult());
		// if no result in no result page
		if (isNoSearchResult()) {
			workAreaDTO.setSearchFolder(getSearchFolder());
			workAreaDTO.setFolderId(getFolderId());
		}
		return workAreaDTO;
	}

	public List<BrandModel> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandModel> brands) {
		this.brands = brands;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getBrandId() {
		return brandId;
	}
}
