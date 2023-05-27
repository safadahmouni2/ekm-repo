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

package vwg.vw.km.presentation.client.costelement;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.faces.component.UIComponent;
import javax.faces.component.UIData;
import javax.faces.component.html.HtmlDataTable;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
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
import org.springframework.util.StringUtils;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.service.dto.CostElementDTO;
import vwg.vw.km.application.service.logic.CostElementService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.integration.persistence.model.DocumentPoolModel;
import vwg.vw.km.integration.persistence.model.ElementCategoryModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentVersionNode;
import vwg.vw.km.presentation.client.util.FileResource;
import vwg.vw.km.presentation.client.util.UploadedFile;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Backing bean class for kostenelement masks
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: zouhair $
 * @version $Revision: 1.198 $ $Date: 2018/11/09 11:13:31 $
 */
public class CostElementBean extends BaseDetailBean<CostElementDTO, CostElementService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8352214050892908312L;

	private final static Log log = LogManager.get().getLog(CostElementBean.class);

	public static final String ELEMENT_EXIST_MESSAGE_ID = "costElement.exist.sameDesignationOrNumber.message";

	public static final String ELEMENT_DESCRIPTION_TOO_LONG = "costElement.descirption.maxlength";

	// enumeration of different filter options
	private SelectItem[] displayFilterItems = new SelectItem[] {
			new SelectItem("ALL", getMessage(FILTER_ALL_MESSAGE_ID)),
			new SelectItem("ALL_FILLED", getMessage(FILTER_FILLED_MESSAGE_ID)),
			new SelectItem("ALL_EMPTY", getMessage(FILTER_EMPTY_MESSAGE_ID)),
			new SelectItem("MECHANICS", getMessage(FILTER_MECANICS_MESSAGE_ID)),
			new SelectItem("ELECTRICAL", getMessage(FILTER_ELECTRICAL_MESSAGE_ID)),
			new SelectItem("HIDE_PARENT_COST", getMessage(FILTER_HIDE_PARENT_COST_MESSAGE_ID)) };

	private SelectItem[] elementCategoryItems;

	private UIData costElementAttributeTable;

	private UIData componentElementListTable;

	private UIData costElementDocumentsTable;

	private UIData versionHistoryTable;

	private UIData versionHistoryDetailTable;

	/**
	 * Getter of costElement attribute table
	 * 
	 * @return UIData
	 */
	public UIData getCostElementAttributeTable() {
		return costElementAttributeTable;
	}

	/**
	 * Setting costElement attribute table
	 * 
	 * @param costElementAttributeTable
	 */
	public void setCostElementAttributeTable(UIData costElementAttributeTable) {
		this.costElementAttributeTable = costElementAttributeTable;
	}

	/**
	 * Returns the cost element version history table
	 * 
	 * @return
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
	 * Returns the cost element components list table
	 * 
	 * @return
	 */
	public UIData getComponentElementListTable() {
		return componentElementListTable;
	}

	/**
	 * Setting a new or a loaded costElement components list table
	 * 
	 * @param componentElementListTable
	 */
	public void setComponentElementListTable(UIData componentElementListTable) {
		this.componentElementListTable = componentElementListTable;
	}

	/**
	 * Getter of costElement attribute table
	 * 
	 * @return UIData
	 */
	public UIData getCostElementDocumentsTable() {
		return costElementDocumentsTable;
	}

	/**
	 * Setting costElement attribute table
	 * 
	 * @param costElementAttributeTable
	 */
	public void setCostElementDocumentsTable(UIData costElementDocumentsTable) {
		this.costElementDocumentsTable = costElementDocumentsTable;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		if (!dto.isSearchMode()) {
			Node node = navigationTree.getSelectedNodeObject();
			if (node.isLeaf()) {
				dto.setSearchString(navigationTree.getSelectedNodeObject().getParent().getParent().getMenuDisplayText()
						+ " " + getMessage("field.search"));
			} else {
				dto.setSearchString(navigationTree.getSelectedNodeObject().getParent().getMenuDisplayText() + " "
						+ getMessage("field.search"));
			}
		}
		setElementCategoryItems(fillCategoryItemsFromDTO());
	}

	/**
	 * Fill the category items from the DTO
	 * 
	 * @return list of SelectItem objects
	 */
	private SelectItem[] fillCategoryItemsFromDTO() {
		// load elementCategories from dto
		List<ElementCategoryModel> elementCategories = dto.getElementCategories();
		SelectItem[] items = null;
		if (elementCategories != null) {
			items = new SelectItem[1 + elementCategories.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		emptyItem.setValue("");
		items[0] = emptyItem;
		if (elementCategories != null) {
			ElementCategoryModel tmpElementCategoryModel;
			for (int i = 0; i < elementCategories.size(); i++) {
				tmpElementCategoryModel = elementCategories.get(i);
				items[i + 1] = new SelectItem(tmpElementCategoryModel.getElementCategoryId().toString(),
						tmpElementCategoryModel.getDesignation());
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
		log.info("Try to save " + dto.getElementVersion());
		service.getExistElementWithSameNumberOrDesignation(dto);
		ElementVersionModel eModel = dto.getElementVersion();
		if (dto.getExistElementWithSameNumberOrDesignation()) {
			return getMessage(ELEMENT_EXIST_MESSAGE_ID);
		} else if (eModel.getDescription() != null && eModel.getDescription().length() > 1000) {
			return getMessage(ELEMENT_DESCRIPTION_TOO_LONG);
		} else if (eModel.getElement().getElementNumber() == null || eModel.getElement().getDesignation() == null) {
					return getMessage("common.popup.UIInput.REQUIRED");
		}
		Long fromStatus = dto.getElementVersion().getEnumStatus().getEnumStatusId();
		service.save(dto);
		if ((EnumStatusManager.Status.NEW.value().equals(fromStatus) || fromStatus == null)
				&& EnumStatusManager.Status.CREATED.value()
						.equals(dto.getElementVersion().getEnumStatus().getEnumStatusId())) {
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.ELEMENT_ID, fromStatus,
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
			actualNode.setOwnerImg(dto.getElementVersion().getElement().getOwnerImg());

			// ZS: PTS_Problem-35917: When adding a new EL or BS add the new node to his parent child list
			actualNode.getParent().addChild(actualNode);

			actualNode.nodeClicked(event);
		} else {
			actualNode.getParent().setMenuDisplayText(getDisplayText());
			actualNode.getParent().setNodeId(getNodeId());
			actualNode.setNodeId(dto.getElementVersion().getElementVersionId());
		}
		// ZS: PTS_Problem-35155: Make possible to navigate in the tree when the user choose to save a previous change
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
		return getMessage("action.costElement.save");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return dto.getElementVersion().getElement().getNumberAndDesignation();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getElementVersion().getElement().getElementId();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.costElement.reload");
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
			// ZS: PTS_Problem-36803: when the filter is changed set a random ID to the input in the data table
			HtmlDataTable myTable = (HtmlDataTable) getCostElementAttributeTable();
			for (UIComponent column : myTable.getChildren()) {
				for (UIComponent comp : column.getChildren()) {
					if (comp instanceof HtmlInputText) {
						comp.setId(generateRandomId(6));
					}
				}
			}
			service.filterElementValues(dto);

			navigationTree.setDownActionDefintion(getMessage("action.costElement.changeDisplayFilter",
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
	 * Return the list of ElementCategory items
	 * 
	 * @return list of SelectItem objects
	 */
	public SelectItem[] getElementCategoryItems() {
		return elementCategoryItems;
	}

	/**
	 * Set the list of ElementCategory items
	 * 
	 * @param elementCategoryItems
	 */
	public void setElementCategoryItems(SelectItem[] elementCategoryItems) {
		this.elementCategoryItems = elementCategoryItems;
	}

	/**
	 * Upload document to the cost element object. Steps: - Create a new documentPoolModel object - Check if the
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
		log.info("########### Element attachment FILE upload ########");
		FileEntry fileEntry = (FileEntry) event.getSource();
		FileEntryResults results = fileEntry.getResults();
		List<DocumentPoolModel> documentList = dto.getDocumentList();

		for (FileEntryResults.FileInfo fileInfo : results.getFiles()) {
			FileEntryStatuses fileStatus = (FileEntryStatuses) fileInfo.getStatus();
			String contentType = fileInfo.getContentType();
			log.info("Uploading File name :" + fileInfo.getFileName() + " -File ContentType : " + contentType);

			// Check if the uploaded file extention is one of those extension
			// zip,jpeg,jpg,png,gif,pdf,pptx,ppt,xlsx,xls,docx,doc,xlsm,xltx,xltm if not, the file will not be uploaded
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
					log.info(fileInfo.toString());
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
						documentPool.setContent(getArrayByteFromFile(fileInfo.getFile()));
						// Attempt to delete file from hard disk
						boolean success = file.delete();
						if (!success) {
							log.error("Delete: deletion failed");
						}
					} catch (IOException e) {
						log.error("Error uploading file : " + e);
					}
					documentList.add(documentPool);
					navigationTree.setDownActionDefintion(getMessage("action.costElement.uploadFile"));
					dto.getPageChanges().add("document_upload");

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
		navigationTree.setDownActionDefintion(getMessage("action.costElement.deleteFile"));
		// PTS problem 35726: Delete document Popup
		closeDeleteFileConfirmation(event);
	}

	/**
	 * Inspect an input value change in order to perform a recalculation
	 * 
	 * @param event
	 */
	public void inputElementValueChanged(ValueChangeEvent event) {
		int rowIndex = getCostElementAttributeTable().getRowIndex();
		if (log.isDebugEnabled()) {
			log.debug("Row to modify " + rowIndex);
		}
		service.loadChangedElementValue(dto, rowIndex, (BaseBigDecimal) event.getNewValue());
		checkChanged(event);
	}

	/**
	 * Close a pop-up model box
	 * 
	 * @param event
	 */
	public void closeModal(ActionEvent event) {
		dto.setRemark("");
		dto.setModalRendered(Boolean.FALSE);
	}

	/**
	 * Close the pop-up model box used to edit the element description field
	 * 
	 * @param event
	 */
	public void closeElementValueDescriptionModal(ActionEvent event) {
		dto.updateElementValueDescription(-1, false);
		dto.setRemark("");
	}

	/**
	 * Open the pop-up model box used to edit the element description field
	 * 
	 * @param event
	 */
	public void openElementValueDescriptionModal(ActionEvent event) {
		int rowIndex = getCostElementAttributeTable().getRowIndex();
		dto.updateElementValueDescription(rowIndex, true);
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
		Long fromStatusId = dto.getElementVersion().getEnumStatus().getEnumStatusId();
		if (dto.getAllowedTransitions() != null) {
			for (EnumTransitionModel t : dto.getAllowedTransitions()) {
				MenuItem actionItem = new MenuItem();
				actionItem.setId("to_status_" + t.getComp_id().getToStatus().getEnumStatusId().toString());
				actionItem.setValue(getMessage("transition.designation." + t.getDesignation()));
				actionItem.addActionListener(new ActionListener() {
					@Override
					public void processAction(ActionEvent arg0) throws AbortProcessingException {
						goToStatus(arg0);
					}
				});
				// only the administrator can performs a freigeben action
				if (EnumStatusManager.Status.APPROVED.value().equals(t.getComp_id().getToStatus().getEnumStatusId())) {
					actionItem.setRendered(dto.getAdminsitratorOnBrand());
				} else if (EnumStatusManager.Status.TO_BE_APPROVED.value()
						.equals(t.getComp_id().getToStatus().getEnumStatusId())) {
					actionItem.setRendered(
							dto.getUserIsOwner() && !dto.getEditIsLockedByUser() && !navigationTree.isVisitorUser());

				} else if (fromStatusId.equals(EnumStatusManager.Status.APPROVED.value())
						&& EnumStatusManager.Status.IN_PROGRESS.value()
								.equals(t.getComp_id().getToStatus().getEnumStatusId())) {
					actionItem.setRendered(dto.getUserIsOwner() && !dto.getElementIsLockedByComponent()
							&& !navigationTree.isVisitorUser());
				} else {
					actionItem.setRendered(dto.getUserIsOwner() && !navigationTree.isVisitorUser());
				}
				actionSubmenu.getChildren().add(actionItem);
			}
		}
		// new
		MenuItem actionNew = new MenuItem();
		actionNew.setId("new");
		actionNew.setValue(getMessage("costelement.new"));
		actionNew.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costElementBean.createFromObjectClicked}", null, new Class[] { ActionEvent.class })));
		actionNew.setRendered(dto.getElementVersion() != null && dto.getElementVersion().getEnumStatus() != null
				&& dto.getElementVersion().getEnumStatus().getAddAllowed() != null
				&& dto.getElementVersion().getEnumStatus().getAddAllowed() && !dto.isSearchMode() && dto.getUserCanAdd()
				&& navigationTree.getSelectedNodeObject() != null
				&& navigationTree.getSelectedNodeObject().getParent() != null
				&& navigationTree.getSelectedNodeObject().getParent().getParent() != null
				&& navigationTree.getSelectedNodeObject().getParent().getParent().getNodeId() > 0);
		actionSubmenu.getChildren().add(actionNew);
		// delete
		MenuItem actionDelete = new MenuItem();
		actionDelete.setId("delete");
		actionDelete.setValue(getMessage("costelement.delete"));
		actionDelete.setRendered(dto.getElementVersion() != null && dto.getElementVersion().getEnumStatus() != null
				&& dto.getElementVersion().getEnumStatus().getDeleteAllowed() != null
				&& dto.getElementVersion().getEnumStatus().getDeleteAllowed() && dto.getUserCanDelete()
				&& dto.getUserHasWriteAccess());
		actionDelete.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costElementBean.deleteObjectClicked}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionDelete);
		// delete_new
		MenuItem actionDeleteNew = new MenuItem();
		actionDeleteNew.setId("delete_new");
		actionDeleteNew.setImmediate(true);
		actionDeleteNew.setValue(getMessage("costelement.delete"));
		actionDeleteNew.setRendered(dto.getElementVersion() != null && dto.getElementVersion().getEnumStatus() != null
				&& dto.getElementVersion().getEnumStatus().getEnumStatusId() != null
				&& dto.getElementVersion().getEnumStatus().getEnumStatusId() == 0);
		actionDeleteNew.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costElementBean.backWithoutSave}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionDeleteNew);
		// print
		MenuItem actionPrint = new MenuItem();
		actionPrint.setId("print");
		actionPrint.setValue(getMessage("costelement.print"));
		actionPrint.setRendered(dto.getElementVersion() != null && dto.getElementVersion().getEnumStatus() != null
				&& dto.getElementVersion().getEnumStatus().getPrintAllowed() != null
				&& dto.getElementVersion().getEnumStatus().getPrintAllowed());
		actionPrint.setUrl("javascript:friendlyPrint();");
		actionSubmenu.getChildren().add(actionPrint);
		// use
		MenuItem actionUse = new MenuItem();
		actionUse.setId("use");
		actionUse.setValue(getMessage("costelement.use"));
		actionUse.setRendered(dto.isElementVersionCanBeUsed());
		actionUse.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costElementBean.useElementVersion}", null, new Class[] { ActionEvent.class })));
		actionSubmenu.getChildren().add(actionUse);
		// unuse
		MenuItem actionUnUse = new MenuItem();
		actionUnUse.setId("unuse");
		actionUnUse.setValue(getMessage("costelement.unuse"));
		actionUnUse.setRendered(dto.isElementVersionUsed());
		actionUnUse.addActionListener(new MethodExpressionActionListener(expressionFactory.createMethodExpression(
				elContext, "#{costElementBean.unuseElementVersion}", null, new Class[] { ActionEvent.class })));
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
		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
		// unlock the version
		service.saveElementVersionLockStatus(dto, false);

		dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
	}

	/**
	 * Used to modify the status of a cost element ZS: PTS_Requirement-22200: Remove the confirmation model box for the
	 * change status: - in Bearbeitung - zur Entscheidung
	 * 
	 * @param event
	 */
	public void goToStatus(ActionEvent event) {

		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}

		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
		// check if the element version is locked
		ElementVersionModel version = service.loadElementVersion(dto);
		if (version.getIsLocked() != null && version.getIsLocked()) {
			dto.setChangeStatusComment(null);
			dto.setToStatusId(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
			navigationTree.openErrorPopup(
					getMessage("costelement.writeAccessNotAllowed", version.getProcessor().getFullName()));
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
			return;
		}
		// PTS_problem 26491 : 31.10.2013 :Bitte diesen Testfall beachten
		Long fromStatusId = dto.getElementVersion().getEnumStatus().getEnumStatusId();
		if (!fromStatusId.equals(version.getEnumStatus().getEnumStatusId())) {
			dto.setChangeStatusComment(null);
			dto.setToStatusId(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.FALSE);
			navigationTree.openErrorPopup(
					getMessage("costelement.changeStatus.alreadyChanged", version.getProcessor().getFullName()));
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
			return;
		}
		log.info("Confirm change status ");
		Long toStatusId = new Long(event.getComponent().getId().replace("to_status_", ""));

		dto.setToStatusId(toStatusId);

		// 06.09.2013: ZS: PTS_Requirement-22183: Lock the EL/BS while it is in the release process
		// lock the version if it will be released
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			service.saveElementVersionLockStatus(dto, true);
			dto.setChangeStatusComment(null);
			dto.setShowConfirmChangeStatusPopup(Boolean.TRUE);
			return;
		}

		String fromStatusDesignation = getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = getMessage("status.designationbyId." + toStatusId);
		log.info("go From Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);

		// check if the status of the Component version is not changed while the user try to
		// release it
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			if (!version.getEnumStatus().getEnumStatusId().equals(fromStatusId)) {
				closeConfirmChangeStatusPopup(event);
				navigationTree.openErrorPopup(getMessage("elementversion.released.cantbe.edited"));
				navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
				return;
			}
		}

		String goToStatus = service.saveNewStatus(dto);
		if (goToStatus.equals("")) {
			dto = service.loadDTOForDetail(dto, dto.getElementVersionId(), dto.getFolderId(), Boolean.FALSE,
					Boolean.FALSE);
			dto.getPageChanges().clear();
			dto.setShowSaveChangesPopup(Boolean.FALSE);
			closeConfirmChangeStatusPopup(event);
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.ELEMENT_ID, fromStatusId, toStatusId);
			navigationTree.setDownActionDefintion(
					getMessage("action.costElement.changeStatus", fromStatusDesignation, toStatusDesignation));
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
		} else if (goToStatus.equals("VERSION_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			navigationTree.openErrorPopup(getMessage("elementversion.cantbe.released"));
		} else {
			closeConfirmChangeStatusPopup(event);
			navigationTree
					.openErrorPopup(getMessage("elementversion.consecutif.samestatus.exist", toStatusDesignation));
		}
	}

	/**
	 * save Release Status ZS: PTS_Requirement-22200: Remove the confirmation model box for the change status: - in
	 * Bearbeitung - zur Entscheidung
	 * 
	 * @param event
	 */
	public void saveReleaseStatus(ActionEvent event) {
		log.info("Try to release version  ...");
		Long fromStatusId = dto.getElementVersion().getEnumStatus().getEnumStatusId();
		Long toStatusId = dto.getToStatusId();
		String fromStatusDesignation = getMessage("status.designationbyId." + fromStatusId);
		String toStatusDesignation = getMessage("status.designationbyId." + toStatusId);
		log.info("go From Status  :  " + fromStatusDesignation + " To " + toStatusDesignation);

		// check if the status of the Component version is not changed while the user try to
		// release it
		if (fromStatusId.equals(EnumStatusManager.Status.TO_BE_APPROVED.value())
				&& toStatusId.equals(EnumStatusManager.Status.APPROVED.value())) {
			ElementVersionModel version = service.loadElementVersion(dto);
			if (!version.getEnumStatus().getEnumStatusId().equals(fromStatusId)) {
				closeConfirmChangeStatusPopup(event);
				navigationTree.openErrorPopup(getMessage("elementversion.released.cantbe.edited"));
				navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
				return;
			}
		}

		String goToStatus = service.saveNewStatus(dto);
		if (goToStatus.equals("")) {
			dto = service.loadDTOForDetail(dto, dto.getElementVersionId(), dto.getFolderId(), Boolean.FALSE,
					Boolean.FALSE);
			dto.getPageChanges().clear();
			dto.setShowSaveChangesPopup(Boolean.FALSE);
			closeConfirmChangeStatusPopup(event);
			navigationTree.refreshSearchStatusNode(event, EnumObjectTypeManager.ELEMENT_ID, fromStatusId, toStatusId);
			navigationTree.setDownActionDefintion(
					getMessage("action.costElement.changeStatus", fromStatusDesignation, toStatusDesignation));
			navigationTree.getSelectedNodeObject().getParent().nodeClicked(event);
		} else if (goToStatus.equals("VERSION_NOT_RELEASED")) {
			closeConfirmChangeStatusPopup(event);
			navigationTree.openErrorPopup(getMessage("elementversion.cantbe.released"));
		} else {
			closeConfirmChangeStatusPopup(event);
			navigationTree
					.openErrorPopup(getMessage("elementversion.consecutif.samestatus.exist", toStatusDesignation));
		}
	}

	/**
	 * Return the actual status of the cost element
	 * 
	 * @return status
	 */
	public String getStatus() {
		String status = dto.getElementVersion().getEnumStatus().getDesignation();
		if (status != null && !"".equals(status)) {
			return getMessage("status.designation." + status);
		}
		return null;
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
		String elementVersionId = (String) getRequestParameterMap().get(ELEMENT_VERSION_ID_PARAM);
		log.info("Navigate to element version with id  " + elementVersionId);
		dto.setElementVersionId(Long.valueOf(elementVersionId));
		dto = service.loadDTOForDetail(dto, dto.getElementVersionId(), dto.getFolderId(), Boolean.FALSE, Boolean.FALSE);
		navigationTree.setDownActionDefintion(
				getMessage("action.costElement.navigateToOtherVersion", dto.getElementVersion().getCreationDate()));
	}

	/**
	 * Open the pop-up model box used to confirm the save of changes
	 * 
	 * @param event
	 */
	public void showComponent(ActionEvent event) {
		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		String componentId = (String) getRequestParameterMap().get("component_id");
		String folderId = (String) getRequestParameterMap().get("folder_id");
		String statusId = (String) getRequestParameterMap().get("status_id");
		String creatorId = (String) getRequestParameterMap().get("creator_id");
		String componentVersionId = (String) getRequestParameterMap().get("component_version_id");
		String componentStandId = (String) getRequestParameterMap().get("component_stand_id");
		log.info("Show Component ..." + componentId);
		// check if component to show is in Erstellt status and created by another user
		if (statusId != null && statusId.equals("1") && (creatorId == null
				|| !creatorId.equals(navigationTree.getUserFromSession().getUserId().toString()))) {
			navigationTree.openErrorPopup(getMessage("componetversion.show.checkstatusAnduser.invalid"));
		}
		// end check
		else {
			if (componentId != null && !"".equals(componentId)) {
				CostComponentNode n = (CostComponentNode) navigationTree.goToNode(Long.parseLong(componentId),
						Long.parseLong(folderId), EnumObjectTypeManager.COMPONENT_ID, event);
				if (n != null) {
					// n.nodeClicked(event);
					n.setExpanded(true);
					n.expandClicked(event);
					Node versionNode = n.getNodeById(Long.parseLong(componentVersionId),
							EnumObjectTypeManager.COMPONENT_ID);
					if (versionNode != null) {
						if (versionNode instanceof CostComponentVersionNode) {
							((CostComponentVersionNode) versionNode).setExpanded(true);
							((CostComponentVersionNode) versionNode).expandClicked(event);
							Node standNode = ((CostComponentVersionNode) versionNode)
									.getNodeById(Long.parseLong(componentStandId), EnumObjectTypeManager.COMPONENT_ID);
							if (standNode != null) {
								standNode.nodeClicked(event);
							}
						} else {
							versionNode.nodeClicked(event);
						}
					}
				}
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.costElement.search");
	}

	/**
	 * Return the list of uploaded files of the costelement
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
	 * Set an Element version as used for a brand
	 * 
	 * @param event
	 */
	public void useElementVersion(ActionEvent event) {
		service.saveElementVersionAsUsed(dto);
		if (dto.getOldVersionUsed()) {
			dto.setShowConfirmChangeUsePopup(Boolean.TRUE);
		} else {
			navigationTree.getSelectedNodeObject().setUsers(dto.getElementVersion().getUsersImg());
			navigationTree.getSelectedNodeObject().getParent()
					.setUsers(dto.getElementVersion().getElement().getUsersImg());
		}
	}

	/**
	 * Change the use of Element version for a brand
	 * 
	 * @param event
	 */
	public void changeUseElementVersion(ActionEvent event) {
		service.saveChangeUseElementVersion(dto);
		closeConfirmChangeUsePopup(event);
		navigationTree.getSelectedNodeObject().setUsers(dto.getElementVersion().getUsersImg());
		navigationTree.getSelectedNodeObject().getParent().setUsers(dto.getElementVersion().getElement().getUsersImg());
		navigationTree.getSelectedNodeObject().getParent().expandClicked(event);
	}

	/**
	 * Set an Element version as unused for a brand
	 * 
	 * @param event
	 */
	public void unuseElementVersion(ActionEvent event) {
		service.saveElementVersionAsUnUsed(dto);
		if (!dto.getVersionUnused()) {
			navigationTree.openErrorPopup(getMessage("costelement.version.cantbe.unuse", dto.getRelatedComponents()));
		} else {
			navigationTree.getSelectedNodeObject().setUsers(dto.getElementVersion().getUsersImg());
			navigationTree.getSelectedNodeObject().getParent()
					.setUsers(dto.getElementVersion().getElement().getUsersImg());
		}
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
		if (dto.getRelatedComponents() == null || "".equals(dto.getRelatedComponents().trim())) {
			return getMessage("costelement.changeUse.confirm", dto.getUsedVersion());
		} else {
			return getMessage("costelement.changeUseLinked.confirm", dto.getUsedVersion(), dto.getRelatedComponents());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#checkChanged(javax.faces.event.ValueChangeEvent)
	 */
	@Override
	public void checkChanged(ValueChangeEvent event) {
		if (dto.getUserIsOwner()) {
			super.checkChanged(event);
		}
	}

	/**
	 * ABA: 21-01-204 :PTS problem 35726: Delete document Popup
	 * 
	 * @param event
	 */
	public void showDeleteFileConfirmation(ActionEvent event) {
		String fileToDelete = (String) getRequestParameterMap().get(FILE_NAME_PARAM);
		dto.setFileToDelete(fileToDelete);
		dto.setShowDeleteFileConfirmation(Boolean.TRUE);
	}

	public void closeDeleteFileConfirmation(ActionEvent event) {
		dto.setFileToDelete(null);
		dto.setShowDeleteFileConfirmation(Boolean.FALSE);
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

	public UIData getVersionHistoryDetailTable() {
		return versionHistoryDetailTable;
	}

	public void setVersionHistoryDetailTable(UIData versionHistoryDetailTable) {
		this.versionHistoryDetailTable = versionHistoryDetailTable;
	}

}
