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
package vwg.vw.km.presentation.client.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;

import org.apache.poi.util.IOUtils;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.implementation.impl.UserManagerImpl;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.BranchNode;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentInProcessVersionNode;
import vwg.vw.km.presentation.client.navigation.node.costcomponent.CostComponentVersionStandNode;
import vwg.vw.km.presentation.client.navigation.node.costelement.CostElementVersionNode;

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
 * @version $Revision: 1.73 $ $Date: 2020/08/12 08:00:29 $
 */
public class BaseBean<T extends BaseDTO, X extends BaseService<T>> implements BaseConstants, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final Log log = LogManager.get().getLog(BaseBean.class);

	protected T dto;

	protected X service;

	protected NavigationBean navigationTree;

	public T getDto() {
		return dto;
	}

	public void setDto(T dto) {
		this.dto = dto;
	}

	public void setService(X service) {
		this.service = service;
	}

	public void setNavigationTree(NavigationBean navigationTree) {
		this.navigationTree = navigationTree;
	}

	private static ResourceBundle resourceBundle = null;

	/**
	 * Gets the message bundle used by the application used for internationalization.
	 * 
	 * @return message bundle for internationalization.
	 */
	public static ResourceBundle getResourceBundle() {
		if (resourceBundle == null)
			initResourceBundleBundle();
		return resourceBundle;
	}

	private TimeZone timeZone;

	public TimeZone getTimeZome() {
		timeZone = TimeZone.getDefault();
		return timeZone;
	}

	/**
	 * init the resource Bundle
	 */
	private static void initResourceBundleBundle() {
		if (resourceBundle == null) {
			Locale locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
			// assign a default local if the faces context has none, shouldn't happen
			if (locale == null) {
				locale = Locale.GERMAN;
			}
			resourceBundle = ResourceBundle.getBundle("resources.messages", locale);
		}
	}

	/**
	 * Get the message from ResourceBundle by messageId and params.
	 * 
	 * @param messageId
	 * @param params
	 * @return message.
	 */
	public String getMessage(String messageId, Object... params) {
		String value = null;
		try {
			value = resourceBundle.getString(messageId);
			if (params != null) {
				value = MessageFormat.format(value, params);
			}
		} catch (MissingResourceException e) {
			log.error("Missing Resource bundle, could not display message : " + messageId);
		} catch (NullPointerException e) {
			log.error("Missing Resource bundle, could not dipslay message : " + messageId);
		} catch (IllegalArgumentException e) {
			log.error(
					"the pattern is invalid, or an argument in the arguments array is not of the type expected by the "
							+ "format element(s) that use it, could not display message : " + messageId);
		}
		return value;
	}

	protected Map<String, ?> getRequestParameterMap() {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
	}

	/**
	 * Get the attribute from Session by key.
	 * 
	 * @param key
	 * @return attribute object.
	 */
	private Object getAttributeFromSession(String key) {
		HttpSession httpSession = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		if (httpSession != null) {
			return httpSession.getAttribute(key);
		}
		return null;
	}

	/**
	 * Set the attribute in Session :key and value.
	 * 
	 * @param key
	 * @param value
	 */
	protected void setAttributeInSession(String key, Object value) {
		HttpSession httpSession = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		if (facesContext != null) {
			httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		if (httpSession != null) {
			httpSession.setAttribute(key, value);
		}
	}

	/**
	 * Add the given message with the given severity to the given component.
	 * 
	 * @param component
	 *            The component to add the given message to.
	 * @param message
	 *            The message to be added to the given component.
	 * @param severity
	 *            The severity to be associated with the given message.
	 */
	private void addMessage(String clientId, String message, Severity severity) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		facesContext.addMessage(clientId, new FacesMessage(severity, message, null));
	}

	/**
	 * Add message having Severity SEVERITY_INFO to facesContext.
	 * 
	 * @param clientId
	 * @param message
	 */
	protected void addInfoMessage(String clientId, String message) {
		addMessage(clientId, message, FacesMessage.SEVERITY_INFO);
	}

	/**
	 * Add message having Severity SEVERITY_ERROR to facesContext.
	 * 
	 * @param clientId
	 * @param message
	 */
	protected void addErrorMessage(String clientId, String message) {
		addMessage(clientId, message, FacesMessage.SEVERITY_ERROR);
	}

	/**
	 * Add message having Severity SEVERITY_WARN to facesContext.
	 * 
	 * @param clientId
	 * @param message
	 */
	protected void addWarnMessage(String clientId, String message) {
		addMessage(clientId, message, FacesMessage.SEVERITY_WARN);
	}

	/**
	 * Returns the label attribute of the given component.
	 * 
	 * @param component
	 *            The component to return the label attribute for.
	 * @return The label attribute of the given component.
	 */
	protected String getLabel(UIComponent component) {
		return (String) component.getAttributes().get("label");
	}

	/**
	 * Get the current user from Session.
	 * 
	 * @return UserModel.
	 */
	public UserModel getUserFromSession() {
		UserModel user = (UserModel) getAttributeFromSession(UserModel.class.getName());
		if (user != null) {
			Map<Long, Long> userCatalog = UserManagerImpl.get().getUserModifiedDefaultCatalog();
			Long userId = user.getUserId();
			if (userCatalog.containsKey(userId)) {
				user.setDefaultStdSatzKatRefId(userCatalog.get(userId));
				setAttributeInSession(UserModel.class.getName(), user);
				userCatalog.remove(userId);
			}
		}
		return user;
	}

	/**
	 * Check if a user is administrator on a Marke
	 * 
	 * @param owner
	 * @return
	 */
	public boolean isLoggedUserAdminOnBrand(BrandModel owner) {
		UserModel loggedUser = getUserFromSession();
		if (loggedUser.haveRole(RoleManager.Role.ADMINISTRATOR.value()) && loggedUser.getUserBrands().contains(owner)) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the user in session has the right to execute an action.
	 * 
	 * @return boolean.
	 */
	public boolean getHasManageUserRight() {
		return getUserFromSession().haveRightToDo(RoleManager.Right.MANAGE_USER.value());
	}

	/**
	 * Check if the user in session has the role of Root Admin.
	 * 
	 * @return boolean.
	 */
	public boolean isRootAdmin() {
		return getUserFromSession().haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value());
	}

	/**
	 * Check if a change happened on a component and add the change to the pagesChanges Set.
	 * 
	 * @param event
	 */
	public void checkChanged(ValueChangeEvent event) {
		// PTS-10757: a new behavior after the integration of Icefaces 3:A partial submit will still fire when we change
		// tabs.
		// If components are bound to String properties, conversion from null to "" causes the valueChangeListeners to
		// fire.
		// As workaround change the "" value from old and new value to null
		if (log.isDebugEnabled()) {
			log.debug("###value was changed ###");
		}
		Object oldValue = event.getOldValue() == "" ? null : event.getOldValue();
		Object newValue = event.getNewValue() == "" ? null : event.getNewValue();
		if (oldValue != newValue) {
			UIComponent component = event.getComponent();
			String clientID = component.getClientId(FacesContext.getCurrentInstance());

			if (log.isDebugEnabled()) {
				log.debug(clientID + " Old: " + oldValue + " New: " + newValue);
			}
			dto.getPageChanges().add(clientID);
		}
	}

	/**
	 * close the SaveMessage Popup.
	 * 
	 * @param event
	 */
	public void closeSaveMessagePopup(ActionEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Close save confirmation popup ... : " + dto.getClass());
		}
		dto.setShowSaveChangesPopup(Boolean.FALSE);
	}

	/**
	 * cancel Modifications
	 * 
	 * @param event
	 */
	public void cancelModifications(ActionEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("Cancel modification and reload page : " + dto.getClass());
		}
		dto.getPageChanges().clear();
		dto.setShowSaveChangesPopup(Boolean.FALSE);
		// ZS: PTS_Change-26775: Make possible to navigate in the tree when the user choose to not save a previous
		// change
		Node gotoNode = navigationTree.getGotoNode();
		Node currentNode = gotoNode != null ? gotoNode : navigationTree.getSelectedNodeObject();
		if (currentNode != null && currentNode.getNodeId() != null) {
			currentNode.nodeClicked(event);
		} else {
			currentNode.getParent().nodeClicked(event);
		}
		navigationTree.setGotoNode(null);
		navigationTree.setDownActionDefintion(specificCancelModificationMessage());
	}

	/**
	 * used for jsf rendering
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Boolean getLoadDTO() {
		Node node = navigationTree.getSelectedNodeObject();
		dto = (T) node.getDto();
		if (!dto.isSearchMode()) {
			if (node.isLeaf()) {
				dto.setSearchString(navigationTree.getSelectedNodeObject().getParent().getMenuDisplayText() + " "
						+ getMessage("field.search"));
			} else {
				dto.setSearchString(
						navigationTree.getSelectedNodeObject().getMenuDisplayText() + " " + getMessage("field.search"));
			}
		} else {
			dto.setSearchString(dto.getSearchFolder() + " " + getMessage("field.search"));
		}
		specificLoadDTO();
		return true;
	}

	/**
	 * specific actions when loading DTO
	 * 
	 */
	public void specificLoadDTO() {
	}

	/**
	 * get message shown in footer when cancel action
	 * 
	 * @return cancelMessage
	 */
	public String specificCancelModificationMessage() {
		return "";
	}

	/**
	 * used for search action
	 * 
	 */
	@SuppressWarnings("unchecked")
	public void search(ActionEvent event) {
		Node currentNode = navigationTree.getSelectedNodeObject();
		BranchNode startFolder;
		if (currentNode instanceof FolderNode) {
			startFolder = (FolderNode) currentNode;
		} else if (currentNode instanceof CostElementVersionNode) {
			startFolder = currentNode.getParent().getParent();
		} else if (currentNode instanceof CostComponentInProcessVersionNode) {
			startFolder = currentNode.getParent().getParent();
		} else if (currentNode instanceof CostComponentVersionStandNode) {
			startFolder = currentNode.getParent().getParent().getParent();
		} else {
			startFolder = currentNode.getParent();
		}
		String folderSearchName = startFolder.getMenuDisplayText();
		Long folderSearchId = startFolder.getNodeId();

		T newDTO = (T) dto.copyUtilAttributesForSearch();
		// if (newDTO.isNoSearchResult()) {
		if (newDTO.getSearchFolder() != null && !newDTO.getSearchFolder().equals(""))
			folderSearchName = newDTO.getSearchFolder();
		folderSearchId = newDTO.getFolderId();
		// }
		Node searchNode = navigationTree.getObjectTypeSearchResultNode().get(startFolder.getObjectContentId());

		log.info("Starting search in \"" + folderSearchName + "[" + folderSearchId + "] \" For \""
				+ dto.getSearchString() + "\"");
		newDTO.setFolderId(folderSearchId);
		newDTO.setSearchFolder(folderSearchName);
		newDTO.setUserLogged(getUserFromSession());
		service.loadDTOForListBySearchString(newDTO);
		// expand parent
		searchNode.getParent().expandClicked(event);
		// set dto and click node
		searchNode.setDto(newDTO);
		searchNode.setExpanded(Boolean.FALSE);
		searchNode.nodeClicked(event);
		navigationTree.setDownActionDefintion(specificSearchMessage());
	}

	/**
	 * get message shown in footer while searching objects
	 * 
	 * @return searchMessage
	 */
	public String specificSearchMessage() {
		return "";
	}

	/**
	 * delete Node by giving its id
	 * 
	 * @param event
	 */
	public void deleteClicked(ActionEvent event) {
		FolderNode currentNode = navigationTree.getSelectedNodeObjectAsFolder();
		String objectId = (String) getRequestParameterMap().get("object_id");
		Node nodeToDelete = currentNode.getNodeById(Long.parseLong(objectId), currentNode.getObjectContentId());
		log.info("Start to delete Object(id,objectTypeId)=(" + objectId + "," + currentNode.getObjectContentId() + ")");
		nodeToDelete.deleteClicked(event);
	}

	/**
	 * delete selected Node
	 * 
	 * @param event
	 */
	public void deleteObjectClicked(ActionEvent event) {
		Node currentNode = navigationTree.getSelectedNodeObject();
		log.info("Start to delete Object(id,objectTypeId)=(" + currentNode.getNodeId() + ","
				+ currentNode.getObjectTypeId() + ")");
		currentNode.deleteClicked(event);
	}

	/**
	 * edit Node by giving its id
	 * 
	 * @param event
	 */
	public void editClicked(ActionEvent event) {
		BranchNode currentNode = navigationTree.getSelectedNodeObjectAsFolder();
		String objectId = (String) getRequestParameterMap().get("object_id");
		Node nodeToEdit = (Node) currentNode.getNodeById(Long.parseLong(objectId), currentNode.getObjectContentId());
		log.info("Start edit Object(id,objectTypeId)=(" + objectId + "," + currentNode.getObjectContentId() + ")");
		nodeToEdit.nodeClicked(event);
	}

	/**
	 * create ObjectNode from folder node
	 * 
	 * @param event
	 */
	public void createClicked(ActionEvent event) {
		FolderNode folder = navigationTree.getSelectedNodeObjectAsFolder();
		log.info("Start creating Object(objectTypeId)=(" + folder.getObjectContentId() + ")");
		folder.addNewObjectClicked(event);
	}

	/**
	 * create ObjectNode when showing another object having same type
	 * 
	 * @param event
	 */
	public void createFromObjectClicked(ActionEvent event) {
		Node currentNode = navigationTree.getSelectedNodeObject();
		BranchNode startFolder = currentNode.getParent();
		if (currentNode instanceof CostElementVersionNode) {
			startFolder = startFolder.getParent();
		}
		if (currentNode instanceof CostComponentInProcessVersionNode) {
			startFolder = startFolder.getParent();
		}
		if (currentNode instanceof CostComponentVersionStandNode) {
			startFolder = startFolder.getParent().getParent();
		}
		log.info("folder: " + startFolder.getMenuDisplayText());

		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		startFolder.addNewObjectClicked(event);
	}

	/**
	 * list the content of folder when showing another object in the same folder
	 * 
	 * @param event
	 */
	public void listFromObjectClicked(ActionEvent event) {
		BranchNode folder = navigationTree.getSelectedNodeObject().getParent();
		log.info("Start get list from Object(objectTypeId)=(" + folder.getObjectContentId() + ")");
		if (dto.getPageChanges().size() > 0) {
			dto.setShowSaveChangesPopup(Boolean.TRUE);
			return;
		}
		folder.nodeClicked(event);
	}

	/**
	 * back action Without Save
	 * 
	 * @param event
	 */
	public void backWithoutSave(ActionEvent event) {
		BranchNode folder = navigationTree.getSelectedNodeObject().getParent();
		log.info("Start get list from Object(objectTypeId)=(" + folder.getObjectContentId() + ")");
		dto.getPageChanges().clear();

		// Force a new View: This causes the current View tree to be discarded and a fresh one created. The new
		// components of course
		// then have no submitted values, and so fetch their displayed values via their value-bindings.
		FacesContext context = FacesContext.getCurrentInstance();
		Application application = context.getApplication();
		ViewHandler viewHandler = application.getViewHandler();
		UIViewRoot viewRoot = viewHandler.createView(context, context.getViewRoot().getViewId());
		context.setViewRoot(viewRoot);
		context.renderResponse();

		folder.setExpanded(Boolean.FALSE);
		folder.nodeClicked(event);

	}

	/**
	 * get absolute classDirectory path
	 * 
	 * @return path
	 */
	protected String getAbsoluteClassDirPath() {
		File file = new File(this.getClass().getResource("/uploadRef.properties").getPath());
		return file.getParentFile().getAbsolutePath();
	}

	/**
	 * get InputStream bytes array
	 * 
	 * @param f
	 * @return array of bits
	 */
	protected byte[] getArrayByteFromFile(File f) throws IOException {
		InputStream input = new FileInputStream(f);
		byte[] data = IOUtils.toByteArray(input);
		input.close();
		return data;
	}

	/**
	 * 
	 * @param obj
	 */
	protected void clearSubmittedValues(Object obj) {

		if (obj == null || obj instanceof UIComponent == false) {
			return;
		}
		List<UIComponent> chld = ((UIComponent) obj).getChildren();
		if (chld.size() != 0) {
			for (int i = 0; i < chld.size(); i++) {
				clearSubmittedValues(chld.get(i));
			}
		}
		if (obj instanceof HtmlInputText) {
			((HtmlInputText) obj).setSubmittedValue(null);
			((HtmlInputText) obj).setValue(null);
			((HtmlInputText) obj).setLocalValueSet(false);
		} else if (obj instanceof UIInput) {

			((UIInput) obj).setSubmittedValue(null);
			((UIInput) obj).setValue(null);
			((UIInput) obj).setLocalValueSet(false);
		}
	}

	/**
	 * 24.10.2013: ZS: PTS_Requirement-22210: Search folders Show folder from the search result
	 * 
	 * @param event
	 */
	public void showFolder(ActionEvent event) {
		String folderId = (String) getRequestParameterMap().get("folder_id");
		String creatorRefId = (String) getRequestParameterMap().get("creator_id");
		FolderNode actualNode = navigationTree.getRootNode();
		if (creatorRefId != null && !creatorRefId.equals("")) {
			actualNode = navigationTree.getWorkingNode();
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(Long.parseLong(creatorRefId),
					EnumObjectTypeManager.FOLDER_ID);
		}
		List<Long> folderIdsHierarchy = navigationTree.getNavigationService()
				.getFolderIdsHierarchy(Long.parseLong(folderId));
		for (int i = 0; i < folderIdsHierarchy.size(); i++) {
			actualNode.setExpanded(true);
			actualNode.expandClicked(event);
			actualNode = (FolderNode) actualNode.getNodeById(folderIdsHierarchy.get(i),
					EnumObjectTypeManager.FOLDER_ID);
		}
		actualNode.nodeClicked(event);
	}

	/**
	 * Check if the logged user has the role of visitor
	 * 
	 * @return true/false
	 */
	public boolean isVisitorUser() {
		return getUserFromSession().haveRole(RoleManager.Role.BETRACHTER.value());
	}

	protected void setSessionLoginParam(UserModel user) {
		HttpSession httpSession = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		// make sure we have a valid facesContext
		if (facesContext != null) {
			httpSession = (HttpSession) facesContext.getExternalContext().getSession(false);
		}
		// finally make sure the session is not null before trying to set the
		// login
		if (httpSession != null) {
			httpSession.setAttribute(UserModel.class.getName(), user);
		}
	}
}
