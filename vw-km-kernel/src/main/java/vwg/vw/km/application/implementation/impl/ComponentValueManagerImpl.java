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
package vwg.vw.km.application.implementation.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.BeanUtils;

import vwg.vw.km.application.implementation.ComponentValueManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.CostCategory;
import vwg.vw.km.integration.persistence.dao.ComponentValueDAO;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.UserModel;

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
 * @version $Revision: 1.22 $ $Date: 2016/02/24 15:31:33 $
 */
public class ComponentValueManagerImpl extends DBManagerImpl<ComponentValueModel, ComponentValueDAO>
		implements ComponentValueManager {

	private final static Set<Integer> TO_HIDE_ATTRIBUTES = new HashSet<Integer>() {
		private static final long serialVersionUID = 1L;
		{
			add(100);
			add(101);
			add(102);
			add(103);
			add(200);
			add(201);
			add(202);
			add(203);
		}
	};

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	public Serializable getObjectPK(ComponentValueModel model) {
		return model.getComponentValueId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentValueManager#getComponentValuesByComponentVersionId(Long)
	 */
	public List<ComponentValueModel> getComponentValuesByComponentVersionId(Long versionId) {
		return dao.getComponentValuesByComponentVersionId(versionId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentValueManager#copyComponentValues(ComponentVersionModel,
	 *      ComponentVersionModel)
	 */
	public void copyComponentValues(ComponentVersionModel from, ComponentVersionModel to) {
		List<ComponentValueModel> setComponentValueModel = getComponentValuesByComponentVersionId(
				from.getComponentVersionId());
		if (setComponentValueModel != null) {
			for (ComponentValueModel gs : setComponentValueModel) {
				ComponentValueModel cloned = new ComponentValueModel();
				BeanUtils.copyProperties(gs, cloned);
				cloned.setLoaded(false);
				cloned.setComponentVersion(to);
				cloned.setCostAttribute(gs.getCostAttribute());
				saveObject(cloned);
			}
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentValueManager#filterComponentValues(List, String)
	 */
	public List<ComponentValueModel> filterComponentValues(List<ComponentValueModel> listToFilter,
			String searchCriteria) {
		List<ComponentValueModel> filteredComponentValues = new ArrayList<ComponentValueModel>();
		List<ComponentValueModel> componentValuesCached = listToFilter;
		if (componentValuesCached != null && !componentValuesCached.isEmpty()) {
			if ("ALL".equals(searchCriteria)) {
				filteredComponentValues = new ArrayList<ComponentValueModel>(componentValuesCached);
			} else if ("ALL_EMPTY".equals(searchCriteria)) {
				for (ComponentValueModel componentValue : componentValuesCached) {
					if (componentValue.getNumber() == null && componentValue.getAmount() == null
							&& componentValue.getTotalElemAmount() == null) {
						filteredComponentValues.add(componentValue);
					}
				}
			} else if ("ALL_FILLED".equals(searchCriteria)) {
				for (ComponentValueModel componentValue : componentValuesCached) {
					if (componentValue.getNumber() != null || componentValue.getAmount() != null
							|| componentValue.getTotalElemAmount() != null) {
						filteredComponentValues.add(componentValue);
					}
				}
			} else if ("MECHANICS".equals(searchCriteria)) {
				for (ComponentValueModel componentValue : componentValuesCached) {
					CostAttributeModel tempCostAttribute = componentValue.getCostAttribute();
					if (tempCostAttribute != null && tempCostAttribute.getCostAttributeCategory() != null
							&& tempCostAttribute.getCostAttributeCategory().getEnumInvestmentPart() != null
							&& CostCategory.MECHANICS.value().equals(tempCostAttribute.getCostAttributeCategory()
									.getEnumInvestmentPart().getCategory())) {
						filteredComponentValues.add(componentValue);
					}
				}
			} else if ("ELECTRICAL".equals(searchCriteria)) {
				for (ComponentValueModel componentValue : componentValuesCached) {
					CostAttributeModel tempCostAttribute = componentValue.getCostAttribute();
					if (tempCostAttribute != null && tempCostAttribute.getCostAttributeCategory() != null
							&& tempCostAttribute.getCostAttributeCategory().getEnumInvestmentPart() != null
							&& CostCategory.ELECTRICAL.value().equals(tempCostAttribute.getCostAttributeCategory()
									.getEnumInvestmentPart().getCategory())) {
						filteredComponentValues.add(componentValue);
					}
				}
			} else if ("HIDE_PARENT_COST".equals(searchCriteria)) {
				for (ComponentValueModel componentValue : componentValuesCached) {
					CostAttributeModel tempCostAttribute = componentValue.getCostAttribute();
					if (tempCostAttribute != null && !TO_HIDE_ATTRIBUTES.contains(tempCostAttribute.getOrderNumber())) {
						filteredComponentValues.add(componentValue);
					}
				}
			}

		}
		if (!filteredComponentValues.isEmpty()) {
			Collections.sort(filteredComponentValues, new Comparator<ComponentValueModel>() {
				public int compare(ComponentValueModel o1, ComponentValueModel o2) {
					int result = 0;
					if (o1 != null && o2 != null) {
						result = o1.getCostAttribute().getOrderNumber()
								.compareTo(o2.getCostAttribute().getOrderNumber());
					}
					return result;
				}
			});
		}
		return filteredComponentValues;
	}

	protected void checkChanges(ComponentValueModel baseModel) {
		String oldValue = new BaseBigDecimal("0").getStringFormatted();
		String newValue = new BaseBigDecimal("0").getStringFormatted();
		if (baseModel.getCostAttribute().getPricePerUnit()) {
			oldValue = (baseModel.getOldNumber() != null) ? baseModel.getOldNumber().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
			newValue = (baseModel.getNumber() != null) ? baseModel.getNumber().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		} else {
			oldValue = (baseModel.getOldAmount() != null) ? baseModel.getOldAmount().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
			newValue = (baseModel.getAmount() != null) ? baseModel.getAmount().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		}

		String description = (baseModel.getDescription() != null) ? baseModel.getDescription() : "";
		String oldDescription = (baseModel.getOldDescription() != null) ? baseModel.getOldDescription() : "";

		if ((!oldValue.equals(newValue)) || (!oldDescription.equals(description))) {
			ComponentVersionModel version = baseModel.getComponentVersion();
			version.getComponentVersionStands().size();
			ComponentStandModel stand = new ArrayList<ComponentStandModel>(version.getComponentVersionStands()).get(0);
			UserModel user = (version.getModifier() != null) ? version.getModifier() : version.getCreator();

			List<ComponentStandChangeModel> changes = ComponentStandChangeManagerImpl.get()
					.getChangesByStandAndChange(stand.getId(), baseModel.getCostAttribute().getDesignation());
			if (oldDescription.equals(description) && (changes == null || changes.isEmpty())) {
				oldDescription = description;
				description = null;
			}

			ComponentStandChangeManagerImpl.get().generateChangeEntry(baseModel.getCostAttribute().getDesignation(),
					oldValue, newValue, stand, user, Boolean.FALSE, ChangeType.ATTRIBUTE_VALUE, oldDescription,
					description, baseModel.getCostAttribute().getUnit());
			// set the old value to the new one
			baseModel.setOldNumber(baseModel.getNumber());
			baseModel.setOldAmount(baseModel.getAmount());
			baseModel.setOldDescription(baseModel.getDescription());
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	protected ComponentValueModel beforeRemove(ComponentValueModel componentValue) {
		String oldValue = new BaseBigDecimal("0").getStringFormatted();
		String newValue = new BaseBigDecimal("0").getStringFormatted();
		if (componentValue.getCostAttribute().getPricePerUnit()) {
			oldValue = (componentValue.getOldNumber() != null) ? componentValue.getOldNumber().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		} else {
			oldValue = (componentValue.getOldAmount() != null) ? componentValue.getOldAmount().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		}

		String description = (componentValue.getDescription() != null) ? componentValue.getDescription() : "";
		String oldDescription = (componentValue.getOldDescription() != null) ? componentValue.getOldDescription() : "";

		if ((!oldValue.equals(newValue)) || (!oldDescription.equals(description))) {
			ComponentVersionModel version = componentValue.getComponentVersion();
			UserModel user = (version.getModifier() != null) ? version.getModifier() : version.getCreator();
			version.getComponentVersionStands().size();
			ComponentStandModel stand = new ArrayList<ComponentStandModel>(version.getComponentVersionStands()).get(0);
			ComponentStandChangeManagerImpl.get().generateChangeEntry(
					componentValue.getCostAttribute().getDesignation(), oldValue, newValue, stand, user, Boolean.FALSE,
					ChangeType.ATTRIBUTE_VALUE, oldDescription, description,
					componentValue.getCostAttribute().getUnit());
			// set the old value to the new one
			componentValue.setOldNumber(componentValue.getNumber());
			componentValue.setOldAmount(componentValue.getAmount());
		}
		return componentValue;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentValueManager#saveOrRemoveComponentValue(ComponentVersionModel)
	 */
	public void saveOrRemoveComponentValue(ComponentVersionModel version) {
		for (ComponentValueModel componentValue : version.getComponentValuesForView()) {
			if ((componentValue.getNumber() != null && !componentValue.getNumber().equals(new BaseBigDecimal("0")))
					|| (componentValue.getAmount() != null
							&& !componentValue.getAmount().equals(new BaseBigDecimal("0")))
					|| (componentValue.getDescription() != null
							&& !"".equals(componentValue.getDescription().trim()))) {
				componentValue.setComponentVersion(version);
				checkChanges(componentValue);
				saveObject(componentValue);
			} else {
				if (componentValue.isLoaded()) {
					beforeRemove(componentValue);
					removeObject(componentValue.getComponentValueId());
					componentValue.setLoaded(Boolean.FALSE);
				}
			}
		}
	}
}
