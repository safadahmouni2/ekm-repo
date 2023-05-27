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

import vwg.vw.km.integration.persistence.dao.HourlyRateDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.HourlyRateModel;

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
 * @version $Revision: 1.6 $ $Date: 2012/09/25 17:34:25 $
 */
public class HourlyRateDAOImpl extends DBDaoImpl<HourlyRateModel> implements HourlyRateDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<HourlyRateModel> getBusinessClass() {
		return HourlyRateModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateDAO#getHourlyRateByCostAttributeAndCatalog(Long, Long)
	 */
	@Override
	public HourlyRateModel getHourlyRateByCostAttributeAndCatalog(Long costAttributeId, Long catalogId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<HourlyRateModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateModel> root = criteria.from(HourlyRateModel.class);
		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("costAttribute").get("costAttributeId"), costAttributeId));
		predicates.add(builder.equal(root.get("hourlyRateCatalog").get("hourlyRateCatalogId"), catalogId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		return setModelAsLoaded(getSingleResultOrNull(createQuery(criteria)));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.HourlyRateDAO#getHourlyRateListByCatalog(Long)
	 */
	@Override
	public List<HourlyRateModel> getHourlyRateListByCatalog(Long catalogId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<HourlyRateModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<HourlyRateModel> root = criteria.from(HourlyRateModel.class);

		criteria.select(root).where(builder.equal(root.get("hourlyRateCatalog").get("hourlyRateCatalogId"), catalogId));
		return getModelList(createQuery(criteria));
	}
}
