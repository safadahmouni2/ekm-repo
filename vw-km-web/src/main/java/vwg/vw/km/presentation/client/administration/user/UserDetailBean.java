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

package vwg.vw.km.presentation.client.administration.user;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icefaces.ace.model.table.RowStateMap;

import vwg.vw.km.application.implementation.RoleManager;
import vwg.vw.km.application.service.dto.UserDTO;
import vwg.vw.km.application.service.dto.WorkAreaDTO;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.application.service.logic.WorkAreaService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;
import vwg.vw.km.integration.persistence.model.RoleModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;
import vwg.vw.km.presentation.client.base.BaseDetailBean;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.util.ModelSelectedObject;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : User settings bean
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: zouhair $
 * @version $Revision: 1.46 $ $Date: 2016/11/24 15:18:20 $
 */
public class UserDetailBean extends BaseDetailBean<UserDTO, UserService> {

	private static final long serialVersionUID = 6562242179223540140L;

	private final Log log = LogManager.get().getLog(UserDetailBean.class);

	private List<ModelSelectedObject> itemsLeft = new ArrayList<ModelSelectedObject>();

	private RowStateMap rowStateMapLeft = new RowStateMap();

	private RowStateMap rowStateMapRight = new RowStateMap();

	private List<ModelSelectedObject> itemsRight = new ArrayList<ModelSelectedObject>();

	private RowStateMap roleRowStateMapLeft = new RowStateMap();

	private RowStateMap roleRowStateMapRight = new RowStateMap();

	private List<ModelSelectedObject> roleItemsLeft = new ArrayList<ModelSelectedObject>();

	private List<ModelSelectedObject> roleItemsRight = new ArrayList<ModelSelectedObject>();

	private WorkAreaService workAreaService = null;

	public List<ModelSelectedObject> getItemsLeft() {
		return itemsLeft;
	}

	public void setItemsLeft(List<ModelSelectedObject> itemsLeft) {
		this.itemsLeft = itemsLeft;
	}

	public List<ModelSelectedObject> getItemsRight() {
		return itemsRight;
	}

	public void setItemsRight(List<ModelSelectedObject> itemsRight) {
		this.itemsRight = itemsRight;
	}

	public List<ModelSelectedObject> getRoleItemsLeft() {
		return roleItemsLeft;
	}

	public void setRoleItemsLeft(List<ModelSelectedObject> roleItemsLeft) {
		this.roleItemsLeft = roleItemsLeft;
	}

	public List<ModelSelectedObject> getRoleItemsRight() {
		return roleItemsRight;
	}

	public void setRoleItemsRight(List<ModelSelectedObject> roleItemsRight) {
		this.roleItemsRight = roleItemsRight;
	}

	public void setworkAreaService(WorkAreaService workAreaService) {
		this.workAreaService = workAreaService;
	}

	public RowStateMap getRowStateMapLeft() {
		return rowStateMapLeft;
	}

	public void setRowStateMapLeft(RowStateMap rowStateMapLeft) {
		this.rowStateMapLeft = rowStateMapLeft;
	}

	public RowStateMap getRowStateMapRight() {
		return rowStateMapRight;
	}

	public void setRowStateMapRight(RowStateMap rowStateMapRight) {
		this.rowStateMapRight = rowStateMapRight;
	}

	public RowStateMap getRoleRowStateMapLeft() {
		return roleRowStateMapLeft;
	}

	public void setRoleRowStateMapLeft(RowStateMap roleRowStateMapLeft) {
		this.roleRowStateMapLeft = roleRowStateMapLeft;
	}

	public RowStateMap getRoleRowStateMapRight() {
		return roleRowStateMapRight;
	}

	public void setRoleRowStateMapRight(RowStateMap roleRowStateMapRight) {
		this.roleRowStateMapRight = roleRowStateMapRight;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		if (!dto.dtoInited) {
			loadAvailableWorkAreas(dto);
			loadAvailableRoles(dto);
			dto.dtoInited = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#specificSave(javax.faces.event.ActionEvent)
	 */
	@Override
	public String specificSave(ActionEvent event) {
		UserModel user = dto.getUser();
		UserModel userByKumsId = service.getUserByKUMSId(user.getkUMSUserId());
		if (user.getUserId() == null && userByKumsId != null || user.getUserId() != null && userByKumsId != null
				&& !user.getUserId().equals(userByKumsId.getUserId())) {
			return getMessage("kUMSUserId.validation.message");
		}
		
		if(user.getGender()== null || user.getLastName() == null || user.getkUMSUserId() == null ||user.getEmail() == null ){
		return getMessage("common.popup.UIInput.REQUIRED");
		}
		log.info("Try to save user " + user);

		// Work area to add
		for (ModelSelectedObject wAreaSelectedObject : getItemsRight()) {
			WorkAreaModel workAreaModel = (WorkAreaModel) wAreaSelectedObject.getModel();
			if (!user.getWorkAreas().contains(workAreaModel)) {
				user.getWorkAreas().add(workAreaModel);
			}
		}

		// Work area to remove
		for (ModelSelectedObject wAreaSelectedObject : getItemsLeft()) {
			WorkAreaModel workAreaModel = (WorkAreaModel) wAreaSelectedObject.getModel();
			if (user.getWorkAreas().contains(workAreaModel)) {
				user.getWorkAreas().remove(workAreaModel);

				// remove the related brand and library if it is associated to the removed work area
				if (workAreaModel.getBrand().getBrandId().equals(user.getDefaultBrand())) {
					user.setDefaultBrand(null);
					user.setDefaultLibrary(null);
				}
			}
		}

		// rights to add
		for (ModelSelectedObject roleSelectedObject : getRoleItemsRight()) {
			RoleModel roleModel = (RoleModel) roleSelectedObject.getModel();
			if (!user.getRoles().contains(roleModel)) {
				user.getRoles().add(roleModel);
			}
		}

		// rights to remove
		for (ModelSelectedObject roleSelectedObject : getRoleItemsLeft()) {
			RoleModel roleModel = (RoleModel) roleSelectedObject.getModel();
			if (user.getRoles().contains(roleModel)) {
				user.getRoles().remove(roleModel);
			}
		}

		// 19.11.2013: ZS: PTS_Requirement-22189: Integration of the new role BETRACHTER
		if (!user.getRoles().isEmpty() && user.getRoles().size() > 1
				&& user.haveRole(RoleManager.Role.BETRACHTER.value())) {
			return getMessage("userAdministration.edit.role.error");
		}

		if (user.getDefaultBrand() == null) {
			if (user.getWorkAreas().size() == 1) {
				for (WorkAreaModel workAreaModel : user.getWorkAreas()) {
					BrandModel brand = workAreaModel.getBrand();
					user.setDefaultBrand(brand.getBrandId());
					if (brand.getLibraries() != null && !brand.getLibraries().isEmpty()) {
						LibraryModel lib = new ArrayList<LibraryModel>(brand.getLibraries()).get(0);
						user.setDefaultLibrary(lib.getLibraryId());
					}
					break;
				}
			}
		}

		// ZS:PTS_Problem-34692: When the created user have role ADMINISTRATOR he can be admin. on the assigned work
		// areas
		if (user.haveRole(RoleManager.Role.ADMINISTRATOR.value())
				|| user.haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			String workAreasAdminOn = "";
			for (WorkAreaModel wModel : user.getWorkAreas()) {
				workAreasAdminOn = workAreasAdminOn + wModel.getWorkAreaId() + ";";
			}
			user.setWorkAreasAdminOn(workAreasAdminOn);
		}
		service.save(dto);

		// ZS: PTS_Problem-34850: Refresh AO folder after saving a user
		Node userWorkingNode = navigationTree.getWorkingNode();
		if (userWorkingNode != null) {
			userWorkingNode.setExpanded(Boolean.FALSE);
		}
		return "";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getSaveMessage()
	 */
	@Override
	public String getSaveMessage() {
		return getMessage("action.user.save");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		String userLabel = (dto.getUser().getFirstName() != null)
				? dto.getUser().getLastName() + " " + dto.getUser().getFirstName()
				: dto.getUser().getLastName();
		return userLabel;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseDetailBean#getNodeId()
	 */
	@Override
	public Long getNodeId() {
		return dto.getUser().getUserId();
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 * 
	 * @param event
	 */
	public void moveSelectedToRight(ActionEvent event) {
		if (rowStateMapLeft.getSelected() != null && !rowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_work_area");
		}
		if (rowStateMapLeft.getSelected() != null) {
			for (Object o : rowStateMapLeft.getSelected()) {
				ModelSelectedObject wAreaSelectedObject = (ModelSelectedObject) o;
				getItemsRight().add(wAreaSelectedObject);
				getItemsLeft().remove(wAreaSelectedObject);
			}
			rowStateMapLeft.clear();
		}
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 * 
	 * @param event
	 */
	public void moveSelectedToLeft(ActionEvent event) {
		if (rowStateMapRight.getSelected() != null && !rowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_work_area");
		}
		if (rowStateMapRight.getSelected() != null) {
			for (Object o : rowStateMapRight.getSelected()) {
				ModelSelectedObject wAreaSelectedObject = (ModelSelectedObject) o;
				if (wAreaSelectedObject.isAccessAllowed()) {
					getItemsLeft().add(wAreaSelectedObject);
					getItemsRight().remove(wAreaSelectedObject);
				}
			}
			rowStateMapRight.clear();
		}
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 * 
	 * @param event
	 */
	public void moveSelectedRoleToRight(ActionEvent event) {
		if (roleRowStateMapLeft.getSelected() != null && !roleRowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_role");
		}
		for (Object o : roleRowStateMapLeft.getSelected()) {
			ModelSelectedObject roleSelectedObject = (ModelSelectedObject) o;
			getRoleItemsRight().add(roleSelectedObject);
		}
		getRoleItemsLeft().removeAll(roleRowStateMapLeft.getSelected());
		roleRowStateMapLeft.clear();
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 * 
	 * @param event
	 */
	public void moveSelectedRoleToLeft(ActionEvent event) {
		if (roleRowStateMapRight.getSelected() != null && !roleRowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_role");
		}
		for (Object o : roleRowStateMapRight.getSelected()) {
			ModelSelectedObject roleSelectedObject = (ModelSelectedObject) o;
			if (roleSelectedObject.isAccessAllowed()) {
				getRoleItemsLeft().add(roleSelectedObject);
				getRoleItemsRight().remove(roleSelectedObject);
			}
		}
		roleRowStateMapRight.clear();
	}

	/**
	 * load available work areas.
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO loadAvailableWorkAreas(UserDTO userDTO) {
		getItemsRight().clear();
		getItemsLeft().clear();
		getRowStateMapRight().clear();
		getRowStateMapLeft().clear();
		Set<WorkAreaModel> workAreasList;
		UserModel userLogged = userDTO.getUserLogged();
		if (userLogged.haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) {
			WorkAreaDTO dto = new WorkAreaDTO();
			workAreasList = new HashSet<WorkAreaModel>(workAreaService.loadWorkAreas(dto).getWorkAreas());
		} else {
			workAreasList = userLogged.getWorkAreas();
		}
		for (WorkAreaModel wModel : workAreasList) {
			if ((userLogged.haveRole(RoleManager.Role.ROOT_ADMINISTRATOR.value())) || (!userLogged.equals(dto.getUser())
					&& (userLogged.isAdminOnWorkArea(wModel.getWorkAreaId().toString())))) {

				ModelSelectedObject wAreaSelectedObject = new ModelSelectedObject();
				wAreaSelectedObject.setModel(wModel);
				wAreaSelectedObject.setAccessAllowed(true);

				if (userDTO.getUser().getWorkAreas().contains(wModel)) {

					getItemsRight().add(wAreaSelectedObject);

				} else if (!getItemsRight().contains(wModel)) {

					getItemsLeft().add(wAreaSelectedObject);

				}
			}
		}
		for (WorkAreaModel wModel : userDTO.getUser().getWorkAreas()) {
			ModelSelectedObject wAreaSelectedObject = new ModelSelectedObject();
			wAreaSelectedObject.setModel(wModel);
			wAreaSelectedObject.setAccessAllowed(false);
			if (!getItemsRight().contains(wAreaSelectedObject)) {
				getItemsRight().add(wAreaSelectedObject);
			}
		}
		return userDTO;
	}

	/**
	 * load available roles.
	 * 
	 * @param userDTO
	 * @return
	 */
	public UserDTO loadAvailableRoles(UserDTO userDTO) {

		getRoleItemsRight().clear();
		getRoleItemsLeft().clear();
		getRoleRowStateMapRight().clear();
		getRoleRowStateMapLeft().clear();
		for (RoleModel wModel : userDTO.getUserLogged().getManagedRoles()) {
			ModelSelectedObject roleSelectedObject = new ModelSelectedObject();
			roleSelectedObject.setModel(wModel);
			roleSelectedObject.setAccessAllowed(true);
			if (userDTO.getUser().getRoles().contains(wModel)) {
				getRoleItemsRight().add(roleSelectedObject);
			} else {
				getRoleItemsLeft().add(roleSelectedObject);
			}
		}
		for (RoleModel wModel : userDTO.getUser().getRoles()) {
			ModelSelectedObject roleSelectedObject = new ModelSelectedObject();
			roleSelectedObject.setModel(wModel);
			roleSelectedObject.setAccessAllowed(false);
			if (!getRoleItemsRight().contains(roleSelectedObject)) {
				getRoleItemsRight().add(roleSelectedObject);
			}
		}
		return userDTO;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	@Override
	public String specificCancelModificationMessage() {
		return getMessage("action.user.reload");
	}

	/**
	 * make validation on kUMSUserId.
	 * 
	 * @param context
	 * @param validate
	 * @param value
	 */
	public void validateKUMSUserId(FacesContext context, UIComponent validate, Object value) {
		String kUMSUserId = (String) value;
		UserModel userModel = service.getUserByKUMSId(kUMSUserId);

		if (userModel != null) {
			((UIInput) validate).setValid(false);
			FacesMessage msg = new FacesMessage(getMessage("kUMSUserId.validation.message"));
			context.addMessage(validate.getClientId(context), msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.user.search");
	}
}
