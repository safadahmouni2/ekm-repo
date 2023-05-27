package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_STDSATZKATALOG"
 * 
 */
@Entity
@Table(name = "T_STDSATZKATALOG")
public class HourlyRateCatalogModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hourly_rate_catalog")
	@SequenceGenerator(name = "seq_hourly_rate_catalog", sequenceName = "SEQ_HOURLY_RATE_CATALOG", allocationSize = 1)
	private Long hourlyRateCatalogId;
	/**
	 * @hibernate.property column="BEZEICHNUNG" length="50" not-null="true"
	 * 
	 */
	@Column(name = "BEZEICHNUNG", length = 50, nullable = false)
	private String designation;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="255"
	 * 
	 */
	@Column(name = "BESCHREIBUNG", length = 255)
	private String description;
	/**
	 * @hibernate.property column="ERSTELLERREFID" length="10" not-null="true"
	 * 
	 */
	@Column(name = "ERSTELLER_REF_ID", length = 10, nullable = false)
	private Long creatorRefId;
	/**
	 * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */
	@Column(name = "ERSTELLT_AM", columnDefinition = "DATE", nullable = false)
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="GEANDERTVONREFID" length="10"
	 * 
	 */
	@Column(name = "GEAENDERT_VON_REF_ID", length = 10)
	private Long modifierIdRef;
	/**
	 * @hibernate.property column="GEANDERTAM" length="7"
	 * 
	 */
	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	private BaseDateTime modificationDate;

	@ManyToOne
	@JoinColumn(name = "WORK_AREA_ID")
	// TODO conflict with DB
	// @JoinColumn(name="WORK_AREA_ID", nullable = false)
	private WorkAreaModel workArea;

	@ManyToOne
	@JoinColumn(name = "ORDNER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name="ORDNER_REF_ID", nullable = false)
	private FolderModel folder;
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="STDSATZKATREFID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Tstundensaetze"
	 * 
	 */
	@OneToMany(mappedBy = "hourlyRateCatalog")
	private Set<HourlyRateModel> hourlyRates = new HashSet<HourlyRateModel>(0);

	public HourlyRateCatalogModel() {
	}

	public HourlyRateCatalogModel(String designation, Long creatorRefId, BaseDateTime creationDate) {
		this.designation = designation;
		this.creatorRefId = creatorRefId;
		this.creationDate = creationDate;
	}

	public HourlyRateCatalogModel(String designation, String description, Long creatorRefId, BaseDateTime creationDate,
			Long modifierIdRef, BaseDateTime modificationDate, WorkAreaModel workArea, FolderModel folder,
			Set<HourlyRateModel> hourlyRates) {
		this.designation = designation;
		this.description = description;
		this.creatorRefId = creatorRefId;
		this.creationDate = creationDate;
		this.modifierIdRef = modifierIdRef;
		this.modificationDate = modificationDate;
		this.workArea = workArea;
		this.folder = folder;
		this.hourlyRates = hourlyRates;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getHourlyRateCatalogId() {
		return this.hourlyRateCatalogId;
	}

	private void setHourlyRateCatalogId(Long hourlyRateCatalogId) {
		this.hourlyRateCatalogId = hourlyRateCatalogId;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" length="50" not-null="true"
	 * 
	 */
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * * @hibernate.property column="BESCHREIBUNG" length="255"
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * * @hibernate.property column="ERSTELLERREFID" length="10" not-null="true"
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
	 * * @hibernate.property column="GEANDERTVONREFID" length="10"
	 * 
	 */
	public Long getModifierIdRef() {
		return this.modifierIdRef;
	}

	public void setModifierIdRef(Long modifierIdRef) {
		this.modifierIdRef = modifierIdRef;
	}

	/**
	 * * @hibernate.property column="GEANDERTAM" length="7"
	 * 
	 */
	public BaseDateTime getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(BaseDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	public WorkAreaModel getWorkArea() {
		return this.workArea;
	}

	public void setWorkArea(WorkAreaModel workArea) {
		this.workArea = workArea;
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
	 * @hibernate.collection-key column="STDSATZKATREFID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Tstundensaetze"
	 * 
	 */
	public Set<HourlyRateModel> getHourlyRates() {
		return this.hourlyRates;
	}

	public void setHourlyRates(Set<HourlyRateModel> hourlyRates) {
		this.hourlyRates = hourlyRates;
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
		buffer.append("hourlyRateCatalogId").append("='").append(getHourlyRateCatalogId()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
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
		if (!(other instanceof HourlyRateCatalogModel)) {
			return false;
		}
		HourlyRateCatalogModel castOther = (HourlyRateCatalogModel) other;

		return ((this.getHourlyRateCatalogId() == castOther.getHourlyRateCatalogId())
				|| (this.getHourlyRateCatalogId() != null && castOther.getHourlyRateCatalogId() != null
						&& this.getHourlyRateCatalogId().equals(castOther.getHourlyRateCatalogId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getHourlyRateCatalogId() == null ? 0 : this.getHourlyRateCatalogId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files
	@Transient
	public HourlyRateModel getHourlyRateForCostAttribute(CostAttributeModel costAttribute) {
		for (HourlyRateModel hrm : getHourlyRates()) {
			if (hrm.getCostAttribute().equals(costAttribute)) {
				return hrm;
			}
		}
		return null;
	}

	// end of extra code specified in the hbm.xml files

}
