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
package vwg.vw.km.application.service.dto;

import java.util.ArrayList;
import java.util.List;

import vwg.vw.km.application.service.dto.base.BaseDTO;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;

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
 * @author boubakers changed by $Author: benamora $
 * @version $Revision: 1.11 $ $Date: 2013/11/20 17:30:55 $
 */
public class ChangeHistoryDTO extends BaseDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5633825828930559228L;

	private SearchHistoryObject searchObject;

	private List<BrandModel> brands;

	private List<ElementVersionChangeModel> elementChangesResults = new ArrayList<ElementVersionChangeModel>();

	private List<ElementVersionChangeModel> elementChangesDetails = new ArrayList<ElementVersionChangeModel>();

	private List<ComponentStandChangeModel> componentChangesResults = new ArrayList<ComponentStandChangeModel>();

	private List<ComponentStandChangeModel> componentChangesDetails = new ArrayList<ComponentStandChangeModel>();

	private List<Long> sortedElementId = new ArrayList<Long>();

	private List<Long> sortedComponentId = new ArrayList<Long>();

	private String objectName;

	private String objectId;

	private String objectFolderId;

	private boolean fromLastChangesPage;

	/**
	 * PTS requirement 22211:ABA :Benachrichtigungsfunktion Add noSearchResult in ChangeHistoryDTO
	 */
	private boolean noSearchResultForLastChanges = Boolean.FALSE;;

	/*
	 * PTS requirement 22207: Add sort feature to dataTables Responsible :ABA Default search column for search results
	 * dataTables in changes search mask
	 */
	private String elementChangesResulsListSortColumnName;
	private String componentChangesResulsListSortColumnName;

	private Boolean changeVersionAscendingForComponents = true;

	public Boolean getChangeVersionAscendingForComponents() {
		return changeVersionAscendingForComponents;
	}

	public void setChangeVersionAscendingForComponents(Boolean changeVersionAscendingForComponents) {
		this.changeVersionAscendingForComponents = changeVersionAscendingForComponents;
	}

	private Boolean changeVersionAscendingForElements = true;

	public Boolean getChangeVersionAscendingForElements() {
		return changeVersionAscendingForElements;
	}

	public void setChangeVersionAscendingForElements(Boolean changeVersionAscendingForElements) {
		this.changeVersionAscendingForElements = changeVersionAscendingForElements;
	}

	public ChangeHistoryDTO() {
		searchObject = new SearchHistoryObject();

	}

	public List<ElementVersionChangeModel> getElementChangesResults() {
		return elementChangesResults;
	}

	public void setElementChangesResults(List<ElementVersionChangeModel> elementChangesResults) {
		this.elementChangesResults = elementChangesResults;
	}

	public List<ComponentStandChangeModel> getComponentChangesResults() {
		return componentChangesResults;
	}

	public void setComponentChangesResults(List<ComponentStandChangeModel> componentChangesResults) {
		this.componentChangesResults = componentChangesResults;
	}

	public SearchHistoryObject getSearchObject() {
		return searchObject;
	}

	public void setSearchObject(SearchHistoryObject searchObject) {
		this.searchObject = searchObject;
	}

	public List<BrandModel> getBrands() {
		return brands;
	}

	public void setBrands(List<BrandModel> brands) {
		this.brands = brands;
	}

	public List<ElementVersionChangeModel> getElementChangesDetails() {
		return elementChangesDetails;
	}

	public void setElementChangesDetails(List<ElementVersionChangeModel> elementChangesDetails) {
		this.elementChangesDetails = elementChangesDetails;
	}

	public List<ComponentStandChangeModel> getComponentChangesDetails() {
		return componentChangesDetails;
	}

	public void setComponentChangesDetails(List<ComponentStandChangeModel> componentChangesDetails) {
		this.componentChangesDetails = componentChangesDetails;
	}

	public List<Long> getSortedElementId() {
		return sortedElementId;
	}

	public void setSortedElementId(List<Long> sortedElementId) {
		this.sortedElementId = sortedElementId;
	}

	public List<Long> getSortedComponentId() {
		return sortedComponentId;
	}

	public void setSortedComponentId(List<Long> sortedComponentId) {
		this.sortedComponentId = sortedComponentId;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getObjectFolderId() {
		return objectFolderId;
	}

	public void setObjectFolderId(String folderId) {
		this.objectFolderId = folderId;
	}

	public String getElementChangesResulsListSortColumnName() {
		return elementChangesResulsListSortColumnName;
	}

	public void setElementChangesResulsListSortColumnName(String elementChangesResulsListSortColumnName) {
		this.elementChangesResulsListSortColumnName = elementChangesResulsListSortColumnName;
	}

	public String getComponentChangesResulsListSortColumnName() {
		return componentChangesResulsListSortColumnName;
	}

	public void setComponentChangesResulsListSortColumnName(String componentChangesResulsListSortColumnName) {
		this.componentChangesResulsListSortColumnName = componentChangesResulsListSortColumnName;
	}

	public boolean getFromLastChangesPage() {
		return fromLastChangesPage;
	}

	public void setFromLastChangesPage(boolean isFromLastChangesPage) {
		this.fromLastChangesPage = isFromLastChangesPage;
	}

	public boolean getNoSearchResultForLastChanges() {
		return noSearchResultForLastChanges;
	}

	public void setNoSearchResultForLastChanges(boolean noSearchResultForLastChanges) {
		this.noSearchResultForLastChanges = noSearchResultForLastChanges;
	}

	public void clearOldResultList() {
		if (getComponentChangesResults() != null) {
			getComponentChangesResults().clear();
		}
		if (getElementChangesResults() != null) {
			getElementChangesResults().clear();
		}
		if (getSearchObject().getElementId() != null) {
			getSearchObject().getElementId().clear();
		}
		if (getSearchObject().getComponentId() != null) {
			getSearchObject().getComponentId().clear();
		}
		if (getSortedComponentId() != null) {
			getSortedComponentId().clear();
		}
		if (getSortedElementId() != null) {
			getSortedElementId().clear();
		}

	}
}
