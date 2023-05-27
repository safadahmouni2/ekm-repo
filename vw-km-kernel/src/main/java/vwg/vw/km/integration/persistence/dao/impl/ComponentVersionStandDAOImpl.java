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
package vwg.vw.km.integration.persistence.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.type.DateType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.springframework.util.CollectionUtils;

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;

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
 * @author Ziad Saidi changed by $Author: saidi $
 * @version $Revision: 1.29 $ $Date: 2017/08/28 08:25:19 $
 */
public class ComponentVersionStandDAOImpl extends DBDaoImpl<ComponentStandModel> implements ComponentVersionStandDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentStandModel> getBusinessClass() {
		return ComponentStandModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getLastComponentVersionStand(java.lang.Long)
	 */
	@Override
	public ComponentStandModel getLastComponentVersionStand(Long componentVersionId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);

		criteria.select(root)
				.where(builder.equal(root.get("componentVersion").get("componentVersionId"), componentVersionId))
				.orderBy(builder.desc(root.get("id")));
		Query q = createQuery(criteria).setMaxResults(1);
		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getComponentVersionStandByLinkedElementVersion(java.util.Map)
	 */
	@Override
	public List<ComponentStandModel> getComponentVersionStandByLinkedElementVersion(
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand) {
		List<Long> searchIn = getComponentVersionStandIdsByLinkedElementVersion(oldElementVersionInUseByBrand);
		if (!CollectionUtils.isEmpty(searchIn)) {
			CriteriaBuilder builder = createBuidler();
			CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

			Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(addInRestrictions(builder, root.get("id"), searchIn));

			criteria.select(root).where(predicates.toArray(new Predicate[] {}));
			return getModelList(createQuery(criteria));
		}
		return null;
	}

	/**
	 * get the last stand ids by a linked
	 * 
	 * @param oldElementVersionInUseByBrand
	 * @return
	 */
	private List<Long> getComponentVersionStandIdsByLinkedElementVersion(
			Map<BrandModel, List<ElementVersionModel>> oldElementVersionInUseByBrand) {
		CriteriaBuilder builder = createBuidler();

		CriteriaQuery<Object[]> componentElementCriteria = builder.createQuery(Object[].class);

		Root<ComponentElementModel> root = componentElementCriteria.from(ComponentElementModel.class);
		Path<ComponentElementModel> path = root.get("componentStand").get("componentVersion").get("component");

		Predicate predicate = null;
		for (Map.Entry<BrandModel, List<ElementVersionModel>> entry : oldElementVersionInUseByBrand.entrySet()) {
			Predicate sameOwner = builder.and(builder.equal(path.get("owner"), entry.getKey()),
					addInRestrictions(builder, root.get("elementVersion"), entry.getValue()));

			if (predicate == null) {
				predicate = sameOwner;
			} else {
				predicate = builder.or(predicate, sameOwner);
			}
		}

		componentElementCriteria
				.multiselect(builder.max(root.get("componentStand").get("id")).alias("maxIds"), path.get("componentId"))
				.where(predicate).groupBy(path.get("componentId"));
		Query q = getSession().createQuery(componentElementCriteria);
		@SuppressWarnings("unchecked")
		List<Object[]> results = q.getResultList();

		List<Long> searchIn = new ArrayList<Long>();
		if (results != null && !results.isEmpty()) {
			for (Object[] column : results) {
				searchIn.add(new Long(column[1].toString()));
			}
		}
		return searchIn;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getComponentVersionStandByNotOwnerLinkedElement(java.util.Map)
	 */
	@Override
	public List<ComponentStandModel> getComponentVersionStandByNotOwnerLinkedElement(
			Map<BrandModel, List<ElementModel>> elementVersionByBrand) {
		List<Long> searchIn = getComponentVersionStandIdsByNotOwnerLinkedElement(elementVersionByBrand);
		if (!CollectionUtils.isEmpty(searchIn)) {
			CriteriaBuilder builder = createBuidler();
			CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

			Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);

			criteria.select(root).where(addInRestrictions(builder, root.get("id"), searchIn));

			return getModelList(createQuery(criteria));
		}
		return null;
	}

	private List<Long> getComponentVersionStandIdsByNotOwnerLinkedElement(
			Map<BrandModel, List<ElementModel>> elementVersionByBrand) {
		CriteriaBuilder builder = createBuidler();

		CriteriaQuery<Object[]> componentElementCriteria = builder.createQuery(Object[].class);

		Root<ComponentElementModel> root = componentElementCriteria.from(ComponentElementModel.class);
		Path<ComponentElementModel> path = root.get("componentStand").get("componentVersion");

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(path.get("enumStatus").get("enumStatusId"), 5L));

		Predicate predicate = null;
		for (Map.Entry<BrandModel, List<ElementModel>> entry : elementVersionByBrand.entrySet()) {
			Predicate sameOwner = builder.and(builder.equal(path.get("component").get("owner"), entry.getKey()),
					addInRestrictions(builder, root.get("elementVersion").get("element"), entry.getValue()));
			if (predicate == null) {
				predicate = sameOwner;
			} else {
				predicate = builder.or(predicate, sameOwner);
			}
		}
		componentElementCriteria
				.multiselect(builder.max(root.get("componentStand").get("id")).alias("maxIds"),
						path.get("component").get("componentId"))
				.where(predicate).groupBy(path.get("component").get("componentId"));

		Query q = getSession().createQuery(componentElementCriteria);
		@SuppressWarnings("unchecked")
		List<Object[]> results = q.getResultList();
		List<Long> searchIn = new ArrayList<Long>();
		if (results != null && !results.isEmpty()) {
			for (Object[] column : results) {
				searchIn.add(new Long(column[1].toString()));
			}
		}
		return searchIn;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getMaxNumberByComponentId(java.lang.Long)
	 */

	@Override
	public Long getMaxNumberByComponentId(Long componentId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<Object[]> criteria = builder.createQuery(Object[].class);

		Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);

		criteria.multiselect(builder.max(root.get("number")))
				.where(builder.equal(root.get("componentVersion").get("component").get("componentId"), componentId));

		Query q = getSession().createQuery(criteria);
		Long maxValue = (Long) q.getSingleResult();
		return (maxValue == null) ? 0L : maxValue;
	}

	@Override
	public List<ComponentStandModel> getStandsByComponentId(Long componentId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);
		criteria.select(root)
				.where(builder.equal(root.get("componentVersion").get("component").get("componentId"), componentId));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getLastComponentStandsBefore(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ComponentStandModel> getLastComponentStandsBefore(Long componentId, Long standId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("componentVersion").get("component").get("componentId"), componentId));
		predicates.add(builder.le(root.get("id"), standId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("id")));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionStandDAO#getLastComponentStand(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public ComponentStandModel getLastComponentStand(Long componentId, Long standId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandModel> root = criteria.from(ComponentStandModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("componentVersion").get("component").get("componentId"), componentId));

		predicates.add(builder.lessThan(root.get("id"), standId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("id")));
		Query q = createQuery(criteria).setMaxResults(1);
		return getSingleResultOrNull(q);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ComponentStandModel> getToExportComponentStands(BaseDateTime beforeDate, List<Long> libraryIds,
			List<String> userIds, List<Long> forlderIds, Long primaryLibrary, boolean approvedStatus) {
		List<ComponentStandModel> inUseVersions = null;
		// This date is used for the stands history: the date will be dd.mm.yyyy 00:00:00
		BaseDateTime beforeDate2 = beforeDate.addSeconds(-(59 * 60 + 23 * 60 * 60 + 59));
		if (!libraryIds.isEmpty() || !userIds.isEmpty()) {

			org.hibernate.Criteria criteria = createCriteria(getBusinessClass());
			if (isCacheable()) {
				criteria.setCacheable(true);
				criteria.setCacheRegion(getCacheRegion());
			}
			criteria.createCriteria("componentVersion", "version");
			criteria.createCriteria("version.component", "comp");
			criteria.createCriteria("version.enumStatus", "status");

			criteria.createCriteria("comp.library", "lib");
			org.hibernate.criterion.Criterion owners = org.hibernate.criterion.Restrictions.and(
					org.hibernate.criterion.Restrictions.in("lib.libraryId", libraryIds),
					org.hibernate.criterion.Restrictions.eq("comp.inactive", Boolean.FALSE));

			// Search in the history for the used stands
			org.hibernate.criterion.Disjunction usedCriteria = org.hibernate.criterion.Restrictions.disjunction();
			List<Long> usedStandsIds = new ArrayList<Long>();
			// 06.05.2013: ZS: PTS_Problem-23113: the stands that are unused any more before the export date are
			// exported.
			// Solution:
			// For every brand that is selected from the export filter, select the stands that are still in use before
			// the export
			// date using the stand history
			StringBuffer usedStandQueryString = new StringBuffer(
					"select componentStand.id from ComponentStandChangeModel c where ");
			usedStandQueryString.append(
					" c.date <= TO_DATE (:beforeDate,'dd.mm.yy hh24:mi:ss') and newValue= :userId and c.type=:use");
			usedStandQueryString.append(
					" and c.componentStand.id  not in ( select componentStand.id from ComponentStandChangeModel c2 ");
			usedStandQueryString.append("where c2.date > c.date  and oldValue= :userId  and c2.type=:unuse");
			usedStandQueryString.append(")");

			for (String userId : userIds) {
				org.hibernate.query.Query<Long> usedStandQuery = getSession()
						.createQuery(usedStandQueryString.toString());
				usedStandQuery.setParameter("beforeDate", beforeDate.toString(), StringType.INSTANCE);
				usedStandQuery.setParameter("userId", userId, StringType.INSTANCE);
				usedStandQuery.setParameter("use", ChangeType.USE_STAND.value(), StringType.INSTANCE);
				usedStandQuery.setParameter("unuse", ChangeType.UNUSE_STAND.value(), StringType.INSTANCE);
				List<Long> usedStandId = usedStandQuery.list();
				usedStandsIds.addAll(usedStandId);
			}
			// 20.06.2013: ZS PTS_Problem-24019: check the used stands is not empty to avoid IN() in the query
			if (!usedStandsIds.isEmpty()) {
				usedCriteria.add(addInRestrictions("id", usedStandsIds));
			}
			org.hibernate.criterion.Disjunction actifCriteria = org.hibernate.criterion.Restrictions.disjunction();
			// Search in the history for the deactivated stands
			String inactiveQueryString = "select componentStand.id from ComponentStandChangeModel c where "
					+ " c.date <= :beforeDate and c.type=:deactivate and c.componentStand.id "
					+ " in ( select componentStand.id from ComponentStandChangeModel c2 "
					+ " where c2.date > :beforeDate  and c2.type=:activate)";

			org.hibernate.query.Query<Long> inactiveQuery = getSession().createQuery(inactiveQueryString);
			inactiveQuery.setParameter("beforeDate", beforeDate2.getDate(), DateType.INSTANCE);
			inactiveQuery.setParameter("deactivate", ChangeType.DEACTIVATE.value(), StringType.INSTANCE);
			inactiveQuery.setParameter("activate", ChangeType.ACTIVATE.value(), StringType.INSTANCE);

			List<Long> inactiveStandsId = inactiveQuery.list();
			if (!inactiveStandsId.isEmpty()) {
				actifCriteria.add(org.hibernate.criterion.Restrictions.not(addInRestrictions("id", inactiveStandsId)));
			}

			if (!userIds.isEmpty()) {
				org.hibernate.criterion.Disjunction or = org.hibernate.criterion.Restrictions.disjunction();

				if (!libraryIds.isEmpty()) {
					or.add(org.hibernate.criterion.Restrictions.or(usedCriteria,
							org.hibernate.criterion.Restrictions.and(owners, actifCriteria)));
					criteria.add(or);
				} else {
					criteria.add(usedCriteria);
				}
			} else {
				criteria.add(owners);
				criteria.add(actifCriteria);
			}
			// statusId
			if (approvedStatus) {// if only approved we must remove the or restriction
				criteria.add(org.hibernate.criterion.Restrictions.or(
						org.hibernate.criterion.Restrictions.eq("status.enumStatusId", 5L),
						org.hibernate.criterion.Restrictions.eq("status.enumStatusId", 3L)));
			} else {
				criteria.add(org.hibernate.criterion.Restrictions.eq("status.enumStatusId", 5L));
			}
			// export date:validFrom
			criteria.add(org.hibernate.criterion.Restrictions.le("standDate", beforeDate));

			// avoid component with external id existing in the primaryLibrary
			if (!libraryIds.isEmpty() && primaryLibrary != null
					&& (libraryIds.size() > 1 || (libraryIds.size() == 1 && !libraryIds.contains(primaryLibrary)))) {
				List<Long> excludedlibraryIds = libraryIds;
				if (libraryIds.size() > 1) {
					excludedlibraryIds.remove(primaryLibrary);
				}
				StringBuffer queryString = new StringBuffer();
				queryString.append(
						"select c.componentId from ComponentModel c where c.library.libraryId in (:libraryIds) and c.externalId in ");
				queryString.append(
						" (select co.externalId from ComponentModel co  where co.library.libraryId =:primaryLibrary )");

				org.hibernate.query.Query<Long> query = getSession().createQuery(queryString.toString());
				query.setParameterList("libraryIds", excludedlibraryIds);
				query.setParameter("primaryLibrary", primaryLibrary, LongType.INSTANCE);

				List<Long> componentIds = query.list();
				if (!componentIds.isEmpty()) {
					criteria.add(org.hibernate.criterion.Restrictions.or(
							org.hibernate.criterion.Restrictions
									.not(addInRestrictions("comp.componentId", componentIds)),
							org.hibernate.criterion.Restrictions.isNull("comp.externalId")));
				}
			}
			// filter by folders
			if (!forlderIds.isEmpty()) {
				criteria.add(addInRestrictions("comp.folder.folderId", forlderIds));
			}
			criteria.addOrder(org.hibernate.criterion.Order.desc("number"));

			inUseVersions = criteria.list();
		}
		Map<Long, ComponentStandModel> inUseVersionsReturnedByComponent = new HashMap<Long, ComponentStandModel>();
		if (inUseVersions != null) {
			for (ComponentStandModel stand : inUseVersions) {
				BaseDateTime versionValidFrom = stand.getComponentVersion().getValidFrom();
				BaseDateTime standDate = stand.getStandDate();
				// ZS: 21.05.2014: PTS_Problem-39148: the stand should be exported only when the version is released
				// before the date
				// of the export
				if (approvedStatus
						|| (!approvedStatus && !(standDate.getDate().compareTo(versionValidFrom.getDate()) < 0
								&& versionValidFrom.getDate().compareTo(beforeDate2.getDate()) > 0))) {
					if (!inUseVersionsReturnedByComponent
							.containsKey(stand.getComponentVersion().getComponent().getComponentId())) {
						stand.setLoaded(Boolean.TRUE);
						inUseVersionsReturnedByComponent
								.put(stand.getComponentVersion().getComponent().getComponentId(), stand);
					} else {
						if (isVersionToExport(stand, userIds, libraryIds, primaryLibrary)) {
							stand.setLoaded(Boolean.TRUE);
							inUseVersionsReturnedByComponent
									.put(stand.getComponentVersion().getComponent().getComponentId(), stand);
						}
					}
				}
			}
		}
		List<ComponentStandModel> inUseVersionsReturned = new ArrayList<ComponentStandModel>();
		if (inUseVersionsReturnedByComponent.values() != null) {
			inUseVersionsReturned = new ArrayList<ComponentStandModel>(inUseVersionsReturnedByComponent.values());
		}
		return inUseVersionsReturned;
	}

	private Boolean isVersionToExport(ComponentStandModel stand, List<String> userIds, List<Long> libraryIds,
			Long primaryLibrary) {
		if (!CollectionUtils.isEmpty(userIds)) {

			for (ComponentStandUsersModel standUser : stand.getUsers()) {
				LibraryModel lib = new ArrayList<LibraryModel>(standUser.getBrand().getLibraries()).get(0);
				if (userIds.contains(standUser.getBrand().getBrandId())
						&& (primaryLibrary == null || (lib.getLibraryId().equals(primaryLibrary)))) {
					return Boolean.TRUE;
				}

			}
		}
		return Boolean.FALSE;
	}
}
