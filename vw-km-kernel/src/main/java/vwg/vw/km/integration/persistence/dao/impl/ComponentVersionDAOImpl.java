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
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.integration.persistence.dao.ComponentVersionDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;

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
 * @version $Revision: 1.51 $ $Date: 2022/01/19 09:37:04 $
 */
public class ComponentVersionDAOImpl extends DBDaoImpl<ComponentVersionModel> implements ComponentVersionDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentVersionModel> getBusinessClass() {
		return ComponentVersionModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getLastComponentVersionByComponentId(java.lang.Long)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionByComponentId(Long componentId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.desc(root.get("validTo")));
		orderList.add(builder.desc(root.get("componentVersionId")));

		criteria.select(root).where(builder.equal(root.get("component").get("componentId"), componentId))
				.orderBy(orderList);

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getComponentVersionToBeTraitedByBatch()
	 */
	@Override
	public List<ComponentVersionModel> getComponentVersionToBeTraitedByBatch() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		criteria.select(root).where(builder.equal(root.get("enumStatus").get("enumStatusId"), 3L))
				.orderBy(builder.desc(root.get("componentVersionId")));

		return getModelList(createQuery(criteria));

	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getLastComponentVersionInUse(java.lang.Long)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionInUse(Long componentId) {


		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("enumStatus").get("enumStatusId"), 5L));
		predicates.add(builder.equal(root.get("component").get("componentId"), componentId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.desc(root.get("componentVersionId")));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getComponentVersionAttachedIds(java.lang.Long)
	 */
	@Override
	public List<Long> getComponentVersionAttachedIds(Long componentId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		criteria.distinct(true).select(root.get("componentVersionId"))
				.where(builder.equal(root.get("component").get("componentId"), componentId));
		@SuppressWarnings("unchecked")
		List<Long> list = createQuery(criteria).getResultList();
		return list;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getLastComponentVersionBeforeDate(java.lang.Long,vwg.vw.km.common.type.BaseDateTime)
	 */
	@Override
	public ComponentVersionModel getLastComponentVersionBeforeDate(Long componentId, BaseDateTime beforeDate) {

		Query q = getSession().createQuery(
				"from ComponentVersionModel c where c.validFrom <= :beforeDate and c.validTo >= :beforeDate and c.component.componentId = :componentId order by c.componentVersionId desc");
		q.setParameter("beforeDate", beforeDate);
		q.setParameter("componentId", componentId);
		q.setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#isTheLastComponentVersion(Long, Long, Long)
	 */
	@Override
	public boolean isTheLastComponentVersion(Long componentVersionId, Long componentId, Long statusId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("component").get("componentId"), componentId));
		predicates.add(builder.gt(root.get("componentVersionId"), componentVersionId));
		predicates.add(builder.equal(root.get("enumStatus").get("enumStatusId"), statusId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.desc(root.get("componentVersionId")));

		Query q = createQuery(criteria);

		q.setMaxResults(1);
		return q.getResultList().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getLastComponentVersionsBeforeVersion(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ComponentVersionModel> getLastComponentVersionsBeforeVersion(Long componentId, Long versionId) {
		Query q = getSession().createQuery(
				"from ComponentVersionModel c where c.componentVersionId <= :versionId  and c.component.componentId = :componentId order by c.componentVersionId desc");
		q.setParameter("versionId", versionId);
		q.setParameter("componentId", componentId);

		return getModelList(q);
	}

	@Override
	public ComponentVersionModel getNextComponentVersion(Long componentVersionId, Long componentId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("component").get("componentId"), componentId));
		predicates.add(builder.gt(root.get("componentVersionId"), componentVersionId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.get("componentVersionId")));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	@Override
	public List<ComponentVersionModel> getComponentsVersionsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentVersionModel> root = criteria.from(ComponentVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Predicate p1 = root.get("component").get("folder").get("folderId").in(listOfFolders);

		Predicate p2 = EscapingRestrictions.ilike(builder, root.get("component").get("designation"), searchString,
				MatchMode.ANYWHERE);

		Predicate p3 = builder.or(EscapingRestrictions.ilike(builder, root.get("component").get("componentNumber"),
				searchString, MatchMode.ANYWHERE), p2);

		Predicate p4 = builder.or(
				EscapingRestrictions.ilike(builder, root.get("description"), searchString, MatchMode.ANYWHERE),
				p3);

		Predicate p5 = builder.or(EscapingRestrictions.ilike(builder, root.get("mechanicalConstruction"), searchString,
				MatchMode.ANYWHERE), p4);

		Predicate p6 = builder.or(EscapingRestrictions.ilike(builder, root.get("mechanicalExecution"), searchString,
				MatchMode.ANYWHERE), p5);

		Predicate p7 = builder.or(
				EscapingRestrictions.ilike(builder, root.get("electric"), searchString, MatchMode.ANYWHERE), p6);

		Predicate p8 = builder.or(EscapingRestrictions.ilike(builder, root.get("component").get("externalId"),
				searchString, MatchMode.ANYWHERE), p7);

		Predicate p9 = builder.or(EscapingRestrictions.ilike(builder, root.get("component").get("componentId"),
				searchString, MatchMode.ANYWHERE), p8);

		predicates.add(p1);
		predicates.add(p9);

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("component").get("componentNumber")));
		orderList.add(builder.asc(root.get("component").get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getAllComponentsVersions() PTS change 35853:ABA:
	 * 24/01/2014 add MECHANIK_KONSTRUKTION,MECHANIK_AUSFUEHRUNG,ELEKTRIK
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllComponentsVersions() {
		Query query = getSession().createSQLQuery("SELECT b.BAUSTEIN_NR,b.BEZEICHNUNG,b.X_INAKTIV,b.ERSTELLER_REF_ID,"
				+ "b.ERSTELLT_AM,b.GEAENDERT_VON_REF_ID,b.GEAENDERT_AM,b.INAKTIV_VON,b.ORDNER_REF_ID,b.BESITZER,s.STAND_DATE,s.STAND_NUMMER,"
				+ "v.BESCHREIBUNG,v.MECHANIK_KONSTRUKTION,v.MECHANIK_AUSFUEHRUNG,v.ELEKTRIK,"
				+ "v.BEMERKUNGEN,v.GUELTIG_VON,v.GUELTIG_BIS,v.ABBILDUNG,v.ERSTELLT_AM AS V_ERSTELLT_AM, v.GEAENDERT_AM AS V_GEAENDERT_AM,v.BEISTELLUNG_AG,"
				+ "v.KATALOG_NUMMER, v.HERSTELLER,v.ERSTELLER_REF_ID AS V_ERSTELLER_REF_ID,v.GEAENDERT_VON_REF_ID AS V_GEAENDERT_VON_REF_ID,v.FREIGABE_DURCH_REF_ID,v.ENUM_STATUS_REF_ID,v.ENUM_BAUSTEIN_TYP_REF_ID,"
				+ "v.VERSION_NUMMER, v.GESPERRT , u.MARKE_REF_ID FROM T_BAUSTEIN b LEFT JOIN T_BAUSTEINVERSION v ON b.ID = v.KB_REF_UU_ID "
				+ "LEFT JOIN T_BAUSTEINSTAND s ON v.KB_VERSION_UU_ID = s.KB_VERSION_REF_ID LEFT JOIN T_REL_BAUSVERSION_NUTZER u "
				+ "on u.KB_STAND_ID = s.ID");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionDAO#getComponentsCountByVersion() PTS problem
	 * 35867:ABA: 27-01-2014:Get component versions count by component ID
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getComponentsCountByVersion() {
		Query query = getSession()
				.createSQLQuery("Select KB_REF_UU_ID,BESITZER,versionCount from T_BAUSTEIN inner join "
						+ "(Select KB_REF_UU_ID,count(KB_VERSION_UU_ID) as versionCount from  T_BAUSTEINVERSION group by KB_REF_UU_ID)"
						+ " comp_versions on comp_versions.KB_REF_UU_ID= ID");
		return query.getResultList();
	}
}