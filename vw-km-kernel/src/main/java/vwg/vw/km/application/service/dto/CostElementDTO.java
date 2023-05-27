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

import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementCategoryModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
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
 * @version $Revision: 1.98 $ $Date: 2017/12/08 08:01:56 $
 */
public class CostElementDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6225577664359706450L;

	private Long elementVersionId;

	private Long elementId;

	private List<ElementValueModel> filteredElementValues;

	private List<ElementCategoryModel> elementCategories;

	private List<DocumentPoolModel> documentList;

	private ElementVersionModel elementVersion;

	private List<ElementModel> elements;

	private String elementCategoryId;

	private List<EnumTransitionModel> allowedTransitions;

	private boolean showConfirmChangeStatusPopup = Boolean.FALSE;

	private String changeStatusComment;

	private Long toStatusId;

	private Boolean existElementWithSameNumberOrDesignation;

	private List<Long> searchStatus;

	private boolean elementVersionCanBeUsed = Boolean.FALSE;

	private boolean elementVersionUsed = Boolean.FALSE;

	private boolean elementDeleted = Boolean.FALSE;

	private boolean oldVersionUsed = Boolean.FALSE;

	private boolean showConfirmChangeUsePopup = Boolean.FALSE;

	private boolean versionUnused = Boolean.TRUE;

	private String relatedComponents;

	private String usedVersion;

	private boolean showDeleteFileConfirmation = Boolean.FALSE;

	private String fileToDelete = null;

	private String changeToShow = null;

	// Initialize sort default column
	private String elementListSortColumnName;
	private String elementAttributesSortColumnName;
	private String componentElementSortColumnName;
	private String versionHistorySortColumnName;
	private String elementDocumentSortColumnName;

	private boolean showHistoryDetailpopup = Boolean.FALSE;

	private List<ElementVersionChangeModel> changeDetails;

	public String getElementListSortColumnName() {
		return elementListSortColumnName;
	}

	public void setElementListSortColumnName(String elementListSortColumnName) {
		this.elementListSortColumnName = elementListSortColumnName;
	}

	public String getElementAttributesSortColumnName() {
		return elementAttributesSortColumnName;
	}

	public void setElementAttributesSortColumnName(String elementAttributesSortColumnName) {
		this.elementAttributesSortColumnName = elementAttributesSortColumnName;
	}

	public String getComponentElementSortColumnName() {
		return componentElementSortColumnName;
	}

	public void setComponentElementSortColumnName(String componentElementSortColumnName) {
		this.componentElementSortColumnName = componentElementSortColumnName;
	}

	public String getVersionHistorySortColumnName() {
		return versionHistorySortColumnName;
	}

	public void setVersionHistorySortColumnName(String versionHistorySortColumnName) {
		this.versionHistorySortColumnName = versionHistorySortColumnName;
	}

	public String getElementDocumentSortColumnName() {
		return elementDocumentSortColumnName;
	}

	public void setElementDocumentSortColumnName(String elementDocumentSortColumnName) {
		this.elementDocumentSortColumnName = elementDocumentSortColumnName;
	}

	private List<ComponentElementModel> componentElementsList;

	public boolean isElementDeleted() {
		return elementDeleted;
	}

	public void setElementDeleted(boolean elementDeleted) {
		this.elementDeleted = elementDeleted;
	}

	public List<Long> getSearchStatus() {
		return searchStatus;
	}

	public void setSearchStatus(List<Long> searchStatus) {
		this.searchStatus = searchStatus;
	}

	public CostElementDTO() {
		ElementVersionModel tmpElementVersion = new ElementVersionModel();
		ElementModel element = new ElementModel();
		element.setInactive(Boolean.FALSE);
		tmpElementVersion.setElement(element);
		tmpElementVersion.setEnumStatus(new EnumStatusModel());
		elementVersion = tmpElementVersion;

	}

	/**
	 * 11-25-2013:PTS change 26651:ABA Im Arbeitsordner sollen Baustein und Elemente zusätzlich noch mit dem jeweiligen
	 * ELE/BS Icon gekennzeichnet werden.
	 */
	public boolean getIsInWorkingFolder() {
		// If the folder which contains the element has a creator, the element is in a working Folder
		if (getElementVersion() != null && getElementVersion().getElement() != null
				&& getElementVersion().getElement().getFolder() != null
				&& getElementVersion().getElement().getFolder().getCreatorRefId() != null) {
			return true;
		}
		return false;
	}

	public Boolean getExistElementWithSameNumberOrDesignation() {
		return existElementWithSameNumberOrDesignation;
	}

	public void setExistElementWithSameNumberOrDesignation(Boolean existElementWithSameNumberOrDesignation) {
		this.existElementWithSameNumberOrDesignation = existElementWithSameNumberOrDesignation;
	}

	public ElementVersionModel getElementVersion() {
		return elementVersion;
	}

	public boolean isShowDeleteFileConfirmation() {
		return showDeleteFileConfirmation;
	}

	public void setShowDeleteFileConfirmation(boolean showDeleteFileConfirmation) {
		this.showDeleteFileConfirmation = showDeleteFileConfirmation;
	}

	public void setElementVersion(ElementVersionModel elementVersion) {
		this.elementVersion = elementVersion;
	}

	public List<ElementValueModel> getFilteredElementValues() {
		return filteredElementValues;
	}

	public void setFilteredElementValues(List<ElementValueModel> filteredElementValues) {
		this.filteredElementValues = filteredElementValues;
	}

	public Long getElementVersionId() {
		return elementVersionId;
	}

	public void setElementVersionId(Long elementVersionId) {
		this.elementVersionId = elementVersionId;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public List<DocumentPoolModel> getDocumentList() {
		return documentList;
	}

	public void setDocumentList(List<DocumentPoolModel> documentList) {
		this.documentList = documentList;
	}

	public List<ElementModel> getElements() {
		return elements;
	}

	public void setElements(List<ElementModel> elements) {
		this.elements = elements;
	}

	public List<ElementCategoryModel> getElementCategories() {
		return elementCategories;
	}

	public void setElementCategories(List<ElementCategoryModel> elementCategories) {
		this.elementCategories = elementCategories;
	}

	public String getElementCategoryIdAsString() {
		return elementCategoryId;
	}

	public void setElementCategoryIdAsString(String elementCategoryId) {
		this.elementCategoryId = elementCategoryId;
	}

	public void setElementCategoryId(Long elementCategoryId) {
		if (elementCategoryId != null) {
			this.elementCategoryId = elementCategoryId.toString();
		}
	}

	public Long getElementCategoryId() {
		Long primaryLibrary = null;
		if (this.elementCategoryId != null && !"".equals(this.elementCategoryId.trim())) {
			try {
				primaryLibrary = Long.parseLong(this.elementCategoryId);
			} catch (NumberFormatException e) {
			}
		}
		return primaryLibrary;
	}

	/**
	 * get ComponentElement List from current element model
	 * 
	 * @return
	 */
	public List<ComponentElementModel> getComponentElementList() {
		return componentElementsList;
	}

	public void setComponentElementList(List<ComponentElementModel> componentElementsList) {
		this.componentElementsList = componentElementsList;
	}

	public boolean isNoElements() {
		if (elements != null && !elements.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public boolean isNoComponents() {
		List<ComponentElementModel> componentElementsList = getComponentElementList();
		if (componentElementsList != null && !componentElementsList.isEmpty()) {
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
		List<ElementVersionModel> versionHistoryList = getVersionHistoryList();
		if (versionHistoryList != null && !versionHistoryList.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public int getVersionsCount() {
		List<ElementVersionModel> versionHistoryList = getVersionHistoryList();
		if (versionHistoryList != null && !versionHistoryList.isEmpty()) {
			return versionHistoryList.size();
		}
		return 0;
	}

	public boolean isNoVersionChanges() {
		List<ElementVersionChangeModel> versionChanges = getVersionChanges();
		if (versionChanges != null && !versionChanges.isEmpty()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	/**
	 * return the list of all versions from current element in memory
	 * 
	 * @return
	 */
	public List<ElementVersionModel> getVersionHistoryList() {
		List<ElementVersionModel> versionHistoryList = new ArrayList<ElementVersionModel>();
		ElementModel element = getElementVersion().getElement();
		if (element != null) {
			// load on demand by sizing
			element.getElementVersions().size();
			versionHistoryList.addAll(element.getElementVersions());
		}
		// load version users
		for (ElementVersionModel elementVersion : versionHistoryList) {
			elementVersion.getElementVersionUsers().size();
		}
		return versionHistoryList;
	}

	/**
	 * return the index of last inuse version from the list of versions
	 * 
	 * @return
	 */
	public int getIndexOfLastInUseVersion() {
		List<ElementVersionModel> versionHistoryList = getVersionHistoryList();
		int i = 0;
		for (ElementVersionModel version : versionHistoryList) {
			if (EnumStatusManager.Status.IN_USE.value().equals(version.getEnumStatus().getEnumStatusId())) {
				return i;
			}
			i++;
		}
		// return -1 if no in use version
		return -1;
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
	 * update ElementValue Description called by JSF
	 * 
	 * @param rowIndex
	 *            index of element value in memory
	 * @param render
	 */
	public void updateElementValueDescription(int rowIndex, boolean render) {
		if (rowIndex > -1) {
			setRowIndex(rowIndex);
		}

		List<ElementValueModel> filtredElements = getFilteredElementValues();
		ElementValueModel elementValue = filtredElements.get(getRowIndex());

		if (rowIndex > -1) {
			setRemark(elementValue.getDescription());
		} else {
			elementValue.setDescription(getRemark());
		}

		setModalRendered(render);
	}

	private List<ElementVersionChangeModel> versionChanges;

	public List<ElementVersionChangeModel> getVersionChanges() {
		return versionChanges;
	}

	public void setVersionChanges(List<ElementVersionChangeModel> versionChanges) {
		this.versionChanges = versionChanges;
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

	public boolean isElementVersionCanBeUsed() {
		// ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (getUserLogged().haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		return elementVersionCanBeUsed;
	}

	public void setElementVersionCanBeUsed(Boolean isElementVersionCanBeUsed) {
		this.elementVersionCanBeUsed = isElementVersionCanBeUsed;
	}

	public boolean isElementVersionUsed() {
		// ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (getUserLogged().haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		return elementVersionUsed;
	}

	public void setElementVersionUsed(Boolean isElementVersionUsed) {
		this.elementVersionUsed = isElementVersionUsed;
	}

	public void setOldVersionUsed(Boolean oldVersionUsed) {
		this.oldVersionUsed = oldVersionUsed;
	}

	public boolean getOldVersionUsed() {
		return oldVersionUsed;
	}

	public boolean getVersionUnused() {
		return versionUnused;
	}

	public void setVersionUnused(boolean versionUnused) {
		this.versionUnused = versionUnused;
	}

	public String getRelatedComponents() {
		return relatedComponents;
	}

	public void setRelatedComponents(String relatedComponents) {
		this.relatedComponents = relatedComponents;
	}

	public Integer getMaxLengthForChangeStatus() {
		Integer usedChars = (elementVersion.getComment() != null)
				? elementVersion.getComment().length() + REMARKS_SEPARATOR.length()
				: 0;
		Integer returnValue = 4000 - usedChars;
		return (returnValue > 0) ? returnValue : 0;
	}

	/**
	 * @see vwg.vw.km.application.service.dto.base.BaseDTO#copyUtilAttributesForSearch()
	 */
	@Override
	public CostElementDTO copyUtilAttributesForSearch() {
		CostElementDTO costElementDTO = new CostElementDTO();
		costElementDTO.setSearchString(getSearchString());
		costElementDTO.setSearchStringResult(getSearchString());
		costElementDTO.setSearchMode(Boolean.TRUE);
		costElementDTO.setNoSearchResult(isNoSearchResult());
		// if no result in no result page
		// if (isNoSearchResult()) {
		costElementDTO.setSearchFolder(getSearchFolder());
		costElementDTO.setFolderId(getFolderId());
		// }
		return costElementDTO;
	}

	/**
	 * Check if a user can perform an save action
	 * 
	 * @return true/false
	 */
	public boolean getUserIsOwner() {
		FolderModel folder = getElementVersion().getElement().getFolder();
		UserModel loggedUser = getUserLogged();
		if (getAdminsitratorOnBrand() && folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}

		Long currentStatus = getElementVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.NEW.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& (getElementVersion().getCreator() == null || loggedUser.equals(getElementVersion().getCreator()))
				&& folder.getCreatorRefId() != null) {

			return Boolean.TRUE;
		}

		if (loggedUser.getUserBrands().contains(getElementVersion().getElement().getOwner())
				&& folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if a user can perform an new action
	 * 
	 * @return true/false
	 */
	public boolean getUserCanAdd() {
		FolderModel folder = getElementVersion().getElement().getFolder();
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (getAdminsitratorOnBrand() && folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}

		Long currentStatus = getElementVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.NEW.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& (getElementVersion().getElement().getCreatorRefId() == null
						|| loggedUser.getUserId().equals(getElementVersion().getElement().getCreatorRefId()))
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
		FolderModel folder = getElementVersion().getElement().getFolder();
		UserModel loggedUser = getUserLogged();
		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (getAdminsitratorOnBrand() && folder.getCreatorRefId() == null) {
			return Boolean.TRUE;
		}

		Long currentStatus = getElementVersion().getEnumStatus().getEnumStatusId();
		if ((EnumStatusManager.Status.CREATED.value().equals(currentStatus)
				|| EnumStatusManager.Status.TO_BE_APPROVED.value().equals(currentStatus)
				|| EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus))
				&& loggedUser.equals(getElementVersion().getCreator()) && folder.getCreatorRefId() != null) {

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if a user can activate or deactivate an Element
	 * 
	 * @return true/false
	 */
	public boolean getUserCanChangeActivation() {
		UserModel loggedUser = getUserLogged();
		FolderModel folder = getElementVersion().getElement().getFolder();

		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (loggedUser.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return Boolean.FALSE;
		}
		if (loggedUser.getDefaultBrand() != null
				&& loggedUser.getDefaultBrand().equals(getElementVersion().getElement().getOwner().getBrandId())
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
				&& loggedUser.getUserBrands().contains(getElementVersion().getElement().getOwner())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
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
		return (!getEditIsLockedByUser() && !getEditIsLockedByComponent());
	}

	/**
	 * Check if an Element status is IN_PROGRESS in not locked by a user
	 * 
	 * @return true/false
	 */
	public boolean getEditIsLockedByUser() {
		UserModel loggedUser = getUserLogged();
		Long currentStatus = getElementVersion().getEnumStatus().getEnumStatusId();
		UserModel processor = getElementVersion().getProcessor();
		if (EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus) && processor != null) {
			if (loggedUser.equals(processor) || loggedUser.haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
				return Boolean.FALSE;
			}
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	/**
	 * Check if an Element status is IN_PROGRESS in not locked by an assigned BS
	 * 
	 * @return true/false
	 */
	public boolean getEditIsLockedByComponent() {
		Long currentStatus = getElementVersion().getEnumStatus().getEnumStatusId();
		if (EnumStatusManager.Status.IN_PROGRESS.value().equals(currentStatus)
				|| EnumStatusManager.Status.CREATED.value().equals(currentStatus)) {

			for (ComponentElementModel componentElementInUse : getComponentElementList()) {
				ComponentVersionModel componentVersion = componentElementInUse.getComponentStand()
						.getComponentVersion();
				Long componentStatus = componentVersion.getEnumStatus().getEnumStatusId();
				if (componentVersion.getComponent().getOwner().equals(getElementVersion().getElement().getOwner())
						&& (componentStatus.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
								|| componentStatus.equals(EnumStatusManager.Status.APPROVED.value()))) {

					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	/**
	 * 
	 * @return true/false
	 */
	public boolean getElementIsLockedByComponent() {

		for (ComponentElementModel componentElementInUse : getComponentElementList()) {
			ComponentVersionModel componentVersion = componentElementInUse.getComponentStand().getComponentVersion();
			Long componentStatus = componentVersion.getEnumStatus().getEnumStatusId();
			if (componentVersion.getComponent().getOwner().equals(getElementVersion().getElement().getOwner())
					&& (componentStatus.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
							|| componentStatus.equals(EnumStatusManager.Status.APPROVED.value()))) {

				return Boolean.TRUE;
			}

		}
		return Boolean.FALSE;
	}

	public boolean isShowConfirmChangeUsePopup() {
		return showConfirmChangeUsePopup;
	}

	public void setShowConfirmChangeUsePopup(boolean showConfirmChangeUsePopup) {
		this.showConfirmChangeUsePopup = showConfirmChangeUsePopup;
	}

	public void setUsedVersion(String usedVersion) {
		this.usedVersion = usedVersion;
	}

	public String getUsedVersion() {
		return usedVersion;
	}

	public void setFileToDelete(String fileToDelete) {
		this.fileToDelete = fileToDelete;
	}

	public String getFileToDelete() {
		return fileToDelete;
	}

	public boolean isShowHistoryDetailpopup() {
		return showHistoryDetailpopup;
	}

	public void setShowHistoryDetailpopup(boolean showHistoryDetailpopup) {
		this.showHistoryDetailpopup = showHistoryDetailpopup;
	}

	public String getChangeToShow() {
		return changeToShow;
	}

	public void setChangeToShow(String changeToShow) {
		this.changeToShow = changeToShow;
	}

	public List<ElementVersionChangeModel> getChangeDetails() {
		return changeDetails;
	}

	public void setChangeDetails(List<ElementVersionChangeModel> changeDetails) {
		this.changeDetails = changeDetails;
	}

}
