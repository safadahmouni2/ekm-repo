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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.implementation.CostAttributeManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.HourlyRateManager;
import vwg.vw.km.application.implementation.UserManager;
import vwg.vw.km.application.implementation.WorkAreaManager;
import vwg.vw.km.application.implementation.impl.UserManagerImpl;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.HourlyRateCatalogDTO;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
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
 * @version $Revision: 1.37 $ $Date: 2020/01/27 16:47:20 $
 */
public class HourlyRateCatalogService extends BaseService<HourlyRateCatalogDTO> {

	private HourlyRateCatalogManager hourlyRateCatalogManager;

	private FolderManager folderManager;

	private HourlyRateManager hourlyRateManager;

	private CostAttributeManager costAttributeManager;

	private WorkAreaManager workAreaManager;

	private UserManager userManager;

	public void setHourlyRateCatalogManager(HourlyRateCatalogManager hourlyRateCatalogManager) {
		this.hourlyRateCatalogManager = hourlyRateCatalogManager;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setHourlyRateManager(HourlyRateManager hourlyRateManager) {
		this.hourlyRateManager = hourlyRateManager;
	}

	public void setCostAttributeManager(CostAttributeManager costAttributeManager) {
		this.costAttributeManager = costAttributeManager;
	}

	public void setWorkAreaManager(WorkAreaManager workAreaManager) {
		this.workAreaManager = workAreaManager;
	}

	/**
	 * save current HourlyRateCatalog and all related objects
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	public HourlyRateCatalogDTO save(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		HourlyRateCatalogModel hourlyRateCatalogModel = hourlyRateCatalogDTO.getHourlyRateCatalogModel();

		boolean isLoadedHourlyRateCatalog = hourlyRateCatalogModel.isLoaded();

		if (hourlyRateCatalogDTO.getWorkAreaId() != null) {
			WorkAreaModel workArea = workAreaManager.getObject(hourlyRateCatalogDTO.getWorkAreaId());
			hourlyRateCatalogModel.setWorkArea(workArea);
		} else {
			hourlyRateCatalogModel.setWorkArea(null);
		}
		hourlyRateCatalogManager.saveObject(hourlyRateCatalogModel);
		for (HourlyRateModel hModelElectric : hourlyRateCatalogDTO.getHourlyRates()) {
			if (hModelElectric.getHourlyRateCatalog() == null) {
				hModelElectric.setHourlyRateCatalog(hourlyRateCatalogModel);
			}
			if (hModelElectric.getValue() == null || hModelElectric.getValue().isNull()) {
				hModelElectric.setValue(new BaseBigDecimal("0"));
			}
			if (hModelElectric.getFactor() == null || hModelElectric.getFactor().isNull()
					|| hModelElectric.getFactor().equals(new BaseBigDecimal(new BigDecimal(0)))) {
				hModelElectric.setFactor(new BaseBigDecimal("100"));
			}
			hourlyRateManager.saveObject(hModelElectric);
		}
		hourlyRateCatalogManager.saveObject(hourlyRateCatalogModel);
		// update default catalog to 1 if work area not a user work area
		if (isLoadedHourlyRateCatalog) {
			List<UserModel> users = userManager.getUsersByCatalog(hourlyRateCatalogModel.getHourlyRateCatalogId());
			if (users != null && !users.isEmpty()) {
				for (UserModel user : users) {
					if (hourlyRateCatalogModel.getWorkArea() == null
							|| !user.getWorkAreas().contains(hourlyRateCatalogModel.getWorkArea())) {
						user.setDefaultStdSatzKatRefId(HourlyRateCatalogManager.DEFAULT_CATALOG_ID);
						userManager.saveObject(user);
						UserManagerImpl.get().getUserModifiedDefaultCatalog().put(user.getUserId(),
								HourlyRateCatalogManager.DEFAULT_CATALOG_ID);
					}
				}
			}
		}
		return hourlyRateCatalogDTO;
	}

	/**
	 * delete current HourlyRateCatalog and its dependent objects
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	public HourlyRateCatalogDTO delete(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		Long versionId = hourlyRateCatalogDTO.getHourlyRateCatalogModelId();
		for (HourlyRateModel hModel : hourlyRateManager.getHourlyRateListByCatalog(versionId)) {
			hourlyRateManager.removeObject(hModel.getHourlyRateId());
		}
		hourlyRateCatalogManager.removeObject(versionId);
		return hourlyRateCatalogDTO;
	}

	private HourlyRateCatalogDTO loadHourlyRateCatalog(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		hourlyRateCatalogDTO.setHourlyRateCatalogModel(
				hourlyRateCatalogManager.getObject(hourlyRateCatalogDTO.getHourlyRateCatalogModelId()));
		return hourlyRateCatalogDTO;
	}

	/**
	 * load an hourly rate catalog in DTO with given hourlyRateCatalogModelId
	 * 
	 * @param hourlyRateCatalogDTO
	 * @param hourlyRateCatalogModelId
	 * @param folderId
	 * @return
	 */
	public HourlyRateCatalogDTO loadDTOForDetail(HourlyRateCatalogDTO hourlyRateCatalogDTO,
			Long hourlyRateCatalogModelId, Long folderId) {

		// set current object id by given param
		hourlyRateCatalogDTO.setHourlyRateCatalogModelId(hourlyRateCatalogModelId);
		// if object in DB load it else create a new one
		if (hourlyRateCatalogModelId != null) {
			loadHourlyRateCatalog(hourlyRateCatalogDTO);
		} else {
			hourlyRateCatalogDTO.setHourlyRateCatalogModelId(null);
			HourlyRateCatalogModel hourlyRateCatalogModel = new HourlyRateCatalogModel();
			hourlyRateCatalogModel.setFolder(folderManager.getObject(folderId));
			hourlyRateCatalogDTO.setHourlyRateCatalogModel(hourlyRateCatalogModel);
		}
		loadRelatedObjects(hourlyRateCatalogDTO);
		return hourlyRateCatalogDTO;
	}

	/**
	 * move an hourly rate catalog to a folder
	 * 
	 * @param hourlyRateCatalogId
	 * @param folderId
	 * @return true if done
	 */
	public boolean saveHourlyRateCatalogToFolder(Long hourlyRateCatalogId, Long folderId) {
		HourlyRateCatalogModel e = hourlyRateCatalogManager.getObject(hourlyRateCatalogId);
		if (loadIsExistCatalog(e.getHourlyRateCatalogId(), e.isLoaded(), e.getDesignation(), folderId)) {
			return Boolean.FALSE;
		}
		e.setFolder(folderManager.getObject(folderId));
		hourlyRateCatalogManager.saveObject(e);
		return Boolean.TRUE;
	}

	/**
	 * load all hourly rate catalogs attached to all workarea associated to current logged user in DTO
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	public HourlyRateCatalogDTO loadDTOForList(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		loadHourlyRateCatalogs(hourlyRateCatalogDTO);
		return hourlyRateCatalogDTO;
	}

	/**
	 * load all hourly rate catalogs attached to all workarea associated to current logged user in DTO
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	private HourlyRateCatalogDTO loadHourlyRateCatalogs(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		List<Long> userWorkAreaIds = new ArrayList<Long>();
		for (WorkAreaModel workArea : hourlyRateCatalogDTO.getUserLogged().getWorkAreas()) {
			userWorkAreaIds.add(workArea.getWorkAreaId());
		}
		hourlyRateCatalogDTO.setHourlyRateCatalogs(
				getHourlyRateCatalogsByFolderAndWA(hourlyRateCatalogDTO.getFolderId(), userWorkAreaIds));
		return hourlyRateCatalogDTO;
	}

	/**
	 * get HourlyRateCatalogs By Folder And WorkArea
	 * 
	 * @param folderId
	 * @param workAreaIds
	 * @return
	 */
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolderAndWA(Long folderId, List<Long> userWorkAreaIds) {
		return hourlyRateCatalogManager.getHourlyRateCatalogsByFolderAndWorkArea(folderId, userWorkAreaIds);
	}

	/**
	 * get HourlyRateCatalogs By Folder
	 * 
	 * @param folderId
	 * @return
	 */
	public List<HourlyRateCatalogModel> getHourlyRateCatalogsByFolder(Long folderId) {
		return hourlyRateCatalogManager.getHourlyRateCatalogsByFolder(folderId);
	}

	/**
	 * load dependency of current hourly rate catalog model in DTO
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	private HourlyRateCatalogDTO loadRelatedObjects(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		List<HourlyRateModel> hourlyRates = new ArrayList<HourlyRateModel>();

		if (hourlyRateCatalogDTO.getHourlyRateCatalogModelId() != null) {
			hourlyRates = hourlyRateManager
					.getHourlyRateListByCatalog(hourlyRateCatalogDTO.getHourlyRateCatalogModelId());
		} else {
			List<CostAttributeModel> costAttributes = costAttributeManager
					.getCostAttributeListByPricePerUnit(Boolean.TRUE);
			for (CostAttributeModel cModel : costAttributes) {
				HourlyRateModel hourlyRateModel = new HourlyRateModel();
				hourlyRateModel.setCostAttribute(cModel);
				hourlyRateModel.setValue(new BaseBigDecimal(new BigDecimal(0)));
				hourlyRateModel.setFactor(new BaseBigDecimal(new BigDecimal(100)));
				hourlyRates.add(hourlyRateModel);
			}
		}
		hourlyRateCatalogDTO.setHourlyRates(hourlyRates);
		if (hourlyRateCatalogDTO.getHourlyRateCatalogModel() != null
				&& hourlyRateCatalogDTO.getHourlyRateCatalogModel().getWorkArea() != null) {
			hourlyRateCatalogDTO
					.setWorkAreaId(hourlyRateCatalogDTO.getHourlyRateCatalogModel().getWorkArea().getWorkAreaId());
		} else {
			hourlyRateCatalogDTO.setWorkAreaId(null);
		}
		hourlyRateCatalogDTO.setWorkAreaModelList(
				new ArrayList<WorkAreaModel>(hourlyRateCatalogDTO.getUserLogged().getWorkAreas()));
		return hourlyRateCatalogDTO;
	}

	/**
	 * check if exist WorkArea By Designation and set result in DTO
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	public HourlyRateCatalogDTO loadExistWorkAreaByDesignation(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		HourlyRateCatalogModel hRateCatalogModel = hourlyRateCatalogDTO.getHourlyRateCatalogModel();
		Boolean alreadyExist = loadIsExistCatalog(hRateCatalogModel.getHourlyRateCatalogId(),
				hRateCatalogModel.isLoaded(), hRateCatalogModel.getDesignation(),
				hRateCatalogModel.getFolder().getFolderId());
		hourlyRateCatalogDTO.setExistHRCatalogByDesignation(alreadyExist);
		return hourlyRateCatalogDTO;
	}

	/**
	 * get HourlyRateCatalog By Designation And Folder used to check unicity
	 * 
	 * @param catalogId
	 *            if catalogId and loaded ==> returned catalog shouldn't be with id = catalogId
	 * @param loaded
	 * @param designation
	 * @param folderId
	 * @return
	 */
	private boolean loadIsExistCatalog(Long catalogId, boolean loaded, String designation, Long folderId) {
		if (hourlyRateCatalogManager.getHourlyRateCatalogByDesignationAndFolder(catalogId, loaded, designation,
				folderId) != null) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Check if this catalog is used by users, if true set flag isCatalogAttachedToUser to true
	 * 
	 * @param hourlyRateCatalogDTO
	 * @return
	 */
	public HourlyRateCatalogDTO loadIsCatalogAttachedToUser(HourlyRateCatalogDTO hourlyRateCatalogDTO) {
		Boolean isCatalogAttachedToUser = Boolean.FALSE;
		if (hourlyRateCatalogDTO.getHourlyRateCatalogModel() != null
				&& hourlyRateCatalogDTO.getHourlyRateCatalogModel().getHourlyRateCatalogId() != null) {
			List<UserModel> users = userManager
					.getUsersByCatalog(hourlyRateCatalogDTO.getHourlyRateCatalogModel().getHourlyRateCatalogId());
			if (users != null && !users.isEmpty()) {
				isCatalogAttachedToUser = Boolean.TRUE;
			}
		}
		hourlyRateCatalogDTO.setIsCatalogAttachedToUser(isCatalogAttachedToUser);
		return hourlyRateCatalogDTO;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}

}
