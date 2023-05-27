package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_ARBEITSBEREICH"
 * 
 */
@Entity
@Table(name = "T_ARBEITSBEREICH")
public class WorkAreaModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_work_area")
	@SequenceGenerator(name = "seq_work_area", sequenceName = "SEQ_WORK_AREA", allocationSize = 1)
	private Long workAreaId;
	/**
	 * @hibernate.property column="BEZEICHNUNG" length="50" not-null="true"
	 * 
	 */

	@Column(name = "BEZEICHNUNG", length = 50, nullable = false)
	private String designation;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="4000"
	 * 
	 */

	@Column(name = "BESCHREIBUNG", length = 4000)
	private String description;
	/**
	 * @hibernate.property column="IMG_ROOT_PFAD" length="500"
	 * 
	 */

	@Column(name = "IMG_ROOT_PFAD", length = 500)
	private String imagesRootPath;
	/**
	 * @hibernate.property column="ERSTELLT_AM" length="7"
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
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="MARKE_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "MARKE_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "MARKE_REF_ID", nullable = false)
	private BrandModel brand;

	public WorkAreaModel() {
	}

	public WorkAreaModel(String designation) {
		this.designation = designation;
	}

	public WorkAreaModel(String designation, String description, String imagesRootPath, BaseDateTime creationDate,
			Long creatorRefId, BaseDateTime modificationDate, Long modifierRefId, BrandModel brand) {
		this.designation = designation;
		this.description = description;
		this.imagesRootPath = imagesRootPath;
		this.creationDate = creationDate;
		this.creatorRefId = creatorRefId;
		this.modificationDate = modificationDate;
		this.modifierRefId = modifierRefId;
		this.brand = brand;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getWorkAreaId() {
		return this.workAreaId;
	}

	private void setWorkAreaId(Long workAreaId) {
		this.workAreaId = workAreaId;
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
	 * * @hibernate.property column="BESCHREIBUNG" length="4000"
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
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
	 * * @hibernate.property column="ERSTELLT_AM" length="7"
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
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="MARKE_REF_ID"
	 * 
	 */
	public BrandModel getBrand() {
		return this.brand;
	}

	public void setBrand(BrandModel brand) {
		this.brand = brand;
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
		buffer.append("workAreaId").append("='").append(getWorkAreaId()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
		buffer.append("creatorRefId").append("='").append(getCreatorRefId()).append("' ");
		buffer.append("modifierRefId").append("='").append(getModifierRefId()).append("' ");
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
		if (!(other instanceof WorkAreaModel)) {
			return false;
		}
		WorkAreaModel castOther = (WorkAreaModel) other;

		return ((this.getWorkAreaId() == castOther.getWorkAreaId()) || (this.getWorkAreaId() != null
				&& castOther.getWorkAreaId() != null && this.getWorkAreaId().equals(castOther.getWorkAreaId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getWorkAreaId() == null ? 0 : this.getWorkAreaId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public String getBrandIcon() {
		return (getBrand() != null) ? getBrand().getOwnImg() : null;
	}

	// end of extra code specified in the hbm.xml files

}
