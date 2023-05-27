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
package vwg.vw.km.presentation.client.navigation.node.calculationsettings;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.service.dto.UserSettingsDTO;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.FolderNode;
import vwg.vw.km.presentation.client.navigation.node.LeafNode;
import vwg.vw.km.presentation.client.navigation.node.StaticNodeIdEnum;

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
 * @version $Revision: 1.23 $ $Date: 2015/12/11 07:59:15 $
 */
public class UserDefinitionSettingsNode extends LeafNode {

	public UserDefinitionSettingsNode(NavigationBean tree, FolderNode parent) {
		super(tree, parent);
		setNodeId(StaticNodeIdEnum.USER_DEFINITION_SETTINGS_NODE.value());
		setTemplateName("./settings/userSettings.xhtml");
		setPopupPageName("./settings/popup.xhtml");
		setLeafIcon(makeIcon("setting.gif"));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.LeafNode#specificLeafNodeClicked()
	 */
	@Override
	public void specificLeafNodeClicked() {
		// fill DTO ()
		UserSettingsDTO dto = new UserSettingsDTO();
		UserModel userLogged = treeBean.getUserFromSession();
		dto.setUserLogged(userLogged);
		dto.setDefaultStdSatzKatRefId(userLogged.getDefaultStdSatzKatRefId());
		dto.setDefaultBrand(userLogged.getDefaultBrand());
		dto.setDefaultLibrary(userLogged.getDefaultLibrary());
		dto.setStatusElementPublish(userLogged.getStatusElementPublish());
		dto.setImagesRootPath(userLogged.getImagesRootPath());
		// 28.08.2013: ZS: PTS_Requirement-22194: Add a new Mask for the user settings definition: empty folder setting
		dto.setShowEmptyFolders(userLogged.getShowEmptyFolders());
		// 19.11.2013: ABA: PTS_Requirement-22211: Add a new Mask for the user settings definition: show changes
		// notifications page setting
		dto.setDisableChangesNotifications(userLogged.getDisableChangesNotifications());
		setDto(treeBean.getUserSettingsService().loadDTO(dto));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getObjectTypeId()
	 */
	@Override
	public final Long getObjectTypeId() {
		return EnumObjectTypeManager.FOLDER_ID;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getCutMessage()
	 */
	@Override
	protected String getCutMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getDeleteSuccessMessage()
	 */
	@Override
	protected String getDeleteSuccessMessage() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.Node#getEditMessage()
	 */
	@Override
	protected String getEditMessage() {
		return null;
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
}
