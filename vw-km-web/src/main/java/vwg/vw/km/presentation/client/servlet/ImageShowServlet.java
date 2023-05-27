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
package vwg.vw.km.presentation.client.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.presentation.client.util.FileResource;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Servlet used to get the content of the image preview of a baustein. object FileResource is already
 * prepared in the bean. If imagePreview object is null return HTTP 404 error.
 * </p>
 * <p>
 * Copyright: VW (c) 2015
 * </p>
 * 
 * @author saidi changed by $Author: zouhair $
 * @version $Revision: 1.3 $ $Date: 2016/11/10 15:22:47 $
 */
public class ImageShowServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6199166972439146572L;

	private final static Log log = LogManager.get().getLog(ImageShowServlet.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		// no cache
		response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		response.setDateHeader("Expires", 0);
		// Cost component Bean in not scope session pleasee see code in FileUploadServlet
		// CostComponentBean bean = (CostComponentBean)request.getSession().getAttribute("costComponentBean");
		// FileResource image= bean.getImagePreview();
		FileResource image = null;
		if (image == null) {
			// Throw an exception, or send 404, or show default/warning image, or just ignore it.
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404.
			return;
		}

		log.info("Get content of File: " + image.getResourceName());
		response.getOutputStream().write(image.getContent());

	}
}