package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @hibernate.class table="T_ENUM_STATUS" q
 */

@Entity
@Table(name = "T_ENUM_STATUS")
// @Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumStatusModel")
public class EnumStatusModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	private Long enumStatusId;
	/**
	 * @hibernate.property column="BEZEICHNUNG" length="75"
	 * 
	 */
	@Column(name = "BEZEICHNUNG", length = 25)
	private String designation;

	@Column(name = "READ_ONLY")
	private Boolean readOnly = true;

	@Column(name = "ADD_ALLOWED")
	private Boolean addAllowed = true;

	@Column(name = "SAVE_ALLOWED")
	private Boolean saveAllowed = true;

	@Column(name = "DELETE_ALLOWED")
	private Boolean deleteAllowed = true;

	@Column(name = "BACK_ALLOWED")
	private Boolean backAllowed = true;

	@Column(name = "PRINT_ALLOWED")
	private Boolean printAllowed = true;

	@Column(name = "EXPORT_ALLOWED")
	private Boolean exportAllowed = true;

	public EnumStatusModel() {
	}

	public EnumStatusModel(Long enumStatusId, Boolean readOnly, Boolean addAllowed, Boolean saveAllowed,
			Boolean deleteAllowed, Boolean backAllowed, Boolean printAllowed, Boolean exportAllowed) {
		this.enumStatusId = enumStatusId;
		this.readOnly = readOnly;
		this.addAllowed = addAllowed;
		this.saveAllowed = saveAllowed;
		this.deleteAllowed = deleteAllowed;
		this.backAllowed = backAllowed;
		this.printAllowed = printAllowed;
		this.exportAllowed = exportAllowed;
	}

	public EnumStatusModel(Long enumStatusId, String designation, Boolean readOnly, Boolean addAllowed,
			Boolean saveAllowed, Boolean deleteAllowed, Boolean backAllowed, Boolean printAllowed,
			Boolean exportAllowed) {
		this.enumStatusId = enumStatusId;
		this.designation = designation;
		this.readOnly = readOnly;
		this.addAllowed = addAllowed;
		this.saveAllowed = saveAllowed;
		this.deleteAllowed = deleteAllowed;
		this.backAllowed = backAllowed;
		this.printAllowed = printAllowed;
		this.exportAllowed = exportAllowed;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEnumStatusId() {
		return this.enumStatusId;
	}

	private void setEnumStatusId(Long enumStatusId) {
		this.enumStatusId = enumStatusId;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" length="75"
	 * 
	 */
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public Boolean getReadOnly() {
		return this.readOnly;
	}

	public void setReadOnly(Boolean readOnly) {
		this.readOnly = readOnly;
	}

	public Boolean getAddAllowed() {
		return this.addAllowed;
	}

	public void setAddAllowed(Boolean addAllowed) {
		this.addAllowed = addAllowed;
	}

	public Boolean getSaveAllowed() {
		return this.saveAllowed;
	}

	public void setSaveAllowed(Boolean saveAllowed) {
		this.saveAllowed = saveAllowed;
	}

	public Boolean getDeleteAllowed() {
		return this.deleteAllowed;
	}

	public void setDeleteAllowed(Boolean deleteAllowed) {
		this.deleteAllowed = deleteAllowed;
	}

	public Boolean getBackAllowed() {
		return this.backAllowed;
	}

	public void setBackAllowed(Boolean backAllowed) {
		this.backAllowed = backAllowed;
	}

	public Boolean getPrintAllowed() {
		return this.printAllowed;
	}

	public void setPrintAllowed(Boolean printAllowed) {
		this.printAllowed = printAllowed;
	}

	public Boolean getExportAllowed() {
		return this.exportAllowed;
	}

	public void setExportAllowed(Boolean exportAllowed) {
		this.exportAllowed = exportAllowed;
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
		buffer.append("enumStatusId").append("='").append(getEnumStatusId()).append("' ");
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
		if (!(other instanceof EnumStatusModel)) {
			return false;
		}
		EnumStatusModel castOther = (EnumStatusModel) other;

		return ((this.getEnumStatusId() == castOther.getEnumStatusId()) || (this.getEnumStatusId() != null
				&& castOther.getEnumStatusId() != null && this.getEnumStatusId().equals(castOther.getEnumStatusId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEnumStatusId() == null ? 0 : this.getEnumStatusId().hashCode());

		return result;
	}

}
