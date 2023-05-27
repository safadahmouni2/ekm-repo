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
package vwg.vw.km.application.implementation;

import java.util.List;

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.integration.persistence.dao.ComponentElementDAO;
import vwg.vw.km.integration.persistence.model.ComponentElementModel;
import vwg.vw.km.integration.persistence.model.ComponentStandModel;

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
 * @version $Revision: 1.15 $ $Date: 2013/12/10 11:40:19 $
 */
public interface ComponentElementManager extends BaseManager<ComponentElementModel, ComponentElementDAO> {

	/**
	 * 
	 * @param componentStandId
	 * @return list of ComponentElementModel for given componentVersionId
	 */
	public List<ComponentElementModel> getComponentElementListByComponentStandId(Long componentStandId);

	/**
	 * copy ComponentElements from a ComponentVersionModel to another
	 * 
	 * @param from
	 * @param to
	 */
	public void copyComponentElements(ComponentStandModel from, ComponentStandModel to);

	/**
	 * 
	 * @param elementVersionId
	 * @return
	 */
	public List<ComponentElementModel> getComponentElementListByElementVersionId(Long elementVersionId);

	/**
	 * 
	 * @param elementVersionId
	 * @param componentId
	 * @return
	 */
	public ComponentElementModel getComponentElementByElementVersionAndComponent(Long elementVersionId,
			Long componentId);

	/**
	 * For statistic purpose select all elements components associations in the DB
	 * 
	 * @return
	 */
	public List<Object[]> getAllElementsComponentsAssociations();
}
