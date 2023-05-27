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

import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentValueManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.CostAttributeCategoryManager;
import vwg.vw.km.application.implementation.CostAttributeManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.ElementVersionUsersManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.CostCategory;
import vwg.vw.km.integration.persistence.dao.ComponentVersionDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.CostAttributeCategoryModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.ElementVersionUsersModel;
import vwg.vw.km.integration.persistence.model.HourlyRateCatalogModel;
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
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.74 $ $Date: 2020/06/12 16:11:07 $
 */
public class ComponentVersionManagerImpl extends DBManagerImpl<ComponentVersionModel, ComponentVersionDAO>
		implements ComponentVersionManager {
	private final Log log = LogManager.get().getLog(ComponentVersionManagerImpl.class);

	private CostAttributeManager costAttributeManager;

	private CostAttributeCategoryManager costAttributeCategoryManager;

	private ComponentValueManager componentValueManager;

	private ComponentElementManager componentElementManager;

	private ElementVersionManager elementVersionManager;

	private ElementVersionUsersManager elementVersionUsersManager;

	public void setCostAttributeManager(CostAttributeManager costAttributeManager) {
		this.costAttributeManager = costAttributeManager;
	}

	public void setCostAttributeCategoryManager(CostAttributeCategoryManager costAttributeCategoryManager) {
		this.costAttributeCategoryManager = costAttributeCategoryManager;
	}

	public void setComponentValueManager(ComponentValueManager componentValueManager) {
		this.componentValueManager = componentValueManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	public void setElementVersionUsersManager(ElementVersionUsersManager elementVersionUsersManager) {
		this.elementVersionUsersManager = elementVersionUsersManager;
	}

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	public Serializable getObjectPK(ComponentVersionModel model) {
		return model.getComponentVersionId();
	}

	/**
	 * @see vwg.vw.km.application.implementation.impl.base.DBManagerImpl#beforeSave(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	protected ComponentVersionModel beforeSave(ComponentVersionModel baseModel) {
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
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#saveWithCheckModifications(ComponentVersionModel,ComponentStandModel)
	 */
	@Override
	public void saveWithCheckModifications(ComponentVersionModel version, ComponentStandModel stand) {
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
			ComponentStandChangeManagerImpl.get().generateChangeEntry("E - Preisstand Datum", oldElectricDateValue,
					newElectricDateValue, stand, user, Boolean.FALSE, ChangeType.E_DATE_VALUE, null, null, null);
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
			ComponentStandChangeManagerImpl.get().generateChangeEntry("E - Preisstand Bemerkung",
					oldElectricNoticeValue, newElectricNoticeValue, stand, user, Boolean.TRUE,
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
			ComponentStandChangeManagerImpl.get().generateChangeEntry("M - Preisstand Datum", oldMecanicDateValue,
					newMecanicDateValue, stand, user, Boolean.FALSE, ChangeType.M_DATE_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMecanicPPPLDate(version.getMecanicPurchasedPartPriceLevelDate());
		}
		String oldMecanicNoticeValue = (version.getOldMecanicPPPLNotice() != null) ? version.getOldMecanicPPPLNotice()
				: "";
		String newMecanicNoticeValue = (version.getMecanicPurchasedPartPriceLevelNotice() != null)
				? version.getMecanicPurchasedPartPriceLevelNotice()
				: "";
		if (!oldMecanicNoticeValue.equals(newMecanicNoticeValue)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry("M - Preisstand Bemerkung", oldMecanicNoticeValue,
					newMecanicNoticeValue, stand, user, Boolean.TRUE, ChangeType.M_NOTICE_VALUE, null, null, null);
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
			ComponentStandChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Mechanik Konstruktion",
					oldMecanicConstructionValue, newMecanicConstructionValue, stand, user, Boolean.TRUE,
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
			ComponentStandChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Mechanik Ausführung",
					oldMechanicalExecutionValue, newMechanicalExecutionValue, stand, user, Boolean.TRUE,
					ChangeType.MECANIC_EXECUTION_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldMechanicalExecution(version.getMechanicalExecution());
		}
		String oldElectricValue = (version.getOldElectric() != null) ? version.getOldElectric() : "";
		String newElectricValue = (version.getElectric() != null) ? version.getElectric() : "";
		if (!oldElectricValue.equals(newElectricValue)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry("Leistungsinhalt Elektrik", oldElectricValue,
					newElectricValue, stand, user, Boolean.TRUE, ChangeType.ELECTRIC_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldElectric(version.getElectric());
		}
		// Comment
		String oldCommentValue = (version.getOldComment() != null) ? version.getOldComment() : "";
		String newCommentValue = (version.getComment() != null) ? version.getComment() : "";
		if (!oldCommentValue.equals(newCommentValue)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry("Bemerkung zur Freigabe", oldCommentValue,
					newCommentValue, stand, user, Boolean.TRUE, ChangeType.COMMENT_VALUE, null, null, null);
			// set the old value to the new one
			version.setOldComment(version.getComment());
		}
		// bBeistellungenValid
		Boolean oldWithProvisionValue = version.getOldWithProvision();
		Boolean newithProvisionValue = version.getWithProvision();
		if ((oldWithProvisionValue == null && newithProvisionValue != null)
				|| (oldWithProvisionValue != null && !oldWithProvisionValue.equals(newithProvisionValue))) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry("Beistellung AG",
					(oldWithProvisionValue != null && oldWithProvisionValue) ? "1" : "0",
					(newithProvisionValue != null && newithProvisionValue) ? "1" : "0", stand, user, Boolean.TRUE,
					ChangeType.WITH_PROVISION, null, null, null);
			// set the old value to the new one
			version.setOldWithProvision(version.getWithProvision());
		}

		// 28.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		// Kommentar
		String oldDescription = (version.getOldDescription() != null) ? version.getOldDescription() : "";
		String newDescription = (version.getDescription() != null) ? version.getDescription() : "";
		if (!oldDescription.equals(newDescription)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_DESCRIPTION.value(),
					oldDescription, newDescription, stand, user, Boolean.TRUE, ChangeType.DESCRIPTION, null, null,
					null);
			// set the old value to the new one
			version.setOldDescription(version.getDescription());
		}

		// Katalognummer
		String oldCatalogNumber = (version.getOldCatalogNumber() != null) ? version.getOldCatalogNumber() : "";
		String newCatalogNumber = (version.getCatalogNumber() != null) ? version.getCatalogNumber() : "";
		if (!oldCatalogNumber.equals(newCatalogNumber)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_CATALOG_NUMBER.value(),
					oldCatalogNumber, newCatalogNumber, stand, user, Boolean.TRUE, ChangeType.CATALOG_NUMBER, null,
					null, null);
			// set the old value to the new one
			version.setOldCatalogNumber(version.getCatalogNumber());
		}

		// Objekttyp
		String oldComponentType = (version.getOldEnumComponentType() != null)
				? version.getOldEnumComponentType().getComponentType()
				: "";
		String newComponentType = (version.getEnumComponentType() != null)
				? version.getEnumComponentType().getComponentType()
				: "";
		if (!oldComponentType.equals(newComponentType)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_CATEGORY.value(),
					oldComponentType, newComponentType, stand, user, Boolean.TRUE, ChangeType.COMPONENT_CATEGORY, null,
					null, null);
			// set the old value to the new one
			version.setOldEnumComponentType(version.getEnumComponentType());
		}

		// Kostengruppe

		String oldCostgroup = (version.getOldCostgroup() != null) ? version.getOldCostgroup().toString() : "";
		String newCostgroup = (version.getCostgroup() != null) ? version.getCostgroup().toString() : "";
		if (!oldCostgroup.equals(newCostgroup)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COST_GROUP.value(), oldCostgroup,
					newCostgroup, stand, user, Boolean.TRUE, ChangeType.COST_GROUP, null, null, null);
			// set the old value to the new one
			version.setOldCostgroup(version.getCostgroup());
		}

		// vorschaubild inhalt
		byte[] oldImagePreviewContent = (version.getOldImagePreviewContent() != null)
				? version.getOldImagePreviewContent()
				: null;
		byte[] newImagePreviewContent = (version.getImagePreviewContent() != null) ? version.getImagePreviewContent()
				: null;

		if ((oldImagePreviewContent == null && newImagePreviewContent != null)) {

			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.IMAGE_PREVIEW.value(), "", "Ja",
					stand, user, Boolean.TRUE, ChangeType.IMAGE_PREVIEW, null, null, null);

			// set the old value to the new one
			version.setOldImagePreviewContent(version.getImagePreviewContent());
		}

		if ((oldImagePreviewContent != null && newImagePreviewContent == null)) {

			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.IMAGE_PREVIEW.value(), "Ja", "",
					stand, user, Boolean.TRUE, ChangeType.IMAGE_PREVIEW, null, null, null);

			// set the old value to the new one
			version.setOldImagePreviewContent(version.getImagePreviewContent());
		}

		// Hersteller
		String oldManufacturer = (version.getOldManufacturer() != null) ? version.getOldManufacturer() : "";
		String newManufacturer = (version.getManufacturer() != null) ? version.getManufacturer() : "";
		if (!oldManufacturer.equals(newManufacturer)) {
			ComponentStandChangeManagerImpl.get().generateChangeEntry(ChangeTypeValue.COMPONENT_MANUFACTURER.value(),
					oldManufacturer, newManufacturer, stand, user, Boolean.TRUE, ChangeType.MANUFACTURER, null, null,
					null);
			// set the old value to the new one
			version.setOldManufacturer(version.getManufacturer());

		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#getLastComponentVersionByComponentId(Long)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionByComponentId(Long componentId) {
		return dao.getLastComponentVersionByComponentId(componentId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#getComponentVersionToBeTraitedByBatch()
	 */
	@Override
	public List<ComponentVersionModel> getComponentVersionToBeTraitedByBatch() {
		return dao.getComponentVersionToBeTraitedByBatch();
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#getLastComponentVersionInUse(Long)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionInUse(Long componentId) {
		return dao.getLastComponentVersionInUse(componentId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#getComponentVersionAttachedIds(Long)
	 */
	@Override
	public List<Long> getComponentVersionAttachedIds(Long componentId) {
		return dao.getComponentVersionAttachedIds(componentId);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#loadDependencies(ComponentStandModel)
	 */
	@Override
	public void loadDependencies(ComponentStandModel componentStand) {
		if (log.isDebugEnabled()) {
			log.debug("Dependencies Loading for ComponentVersion ..." + componentStand.getComponentVersion());
		}
		loadComponentValuesForView(componentStand.getComponentVersion());
		loadCostAttributeCategoriesObjects(componentStand.getComponentVersion());
		loadComponentElementList(componentStand);
	}

	private void loadComponentValuesForView(ComponentVersionModel componentVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Load ComponentValues In ComponentVersion..." + componentVersion);
		}
		List<ComponentValueModel> componentValues = new ArrayList<ComponentValueModel>();
		// 1- load element values from DB by element version
		List<ComponentValueModel> componentValuesAffected = componentValueManager
				.getComponentValuesByComponentVersionId(componentVersion.getComponentVersionId());
		if (componentValuesAffected != null && !componentValuesAffected.isEmpty()) {
			componentValues.addAll(componentValuesAffected);
		}
		// 2- get cost attributes used for the element version
		List<CostAttributeModel> usedCostAttributes = new ArrayList<CostAttributeModel>();
		for (ComponentValueModel componentValue : componentValues) {
			HourlyRateModel rateModel = componentVersion.getCatalogModel()
					.getHourlyRateForCostAttribute(componentValue.getCostAttribute());
			componentValue.setRateModel(rateModel);
			usedCostAttributes.add(componentValue.getCostAttribute());
		}
		// 3- load all cost attributes to complete previews list
		List<CostAttributeModel> costAttributes = costAttributeManager.getCostAttributes();
		for (CostAttributeModel costAttribute : costAttributes) {
			if (!usedCostAttributes.contains(costAttribute)) {
				ComponentValueModel componentValue = new ComponentValueModel();
				componentValue.setCostAttribute(costAttribute);
				HourlyRateModel rateModel = componentVersion.getCatalogModel()
						.getHourlyRateForCostAttribute(costAttribute);
				componentValue.setRateModel(rateModel);
				componentValues.add(componentValue);
			}
		}
		componentVersion.setComponentValuesForView(componentValues);
	}

	private void loadCostAttributeCategoriesObjects(ComponentVersionModel componentVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Load CostAttributeCategories In ComponentVersion..." + componentVersion);
		}
		componentVersion.setElectricalCostAttributeCategories(
				costAttributeCategoryManager.getCostAttributeCategoriesByCategory(CostCategory.ELECTRICAL.value()));
		componentVersion.setMecanicsCostAttributeCategories(
				costAttributeCategoryManager.getCostAttributeCategoriesByCategory(CostCategory.MECHANICS.value()));
	}

	private void loadComponentElementList(ComponentStandModel componentStand) {
		List<ComponentElementModel> list = new ArrayList<ComponentElementModel>();
		if (componentStand != null && componentStand.getId() != null) {
			if (log.isDebugEnabled()) {
				log.debug("Load  ComponentElements In ComponentStand..." + componentStand);
			}
			list = componentElementManager.getComponentElementListByComponentStandId(componentStand.getId());
			for (ComponentElementModel cElementModel : list) {
				// load element version in element component
				loadElementVersionInElementComponent(cElementModel,
						componentStand.getComponentVersion().getCatalogModel(),
						componentStand.getComponentVersion().getStatusElementPublish(), false);
			}
		}
		componentStand.getComponentVersion().setAvailableComponentElementList(list);
	}

	/**
	 * 
	 * @param cElementModel
	 * @param statusElementPublish
	 * @return
	 */
	private ElementVersionModel loadElementVersion(ComponentElementModel cElementModel, Boolean statusElementPublish) {
		BrandModel brand = cElementModel.getComponentStand().getComponentVersion().getComponent().getOwner();
		ElementVersionModel elementVersion = null;
		if (!cElementModel.getElementVersion().getElement().getOwner().equals(brand)) {
			ElementVersionUsersModel elementVersionUsers = elementVersionUsersManager
					.getElementUsers(cElementModel.getElementVersion().getElement().getElementId(), brand.getBrandId());
			if (elementVersionUsers != null) {
				return elementVersionUsers.getElementVersion();
			}
		}
		if (statusElementPublish != null && statusElementPublish) {
			elementVersion = elementVersionManager.getLastElementVersionByStatus(
					cElementModel.getElementVersion().getElement().getElementId(),
					EnumStatusManager.Status.APPROVED.value());
		}
		if (elementVersion == null) {
			elementVersion = elementVersionManager.getLastElementVersionByStatus(
					cElementModel.getElementVersion().getElement().getElementId(),
					EnumStatusManager.Status.IN_USE.value());
		}

		if (elementVersion == null) {
			elementVersion = cElementModel.getElementVersion();
		}
		return elementVersion;
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#loadElementVersionInElementComponent(ComponentElementModel,
	 *      HourlyRateCatalogModel, Boolean, Boolean)
	 */
	@Override
	public void loadElementVersionInElementComponent(ComponentElementModel cElementModel,
			HourlyRateCatalogModel catalogModel, Boolean statusElementPublish, Boolean loadLastElementVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Load  ElementVersion In ElementComponent..." + cElementModel);
		}
		ElementVersionModel elementVersion = null;
		if (loadLastElementVersion) {

			elementVersion = loadElementVersion(cElementModel, statusElementPublish);
			if (elementVersion != null) {
				elementVersion.setCatalogModel(catalogModel);
				elementVersionManager.loadDependenciesAndCalculate(elementVersion);
			}

		} else {
			elementVersion = cElementModel.getElementVersion();
		}
		// set element version in component element
		cElementModel.setElementVersion(setCatalogAndCalculate(elementVersion, catalogModel));
	}

	private ElementVersionModel setCatalogAndCalculate(ElementVersionModel elementVersion,
			HourlyRateCatalogModel catalogModel) {
		if (elementVersion != null) {
			elementVersion.setCatalogModel(catalogModel);
			elementVersionManager.loadDependenciesAndCalculate(elementVersion);
		}
		return elementVersion;
	}

	private void calculateComponentValues(ComponentVersionModel componentVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Calculate ComponentValues ..." + componentVersion);
		}
		BaseBigDecimal mechanicFactor = null;
		BaseBigDecimal electricFactor = null;
		List<ComponentValueModel> componentValues = componentVersion.getComponentValuesForView();

		// In this iterate we will get the value of the kaufteil factor (M: id 19 and E:
		// id 3.83)
		for (ComponentValueModel componentValue : componentValues) {
			HourlyRateModel rateModel = componentValue.getRateModel();
			if (rateModel != null) {
				CostAttributeModel costAttribute = rateModel.getCostAttribute();
				if (costAttribute.getCostAttributeId().equals(Long.valueOf(19))) {
					mechanicFactor = rateModel.getFactor();
				}
				if (costAttribute.getCostAttributeId().equals(Long.valueOf(38))) {
					electricFactor = rateModel.getFactor();
				}
			}
		}

		for (ComponentValueModel componentValue : componentValues) {

			addSumAttachedElements(componentValue, componentVersion, componentValue.getCostAttribute());

			CostAttributeModel costAttribute = componentValue.getCostAttribute();
			// use the value of the factor depends on category of the cost attribute
			BaseBigDecimal factor = mechanicFactor;
			if (CostCategory.ELECTRICAL.value()
					.equals(costAttribute.getCostAttributeCategory().getEnumInvestmentPart().getCategory())) {
				factor = electricFactor;
			}
			// the cost attribute that will be calculated using the factor values are:
			// 1. depends from factor and NOT depend from provision
			// 2. depends from factor AND from provision AND the calculation with provision
			// flag is false
			if ((costAttribute.getDependOnFactor() && !costAttribute.getDependFromProvision())
					|| (costAttribute.getDependOnFactor() && costAttribute.getDependFromProvision()
							&& !componentVersion.getWithProvision())) {
				if (componentValue.getTotalSumAmount() != null) {
					BaseBigDecimal totalAmount = componentValue.getTotalSumAmount().multiply(factor)
							.multiply(new BaseBigDecimal("0.01"));
					componentValue.setTotalAmount(totalAmount);
				} else {
					componentValue.setTotalAmount(null);
				}
			}
			// regular calculation for cost attribute that:
			// 1. NOT depends from factor
			// 2. depends from factor AND from provision AND the calculation with provision
			// flag is true
			if (componentValue.getCostAttribute().getPricePerUnit()) {
				if (componentValue.getTotalSumAmount() != null) {
					HourlyRateModel rateModel = componentValue.getRateModel();

					if ((!costAttribute.getDependOnFactor()) || (costAttribute.getDependOnFactor()
							&& costAttribute.getDependFromProvision() && componentVersion.getWithProvision())) {
						BaseBigDecimal totalAmount = componentValue.getTotalSumAmount().multiply(rateModel.getValue());
						componentValue.setTotalAmount(totalAmount);
					}

				} else {
					componentValue.setTotalAmount(null);
				}
			} else {
				if (!costAttribute.getDependOnFactor() || (costAttribute.getDependOnFactor()
						&& costAttribute.getDependFromProvision() && componentVersion.getWithProvision())) {
					componentValue.setTotalAmount(componentValue.getTotalSumAmount());
				}

			}
			if (log.isDebugEnabled()) {
				log.debug("componentVersionn : TotalAmount for CostAttribute "
						+ componentValue.getCostAttribute().getDesignation() + " is "
						+ componentValue.getTotalAmount());
			}

		}
	}

	private void addSumAttachedElements(ComponentValueModel componentValue, ComponentVersionModel cmpVer,
			CostAttributeModel costAttribute) {
		componentValue.setTotalSumAmount(null);
		componentValue.setTotalElemAmount(null);
		// add sum of attached elements
		for (ComponentElementModel cmpElt : cmpVer.getAvailableComponentElementList()) {
			ElementValueModel eltValue = cmpElt.getElementVersion().getElementValueByCostAttribute(costAttribute);
			Long elementUnits = cmpElt.getNumber();
			if (eltValue != null) {
				BaseBigDecimal valueElement;
				if (eltValue.getCostAttribute().getPricePerUnit()) {
					valueElement = eltValue.getNumber();
				} else {
					valueElement = eltValue.getAmount();
				}
				if (valueElement != null) {
					valueElement = valueElement.multiply(elementUnits);
					if (componentValue.getTotalElemAmount() != null) {
						componentValue.setTotalElemAmount(componentValue.getTotalElemAmount().add(valueElement));
					} else {
						componentValue.setTotalElemAmount(valueElement);
					}
				}

			}
		}
		BaseBigDecimal componentAmount = new BaseBigDecimal("0");
		if (componentValue.getCostAttribute().getPricePerUnit()) {
			if (componentValue.getNumber() != null) {
				componentAmount = componentValue.getNumber();
			}
		} else {
			if (componentValue.getAmount() != null) {
				componentAmount = componentValue.getAmount();
			}
		}

		if (componentValue.getTotalElemAmount() != null) {
			componentValue.setTotalSumAmount(componentAmount.add(componentValue.getTotalElemAmount()));
		} else {
			componentValue.setTotalSumAmount(componentAmount.equals(new BaseBigDecimal("0")) ? null : componentAmount);
		}
	}

	private void calculateComponentElementList(ComponentVersionModel componentVersion) {
		if (componentVersion != null && componentVersion.getAvailableComponentElementList() != null) {
			if (log.isDebugEnabled()) {
				log.debug("Calculate ComponentElements ..." + componentVersion);
			}
			for (ComponentElementModel cElementModel : componentVersion.getAvailableComponentElementList()) {
				// set element version in component element
				BaseBigDecimal elementSum = (cElementModel.getElementVersion() != null)
						? cElementModel.getElementVersion().getTotal()
						: new BaseBigDecimal("0");
				BaseBigDecimal elementCount = (cElementModel.getNumber() != null)
						? new BaseBigDecimal(cElementModel.getNumber().toString())
						: new BaseBigDecimal("0");
				cElementModel.setTotalAmount(elementSum.multiply(elementCount));
				if (log.isDebugEnabled()) {
					log.debug("componentVersionn : TotalAmount for ComponentElement by element"
							+ cElementModel.getElementVersion().getElement().getElementId() + " is "
							+ cElementModel.getTotalAmount());
				}
			}
		}
	}

	private void calculateCategories(ComponentVersionModel componentVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Calculate categories ..." + componentVersion);
		}
		List<ComponentValueModel> componentValues = componentVersion.getComponentValuesForView();
		calculateXCostAttributeCategories(componentVersion, componentValues, CostCategory.ELECTRICAL.value());
		calculateXCostAttributeCategories(componentVersion, componentValues, CostCategory.MECHANICS.value());
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#calculate(ComponentVersionModel)
	 */
	@Override
	public void calculate(ComponentVersionModel componentVersion) {
		if (log.isDebugEnabled()) {
			log.debug("Calculate ComponentVersion...(Catalog)=(" + componentVersion.getCatalogModel() + ")");
		}
		calculateComponentValues(componentVersion);
		calculateComponentElementList(componentVersion);
		calculateCategories(componentVersion);
	}

	private void calculateXCostAttributeCategories(ComponentVersionModel componentVersion,
			List<ComponentValueModel> componentValues, String category) {
		List<CostAttributeCategoryModel> xList = new ArrayList<CostAttributeCategoryModel>();
		if (CostCategory.ELECTRICAL.value().equals(category)) {
			if (log.isDebugEnabled()) {
				log.debug("Calculate ELECTRICAL CostAttributeCategories...");
			}
			xList = componentVersion.getElectricalCostAttributeCategories();
		} else if (CostCategory.MECHANICS.value().equals(category)) {
			if (log.isDebugEnabled()) {
				log.debug("Calculate MECHANICS CostAttributeCategories...");
			}
			xList = componentVersion.getMecanicsCostAttributeCategories();
		}
		BaseBigDecimal xSum = new BaseBigDecimal("0");
		BaseBigDecimal xProvisionSum = new BaseBigDecimal("0");
		BaseBigDecimal xComponentSum = new BaseBigDecimal("0");
		BaseBigDecimal xComponentProvisionSum = new BaseBigDecimal("0");
		BaseBigDecimal xComponentElementSum = new BaseBigDecimal("0");
		BaseBigDecimal xComponentElementProvisionSum = new BaseBigDecimal("0");

		if (xList != null && !xList.isEmpty() && componentValues != null) {
			for (CostAttributeCategoryModel x : xList) {
				BaseBigDecimal xCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xComponentCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xComponentElementCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xProvisionCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xProvisionComponentCategorySum = new BaseBigDecimal("0");
				BaseBigDecimal xProvisionComponentElementCategorySum = new BaseBigDecimal("0");
				for (ComponentValueModel componentValue : componentValues) {
					if (x.equals(componentValue.getCostAttribute().getCostAttributeCategory())) {
						if (componentValue.getTotalAmount() != null) {
							if (componentValue.getCostAttribute().getDependFromProvision()) {
								xProvisionCategorySum = xProvisionCategorySum.add(componentValue.getTotalAmount());
							} else {
								xCategorySum = xCategorySum.add(componentValue.getTotalAmount());
							}
						}
					}
				}
				// 1-get electrical category and provision sum of all associated elements
				BaseBigDecimal[] categortAndProvisionSum = componentVersion
						.getTotalAndProvisionFromComponentElementsByCategory(x);
				// 2-add electrical category and provision sum of all associated elements
				xComponentCategorySum = xComponentCategorySum.add(xCategorySum);
				xProvisionComponentCategorySum = xProvisionComponentCategorySum.add(xProvisionCategorySum);
				xComponentElementCategorySum = xComponentElementCategorySum.add(categortAndProvisionSum[0]);
				xProvisionComponentElementCategorySum = xProvisionComponentElementCategorySum
						.add(categortAndProvisionSum[1]);
				// xCategorySum = xCategorySum.add(categortAndProvisionSum[0]);
				// xProvisionCategorySum =
				// xProvisionCategorySum.add(categortAndProvisionSum[1]);
				// check if version without provision no provision amount
				if (!componentVersion.getWithProvision()) {
					xCategorySum = xCategorySum.add(xProvisionCategorySum);
					xProvisionCategorySum = new BaseBigDecimal("0");
					xComponentCategorySum = xComponentCategorySum.add(xProvisionComponentCategorySum);
					xProvisionComponentCategorySum = new BaseBigDecimal("0");
					xComponentElementCategorySum = xComponentElementCategorySum
							.add(xProvisionComponentElementCategorySum);
					xProvisionComponentElementCategorySum = new BaseBigDecimal("0");
				}
				x.setAmount(xCategorySum);
				x.setProvisionAmount(xProvisionCategorySum);
				// 1- 3 calculate the sum for electrical category
				xSum = xSum.add(xCategorySum);
				xProvisionSum = xProvisionSum.add(xProvisionCategorySum);
				xComponentSum = xComponentSum.add(xComponentCategorySum);
				xComponentProvisionSum = xComponentProvisionSum.add(xProvisionComponentCategorySum);
				xComponentElementSum = xComponentElementSum.add(xComponentElementCategorySum);
				xComponentElementProvisionSum = xComponentElementProvisionSum
						.add(xProvisionComponentElementCategorySum);

			}
		}
		if (CostCategory.ELECTRICAL.value().equals(category)) {
			componentVersion.setElectricalCostAttributeCategories(xList);
			// componentVersion.setElectricalSum(xSum);
			componentVersion.setElectricalSum(xComponentSum);
			componentVersion.setElectricalProvisionSum(xProvisionSum);
			componentVersion.setElectricalComponentSum(xComponentSum);
			componentVersion.setElectricalComponentProvisionSum(xComponentProvisionSum);
			componentVersion.setElectricalComponentElementSum(xComponentElementSum);
			componentVersion.setElectricalComponentElementProvisionSum(xComponentElementProvisionSum);

		} else if (CostCategory.MECHANICS.value().equals(category)) {
			componentVersion.setMecanicsCostAttributeCategories(xList);
			// componentVersion.setMecanicsSum(xSum);
			componentVersion.setMecanicsSum(xComponentSum);
			componentVersion.setMecanicsProvisionSum(xProvisionSum);
			componentVersion.setMecanicsComponentSum(xComponentSum);
			componentVersion.setMecanicsComponentProvisionSum(xComponentProvisionSum);
			componentVersion.setMecanicsComponentElementSum(xComponentElementSum);
			componentVersion.setMecanicsComponentElementProvisionSum(xComponentElementProvisionSum);
		}

	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#getLastComponentVersionBeforeDate(Long,
	 *      BaseDateTime)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionBeforeDate(Long componentId, BaseDateTime beforeDate) {
		return dao.getLastComponentVersionBeforeDate(componentId, beforeDate);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#isTheLastComponentVersion(Long, Long, Long)
	 */
	@Override
	public boolean isTheLastComponentVersion(Long componentVersionId, Long componentId, Long statusId) {
		return dao.isTheLastComponentVersion(componentVersionId, componentId, statusId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager#
	 * getLastComponentVersionsBeforeVersion(java.lang. Long, java.lang.Long)
	 */
	@Override
	public List<ComponentVersionModel> getLastComponentVersionsBeforeVersion(Long componentId, Long versionId) {
		return dao.getLastComponentVersionsBeforeVersion(componentId, versionId);
	}

	@Override
	public ComponentVersionModel getNextComponentVersion(Long componentVersionId, Long componentId) {
		return dao.getNextComponentVersion(componentVersionId, componentId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager# getAllComponentsVersions()
	 */
	@Override
	public List<Object[]> getAllComponentsVersions() {
		return dao.getAllComponentsVersions();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.application.implementation.ComponentVersionManager# getComponentsCountByVersion()
	 */
	@Override
	public List<Object[]> getComponentsCountByVersion() {
		return dao.getComponentsCountByVersion();
	}

	@Override
	public List<ComponentVersionModel> getComponentsVersionsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {
		return dao.getComponentsVersionsBySearchString(listOfFolders, loggedUser, searchString);
	}
}
