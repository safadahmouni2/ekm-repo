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

import vwg.vw.km.integration.persistence.model.DocumentPoolModel;

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
 * @version $Revision: 1.2 $ $Date: 2011/05/23 10:07:46 $
 */
public class UploadedFile {
	private FileResource myResource;

	private DocumentPoolModel documentPool;

	/**
	 * @param myResource
	 * @param documentPool
	 */
	public UploadedFile(FileResource myResource, DocumentPoolModel documentPool) {
		this.myResource = myResource;
		this.documentPool = documentPool;
	}

	public FileResource getMyResource() {
		return myResource;
	}

	public void setMyResource(FileResource myResource) {
		this.myResource = myResource;
	}

	public DocumentPoolModel getDocumentPool() {
		return documentPool;
	}

	public void setDocumentPool(DocumentPoolModel documentPool) {
		this.documentPool = documentPool;
	}

}