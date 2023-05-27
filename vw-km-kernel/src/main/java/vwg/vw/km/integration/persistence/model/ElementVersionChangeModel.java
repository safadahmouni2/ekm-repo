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
import javax.persistence.Transient;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_ELEMENTVERSIONANDERUNG"
 * 
 */
@Entity
@Table(name = "T_ELEMENTVERSIONANDERUNG")
public class ElementVersionChangeModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ANDERUNG_ID"
	 * 
	 */
	@Id
	@Column(name = "ANDERUNG_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ELEMENT_VERSION_ANDERUNG")
	@SequenceGenerator(name = "SEQ_ELEMENT_VERSION_ANDERUNG", sequenceName = "SEQ_ELEMENT_VERSION_ANDERUNG", allocationSize = 1)
	private Long changeId;

	@Column(name = "ANDERUNG", length = 300)
	private String change;

	@Column(name = "ANDERUNG_ART", length = 300)
	private String changeType;

	@Column(name = "ALTER_WERT", length = 4000)
	private String oldValue;

	@Column(name = "NEUER_WERT", length = 4000)
	private String newValue;

	@Column(name = "ALTER_KOMMENTAR", length = 4000)
	private String oldComment;

	@Column(name = "NEUER_KOMMENTAR", length = 4000)
	private String newComment;

	@Column(name = "DATUM", columnDefinition = "DATE", nullable = false)
	private BaseDateTime date;

	@Column(name = "LANG", length = 1, nullable = false)
	private Boolean longValue;

	@Column(name = "ART", length = 100, nullable = false)
	private String type;

	@Column(name = "BENUTZER", length = 150)
	private String userFullName;
	/**
	 * @hibernate.property column="EINHEIT" length="10"
	 * 
	 */
	@Column(name = "EINHEIT", length = 10)
	private String unit;
	/**
	 * @hibernate.property column="ERSTELLER_REF_ID" length="19"
	 * 
	 */
	@Column(name = "ERSTELLER_REF_ID", length = 10)
	private Long creatorRefId;

	@ManyToOne
	@JoinColumn(name = "ELEM_VER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ELEM_VER_REF_ID", nullable = false)
	private ElementVersionModel elementVersion;

	public ElementVersionChangeModel() {
	}

	public ElementVersionChangeModel(BaseDateTime date, Boolean longValue) {
		this.date = date;
		this.longValue = longValue;
	}

	public ElementVersionChangeModel(String change, String changeType, String oldValue, String newValue,
			String oldComment, String newComment, BaseDateTime date, Boolean longValue, String type,
			String userFullName, String unit, Long creatorRefId, ElementVersionModel elementVersion) {
		this.change = change;
		this.changeType = changeType;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.oldComment = oldComment;
		this.newComment = newComment;
		this.date = date;
		this.longValue = longValue;
		this.type = type;
		this.userFullName = userFullName;
		this.unit = unit;
		this.creatorRefId = creatorRefId;
		this.elementVersion = elementVersion;
	}

	/**
	 * * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ANDERUNG_ID"
	 * 
	 */
	public Long getChangeId() {
		return this.changeId;
	}

	private void setChangeId(Long changeId) {
		this.changeId = changeId;
	}

	public String getChange() {
		return this.change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public String getChangeType() {
		return this.changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}

	public String getOldValue() {
		return this.oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return this.newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getOldComment() {
		return this.oldComment;
	}

	public void setOldComment(String oldComment) {
		this.oldComment = oldComment;
	}

	public String getNewComment() {
		return this.newComment;
	}

	public void setNewComment(String newComment) {
		this.newComment = newComment;
	}

	public BaseDateTime getDate() {
		return this.date;
	}

	public void setDate(BaseDateTime date) {
		this.date = date;
	}

	public Boolean getLongValue() {
		return this.longValue;
	}

	public void setLongValue(Boolean longValue) {
		this.longValue = longValue;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUserFullName() {
		return this.userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	/**
	 * * @hibernate.property column="EINHEIT" length="10"
	 * 
	 */
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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

	public ElementVersionModel getElementVersion() {
		return this.elementVersion;
	}

	public void setElementVersion(ElementVersionModel elementVersion) {
		this.elementVersion = elementVersion;
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
		buffer.append("changeId").append("='").append(getChangeId()).append("' ");
		buffer.append("creatorRefId").append("='").append(getCreatorRefId()).append("' ");
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
		if (!(other instanceof ElementVersionChangeModel)) {
			return false;
		}
		ElementVersionChangeModel castOther = (ElementVersionChangeModel) other;

		return ((this.getChangeId() == castOther.getChangeId()) || (this.getChangeId() != null
				&& castOther.getChangeId() != null && this.getChangeId().equals(castOther.getChangeId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getChangeId() == null ? 0 : this.getChangeId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	@Transient
	private String remark;

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Transient
	private java.util.List<BrandModel> users = new java.util.ArrayList<BrandModel>(0);

	public java.util.List<BrandModel> getUsers() {
		return users;
	}

	public void setUsers(java.util.List<BrandModel> users) {
		this.users = users;
	}

	// end of extra code specified in the hbm.xml files

}
