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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.criterion.MatchMode;

import vwg.vw.km.integration.persistence.dao.ComponentDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;

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
 * @version $Revision: 1.52 $ $Date: 2018/01/30 11:11:12 $
 */
public class ComponentDAOImpl extends DBDaoImpl<ComponentModel> implements ComponentDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ComponentModel> getBusinessClass() {
		return ComponentModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentsByFolder(java.lang.Long,java.lang.Long,java.lang.Boolean)
	 */
	@Override
	public List<ComponentModel> getComponentsByFolder(Long folderId, Long loggedUser, Boolean inUse) {

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(root.get("folder").get("folderId").isNull());
		}
		if (inUse) {

			CriteriaQuery<ComponentVersionModel> criteriaVersion = builder.createQuery(ComponentVersionModel.class);

			Root<ComponentVersionModel> rootOwner = criteriaVersion.from(ComponentVersionModel.class);

			Predicate p1 = builder.equal(rootOwner.get("enumStatus").get("enumStatusId"), 5L);

			Query q = getSession()
					.createQuery(criteriaVersion.select(rootOwner.get("component").get("componentId")).where(p1));

			predicates.add(root.get("componentId").in(q.getResultList()));

		}

		/**
		 * 12/11/20113:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten Change column used for sort
		 **/

		criteria.select(root).where(predicates.toArray(new Predicate[] {}))
				.orderBy(builder.asc(root.get("displayOrder")));

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentByNmberOrDesignation(java.lang.Long, boolean,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ComponentModel getComponentByNmberOrDesignation(Long componentId, boolean loaded, String componentNumber,
			String designation, String ownerId) {
		ComponentModel componentModel = null;
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (componentNumber != null && designation != null) {

			predicates.add(builder.or(
					EscapingRestrictions.ilike(builder, root.get("componentNumber"), componentNumber, MatchMode.EXACT),
					EscapingRestrictions.ilike(builder, root.get("designation"), designation, MatchMode.EXACT)));

			predicates.add(builder.equal(root.get("owner").get("brandId"), ownerId));

			if (componentId != null && loaded) {

				predicates.add(builder.notEqual(root.get("componentId"), componentId));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[] {}));

			Query q = createQuery(criteria);

			componentModel = setModelAsLoaded(getSingleResultOrNull(q));

		}
		return componentModel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentByNmberOrDesignation(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public ComponentModel getComponentByNmberOrDesignation(String componentNumber, String designation, String ownerId) {
		ComponentModel componentModel = null;
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (componentNumber != null && designation != null) {
			predicates.add(builder.or(builder.equal(root.get("componentNumber"), componentNumber),
					builder.equal(root.get("designation"), designation)));

			predicates.add(builder.equal(root.get("owner").get("brandId"), ownerId));

			criteria.select(root).where(predicates.toArray(new Predicate[] {}));

			Query q = createQuery(criteria);

			@SuppressWarnings("unchecked")
			List<ComponentModel> returnValue = q.getResultList();

			if (returnValue.size() > 0) {
				componentModel = setModelAsLoaded(returnValue.get(0));
			}
		}
		return componentModel;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentsBySearchString(java.util.List,java.lang.Long,java.lang.String)
	 */
	@Override
	public List<ComponentModel> getComponentsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(root.get("folder").get("folderId").in(listOfFolders));

		predicates.add(builder.or(
				EscapingRestrictions.ilike(builder, root.get("designation"), searchString, MatchMode.ANYWHERE),
				EscapingRestrictions.ilike(builder, root.get("componentNumber"), searchString, MatchMode.ANYWHERE)));

		List<Order> orderList = new ArrayList<>();

		orderList.add(builder.asc(root.get("componentNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentsByStatus(java.util.List, java.lang.Long)
	 */
	@Override
	public List<ComponentModel> getComponentsByStatus(List<Long> searchStatus, Long loggedUser) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		CriteriaQuery<ComponentVersionModel> criteriaVersion = builder.createQuery(ComponentVersionModel.class);

		Root<ComponentVersionModel> rootOwner = criteriaVersion.from(ComponentVersionModel.class);

		Predicate p1 = rootOwner.get("enumStatus").get("enumStatusId").in(searchStatus);

		Query q = getSession()
				.createQuery(criteriaVersion.select(rootOwner.get("component").get("componentId")).where(p1));

		predicates.add(root.get("componentId").in(q.getResultList()));

		List<Order> orderList = new ArrayList<>();

		orderList.add(builder.asc(root.get("componentNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getInactiveComponentsByFolder(java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ComponentModel> getInactiveComponentsByFolder(Long folderId, Long loggedUser) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(builder.isNull(root.get("folder").get("folderId")));
		}

		predicates.add(builder.equal(root.get("inactive"), Boolean.TRUE));

		List<Order> orderList = new ArrayList<>();

		orderList.add(builder.asc(root.get("componentNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);
		return getModelList(createQuery(criteria));
	}

	/**
	 * 28-11-2013:ABA:PTS problem 34579 : get inactive value for component by folder ID and display order
	 * 
	 * @param order
	 * @param folderId
	 * @return
	 */
	private boolean getIsInactiveByOrderInFolder(Long order, Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		predicates.add(builder.equal(root.get("displayOrder"), order));

		criteria.distinct(true).select(root.get("inactive")).where(predicates.toArray(new Predicate[] {}));

		Query q = createQuery(criteria);
		Boolean inactive = (Boolean) q.getResultList().get(0);
		return inactive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentsForWorkingFolder(java.util.List,
	 * java.lang.Long, java.lang.Long)
	 */
	@Override
	public List<ComponentModel> getComponentsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);

		// ZS: PTS_Problem-34863: Review the query of loading EL/BS of the user working folder
		List<Order> orderList = new ArrayList<>();

		orderList.add(builder.asc(root.get("componentNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(builder.equal(root.get("folder").get("folderId"), folderId)).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getComponentByExternalIdAndLib(java.lang.String,
	 * java.lang.Long)
	 */
	@Override
	public ComponentModel getComponentByExternalIdAndLib(String externalId, Long librayId) {
		ComponentModel componentModel = null;
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ComponentModel> root = criteria.from(ComponentModel.class);
		criteria.select(root);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (externalId != null) {

			predicates.add(builder.equal(root.get("externalId"), externalId));

			if (librayId != null) {
				predicates.add(builder.equal(root.get("library").get("libraryId"), librayId));
			}

			predicates.add(builder.equal(root.get("inactive"), Boolean.FALSE));

			criteria.distinct(true).select(root).where(predicates.toArray(new Predicate[] {}));

			Query q = createQuery(criteria);
			q.setMaxResults(1);
			@SuppressWarnings("unchecked")
			List<ComponentModel> returnValue = q.getResultList();

			if (returnValue.size() > 0) {
				componentModel = setModelAsLoaded(returnValue.get(0));
			}

		}
		return componentModel;
	}

	// 12/11/20113:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten
	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getMaxDisplayOrderByFolderId(java.lang.Long)
	 */
	@Override
	public Long getMaxDisplayOrderByFolderId(Long folderId) {
		Query query = getSession()
				.createSQLQuery("select COALESCE(max(ANZEIGE_ORDER),0) from T_BAUSTEIN where ORDNER_REF_ID= :folderId");
		query.setParameter("folderId", folderId);
		return new Long(query.getResultList().get(0).toString());
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getMaxDisplayOrderByFolderId(java.lang.Long)
	 */
	public Long getCurrentPositionForSelectedNodeInFolder(Long componentId) {

		Query query = getSession().createSQLQuery("select ANZEIGE_ORDER from T_BAUSTEIN where ID= :componentId");
		query.setParameter("componentId", componentId);
		Long maxValue = ((BigDecimal) query.getResultList().get(0)).longValue();
		return maxValue;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#changeSelectedComponentOrder(Long,int)
	 */
	public void changeSelectedComponentOrder(Long componentId, int newPosition) {
		Query query = getSession()
				.createSQLQuery("update T_BAUSTEIN set ANZEIGE_ORDER = :switchPosition where ID = :componentId ");
		query.setParameter("switchPosition", newPosition);
		query.setParameter("componentId", componentId);
		query.executeUpdate();
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#UpdateDisplayOrdersAfterDragAndDrop(java.lang.Long,java.lang.Long)
	 */
	@Override
	public Long updateDisplayOrdersForOtherNodes(Long componentId, Long folderId) {
		Long nodeCurrentPosition = getCurrentPositionForSelectedNodeInFolder(componentId);
		Long maxOrderInFolder = getMaxDisplayOrderByFolderId(folderId);

		Query query = getSession().createSQLQuery(
				"update T_BAUSTEIN  set ANZEIGE_ORDER = (ANZEIGE_ORDER-1)  where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER between :startPosition and :maxPosition");
		query.setParameter("startPosition", nodeCurrentPosition.intValue() + 1);
		query.setParameter("maxPosition", maxOrderInFolder);
		query.setParameter("folderId", folderId);
		query.executeUpdate();
		return maxOrderInFolder;

	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#performUpOrDownOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */
	public void performUpOrDownOperation(Long componentId, Long folderId, String operation, Long nodeCurrentPosition) {
		int limitOrder = 0;
		Long switchNodePosition = null;

		if (operation.equals("UP")) {
			limitOrder = 1;
			switchNodePosition = nodeCurrentPosition - 1;
			// PTS problem 34579 : get the first switch component position which is visible (not inactive),
			while ((switchNodePosition > 0) && getIsInactiveByOrderInFolder(switchNodePosition, folderId)) {
				switchNodePosition--;
			}
		} else if (operation.equals("DOWN")) {
			limitOrder = getMaxDisplayOrderByFolderId(folderId).intValue();
			switchNodePosition = nodeCurrentPosition + 1;
			// PTS problem 34579 : get the first switch component position which is visible (not inactive),
			while ((switchNodePosition <= limitOrder) && getIsInactiveByOrderInFolder(switchNodePosition, folderId)) {
				switchNodePosition++;
			}
		}
		// Update switch node position
		if (nodeCurrentPosition != limitOrder) {
			Query query1 = getSession().createSQLQuery(
					"update T_BAUSTEIN  set ANZEIGE_ORDER = :currentPosition where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER = :switchPosition");
			query1.setParameter("currentPosition", nodeCurrentPosition);
			query1.setParameter("folderId", folderId);
			query1.setParameter("switchPosition", switchNodePosition);
			query1.executeUpdate();

			// Finally update the one we moved to correct value
			changeSelectedComponentOrder(componentId, switchNodePosition.intValue());

		}
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#performMoveToTopOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */
	public void performMoveToTopOperation(Long componentId, Long folderId, Long nodeCurrentPosition) {

		// Update other nodes position
		Query query1 = getSession().createSQLQuery(
				"update T_BAUSTEIN  set ANZEIGE_ORDER = (ANZEIGE_ORDER+1) where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER between 1 and :maxPosition");
		query1.setParameter("maxPosition", nodeCurrentPosition.intValue() - 1);
		query1.setParameter("folderId", folderId);
		query1.executeUpdate();

		// Finally update the one we moved to correct value
		changeSelectedComponentOrder(componentId, 1);
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#performMoveToBottomOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */

	public void performMoveToBottomOperation(Long componentId, Long folderId, Long nodeCurrentPosition) {

		// Update other nodes position
		Long maxOrderInFolder = updateDisplayOrdersForOtherNodes(componentId, folderId);

		// Finally update the one we moved to correct value
		changeSelectedComponentOrder(componentId, maxOrderInFolder.intValue());
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#performElementChangeOrder(Long,Long,String)
	 */
	@Override
	public void performComponentChangeOrder(Long componentId, Long folderId, String operation) {
		// Get selected node origin position
		Long nodeCurrentPosition = getCurrentPositionForSelectedNodeInFolder(componentId);

		if (operation.equals("TOP")) {
			performMoveToTopOperation(componentId, folderId, nodeCurrentPosition);
		}
		if (operation.equals("BOTTOM")) {
			performMoveToBottomOperation(componentId, folderId, nodeCurrentPosition);
		}
		if (operation.equals("UP") || operation.equals("DOWN")) {
			performUpOrDownOperation(componentId, folderId, operation, nodeCurrentPosition);
		}
	}

	// 14/11/2013:ABA: PTS problem 23631:Optimize Empty filter for components
	@SuppressWarnings("unchecked")
	/**
	 * Get Active components and incative but used by user brand components list
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getElementsListToBeFiltred(Long,String)
	 */
	@Override
	public List<Long> getComponentsListToBeFiltred(Long folderId, String userBrand) {

		List<Long> bausteinIds = new ArrayList<Long>();

		Query query = getSession()
				.createSQLQuery("SELECT ID from T_BAUSTEIN where ORDNER_REF_ID= :folderId and X_INAKTIV=0 ");
		query.setParameter("folderId", folderId);

		bausteinIds = query.getResultList();

		Query query1 = getSession().createSQLQuery(
				"SELECT T_BAUSTEIN.Id FROM T_BAUSTEIN RIGHT JOIN T_BAUSTEINVERSION ON T_BAUSTEIN.ID = T_BAUSTEINVERSION.KB_REF_UU_ID "
						+ " RIGHT JOIN T_BAUSTEINSTAND ON T_BAUSTEINVERSION.KB_VERSION_UU_ID = T_BAUSTEINSTAND.KB_VERSION_REF_ID RIGHT JOIN T_REL_BAUSVERSION_NUTZER "
						+ " on T_REL_BAUSVERSION_NUTZER.KB_STAND_ID = T_BAUSTEINSTAND.ID Where T_REL_BAUSVERSION_NUTZER.MARKE_REF_ID = :userBrand and T_BAUSTEIN.X_INAKTIV=1 "
						+ " and T_BAUSTEIN.ORDNER_REF_ID= :folderId");
		query1.setParameter("userBrand", userBrand);
		query1.setParameter("folderId", folderId);
		bausteinIds.addAll(query1.getResultList());
		return bausteinIds;

	}

	/**
	 * Check if folder has components
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getHasComponents(Long,String)
	 */
	@Override
	public boolean getHasComponents(Long folderId) {

		Query query = getSession().createSQLQuery("SELECT count(*) from T_BAUSTEIN where ORDNER_REF_ID= :folderId");
		query.setParameter("folderId", folderId);

		if (new Integer(query.getResultList().get(0).toString()) == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Check if folder has inactive components
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getInactiveComponentIds(Long,String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getInactiveComponentIds(Long folderId) {
		Query query = getSession()
				.createSQLQuery("SELECT ID from T_BAUSTEIN where ORDNER_REF_ID= :folderId and X_INAKTIV=1 ");
		query.setParameter("folderId", folderId);
		return query.getResultList();
	}

	/**
	 * Check if folder has components that should be visible by owner filter
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#hasVisibleComponentsByOwnerFilter(List,String,List)
	 */
	@Override
	public boolean hasVisibleComponentsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> bausteinIds) {

		if (!ownerFilters.isEmpty() && !bausteinIds.isEmpty()) {
			Query query3 = getSession().createSQLQuery(
					"select count(*) from T_BAUSTEIN where BESITZER in (:brands) and ID in (:bausteinIds)");

			query3.setParameter("brands", ownerFilters);
			query3.setParameter("bausteinIds", bausteinIds);
			if (new Integer(query3.getResultList().get(0).toString()) != 0) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Check if folder has components that should be visible by users filter
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#hasVisibleElementsByUsersFilter(List,String,List)
	 */
	@Override
	public boolean hasVisibleComponentsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> bausteinIds) {
		if (!usersFilters.isEmpty() && !bausteinIds.isEmpty()) {
			Query query4 = getSession().createSQLQuery(
					"SELECT count(T_BAUSTEIN.Id) FROM T_BAUSTEIN RIGHT JOIN T_BAUSTEINVERSION ON T_BAUSTEIN.ID = T_BAUSTEINVERSION.KB_REF_UU_ID "
							+ " RIGHT JOIN T_BAUSTEINSTAND ON T_BAUSTEINVERSION.KB_VERSION_UU_ID = T_BAUSTEINSTAND.KB_VERSION_REF_ID RIGHT JOIN T_REL_BAUSVERSION_NUTZER"
							+ " on T_REL_BAUSVERSION_NUTZER.KB_STAND_ID = T_BAUSTEINSTAND.ID Where T_REL_BAUSVERSION_NUTZER.MARKE_REF_ID in( :usersBrands) "
							+ " and T_BAUSTEIN.Id in(:bausteinIds)");
			query4.setParameter("usersBrands", usersFilters);
			query4.setParameter("bausteinIds", bausteinIds);

			if (new Integer(query4.getResultList().get(0).toString()) != 0) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getAllComponents()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllComponents() {
		Query query = getSession().createSQLQuery("SELECT BAUSTEIN_NR,BEZEICHNUNG,X_INAKTIV,ERSTELLER_REF_ID,"
				+ "ERSTELLT_AM,GEAENDERT_VON_REF_ID, GEAENDERT_AM,INAKTIV_VON,ORDNER_REF_ID, BESITZER from T_BAUSTEIN");
		return query.getResultList();
	}

	@Override
	public ComponentModel getComponentByExternalIdAndBrand(String externalId, String brandId) {
		ComponentModel componentModel = null;

		if (externalId != null && brandId != null) {

			CriteriaBuilder builder = createBuidler();
			CriteriaQuery<ComponentModel> criteria = createCriteria(getBusinessClass(), builder);

			Root<ComponentModel> root = criteria.from(ComponentModel.class);
			List<Predicate> predicates = new ArrayList<Predicate>();
			predicates.add(builder.equal(root.get("externalId"), externalId));
			predicates.add(builder.equal(root.get("owner.brandId"), brandId));

			criteria.distinct(true).select(root).where(predicates.toArray(new Predicate[] {}));

			Query q = createQuery(criteria);
			q.setMaxResults(1);
			componentModel = setModelAsLoaded((ComponentModel) q.getResultList());

		}
		return componentModel;
	}
}
