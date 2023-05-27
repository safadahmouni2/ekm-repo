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
package vwg.vw.km.application.service.base;

import java.io.File;

import vwg.vw.km.application.service.dto.base.BaseDTO;

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
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.7 $ $Date: 2011/08/17 09:09:43 $
 */
public abstract class BaseService<T extends BaseDTO> {

	/**
	 * load DTO Fo rList By SearchString
	 * 
	 * @param dto
	 * @return
	 */
	public T loadDTOForListBySearchString(T dto) {
		throw new RuntimeException("Implement me for search");
	}

	/**
	 * get Absolute ClassDir Path
	 * 
	 * @return
	 */
	protected String getAbsoluteClassDirPath() {
		File file = new File(this.getClass().getResource("/uploadRef.properties").getPath());
		return file.getParentFile().getAbsolutePath();
	}
}
