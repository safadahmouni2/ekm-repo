package vwg.vw.km.presentation.client.base;

import java.io.File;

import javax.faces.event.ActionEvent;

import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.presentation.client.navigation.node.Node;

public abstract class BaseDetailBean<T extends BaseDTO, X extends BaseService<T>> extends BaseBean<T, X> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6523541124822919589L;

	/**
	 * related actions to each save action
	 * 
	 * @param event
	 */
	public void save(ActionEvent event) {
		String saveResult = specificSave(event);
		dto.setShowSaveChangesPopup(Boolean.FALSE);
		if ("".equals(saveResult)) {
			dto.getPageChanges().clear();
			afterSave(event);
			navigationTree.setDownActionDefintion(getSaveMessage());
		} else {
			navigationTree.setGotoNode(null);
			navigationTree.openErrorPopup(saveResult);
		}
	}

	/**
	 * after save action update display text and node id
	 */
	protected void afterSave(ActionEvent event) {
		Node actualNode = navigationTree.getSelectedNodeObject();
		actualNode.setMenuDisplayText(getDisplayText());
		actualNode.setNodeId(getNodeId());
		// ZS: PTS_Problem-35155: Make possible to navigate in the tree when the user choose to save a previous change
		Node gotoNode = navigationTree.getGotoNode();
		if (gotoNode != null) {
			gotoNode.nodeClicked(event);
		}
		navigationTree.setGotoNode(null);
	}

	/**
	 * specific save actions
	 * 
	 * @param event
	 */
	public abstract String specificSave(ActionEvent event);

	/**
	 * get the selected nodeId
	 * 
	 * @return nodeId
	 */
	public abstract Long getNodeId();

	/**
	 * get the selected node displayText
	 * 
	 * @return displayText
	 */
	public abstract String getDisplayText();

	/**
	 * get message shown in footer while saving object
	 * 
	 * @return saveMessage
	 */
	public abstract String getSaveMessage();

	/**
	 * get the document upload directory path
	 * 
	 * @return
	 */
	public String getDocumentUploadDirectory() {
		return "WEB-INF" + File.separator + "classes" + File.separator + "upload";
	}

	/**
	 * ZS: PTS_Problem-36803: Generate a random ID
	 * 
	 * @param length
	 * @return
	 */
	protected String generateRandomId(int length) {
		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
}
