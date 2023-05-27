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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.DocumentPoolManager;
import vwg.vw.km.application.implementation.ElementCategoryManager;
import vwg.vw.km.application.implementation.ElementManager;
import vwg.vw.km.application.implementation.ElementValueManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.ElementVersionUsersManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.impl.ElementVersionChangeManagerImpl;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @version $Revision: 1.221 $ $Date: 2018/02/19 08:52:18 $
 */
/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2012
 * </p>
 * 
 * @author zouhairs changed by $Author: saidi $
 * @version $Revision: 1.221 $ $Date: 2018/02/19 08:52:18 $
 */
public class CostElementService extends BaseService<CostElementDTO> {
	private final Log log = LogManager.get().getLog(CostElementService.class);

	private ElementVersionManager elementVersionManager;

	private ElementManager elementManager;

	private ElementValueManager elementValueManager;

	private EnumStatusManager enumStatusManager;

	private ElementCategoryManager elementCategoryManager;

	private FolderManager folderManager;

	private DocumentPoolManager documentPoolManager;

	private HourlyRateCatalogManager hourlyRateCatalogManager;

	private ComponentElementManager componentElementManager;

	private BrandManager brandManager;

	private ElementVersionUsersManager elementVersionUsersManager;

	private ComponentVersionManager componentVersionManager;

	// start transactional methods

	/**
	 * save current element and element version in DTO and all related objects
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO save(CostElementDTO costElementDTO) {

		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		ElementModel element = elementVersion.getElement();
		UserModel user = costElementDTO.getUserLogged();

		if (elementVersion.isLoaded()) {
			element.setLoaded(Boolean.TRUE);
			element.setModifierRefId(user.getUserId());
		} else {
			element.setCreatorRefId(user.getUserId());
			// set Display order for new element
			setNewDisplayOrder(element.getFolder().getFolderId(), element);
		}

		elementVersion.setElement(element);
		elementManager.saveObject(element);

		boolean elementVersionLoaded = elementVersion.isLoaded();
		if (!elementVersionLoaded) {
			elementVersion.setCreator(user);
			EnumStatusModel enumStatus = enumStatusManager.getObject(EnumStatusManager.Status.CREATED.value());
			elementVersion.setEnumStatus(enumStatus);
			costElementDTO
					.setAllowedTransitions(enumStatusManager.getAllowedTransitions(enumStatus, Boolean.FALSE, null));
			BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
			// set validFrom validTo attributes
			elementVersion.setValidFrom(validFrom);
			elementVersion.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));

			elementVersionManager.saveObject(elementVersion);

			// add element version to set in element
			Set<ElementVersionModel> elementVersions = new HashSet<ElementVersionModel>();
			elementVersions.add(elementVersion);
			element.setElementVersions(elementVersions);
		} else {
			elementVersion.setModifier(user);
		}
		if (costElementDTO.getElementCategoryId() != null) {
			elementVersion.setElementCategory(elementCategoryManager.getObject(costElementDTO.getElementCategoryId()));
		}

		// save documents
		manageAssociatedDocuments(costElementDTO);
		elementVersionManager.saveWithCheckModifications(elementVersion);
		elementValueManager.saveOrRemoveElementValue(elementVersion);

		// add change entry when element is activated or deactivated
		if (element.getOldInactive() != null && !element.getInactive().equals(element.getOldInactive())) {
			if (element.getInactive()) {
				ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.DEACTIVATE.value(), "",
						"", elementVersion, user, Boolean.FALSE, ChangeType.DEACTIVATE);
			} else {
				ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.ACTIVATE.value(), "", "",
						elementVersion, user, Boolean.FALSE, ChangeType.ACTIVATE);
			}
		}
		element.setOldInactive(element.getInactive());

		// 27.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing

		if (element.getOldDesignation() != null && !element.getDesignation().equals(element.getOldDesignation())) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_DESIGNATION.value(),
					element.getOldDesignation(), element.getDesignation(), elementVersion, user, Boolean.FALSE,
					ChangeType.DESIGNATION, null, null, null);
		}
		element.setOldDesignation(element.getDesignation());

		if (element.getOldElementNumber() != null
				&& !element.getElementNumber().equals(element.getOldElementNumber())) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_NUMBER.value(),
					element.getOldElementNumber(), element.getElementNumber(), elementVersion, user, Boolean.FALSE,
					ChangeType.NUMBER, null, null, null);
		}
		element.setOldElementNumber(element.getElementNumber());

		costElementDTO.setElementVersionId(elementVersion.getElementVersionId());
		loadChangesForVersion(costElementDTO, elementVersion.getElementVersionId());
		return costElementDTO;
	}

	/**
	 * 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten Set new Object display order to Max
	 * in Folder
	 * 
	 * @param folderId
	 * @param element
	 */
	private void setNewDisplayOrder(Long folderId, ElementModel element) {
		Long newObjectOrder = elementManager.getMaxDisplayOrderByFolderId(folderId) + 1;
		element.setDisplayOrder(newObjectOrder);
	}

	/**
	 * Check if folder has elements that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param userBrand
	 *            : user default Brand
	 * @return elementsIds:list of elements to be filtered
	 */
	public List<Long> getElementsListToBeFiltred(Long folderId, String userBrand) {
		return elementManager.getElementsListToBeFiltred(folderId, userBrand);
	}

	/**
	 * Check if folder has elements that should be visible by users filter
	 * 
	 * @param ownerFilters
	 *            : owner brands to filter by
	 * @param folderId
	 *            : parent folder id
	 * @param elementsIds
	 *            :list of elements to be filtered
	 * @return
	 */
	public boolean getHasVisibleElementsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> elementsIds) {
		return elementManager.hasVisibleElementsByOwnerFilter(ownerFilters, folderId, elementsIds);
	}

	/**
	 * Check if folder has elements that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param folderId
	 *            : parent folder id
	 * @param elementsIds
	 *            : list of elements to be filtered
	 * @return
	 */
	public boolean getHasVisibleElementsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> elementsIds) {
		return elementManager.hasVisibleElementsByUsersFilter(usersFilters, folderId, elementsIds);
	}

	/**
	 * Check if folder has elements that should be visible by users filter
	 * 
	 * @param folderId
	 *            : parent folder id
	 * @return
	 */
	public boolean getHasElements(Long folderId) {
		return elementManager.getHasElements(folderId);
	}

	/**
	 * Check if folder has inactive elements
	 * 
	 * @param folderId
	 *            : parent folder id
	 * @return
	 */
	public List<Long> getInactiveElementIds(Long folderId) {
		return elementManager.getInactiveElementIds(folderId);
	}

	/**
	 * load detail and dependencies for current element version in DTO
	 * 
	 * @param costElementDTO
	 * @return
	 */
	private CostElementDTO loadDetail(CostElementDTO costElementDTO) {
		// load dependencies to element version
		log.info("Start loading Detail for: " + costElementDTO.getElementVersion());
		elementVersionManager.loadDependencies(costElementDTO.getElementVersion());
		// calculate element value
		calculate(costElementDTO);
		// list to be used in search
		filterElementValues(costElementDTO);
		// cached list from manager to DTO
		loadDocuments(costElementDTO);
		// load element category from DB
		costElementDTO.setElementCategories(elementCategoryManager.getObjects());
		// load data from db on demand
		if (costElementDTO.getElementVersionId() != null) {
			costElementDTO.setComponentElementList(getComponentElementList(costElementDTO));
		} else {
			costElementDTO.setComponentElementList(new ArrayList<ComponentElementModel>());
		}
		List<ElementVersionModel> elementVersions = costElementDTO.getVersionHistoryList();
		// load allowed transitions
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		if (elementVersion != null) {
			List<Long> withoutToStatus = new ArrayList<Long>();
			if (!elementVersions.isEmpty()) {
				ElementVersionModel lastVersion = elementVersions.get(0);
				if (!elementVersion.equals(lastVersion)) {
					withoutToStatus.add(EnumStatusManager.Status.IN_PROGRESS.value());
				}
			}

			List<EnumTransitionModel> transitions = enumStatusManager
					.getAllowedTransitions(elementVersion.getEnumStatus(), Boolean.FALSE, withoutToStatus);
			costElementDTO.setAllowedTransitions(transitions);
			checkElementVersionUse(costElementDTO);
			loadChangesForVersion(costElementDTO, elementVersion.getElementVersionId());
		}
		return costElementDTO;
	}

	/**
	 * Check if an element version is used or not
	 * 
	 * @param costElementDTO
	 * @return
	 */
	private CostElementDTO checkElementVersionUse(CostElementDTO costElementDTO) {
		UserModel user = costElementDTO.getUserLogged();
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		if (user.getDefaultBrand() != null && costElementDTO.getElementVersionId() != null) {
			Boolean isElementVersionUsed = elementVersionUsersManager
					.getElementVersionUses(elementVersion.getElementVersionId(), user.getDefaultBrand()) != null
							? Boolean.TRUE
							: Boolean.FALSE;
			costElementDTO.setElementVersionUsed(isElementVersionUsed);
			// ZS: PTS-Requirement_22198: When an element is already used no need to check if it can be used
			if (!isElementVersionUsed) {
				Boolean isElementVersionCanBeUsed = isElementVersionCanBeUsed(elementVersion, user.getDefaultBrand());
				costElementDTO.setElementVersionCanBeUsed(isElementVersionCanBeUsed);
			}
		}
		return costElementDTO;
	}

	/**
	 * Check all the conditions to use an element version
	 * 
	 * @param elementVersion
	 * @param brand
	 * @return
	 */
	private Boolean isElementVersionCanBeUsed(ElementVersionModel elementVersion, String brandId) {
		// if the element version is not in use or approved
		if (!elementVersion.getEnumStatus().equals(enumStatusManager.getObject(EnumStatusManager.Status.IN_USE.value()))
				&& !elementVersion.getEnumStatus()
						.equals(enumStatusManager.getObject(EnumStatusManager.Status.APPROVED.value()))) {
			log.info("The status of the element version is not IN_USE or APPROVED");
			return Boolean.FALSE;
		}
		BrandModel elementOwner = elementVersion.getElement().getOwner();
		// If the user brand is the owner
		if (elementOwner != null && elementOwner.getBrandId().equals(brandId)) {
			log.info("The user brand is the same as the owner of the component");
			return Boolean.FALSE;
		}
		// if the brand already used this version
		if (elementVersionUsersManager.getElementVersionUses(elementVersion.getElementVersionId(), brandId) != null) {
			log.info("The brand " + brandId + " already use this version");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * save associated documents to current element version
	 * 
	 * @param costElementDTO
	 */
	private void manageAssociatedDocuments(CostElementDTO costElementDTO) {
		List<DocumentPoolModel> documentsToBeSaved = costElementDTO.getDocumentList();
		if (documentsToBeSaved != null && !documentsToBeSaved.isEmpty()) {
			for (DocumentPoolModel document : documentsToBeSaved) {
				if (document.getDocumentPoolId() == null) {
					documentPoolManager.saveObject(document);
				}
			}
		}
		costElementDTO.getElementVersion()
				.setDocuments(new HashSet<DocumentPoolModel>(costElementDTO.getDocumentList()));
		// TO-DO remove document when no associated
	}

	/**
	 * calculate all fields called when an element value is changed
	 * 
	 * @param costElementDTO
	 * @return
	 */
	private CostElementDTO calculate(CostElementDTO costElementDTO) {
		ElementVersionModel elVer = costElementDTO.getElementVersion();
		elementVersionManager.calculate(elVer);
		return costElementDTO;
	}

	/**
	 * Filter element values
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO filterElementValues(CostElementDTO costElementDTO) {
		costElementDTO.setFilteredElementValues(
				elementValueManager.filterElementValues(costElementDTO.getElementVersion().getElementValuesForView(),
						costElementDTO.getUserLogged().getDisplayFilter()));
		return costElementDTO;
	}

	/**
	 * load documents of current element version from memory
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadDocuments(CostElementDTO costElementDTO) {
		List<DocumentPoolModel> objectDocumentList = new ArrayList<DocumentPoolModel>();
		if (costElementDTO.getElementVersion() != null) {
			objectDocumentList.addAll(costElementDTO.getElementVersion().getDocuments());
		}
		costElementDTO.setDocumentList(objectDocumentList);
		return costElementDTO;
	}

	/**
	 * load Actual ElementVersion By Element
	 * 
	 * @param costElementDTO
	 * @param lastVersion
	 * @return
	 */
	private CostElementDTO loadActualElementVersionByElement(CostElementDTO costElementDTO, boolean lastVersion) {
		Long elementId = costElementDTO.getElementId();
		ElementVersionModel elementVersion = null;
		if (!lastVersion) {
			elementVersion = elementVersionManager.getLastElementVersionByStatus(elementId,
					EnumStatusManager.Status.IN_USE.value());
		}
		if (elementVersion == null) {
			elementVersion = elementVersionManager.getLastElementVersionByElementId(elementId);
		}
		if (elementVersion != null) {
			costElementDTO.setElementVersionId(elementVersion.getElementVersionId());
			if (elementVersion.getElementCategory() != null) {
				costElementDTO.setElementCategoryId(elementVersion.getElementCategory().getElementCategoryId());
			}
		}
		costElementDTO.setElementVersion(elementVersion);
		return costElementDTO;
	}

	/**
	 * load ElementVersion By ElementVersionId
	 * 
	 * @param costElementDTO
	 * @return
	 */
	private CostElementDTO loadElementVersionByElementVersionId(CostElementDTO costElementDTO) {
		ElementVersionModel elementVersion = elementVersionManager.getObject(costElementDTO.getElementVersionId());
		if (elementVersion != null) {
			costElementDTO.setElementId(elementVersion.getElement().getElementId());
			if (elementVersion.getElementCategory() != null) {
				costElementDTO.setElementCategoryId(elementVersion.getElementCategory().getElementCategoryId());
			}
		}
		costElementDTO.setElementVersion(elementVersion);
		costElementDTO.setFolderId(elementVersion.getElement().getFolder().getFolderId());
		return costElementDTO;
	}

	/**
	 * Delete current element version in DTO
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO deleteVersion(CostElementDTO costElementDTO) {
		Long versionId = costElementDTO.getElementVersionId();
		ElementVersionModel elementVersion = elementVersionManager.getObject(versionId);
		if (elementVersion != null) {
			// TO-DO remove element when no element version attached
			List<Long> elementVersionAttachedIds = elementVersionManager
					.getElementVersionAttachedIds(elementVersion.getElement().getElementId());
			if (elementVersionAttachedIds != null) {
				if (elementVersionAttachedIds.size() == 1) {
					// remove only element and with cascade remove element version
					if (elementVersionAttachedIds.contains(versionId)) {
						// 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten
						// Decrease display order for folder elements after element delete
						Long elementId = elementVersion.getElement().getElementId();
						Long oldFolderId = elementVersion.getElement().getFolder().getFolderId();
						elementManager.removeObject(elementId);
						costElementDTO.setElementDeleted(Boolean.TRUE);
						elementManager.updateDisplayOrdersForOtherNodes(elementId, oldFolderId);
					}
				} else {// remove only element version
					elementVersionManager.removeObject(elementVersion.getElementVersionId());
				}
			}
		}

		return costElementDTO;
	}

	/**
	 * Load and element with all detail and related objects for Administration purpose
	 * 
	 * @param costElementDTO
	 * @param objectId
	 * @param folderId
	 * @param isElement
	 * @param lastVersion
	 * @return
	 */
	public CostElementDTO loadDTOForDetail(CostElementDTO costElementDTO, Long objectId, Long folderId,
			boolean isElement, boolean lastVersion) {

		Long defaultCatalogId = HourlyRateCatalogManager.DEFAULT_CATALOG_ID;
		if (costElementDTO.getUserLogged().getDefaultStdSatzKatRefId() != null) {
			defaultCatalogId = costElementDTO.getUserLogged().getDefaultStdSatzKatRefId();
		}
		HourlyRateCatalogModel catalogModel = hourlyRateCatalogManager.getObject(defaultCatalogId);
		// set current object id by given param
		if (isElement) {
			costElementDTO.setElementId(objectId);
			costElementDTO.setElementVersionId(null);
		} else {
			costElementDTO.setElementVersionId(objectId);
		}
		// if object in DB load it else create a new one
		if (objectId != null) {
			if (isElement) {
				loadActualElementVersionByElement(costElementDTO, lastVersion);
			} else {
				loadElementVersionByElementVersionId(costElementDTO);
			}
		} else {
			costElementDTO.setElementId(null);
			costElementDTO.setElementVersionId(null);
			costElementDTO.setElementCategoryId(null);
			ElementVersionModel tmpElementVersion = new ElementVersionModel();
			ElementModel element = new ElementModel();
			element.setInactive(Boolean.FALSE);
			element.setFolder(folderManager.getObject(folderId));

			BrandModel owner = getDefaultBrand(costElementDTO.getUserLogged().getDefaultBrand());
			element.setOwner(owner);

			tmpElementVersion.setElement(element);
			tmpElementVersion.setEnumStatus(enumStatusManager.getObject(EnumStatusManager.Status.NEW.value()));
			tmpElementVersion.setWithProvision(Boolean.FALSE);
			tmpElementVersion.setCatalogModel(catalogModel);
			costElementDTO.setElementVersion(tmpElementVersion);
			loadDetail(costElementDTO);
		}
		// ZS: PTS-Requirement_22198: load detail only when the element version node is selected
		if (!isElement) {
			ElementVersionModel elVer = costElementDTO.getElementVersion();
			elVer.setCatalogModel(catalogModel);
			loadDetail(costElementDTO);
		}
		costElementDTO.getVersionHistoryList();
		return costElementDTO;
	}

	/**
	 * PTS-Requirement_22198: load DTO for copy purpose
	 * 
	 * @param costElementDTO
	 * @param objectId
	 * @param folderId
	 * @param isElement
	 * @param lastVersion
	 * @return
	 */
	public CostElementDTO loadDTOForCopy(CostElementDTO costElementDTO, Long objectId, Long folderId, boolean isElement,
			boolean lastVersion) {

		log.info("load Element version for copy: " + costElementDTO.getElementVersion());
		Long defaultCatalogId = HourlyRateCatalogManager.DEFAULT_CATALOG_ID;
		if (costElementDTO.getUserLogged().getDefaultStdSatzKatRefId() != null) {
			defaultCatalogId = costElementDTO.getUserLogged().getDefaultStdSatzKatRefId();
		}
		HourlyRateCatalogModel catalogModel = hourlyRateCatalogManager.getObject(defaultCatalogId);

		// set current object id by given param
		costElementDTO.setElementId(objectId);
		costElementDTO.setElementVersionId(null);

		// if object in DB load it else create a new one
		if (objectId != null) {
			loadActualElementVersionByElement(costElementDTO, lastVersion);
		}

		ElementVersionModel elVer = costElementDTO.getElementVersion();
		elVer.setCatalogModel(catalogModel);

		elementVersionManager.loadDependencies(costElementDTO.getElementVersion());
		// calculate element value
		calculate(costElementDTO);

		loadDocuments(costElementDTO);

		List<ElementVersionModel> elementVersions = costElementDTO.getVersionHistoryList();
		// load allowed transitions
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		if (elementVersion != null) {
			List<Long> withoutToStatus = new ArrayList<Long>();
			if (!elementVersions.isEmpty()) {
				ElementVersionModel lastElementVersion = elementVersions.get(0);
				if (!elementVersion.equals(lastElementVersion)) {
					withoutToStatus.add(EnumStatusManager.Status.IN_PROGRESS.value());
				}
			}
			List<EnumTransitionModel> transitions = enumStatusManager
					.getAllowedTransitions(elementVersion.getEnumStatus(), Boolean.FALSE, withoutToStatus);
			costElementDTO.setAllowedTransitions(transitions);
		}
		return costElementDTO;
	}

	/**
	 * Load list of element that can be assigned to a component
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadCanBeAttachedElements(CostElementDTO costElementDTO) {
		List<Long> status = new ArrayList<Long>();
		costElementDTO.setElements(
				getElementsByFolder(costElementDTO.getFolderId(), costElementDTO.getUserLogged().getUserId(), status));
		return costElementDTO;
	}

	/**
	 * Load of element list
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadDTOForList(CostElementDTO costElementDTO) {
		costElementDTO.setElements(
				getElementsByFolder(costElementDTO.getFolderId(), costElementDTO.getUserLogged().getUserId(), null));
		return costElementDTO;
	}

	/**
	 * Return list of elements for given criteria
	 * 
	 * @param folderId
	 *            folder
	 * @param loggedUser
	 * @param status
	 *            list of status
	 * @return
	 */
	public List<ElementModel> getElementsByFolder(Long folderId, Long loggedUser, List<Long> status) {
		return elementManager.getElementsByFolder(folderId, loggedUser, status);
	}

	/**
	 * Load all elements by status in DTO (in process node / to be approved node)
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadDTOForListByStatus(CostElementDTO costElementDTO) {
		Long loggedUserId = null;
		if (costElementDTO.getUserLogged() != null) {
			loggedUserId = costElementDTO.getUserLogged().getUserId();
		}
		costElementDTO.setElements(elementManager.getElementsByStatus(costElementDTO.getSearchStatus(), loggedUserId));
		return costElementDTO;
	}

	/**
	 * Load element list for working folder
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadDTOForWorkingFolder(CostElementDTO costElementDTO, Long userId, Long folderId) {
		costElementDTO.setElements(
				elementManager.getElementsForWorkingFolder(costElementDTO.getSearchStatus(), userId, folderId));
		return costElementDTO;
	}

	/**
	 * load all inactive elements
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO loadDTOForInactiveList(Long folderId, CostElementDTO costElementDTO) {
		costElementDTO.setElements(elementManager.getInactiveElements(folderId));
		return costElementDTO;
	}

	/**
	 * @see vwg.vw.km.application.service.base.BaseService#loadDTOForListBySearchString(vwg.vw.km.application.service.dto.base.BaseDTO)
	 */
	@Override
	public CostElementDTO loadDTOForListBySearchString(CostElementDTO costElementDTO) {
		// search for folders
		List<FolderModel> folderResultList = folderManager.getFoldersBySearchString(costElementDTO.getFolderId(),
				costElementDTO.getSearchString(), EnumObjectTypeManager.ELEMENT_ID);
		costElementDTO.setFolders(folderResultList);

		List<ElementModel> resultList = elementManager.getElementsBySearchString(costElementDTO.getFolderId(),
				costElementDTO.getUserLogged().getUserId(), costElementDTO.getSearchString());
		costElementDTO.setElements(resultList);
		costElementDTO.setNoSearchResult(costElementDTO.isNoElements());
		return costElementDTO;
	}

	/**
	 * load Changed ElementValue
	 * 
	 * @param costElementDTO
	 * @param elementValueIndex
	 * @param newValue
	 * @return
	 */
	public CostElementDTO loadChangedElementValue(CostElementDTO costElementDTO, int elementValueIndex,
			BaseBigDecimal newValue) {
		// 1-update one element in table
		// get list from DTO
		List<ElementValueModel> filtredElements = costElementDTO.getFilteredElementValues();
		// get element from list using given index
		ElementValueModel elementValue = filtredElements.get(elementValueIndex);
		// if price per unit is true set number else set amount
		if (elementValue.getCostAttribute().getPricePerUnit()) {
			elementValue.setNumber(newValue);
		} else {
			elementValue.setAmount(newValue);
		}
		// 2-update object in overview Tab
		calculate(costElementDTO);
		return costElementDTO;
	}

	/**
	 * move element to folder
	 * 
	 * @param elementId
	 * @param folderId
	 * @return
	 */
	public Boolean saveElementToFolder(Long elementId, Long folderId) {
		ElementModel e = elementManager.getObject(elementId);
		Long oldFolderId = e.getFolder().getFolderId();
		e.setFolder(folderManager.getObject(folderId));

		/*
		 * 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten Set display order to Max order in
		 * new folder for pasted Object
		 */
		setNewDisplayOrder(folderId, e);
		elementManager.saveObject(e);
		elementManager.updateDisplayOrdersForOtherNodes(elementId, oldFolderId);
		return Boolean.TRUE;
	}

	/**
	 * load element version from DB
	 * 
	 * @param costElementDTO
	 * @return version
	 */
	public ElementVersionModel loadElementVersion(CostElementDTO costElementDTO) {
		ElementVersionModel version = elementVersionManager
				.getObject(costElementDTO.getElementVersion().getElementVersionId());
		return version;
	}

	/**
	 * method used to change status of version from current one to selected one if from status = inuse a new version
	 * model is created
	 * 
	 * @param costElementDTO
	 */
	public String saveNewStatus(CostElementDTO costElementDTO) {
		ElementVersionModel e = costElementDTO.getElementVersion();
		Long fromStatusId = e.getEnumStatus().getEnumStatusId();
		EnumStatusModel toStatus = enumStatusManager.getObject(costElementDTO.getToStatusId());
		// check if creation of new version by the new status is allowed
		boolean isLastVersion = elementVersionManager.isTheLastElementVersion(e.getElementVersionId(),
				e.getElement().getElementId(), toStatus.getEnumStatusId());

		if (!isLastVersion) {
			log.info("try to create another component version denied");
			return "VERSION_CREATION_DENIED";
		} else {

			ElementModel element = e.getElement();
			if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
					&& toStatus.getEnumStatusId().equals(EnumStatusManager.Status.APPROVED.value())
					&& element.getFolder().getCreatorRefId() != null) {
				return "VERSION_NOT_RELEASED";
			}

			UserModel user = costElementDTO.getUserLogged();
			if (e.getComment() != null) {
				if (costElementDTO.getChangeStatusComment() != null
						&& !"".equals(costElementDTO.getChangeStatusComment())) {
					e.setComment(e.getComment()
							.concat(costElementDTO.REMARKS_SEPARATOR + costElementDTO.getChangeStatusComment()));
				}
			} else {
				e.setComment(costElementDTO.getChangeStatusComment());
			}
			if (fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())
					&& costElementDTO.getToStatusId().equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(e.getDocuments());
				// empty all relations
				e.setDocuments(null);
				e.setElementValues(null);
				e.setElementVersionChanges(null);
				e.setElementVersionUsers(null);
				// 29.10.2013: ZS: PTS_Problem-26455: reset ComponentElements list before creating the clone version
				e.setComponentElements(null);
				// clone
				ElementVersionModel cloned = new ElementVersionModel();
				BeanUtils.copyProperties(e, cloned);
				// empty remark field
				cloned.setComment(null);
				cloned.setOldComment(null);
				// for framework use
				cloned.setLoaded(false);
				BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
				cloned.setValidFrom(validFrom);
				cloned.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));
				cloned.setEnumStatus(toStatus);
				cloned.setDocuments(documents);
				cloned.setCreator(user);
				cloned.setModifier(null);
				cloned.setReleaser(null);
				cloned.setNumber(null);
				elementVersionManager.saveObject(cloned);
				// copy element values
				elementValueManager.copyElementValues(e, cloned);
				costElementDTO.setElementVersionId(cloned.getElementVersionId());
			} else {
				if (costElementDTO.getToStatusId().equals(EnumStatusManager.Status.APPROVED.value())) {
					e.setReleaser(user);
					// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
					// unlock the version
					saveElementVersionLockStatus(costElementDTO, false);

				}
				e.setModifier(user);
				e.setEnumStatus(toStatus);
				elementVersionManager.saveWithCheckModifications(e);
			}
			return "";
		}
	}

	/**
	 * check if there is an olde element with entered number and designation in the same brand
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO getExistElementWithSameNumberOrDesignation(CostElementDTO costElementDTO) {
		ElementModel eModel = costElementDTO.getElementVersion().getElement();
		if ((elementManager.getElementByNmberOrDesignation(eModel.getElementId(),
				costElementDTO.getElementVersion().isLoaded(), eModel.getElementNumber(), eModel.getDesignation(),
				eModel.getOwner().getBrandId())) != null) {
			costElementDTO.setExistElementWithSameNumberOrDesignation(Boolean.TRUE);
		} else {
			costElementDTO.setExistElementWithSameNumberOrDesignation(Boolean.FALSE);
		}
		return costElementDTO;
	}

	// end transactional methods

	/**
	 * Create a copy of a element. This method will change current element in DTO to a new one changing name an
	 * designation and setting some properties to null
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO copyElement(CostElementDTO costElementDTO) {
		// set objects as not loaded
		costElementDTO.getElementVersion().getElement().setLoaded(Boolean.FALSE);
		costElementDTO.getElementVersion().setLoaded(Boolean.FALSE);
		// new number / designation / status
		String newNumber = "Kopie_" + costElementDTO.getElementVersion().getElement().getElementNumber();
		String newDesignation = "Kopie_" + costElementDTO.getElementVersion().getElement().getDesignation();
		costElementDTO.getElementVersion().getElement().setElementNumber(newNumber);
		costElementDTO.getElementVersion().getElement().setDesignation(newDesignation);
		BrandModel owner = getDefaultBrand(costElementDTO.getUserLogged().getDefaultBrand());
		costElementDTO.getElementVersion().getElement().setOwner(owner);

		costElementDTO.getElementVersion()
				.setEnumStatus(enumStatusManager.getObject(EnumStatusManager.Status.NEW.value()));
		costElementDTO.setAllowedTransitions(enumStatusManager
				.getAllowedTransitions(costElementDTO.getElementVersion().getEnumStatus(), Boolean.FALSE, null));
		costElementDTO.getElementVersion().setComment(null);
		costElementDTO.getElementVersion().setCreationDate(null);
		costElementDTO.getElementVersion().setCreator(null);
		costElementDTO.getElementVersion().setModificationDate(null);
		costElementDTO.getElementVersion().setModifier(null);
		costElementDTO.getElementVersion().setReleaser(null);
		costElementDTO.getElementVersion().setValidFrom(null);
		costElementDTO.getElementVersion().setValidTo(null);
		costElementDTO.getElementVersion().setNumber(null);

		// reset old values
		costElementDTO.getElementVersion().getElement().setOldElementNumber(null);
		costElementDTO.getElementVersion().getElement().setOldDesignation(null);
		costElementDTO.getElementVersion().setOldComment(null);
		costElementDTO.getElementVersion().setOldElectric(null);
		costElementDTO.getElementVersion().setOldElectricPPPLNotice(null);
		costElementDTO.getElementVersion().setOldElectricPurchasedPartPriceLevelDate(null);
		costElementDTO.getElementVersion().setOldMecanicPPPLDate(null);
		costElementDTO.getElementVersion().setOldMecanicPPPLNotice(null);
		costElementDTO.getElementVersion().setOldMechanicalConstruction(null);
		costElementDTO.getElementVersion().setOldMechanicalExecution(null);

		// 27.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		costElementDTO.getElementVersion().setOldCatalogNumber(null);
		costElementDTO.getElementVersion().setOldDescription(null);
		costElementDTO.getElementVersion().setOldElementCategory(null);
		costElementDTO.getElementVersion().setOldManufacturer(null);
		// avoid not copied set
		costElementDTO.getElementVersion().getElement().setElementVersions(new HashSet<ElementVersionModel>());
		costElementDTO.getElementVersion().setComponentElements(null);
		costElementDTO.getElementVersion().setElementValues(null);

		// 06.03.2014: AMR: PTS_Problem-36750: Copy joined documents when copying elements
		Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(costElementDTO.getDocumentList());
		costElementDTO.getElementVersion().setDocuments(documents);
		costElementDTO.setDocumentList(new ArrayList<DocumentPoolModel>(documents));

		costElementDTO.getElementVersion().setElementVersionChanges(null);
		costElementDTO.getElementVersion().setElementVersionUsers(null);
		// copy components values
		for (ElementValueModel vModel : costElementDTO.getElementVersion().getElementValuesForView()) {
			vModel.setOldAmount(null);
			vModel.setOldNumber(null);
			vModel.setLoaded(Boolean.FALSE);
		}
		costElementDTO.setElementVersionCanBeUsed(Boolean.FALSE);
		costElementDTO.setElementVersionUsed(Boolean.FALSE);
		return costElementDTO;
	}

	/**
	 * load all changes relatives to given version model id in DTO
	 * 
	 * @param costElementDTO
	 * @param elementVersionId
	 * @return
	 */
	private CostElementDTO loadChangesForVersion(CostElementDTO costElementDTO, Long elementVersionId) {
		List<ElementVersionChangeModel> elementVersionChanges = ElementVersionChangeManagerImpl.get()
				.getChangesByVersion(elementVersionId, null);
		costElementDTO.setVersionChanges(elementVersionChanges);
		return costElementDTO;
	}

	/**
	 * Return the defualt of the brand
	 * 
	 * @param defaultBrand
	 * @return
	 */
	public BrandModel getDefaultBrand(String defaultBrand) {
		return brandManager.getObject(defaultBrand);
	}

	/**
	 * Set an Element version as used for a brand save an entry in element version change
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO saveElementVersionAsUsed(CostElementDTO costElementDTO) {
		ElementVersionUsersModel elementVersionUses = new ElementVersionUsersModel();
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		UserModel user = costElementDTO.getUserLogged();

		BrandModel brand = getDefaultBrand(user.getDefaultBrand());

		// check if there is an other element version already used
		ElementVersionUsersModel oldUser = elementVersionUsersManager
				.getElementUsers(elementVersion.getElement().getElementId(), brand.getBrandId());
		costElementDTO.setRelatedComponents(null);
		if (oldUser != null) {
			log.info("currently used element version and issue of transition to new item version. ");
			ElementVersionModel oldElementVersion = oldUser.getElementVersion();
			costElementDTO.setOldVersionUsed(Boolean.TRUE);
			costElementDTO.setUsedVersion(
					oldElementVersion.getNumber() != null ? oldElementVersion.getNumber().toString() : "");

			// load on demand
			oldElementVersion.getComponentElements().size();
			Set<ComponentElementModel> componentElements = oldElementVersion.getComponentElements();
			Set<String> relatedComponents = new HashSet<String>();
			if (!componentElements.isEmpty()) {
				for (ComponentElementModel componentElement : componentElements) {
					ComponentModel component = componentElement.getComponentStand().getComponentVersion()
							.getComponent();
					if (!oldElementVersion.getElement().getOwner().equals(component.getOwner())) {
						if (componentVersionManager.isTheLastComponentVersion(
								componentElement.getComponentStand().getComponentVersion().getComponentVersionId(),
								componentElement.getComponentStand().getComponentVersion().getComponent()
										.getComponentId(),
								EnumStatusManager.Status.IN_USE.value())) {
							relatedComponents.add(component.getNumberAndDesignation());
						}
					}
				}

				if (!relatedComponents.isEmpty()) {
					StringBuffer components = new StringBuffer();
					for (String name : relatedComponents) {
						components.append("<br/>");
						components.append(name);
					}
					costElementDTO.setRelatedComponents(components.toString());
				}
			}

			return costElementDTO;
		}

		elementVersionUses.setBrand(brand);
		elementVersionUses.setElementVersion(elementVersion);
		elementVersionUses.setActive(false);
		elementVersionUsersManager.saveObject(elementVersionUses);
		log.info("use the elementVersion: " + elementVersion.getElementVersionId() + " For Marke: "
				+ brand.getBrandId());

		// add change entry
		ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.USE_STAND.value(), "",
				brand.getBrandId(), elementVersion, user, Boolean.FALSE, ChangeType.USE_STAND);

		costElementDTO.setElementVersionUsed(Boolean.TRUE);
		costElementDTO.setElementVersionCanBeUsed(Boolean.FALSE);

		// set the list of brand using an element version (Nutzer)
		costElementDTO.getElementVersion().getElementVersionUsers().add(elementVersionUses);
		loadChangesForVersion(costElementDTO, elementVersion.getElementVersionId());
		return costElementDTO;
	}

	/**
	 * change use of Element version for a brand save an entry in element version change
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO saveChangeUseElementVersion(CostElementDTO costElementDTO) {
		ElementVersionUsersModel elementVersionUses = new ElementVersionUsersModel();

		UserModel user = costElementDTO.getUserLogged();
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();
		BrandModel brand = getDefaultBrand(user.getDefaultBrand());

		// remove the other element version already used
		ElementVersionUsersModel oldElementVersionUses = elementVersionUsersManager
				.getElementUsers(elementVersion.getElement().getElementId(), brand.getBrandId());
		if (oldElementVersionUses != null) {
			ElementVersionModel targetVersion = oldElementVersionUses.getElementVersion();
			targetVersion.getElementVersionUsers().remove(oldElementVersionUses);
			costElementDTO.getElementVersion().getElement().getElementVersions().remove(targetVersion);
			elementVersionUsersManager.removeObject(oldElementVersionUses.getId());
			log.info("remove the use of elementVersion: " + targetVersion.getElementVersionId() + " For Marke: "
					+ brand.getBrandId());

			// add change entry
			ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(),
					brand.getBrandId(), "", targetVersion, user, Boolean.FALSE, ChangeType.UNUSE_STAND);

			costElementDTO.getElementVersion().getElement().getElementVersions().add(targetVersion);
		}

		elementVersionUses.setBrand(brand);
		elementVersionUses.setElementVersion(elementVersion);
		elementVersionUses.setActive(false);

		elementVersionUsersManager.saveObject(elementVersionUses);
		log.info("use the elementVersion: " + costElementDTO.getElementVersion().getElementVersionId() + " For Marke: "
				+ brand.getBrandId());

		// add change entry
		ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.USE_STAND.value(), "",
				brand.getBrandId(), costElementDTO.getElementVersion(), user, Boolean.FALSE, ChangeType.USE_STAND);

		costElementDTO.setElementVersionUsed(Boolean.TRUE);
		costElementDTO.setElementVersionCanBeUsed(Boolean.FALSE);

		// set the list of brand using an element version (Nutzer)
		costElementDTO.getElementVersion().getElementVersionUsers().add(elementVersionUses);
		costElementDTO.getElementVersion().getElement().getElementVersions().size();
		loadChangesForVersion(costElementDTO, elementVersion.getElementVersionId());
		return costElementDTO;
	}

	/**
	 * Set an Element version as unused for a brand save an entry in element version change
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostElementDTO saveElementVersionAsUnUsed(CostElementDTO costElementDTO) {

		UserModel user = costElementDTO.getUserLogged();
		ElementVersionModel elementVersion = costElementDTO.getElementVersion();

		String brandId = user.getDefaultBrand();

		ElementVersionUsersModel elementVersionUses = elementVersionUsersManager
				.getElementVersionUses(elementVersion.getElementVersionId(), brandId);

		if (elementVersionUses != null) {
			// load on demand
			// elementVersion.getComponentElements().size();
			List<ComponentElementModel> componentElements = costElementDTO.getComponentElementList();
			Set<String> relatedComponents = new HashSet<String>();
			if (!componentElements.isEmpty()) {
				for (ComponentElementModel componentElement : componentElements) {
					ComponentModel component = componentElement.getComponentStand().getComponentVersion()
							.getComponent();
					if (!elementVersion.getElement().getOwner().equals(component.getOwner())) {
						relatedComponents.add(component.getNumberAndDesignation());
					}
				}

				if (!relatedComponents.isEmpty()) {
					StringBuffer components = new StringBuffer();
					for (String name : relatedComponents) {
						components.append("<br/>");
						components.append(name);
					}
					costElementDTO.setRelatedComponents(components.toString());
					costElementDTO.setVersionUnused(Boolean.FALSE);
					return costElementDTO;
				}
			}
			// set the list of brand using an element version (Nutzer)
			costElementDTO.getElementVersion().getElementVersionUsers().remove(elementVersionUses);

			// removing the association
			elementVersionUsersManager.removeObject(elementVersionUses.getId());
			log.info("unuse the element Version: " + elementVersion.getElementVersionId() + " For Marke: " + brandId);

			costElementDTO.setElementVersionUsed(Boolean.FALSE);
			costElementDTO.setElementVersionCanBeUsed(Boolean.TRUE);

			// add change entry
			ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(), brandId,
					"", elementVersion, user, Boolean.FALSE, ChangeType.UNUSE_STAND);
			loadChangesForVersion(costElementDTO, elementVersion.getElementVersionId());
		}

		return costElementDTO;
	}

	// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
	/**
	 * @param dto
	 * @param isLocked
	 */
	public void saveElementVersionLockStatus(CostElementDTO dto, boolean isLocked) {
		ElementVersionModel version = dto.getElementVersion();
		version.setIsLocked(isLocked);
		// 11.11.2013: ZS: PTS_Requirement-26761: Set the user who lock the BS in the release process
		version.setModifier(dto.getUserLogged());
		elementVersionManager.saveObject(version);
		log.info("Lock staus is changed to: " + isLocked + " for version " + version);
	}

	/**
	 * return the list of ComponentElement associated to the Element version 20.11.2013: ZS: PTS_Requirement-22204: Sort
	 * the list by BS number to be group by this column in the mask
	 * 
	 * @param dto
	 * @return
	 */
	public List<ComponentElementModel> getComponentElementList(CostElementDTO dto) {
		return componentElementManager.getComponentElementListByElementVersionId(dto.getElementVersionId());
	}

	public CostElementDTO getChangeDetailByVersion(CostElementDTO dto) {
		List<ElementVersionChangeModel> changeDetails = ElementVersionChangeManagerImpl.get()
				.getChangeDetailByVersion(dto.getElementVersionId(), dto.getChangeToShow());

		dto.setChangeDetails(changeDetails);
		return dto;
	}

	/**
	 * method used to change display order of an element in its folder
	 * 
	 * @param objectId
	 *            ,
	 * @param folderId
	 *            ,
	 * @param actionToDo
	 */
	public void saveNewOrder(Long objectId, Long folderId, String actionToDo) {
		elementManager.performElementChangeOrder(objectId, folderId, actionToDo);
	}

	// start manager setter
	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	public void setElementManager(ElementManager elementManager) {
		this.elementManager = elementManager;
	}

	public void setElementValueManager(ElementValueManager elementValueManager) {
		this.elementValueManager = elementValueManager;
	}

	public void setEnumStatusManager(EnumStatusManager enumStatusManager) {
		this.enumStatusManager = enumStatusManager;
	}

	public void setElementCategoryManager(ElementCategoryManager elementCategoryManager) {
		this.elementCategoryManager = elementCategoryManager;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setDocumentPoolManager(DocumentPoolManager documentPoolManager) {
		this.documentPoolManager = documentPoolManager;
	}

	public void setHourlyRateCatalogManager(HourlyRateCatalogManager hourlyRateCatalogManager) {
		this.hourlyRateCatalogManager = hourlyRateCatalogManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setElementVersionUsersManager(ElementVersionUsersManager elementVersionUsesManager) {
		this.elementVersionUsersManager = elementVersionUsesManager;
	}

	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

	// end manager setter

}
