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
import java.util.Date;
import java.util.List;
import java.util.Map;

import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.EnumComponentClassModel;
import vwg.vw.km.integration.persistence.model.EnumComponentTypeModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.107 $ $Date: 2019/12/10 10:24:26 $
 */
public class CostComponentDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Log log = LogManager.get().getLog(CostComponentDTO.class);

	private Long componentVersionId;

	private Long componentId;

	private List<ComponentValueModel> filteredComponentValues;

	private List<EnumComponentTypeModel> componentTypes;

	private List<EnumComponentClassModel> componentClasses;

	private List<DocumentPoolModel> documentList;

	private ComponentStandModel componentStand;

	private List<ComponentModel> components;

	private Long enumComponentTypeId;

	private Long enumComponentClassId;

	private List<EnumTransitionModel> allowedTransitions;

	private Long componentElementId;

	private List<ElementModel> selectedElements = new ArrayList<ElementModel>();

	private Long selectedElementModelId;

	private List<ComponentElementModel> selectedComponentElementList = new ArrayList<ComponentElementModel>();

	private boolean showConfirmChangeStatusPopup = Boolean.FALSE;

	private String changeStatusComment;

	private Long toStatusId;

	private Boolean existComponentWithSameNumberOrDesignation;

	private Integer costElementsPage = 1;

	private List<Long> searchStatus;

	private Date exportDate;

	protected boolean showExcelExportPopup = false;

	protected boolean showXmlExportPopup = false;

	protected boolean showXmlComponentExportPopup = false;

	private boolean exportWithoutAttributes = false;

	private boolean componentStandCanBeUsed = Boolean.FALSE;

	private boolean componentStandUsed = Boolean.FALSE;

	private boolean componentDeleted = Boolean.FALSE;

	private boolean oldVersionUsed = Boolean.FALSE;

	private List<BrandModel> brandUsers;

	private String componentType;

	private boolean exportAsErgBib = Boolean.FALSE;

	private List<LibraryModel> libraries;

	private List<String> selectedLibraryIds = new ArrayList<String>();

	private String selectedPrimaryLibraryAsString;

	private String usedVersion;

	private String componentWithSameExternalId;

	private String objectType;

	private boolean showConfirmChangeUsePopup = Boolean.FALSE;

	private boolean exportApprovedComponent = Boolean.FALSE;

	private boolean showElementVersionChoicePopup = Boolean.FALSE;

	private Map<ElementVersionModel, String> elementVersionsToAssign;

	private List<ElementVersionModel> workingElementVersions;

	private String elementVersionToAssignAsString;

	private String relatedComponents;

	private Map<String, String> relatedComponentsByElement;

	private String inEditElements;

	private String componentsTobeAffected;

	private boolean showConfirmChangeElUsePopup = Boolean.FALSE;

	private String elementUsedVersion;

	private boolean showConfirmReleasePopup = Boolean.FALSE;

	private String releaseWorkingElErrorMessage;

	private boolean showElementComponentpopup = Boolean.FALSE;

	private boolean showDeleteFileConfirmation = Boolean.FALSE;

	private boolean showDeleteImagePreviewConfirmation;

	private String fileToDelete = null;
	
	private String exportTarget = null;

	private List<String> selectedUsersIds = new ArrayList<String>();

	private String imagesRootPath;

	private String imagePath;

	private String imageUrl = "/imageShow";

	private String changeToShow = null;

	private boolean showHistoryDetailpopup = Boolean.FALSE;

	private List<ComponentStandChangeModel> changeDetails;


	public String getExportTarget() {
		return exportTarget;
	}

	public void setExportTarget(String exportTarget) {
		this.exportTarget = exportTarget;
	}

	public Date getExportDate() {
		if (exportDate == null) {
			return new Date();
		}
		return exportDate;
	}

	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	public boolean isShowXmlExportPopup() {
		return showXmlExportPopup;
	}

	public void setShowXmlExportPopup(boolean showXmlExportPopup) {
		this.showXmlExportPopup = showXmlExportPopup;
	}

	public boolean isExportWithoutAttributes() {
		return exportWithoutAttributes;
	}

	public void setExportWithoutAttributes(boolean exportWithoutAttributes) {
		this.exportWithoutAttributes = exportWithoutAttributes;
	}

	public boolean isExportApprovedComponent() {
		return exportApprovedComponent;
	}

	public void setExportApprovedComponent(boolean exportApprovedComponent) {
		this.exportApprovedComponent = exportApprovedComponent;
	}

	public List<Long> getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(List<Long> searchStatus) {
		this.searchStatus = searchStatus;
	}

	public Integer getCostElementsPage() {
		return costElementsPage;
	}

	public void setCostElementsPage(Integer costElementsPage) {
		this.costElementsPage = costElementsPage;
	}

	public Boolean getExistComponentWithSameNumberOrDesignation() {
		return existComponentWithSameNumberOrDesignation;
	}

	public void setExistComponentWithSameNumberOrDesignation(Boolean existComponentWithSameNumberOrDesignation) {
		this.existComponentWithSameNumberOrDesignation = existComponentWithSameNumberOrDesignation;
	}

	public CostComponentDTO() {
		ComponentVersionModel tmpComponentVersion = new ComponentVersionModel();
		ComponentModel comp = new ComponentModel();
		comp.setInactive(Boolean.FALSE);
		tmpComponentVersion.setComponent(comp);
		tmpComponentVersion.setWithProvision(Boolean.FALSE);
		tmpComponentVersion.setEnumStatus(new EnumStatusModel());
		ComponentStandModel componentStandModel = new ComponentStandModel();
		componentStandModel.setComponentVersion(tmpComponentVersion);
		this.componentStand = componentStandModel;
	}

	public ComponentVersionModel getComponentVersion() {
		return componentStand.getComponentVersion();
	}

	public List<ComponentValueModel> getFilteredComponentValues() {
		return filteredComponentValues;
	}

	public void setFilteredComponentValues(List<ComponentValueModel> filteredComponentValues) {
		this.filteredComponentValues = filteredComponentValues;
	}

	public Long getComponentVersionId() {
		return componentVersionId;
	}

	public void setComponentVersionId(Long componentVersionId) {
		this.componentVersionId = componentVersionId;
	}

	public Long getComponentId() {
		return componentId;
	}

	public void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	public List<DocumentPoolModel> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<DocumentPoolModel> documentList) {
		this.documentList = documentList;
	}

	public List<ComponentModel> getComponents() {
		return components;
	}

	public void setComponents(List<ComponentModel> components) {
		this.components = components;
	}

	public List<EnumComponentTypeModel> getComponentTypes() {
		return componentTypes;
	}

	public void setComponentTypes(List<EnumComponentTypeModel> componentTypes) {
		this.componentTypes = componentTypes;
	}

	public List<EnumComponentClassModel> getComponentClasses() {
		return componentClasses;
	}

	public void setComponentClasses(List<EnumComponentClassModel> componentClasses) {
		this.componentClasses = componentClasses;
	}

	public Long getEnumComponentTypeId() {
		return enumComponentTypeId;
	}

	public void setEnumComponentTypeId(Long enumComponentTypeId) {
		this.enumComponentTypeId = enumComponentTypeId;
	}

	public Long getEnumComponentClassId() {
		return enumComponentClassId;
	}

	public void setEnumComponentClassId(Long enumComponentClassId) {
		this.enumComponentClassId = enumComponentClassId;
	}

	public List<ElementModel> getSelectedElements() {
		return selectedElements;
	}

	public void setSelectedElements(List<ElementModel> selectedElements) {
		this.selectedElements = selectedElements;
	}

	/**
	 * 11-25-2013:PTS change 26651:ABA Im Arbeitsordner sollen Baustein und Elemente zusätzlich noch mit dem jeweiligen
	 * ELE/BS Icon gekennzeichnet werden.
	 */
	public boolean getIsInWorkingFolder() {
		// If the folder which contains the component has a creator, the component is in a working Folder
		if (getComponentVersion().getComponent().getFolder().getCreatorRefId() != null) {
			return true;
		}
		return false;
	}

	/**
	 * @return the selectedElementModelId
	 */
	public Long getSelectedElementModelId() {
		return selectedElementModelId;
	}

	private ElementModel selectedElementModel;

	/**
	 * @param ElementModel
	 *            the ElementModel to set
	 */
	public void setSelectedElementModel(ElementModel selectedElementModel) {
		this.selectedElementModel = selectedElementModel;
	}

	/**
	 * @return the ElementModel
	 */
	public ElementModel getSelectedElementModel() {
		return selectedElementModel;
	}

	/**
	 * @param selectedElementModelId
	 *            the selectedElementModelId to set
	 */
	public void setSelectedElementModelId(Long selectedElementModelId) {
		this.selectedElementModelId = selectedElementModelId;
	}

	public boolean isNoComponents() {
		if (components != null && !components.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean isNoDocuments() {
		if (documentList != null && !documentList.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean isNoHistoryVersions() {
		if (getVersionHistoryList() != null && !getVersionHistoryList().isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean isNoComponentElements() {
		if (componentStand.getComponentVersion().getAvailableComponentElementList() != null
				&& !componentStand.getComponentVersion().getAvailableComponentElementList().isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * get Version History List
	 * 
	 * @return
	 */
	public List<ComponentVersionModel> getVersionHistoryList() {
		List<ComponentVersionModel> versionHistoryList = new ArrayList<ComponentVersionModel>();
		ComponentModel component = getComponentVersion().getComponent();
		if (component != null) {
			// load on demand by sizing
			component.getComponentVersions().size();
			versionHistoryList.addAll(component.getComponentVersions());
		}

		// load version users collection
		for (ComponentVersionModel componentVersion : versionHistoryList) {
			componentVersion.getComponentVersionStands().size();
			if (componentVersion.getComponentVersionStands() != null
					&& !componentVersion.getComponentVersionStands().isEmpty()) {
				List<ComponentStandModel> componentVersionsStands = new ArrayList<ComponentStandModel>(
						componentVersion.getComponentVersionStands());
				for (ComponentStandModel componentStand : componentVersionsStands) {
					componentStand.getComponentVersionUsers().size();
				}
			}
		}
		return versionHistoryList;
	}

	public List<ComponentStandModel> getVersionStandList(Long versionId) {
		List<ComponentVersionModel> versionHistoryList = getVersionHistoryList();
		for (ComponentVersionModel componentVersion : versionHistoryList) {
			if (componentVersion.getComponentVersionId().equals(versionId)) {
				if (componentVersion.getComponentVersionStands() != null
						&& !componentVersion.getComponentVersionStands().isEmpty()) {
					log.info("Number of Stand : " + componentVersion.getStandCount());
					List<ComponentStandModel> componentVersionsStands = new ArrayList<ComponentStandModel>(
							componentVersion.getComponentVersionStands());
					for (ComponentStandModel componentStand : componentVersionsStands) {
						componentStand.getComponentVersionUsers().size();
					}
					return componentVersionsStands;
				}
			}
		}
		return new ArrayList<ComponentStandModel>();
	}

	/**
	 * get Index Of Last InUse Version
	 * 
	 * @return
	 */
	public int getIndexOfLastInUseVersion() {
		List<ComponentVersionModel> versionHistoryList = getVersionHistoryList();
		int i = 0;
		for (ComponentVersionModel version : versionHistoryList) {
			if (EnumStatusManager.Status.IN_USE.value().equals(version.getEnumStatus().getEnumStatusId())) {
				return i;
			}
			i++;
		}
		// return -1 if no in use version
		return -1;
	}

	/**
	 * check if the baustein is still in the first version and not in use yet then the Klasse filed can be changed
	 * Ticket 9952: ZS
	 * 
	 * @return true or false
	 */
	public boolean isTheFirstInEditVersion() {
		if (isNoHistoryVersions() || getIndexOfLastInUseVersion() == -1) {
			return true;
		}
		return false;
	}

	public List<ComponentElementModel> getSelectedComponentElementList() {
		return selectedComponentElementList;
	}

	public void setSelectedComponentElementList(List<ComponentElementModel> selectedComponentElementList) {
		this.selectedComponentElementList = selectedComponentElementList;
	}

	public Long getComponentElementId() {
		return componentElementId;
	}

	public void setComponentElementId(Long componentElementId) {
		this.componentElementId = componentElementId;
	}

	/**************** Popup Bemerkung update ************************************************/
	private boolean modalRendered = false;

	public boolean isModalRendered() {
		return modalRendered;
	}

	public void setModalRendered(boolean modalRendered) {
		this.modalRendered = modalRendered;
	}

	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	private int rowIndex;

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	/**
	 * update ComponentValue Description
	 * 
	 * @param rowIndex
	 * @param render
	 */
	public void updateComponentValueDescription(int rowIndex, boolean render) {
		if (rowIndex > -1) {
			setRowIndex(rowIndex);
		}

		List<ComponentValueModel> filtredComponents = getFilteredComponentValues();
		ComponentValueModel componentValue = filtredComponents.get(getRowIndex());

		if (rowIndex > -1) {
			setRemark(componentValue.getDescription());
		} else {
			componentValue.setDescription(getRemark());
		}

		setModalRendered(render);
	}

	/**************** Popup version history Bemerkung update ************************************************/

	private List<ComponentStandChangeModel> versionChanges;

	public List<ComponentStandChangeModel> getVersionChanges() {
		return versionChanges;
	}

	public void setVersionChanges(List<ComponentStandChangeModel> versionChanges) {
		this.versionChanges = versionChanges;
	}

	public boolean isNoVersionChanges() {
		List<ComponentStandChangeModel> versionChanges = getVersionChanges();
		if (versionChanges != null && !versionChanges.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public List<EnumTransitionModel> getAllowedTransitions() {
		return allowedTransitions;
	}

	public void setAllowedTransitions(List<EnumTransitionModel> allowedTransitions) {
		this.allowedTransitions = allowedTransitions;
	}

	public boolean isShowConfirmChangeStatusPopup() {
		return showConfirmChangeStatusPopup;
	}

	public void setShowConfirmChangeStatusPopup(boolean showConfirmChangeStatusPopup) {
		this.showConfirmChangeStatusPopup = showConfirmChangeStatusPopup;
	}

	public String getChangeStatusComment() {
		return changeStatusComment;
	}

	public void setChangeStatusComment(String changeStatusComment) {
		this.changeStatusComment = changeStatusComment;
	}

	public Long getToStatusId() {
		return toStatusId;
	}

	public void setToStatusId(Long toStatusId) {
		this.toStatusId = toStatusId;
	}

	public ComponentStandModel getComponentStand() {
		return componentStand;
	}

	public void setComponentStand(ComponentStandModel componentStand) {
		this.componentStand = componentStand;
	}

	public boolean isComponentStandCanBeUsed() {
		// ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (getUserLogged().haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		return componentStandCanBeUsed;
	}

	public void setComponentStandCanBeUsed(Boolean isComponentStandCanBeUsed) {
		this.componentStandCanBeUsed = isComponentStandCanBeUsed;
	}

	public boolean isComponentStandUsed() {
		// ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (getUserLogged().haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		return componentStandUsed;
	}

	public void setComponentStandUsed(Boolean isComponentStandUsed) {
		this.componentStandUsed = isComponentStandUsed;
	}

	public List<BrandModel> getBrandUsers() {
		return brandUsers;
	}

	public void setBrandUsers(List<BrandModel> brandUsers) {
		this.brandUsers = brandUsers;
	}

	public List<LibraryModel> getLibraries() {
		return libraries;
	}

	public void setLibraries(List<LibraryModel> libraries) {
		this.libraries = libraries;
	}

	public boolean isComponentDeleted() {
		return componentDeleted;
	}

	public void setComponentDeleted(boolean componentDeleted) {
		this.componentDeleted = componentDeleted;
	}

	public void setOldVersionUsed(Boolean oldVersionUsed) {
		this.oldVersionUsed = oldVersionUsed;
	}

	public boolean getOldVersionUsed() {
		return oldVersionUsed;
	}

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public boolean isExportAsErgBib() {
		return exportAsErgBib;
	}

	public void setExportAsErgBib(boolean exportAsErgBib) {
		this.exportAsErgBib = exportAsErgBib;
	}

	public List<String> getSelectedLibraryIds() {
		return selectedLibraryIds;
	}

	public void setSelectedLibraryIds(List<String> selectedLibraryIds) {
		this.selectedLibraryIds = selectedLibraryIds;
	}

	public String getSelectedPrimaryLibraryAsString() {
		return selectedPrimaryLibraryAsString;
	}

	public void setSelectedPrimaryLibraryAsString(String selectedPrimaryLibraryAsString) {
		this.selectedPrimaryLibraryAsString = selectedPrimaryLibraryAsString;
	}

	public Long getSelectedPrimaryLibrary() {
		Long primaryLibrary = null;
		if (this.selectedPrimaryLibraryAsString != null && !"".equals(this.selectedPrimaryLibraryAsString.trim())) {
			try {
				primaryLibrary = Long.parseLong(this.selectedPrimaryLibraryAsString);
			} catch (NumberFormatException e) {
				log.error("Invalid selected primary library : " + selectedPrimaryLibraryAsString);
			}
		}
		return primaryLibrary;
	}

	public Integer getMaxLengthForChangeStatus() {
		Integer usedChars = (componentStand.getComponentVersion().getComment() != null)
				? componentStand.getComponentVersion().getComment().length() + REMARKS_SEPARATOR.length()
				: 0;
		Integer returnValue = 4000 - usedChars;
		return (returnValue > 0) ? returnValue : 0;
	}

	/**
	 * @see vwg.vw.km.application.service.dto.base.BaseDTO#copyUtilAttributesForSearch()
	 */
	@Override
	public CostComponentDTO copyUtilAttributesForSearch() {
		CostComponentDTO costComponentDTO = new CostComponentDTO();
		costComponentDTO.setSearchString(getSearchString());
		costComponentDTO.setSearchMode(Boolean.TRUE);
		costComponentDTO.setSearchStringResult(getSearchString());
		costComponentDTO.setNoSearchResult(isNoSearchResult());
		// if no result in no result page
		// if (isNoSearchResult()) {
		costComponentDTO.setSearchFolder(getSearchFolder());
		costComponentDTO.setFolderId(getFolderId());
		// }
		return costComponentDTO;
	}

	public int getVersionsCount() {
		List<ComponentVersionModel> versionHistoryList = getVersionHistoryList();
		if (versionHistoryList != null && !versionHistoryList.isEmpty()) {
			return versionHistoryList.size();
		}
		return 0;
	}

	/**
	 * Check if a user can perform an save/new action
	 * 
	 * @return true/false
	 */
	public boolean getUserIsOwner() {
		FolderModel folder = getComponentVersion().getComponent().getFolder();
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (folder.getCreatorRefId() == null && getAdminsitratorOnBrand()) {
			return Boolean.TRUE;
		}

		Long currentStatus = getComponentVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.NEW.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& (getComponentVersion().getCreator() == null || loggedUser.equals(getComponentVersion().getCreator()))
				&& folder.getCreatorRefId() != null) {

			return Boolean.TRUE;
		}
		if (folder.getCreatorRefId() == null
				&& getUserLogged().getUserBrands().contains(getComponentVersion().getComponent().getOwner())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean getUserCanAdd() {
		FolderModel folder = getComponentVersion().getComponent().getFolder();
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (folder.getCreatorRefId() == null && getAdminsitratorOnBrand()) {
			return Boolean.TRUE;
		}

		Long currentStatus = getComponentVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.NEW.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& (getComponentVersion().getCreator() == null || loggedUser.equals(getComponentVersion().getCreator()))
				&& folder.getCreatorRefId() != null) {

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if a user can perform a delete action
	 * 
	 * @return true/false
	 */
	public boolean getUserCanDelete() {
		FolderModel folder = getComponentVersion().getComponent().getFolder();
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (getAdminsitratorOnBrand() && folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}

		Long currentStatus = getComponentVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.NEW.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& (getComponentVersion().getCreator() == null || loggedUser.equals(getComponentVersion().getCreator()))
				&& folder.getCreatorRefId() != null) {

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if a user can activate or deactivate a Baustein
	 * 
	 * @return true/false
	 */
	public boolean getUserCanChangeActivation() {
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		FolderModel folder = getComponentVersion().getComponent().getFolder();
		if (loggedUser.getDefaultBrand() != null
				&& loggedUser.getDefaultBrand().equals(getComponentVersion().getComponent().getOwner().getBrandId())
				&& folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if the user is an administrator on brand(s)
	 * 
	 * @return true/false
	 */
	public boolean getAdminsitratorOnBrand() {
		UserModel loggedUser = getUserLogged();
		if (loggedUser.haveRole(RoleManager.Role.ADMINISTRATOR.value())
				&& loggedUser.getUserBrands().contains(getComponentVersion().getComponent().getOwner())) {
			return Boolean.TRUE;
		}
		return false;
	}

	/**
	 * Check if a user can perform an save/delete/changeStatus action when status is IN_PROGRESS
	 * 
	 * @return true/false
	 */
	public boolean getUserHasWriteAccess() {
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		Long currentStatus = getComponentVersion().getEnumStatus().getEnumStatusId();
		UserModel processor = getComponentVersion().getProcessor();
		if (EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus) && processor != null) {
			if (loggedUser.equals(processor) || loggedUser.haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * Check if the user has right to sort the ComponentElements list Only the Administrator or the creator of the
	 * version have this right
	 * 
	 * @return
	 */
	public boolean getUserCanSort() {
		UserModel loggedUser = getUserLogged();

		UserModel creator = getComponentVersion().getCreator();
		if ((getUserIsOwner() && getUserHasWriteAccess())
				&& (getAdminsitratorOnBrand() || (creator != null && loggedUser.equals(creator)))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public void setUsedVersion(String usedVersion) {
		this.usedVersion = usedVersion;
	}

	public String getUsedVersion() {
		return usedVersion;
	}

	public boolean isShowConfirmChangeUsePopup() {
		return showConfirmChangeUsePopup;
	}

	public void setShowConfirmChangeUsePopup(boolean showConfirmChangeUsePopup) {
		this.showConfirmChangeUsePopup = showConfirmChangeUsePopup;
	}

	public boolean isShowExcelExportPopup() {
		return showExcelExportPopup;
	}

	public void setShowExcelExportPopup(boolean showExcelExportPopup) {
		this.showExcelExportPopup = showExcelExportPopup;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public boolean isShowXmlComponentExportPopup() {
		return showXmlComponentExportPopup;
	}

	public void setShowXmlComponentExportPopup(boolean showXmlComponentExportPopup) {
		this.showXmlComponentExportPopup = showXmlComponentExportPopup;
	}

	public void setComponentWithSameExternalId(String componentWithSameExternalId) {
		this.componentWithSameExternalId = componentWithSameExternalId;
	}

	public String getComponentWithSameExternalId() {
		return componentWithSameExternalId;
	}

	public void setShowElementVersionChoicePopup(boolean showElementVersionChoicePopup) {
		this.showElementVersionChoicePopup = showElementVersionChoicePopup;
	}

	public boolean isShowElementVersionChoicePopup() {
		return showElementVersionChoicePopup;
	}

	public void setElementVersionsToAssign(Map<ElementVersionModel, String> elementVersionsToAssign) {
		this.elementVersionsToAssign = elementVersionsToAssign;
	}

	public Map<ElementVersionModel, String> getElementVersionsToAssign() {
		return elementVersionsToAssign;
	}

	public void setElementVersionToAssignAsString(String elementVersionToAssignAsString) {
		this.elementVersionToAssignAsString = elementVersionToAssignAsString;
	}

	public String getElementVersionToAssignAsString() {
		return elementVersionToAssignAsString;
	}

	public Long getElementVersionToAssign() {
		Long elementVersionToAssign = null;
		if (this.elementVersionToAssignAsString != null && !"".equals(this.elementVersionToAssignAsString.trim())) {
			try {
				elementVersionToAssign = Long.parseLong(this.elementVersionToAssignAsString);
			} catch (NumberFormatException e) {
				log.error("Invalid selected element Version to assign : " + elementVersionToAssignAsString);
			}
		}
		return elementVersionToAssign;
	}

	public String getRelatedComponents() {
		return relatedComponents;
	}

	public void setRelatedComponents(String relatedComponents) {
		this.relatedComponents = relatedComponents;
	}

	public Map<String, String> getRelatedComponentsByElement() {
		return relatedComponentsByElement;
	}

	public void setRelatedComponentsByElement(Map<String, String> relatedComponentsByElement) {
		this.relatedComponentsByElement = relatedComponentsByElement;
	}

	public boolean isShowConfirmChangeElUsePopup() {
		return showConfirmChangeElUsePopup;
	}

	public void setShowConfirmChangeElUsePopup(boolean showConfirmChangeElUsePopup) {
		this.showConfirmChangeElUsePopup = showConfirmChangeElUsePopup;
	}

	public void setElementUsedVersion(String elementUsedVersion) {
		this.elementUsedVersion = elementUsedVersion;
	}

	public String getElementUsedVersion() {
		return elementUsedVersion;
	}

	public void setInEditElements(String inEditElements) {
		this.inEditElements = inEditElements;
	}

	public String getInEditElements() {
		return inEditElements;
	}

	public void setComponentsTobeAffected(String componentsTobeAffected) {
		this.componentsTobeAffected = componentsTobeAffected;
	}

	public String getComponentsTobeAffected() {
		return componentsTobeAffected;
	}

	public void setShowConfirmReleasePopup(boolean showConfirmReleasePopup) {
		this.showConfirmReleasePopup = showConfirmReleasePopup;
	}

	public boolean isShowConfirmReleasePopup() {
		return showConfirmReleasePopup;
	}

	public void setWorkingElementVersions(List<ElementVersionModel> workingElementVersions) {
		this.workingElementVersions = workingElementVersions;
	}

	public List<ElementVersionModel> getWorkingElementVersions() {
		return workingElementVersions;
	}

	public void setReleaseWorkingElErrorMessage(String releaseWorkingElErrorMessage) {
		this.releaseWorkingElErrorMessage = releaseWorkingElErrorMessage;
	}

	public String getReleaseWorkingElErrorMessage() {
		return releaseWorkingElErrorMessage;
	}

	// 04.09.2013: ZS: PTS_Requirement-22201: Redesign of the elements assign mask

	public boolean isShowElementComponentpopup() {
		return showElementComponentpopup;
	}

	public void setShowElementComponentpopup(boolean showElementComponentpopup) {
		this.showElementComponentpopup = showElementComponentpopup;
	}

	public List<String> getSelectedUsersIds() {
		return selectedUsersIds;
	}

	public void setSelectedUsersIds(List<String> selectedUsersIds) {
		this.selectedUsersIds = selectedUsersIds;
	}

	public boolean isShowDeleteFileConfirmation() {
		return showDeleteFileConfirmation;
	}

	public void setShowDeleteFileConfirmation(boolean showDeleteFileConfirmation) {
		this.showDeleteFileConfirmation = showDeleteFileConfirmation;
	}

	public boolean isShowDeleteImagePreviewConfirmation() {
		return showDeleteImagePreviewConfirmation;
	}

	public void setShowDeleteImagePreviewConfirmation(boolean showDeleteImagePreviewConfirmation) {
		this.showDeleteImagePreviewConfirmation = showDeleteImagePreviewConfirmation;
	}

	public void setFileToDelete(String fileToDelete) {
		this.fileToDelete = fileToDelete;
	}

	public String getFileToDelete() {
		return fileToDelete;
	}

	public String getImagesRootPath() {
		return imagesRootPath;
	}

	public void setImagesRootPath(String imagesRootPath) {
		this.imagesRootPath = imagesRootPath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImageUrl(String parms) {
		this.imageUrl = "/imageShow?date=" + parms;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getChangeToShow() {
		return changeToShow;
	}

	public void setChangeToShow(String changeToShow) {
		this.changeToShow = changeToShow;
	}

	public boolean isShowHistoryDetailpopup() {
		return showHistoryDetailpopup;
	}

	public void setShowHistoryDetailpopup(boolean showHistoryDetailpopup) {
		this.showHistoryDetailpopup = showHistoryDetailpopup;
	}

	public List<ComponentStandChangeModel> getChangeDetails() {
		return changeDetails;
	}

	public void setChangeDetails(List<ComponentStandChangeModel> changeDetails) {
		this.changeDetails = changeDetails;
	}

	
}
