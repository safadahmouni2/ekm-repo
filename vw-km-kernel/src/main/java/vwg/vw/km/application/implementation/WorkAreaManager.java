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
import vwg.vw.km.integration.persistence.dao.WorkAreaDAO;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;

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
 * @version $Revision: 1.8 $ $Date: 2015/12/11 08:00:44 $
 */
public interface WorkAreaManager extends BaseManager<WorkAreaModel, WorkAreaDAO> {

	/**
	 * get WorkAreaModel By Designation used in check unicity
	 * 
	 * @param designation
	 * @return
	 */
	public WorkAreaModel getWorkAreaModelByDesignation(String designation);

	/**
	 * get WorkAreas ordered with alpha order
	 * 
	 * @return
	 */
	public List<WorkAreaModel> getAlphaOrderedWorkAreas();

	/**
	 * get WorkAreas By SearchString
	 * 
	 * @param searchString
	 * @return
	 */
	public List<WorkAreaModel> getWorkAreasBySearchString(String searchString);

	/**
	 * Get a WorkArea by its brand
	 * 
	 * @param brandId
	 * @return
	 */
	public WorkAreaModel getWorkAreaByBrand(String brandId);
}
