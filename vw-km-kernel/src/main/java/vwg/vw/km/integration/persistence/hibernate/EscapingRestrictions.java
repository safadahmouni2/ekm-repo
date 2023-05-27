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
package vwg.vw.km.integration.persistence.hibernate;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;

import org.hibernate.criterion.MatchMode;

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
 * @param <T>
 * @author Sebri Zouhaier changed by $Author: mrad $
 * @version $Revision: 1.3 $ $Date: 2011/08/17 06:58:22 $
 */
public class EscapingRestrictions {

	public static Predicate ilike(CriteriaBuilder builder, Path<String> searchPath, Object inputString,
			MatchMode matchMode) {

		String sqlEscapedString = escapeString(inputString);

		if (matchMode.equals(MatchMode.ANYWHERE)) {
			sqlEscapedString = "%" + escapeString(inputString) + "%";
		}
		return builder.like(builder.lower(searchPath.as(String.class)),
				sqlEscapedString != null ? sqlEscapedString.toLowerCase() : null, '!');
	}

	static String escapeString(Object inputString) {
		String sqlEscapedString = null;
		if (inputString != null) {
			sqlEscapedString = inputString.toString().replaceAll("!", "!!").replaceAll("%", "!%").replaceAll("_", "!_");
		}
		return sqlEscapedString;
	}
}
