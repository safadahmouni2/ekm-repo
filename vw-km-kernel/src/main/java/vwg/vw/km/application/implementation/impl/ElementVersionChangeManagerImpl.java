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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import vwg.vw.km.application.implementation.ElementVersionChangeManager;
import vwg.vw.km.application.implementation.impl.base.DBManagerImpl;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO;
import vwg.vw.km.integration.persistence.model.BrandModel;
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
 * @version $Revision: 1.54 $ $Date: 2019/01/18 15:00:44 $
 */
public class ElementVersionChangeManagerImpl extends DBManagerImpl<ElementVersionChangeModel, ElementVersionChangeDAO>
		implements ElementVersionChangeManager {
	private final Log log = LogManager.get().getLog(ElementVersionChangeManagerImpl.class);

	/**
	 * @see vwg.vw.km.application.implementation.base.BaseManager#getObjectPK(vwg.vw.km.integration.persistence.model.base.BaseModel)
	 */
	@Override
	public Serializable getObjectPK(ElementVersionChangeModel model) {
		return model.getChangeId();
	}

	private volatile static ElementVersionChangeManagerImpl manager;

	public static ElementVersionChangeManagerImpl get() {
		if (manager == null) {
			synchronized (ElementVersionChangeManagerImpl.class) {
				if (manager == null) {
					manager = new ElementVersionChangeManagerImpl();
				}
			}
		}
		return manager;
	}

	/**
	 * Insert a new element value change
	 * 
	 * @param change
	 * @param oldValue
	 * @param newValue
	 * @param elementVersion
	 * @param user
	 * @param longValue
	 * @param type
	 * @param oldComment
	 * @param newComment
	 */
	public void generateChangeEntry(String change, String oldValue, String newValue, ElementVersionModel elementVersion,
			UserModel user, boolean longValue, ChangeType type, String oldComment, String newComment, String unit) {
		String changeType;
		int standCount = elementVersion.getElement().getElementVersions().size();
		if (standCount <= 1) {
			changeType = ChangeTypeValue.NEW.value();
		} else {
			changeType = ChangeTypeValue.IN_PROCESS.value();
		}
		generateChange(changeType, change, oldValue, newValue, elementVersion, user, longValue, type, oldComment,
				newComment, unit);
	}

	/**
	 * Insert a new change type entry
	 * 
	 * @param changeType
	 * @param oldValue
	 * @param newValue
	 * @param elementVersion
	 * @param user
	 * @param longValue
	 * @param type
	 */
	public void generateChangeTypeEntry(String changeType, String oldValue, String newValue,
			ElementVersionModel elementVersion, UserModel user, boolean longValue, ChangeType type) {
		generateChange(changeType, null, oldValue, newValue, elementVersion, user, longValue, type, null, null, null);
	}

	/**
	 * Insert a new change entry
	 * 
	 * @param changeType
	 * @param change
	 * @param oldValue
	 * @param newValue
	 * @param elementVersion
	 * @param user
	 * @param longValue
	 * @param type
	 * @param oldComment
	 * @param newComment
	 */
	public void generateChange(String changeType, String change, String oldValue, String newValue,
			ElementVersionModel elementVersion, UserModel user, boolean longValue, ChangeType type, String oldComment,
			String newComment, String unit) {
		ElementVersionChangeModel changeEntry = new ElementVersionChangeModel();
		changeEntry.setChangeType(changeType);
		changeEntry.setChange(change);
		changeEntry.setOldValue(oldValue);
		changeEntry.setNewValue(newValue);
		changeEntry.setOldComment(oldComment);
		changeEntry.setNewComment(newComment);
		changeEntry.setDate(BaseDateTime.getCurrentDateTime());
		changeEntry.setUserFullName(user.getSigFullName());
		changeEntry.setCreatorRefId(user.getUserId());
		changeEntry.setElementVersion(elementVersion);
		changeEntry.setLongValue(longValue);
		changeEntry.setType(type.value());
		changeEntry.setUnit(unit);
		saveObject(changeEntry);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionChangeManager#getChangesByVersion(Long, ChangeType)
	 */
	@Override
	public List<ElementVersionChangeModel> getChangesByVersion(Long versionId, ChangeType type) {
		List<ElementVersionChangeModel> modifications = new ArrayList<ElementVersionChangeModel>();
		List<ElementVersionChangeModel> allchanges = new ArrayList<ElementVersionChangeModel>();
		List<ElementVersionChangeModel> tempList = dao.getChangesByVersion(versionId,
				((type != null) ? type.value() : null));
		for (ElementVersionChangeModel changeModel : tempList) {
			if (hasChangeInValues(changeModel)) {
				modifications.add(changeModel);
			} else {
				allchanges.add(changeModel);
			}
		}
		List<ElementVersionChangeModel> result = getDiffChangesByVersion(modifications,
				((type != null) ? false : true));
		allchanges.addAll(result);
		setChangesRemark(allchanges);
		Collections.sort(allchanges, new ElementVersionChangeComparable());
		return allchanges;
	}

	private List<ElementVersionChangeModel> getDiffChangesByVersion(List<ElementVersionChangeModel> allChanges,
			boolean groupByType) {
		Map<String, ElementVersionChangeModel> modifications = new HashMap<String, ElementVersionChangeModel>();
		if (allChanges != null) {
			Collections.reverse(allChanges);
			for (ElementVersionChangeModel changeModel : allChanges) {
				String key = changeModel.getChange();
				if (!modifications.containsKey(key)) {
					modifications.put(key, changeModel);
				} else {
					modifications.get(key).setNewValue(changeModel.getNewValue());
					modifications.get(key).setUserFullName(changeModel.getUserFullName());
					modifications.get(key).setDate(changeModel.getDate());
					modifications.get(key).setNewComment(changeModel.getNewComment());
				}

				ElementVersionChangeModel diffChangeModel = modifications.get(key);
				dao.evict(diffChangeModel);

				if (((diffChangeModel.getNewValue() == null && diffChangeModel.getOldValue() == null)
						|| ((diffChangeModel.getNewValue() != null && diffChangeModel.getOldValue() != null)
								&& diffChangeModel.getNewValue().equalsIgnoreCase(diffChangeModel.getOldValue())))

						&& ((diffChangeModel.getNewComment() == null && diffChangeModel.getOldComment() == null)
								|| ((diffChangeModel.getNewComment() != null && diffChangeModel.getOldComment() != null)
										&& diffChangeModel.getNewComment()
												.equalsIgnoreCase(diffChangeModel.getOldComment())))) {
					modifications.remove(key);
				}
			}
		}
		List<ElementVersionChangeModel> result = new ArrayList<ElementVersionChangeModel>(modifications.values());
		if (groupByType) {
			return groupChangesByType(result);
		}
		return result;
	}

	/**
	 * 
	 * @param allChanges
	 * @param groupByType
	 * @return Set the remark field
	 */
	private void setChangesRemark(List<ElementVersionChangeModel> allChanges) {
		BaseBigDecimal oldValue, newValue;
		for (ElementVersionChangeModel changeModel : allChanges) {
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
				if (diff.signum() == 1) {
					changeModel.setRemark("+" + diff.getStringFormatted() + " " + changeModel.getUnit());
				} else {
					changeModel.setRemark(diff.getStringFormatted() + " " + changeModel.getUnit());
				}
			}
		}
	}

	private List<ElementVersionChangeModel> groupChangesByType(List<ElementVersionChangeModel> modifications) {
		List<ElementVersionChangeModel> result = new ArrayList<ElementVersionChangeModel>(modifications);
		// separate list By type
		Map<String, List<ElementVersionChangeModel>> typeChanges = new HashMap<String, List<ElementVersionChangeModel>>();
		for (ElementVersionChangeModel change : result) {
			if (!typeChanges.containsKey(change.getType())) {
				List<ElementVersionChangeModel> changes = new ArrayList<ElementVersionChangeModel>();
				changes.add(change);
				typeChanges.put(change.getType(), changes);
			} else {
				typeChanges.get(change.getType()).add(change);
			}
		}
		List<ElementVersionChangeModel> finalResult = new ArrayList<ElementVersionChangeModel>();
		if (typeChanges.containsKey(ChangeType.COMMENT_VALUE.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.COMMENT_VALUE.value()));
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

		// 27.08.2013: ZS: PTS_Requirement-22186: Addition of new fields historicizing
		if (typeChanges.containsKey(ChangeType.DESCRIPTION.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.DESCRIPTION.value()));
		}
		if (typeChanges.containsKey(ChangeType.CATALOG_NUMBER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.CATALOG_NUMBER.value()));
		}
		if (typeChanges.containsKey(ChangeType.ELEMENT_CATEGORY.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.ELEMENT_CATEGORY.value()));
		}
		if (typeChanges.containsKey(ChangeType.MANUFACTURER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.MANUFACTURER.value()));
		}
		if (typeChanges.containsKey(ChangeType.DESIGNATION.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.DESIGNATION.value()));
		}
		if (typeChanges.containsKey(ChangeType.NUMBER.value())) {
			finalResult.addAll(typeChanges.get(ChangeType.NUMBER.value()));
		}
		return finalResult;
	}

	@Override
	public List<ElementVersionChangeModel> getChangesByCriteria(SearchHistoryObject searchHistoryObject) {

		if (log.isInfoEnabled()) {
			log.info("Start searching Elements changes by criteria");
		}
		List<ElementVersionChangeModel> result;
		List<ElementVersionChangeModel> searchresult = new ArrayList<ElementVersionChangeModel>();
		if (searchHistoryObject.getElementId().isEmpty()) {
			result = dao.getChangesByCriteria(searchHistoryObject);
		} else {
			result = getDiffChangesByChangeType(dao.getChangesByCriteria(searchHistoryObject));
		}
		setChangesRemark(result);
		Collections.sort(result, new ElementVersionChangeComparable());
		if (searchHistoryObject.getElementId().isEmpty()) {
			for (ElementVersionChangeModel changeModel : result) {
				if (!changesContain(searchresult, changeModel)) {
					searchresult.add(changeModel);
				}
			}
			setChangeUsers(searchresult);
			// remove change type USE_STAND when nutzer selected
			if (searchHistoryObject.getExchangeUnusedState() && searchHistoryObject.getUser() != null
					&& !searchHistoryObject.getUser().isEmpty()) {
				for (Iterator<ElementVersionChangeModel> it = searchresult.iterator(); it.hasNext();) {
					ElementVersionChangeModel var = it.next();
					if (ChangeTypeValue.USE_STAND.value().equals(var.getChangeType())
							&& !var.getUsers().contains(new BrandModel(searchHistoryObject.getUser()))) {
						it.remove();
					}
				}
			}
			if (log.isInfoEnabled()) {
				log.info("End searching Element changes by criteria");
			}
			return searchresult;
		}
		if (log.isInfoEnabled()) {
			log.info("End searching Element changes by criteria");
		}
		return result;
	}

	private void setChangeUsers(List<ElementVersionChangeModel> allChanges) {
		if (allChanges != null && !allChanges.isEmpty()) {
			Set<Long> allChangesVersionIdsSet = new HashSet<Long>();
			for (ElementVersionChangeModel changeModel : allChanges) {
				allChangesVersionIdsSet.add(changeModel.getElementVersion().getElementVersionId());
			}
			List<Long> allChangesVersionIds = new ArrayList<Long>(allChangesVersionIdsSet);
			List<ElementVersionChangeModel> useStandChanges = dao
					.getChangesByVersionsAndChangeType(allChangesVersionIds, ChangeTypeValue.USE_STAND);
			Map<ElementVersionModel, List<ElementVersionChangeModel>> changesByVersionMap = getChangesByVersionMap(
					useStandChanges);
			for (ElementVersionChangeModel changeModel : allChanges) {
				List<ElementVersionChangeModel> elementStandChanges = changesByVersionMap
						.get(changeModel.getElementVersion());
				if (!CollectionUtils.isEmpty(elementStandChanges)) {
					Set<String> brands = new HashSet<String>();
					for (ElementVersionChangeModel useChangeModel : elementStandChanges) {
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

	private Map<ElementVersionModel, List<ElementVersionChangeModel>> getChangesByVersionMap(
			List<ElementVersionChangeModel> allChanges) {
		Map<ElementVersionModel, List<ElementVersionChangeModel>> changesByVersionMap = new HashMap<ElementVersionModel, List<ElementVersionChangeModel>>();
		if (!CollectionUtils.isEmpty(allChanges)) {
			for (ElementVersionChangeModel changeModel : allChanges) {
				if (changesByVersionMap.containsKey(changeModel.getElementVersion())) {
					changesByVersionMap.get(changeModel.getElementVersion()).add(changeModel);
				} else {
					List<ElementVersionChangeModel> temp = new ArrayList<ElementVersionChangeModel>();
					temp.add(changeModel);
					changesByVersionMap.put(changeModel.getElementVersion(), temp);
				}
			}
		}
		return changesByVersionMap;
	}

	/**
	 * 
	 * @param result
	 * @param changeModel
	 * @return return changes for the search list filtred by changeType version number
	 */
	private boolean changesContain(List<ElementVersionChangeModel> result, ElementVersionChangeModel changeModel) {
		for (ElementVersionChangeModel model : result) {
			if ((model.getChangeType() != null && model.getChangeType().equals(changeModel.getChangeType()))
					&& model.getElementVersion().getElement().getElementId()
							.equals(changeModel.getElementVersion().getElement().getElementId())
					&& (model.getElementVersion().getNumber() == changeModel.getElementVersion().getNumber())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the last änderungsart of a version
	 * 
	 * @param versionId
	 * @return
	 */
	public ElementVersionChangeModel getLastChangeTypeByVersion(Long versionId) {
		return dao.getLastChangeTypeByVersion(versionId);
	}

	/**
	 * Get the diff by change for the returned list from DB without loosing the affected Start.
	 * 
	 * @param allChanges
	 * @return
	 */
	private List<ElementVersionChangeModel> getDiffChangesByChangeType(List<ElementVersionChangeModel> allChanges) {
		int index = 1;
		Long id;
		List<ElementVersionChangeModel> list = new ArrayList<ElementVersionChangeModel>();
		List<ElementVersionChangeModel> attributChanges = new ArrayList<ElementVersionChangeModel>();
		Map<String, List<ElementVersionChangeModel>> modifications = new HashMap<String, List<ElementVersionChangeModel>>();
		Map<Long, List<ElementVersionChangeModel>> changes = new HashMap<Long, List<ElementVersionChangeModel>>();
		if (allChanges != null) {
			// get only attribut value changes
			for (ElementVersionChangeModel changeModel : allChanges) {
				if (hasChangeInValues(changeModel)) {
					attributChanges.add(changeModel);
				} else {
					list.add(changeModel);
				}
			}
			for (ElementVersionChangeModel changeModel : attributChanges) {
				id = changeModel.getElementVersion().getElement().getElementId();
				if (changes.containsKey(id)) {
					changes.get(id).add(changeModel);
				} else {
					changes.put(id, new ArrayList<ElementVersionChangeModel>());
					changes.get(id).add(changeModel);
				}
			}
			for (Map.Entry<Long, List<ElementVersionChangeModel>> entry : changes.entrySet()) {
				for (ElementVersionChangeModel changeModel : entry.getValue()) {
					String key = changeModel.getChangeType();
					if (!modifications.containsKey(key + index)) {
						index++;
						modifications.put(key + index, new ArrayList<ElementVersionChangeModel>());
					}
					modifications.get(key + index).add(changeModel);
				}
				for (Map.Entry<String, List<ElementVersionChangeModel>> entrym : modifications.entrySet()) {
					list.addAll(getDiffChangesByVersion(entrym.getValue(), false));
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
	private boolean hasChangeInValues(ElementVersionChangeModel changeModel) {
		String[] changeTypes = new String[] { ChangeType.ACTIVATE.value(), ChangeType.DEACTIVATE.value(),
				ChangeType.USE_STAND.value(), ChangeType.UNUSE_STAND.value() };
		return !(Arrays.asList(changeTypes).contains(changeModel.getType()));

	}

	/**
	 * ElementVersionChange Comparator by date desc
	 */
	class ElementVersionChangeComparable implements Comparator<ElementVersionChangeModel> {
		@Override
		public int compare(ElementVersionChangeModel o1, ElementVersionChangeModel o2) {
			return o2.getDate().getDate().compareTo(o1.getDate().getDate());
		}
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionChangeManager#changesByVersion(Long, ChangeType,
	 *      ChangeType)
	 */
	@Override
	public void updateChangeTypeForVersion(Long versionId, ChangeTypeValue fromChangeType,
			ChangeTypeValue toChangeType) {
		if (fromChangeType != null && toChangeType != null) {
			List<ElementVersionChangeModel> elementVersionChanges = dao.getChangesByVersionAndChangeType(versionId,
					fromChangeType);
			if (!CollectionUtils.isEmpty(elementVersionChanges)) {
				for (ElementVersionChangeModel elementVersionChange : elementVersionChanges) {
					elementVersionChange.setChangeType(toChangeType.value());
				}
				saveObjects(elementVersionChanges);
			}
		}
	}

	/**
	 * 
	 * @param versionIds
	 * @param change
	 * @return list of ElementVersionChangeModel for given version ids and change
	 */
	public List<ElementVersionChangeModel> getChangesByStandsAndChange(List<Long> versionIds, String change) {
		return dao.getChangesByStandsAndChange(versionIds, change);
	}

	public List<ElementVersionChangeModel> getChangesByVersionAndType(Long versionId, ChangeType type) {
		List<ElementVersionChangeModel> allchanges = dao.getChangesByVersion(versionId,
				((type != null) ? type.value() : null));
		return allchanges;
	}

	@Override
	public List<ElementVersionChangeModel> getChangeDetailByVersion(Long versionId, String change) {
		return dao.getChangeDetailByVersion(versionId, change);
	}

	/**
	 * @see vwg.vw.km.application.implementation.ElementVersionChangeManager#anonomyzeUser(Long)
	 */
	@Override
	public void anonomyzeUser(Long userId) {
		dao.anonomyzeUser(userId);
	}
}
