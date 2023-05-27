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

import vwg.vw.km.application.implementation.ElementValueManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.CostCategory;
import vwg.vw.km.integration.persistence.dao.ElementValueDAO;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
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
 * @author Sebri Zouhaier changed by $Author: mrada $
 * @version $Revision: 1.22 $ $Date: 2014/03/05 14:15:12 $
 */
public class ElementValueManagerImpl extends DBManagerImpl<ElementValueModel, ElementValueDAO>
		implements ElementValueManager {

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
	@Override
	public Serializable getObjectPK(ElementValueModel model) {
		return model.getElementValueId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementValueManager#getElementValuesByElementVersionId(Long)
	 */
	@Override
	public List<ElementValueModel> getElementValuesByElementVersionId(Long versionId) {
		return dao.getElementValuesByElementVersionId(versionId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementValueManager#copyElementValues(ElementVersionModel,
	 *      ElementVersionModel)
	 */
	@Override
	public void copyElementValues(ElementVersionModel from, ElementVersionModel to) {
		List<ElementValueModel> setElementValueModel = getElementValuesByElementVersionId(from.getElementVersionId());
		if (setElementValueModel != null) {
			for (ElementValueModel gs : setElementValueModel) {
				ElementValueModel cloned = new ElementValueModel();
				BeanUtils.copyProperties(gs, cloned);
				cloned.setLoaded(false);
				cloned.setElementVersion(to);
				cloned.setCostAttribute(gs.getCostAttribute());
				saveObject(cloned);
			}
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementValueManager#filterElementValues(List, String)
	 */
	@Override
	public List<ElementValueModel> filterElementValues(List<ElementValueModel> listToFilter, String searchCriteria) {
		List<ElementValueModel> filteredElementValues = new ArrayList<ElementValueModel>();
		List<ElementValueModel> elementValuesCached = listToFilter;
		if (elementValuesCached != null && !elementValuesCached.isEmpty()) {
			if ("ALL".equals(searchCriteria)) {
				filteredElementValues = new ArrayList<ElementValueModel>(elementValuesCached);
			} else if ("ALL_EMPTY".equals(searchCriteria)) {
				for (ElementValueModel elementValue : elementValuesCached) {
					if (elementValue.getNumber() == null && elementValue.getAmount() == null) {
						filteredElementValues.add(elementValue);
					}
				}
			} else if ("ALL_FILLED".equals(searchCriteria)) {
				for (ElementValueModel elementValue : elementValuesCached) {
					if (elementValue.getNumber() != null || elementValue.getAmount() != null) {
						filteredElementValues.add(elementValue);
					}
				}
			} else if ("MECHANICS".equals(searchCriteria)) {
				for (ElementValueModel elementValue : elementValuesCached) {
					CostAttributeModel tempCostAttribute = elementValue.getCostAttribute();
					if (tempCostAttribute != null && tempCostAttribute.getCostAttributeCategory() != null
							&& tempCostAttribute.getCostAttributeCategory().getEnumInvestmentPart() != null
							&& CostCategory.MECHANICS.value().equals(tempCostAttribute.getCostAttributeCategory()
									.getEnumInvestmentPart().getCategory())) {
						filteredElementValues.add(elementValue);
					}
				}
			} else if ("ELECTRICAL".equals(searchCriteria)) {
				for (ElementValueModel elementValue : elementValuesCached) {
					CostAttributeModel tempCostAttribute = elementValue.getCostAttribute();
					if (tempCostAttribute != null && tempCostAttribute.getCostAttributeCategory() != null
							&& tempCostAttribute.getCostAttributeCategory().getEnumInvestmentPart() != null
							&& CostCategory.ELECTRICAL.value().equals(tempCostAttribute.getCostAttributeCategory()
									.getEnumInvestmentPart().getCategory())) {
						filteredElementValues.add(elementValue);
					}
				}
			} else if ("HIDE_PARENT_COST".equals(searchCriteria)) {
				for (ElementValueModel elementValue : elementValuesCached) {
					CostAttributeModel tempCostAttribute = elementValue.getCostAttribute();
					if (tempCostAttribute != null && !TO_HIDE_ATTRIBUTES.contains(tempCostAttribute.getOrderNumber())) {
						filteredElementValues.add(elementValue);
					}
				}
			}

		}
		if (!filteredElementValues.isEmpty()) {
			Collections.sort(filteredElementValues, new Comparator<ElementValueModel>() {
				@Override
				public int compare(ElementValueModel o1, ElementValueModel o2) {
					int result = 0;
					if (o1 != null && o2 != null) {
						result = o1.getCostAttribute().getOrderNumber()
								.compareTo(o2.getCostAttribute().getOrderNumber());
					}
					return result;
				}
			});
		}
		return filteredElementValues;
	}

	protected void checkChanges(ElementValueModel baseModel) {
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
			ElementVersionModel version = baseModel.getElementVersion();
			UserModel user = (version.getModifier() != null) ? version.getModifier() : version.getCreator();
			List<Long> versionIds = new ArrayList<Long>();
			versionIds.add(version.getElementVersionId());
			List<ElementVersionChangeModel> changes = ElementVersionChangeManagerImpl.get()
					.getChangesByStandsAndChange(versionIds, baseModel.getCostAttribute().getDesignation());
			if (oldDescription.equals(description) && (changes == null || changes.isEmpty())) {
				oldDescription = description;
				description = null;
			}

			ElementVersionChangeManagerImpl.get().generateChangeEntry(baseModel.getCostAttribute().getDesignation(),
					oldValue, newValue, version, user, Boolean.FALSE, ChangeType.ATTRIBUTE_VALUE, oldDescription,
					description, baseModel.getCostAttribute().getUnit());
			// set the old value to the new one
			baseModel.setOldNumber(baseModel.getNumber());
			baseModel.setOldAmount(baseModel.getAmount());
			baseModel.setOldDescription(baseModel.getDescription());
		}
	}

	protected ElementValueModel beforeRemove(ElementValueModel elementValue) {
		String oldValue = new BaseBigDecimal("0").getStringFormatted();
		String newValue = new BaseBigDecimal("0").getStringFormatted();
		if (elementValue.getCostAttribute().getPricePerUnit()) {
			oldValue = (elementValue.getOldNumber() != null) ? elementValue.getOldNumber().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		} else {
			oldValue = (elementValue.getOldAmount() != null) ? elementValue.getOldAmount().getStringFormatted()
					: new BaseBigDecimal("0").getStringFormatted();
		}

		String description = (elementValue.getDescription() != null) ? elementValue.getDescription() : "";
		String oldDescription = (elementValue.getOldDescription() != null) ? elementValue.getOldDescription() : "";

		if ((!oldValue.equals(newValue)) || (!oldDescription.equals(description))) {
			ElementVersionModel version = elementValue.getElementVersion();
			UserModel user = (version.getModifier() != null) ? version.getModifier() : version.getCreator();
			ElementVersionChangeManagerImpl.get().generateChangeEntry(elementValue.getCostAttribute().getDesignation(),
					oldValue, newValue, version, user, Boolean.FALSE, ChangeType.ATTRIBUTE_VALUE, oldDescription,
					description, elementValue.getCostAttribute().getUnit());
			// set the old value to the new one
			elementValue.setOldNumber(elementValue.getNumber());
			elementValue.setOldAmount(elementValue.getAmount());
		}
		return elementValue;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementValueManager#saveOrRemoveElementValue(ElementVersionModel)
	 */
	@Override
	public void saveOrRemoveElementValue(ElementVersionModel elementVersion) {
		for (ElementValueModel elementValue : elementVersion.getElementValuesForView()) {
			if ((elementValue.getNumber() != null && !elementValue.getNumber().equals(new BaseBigDecimal("0")))
					|| (elementValue.getAmount() != null && !elementValue.getAmount().equals(new BaseBigDecimal("0")))
					|| (elementValue.getDescription() != null && !"".equals(elementValue.getDescription().trim()))) {
				elementValue.setElementVersion(elementVersion);
				checkChanges(elementValue);
				saveObject(elementValue);
			} else {
				if (elementValue.isLoaded()) {
					beforeRemove(elementValue);
					removeObject(elementValue.getElementValueId());
					elementValue.setLoaded(Boolean.FALSE);
				}
			}
		}
	}
}
