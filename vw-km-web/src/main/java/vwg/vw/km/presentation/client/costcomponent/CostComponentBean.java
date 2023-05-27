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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesEvent;
import javax.faces.event.MethodExpressionActionListener;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.ace.component.fileentry.FileEntry;
import org.icefaces.ace.component.fileentry.FileEntryEvent;
import org.icefaces.ace.component.fileentry.FileEntryResults;
import org.icefaces.ace.component.fileentry.FileEntryStatuses;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.ace.component.submenu.Submenu;
import org.icefaces.ace.model.DefaultMenuModel;
import org.icefaces.ace.model.MenuModel;
import org.icefaces.ace.model.table.RowStateMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostComponentDTO;
import vwg.vw.km.application.service.logic.CostComponentService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.EnumComponentClassModel;
import vwg.vw.km.integration.persistence.model.EnumComponentTypeModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentInProcessVersionNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentVersionStandNode;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementNode;
import vwg.vw.km.presentation.client.util.FileResource;
import vwg.vw.km.presentation.client.util.UploadedFile;

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
 * @author zouhairs changed by $Author: abidh $
 * @version $Revision: 1.213 $ $Date: 2019/11/27 14:39:00 $
 */
public class CostComponentBean extends BaseDetailBean<CostComponentDTO, CostComponentService> {

	private static final long serialVersionUID = 3557215757545745325L;

	private final Log log = LogManager.get().getLog(CostComponentBean.class);

	public static final String COMPONENT_EXIST_MESSAGE_ID = "costComponent.exist.sameDesignationOrNumber.message";

	public static final String COMPONENT_DESCRIPTION_TOO_LONG = "costComponent.descirption.maxlength";

	public static final String COMPONENT_SERVICE_CONTENT_AT_LEAST_ONE = "costComponent.serviceContent.fillAtLeastOne";

	private static final int MAX_IMAGE_PREVIEW_SIZE = 1048576;// in bytes = 1MB.

	protected NavigationElementBean navigationElementBean;

	public void setNavigationElementBean(NavigationElementBean navigationElementBean) {
		this.navigationElementBean = navigationElementBean;
	}

	private List<CostElementNode> selectedNodelist;

	// enumeration of different filter options
	private SelectItem[] displayFilterItems = new SelectItem[] {
			new SelectItem("ALL", getMessage(FILTER_ALL_MESSAGE_ID)),
			new SelectItem("ALL_FILLED", getMessage(FILTER_FILLED_MESSAGE_ID)),
			new SelectItem("ALL_EMPTY", getMessage(FILTER_EMPTY_MESSAGE_ID)),
			new SelectItem("MECHANICS", getMessage(FILTER_MECANICS_MESSAGE_ID)),
			new SelectItem("ELECTRICAL", getMessage(FILTER_ELECTRICAL_MESSAGE_ID)),
			new SelectItem("HIDE_PARENT_COST", getMessage(FILTER_HIDE_PARENT_COST_MESSAGE_ID)) };

	private SelectItem[] componentTypeItems;

	private SelectItem[] componentClassItems;

	private FileResource excelResource;

	private FileResource xmlResource;

	private boolean showExcelViewFile = Boolean.FALSE;

	private boolean showXmlComponentViewFile = Boolean.FALSE;

	private boolean showXmlViewFile = Boolean.FALSE;

	private UIData costComponentAttributeTable;

	private UIData versionHistoryTable;

	private UIData costElementTable;

	private UIData componentElementTable1;

	private UIData componentElementTable2;

	private Integer availableCostElementsPage = 1;

	private SelectItem[] libraryItems = null;

	private SelectItem[] userItems = null;

	private SelectItem[] ownerItems = null;

	private SelectItem[] elementVersionsToAssignItems;

	private FileResource imagePreview;

	private String imagesServer;

	private String imagesServerUrl;

	private boolean showActivationError = Boolean.FALSE;

	private String activationErrorMessage = "";

	private UIData versionHistoryDetailTable;

	/*
	 * PTS Problem 26686 Which Table the user is using to manage element assigned to the selected component
	 */
	int fromTab = 1;

	private RowStateMap rowStateMapRight = new RowStateMap();

	/**
	 * Return the list of selected element node objects
	 * 
	 * @return CostElementNode list
	 */
	public List<CostElementNode> getSelectedNodelist() {
		return selectedNodelist;
	}

	/**
	 * Setter of selected element node objects
	 * 
	 * @param selectedNodelist
	 */
	public void setSelectedNodelist(List<CostElementNode> selectedNodelist) {
		this.selectedNodelist = selectedNodelist;
	}

	/**
	 * Getter of costElement attribute table
	 * 
	 * @return UIData object
	 */
	public UIData getCostComponentAttributeTable() {
		return costComponentAttributeTable;
	}

	/**
	 * Setting costElement attribute table
	 * 
	 * @param costComponentAttributeTable
	 */
	public void setCostComponentAttributeTable(UIData costComponentAttributeTable) {
		this.costComponentAttributeTable = costComponentAttributeTable;
	}

	/**
	 * Return the cost element version history table
	 * 
	 * @return UIData object
	 */
	public UIData getVersionHistoryTable() {
		return versionHistoryTable;
	}

	/**
	 * Setting a new or a loaded costElement version history table
	 * 
	 * @param versionHistoryTable
	 */
	public void setVersionHistoryTable(UIData versionHistoryTable) {
		this.versionHistoryTable = versionHistoryTable;
	}

	/**
	 * Return the cost element table
	 * 
	 * @return UIData object
	 */
	public UIData getCostElementTable() {
		return costElementTable;
	}

	/**
	 * Setter of the cost element table
	 * 
	 * @param costElementTable
	 */
	public void setCostElementTable(UIData costElementTable) {
		this.costElementTable = costElementTable;
	}

	/**
	 * Getter of component element table
	 * 
	 * @return UIData object
	 */
	public UIData getComponentElementTable1() {
		return componentElementTable1;
	}

	/**
	 * Setter of component element table
	 * 
	 * @param componentElementTable1
	 */
	public void setComponentElementTable1(UIData componentElementTable1) {
		this.componentElementTable1 = componentElementTable1;
	}

	/**
	 * Getter of component element table
	 * 
	 * @return UIData object
	 */
	public UIData getComponentElementTable2() {
		return componentElementTable2;
	}

	/**
	 * Setter of component element table
	 * 
	 * @param componentElementTable1
	 */
	public void setComponentElementTable2(UIData componentElementTable2) {
		this.componentElementTable2 = componentElementTable2;
	}

	/**
	 * Return the value of available cost elements page
	 * 
	 * @return integer
	 */
	public Integer getAvailableCostElementsPage() {
		if (dto != null && dto.getCostElementsPage() != null) {
			availableCostElementsPage = dto.getCostElementsPage();
		}
		return availableCostElementsPage;
	}

	/**
	 * Set the value of available cost elements page
	 * 
	 * @param availableCostElementsPage
	 */
	public void setAvailableCostElementsPage(Integer availableCostElementsPage) {
		this.availableCostElementsPage = availableCostElementsPage;
	}

	public RowStateMap getRowStateMapRight() {
		return rowStateMapRight;
	}

	public void setRowStateMapRight(RowStateMap rowStateMapRight) {
		this.rowStateMapRight = rowStateMapRight;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		if (!dto.isSearchMode()) {
			String searchFolderName = "";
			Node node = navigationTree.getSelectedNodeObject();
			if (node.isLeaf()) {
				if (node instanceof CostComponentVersionStandNode) {
					searchFolderName = node.getParent().getParent().getParent().getMenuDisplayText();
				} else {
					searchFolderName = node.getParent().getParent().getMenuDisplayText();
				}
			} else {
				searchFolderName = node.getParent().getMenuDisplayText();
			}
			dto.setSearchString(searchFolderName + " " + getMessage("field.search"));
		}
		setComponentTypeItems(fillTypeItemsFromDTO());
		setComponentClassItems(fillClassItemsFromDTO());
		navigationElementBean.setAssignedElementIds(getAssignedElementIds());
		if (xmlResource == null) {
			xmlResource = new FileResource(null, "Prozessdaten.xml");
		}
	}

	/**
	 * Return the list of assigned element
	 * 
	 * @return list of id
	 */
	private List<Long> getAssignedElementIds() {
		List<Long> returnList = new ArrayList<Long>();
		List<ComponentElementModel> availableComponentElementList = dto.getComponentVersion()
				.getAvailableComponentElementList();
		if (availableComponentElementList != null) {
			for (ComponentElementModel cElementModel : availableComponentElementList) {
				returnList.add(cElementModel.getElementVersion().getElement().getElementId());
			}
		}
		return returnList;
	}

	/**
	 * Fill the type items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillTypeItemsFromDTO() {
		List<EnumComponentTypeModel> componentTypes = dto.getComponentTypes();
		SelectItem[] items = null;
		if (componentTypes != null) {
			items = new SelectItem[1 + componentTypes.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (componentTypes != null) {
			EnumComponentTypeModel tmpEnumComponentTypeModel;
			for (int i = 0; i < componentTypes.size(); i++) {
				tmpEnumComponentTypeModel = componentTypes.get(i);
				items[i + 1] = new SelectItem(tmpEnumComponentTypeModel.getEnumComponentTypeId().toString(),
						tmpEnumComponentTypeModel.getComponentType());
			}
		}
		return items;
	}

	/**
	 * Fill the class items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillClassItemsFromDTO() {
		List<EnumComponentClassModel> componentClasses = dto.getComponentClasses();
		SelectItem[] items = null;
		if (componentClasses != null) {
			items = new SelectItem[1 + componentClasses.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (componentClasses != null) {
			EnumComponentClassModel tmpEnumComponentClassModel;
			for (int i = 0; i < componentClasses.size(); i++) {
				tmpEnumComponentClassModel = componentClasses.get(i);
				items[i + 1] = new SelectItem(tmpEnumComponentClassModel.getEnumComponentClassId().toString(),
						tmpEnumComponentClassModel.getComponentClass());
			}
		}
		return items;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#specificSave(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificSave(ActionEvent event) {
		log.info("Try to save " + dto.getComponentVersion());
		service.getExistComponentWithSameNumberOrDesignation(dto);
		ComponentVersionModel eModel = dto.getComponentVersion();
		if (dto.getExistComponentWithSameNumberOrDesignation()) {
			return getMessage(COMPONENT_EXIST_MESSAGE_ID);
		} else if (eModel.getDescription() != null && eModel.getDescription().length() > 2048) {
			return getMessage(COMPONENT_DESCRIPTION_TOO_LONG);
		} else if ((eModel.getMechanicalConstruction() == null || eModel.getMechanicalConstruction().equals(""))
				&& (eModel.getMechanicalExecution() == null || eModel.getMechanicalExecution().equals(""))
				&& (eModel.getElectric() == null || eModel.getElectric().equals(""))) {
			return getMessage(COMPONENT_SERVICE_CONTENT_AT_LEAST_ONE);
		}
		Long fromStatus = dto.getComponentVersion().getEnumStatus().getEnumStatusId();
		service.save(dto);
		service.loadChangesForStand(dto);
		if ((EnumStatusManager.Status.NEW.value().equals(fromStatus) || fromStatus == null)
				&& EnumStatusManager.Status.CREATED.value()
						.equals(dto.getComponentVersion().getEnumStatus().getEnumStatusId())) {
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.COMPONENT_ID, fromStatus,
					EnumStatusManager.Status.CREATED.value());
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#afterSave()
	 */
	@Override
	protected void afterSave(ActionEvent event) {
		Node actualNode = navigationTree.getSelectedNodeObject();
		if (actualNode instanceof BranchNode) {
			actualNode.setMenuDisplayText(getDisplayText());
			actualNode.setNodeId(getNodeId());
			actualNode.setOwnerImg(dto.getComponentVersion().getComponent().getOwnerImg());

			// ZS: PTS_Problem-35917: When adding a new EL or BS add the new node to his
			// parent child list
			actualNode.getParent().addChild(actualNode);

			actualNode.nodeClicked(event);
			// ZS: PTS-Ticket-23111 Update of the parent node should be only the
			// bearbeitungsversion case
		} else if (actualNode instanceof CostComponentInProcessVersionNode) {
			actualNode.getParent().setMenuDisplayText(getDisplayText());
			actualNode.getParent().setNodeId(getNodeId());
			actualNode.setNodeId(dto.getComponentVersion().getComponentVersionId());
		}
		// ZS: PTS_Problem-35155: Make possible to navigate in the tree when the user
		// choose to save a previous change
		Node gotoNode = navigationTree.getGotoNode();
		if (gotoNode != null) {
			gotoNode.nodeClicked(event);
		}
		navigationTree.setGotoNode(null);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getSaveMessage()
	 */
	@Override
	public String getSaveMessage() {
		return getMessage("action.costComponent.save");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return dto.getComponentVersion().getComponent().getNumberAndDesignation();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getComponentVersion().getComponent().getComponentId();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.costComponent.reload");
	}

	/**
	 * Set the new value of search filter
	 * 
	 * @param event
	 *            executed when UI element value is changed
	 */
	public void doChangeDisplayFilter(ValueChangeEvent event) {
		log.info("Display filter is " + dto.getUserLogged().getDisplayFilter());
		if (event.getNewValue() != null) {
			dto.getUserLogged().setDisplayFilter(event.getNewValue().toString());
			// ZS: PTS_Problem-36803: when the filter is changed set a random ID to the
			// input in the data table
			HtmlDataTable myTable = (HtmlDataTable) getCostComponentAttributeTable();
			for (UIComponent column : myTable.getChildren()) {
				for (UIComponent comp : column.getChildren()) {
					if (comp instanceof HtmlInputText) {
						comp.setId(generateRandomId(6));
					}
				}
			}

			service.filterComponentValues(dto);
			navigationTree.setDownActionDefintion(getMessage("action.costComponent.changeDisplayFilter",
					getMessage("costelement.elementvalue.filter." + event.getNewValue().toString())));
		} else {
			log.info("ValueChangeEvent: new value is null ");
		}
	}

	/**
	 * Return the different search filter value options
	 * 
	 * @return list of SelectItem objects
	 */
	public SelectItem[] getDisplayFilterItems() {
		return displayFilterItems;
	}

	/**
	 * Set the search filter value options
	 * 
	 * @param displayFilterItems
	 */
	public void setDisplayFilterItems(SelectItem[] displayFilterItems) {
		this.displayFilterItems = displayFilterItems;
	}

	/**
	 * Return the list of ComponentType items
	 * 
	 * @return list of SelectItem objects
	 */
	public SelectItem[] getComponentTypeItems() {
		return componentTypeItems;
	}

	/**
	 * Set the list of ComponentType items
	 * 
	 * @param elementCategoryItems
	 */
	public void setComponentTypeItems(SelectItem[] componentTypeItems) {
		this.componentTypeItems = componentTypeItems;
	}

	/**
	 * Return the list of ComponentClass items
	 * 
	 * @return list of SelectItem objects
	 */
	public SelectItem[] getComponentClassItems() {
		return componentClassItems;
	}

	/**
	 * Set the list of ComponentClass items
	 * 
	 * @param elementCategoryItems
	 */
	public void setComponentClassItems(SelectItem[] componentClassItems) {
		this.componentClassItems = componentClassItems;
	}

	/**
	 * Upload document to the cost component object. Steps: - Create a new documentPoolModel object - Check if the
	 * document is already exist - Check the validity of the file: max. length, type, an empty file name - Open an error
	 * model box in case of invalidity - Add the documentPoolModel object DTO document list
	 * 
	 * @param event
	 */

	/*
	 * 
	 * PTS-ticket-id: 10757 Responsible ABA
	 */
	public void uploadFile(FileEntryEvent event) {
		log.info("###########  Component attachment FILE upload ########");
		FileEntry fileEntry = (FileEntry) event.getSource();
		FileEntryResults results = fileEntry.getResults();
		List<DocumentPoolModel> documentList = dto.getDocumentList();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			FileEntryStatuses fileStatus = (FileEntryStatuses) fileInfo.getStatus();
			String contentType = fileInfo.getContentType();
			log.info("Uploading File name :" + fileInfo.getFileName() + " -File ContentType : " + contentType);

			// Check if the uploaded file extention is one of those extension
			// zip,jpeg,jpg,png,gif,pdf,pptx,ppt,xlsx,xls,docx,doc,xlsm,xltx,xltm if not,
			// the file will not be uploaded
			// and a pop-up message
			// will be displayed
			if (!contentType.equalsIgnoreCase("application/x-zip-compressed")
					&& !contentType.equalsIgnoreCase("image/pjpeg") && !contentType.equalsIgnoreCase("image/jpeg")
					&& !contentType.equalsIgnoreCase("image/x-png") && !contentType.equalsIgnoreCase("image/png")
					&& !contentType.equalsIgnoreCase("image/gif") && !contentType.equalsIgnoreCase("application/pdf")
					&& !contentType.equalsIgnoreCase("text/plain")
					&& !contentType.equalsIgnoreCase(
							"application/vnd.openxmlformats-officedocument.presentationml.presentation")
					&& !contentType.equalsIgnoreCase("application/vnd.ms-powerpoint")
					&& !contentType
							.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")// .xlsx
					&& !contentType
							.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.template") // .xltx
					&& !contentType.equalsIgnoreCase("application/vnd.ms-excel.sheet.macroEnabled.12")// .xlsm
					&& !contentType.equalsIgnoreCase("application/vnd.ms-excel.template.macroEnabled.12")// .xltm
					&& !contentType.equalsIgnoreCase("application/vnd.ms-excel")// .xls,.xlt,.xla
					&& !contentType
							.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
					&& !contentType.equalsIgnoreCase("application/msword")) {
				log.error("Attempt to upload an invalid document type");
				navigationTree.openErrorPopup(getMessage("documentPool.documentInvalidExtension"));
				break;
			} else {
				if (fileInfo.isSaved()) {
					// handle uploaded file
					String fullFileName = fileInfo.getFileName();
					DocumentPoolModel documentPool = new DocumentPoolModel();
					String description = fullFileName;
					if (fullFileName.lastIndexOf(".") > -1) {
						description = fullFileName.substring(0, fullFileName.lastIndexOf("."));
					}
					documentPool.setDescription(description);
					documentPool.setFileType(StringUtils.getFilenameExtension(fullFileName));
					documentPool.setPath(fullFileName);
					// testing if the document already exists
					boolean testExistDoc = false;
					for (DocumentPoolModel document : documentList) {
						if (document.getDescription().equals(documentPool.getDescription())) {
							testExistDoc = true;
							break;
						}
					}
					if (testExistDoc) {
						log.error("Attempt to upload an existant document");
						navigationTree.openErrorPopup(getMessage("documentPool.documentAlreadyExistant"));
						break;
					}
					try {
						File file = fileInfo.getFile();
						documentPool.setContent(getArrayByteFromFile(file));
						// Attempt to delete file from hard disk
						boolean success = file.delete();
						if (!success) {
							log.error("Delete: deletion failed");
						}
					} catch (IOException e) {
						log.error("Error uploading file : " + e);
					}
					documentList.add(documentPool);
					dto.getPageChanges().add("document_upload");
					navigationTree.setDownActionDefintion(getMessage("action.costElement.uploadFile"));

				} else {
					switch (fileStatus) {

					case MAX_FILE_SIZE_EXCEEDED: {
						// file too big error
						log.error("file too big error");
						navigationTree.openErrorPopup(
								getMessage("com.icesoft.faces.component.inputfile.SIZE_LIMIT_EXCEEDED"));
						break;
					}
					case INVALID: {
						// invalid file error
						log.error("invalid file error");
						navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.INVALID_FILE"));
						break;
					}

					case UNSPECIFIED_NAME: {

						log.error("empty file name");
						navigationTree
								.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.UNSPECIFIED_NAME"));
						break;
					}
					case INVALID_CONTENT_TYPE: {

						log.error("empty file name");
						navigationTree.openErrorPopup(
								getMessage("com.icesoft.faces.component.inputfile.INVALID_CONTENT_TYPE"));
						break;
					}
					case UNKNOWN_SIZE: {

						log.error("Unknown file size");
						navigationTree.openErrorPopup(getMessage("com.icesoft.faces.component.inputfile.UNKNOWN_SIZE"));
						break;
					}
					default:
						break;

					}
				}
			}
		}

		dto.setDocumentList(documentList);
		fileEntry = null;
	}

	public void uploadImagePreview(FileEntryEvent event) {

		log.info("###########  Component image preview FILE upload ########");

		ComponentVersionModel componentVersionModel = dto.getComponentVersion();

		FileEntry fileEntry = (FileEntry) event.getSource();
		FileEntryResults results = fileEntry.getResults();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {

			File file = fileInfo.getFile();

			String contentType = fileInfo.getContentType();

			log.info("Uploading image preview :" + fileInfo.getFileName() + " - File ContentType : " + contentType);

			// Check if the uploaded image extention is one of those extensions
			// jpg,png,gif if not,
			// the file will not be uploaded
			// and a pop-up message
			// will be displayed
			if (!contentType.equalsIgnoreCase("image/jpeg") && !contentType.equalsIgnoreCase("image/png")
					&& !contentType.equalsIgnoreCase("image/gif")) {
				log.error("Attempt to upload an invalid image preview type: " + contentType);
				navigationTree.openErrorPopup(getMessage("validation.imagepreview.format"));
				break;

			} else {

				long fileSize = fileInfo.getSize();

				if (fileSize > MAX_IMAGE_PREVIEW_SIZE) {

					// file too big error
					log.error("file too big error. size in Bytes: " + fileSize);
					navigationTree.openErrorPopup(getMessage("validation.imagepreview.maxlength"));
					break;

				} else {
					if (fileInfo.isSaved()) {
						try {
							componentVersionModel.setImagePreviewContent(getArrayByteFromFile(file));
							componentVersionModel.setImagePreviewName(fileInfo.getFileName());
							// Attempt to delete file from hard disk
							boolean success = file.delete();
							if (!success) {
								log.error("Delete: deletion failed");
							}
						} catch (IOException e) {
							log.error("Error uploading file : " + e);
						}

						dto.getPageChanges().add("document_image_preview");
						navigationTree.setDownActionDefintion(getMessage("action.costComponent.uploadFile"));
					}
				}
			}

		}

		fileEntry = null;
	}

	public long getImagePreviewId() {

		return BaseDateTime.getCurrentDateTime().getDate().getTime();
	}

	/**
	 * The file to be deleted is requested by his name. It is removed from documents list of the dto.
	 * 
	 * @param event
	 */
	public void deleteFile(ActionEvent event) {
		String fileName = dto.getFileToDelete();
		log.info("Delete document " + fileName);
		List<DocumentPoolModel> documentList = new ArrayList<DocumentPoolModel>();
		for (DocumentPoolModel document : dto.getDocumentList()) {
			// remove our file
			if (!document.getDescription().equals(fileName)) {
				documentList.add(document);
			}
		}
		dto.setDocumentList(documentList);
		dto.getPageChanges().add("document_delete");
		navigationTree.setDownActionDefintion(getMessage("action.costComponent.deleteFile"));

		closeDeleteFileConfirmation(event);
	}

	public void deleteImagePreview(ActionEvent event) {

		log.info("Delete image preview");

		dto.getComponentVersion().setImagePreviewContent(null);

		dto.getPageChanges().add("image_preview_delete");
		navigationTree.setDownActionDefintion(getMessage("action.costComponent.deleteFile"));
		closeDeleteImagePreviewConfirmation(event);
	}

	/**
	 * Inspect an input value change in order to perform a recalculation
	 * 
	 * @param event
	 */
	public void inputComponentValueChanged(ValueChangeEvent event) {
		int rowIndex = getCostComponentAttributeTable().getRowIndex();
		service.loadChangedComponentValue(dto, rowIndex, (BaseBigDecimal) event.getNewValue());
		checkChanged(event);
	}

	/**
	 * Close a pop-up model box
	 * 
	 * @param event
	 */
	public void closeModal(ActionEvent event) {
		dto.updateComponentValueDescription(-1, false);
		dto.setRemark("");
	}

	/**
	 * Open a pop-up model box
	 * 
	 * @param event
	 */
	public void openModal(ActionEvent event) {
		int rowIndex = getCostComponentAttributeTable().getRowIndex();
		dto.updateComponentValueDescription(rowIndex, true);
	}

	/**
	 * Return the list of allowed transitions used in the Akionen menu
	 * 
	 * @return list of MenuItem objects
	 */
	public MenuModel getAllowedTransitionsAsMenu() {
		ELContext elContext = FacesContext.getCurrentInstance().getELContext();
		ExpressionFactory expressionFactory = FacesContext.getCurrentInstance().getApplication().getExpressionFactory();
		// 'Action' menu
		MenuModel actionMenu = new DefaultMenuModel();
		Submenu actionSubmenu = new Submenu();
		actionSubmenu.setId("action");
		actionSubmenu.setLabel(getMessage("common.action.button"));
		actionMenu.addSubmenu(actionSubmenu);
		if (dto.getAllowedTransitions() != null) {
			for (EnumTransitionModel t : dto.getAllowedTransitions()) {
				MenuItem actionMenuItem = new MenuItem();
				actionMenuItem.setId("to_status_" + t.getComp_id().getToStatus().getEnumStatusId().toString());
				actionMenuItem.setValue(getMessage("transition.designation." + t.getDesignation()));
				actionMenuItem.addActionListener(new ActionListener() {
					@Override
					public void processAction(ActionEvent arg0) throws AbortProcessingException {
						goToStatus(arg0);
					}
				});

				// only the administrator can performs a freigeben action
				if (EnumStatusManager.Status.APPROVED.value().equals(t.getComp_id().getToStatus().getEnumStatusId())) {
					actionMenuItem.setRendered(dto.getAdminsitratorOnBrand());
				} else if (EnumStatusManager.Status.TO_BE_APPROVED.value()
						.equals(t.getComp_id().getToStatus().getEnumStatusId())) {
					actionMenuItem.setRendered(dto.getUserIsOwner() && dto.getUserHasWriteAccess());
				} else {
					actionMenuItem.setRendered(dto.getUserIsOwner() && !navigationTree.isVisitorUser());
				}
				actionSubmenu.getChildren().add(actionMenuItem);
			}
		}

		// new
		MenuItem actionNew = new MenuItem();
		actionNew.setId("new");
		actionNew.setValue(getMessage("costcomponent.new"));
		actionNew.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.createFromObjectClicked}", null, new Class[] { ActionEvent.class })));
		actionNew.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
				&& dto.getComponentVersion().getEnumStatus().getAddAllowed() != null
				&& dto.getComponentVersion().getEnumStatus().getAddAllowed() && !dto.isSearchMode()
				&& dto.getUserCanAdd() && navigationTree.getSelectedNodeObject() != null
				&& navigationTree.getSelectedNodeObject().getParent() != null
				&& navigationTree.getSelectedNodeObject().getParent().getParent() != null
				&& navigationTree.getSelectedNodeObject().getParent().getParent().getNodeId() > 0);
		actionSubmenu.getChildren().add(actionNew);
		// delete
		MenuItem actionDelete = new MenuItem();
		actionDelete.setId("delete");
		actionDelete.setValue(getMessage("costcomponent.delete"));
		actionDelete.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
				&& dto.getComponentVersion().getEnumStatus().getDeleteAllowed() != null
				&& dto.getComponentVersion().getEnumStatus().getDeleteAllowed() && dto.getUserCanDelete()
				&& dto.getUserHasWriteAccess());
		actionDelete.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.deleteObjectClicked}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionDelete);
		// delete_new
		MenuItem actionDeleteNew = new MenuItem();
		actionDeleteNew.setId("delete_new");
		actionDeleteNew.setImmediate(true);
		actionDeleteNew.setValue(getMessage("costcomponent.delete"));
		actionDeleteNew
				.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
						&& dto.getComponentVersion().getEnumStatus().getEnumStatusId() != null
						&& dto.getComponentVersion().getEnumStatus().getEnumStatusId() == 0);
		actionDeleteNew.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.backWithoutSave}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionDeleteNew);
		// export
		MenuItem actionExcelExport = new MenuItem();
		actionExcelExport.setId("export");
		actionExcelExport.setImmediate(true);
		actionExcelExport.setValue(getMessage("costcomponent.export"));
		actionExcelExport.setTarget("_blank");
		actionExcelExport
				.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed());
		actionExcelExport.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.showExcelExportModal}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionExcelExport);
		// export_component
		MenuItem actionComponentExport = new MenuItem();
		actionComponentExport.setId("xml_export_B");
		actionComponentExport.setImmediate(true);
		actionComponentExport.setValue(getMessage("costcomponent.export.xml.component"));
		actionComponentExport.setTarget("_blank");
		actionComponentExport
				.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed());
		actionComponentExport.addActionListener(
				new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext,
						"#{costComponentBean.showXmlExportModal}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionComponentExport);
		// export_folder
		MenuItem actionFolderExport = new MenuItem();
		actionFolderExport.setId("xml_export_C");
		actionFolderExport.setImmediate(true);
		actionFolderExport.setValue(getMessage("costcomponent.export.xml.folder"));
		actionFolderExport.setTarget("_blank");
		actionFolderExport
				.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed());
		actionFolderExport.addActionListener(
				new MethodExpressionActionListener(expressionFactory.createMethodExpression(elContext,
						"#{costComponentBean.showXmlExportModal}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionFolderExport);
		// export_root
		MenuItem actionRootExport = new MenuItem();
		actionRootExport.setId("xml_export_R");
		actionRootExport.setImmediate(true);
		actionRootExport.setValue(getMessage("costcomponent.export.xml"));
		actionRootExport.setTarget("_blank");
		actionRootExport
				.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed() != null
						&& dto.getComponentVersion().getEnumStatus().getExportAllowed());
		actionRootExport.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.showXmlExportModal}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionRootExport);
		// print
		MenuItem actionPrint = new MenuItem();
		actionPrint.setId("print");
		actionPrint.setValue(getMessage("costcomponent.print"));
		actionPrint.setRendered(dto.getComponentVersion() != null && dto.getComponentVersion().getEnumStatus() != null
				&& dto.getComponentVersion().getEnumStatus().getPrintAllowed() != null
				&& dto.getComponentVersion().getEnumStatus().getPrintAllowed());
		actionPrint.setUrl("javascript:friendlyPrint();");
		actionSubmenu.getChildren().add(actionPrint);
		// use
		MenuItem actionUse = new MenuItem();
		actionUse.setId("use");
		actionUse.setValue(getMessage("costcomponent.use"));
		actionUse.setRendered(dto.isComponentStandCanBeUsed());
		actionUse.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.useComponentStand}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionUse);
		// unuse
		MenuItem actionUnUse = new MenuItem();
		actionUnUse.setId("unuse");
		actionUnUse.setValue(getMessage("costcomponent.unuse"));
		actionUnUse.setRendered(dto.isComponentStandUsed());
		actionUnUse.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costComponentBean.unuseComponentStand}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionUnUse);
		return actionMenu;
	}

	/**
	 * Close pop-up model box used to confirm status change
	 * 
	 * @param event
	 */
	public void closeConfirmChangeStatusPopup(ActionEvent event) {
		dto.setChangeStatusComment(null);
		dto.setToStatusId(null);

		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the
		// release process
		// unlock the version
		service.saveComponentVersionLockStatus(dto, false);

		dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
	}

	/**
	 * Used to modify the status of a cost element
	 * 
	 * ZS: PTS_Requirement-22200: Remove the confirmation model box for the change status: - in Bearbeitung - zur
	 * Entscheidung
	 * 
	 * @param event
	 */
	public void goToStatus(ActionEvent event) {

		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the
		// release process
		ComponentVersionModel version = service.loadComponentVersion(dto);
		log.info("Try to change status for version: " + version);
		if (version.getIsLocked() != null && version.getIsLocked()) {
			dto.setChangeStatusComment(null);
			dto.setToStatusId(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
			String errorMsg = getMessage("costcomponent.writeAccessNotAllowed", version.getProcessor().getFullName());
			log.error(errorMsg);
			navigationTree.openErrorPopup(errorMsg);
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
			return;
		}
		// PTS_problem 26491 : 31.10.2013 :Bitte diesen Testfall beachten
		Long fromStatusId = dto.getComponentVersion().getEnumStatus().getEnumStatusId();
		if (!fromStatusId.equals(version.getEnumStatus().getEnumStatusId())) {
			dto.setChangeStatusComment(null);
			dto.setToStatusId(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
			String errorMsg = getMessage("costcomponent.changeStatus.alreadyChanged",
					version.getProcessor().getFullName());
			log.error(errorMsg);
			navigationTree.openErrorPopup(errorMsg);
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
			return;
		}

		Long toStatusId = new Long(event.getComponent().getId().replace("to_status_", ""));

		dto.setToStatusId(toStatusId);

		String fromStatusDesignation = getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = getMessage("status.designationbyId." + toStatusId);
		log.info("go From Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);

		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the
		// release process
		// lock the version if it will be released
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			service.saveComponentVersionLockStatus(dto, true);
			dto.setChangeStatusComment(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.TRUE);
			return;
		}

		String goToStatus = service.saveNewStatus(dto);
		if (goToStatus.equals("")) {
			dto = service.loadDTOForDetail(dto, dto.getComponentVersionId(), dto.getFolderId(), "VERSION",
					Boolean.FALSE);
			dto.getPageChanges().clear();
			dto.setShowSaveChangesPopup(Boolean.FALSE);
			closeConfirmChangeStatusPopup(event);
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.COMPONENT_ID, fromStatusId, toStatusId);
			navigationTree.setDownActionDefintion(
					getMessage("action.costComponent.changeStatus", fromStatusDesignation, toStatusDesignation));

			if (fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())
					&& toStatusId.equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				navigationTree.getSelectedNodeObject().getParent().getParent().nodeClicked(event);
			}
		} else {

			closeConfirmChangeStatusPopup(event);
			String errorMsg = getMessage("componentversion.consecutif.samestatus.exist", toStatusDesignation);
			log.error(errorMsg);
			navigationTree.openErrorPopup(errorMsg);
		}
	}

	/**
	 * save Release Status ZS: PTS_Requirement-22200: Remove the confirmation model box for the change status: - in
	 * Bearbeitung - zur Entscheidung
	 * 
	 * @param event
	 */
	public void saveReleaseStatus(ActionEvent event) {

		Long fromStatusId = dto.getComponentVersion().getEnumStatus().getEnumStatusId();
		Long toStatusId = dto.getToStatusId();
		String fromStatusDesignation = getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = getMessage("status.designationbyId." + toStatusId);
		log.info("go from Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);

		// check if the status of the Component version is not changed while the user
		// try to
		// release it
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			ComponentVersionModel version = service.loadComponentVersion(dto);
			if (!version.getEnumStatus().getEnumStatusId().equals(fromStatusId)) {
				closeConfirmChangeStatusPopup(event);
				navigationTree.openErrorPopup(getMessage("componentversion.released.cantbe.edited"));
				navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
				return;
			}
		}

		String goToStatus = service.saveNewStatus(dto);
		if (goToStatus.equals("")) {
			dto = service.loadDTOForDetail(dto, dto.getComponentVersionId(), dto.getFolderId(), "VERSION",
					Boolean.FALSE);
			dto.getPageChanges().clear();
			dto.setShowSaveChangesPopup(Boolean.FALSE);
			closeConfirmChangeStatusPopup(event);
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.COMPONENT_ID, fromStatusId, toStatusId);
			navigationTree.setDownActionDefintion(
					getMessage("action.costComponent.changeStatus", fromStatusDesignation, toStatusDesignation));

			if (fromStatusId.equals(EnumStatusManager.Status.IN_USE.value())
					&& toStatusId.equals(EnumStatusManager.Status.IN_PROGRESS.value())) {
				navigationTree.getSelectedNodeObject().getParent().getParent().nodeClicked(event);
			}
		} else if (goToStatus.equals("VERSION_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			navigationTree.openErrorPopup(getMessage("componentversion.cantbe.released"));
		} else if (goToStatus.equals("ASSIGN_FB_ELEMENT_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			if (dto.getComponentsTobeAffected() == null || "".equals(dto.getComponentsTobeAffected())) {
				String errorMsg = getMessage("componentversion.fbelement.cantbe.released", dto.getInEditElements());
				log.error(errorMsg);
				navigationTree.openErrorPopup(errorMsg);
			} else {
				String errorMsg = getMessage("componentversion.fbelement.cantbe.released.2", dto.getInEditElements(),
						dto.getComponentsTobeAffected());
				log.error(errorMsg);
				navigationTree.openErrorPopup(errorMsg);
			}
		} else if (goToStatus.equals("ASSIGN_ELEMENT_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			dto.setToStatusId(toStatusId);
			dto.setShowConfirmReleasePopup(Boolean.TRUE);
		} else if (goToStatus.equals("ASSIGN_LOCKED_ELEMENT_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			navigationTree.openErrorPopup(dto.getReleaseWorkingElErrorMessage());
		} else {
			closeConfirmChangeStatusPopup(event);
			String errorMsg = getMessage("componentversion.consecutif.samestatus.exist", toStatusDesignation);
			log.error(errorMsg);
			navigationTree.openErrorPopup(errorMsg);
		}
	}

	/**
	 * Return the actual status of the cost element
	 * 
	 * @return status
	 */
	public String getStatus() {
		String status = dto.getComponentVersion().getEnumStatus().getDesignation();
		if (status != null && !"".equals(status)) {
			return getMessage("status.designation." + status);
		}
		return null;
	}

	/**
	 * Change available cost element page
	 * 
	 * @param event
	 */
	public void changeAvailableCostElementPage(ActionEvent event) {
		String pageIndex = (String) getRequestParameterMap().get(AVAILABLE_ELEMENTS_PAGAE_PARAM);
		dto.setCostElementsPage(Integer.valueOf(pageIndex));
	}

	/**
	 * assign one element to the component via context menu
	 * 
	 * @param event
	 */
	public void assignElement(ActionEvent event) {
		String elementModelIdAsString = (String) getRequestParameterMap().get("elem_id");
		Long selectedElementModelId = new Long(elementModelIdAsString);
		if (navigationElementBean.getElementsMap().containsKey(selectedElementModelId)) {
			ElementModel element = navigationElementBean.getElementsMap().get(selectedElementModelId).getElementModel();
			dto.getSelectedElements().add(element);
			log.info("Assign Element: " + element);
		}
		navigationElementBean.getElementsMap().clear();
		if (dto.getSelectedElements() != null && !dto.getSelectedElements().isEmpty()) {
			dto.getPageChanges().add("add_element_component");
		}
		service.loadElementVersionListToAssign(dto);
		setElementVersionsToAssignItems(fillElementVersionsItems());

	}

	public void assignElementVersion(ActionEvent event) {

		dto.getPageChanges().add("add_element_component");
		service.assignElementToComponent(dto);
		dto.setShowElementVersionChoicePopup(false);
		ElementModel elementModel = null;
		for (Iterator<ElementModel> iterator = dto.getSelectedElements().iterator(); iterator.hasNext();) {
			elementModel = iterator.next();
			if (elementModel.equals(dto.getSelectedElementModel())) {
				iterator.remove();
				break;
			}
		}
		service.loadElementVersionListToAssign(dto);
		setElementVersionsToAssignItems(fillElementVersionsItems());
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 * 
	 * @param event
	 */
	public void moveSelectedToRight(ActionEvent event) {
		for (Entry<Long, CostElementAttachNode> entry : navigationElementBean.getElementsMap().entrySet()) {
			dto.getSelectedElements().add(entry.getValue().getElementModel());
			entry.getValue().setSelected(Boolean.FALSE);
		}
		navigationElementBean.getElementsMap().clear();
		if (dto.getSelectedElements() != null && !dto.getSelectedElements().isEmpty()) {
			dto.getPageChanges().add("add_element_component");
		}
		service.loadElementVersionListToAssign(dto);
		setElementVersionsToAssignItems(fillElementVersionsItems());

	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 * 
	 * @param event
	 */
	@SuppressWarnings("unchecked")
	public void moveSelectedToLeft(ActionEvent event) {
		if (rowStateMapRight.getSelected() != null && !rowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_element_component");
		}
		dto.getSelectedComponentElementList().addAll(rowStateMapRight.getSelected());
		service.unassignElementComponent(dto);
		rowStateMapRight.clear();
	}

	/**
	 * Change the provision flag value and performs calculation
	 * 
	 * @param event
	 */
	public void doChangeProvision(ValueChangeEvent event) {
		log.info("Provision changed to " + event.getNewValue());
		dto.getComponentVersion().setWithProvision((Boolean) event.getNewValue());
		service.calculate(dto);
		String activProvision = getMessage("action.costComponent.changeProvision.active");
		if (!(Boolean) event.getNewValue()) {
			activProvision = getMessage("action.costComponent.changeProvision.inactive");
		}
		navigationTree.setDownActionDefintion(getMessage("action.costComponent.changeProvision", activProvision));
		checkChanged(event);
	}

	/**
	 * Used to switch from different status of a cost element
	 * 
	 * @param event
	 */
	public void navigateToOtherVersion(ActionEvent event) {
		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		String componentVersionId = (String) getRequestParameterMap().get(COMPONENT_VERSION_ID_PARAM);
		log.info("Navigate to component version with id  " + componentVersionId);
		dto.setComponentVersionId(Long.valueOf(componentVersionId));
		dto = service.loadDTOForDetail(dto, dto.getComponentVersionId(), dto.getFolderId(), "VERSION", Boolean.FALSE);
		navigationTree.setDownActionDefintion(
				getMessage("action.costComponent.navigateToOtherVersion", dto.getComponentVersion().getCreationDate()));
	}

	/**
	 * Inspect the component element number value changes from Tab1
	 * 
	 * @param event
	 */

	public void changedComponentElementNumberValueFromTab1(ValueChangeEvent event) {
		/*
		 * PTS Problem 26686Check if the value of Element Number in component is changed from the principal Tab
		 */
		if (fromTab == 1) {
			int rowIndex = getComponentElementTable1().getRowIndex();
			inputComponentElementNumberValueChanged(event, rowIndex);
		}
	}

	/**
	 * Inspect the component element number value changes from Tab2
	 * 
	 * @param event
	 */
	public void changedComponentElementNumberValueFromTab2(ValueChangeEvent event) {
		/*
		 * PTS Problem 26686Set from which Tab the Element Number in component was changed
		 */
		fromTab = 2;
		int rowIndex = getComponentElementTable2().getRowIndex();
		inputComponentElementNumberValueChanged(event, rowIndex);

	}

	/**
	 * perform a recalculation
	 * 
	 * @param event
	 * @param rowIndex
	 */
	private void inputComponentElementNumberValueChanged(ValueChangeEvent event, int rowIndex) {
		Long newNumber = (Long) event.getNewValue();
		log.info("Row to modify " + rowIndex + " element number changed from " + event.getOldValue() + " to "
				+ newNumber);
		service.loadChangedComponentElementNumberValue(dto, rowIndex, newNumber);
		checkChanged(event);
	}

	/**
	 * Open the pop-up model box used to confirm the save of changes
	 * 
	 * @param event
	 */
	public void showElement(ActionEvent event) {
		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		String elementId = (String) getRequestParameterMap().get("element_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		String elementVersionId = (String) getRequestParameterMap().get("element_version_id");
		log.info("Show Element,folder = " + elementId + "," + folderId);
		if (elementId != null && !"".equals(elementId) && !"null".equals(elementId)) {
			CostElementNode n = (CostElementNode) navigationTree.goToNode(Long.parseLong(elementId),
					Long.parseLong(folderId), EnumObjectTypeManager.ELEMENT_ID, event);
			if (n != null) {
				// n.nodeClicked(event);
				n.setExpanded(true);
				n.expandClicked(event);
				Node versionNode = n.getNodeById(Long.parseLong(elementVersionId), EnumObjectTypeManager.ELEMENT_ID);
				if (versionNode != null) {
					versionNode.nodeClicked(event);
				}
			}
		}
	}

	/*************************** Excel Export ************************************/

	/**
	 * The excel export is based on a template file. Steps: - find cost component by version date - load the template in
	 * a HSSFWorkbook object - Fill the data from cost component founded - Generate an Excel file
	 * 
	 * @param event
	 */
	public void excelExport(FacesEvent event) {
		setShowExcelViewFile(Boolean.FALSE);
		BaseDateTime date = null;
		if (dto.getExportDate() != null) {
			date = new BaseDateTime(new BaseDateTime(dto.getExportDate()).getDateSeparatedPt());
		}
		if (date != null) {
			date = date.addSeconds(59 * 60 + 23 * 60 * 60 + 59);
			log.info("Excel Export date: " + date);
			try {
				Object[] fileNameAndContent = service.loadExcelExportFile(date, dto);
				if (fileNameAndContent != null) {
					excelResource = new FileResource((byte[]) fileNameAndContent[1], (String) fileNameAndContent[0]);
					setShowExcelViewFile(Boolean.TRUE);
				} else {
					dto.setShowExcelExportPopup(false);
					dto.setExportDate(null);
					navigationTree.openErrorPopup(getMessage("costcomponent.exelexport.noresult.message"));
				}
			} catch (Throwable e) {
				log.error("InvalidFormatException", e);
				dto.setShowExcelExportPopup(false);
				dto.setExportDate(null);
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	public void xmlExport(ActionEvent event) {
		log.info("Try to export xml...");
		setShowXmlComponentViewFile(Boolean.FALSE);
		setShowXmlViewFile(Boolean.FALSE);
		BaseDateTime date = null;
		if (dto.getExportDate() != null) {
			date = new BaseDateTime(new BaseDateTime(dto.getExportDate()).getDateSeparatedPt());
		}
		if (date != null) {
			date = date.addSeconds(59 * 60 + 23 * 60 * 60 + 59);
			log.info("Xml Export date: " + date);
			Object[] fileNameAndContent = null;
			try {
				if ((dto.getSelectedLibraryIds() != null && dto.getSelectedLibraryIds().size() > 1)
						&& dto.getSelectedPrimaryLibrary() == null) {
					dto.setShowXmlExportPopup(false);
					navigationTree.openErrorPopup(getMessage("costcomponent.export.xml.error"));
					return;
				}

				List<String> userIds = new ArrayList<String>();
				userIds.addAll(dto.getSelectedUsersIds());
				String exportTarget = (String) getRequestParameterMap().get("export_target");
				dto.setExportTarget(exportTarget);

				if ("B".equals(dto.getObjectType())) {
					fileNameAndContent = service.loadXmlExpotedFile(date, dto);
				} else {
					Long folderId = 7L;
					if ("C".equals(dto.getObjectType())) {
						folderId = dto.getComponentVersion().getComponent().getFolder().getFolderId();
					}

					fileNameAndContent = service.loadXmlExpotedFile(date, dto, userIds, folderId);
				}
				if (fileNameAndContent == null) {
					dto.setShowXmlExportPopup(false);
					navigationTree.openErrorPopup(getMessage("costcomponent.export.xml.noresult.error"));
					return;
				}
				xmlResource = new FileResource((byte[]) fileNameAndContent[1], (String) fileNameAndContent[0]);

				if ("B".equals(dto.getObjectType())) {

					setShowXmlComponentViewFile(Boolean.TRUE);
				} else {
					setShowXmlViewFile(Boolean.TRUE);

				}
			} catch (Throwable ex) {
				log.error("Error : ", ex);
			}
		}
	}

	/**
	 * Open the pop-up model box used to export Excel file
	 * 
	 * @param event
	 */
	public void showExcelExportModal(ActionEvent event) {
		// reset the export input
		FacesContext facesContext = FacesContext.getCurrentInstance();
		UIViewRoot uiViewRoot = facesContext.getViewRoot();
		UIComponent inputContainer = uiViewRoot.findComponent("exExcForm:date_export1");
		clearSubmittedValues(inputContainer);
		dto.setShowExcelExportPopup(true);
		dto.setExportDate(getExportDate());
		setShowExcelViewFile(Boolean.FALSE);
		excelExport(event);
	}

	/**
	 * Show the popup model box used to the XML export
	 */
	public void showXmlExportModal(ActionEvent event) {
		String objectType = event.getComponent().getId().replace("xml_export_", "");
		dto.setObjectType(objectType);
		if ("B".equals(objectType)) {
			dto.setShowXmlComponentExportPopup(true);
		} else {
			dto.setShowXmlExportPopup(true);
			loadAvailableUsersAndLibraries();
		}
		setShowXmlViewFile(Boolean.FALSE);
		setShowXmlComponentViewFile(Boolean.FALSE);
	}

	/**
	 * calculate the excel export date
	 * 
	 * @return the excel export date
	 */
	private Date getExportDate() {
		Date exportDate = new BaseDateTime(BaseDateTime.getCurrentDateTime().getDateSeparatedPt())
				.addSeconds(59 * 60 + 23 * 60 * 60 + 59).getDate();
		ComponentVersionModel nextVersion = service.getNextComponentVersion(dto.getComponentVersionId(),
				dto.getComponentId());
		// if there is no version, the export date is the current date
		if (nextVersion == null) {
			return exportDate;
		} else {
			// for an old version, export date of export is the valid_to date
			if (dto.getComponentVersion().getValidTo() != null
					&& dto.getComponentVersion().getValidTo().getTime() <= exportDate.getTime()) {
				exportDate = dto.getComponentVersion().getValidTo().getDate();
			}
			// if the next version is in_edit status the export date will be the
			// previous day of
			// next version valid_from date
			else {
				exportDate = BaseDateTime
						.getPreviousDay(nextVersion.getValidFrom().addSeconds(59 * 60 + 23 * 60 * 60 + 59).getDate());
			}
		}
		return exportDate;
	}

	/**
	 * prepare excel file when date is changed
	 * 
	 * @param event
	 */
	public void prepareExcelExport(ValueChangeEvent event) {
		dto.setExportDate((java.util.Date) event.getNewValue());
		excelExport(event);
	}

	/**
	 * close excel export pop up when resource link is clicked
	 * 
	 * @param vce
	 */
	public void excelResourceClicked(ValueChangeEvent vce) {
		log.info("Excel was download by " + dto.getUserLogged().getFullName());
		cancelExport();
		log.info("ExportDate " + dto.getExportDate());
	}

	/**
	 * Cancel Excel export
	 */
	public void cancelExport() {
		dto.setShowExcelExportPopup(false);
		dto.setExportDate(null);
	}

	/**
	 * Cancel the XML export
	 */
	public void cancelXmlExport() {
		dto.setShowXmlExportPopup(false);
		dto.setShowXmlComponentExportPopup(false);
		dto.setExportDate(null);
		dto.setExportWithoutAttributes(Boolean.FALSE);
		dto.setExportApprovedComponent(Boolean.FALSE);
	}

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
	 * Return the list of uploaded files of the cost component
	 * 
	 * @return list of UploadedFile objects
	 */
	public List<UploadedFile> getUploadedFiles() {
		List<UploadedFile> uploadedFiles = new ArrayList<UploadedFile>();
		if (dto != null) {
			List<DocumentPoolModel> documents = dto.getDocumentList();
			if (documents != null && !documents.isEmpty()) {
				for (DocumentPoolModel document : documents) {
					FileResource myResource = new FileResource(document.getContent(),
							document.getDescription() + "." + document.getFileType());
					uploadedFiles.add(new UploadedFile(myResource, document));
				}
			}
		}
		return uploadedFiles;
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

	public boolean isShowXmlComponentViewFile() {
		return showXmlComponentViewFile;
	}

	public void setShowXmlComponentViewFile(boolean showXmlComponentViewFile) {
		this.showXmlComponentViewFile = showXmlComponentViewFile;
	}

	public boolean isShowXmlViewFile() {
		return showXmlViewFile;
	}

	public void setShowXmlViewFile(boolean showXmlViewFile) {
		this.showXmlViewFile = showXmlViewFile;
	}

	/**
	 * Return the download resource of the Excel export file
	 * 
	 * @return
	 */
	public Resource getExcelResource() {
		return excelResource;
	}

	public FileResource getXmlResource() {
		return xmlResource;
	}

	public void setXmlResource(FileResource xmlResource) {
		this.xmlResource = xmlResource;
	}

	/**
	 * Set an Element version as used for a brand
	 * 
	 * @param event
	 */
	public void useComponentStand(ActionEvent event) {
		dto.setRelatedComponentsByElement(null);
		service.saveComponentStandAsUsed(dto);
		if (dto.getComponentWithSameExternalId() != null) {
			navigationTree.openErrorPopup(
					getMessage("costcomponent.withsameexternalid.exist", dto.getComponentWithSameExternalId()));
		}
		if (dto.getOldVersionUsed()
				|| (dto.getRelatedComponentsByElement() != null && !dto.getRelatedComponentsByElement().isEmpty())) {
			dto.setShowConfirmChangeUsePopup(Boolean.TRUE);
		} else {
			// refresh the tree
			Node selectedNode = navigationTree.getSelectedNodeObject();
			selectedNode.setUsers(dto.getComponentStand().getUsersImg());
			selectedNode.getParent().setUsers(dto.getComponentVersion().getUsersImg());
			if (dto.getComponentVersion().getNumber() != null) {
				ComponentModel component = dto.getComponentVersion().getComponent();
				component.setUsers(service.loadComponentVersionUsers(component));
				selectedNode.getParent().getParent().setUsers(component.getUsersImg());
			}
		}
	}

	public void changeUseComponentStand(ActionEvent event) {
		service.saveChangeUseComponentStand(dto);
		closeConfirmChangeUsePopup(event);
		// refresh the tree
		Node selectedNode = navigationTree.getSelectedNodeObject();
		selectedNode.setUsers(dto.getComponentStand().getUsersImg());
		selectedNode.getParent().setUsers(dto.getComponentVersion().getUsersImg());
		if (dto.getComponentVersion().getNumber() != null) {
			ComponentModel component = dto.getComponentVersion().getComponent();
			component.setUsers(service.loadComponentVersionUsers(component));
			selectedNode.getParent().getParent().setUsers(component.getUsersImg());
		}
		navigationTree.getSelectedNodeObject().getParent().getParent().expandClicked(event);
	}

	/**
	 * Set an Element version as unused for a brand
	 * 
	 * @param event
	 */
	public void unuseComponentStand(ActionEvent event) {
		service.saveComponentStandAsUnUsed(dto);

		// refresh the tree
		Node selectedNode = navigationTree.getSelectedNodeObject();
		selectedNode.setUsers(dto.getComponentStand().getUsersImg());
		selectedNode.getParent().setUsers(dto.getComponentVersion().getUsersImg());
		ComponentModel component = dto.getComponentVersion().getComponent();
		component.setUsers(service.loadComponentVersionUsers(component));
		selectedNode.getParent().getParent().setUsers(component.getUsersImg());

	}

	/**
	 * release component version and release all owned and assigned working element versions
	 * 
	 * @param event
	 */
	public void releaseComponentVersion(ActionEvent event) {
		Long fromStatusId = dto.getComponentVersion().getEnumStatus().getEnumStatusId();
		Long toStatusId = dto.getToStatusId();

		// check if the status of the Component version is not changed while the user
		// try to
		// release it
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			ComponentVersionModel version = service.loadComponentVersion(dto);
			if (!version.getEnumStatus().getEnumStatusId().equals(fromStatusId)) {
				closeConfirmReleasePopup(event);
				navigationTree.openErrorPopup(getMessage("componentversion.released.cantbe.edited"));
				navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
				return;
			}
		}

		service.saveReleaseComponentVersion(dto);
		dto.setShowConfirmReleasePopup(Boolean.FALSE);
	}

	/**
	 * Close pop-up model box used to confirm use version change
	 * 
	 * @param event
	 */
	public void closeConfirmChangeUsePopup(ActionEvent event) {
		dto.setShowConfirmChangeUsePopup(Boolean.FALSE);
	}

	public String getChangeUseConfirmMessage() {
		if (dto.getRelatedComponentsByElement() == null || dto.getRelatedComponentsByElement().isEmpty()) {
			return getMessage("costcomponent.changeUse.confirm", dto.getUsedVersion());
		} else {
			StringBuilder message = new StringBuilder();
			message.append("<div style=\"text-align: left;\">");
			if (dto.getOldVersionUsed()) {
				message.append("Sie nutzten aktuell Bausteinstand " + dto.getUsedVersion() + ".");
				message.append("</br>");
			}
			if (!CollectionUtils.isEmpty(dto.getRelatedComponentsByElement().keySet())) {
				message.append("Die Nutzerkennung von folgenden Elementen ");
			}
			if (!CollectionUtils.isEmpty(dto.getRelatedComponentsByElement().entrySet())) {
				message.append("und die Bausteinstände von folgenden Bausteinen ");
			}
			message.append("werden ");
			if (!dto.getOldVersionUsed()) {
				message.append("beim setzen einer neuen Nutzerkennung an diesem Baustein ");
			} else {
				message.append("beim wechsel ");
			}
			message.append("geändert:</br>");
			message.append("<dl style='margin: 10px 0px 0px 0px'>");
			for (Map.Entry<String, String> entry : dto.getRelatedComponentsByElement().entrySet()) {
				message.append("<dt>- " + entry.getKey() + "</dt>");
				if (entry.getValue() != null && !"".equals(entry.getValue().trim())) {
					String[] componets = entry.getValue().split(";");
					for (String component : componets) {
						message.append("<dd>- " + component + "</dd>");
					}
				}
			}
			message.append("</dl>");
			message.append("</div>");
			message.append("<div style=\"text-align: center;padding:10px 0px 10px 0px\">");
			message.append("Wollen Sie wirklich wechseln?");
			message.append("</div>");
			return message.toString();
		}

	}

	public void closeConfirmReleasePopup(ActionEvent event) {
		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the
		// release process
		// unlock the version
		service.saveComponentVersionLockStatus(dto, false);

		dto.setShowConfirmReleasePopup(Boolean.FALSE);
	}

	public String getReleaseConfirmMessage() {
		if (dto.getComponentsTobeAffected() == null || "".equals(dto.getComponentsTobeAffected())) {
			return getMessage("componentversion.element.willbe.released", dto.getInEditElements());
		} else {
			return getMessage("componentversion.element.willbe.released.2", dto.getInEditElements(),
					dto.getComponentsTobeAffected());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#checkChanged(javax.faces.event .ValueChangeEvent)
	 */
	@Override
	public void checkChanged(ValueChangeEvent event) {
		if (dto.getUserIsOwner()) {
			super.checkChanged(event);
		}
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

	public SelectItem[] getLibraryItems() {
		return libraryItems;
	}

	public void setLibraryItems(SelectItem[] libraryItems) {
		this.libraryItems = libraryItems;
	}

	public void setElementVersionsToAssignItems(SelectItem[] elementVersionsToAssignItems) {
		this.elementVersionsToAssignItems = elementVersionsToAssignItems;
	}

	public SelectItem[] getElementVersionsToAssignItems() {
		return elementVersionsToAssignItems;
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

	/**
	 * Fill the class items from the DTO
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

	private SelectItem[] fillElementVersionsItems() {
		Map<ElementVersionModel, String> elementVersions = dto.getElementVersionsToAssign();
		SelectItem[] items = null;
		if (elementVersions != null) {
			items = new SelectItem[elementVersions.size()];
		} else {
			items = new SelectItem[0];
		}

		if (elementVersions != null) {
			int i = 0;
			for (Map.Entry<ElementVersionModel, String> entry : elementVersions.entrySet()) {
				ElementVersionModel elementVersion = entry.getKey();
				String label = entry.getValue();
				if (label.contains("costcomponent.assignelement.stand")) {
					items[i] = new SelectItem(String.valueOf(elementVersion.getElementVersionId()),
							getMessage("costcomponent.assignelement.stand", elementVersion.getNumber()));
				} else {
					items[i] = new SelectItem(String.valueOf(elementVersion.getElementVersionId()), getMessage(label));
				}
				i++;
			}
		}
		return items;
	}

	public void closeElementVersionChoicePopup(ActionEvent event) {
		dto.setShowElementVersionChoicePopup(Boolean.FALSE);
		ElementModel elementModel = null;
		for (Iterator<ElementModel> iterator = dto.getSelectedElements().iterator(); iterator.hasNext();) {
			elementModel = iterator.next();
			if (elementModel.equals(dto.getSelectedElementModel())) {
				iterator.remove();
				break;
			}
		}
		service.loadElementVersionListToAssign(dto);
		setElementVersionsToAssignItems(fillElementVersionsItems());
	}

	/**
	 * Change the use of Element version for a brand
	 * 
	 * @param event
	 */
	public void changeUseElementVersion(ActionEvent event) {
		log.info("change use element version from element assign");
		service.saveChangeUseElementVersion(dto);
		closeConfirmChangeElUsePopup(event);

	}

	/**
	 * Close pop-up model box used to confirm element use version change
	 * 
	 * @param event
	 */
	public void closeConfirmChangeElUsePopup(ActionEvent event) {
		dto.setShowConfirmChangeElUsePopup(Boolean.FALSE);
		ElementModel elementModel = null;
		for (Iterator<ElementModel> iterator = dto.getSelectedElements().iterator(); iterator.hasNext();) {
			elementModel = iterator.next();
			if (elementModel.equals(dto.getSelectedElementModel())) {
				iterator.remove();
				break;
			}
		}
		service.loadElementVersionListToAssign(dto);
		setElementVersionsToAssignItems(fillElementVersionsItems());
	}

	public String getChangeElUseConfirmMessage() {
		if (dto.getRelatedComponents() == null || "".equals(dto.getRelatedComponents().trim())) {
			return getMessage("costelement.changeUse.confirm", dto.getElementUsedVersion());
		} else {
			return getMessage("costelement.changeUseLinked.confirm", dto.getElementUsedVersion(),
					dto.getRelatedComponents());
		}
	}

	public void assignInEditElementVersion(ActionEvent event) {
		String elementModelIdAsString = (String) getRequestParameterMap().get("elem_id");
		Long selectedElementModelId = new Long(elementModelIdAsString);
		dto.setSelectedElementModelId(selectedElementModelId);
		service.assignInEditElementVersion(dto);
		if (dto.getElementVersionToAssign() != null) {
			dto.getPageChanges().add("add_element_component");
		}
	}

	// 04.09.2013: ZS: PTS_Requirement-22201: Redesign of the elements assign mask

	/**
	 * Show available cost element window
	 * 
	 * @param event
	 */
	public void showAvailableCostElementPopUp(ActionEvent event) {
		dto.setShowElementComponentpopup(Boolean.TRUE);
	}

	/**
	 * Close available cost element window
	 * 
	 * @param event
	 */
	public void closeAvailableCostElementPopUp(ActionEvent event) {
		String pageIndex = (String) getRequestParameterMap().get(AVAILABLE_ELEMENTS_PAGAE_PARAM);
		dto.setCostElementsPage(Integer.valueOf(pageIndex));
		dto.setShowElementComponentpopup(Boolean.FALSE);
		/*
		 * PTS Problem 26686Reset fromTab to first Tab to indicate that the pop up is closed
		 */
		fromTab = 1;
	}

	/**
	 * Method to change order of component element in baustein page
	 * 
	 * @param event
	 */
	public void sortAvailableComponentElementFromTab1(ActionEvent event) {
		String actionToDo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("sort_parameter");
		if (fromTab == 1) {
			int rowIndex = getComponentElementTable1().getRowIndex();
			log.info(actionToDo + " clicked from row " + rowIndex);
			service.loadChangedComponentElementOrder(dto, rowIndex, actionToDo);
			dto.getPageChanges().add("reorder_element_component");
		}
	}

	public void sortAvailableComponentElementFromTab2(ActionEvent event) {
		fromTab = 2;
		String actionToDo = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
				.get("sort_parameter");
		int rowIndex = getComponentElementTable2().getRowIndex();
		log.info(actionToDo + " clicked from row " + rowIndex);
		service.loadChangedComponentElementOrder(dto, rowIndex, actionToDo);
		dto.getPageChanges().add("reorder_element_component");
	}

	public void showDeleteFileConfirmation(ActionEvent event) {
		String fileToDelete = (String) getRequestParameterMap().get(FILE_NAME_PARAM);
		dto.setFileToDelete(fileToDelete);
		dto.setShowDeleteFileConfirmation(Boolean.TRUE);
	}

	public void closeDeleteFileConfirmation(ActionEvent event) {
		dto.setFileToDelete(null);
		dto.setShowDeleteFileConfirmation(Boolean.FALSE);
	}

	public void showDeleteImagePreviewConfirmation(ActionEvent event) {
		dto.setShowDeleteImagePreviewConfirmation(Boolean.TRUE);
	}

	public void closeDeleteImagePreviewConfirmation(ActionEvent event) {
		dto.setShowDeleteImagePreviewConfirmation(Boolean.FALSE);
	}

	public boolean getFileExist() {
		String imagePath = dto.getImagePath();
		if (imagePath != null) {
			File imageFile = new File(imagesServer + imagePath);
			return (imageFile.exists() && imageFile.isFile());
		}
		return false;
	}

	public FileResource getImagePreview() {
		String imagePath = dto.getImagePath();
		if (imagePath != null) {
			log.info("load image: " + imagesServer + imagePath);
			File imageFile = new File(imagesServer + imagePath);
			if (imageFile.exists() && imageFile.isFile()) {
				FileInputStream img = null;
				try {
					img = new FileInputStream(imageFile);
					byte fileContent[] = new byte[(int) imageFile.length()];
					img.read(fileContent);
					FileResource myResource = new FileResource(fileContent, imageFile.getName());
					return myResource;
				} catch (IOException e) {
					log.error("IO Exception: " + e);
				} finally {
					// close the streams using close method
					try {
						if (img != null) {
							img.close();
						}
					} catch (IOException ioe) {
						log.error("Error while closing stream: " + ioe);
					}
				}
			}
		}
		return imagePreview;
	}

	public String getImageUrl() {
		String imagePath = dto.getImagePath();
		String imageUrl = "";
		if (imagePath != null) {
			imagePath = imagePath.replace("\\", "/");
			imageUrl = imagesServerUrl + imagePath;
		}

		return imageUrl;
	}

	public void setImagesServer(String imagesServer) {
		this.imagesServer = imagesServer;
	}

	public void setImagesServerUrl(String imagesServerUrl) {
		this.imagesServerUrl = imagesServerUrl;
	}

	public void changeComponentActivation(ValueChangeEvent event) {
		log.info("Activation change: " + event.getNewValue());
		// BS has been activated
		if (!(Boolean) event.getNewValue()) {
			service.getIfComponentCanBeActivated(dto);
			if (dto.getComponentWithSameExternalId() != null) {
				setShowActivationError(Boolean.TRUE);
				setActivationErrorMessage(getMessage("costcomponent.activation.withsameexternalid.exist",
						dto.getComponentWithSameExternalId()));

			} else {
				checkChanged(event);
			}
		} else {
			checkChanged(event);
		}

	}

	public void closeActivationErrorPopup(ActionEvent event) {
		setShowActivationError(Boolean.FALSE);
		setActivationErrorMessage(null);
		cancelModifications(event);
	}

	public boolean isShowActivationError() {
		return showActivationError;
	}

	public void setShowActivationError(boolean showActivationError) {
		this.showActivationError = showActivationError;
	}

	public String getActivationErrorMessage() {
		return activationErrorMessage;
	}

	public void setActivationErrorMessage(String activationErrorMessage) {
		this.activationErrorMessage = activationErrorMessage;
	}

	public UIData getVersionHistoryDetailTable() {
		return versionHistoryDetailTable;
	}

	public void setVersionHistoryDetailTable(UIData versionHistoryDetailTable) {
		this.versionHistoryDetailTable = versionHistoryDetailTable;
	}

	public void showHistoryDetailPopup(ActionEvent event) {
		String changeToShow = (String) getRequestParameterMap().get(CHANGE_PARAM);
		dto.setChangeToShow(changeToShow);
		service.getChangeDetailByVersion(dto);
		dto.setShowHistoryDetailpopup(Boolean.TRUE);
	}

	public void closeHistoryDetailPopup(ActionEvent event) {
		dto.setChangeToShow(null);
		dto.setShowHistoryDetailpopup(Boolean.FALSE);
	}
}
