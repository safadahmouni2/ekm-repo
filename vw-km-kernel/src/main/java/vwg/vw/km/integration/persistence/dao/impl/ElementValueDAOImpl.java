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

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import vwg.vw.km.integration.persistence.dao.ElementValueDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.model.ElementValueModel;

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
 * @version $Revision: 1.8 $ $Date: 2012/09/25 17:34:25 $
 */
public class ElementValueDAOImpl extends DBDaoImpl<ElementValueModel> implements ElementValueDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ElementValueModel> getBusinessClass() {
		return ElementValueModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementValueDAO#getElementValuesByElementVersionId(java.lang.Long)
	 */
	public List<ElementValueModel> getElementValuesByElementVersionId(Long versionId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementValueModel> criteria = builder.createQuery(ElementValueModel.class);

		Root<ElementValueModel> root = criteria.from(ElementValueModel.class);

		criteria.select(root).where(builder.equal(root.get("elementVersion").get("elementVersionId"), versionId));

		return getModelList(createQuery(criteria));
	}
}
