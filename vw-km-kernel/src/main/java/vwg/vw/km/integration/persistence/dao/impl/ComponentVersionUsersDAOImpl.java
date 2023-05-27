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
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.ComponentVersionUsersDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandUsersModel;

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
 * @version $Revision: 1.6 $ $Date: 2013/02/01 08:31:40 $
 */
public class ComponentVersionUsersDAOImpl extends DBDaoImpl<ComponentStandUsersModel>
		implements ComponentVersionUsersDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentStandUsersModel> getBusinessClass() {
		return ComponentStandUsersModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionUsersDAO#getComponentStandUsers(vwg.vw.km.integration.persistence.model.ComponentStandModel,
	 *      vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	@Override
	public ComponentStandUsersModel getComponentStandUsers(Long standId, BrandModel brand) {
		ComponentStandUsersModel result = null;
		if (standId != null) {



			CriteriaBuilder builder = createBuidler();
			CriteriaQuery<ComponentStandUsersModel> criteria = createCriteria(getBusinessClass(), builder);

			Root<ComponentStandUsersModel> root = criteria.from(ComponentStandUsersModel.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			predicates.add(builder.equal(root.get("componentStand").get("id"), standId));
			predicates.add(builder.equal(root.get("brand"), brand));
			criteria.select(root).where(predicates.toArray(new Predicate[] {}));
			Query q = createQuery(criteria);

			result = setModelAsLoaded(getSingleResultOrNull(q));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentVersionUsersDAO#getComponentUsers(java.lang.Long,
	 * vwg.vw.km.integration.persistence.model.BrandModel)
	 */
	@Override
	public ComponentStandUsersModel getComponentUsers(Long componentId, BrandModel brand) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandUsersModel> root = criteria.from(ComponentStandUsersModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Path<Object> path = root.get("componentStand").get("componentVersion").get("component");

		predicates.add(builder.equal(path.get("componentId"), componentId));
		predicates.add(builder.equal(root.get("brand"), brand));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria).setMaxResults(1);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	@Override
	public List<ComponentStandUsersModel> getComponentUsers(Long componentId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandUsersModel> root = criteria.from(ComponentStandUsersModel.class);

		Path<Object> path = root.get("componentStand").get("componentVersion").get("component");

		criteria.select(root).where(builder.equal(path.get("componentId"), componentId));

		return getModelList(createQuery(criteria));
	}

	@Override
	public List<ComponentStandUsersModel> getInactivatedComponentStandUsers() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentStandUsersModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentStandUsersModel> root = criteria.from(ComponentStandUsersModel.class);
		criteria.select(root).where(builder.equal(root.get("active"), false));
		return getModelList(createQuery(criteria));
	}
}
