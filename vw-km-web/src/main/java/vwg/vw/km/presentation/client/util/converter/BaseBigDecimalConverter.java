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
package vwg.vw.km.presentation.client.util.converter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import com.sun.faces.util.MessageFactory;

import vwg.vw.km.common.type.BaseBigDecimal;

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
 * @version $Revision: 1.11 $ $Date: 2011/08/18 09:45:25 $
 */
public class BaseBigDecimalConverter implements Converter {

	public static final String DECIMAL_ID = "javax.faces.converter.BigDecimalConverter.DECIMAL";

	public BaseBigDecimalConverter() {
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext facesContext, UIComponent uiComponent, String param) {
		FacesMessage message = MessageFactory.getMessage(facesContext, DECIMAL_ID, param);
		if (param == null || "".equals(param.trim())) {
			return null;
		}
		try {
			if (doesMatch(param)) {
				param = param.replaceAll("\\.", "");
				BaseBigDecimal object = new BaseBigDecimal((String) param);
				if (object.signum() > -1) {
					return object;
				} else {
					throw new ConverterException(message);
				}
			} else {
				throw new ConverterException(message);
			}
		} catch (Exception exception) {
			throw new ConverterException(message, exception);
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext facesContext, UIComponent uiComponent, Object obj) {
		try {
			if (obj != null) {
				return ((BaseBigDecimal) obj).getStringFormatted();
			}
			return null;

		} catch (Exception exception) {
			throw new ConverterException(exception);
		}
	}

	private boolean doesMatch(String s) {
		try {
			Pattern patt = Pattern.compile("^(?!0\\.?\\d)(((\\d+)(\\.\\d{3})*)|(\\d+))(\\,\\d{1,2})*?$",
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = patt.matcher(s);
			return matcher.matches();
		} catch (RuntimeException e) {
			return false;
		}
	}
}
