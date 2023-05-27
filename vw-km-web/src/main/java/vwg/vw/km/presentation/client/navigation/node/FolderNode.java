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
package vwg.vw.km.presentation.client.navigation.node;

import java.util.List;
import java.util.Set;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.commons.collections4.CollectionUtils;
import org.icefaces.util.JavaScriptRunner;

import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.application.service.exception.DestinationFolderIsSubFolderException;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;

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
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.100 $ $Date: 2016/11/21 08:56:12 $
 */
public abstract class FolderNode extends BranchNode {

	private final Log log = LogManager.get().getLog(FolderNode.class);

	public FolderNode(NavigationBean tree, FolderNode parent, Long folderId) {
		super(tree, parent, folderId);
	}

	// List of method used for adding new elements
	/**
	 * event called when new folder link clicked
	 * 
	 * @param evt
	 */
	@Override
	public void addFolderClicked(ActionEvent evt) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		// check if the filter on empty folders is activated
		UserModel user = treeBean.getUserFromSession();
		if (user.getShowEmptyFolders() != null && !user.getShowEmptyFolders()
				&& (getObjectContentId().equals(EnumObjectTypeManager.COMPONENT_ID)
						|| getObjectContentId().equals(EnumObjectTypeManager.ELEMENT_ID))) {
			treeBean.openConfirmAddFolderPopup();
			treeBean.setFolderNodeToBeAdded(this);
			return;
		}
		log.info("Add clicked for " + this.getMenuDisplayText());
		log.info("New Folder Name " + getNewFolderName());
		log.info("Parent Id " + getNodeId());
		log.info("ObjectContentId " + getObjectContentId());
		FolderModel added = treeBean.getNavigationService().saveNewFolder(getNewFolderName(), getNodeId(),
				getObjectContentId());
		log.info("Folder created " + added.getFolderId());
		setExpanded(Boolean.FALSE);
		nodeClicked(evt);
		FolderNode addedNode = (FolderNode) getNodeById(added.getFolderId(), getObjectTypeId());
		addedNode.editClicked(evt);
		treeBean.setDownActionDefintion(treeBean.getMessage("action.folder.createFolder"));
	}

	public void addHiddenFolderClicked(ActionEvent evt) {

		log.info("Add clicked for " + this.getMenuDisplayText());
		log.info("New Folder Name " + getNewFolderName());
		log.info("Parent Id " + getNodeId());
		log.info("ObjectContentId " + getObjectContentId());
		FolderModel added = treeBean.getNavigationService().saveNewFolder(getNewFolderName(), getNodeId(),
				getObjectContentId());
		log.info("Folder created " + added.getFolderId());

		setExpanded(Boolean.FALSE);
		nodeClicked(evt);
		FolderNode addedNode = (FolderNode) getNodeById(added.getFolderId(), getObjectTypeId());
		addedNode.setVisible(true);
		addedNode.editClicked(evt);
		treeBean.setDownActionDefintion(treeBean.getMessage("action.folder.createFolder"));

	}

	/**
	 * new folder name used for new created folder
	 * 
	 * @return
	 */
	@Override
	public abstract String getNewFolderName();

	// List of methods used to update element name
	/**
	 * event called when edit link clicked from context menu
	 * 
	 * @param evt
	 */
	@Override
	public void editClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		log.info("Edit clicked for " + this.getMenuDisplayText());
		treeBean.setFolderNodeToBeEdited(this);
		JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "setFocusToEditableFolderName();");
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#nodeClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public void nodeClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			treeBean.setGotoNode(this);
			return;
		}
		// Expand tree and display childs
		// Is child loaded, Is node expanded ?
		if (objectChildsCount == 1) {
			expandClicked(event);
		}
		if (!this.expanded) {
			expandClicked(event);
			this.setExpanded(Boolean.TRUE);
		} else {
			this.setExpanded(Boolean.FALSE);
		}
		treeBean.setSelectedNodeObject(this);

		// execute specific workflow
		specificNodeClicked(event);

		JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "scrollToSelectedNode();");
		treeBean.setDownActionDefintion(getListClickedMessage());
	}

	/**
	 * if one child found, it will be displayed directly in the content.
	 * 
	 * @param event
	 */
	protected void specificNodeClicked(ActionEvent event) {
		if (objectChildsCount == 1) {
			Node n = (childs.get(indexOfLastObject));
			if (n.isVisible()) {
				childs.get(indexOfLastObject).nodeClicked(event);
				this.setExpanded(Boolean.TRUE);
				JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "scrollToSelectedNode();");
				return;
			}
		}
	}

	/**
	 * event called when folder name is changed
	 * 
	 * @param vce
	 */
	public void changedValue(ValueChangeEvent vce) {
		Object nvalue = vce.getNewValue();
		log.info("change value for " + getMenuDisplayText() + " to " + nvalue);
		if (nvalue instanceof String) {
			Boolean result = treeBean.getNavigationService().saveNewNameFolder("" + nvalue, getNodeId());
			if (!result) {
				treeBean.openErrorPopup(treeBean.getMessage("folder.existSameName"));
				// this.setText((String)vce.getOldValue());
			} else {
				treeBean.getNavigationElementBean().setDirty(true);
				treeBean.setFolderNodeToBeEdited(NavigationBean.NEW_FOLDER);
				getParent().expandClicked(new ActionEvent(vce.getComponent().getParent()));
				treeBean.setDownActionDefintion(treeBean.getMessage("action.folder.renameFolder"));
			}
		}
	}

	/**
	 * Action event called to rename folder in the tree. before saving the new name try to check if it exist the same
	 * name for other folders with the same parent
	 * 
	 * @param evt
	 *            action event
	 */
	public void renameFolder(ActionEvent evt) {
		String folderName = getMenuDisplayText();
		log.info("change value to " + folderName);
		if (folderName != null && !"".equals(folderName.trim())) {
			Boolean result = treeBean.getNavigationService().saveNewNameFolder(getMenuDisplayText(), getNodeId());
			if (!result) {
				treeBean.openErrorPopup(treeBean.getMessage("folder.existSameName"));
			} else {
				treeBean.getNavigationElementBean().setDirty(true);
				treeBean.setFolderNodeToBeEdited(NavigationBean.NEW_FOLDER);
				getParent().expandClicked(new ActionEvent(evt.getComponent().getParent()));
				treeBean.setDownActionDefintion(treeBean.getMessage("action.folder.renameFolder"));
			}
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#specificDeleteClicked(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificDeleteClicked(ActionEvent event) {
		log.info("Delete clicked for " + this.getMenuDisplayText());
		String msg = treeBean.getNavigationService().deleteFolder(getNodeId(), getObjectContentId());
		treeBean.getNavigationElementBean().setDirty(true);
		if (!"".equals(msg)) {
			return treeBean.getMessage(msg);
		}
		return "";
	}

	/**
	 * event called when paste link from context menu is clicked
	 * 
	 * @param event
	 */
	public void pasteClicked(ActionEvent event) {
		log.info("Paste cutted Node under the clicked node for " + this.getMenuDisplayText());
		Node toCut = treeBean.getCuttedNode();
		String error = "";
		boolean errorOccured = Boolean.FALSE;
		if (toCut instanceof FolderNode) {
			if (((FolderNode) toCut).getObjectContentId().equals(getObjectContentId())) {
				try {
					Boolean saved = treeBean.getNavigationService().saveFolderToFolder(toCut.getNodeId(), getNodeId());
					if (!saved) {
						log.error("Can not paste : existing folder name");
						error = getExistingFolderMessage();
						errorOccured = Boolean.TRUE;
					}
				} catch (DestinationFolderIsSubFolderException e) {
					log.error("Can not paste : The destination folder is a subfolder of the source folder");
					error = treeBean.getMessage("folder.pasteInChild.error");
					errorOccured = Boolean.TRUE;
				}
			} else {
				// alert object aren't of same type
				log.error("Can not paste : object are not of same type");
				error = treeBean.getMessage("object.notSameType");
				errorOccured = Boolean.TRUE;
			}
		} else {
			if (toCut.getObjectTypeId().equals(getObjectContentId())) {
				Boolean moved = moveObjectNodeToFolder(toCut, this, false);
				if (!moved) {
					log.error("Can not paste : existing object name");
					error = getExistingObjectMessage();
					errorOccured = Boolean.TRUE;
				}
			} else {
				// alert object aren't of same type
				log.error("Can not paste : object are not of same type");
				error = treeBean.getMessage("object.notSameType");
				errorOccured = Boolean.TRUE;
			}
		}
		if (errorOccured) {
			treeBean.setErrorMessage(error);
			treeBean.setShowError(Boolean.TRUE);
			return;
		}
		// ZS: PTS_Problem-34862: remove the old child reference of the node to be cut
		if (toCut.getParent().childs != null) {
			toCut.getParent().childs.remove(toCut);
		}

		toCut.removeFromParent();

		// setExpanded(true);
		toCut.setParent(this);
		this.addChild(toCut);

		treeBean.setCuttedNode(null);

		// expand parent when child is selected
		expandClicked(event);
		setExpanded(Boolean.TRUE);
		// click the pasted child
		toCut.nodeClicked(event);
		// Cutted from search folder do refresh
		if (treeBean.getSourceCopydNode() instanceof StaticFolderNode) {
			log.info("Cutted from search folder do refresh");
			((FolderNode) treeBean.getSourceCopydNode()).expandClicked(event);
		}

		treeBean.getNavigationElementBean().setDirty(true);
		treeBean.setDownActionDefintion(getPasteMessage());
	}

	/**
	 * event called when paste link from context menu is clicked
	 * 
	 * @param event
	 */
	public void pasteCopyClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		log.info("Paste copied Node under the clicked node for " + this.getMenuDisplayText());
		Node toCopy = treeBean.getCopiedNode();

		String error = "";
		boolean errorOccured = Boolean.FALSE;
		if (!toCopy.getObjectTypeId().equals(getObjectContentId())) {
			// alert object aren't of same type
			log.error("Can not paste : object are not of same type");
			error = treeBean.getMessage("object.notSameType");
			errorOccured = Boolean.TRUE;
		}

		if (errorOccured) {
			treeBean.setErrorMessage(error);
			treeBean.setShowError(Boolean.TRUE);
			return;
		}
		moveObjectNodeToFolder(toCopy, this, true);
		log.info("copied Node " + toCopy.getMenuDisplayText());

		// make a new copy of the node to be used
		Node sourceCopyNode = treeBean.getSourceCopydNode();
		sourceCopyNode.copyClicked(event);
		// 22.11.2013: ZS: PTS_Requirement-26653: Copied node is saved in the past action
		toCopy.getDto().getPageChanges().clear();
		String saveError = specificSaveNode(toCopy.getDto());
		if (saveError != null) {
			StringBuilder errorMsg = new StringBuilder();
			errorMsg.append(saveError);
			errorMsg.append(toCopy.getMenuDisplayText());
			errorMsg.append("<br/>");
			treeBean.setErrorMessage(errorMsg.toString());
			treeBean.setShowError(Boolean.TRUE);
			return;
		}
		expandClicked(event);
		setExpanded(Boolean.TRUE);
		treeBean.getNavigationElementBean().setDirty(true);
		treeBean.setDownActionDefintion(getPasteMessage());
	}

	/**
	 * return the message to show in status bar when paste function is called
	 * 
	 * @return
	 */
	@Override
	protected abstract String getPasteMessage();

	/**
	 * return the message to show in status bar when paste function is called
	 * 
	 * @return
	 */
	protected String getMarkedPasteMessage(String nodeMarkedCount) {
		throw new RuntimeException("Implement me for object delete");
	}

	/**
	 * this method move an object node from its original folder to a new folder
	 * 
	 * @param node
	 * @param toNode
	 * @return
	 */
	public abstract Boolean moveObjectNodeToFolder(Node node, FolderNode toNode, boolean isCopy);

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public final Long getObjectTypeId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#copyClicked(javax.faces .event.ActionEvent)
	 */
	@Override
	public final void copyClicked(ActionEvent event) {
		throw new RuntimeException("can't implement object copy");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCopyMessage()
	 */
	@Override
	protected String getCopyMessage() {
		return null;
	}

	/**
	 * event called when paste link from context menu is clicked
	 * 
	 * @param event
	 */
	public void pasteMarkedClicked(ActionEvent event) {
		Set<Node> toCutList = treeBean.getCuttedMarkedNodes();
		log.info("Paste " + CollectionUtils.size(toCutList) + " selected Nodes under " + this.getMenuDisplayText());
		int toCutNodeSize = 0;
		if (!CollectionUtils.isEmpty(toCutList)) {
			toCutNodeSize = toCutList.size();
		}
		StringBuilder error = new StringBuilder();
		boolean errorOccured = Boolean.FALSE;
		for (Node toCut : toCutList) {
			if (toCut instanceof FolderNode) {
				if (((FolderNode) toCut).getObjectContentId().equals(getObjectContentId())) {
					try {
						Boolean saved = treeBean.getNavigationService().saveFolderToFolder(toCut.getNodeId(),
								getNodeId());
						if (!saved) {
							log.error("Can not paste : existing folder name");
							error.append(getExistingFolderMessage());
							error.append("<br/>");
							errorOccured = Boolean.TRUE;
						}
					} catch (DestinationFolderIsSubFolderException e) {
						log.error("Can not paste : The destination folder is a subfolder of the source folder");
						error.append(treeBean.getMessage("folder.pasteInChild.error"));
						error.append("<br/>");
						errorOccured = Boolean.TRUE;
					}
				} else {
					// alert object aren't of same type
					log.error("Can not paste : object are not of same type");
					error.append(treeBean.getMessage("object.notSameType"));
					error.append("<br/>");
					errorOccured = Boolean.TRUE;
				}
			} else {
				if (toCut.getObjectTypeId().equals(getObjectContentId())) {
					Boolean moved = moveObjectNodeToFolder(toCut, this, false);
					if (!moved) {
						log.error("Can not paste : existing object name");
						error.append(getExistingObjectMessage());
						error.append("<br/>");
						errorOccured = Boolean.TRUE;
						toCutNodeSize--;
					}
				} else {
					// alert object aren't of same type
					log.error("Can not paste : object are not of same type");
					error.append(treeBean.getMessage("object.notSameType"));
					error.append("<br/>");
					errorOccured = Boolean.TRUE;
					toCutNodeSize--;
				}
			}
			if (errorOccured) {
				continue;
			}
			toCut.removeFromParent();
			// ZS: PTS_Problem-34862: remove the old child reference of the node to be cut
			// if(toCut.getChilds != null) {
			// toCut.getChilds.remove(toCut);
			// }
			// setExpanded(true);
			toCut.setParent(this);
			this.addChild(toCut);
		}
		treeBean.setCuttedMarkedNodes(null);
		// expand parent when child is selected
		expandClicked(event);
		setExpanded(Boolean.TRUE);
		// click the pasted child
		// toCut.nodeClicked(event);

		if (toCutNodeSize == 1) {
			treeBean.setDownActionDefintion(getPasteMessage());
		} else {
			treeBean.setDownActionDefintion(getMarkedPasteMessage(toCutNodeSize + ""));
		}

		// ZS: PTS_Problem-34861 : Cancel multiple selection after performing the action
		treeBean.getMarkedNodes().get(getObjectContentId()).clear();

		if (error.length() > 0) {
			treeBean.setErrorMessage(error.toString());
			treeBean.setShowError(Boolean.TRUE);
			return;
		}
		treeBean.getNavigationElementBean().setDirty(true);
		treeBean.setDownActionDefintion(getPasteMessage());
	}

	/**
	 * event called when paste link from context menu is clicked
	 * 
	 * @param event
	 */
	public void pasteCopyMarkedClicked(ActionEvent event) {
		if (treeBean.getNeedSaveMessageConfirmation()) {
			return;
		}
		List<Node> toCopyList = treeBean.getCopiedMarkedNodes();
		log.info("Paste " + CollectionUtils.size(toCopyList) + " selected Nodes under " + this.getMenuDisplayText());
		int toCopyNodeSize = 0;
		if (!CollectionUtils.isEmpty(toCopyList)) {
			toCopyNodeSize = toCopyList.size();
		}
		StringBuilder error = new StringBuilder();
		String globelaErrorMessage = null;
		boolean errorOccured = Boolean.FALSE;
		for (Node toCopy : toCopyList) {
			if (!toCopy.getObjectTypeId().equals(getObjectContentId())) {
				// alert object aren't of same type
				log.error("Can not paste : object are not of same type");
				error.append(treeBean.getMessage("object.notSameType"));
				error.append("<br/>");
				errorOccured = Boolean.TRUE;
			}

			if (errorOccured) {
				toCopyNodeSize = toCopyNodeSize - 1;
				continue;
			}
			errorOccured = Boolean.FALSE;
			moveObjectNodeToFolder(toCopy, this, true);
			String saveError = specificSaveNode(toCopy.getDto());
			if (saveError != null) {
				globelaErrorMessage = saveError;
				error.append(toCopy.getMenuDisplayText());
				error.append("<br/>");
				toCopyNodeSize = toCopyNodeSize - 1;
				continue;
			}
			log.info("copied Node " + toCopy.getMenuDisplayText());
		}
		// PTS problem 34323 :ZSE clear copied node after paste
		treeBean.setCopiedMarkedNodes(null);
		expandClicked(event);
		setExpanded(Boolean.TRUE);

		if (toCopyNodeSize == 1) {
			treeBean.setDownActionDefintion(getPasteMessage());
		} else {
			treeBean.setDownActionDefintion(getMarkedPasteMessage(toCopyNodeSize + ""));
		}

		// ZS: PTS_Problem-34861 : Cancel multiple selection after performing the action
		treeBean.getMarkedNodes().get(getObjectContentId()).clear();

		if (error.length() > 0) {
			treeBean.setErrorMessage(globelaErrorMessage + "" + error.toString());
			treeBean.setShowError(Boolean.TRUE);
			return;
		}

		treeBean.getNavigationElementBean().setDirty(true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#copyClicked(javax.faces .event.ActionEvent)
	 */
	public String specificSaveNode(BaseDTO dto) {
		throw new RuntimeException("can't implement object copy");
	}
}
