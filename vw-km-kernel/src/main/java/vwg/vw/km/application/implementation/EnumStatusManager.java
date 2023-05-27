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
import vwg.vw.km.integration.persistence.dao.EnumStatusDAO;
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
 * @author Sebri Zouhaier changed by $Author: monam $
 * @version $Revision: 1.8 $ $Date: 2011/08/17 10:38:15 $
 */
public interface EnumStatusManager extends BaseManager<EnumStatusModel, EnumStatusDAO> {

	public static enum Status {
		NEW(0L), CREATED(1L), TO_BE_APPROVED(2L), APPROVED(3L), IN_PROGRESS(4L), IN_USE(5L);

		void setValue(Long desc) {
			this.desc = desc;
		}

		public Long value() {
			return desc;
		}

		private Status(Long desc) {
			this.desc = desc;
		}

		private Long desc;
	};

	/**
	 * return all allowed transitions from given status
	 * 
	 * @param fromStatus
	 *            start status
	 * @param isBatchTransaction
	 *            is batch transition
	 * @param withoutToStatus
	 *            list of end status those noy to be considered
	 * @return
	 */
	public List<EnumTransitionModel> getAllowedTransitions(EnumStatusModel fromStatus, Boolean isBatchTransaction,
			List<Long> withoutToStatus);

}
