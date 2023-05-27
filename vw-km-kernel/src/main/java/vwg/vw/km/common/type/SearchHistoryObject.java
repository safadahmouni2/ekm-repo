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
package vwg.vw.km.common.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

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
 * @author boubakers changed by $Author: saidi $
 * @version $Revision: 1.15 $ $Date: 2022/01/19 09:37:04 $
 */
public class SearchHistoryObject {

	private final Log log = LogManager.get().getLog(SearchHistoryObject.class);

	private Date fromDate;

	private Date toDate;

	private Boolean desactivaton = Boolean.FALSE;

	private Boolean activation = Boolean.FALSE;

	private Boolean inProgress = Boolean.FALSE;

	private Boolean newState = Boolean.FALSE;

	private Boolean newOne = Boolean.FALSE;

	private Boolean exchangeUnusedState = Boolean.FALSE;

	private Boolean justEelement = Boolean.FALSE;

	private Boolean justComponent = Boolean.FALSE;

	private String owner;

	private List<String> usersList;

	private String user;

	private String numElement;

	private String numComponent;

	private String externalId;

	private List<Long> elementId = new ArrayList<Long>();

	private List<Long> componentId = new ArrayList<Long>();

	private List<Long> statusList = new ArrayList<Long>();

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Boolean getDesactivaton() {
		return desactivaton;
	}

	public void setDesactivaton(Boolean desactivaton) {
		this.desactivaton = desactivaton;
	}

	public Boolean getActivation() {
		return activation;
	}

	public void setActivation(Boolean activation) {
		this.activation = activation;
	}

	public Boolean getInProgress() {
		return inProgress;
	}

	public void setInProgress(Boolean inProgress) {
		this.inProgress = inProgress;
	}

	public Boolean getNewState() {
		return newState;
	}

	public void setNewState(Boolean newState) {
		this.newState = newState;
	}

	public Boolean getNewOne() {
		return newOne;
	}

	public void setNewOne(Boolean newOne) {
		this.newOne = newOne;
	}

	public Boolean getExchangeUnusedState() {
		return exchangeUnusedState;
	}

	public void setExchangeUnusedState(Boolean exchangeUnusedState) {
		this.exchangeUnusedState = exchangeUnusedState;
	}

	public Boolean getJustEelement() {
		return justEelement;
	}

	public void setJustEelement(Boolean justEelement) {
		this.justEelement = justEelement;
	}

	public Boolean getJustComponent() {
		return justComponent;
	}

	public void setJustComponent(Boolean justComponent) {
		this.justComponent = justComponent;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public List<String> getUsersList() {
		return usersList;
	}

	public void setUsersList(List<String> usersList) {
		this.usersList = usersList;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String userChoice) {
		this.user = userChoice;
	}

	public String getNumElement() {
		return numElement;
	}

	public void setNumElement(String numElement) {
		this.numElement = numElement;
	}

	public String getNumComponent() {
		return numComponent;
	}

	public void setNumComponent(String numComponent) {
		this.numComponent = numComponent;
	}

	public String getExternalId() {
		return externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	public List<Long> getElementId() {
		return elementId;
	}

	public void setElementId(List<Long> elementId) {
		this.elementId = elementId;
	}

	public List<Long> getComponentId() {
		return componentId;
	}

	public void setComponentId(List<Long> componentId) {
		this.componentId = componentId;
	}

	/**
	 * PTS requirement 22211:ABA :Benachrichtigungsfunktion Berücksichtigt werden dabei nur Objekte im Status „in
	 * Verwendung“ sowie "Freigegebene" Objekte ).
	 */

	public List<Long> getStatusList() {
		return statusList;
	}

	public void setStatusList(List<Long> statusList) {
		this.statusList = statusList;
	}

	/**
	 * 
	 * @return
	 */
	public List<String> getChangeTypes() {
		List<String> changeTypes = new ArrayList<String>();
		if (getActivation()) {
			changeTypes.add(ChangeTypeValue.ACTIVATE.value());
		}
		if (getDesactivaton()) {
			changeTypes.add(ChangeTypeValue.DEACTIVATE.value());
		}
		if (getNewOne()) {
			changeTypes.add(ChangeTypeValue.NEW.value());
		}
		if (getNewState()) {
			changeTypes.add(ChangeTypeValue.NEW_STAND.value());
		}
		if (getInProgress()) {
			changeTypes.add(ChangeTypeValue.IN_PROCESS.value());
		}
		if (getExchangeUnusedState()) {
			changeTypes.add(ChangeTypeValue.USE_STAND.value());
		}
		return changeTypes;
	}

	public boolean searchJustComponent() {
		if (getJustComponent() && !getJustEelement()
				|| (((getNumComponent() != null && !"".equals(getNumComponent().trim()))
						|| (getExternalId() != null && !"".equals(getExternalId().trim())))
						&& (getNumElement() == null || "".equals(getNumElement().trim())))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean searchJustElement() {
		if (!getJustComponent() && getJustEelement() || ((getNumElement() != null && !"".equals(getNumElement().trim()))
				&& ((getNumComponent() == null || "".equals(getNumComponent().trim()))
						|| (getExternalId() != null && !"".equals(getExternalId().trim()))))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public boolean searchInDBNotRequired() {
		// Das System liefert kein Ergebnis, da die Auswahl Wechsel genutzter Stand ohne Angabe eines Nutzers unwirksam
		// bleibt.
		if ((getUsersList() == null || getUsersList().isEmpty()) && getExchangeUnusedState()
				&& (!getActivation() && !getDesactivaton() && !getInProgress() && !getNewOne() && !getNewState())) {
			return Boolean.TRUE;
		}

		if (getUser() != null && !getUser().isEmpty() && getUser().equals(getOwner())) {
			return Boolean.TRUE;
		}
		// PTS 11473 just compoent with element numer or just element with component nummer must return no result
		if ((getJustComponent() && !getJustEelement() && getNumElement() != null && !"".equals(getNumElement().trim()))
				|| (getJustEelement() && !getJustComponent()
						&& (getNumComponent() != null && !"".equals(getNumComponent().trim())
								|| (getExternalId() != null && !"".equals(getExternalId().trim()))))) {
			return Boolean.TRUE;
		}
		// "Von" Date is after "bis" date
		if ((getFromDate() != null && getToDate() != null) && getFromDate().compareTo(getToDate()) > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public String toString() {
		return "SearchHistoryObject [activation=" + activation + ", componentId=" + componentId + ", desactivaton="
				+ desactivaton + ", elementId=" + elementId + ", exchangeUnusedState=" + exchangeUnusedState
				+ ", fromDate=" + fromDate + ", inProgress=" + inProgress + ", justComponent=" + justComponent
				+ ", justEelement=" + justEelement + ", numComponent=" + numComponent + ", externalId=" + externalId
				+ ", numElement=" + numElement + ", owner=" + owner + ", toDate=" + toDate + ", user ="
				+ usersList.toString() + "]";
	}
}
