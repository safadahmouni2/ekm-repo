package vwg.vw.km.presentation.client.navigation.node.administration;

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.navigation.node.Node;

public class WorkAreaUserListNode extends UserAdministrationNode {

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.navigation.node.administration.UserAdministrationNode#getChilds()
	 */
	@Override
	public List<Node> getChilds() {
		List<Node> childs = new ArrayList<Node>();
		// load users
		UserDTO dto = new UserDTO();
		dto.setUserLogged(treeBean.getUserFromSession());
		dto.setWorkAreaId(getNodeId());
		List<UserModel> list = treeBean.getUserService().loadUsers(dto).getUsers();
		dto.setUserCreationAllowedOnPage(Boolean.FALSE);
		setDto(dto);
		for (UserModel userModel : list) {
			String userLabel = (userModel.getFirstName() != null)
					? userModel.getLastName() + " " + userModel.getFirstName()
					: userModel.getLastName();
			UserNode n = new UserNode(treeBean, this, userLabel, userModel.getUserId(), Boolean.FALSE);
			childs.add(n);
			objectChildsCount++;
			indexOfLastObject = childs.size() - 1;
		}
		return childs;
	}

	public WorkAreaUserListNode(NavigationBean tree, UserAdministrationNode parent, String workAreaLabel,
			Long workAreaId) {
		super(tree, parent, false);
		setContextMenu(null);
		setMenuDisplayText(workAreaLabel);
		setFromBundle(Boolean.FALSE);
		setNodeId(workAreaId);
	}

}
