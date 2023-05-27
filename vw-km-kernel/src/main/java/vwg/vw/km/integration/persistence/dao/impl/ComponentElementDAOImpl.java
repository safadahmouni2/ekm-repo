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
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.ComponentElementDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;
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
 * @author Sebri Zouhaier changed by $Author: benamora $
 * @version $Revision: 1.18 $ $Date: 2013/12/10 11:40:19 $
 */
public class ComponentElementDAOImpl extends DBDaoImpl<ComponentElementModel> implements ComponentElementDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentElementModel> getBusinessClass() {
		return ComponentElementModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentElementDAO#getComponentElementListByComponentStandId(java.lang.Long)
	 */
	@Override
	public List<ComponentElementModel> getComponentElementListByComponentStandId(Long componentStandId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentElementModel> criteria = createCriteria(getBusinessClass(), builder);
		List<Predicate> predicates = new ArrayList<>();

		Root<ComponentElementModel> root = criteria.from(ComponentElementModel.class);

		Join<ComponentElementModel, ComponentStandModel> componentStand = root.join("componentStand");
		Join<ComponentElementModel, ElementVersionModel> elementVersion = root.join("elementVersion");

		predicates.add(builder.equal(root.get("componentStand").get("id"), componentStandId));
		predicates.add(builder.equal(root.get("componentStand").get("id"), componentStand.get("id")));
		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"),
				elementVersion.get("elementVersionId")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.get("displayOrder")));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vwg.vw.km.integration.persistence.dao.ComponentElementDAO#getComponentElementListByElementVersionId(java.lang.
	 * Long) 25.11.2013: ZS: PTS_Problem-34436: Add an order by stand number desc
	 */
	@Override
	public List<ComponentElementModel> getComponentElementListByElementVersionId(Long elementVersionId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentElementModel> root = criteria.from(ComponentElementModel.class);
		
		List<Order> orderList = new ArrayList<>();

		Path<Object> pathStand = root.get("componentStand");
		Path<Object> pathComponent = pathStand.get("componentVersion").get("component");

		orderList.add(builder.asc(pathComponent.get("componentNumber")));
		orderList.add(builder.desc(pathStand.get("number")));
		

		root.fetch("componentStand");
		root.fetch("elementVersion");
		
		List<Predicate> predicates = new ArrayList<>();
		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), elementVersionId));

		criteria.select(root)
				.where(predicates.toArray(new Predicate[] {}))
				.orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	@Override
	public ComponentElementModel getComponentElementByElementVersionAndComponent(Long elementVersionId,
			Long componentId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentElementModel> root = criteria.from(ComponentElementModel.class);

		Path<Object> path = root.get("componentStand").get("componentVersion").get("component");

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("elementVersion").get("elementVersionId"), elementVersionId));
		predicates.add(builder.equal(path.get("componentId"), componentId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentElementDAO#getAllElementsComponentsAssociations()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllElementsComponentsAssociations() {
		Query query = getSession()
				.createSQLQuery("SELECT b.BAUSTEIN_NR,SUM(rel.ANZAHL) FROM T_BAUSTEIN b LEFT JOIN T_BAUSTEINVERSION v "
						+ "ON b.ID = v.KB_REF_UU_ID JOIN T_BAUSTEINSTAND s ON v.KB_VERSION_UU_ID = s.KB_VERSION_REF_ID JOIN T_REL_BAUSTEIN_ELEMENT rel "
						+ "ON rel.KB_STAND_REF_UUID = s.ID GROUP BY b.BAUSTEIN_NR");
		return query.getResultList();
	}
}
