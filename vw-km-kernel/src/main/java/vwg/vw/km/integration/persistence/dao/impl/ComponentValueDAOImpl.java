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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.ComponentValueDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;

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
 * @version $Revision: 1.7 $ $Date: 2012/11/21 10:54:09 $
 */
public class ComponentValueDAOImpl extends DBDaoImpl<ComponentValueModel> implements ComponentValueDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentValueModel> getBusinessClass() {
		return ComponentValueModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentValueDAO#getComponentValuesByComponentVersionId(java.lang.Long)
	 */
	@Override
	public List<ComponentValueModel> getComponentValuesByComponentVersionId(Long versionId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentValueModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentValueModel> root = criteria.from(ComponentValueModel.class);

		criteria.select(root).where(builder.equal(root.get("componentVersion").get("componentVersionId"), versionId));

		return getModelList(createQuery(criteria));
	}

	@Override
	public ComponentValueModel getComponentValueByComponentVersionId(Long versionId, CostAttributeModel costAttribute) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentValueModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentValueModel> root = criteria.from(ComponentValueModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("componentVersion").get("componentVersionId"), versionId));
		predicates.add(builder.equal(root.get("costAttribute"), costAttribute));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria).setMaxResults(1);

		return getSingleResultOrNull(q);
	}
}
