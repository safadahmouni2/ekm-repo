package vwg.vw.km.presentation.client.listener;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

public class ReverseProxyViewHandler extends ViewHandlerWrapper {
	static Log log = LogManager.get().getLog(ReverseProxyViewHandler.class);

	ViewHandler defaultHandler;

	/**
	 * Reverse Proxy ViewHandler
	 * 
	 * @param delegate
	 */
	public ReverseProxyViewHandler(ViewHandler delegate) {
		this.defaultHandler = delegate;

		log.info("ReverseProxyViewHandler:" + defaultHandler);

	}

	@Override
	public ViewHandler getWrapped() {
		// TODO Auto-generated method stub
		return defaultHandler;
	}

	/**
	 * get the action url
	 * 
	 * @param context
	 * @param viewId
	 * @param action
	 *            url
	 */
	@Override
	public String getActionURL(FacesContext context, String viewId) {

		String actionURL = defaultHandler.getActionURL(context, viewId);
		String jname = context.getExternalContext().getInitParameter("vwg.vw.km.faces.JUNCTION_NAME");
		if (log.isDebugEnabled()) {
			log.debug("getActionURL - prefijoProxy + actionURL:" + jname + actionURL);
		}
		if (jname != null && !"/".equals(jname)) {
			actionURL = jname + actionURL;
		}
		return actionURL;
	}

	/**
	 * get Resource url
	 * 
	 * @param context
	 * @param viewId
	 * @param Resource
	 *            url
	 */
	@Override
	public String getResourceURL(FacesContext context, String path) {
		String resourceUrl = defaultHandler.getResourceURL(context, path);
		String jname = context.getExternalContext().getInitParameter("vwg.vw.km.faces.JUNCTION_NAME");
		if (log.isDebugEnabled()) {
			log.debug("getActionURL - prefijoProxy + actionURL:" + jname + path);
		}
		if (jname != null && !"/".equals(jname) && path.equals("/")) {
			resourceUrl = jname + resourceUrl;
		}
		return resourceUrl;
	}

}
