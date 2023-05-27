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

package vwg.vw.km.application.util.xml;

import java.util.List;

import vwg.vw.km.integration.persistence.model.FolderModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author benhmedm changed by $Author: saidi $
 * @version $Revision: 1.2 $ $Date: 2012/10/08 13:21:47 $
 */
public class XmlFolder {

	private FolderModel folderModel;

	private List<String> folderChilds;

	private boolean isAlreadyExist = Boolean.FALSE;

	/**
	 * @return the folderModel
	 */
	public FolderModel getFolderModel() {
		return folderModel;
	}

	/**
	 * @param folderModel
	 *            the folderModel to set
	 */
	public void setFolderModel(FolderModel folderModel) {
		this.folderModel = folderModel;
	}

	/**
	 * @return the folderChilds
	 */
	public List<String> getFolderChilds() {
		return folderChilds;
	}

	/**
	 * @param folderChilds
	 *            the folderChilds to set
	 */
	public void setFolderChilds(List<String> folderChilds) {
		this.folderChilds = folderChilds;
	}

	public boolean isAlreadyExist() {
		return isAlreadyExist;
	}

	public void setAlreadyExist(boolean isAlreadyExist) {
		this.isAlreadyExist = isAlreadyExist;
	}
}
