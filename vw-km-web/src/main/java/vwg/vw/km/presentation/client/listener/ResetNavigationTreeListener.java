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

package vwg.vw.km.presentation.client.listener;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import vwg.vw.km.presentation.client.navigation.NavigationBean;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2013
 * </p>
 * 
 * @author zouhairs changed by $Author: zouhair $
 * @version $Revision: 1.1 $ $Date: 2013/11/21 16:51:06 $
 */
public class ResetNavigationTreeListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.APPLY_REQUEST_VALUES;
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
	}

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		NavigationBean navigationBean = (NavigationBean) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("navigationTree");
		if (navigationBean != null) {
			navigationBean.setRightClickedNodeObject(null);
		}
	}

}