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
import javax.persistence.Transient;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_BAUSTEIN"
 * 
 */

@Entity
@Table(name = "T_BAUSTEIN")
public class ComponentModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKB"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component")
	@SequenceGenerator(name = "seq_component", sequenceName = "SEQ_COMPONENT", allocationSize = 1)
	private Long componentId;
	/**
	 * @hibernate.property column="UUIDKB" length="1024"
	 * 
	 */

	@Column(name = "UUIDKB", length = 1024)
	private String externalId;
	/**
	 * @hibernate.property column="BAUSTEINNR" unique="true" length="256" not-null="true"
	 * 
	 */

	@Column(name = "BAUSTEIN_NR", length = 256, unique = true, nullable = false)
	private String componentNumber;

	@Column(name = "BAUSTEIN_NR", length = 256, insertable = false, updatable = false)
	private String oldComponentNumber;
	/**
	 * @hibernate.property column="BEZEICHNUNG" unique="true" length="256" not-null="true"
	 * 
	 */

	@Column(name = "BEZEICHNUNG", length = 256, nullable = false, unique = true)
	private String designation;

	@Column(name = "BEZEICHNUNG", length = 256, insertable = false, updatable = false)
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

	@Column(name = "X_INAKTIV", insertable = false, updatable = false)
	private Boolean oldInactive;
	/**
	 * @hibernate.property column="ERSTELLERREFID" length="10" not-null="true"
	 * 
	 */

	@Column(name = "ERSTELLER_REF_ID", length = 10, nullable = false)
	private long manifacturerRefId;
	/**
	 * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */

	@Column(name = "ERSTELLT_AM", nullable = false, columnDefinition = "DATE")
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="GEAENDERTVONREFID" length="10"
	 * 
	 */

	@Column(name = "GEAENDERT_VON_REF_ID", length = 10)
	private Long modifierRefId;
	/**
	 * @hibernate.property column="GEANDETAM" length="7"
	 * 
	 */
	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	// TODO conflict with DB
	// @Column(name = "GEAENDERT_AM", length = 8, nullable = false, columnDefinition = "DATE")
	private BaseDateTime modificationDate;

	@Column(name = "INAKTIV_VON", columnDefinition = "DATE")
	// TODO conflict with DB
	// @Column(name = "INAKTIV_VON", length = 8, nullable = false, columnDefinition = "DATE")
	private BaseDateTime inactiveFromDate;
	/**
	 * @hibernate.property column="ANZEIGE_ORDER"
	 * 
	 */
	@Column(name = "ANZEIGE_ORDER", nullable = false)
	private Long displayOrder;
	/**
	 * @hibernate.property column="IMG_PFAD" length="1000"
	 * 
	 */

	@Column(name = "IMG_PFAD", length = 1000)
	private String imagePath;

	@ManyToOne
	@JoinColumn(name = "ORDNER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ORDNER_REF_ID", nullable = false)
	private FolderModel folder;
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
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="BIBLIOTHEK_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "BIBLIOTHEK_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "BIBLIOTHEK_REF_ID", nullable = false)
	private LibraryModel library;
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="KBREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Tbausteinversion"
	 * 
	 */

	@OneToMany(mappedBy = "component", fetch = FetchType.LAZY, orphanRemoval = true)
	@OrderBy("ERSTELLT_AM DESC")
	private Set<ComponentVersionModel> componentVersions = new HashSet<ComponentVersionModel>(0);

	public ComponentModel() {
	}

	public ComponentModel(String componentNumber, String designation, Boolean inactive, long manifacturerRefId,
			BaseDateTime creationDate, Long displayOrder) {
		this.componentNumber = componentNumber;
		this.designation = designation;
		this.inactive = inactive;
		this.manifacturerRefId = manifacturerRefId;
		this.creationDate = creationDate;
		this.displayOrder = displayOrder;
	}

	public ComponentModel(String externalId, String componentNumber, String oldComponentNumber, String designation,
			String oldDesignation, Boolean inactive, Boolean oldInactive, long manifacturerRefId,
			BaseDateTime creationDate, Long modifierRefId, BaseDateTime modificationDate, BaseDateTime inactiveFromDate,
			Long displayOrder, String imagePath, FolderModel folder, BrandModel owner, LibraryModel library,
			Set<ComponentVersionModel> componentVersions) {
		this.externalId = externalId;
		this.componentNumber = componentNumber;
		this.oldComponentNumber = oldComponentNumber;
		this.designation = designation;
		this.oldDesignation = oldDesignation;
		this.inactive = inactive;
		this.oldInactive = oldInactive;
		this.manifacturerRefId = manifacturerRefId;
		this.creationDate = creationDate;
		this.modifierRefId = modifierRefId;
		this.modificationDate = modificationDate;
		this.inactiveFromDate = inactiveFromDate;
		this.displayOrder = displayOrder;
		this.imagePath = imagePath;
		this.folder = folder;
		this.owner = owner;
		this.library = library;
		this.componentVersions = componentVersions;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKB"
	 * 
	 */
	public Long getComponentId() {
		return this.componentId;
	}

	private void setComponentId(Long componentId) {
		this.componentId = componentId;
	}

	/**
	 * * @hibernate.property column="UUIDKB" length="1024"
	 * 
	 */
	public String getExternalId() {
		return this.externalId;
	}

	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * * @hibernate.property column="BAUSTEINNR" unique="true" length="256" not-null="true"
	 * 
	 */
	public String getComponentNumber() {
		return this.componentNumber;
	}

	public void setComponentNumber(String componentNumber) {
		this.componentNumber = componentNumber;
	}

	public String getOldComponentNumber() {
		return this.oldComponentNumber;
	}

	public void setOldComponentNumber(String oldComponentNumber) {
		this.oldComponentNumber = oldComponentNumber;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" unique="true" length="256" not-null="true"
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
	 * * @hibernate.property column="ERSTELLERREFID" length="10" not-null="true"
	 * 
	 */
	public long getManifacturerRefId() {
		return this.manifacturerRefId;
	}

	public void setManifacturerRefId(long manifacturerRefId) {
		this.manifacturerRefId = manifacturerRefId;
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
	 * * @hibernate.property column="GEAENDERTVONREFID" length="10"
	 * 
	 */
	public Long getModifierRefId() {
		return this.modifierRefId;
	}

	public void setModifierRefId(Long modifierRefId) {
		this.modifierRefId = modifierRefId;
	}

	/**
	 * * @hibernate.property column="GEANDETAM" length="7"
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

	/**
	 * * @hibernate.property column="IMG_PFAD" length="1000"
	 * 
	 */
	public String getImagePath() {
		return this.imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public FolderModel getFolder() {
		return this.folder;
	}

	public void setFolder(FolderModel folder) {
		this.folder = folder;
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
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="BIBLIOTHEK_REF_ID"
	 * 
	 */
	public LibraryModel getLibrary() {
		return this.library;
	}

	public void setLibrary(LibraryModel library) {
		this.library = library;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="KBREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Tbausteinversion"
	 * 
	 */
	public Set<ComponentVersionModel> getComponentVersions() {
		return this.componentVersions;
	}

	public void setComponentVersions(Set<ComponentVersionModel> componentVersions) {
		this.componentVersions = componentVersions;
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
		buffer.append("componentId").append("='").append(getComponentId()).append("' ");
		buffer.append("componentNumber").append("='").append(getComponentNumber()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
		buffer.append("inactive").append("='").append(getInactive()).append("' ");
		buffer.append("creationDate").append("='").append(getCreationDate()).append("' ");
		buffer.append("modifierRefId").append("='").append(getModifierRefId()).append("' ");
		buffer.append("modificationDate").append("='").append(getModificationDate()).append("' ");
		buffer.append("inactiveFromDate").append("='").append(getInactiveFromDate()).append("' ");
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
		if (!(other instanceof ComponentModel)) {
			return false;
		}
		ComponentModel castOther = (ComponentModel) other;

		return ((this.getComponentId() == castOther.getComponentId()) || (this.getComponentId() != null
				&& castOther.getComponentId() != null && this.getComponentId().equals(castOther.getComponentId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getComponentId() == null ? 0 : this.getComponentId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public String getNumberAndDesignation() {
		return getComponentNumber() + " " + getDesignation();
	}

	public String getExternalComponentId() {
		if (getComponentId() == null) {
			return "";
		}
		if (getExternalId() == null) {
			return "" + getComponentId();
		} else {
			return getExternalId();
		}
	}

	public String getOwnerImg() {
		return (getOwner() != null) ? getOwner().getOwnImg() : null;
	}

	@Transient
	List<ComponentStandUsersModel> users;

	public void setUsers(List<ComponentStandUsersModel> users) {
		this.users = users;
	}

	public List<ComponentStandUsersModel> getUsers() {
		return users;
	}

	public List<String> getUsersImg() {
		List<String> usersImg = new java.util.ArrayList<String>();
		List<ComponentStandUsersModel> usersObject = getUsers();
		if (usersObject != null) {
			for (ComponentStandUsersModel standUser : usersObject) {
				if (standUser.getActive() != null && standUser.getActive()) {
					usersImg.add(standUser.getBrand().getUseImg());
				} else {
					usersImg.add(standUser.getBrand().getUseInactiveImg());
				}

			}
			return usersImg;
		}
		return null;
	}

	public String getLibrayLabel() {
		String libName = null;
		LibraryModel lib = getLibrary();
		if (lib != null) {
			libName = lib.getName();
		}
		return libName;
	}

	public String getLibrayIcon() {
		String libIcon = null;
		LibraryModel lib = getLibrary();
		if (lib != null) {
			libIcon = lib.getColor();
		}
		return libIcon;
	}

	public int getStandCount() {
		int count = 0;
		for (ComponentVersionModel componentVersion : getComponentVersions()) {
			if (componentVersion.getNumber() != null) {
				count += componentVersion.getStandCount();
			}
		}
		return count;
	}

	// end of extra code specified in the hbm.xml files

}
