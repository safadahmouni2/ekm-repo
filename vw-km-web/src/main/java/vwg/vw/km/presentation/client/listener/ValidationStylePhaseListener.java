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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.9 $ $Date: 2016/11/21 15:54:07 $
 */
public class ValidationStylePhaseListener implements PhaseListener {

	private static final long serialVersionUID = 1L;

	private final String ORIGINAL_STYLE = "vw.faces.original.style";

	private final String ORIGINAL_TITLE = "vw.faces.original.title";

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.RENDER_RESPONSE;
	}

	@Override
	public void beforePhase(PhaseEvent phaseEvent) {
		FacesContext context = FacesContext.getCurrentInstance();
		UIViewRoot root = context.getViewRoot();
		restoreOriginalStyles(context, root);
		restoreOriginalTitle(context, root);
		Iterator<String> i = context.getClientIdsWithMessages();

		while (i.hasNext()) {
			String id = i.next();
			UIComponent component = root.findComponent(id);

			if (component instanceof UIInput) {
				String style = (String) component.getAttributes().get("styleClass");
				style = style == null ? "" : style + " ";
				String title = (String) component.getAttributes().get("title");
				title = title == null ? "" : title;
				component.getAttributes().put("styleClass", style + "validation-error");
				Iterator<FacesMessage> messages = context.getMessages(id);
				while (messages.hasNext()) {
					FacesMessage message = messages.next();
					component.getAttributes().put("title", message.getDetail());
				}
				saveOriginalStyle(id, style, context);
				saveOriginalTitle(id, title, context);
			}
		}
	}

	/**
	 * restore OriginalStyles of all HTML components in current view
	 * 
	 * @param context
	 * @param root
	 */
	private void restoreOriginalStyles(FacesContext context, UIViewRoot root) {
		Map<String, Object> session = context.getExternalContext().getSessionMap();

		if (session.containsKey(ORIGINAL_STYLE)) {

			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) session.get(ORIGINAL_STYLE);

			for (Map<String, String> item : list) {

				Map.Entry<String, String> entry = item.entrySet().iterator().next();
				UIComponent component = root.findComponent(entry.getKey());

				if (component != null) {
					component.getAttributes().put("styleClass", entry.getValue());
				}

			}

			session.remove(ORIGINAL_STYLE);
		}
	}

	/**
	 * restore OriginalTitle of all HTML components in current view
	 * 
	 * @param context
	 * @param root
	 */
	private void restoreOriginalTitle(FacesContext context, UIViewRoot root) {
		Map<String, Object> session = context.getExternalContext().getSessionMap();
		if (session.containsKey(ORIGINAL_TITLE)) {
			@SuppressWarnings("unchecked")
			List<Map<String, String>> list = (List<Map<String, String>>) session.get(ORIGINAL_TITLE);
			for (Map<String, String> item : list) {
				Map.Entry<String, String> entry = item.entrySet().iterator().next();
				UIComponent component = root.findComponent(entry.getKey());

				if (component != null) {
					component.getAttributes().put("title", entry.getValue());
				}
			}

			session.remove(ORIGINAL_TITLE);
		}
	}

	/**
	 * save Original Style of given HTML component by id in current view
	 * 
	 * @param id
	 * @param style
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	private void saveOriginalStyle(String id, String style, FacesContext context) {
		Map<String, Object> session = context.getExternalContext().getSessionMap();
		Map<String, String> originalStyle = new HashMap<>();
		originalStyle.put(id, style);

		if (session.get(ORIGINAL_STYLE) == null) {
			session.put(ORIGINAL_STYLE, new ArrayList<>());
		}

		((List<Map<String, String>>) session.get(ORIGINAL_STYLE)).add(originalStyle);
	}

	/**
	 * save Original Title of given HTML component by id in current view
	 * 
	 * @param id
	 * @param style
	 * @param context
	 */
	@SuppressWarnings("unchecked")
	private void saveOriginalTitle(String id, String style, FacesContext context) {
		Map<String, Object> session = context.getExternalContext().getSessionMap();
		Map<String, String> originalTitle = new HashMap<>();
		originalTitle.put(id, style);

		if (session.get(ORIGINAL_TITLE) == null) {
			session.put(ORIGINAL_TITLE, new ArrayList<>());
		}

		((List<Map<String, String>>) session.get(ORIGINAL_TITLE)).add(originalTitle);
	}

	@Override
	public void afterPhase(PhaseEvent phaseEvent) {
		// NavigationBean navigationBean = (NavigationBean) FacesContext.getCurrentInstance().getExternalContext()
		// .getSessionMap().get("navigationTree");
		// if (navigationBean != null) {
		// navigationBean.setRightClickedNodeObject(null);
		// }
	}

}