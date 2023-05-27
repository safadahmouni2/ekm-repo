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

package vwg.vw.km.presentation.client.changehistory;

import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.ChangeHistoryDTO;
import vwg.vw.km.application.service.logic.ChangeHistoryService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.changehistory.ChangeHistoryNode;
import vwg.vw.km.presentation.client.navigation.node.changehistory.LastChangesNotificationsNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentNode;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostComponentNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostElementNode;
import vwg.vw.km.presentation.client.util.FileResource;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author boubakers changed by $Author: abidh $
 * @version $Revision: 1.31 $ $Date: 2019/11/22 09:27:07 $
 */
public class ChangeHistoryBean extends BaseBean<ChangeHistoryDTO, ChangeHistoryService> {

	private static final long serialVersionUID = 8577689730007775141L;

	private final static Log log = LogManager.get().getLog(ChangeHistoryBean.class);

	private static String CHANGE_HISTORY_DETAILS_MASK = "./changeHistory/changeHistoryDetails.xhtml";

	private Long objectType;

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	private SelectItem[] brandItems;

	private FileResource excelResource;

	private boolean showExcelViewFile = Boolean.FALSE;

	private boolean showExcelExportPopup = Boolean.FALSE;

	private boolean noSearchResult = Boolean.FALSE;

	private int currentIndex;

	private int maxRange;

	protected NavigationBean treeBean;

	@Override
	public String specificSearchMessage() {
		return getMessage("action.workArea.search");
	}

	/**
	 * Used to search history entry based on criteria entred by user
	 * 
	 * @param event
	 *            ActionEvent object
	 */
	public void search(ActionEvent event) {

		if (dto.getSearchObject().searchInDBNotRequired()) {
			navigationTree.openErrorPopup(getMessage("changehistory.search.filter.nosense"));
			return;
		}

		if (log.isDebugEnabled()) {
			log.debug("search object: " + dto.getSearchObject());
		}
		service.search(dto);
		currentIndex = 0;
		if (dto.getComponentChangesResults().isEmpty() && dto.getElementChangesResults().isEmpty()) {
			setNoSearchResult(true);
		} else {
			setNoSearchResult(false);
		}
	}

	/**
	 * Return back from the details mak to the search one.
	 * 
	 * @param event
	 */
	public void back(ActionEvent event) {
		if (dto.getFromLastChangesPage()) {
			navigationTree.getSelectedNodeObject()
					.setTemplateName(LastChangesNotificationsNode.CHANGE_HISTORY_NOTIFICATION_MASK);
		} else {
			navigationTree.getSelectedNodeObject().setTemplateName(ChangeHistoryNode.CHANGE_HISTORY_SEARCH_MASK);
		}
	}

	/**
	 * open changes for a choosen version
	 * 
	 * @param event
	 */
	public void openDetails(ActionEvent event) {
		String objectId = (String) getRequestParameterMap().get("object_id");
		objectType = Long.valueOf((String) getRequestParameterMap().get("object_type"));
		service.searchDetails(Long.valueOf(objectId), objectType, dto);
		navigationTree.getSelectedNodeObject().setTemplateName(CHANGE_HISTORY_DETAILS_MASK);
		if (EnumObjectTypeManager.ELEMENT_ID.equals(objectType)) {
			maxRange = dto.getSortedElementId().size();
			currentIndex = dto.getSortedElementId().indexOf(Long.valueOf(objectId));
		} else {
			maxRange = dto.getSortedComponentId().size();
			currentIndex = dto.getSortedComponentId().indexOf(Long.valueOf(objectId));
		}
	}

	/**
	 * navigate to next/previous element/component details
	 * 
	 * @param event
	 */
	public void navigateIntoDetails(ActionEvent event) {
		String direction = (String) getRequestParameterMap().get("direction");
		if ("N".equals(direction)) {
			currentIndex++;
		} else {
			currentIndex--;
		}
		Long sortedObjectId;
		if (EnumObjectTypeManager.ELEMENT_ID.equals(objectType)) {
			sortedObjectId = dto.getSortedElementId().get(currentIndex);
		} else {
			sortedObjectId = dto.getSortedComponentId().get(currentIndex);
		}
		service.searchDetails(sortedObjectId, objectType, dto);
	}

	@Override
	public void specificLoadDTO() {
		setBrandItems(fillBrandItemsFromDTO());
	}

	/**
	 * Fill the class items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillBrandItemsFromDTO() {
		List<BrandModel> brands = dto.getBrands();
		SelectItem[] items = null;
		if (brands != null) {
			items = new SelectItem[1 + brands.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select.all"));
		items[0] = emptyItem;
		if (brands != null) {
			BrandModel brand;
			for (int i = 0; i < brands.size(); i++) {
				brand = brands.get(i);
				items[i + 1] = new SelectItem(brand.getBrandId(), brand.getBrandId());
			}
		}
		return items;
	}

	public SelectItem[] getBrandItems() {
		return brandItems;
	}

	public void setBrandItems(SelectItem[] brandItems) {
		this.brandItems = brandItems;
	}

	/**
	 * The excel export is based on a template file. Steps: - find cost component by version date - load the template in
	 * a HSSFWorkbook object - Fill the data from cost component founded - Generate an Excel file
	 * 
	 * @param event
	 */
	public void excelExport(ActionEvent event) {
		setShowExcelViewFile(Boolean.FALSE);
		Object[] fileNameAndContent = null;
		try {
			fileNameAndContent = service.loadExcelExpotedFile(dto);
			if (fileNameAndContent[1] != null) {
				excelResource = new FileResource((byte[]) fileNameAndContent[1], (String) fileNameAndContent[0]);
				setShowExcelViewFile(Boolean.TRUE);
			}
		} catch (Throwable ex) {
			log.error("Error :", ex);
		}
	}

	/**
	 * Show the popup model box used to the Excel export
	 */
	public void showExcelExportModal(ActionEvent event) {
		setShowExcelExportPopup(true);
		setShowExcelViewFile(Boolean.FALSE);
		excelExport(event);
	}

	/**
	 * Cancel Excel export
	 */
	public void cancelExport() {
		setShowExcelExportPopup(false);
	}

	/**
	 * close excel export pop up when resource link is clicked
	 * 
	 * @param vce
	 */
	public void excelResourceClicked(ValueChangeEvent vce) {
		log.info("Excel was download by " + dto.getUserLogged().getFullName());
		cancelExport();
	}

	public FileResource getExcelResource() {
		return excelResource;
	}

	public void setExcelResource(FileResource excelResource) {
		this.excelResource = excelResource;
	}

	/**
	 * Return the value of flag to show the Excel export file
	 * 
	 * @return true/false
	 */
	public boolean isShowExcelViewFile() {
		return showExcelViewFile;
	}

	/**
	 * Set the flag to show the Excel export file
	 * 
	 * @param showExcelViewFile
	 */
	public void setShowExcelViewFile(boolean showExcelViewFile) {
		this.showExcelViewFile = showExcelViewFile;
	}

	public boolean isShowExcelExportPopup() {
		return showExcelExportPopup;
	}

	public void setShowExcelExportPopup(boolean showExcelExportPopup) {
		this.showExcelExportPopup = showExcelExportPopup;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public Long getObjectType() {
		return objectType;
	}

	public void setObjectType(Long objectType) {
		this.objectType = objectType;
	}

	public int getMaxRange() {
		return maxRange;
	}

	public void setMaxRange(int maxRange) {
		this.maxRange = maxRange;
	}

	public void setNoSearchResult(boolean isNoSearchResult) {
		this.noSearchResult = isNoSearchResult;
	}

	public boolean isNoSearchResult() {
		return noSearchResult;
	}

	/**
	 * Go to Baustein in the tree from the search detail
	 * 
	 * @param event
	 */
	public void showComponent(ActionEvent event) {
		String componentId = (String) getRequestParameterMap().get("component_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		FolderModel folder = navigationTree.getNavigationService().getFolder(Long.parseLong(folderId));
		log.info("Show Component ..." + componentId);
		if (componentId != null && !"".equals(componentId)) {
			// component is in a working folder
			if (folder.getCreatorRefId() != null) {
				Long userId = folder.getCreatorRefId();
				FolderNode workingNode = navigationTree.getWorkingNode();
				workingNode.setExpanded(true);
				workingNode.expandClicked(event);

				FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
						EnumObjectTypeManager.FOLDER_ID);
				WorkingCostComponentNode n = (WorkingCostComponentNode) navigationTree.goToNode(userWorkingFolder,
						Long.parseLong(componentId), Long.parseLong(folderId), EnumObjectTypeManager.COMPONENT_ID,
						event);
				if (n != null) {
					n.nodeClicked(event);
				}
			} else {
				CostComponentNode n = (CostComponentNode) navigationTree.goToNode(Long.parseLong(componentId),
						Long.parseLong(folderId), EnumObjectTypeManager.COMPONENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
				}
			}
		}
	}

	/**
	 * Go to Element in the tree from the search detail
	 * 
	 * @param event
	 */
	public void showElement(ActionEvent event) {
		String elementId = (String) getRequestParameterMap().get("element_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		log.info("Show Element,folder = " + elementId + "," + folderId);
		FolderModel folder = navigationTree.getNavigationService().getFolder(Long.parseLong(folderId));
		// element is in a working folder
		if (elementId != null && !"".equals(elementId) && !"null".equals(elementId)) {
			if (folder.getCreatorRefId() != null) {
				Long userId = folder.getCreatorRefId();
				FolderNode workingNode = navigationTree.getWorkingNode();
				workingNode.setExpanded(true);
				workingNode.expandClicked(event);

				FolderNode userWorkingFolder = (FolderNode) workingNode.getNodeById(userId,
						EnumObjectTypeManager.FOLDER_ID);
				WorkingCostElementNode n = (WorkingCostElementNode) navigationTree.goToNode(userWorkingFolder,
						Long.parseLong(elementId), Long.parseLong(folderId), EnumObjectTypeManager.ELEMENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
					n.setExpanded(true);
					n.expandClicked(event);
				}
			} else {
				CostElementNode n = (CostElementNode) navigationTree.goToNode(Long.parseLong(elementId),
						Long.parseLong(folderId), EnumObjectTypeManager.ELEMENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
					n.setExpanded(true);
					n.expandClicked(event);
				}
			}
		}
	}

	/**
	 * Used to Load News list without a clicked node on LastChangeHistory
	 * 
	 * @param event
	 *            ActionEvent object
	 */
	public Boolean getLoadWithoutClickedNode() {
		log.info("loadWithoutClickedNode changeHistoryDTO method started");

		ChangeHistoryDTO dto = new ChangeHistoryDTO();
		dto.setFromLastChangesPage(Boolean.TRUE);
		setDto(dto);
		// If the user has Logged IN once, get changes list from last loginTime to current login time
		if (getUserFromSession().getPreviousLoginTime() != null) {
			setDto(service.loadDTOForLastChangeNotifications(dto));
			// From Date is last login dateTime
			dto.getSearchObject().setFromDate(getUserFromSession().getPreviousLoginTime().getDate());
			// Get last Changes from last login
			service.search(dto);
		}

		log.info("loadWithoutClickedNode changeHistoryDTO method executed");

		return true;
	}

}
