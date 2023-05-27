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

import vwg.vw.km.integration.persistence.dao.ElementVersionDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;

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
 * @version $Revision: 1.27 $ $Date: 2015/12/07 13:13:31 $
 */
public class ElementVersionDAOImpl extends DBDaoImpl<ElementVersionModel> implements ElementVersionDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ElementVersionModel> getBusinessClass() {
		return ElementVersionModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getLastElementVersionByElementId(java.lang.Long)
	 */
	@Override
	public ElementVersionModel getLastElementVersionByElementId(Long elementId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.desc(root.get("validTo")));
		orderList.add(builder.desc(root.get("elementVersionId")));

		criteria.select(root).where(builder.equal(root.get("element").get("elementId"), elementId)).orderBy(orderList);

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getElementVersionToBeTraitedByBatch()
	 */
	@Override
	public List<ElementVersionModel> getElementVersionToBeTraitedByBatch() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		criteria.select(root).where(builder.equal(root.get("enumStatus").get("enumStatusId"), 3L))
				.orderBy(builder.desc(root.get("elementVersionId")));

		return getModelList(createQuery(criteria));

	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getLastElementVersionByStatus(java.lang.Long,java.lang.Long)
	 */
	@Override
	public ElementVersionModel getLastElementVersionByStatus(Long elementId, Long statusId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("enumStatus").get("enumStatusId"), statusId));
		predicates.add(builder.equal(root.get("element").get("elementId"), elementId));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.desc(root.get("validTo")));
		orderList.add(builder.desc(root.get("elementVersionId")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getElementVersionAttachedIds(java.lang.Long)
	 */
	@Override
	public List<Long> getElementVersionAttachedIds(Long elementId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		criteria.distinct(true).select(root.get("elementVersionId"))
				.where(builder.equal(root.get("element").get("elementId"), elementId));
		@SuppressWarnings("unchecked")
		List<Long> list = createQuery(criteria).getResultList();
		return list;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#isTheLastElementVersion(Long, Long, long)
	 */
	@Override
	public boolean isTheLastElementVersion(Long elementVersionId, Long elementId, Long statusId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("element").get("elementId"), elementId));
		predicates.add(builder.gt(root.get("elementVersionId"), elementVersionId));
		predicates.add(builder.equal(root.get("enumStatus").get("enumStatusId"), statusId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.desc(root.get("elementVersionId")));

		Query q = createQuery(criteria);
		q.setMaxResults(1);
		return q.getResultList().isEmpty();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getElementVersionsByStatus(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ElementVersionModel> getElementVersionsByStatus(Long elementId, Long statusId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("enumStatus").get("enumStatusId"), statusId));
		predicates.add(builder.equal(root.get("element").get("elementId"), elementId));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.desc(root.get("validTo")));
		orderList.add(builder.desc(root.get("elementVersionId")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementVersionDAO#getAllElementsVersions() PTS change 35853:ABA:
	 * 24/01/2014 add MECHANIK_KONSTRUKTION,MECHANIK_AUSFUEHRUNG,ELEKTRIK
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllElementsVersions() {
		Query query = getSession().createSQLQuery("SELECT e.ELEMENT_NR,e.BEZEICHNUNG,e.X_INAKTIV,e.ERSTELLER_REF_ID,"
				+ "e.ERSTELLT_AM,e.GEAENDERT_VON_REF_ID,e.GEAENDERT_AM,e.INAKTIV_VON,e.ORDNER_REF_ID,e.BESITZER,v.BESCHREIBUNG,"
				+ "v.MECHANIK_KONSTRUKTION,v.MECHANIK_AUSFUEHRUNG,v.ELEKTRIK,v.BEMERKUNGEN,"
				+ "v.GUELTIG_VON,v.GUELTIG_BIS,v.ERSTELLT_AM AS V_ERSTELLT_AM, v.GEAENDERT_AM AS V_GEAENDERT_AM,v.BEISTELLUNG_AG,v.E_PREISSTAND_DATUM, v.E_PREISSTAND_BEMERKUNG,"
				+ "v.M_PREISSTAND_DATUM,v.M_PREISSTAND_BEMERKUNG,v.KATALOG_NUMMER,v.HERSTELLER,v.FREIGABE_DURCH_REF_ID,v.ENUM_STATUS_REF_ID,"
				+ "v.VERSION_NUMMER, v.GESPERRT , u.MARKE_REF_ID FROM T_ELEMENT e  LEFT JOIN T_ELEMENTVERSION v ON e.UUIDKE = v.KE_REF_UU_ID "
				+ "LEFT JOIN T_REL_ELEMVERSION_NUTZER u ON u.KE_VERSION_REF_ID = v.UUIDKEVERSION ");
		return query.getResultList();
	}

	@Override
	public List<ElementVersionModel> getElementsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementVersionModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementVersionModel> root = criteria.from(ElementVersionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate p1 = root.get("element").get("folder").get("folderId").in(listOfFolders);
		Predicate p2 = EscapingRestrictions.ilike(builder, root.get("element").get("designation"), searchString,
				MatchMode.ANYWHERE);
		Predicate p3 = builder.or(EscapingRestrictions.ilike(builder, root.get("element").get("elementNumber"),
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

		predicates.add(p1);
		predicates.add(p7);

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("element").get("elementNumber")));
		orderList.add(builder.asc(root.get("element").get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}
}
