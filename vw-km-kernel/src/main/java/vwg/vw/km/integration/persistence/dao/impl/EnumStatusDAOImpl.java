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

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.EnumStatusDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.EnumStatusModel;
import vwg.vw.km.integration.persistence.model.EnumTransitionModel;

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
 * @version $Revision: 1.8 $ $Date: 2012/09/26 07:42:04 $
 */
public class EnumStatusDAOImpl extends DBDaoImpl<EnumStatusModel> implements EnumStatusDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<EnumStatusModel> getBusinessClass() {
		return EnumStatusModel.class;
	}

	/**
	 * 
	 * @return cache region used by hibernate caching API
	 */
	@Override
	protected String getCacheRegion() {
		return "query.enumStatus";
	}

	/**
	 * 
	 * @return true if this object is cacheable
	 */
	@Override
	protected boolean isCacheable() {
		return true;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.EnumStatusDAO#getAllowedTransitions(vwg.vw.km.integration.persistence.model.EnumStatusModel,java.lang.Boolean)
	 */
	@Override
	public List<EnumTransitionModel> getAllowedTransitions(EnumStatusModel fromStatus, Boolean isBatchTransaction,
			List<Long> withoutToStatus) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<EnumTransitionModel> criteria = builder.createQuery(EnumTransitionModel.class);

		Root<EnumTransitionModel> root = criteria.from(EnumTransitionModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(
				builder.equal(root.get("comp_id").get("fromStatus").get("enumStatusId"), fromStatus.getEnumStatusId()));
		if (isBatchTransaction != null) {
			predicates.add(builder.equal(root.get("batch"), isBatchTransaction));
		}
		if (withoutToStatus != null && !withoutToStatus.isEmpty()) {
			predicates.add(builder.not(root.get("comp_id").get("toStatus").get("enumStatusId").in(withoutToStatus)));
		}

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		List<EnumTransitionModel> list = getSession().createQuery(criteria).getResultList();
		return list;
	}

}
