package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_ELEMENT"
 * 
 */

@Entity
@Table(name = "T_ELEMENT")
public class ElementModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKE"
	 * 
	 */

	@Id
	@Column(name = "UUIDKE")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_element")
	@SequenceGenerator(name = "seq_element", sequenceName = "SEQ_ELEMENT", allocationSize = 1)
	private Long elementId;
	/**
	 * @hibernate.property column="ELEMENTNR" unique="true" length="256" not-null="true"
	 * 
	 */

	@Column(name = "ELEMENT_NR", length = 256, nullable = false, unique = true)
	private String elementNumber;

	@Column(name = "ELEMENT_NR", length = 256, insertable = false, updatable = false)
	private String oldElementNumber;
	/**
	 * @hibernate.property column="BEZEICHNUNG" unique="true" length="255" not-null="true"
	 * 
	 */

	@Column(name = "BEZEICHNUNG", length = 255, nullable = false, unique = true)
	private String designation;

	@Column(name = "BEZEICHNUNG", length = 255, insertable = false, updatable = false)
	private String oldDesignation;
	/**
	 * @hibernate.property column="XINAKTIV" length="1" not-null="true"
	 * 
	 */

	@Column(name = "X_INAKTIV", length = 1, nullable = false)
	private Boolean inactive;
	/**
	 * @hibernate.property column="XINAKTIV" length="1" not-null="true"
	 * 
	 */
	@Column(name = "X_INAKTIV", length = 1, insertable = false, updatable = false)
	private Boolean oldInactive;
	/**
	 * @hibernate.property column="ERSTELLERREFID" length="10"
	 * 
	 */
	@Column(name = "ERSTELLER_REF_ID", length = 10)
	private Long creatorRefId;
	/**
	 * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */
	@Column(name = "ERSTELLT_AM", columnDefinition = "DATE", nullable = false)
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="GEAENDERVONREFID" length="10"
	 * 
	 */
	@Column(name = "GEAENDERT_VON_REF_ID", length = 10)
	private Long modifierRefId;
	/**
	 * @hibernate.property column="GEAENDERTAM" length="7"
	 * 
	 */
	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	private BaseDateTime modificationDate;

	@Column(name = "INAKTIV_VON", columnDefinition = "DATE")
	private BaseDateTime inactiveFromDate;
	/**
	 * @hibernate.property column="ANZEIGE_ORDER"
	 * 
	 */
	@Column(name = "ANZEIGE_ORDER", nullable = false)
	private Long displayOrder;

	@ManyToOne
	@JoinColumn(name = "ORDNER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ORDNER_REF_ID", nullable = false)
	private FolderModel folder;
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="KEREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Telementversion"
	 * 
	 */
	@OneToMany(mappedBy = "element", fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("ERSTELLT_AM DESC")
	private Set<ElementVersionModel> elementVersions = new HashSet<ElementVersionModel>(0);
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="BESITZER"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "BESITZER")
	// TODO conflict with DB
	// @JoinColumn(name = "BESITZER", nullable = false)
	private BrandModel owner;

	public ElementModel() {
	}

	public ElementModel(String elementNumber, String designation, Boolean inactive, BaseDateTime creationDate,
			Long displayOrder) {
		this.elementNumber = elementNumber;
		this.designation = designation;
		this.inactive = inactive;
		this.creationDate = creationDate;
		this.displayOrder = displayOrder;
	}

	public ElementModel(String elementNumber, String oldElementNumber, String designation, String oldDesignation,
			Boolean inactive, Boolean oldInactive, Long creatorRefId, BaseDateTime creationDate, Long modifierRefId,
			BaseDateTime modificationDate, BaseDateTime inactiveFromDate, Long displayOrder, FolderModel folder,
			Set<ElementVersionModel> elementVersions, BrandModel owner) {
		this.elementNumber = elementNumber;
		this.oldElementNumber = oldElementNumber;
		this.designation = designation;
		this.oldDesignation = oldDesignation;
		this.inactive = inactive;
		this.oldInactive = oldInactive;
		this.creatorRefId = creatorRefId;
		this.creationDate = creationDate;
		this.modifierRefId = modifierRefId;
		this.modificationDate = modificationDate;
		this.inactiveFromDate = inactiveFromDate;
		this.displayOrder = displayOrder;
		this.folder = folder;
		this.elementVersions = elementVersions;
		this.owner = owner;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKE"
	 * 
	 */
	public Long getElementId() {
		return this.elementId;
	}

	private void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	/**
	 * * @hibernate.property column="ELEMENTNR" unique="true" length="256" not-null="true"
	 * 
	 */
	public String getElementNumber() {
		return this.elementNumber;
	}

	public void setElementNumber(String elementNumber) {
		this.elementNumber = elementNumber;
	}

	public String getOldElementNumber() {
		return this.oldElementNumber;
	}

	public void setOldElementNumber(String oldElementNumber) {
		this.oldElementNumber = oldElementNumber;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" unique="true" length="255" not-null="true"
	 * 
	 */
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getOldDesignation() {
		return this.oldDesignation;
	}

	public void setOldDesignation(String oldDesignation) {
		this.oldDesignation = oldDesignation;
	}

	/**
	 * * @hibernate.property column="XINAKTIV" length="1" not-null="true"
	 * 
	 */
	public Boolean getInactive() {
		return this.inactive;
	}

	public void setInactive(Boolean inactive) {
		this.inactive = inactive;
	}

	/**
	 * * @hibernate.property column="XINAKTIV" length="1" not-null="true"
	 * 
	 */
	public Boolean getOldInactive() {
		return this.oldInactive;
	}

	public void setOldInactive(Boolean oldInactive) {
		this.oldInactive = oldInactive;
	}

	/**
	 * * @hibernate.property column="ERSTELLERREFID" length="10"
	 * 
	 */
	public Long getCreatorRefId() {
		return this.creatorRefId;
	}

	public void setCreatorRefId(Long creatorRefId) {
		this.creatorRefId = creatorRefId;
	}

	/**
	 * * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */
	public BaseDateTime getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(BaseDateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * * @hibernate.property column="GEAENDERVONREFID" length="10"
	 * 
	 */
	public Long getModifierRefId() {
		return this.modifierRefId;
	}

	public void setModifierRefId(Long modifierRefId) {
		this.modifierRefId = modifierRefId;
	}

	/**
	 * * @hibernate.property column="GEAENDERTAM" length="7"
	 * 
	 */
	public BaseDateTime getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(BaseDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public BaseDateTime getInactiveFromDate() {
		return this.inactiveFromDate;
	}

	public void setInactiveFromDate(BaseDateTime inactiveFromDate) {
		this.inactiveFromDate = inactiveFromDate;
	}

	/**
	 * * @hibernate.property column="ANZEIGE_ORDER"
	 * 
	 */
	public Long getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public FolderModel getFolder() {
		return this.folder;
	}

	public void setFolder(FolderModel folder) {
		this.folder = folder;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="KEREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Telementversion"
	 * 
	 */
	public Set<ElementVersionModel> getElementVersions() {
		return this.elementVersions;
	}

	public void setElementVersions(Set<ElementVersionModel> elementVersions) {
		this.elementVersions = elementVersions;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="BESITZER"
	 * 
	 */
	public BrandModel getOwner() {
		return this.owner;
	}

	public void setOwner(BrandModel owner) {
		this.owner = owner;
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
		buffer.append("elementId").append("='").append(getElementId()).append("' ");
		buffer.append("elementNumber").append("='").append(getElementNumber()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
		buffer.append("inactive").append("='").append(getInactive()).append("' ");
		buffer.append("creatorRefId").append("='").append(getCreatorRefId()).append("' ");
		buffer.append("creationDate").append("='").append(getCreationDate()).append("' ");
		buffer.append("modifierRefId").append("='").append(getModifierRefId()).append("' ");
		buffer.append("modificationDate").append("='").append(getModificationDate()).append("' ");
		buffer.append("displayOrder").append("='").append(getDisplayOrder()).append("' ");
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
		if (!(other instanceof ElementModel)) {
			return false;
		}
		ElementModel castOther = (ElementModel) other;

		return ((this.getElementId() == castOther.getElementId()) || (this.getElementId() != null
				&& castOther.getElementId() != null && this.getElementId().equals(castOther.getElementId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getElementId() == null ? 0 : this.getElementId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public String getNumberAndDesignation() {
		return getElementNumber() + " " + getDesignation();
	}

	public List<ElementVersionUsersModel> getUsers() {
		Set<ElementVersionUsersModel> users = new HashSet<ElementVersionUsersModel>();
		for (ElementVersionModel elementVersion : getElementVersions()) {
			for (ElementVersionUsersModel elementVersionUser : elementVersion.getElementVersionUsers()) {
				users.add(elementVersionUser);
			}
		}
		return new java.util.ArrayList<ElementVersionUsersModel>(users);
	}

	public List<String> getUsersImg() {
		List<String> users = new java.util.ArrayList<String>();
		List<ElementVersionUsersModel> usersObject = getUsers();
		if (usersObject != null) {
			for (ElementVersionUsersModel elementVersionUser : usersObject) {
				if (elementVersionUser.getActive() != null && elementVersionUser.getActive()) {
					users.add(elementVersionUser.getBrand().getUseImg());
				} else {
					users.add(elementVersionUser.getBrand().getUseInactiveImg());
				}

			}
			return users;
		}
		return null;
	}

	public String getOwnerImg() {
		return (getOwner() != null) ? getOwner().getOwnImg() : null;
	}

	// end of extra code specified in the hbm.xml files

}
