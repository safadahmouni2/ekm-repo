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
package vwg.vw.km.presentation.client.util;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author raissi changed by $Author: zouhair $
 * @version $Revision: 1.3 $ $Date: 2012/10/22 14:21:53 $
 */
public class ModelSelectedObject {
	private Object model;

	private boolean selected;

	private boolean accessAllowed;

	@Override
	public boolean equals(Object obj) {
		return model.equals(((ModelSelectedObject) obj).getModel());
	}

	public Object getModel() {
		return model;
	}

	public void setModel(Object model) {
		this.model = model;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public boolean isAccessAllowed() {
		return accessAllowed;
	}

	public void setAccessAllowed(boolean accessAllowed) {
		this.accessAllowed = accessAllowed;
	}

	public ModelSelectedObject() {
	}

	public ModelSelectedObject(ModelSelectedObject another) {
		this.model = another.model;
		this.selected = another.selected;
		this.accessAllowed = another.accessAllowed;
	}
}
