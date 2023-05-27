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
package vwg.vw.km.presentation.client.settings;

import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.icefaces.ace.model.table.RowStateMap;

import vwg.vw.km.application.service.dto.UserSettingsDTO;
import vwg.vw.km.application.service.logic.UserSettingsService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
import vwg.vw.km.integration.persistence.model.UserModel;
import vwg.vw.km.presentation.client.base.BaseBean;
import vwg.vw.km.presentation.client.navigation.node.Node;
import vwg.vw.km.presentation.client.util.ModelSelectedObject;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.32 $ $Date: 2016/11/01 17:30:07 $
 */
public class UserDefinitionSettingsBean extends BaseBean<UserSettingsDTO, UserSettingsService> {

	private static final long serialVersionUID = 1071825446152272105L;

	private final Log log = LogManager.get().getLog(UserDefinitionSettingsBean.class);

	private SelectItem[] hourlyRateCatalogs;

	private SelectItem[] brandsItems;

	private List<ModelSelectedObject> componentUserBrandLeft = new ArrayList<ModelSelectedObject>();

	private List<ModelSelectedObject> componentUserBrandRight = new ArrayList<ModelSelectedObject>();

	private RowStateMap componentUserBrandRowStateMapLeft = new RowStateMap();

	private RowStateMap componentUserBrandRowStateMapRight = new RowStateMap();

	private List<ModelSelectedObject> componentOwnerBrandLeft = new ArrayList<ModelSelectedObject>();

	private List<ModelSelectedObject> componentOwnerBrandRight = new ArrayList<ModelSelectedObject>();

	private RowStateMap componentOwnerBrandRowStateMapLeft = new RowStateMap();

	private RowStateMap componentOwnerBrandRowStateMapRight = new RowStateMap();

	private List<ModelSelectedObject> elementUserBrandLeft = new ArrayList<ModelSelectedObject>();

	private List<ModelSelectedObject> elementUserBrandRight = new ArrayList<ModelSelectedObject>();

	private RowStateMap elementUserBrandRowStateMapLeft = new RowStateMap();

	private RowStateMap elementUserBrandRowStateMapRight = new RowStateMap();

	private List<ModelSelectedObject> elementOwnerBrandLeft = new ArrayList<ModelSelectedObject>();

	private List<ModelSelectedObject> elementOwnerBrandRight = new ArrayList<ModelSelectedObject>();

	private RowStateMap elementOwnerBrandRowStateMapLeft = new RowStateMap();

	private RowStateMap elementOwnerBrandRowStateMapRight = new RowStateMap();

	public SelectItem[] getHourlyRateCatalogs() {
		return hourlyRateCatalogs;
	}

	public void setHourlyRateCatalogs(SelectItem[] hourlyRateCatalogs) {
		this.hourlyRateCatalogs = hourlyRateCatalogs;
	}

	public SelectItem[] getBrandsItems() {
		return brandsItems;
	}

	public void setBrandsItems(SelectItem[] brandsItems) {
		this.brandsItems = brandsItems;
	}

	public List<ModelSelectedObject> getComponentUserBrandLeft() {
		return componentUserBrandLeft;
	}

	public void setComponentUserBrandLeft(List<ModelSelectedObject> componentUserBrandLeft) {
		this.componentUserBrandLeft = componentUserBrandLeft;
	}

	public List<ModelSelectedObject> getComponentUserBrandRight() {
		return componentUserBrandRight;
	}

	public void setComponentUserBrandRight(List<ModelSelectedObject> componentUserBrandRight) {
		this.componentUserBrandRight = componentUserBrandRight;
	}

	public List<ModelSelectedObject> getComponentOwnerBrandLeft() {
		return componentOwnerBrandLeft;
	}

	public void setComponentOwnerBrandLeft(List<ModelSelectedObject> componentOwnerBrandLeft) {
		this.componentOwnerBrandLeft = componentOwnerBrandLeft;
	}

	public List<ModelSelectedObject> getComponentOwnerBrandRight() {
		return componentOwnerBrandRight;
	}

	public void setComponentOwnerBrandRight(List<ModelSelectedObject> componentOwnerBrandRight) {
		this.componentOwnerBrandRight = componentOwnerBrandRight;
	}

	public List<ModelSelectedObject> getElementUserBrandLeft() {
		return elementUserBrandLeft;
	}

	public void setElementUserBrandLeft(List<ModelSelectedObject> elementUserBrandLeft) {
		this.elementUserBrandLeft = elementUserBrandLeft;
	}

	public List<ModelSelectedObject> getElementUserBrandRight() {
		return elementUserBrandRight;
	}

	public void setElementUserBrandRight(List<ModelSelectedObject> elementUserBrandRight) {
		this.elementUserBrandRight = elementUserBrandRight;
	}

	public List<ModelSelectedObject> getElementOwnerBrandLeft() {
		return elementOwnerBrandLeft;
	}

	public void setElementOwnerBrandLeft(List<ModelSelectedObject> elementOwnerBrandLeft) {
		this.elementOwnerBrandLeft = elementOwnerBrandLeft;
	}

	public List<ModelSelectedObject> getElementOwnerBrandRight() {
		return elementOwnerBrandRight;
	}

	public void setElementOwnerBrandRight(List<ModelSelectedObject> elementOwnerBrandRight) {
		this.elementOwnerBrandRight = elementOwnerBrandRight;
	}

	public RowStateMap getComponentUserBrandRowStateMapLeft() {
		return componentUserBrandRowStateMapLeft;
	}

	public void setComponentUserBrandRowStateMapLeft(RowStateMap componentUserBrandRowStateMapLeft) {
		this.componentUserBrandRowStateMapLeft = componentUserBrandRowStateMapLeft;
	}

	public RowStateMap getComponentUserBrandRowStateMapRight() {
		return componentUserBrandRowStateMapRight;
	}

	public void setComponentUserBrandRowStateMapRight(RowStateMap componentUserBrandRowStateMapRight) {
		this.componentUserBrandRowStateMapRight = componentUserBrandRowStateMapRight;
	}

	public RowStateMap getComponentOwnerBrandRowStateMapLeft() {
		return componentOwnerBrandRowStateMapLeft;
	}

	public void setComponentOwnerBrandRowStateMapLeft(RowStateMap componentOwnerBrandRowStateMapLeft) {
		this.componentOwnerBrandRowStateMapLeft = componentOwnerBrandRowStateMapLeft;
	}

	public RowStateMap getComponentOwnerBrandRowStateMapRight() {
		return componentOwnerBrandRowStateMapRight;
	}

	public void setComponentOwnerBrandRowStateMapRight(RowStateMap componentOwnerBrandRowStateMapRight) {
		this.componentOwnerBrandRowStateMapRight = componentOwnerBrandRowStateMapRight;
	}

	public RowStateMap getElementUserBrandRowStateMapLeft() {
		return elementUserBrandRowStateMapLeft;
	}

	public void setElementUserBrandRowStateMapLeft(RowStateMap elementUserBrandRowStateMapLeft) {
		this.elementUserBrandRowStateMapLeft = elementUserBrandRowStateMapLeft;
	}

	public RowStateMap getElementUserBrandRowStateMapRight() {
		return elementUserBrandRowStateMapRight;
	}

	public void setElementUserBrandRowStateMapRight(RowStateMap elementUserBrandRowStateMapRight) {
		this.elementUserBrandRowStateMapRight = elementUserBrandRowStateMapRight;
	}

	public RowStateMap getElementOwnerBrandRowStateMapLeft() {
		return elementOwnerBrandRowStateMapLeft;
	}

	public void setElementOwnerBrandRowStateMapLeft(RowStateMap elementOwnerBrandRowStateMapLeft) {
		this.elementOwnerBrandRowStateMapLeft = elementOwnerBrandRowStateMapLeft;
	}

	public RowStateMap getElementOwnerBrandRowStateMapRight() {
		return elementOwnerBrandRowStateMapRight;
	}

	public void setElementOwnerBrandRowStateMapRight(RowStateMap elementOwnerBrandRowStateMapRight) {
		this.elementOwnerBrandRowStateMapRight = elementOwnerBrandRowStateMapRight;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificLoadDTO()
	 */
	@Override
	public void specificLoadDTO() {
		setHourlyRateCatalogs(fillHourlyRateCatalogs());
		setBrandsItems(fillBrandItems());
		if (!dto.dtoInited) {
			loadAvailableToSelectBrands(dto);
			dto.dtoInited = true;
		}
	}

	/**
	 * prepare hourlyRateCatalog drop down
	 * 
	 * @param event
	 * @return SelectItem[]
	 */
	private SelectItem[] fillHourlyRateCatalogs() {
		List<HourlyRateCatalogModel> hourlyRateCatalogList = dto.getHourlyRateCatalogs();
		SelectItem[] items = null;
		if (hourlyRateCatalogList != null) {
			items = new SelectItem[1 + hourlyRateCatalogList.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (hourlyRateCatalogList != null) {
			for (int i = 0; i < hourlyRateCatalogList.size(); i++) {
				HourlyRateCatalogModel catalog = hourlyRateCatalogList.get(i);
				items[i + 1] = new SelectItem(catalog.getHourlyRateCatalogId(), catalog.getDesignation());
			}
		}
		return items;
	}

	/**
	 * prepare Marke drop down
	 * 
	 * @param event
	 * @return SelectItem[]
	 */
	private SelectItem[] fillBrandItems() {
		List<BrandModel> brandList = dto.getBrands();
		SelectItem[] items = null;
		if (brandList != null) {
			items = new SelectItem[1 + brandList.size()];
		} else {
			items = new SelectItem[1];
		}
		SelectItem emptyItem = new SelectItem();
		emptyItem.setLabel(getMessage("common.message.select"));
		items[0] = emptyItem;
		if (brandList != null) {
			for (int i = 0; i < brandList.size(); i++) {
				BrandModel brand = brandList.get(i);
				items[i + 1] = new SelectItem(brand.getBrandId(), brand.getBrandId());
			}
		}
		return items;
	}

	/**
	 * load available Component User Brands.
	 */
	public UserSettingsDTO loadAvailableToSelectBrands(UserSettingsDTO userSettingsDTO) {
		// clear owner element
		getElementOwnerBrandRight().clear();
		getElementOwnerBrandLeft().clear();
		getElementOwnerBrandRowStateMapRight().clear();
		getElementOwnerBrandRowStateMapLeft().clear();
		// clear user element
		getElementUserBrandRight().clear();
		getElementUserBrandLeft().clear();
		getElementUserBrandRowStateMapRight().clear();
		getElementUserBrandRowStateMapLeft().clear();
		// clear owner component
		getComponentOwnerBrandRight().clear();
		getComponentOwnerBrandLeft().clear();
		getComponentOwnerBrandRowStateMapRight().clear();
		getComponentOwnerBrandRowStateMapLeft().clear();
		// clear user component
		getComponentUserBrandRight().clear();
		getComponentUserBrandLeft().clear();
		getComponentUserBrandRowStateMapRight().clear();
		getComponentUserBrandRowStateMapLeft().clear();
		for (BrandModel brandModel : userSettingsDTO.getAllBrands()) {
			ModelSelectedObject brandSelectedObject = new ModelSelectedObject();
			brandSelectedObject.setModel(brandModel);
			brandSelectedObject.setSelected(false);
			brandSelectedObject.setAccessAllowed(true);
			if (userSettingsDTO.getUserLogged().getComponentOwnerFilter().contains(brandModel)) {
				getComponentOwnerBrandRight().add(brandSelectedObject);
			} else {
				getComponentOwnerBrandLeft().add(brandSelectedObject);
			}
			ModelSelectedObject brandSelectedObject1 = new ModelSelectedObject(brandSelectedObject);
			if (userSettingsDTO.getUserLogged().getComponentUserFilter().contains(brandModel)) {
				getComponentUserBrandRight().add(brandSelectedObject1);
			} else {
				getComponentUserBrandLeft().add(brandSelectedObject1);
			}
			ModelSelectedObject brandSelectedObject2 = new ModelSelectedObject(brandSelectedObject);
			if (userSettingsDTO.getUserLogged().getElementOwnerFilter().contains(brandModel)) {
				getElementOwnerBrandRight().add(brandSelectedObject2);
			} else {
				getElementOwnerBrandLeft().add(brandSelectedObject2);
			}
			ModelSelectedObject brandSelectedObject3 = new ModelSelectedObject(brandSelectedObject);
			if (userSettingsDTO.getUserLogged().getElementUserFilter().contains(brandModel)) {
				getElementUserBrandRight().add(brandSelectedObject3);
			} else {
				getElementUserBrandLeft().add(brandSelectedObject3);
			}
		}
		for (BrandModel brandModel : userSettingsDTO.getUserLogged().getComponentOwnerFilter()) {
			ModelSelectedObject brandSelectedObject = new ModelSelectedObject();
			brandSelectedObject.setModel(brandModel);
			brandSelectedObject.setSelected(false);
			brandSelectedObject.setAccessAllowed(false);
			if (!getComponentOwnerBrandRight().contains(brandSelectedObject)) {
				getComponentOwnerBrandRight().add(brandSelectedObject);
			}
		}
		for (BrandModel brandModel : userSettingsDTO.getUserLogged().getComponentUserFilter()) {
			ModelSelectedObject brandSelectedObject = new ModelSelectedObject();
			brandSelectedObject.setModel(brandModel);
			brandSelectedObject.setSelected(false);
			brandSelectedObject.setAccessAllowed(false);
			if (!getComponentUserBrandRight().contains(brandSelectedObject)) {
				getComponentUserBrandRight().add(brandSelectedObject);
			}
		}
		for (BrandModel brandModel : userSettingsDTO.getUserLogged().getElementOwnerFilter()) {
			ModelSelectedObject brandSelectedObject = new ModelSelectedObject();
			brandSelectedObject.setModel(brandModel);
			brandSelectedObject.setSelected(false);
			brandSelectedObject.setAccessAllowed(false);
			if (!getElementOwnerBrandRight().contains(brandSelectedObject)) {
				getElementOwnerBrandRight().add(brandSelectedObject);
			}
		}
		for (BrandModel brandModel : userSettingsDTO.getUserLogged().getElementUserFilter()) {
			ModelSelectedObject brandSelectedObject = new ModelSelectedObject();
			brandSelectedObject.setModel(brandModel);
			brandSelectedObject.setSelected(false);
			brandSelectedObject.setAccessAllowed(false);
			if (!getElementUserBrandRight().contains(brandSelectedObject)) {
				getElementUserBrandRight().add(brandSelectedObject);
			}
		}
		return userSettingsDTO;
	}

	/**
	 * save user definition settings
	 * 
	 * @param event
	 */
	public void save(ActionEvent event) {
		log.info("Try to save " + dto.getUserLogged());
		service.loadDefaultLibrary(dto);
		dto.getUserLogged().setDefaultStdSatzKatRefId(dto.getDefaultStdSatzKatRefId());
		dto.getUserLogged().setStatusElementPublish(dto.getStatusElementPublish());
		dto.getUserLogged().setDefaultBrand(dto.getDefaultBrand());
		dto.getUserLogged().setDefaultLibrary(dto.getDefaultLibrary());
		dto.getUserLogged().setImagesRootPath(dto.getImagesRootPath());
		// 28.08.2013: ZS: PTS_Requirement-22194: Add a new Mask for the user settings definition: empty folder setting
		dto.getUserLogged().setShowEmptyFolders(dto.getShowEmptyFolders());
		// 18.11.2013: ABA: PTS_Requirement-22211: Add a new Mask for the user settings definition: show changes
		// notifications page setting
		dto.getUserLogged().setDisableChangesNotifications(dto.getDisableChangesNotifications());
		String componentOwnerFilterIds = "";
		for (ModelSelectedObject roleSelectedObject : getComponentOwnerBrandRight()) {
			BrandModel brandModel = (BrandModel) roleSelectedObject.getModel();
			componentOwnerFilterIds = componentOwnerFilterIds + brandModel.getBrandId() + ";";
		}
		dto.getUserLogged().setComponentOwnerFilterIds(componentOwnerFilterIds);
		String componentUserFilterIds = "";
		for (ModelSelectedObject roleSelectedObject : getComponentUserBrandRight()) {
			BrandModel brandModel = (BrandModel) roleSelectedObject.getModel();
			componentUserFilterIds = componentUserFilterIds + brandModel.getBrandId() + ";";
		}
		dto.getUserLogged().setComponentUserFilterIds(componentUserFilterIds);
		String elementOwnerFilterIds = "";
		for (ModelSelectedObject roleSelectedObject : getElementOwnerBrandRight()) {
			BrandModel brandModel = (BrandModel) roleSelectedObject.getModel();
			elementOwnerFilterIds = elementOwnerFilterIds + brandModel.getBrandId() + ";";
		}
		dto.getUserLogged().setElementOwnerFilterIds(elementOwnerFilterIds);
		String elementUserFilterIds = "";
		for (ModelSelectedObject roleSelectedObject : getElementUserBrandRight()) {
			BrandModel brandModel = (BrandModel) roleSelectedObject.getModel();
			elementUserFilterIds = elementUserFilterIds + brandModel.getBrandId() + ";";
		}
		dto.getUserLogged().setElementUserFilterIds(elementUserFilterIds);
		service.save(dto);
		dto.getPageChanges().clear();
		dto.setShowSaveChangesPopup(Boolean.FALSE);
		UserModel user = getUserFromSession();
		user.setDefaultStdSatzKatRefId(dto.getDefaultStdSatzKatRefId());
		user.setStatusElementPublish(dto.getStatusElementPublish());
		user.setDefaultBrand(dto.getDefaultBrand());
		user.setDefaultLibrary(dto.getDefaultLibrary());
		user.setImagesRootPath(dto.getImagesRootPath());
		// 28.08.2013: ZS: PTS_Requirement-22194: Add a new Mask for the user settings definition: empty folder setting
		user.setShowEmptyFolders(dto.getShowEmptyFolders());
		// 18.11.2013: ABA: PTS_Requirement-22211: Add a new Mask for the user settings definition: show changes
		// notifications page setting
		user.setDisableChangesNotifications(dto.getDisableChangesNotifications());
		user.setComponentOwnerFilterIds(componentOwnerFilterIds);
		user.setComponentUserFilterIds(componentUserFilterIds);
		user.setElementOwnerFilterIds(elementOwnerFilterIds);
		user.setElementUserFilterIds(elementUserFilterIds);
		setAttributeInSession(UserModel.class.getName(), user);
		// TO-DO not update node if filter activated status not changed
		Node rootComponentNode = navigationTree.getRootComponentNode();
		if (rootComponentNode != null) {
			rootComponentNode.setActiveFilter(user.isActiveComponentFilter());
			rootComponentNode.setExpanded(Boolean.FALSE);
		}
		Node elementComponentNode = navigationTree.getRootElementNode();
		if (elementComponentNode != null) {
			elementComponentNode.setActiveFilter(user.isActiveElementFilter());
			elementComponentNode.setExpanded(Boolean.FALSE);
		}
		// 19.11.2013: ABA: PTS_Requirement-22211: Refresh tree for user settings definition: show changes notifications
		// page setting
		Node changeHistoryNode = navigationTree.getChangeHistoryNode();
		if (changeHistoryNode != null) {
			changeHistoryNode.setActiveFilter(user.getDisableChangesNotifications());
			changeHistoryNode.setExpanded(Boolean.FALSE);
		}
		// ZS: PTS_Problem-35155: Make possible to navigate in the tree when the user choose to save a previous change
		Node gotoNode = navigationTree.getGotoNode();
		if (gotoNode != null) {
			gotoNode.nodeClicked(event);
		}
		navigationTree.setGotoNode(null);

		navigationTree.getNavigationElementBean().setDirty(true);
		navigationTree.setDownActionDefintion(getMessage("action.user.settings.save"));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificCancelModificationMessage()
	 */
	public String specificCancelModificationMessage() {
		return getMessage("action.user.settings.reload");
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 */
	public void moveSelectedComponentOwnerBrandToRight(ActionEvent event) {
		if (componentOwnerBrandRowStateMapLeft.getSelected() != null
				&& !componentOwnerBrandRowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_component_owner_filter");
		}
		if (componentOwnerBrandRowStateMapLeft.getSelected() != null) {
			for (Object o : componentOwnerBrandRowStateMapLeft.getSelected()) {
				ModelSelectedObject brandSelectedObject = (ModelSelectedObject) o;
				getComponentOwnerBrandRight().add(brandSelectedObject);
			}
			getComponentOwnerBrandLeft().removeAll(componentOwnerBrandRowStateMapLeft.getSelected());
		}
		componentOwnerBrandRowStateMapLeft.clear();
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 */
	public void moveSelectedComponentOwnerBrandToLeft(ActionEvent event) {
		if (componentOwnerBrandRowStateMapRight.getSelected() != null
				&& !componentOwnerBrandRowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_component_owner_filter");
		}
		if (componentOwnerBrandRowStateMapRight.getSelected() != null) {
			for (Object o : componentOwnerBrandRowStateMapRight.getSelected()) {
				ModelSelectedObject brandObject = (ModelSelectedObject) o;
				getComponentOwnerBrandLeft().add(brandObject);
				getComponentOwnerBrandRight().remove(brandObject);
			}
		}
		componentOwnerBrandRowStateMapRight.clear();
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 */
	public void moveSelectedComponentUserBrandToRight(ActionEvent event) {
		if (componentUserBrandRowStateMapLeft.getSelected() != null
				&& !componentUserBrandRowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_component_user_filter");
		}
		if (componentUserBrandRowStateMapLeft.getSelected() != null) {
			for (Object o : componentUserBrandRowStateMapLeft.getSelected()) {
				ModelSelectedObject brandSelectedObject = (ModelSelectedObject) o;
				getComponentUserBrandRight().add(brandSelectedObject);
			}
			getComponentUserBrandLeft().removeAll(componentUserBrandRowStateMapLeft.getSelected());
		}
		componentUserBrandRowStateMapLeft.clear();
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 */
	public void moveSelectedComponentUserBrandToLeft(ActionEvent event) {
		if (componentUserBrandRowStateMapRight.getSelected() != null
				&& !componentUserBrandRowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_component_user_filter");
		}
		if (componentUserBrandRowStateMapRight.getSelected() != null) {
			for (Object o : componentUserBrandRowStateMapRight.getSelected()) {
				ModelSelectedObject brandObject = (ModelSelectedObject) o;
				if (brandObject.isAccessAllowed()) {
					getComponentUserBrandLeft().add(brandObject);
					getComponentUserBrandRight().remove(brandObject);
				}
			}

		}
		componentUserBrandRowStateMapRight.clear();
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 */
	public void moveSelectedElementOwnerBrandToRight(ActionEvent event) {
		if (elementOwnerBrandRowStateMapLeft.getSelected() != null
				&& !elementOwnerBrandRowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_element_owner_filter");
		}
		if (elementOwnerBrandRowStateMapLeft.getSelected() != null) {
			for (Object o : elementOwnerBrandRowStateMapLeft.getSelected()) {
				ModelSelectedObject brandSelectedObject = (ModelSelectedObject) o;
				getElementOwnerBrandRight().add(brandSelectedObject);
			}
			getElementOwnerBrandLeft().removeAll(elementOwnerBrandRowStateMapLeft.getSelected());
		}
		elementOwnerBrandRowStateMapLeft.clear();
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 */
	public void moveSelectedElementOwnerBrandToLeft(ActionEvent event) {
		if (elementOwnerBrandRowStateMapRight.getSelected() != null
				&& !elementOwnerBrandRowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_element_owner_filter");
		}
		if (elementOwnerBrandRowStateMapRight.getSelected() != null) {
			for (Object o : elementOwnerBrandRowStateMapRight.getSelected()) {
				ModelSelectedObject brandObject = (ModelSelectedObject) o;
				if (brandObject.isAccessAllowed()) {
					getElementOwnerBrandLeft().add(brandObject);
					getElementOwnerBrandRight().remove(brandObject);
				}
			}
		}
		elementOwnerBrandRowStateMapRight.clear();
	}

	/**
	 * Moves the selected item(s) in the left list to the right list.
	 */
	public void moveSelectedElementUserBrandToRight(ActionEvent event) {
		if (elementUserBrandRowStateMapLeft.getSelected() != null
				&& !elementUserBrandRowStateMapLeft.getSelected().isEmpty()) {
			dto.getPageChanges().add("add_element_user_filter");
		}
		if (elementUserBrandRowStateMapLeft.getSelected() != null) {
			for (Object o : elementUserBrandRowStateMapLeft.getSelected()) {
				ModelSelectedObject brandSelectedObject = (ModelSelectedObject) o;
				getElementUserBrandRight().add(brandSelectedObject);
			}
			getElementUserBrandLeft().removeAll(elementUserBrandRowStateMapLeft.getSelected());
		}
		elementUserBrandRowStateMapLeft.clear();
	}

	/**
	 * Moves the selected item(s) in the right list to the left list.
	 */
	public void moveSelectedElementUserBrandToLeft(ActionEvent event) {
		if (elementUserBrandRowStateMapRight.getSelected() != null
				&& !elementUserBrandRowStateMapRight.getSelected().isEmpty()) {
			dto.getPageChanges().add("remove_element_user_filter");
		}
		if (elementUserBrandRowStateMapRight.getSelected() != null) {
			for (Object o : elementUserBrandRowStateMapRight.getSelected()) {
				ModelSelectedObject brandObject = (ModelSelectedObject) o;
				getElementUserBrandLeft().add(brandObject);
				getElementUserBrandRight().remove(brandObject);
			}
		}
		elementUserBrandRowStateMapRight.clear();
	}

	/**
	 * 
	 * @param event
	 */
	public void loadDefaultImgPath(ValueChangeEvent event) {
		checkChanged(event);

		dto.setDefaultBrand((String) event.getNewValue());
		dto = service.loadDefaultImgPath(dto);

	}
}
