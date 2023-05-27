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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.faces.context.FacesContext;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: zouhair $
 * @version $Revision: 1.11 $ $Date: 2021/07/15 11:13:20 $
 */
public class FileResource extends javax.faces.application.Resource implements java.io.Serializable {

	private static final long serialVersionUID = 8836773460641308917L;

	private final Log log = LogManager.get().getLog(FileResource.class);

	private String path = "";

	private HashMap<String, String> headers;

	private byte[] bytes;

	public FileResource(byte[] content, String resourceName) {
		setResourceName(encodeContentDispositionFilename(resourceName));
		if (content != null) {
			this.bytes = content;
		} else {
			this.bytes = new byte[0];
		}
		this.headers = new HashMap<String, String>();
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.application.Resource#getInputStream()
	 */
	public InputStream getInputStream() {
		return new ByteArrayInputStream(this.bytes);
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.application.Resource#getURL()
	 */
	public URL getURL() {
		return null;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.application.Resource#getResponseHeaders()
	 */
	public Map<String, String> getResponseHeaders() {
		return headers;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.application.Resource#getRequestPath()
	 */
	public String getRequestPath() {
		return path;
	}

	public void setRequestPath(String path) {
		this.path = path;
	}

	public byte[] getContent() {
		return this.bytes;
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see javax.faces.application.Resource#context()
	 */
	public boolean userAgentNeedsUpdate(FacesContext context) {
		return false;
	}

	/**
	 * We use this behavior to offer correct file name for download.
	 * 
	 * @param fileName
	 * @return encoded file name
	 */
	private String encodeContentDispositionFilename(String fileName) {
		if (fileName == null || fileName.trim().length() == 0) {
			return null;
		}
		// ZS: PTS_Problem-26725: replace all spaces in the file name by _
		fileName = fileName.replaceAll("\\s+", "_");
		String defaultFileName = fileName;
		try {
			return URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			log.error("Unsupported Encoding " + e.getMessage());
		}
		return defaultFileName;
	}
}