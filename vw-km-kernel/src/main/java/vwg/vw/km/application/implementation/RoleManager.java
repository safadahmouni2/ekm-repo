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

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.integration.persistence.dao.RoleDAO;
import vwg.vw.km.integration.persistence.model.RoleModel;

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
 * @version $Revision: 1.9 $ $Date: 2013/12/19 11:16:28 $
 */
public interface RoleManager extends BaseManager<RoleModel, RoleDAO> {

	public static enum Right {
		MANAGE_WORK_AREA(1L), MANAGE_USER(2L), MANAGE_HOURLY_RATE(3L), MANAGE_COMPONENT(4L), MANAGE_ELEMENT(
				5L), VIEW_USER(6L), UPDATE_FOLDER(
						7L), VIEW_STATISTIC(8L), MANAGE_CALCULATOR_MODULE(9L), MANAGE_COST_MANAGEMENT_MODULE(10L);

		void setValue(Long desc) {
			this.desc = desc;
		}

		public Long value() {
			return desc;
		}

		private Right(Long desc) {
			this.desc = desc;
		}

		private Long desc;
	};

	public static enum Role {
		ROOT_ADMINISTRATOR(1L), ADMINISTRATOR(2L), KALKULATOR(3L), BETRACHTER(4L), INVESTERMITTLER(5L);

		void setValue(Long desc) {
			this.desc = desc;
		}

		public Long value() {
			return desc;
		}

		private Role(Long desc) {
			this.desc = desc;
		}

		private Long desc;
	};
}
