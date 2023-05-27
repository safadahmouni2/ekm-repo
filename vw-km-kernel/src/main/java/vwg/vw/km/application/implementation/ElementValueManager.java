package vwg.vw.km.application.implementation;

// Generated Feb 24, 2011 12:09:56 PM by Hibernate Tools 3.2.0.b9

import java.util.List;

import vwg.vw.km.application.implementation.base.BaseManager;
import vwg.vw.km.integration.persistence.dao.ElementValueDAO;
import vwg.vw.km.integration.persistence.model.ElementValueModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;

/**
 * @hibernate.class table="T_ELEMENTWERTE"
 * 
 */
public interface ElementValueManager extends BaseManager<ElementValueModel, ElementValueDAO> {

	/**
	 * 
	 * @param versionId
	 * @return ElementValues By ElementVersionId
	 */
	public List<ElementValueModel> getElementValuesByElementVersionId(Long versionId);

	/**
	 * copy ElementValues from a ElementVersionModel to another
	 * 
	 * @param from
	 * @param to
	 */
	public void copyElementValues(ElementVersionModel from, ElementVersionModel to);

	/**
	 * 
	 * @param listToFilter
	 * @param searchCriteria
	 * @return filtered list of ElementValueModel
	 */
	public List<ElementValueModel> filterElementValues(List<ElementValueModel> listToFilter, String searchCriteria);

	public void saveOrRemoveElementValue(ElementVersionModel elementVersion);

}
