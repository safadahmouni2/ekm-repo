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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentStandChangeManager;
import vwg.vw.km.application.implementation.ComponentVersionStandManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.56 $ $Date: 2019/01/18 15:00:44 $
 */
public class ComponentStandChangeManagerImpl extends DBManagerImpl<ComponentStandChangeModel, ComponentStandChangeDAO>
		implements ComponentStandChangeManager {
	private final Log log = LogManager.get().getLog(ComponentStandChangeManagerImpl.class);

	private ComponentVersionStandManager componentVersionStandManager;

	private ComponentElementManager componentElementManager;

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	public Serializable getObjectPK(ComponentStandChangeModel model) {
		return model.getChangeId();
	}

	private volatile static ComponentStandChangeManagerImpl manager;

	public static ComponentStandChangeManagerImpl get() {
		if (manager == null) {
			synchronized (ComponentStandChangeManagerImpl.class) {
				if (manager == null) {
					manager = new ComponentStandChangeManagerImpl();
				}
			}
		}
		return manager;
	}

	/**
	 * Insert a new component value change
	 * 
	 * @param change
	 * @param oldValue
	 * @param newValue
	 * @param version
	 * @param user
	 * @param longValue
	 * @param type
	 * @param oldComment
	 * @param newComment
	 */
	public void generateChangeEntry(String change, String oldValue, String newValue, ComponentStandModel stand,
			UserModel user, boolean longValue, ChangeType type, String oldComment, String newComment, String unit) {
		String changeType;
		int standCount = stand.getComponentVersion().getComponent().getStandCount();
		if (standCount == 0) {
			changeType = ChangeTypeValue.NEW.value();
		} else {
			changeType = ChangeTypeValue.IN_PROCESS.value();
		}
		generateChange(changeType, change, oldValue, newValue, stand, user, longValue, type, oldComment, newComment,
				unit);
	}

	/**
	 * Insert a new change type entry
	 * 
	 * @param changeType
	 * @param oldValue
	 * @param newValue
	 * @param version
	 * @param user
	 * @param longValue
	 * @param type
	 */
	public void generateChangeTypeEntry(String changeType, String oldValue, String newValue,
			ComponentStandModel version, UserModel user, boolean longValue, ChangeType type) {
		generateChange(changeType, null, oldValue, newValue, version, user, longValue, type, null, null, null);
	}

	/**
	 * Insert a new change entry
	 * 
	 * @param changeType
	 * @param change
	 * @param oldValue
	 * @param newValue
	 * @param version
	 * @param user
	 * @param longValue
	 * @param type
	 * @param oldComment
	 * @param newComment
	 */
	public void generateChange(String changeType, String change, String oldValue, String newValue,
			ComponentStandModel version, UserModel user, boolean longValue, ChangeType type, String oldComment,
			String newComment, String unit) {
		ComponentStandChangeModel changeEntry = new ComponentStandChangeModel();
		changeEntry.setChangeType(changeType);
		changeEntry.setChange(change);
		changeEntry.setOldValue(oldValue);
		changeEntry.setNewValue(newValue);
		changeEntry.setNewComment(newComment);
		changeEntry.setOldComment(oldComment);
		changeEntry.setDate(BaseDateTime.getCurrentDateTime());
		if (user != null) {
			changeEntry.setUserFullName(user.getSigFullName());
			changeEntry.setCreatorRefId(user.getUserId());
		}
		changeEntry.setComponentStand(version);
		changeEntry.setLongValue(longValue);
		changeEntry.setUnit(unit);
		changeEntry.setType(type.value());
		changeEntry.setLoaded(false);
		saveObject(changeEntry);
	}

	private Map<String, ComponentStandChangeModel> getDiffChangesByVersion(List<ComponentStandChangeModel> allChanges) {
		Map<String, ComponentStandChangeModel> modifications = new HashMap<String, ComponentStandChangeModel>();
		if (allChanges != null) {
			Collections.reverse(allChanges);
			for (ComponentStandChangeModel changeModel : allChanges) {
				String key = changeModel.getChange();
				if (ChangeType.ADD_ELEMENT.value().equals(changeModel.getType())) {
					key = changeModel.getNewValue();
				} else if (ChangeType.REMOVE_ELEMENT.value().equals(changeModel.getType())) {
					key = changeModel.getOldValue();
				}
				if (!modifications.containsKey(key)) {
					modifications.put(key, changeModel);
				} else {
					if (ChangeType.ADD_ELEMENT.value().equals(changeModel.getType())
							|| ChangeType.REMOVE_ELEMENT.value().equals(changeModel.getType())) {
						modifications.remove(key);
					} else {
						modifications.get(key).setNewValue(changeModel.getNewValue());
						modifications.get(key).setUserFullName(changeModel.getUserFullName());
						modifications.get(key).setDate(changeModel.getDate());
						modifications.get(key).setNewComment(changeModel.getNewComment());
					}
				}

				ComponentStandChangeModel diffChangeModel = modifications.get(key);

				if (diffChangeModel != null) {
					dao.evict(diffChangeModel);
					if (((diffChangeModel.getNewValue() == null && diffChangeModel.getOldValue() == null)
							|| ((diffChangeModel.getNewValue() != null && diffChangeModel.getOldValue() != null)
									&& diffChangeModel.getNewValue().equalsIgnoreCase(diffChangeModel.getOldValue())))

							&& ((diffChangeModel.getNewComment() == null && diffChangeModel.getOldComment() == null)
									|| ((diffChangeModel.getNewComment() != null
											&& diffChangeModel.getOldComment() != null)
											&& diffChangeModel.getNewComment()
													.equalsIgnoreCase(diffChangeModel.getOldComment())))) {
						modifications.remove(key);
					}
				}
			}
		}
		return modifications;
	}

	/**
	 * 
	 * @param allChanges
	 * @param groupByType
	 * @return Set the remark field
	 */
	private void setChangesRemark(Collection<ComponentStandChangeModel> allChanges) {
		for (ComponentStandChangeModel changeModel : allChanges) {
			setChangeRemark(changeModel);
		}
	}

	private void setChangeRemark(ComponentStandChangeModel changeModel) {
		BaseBigDecimal newValue, oldValue;
		// ADD_ELEMENT
		if (ChangeType.ADD_ELEMENT.value().equals(changeModel.getType())) {
			changeModel.setRemark(changeModel.getNewValue() + " hinzugefügt");
		}
		if (ChangeType.REMOVE_ELEMENT.value().equals(changeModel.getType())) {
			changeModel.setRemark(changeModel.getOldValue() + " entfernt");
		}
		if (ChangeType.ATTRIBUTE_VALUE.value().equals(changeModel.getType())) {
			oldValue = new BaseBigDecimal(0);
			if (changeModel.getOldValue() != null && !"".equals(changeModel.getOldValue().trim())) {
				oldValue = new BaseBigDecimal(changeModel.getOldValue().replaceAll("\\.", ""));
			}
			newValue = new BaseBigDecimal(0);
			if (changeModel.getNewValue() != null && !"".equals(changeModel.getNewValue().trim())) {
				newValue = new BaseBigDecimal(changeModel.getNewValue().replaceAll("\\.", ""));
			}
			// calc diff
			BaseBigDecimal diff = newValue.subtract(oldValue);
			changeModel.setRemark("Bausteinänderung: " + ((diff.signum() == 1) ? "+" : "") + diff.getStringFormatted()
					+ " " + changeModel.getUnit());
		}
	}

	private List<ComponentStandChangeModel> groupChangesByType(Collection<ComponentStandChangeModel> modifications) {
		List<ComponentStandChangeModel> result = new ArrayList<ComponentStandChangeModel>(modifications);
		// separate list By type
		Map<String, List<ComponentStandChangeModel>> typeChanges = new HashMap<String, List<ComponentStandChangeModel>>();
		for (ComponentStandChangeModel change : result) {
			if (!typeChanges.containsKey(change.getType())) {
				List<ComponentStandChangeModel> changes = new ArrayList<ComponentStandChangeModel>();
				changes.add(change);
				typeChanges.put(change.getType(), changes);
			} else {
				typeChanges.get(change.getType()).add(change);
			}
		}
		List<ComponentStandChangeModel> finalResult = new ArrayList<ComponentStandChangeModel>();
		if (typeChanges.containsKey(ChangeType.COMMENT_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.COMMENT_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.ADD_ELEMENT.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.ADD_ELEMENT.value()));
		}
		if (typeChanges.containsKey(ChangeType.REMOVE_ELEMENT.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.REMOVE_ELEMENT.value()));
		}
		if (typeChanges.containsKey(ChangeType.ATTRIBUTE_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.ATTRIBUTE_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.M_DATE_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.M_DATE_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.M_NOTICE_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.M_NOTICE_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.E_DATE_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.E_DATE_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.E_NOTICE_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.E_NOTICE_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.MECANIC_CONSTRUCTION_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.MECANIC_CONSTRUCTION_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.MECANIC_EXECUTION_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.MECANIC_EXECUTION_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.ELECTRIC_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.ELECTRIC_VALUE.value()));
		}
		if (typeChanges.containsKey(ChangeType.WITH_PROVISION.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.WITH_PROVISION.value()));
		}

		// 28.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		if (typeChanges.containsKey(ChangeType.DESCRIPTION.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.DESCRIPTION.value()));
		}
		if (typeChanges.containsKey(ChangeType.CATALOG_NUMBER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.CATALOG_NUMBER.value()));
		}
		if (typeChanges.containsKey(ChangeType.COMPONENT_CATEGORY.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.COMPONENT_CATEGORY.value()));
		}
		
		if (typeChanges.containsKey(ChangeType.MANUFACTURER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.MANUFACTURER.value()));
		}
		//02.12.2022 SDH 
	
		if (typeChanges.containsKey(ChangeType.COST_GROUP.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.COST_GROUP.value()));
		}

		if (typeChanges.containsKey(ChangeType.IMAGE_PREVIEW.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.IMAGE_PREVIEW.value()));
		}

		if (typeChanges.containsKey(ChangeType.DESIGNATION.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.DESIGNATION.value()));
		}
		if (typeChanges.containsKey(ChangeType.NUMBER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.NUMBER.value()));
		}
		return finalResult;
	}

	public List<ComponentStandChangeModel> getAggregatedStandChanges(ComponentStandModel stand,
			List<ComponentElementModel> compElems) {

		List<ComponentStandChangeModel> modifications = new ArrayList<ComponentStandChangeModel>();
		List<ComponentStandChangeModel> allchanges = new ArrayList<ComponentStandChangeModel>();
		List<ComponentStandChangeModel> tempList = dao.getChangesByComponentStand(stand.getId());
		for (ComponentStandChangeModel changeModel : tempList) {
			if (hasChangeInValues(changeModel)) {
				modifications.add(changeModel);
			} else {
				allchanges.add(changeModel);
			}
		}
		Map<String, ComponentStandChangeModel> modificationsMap = getDiffChangesByVersion(modifications);
		setChangesRemark(modificationsMap.values());
		// get aggregated changes

		updateOrAddAggregatedChanges(stand, modificationsMap, compElems);

		// add agregated changes for detail view
		List<ComponentStandChangeModel> result = groupChangesByType(modificationsMap.values());
		allchanges.addAll(result);
		Collections.sort(allchanges, new ComponentVersionChangeComparable());
		return allchanges;
	}

	private void updateOrAddAggregatedChanges(ComponentStandModel stand,
			Map<String, ComponentStandChangeModel> modificationsMap, List<ComponentElementModel> compElems) {
		// get element changes
		List<ElementVersionChangeModel> costAttributeChanges = getElementCostAttributeChanges(stand, compElems);
		Map<Long, List<ElementVersionChangeModel>> elementsUseChanges = new HashMap<Long, List<ElementVersionChangeModel>>();
		for (ElementVersionChangeModel elementCostAttributeChange : costAttributeChanges) {
			Long versionId = elementCostAttributeChange.getElementVersion().getElementVersionId();
			if (elementsUseChanges.get(versionId) == null) {
				List<ElementVersionChangeModel> allchanges = ElementVersionChangeManagerImpl.get()
						.getChangesByVersionAndType(versionId, ChangeType.USE_STAND);
				elementsUseChanges.put(versionId, allchanges);
			}
		}
		for (ElementVersionChangeModel elementCostAttributeChange : costAttributeChanges) {
			BaseBigDecimal oldValue = new BaseBigDecimal(0);
			if (elementCostAttributeChange.getOldValue() != null
					&& !"".equals(elementCostAttributeChange.getOldValue().trim())) {
				oldValue = new BaseBigDecimal(elementCostAttributeChange.getOldValue().replaceAll("\\.", ""));
			}
			BaseBigDecimal newValue = new BaseBigDecimal(0);
			if (elementCostAttributeChange.getNewValue() != null
					&& !"".equals(elementCostAttributeChange.getNewValue().trim())) {
				newValue = new BaseBigDecimal(elementCostAttributeChange.getNewValue().replaceAll("\\.", ""));
			}
			// calc diff
			BaseBigDecimal diff = newValue.subtract(oldValue);
			ComponentStandChangeModel compChange;
			String useChangeUser = null;
			if (modificationsMap.containsKey(elementCostAttributeChange.getChange())) {
				compChange = modificationsMap.get(elementCostAttributeChange.getChange());
				if (compChange.getDate().getDate().before(elementCostAttributeChange.getDate().getDate())) {
					compChange.setDate(elementCostAttributeChange.getDate());
					compChange.setUserFullName(elementCostAttributeChange.getUserFullName());
				}
				BaseBigDecimal sumOldValue = new BaseBigDecimal(0);
				BaseBigDecimal sumNewValue = new BaseBigDecimal(0);
				if (compChange.getOldValue() != null && !"".equals(compChange.getOldValue().trim())) {
					sumOldValue = new BaseBigDecimal(compChange.getOldValue().replaceAll("\\.", ""));
					sumOldValue = sumOldValue.add(oldValue);
				}
				if (compChange.getNewValue() != null && !"".equals(compChange.getNewValue().trim())) {
					sumNewValue = new BaseBigDecimal(compChange.getNewValue().replaceAll("\\.", ""));
					sumNewValue = sumNewValue.add(newValue);
				}
				compChange.setOldValue(sumOldValue.getStringFormatted());
				compChange.setNewValue(sumNewValue.getStringFormatted());
			} else {
				compChange = new ComponentStandChangeModel();
				BeanUtils.copyProperties(elementCostAttributeChange, compChange, new String[] { "changeId", "remark" });
				compChange.setComponentStand(stand);
				String changeType;
				int standCount = stand.getComponentVersion().getComponent().getStandCount();
				if (stand.getNumber() != null) {
					if (stand.getNumber() == 1) {
						changeType = ChangeTypeValue.NEW.value();
					} else {
						changeType = ChangeTypeValue.NEW_STAND.value();
						useChangeUser = getUseChangeUser(elementCostAttributeChange, stand, elementsUseChanges);
					}
				} else {
					if (standCount == 0) {
						changeType = ChangeTypeValue.NEW.value();
					} else {
						changeType = ChangeTypeValue.IN_PROCESS.value();
						useChangeUser = getUseChangeUser(elementCostAttributeChange, stand, elementsUseChanges);
					}
				}
				compChange.setChangeType(changeType);
				if (useChangeUser != null) {
					compChange.setUserFullName(useChangeUser);
				}
				// get the value from the element stand used in the last component stand when the current
				// BS stand is using an old version of this element
				if (standCount > 1 && useChangeUser != null) {
					ComponentStandModel lastStand = componentVersionStandManager.getLastComponentStand(
							stand.getComponentVersion().getComponent().getComponentId(), stand.getId());
					if (lastStand != null) {
						List<ComponentElementModel> list = componentElementManager
								.getComponentElementListByComponentStandId(lastStand.getId());
						for (ComponentElementModel cmpElem : list) {
							ElementVersionModel lastversion = cmpElem.getElementVersion();

							if (lastversion.getElement()
									.equals(elementCostAttributeChange.getElementVersion().getElement())
									&& lastversion.getNumber() > elementCostAttributeChange.getElementVersion()
											.getNumber()) {
								String oldCostValue = null;
								List<ElementVersionChangeModel> elementVersionChanges = ElementVersionChangeManagerImpl
										.get().getChangesByVersion(lastversion.getElementVersionId(),
												ChangeType.ATTRIBUTE_VALUE);
								for (ElementVersionChangeModel elementVersionChange : elementVersionChanges) {
									if (elementVersionChange.getChange()
											.equals(elementCostAttributeChange.getChange())) {
										oldCostValue = elementVersionChange.getNewValue();
										compChange.setOldValue(oldCostValue);
										break;
									}
								}
							}
						}
					}
				}
				modificationsMap.put(elementCostAttributeChange.getChange(), compChange);
			}
			// set remark field
			StringBuilder remark = new StringBuilder();
			if (modificationsMap.get(elementCostAttributeChange.getChange()).getRemark() != null) {
				remark.append(modificationsMap.get(elementCostAttributeChange.getChange()).getRemark());
			}
			if (remark.toString().length() != 0) {
				remark.append("</br>");
			}
			String useChangeAccepted = "";
			if (useChangeUser != null) {
				useChangeAccepted = " akzeptiert";
			}
			if (!remark.toString().contains("Elementänderung:")) {
				remark.append("Elementänderung" + useChangeAccepted + ":</br>");
			}
			remark.append(elementCostAttributeChange.getElementVersion().getElement().getNumberAndDesignation());
			remark.append(" ");
			remark.append((diff.signum() == 1) ? "+" : "");
			remark.append(diff.getStringFormatted());
			modificationsMap.get(elementCostAttributeChange.getChange()).setRemark(remark.toString());
		}
	}

	/**
	 * if the user accept a new version of a used element then set the name of the user to the change and for the remark
	 * it will be Elementänderung akzeptiert
	 * 
	 * @param elementCostAttributeChange
	 * @param stand
	 * @return
	 */
	private String getUseChangeUser(ElementVersionChangeModel elementCostAttributeChange, ComponentStandModel stand,
			Map<Long, List<ElementVersionChangeModel>> elementsUseChanges) {
		String useChangeUser = null;
		List<ElementVersionChangeModel> allchanges = elementsUseChanges
				.get(elementCostAttributeChange.getElementVersion().getElementVersionId());
		for (ElementVersionChangeModel elementVersionChange : allchanges) {
			if (stand.getComponentVersion().getComponent().getOwner().getBrandId()
					.equals(elementVersionChange.getNewValue())
					&& elementAlreadyUsed(elementCostAttributeChange.getElementVersion(), stand)) {
				useChangeUser = elementVersionChange.getUserFullName();
				break;
			}
		}
		return useChangeUser;
	}

	private List<ElementVersionChangeModel> getElementCostAttributeChanges(ComponentStandModel stand,
			List<ComponentElementModel> compElems) {
		List<ElementVersionChangeModel> modifications = new ArrayList<ElementVersionChangeModel>();
		if (compElems != null && !compElems.isEmpty()) {
			for (ComponentElementModel cmpElem : compElems) {
				ElementVersionModel elementVersionModel = cmpElem.getElementVersion();
				if (elementVersionModel != null && elementVersionModel.getElementVersionId() != null
						&& elementVersionCanbeUsedInAggregation(elementVersionModel, stand)) {
					List<ElementVersionChangeModel> elementChanges = ElementVersionChangeManagerImpl.get()
							.getChangesByVersion(elementVersionModel.getElementVersionId(), ChangeType.ATTRIBUTE_VALUE);
					if (elementChanges != null && !elementChanges.isEmpty()) {
						modifications.addAll(elementChanges);
					}
				}
			}
		}
		return modifications;
	}

	private boolean elementAlreadyUsed(ElementVersionModel elementVersion, ComponentStandModel stand) {
		elementVersion.getElement().getElementVersions().size();
		for (ElementVersionModel elversion : elementVersion.getElement().getElementVersions()) {
			if (!elversion.equals(elementVersion)) {
				ComponentModel component = stand.getComponentVersion().getComponent();
				ComponentElementModel componentElementInUse = componentElementManager
						.getComponentElementByElementVersionAndComponent(elversion.getElementVersionId(),
								component.getComponentId());
				if (componentElementInUse != null) {
					return Boolean.TRUE;
				}
			}
		}
		return Boolean.FALSE;
	}

	private boolean elementVersionCanbeUsedInAggregation(ElementVersionModel elementVersionModel,
			ComponentStandModel stand) {
		int standCount = stand.getComponentVersion().getComponent().getStandCount();
		if (elementVersionModel != null) {
			if ((stand.getNumber() != null && stand.getNumber() == 1)
					|| (stand.getNumber() == null && standCount == 0)) {
				return Boolean.TRUE;
			}
			ComponentModel component = stand.getComponentVersion().getComponent();
			ComponentElementModel componentElementInUse = componentElementManager
					.getComponentElementByElementVersionAndComponent(elementVersionModel.getElementVersionId(),
							component.getComponentId());

			if (componentElementInUse.getComponentStand().getNumber() != null && ((stand.getNumber() != null
					&& componentElementInUse.getComponentStand().getNumber().compareTo(stand.getNumber()) < 0)
					|| (stand.getNumber() == null && componentElementInUse.getComponentStand().getNumber()
							.compareTo(Long.valueOf(standCount)) <= 0))) {
				return Boolean.FALSE;
			}

			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private Map<String, ElementVersionChangeModel> getAggregatedElementCostAttributeChanges(
			List<ComponentElementModel> compElems) {
		Map<String, ElementVersionChangeModel> modifications = new HashMap<String, ElementVersionChangeModel>();
		if (compElems != null && !compElems.isEmpty()) {
			for (ComponentElementModel cmpElem : compElems) {
				if (cmpElem.getElementVersion() != null && cmpElem.getElementVersion().getElementVersionId() != null) {
					List<ElementVersionChangeModel> elementChanges = ElementVersionChangeManagerImpl.get()
							.getChangesByVersion(cmpElem.getElementVersion().getElementVersionId(),
									ChangeType.ATTRIBUTE_VALUE);
					if (elementChanges != null && !elementChanges.isEmpty()) {
						for (ElementVersionChangeModel elemChange : elementChanges) {
							String changeKey = elemChange.getChange();
							if (!modifications.containsKey(changeKey)) {
								modifications.put(changeKey, elemChange);
							} else {
								// check if modification in newer
								if (modifications.get(changeKey).getDate().getDate()
										.before(elemChange.getDate().getDate())) {
									modifications.put(changeKey, elemChange);
								}
							}
						}
					}
				}
			}
		}
		return modifications;
	}

	private List<ComponentStandChangeModel> getAggregatedChanges(ComponentVersionModel componentVersionModel,
			ComponentStandModel stand) {
		// get component changes
		List<ComponentStandChangeModel> tempList = dao.getChangesByComponentStand(stand.getId());
		Map<String, ComponentStandChangeModel> modifications = getDiffChangesByVersion(tempList);
		// get element changes
		Map<String, ElementVersionChangeModel> costAttributeChanges = getAggregatedElementCostAttributeChanges(
				componentVersionModel.getAvailableComponentElementList());
		for (Map.Entry<String, ElementVersionChangeModel> entry : costAttributeChanges.entrySet()) {
			if (modifications.containsKey(entry.getKey())) {
				if (modifications.get(entry.getKey()).getDate().getDate()
						.before(entry.getValue().getDate().getDate())) {
					ComponentStandChangeModel compChange = modifications.get(entry.getKey());
					compChange.setDate(entry.getValue().getDate());
					compChange.setUserFullName(entry.getValue().getUserFullName());
				}
			} else {
				ComponentStandChangeModel compChange = new ComponentStandChangeModel();
				compChange.setChange(entry.getValue().getChange());
				compChange.setDate(entry.getValue().getDate());
				compChange.setUserFullName(entry.getValue().getUserFullName());
				modifications.put(entry.getKey(), compChange);
			}
		}
		return new ArrayList<ComponentStandChangeModel>(modifications.values());
	}

	/**
	 * @see vwg.vw.km.application.implementation.ComponentStandChangeManager#getAggregatedChangesByVersions(java.util.List)
	 */
	@Override
	public List<ComponentStandChangeModel> getAggregatedChangesByVersions(List<ComponentVersionModel> cmpVersionList) {
		Map<String, ComponentStandChangeModel> modifications = new HashMap<String, ComponentStandChangeModel>();
		for (ComponentVersionModel componentVersion : cmpVersionList) {
			List<ComponentStandModel> standList = new ArrayList<ComponentStandModel>(
					componentVersion.getComponentVersionStands());
			for (ComponentStandModel stand : standList) {
				List<ComponentStandChangeModel> cmpChanges = getAggregatedChanges(componentVersion, stand);
				for (ComponentStandChangeModel cmpChange : cmpChanges) {
					if (!modifications.containsKey(cmpChange.getChange())) {
						modifications.put(cmpChange.getChange(), cmpChange);
					}
				}
			}
		}
		return new ArrayList<ComponentStandChangeModel>(modifications.values());
	}

	@Override
	public List<ComponentStandChangeModel> getChangesByCriteria(SearchHistoryObject historyObject) {
		if (log.isInfoEnabled()) {
			log.info("Start searching Components' changes by criteria");
		}

		List<ComponentStandChangeModel> result;
		List<ComponentStandChangeModel> searchresult = new ArrayList<ComponentStandChangeModel>();
		if (historyObject.getComponentId().isEmpty()) {
			result = dao.getChangesByCriteria(historyObject);
		} else {
			result = getDiffChangesByChangeType(dao.getChangesByCriteria(historyObject));
		}
		setChangesRemark(result);
		Collections.sort(result, new ComponentVersionChangeComparable());
		if (historyObject.getComponentId().isEmpty()) {
			for (ComponentStandChangeModel changeModel : result) {
				if (!changesContain(searchresult, changeModel)) {
					searchresult.add(changeModel);
				}
			}
			setChangeUsers(searchresult);
			// remove change type USE_STAND when nutzer selected
			if (historyObject.getExchangeUnusedState() && historyObject.getUser() != null
					&& !historyObject.getUser().isEmpty()) {
				for (Iterator<ComponentStandChangeModel> it = searchresult.iterator(); it.hasNext();) {
					ComponentStandChangeModel var = it.next();
					if (ChangeTypeValue.USE_STAND.value().equals(var.getChangeType())
							&& !var.getUsers().contains(new BrandModel(historyObject.getUser()))) {
						it.remove();
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("End searching Component changes by criteria ...");
			}
			return searchresult;
		}
		if (log.isInfoEnabled()) {
			log.info("End searching Component changes by criteria ...");
		}
		return result;
	}

	private void setChangeUsers(List<ComponentStandChangeModel> allChanges) {
		if (allChanges != null && !allChanges.isEmpty()) {
			Set<Long> allChangesVersionIdsSet = new HashSet<Long>();
			for (ComponentStandChangeModel changeModel : allChanges) {
				allChangesVersionIdsSet.add(changeModel.getComponentStand().getId());
			}
			List<Long> allChangesVersionIds = new ArrayList<Long>(allChangesVersionIdsSet);
			List<ComponentStandChangeModel> useStandChanges = dao.getChangesByStandsAndChangeType(allChangesVersionIds,
					ChangeTypeValue.USE_STAND);
			Map<ComponentStandModel, List<ComponentStandChangeModel>> changesByVersionMap = getChangesByVersionMap(
					useStandChanges);
			for (ComponentStandChangeModel changeModel : allChanges) {
				List<ComponentStandChangeModel> componentStandChanges = changesByVersionMap
						.get(changeModel.getComponentStand());
				if (!CollectionUtils.isEmpty(componentStandChanges)) {
					Set<String> brands = new HashSet<String>();
					for (ComponentStandChangeModel useChangeModel : componentStandChanges) {
						if (ChangeType.USE_STAND.value().equals(useChangeModel.getType()) && (useChangeModel.getDate()
								.getDate().equals(changeModel.getDate().getDate())
								|| useChangeModel.getDate().getDate().before(changeModel.getDate().getDate()))) {
							brands.add(useChangeModel.getNewValue());
						}
					}
					for (String brandId : brands) {
						changeModel.getUsers().add(new BrandModel(brandId));
					}
				}
			}
		}
	}

	private Map<ComponentStandModel, List<ComponentStandChangeModel>> getChangesByVersionMap(
			List<ComponentStandChangeModel> allChanges) {
		Map<ComponentStandModel, List<ComponentStandChangeModel>> changesByVersionMap = new HashMap<ComponentStandModel, List<ComponentStandChangeModel>>();
		if (!CollectionUtils.isEmpty(allChanges)) {
			for (ComponentStandChangeModel changeModel : allChanges) {
				if (changesByVersionMap.containsKey(changeModel.getComponentStand())) {
					changesByVersionMap.get(changeModel.getComponentStand()).add(changeModel);
				} else {
					List<ComponentStandChangeModel> temp = new ArrayList<ComponentStandChangeModel>();
					temp.add(changeModel);
					changesByVersionMap.put(changeModel.getComponentStand(), temp);
				}
			}
		}
		return changesByVersionMap;
	}

	/**
	 * 
	 * @param result
	 * @param changeModel
	 * @return return changes for the search list filtred by changeType version number and stand number
	 */
	private boolean changesContain(List<ComponentStandChangeModel> result, ComponentStandChangeModel changeModel) {
		for (ComponentStandChangeModel model : result) {
			if (model.getChangeType().equals(changeModel.getChangeType())
					&& model.getComponentStand().getComponentVersion().getComponent().getComponentId().equals(
							changeModel.getComponentStand().getComponentVersion().getComponent().getComponentId())
					&& (model.getComponentStand().getNumber() == changeModel.getComponentStand().getNumber())
					&& (model.getComponentStand().getComponentVersion().getNumber() == model.getComponentStand()
							.getComponentVersion().getNumber())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Get the diff by change for the returned list from DB without loosing the affected Start.
	 * 
	 * @param allChanges
	 * @return
	 */
	private List<ComponentStandChangeModel> getDiffChangesByChangeType(List<ComponentStandChangeModel> allChanges) {
		int index = 1;
		Long id;
		List<ComponentStandChangeModel> list = new ArrayList<ComponentStandChangeModel>();
		List<ComponentStandChangeModel> attributChanges = new ArrayList<ComponentStandChangeModel>();
		Map<String, List<ComponentStandChangeModel>> modifications = new HashMap<String, List<ComponentStandChangeModel>>();
		Map<Long, List<ComponentStandChangeModel>> changes = new HashMap<Long, List<ComponentStandChangeModel>>();
		if (allChanges != null) {
			// get only attribut value changes
			for (ComponentStandChangeModel changeModel : allChanges) {
				if (hasChangeInValues(changeModel)) {
					attributChanges.add(changeModel);
				} else {
					list.add(changeModel);
				}
			}
			for (ComponentStandChangeModel changeModel : attributChanges) {
				id = changeModel.getComponentStand().getComponentVersion().getComponent().getComponentId();
				if (changes.containsKey(id)) {
					changes.get(id).add(changeModel);
				} else {
					changes.put(id, new ArrayList<ComponentStandChangeModel>());
					changes.get(id).add(changeModel);
				}
			}
			for (Map.Entry<Long, List<ComponentStandChangeModel>> entry : changes.entrySet()) {
				for (ComponentStandChangeModel changeModel : entry.getValue()) {
					String key = changeModel.getChangeType();
					if (!modifications.containsKey(key + index)) {
						index++;
						modifications.put(key + index, new ArrayList<ComponentStandChangeModel>());
					}
					modifications.get(key + index).add(changeModel);
				}
				for (Map.Entry<String, List<ComponentStandChangeModel>> entrym : modifications.entrySet()) {
					list.addAll(getDiffChangesByVersion(entrym.getValue()).values());
				}
				modifications.clear();
			}
		}
		return list;
	}

	/**
	 * 
	 * @param changeModel
	 * @return if this change can compute a value modification.
	 */
	private boolean hasChangeInValues(ComponentStandChangeModel changeModel) {
		String[] changeTypes = new String[] { ChangeType.ACTIVATE.value(), ChangeType.DEACTIVATE.value(),
				ChangeType.USE_STAND.value(), ChangeType.UNUSE_STAND.value() };
		return !(Arrays.asList(changeTypes).contains(changeModel.getType()));
	}

	/**
	 * ComponentVersionChange Comparator by date desc
	 */
	class ComponentVersionChangeComparable implements Comparator<ComponentStandChangeModel> {
		@Override
		public int compare(ComponentStandChangeModel o1, ComponentStandChangeModel o2) {
			return o2.getDate().getDate().compareTo(o1.getDate().getDate());
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionChangeManager#changesByVersion(Long, ChangeType,
	 *      ChangeType)
	 */
	public void updateChangeTypeForVersion(Long standId, ChangeTypeValue fromChangeType, ChangeTypeValue toChangeType) {
		if (fromChangeType != null && toChangeType != null) {
			List<ComponentStandChangeModel> componentStandChanges = dao.getChangesByStandAndChangeType(standId,
					fromChangeType);
			if (!CollectionUtils.isEmpty(componentStandChanges)) {
				for (ComponentStandChangeModel componentStandChange : componentStandChanges) {
					componentStandChange.setChangeType(toChangeType.value());
				}
				saveObjects(componentStandChanges);
			}
		}
	}

	/**
	 * 
	 * @param standId
	 * @param change
	 * @return list of ComponentStandChangeModel for given component stand id and change
	 */
	public List<ComponentStandChangeModel> getChangesByStandAndChange(Long standId, String change) {
		return dao.getChangesByStandAndChange(standId, change);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionChangeManager#anonomyzeUser(Long)
	 */
	@Override
	public void anonomyzeUser(Long userId) {
		dao.anonomyzeUser(userId);
	}

	public void setComponentVersionStandManager(ComponentVersionStandManager componentVersionStandManager) {
		this.componentVersionStandManager = componentVersionStandManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

}
