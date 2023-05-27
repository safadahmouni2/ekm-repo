/*
 * * Copyright (c) VW All Rights Reserved.
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.WorkbookUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentManager;
import vwg.vw.km.application.implementation.ComponentValueManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.ComponentVersionStandManager;
import vwg.vw.km.application.implementation.ComponentVersionUsersManager;
import vwg.vw.km.application.implementation.DocumentPoolManager;
import vwg.vw.km.application.implementation.ElementManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.ElementVersionUsersManager;
import vwg.vw.km.application.implementation.EnumComponentClassManager;
import vwg.vw.km.application.implementation.EnumComponentTypeManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.HourlyRateCatalogManager;
import vwg.vw.km.application.implementation.LibraryManager;
import vwg.vw.km.application.implementation.WorkAreaManager;
import vwg.vw.km.application.implementation.impl.ComponentStandChangeManagerImpl;
import vwg.vw.km.application.implementation.impl.ElementVersionChangeManagerImpl;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.CostCategory;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
import vwg.vw.km.integration.persistence.model.UserModel;

/**
 * <p>
 * Title: VW_KM
 * </p>
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: Externes Kalkulatorenmodul (c) 2012
 * </p>
 * 
 * @author saidi changed by $Author: saidi $
 * @version $Revision: 1.348 $ $Date: 2020/08/17 11:16:55 $
 */
public class CostComponentService extends BaseService<CostComponentDTO> {

	private final Log log = LogManager.get().getLog(CostComponentService.class);

	private ComponentVersionManager componentVersionManager;

	private ComponentManager componentManager;

	private ComponentValueManager componentValueManager;

	private EnumStatusManager enumStatusManager;

	private EnumComponentTypeManager enumComponentTypeManager;

	private EnumComponentClassManager enumComponentClassManager;
	
	private ComponentElementManager componentElementManager;

	private FolderManager folderManager;

	private DocumentPoolManager documentPoolManager;

	private ElementManager elementManager;

	private HourlyRateCatalogManager hourlyRateCatalogManager;

	private BrandManager brandManager;

	private LibraryManager libraryManager;

	private ElementVersionUsersManager elementVersionUsersManager;

	private ComponentVersionStandManager componentVersionStandManager;

	private ComponentVersionUsersManager componentVersionUsersManager;

	private ElementVersionManager elementVersionManager;

	private WorkAreaManager workAreaManager;

	/******************************** start transactional methods **/

	// start transactional methods
	/**
	 * save Cost component and all its dependency
	 */
	public CostComponentDTO save(CostComponentDTO costComponentDTO) {
		ComponentVersionModel componentVersion = costComponentDTO.getComponentVersion();
		ComponentModel component = componentVersion.getComponent();
		ComponentStandModel stand = costComponentDTO.getComponentStand();
		UserModel user = costComponentDTO.getUserLogged();
		if (componentVersion.isLoaded()) {
			component.setLoaded(Boolean.TRUE);
			component.setModifierRefId(user.getUserId());
		} else {
			// set Display order for new component
			setNewDisplayOrder(component.getFolder().getFolderId(), component);
		}
		// String imagePath = costComponentDTO.getImagePath();
		// if (costComponentDTO.getImagesRootPath() != null
		// && imagePath.startsWith(costComponentDTO.getImagesRootPath())) {
		// imagePath = imagePath.substring(costComponentDTO.getImagesRootPath().length());
		// }
		// if (imagePath.startsWith(File.separator)) {
		// imagePath = imagePath.substring(File.separator.length());
		// }
		// component.setImagePath(imagePath);
		componentVersion.setComponent(component);
		componentManager.saveObject(component);
		costComponentDTO.setImageUrl(new Date().getTime() + "");
		boolean componentVersionLoaded = componentVersion.isLoaded();
		if (!componentVersionLoaded) {
			componentVersion.setCreator(user);
			EnumStatusModel enumStatus = enumStatusManager.getObject(EnumStatusManager.Status.CREATED.value());
			componentVersion.setEnumStatus(enumStatus);
			costComponentDTO
					.setAllowedTransitions(enumStatusManager.getAllowedTransitions(enumStatus, Boolean.FALSE, null));
			BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
			// set validFrom validTo attributes
			componentVersion.setValidFrom(validFrom);
			componentVersion.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));
		} else {
			componentVersion.setModifier(user);
		}
		if (costComponentDTO.getEnumComponentTypeId() != null) {
			componentVersion.setEnumComponentType(
					enumComponentTypeManager.getObject(costComponentDTO.getEnumComponentTypeId()));
		}
		if (costComponentDTO.getEnumComponentClassId() != null) {
			componentVersion.setEnumComponentClass(
					enumComponentClassManager.getObject(costComponentDTO.getEnumComponentClassId()));
		}
		if (costComponentDTO.getComponentStand().getId() == null) {
			// save the component version
			componentVersionManager.saveObject(componentVersion);

			// add Component version to set in Component
			Set<ComponentVersionModel> componentVersions = new HashSet<ComponentVersionModel>();
			componentVersions.add(componentVersion);
			component.setComponentVersions(componentVersions);

			stand = new ComponentStandModel();
			stand.setStandDate(BaseDateTime.getCurrentDateTime());
			stand.setComponentVersion(componentVersion);
			componentVersionStandManager.saveObject(stand);
			costComponentDTO.setComponentStand(stand);
			// add the new stand to the set in component version
			Set<ComponentStandModel> componentStands = new HashSet<ComponentStandModel>();
			componentStands.add(stand);
			componentVersion.setComponentVersionStands(componentStands);
		}
		// save documents
		manageAssociatedDocuments(costComponentDTO);

		// save values changes
		componentVersionManager.saveWithCheckModifications(componentVersion, stand);
		componentValueManager.saveOrRemoveComponentValue(componentVersion);
		// add change entry when element is activated or deactivated
		if (component.getOldInactive() != null && !component.getInactive().equals(component.getOldInactive())) {
			if (component.getInactive()) {
				ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.DEACTIVATE.value(), "",
						"", stand, user, Boolean.FALSE, ChangeType.DEACTIVATE);
			} else {
				ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.ACTIVATE.value(), "", "",
						stand, user, Boolean.FALSE, ChangeType.ACTIVATE);
			}
		}
		component.setOldInactive(component.getInactive());

		// 28.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		if (component.getOldDesignation() != null
				&& !component.getDesignation().equals(component.getOldDesignation())) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_DESIGNATION.value(),
					component.getOldDesignation(), component.getDesignation(), stand, user, Boolean.FALSE,
					ChangeType.DESIGNATION, null, null, null);
		}
		component.setOldDesignation(component.getDesignation());

		if (component.getOldComponentNumber() != null
				&& !component.getComponentNumber().equals(component.getOldComponentNumber())) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_NUMBER.value(),
					component.getOldComponentNumber(), component.getComponentNumber(), stand, user, Boolean.FALSE,
					ChangeType.NUMBER, null, null, null);
		}
		component.setOldComponentNumber(component.getComponentNumber());

		manageAssociatedComponentElements(componentVersion, costComponentDTO);
		costComponentDTO.setComponentVersionId(componentVersion.getComponentVersionId());
		return costComponentDTO;
	}

	/**
	 * 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten Set new Object display order to Max
	 * in Folder
	 * 
	 * @param folderId
	 * @param component
	 */
	private void setNewDisplayOrder(Long folderId, ComponentModel component) {
		Long newObjectOrder = componentManager.getMaxDisplayOrderByFolderId(folderId) + 1;
		component.setDisplayOrder(newObjectOrder);
	}

	/**
	 * save all association between current component version and attached elements
	 * 
	 * @param componentVersion
	 * @param costComponentDTO
	 */
	private void manageAssociatedComponentElements(ComponentVersionModel componentVersion,
			CostComponentDTO costComponentDTO) {
		UserModel user = (componentVersion.getModifier() != null) ? componentVersion.getModifier()
				: componentVersion.getCreator();

		List<ComponentElementModel> viewCEs = costComponentDTO.getComponentVersion().getAvailableComponentElementList();
		List<ComponentElementModel> dBElements = componentElementManager
				.getComponentElementListByComponentStandId(costComponentDTO.getComponentStand().getId());
		insertOrUpdateCEList(viewCEs, dBElements, costComponentDTO.getComponentStand(), user);
		removeCEList(viewCEs, dBElements, componentVersion, user, costComponentDTO);
	}

	/**
	 * insert or update ComponentElementModel list
	 * 
	 * @param viewCEs
	 * @param dBCEs
	 * @param version
	 * @param user
	 */
	private void insertOrUpdateCEList(List<ComponentElementModel> viewCEs, List<ComponentElementModel> dBCEs,
			ComponentStandModel stand, UserModel user) {
		Long displayOrder = 1L;
		for (ComponentElementModel viewCE : viewCEs) {
			boolean exist = Boolean.FALSE;
			if (viewCE.getNumber() == null) {
				viewCE.setNumber(1L);
			}
			for (ComponentElementModel dbCE : dBCEs) {
				if (viewCE.getElementVersion().equals(dbCE.getElementVersion())) {
					dbCE.setNumber(viewCE.getNumber());
					dbCE.setDisplayOrder(displayOrder);
					// to have same object
					viewCE = dbCE;
					// update entry in DB
					componentElementManager.saveObject(dbCE);
					exist = Boolean.TRUE;
					break;
				}
			}
			if (!exist) {
				// insert new in DB
				viewCE.setComponentStand(stand);
				viewCE.setDisplayOrder(displayOrder);
				componentElementManager.saveObject(viewCE);
				// Elemente Zuordnung, Nutzerkennung setzen
				// saveElementVersionAsUsed(viewCE, user);

				// add change entry
				ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ADD_ELEMENT.value(), "",
						viewCE.getElementVersion().getElement().getNumberAndDesignation(), stand, user, Boolean.FALSE,
						ChangeType.ADD_ELEMENT, null, null, null);
			}
			displayOrder = displayOrder + 1;
		}
	}

	/**
	 * remove list of ComponentElementModel
	 * 
	 * @param viewCEs
	 * @param dBCEs
	 * @param version
	 * @param user
	 */
	private void removeCEList(List<ComponentElementModel> viewCEs, List<ComponentElementModel> dBCEs,
			ComponentVersionModel version, UserModel user, CostComponentDTO costComponentDTO) {
		for (ComponentElementModel cElementModel : dBCEs) {
			boolean exist = Boolean.FALSE;
			for (ComponentElementModel newElementModel : viewCEs) {
				if (cElementModel.getElementVersion().equals(newElementModel.getElementVersion())) {
					exist = Boolean.TRUE;
					break;
				}
			}
			if (!exist) {
				saveElementVersionAsUnUsed(cElementModel, user);
				componentElementManager.removeObject(cElementModel.getComponentElementId());
				// add change entry
				ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.REMOVE_ELEMENT.value(),
						cElementModel.getElementVersion().getElement().getNumberAndDesignation(), null,
						costComponentDTO.getComponentStand(), user, Boolean.FALSE, ChangeType.REMOVE_ELEMENT, null,
						null, null);
			}
		}
	}

	/**
	 * load component version detail and all its dependencies
	 * 
	 * @param costComponentDTO
	 * @param catalogId
	 * @param statusElementPublish
	 * @return
	 */
	private CostComponentDTO loadDetail(CostComponentDTO costComponentDTO, Long catalogId,
			Boolean statusElementPublish) {
		ComponentVersionModel cmpVer = costComponentDTO.getComponentVersion();
		cmpVer.setStatusElementPublish(statusElementPublish);
		cmpVer.setCatalogModel(hourlyRateCatalogManager.getObject(catalogId));
		cmpVer.getCatalogModel().getHourlyRates().size();
		// load dependencies to element version
		// TO-DO load detail not necessary for not detail node
		if (costComponentDTO.getComponentStand() != null) {
			componentVersionManager.loadDependencies(costComponentDTO.getComponentStand());
			calculate(costComponentDTO);
		}
		// list to be used in search
		filterComponentValues(costComponentDTO);
		// cached list from manager to DTO
		loadDocuments(costComponentDTO);
		// load element category from DB
		costComponentDTO.setComponentTypes(enumComponentTypeManager.getObjects());
		costComponentDTO.setComponentClasses(enumComponentClassManager.getObjects());
		// load allowed transitions
		// load data from db on demand
		List<ComponentVersionModel> componentVersions = costComponentDTO.getVersionHistoryList();
		ComponentVersionModel componentVersion = costComponentDTO.getComponentVersion();
		if (componentVersion != null) {
			List<Long> withoutToStatus = new ArrayList<Long>();
			if (!componentVersions.isEmpty()) {
				ComponentVersionModel lastVersion = componentVersions.get(0);
				if (!componentVersion.equals(lastVersion)) {
					withoutToStatus.add(EnumStatusManager.Status.IN_PROGRESS.value());
				}
			}
			List<EnumTransitionModel> transitions = enumStatusManager
					.getAllowedTransitions(componentVersion.getEnumStatus(), Boolean.FALSE, withoutToStatus);
			costComponentDTO.setAllowedTransitions(transitions);

			if (costComponentDTO.getComponentStand() != null) {
				checkComponentStandUse(costComponentDTO);
			}
			loadChangesForStand(costComponentDTO);
		}
		// load value o image_root_path from user workarea if it is not yet defined
		// UserModel loggedUser = costComponentDTO.getUserLogged();
		// String rootPath = "";
		// if ((loggedUser.getImagesRootPath() == null || "".equals(loggedUser.getImagesRootPath()))
		// && (loggedUser.getDefaultBrand() != null && !"".equals(loggedUser.getDefaultBrand()))) {
		// WorkAreaModel userWorkArea = workAreaManager.getWorkAreaByBrand(loggedUser.getDefaultBrand());
		// if (userWorkArea != null && userWorkArea.getImagesRootPath()!=null) {
		// rootPath = userWorkArea.getImagesRootPath();
		//
		// }
		// } else if (loggedUser.getImagesRootPath() != null) {
		// rootPath = loggedUser.getImagesRootPath();
		// }
		// costComponentDTO.setImagesRootPath(rootPath);
		// String imagePath = costComponentDTO.getComponentVersion().getComponent().getImagePath();
		// if (imagePath != null && !"".equals(imagePath)) {
		// costComponentDTO.setImagePath(rootPath +File.separator+ imagePath);
		// }
		// costComponentDTO.setImageUrl(new Date().getTime() + "");
		return costComponentDTO;
	}

	/**
	 * Load version detail for export purpose
	 * 
	 * @param costComponentDTO
	 * @param catalogId
	 * @param statusElementPublish
	 * @return
	 */
	private CostComponentDTO loadDetailForExport(CostComponentDTO costComponentDTO, Long catalogId,
			Boolean statusElementPublish) {
		ComponentVersionModel cmpVer = costComponentDTO.getComponentVersion();
		cmpVer.setStatusElementPublish(statusElementPublish);
		cmpVer.setCatalogModel(hourlyRateCatalogManager.getObject(catalogId));
		cmpVer.getCatalogModel().getHourlyRates().size();
		// load dependencies to element version
		// TO-DO load detail not necessary for not detail node
		if (costComponentDTO.getComponentStand() != null) {
			componentVersionManager.loadDependencies(costComponentDTO.getComponentStand());
			calculate(costComponentDTO);
		}

		return costComponentDTO;
	}

	/**
	 * save all associated documents for current component version
	 * 
	 * @param costComponentDTO
	 */
	private void manageAssociatedDocuments(CostComponentDTO costComponentDTO) {
		List<DocumentPoolModel> documentsToBeSaved = costComponentDTO.getDocumentList();
		if (documentsToBeSaved != null && !documentsToBeSaved.isEmpty()) {
			for (DocumentPoolModel document : documentsToBeSaved) {
				if (document.getDocumentPoolId() == null) {
					documentPoolManager.saveObject(document);
				}
			}
		}
		costComponentDTO.getComponentVersion()
				.setDocuments(new HashSet<DocumentPoolModel>(costComponentDTO.getDocumentList()));
		// TO-DO remove document when no associated
	}

	/**
	 * calculate fields after changing component value
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO calculate(CostComponentDTO costComponentDTO) {
		componentVersionManager.calculate(costComponentDTO.getComponentVersion());
		return costComponentDTO;
	}

	/**
	 * calculate aggregated cost attributes for export purpose
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	private CostComponentDTO calculateAggregatedCostAttributeValues(CostComponentDTO costComponentDTO) {
		ComponentVersionModel cmpVer = costComponentDTO.getComponentVersion();
		cmpVer.setAggregatedComponentValuesForView(new ArrayList<ComponentValueModel>());
		for (ComponentValueModel cmpValue : cmpVer.getComponentValuesForView()) {
			ComponentValueModel aggregated = new ComponentValueModel();
			// clone all properties
			aggregated.setComponentVersion(cmpValue.getComponentVersion());
			aggregated.setCostAttribute(cmpValue.getCostAttribute());
			aggregated.setDescription(cmpValue.getDescription());
			aggregated.setAmount(cmpValue.getAmount());
			aggregated.setNumber(cmpValue.getNumber());
			aggregated.setRateModel(cmpValue.getRateModel());
			aggregated.setTotalAmount(cmpValue.getTotalAmount());
			// add sum of attached elements
			addSumAttachedElements(aggregated, cmpVer, cmpValue.getCostAttribute());
			cmpVer.getAggregatedComponentValuesForView().add(aggregated);
		}
		return costComponentDTO;
	}

	/**
	 * Add the sum of attached Elements
	 * 
	 * @param aggregated
	 * @param cmpVer
	 * @param costAttribute
	 */
	private void addSumAttachedElements(ComponentValueModel aggregated, ComponentVersionModel cmpVer,
			CostAttributeModel costAttribute) {
		// add sum of attached elements
		for (ComponentElementModel cmpElt : cmpVer.getAvailableComponentElementList()) {
			ElementValueModel eltValue = cmpElt.getElementVersion().getElementValueByCostAttribute(costAttribute);
			Long elementUnits = cmpElt.getNumber();
			if (eltValue != null) {
				if (eltValue.getCostAttribute().getPricePerUnit()) {
					BaseBigDecimal valueElement = eltValue.getNumber();
					if (valueElement != null) {
						valueElement = valueElement.multiply(elementUnits);
						if (aggregated.getNumber() != null) {
							aggregated.setNumber(aggregated.getNumber().add(valueElement));
						} else {
							aggregated.setNumber(valueElement);
						}
					}
				} else {
					BaseBigDecimal valueElement = eltValue.getAmount();
					if (valueElement != null) {
						valueElement = valueElement.multiply(elementUnits);
						if (aggregated.getAmount() != null) {
							aggregated.setAmount(aggregated.getAmount().add(valueElement));
						} else {
							aggregated.setAmount(valueElement);
						}
					}
				}
			}
		}
	}

	/**
	 * Filter component values
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO filterComponentValues(CostComponentDTO costComponentDTO) {
		costComponentDTO.setFilteredComponentValues(componentValueManager.filterComponentValues(
				costComponentDTO.getComponentVersion().getComponentValuesForView(),
				costComponentDTO.getUserLogged().getDisplayFilter()));
		return costComponentDTO;
	}

	/**
	 * put component version list documents in list in memory
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadDocuments(CostComponentDTO costComponentDTO) {
		List<DocumentPoolModel> objectDocumentList = new ArrayList<>();
		if (costComponentDTO.getComponentVersion() != null) {
			objectDocumentList.addAll(costComponentDTO.getComponentVersion().getDocuments());
		}

		costComponentDTO.setDocumentList(objectDocumentList);
		return costComponentDTO;
	}

	/**
	 * load last version by component
	 * 
	 * @param costComponentDTO
	 * @param lastVersion
	 * @return
	 */
	private ComponentVersionModel loadLastComponentVersionByComponent(Long componentId, boolean lastVersion) {
		ComponentVersionModel componentVersion = null;
		if (!lastVersion) {
			componentVersion = componentVersionManager.getLastComponentVersionInUse(componentId);
		}
		if (componentVersion == null) {
			componentVersion = componentVersionManager.getLastComponentVersionByComponentId(componentId);
		}
		return componentVersion;
	}

	/**
	 * delete current component version model
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO deleteVersion(CostComponentDTO costComponentDTO) {
		Long versionId = costComponentDTO.getComponentVersionId();
		ComponentVersionModel componentVersion = componentVersionManager.getObject(versionId);
		if (componentVersion != null) {
			// remove element when no element version attached
			List<Long> componentVersionAttachedIds = componentVersionManager
					.getComponentVersionAttachedIds(componentVersion.getComponent().getComponentId());
			if (componentVersionAttachedIds != null) {
				if (componentVersionAttachedIds.size() == 1) {
					// remove only element and with cascade remove element version
					if (componentVersionAttachedIds.contains(versionId)) {
						// 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten
						// Decrease display order for folder components after component delete
						Long componentId = componentVersion.getComponent().getComponentId();
						Long oldFolderId = componentVersion.getComponent().getFolder().getFolderId();
						componentManager.removeObject(componentId);
						costComponentDTO.setComponentDeleted(Boolean.TRUE);
						componentManager.updateDisplayOrdersForOtherNodes(componentId, oldFolderId);
					}
				} else {// remove only element version
					componentVersionManager.removeObject(componentVersion.getComponentVersionId());
				}
			}
		}
		return costComponentDTO;
	}

	/**
	 * load component version and component detail for stand detail purpose
	 * 
	 * @param costComponentDTO
	 * @param objectId
	 * @return CostComponentDTO
	 */
	public CostComponentDTO loadDTOForStand(CostComponentDTO costComponentDTO, Long objectId) {
		ComponentStandModel componentStand = componentVersionStandManager.getObject(objectId);
		if (componentStand != null) {
			costComponentDTO.setComponentStand(componentStand);
			Long folderId = null;
			if (componentStand.getComponentVersion().getComponent().getFolder() != null) {
				folderId = componentStand.getComponentVersion().getComponent().getFolder().getFolderId();
				costComponentDTO.setFolderId(folderId);
			}
			loadDTOForDetail(costComponentDTO, componentStand.getComponentVersion().getComponentVersionId(), folderId,
					"STAND", Boolean.TRUE);
		}
		return costComponentDTO;
	}

	/**
	 * load component version and component detail for in edit version purpose
	 * 
	 * @param costComponentDTO
	 * @param objectId
	 * @return CostComponentDTO
	 */
	public CostComponentDTO loadDTOForInProcessVersion(CostComponentDTO costComponentDTO, Long objectId) {
		ComponentVersionModel componentVersion = componentVersionManager.getObject(objectId);
		if (componentVersion != null) {
			componentVersion.getComponentVersionStands().size();
			loadDTOForStand(costComponentDTO,
					new ArrayList<ComponentStandModel>(componentVersion.getComponentVersionStands()).get(0).getId());
		}
		return costComponentDTO;
	}

	/**
	 * load component version and component detail
	 * 
	 * @param costComponentDTO
	 * @param objectId
	 * @param folderId
	 * @param isComponent
	 * @param lastVersion
	 * @return
	 */
	public CostComponentDTO loadDTOForDetail(CostComponentDTO costComponentDTO, Long objectId, Long folderId,
			String componentPart, boolean lastVersion) {
		// set current object id by given param
		if ("COMPONENT".equalsIgnoreCase(componentPart)) {
			costComponentDTO.setComponentId(objectId);
			costComponentDTO.setComponentVersionId(null);
		} else {
			costComponentDTO.setComponentVersionId(objectId);
		}
		// if object in DB load it else create a new one
		if (objectId != null) {
			ComponentVersionModel componentVersion = null;
			if ("COMPONENT".equalsIgnoreCase(componentPart)) {
				componentVersion = loadLastComponentVersionByComponent(costComponentDTO.getComponentId(), lastVersion);
			} else {
				componentVersion = componentVersionManager.getObject(costComponentDTO.getComponentVersionId());
			}
			if (componentVersion != null) {
				costComponentDTO.setComponentId(componentVersion.getComponent().getComponentId());
				costComponentDTO.setComponentVersionId(componentVersion.getComponentVersionId());
				if (componentVersion.getEnumComponentType() != null) {
					costComponentDTO
							.setEnumComponentTypeId(componentVersion.getEnumComponentType().getEnumComponentTypeId());
				}
				if (componentVersion.getEnumComponentClass() != null) {
					costComponentDTO.setEnumComponentClassId(
							componentVersion.getEnumComponentClass().getEnumComponentClassId());
				}
			}
			if (!"STAND".equalsIgnoreCase(componentPart)) {
				ComponentStandModel componentStand = null;
				if (componentVersion != null) {
					componentStand = new ArrayList<ComponentStandModel>(componentVersion.getComponentVersionStands())
							.get(0);
				}
				costComponentDTO.setComponentStand(componentStand);
			}
		} else {
			costComponentDTO.setComponentId(null);
			costComponentDTO.setComponentVersionId(null);
			costComponentDTO.setEnumComponentTypeId(null);
			costComponentDTO.setEnumComponentClassId(null);
			ComponentVersionModel tmpComponentVersion = new ComponentVersionModel();
			ComponentModel component = new ComponentModel();
			component.setFolder(folderManager.getObject(folderId));
			component.setInactive(Boolean.FALSE);

			BrandModel owner = brandManager.getObject(costComponentDTO.getUserLogged().getDefaultBrand());
			component.setOwner(owner);
			// set the new library from the user
			LibraryModel lib = libraryManager.getObject(costComponentDTO.getUserLogged().getDefaultLibrary());
			component.setLibrary(lib);

			tmpComponentVersion.setComponent(component);
			tmpComponentVersion.setEnumStatus(enumStatusManager.getObject(EnumStatusManager.Status.NEW.value()));
			tmpComponentVersion.setWithProvision(Boolean.FALSE);
			ComponentStandModel componentStand = new ComponentStandModel();
			componentStand.setComponentVersion(tmpComponentVersion);
			costComponentDTO.setComponentStand(componentStand);
		}
		Long defaultCatalogId = HourlyRateCatalogManager.DEFAULT_CATALOG_ID;
		if (costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId() != null) {
			defaultCatalogId = costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId();
		}

		if (costComponentDTO.getComponentStand() != null) {
			loadDetail(costComponentDTO, defaultCatalogId, costComponentDTO.getUserLogged().getStatusElementPublish());
		}
		return costComponentDTO;
	}

	/**
	 * load component list
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadDTOForList(CostComponentDTO costComponentDTO) {
		costComponentDTO.setComponents(
				getComponentsByFolder(costComponentDTO.getFolderId(), costComponentDTO.getUserLogged().getUserId()));
		return costComponentDTO;
	}

	/**
	 * return list of components in given folder that satisfy conditions
	 * 
	 * @param folderId
	 *            folders of components
	 * @param loggedUser
	 *            if != null return all "created" components only for given user
	 * @param inUse
	 *            if != null return only "in use" components
	 * @return
	 */
	public List<ComponentModel> getComponentsByFolder(Long folderId, Long loggedUser) {
		return componentManager.getComponentsByFolder(folderId, loggedUser, Boolean.FALSE);
	}

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param userBrand
	 *            : user default Brand
	 * @return bausteinIds:list of components to be filtered
	 */

	public List<Long> getComponentsListToBeFiltred(Long folderId, String userBrand) {
		return componentManager.getComponentsListToBeFiltred(folderId, userBrand);
	}

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param ownerFilters
	 *            : owner brands to filter by
	 * @param folderId
	 *            : parent folder id
	 * @param bausteinIds
	 *            :list of components to be filtered
	 * @return
	 */

	public boolean getHasVisibleComponentsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> bausteinIds) {
		return componentManager.hasVisibleComponentsByOwnerFilter(ownerFilters, folderId, bausteinIds);
	}

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @param usersFilters
	 *            : user brands to filter by
	 * @param folderId
	 *            : parent folder id
	 * @param bausteinIds
	 *            :list of components to be filtered
	 * @return
	 */
	public boolean getHasVisibleComponentsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> bausteinIds) {
		return componentManager.hasVisibleComponentsByUsersFilter(usersFilters, folderId, bausteinIds);
	}

	/**
	 * Check if a folder has components
	 * 
	 * @param folderId
	 * @return
	 */
	public boolean getHasComponents(Long folderId) {
		return componentManager.getHasComponents(folderId);
	}

	/**
	 * Return the list of inactive component id's of a given folder
	 * 
	 * @param folderId
	 * @return
	 */
	public List<Long> getInactiveComponentIds(Long folderId) {
		return componentManager.getInactiveComponentIds(folderId);
	}

	/**
	 * Load component list for different search folder
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadDTOForListByStatus(CostComponentDTO costComponentDTO) {
		Long loggedUserId = null;
		if (costComponentDTO.getUserLogged() != null) {
			loggedUserId = costComponentDTO.getUserLogged().getUserId();
		}
		costComponentDTO.setComponents(
				componentManager.getComponentsByStatus(costComponentDTO.getSearchStatus(), loggedUserId));
		return costComponentDTO;
	}

	/**
	 * Load component list for working folder
	 * 
	 * @param costComponentDTO
	 * @param userId
	 * @param folderId
	 * @return
	 */
	public CostComponentDTO loadDTOForWorkingFolder(CostComponentDTO costComponentDTO, Long userId, Long folderId) {
		costComponentDTO.setComponents(
				componentManager.getComponentsForWorkingFolder(costComponentDTO.getSearchStatus(), userId, folderId));
		return costComponentDTO;
	}

	/**
	 * Load inactive component list
	 * 
	 * @param folderId
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadDTOForInactiveList(Long folderId, CostComponentDTO costComponentDTO) {
		Long loggedUser = null;
		if (costComponentDTO.getUserLogged() != null) {
			loggedUser = costComponentDTO.getUserLogged().getUserId();
		}
		costComponentDTO.setComponents(componentManager.getInactiveComponentsByFolder(folderId, loggedUser));
		return costComponentDTO;
	}

	/**
	 * @see vwg.vw.km.application.service.base.BaseService#loadDTOForListBySearchString(vwg.vw.km.application.service.dto.base.BaseDTO)
	 */
	@Override
	public CostComponentDTO loadDTOForListBySearchString(CostComponentDTO costComponentDTO) {

		// 24.10.2013: ZS: PTS_Requirement-22210: Search folders
		List<FolderModel> folderResultList = folderManager.getFoldersBySearchString(costComponentDTO.getFolderId(),
				costComponentDTO.getSearchString(), EnumObjectTypeManager.COMPONENT_ID);
		costComponentDTO.setFolders(folderResultList);

		List<ComponentModel> resultList = componentManager.getComponentsBySearchString(costComponentDTO.getFolderId(),
				costComponentDTO.getUserLogged().getUserId(), costComponentDTO.getSearchString());
		costComponentDTO.setComponents(resultList);
		costComponentDTO.setNoSearchResult(costComponentDTO.isNoComponents());
		return costComponentDTO;
	}

	/**
	 * Load changed component value
	 * 
	 * @param costComponentDTO
	 * @param componentValueIndex
	 * @param newValue
	 * @return
	 */
	public CostComponentDTO loadChangedComponentValue(CostComponentDTO costComponentDTO, int componentValueIndex,
			BaseBigDecimal newValue) {
		// 1-update one element in table
		// get list from DTO
		List<ComponentValueModel> filtredComponents = costComponentDTO.getFilteredComponentValues();
		// get element from list using given index
		ComponentValueModel componentValue = filtredComponents.get(componentValueIndex);
		// if price per unit is true calculate total amount else
		if (componentValue.getCostAttribute().getPricePerUnit()) {
			componentValue.setNumber(newValue);
		} else {
			componentValue.setAmount(newValue);
		}
		// 2-update object in overview Tab
		calculate(costComponentDTO);
		return costComponentDTO;
	}

	/**
	 * Load changed component element number value
	 * 
	 * @param costComponentDTO
	 * @param componentElementIndex
	 * @param newValue
	 * @return
	 */
	public CostComponentDTO loadChangedComponentElementNumberValue(CostComponentDTO costComponentDTO,
			int componentElementIndex, Long newValue) {
		// 1-update one element in table
		// get list from DTO
		List<ComponentElementModel> componentElements = costComponentDTO.getComponentVersion()
				.getAvailableComponentElementList();
		// get component element from list using given index
		ComponentElementModel componentElement = componentElements.get(componentElementIndex);
		componentElement.setNumber(newValue);
		// 2-update object in overview Tab
		calculate(costComponentDTO);
		return costComponentDTO;
	}

	/**
	 * Update assigned element version position order in the list
	 * 
	 * @param costComponentDTO
	 * @param componentElementIndex
	 * @param orderAction
	 * @return CostComponentDTO
	 */
	public CostComponentDTO loadChangedComponentElementOrder(CostComponentDTO costComponentDTO,
			int componentElementIndex, String orderAction) {
		List<ComponentElementModel> componentElements = costComponentDTO.getComponentVersion()
				.getAvailableComponentElementList();
		if ("TOP".equals(orderAction)) {
			for (int i = componentElementIndex; i > 0; i--) {
				Collections.swap(componentElements, i - 1, i);
			}
		}
		if ("BOTTOM".equals(orderAction)) {
			for (int i = componentElementIndex; i < componentElements.size() - 1; i++) {
				Collections.swap(componentElements, i, i + 1);
			}
		}
		if ("UP".equals(orderAction)) {
			if (componentElementIndex > 0) {
				Collections.swap(componentElements, componentElementIndex - 1, componentElementIndex);
			}
		}
		if ("DOWN".equals(orderAction)) {
			if (componentElementIndex < componentElements.size() - 1) {
				Collections.swap(componentElements, componentElementIndex, componentElementIndex + 1);
			}
		}
		return costComponentDTO;
	}

	/**
	 * Load a component version from DB
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public ComponentVersionModel loadComponentVersion(CostComponentDTO costComponentDTO) {
		ComponentVersionModel version = componentVersionManager
				.getObject(costComponentDTO.getComponentVersion().getComponentVersionId());
		return version;
	}

	/**
	 * Save a component to a folder
	 * 
	 * @param componentId
	 * @param folderId
	 * @return
	 */
	public Boolean saveComponentToFolder(Long componentId, Long folderId) {
		ComponentModel e = componentManager.getObject(componentId);
		Long oldFolderId = e.getFolder().getFolderId();
		e.setFolder(folderManager.getObject(folderId));

		/*
		 * 12/11/2013:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten Set display order to Max order in
		 * new folder for pasted Object
		 */
		setNewDisplayOrder(folderId, e);
		componentManager.saveObject(e);
		componentManager.updateDisplayOrdersForOtherNodes(componentId, oldFolderId);
		return Boolean.TRUE;
	}

	/**
	 * Release component version and release all owned and assigned working element versions
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO saveReleaseComponentVersion(CostComponentDTO costComponentDTO) {
		UserModel user = costComponentDTO.getUserLogged();
		ComponentVersionModel e = costComponentDTO.getComponentVersion();
		EnumStatusModel toStatus = enumStatusManager.getObject(EnumStatusManager.Status.APPROVED.value());
		e.setReleaser(user);
		e.setModifier(user);
		e.setEnumStatus(toStatus);

		if (e.getComment() != null) {
			if (costComponentDTO.getChangeStatusComment() != null
					&& !"".equals(costComponentDTO.getChangeStatusComment())) {
				e.setComment(e.getComment()
						.concat(costComponentDTO.REMARKS_SEPARATOR + costComponentDTO.getChangeStatusComment()));
			}
		} else {
			e.setComment(costComponentDTO.getChangeStatusComment());
		}
		componentVersionManager.saveWithCheckModifications(e, costComponentDTO.getComponentStand());

		List<ElementVersionModel> workingElementVersions = costComponentDTO.getWorkingElementVersions();
		for (ElementVersionModel elementVersion : workingElementVersions) {
			elementVersion.setLoaded(true);
			elementVersion.setEnumStatus(toStatus);
			elementVersion.setReleaser(user);
			elementVersion.setModifier(user);
			elementVersionManager.saveWithCheckModifications(elementVersion);
		}
		costComponentDTO
				.setAllowedTransitions(enumStatusManager.getAllowedTransitions(e.getEnumStatus(), Boolean.FALSE, null));
		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
		// unlock the version
		saveComponentVersionLockStatus(costComponentDTO, false);

		return costComponentDTO;
	}

	/**
	 * Change status of version from current one to selected one if from status = IN_USE a new version model is created
	 * 
	 * @param costComponentDTO
	 */
	public String saveNewStatus(CostComponentDTO costComponentDTO) {
		ComponentVersionModel e = costComponentDTO.getComponentVersion();
		Long fromStatusId = e.getEnumStatus().getEnumStatusId();
		EnumStatusModel toStatus = enumStatusManager.getObject(costComponentDTO.getToStatusId());
		// check if creation of new version by the new status is allowed
		boolean isLastVersion = componentVersionManager.isTheLastComponentVersion(e.getComponentVersionId(),
				e.getComponent().getComponentId(), toStatus.getEnumStatusId());
		UserModel user = costComponentDTO.getUserLogged();

		if (!isLastVersion) {
			log.info("try to create another component version denied");
			return "VERSION_CREATION_DENIED";
		} else {

			ComponentModel component = e.getComponent();
			if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
					&& toStatus.getEnumStatusId().equals(EnumStatusManager.Status.APPROVED.value())) {

				// check if the component is in the regular structure
				if (component.getFolder().getCreatorRefId() != null) {
					return "VERSION_NOT_RELEASED";
				}

				// BrandModel userBrand = getDefaultBrand(user.getDefaultBrand());
				// check if there are element versions assigned to the component version that are still in edit status
				List<ComponentElementModel> assignedComponentElements = e.getAvailableComponentElementList();
				List<ElementVersionModel> inEditElements = new ArrayList<ElementVersionModel>();
				Set<ElementVersionModel> inEditForeignBrandElements = new HashSet<ElementVersionModel>();
				for (ComponentElementModel componentElement : assignedComponentElements) {
					ElementVersionModel elementVersion = componentElement.getElementVersion();

					if (!EnumStatusManager.Status.IN_USE.value()
							.equals(elementVersion.getEnumStatus().getEnumStatusId())
							&& !EnumStatusManager.Status.APPROVED.value()
									.equals(elementVersion.getEnumStatus().getEnumStatusId())) {

						if (costComponentDTO.getUserLogged().getDefaultBrand() != null && !elementVersion.getElement()
								.getOwner().getBrandId().equals(user.getDefaultBrand())) {
							inEditForeignBrandElements.add(elementVersion);
						} else {
							inEditElements.add(elementVersion);
						}
					}
				}
				if (!inEditForeignBrandElements.isEmpty()) {
					Set<String> elements = new HashSet<String>();
					StringBuffer components = new StringBuffer();
					for (ElementVersionModel version : inEditForeignBrandElements) {
						elements.add(version.getElement().getNumberAndDesignation());
						ElementVersionUsersModel elementVersionUsers = elementVersionUsersManager
								.getElementUsers(version.getElement().getElementId(), user.getDefaultBrand());
						if (elementVersionUsers != null) {
							ElementVersionModel lastElementVersionInUse = elementVersionUsers.getElementVersion();
							lastElementVersionInUse.getComponentElements().size();
							Set<String> relatedComponents = new HashSet<String>();
							for (ComponentElementModel fbComponentElement : lastElementVersionInUse
									.getComponentElements()) {
								relatedComponents.add(fbComponentElement.getComponentStand().getComponentVersion()
										.getComponent().getNumberAndDesignation());
							}
							components.append(relatedComponents.toString());
						}
					}
					costComponentDTO.setInEditElements(elements.toString());
					if (components.length() > 2) {
						costComponentDTO.setComponentsTobeAffected(components.toString());
					}
					return "ASSIGN_FB_ELEMENT_NOT_RELEASED";
				}

				if (!inEditElements.isEmpty()) {
					Set<String> elements = new HashSet<String>();
					StringBuffer components = new StringBuffer();
					for (ElementVersionModel version : inEditElements) {
						elements.add(version.getElement().getNumberAndDesignation());

						if (!version.getProcessor().equals(e.getProcessor())) {
							String errorMessage = "Element " + version.getElement().getNumberAndDesignation()
									+ " wird von Benutzer " + version.getProcessor().getFullName()
									+ " bearbeitet. Eine Freigabe kann nicht erfolgen.";
							costComponentDTO.setReleaseWorkingElErrorMessage(errorMessage);
							return "ASSIGN_LOCKED_ELEMENT_NOT_RELEASED";
						}
						ElementVersionModel lastElementVersionInUse = elementVersionManager
								.getLastElementVersionByStatus(version.getElement().getElementId(),
										EnumStatusManager.Status.IN_USE.value());
						if (lastElementVersionInUse != null) {
							lastElementVersionInUse.getComponentElements().size();
							Set<String> relatedComponents = new HashSet<String>();
							for (ComponentElementModel componentElementInUse : lastElementVersionInUse
									.getComponentElements()) {
								relatedComponents.add(componentElementInUse.getComponentStand().getComponentVersion()
										.getComponent().getNumberAndDesignation());
							}
							components.append(relatedComponents.toString());
						}
					}

					costComponentDTO.setInEditElements(elements.toString());
					// avoid empty string buffer
					if (components.length() > 2) {
						costComponentDTO.setComponentsTobeAffected(components.toString());
					}
					costComponentDTO.setWorkingElementVersions(inEditElements);
					return "ASSIGN_ELEMENT_NOT_RELEASED";
				}

				for (ComponentElementModel assignedComponentElement : assignedComponentElements) {
					saveElementVersionAsUsed(assignedComponentElement, user);
				}
			}

			if (e.getComment() != null) {
				if (costComponentDTO.getChangeStatusComment() != null
						&& !"".equals(costComponentDTO.getChangeStatusComment())) {
					e.setComment(e.getComment()
							.concat(costComponentDTO.REMARKS_SEPARATOR + costComponentDTO.getChangeStatusComment()));
				}
			} else {
				e.setComment(costComponentDTO.getChangeStatusComment());
			}

			if (fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())
					&& costComponentDTO.getToStatusId().equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(e.getDocuments());
				// empty all relations
				e.setDocuments(null);
				e.setComponentValues(null);
				e.setComponentVersionStands(null);
				// clone
				ComponentVersionModel cloned = new ComponentVersionModel();
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
				componentVersionManager.saveObject(cloned);
				// copy element values
				componentValueManager.copyComponentValues(e, cloned);
				costComponentDTO.setComponentVersionId(cloned.getComponentVersionId());

				// save new a stand for the version
				ComponentStandModel stand = new ComponentStandModel();
				stand.setStandDate(BaseDateTime.getCurrentDateTime());
				stand.setComponentVersion(cloned);
				componentVersionStandManager.saveObject(stand);
				// TO-DO copy component elements
				componentElementManager.copyComponentElements(costComponentDTO.getComponentStand(), stand);
				costComponentDTO.setComponentStand(stand);
			} else {
				if (costComponentDTO.getToStatusId().equals(EnumStatusManager.Status.APPROVED.value())) {
					e.setReleaser(user);
					// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
					// unlock the version
					saveComponentVersionLockStatus(costComponentDTO, false);
				}
				e.setModifier(user);
				e.setEnumStatus(toStatus);
				componentVersionManager.saveWithCheckModifications(e, costComponentDTO.getComponentStand());
			}
			return "";
		}
	}

	/**
	 * Load all the versions of an element to be assign
	 * 
	 * @param costComponentDTO
	 * @return costComponentDTO
	 */
	public CostComponentDTO loadElementVersionListToAssign(CostComponentDTO costComponentDTO) {
		List<ElementModel> selectedElements = costComponentDTO.getSelectedElements();
		for (Iterator<ElementModel> iterator = selectedElements.iterator(); iterator.hasNext();) {
			ElementModel elementModel = iterator.next();
			costComponentDTO.setSelectedElementModel(elementModel);
			log.info("Try to add Element: " + elementModel);
			loadElementVersionToAssign(costComponentDTO);
			if (costComponentDTO.getElementVersionsToAssign() != null
					&& costComponentDTO.getElementVersionsToAssign().size() > 1) {
				costComponentDTO.setShowElementVersionChoicePopup(Boolean.TRUE);
				return costComponentDTO;
			}
			iterator.remove();
		}
		// after assigning all elements calculate
		if (selectedElements.isEmpty()) {
			calculate(costComponentDTO);
		}
		return costComponentDTO;
	}

	/**
	 * Load element version to be assign
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadElementVersionToAssign(CostComponentDTO costComponentDTO) {
		ElementModel elementModel = costComponentDTO.getSelectedElementModel();
		ElementVersionModel elementVersion = null;
		ElementVersionModel elementInUseVersion = null;
		if (elementModel != null) {
			elementVersion = elementVersionManager.getLastElementVersionByElementId(elementModel.getElementId());
			BrandModel brand = costComponentDTO.getComponentVersion().getComponent().getOwner();

			elementInUseVersion = elementVersionManager.getLastElementVersionInUse(elementModel.getElementId(),
					costComponentDTO.getComponentVersion().getStatusElementPublish());

			Map<ElementVersionModel, String> elementVersionsToAssign = new LinkedHashMap<ElementVersionModel, String>();

			if (!EnumStatusManager.Status.IN_USE.value().equals(elementVersion.getEnumStatus().getEnumStatusId())
					&& !EnumStatusManager.Status.APPROVED.value()
							.equals(elementVersion.getEnumStatus().getEnumStatusId())) {
				elementVersionsToAssign.put(elementVersion, "costcomponent.assignelement.inedit");
			}
			// check nutzer element
			if (!elementModel.getOwner().equals(brand)) {
				ElementVersionUsersModel elementVersionUsers = elementVersionUsersManager
						.getElementUsers(elementModel.getElementId(), brand.getBrandId());
				ElementVersionModel elementVersionUsed = null;
				if (elementVersionUsers != null) {
					elementVersionUsed = elementVersionUsers.getElementVersion();
					elementVersionsToAssign.put(elementVersionUsed, "costcomponent.assignelement.used");

				}

				if (elementInUseVersion != null) {
					elementVersionsToAssign.put(elementInUseVersion, "costcomponent.assignelement.inuse");
				}

				List<ElementVersionModel> lastInUseElementVersions = elementVersionManager.getElementVersionsByStatus(
						elementModel.getElementId(), EnumStatusManager.Status.IN_USE.value());
				for (ElementVersionModel version : lastInUseElementVersions) {
					if (!version.equals(elementInUseVersion) && !version.equals(elementVersionUsed)) {
						elementVersionsToAssign.put(version,
								"costcomponent.assignelement.stand." + version.getNumber());
					}
				}
			}

			if (elementInUseVersion != null && !elementVersionsToAssign.containsKey(elementInUseVersion)) {
				elementVersionsToAssign.put(elementInUseVersion, "costcomponent.assignelement.inuse");
			}
			costComponentDTO.setElementVersionsToAssign(elementVersionsToAssign);

			if (elementVersionsToAssign.size() == 1) {

				ComponentElementModel newComponentElement = new ComponentElementModel();
				newComponentElement.setElementVersion(elementVersion);
				newComponentElement.setNumber(1L);
				newComponentElement.setComponentStand(costComponentDTO.getComponentStand());
				componentVersionManager.loadElementVersionInElementComponent(newComponentElement,
						costComponentDTO.getComponentVersion().getCatalogModel(),
						costComponentDTO.getComponentVersion().getStatusElementPublish(), false);
				costComponentDTO.getComponentVersion().getAvailableComponentElementList().add(newComponentElement);
			}

		}
		return costComponentDTO;
	}

	/**
	 * Assign element version in status in edit
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO assignInEditElementVersion(CostComponentDTO costComponentDTO) {
		ElementModel elementModel = elementManager.getObject(costComponentDTO.getSelectedElementModelId());
		ElementVersionModel elementVersion = null;

		if (elementModel != null) {
			elementVersion = elementVersionManager.getLastElementVersionByElementId(elementModel.getElementId());
			log.info("elementVersion status " + elementVersion.getEnumStatus().getEnumStatusId());
			if (!EnumStatusManager.Status.IN_USE.value().equals(elementVersion.getEnumStatus().getEnumStatusId())
					&& !EnumStatusManager.Status.APPROVED.value()
							.equals(elementVersion.getEnumStatus().getEnumStatusId())) {
				log.info("Element version in edit will be assign " + elementVersion);
				costComponentDTO.setElementVersionToAssignAsString(elementVersion.getElementVersionId().toString());
				assignElementToComponent(costComponentDTO);
				calculate(costComponentDTO);
			}
		}
		return costComponentDTO;
	}

	/**
	 * Assign one Element to Component
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO assignElementToComponent(CostComponentDTO costComponentDTO) {
		// ElementModel elementModel = elementManager.getObject(costComponentDTO.getSelectedElementModelId());
		log.info("Selected version id: " + costComponentDTO.getElementVersionToAssign());
		BrandModel brand = costComponentDTO.getComponentVersion().getComponent().getOwner();
		if (costComponentDTO.getElementVersionToAssign() != null) {

			ElementVersionModel elementVersion = elementVersionManager
					.getObject(costComponentDTO.getElementVersionToAssign());

			// check nutzer element
			if (!elementVersion.getElement().getOwner().equals(brand)) {
				ElementVersionUsersModel elementVersionUsers = elementVersionUsersManager
						.getElementUsers(elementVersion.getElement().getElementId(), brand.getBrandId());

				if (elementVersionUsers != null && !elementVersionUsers.getElementVersion().equals(elementVersion)) {
					ElementVersionModel oldElementVersion = elementVersionUsers.getElementVersion();
					costComponentDTO.setElementUsedVersion(
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
										componentElement.getComponentStand().getComponentVersion()
												.getComponentVersionId(),
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
							costComponentDTO.setRelatedComponents(components.toString());
						}
					}
					costComponentDTO.setShowConfirmChangeElUsePopup(true);
					return costComponentDTO;
				}
			}

			ComponentElementModel newComponentElement = new ComponentElementModel();

			newComponentElement.setElementVersion(elementVersion);

			newComponentElement.setNumber(1L);
			newComponentElement.setComponentStand(costComponentDTO.getComponentStand());
			componentVersionManager.loadElementVersionInElementComponent(newComponentElement,
					costComponentDTO.getComponentVersion().getCatalogModel(),
					costComponentDTO.getComponentVersion().getStatusElementPublish(), false);
			List<ComponentElementModel> assignedComponentElements = costComponentDTO.getComponentVersion()
					.getAvailableComponentElementList();
			for (ComponentElementModel componentElement : assignedComponentElements) {
				if (componentElement.getElementVersion().getElement()
						.equals(newComponentElement.getElementVersion().getElement())) {
					costComponentDTO.getComponentVersion().getAvailableComponentElementList().remove(componentElement);
					break;
				}
			}
			costComponentDTO.getComponentVersion().getAvailableComponentElementList().add(newComponentElement);
		}
		return costComponentDTO;
	}

	/**
	 * Add a new assigned element version that will be use and remove from list the old used element version
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO saveChangeUseElementVersion(CostComponentDTO costComponentDTO) {

		ElementVersionModel elementVersion = elementVersionManager
				.getObject(costComponentDTO.getElementVersionToAssign());

		// add element version to the component
		ComponentElementModel newComponentElement = new ComponentElementModel();

		newComponentElement.setElementVersion(elementVersion);
		newComponentElement.setNumber(1L);
		newComponentElement.setComponentStand(costComponentDTO.getComponentStand());
		componentVersionManager.loadElementVersionInElementComponent(newComponentElement,
				costComponentDTO.getComponentVersion().getCatalogModel(),
				costComponentDTO.getComponentVersion().getStatusElementPublish(), false);

		List<ComponentElementModel> assignedComponentElements = costComponentDTO.getComponentVersion()
				.getAvailableComponentElementList();
		for (ComponentElementModel componentElement : assignedComponentElements) {
			if (componentElement.getElementVersion().getElement()
					.equals(newComponentElement.getElementVersion().getElement())) {
				costComponentDTO.getComponentVersion().getAvailableComponentElementList().remove(componentElement);
				log.info("Remove from list the old used element version " + componentElement.getElementVersion());
				break;
			}
		}
		costComponentDTO.getComponentVersion().getAvailableComponentElementList().add(newComponentElement);
		log.info("Add to list the new used element version " + newComponentElement.getElementVersion());
		calculate(costComponentDTO);

		return costComponentDTO;
	}

	/**
	 * Set an Element version as used for a brand, it will take the last element version and save an entry in element
	 * version change
	 * 
	 * @param componentElementModel
	 * @param user
	 */
	private void saveElementVersionAsUsed(ComponentElementModel componentElementModel, UserModel user) {
		ComponentModel component = componentElementModel.getComponentStand().getComponentVersion().getComponent();
		BrandModel brand = component.getOwner();
		ElementVersionModel elementVersion = componentElementModel.getElementVersion();

		if (isElementVersionCanBeUsed(elementVersion, brand)) {

			// remove the other element version already used
			ElementVersionUsersModel oldElementVersionUses = elementVersionUsersManager
					.getElementUsers(elementVersion.getElement().getElementId(), brand.getBrandId());
			if (oldElementVersionUses != null) {
				ElementVersionModel targetVersion = oldElementVersionUses.getElementVersion();
				targetVersion.getElementVersionUsers().remove(oldElementVersionUses);
				log.info("Remove element version already used: " + oldElementVersionUses.getElementVersion());
				elementVersionUsersManager.removeObject(oldElementVersionUses.getId());

				// add change entry
				ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(),
						brand.getBrandId(), "", targetVersion, user, Boolean.FALSE, ChangeType.UNUSE_STAND);
			}

			ElementVersionUsersModel elementVersionUses = new ElementVersionUsersModel();
			elementVersionUses.setBrand(brand);
			elementVersionUses.setElementVersion(elementVersion);
			elementVersionUses.setActive(false);

			elementVersionUsersManager.saveObject(elementVersionUses);
			log.info("Use the elementVersion: " + elementVersion.getElementVersionId() + " For Marke: "
					+ brand.getBrandId());

			// add change entry
			ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.USE_STAND.value(), "",
					brand.getBrandId(), elementVersion, user, Boolean.FALSE, ChangeType.USE_STAND);
		}
	}

	/**
	 * Set the unassigned element version as unused when it's only used by this component stand
	 * 
	 * @param componentElementModel
	 * @param user
	 * @param costComponentDTO
	 */
	private void saveElementVersionAsUnUsed(ComponentElementModel componentElementModel, UserModel user) {

		ComponentModel component = componentElementModel.getComponentStand().getComponentVersion().getComponent();
		BrandModel brand = component.getOwner();

		ElementVersionModel elementVersion = componentElementModel.getElementVersion();
		elementVersion.getComponentElements().size();
		List<ComponentElementModel> componentElements = new ArrayList<ComponentElementModel>(
				elementVersion.getComponentElements());
		if (componentElements.size() == 1 && !brand.equals(elementVersion.getElement().getOwner())
				&& componentElements.get(0).getComponentStand().equals(componentElementModel.getComponentStand())) {
			ElementVersionUsersModel elementVersionUses = elementVersionUsersManager
					.getElementVersionUses(elementVersion.getElementVersionId(), brand.getBrandId());
			if (elementVersionUses != null) {
				// removing the association
				elementVersionUsersManager.removeObject(elementVersionUses.getId());
				log.info("unuse the elementVersion: " + elementVersion.getElementVersionId() + " For Marke: "
						+ brand.getBrandId());

				// add change entry
				ElementVersionChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(),
						brand.getBrandId(), "", elementVersion, user, Boolean.FALSE, ChangeType.UNUSE_STAND);

			}
		}
	}

	/**
	 * Remove assigned Element Version
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO unassignElementComponent(CostComponentDTO costComponentDTO) {
		for (ComponentElementModel componentElementModel : costComponentDTO.getSelectedComponentElementList()) {
			componentElementModel.setSelected(false);
			removeFromAvailableComponentElements(componentElementModel, costComponentDTO);
			log.info("remove assigned Element Version: " + componentElementModel.getElementVersion());
		}
		costComponentDTO.getSelectedComponentElementList().clear();
		calculate(costComponentDTO);
		return costComponentDTO;
	}

	/**
	 * Check all the conditions to use an element version
	 * 
	 * @param elementVersion
	 * @param brand
	 * @return
	 */
	private Boolean isElementVersionCanBeUsed(ElementVersionModel elementVersion, BrandModel brand) {

		// If the user brand is the owner
		if (elementVersion.getElement().getOwner() != null && elementVersion.getElement().getOwner().equals(brand)) {
			log.info("Element version cannot be used: the element brand " + brand.getBrandId()
					+ "  is the same as component");
			return Boolean.FALSE;
		}
		// if the brand already used this version
		if (elementVersionUsersManager.getElementVersionUses(elementVersion.getElementVersionId(),
				brand.getBrandId()) != null) {
			log.info("Element version cannot be used: the brand " + brand.getBrandId() + " already use this version");
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	/**
	 * After assigning an element it will be removed from the mist of available elements
	 * 
	 * @param componentElementModel
	 * @param costComponentDTO
	 */
	private void removeFromAvailableComponentElements(ComponentElementModel componentElementModel,
			CostComponentDTO costComponentDTO) {
		List<ComponentElementModel> list = costComponentDTO.getComponentVersion().getAvailableComponentElementList();
		for (int i = 0; i < list.size(); i++) {
			ComponentElementModel cElementModel = list.get(i);
			if (cElementModel.getElementVersion().equals(componentElementModel.getElementVersion())) {
				list.remove(i);
				break;
			}
		}
	}

	/**
	 * Check if there is an old component with same number or designation in the same brand
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO getExistComponentWithSameNumberOrDesignation(CostComponentDTO costComponentDTO) {
		ComponentModel cModel = costComponentDTO.getComponentVersion().getComponent();
		if ((componentManager.getComponentByNmberOrDesignation(cModel.getComponentId(),
				costComponentDTO.getComponentVersion().isLoaded(), cModel.getComponentNumber(), cModel.getDesignation(),
				cModel.getOwner().getBrandId())) != null) {
			costComponentDTO.setExistComponentWithSameNumberOrDesignation(Boolean.TRUE);
		} else {
			costComponentDTO.setExistComponentWithSameNumberOrDesignation(Boolean.FALSE);
		}
		return costComponentDTO;
	}

	/**
	 * Create a copy of a component. This method will change current component in DTO to a new one changing name an
	 * designation and setting some properties to null
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO copyComponent(CostComponentDTO costComponentDTO) {
		// set objects as not loaded
		costComponentDTO.getComponentVersion().getComponent().setLoaded(Boolean.FALSE);
		costComponentDTO.getComponentVersion().setLoaded(Boolean.FALSE);
		// new number / designation / status
		String newNumber = "Kopie_" + costComponentDTO.getComponentVersion().getComponent().getComponentNumber();
		String newDesignation = "Kopie_" + costComponentDTO.getComponentVersion().getComponent().getDesignation();
		costComponentDTO.getComponentVersion().getComponent().setComponentNumber(newNumber);
		costComponentDTO.getComponentVersion().getComponent().setDesignation(newDesignation);
		costComponentDTO.getComponentVersion().getComponent().setExternalId(null);
		// set the new brand from the user that performs this action
		BrandModel owner = brandManager.getObject(costComponentDTO.getUserLogged().getDefaultBrand());
		costComponentDTO.getComponentVersion().getComponent().setOwner(owner);
		// set the new library from the user that performs this action
		LibraryModel lib = libraryManager.getObject(costComponentDTO.getUserLogged().getDefaultLibrary());
		costComponentDTO.getComponentVersion().getComponent().setLibrary(lib);

		costComponentDTO.getComponentVersion()
				.setEnumStatus(enumStatusManager.getObject(EnumStatusManager.Status.NEW.value()));
		costComponentDTO.getComponentVersion().setComment(null);
		costComponentDTO.getComponentVersion().setCreationDate(null);
		costComponentDTO.getComponentVersion().setCreator(null);
		costComponentDTO.getComponentVersion().setModificationDate(null);
		costComponentDTO.getComponentVersion().setModifier(null);
		costComponentDTO.getComponentVersion().setReleaser(null);
		costComponentDTO.getComponentVersion().setValidFrom(null);
		costComponentDTO.getComponentVersion().setValidTo(null);
		costComponentDTO.getComponentVersion().setNumber(null);

		// reset old values
		costComponentDTO.getComponentVersion().setOldComment(null);
		costComponentDTO.getComponentVersion().setOldElectric(null);
		costComponentDTO.getComponentVersion().setOldElectricPPPLNotice(null);
		costComponentDTO.getComponentVersion().setOldElectricPurchasedPartPriceLevelDate(null);
		costComponentDTO.getComponentVersion().setOldMecanicPPPLDate(null);
		costComponentDTO.getComponentVersion().setOldMecanicPPPLNotice(null);
		costComponentDTO.getComponentVersion().setOldMechanicalConstruction(null);
		costComponentDTO.getComponentVersion().setOldMechanicalExecution(null);
		costComponentDTO.getComponentVersion().setOldWithProvision(null);

		// 28.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		costComponentDTO.getComponentVersion().setOldCatalogNumber(null);
		costComponentDTO.getComponentVersion().setOldDescription(null);
		costComponentDTO.getComponentVersion().setOldEnumComponentType(null);
		costComponentDTO.getComponentVersion().setOldManufacturer(null);

		// avoid not copied set
		costComponentDTO.getComponentVersion().getComponent()
				.setComponentVersions(new HashSet<ComponentVersionModel>());
		costComponentDTO.getComponentVersion().setComponentValues(null);

		// 06.03.2014: AMR: PTS_Problem-36750: Copy joined documents when copying components
		Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(costComponentDTO.getDocumentList());
		costComponentDTO.getComponentVersion().setDocuments(documents);
		costComponentDTO.setDocumentList(new ArrayList<DocumentPoolModel>(documents));

		costComponentDTO.getComponentVersion().setComponentVersionStands(null);
		// copy component_version/element association
		for (ComponentElementModel cModel : costComponentDTO.getComponentVersion().getAvailableComponentElementList()) {
			// cModel.setComponentVersion(componentVersion)
			cModel.setLoaded(Boolean.FALSE);
		}
		// copy components values
		for (ComponentValueModel vModel : costComponentDTO.getComponentVersion().getComponentValuesForView()) {
			vModel.setOldAmount(null);
			vModel.setOldNumber(null);
			vModel.setLoaded(Boolean.FALSE);
		}
		costComponentDTO.setComponentStandCanBeUsed(Boolean.FALSE);
		costComponentDTO.setComponentStandUsed(Boolean.FALSE);

		List<EnumTransitionModel> transitions = enumStatusManager
				.getAllowedTransitions(costComponentDTO.getComponentVersion().getEnumStatus(), Boolean.FALSE, null);

		costComponentDTO.setAllowedTransitions(transitions);
		ComponentVersionModel toCopyVersion = costComponentDTO.getComponentVersion();

		costComponentDTO.setComponentStand(new ComponentStandModel());
		costComponentDTO.getComponentStand().setComponentVersion(toCopyVersion);
		return costComponentDTO;
	}

	// --------------------XML export

	/**
	 * 
	 * @param exportDate
	 * @param dto
	 * @param userIds
	 * @param folderId
	 * @return
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 * @throws IOException
	 */
	public Object[] loadXmlExpotedFile(BaseDateTime exportDate, CostComponentDTO dto, List<String> userIds,
			Long folderId) throws ParserConfigurationException, TransformerException, IOException {
		log.info("START LOADING XML EXPORTED FILE: " + BaseDateTime.getCurrentDateTime());
		Object[] fileNameAndContent = new Object[2];
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		// root node
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Data");
		Attr rootAttr = doc.createAttribute("PpXmlVer");
		rootAttr.setValue("7.0");
		rootElement.setAttributeNode(rootAttr);
		doc.appendChild(rootElement);
		// Objects node
		Element objects = doc.createElement("Objects");
		rootElement.appendChild(objects);
		doc = fillXmlDoc(doc, objects, exportDate, dto, userIds, folderId, fileNameAndContent);
		if (doc == null) {
			log.info("DOC is Null");
			return null;
		}
		// write the content into xml file
		return writeContentAsXml(fileNameAndContent, doc);
	}

	/**
	 * 
	 * @param dto
	 * @param componentVersion
	 * @return component as xml.
	 * @throws ParserConfigurationException
	 */
	public Object[] loadXmlExpotedFile(BaseDateTime exportDate, CostComponentDTO dto)
			throws ParserConfigurationException, TransformerException {
		log.info("START LOADING XML EXPORTED FILE For B Type Object: " + BaseDateTime.getCurrentDateTime());
		Object[] fileNameAndContent = new Object[2];
		ComponentVersionModel componentVersion = getLastInUseComponentVersion(dto, exportDate);
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		fileNameAndContent[0] = generateFileName(componentVersion.getComponent().getLibrayLabel(),
				componentVersion.getComponent().getComponentNumber(), null, exportDate);
		// root node
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("Data");
		Attr rootAttr = doc.createAttribute("PpXmlVer");
		rootAttr.setValue("7.0");
		rootElement.setAttributeNode(rootAttr);
		doc.appendChild(rootElement);
		// Objects node
		Element objects = doc.createElement("Objects");
		rootElement.appendChild(objects);

		File template = templateFile(dto.isExportWithoutAttributes(), dto.getExportTarget());
		if (!componentVersion.getComponentVersionStands().isEmpty()) {
			CostComponentDTO costComponentDTO = new CostComponentDTO();
			costComponentDTO.setComponentVersionId(componentVersion.getComponentVersionId());
			costComponentDTO.setComponentStand(getStandToExport(componentVersion, exportDate));
			costComponentDTO.setUserLogged(dto.getUserLogged());
			costComponentDTO = loadDetail(costComponentDTO, HourlyRateCatalogManager.DEFAULT_CATALOG_ID, Boolean.FALSE);
			// No need to calculate aggregated Cost Attributes because it will not be exported
			if (!dto.isExportWithoutAttributes()) {
				costComponentDTO = calculateAggregatedCostAttributeValues(costComponentDTO);
			}
			// 19.11.2013: ZS: PTS_Requirement-26653: Add the export date to the XML file
			exportDate = exportDate.addSeconds(-(59 * 60 + 23 * 60 * 60 + 59));
			addComponent(doc, template, objects, costComponentDTO.getComponentStand(), dto.isExportWithoutAttributes(),
					dto.getComponentType(), false, exportDate);
		}
		return writeContentAsXml(fileNameAndContent, doc);
	}

	private Object[] writeContentAsXml(Object[] fileNameAndContent, Document doc)
			throws TransformerConfigurationException, TransformerFactoryConfigurationError, TransformerException {
		log.info("START write Content AS XML : " + BaseDateTime.getCurrentDateTime());
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, "Windows-1252");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		// StringWriter writer = new StringWriter();
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		StreamResult result = new StreamResult(out);
		DOMSource domSource = new DOMSource(doc);
		transformer.transform(domSource, result);
		fileNameAndContent[1] = out.toByteArray();
		log.info("END write Content AS XML : " + BaseDateTime.getCurrentDateTime());
		return fileNameAndContent;
	}

	private String generateFileName(String library, String componentNumber, String folderDesignation,
			BaseDateTime exportDate) {
		String prefix = "_";
		StringBuilder fileName = new StringBuilder("Prozessdaten");
		if (library != null && !"".equals(library.trim())) {
			fileName.append(prefix);
			fileName.append(library);
		}
		if (componentNumber != null && !"".equals(componentNumber.trim())) {
			fileName.append(prefix);
			fileName.append(componentNumber);
		}
		if (folderDesignation != null && !"".equals(folderDesignation.trim())) {
			fileName.append(prefix);
			fileName.append(folderDesignation);
		}
		
		fileName.append(prefix);
		fileName.append(exportDate.getString(BaseDateTime.YYYYMMDD, BaseDateTime.PNT));
		fileName.append(".xml");

		// 31.10.2013: ZS: PTS_Problem-26506: Exported XML file can not be opened
		String completeFileName = fileName.toString();
		// replace all spaces in the file name by _
		completeFileName = completeFileName.replaceAll("\\s+", "_");
		return completeFileName;
	}

	/**
	 * export components verions in XML file
	 * 
	 * @param doc
	 * @param objects
	 * @param exportDate
	 * @param userModel
	 * @param withoutAttributes
	 * @param primaryLibrary
	 * @param fileNameAndContent
	 * @return
	 */
	private Document fillXmlDoc(Document doc, Element objects, BaseDateTime exportDate, CostComponentDTO dto,
			List<String> userIds, Long forlderId, Object[] fileNameAndContent) {
		log.info("START FILLING XML EXPORTED FILE: " + BaseDateTime.getCurrentDateTime());
		UserModel userModel = dto.getUserLogged();
		boolean withoutAttributes = dto.isExportWithoutAttributes();
		// resolve the selectManyMenu long issue
		List<Long> libraryIds = new ArrayList<Long>();
		for (String id : dto.getSelectedLibraryIds()) {
			libraryIds.add(Long.valueOf(id));
		}
		String componentType = dto.getComponentType();
		boolean exportAsErgBib = dto.isExportAsErgBib();
		Long primaryLibrary = dto.getSelectedPrimaryLibrary();
		boolean exportApprovedComponent = dto.isExportApprovedComponent();
		StringBuilder libraryLabel = new StringBuilder();
		String prefix = "";
		for (Long libraryId : libraryIds) {
			libraryLabel.append(prefix);
			prefix = "_";
			libraryLabel.append(libraryManager.getObject(libraryId).getName());
		}
		log.info("START LOADING FOLDER HIERARCHY: " + BaseDateTime.getCurrentDateTime());
		FolderModel folder = folderManager.getObject(forlderId);
		String folderName = folder.getDesignation().replaceAll("/", "_");
		fileNameAndContent[0] = generateFileName(libraryLabel.toString(), null,
				((forlderId.equals(7L)) ? "Gesamtbibliothek" : folderName), exportDate);
		List<Long> foldersId = new ArrayList<Long>();
		if (!forlderId.equals(7L)) {
			List<FolderModel> folders = new ArrayList<FolderModel>();
			getAllUnderFolders(folder, folders);
			// fill foldersid list without up to folder for search reason
			for (FolderModel folderModel : folders) {
				foldersId.add(folderModel.getFolderId());
			}
		}
		log.info("END LOADING FOLDER HIERARCHY: " + BaseDateTime.getCurrentDateTime());
		// get concerned component stand Ids
		log.info("Export criteria: ");
		log.info("Export Date: " + exportDate);
		log.info("library Ids: " + libraryIds);
		log.info("User Ids: " + userIds);
		log.info("Folder Id: " + foldersId);
		log.info("Primary Library: " + primaryLibrary);
		log.info("ExportApprovedComponent: " + exportApprovedComponent);
		List<ComponentStandModel> stands = componentVersionStandManager.getToExportComponentStands(exportDate,
				libraryIds, userIds, foldersId, primaryLibrary, exportApprovedComponent);

		log.info("Number of stands found: " + stands.size());
		if (stands.isEmpty()) {
			log.info("Stand List is Empty: return NULL");
			return null;
		}
		log.info("START Mapping Folder To Component: " + BaseDateTime.getCurrentDateTime());
		Map<Long, Set<ComponentModel>> toExportComponentByfolder = new LinkedHashMap<Long, Set<ComponentModel>>();
		Set<FolderModel> notEmptyFolders = new HashSet<FolderModel>();
		for (ComponentStandModel stand : stands) {
			ComponentModel component = stand.getComponentVersion().getComponent();
			if (!toExportComponentByfolder.containsKey(component.getFolder().getFolderId())) {
				Set<ComponentModel> components = new HashSet<ComponentModel>();
				components.add(component);
				toExportComponentByfolder.put(component.getFolder().getFolderId(), components);
			} else {
				toExportComponentByfolder.get(component.getFolder().getFolderId()).add(component);
			}
			notEmptyFolders.add(component.getFolder());
		}
		for (Map.Entry<Long, Set<ComponentModel>> entry : toExportComponentByfolder.entrySet()) {
			List<ComponentModel> componentList = new ArrayList<ComponentModel>(entry.getValue());
			Collections.sort(componentList, new Comparator<ComponentModel>() {
				@Override
				public int compare(ComponentModel o1, ComponentModel o2) {
					return o1.getDisplayOrder().compareTo(o2.getDisplayOrder());
				}
			});
			entry.setValue(new LinkedHashSet<ComponentModel>(componentList));
		}
		// up to root : commented dur to problem 12110
		upToRootFolder(notEmptyFolders, forlderId);
		Map<Long, List<FolderModel>> toExportFolderByfolder = getToExportFolderByFolderMap(notEmptyFolders);
		for (FolderModel folderModel : notEmptyFolders) {
			if (folderModel.getFolderId().equals(7L)) {
				addRootFolder(doc, objects, folderModel, exportDate, exportAsErgBib, exportApprovedComponent,
						toExportFolderByfolder, toExportComponentByfolder);
			} else {
				addChildFolder(doc, objects, folderModel, exportAsErgBib, toExportFolderByfolder,
						toExportComponentByfolder);
			}
		}
		log.info("END Mapping Folder To Component: " + BaseDateTime.getCurrentDateTime());
		File template = templateFile(withoutAttributes,dto.getExportTarget());
		// 19.11.2013: ZS: PTS_Requirement-26653: Add the export date to the XML file
		exportDate = exportDate.addSeconds(-(59 * 60 + 23 * 60 * 60 + 59));
		log.info("START adding XML Component : " + BaseDateTime.getCurrentDateTime());
		for (ComponentStandModel stand : stands) {
			CostComponentDTO costComponentDTO = new CostComponentDTO();
			costComponentDTO.setComponentVersionId(stand.getComponentVersion().getComponentVersionId());
			// PTS 11973 : Export Nutzer Stand and not owner
			costComponentDTO.setComponentStand(stand);
			costComponentDTO.setUserLogged(userModel);
			costComponentDTO = loadDetailForExport(costComponentDTO, HourlyRateCatalogManager.DEFAULT_CATALOG_ID,
					Boolean.FALSE);
			// No need to calculate aggregated Cost Attributes because it will not be exported
			if (!withoutAttributes) {
				costComponentDTO = calculateAggregatedCostAttributeValues(costComponentDTO);
			}
			addComponent(doc, template, objects, stand, withoutAttributes, componentType, exportAsErgBib, exportDate);

		}
		log.info("END adding XML Component : " + BaseDateTime.getCurrentDateTime());
		log.info("END FILLING XML EXPORTED FILE: " + BaseDateTime.getCurrentDateTime());
		return doc;
	}

	private void upToRootFolder(Set<FolderModel> notEmptyFolders, Long rootFolderId) {
		Set<FolderModel> parentNotEmptyFolders = new HashSet<FolderModel>();
		for (FolderModel folderModel : notEmptyFolders) {
			folderManager.getFolderParentsUtilFolder(folderModel, rootFolderId, parentNotEmptyFolders);
		}

		notEmptyFolders.addAll(parentNotEmptyFolders);
	}

	private ComponentStandModel getStandToExport(ComponentVersionModel cVersionModel, BaseDateTime exportDate) {
		List<ComponentStandModel> filtredStandList = new ArrayList<ComponentStandModel>();
		for (ComponentStandModel componentStandModel : cVersionModel.getComponentVersionStands()) {
			if (componentStandModel.getStandDate().getDate().compareTo(exportDate.getDate()) <= 0) {
				filtredStandList.add(componentStandModel);
			}
		}
		ComponentStandModel componentStand = filtredStandList.get(0);
		return componentStand;
	}

	/**
	 * Fill the allFolders list with all under folders in a recursive way.
	 * 
	 * @param folder
	 * @param allFolders
	 */
	private void getAllUnderFolders(FolderModel folder, List<FolderModel> allFolders) {
		List<FolderModel> tempList = folderManager.getFoldersUnder(folder.getFolderId(), 2L);
		if (tempList != null && !tempList.isEmpty()) {
			for (FolderModel model : tempList) {
				getAllUnderFolders(model, allFolders);
			}
		}
		Collections.sort(allFolders, new Comparator<FolderModel>() {
			@Override
			public int compare(FolderModel o1, FolderModel o2) {
				return o1.getDesignation().compareTo(o2.getDesignation());
			}
		});
		allFolders.add(folder);

	}

	private Map<Long, List<FolderModel>> getToExportFolderByFolderMap(Set<FolderModel> notEmptyFolders) {
		Map<Long, List<FolderModel>> toExportFolderByfolder = new HashMap<Long, List<FolderModel>>();
		for (FolderModel folderModel : notEmptyFolders) {
			if (!toExportFolderByfolder.containsKey(folderModel.getFolderId())) {
				toExportFolderByfolder.put(folderModel.getFolderId(), new ArrayList<FolderModel>());
			}
		}
		for (FolderModel folderModel : notEmptyFolders) {
			if (folderModel.getFolder() != null) {
				if (toExportFolderByfolder.containsKey(folderModel.getFolder().getFolderId())) {
					toExportFolderByfolder.get(folderModel.getFolder().getFolderId()).add(folderModel);
				}
			}
		}
		for (Map.Entry<Long, List<FolderModel>> entry : toExportFolderByfolder.entrySet()) {
			List<FolderModel> folderList = entry.getValue();
			Collections.sort(folderList, new Comparator<FolderModel>() {
				@Override
				public int compare(FolderModel o1, FolderModel o2) {
					return o1.getDesignation().compareTo(o2.getDesignation());
				}
			});
			entry.setValue(folderList);
		}
		return toExportFolderByfolder;
	}

	private void addRootFolder(Document doc, Element parentElement, FolderModel folderModel, BaseDateTime exportDate,
			boolean exportAsErgBib, boolean exportApprovedComponent,
			Map<Long, List<FolderModel>> toExportFolderByfolder,
			Map<Long, Set<ComponentModel>> toExportComponentByfolder) {
		StringBuilder name = new StringBuilder("Komponenten KAL");
		if (exportAsErgBib) {
			name.append(" Ergänzung ");
		}
		name.append("(Stand:");
		String exportFormattedDate = exportDate.getString(BaseDateTime.DD_MM_YYYY, BaseDateTime.MIN);
		name.append(exportFormattedDate);
		if (exportApprovedComponent) {
			if (exportFormattedDate
					.equals(BaseDateTime.getCurrentDateTime().getString(BaseDateTime.DD_MM_YYYY, BaseDateTime.MIN))) {
				String time = BaseDateTime.getCurrentDateTime().getString(BaseDateTime.HH_MM);
				name.append(", " + time + " Uhrzeit");
			} else {
				name.append(", 23:59 Uhrzeit");
			}
		}
		name.append(")");
		if (exportApprovedComponent) {
			name.append(" - vorläufig");
		}
		log.info("title of the export: " + name);

		Element folderElement = createFolderElement(doc, folderModel, name.toString(), exportAsErgBib,
				toExportFolderByfolder, toExportComponentByfolder);
		Node firstChild = parentElement.getFirstChild();
		if (firstChild != null) {
			parentElement.insertBefore(folderElement, firstChild);
		} else {
			parentElement.appendChild(folderElement);
		}
	}

	private void addChildFolder(Document doc, Element parentElement, FolderModel folderModel, boolean exportAsErgBib,
			Map<Long, List<FolderModel>> toExportFolderByfolder,
			Map<Long, Set<ComponentModel>> toExportComponentByfolder) {
		String name = folderModel.getDesignation();
		Element folderElement = createFolderElement(doc, folderModel, name, exportAsErgBib, toExportFolderByfolder,
				toExportComponentByfolder);
		parentElement.appendChild(folderElement);
	}

	private Element createFolderElement(Document doc, FolderModel folderModel, String name, boolean exportAsErgBib,
			Map<Long, List<FolderModel>> toExportFolderByfolder,
			Map<Long, Set<ComponentModel>> toExportComponentByfolder) {
		Element folderElement = doc.createElement("PmResourceLibrary");
		Attr folderAttr = doc.createAttribute("ExternalId");
		String postfixAsErgBib = (exportAsErgBib ? "_ergbib" : "");
		folderAttr.setValue(folderModel.getExternalFolderId() + postfixAsErgBib);
		folderElement.setAttributeNode(folderAttr);
		addElementNode(doc, folderElement, "Objekttyp", "");
		Element childrenNode = doc.createElement("children");
		// add chldren items: sub folders and components
		List<FolderModel> folders = toExportFolderByfolder.get(folderModel.getFolderId());
		if (folders != null) {
			for (FolderModel fModel : folders) {
				addElementNode(doc, childrenNode, "item", fModel.getExternalFolderId() + postfixAsErgBib);
			}
		}
		if (toExportComponentByfolder != null) {
			Set<ComponentModel> components = toExportComponentByfolder.get(folderModel.getFolderId());
			if (components != null) {
				for (ComponentModel cModel : components) {
					addElementNode(doc, childrenNode, "item", cModel.getExternalComponentId());
				}
			}
		}
		folderElement.appendChild(childrenNode);
		// Field removed : change 1905 Ordner Metainformationen berücksichtigen
		// Ticket ID: 7365
		// addElementNode(doc, folderElement, "comment", "");
		addElementNode(doc, folderElement, "name", name);
		return folderElement;
	}

	private void addComponent(Document doc, File template, Element parentElement, ComponentStandModel stand,
			boolean withoutAttributes, String componentType, boolean exportAsErgBib, BaseDateTime exportDate) {
		Map<String, String> componentAttributeMap = new HashMap<String, String>();
		ComponentVersionModel componentVersionModel = stand.getComponentVersion();
		fillComponentAttributes(stand, componentAttributeMap, componentType, exportDate);
		if (!withoutAttributes) {
			fillCostAttributesMap(componentAttributeMap, componentVersionModel);
		}
		fillChangesMap(componentAttributeMap, componentVersionModel);
		Element componentElement = doc.createElement(componentVersionModel.getEnumComponentClass().getComponentClass());
		Attr componentAttr = doc.createAttribute("ExternalId");
		componentAttr.setValue(componentVersionModel.getComponent().getExternalComponentId());
		componentElement.setAttributeNode(componentAttr);
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			Document docA = docBuilder.parse(template);
			Element rootA = docA.getDocumentElement();
			NodeList nList = doc.importNode(rootA, true).getChildNodes();
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					if (componentAttributeMap.containsKey(nNode.getNodeName())
							&& componentAttributeMap.get(nNode.getNodeName()) != null) {
						addElementNode(doc, componentElement, nNode.getNodeName(),
								componentAttributeMap.get(nNode.getNodeName()));
					} else {
						componentElement.appendChild(doc.importNode(nNode, true));
					}
				}
			}
		} catch (Exception e) {
			log.error("Error : ", e);
		}
		parentElement.appendChild(componentElement);
		// if (componentVersionModel.getComponent().getImagePath() != null
		// && !componentVersionModel.getComponent().getImagePath().equals("")) {
		// File userFile = new File(componentVersionModel.getComponent().getImagePath());
		// String filename = userFile.getName();
		// addPmImageElement(doc, parentElement, componentVersionModel.getComponent().getExternalComponentId(),
		// filename);
		// String imagePath = "#" + componentVersionModel.getComponent().getImagePath();
		// addPmReferenceFileElement(doc, parentElement, componentVersionModel.getComponent().getExternalComponentId(),
		// imagePath);
		// }
	}

	@SuppressWarnings("unused")
	private void addPmImageElement(Document doc, Element parentElement, String externalId, String filename) {
		Element pmImageElement = doc.createElement("PmImage");
		Attr exterAttr = doc.createAttribute("ExternalId");
		exterAttr.setValue(externalId);
		pmImageElement.setAttributeNode(exterAttr);
		addElementNode(doc, pmImageElement, "name", filename);
		addElementNode(doc, pmImageElement, "file", externalId);
		addElementNode(doc, pmImageElement, "comment", "");
		addElementNode(doc, pmImageElement, "attachments", "");
		addElementNode(doc, pmImageElement, "status", "");
		addElementNode(doc, pmImageElement, "type", "");
		Element infoElement = doc.createElement("NodeInfo");
		addElementNode(doc, infoElement, "Id", "");
		addElementNode(doc, infoElement, "Name", "");
		addElementNode(doc, infoElement, "family", "");
		addElementNode(doc, infoElement, "version", "");
		addElementNode(doc, infoElement, "status", "");
		addElementNode(doc, infoElement, "cioState", "");
		pmImageElement.appendChild(infoElement);
		parentElement.appendChild(pmImageElement);
	}

	@SuppressWarnings("unused")
	private void addPmReferenceFileElement(Document doc, Element parentElement, String externalId, String imagePath) {
		Element pmReferenceElement = doc.createElement("PmReferenceFile");
		Attr exterAttr = doc.createAttribute("ExternalId");
		exterAttr.setValue(externalId);
		pmReferenceElement.setAttributeNode(exterAttr);
		addElementNode(doc, pmReferenceElement, "name", "");
		addElementNode(doc, pmReferenceElement, "comment", "");
		addElementNode(doc, pmReferenceElement, "attachments", "");
		addElementNode(doc, pmReferenceElement, "status", "");
		addElementNode(doc, pmReferenceElement, "fileName", imagePath);
		addElementNode(doc, pmReferenceElement, "fileModificationDate", "");
		Element infoElement = doc.createElement("NodeInfo");
		addElementNode(doc, infoElement, "Id", "");
		addElementNode(doc, infoElement, "Name", "");
		addElementNode(doc, infoElement, "family", "");
		addElementNode(doc, infoElement, "version", "");
		addElementNode(doc, infoElement, "status", "");
		addElementNode(doc, infoElement, "cioState", "");
		pmReferenceElement.appendChild(infoElement);
		parentElement.appendChild(pmReferenceElement);
	}

	private void addElementNode(Document doc, Element parentElement, String name, String value) {
		Element newElement = doc.createElement(name);
		newElement.appendChild(doc.createTextNode(value));
		parentElement.appendChild(newElement);
	}

	public void fillComponentAttributes(ComponentStandModel stand, Map<String, String> componentAttributeMap,
			String componentType, BaseDateTime exportDate) {
		componentAttributeMap.clear();
		ComponentVersionModel componentVersionModel = stand.getComponentVersion();
		ComponentModel componentModel = componentVersionModel.getComponent();
		componentAttributeMap.put("Costgroup", nullToEmptyConverter(componentVersionModel.getCostgroup()));
		componentAttributeMap.put("Pos", componentModel.getComponentNumber());
		componentAttributeMap.put("name", componentModel.getDesignation());	
		if (componentVersionModel.getWithProvision()) {
			componentAttributeMap.put("bBeistellungenValid", "1");
		} else {
			componentAttributeMap.put("bBeistellungenValid", "0");
		}
		componentAttributeMap.put("comment", nullToEmptyConverter(componentVersionModel.getDescription()));
		componentAttributeMap.put("Inhalt_Mech_Ausfuerung",
				nullToEmptyConverter(componentVersionModel.getMechanicalExecution()));
		componentAttributeMap.put("Inhalt_Mech_Konstruktion",
				nullToEmptyConverter(componentVersionModel.getMechanicalConstruction()));
		componentAttributeMap.put("Inhalt_Elek", nullToEmptyConverter(componentVersionModel.getElectric()));
		// fill the Objekttyp
		// Change 10293694: Objekttypen: in PD müssen beide Sprachen angezeigt werden
		String enumComponentType = "";
		if (componentVersionModel.getEnumComponentType() != null) {
			enumComponentType = componentVersionModel.getEnumComponentType().getComponentType();
			if (componentVersionModel.getEnumComponentType().getComponentTypeEn() != null
					&& !"".equals(componentVersionModel.getEnumComponentType().getComponentTypeEn())) {
				enumComponentType = enumComponentType + " / "
						+ componentVersionModel.getEnumComponentType().getComponentTypeEn();
			}
		}
		componentAttributeMap.put("Objekttyp", enumComponentType);
		
		componentAttributeMap.put("catalogNumber", nullToEmptyConverter(componentVersionModel.getCatalogNumber()));
		componentAttributeMap.put("Hersteller", nullToEmptyConverter(componentVersionModel.getManufacturer()));
		componentAttributeMap.put("NKMKaufteileInfo",
				nullToEmptyConverter(componentVersionModel.getMecanicPurchasedPartPriceLevelNotice()));

		componentAttributeMap.put("NKMKaufteileDatum", nullToSpecificValueConverter(
				baseDateTimeToExportDateConverter(componentVersionModel.getMecanicPurchasedPartPriceLevelDate()),
				"01/01/2000 0:00:00"));

		componentAttributeMap.put("NKEKaufteileInfo",
				nullToEmptyConverter(componentVersionModel.getElectricPurchasedPartPriceLevelNotice()));

		componentAttributeMap.put("NKEKaufteileDatum", nullToSpecificValueConverter(
				baseDateTimeToExportDateConverter(componentVersionModel.getElectricPurchasedPartPriceLevelDate()),
				"01/01/2000 0:00:00"));
		componentAttributeMap.put("KomponentenTyp", nullToEmptyConverter(componentType));
		// PTS change : 10332
		componentAttributeMap.put("Hide2DLabel", (componentModel.getComponentNumber().endsWith("_D") ? "0" : "1"));
		componentAttributeMap.put("hide2DObject", (componentModel.getComponentNumber().endsWith("_D") ? "0" : "1"));
		// PTS problem: 12493
		componentAttributeMap.put("AEKBrand", componentVersionModel.getComponent().getOwner().getBrandId());
	

		// PTS req. 83092
		// String imageReference = null;
		// if (componentVersionModel.getComponent().getImagePath() != null
		// && !componentVersionModel.getComponent().getImagePath().equals("")) {
		// imageReference = componentVersionModel.getComponent().getExternalComponentId();
		// }
		// componentAttributeMap.put("image", nullToSpecificValueConverter(imageReference, "NULL"));
		// 19.11.2013: ZS: PTS_Requirement-26653: Add the export date to the XML file
		componentAttributeMap.put("PmToolPrototype.BIBVersionDate", baseDateTimeToExportDateConverter(exportDate));
	}

	private void fillCostAttributesMap(Map<String, String> componentAttributeMap,
			ComponentVersionModel componentVersionModel) {
		Map<Long, BaseBigDecimal> componentValuesMap = new HashMap<Long, BaseBigDecimal>();
		Map<Long, String> componentValuesRemarkMap = new HashMap<Long, String>();
		for (ComponentValueModel cValueModel : componentVersionModel.getAggregatedComponentValuesForView()) {
			if (cValueModel.getCostAttribute().getPricePerUnit()) {
				componentValuesMap.put(cValueModel.getCostAttribute().getCostAttributeId(),
						(cValueModel.getNumber() != null) ? cValueModel.getNumber()
								: new BaseBigDecimal(new BigDecimal(0)));

			} else {
				componentValuesMap.put(cValueModel.getCostAttribute().getCostAttributeId(),
						(cValueModel.getAmount() != null) ? cValueModel.getAmount()
								: new BaseBigDecimal(new BigDecimal(0)));
			}
			componentValuesRemarkMap.put(cValueModel.getCostAttribute().getCostAttributeId(),
					nullToEmptyConverter(cValueModel.getDescription()));
		}
		if (componentValuesMap.containsKey(2L) || componentValuesMap.containsKey(3L)
				|| componentValuesMap.containsKey(4L)) {
			BaseBigDecimal v1 = (componentValuesMap.get(2L) != null) ? componentValuesMap.get(2L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v2 = (componentValuesMap.get(3L) != null) ? componentValuesMap.get(3L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v3 = (componentValuesMap.get(4L) != null) ? componentValuesMap.get(4L)
					: new BaseBigDecimal("0");
			componentAttributeMap.put("NKMPlanMethPlanS", (v1.add(v2).add(v3)).getExportString());
		}
		if (componentValuesMap.containsKey(5L)) {
			componentAttributeMap.put("NKMRobSimS", componentValuesMap.get(5L).getExportString());
		}
		if (componentValuesMap.containsKey(6L)) {
			componentAttributeMap.put("NKMRobUntersS", componentValuesMap.get(6L).getExportString());
		}
		if (componentValuesMap.containsKey(7L) || componentValuesMap.containsKey(8L)) {
			BaseBigDecimal v1 = (componentValuesMap.get(7L) != null) ? componentValuesMap.get(7L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v2 = (componentValuesMap.get(8L) != null) ? componentValuesMap.get(8L)
					: new BaseBigDecimal("0");
			componentAttributeMap.put("NKMCADKonS", (v1.add(v2)).getExportString());
		}
		if (componentValuesMap.containsKey(9L)) {
			componentAttributeMap.put("NKMRobProgS", componentValuesMap.get(9L).getExportString());
		}
		if (componentValuesMap.containsKey(10L)) {
			componentAttributeMap.put("NKMCADCAMProg2D3DS", componentValuesMap.get(10L).getExportString());
		}
		if (componentValuesMap.containsKey(11L)) {
			componentAttributeMap.put("NKMBankarbeitS", componentValuesMap.get(11L).getExportString());
		}
		if (componentValuesMap.containsKey(12L)) {
			componentAttributeMap.put("NKMKleineMaschineS", componentValuesMap.get(12L).getExportString());
		}
		if (componentValuesMap.containsKey(13L)) {
			componentAttributeMap.put("NKMMittlereMaschineS", componentValuesMap.get(13L).getExportString());
		}
		if (componentValuesMap.containsKey(14L)) {
			componentAttributeMap.put("NKMGrosseMaschineS", componentValuesMap.get(14L).getExportString());
		}
		if (componentValuesMap.containsKey(15L)) {
			componentAttributeMap.put("NKMMessmaschineS", componentValuesMap.get(15L).getExportString());
		}
		if (componentValuesMap.containsKey(16L)) {
			componentAttributeMap.put("NKMFremdanfertigung", componentValuesMap.get(16L).getExportString());
		}
		if (componentValuesMap.containsKey(17L)) {
			componentAttributeMap.put("NKMBaustahlKg", componentValuesMap.get(17L).getExportString());
		}
		if (componentValuesMap.containsKey(18L)) {
			componentAttributeMap.put("NKMWkzgstNEMetalleKg", componentValuesMap.get(18L).getExportString());
		}
		if (componentValuesMap.containsKey(19L) || componentValuesMap.containsKey(1L)) {
			BaseBigDecimal v1 = (componentValuesMap.get(1L) != null) ? componentValuesMap.get(1L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v2 = (componentValuesMap.get(19L) != null) ? componentValuesMap.get(19L)
					: new BaseBigDecimal("0");

			componentAttributeMap.put("NKMKaufteile", (v1.add(v2)).getExportString());
		}
		if (componentValuesMap.containsKey(20L)) {
			componentAttributeMap.put("NKMVerpFr", componentValuesMap.get(20L).getExportString());
		}
		if (componentValuesMap.containsKey(21L)) {
			if (componentVersionModel.getWithProvision()) {
				componentAttributeMap.put("AKMKaufteilePotBeist", componentValuesMap.get(21L).getExportString());
			} else {
				componentAttributeMap.put("AKMKaufteilePotBeistBackup", componentValuesMap.get(21L).getExportString());
			}
			componentAttributeMap.put("ME_PreisstandBeistellung", componentValuesRemarkMap.get(21L));
		}
		if (componentValuesMap.containsKey(22L)) {
			componentAttributeMap.put("NKMMonEinarbeitInbANS", componentValuesMap.get(22L).getExportString());
		}
		if (componentValuesMap.containsKey(23L)) {
			componentAttributeMap.put("NKMMonEinarbeitInbS", componentValuesMap.get(23L).getExportString());
		}
		if (componentValuesMap.containsKey(24L)) {
			componentAttributeMap.put("NKMSaZS", componentValuesMap.get(24L).getExportString());
		}
		if (componentValuesMap.containsKey(25L)) {
			componentAttributeMap.put("NKMSoFZS", componentValuesMap.get(25L).getExportString());
		}
		if (componentValuesMap.containsKey(27L) || componentValuesMap.containsKey(28L)
				|| componentValuesMap.containsKey(29L)) {
			BaseBigDecimal v1 = (componentValuesMap.get(27L) != null) ? componentValuesMap.get(27L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v2 = (componentValuesMap.get(28L) != null) ? componentValuesMap.get(28L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v3 = (componentValuesMap.get(29L) != null) ? componentValuesMap.get(29L)
					: new BaseBigDecimal("0");
			componentAttributeMap.put("NKEPlanMethPlanS", (v1.add(v2).add(v3)).getExportString());
		}
		if (componentValuesMap.containsKey(30L)) {
			componentAttributeMap.put("NKEHWKonS", componentValuesMap.get(30L).getExportString());
		}
		if (componentValuesMap.containsKey(31L)) {
			componentAttributeMap.put("NKESWKonLogikS", componentValuesMap.get(31L).getExportString());
		}
		if (componentValuesMap.containsKey(32L)) {
			componentAttributeMap.put("NKEInstPerS", componentValuesMap.get(32L).getExportString());
		}
		if (componentValuesMap.containsKey(33L)) {
			componentAttributeMap.put("NKEInbS", componentValuesMap.get(33L).getExportString());
		}
		if (componentValuesMap.containsKey(34L)) {
			componentAttributeMap.put("NKEInbSSa", componentValuesMap.get(34L).getExportString());
		}
		if (componentValuesMap.containsKey(35L)) {
			componentAttributeMap.put("NKEInbSSoF", componentValuesMap.get(35L).getExportString());
		}

		if (componentValuesMap.containsKey(36L)) {
			componentAttributeMap.put("NKERobProgOnLineS", componentValuesMap.get(36L).getExportString());
		}
		if (componentValuesMap.containsKey(26L) || componentValuesMap.containsKey(37L)
				|| componentValuesMap.containsKey(38L)) {
			BaseBigDecimal v1 = (componentValuesMap.get(37L) != null) ? componentValuesMap.get(37L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v2 = (componentValuesMap.get(38L) != null) ? componentValuesMap.get(38L)
					: new BaseBigDecimal("0");
			BaseBigDecimal v3 = (componentValuesMap.get(26L) != null) ? componentValuesMap.get(26L)
					: new BaseBigDecimal("0");
			componentAttributeMap.put("NKEInstMat", (v1.add(v2).add(v3)).getExportString());
		}
		if (componentValuesMap.containsKey(39L)) {
			if (componentVersionModel.getWithProvision()) {
				componentAttributeMap.put("AKEKaufteilePotBeist", componentValuesMap.get(39L).getExportString());
			} else {
				componentAttributeMap.put("AKEKaufteilePotBeistBackup", componentValuesMap.get(39L).getExportString());
			}
			componentAttributeMap.put("EL_PreisstandBeistellung", componentValuesRemarkMap.get(39L));
		}

		if (componentValuesMap.containsKey(41L)) {
			componentAttributeMap.put("NKESWKonKopplungS", componentValuesMap.get(41L).getExportString());
		}
	}

	private void fillChangesMap(Map<String, String> componentAttributeMap,
			ComponentVersionModel componentVersionModel) {

		// load all last component versions before a given one
		List<ComponentVersionModel> cmpVersionList = componentVersionManager.getLastComponentVersionsBeforeVersion(
				componentVersionModel.getComponent().getComponentId(), componentVersionModel.getComponentVersionId());

		// get the aggregated changes for the last versions
		List<ComponentStandChangeModel> changes = ComponentStandChangeManagerImpl.get()
				.getAggregatedChangesByVersions(cmpVersionList);

		for (ComponentStandChangeModel change : changes) {
			String userFullName = change.getUserFullName();
			String date = change.getDate().getDateSeparatedPt();
			if ("E - Preisstand Datum".equals(change.getChange())) {
				componentAttributeMap.put("NKEKaufteileDatumB", userFullName);
				componentAttributeMap.put("NKEKaufteileDatumD", date);
				continue;
			}
			if ("E - Preisstand Bemerkung".equals(change.getChange())) {
				componentAttributeMap.put("NKEKaufteileInfoB", userFullName);
				componentAttributeMap.put("NKEKaufteileInfoD", date);
				continue;
			}
			if ("M - Preisstand Datum".equals(change.getChange())) {
				componentAttributeMap.put("NKMKaufteileDatumB", userFullName);
				componentAttributeMap.put("NKMKaufteileDatumD", date);
				continue;
			}
			if ("M - Preisstand Bemerkung".equals(change.getChange())) {
				componentAttributeMap.put("NKMKaufteileInfoB", userFullName);
				componentAttributeMap.put("NKMKaufteileInfoD", date);
				continue;
			}
			if ("Leistungsinhalt Elektrik".equals(change.getChange())) {
				componentAttributeMap.put("AKEInhaltVLB", userFullName);
				componentAttributeMap.put("AKEInhaltVLD", date);
				continue;
			}

			// ---------------COST ATTRIBUTES CHANGES--------------------
			// cost attribute 2L,3L,4L
			if ("M - Projektleiter".equals(change.getChange()) || "M - Baustellenleiter".equals(change.getChange())
					|| "M - Planer".equals(change.getChange())) {
				// the last one modified
				if (componentAttributeMap.get("NKMPlanMethPlanB") == null) {
					componentAttributeMap.put("NKMPlanMethPlanB", userFullName);
					componentAttributeMap.put("NKMPlanMethPlanD", date);
				}
				continue;
			}
			// cost attribute 5L
			if ("M - Virtuelle Realität (Process Designer)".equals(change.getChange())) {
				componentAttributeMap.put("NKMRobSimB", userFullName);
				componentAttributeMap.put("NKMRobSimD", date);
				continue;
			}
			// cost attribute 6L
			if ("M - Off-Line-Untersuchung (Simulation)".equals(change.getChange())) {
				componentAttributeMap.put("NKMRobUntersB", userFullName);
				componentAttributeMap.put("NKMRobUntersD", date);
				continue;
			}
			// cost attribute 7L + 8L
			if ("M - CAD-Konstruktion".equals(change.getChange())
					|| "M - Ablaufsimulation".equals(change.getChange())) {
				// the last one modified
				if (componentAttributeMap.get("NKMCADKonB") == null) {
					componentAttributeMap.put("NKMCADKonB", userFullName);
					componentAttributeMap.put("NKMCADKonD", date);
				}
				continue;
			}
			// cost attribute 9L
			if ("M - Off-Line-Programmierung (Simulation)".equals(change.getChange())) {
				componentAttributeMap.put("NKMRobProgB", userFullName);
				componentAttributeMap.put("NKMRobProgD", date);
				continue;
			}
			// cost attribute 10L
			if ("M - CAD-/CAM-Programmierung (2D/3D)".equals(change.getChange())) {
				componentAttributeMap.put("NKMCADCAMProg2D3DB", userFullName);
				componentAttributeMap.put("NKMCADCAMProg2D3DD", date);
				continue;
			}
			// cost attribute 11L
			if ("M - Bankarbeit".equals(change.getChange())) {
				componentAttributeMap.put("NKMBankarbeitB", userFullName);
				componentAttributeMap.put("NKMBankarbeitD", date);
				continue;
			}
			// cost attribute 12L
			if ("M - Kleine Maschine".equals(change.getChange())) {
				componentAttributeMap.put("NKMKleineMaschineB", userFullName);
				componentAttributeMap.put("NKMKleineMaschineD", date);
				continue;
			}
			// cost attribute 13L
			if ("M - Mittlere Maschine".equals(change.getChange())) {
				componentAttributeMap.put("NKMMittlereMaschineB", userFullName);
				componentAttributeMap.put("NKMMittlereMaschineD", date);
				continue;
			}
			// cost attribute 14L
			if ("M - Große Maschine".equals(change.getChange())) {
				componentAttributeMap.put("NKMGrosseMaschineB", userFullName);
				componentAttributeMap.put("NKMGrosseMaschineD", date);
				continue;
			}
			// cost attribute 15L
			if ("M - Messmaschine".equals(change.getChange())) {
				componentAttributeMap.put("NKMMessmaschineB", userFullName);
				componentAttributeMap.put("NKMMessmaschineD", date);
				continue;
			}
			// cost attribute 16L
			if ("M - Fremdanfertigung (als Kaufteil)".equals(change.getChange())) {
				componentAttributeMap.put("NKMFremdanfertigungB", userFullName);
				componentAttributeMap.put("NKMFremdanfertigungD", date);
				continue;
			}
			// cost attribute 17L
			if ("M - Baustahl".equals(change.getChange())) {
				componentAttributeMap.put("NKMBaustahlB", userFullName);
				componentAttributeMap.put("NKMBaustahlD", date);
				continue;
			}
			// cost attribute 18L
			if ("M - Werkzeugstahl, NE-Metalle".equals(change.getChange())) {
				componentAttributeMap.put("NKMWkzgstNEMetalleB", userFullName);
				componentAttributeMap.put("NKMWkzgstNEMetalleD", date);
				continue;
			}
			// cost attribute 1L + 19L
			if ("M - Übergeordnete Kosten".equals(change.getChange()) || "M - Kaufteile".equals(change.getChange())) {
				// the last one modified
				if (componentAttributeMap.get("NKMKaufteileB") == null) {
					componentAttributeMap.put("NKMKaufteileB", userFullName);
					componentAttributeMap.put("NKMKaufteileD", date);
				}
				continue;
			}
			// cost attribute 20L
			if ("M - Verpackung, Fracht".equals(change.getChange())) {
				componentAttributeMap.put("NKMVerpFrB", userFullName);
				componentAttributeMap.put("NKMVerpFrD", date);
				continue;
			}
			// cost attribute 21L
			if ("M - Potentielle Beistellung".equals(change.getChange())) {
				componentAttributeMap.put("AKMKaufteilePotBeistB", userFullName);
				componentAttributeMap.put("AKMKaufteilePotBeistD", date);
				continue;
			}
			// cost attribute 22L
			if ("M - Montage, Inbetriebnahme bei AN".equals(change.getChange())) {
				componentAttributeMap.put("NKMMonEinarbeitInbANB", userFullName);
				componentAttributeMap.put("NKMMonEinarbeitInbAND", date);
				continue;
			}
			// cost attribute 23L
			if ("M - Montage, Inbetriebnahme bei AG".equals(change.getChange())) {
				componentAttributeMap.put("NKMMonEinarbeitInbB", userFullName);
				componentAttributeMap.put("NKMMonEinarbeitInbD", date);
				continue;
			}
			// cost attribute 24L
			if ("M - Samstag-Zuschlag".equals(change.getChange())) {
				componentAttributeMap.put("NKMSaZB", userFullName);
				componentAttributeMap.put("NKMSaZD", date);
				continue;
			}
			// cost attribute 25L
			if ("M - Sonn- und Feiertagszuschlag/Nachtschicht".equals(change.getChange())) {
				componentAttributeMap.put("NKMSoFZB", userFullName);
				componentAttributeMap.put("NKMSoFZD", date);
				continue;
			}
			// cost attribute 27L + 28L + 29L
			if ("E - Projektleiter".equals(change.getChange()) || "E - Baustellenleiter".equals(change.getChange())
					|| "E - Planer".equals(change.getChange())) {
				// the last one modified
				if (componentAttributeMap.get("NKEPlanMethPlanB") == null) {
					componentAttributeMap.put("NKEPlanMethPlanB", userFullName);
					componentAttributeMap.put("NKEPlanMethPlanD", date);
				}
				continue;
			}
			// cost attribute 30L
			if ("E - Konstruktion Hardware (incl. Doku)".equals(change.getChange())) {
				componentAttributeMap.put("NKEHWKonB", userFullName);
				componentAttributeMap.put("NKEHWKonD", date);
				continue;
			}
			// cost attribute 31L
			if ("E - Konstruktion Software".equals(change.getChange())) {
				componentAttributeMap.put("NKESWKonLogikB", userFullName);
				componentAttributeMap.put("NKESWKonLogikD", date);
				continue;
			}
			// cost attribute 32L
			if ("E - Montage, Installation".equals(change.getChange())) {
				componentAttributeMap.put("NKEInstPerB", userFullName);
				componentAttributeMap.put("NKEInstPerD", date);
				continue;
			}
			// cost attribute 33L
			if ("E - Inbetriebnahme".equals(change.getChange())) {
				componentAttributeMap.put("NKEInbB", userFullName);
				componentAttributeMap.put("NKEInbD", date);
				continue;
			}
			// missing attribute 34L,35L
			// cost attribute 36L
			if ("E - Roboter-Online Programmierung".equals(change.getChange())) {
				componentAttributeMap.put("NKERobProgOnLineB", userFullName);
				componentAttributeMap.put("NKERobProgOnLineD", date);
				continue;
			}

			// cost attribute 37L + 38L + 26L
			if ("E - Installationsmaterial".equals(change.getChange()) || "E - Kaufteil".equals(change.getChange())
					|| "E - Übergeordnete Kosten".equals(change.getChange())) {
				// the last one modified
				if (componentAttributeMap.get("NKEInstMatB") == null) {
					componentAttributeMap.put("NKEInstMatB", userFullName);
					componentAttributeMap.put("NKEInstMatD", date);
				}
				continue;
			}
			// cost attribute 39L
			if ("E - Potentielle Beistellung".equals(change.getChange())) {
				componentAttributeMap.put("AKEKaufteilePotBeistB", userFullName);
				componentAttributeMap.put("AKEKaufteilePotBeistD", date);
				continue;
			}
			// WithProvision Value
			if ("Beistellung AG".equals(change.getChange())) {
				componentAttributeMap.put("BeistellungenB", userFullName);
				componentAttributeMap.put("BeistellungenD", date);
				continue;
			}

		}
	}

	private String nullToEmptyConverter(Object value) {
		return nullToSpecificValueConverter(value, "");
	}

	private String nullToSpecificValueConverter(Object value, String nullValue) {
		if (value == null) {
			return nullValue;
		}
		return value.toString();
	}

	private String baseDateTimeToExportDateConverter(BaseDateTime date) {
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy H:mm:ss");
			return dateFormat.format(date.getDate());
		}
		return null;
	}

	private File templateFile(boolean withoutAttributes , String exportTarget) {
		StringBuilder templatePath = new StringBuilder();
		templatePath.append(getAbsoluteClassDirPath());
		templatePath.append(File.separator);
		templatePath.append("templates");
		templatePath.append(File.separator);
		templatePath.append("xml");
		templatePath.append(File.separator);

		StringBuilder fileName = new StringBuilder("component");
		if (withoutAttributes) {
			fileName.append("_without_attributes");
		}
		if("connect".equals(exportTarget)) {
			fileName.append("_connect");
		}

		fileName.append(".xml");

		templatePath.append(fileName);

		File template = new File(templatePath.toString());
		// @todo: use property to declare the file path
		if (!template.exists()) {
			log.info(template + " File not found");
			template = new File("/appl/webappl/process_designer/appl/wl_config/templates/xml/" + fileName.toString());
		}
		
		log.info("Use file: " + template);
		return template;
	}


	// -------------------- end xml export

	/**
	 * get Last InUse ComponentVersion
	 */
	public ComponentVersionModel getLastInUseComponentVersion(CostComponentDTO costComponentDTO,
			BaseDateTime beforeDate) {
		ComponentVersionModel componentVersion = componentVersionManager.getLastComponentVersionBeforeDate(
				costComponentDTO.getComponentVersion().getComponent().getComponentId(), beforeDate);
		if (componentVersion != null) {
			Long catalogId = HourlyRateCatalogManager.DEFAULT_CATALOG_ID;
			if (costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId() != null) {
				catalogId = costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId();
			}
			componentVersion.setCatalogModel(hourlyRateCatalogManager.getObject(catalogId));
			componentVersion.getComponentVersionStands().size();
			componentVersionManager.loadDependencies(
					new ArrayList<ComponentStandModel>(componentVersion.getComponentVersionStands()).get(0));
		}
		return componentVersion;
	}

	public CostComponentDTO loadChangesForStand(CostComponentDTO costComponentDTO) {
		ComponentStandModel stand = costComponentDTO.getComponentStand();
		if (stand != null && stand.getId() != null) {
			// List<ComponentElementModel> dBElements = componentElementManager
			// .getComponentElementListByComponentStandId(stand.getId());
			List<ComponentElementModel> dBElements = stand.getComponentVersion().getAvailableComponentElementList();
			List<ComponentStandChangeModel> changes = ComponentStandChangeManagerImpl.get()
					.getAggregatedStandChanges(stand, dBElements);
			costComponentDTO.setVersionChanges(changes);
		}
		return costComponentDTO;
	}

	public ComponentVersionModel getNextComponentVersion(Long componentVersionId, Long componentId) {
		return componentVersionManager.getNextComponentVersion(componentVersionId, componentId);
	}

	/**
	 * Set a Baustein stand as used for a brand save an entry in baustein version change
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO saveComponentStandAsUsed(CostComponentDTO costComponentDTO) {
		ComponentStandModel componentStand = costComponentDTO.getComponentStand();
		UserModel user = costComponentDTO.getUserLogged();
		if (user.getDefaultBrand() != null && componentStand != null) {
			BrandModel brand = getDefaultBrand(user.getDefaultBrand());
			ComponentModel currentComponent = componentStand.getComponentVersion().getComponent();
			ComponentModel componentWithSameExternalId = componentManager
					.getComponentByExternalIdAndLib(currentComponent.getExternalId(), user.getDefaultLibrary());
			if (componentWithSameExternalId != null) {
				log.info("currently a component with same external Id(" + currentComponent.getExternalId()
						+ ") exist in the library of the user(" + user.getDefaultLibrary() + ")");
				costComponentDTO.setComponentWithSameExternalId(componentWithSameExternalId.getNumberAndDesignation());
				return costComponentDTO;
			}

			// check if there is an other component version already used
			ComponentStandUsersModel oldUsers = componentVersionUsersManager
					.getComponentUsers(componentStand.getComponentVersion().getComponent().getComponentId(), brand);
			if (oldUsers != null) {
				log.info("currently used component stand and issue of transition to new item stand. ");
				costComponentDTO.setOldVersionUsed(Boolean.TRUE);
				String usedVersion = oldUsers.getComponentStand().getNumber() != null
						? oldUsers.getComponentStand().getNumber().toString()
						: "";
				costComponentDTO.setUsedVersion(usedVersion);
			}

			// start check for related element to be used when using baustein
			List<ComponentElementModel> componentElements = componentElementManager
					.getComponentElementListByComponentStandId(componentStand.getId());
			loadAffectedElementByUseComponent(componentStand, brand, costComponentDTO, componentElements);
			// end check for related element to be used when using baustein
			if (costComponentDTO.getOldVersionUsed()
					|| !CollectionUtils.isEmpty(costComponentDTO.getRelatedComponentsByElement())) {
				return costComponentDTO;
			}
			// setting component stand as used
			ComponentStandUsersModel standUsers = new ComponentStandUsersModel();
			standUsers.setBrand(brand);
			standUsers.setComponentStand(componentStand);
			log.info("use the component Stand: " + componentStand.getId() + " For Marke: " + brand.getBrandId());
			componentVersionUsersManager.saveObject(standUsers);
			// add change entry
			ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.USE_STAND.value(), "",
					brand.getBrandId(), componentStand, user, Boolean.FALSE, ChangeType.USE_STAND);
			saveLinkedElementAsUsedByUsingComponent(user, componentElements);
			costComponentDTO.setComponentStandUsed(Boolean.TRUE);
			costComponentDTO.setComponentStandCanBeUsed(Boolean.FALSE);
			costComponentDTO.getComponentStand().getComponentVersionUsers().add(standUsers);
			loadChangesForStand(costComponentDTO);
		}
		return costComponentDTO;
	}

	/**
	 * 
	 * @param componentStand
	 * @param brand
	 * @param costComponentDTO
	 */
	private void loadAffectedElementByUseComponent(ComponentStandModel componentStand, BrandModel brand,
			CostComponentDTO costComponentDTO, List<ComponentElementModel> componentElements) {
		if (componentStand != null && componentStand.getId() != null) {
			Map<String, String> componentsByElement = new HashMap<String, String>();
			Map<BrandModel, List<ElementModel>> elementByBrand = new HashMap<BrandModel, List<ElementModel>>();
			for (ComponentElementModel componentElement : componentElements) {
				ElementVersionModel elementVersion = componentElement.getElementVersion();
				// only not my elements
				if (!elementVersion.getElement().getOwner().equals(brand)) {
					// element version not used by me
					if (elementVersion.getUsers() == null || !elementVersion.getUsers().contains(brand)) {

						StringBuffer components = new StringBuffer();
						// load on demand
						List<ElementModel> list = new ArrayList<ElementModel>();
						list.add(elementVersion.getElement());
						elementByBrand.put(brand, list);
						List<ComponentStandModel> componentStandsLinked = componentVersionStandManager
								.getComponentVersionStandByNotOwnerLinkedElement(elementByBrand);
						if (componentStandsLinked != null && !componentStandsLinked.isEmpty()) {
							Set<String> relatedComponents = new HashSet<String>();

							for (ComponentStandModel oldStand : componentStandsLinked) {
								// new stand can be created only in last version in use
								// only last version
								if (componentVersionManager.isTheLastComponentVersion(
										oldStand.getComponentVersion().getComponentVersionId(),
										oldStand.getComponentVersion().getComponent().getComponentId(),
										EnumStatusManager.Status.IN_USE.value())) {
									relatedComponents.add(
											oldStand.getComponentVersion().getComponent().getNumberAndDesignation());
								}
							}
							if (!relatedComponents.isEmpty()) {
								String delimiter = "";
								for (String name : relatedComponents) {
									components.append(delimiter);
									components.append(name);
									delimiter = ";";

								}
							}
						}
						componentsByElement.put(elementVersion.getElement().getNumberAndDesignation(),
								components.toString());
					}
				}
			}
			if (!componentsByElement.isEmpty()) {
				costComponentDTO.setRelatedComponentsByElement(componentsByElement);
			}
		}
	}

	private void saveLinkedElementAsUsedByUsingComponent(UserModel user,
			List<ComponentElementModel> componentElements) {
		for (ComponentElementModel assignedComponentElement : componentElements) {
			saveElementVersionAsUsed(assignedComponentElement, user);
		}
	}

	/**
	 * Set a Baustein stand as used for a brand save an entry in baustein version change
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO saveChangeUseComponentStand(CostComponentDTO costComponentDTO) {
		ComponentStandModel componentStand = costComponentDTO.getComponentStand();
		UserModel user = costComponentDTO.getUserLogged();
		if (user.getDefaultBrand() != null && componentStand != null) {
			BrandModel brand = getDefaultBrand(user.getDefaultBrand());
			ComponentStandUsersModel oldUsers = componentVersionUsersManager
					.getComponentUsers(componentStand.getComponentVersion().getComponent().getComponentId(), brand);
			// check if there is an other component version already used
			if (oldUsers != null) {
				costComponentDTO.getComponentStand().getComponentVersionUsers().remove(oldUsers);
				componentVersionUsersManager.removeObject(oldUsers.getId());
				log.info("remove the use of component Stand: " + oldUsers.getComponentStand().getId() + " For Marke: "
						+ brand.getBrandId());

				// add change entry
				ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(),
						brand.getBrandId(), "", oldUsers.getComponentStand(), user, Boolean.FALSE,
						ChangeType.UNUSE_STAND);
			}
			ComponentStandUsersModel standUsers = new ComponentStandUsersModel();
			standUsers.setBrand(brand);
			standUsers.setComponentStand(componentStand);
			log.info("use the component Stand: " + componentStand.getId() + " For Marke: " + brand.getBrandId());
			componentVersionUsersManager.saveObject(standUsers);
			// add change entry
			ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.USE_STAND.value(), "",
					brand.getBrandId(), componentStand, user, Boolean.FALSE, ChangeType.USE_STAND);
			List<ComponentElementModel> componentElements = componentElementManager
					.getComponentElementListByComponentStandId(componentStand.getId());
			saveLinkedElementAsUsedByUsingComponent(user, componentElements);
			costComponentDTO.setComponentStandUsed(Boolean.TRUE);
			costComponentDTO.setComponentStandCanBeUsed(Boolean.FALSE);

			costComponentDTO.getComponentStand().getComponentVersionUsers().add(standUsers);
			loadChangesForStand(costComponentDTO);

		}
		return costComponentDTO;
	}

	/**
	 * Set a Baustein stand as unused for a brand save an entry in baustein version change
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO saveComponentStandAsUnUsed(CostComponentDTO costComponentDTO) {
		UserModel user = costComponentDTO.getUserLogged();
		ComponentStandModel componentStand = costComponentDTO.getComponentStand();
		if (user.getDefaultBrand() != null && componentStand != null) {
			BrandModel brand = brandManager.getObject(user.getDefaultBrand());
			ComponentStandUsersModel standUsers = componentVersionUsersManager
					.getComponentStandUsers(componentStand.getId(), brand);
			if (standUsers != null) {
				costComponentDTO.getComponentStand().getComponentVersionUsers().remove(standUsers);
				componentVersionUsersManager.removeObject(standUsers.getId());
				log.info("unuse the component Stand: " + componentStand.getId() + " For Marke: " + brand.getBrandId());
				// add change entry
				ComponentStandChangeManagerImpl.get().generateChangeTypeEntry(ChangeTypeValue.UNUSE_STAND.value(),
						brand.getBrandId(), "", componentStand, user, Boolean.FALSE, ChangeType.UNUSE_STAND);

				costComponentDTO.setComponentStandUsed(Boolean.FALSE);
				costComponentDTO.setComponentStandCanBeUsed(Boolean.TRUE);
				loadChangesForStand(costComponentDTO);
			}
		}

		return costComponentDTO;
	}

	/**
	 * Check if an component stand is used or not
	 * 
	 * @param costElementDTO
	 * @return
	 */
	public CostComponentDTO checkComponentStandUse(CostComponentDTO costComponentDTO) {
		UserModel user = costComponentDTO.getUserLogged();
		ComponentStandModel componentStand = costComponentDTO.getComponentStand();
		if (user.getDefaultBrand() != null && componentStand != null) {
			BrandModel brand = brandManager.getObject(user.getDefaultBrand());

			Boolean isComponentStandUsed = componentVersionUsersManager.getComponentStandUsers(componentStand.getId(),
					brand) != null ? Boolean.TRUE : Boolean.FALSE;
			costComponentDTO.setComponentStandUsed(isComponentStandUsed);
			if (!isComponentStandUsed) {
				Boolean isComponentStandCanBeUsed = isComponentStandCanBeUsed(componentStand, brand);
				costComponentDTO.setComponentStandCanBeUsed(isComponentStandCanBeUsed);
			}
		}
		return costComponentDTO;
	}

	/**
	 * Check all the conditions to use an element version
	 * 
	 * @param componentStand
	 * @param brand
	 * @return
	 */
	private Boolean isComponentStandCanBeUsed(ComponentStandModel componentStand, BrandModel brand) {

		// if the component version is not in use or approved
		if (!componentStand.getComponentVersion().getEnumStatus()
				.equals(enumStatusManager.getObject(EnumStatusManager.Status.IN_USE.value()))
				&& !componentStand.getComponentVersion().getEnumStatus()
						.equals(enumStatusManager.getObject(EnumStatusManager.Status.APPROVED.value()))) {
			log.info("The status of the component version is not IN_USE or APPROVED");
			return Boolean.FALSE;
		}

		// If the user brand is the owner
		if (componentStand.getComponentVersion().getComponent().getOwner() != null
				&& componentStand.getComponentVersion().getComponent().getOwner().equals(brand)) {
			log.info("The user brand is the same as the owner of the component");
			return Boolean.FALSE;
		}
		// if the brand already used this version
		if (componentVersionUsersManager.getComponentStandUsers(componentStand.getId(), brand) != null) {
			log.info("The brand " + brand.getBrandId() + " already use this version");
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Return the default brand
	 * 
	 * @param defaultBrand
	 * @return
	 */
	public BrandModel getDefaultBrand(String defaultBrand) {
		return brandManager.getObject(defaultBrand);
	}

	/**
	 * Return the icon of the library
	 * 
	 * @param defaultLib
	 * @return
	 */
	public String getDefaultLibraryIcon(Long defaultLib) {
		return libraryManager.getObject(defaultLib).getColor();
	}

	/**
	 * 
	 * @param component
	 * @return
	 */
	public List<ComponentStandUsersModel> loadComponentVersionUsers(ComponentModel component) {
		return componentVersionUsersManager.getComponentUsers(component.getComponentId());
	}

	/**
	 * method used to change display order of component in its folder
	 * 
	 * @param objectId
	 *            ,
	 * @param folderId
	 *            ,
	 * @param actionToDo
	 */
	public void saveNewOrder(Long objectId, Long folderId, String actionToDo) {
		componentManager.performComponentChangeOrder(objectId, folderId, actionToDo);
	}

	/**
	 * Load user list with the required libraries
	 * 
	 * @param costComponentDTO
	 * @return
	 */
	public CostComponentDTO loadBrandUsersAndLibraries(CostComponentDTO costComponentDTO) {
		// Load brands for the export mask & libraries
		costComponentDTO.setBrandUsers(brandManager.getObjects());
		// libraries
		costComponentDTO.setLibraries(libraryManager.getObjects());
		return costComponentDTO;
	}

	/******************** Start Excel export methods **************************/
	public Object[] loadExcelExportFile(BaseDateTime date, CostComponentDTO dto) throws Exception {
		ComponentVersionModel componentVersion = getLastInUseComponentVersion(dto, date);
		Object[] fileNameAndContent = null;
		if (componentVersion != null) {
			log.info("Excel Export for component Version " + componentVersion);
			log.info("Excel Export Date " + date.getDateSeparatedPt());
			File template = new File(getAbsoluteClassDirPath() + File.separator + "templates" + File.separator + "excel"
					+ File.separator + "Baustein_Export.xls");
			// @todo: use property to declare the file path
			if (!template.exists()) {
				log.info(template + " File not found");
				template = new File(
						"/appl/webappl/process_designer/appl/wl_config/templates/excel/Baustein_Export.xls");
				log.info("Use file: " + template);
			}
			HSSFWorkbook wb = null;
			FileInputStream fis;
			fis = new FileInputStream(template);
			wb = new HSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			// Sheet name should contain the Baustein name
			String sheetName = removeSpecialChars(
					"Baustein Export " + componentVersion.getComponent().getDesignation());
			sheetName = WorkbookUtil.createSafeSheetName(sheetName);
			wb.setSheetName(0, sheetName);
			// fill header
			fillExcelHeader(componentVersion, sheet, wb);
			List<ComponentValueModel> componentValues = componentVersion.getComponentValuesForView();
			// sort the list depends on excel order number
			if (!componentValues.isEmpty()) {
				Collections.sort(componentValues, new Comparator<ComponentValueModel>() {
					@Override
					public int compare(ComponentValueModel o1, ComponentValueModel o2) {
						int result = 0;
						if (o1 != null && o2 != null) {
							result = o1.getCostAttribute().getOrderNumber()
									.compareTo(o2.getCostAttribute().getOrderNumber());
						}
						return result;
					}
				});
			}
			// fill Stundensatz (in €) line
			Row row = sheet.getRow(sheet.getFirstRowNum() + 4);
			int i = 3;
			// In this iterate we will get the value of the kaufteil factor (M: id 19 and E: id 3.83)
			BaseBigDecimal mechanicFactor = null;
			BaseBigDecimal electricFactor = null;
			for (ComponentValueModel componentValue : componentValues) {
				HourlyRateModel rateModel = componentValue.getRateModel();
				if (rateModel != null) {
					CostAttributeModel costAttribute = rateModel.getCostAttribute();
					if (costAttribute.getCostAttributeId().equals(Long.valueOf(19))) {
						mechanicFactor = rateModel.getFactor();
					}
					if (costAttribute.getCostAttributeId().equals(Long.valueOf(38))) {
						electricFactor = rateModel.getFactor();
					}
				}
			}
			for (ComponentValueModel componentValue : componentValues) {
				// use the value of the factor depends on category of the cost attribute
				CostAttributeModel costAttribute = componentValue.getCostAttribute();
				BaseBigDecimal factor = mechanicFactor;
				if (CostCategory.ELECTRICAL.value()
						.equals(costAttribute.getCostAttributeCategory().getEnumInvestmentPart().getCategory())) {
					factor = electricFactor;
				}
				Cell cell = row.getCell(i);
				if (componentValue.getCostAttribute().getUnit().equals("Std.")
						|| componentValue.getCostAttribute().getUnit().equals("Kg")) {
					cell.setCellValue("");
					HourlyRateModel rateModel = componentValue.getRateModel();
					cell.setCellValue(rateModel.getValue().doubleValue());
				}
				// the cost attribute that will be calculated using the factor values are:
				// 1. depends from factor and NOT depend from provision
				// 2. depends from factor AND from provision AND the calculation with provision flag is false
				if ((costAttribute.getDependOnFactor() && !costAttribute.getDependFromProvision())
						|| (costAttribute.getDependOnFactor() && costAttribute.getDependFromProvision()
								&& !componentVersion.getWithProvision())) {
					BaseBigDecimal rate = factor.multiply(new BaseBigDecimal("0.01"));
					cell.setCellValue(rate.doubleValue());
				}

				i++;
			}

			// fill Amount
			row = sheet.getRow(sheet.getFirstRowNum() + 13);
			Cell cell = row.getCell(0);
			cell.setCellValue(componentVersion.getComponent().getComponentNumber());
			// 28.08.2013: ZS: PTS_Requirement-22208: Make BS name in the BS data row visible
			cell = row.getCell(2);
			cell.setCellValue(componentVersion.getComponent().getDesignation());

			i = 3;
			for (ComponentValueModel componentValue : componentValues) {
				cell = row.getCell(i);
				if (componentValue.getCostAttribute().getPricePerUnit()) {
					if (componentValue.getNumber() != null && !componentValue.getNumber().isNull()) {
						cell.setCellValue(componentValue.getNumber().doubleValue());
					}
				} else if (componentValue.getAmount() != null && !componentValue.getAmount().isNull()) {
					if (componentValue.getCostAttribute().getCostAttributeId() != 19
							|| componentValue.getCostAttribute().getCostAttributeId() != 38) {
						cell.setCellValue(componentValue.getAmount().doubleValue());
					}
				}
				i++;
			}

			// fill Elemente list
			List<ComponentElementModel> availableComponentElementList = componentVersion
					.getAvailableComponentElementList();
			int rowIndx = 15;

			fillElementList(availableComponentElementList, rowIndx, sheet);

			/**
			 * Ticket_id: 22144 Responsible: ABA We should add evaluator.clearAllCachedResultValues() before updating
			 * cell Because if evaluateFormula method is invoked on the same cell it could cause an
			 * ArrayIndexOutOfBoundsException
			 **/
			// re-calculate with formulas
			FormulaEvaluator evaluator = wb.getCreationHelper().createFormulaEvaluator();
			for (Row r : sheet) {
				for (Cell c : r) {
					if (c.getCellType() == Cell.CELL_TYPE_FORMULA) {
						evaluator.clearAllCachedResultValues();
						evaluator.evaluateFormulaCell(c);
					}
				}
			}
			// crate the excel file
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			wb.write(stream);
			fileNameAndContent = new Object[2];
			// 31.10.2013: ZS: PTS_Change-26380: the Excel export file name will contains the BS number
			String componentNumber = componentVersion.getComponent().getComponentNumber().replaceAll("\\s+", "_");
			componentNumber = componentNumber.replaceAll("[:\\\\/*\"?|<>']", "");
			fileNameAndContent[0] = componentNumber + "_Baustein_Export_" + date.getDateSeparatedPt() + ".xls";
			fileNameAndContent[1] = stream.toByteArray();

		}
		return fileNameAndContent;
	}

	/**
	 * fill the excel export file header
	 * 
	 * @param componentVersion
	 * @param sheet
	 */
	private void fillExcelHeader(ComponentVersionModel componentVersion, Sheet sheet, HSSFWorkbook wb) {

		// fill Objekttyp
		Row row = sheet.getRow(sheet.getFirstRowNum());
		Cell cell = row.getCell(3);
		cell.setCellValue((componentVersion.getEnumComponentType() != null)
				? componentVersion.getEnumComponentType().getComponentType()
				: "");

		row = sheet.getRow(sheet.getFirstRowNum() + 1);

		// fill Ersteller
		cell = row.getCell(2);
		String creatorName = "";
		// imported version without creator so we need check
		UserModel creator = componentVersion.getCreator();
		if (creator != null) {

			if (creator.getIsExternal() != null && creator.getIsExternal()) {
				creatorName = componentVersion.getCreator().getLastName() + " / "
						+ componentVersion.getCreator().getFirstName() + " im Auftrag von: " + creator.getOnBehalfOf();

			} else {
				creatorName = componentVersion.getCreator().getLastName() + " / "
						+ componentVersion.getCreator().getFirstName();
			}
		}

		cell.setCellValue(creatorName);
		CellStyle cs = cell.getCellStyle();
		cs.setWrapText(true);
		cell.setCellStyle(cs);
		// fill Bezeichnung
		cell = row.getCell(6);
		String designation = componentVersion.getComponent().getDesignation();
		int startIndexFont = designation.length();
		int endIndexFont = startIndexFont;
		if (componentVersion.getDescription() != null && !"".equals(componentVersion.getDescription())) {
			String labelRemark = " " + "Kommentar: " + " ";
			designation += labelRemark + componentVersion.getDescription();
			endIndexFont = startIndexFont + labelRemark.length();
		}
		HSSFRichTextString designationAndRemark = new HSSFRichTextString(designation);
		if (endIndexFont != startIndexFont) {
			Font redFont = wb.createFont();
			redFont.setColor(IndexedColors.RED.getIndex());
			designationAndRemark.applyFont(startIndexFont, endIndexFont, redFont);
		}
		cell.setCellValue(designationAndRemark);

		// fill Bausteinnummer
		cell = row.getCell(43);
		cell.setCellValue(componentVersion.getComponent().getComponentNumber());

		row = sheet.getRow(sheet.getFirstRowNum() + 2);

		// fill Erstelld
		cell = row.getCell(2);
		cell.setCellValue(componentVersion.getCreationDate().getString(BaseDateTime.DD_MM_YYYY, "/"));

		// fill Bausteinversion
		cell = row.getCell(43);
		cell.setCellValue(componentVersion.getNumber() != null ? "Version " + componentVersion.getNumber() : "");
	}

	/**
	 * fill Excel export with element list
	 * 
	 * @param availableComponentElementList
	 * @param rowIndx
	 * @param sheet
	 */
	private void fillElementList(List<ComponentElementModel> availableComponentElementList, int rowIndx, Sheet sheet) {

		for (ComponentElementModel componentElement : availableComponentElementList) {
			Row row = sheet.getRow(rowIndx);
			if (row == null) {
				row = sheet.createRow((short) sheet.getFirstRowNum() + rowIndx);
			}
			Cell cell = row.getCell(0);
			if (cell == null) {
				cell = row.createCell(0);
				cell.setCellStyle(sheet.getRow(rowIndx - 1).getCell(0).getCellStyle());
			}
			cell.setCellValue(componentElement.getElementVersion().getElement().getElementNumber());
			cell = row.getCell(1);
			if (cell == null) {
				cell = row.createCell(1);
				cell.setCellStyle(sheet.getRow(rowIndx - 1).getCell(1).getCellStyle());
			}
			cell.setCellValue(componentElement.getNumber());
			cell = row.getCell(2);
			if (cell == null) {
				cell = row.createCell(2);
				cell.setCellStyle(sheet.getRow(rowIndx - 1).getCell(2).getCellStyle());
			}
			cell.setCellValue(componentElement.getElementVersion().getElement().getDesignation());
			int elementIndx = 3;
			if (componentElement.getElementVersion() != null) {
				List<ElementValueModel> elementValues = componentElement.getElementVersion().getElementValuesForView();
				// sort the list depends on excel order number
				if (!elementValues.isEmpty()) {
					Collections.sort(elementValues, new Comparator<ElementValueModel>() {
						@Override
						public int compare(ElementValueModel o1, ElementValueModel o2) {
							int result = 0;
							if (o1 != null && o2 != null) {
								result = o1.getCostAttribute().getOrderNumber()
										.compareTo(o2.getCostAttribute().getOrderNumber());
							}
							return result;
						}
					});
				}
				for (ElementValueModel elementValue : elementValues) {
					cell = row.getCell(elementIndx);
					if (cell == null) {
						cell = row.createCell(elementIndx);
					}
					if (rowIndx > 15) {
						cell.setCellStyle(sheet.getRow(rowIndx - 1).getCell(elementIndx).getCellStyle());
					}
					if (elementValue.getCostAttribute().getPricePerUnit()) {
						if (elementValue.getNumber() != null && !elementValue.getNumber().isNull()) {
							cell.setCellValue(elementValue.getNumber().doubleValue());
						}
					} else if (elementValue.getAmount() != null && !elementValue.getAmount().isNull()) {
						cell.setCellValue(elementValue.getAmount().doubleValue());
					}
					elementIndx++;
				}
			}
			cell = row.getCell(elementIndx);
			if (cell == null) {
				cell = row.createCell(elementIndx);
				cell.setCellStyle(sheet.getRow(rowIndx - 1).getCell(elementIndx).getCellStyle());
				cell.setCellFormula(
						"SUMPRODUCT(D" + (rowIndx + 1) + ":AP" + (rowIndx + 1) + ",D$5:AP$5)*$B" + (rowIndx + 1));
			}
			rowIndx++;
		}
	}

	/**
	 * replace special characters for excel show
	 * 
	 * @param String
	 * @return String
	 */
	private String removeSpecialChars(String inputString) {
		String outputString = inputString.replace("/", "&#47;");
		outputString = outputString.replace("\\", "&#92;");
		outputString = outputString.replace("[", "&#91;");
		outputString = outputString.replace("]", "&#93;");
		outputString = outputString.replace("*", "&#42;");
		return outputString;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public CostComponentDTO getIfComponentCanBeActivated(CostComponentDTO costComponentDTO) {
		ComponentStandModel componentStand = costComponentDTO.getComponentStand();
		UserModel user = costComponentDTO.getUserLogged();
		ComponentModel currentComponent = componentStand.getComponentVersion().getComponent();
		log.info("Check if BS can be activaed again");
		ComponentModel componentWithSameExternalId = componentManager
				.getComponentByExternalIdAndLib(currentComponent.getExternalId(), null);
		if (componentWithSameExternalId != null) {
			ComponentStandUsersModel componentUsed = componentVersionUsersManager
					.getComponentUsers(componentWithSameExternalId.getComponentId(), currentComponent.getOwner());
			if (componentUsed != null) {
				log.info("currently a used component with same external Id(" + currentComponent.getExternalId()
						+ ") exist in the library of the user(" + user.getDefaultLibrary() + ")");
				costComponentDTO.setComponentWithSameExternalId(componentWithSameExternalId.getNumberAndDesignation());

			}

		}
		return costComponentDTO;
	}

	/******************** End Excel export methods **************************/

	// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
	/**
	 * @param dto
	 * @param isLocked
	 */
	public void saveComponentVersionLockStatus(CostComponentDTO dto, boolean isLocked) {
		ComponentVersionModel version = dto.getComponentVersion();
		version.setIsLocked(isLocked);
		// 11.11.2013: ZS: PTS_Requirement-26761: Set the user who lock the BS in the release process
		version.setModifier(dto.getUserLogged());
		componentVersionManager.saveObject(version);
		log.info("Lock staus is changed to: " + isLocked + " for version " + version);
	}

	/**
	 * 
	 * @param costComponentDTO
	 * @param objectId
	 * @param folderId
	 * @param componentPart
	 * @param lastVersion
	 * @return
	 */
	public CostComponentDTO loadDTOForCopy(CostComponentDTO costComponentDTO, Long objectId, Long folderId,
			String componentPart, boolean lastVersion) {

		// if object in DB load it else create a new one
		if (objectId != null) {
			costComponentDTO.setComponentId(objectId);
			ComponentVersionModel componentVersion = loadLastComponentVersionByComponent(
					costComponentDTO.getComponentId(), lastVersion);
			if (componentVersion != null) {
				costComponentDTO.setComponentId(componentVersion.getComponent().getComponentId());
				costComponentDTO.setComponentVersionId(componentVersion.getComponentVersionId());

				ComponentStandModel componentStand = null;
				if (componentVersion != null) {
					componentStand = new ArrayList<ComponentStandModel>(componentVersion.getComponentVersionStands())
							.get(0);
				}
				costComponentDTO.setComponentStand(componentStand);
				log.info("Load version for copy purpose " + componentVersion);
			}
			Long defaultCatalogId = HourlyRateCatalogManager.DEFAULT_CATALOG_ID;
			if (costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId() != null) {
				defaultCatalogId = costComponentDTO.getUserLogged().getDefaultStdSatzKatRefId();
			}

			ComponentVersionModel cmpVer = costComponentDTO.getComponentVersion();
			cmpVer.setStatusElementPublish(costComponentDTO.getUserLogged().getStatusElementPublish());
			cmpVer.setCatalogModel(hourlyRateCatalogManager.getObject(defaultCatalogId));

			componentVersionManager.loadDependencies(costComponentDTO.getComponentStand());

			// load documents
			loadDocuments(costComponentDTO);

			List<ComponentVersionModel> componentVersions = costComponentDTO.getVersionHistoryList();
			List<Long> withoutToStatus = new ArrayList<Long>();
			if (!componentVersions.isEmpty()) {
				ComponentVersionModel lastComponentVersion = componentVersions.get(0);
				if (!componentVersion.equals(lastComponentVersion)) {
					withoutToStatus.add(EnumStatusManager.Status.IN_PROGRESS.value());
				}
			}
			List<EnumTransitionModel> transitions = enumStatusManager
					.getAllowedTransitions(componentVersion.getEnumStatus(), Boolean.FALSE, withoutToStatus);
			costComponentDTO.setAllowedTransitions(transitions);

		}
		return costComponentDTO;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public CostComponentDTO getChangeDetailByVersion(CostComponentDTO dto) {
		List<ComponentStandChangeModel> changeDetails = ComponentStandChangeManagerImpl.get()
				.getChangesByStandAndChange(dto.getComponentStand().getId(), dto.getChangeToShow());
		dto.setChangeDetails(changeDetails);
		return dto;
	}

	/******************************** end transactional methods **/

	/******************************* start manager setter **/
	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public void setComponentValueManager(ComponentValueManager componentValueManager) {
		this.componentValueManager = componentValueManager;
	}

	public void setEnumStatusManager(EnumStatusManager enumStatusManager) {
		this.enumStatusManager = enumStatusManager;
	}

	public void setEnumComponentTypeManager(EnumComponentTypeManager enumComponentTypeManager) {
		this.enumComponentTypeManager = enumComponentTypeManager;
	}

	public void setEnumComponentClassManager(EnumComponentClassManager enumComponentClassManager) {
		this.enumComponentClassManager = enumComponentClassManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setDocumentPoolManager(DocumentPoolManager documentPoolManager) {
		this.documentPoolManager = documentPoolManager;
	}

	public ElementManager getElementManager() {
		return elementManager;
	}

	public void setElementManager(ElementManager elementManager) {
		this.elementManager = elementManager;
	}

	public void setHourlyRateCatalogManager(HourlyRateCatalogManager hourlyRateCatalogManager) {
		this.hourlyRateCatalogManager = hourlyRateCatalogManager;
	}

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setLibraryManager(LibraryManager libraryManager) {
		this.libraryManager = libraryManager;
	}

	public void setElementVersionUsersManager(ElementVersionUsersManager elementVersionUsesManager) {
		this.elementVersionUsersManager = elementVersionUsesManager;
	}

	public void setComponentVersionStandManager(ComponentVersionStandManager componentVersionStandManager) {
		this.componentVersionStandManager = componentVersionStandManager;
	}

	public void setComponentVersionUsersManager(ComponentVersionUsersManager componentVersionUsersManager) {
		this.componentVersionUsersManager = componentVersionUsersManager;
	}

	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	public void setWorkAreaManager(WorkAreaManager workAreaManager) {
		this.workAreaManager = workAreaManager;
	}

	/******************************* end manager setter **/

}