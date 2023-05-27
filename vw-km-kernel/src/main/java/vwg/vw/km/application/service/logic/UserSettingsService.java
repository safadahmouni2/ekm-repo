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

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.implementation.WorkAreaManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.UserSettingsDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.21 $ $Date: 2015/12/11 08:00:44 $
 */
public class UserSettingsService extends BaseService<UserSettingsDTO> {

	private HourlyRateCatalogManager hourlyRateCatalogManager;

	private UserManager userManager;

	private BrandManager brandManager;

	private WorkAreaManager workAreaManager;

	private final Log log = LogManager.get().getLog(UserSettingsService.class);

	// private

	public void setHourlyRateCatalogManager(HourlyRateCatalogManager hourlyRateCatalogManager) {
		this.hourlyRateCatalogManager = hourlyRateCatalogManager;
	}

	public BrandManager getBrandManager() {
		return brandManager;
	}

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

	public void setWorkAreaManager(WorkAreaManager workAreaManager) {
		this.workAreaManager = workAreaManager;
	}

	/**
	 * load all hourlyRateCatalog in DTO for selection purpose
	 * 
	 * @param userSettingsDTO
	 * @return
	 */
	private UserSettingsDTO loadHourlyRateCatalogs(UserSettingsDTO userSettingsDTO) {

		// Add the default catalog (1€)
		HourlyRateCatalogModel defaulCatalog = hourlyRateCatalogManager
				.getObject(HourlyRateCatalogManager.DEFAULT_CATALOG_ID);
		List<HourlyRateCatalogModel> hourlyRateCatalogs = new ArrayList<HourlyRateCatalogModel>();
		hourlyRateCatalogs.add(defaulCatalog);

		List<Long> userWorkAreaIds = new ArrayList<Long>();
		for (WorkAreaModel workArea : userSettingsDTO.getUserLogged().getWorkAreas()) {
			userWorkAreaIds.add(workArea.getWorkAreaId());
		}

		hourlyRateCatalogs
				.addAll(hourlyRateCatalogManager.getHourlyRateCatalogsByFolderAndWorkArea(null, userWorkAreaIds));
		userSettingsDTO.setHourlyRateCatalogs(hourlyRateCatalogs);

		return userSettingsDTO;
	}

	/**
	 * load all hourlyRateCatalog in DTO for selection purpose
	 * 
	 * @param userSettingsDTO
	 * @return
	 */
	public UserSettingsDTO loadDTO(UserSettingsDTO dto) {
		loadHourlyRateCatalogs(dto);
		List<BrandModel> userBrands = new ArrayList<BrandModel>();
		for (WorkAreaModel workArea : dto.getUserLogged().getWorkAreas()) {
			userBrands.add(workArea.getBrand());
		}
		// load value o image_root_path from user workarea if it is not yet defined
		if ((dto.getImagesRootPath() == null || "".equals(dto.getImagesRootPath()))
				&& (!"".equals(dto.getDefaultBrand()))) {
			WorkAreaModel userWorkArea = workAreaManager.getWorkAreaByBrand(dto.getDefaultBrand());
			if (userWorkArea != null) {
				dto.setImagesRootPath(userWorkArea.getImagesRootPath());
			}
		}
		dto.setBrands(userBrands);
		dto.setAllBrands(brandManager.getObjects());
		loadDefaultLibrary(dto);
		return dto;
	}

	public UserSettingsDTO loadDefaultLibrary(UserSettingsDTO dto) {
		String brandId = dto.getDefaultBrand();
		if (brandId != null && !brandId.equals("")) {
			BrandModel brand = brandManager.getObject(brandId);
			List<LibraryModel> libraries = new ArrayList<LibraryModel>(brand.getLibraries());
			if (!libraries.isEmpty()) {
				dto.setDefaultLibrary(libraries.get(0).getLibraryId());
			}
		} else {
			dto.setDefaultLibrary(null);
		}
		return dto;
	}

	/**
	 * save user object
	 * 
	 * @param userSettingsDTO
	 * @return
	 */
	public UserSettingsDTO save(UserSettingsDTO userSettingsDTO) {
		UserModel user = userSettingsDTO.getUserLogged();
		userManager.saveObject(user);
		return userSettingsDTO;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public UserSettingsDTO loadDefaultImgPath(UserSettingsDTO dto) {
		UserModel user = dto.getUserLogged();
		log.info("DefaultBrand: " + dto.getDefaultBrand());
		if ((user.getImagesRootPath() == null || "".equals(user.getImagesRootPath()))
				&& (!"".equals(dto.getDefaultBrand()))) {
			WorkAreaModel userWorkArea = workAreaManager.getWorkAreaByBrand(dto.getDefaultBrand());
			if (userWorkArea != null) {
				dto.setImagesRootPath(userWorkArea.getImagesRootPath());
				log.info("ImagesRootPath: " + dto.getImagesRootPath());
			}
		}
		return dto;
	}
}
