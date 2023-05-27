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
import java.util.List;

import vwg.vw.km.application.implementation.CostAttributeCategoryManager;
import vwg.vw.km.application.implementation.CostAttributeManager;
import vwg.vw.km.application.implementation.ElementValueManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.CostCategory;
import vwg.vw.km.integration.persistence.dao.ElementVersionDAO;
import vwg.vw.km.integration.persistence.model.CostAttributeCategoryModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;
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
 * @author Sebri Zouhaier changed by $Author: abidh $
 * @version $Revision: 1.53 $ $Date: 2020/01/20 14:13:47 $
 */
public class ElementVersionManagerImpl extends DBManagerImpl<ElementVersionModel, ElementVersionDAO>
		implements ElementVersionManager {
	private final Log log = LogManager.get().getLog(ElementVersionManagerImpl.class);

	private CostAttributeManager costAttributeManager;

	private CostAttributeCategoryManager costAttributeCategoryManager;

	private ElementValueManager elementValueManager;

	public void setCostAttributeManager(CostAttributeManager costAttributeManager) {
		this.costAttributeManager = costAttributeManager;
	}

	public void setCostAttributeCategoryManager(CostAttributeCategoryManager costAttributeCategoryManager) {
		this.costAttributeCategoryManager = costAttributeCategoryManager;
	}

	public void setElementValueManager(ElementValueManager elementValueManager) {
		this.elementValueManager = elementValueManager;
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	public Serializable getObjectPK(ElementVersionModel model) {
		return model.getElementVersionId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected ElementVersionModel beforeSave(ElementVersionModel baseModel) {
		// TODO Auto-generated method stub
		baseModel = super.beforeSave(baseModel);
		if (!baseModel.isLoaded()) {
			baseModel.setCreationDate(BaseDateTime.getCurrentDateTime());
		} else {
			baseModel.setModificationDate(BaseDateTime.getCurrentDateTime());
		}
		return baseModel;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#saveWithCheckModifications(ElementVersionModel)
	 */
	@Override
	public void saveWithCheckModifications(ElementVersionModel version) {
		saveObject(version);
		UserModel user = (version.getModifier() != null) ? version.getModifier() : version.getCreator();
		// E - Preisstand
		String oldElectricDateValue = (version.getOldElectricPurchasedPartPriceLevelDate() != null)
				? version.getOldElectricPurchasedPartPriceLevelDate().getDateSeparatedPt()
				: "";
		String newElectricDateValue = (version.getElectricPurchasedPartPriceLevelDate() != null)
				? version.getElectricPurchasedPartPriceLevelDate().getDateSeparatedPt()
				: "";
		if (!oldElectricDateValue.equals(newElectricDateValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("E - Preisstand Datum", oldElectricDateValue,
					newElectricDateValue, version, user, Boolean.FALSE, ChangeType.E_DATE_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldElectricPurchasedPartPriceLevelDate(version.getElectricPurchasedPartPriceLevelDate());
		}
		String oldElectricNoticeValue = (version.getOldElectricPPPLNotice() != null)
				? version.getOldElectricPPPLNotice()
				: "";
		String newElectricNoticeValue = (version.getElectricPurchasedPartPriceLevelNotice() != null)
				? version.getElectricPurchasedPartPriceLevelNotice()
				: "";
		if (!oldElectricNoticeValue.equals(newElectricNoticeValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("E - Preisstand Bemerkung",
					oldElectricNoticeValue, newElectricNoticeValue, version, user, Boolean.TRUE,
					ChangeType.E_NOTICE_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldElectricPPPLNotice(version.getElectricPurchasedPartPriceLevelNotice());
		}
		// M - Preisstand
		String oldMecanicDateValue = (version.getOldMecanicPPPLDate() != null)
				? version.getOldMecanicPPPLDate().getDateSeparatedPt()
				: "";
		String newMecanicDateValue = (version.getMecanicPurchasedPartPriceLevelDate() != null)
				? version.getMecanicPurchasedPartPriceLevelDate().getDateSeparatedPt()
				: "";
		if (!oldMecanicDateValue.equals(newMecanicDateValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("M - Preisstand Datum", oldMecanicDateValue,
					newMecanicDateValue, version, user, Boolean.FALSE, ChangeType.M_DATE_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMecanicPPPLDate(version.getMecanicPurchasedPartPriceLevelDate());
		}
		String oldMecanicNoticeValue = (version.getOldMecanicPPPLNotice() != null) ? version.getOldMecanicPPPLNotice()
				: "";
		String newMecanicNoticeValue = (version.getMecanicPurchasedPartPriceLevelNotice() != null)
				? version.getMecanicPurchasedPartPriceLevelNotice()
				: "";
		if (!oldMecanicNoticeValue.equals(newMecanicNoticeValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("M - Preisstand Bemerkung", oldMecanicNoticeValue,
					newMecanicNoticeValue, version, user, Boolean.TRUE, ChangeType.M_NOTICE_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMecanicPPPLNotice(version.getMecanicPurchasedPartPriceLevelNotice());
		}
		// Leistungsinhalt
		String oldMecanicConstructionValue = (version.getOldMechanicalConstruction() != null)
				? version.getOldMechanicalConstruction()
				: "";
		String newMecanicConstructionValue = (version.getMechanicalConstruction() != null)
				? version.getMechanicalConstruction()
				: "";
		if (!oldMecanicConstructionValue.equals(newMecanicConstructionValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Mechanik Konstruktion",
					oldMecanicConstructionValue, newMecanicConstructionValue, version, user, Boolean.TRUE,
					ChangeType.MECANIC_CONSTRUCTION_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMechanicalConstruction(version.getMechanicalConstruction());
		}
		String oldMechanicalExecutionValue = (version.getOldMechanicalExecution() != null)
				? version.getOldMechanicalExecution()
				: "";
		String newMechanicalExecutionValue = (version.getMechanicalExecution() != null)
				? version.getMechanicalExecution()
				: "";
		if (!oldMechanicalExecutionValue.equals(newMechanicalExecutionValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Mechanik Ausführung",
					oldMechanicalExecutionValue, newMechanicalExecutionValue, version, user, Boolean.TRUE,
					ChangeType.MECANIC_EXECUTION_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMechanicalExecution(version.getMechanicalExecution());
		}
		String oldElectricValue = (version.getOldElectric() != null) ? version.getOldElectric() : "";
		String newElectricValue = (version.getElectric() != null) ? version.getElectric() : "";
		if (!oldElectricValue.equals(newElectricValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Elektrik", oldElectricValue,
					newElectricValue, version, user, Boolean.TRUE, ChangeType.ELECTRIC_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldElectric(version.getElectric());
		}
		// Comment
		String oldCommentValue = (version.getOldComment() != null) ? version.getOldComment() : "";
		String newCommentValue = (version.getComment() != null) ? version.getComment() : "";
		if (!oldCommentValue.equals(newCommentValue)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry("Bemerkung zur Freigabe", oldCommentValue,
					newCommentValue, version, user, Boolean.TRUE, ChangeType.COMMENT_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldComment(version.getComment());
		}
		// 27.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		// Kommentar
		String oldDescription = (version.getOldDescription() != null) ? version.getOldDescription() : "";
		String newDescription = (version.getDescription() != null) ? version.getDescription() : "";
		if (!oldDescription.equals(newDescription)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_DESCRIPTION.value(),
					oldDescription, newDescription, version, user, Boolean.TRUE, ChangeType.DESCRIPTION, null, null,
					null);
			// set the old value to the new one
			version.setOldDescription(version.getDescription());
		}

		// Katalognummer
		String oldCatalogNumber = (version.getOldCatalogNumber() != null) ? version.getOldCatalogNumber() : "";
		String newCatalogNumber = (version.getCatalogNumber() != null) ? version.getCatalogNumber() : "";
		if (!oldCatalogNumber.equals(newCatalogNumber)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_CATALOG_NUMBER.value(),
					oldCatalogNumber, newCatalogNumber, version, user, Boolean.TRUE, ChangeType.CATALOG_NUMBER, null,
					null, null);
			// set the old value to the new one
			version.setOldCatalogNumber(version.getCatalogNumber());
		}

		// Kategorie
		String oldCategory = (version.getOldElementCategory() != null)
				? version.getOldElementCategory().getDesignation()
				: "";
		String newCategory = (version.getElementCategory() != null) ? version.getElementCategory().getDesignation()
				: "";
		if (!oldCategory.equals(newCategory)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_CATEGORY.value(),
					oldCategory, newCategory, version, user, Boolean.TRUE, ChangeType.ELEMENT_CATEGORY, null, null,
					null);
			// set the old value to the new one
			version.setOldElementCategory(version.getElementCategory());
		}

		// Hersteller
		String oldManufacturer = (version.getOldManufacturer() != null) ? version.getOldManufacturer() : "";
		String newManufacturer = (version.getManufacturer() != null) ? version.getManufacturer() : "";
		if (!oldManufacturer.equals(newManufacturer)) {
			ElementVersionChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.ELEMENT_MANUFACTURER.value(),
					oldManufacturer, newManufacturer, version, user, Boolean.TRUE, ChangeType.MANUFACTURER, null, null,
					null);
			// set the old value to the new one
			version.setOldManufacturer(version.getManufacturer());
		}

	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getLastElementVersionByElementId(Long)
	 */
	@Override
	public ElementVersionModel getLastElementVersionByElementId(Long elementId) {
		return dao.getLastElementVersionByElementId(elementId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getElementVersionToBeTraitedByBatch()
	 */
	@Override
	public List<ElementVersionModel> getElementVersionToBeTraitedByBatch() {
		return dao.getElementVersionToBeTraitedByBatch();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getLastElementVersionByStatus(Long, Long)
	 */
	@Override
	public ElementVersionModel getLastElementVersionByStatus(Long elementId, Long statusId) {
		return dao.getLastElementVersionByStatus(elementId, statusId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getElementVersionAttachedIds(Long)
	 */
	@Override
	public List<Long> getElementVersionAttachedIds(Long elementId) {
		return dao.getElementVersionAttachedIds(elementId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#loadDependencies(ElementVersionModel)
	 */
	@Override
	public void loadDependencies(ElementVersionModel elementVersion) {
		loadElementValuesForView(elementVersion);
		loadCostAttributeCategoriesObjects(elementVersion);
	}

	private void loadElementValuesForView(ElementVersionModel elementVersion) {

		List<ElementValueModel> elementValues = new ArrayList<ElementValueModel>();
		// 1- load element values from DB by element version
		List<ElementValueModel> elementValuesAffected = elementValueManager
				.getElementValuesByElementVersionId(elementVersion.getElementVersionId());
		if (elementValuesAffected != null && !elementValuesAffected.isEmpty()) {
			elementValues.addAll(elementValuesAffected);
		}
		// 2- get cost attributes used for the element version
		List<CostAttributeModel> usedCostAttributes = new ArrayList<CostAttributeModel>();
		for (ElementValueModel elementValue : elementValues) {
			HourlyRateModel rateModel = elementVersion.getCatalogModel()
					.getHourlyRateForCostAttribute(elementValue.getCostAttribute());
			elementValue.setRateModel(rateModel);
			usedCostAttributes.add(elementValue.getCostAttribute());
		}
		// 3- load all cost attributes to complete previews list
		List<CostAttributeModel> costAttributes = costAttributeManager.getCostAttributes();
		for (CostAttributeModel costAttribute : costAttributes) {
			if (!usedCostAttributes.contains(costAttribute)) {
				ElementValueModel elementValue = new ElementValueModel();
				elementValue.setCostAttribute(costAttribute);
				HourlyRateModel rateModel = elementVersion.getCatalogModel()
						.getHourlyRateForCostAttribute(costAttribute);
				elementValue.setRateModel(rateModel);
				elementValues.add(elementValue);
			}
		}
		elementVersion.setElementValuesForView(elementValues);
	}

	private void loadCostAttributeCategoriesObjects(ElementVersionModel elementVersion) {
		elementVersion.setElectricalCostAttributeCategories(
				costAttributeCategoryManager.getCostAttributeCategoriesByCategory(CostCategory.ELECTRICAL.value()));
		elementVersion.setMecanicsCostAttributeCategories(
				costAttributeCategoryManager.getCostAttributeCategoriesByCategory(CostCategory.MECHANICS.value()));
	}

	private void calculateElementValues(ElementVersionModel elementVersion) {
		List<ElementValueModel> elementValues = elementVersion.getElementValuesForView();
		for (ElementValueModel elementValue : elementValues) {
			if (elementValue.getCostAttribute().getPricePerUnit()) {
				// we could have not null + costAttribute.wert is null for the old katalog
				if (elementValue.getNumber() != null) {
					HourlyRateModel rateModel = elementValue.getRateModel();

					if (rateModel.getCostAttribute().getCostAttributeId() == 19
							|| rateModel.getCostAttribute().getCostAttributeId() == 38) {
						// totalAmmount is a percentage
						BaseBigDecimal totalAmount = elementValue.getNumber().multiply(rateModel.getFactor())
								.multiply(new BaseBigDecimal("0.01"));
						elementValue.setTotalAmount(totalAmount);
					} else {
						BaseBigDecimal totalAmount = elementValue.getNumber().multiply(rateModel.getValue());
						elementValue.setTotalAmount(totalAmount);
					}

				} else {
					elementValue.setTotalAmount(null);
				}
			} else {
				elementValue.setTotalAmount(elementValue.getAmount());
			}
		}
	}

	private void calculateCategories(ElementVersionModel elementVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Calculate categories for elementVersion: " + elementVersion);
		}
		List<ElementValueModel> elementValues = elementVersion.getElementValuesForView();
		calculateXCostAttributeCategories(elementVersion, elementValues, CostCategory.ELECTRICAL.value());
		calculateXCostAttributeCategories(elementVersion, elementValues, CostCategory.MECHANICS.value());
	}

	private void calculateXCostAttributeCategories(ElementVersionModel elementVersion,
			List<ElementValueModel> elementValues, String category) {
		List<CostAttributeCategoryModel> xList = new ArrayList<CostAttributeCategoryModel>();
		if (CostCategory.ELECTRICAL.value().equals(category)) {
			if (log.isDebugEnabled()) {
				log.debug("Calculate ELECTRICAL CostAttributeCategories...");
			}
			xList = elementVersion.getElectricalCostAttributeCategories();
		} else if (CostCategory.MECHANICS.value().equals(category)) {
			if (log.isDebugEnabled()) {
				log.debug("Calculate MECHANICS CostAttributeCategories...");
			}
			xList = elementVersion.getMecanicsCostAttributeCategories();
		}
		BaseBigDecimal xSum = new BaseBigDecimal("0");
		BaseBigDecimal xProvisionSum = new BaseBigDecimal("0");
		if (xList != null && !xList.isEmpty() && elementValues != null) {
			for (CostAttributeCategoryModel x : xList) {
				BaseBigDecimal xCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xProvisionCategorySum = new BaseBigDecimal("0");
				for (ElementValueModel elementValue : elementValues) {
					if (x.equals(elementValue.getCostAttribute().getCostAttributeCategory())) {
						if (elementValue.getTotalAmount() != null) {
							// if (elementValue.getCostAttribute().getDependFromProvision()) {
							// xProvisionCategorySum = xProvisionCategorySum.add(elementValue.getTotalAmount());
							// }
							// else {
							xCategorySum = xCategorySum.add(elementValue.getTotalAmount());
							// }
						}
					}
				}
				// check if version without provision no provision amount
				// if (!elementVersion.getWithProvision()) {
				// xCategorySum = xCategorySum.add(xProvisionCategorySum);
				// xProvisionCategorySum = new BaseBigDecimal("0");
				// }
				x.setAmount(xCategorySum);
				x.setProvisionAmount(xProvisionCategorySum);
				// 1- 3 calculate the sum for electrical category
				xSum = xSum.add(xCategorySum);
				xProvisionSum = xProvisionSum.add(xProvisionCategorySum);
			}
		}
		if (CostCategory.ELECTRICAL.value().equals(category)) {
			elementVersion.setElectricalCostAttributeCategories(xList);
			elementVersion.setElectricalSum(xSum);
			elementVersion.setElectricalProvisionSum(xProvisionSum);
		} else if (CostCategory.MECHANICS.value().equals(category)) {
			elementVersion.setMecanicsCostAttributeCategories(xList);
			elementVersion.setMecanicsSum(xSum);
			elementVersion.setMecanicsProvisionSum(xProvisionSum);
		}

	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#calculate(ElementVersionModel)
	 */
	@Override
	public void calculate(ElementVersionModel elementVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Calculate ElementVersion...(Catalog)=(" + elementVersion.getCatalogModel() + ")");
		}
		calculateElementValues(elementVersion);
		calculateCategories(elementVersion);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#loadDependenciesAndCalculate(ElementVersionModel)
	 */
	@Override
	public void loadDependenciesAndCalculate(ElementVersionModel elementVersion) {
		loadDependencies(elementVersion);
		calculate(elementVersion);

	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.ElementVersionManager#isTheLastElementVersion(Long, Long, long)
	 */
	@Override
	public boolean isTheLastElementVersion(Long elementVersionId, Long elementId, Long statusId) {
		return dao.isTheLastElementVersion(elementVersionId, elementId, statusId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getLastElementVersionInUse(java.lang.Long,
	 * java.lang.Boolean)
	 */
	@Override
	public ElementVersionModel getLastElementVersionInUse(Long elementId, Boolean statusElementPublish) {
		ElementVersionModel elementVersion = getLastElementVersionByStatus(elementId,
				EnumStatusManager.Status.IN_USE.value());

		if (elementVersion == null) {
			elementVersion = getLastElementVersionByStatus(elementId, EnumStatusManager.Status.APPROVED.value());
		}
		return elementVersion;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ElementVersionManager#getElementVersionsByStatus(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ElementVersionModel> getElementVersionsByStatus(Long elementId, Long statusId) {
		return dao.getElementVersionsByStatus(elementId, statusId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionStandManager#getAllElementsVersions()
	 */
	@Override
	public List<Object[]> getAllElementsVersions() {
		return dao.getAllElementsVersions();
	}

	@Override
	public List<ElementVersionModel> getElementsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {
		return dao.getElementsBySearchString(listOfFolders, loggedUser, searchString);
	}

}
