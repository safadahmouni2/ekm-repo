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
package vwg.vw.km.integration.persistence.dao.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.integration.persistence.dao.FolderDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
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
 * .
 * 
 * @author Sebri Zouhaier changed by $Author: saidi $
 * @version $Revision: 1.22 $ $Date: 2013/11/25 13:14:32 $
 */
public class FolderDAOImpl extends DBDaoImpl<FolderModel> implements FolderDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<FolderModel> getBusinessClass() {
		return FolderModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getFoldersUnder(java.lang.Long,java.lang.Long)
	 */
	@Override
	public List<FolderModel> getFoldersUnder(Long folderId, Long objectTypeId) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<FolderModel> root = criteria.from(FolderModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		predicates.add(builder.equal(root.get("objectType"), objectTypeId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.get("designation")));
		Query q = createQuery(criteria);
		return getModelList(q);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getWorkingFoldersUnder(java.lang.Long, java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<FolderModel> getWorkingFoldersUnder(Long folderId, Long objectTypeId, Long userId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<FolderModel> root = criteria.from(FolderModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(root.get("folder").get("folderId").isNull());
		}
		if (userId != null) {
			predicates.add(builder.equal(root.get("creatorRefId"), userId));
		} else {
			predicates.add(root.get("creatorRefId").isNotNull());
		}
		predicates.add(builder.equal(root.get("objectType").get("enumObjectTypeId"), objectTypeId));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getFolderUnderParentByName(Long, Long, String)
	 */
	@Override
	public FolderModel getFolderUnderParentByName(Long parentFolderId, Long folderId, String name) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);
		
		Root<FolderModel> root = criteria.from(FolderModel.class);
		
		List<Predicate> predicates = new ArrayList<Predicate>();
		
		predicates.add(builder.equal(root.get("folder").get("folderId"), parentFolderId));
		predicates.add(builder.notEqual(root.get("folderId"), folderId));
		predicates.add(EscapingRestrictions.ilike(builder, root.get("designation"), name, MatchMode.EXACT));
		
		
		criteria.select(root).where(predicates.toArray(new Predicate[] {}));
		Query q = createQuery(criteria);

		return setModelAsLoaded(getSingleResultOrNull(q));
	}

	@Override
	public Long getMaxFolderId() {
		return (Long) getSession().createQuery("select max(e.folderId) from FolderModel e").list().get(0);
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getFolderIdsHierarchy(Long)
	 */
	@Override
	public List<Long> getFolderIdsHierarchy(Long startFolderId) {

		List<Long> retrunList = new ArrayList<Long>();

		Query query = getSession().createSQLQuery(
				"select ID from T_ORDNER  start with ID =:startFolderId" + " connect by prior ordner_ref_id = ID");
		query.setParameter("startFolderId", startFolderId);
		@SuppressWarnings("unchecked")
		List<BigDecimal> list = query.getResultList();

		for (int i = list.size() - 1; i >= 0; i--) {
			Long item = new Long(list.get(i).longValue());
			retrunList.add(item);
		}
		return retrunList;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getComponentExportedFolders()
	 */
	@Override
	public List<FolderModel> getComponentExportedFolders() {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<FolderModel> root = criteria.from(FolderModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.ge(root.get("folderId"), 7L));
		predicates.add(builder.ge(root.get("objectType").get("enumObjectTypeId"), 2L));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}));

		return getModelList(createQuery(criteria));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getFolderByExternalID(java.lang.String)
	 */
	@Override
	public FolderModel getFolderByExternalID(String externalID) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<FolderModel> root = criteria.from(FolderModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("externalId"), externalID));

		criteria.distinct(true).select(root).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria);
		q.setMaxResults(1);
		return setModelAsLoaded((FolderModel) q.getResultList());
	}

	@Override
	public boolean getHasFoldersUnder(Long folderId, Long objectTypeId) {
		Query query = getSession()
				.createQuery("select count(e.folderId) from FolderModel e where e.folder.folderId=:folderId "
						+ "and objectType.enumObjectTypeId=:objectTypeId");
		query.setParameter("folderId", folderId);
		query.setParameter("objectTypeId", objectTypeId);
		Long count = (Long) query.getResultList().get(0);
		return (count > 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.FolderDAO#getFoldersBySearchString(java.lang.Long, java.lang.Long,
	 * java.lang.String)
	 */
	@Override
	public List<FolderModel> getFoldersBySearchString(Long parentFolder, Long objectTypeId, String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<FolderModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<FolderModel> root = criteria.from(FolderModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		Predicate p1 = builder.equal(root.get("folder").get("folderId"), parentFolder);
		Predicate p2 = root.get("folder").get("folderId").isNotNull();
		Predicate p3 = builder.and(root.get("creatorRefId").isNotNull(),
				root.get("folder").get("folderId").isNotNull());


		// if the search is started from the root folder, search will include the working folders
		if (parentFolder != null && (parentFolder.equals(7L) || parentFolder.equals(6L))) {
			predicates.add(builder.or(p2, p3));
		} else {
			predicates.add(p1);
		}

		predicates.add(builder.equal(root.get("objectType"), objectTypeId));
		predicates.add(
				EscapingRestrictions.ilike(builder, root.get("designation"), searchString, MatchMode.ANYWHERE));

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.get("designation")));

		return getModelList(createQuery(criteria));
	}
}
