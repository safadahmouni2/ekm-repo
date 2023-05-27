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

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.39 $ $Date: 2022/01/19 09:37:04 $
 */
public class ComponentStandChangeDAOImpl extends DBDaoImpl<ComponentStandChangeModel>
		implements ComponentStandChangeDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentStandChangeModel> getBusinessClass() {
		return ComponentStandChangeModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO#getChangesByComponentStand(java.lang.Long)
	 */
	@Override
	public List<ComponentStandChangeModel> getChangesByComponentStand(Long standId) {
		Query q = getSession().createQuery(
				"from ComponentStandChangeModel model where model.componentStand.id= :standId order by model.changeId desc");
		q.setParameter("standId", standId);
		return getModelList(q);
	}

	/**
	 * 31.10.2013: ZS: PTS_Problem-26241: results displayed outside the period set filter
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO#getChangesByCriteria(SearchHistoryObject)
	 */
	@Override
	public List<ComponentStandChangeModel> getChangesByCriteria(SearchHistoryObject historyObject) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandChangeModel> root = criteria.from(ComponentStandChangeModel.class);

		Path<ComponentStandChangeModel> path = root.get("componentStand").get("componentVersion");

		List<Predicate> predicates = new ArrayList<Predicate>();

		// AMR: PTS-Change_36691 : add restriction to search criteria : changes in work folder must not appear in
		// research

		predicates.add(path.get("component").get("folder").get("creatorRefId").isNull());

		if (!historyObject.getComponentId().isEmpty()) {
			predicates.add(addInRestrictions(builder, path.get("component").get("componentId"),
					historyObject.getComponentId()));
		}
		if (!historyObject.getStatusList().isEmpty()) {
			predicates.add(addInRestrictions(builder, path.get("enumStatus").get("enumStatusId"),
					historyObject.getStatusList()));
		}

		root.fetch("componentStand").fetch("componentVersionUsers", JoinType.LEFT);
		root.fetch("componentStand").fetch("componentVersion").fetch("component").fetch("owner", JoinType.LEFT);

		if (historyObject.getNumComponent() != null && !"".equals(historyObject.getNumComponent().trim())) {
			predicates.add(EscapingRestrictions.ilike(builder, path.get("component").get("componentNumber"),
					historyObject.getNumComponent(), MatchMode.ANYWHERE));
		}
		if (historyObject.getExternalId() != null && !"".equals(historyObject.getExternalId().trim())) {
			Predicate p1 = EscapingRestrictions.ilike(builder, path.get("component").get("externalId"),
					historyObject.getExternalId().trim(), MatchMode.ANYWHERE);

			Predicate p2 = builder.equal(path.get("component").get("componentId").as(String.class),
					historyObject.getExternalId().trim());

			predicates.add(builder.or(p2, p1));

		}
		if (historyObject.getOwner() != null && !historyObject.getOwner().isEmpty()) {
			predicates.add(builder.equal(path.get("component").get("owner").get("brandId"), historyObject.getOwner()));
		}

		// ZS: PTS-Problem_37250 : Review history query when criteria 'All' Nutzer is set
		if (historyObject.getUser() != null && !historyObject.getUser().isEmpty()) {

			CriteriaQuery<ComponentStandChangeModel> usedCriteria = builder
					.createQuery(ComponentStandChangeModel.class);
			Root<ComponentStandChangeModel> usedRoot = usedCriteria.from(ComponentStandChangeModel.class);
			List<Predicate> usedPredicates = new ArrayList<Predicate>();
			usedPredicates.add(builder.equal(usedRoot.get("newValue"), historyObject.getUser()));
			usedPredicates.add(builder.equal(usedRoot.get("type"), ChangeType.USE_STAND.value()));

			usedCriteria.select(usedRoot.get("componentStand").get("id"))
					.where(usedPredicates.toArray(new Predicate[] {}));

			Query q = getSession().createQuery(usedCriteria);
			predicates.add(root.get("componentStand").get("id").in(q.getResultList()));
		}
		if (historyObject.getFromDate() != null) {
			predicates.add(builder.greaterThanOrEqualTo(root.get("date").as(Date.class),
					new BaseDateTime(historyObject.getFromDate()).getDate()));
		}
		if (historyObject.getToDate() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(historyObject.getToDate());
			cal.add(Calendar.DATE, 1);

			predicates.add(builder.lessThanOrEqualTo(root.get("date").as(Date.class),
					new BaseDateTime(cal.getTime()).getDate()));
		}
		if (!historyObject.getChangeTypes().isEmpty()) {
			predicates.add(root.get("changeType").in(historyObject.getChangeTypes()));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("changeId")));

		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO#getChangesByVersionAndChangeType(java.lang.Long,
	 *      vwg.vw.km.common.type.ChangeTypeValue)
	 */
	@Override
	public List<ComponentStandChangeModel> getChangesByStandAndChangeType(Long standId, ChangeTypeValue type) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandChangeModel> root = criteria.from(ComponentStandChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("componentStand").get("id"), standId));

		if (type != null) {

			predicates.add(builder.equal(root.get("changeType"), type.value()));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionChangeDAO#getChangesByStandsAndChangeType(java.util.List,vwg.vw.km.common.type.ChangeTypeValue)
	 */
	@Override
	public List<ComponentStandChangeModel> getChangesByStandsAndChangeType(List<Long> standIds, ChangeTypeValue type) {
		List<ComponentStandChangeModel> result = new ArrayList<ComponentStandChangeModel>();
		for (Long standId : standIds) {
			CriteriaBuilder builder = createBuidler();
			CriteriaQuery<ComponentStandChangeModel> criteria = createCriteria(getBusinessClass(), builder);

			Root<ComponentStandChangeModel> root = criteria.from(ComponentStandChangeModel.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.equal(root.get("componentStand").get("id"), standId));
			if (type != null) {
				predicates.add(builder.equal(root.get("changeType"), type.value()));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));
			result.addAll(getModelList(createQuery(criteria)));
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO#getChangesByStandAndChange(java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public List<ComponentStandChangeModel> getChangesByStandAndChange(Long standId, String change) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandChangeModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandChangeModel> root = criteria.from(ComponentStandChangeModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("componentStand").get("id"), standId));
		if (change != null) {
			predicates.add(builder.equal(root.get("change"), change));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(builder.desc(root.get("date")));
		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentStandChangeDAO#anonomyzeUser(java.lang.Long)
	 */
	@Override
	public void anonomyzeUser(Long userId) {
		Query query = getSession().createSQLQuery(
				"UPDATE T_BAUSTEINVERSIONANDERUNG SET BENUTZER = 'Anonymous' WHERE ERSTELLER_REF_ID = :userId ");
		query.setParameter("userId", userId);
		query.executeUpdate();
	}
}
