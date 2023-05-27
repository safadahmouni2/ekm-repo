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

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.implementation.WorkAreaManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.WorkAreaDTO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.22 $ $Date: 2013/12/06 17:14:28 $
 */
public class WorkAreaService extends BaseService<WorkAreaDTO> {

	private WorkAreaManager workAreaManager;

	private UserManager userManager;

	private HourlyRateCatalogManager hourlyRateCatalogManager;

	private BrandManager brandManager;

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setWorkAreaManager(WorkAreaManager workAreaManager) {
		this.workAreaManager = workAreaManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setHourlyRateCatalogManager(HourlyRateCatalogManager hourlyRateCatalogManager) {
		this.hourlyRateCatalogManager = hourlyRateCatalogManager;
	}

	/**
	 * save current work area in DTO
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO save(WorkAreaDTO workAreaDTO) {
		WorkAreaModel workArea = workAreaDTO.getCurrentWorkArea();

		if (workAreaDTO.getBrandId() != null) {
			workArea.setBrand(brandManager.getObject(workAreaDTO.getBrandId()));
		}
		workAreaManager.saveObject(workArea);
		return workAreaDTO;
	}

	/**
	 * delete current workarea in DTO
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO delete(WorkAreaDTO workAreaDTO) {
		workAreaManager.removeObject(workAreaDTO.getWorkAreaId());
		return workAreaDTO;
	}

	/**
	 * load list of all work areas ordered alphabetically to show them in tree
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO loadWorkAreas(WorkAreaDTO workAreaDTO) {
		workAreaDTO.setWorkAreas(workAreaManager.getAlphaOrderedWorkAreas());
		return workAreaDTO;
	}

	public WorkAreaDTO loadBrand(WorkAreaDTO workAreaDTO) {
		if (workAreaDTO.getBrandId() != null) {
			BrandModel brand = brandManager.getObject(workAreaDTO.getBrandId());
			workAreaDTO.getCurrentWorkArea().setBrand(brand);
		}
		return workAreaDTO;
	}

	/**
	 * check if new created work area in DTO is unique by its designation
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO getExistWorkAreaByDesignation(WorkAreaDTO workAreaDTO) {
		if ((workAreaManager
				.getWorkAreaModelByDesignation(workAreaDTO.getCurrentWorkArea().getDesignation())) != null) {
			workAreaDTO.setExistWorkAreaByDesignation(Boolean.TRUE);
		} else {
			workAreaDTO.setExistWorkAreaByDesignation(Boolean.FALSE);
		}
		return workAreaDTO;
	}

	/**
	 * check if current work area in DTO is attached to a user and set result in DTO
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO getIsWorkAreaAttached(WorkAreaDTO workAreaDTO) {
		Boolean isWorkAreaAttached = Boolean.FALSE;
		for (UserModel userModel : userManager.getObjects()) {
			if (userModel.getWorkAreas().contains(workAreaDTO.getCurrentWorkArea())) {
				isWorkAreaAttached = Boolean.TRUE;
				break;
			}
		}
		workAreaDTO.setIsWorkAreaAttached(isWorkAreaAttached);
		return workAreaDTO;
	}

	/**
	 * check if current work area in DTO is attached to a catalog and set result in DTO
	 * 
	 * @param workAreaDTO
	 * @return
	 */
	public WorkAreaDTO getIsWorkAreaAttachedToCatalog(WorkAreaDTO workAreaDTO) {
		Boolean isWorkAreaAttachedToCatalog = Boolean.FALSE;
		for (HourlyRateCatalogModel hRateCatalogModel : hourlyRateCatalogManager.getObjects()) {
			if (hRateCatalogModel.getWorkArea() != null
					&& hRateCatalogModel.getWorkArea().equals(workAreaDTO.getCurrentWorkArea())) {
				isWorkAreaAttachedToCatalog = Boolean.TRUE;
				break;
			}
		}
		workAreaDTO.setIsWorkAreaAttachedToCatalog(isWorkAreaAttachedToCatalog);
		return workAreaDTO;
	}

	/**
	 * load work area object from DB to DTO for administration purpose
	 * 
	 * @param workAreaDTO
	 * @param workAreaId
	 * @return
	 */
	public WorkAreaDTO loadDTO(WorkAreaDTO workAreaDTO, Long workAreaId) {
		workAreaDTO.setWorkAreaId(workAreaId);
		if (workAreaId != null) {
			workAreaDTO.setCurrentWorkArea(workAreaManager.getObject(workAreaDTO.getWorkAreaId()));
			// set the Marke for the work area
			if (workAreaDTO.getCurrentWorkArea().getBrand() != null) {
				workAreaDTO.setBrandId(workAreaDTO.getCurrentWorkArea().getBrand().getBrandId());
			}
		}
		// load all Marke
		workAreaDTO.setBrands(brandManager.getObjects());
		return workAreaDTO;
	}

	/**
	 * @see vwg.vw.km.application.service.base.BaseService#loadDTOForListBySearchString(vwg.vw.km.application.service.dto.base.BaseDTO)
	 */
	public WorkAreaDTO loadDTOForListBySearchString(WorkAreaDTO workAreaDTO) {
		workAreaDTO.setWorkAreas(workAreaManager.getWorkAreasBySearchString(workAreaDTO.getSearchString()));
		workAreaDTO.setNoSearchResult(workAreaDTO.isNoWorkAreas());
		return workAreaDTO;
	}
}
