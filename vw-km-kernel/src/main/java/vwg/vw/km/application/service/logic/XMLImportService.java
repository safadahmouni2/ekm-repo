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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.ComponentManager;
import vwg.vw.km.application.implementation.ComponentStandChangeManager;
import vwg.vw.km.application.implementation.ComponentValueManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.ComponentVersionStandManager;
import vwg.vw.km.application.implementation.CostAttributeManager;
import vwg.vw.km.application.implementation.EnumComponentClassManager;
import vwg.vw.km.application.implementation.EnumComponentTypeManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.implementation.LibraryManager;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.EnumComponentClassModel;
import vwg.vw.km.integration.persistence.model.EnumComponentTypeModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;

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
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.7 $ $Date: 2015/12/11 08:00:44 $
 */
public class XMLImportService {

	private final Log log = LogManager.get().getLog(XMLImportService.class);

	public FolderManager folderManager;

	public ComponentManager componentManager;

	public EnumObjectTypeManager enumObjectTypeManager;

	public CostAttributeManager costAttributeManager;

	public EnumComponentClassManager enumComponentClassManager;

	public EnumComponentTypeManager enumComponentTypeManager;

	public BrandManager brandManager;

	public LibraryManager libraryManager;

	private ComponentStandChangeManager componentStandChangeManager;

	private ComponentVersionManager componentVersionManager;

	private ComponentValueManager componentValueManager;

	private EnumStatusManager enumStatusManager;

	private ComponentVersionStandManager componentVersionStandManager;

	/**
	 * save Imported Folder
	 * 
	 * @param folderModel
	 * @param isNew
	 */
	public void saveImportedFolder(FolderModel folder, boolean isNew) {
		if (isNew) {
			if (folder.getFolder() == null) {
				folder.setDesignation(folder.getDesignation() + (folderManager.getMaxFolderId() + 1));
			}

			folder.setObjectType(enumObjectTypeManager.getObject(2L));
		} else {
			folder.setDesignation(folder.getDesignation().substring(0,
					folder.getDesignation().length() - (folder.getFolderId().toString().length())));
			FolderModel oldFolder = folderManager.getFolderUnderParentByName(
					(folder.getFolder() != null ? folder.getFolder().getFolderId() : null), folder.getFolderId(),
					folder.getDesignation());
			if (oldFolder != null) {
				log.info("--------------------- : Same Folder Exist" + folder.getDesignation());
				folder.setDesignation(folder.getDesignation() + " ");
			}
		}
		folderManager.saveObject(folder);
	}

	public CostAttributeModel getCostAttribute(Long id) {
		return costAttributeManager.getObject(id);
	}

	public FolderModel getFolderByExternalID(String folderNodeId) {
		return folderManager.getFolderByExternalID(folderNodeId);
	}

	public FolderModel getFolder(Long folderId) {
		return folderManager.getObject(folderId);
	}

	public BrandModel getOwner(String owner) {
		return brandManager.getObject(owner);
	}

	public LibraryModel getLib(Long id) {
		return libraryManager.getObject(id);
	}

	public List<EnumComponentTypeModel> getEnumComponentTypeList() {
		return enumComponentTypeManager.getObjects();
	}

	public List<EnumComponentClassModel> getEnumComponentClassList() {
		return enumComponentClassManager.getObjects();
	}

	// method used in import class
	public void saveComponentVersion(ComponentVersionModel componentVersion,
			List<ComponentValueModel> componentValueList, List<ComponentStandChangeModel> changeList) {
		ComponentModel component = componentVersion.getComponent();

		componentManager.saveObject(component);
		componentVersion.setComponent(component);
		EnumStatusModel enumStatus = enumStatusManager.getObject(EnumStatusManager.Status.IN_USE.value());
		componentVersion.setEnumStatus(enumStatus);
		BaseDateTime validFrom = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		// set validFrom validTo attributes
		componentVersion.setValidFrom(validFrom);
		componentVersion.setValidTo(validFrom.addSeconds(59 * 60 + 23 * 60 * 60 + 59).addYears(200));
		componentVersion.setCreationDate(BaseDateTime.getCurrentDateTime());
		componentVersion.setModificationDate(BaseDateTime.getCurrentDateTime());
		componentVersion.setNumber(1L);

		componentVersionManager.saveObject(componentVersion);

		ComponentStandModel stand = new ComponentStandModel();
		stand.setNumber(1L);
		stand.setComponentVersion(componentVersion);
		stand.setStandDate(BaseDateTime.getCurrentDateTime());
		componentVersionStandManager.saveObject(stand);
		// add the new stand to the set in component version
		Set<ComponentStandModel> componentStands = new HashSet<ComponentStandModel>();
		componentStands.add(stand);
		componentVersion.setComponentVersionStands(componentStands);

		for (ComponentValueModel componentValue : componentValueList) {
			BaseBigDecimal number = componentValue.getNumber();
			BaseBigDecimal amount = componentValue.getAmount();
			if ((number != null && !number.isNull() && !number.equals(new BaseBigDecimal("0")))
					|| (amount != null && !amount.isNull() && !amount.equals(new BaseBigDecimal("0")))
					|| (componentValue.getDescription() != null
							&& !"".equals(componentValue.getDescription().trim()))) {
				componentValue.setComponentVersion(componentVersion);
				componentValueManager.saveObject(componentValue);
			}
		}

		for (ComponentStandChangeModel changeEntry : changeList) {
			changeEntry.setComponentStand(stand);
			if (changeEntry.getDate() == null) {
				changeEntry.setDate(BaseDateTime.getCurrentDateTime());
			}
			componentStandChangeManager.saveObject(changeEntry);
		}

	}

	/**
	 * delete given folder
	 * 
	 * @param folderId
	 * @param objectType
	 * @return error code if exist and empty String if delete done
	 */
	public void deleteFolder(Long folderId, Long objectType) {
		folderManager.removeObject(folderId);
	}

	/**
	 * 
	 * @param externalId
	 * @param brandId
	 * @return
	 */
	public ComponentModel getComponent(String externalId, String brandId) {
		return componentManager.getComponentByExternalIdAndBrand(externalId, brandId);
	}

	public void saveComponent(ComponentModel component) {
		componentManager.saveObject(component);
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public void setEnumObjectTypeManager(EnumObjectTypeManager enumObjectTypeManager) {
		this.enumObjectTypeManager = enumObjectTypeManager;
	}

	public void setCostAttributeManager(CostAttributeManager costAttributeManager) {
		this.costAttributeManager = costAttributeManager;
	}

	public void setEnumComponentClassManager(EnumComponentClassManager enumComponentClassManager) {
		this.enumComponentClassManager = enumComponentClassManager;
	}

	public void setEnumComponentTypeManager(EnumComponentTypeManager enumComponentTypeManager) {
		this.enumComponentTypeManager = enumComponentTypeManager;
	}

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setLibraryManager(LibraryManager libManager) {
		this.libraryManager = libManager;
	}

	public void setComponentStandChangeManager(ComponentStandChangeManager componentStandChangeManager) {
		this.componentStandChangeManager = componentStandChangeManager;
	}

	public void setComponentVersionStandManager(ComponentVersionStandManager componentVersionStandManager) {
		this.componentVersionStandManager = componentVersionStandManager;
	}

	public void setComponentValueManager(ComponentValueManager componentValueManager) {
		this.componentValueManager = componentValueManager;
	}

	public void setEnumStatusManager(EnumStatusManager enumStatusManager) {
		this.enumStatusManager = enumStatusManager;
	}

	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

}
