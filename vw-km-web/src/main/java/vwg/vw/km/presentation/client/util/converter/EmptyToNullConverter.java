package vwg.vw.km.presentation.client.util.converter;

import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

public class EmptyToNullConverter implements Converter {

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.String)
	 */
	public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
		if (value == null || value.trim().length() == 0) {
			if (component instanceof EditableValueHolder) {
				((EditableValueHolder) component).setSubmittedValue(null);
			}
			return null;
		}
		return value;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
	 *      javax.faces.component.UIComponent, java.lang.Object)
	 */
	public String getAsString(FacesContext facesContext, UIComponent component, Object value) {
		return (value == null) ? null : value.toString();
	}

}
