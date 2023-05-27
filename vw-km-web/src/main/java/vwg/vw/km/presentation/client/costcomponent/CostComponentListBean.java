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

package vwg.vw.km.presentation.client.costcomponent;

import java.util.ArrayList;
import java.util.List;

import javax.faces.application.Resource;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.application.service.logic.CostComponentService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentNode;
import vwg.vw.km.presentation.client.navigation.node.working.WorkingCostComponentNode;
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
 * @author zouhairs changed by $Author: saidi $
 * @version $Revision: 1.48 $ $Date: 2018/02/13 07:45:41 $
 */
public class CostComponentListBean extends BaseBean<CostComponentDTO, CostComponentService> {
	/**
	 */
	private static final long serialVersionUID = 789547302569333015L;

	private final Log log = LogManager.get().getLog(CostComponentListBean.class);

	private FileResource xmlResource;

	private boolean showXmlViewFile = Boolean.FALSE;

	private SelectItem[] libraryItems = null;

	private SelectItem[] userItems = null;

	private SelectItem[] ownerItems = null;

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.costComponent.search");
	}

	/**
	 * Show the popup model box used to the XML export
	 */
	public void showXmlExportModal() {
		String objectType = (String) getRequestParameterMap().get("object_type");
		dto.setObjectType(objectType);
		loadAvailableUsersAndLibraries();
		dto.setShowXmlExportPopup(true);
		setShowXmlViewFile(Boolean.FALSE);
	}

	/**
	 * Cancel the XML export
	 */
	public void cancelExport() {
		dto.setShowXmlExportPopup(false);
		dto.setExportDate(null);
		dto.setExportWithoutAttributes(Boolean.FALSE);
		dto.setExportApprovedComponent(Boolean.FALSE);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		if (xmlResource == null) {
			xmlResource = new FileResource(null, "Prozessdaten.xml");
		}
	}

	/**
	 * Export an XML file. Show the popup model box used to manage the export Check if the export date is not null
	 * 
	 * @param event
	 */
	public void xmlExport(ActionEvent event) {
		log.info("Try to export xml...");
		setShowXmlViewFile(Boolean.FALSE);

		BaseDateTime date = null;
		if (dto.getExportDate() != null) {
			date = new BaseDateTime(new BaseDateTime(dto.getExportDate()).getDateSeparatedPt());
		}
		if (date != null) {
			date = date.addSeconds(59 * 60 + 23 * 60 * 60 + 59);
			log.info("Xml Export date: " + date);
			List<String> userIds = new ArrayList<String>();
			String exportTarget = (String) getRequestParameterMap().get("export_target");
			dto.setExportTarget(exportTarget);

			if (dto.getExportDate() != null) {
				Long folderId = 7L;
				if ("C".equals(dto.getObjectType())) {
					folderId = navigationTree.getSelectedNodeObject().getNodeId();
				}
				userIds.addAll(dto.getSelectedUsersIds());
				try {
					if ((dto.getSelectedLibraryIds() != null && dto.getSelectedLibraryIds().size() > 1)
							&& dto.getSelectedPrimaryLibrary() == null) {
						dto.setShowXmlExportPopup(false);
						navigationTree.openErrorPopup(getMessage("costcomponent.export.xml.error"));
						return;
					}
					Object[] fileNameAndContent = service.loadXmlExpotedFile(date, dto, userIds, folderId);
					if (fileNameAndContent == null) {
						dto.setShowXmlExportPopup(false);
						navigationTree.openErrorPopup(getMessage("costcomponent.export.xml.noresult.error"));
						return;
					}
					xmlResource = new FileResource((byte[]) fileNameAndContent[1], (String) fileNameAndContent[0]);
					setShowXmlViewFile(Boolean.TRUE);

				} catch (Throwable ex) {
					log.error("Error : ", ex);
				}
			}
		}
	}

	/**
	 * Return the value of flag to show the XML export file
	 * 
	 * @return true/false
	 */
	public boolean isShowXmlViewFile() {
		return showXmlViewFile;
	}

	/**
	 * Set the flag to show the XML export file
	 * 
	 * @param showXmlViewFile
	 */
	public void setShowXmlViewFile(boolean showXmlViewFile) {
		this.showXmlViewFile = showXmlViewFile;
	}

	/**
	 * Return the download resource of the XML export file
	 * 
	 * @return Resource
	 */
	public Resource getXmlResource() {
		return xmlResource;
	}

	/**
	 * load available users and libraries.
	 */
	public void loadAvailableUsersAndLibraries() {
		dto = service.loadBrandUsersAndLibraries(dto);
		setUserItems(fillUsersItemsFromDTO());
		setLibraryItems(fillLibraryItemsFromDTO());
		setOwnerItems(fillOwnerLibraryItemsFromDTO());
	}

	/**
	 * Fill the library items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillLibraryItemsFromDTO() {
		List<LibraryModel> libraries = dto.getLibraries();
		SelectItem[] items = null;
		if (libraries != null) {
			items = new SelectItem[libraries.size()];
		} else {
			items = new SelectItem[0];
		}
		if (libraries != null) {
			LibraryModel library;
			for (int i = 0; i < libraries.size(); i++) {
				library = libraries.get(i);
				items[i] = new SelectItem(String.valueOf(library.getLibraryId()), library.getName());
			}
		}
		return items;
	}

	/**
	 * Fill the Owner Library items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillOwnerLibraryItemsFromDTO() {
		List<LibraryModel> libraries = dto.getLibraries();
		SelectItem[] items = null;
		if (libraries != null) {
			items = new SelectItem[libraries.size()];
		} else {
			items = new SelectItem[0];
		}
		if (libraries != null) {
			LibraryModel library;
			for (int i = 0; i < libraries.size(); i++) {
				library = libraries.get(i);
				items[i] = new SelectItem(String.valueOf(library.getLibraryId()), library.getBrand().getBrandId());
			}
		}
		return items;
	}

	/**
	 * Fill the brand items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillUsersItemsFromDTO() {
		List<BrandModel> brands = dto.getBrandUsers();
		SelectItem[] items = null;
		if (brands != null) {
			items = new SelectItem[brands.size()];
		} else {
			items = new SelectItem[0];
		}
		if (brands != null) {
			BrandModel brand;
			for (int i = 0; i < brands.size(); i++) {
				brand = brands.get(i);
				items[i] = new SelectItem(String.valueOf(brand.getBrandId()), brand.getBrandId());
			}
		}
		return items;
	}

	public SelectItem[] getLibraryItems() {
		return libraryItems;
	}

	public void setLibraryItems(SelectItem[] libraryItems) {
		this.libraryItems = libraryItems;
	}

	public SelectItem[] getOwnerItems() {
		return ownerItems;
	}

	public void setOwnerItems(SelectItem[] ownerItems) {
		this.ownerItems = ownerItems;
	}

	public SelectItem[] getUserItems() {
		return userItems;
	}

	public void setUserItems(SelectItem[] userItems) {
		this.userItems = userItems;
	}

	/**
	 * Go to Baustein in the tree from the search detail
	 * 
	 * @param event
	 */
	public void showComponent(ActionEvent event) {
		String componentId = (String) getRequestParameterMap().get("component_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		String inactive = (String) getRequestParameterMap().get("inactive");
		FolderModel folder = navigationTree.getNavigationService().getFolder(Long.parseLong(folderId));
		log.info("Show Component ..." + componentId);
		if (componentId != null && !"".equals(componentId)) {

			if ("true".equals(inactive)) {
				FolderNode componentNode = (FolderNode) navigationTree.getRootComponentNode();
				FolderNode searchFolder = (FolderNode) componentNode.getNodeById(
						StaticNodeIdEnum.COST_COMPONENT_SEARCH_FOLDER_NODE.value(), EnumObjectTypeManager.FOLDER_ID);
				searchFolder.setExpanded(true);
				searchFolder.expandClicked(event);
				FolderNode inactiveFolder = (FolderNode) searchFolder.getNodeById(-47L,
						EnumObjectTypeManager.FOLDER_ID);

				CostComponentNode n = (CostComponentNode) goToInactiveNode(inactiveFolder, Long.parseLong(componentId),
						Long.parseLong(folderId), EnumObjectTypeManager.COMPONENT_ID, event);
				if (n != null) {
					n.nodeClicked(event);
				}
			} else {

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
	}

	private Node goToInactiveNode(FolderNode actualNode, Long nodeId, Long startFolderId, Long objectTypeId,
			ActionEvent event) {
		List<Long> folderIdsHierarchy = navigationTree.getNavigationService().getFolderIdsHierarchy(startFolderId);
		for (int i = 1; i < folderIdsHierarchy.size(); i++) {
			log.info(folderIdsHierarchy.get(i) + " actualNode: " + actualNode);
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(-folderIdsHierarchy.get(i),
					EnumObjectTypeManager.FOLDER_ID);
		}
		actualNode.setExpanded(true);
		actualNode.expandClicked(event);
		return actualNode.getNodeById(nodeId, objectTypeId);
	}
}
