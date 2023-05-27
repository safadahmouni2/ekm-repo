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

import vwg.vw.km.integration.persistence.dao.ElementDAO;
import vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl;
import vwg.vw.km.integration.persistence.hibernate.EscapingRestrictions;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionModel;

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
 * @version $Revision: 1.50 $ $Date: 2013/12/19 15:24:27 $
 */
public class ElementDAOImpl extends DBDaoImpl<ElementModel> implements ElementDAO {

	/**
	 * @see vwg.vw.km.integration.persistence.dao.impl.base.DBDaoImpl#getBusinessClass()
	 */
	@Override
	protected Class<ElementModel> getBusinessClass() {
		return ElementModel.class;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementsByFolder(java.lang.Long,java.lang.Long,java.util.List)
	 */
	@Override
	public List<ElementModel> getElementsByFolder(Long folderId, Long loggedUser, List<Long> status) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(root.get("folder").get("folderId").isNull());
		}
		if (status != null && !status.isEmpty()) {
			CriteriaQuery<ElementVersionModel> ownerCriteria = builder.createQuery(ElementVersionModel.class);

			Root<ElementVersionModel> rootOwner = ownerCriteria.from(ElementVersionModel.class);

			ownerCriteria.select(rootOwner.get("element").get("elementId"))
					.where(rootOwner.get("enumStatus").get("enumStatusId").in(status));

			predicates.add(root.get("elementId").in(getSession().createQuery(ownerCriteria).getResultList()));

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
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementByNmberOrDesignation(java.lang.Long, boolean,
	 * java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public ElementModel getElementByNmberOrDesignation(Long elementId, boolean loaded, String elementNumber,
			String designation, String ownerId) {

		ElementModel elementModel = null;

		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);
		List<Predicate> predicates = new ArrayList<Predicate>();
		if (elementNumber != null && designation != null) {
			predicates.add(builder.or(
					EscapingRestrictions.ilike(builder, root.get("elementNumber"), elementNumber, MatchMode.EXACT),
					EscapingRestrictions.ilike(builder, root.get("designation"), designation, MatchMode.EXACT)));
			predicates.add(builder.equal(root.get("owner").get("brandId"), ownerId));
			if (elementId != null && loaded) {
				predicates.add(builder.notEqual(root.get("elementId"), elementId));
			}

			criteria.select(root).where(predicates.toArray(new Predicate[] {}));

			Query q = createQuery(criteria);
			if (q.getResultList().size() > 0) {
				elementModel = setModelAsLoaded((ElementModel) q.getResultList().get(0));
			}
		}
		return elementModel;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementsBySearchString(java.util.List,java.lang.Long,java.lang.String)
	 */
	@Override
	public List<ElementModel> getElementsBySearchString(List<Long> listOfFolders, Long loggedUser,
			String searchString) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		Predicate p1 = root.get("folder").get("folderId").in(listOfFolders);
		Predicate p2 = EscapingRestrictions.ilike(builder, root.get("designation"), searchString,
				MatchMode.ANYWHERE);
		Predicate p3 = EscapingRestrictions.ilike(builder, root.get("elementNumber"), searchString,
				MatchMode.ANYWHERE);

		predicates.add(p1);
		predicates.add(builder.or(p2, p3));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("elementNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);
		return getModelList(createQuery(criteria));
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementsByStatus(java.util.List,java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ElementModel> getElementsByStatus(List<Long> searchStatus, Long loggedUser) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		CriteriaQuery<ElementVersionModel> ownerCriteria = builder.createQuery(ElementVersionModel.class);

		Root<ElementVersionModel> rootOwner = ownerCriteria.from(ElementVersionModel.class);

		ownerCriteria.select(rootOwner.get("element").get("elementId"))
				.where(rootOwner.get("enumStatus").get("enumStatusId").in(searchStatus));

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("elementNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(root.get("elementId").in(getSession().createQuery(ownerCriteria).getResultList()))
				.orderBy(orderList);

		return setModelsListAsLoaded(createQuery(criteria).getResultList());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getInactiveElements(java.lang.Long)
	 */
	@Override
	public List<ElementModel> getInactiveElements(Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();

		if (folderId != null) {
			predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		} else {
			predicates.add(root.get("folder").get("folderId").isNull());
		}

		predicates.add(builder.equal(root.get("inactive"), Boolean.TRUE));
		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("elementNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(predicates.toArray(new Predicate[] {})).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	/**
	 * 28-11-2013:ABA:PTS problem 34579 : get inactive value for element by folder ID and display order
	 * 
	 * @param order
	 * @param folderId
	 * @return
	 */
	private boolean getIsInactiveByOrderInFolder(Long order, Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		List<Predicate> predicates = new ArrayList<Predicate>();
		predicates.add(builder.equal(root.get("folder").get("folderId"), folderId));
		predicates.add(builder.equal(root.get("displayOrder"), order));

		criteria.select(root.get("inactive")).where(predicates.toArray(new Predicate[] {}));

		Boolean inactive = (Boolean) createQuery(criteria).getSingleResult();
		return inactive;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementsForWorkingFolder(java.util.List, java.lang.Long,
	 * java.lang.Long)
	 */
	@Override
	public List<ElementModel> getElementsForWorkingFolder(List<Long> searchStatus, Long userId, Long folderId) {
		CriteriaBuilder builder = createBuidler();
		CriteriaQuery<ElementModel> criteria = createCriteria(getBusinessClass(), builder);

		Root<ElementModel> root = criteria.from(ElementModel.class);

		// ZS: PTS_Problem-34863: Review the query of loading EL/BS of the user working folder

		List<Order> orderList = new ArrayList<Order>();

		orderList.add(builder.asc(root.get("elementNumber")));
		orderList.add(builder.asc(root.get("designation")));

		criteria.select(root).where(builder.equal(root.get("folder").get("folderId"), folderId)).orderBy(orderList);

		return getModelList(createQuery(criteria));
	}

	// 12-11-20113:ABA: PTS requirement 22190:Individuelle Platzierung von Objekten

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getMaxDisplayOrderByFolderId(java.lang.Long)
	 */
	@Override
	public Long getMaxDisplayOrderByFolderId(Long folderId) {
		Query query = getSession()
				.createSQLQuery("select COALESCE(max(ANZEIGE_ORDER),0) from T_ELEMENT where ORDNER_REF_ID= :folderId");
		query.setParameter("folderId", folderId);
		return new Long(query.getSingleResult().toString());
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getMaxDisplayOrderByFolderId(java.lang.Long)
	 */
	public Long getCurrentPositionForSelectedNodeInFolder(Long elementId) {

		Query query = getSession().createSQLQuery("select ANZEIGE_ORDER from T_ELEMENT where UUIDKE= :elementId");
		query.setParameter("elementId", elementId);
		Long maxValue = ((BigDecimal) query.getSingleResult()).longValue();
		return maxValue;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#changeSelectedElementOrder(Long,int)
	 */
	public void changeSelectedElementOrder(Long elementId, int newPosition) {
		Query query = getSession().createSQLQuery(
				"update T_ELEMENT set ANZEIGE_ORDER = :selectedNodeNewPosition where UUIDKE = :elementId ");
		query.setParameter("selectedNodeNewPosition", newPosition);
		query.setParameter("elementId", elementId);
		query.executeUpdate();
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#updateDisplayOrdersAfterDragAndDrop(Long,Long)
	 */
	@Override
	public Long updateDisplayOrdersForOtherNodes(Long elementId, Long folderId) {

		Long nodeCurrentPosition = getCurrentPositionForSelectedNodeInFolder(elementId);
		Long maxOrderInFolder = getMaxDisplayOrderByFolderId(folderId);
		Query query = getSession().createSQLQuery(
				"update T_ELEMENT  set ANZEIGE_ORDER = (ANZEIGE_ORDER-1)  where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER between :startPosition and :maxPosition");

		query.setParameter("startPosition", nodeCurrentPosition.intValue() + 1);
		query.setParameter("maxPosition", maxOrderInFolder);
		query.setParameter("folderId", folderId);
		query.executeUpdate();
		return maxOrderInFolder;
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#performUpOrDownOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */
	public void performUpOrDownOperation(Long elementId, Long folderId, String operation, Long nodeCurrentPosition) {
		int limitOrder = 0;
		Long switchNodePosition = null;

		if (operation.equals("UP")) {
			limitOrder = 1;
			switchNodePosition = nodeCurrentPosition - 1;
			// PTS problem 34579 : get the first switch element position which is visible (not inactive),
			while ((switchNodePosition > 0) && getIsInactiveByOrderInFolder(switchNodePosition, folderId)) {
				switchNodePosition--;
			}
		} else if (operation.equals("DOWN")) {
			limitOrder = getMaxDisplayOrderByFolderId(folderId).intValue();
			switchNodePosition = nodeCurrentPosition + 1;
			// PTS problem 34579 : get the first switch element position which is visible (not inactive),
			while ((switchNodePosition <= limitOrder) && getIsInactiveByOrderInFolder(switchNodePosition, folderId)) {
				switchNodePosition++;
			}
		}
		// Update switch node position
		if (nodeCurrentPosition != limitOrder) {
			Query query1 = getSession().createSQLQuery(
					"update T_ELEMENT  set ANZEIGE_ORDER = :currentPosition where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER = :switchPosition");
			query1.setParameter("currentPosition", nodeCurrentPosition);
			query1.setParameter("folderId", folderId);
			query1.setParameter("switchPosition", switchNodePosition);
			query1.executeUpdate();

			// Finally update the one we moved to correct value
			changeSelectedElementOrder(elementId, switchNodePosition.intValue());

		}
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#performMoveToTopOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */
	public void performMoveToTopOperation(Long elementId, Long folderId, Long nodeCurrentPosition) {

		// Update other nodes position
		Query query1 = getSession().createSQLQuery(
				"update T_ELEMENT  set ANZEIGE_ORDER = (ANZEIGE_ORDER+1) where ORDNER_REF_ID= :folderId and ANZEIGE_ORDER between 1 and :maxPosition");
		query1.setParameter("maxPosition", nodeCurrentPosition.intValue() - 1);
		query1.setParameter("folderId", folderId);
		query1.executeUpdate();

		// Finally update the one we moved to correct value
		changeSelectedElementOrder(elementId, 1);
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#performMoveToBottomOperation(java.lang.Long,java.lang.Long,java.lang.Long)
	 */
	public void performMoveToBottomOperation(Long elementId, Long folderId, Long nodeCurrentPosition) {

		// Update other nodes position
		Long maxOrderInFolder = updateDisplayOrdersForOtherNodes(elementId, folderId);
		// Finally update the one we moved to correct value
		changeSelectedElementOrder(elementId, maxOrderInFolder.intValue());
	}

	/**
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#performElementChangeOrder(Long,Long,String)
	 */
	@Override
	public void performElementChangeOrder(Long elementId, Long folderId, String operation) {
		// Get selected node origin position
		Long nodeCurrentPosition = getCurrentPositionForSelectedNodeInFolder(elementId);

		if (operation.equals("TOP")) {
			performMoveToTopOperation(elementId, folderId, nodeCurrentPosition);
		}
		if (operation.equals("BOTTOM")) {
			performMoveToBottomOperation(elementId, folderId, nodeCurrentPosition);
		}
		if (operation.equals("UP") || operation.equals("DOWN")) {
			performUpOrDownOperation(elementId, folderId, operation, nodeCurrentPosition);
		}
	}

	// 14/11/2013:ABA: PTS problem 23631:Optimize Empty filter for elements

	/**
	 * Get Active elements and incative but used by user brand elements list
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getElementsListToBeFiltred(Long,String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getElementsListToBeFiltred(Long folderId, String userBrand) {

		List<Long> elementsIds = new ArrayList<Long>();

		Query query = getSession()
				.createSQLQuery("SELECT UUIDKE from T_ELEMENT where ORDNER_REF_ID= :folderId and X_INAKTIV=0 ");
		query.setParameter("folderId", folderId);
		elementsIds = query.getResultList();

		Query query1 = getSession().createSQLQuery(
				"SELECT T_ELEMENT.UUIDKE FROM T_ELEMENT RIGHT JOIN T_ELEMENTVERSION ON T_ELEMENT.UUIDKE = T_ELEMENTVERSION.KE_REF_UU_ID "
						+ " RIGHT JOIN T_REL_ELEMVERSION_NUTZER on T_REL_ELEMVERSION_NUTZER.KE_VERSION_REF_ID = T_ELEMENTVERSION.UUIDKEVERSION "
						+ " WHERE T_REL_ELEMVERSION_NUTZER.MARKE_REF_ID = :userBrand AND T_ELEMENT.X_INAKTIV=1 "
						+ " AND T_ELEMENT.ORDNER_REF_ID= :folderId");
		query1.setParameter("userBrand", userBrand);
		query1.setParameter("folderId", folderId);
		elementsIds.addAll(query1.getResultList());
		return elementsIds;

	}

	/**
	 * Check if folder has components
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ComponentDAO#getHasComponents(Long,String)
	 */
	@Override
	public boolean getHasElements(Long folderId) {
		Query query = getSession().createSQLQuery("SELECT count(*) from T_ELEMENT where ORDNER_REF_ID= :folderId");
		query.setParameter("folderId", folderId);
		query.getSingleResult();
		if (new Integer(query.getSingleResult().toString()) == 0) {
			return false;
		}

		return true;
	}

	/**
	 * Check if folder has elements that should be visible by owner filter
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#hasVisibleElementsByOwnerFilter(List,String,List)
	 */
	@Override
	public boolean hasVisibleElementsByOwnerFilter(List<BrandModel> ownerFilters, Long folderId,
			List<Long> elementsIds) {

		if (!ownerFilters.isEmpty() && !elementsIds.isEmpty()) {
			Query query = getSession().createSQLQuery(
					"SELECT count(*) FROM T_ELEMENT WHERE BESITZER in (:brands) AND UUIDKE in (:elementIds)");
			query.setParameter("brands", ownerFilters);
			query.setParameter("elementIds", elementsIds);
			if (new Integer(query.getSingleResult().toString()) != 0) {
				return true;
			}
		}

		return false;

	}

	/**
	 * Check if folder has elements that should be visible by users filter
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#hasVisibleElementsByUsersFilter(List,String,List)
	 */
	@Override
	public boolean hasVisibleElementsByUsersFilter(List<BrandModel> usersFilters, Long folderId,
			List<Long> elementsIds) {

		if (!usersFilters.isEmpty() && !elementsIds.isEmpty()) {
			Query query = getSession().createSQLQuery(
					"SELECT count(T_ELEMENT.UUIDKE) FROM T_ELEMENT RIGHT JOIN T_ELEMENTVERSION ON T_ELEMENT.UUIDKE = T_ELEMENTVERSION.KE_REF_UU_ID "
							+ " RIGHT JOIN T_REL_ELEMVERSION_NUTZER on T_REL_ELEMVERSION_NUTZER.KE_VERSION_REF_ID = T_ELEMENTVERSION.UUIDKEVERSION "
							+ " where T_REL_ELEMVERSION_NUTZER.MARKE_REF_ID in( :usersBrands) and T_ELEMENT.UUIDKE in(:elementIds)");

			query.setParameter("usersBrands", usersFilters);
			query.setParameter("elementIds", elementsIds);

			if (new Integer(query.getSingleResult().toString()) != 0) {
				return true;
			}
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getInactiveElementIds(java.lang.Long)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Long> getInactiveElementIds(Long folderId) {
		Query query = getSession()
				.createSQLQuery("SELECT UUIDKE from T_ELEMENT where ORDNER_REF_ID= :folderId and X_INAKTIV=1 ");
		query.setParameter("folderId", folderId);
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.integration.persistence.dao.ElementDAO#getAllElements()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Object[]> getAllElements() {
		Query query = getSession().createSQLQuery("SELECT ELEMENT_NR,BEZEICHNUNG,X_INAKTIV,ERSTELLER_REF_ID,"
				+ "ERSTELLT_AM,GEAENDERT_VON_REF_ID, GEAENDERT_AM,INAKTIV_VON,ORDNER_REF_ID, BESITZER from T_ELEMENT ");
		return query.getResultList();
	}

}