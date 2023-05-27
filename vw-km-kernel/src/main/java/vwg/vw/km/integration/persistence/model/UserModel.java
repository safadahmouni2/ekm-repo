package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_BENUTZER"
 * 
 */
@Entity
@Table(name = "T_BENUTZER")
public class UserModel extends vwg.vw.km.integration.persistence.model.base.BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="USERID"
	 * 
	 */
	@Id
	@Column(name = "USER_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_user")
	@SequenceGenerator(name = "seq_user", sequenceName = "SEQ_USER", allocationSize = 1)
	private Long userId;
	/**
	 * @hibernate.property column="NACHNAME" length="50" not-null="true"
	 * 
	 */
	@Column(name = "NACHNAME", length = 50, nullable = false)
	private String lastName;
	/**
	 * @hibernate.property column="VORNAME" length="50"
	 * 
	 */
	@Column(name = "VORNAME", length = 50)
	private String firstName;
	/**
	 * @hibernate.property column="KUMSUSERID" length="10" not-null="true"
	 * 
	 */
	@Column(name = "KUMS_USER_ID", length = 10, nullable = false, unique = true)
	private String kUMSUserId;
	/**
	 * @hibernate.property column="EMAIL" length="50"
	 * 
	 */
	@Column(name = "EMAIL", length = 50)
	private String email;
	/**
	 * @hibernate.property column="TELEFON" length="20"
	 * 
	 */

	@Column(name = "TELEFON", length = 20)
	private String telephone;

	@Column(name = "ANREDE", length = 20)
	private String gender;
	/**
	 * @hibernate.property column="DEFAULTSTDSATZKATREFID" length="10"
	 * 
	 */

	@Column(name = "DEFAULT_STD_SATZ_KAT_REF_ID", length = 10)
	private Long defaultStdSatzKatRefId;
	/**
	 * @hibernate.property column="STATUS_ELEMENT_FREIGEGEBEN" length="1" not-null="true"
	 * 
	 */

	@Column(name = "STATUS_ELEMENT_FREIGEGEBEN", length = 1)
	private Boolean statusElementPublish;
	/**
	 * @hibernate.property column="AKTIV" length="1" not-null="true"
	 * 
	 */

	@Column(name = "AKTIV", length = 1, nullable = false)
	private Boolean active;
	/**
	 * @hibernate.property column="AKTIV" length="1" not-null="true"
	 * 
	 */
	@Column(name = "AKTIV", length = 1, insertable = false, updatable = false)
	private Boolean oldActive;
	/**
	 * @hibernate.property column="DEFAULT_MARKE_REF_ID" length="3"
	 * 
	 */

	@Column(name = "DEFAULT_MARKE_REF_ID", length = 3)
	private String defaultBrand;
	/**
	 * @hibernate.property column="DEFAULT_BIBLIOTHEK_REF_ID" length="12"
	 * 
	 */

	@Column(name = "DEFAULT_BIBLIOTHEK_REF_ID", length = 12)
	private Long defaultLibrary;

	@Column(name = "BENUTZER_ADMIN_ARBEITS", length = 100)
	private String workAreasAdminOn;
	/**
	 * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */

	@Column(name = "ELEMENT_BESITZER_FILTER", length = 100)
	private String elementOwnerFilterIds;
	/**
	 * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */

	@Column(name = "ELEMENT_NUTZER_FILTER", length = 100)
	private String elementUserFilterIds;
	/**
	 * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */

	@Column(name = "BAUSTEIN_BESITZER_FILTER", length = 100)
	private String componentOwnerFilterIds;
	/**
	 * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */

	@Column(name = "BAUSTEIN_NUTZER_FILTER", length = 100)
	private String componentUserFilterIds;
	/**
	 * @hibernate.property column="ANZEIGEN_LEER_ORDNER" length="1"
	 * 
	 */

	@Column(name = "ANZEIGEN_LEER_ORDNER", length = 1)
	private Boolean showEmptyFolders;
	/**
	 * @hibernate.property column="BENACHRICHTIGUNGS_FILTER" length="1"
	 * 
	 */

	@Column(name = "BENACHRICHTIGUNGS_OFF", length = 1, nullable = false)
	private Boolean disableChangesNotifications = Boolean.FALSE;
	/**
	 * @hibernate.property column="EXTERNER_MIT" length="1"
	 * 
	 */

	@Column(name = "EXTERNER_MIT", length = 1, nullable = false)
	private Boolean isExternal = Boolean.FALSE;
	/**
	 * @hibernate.property column="IM_AUFTRAG_VON" length="250" not-null="false"
	 * 
	 */

	@Column(name = "IM_AUFTRAG_VON", length = 250)
	private String onBehalfOf;
	/**
	 * @hibernate.property column="LETZTER_LOGIN" length="8"
	 * 
	 */

	@Column(name = "LETZTER_LOGIN", columnDefinition = "DATE")
	private BaseDateTime lastLogInTime;
	/**
	 * @hibernate.property column="DEAKTIVIERUNGS_DATUM" length="8"
	 * 
	 */
	@Column(name = "DEAKTIVIERUNGS_DATUM", columnDefinition = "DATE")
	private BaseDateTime deactivationDate;
	/**
	 * @hibernate.property column="ANONYMISIERT" length="1" not-null="true"
	 * 
	 */

	@Column(name = "ANONYMISIERT", length = 1, nullable = false)
	private Boolean anonymized = Boolean.FALSE;
	/**
	 * @hibernate.property column="IMG_ROOT_PFAD" length="500"
	 * 
	 */

	@Column(name = "IMG_ROOT_PFAD", length = 500)
	private String imagesRootPath;
	/**
	 * @hibernate.property column="ERSTELLT_AM" length="8"
	 * 
	 */
	@Column(name = "ERSTELLT_AM", columnDefinition = "DATE")
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="ERSTELLER_REF_ID" length="19"
	 * 
	 */

	@Column(name = "ERSTELLER_REF_ID", length = 19)
	private Long creatorRefId;
	/**
	 * @hibernate.property column="GEAENDERT_AM" length="7"
	 * 
	 */
	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	private BaseDateTime modificationDate;
	/**
	 * @hibernate.property column="GEAENDERT_VON_REF_ID" length="19"
	 * 
	 */

	@Column(name = "GEAENDERT_VON_REF_ID", length = 19)
	private Long modifierRefId;
	/**
	 * @hibernate.property column="AKTIVIERUNGS_CODE" length="10"
	 * 
	 */

	@Column(name = "AKTIVIERUNGS_CODE", length = 10, unique = true)
	private String activationCode;
	/**
	 * @hibernate.property column="AKTIVIERUNGS_CODE_GESENDET_AM" length="8"
	 * 
	 */
	@Column(name = "AKTIVIERUNGS_CODE_GESENDET_AM", columnDefinition = "DATE")
	private BaseDateTime activationCodeSentDate;
	/**
	 * @hibernate.property column="DATENSCHUTZ_HIN_BESTAETIGT_AM" length="8"
	 * 
	 */
	@Column(name = "DATENSCHUTZ_HIN_BESTAETIGT_AM", columnDefinition = "DATE")
	private BaseDateTime lastNotificationDate;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_REL_BENUTZER_ROLLEN", joinColumns = {
			@JoinColumn(name = "USER_REF_ID") }, inverseJoinColumns = { @JoinColumn(name = "ROLLE_REF_ID") })
	@Fetch(FetchMode.SUBSELECT)
	private Set<RoleModel> roles = new HashSet<RoleModel>(0);

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_REL_BENUTZER_ARBEITSBEREICH", joinColumns = {
			@JoinColumn(name = "USER_ID_REF") }, inverseJoinColumns = { @JoinColumn(name = "ARBEITSBEREICH_REF") })
	@Fetch(FetchMode.SUBSELECT)
	private Set<WorkAreaModel> workAreas = new HashSet<WorkAreaModel>(0);

	public UserModel() {
	}

	public UserModel(String lastName, String kUMSUserId, Boolean active) {
		this.lastName = lastName;
		this.kUMSUserId = kUMSUserId;
		this.active = active;
	}

	public UserModel(String lastName, String firstName, String kUMSUserId, String email, String telephone,
			String gender, Long defaultStdSatzKatRefId, Boolean statusElementPublish, Boolean active, Boolean oldActive,
			String defaultBrand, Long defaultLibrary, String workAreasAdminOn, String elementOwnerFilterIds,
			String elementUserFilterIds, String componentOwnerFilterIds, String componentUserFilterIds,
			Boolean showEmptyFolders, Boolean disableChangesNotifications, Boolean isExternal, String onBehalfOf,
			BaseDateTime lastLogInTime, BaseDateTime deactivationDate, Boolean anonymized, String imagesRootPath,
			BaseDateTime creationDate, Long creatorRefId, BaseDateTime modificationDate, Long modifierRefId,
			String activationCode, BaseDateTime activationCodeSentDate, BaseDateTime lastNotificationDate,
			Set<RoleModel> roles, Set<WorkAreaModel> workAreas) {
		this.lastName = lastName;
		this.firstName = firstName;
		this.kUMSUserId = kUMSUserId;
		this.email = email;
		this.telephone = telephone;
		this.gender = gender;
		this.defaultStdSatzKatRefId = defaultStdSatzKatRefId;
		this.statusElementPublish = statusElementPublish;
		this.active = active;
		this.oldActive = oldActive;
		this.defaultBrand = defaultBrand;
		this.defaultLibrary = defaultLibrary;
		this.workAreasAdminOn = workAreasAdminOn;
		this.elementOwnerFilterIds = elementOwnerFilterIds;
		this.elementUserFilterIds = elementUserFilterIds;
		this.componentOwnerFilterIds = componentOwnerFilterIds;
		this.componentUserFilterIds = componentUserFilterIds;
		this.showEmptyFolders = showEmptyFolders;
		this.disableChangesNotifications = disableChangesNotifications;
		this.isExternal = isExternal;
		this.onBehalfOf = onBehalfOf;
		this.lastLogInTime = lastLogInTime;
		this.deactivationDate = deactivationDate;
		this.anonymized = anonymized;
		this.imagesRootPath = imagesRootPath;
		this.creationDate = creationDate;
		this.creatorRefId = creatorRefId;
		this.modificationDate = modificationDate;
		this.modifierRefId = modifierRefId;
		this.activationCode = activationCode;
		this.activationCodeSentDate = activationCodeSentDate;
		this.lastNotificationDate = lastNotificationDate;
		this.roles = roles;
		this.workAreas = workAreas;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="USERID"
	 * 
	 */
	public Long getUserId() {
		return this.userId;
	}

	private void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * * @hibernate.property column="NACHNAME" length="50" not-null="true"
	 * 
	 */
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * * @hibernate.property column="VORNAME" length="50"
	 * 
	 */
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * * @hibernate.property column="KUMSUSERID" length="10" not-null="true"
	 * 
	 */
	public String getkUMSUserId() {
		return this.kUMSUserId;
	}

	public void setkUMSUserId(String kUMSUserId) {
		this.kUMSUserId = kUMSUserId;
	}

	/**
	 * * @hibernate.property column="EMAIL" length="50"
	 * 
	 */
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * * @hibernate.property column="TELEFON" length="20"
	 * 
	 */
	public String getTelephone() {
		return this.telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * * @hibernate.property column="DEFAULTSTDSATZKATREFID" length="10"
	 * 
	 */
	public Long getDefaultStdSatzKatRefId() {
		return this.defaultStdSatzKatRefId;
	}

	public void setDefaultStdSatzKatRefId(Long defaultStdSatzKatRefId) {
		this.defaultStdSatzKatRefId = defaultStdSatzKatRefId;
	}

	/**
	 * * @hibernate.property column="STATUS_ELEMENT_FREIGEGEBEN" length="1" not-null="true"
	 * 
	 */
	public Boolean getStatusElementPublish() {
		return this.statusElementPublish;
	}

	public void setStatusElementPublish(Boolean statusElementPublish) {
		this.statusElementPublish = statusElementPublish;
	}

	/**
	 * * @hibernate.property column="AKTIV" length="1" not-null="true"
	 * 
	 */
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * * @hibernate.property column="AKTIV" length="1" not-null="true"
	 * 
	 */
	public Boolean getOldActive() {
		return this.oldActive;
	}

	public void setOldActive(Boolean oldActive) {
		this.oldActive = oldActive;
	}

	/**
	 * * @hibernate.property column="DEFAULT_MARKE_REF_ID" length="3"
	 * 
	 */
	public String getDefaultBrand() {
		return this.defaultBrand;
	}

	public void setDefaultBrand(String defaultBrand) {
		this.defaultBrand = defaultBrand;
	}

	/**
	 * * @hibernate.property column="DEFAULT_BIBLIOTHEK_REF_ID" length="12"
	 * 
	 */
	public Long getDefaultLibrary() {
		return this.defaultLibrary;
	}

	public void setDefaultLibrary(Long defaultLibrary) {
		this.defaultLibrary = defaultLibrary;
	}

	public String getWorkAreasAdminOn() {
		return this.workAreasAdminOn;
	}

	public void setWorkAreasAdminOn(String workAreasAdminOn) {
		this.workAreasAdminOn = workAreasAdminOn;
	}

	/**
	 * * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */
	public String getElementOwnerFilterIds() {
		return this.elementOwnerFilterIds;
	}

	public void setElementOwnerFilterIds(String elementOwnerFilterIds) {
		this.elementOwnerFilterIds = elementOwnerFilterIds;
	}

	/**
	 * * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */
	public String getElementUserFilterIds() {
		return this.elementUserFilterIds;
	}

	public void setElementUserFilterIds(String elementUserFilterIds) {
		this.elementUserFilterIds = elementUserFilterIds;
	}

	/**
	 * * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */
	public String getComponentOwnerFilterIds() {
		return this.componentOwnerFilterIds;
	}

	public void setComponentOwnerFilterIds(String componentOwnerFilterIds) {
		this.componentOwnerFilterIds = componentOwnerFilterIds;
	}

	/**
	 * * @hibernate.property column="country_ids" length="100" not-null="false"
	 * 
	 */
	public String getComponentUserFilterIds() {
		return this.componentUserFilterIds;
	}

	public void setComponentUserFilterIds(String componentUserFilterIds) {
		this.componentUserFilterIds = componentUserFilterIds;
	}

	/**
	 * * @hibernate.property column="ANZEIGEN_LEER_ORDNER" length="1"
	 * 
	 */
	public Boolean getShowEmptyFolders() {
		return this.showEmptyFolders;
	}

	public void setShowEmptyFolders(Boolean showEmptyFolders) {
		this.showEmptyFolders = showEmptyFolders;
	}

	/**
	 * * @hibernate.property column="BENACHRICHTIGUNGS_FILTER" length="1"
	 * 
	 */
	public Boolean getDisableChangesNotifications() {
		return this.disableChangesNotifications;
	}

	public void setDisableChangesNotifications(Boolean disableChangesNotifications) {
		this.disableChangesNotifications = disableChangesNotifications;
	}

	/**
	 * * @hibernate.property column="EXTERNER_MIT" length="1"
	 * 
	 */
	public Boolean getIsExternal() {
		return this.isExternal;
	}

	public void setIsExternal(Boolean isExternal) {
		this.isExternal = isExternal;
	}

	/**
	 * * @hibernate.property column="IM_AUFTRAG_VON" length="250" not-null="false"
	 * 
	 */
	public String getOnBehalfOf() {
		return this.onBehalfOf;
	}

	public void setOnBehalfOf(String onBehalfOf) {
		this.onBehalfOf = onBehalfOf;
	}

	/**
	 * * @hibernate.property column="LETZTER_LOGIN" length="8"
	 * 
	 */
	public BaseDateTime getLastLogInTime() {
		return this.lastLogInTime;
	}

	public void setLastLogInTime(BaseDateTime lastLogInTime) {
		this.lastLogInTime = lastLogInTime;
	}

	/**
	 * * @hibernate.property column="DEAKTIVIERUNGS_DATUM" length="8"
	 * 
	 */
	public BaseDateTime getDeactivationDate() {
		return this.deactivationDate;
	}

	public void setDeactivationDate(BaseDateTime deactivationDate) {
		this.deactivationDate = deactivationDate;
	}

	/**
	 * * @hibernate.property column="ANONYMISIERT" length="1" not-null="true"
	 * 
	 */
	public Boolean getAnonymized() {
		return this.anonymized;
	}

	public void setAnonymized(Boolean anonymized) {
		this.anonymized = anonymized;
	}

	/**
	 * * @hibernate.property column="IMG_ROOT_PFAD" length="500"
	 * 
	 */
	public String getImagesRootPath() {
		return this.imagesRootPath;
	}

	public void setImagesRootPath(String imagesRootPath) {
		this.imagesRootPath = imagesRootPath;
	}

	/**
	 * * @hibernate.property column="ERSTELLT_AM" length="8"
	 * 
	 */
	public BaseDateTime getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(BaseDateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * * @hibernate.property column="ERSTELLER_REF_ID" length="19"
	 * 
	 */
	public Long getCreatorRefId() {
		return this.creatorRefId;
	}

	public void setCreatorRefId(Long creatorRefId) {
		this.creatorRefId = creatorRefId;
	}

	/**
	 * * @hibernate.property column="GEAENDERT_AM" length="7"
	 * 
	 */
	public BaseDateTime getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(BaseDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * * @hibernate.property column="GEAENDERT_VON_REF_ID" length="19"
	 * 
	 */
	public Long getModifierRefId() {
		return this.modifierRefId;
	}

	public void setModifierRefId(Long modifierRefId) {
		this.modifierRefId = modifierRefId;
	}

	/**
	 * * @hibernate.property column="AKTIVIERUNGS_CODE" length="10"
	 * 
	 */
	public String getActivationCode() {
		return this.activationCode;
	}

	public void setActivationCode(String activationCode) {
		this.activationCode = activationCode;
	}

	/**
	 * * @hibernate.property column="AKTIVIERUNGS_CODE_GESENDET_AM" length="8"
	 * 
	 */
	public BaseDateTime getActivationCodeSentDate() {
		return this.activationCodeSentDate;
	}

	public void setActivationCodeSentDate(BaseDateTime activationCodeSentDate) {
		this.activationCodeSentDate = activationCodeSentDate;
	}

	/**
	 * * @hibernate.property column="DATENSCHUTZ_HIN_BESTAETIGT_AM" length="8"
	 * 
	 */
	public BaseDateTime getLastNotificationDate() {
		return this.lastNotificationDate;
	}

	public void setLastNotificationDate(BaseDateTime lastNotificationDate) {
		this.lastNotificationDate = lastNotificationDate;
	}

	public Set<RoleModel> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<RoleModel> roles) {
		this.roles = roles;
	}

	public Set<WorkAreaModel> getWorkAreas() {
		return this.workAreas;
	}

	public void setWorkAreas(Set<WorkAreaModel> workAreas) {
		this.workAreas = workAreas;
	}

	/**
	 * toString
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("userId").append("='").append(getUserId()).append("' ");
		buffer.append("lastName").append("='").append(getLastName()).append("' ");
		buffer.append("firstName").append("='").append(getFirstName()).append("' ");
		buffer.append("kUMSUserId").append("='").append(getkUMSUserId()).append("' ");
		buffer.append("defaultBrand").append("='").append(getDefaultBrand()).append("' ");
		buffer.append("defaultLibrary").append("='").append(getDefaultLibrary()).append("' ");
		buffer.append("workAreasAdminOn").append("='").append(getWorkAreasAdminOn()).append("' ");
		buffer.append("elementOwnerFilterIds").append("='").append(getElementOwnerFilterIds()).append("' ");
		buffer.append("elementUserFilterIds").append("='").append(getElementUserFilterIds()).append("' ");
		buffer.append("componentOwnerFilterIds").append("='").append(getComponentOwnerFilterIds()).append("' ");
		buffer.append("componentUserFilterIds").append("='").append(getComponentUserFilterIds()).append("' ");
		buffer.append("showEmptyFolders").append("='").append(getShowEmptyFolders()).append("' ");
		buffer.append("onBehalfOf").append("='").append(getOnBehalfOf()).append("' ");
		buffer.append("lastLogInTime").append("='").append(getLastLogInTime()).append("' ");
		buffer.append("deactivationDate").append("='").append(getDeactivationDate()).append("' ");
		buffer.append("creatorRefId").append("='").append(getCreatorRefId()).append("' ");
		buffer.append("modifierRefId").append("='").append(getModifierRefId()).append("' ");
		buffer.append("activationCode").append("='").append(getActivationCode()).append("' ");
		buffer.append("lastNotificationDate").append("='").append(getLastNotificationDate()).append("' ");
		buffer.append("roles").append("='").append(getRoles()).append("' ");
		buffer.append("workAreas").append("='").append(getWorkAreas()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

	@Override
	public boolean equals(Object other) {
		if ((this == other)) {
			return true;
		}
		if ((other == null)) {
			return false;
		}
		if (!(other instanceof UserModel)) {
			return false;
		}
		UserModel castOther = (UserModel) other;

		return ((this.getUserId() == castOther.getUserId()) || (this.getUserId() != null
				&& castOther.getUserId() != null && this.getUserId().equals(castOther.getUserId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getUserId() == null ? 0 : this.getUserId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public boolean haveRightToDo(Long actionId) {
		for (RoleModel role : getRoles()) {
			for (RightModel right : role.getRights()) {
				if (right.getRightId().equals(actionId)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean haveRole(Long roleId) {
		for (RoleModel role : getRoles()) {
			if (role.getRoleId().equals(roleId)) {
				return true;
			}
		}
		return false;
	}

	public Set<RoleModel> getManagedRoles() {
		Set<RoleModel> managedRoles = new HashSet<RoleModel>(0);
		for (RoleModel role : getRoles()) {
			managedRoles.addAll(role.getManagedRoles());
		}
		return managedRoles;
	}

	// controls the index of tab to be displayed
	@Transient
	private int selectedTabIndex = 0;

	public int getSelectedTabIndex() {
		return selectedTabIndex;
	}

	public void setSelectedTabIndex(int i) {
		selectedTabIndex = i;
	}

	// controls the search filer to be used
	@Transient
	private String displayFilter = "ALL";

	public String getDisplayFilter() {
		return displayFilter;
	}

	public void setDisplayFilter(String displayFilter) {
		this.displayFilter = displayFilter;
	}

	public boolean isAdminOnWorkArea(String workAreaId) {
		boolean returnValue = false;
		if (getWorkAreasAdminOn() != null && !"".equals(getWorkAreasAdminOn())) {
			String[] ids = getWorkAreasAdminOn().split(";");
			for (int i = 0; i < ids.length; i++) {
				String id = ids[i];
				if (id.equals(workAreaId)) {
					returnValue = true;
					break;
				}
			}
		}
		return returnValue;
	}

	public String getFullName() {
		if (getFirstName() == null || "".equals(getFirstName().trim())) {
			return getLastName();
		}
		if (getLastName() == null || "".equals(getLastName().trim())) {
			return getFirstName();
		}
		return getLastName() + " " + getFirstName();
	}

	public String getSigFullName() {
		String result = getFullName();
		String onBehalfe = " im Auftrag von " + getOnBehalfOf();
		if (getIsExternal() != null && getIsExternal()) {
			result = result + onBehalfe;
		}
		return result;
	}

	public List<BrandModel> getUserBrands() {
		Set<BrandModel> userBrands = new HashSet<BrandModel>();
		for (WorkAreaModel workArea : getWorkAreas()) {
			if (workArea.getBrand() != null) {
				userBrands.add(workArea.getBrand());
			}
		}
		return new java.util.ArrayList<BrandModel>(userBrands);
	}

	public List<BrandModel> getElementOwnerFilter() {
		List<BrandModel> returnValue = new ArrayList<BrandModel>();
		if (getElementOwnerFilterIds() != null && !"".equals(getElementOwnerFilterIds())) {
			String[] ids = getElementOwnerFilterIds().split(";");
			for (String id : ids) {
				returnValue.add(new BrandModel(id));
			}
		}
		return returnValue;
	}

	public List<BrandModel> getElementUserFilter() {
		List<BrandModel> returnValue = new ArrayList<BrandModel>();
		if (getElementUserFilterIds() != null && !"".equals(getElementUserFilterIds())) {
			String[] ids = getElementUserFilterIds().split(";");
			for (String id : ids) {
				returnValue.add(new BrandModel(id));
			}
		}
		return returnValue;
	}

	public List<BrandModel> getComponentUserFilter() {
		List<BrandModel> returnValue = new ArrayList<BrandModel>();
		if (getComponentUserFilterIds() != null && !"".equals(getComponentUserFilterIds())) {
			String[] ids = getComponentUserFilterIds().split(";");
			for (String id : ids) {
				returnValue.add(new BrandModel(id));
			}
		}
		return returnValue;
	}

	public List<BrandModel> getComponentOwnerFilter() {
		List<BrandModel> returnValue = new ArrayList<BrandModel>();
		if (getComponentOwnerFilterIds() != null && !"".equals(getComponentOwnerFilterIds())) {
			String[] ids = getComponentOwnerFilterIds().split(";");
			for (String id : ids) {
				returnValue.add(new BrandModel(id));
			}
		}
		return returnValue;
	}

	public boolean isActiveElementFilter() {
		return !getElementOwnerFilter().isEmpty() || !getElementUserFilter().isEmpty();
	}

	public boolean isActiveComponentFilter() {
		return !getComponentOwnerFilter().isEmpty() || !getComponentUserFilter().isEmpty();
	}

	@Transient
	private BaseDateTime previousLoginTime;

	public BaseDateTime getPreviousLoginTime() {
		return previousLoginTime;
	}

	public void setPreviousLoginTime(BaseDateTime previousLoginTime) {
		this.previousLoginTime = previousLoginTime;
	}

	@Transient
	private boolean toBeNotified;

	public boolean isToBeNotified() {
		return toBeNotified;
	}

	public void setToBeNotified(boolean toBeNotified) {
		this.toBeNotified = toBeNotified;
	}

	// end of extra code specified in the hbm.xml files

}
