package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @hibernate.class table="T_LEISTUNGSINHALT"
 * 
 */

@Entity
@Table(name = "T_LEISTUNGSINHALT")
public class EffortContentModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_effort_content")
	@SequenceGenerator(name = "seq_effort_content", sequenceName = "SEQ_EFFORT_CONTENT", allocationSize = 1)
	private Long effortContentId;
	/**
	 * @hibernate.property column="MECHANIKKONSTRUKTION" length="4000" not-null="true"
	 * 
	 */

	@Column(name = "MECHANIK_KONSTRUKTION", length = 4000, nullable = false)
	private String mechanicalConstruction;
	/**
	 * @hibernate.property column="MECHANIKAUSFUEHRUNG" length="4000" not-null="true"
	 * 
	 */

	@Column(name = "MECHANIK_AUSFUEHRUNG", length = 4000, nullable = false)
	private String mechanicalExecution;
	/**
	 * @hibernate.property column="ELEKTRIK" length="4000" not-null="true"
	 * 
	 */

	@Column(name = "ELEKTRIK", length = 4000, nullable = false)
	private String electric;
	/**
	 * @hibernate.property column="BEMERKUNG" length="4000" not-null="true"
	 * 
	 */

	@Column(name = "BEMERKUNG", length = 4000, nullable = false)
	private String comment;
	/**
	 * @hibernate.property column="XDEFAULTLEISTUNGSINHALT" length="1" not-null="true"
	 * 
	 */

	@Column(name = "X_DEFAULT_LEISTUNGSINHALT", length = 1, nullable = false)
	private Boolean defaultEffortContent;
	/**
	 * @hibernate.property column="ENUMOBJREFID" length="10"
	 * 
	 */

	@Column(name = "ENUM_OBJ_REF_ID", length = 10)
	private Long enumObjectRefId;
	/**
	 * @hibernate.property column="OBJVERREFID" length="10"
	 * 
	 */

	@Column(name = "OBJ_VER_REF_ID", length = 10)
	private Long objectVersionRefId;

	public EffortContentModel() {
	}

	public EffortContentModel(String mechanicalConstruction, String mechanicalExecution, String electric,
			String comment, Boolean defaultEffortContent) {
		this.mechanicalConstruction = mechanicalConstruction;
		this.mechanicalExecution = mechanicalExecution;
		this.electric = electric;
		this.comment = comment;
		this.defaultEffortContent = defaultEffortContent;
	}

	public EffortContentModel(String mechanicalConstruction, String mechanicalExecution, String electric,
			String comment, Boolean defaultEffortContent, Long enumObjectRefId, Long objectVersionRefId) {
		this.mechanicalConstruction = mechanicalConstruction;
		this.mechanicalExecution = mechanicalExecution;
		this.electric = electric;
		this.comment = comment;
		this.defaultEffortContent = defaultEffortContent;
		this.enumObjectRefId = enumObjectRefId;
		this.objectVersionRefId = objectVersionRefId;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEffortContentId() {
		return this.effortContentId;
	}

	private void setEffortContentId(Long effortContentId) {
		this.effortContentId = effortContentId;
	}

	/**
	 * * @hibernate.property column="MECHANIKKONSTRUKTION" length="4000" not-null="true"
	 * 
	 */
	public String getMechanicalConstruction() {
		return this.mechanicalConstruction;
	}

	public void setMechanicalConstruction(String mechanicalConstruction) {
		this.mechanicalConstruction = mechanicalConstruction;
	}

	/**
	 * * @hibernate.property column="MECHANIKAUSFUEHRUNG" length="4000" not-null="true"
	 * 
	 */
	public String getMechanicalExecution() {
		return this.mechanicalExecution;
	}

	public void setMechanicalExecution(String mechanicalExecution) {
		this.mechanicalExecution = mechanicalExecution;
	}

	/**
	 * * @hibernate.property column="ELEKTRIK" length="4000" not-null="true"
	 * 
	 */
	public String getElectric() {
		return this.electric;
	}

	public void setElectric(String electric) {
		this.electric = electric;
	}

	/**
	 * * @hibernate.property column="BEMERKUNG" length="4000" not-null="true"
	 * 
	 */
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * * @hibernate.property column="XDEFAULTLEISTUNGSINHALT" length="1" not-null="true"
	 * 
	 */
	public Boolean getDefaultEffortContent() {
		return this.defaultEffortContent;
	}

	public void setDefaultEffortContent(Boolean defaultEffortContent) {
		this.defaultEffortContent = defaultEffortContent;
	}

	/**
	 * * @hibernate.property column="ENUMOBJREFID" length="10"
	 * 
	 */
	public Long getEnumObjectRefId() {
		return this.enumObjectRefId;
	}

	public void setEnumObjectRefId(Long enumObjectRefId) {
		this.enumObjectRefId = enumObjectRefId;
	}

	/**
	 * * @hibernate.property column="OBJVERREFID" length="10"
	 * 
	 */
	public Long getObjectVersionRefId() {
		return this.objectVersionRefId;
	}

	public void setObjectVersionRefId(Long objectVersionRefId) {
		this.objectVersionRefId = objectVersionRefId;
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
		buffer.append("effortContentId").append("='").append(getEffortContentId()).append("' ");
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
		if (!(other instanceof EffortContentModel)) {
			return false;
		}
		EffortContentModel castOther = (EffortContentModel) other;

		return ((this.getEffortContentId() == castOther.getEffortContentId())
				|| (this.getEffortContentId() != null && castOther.getEffortContentId() != null
						&& this.getEffortContentId().equals(castOther.getEffortContentId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEffortContentId() == null ? 0 : this.getEffortContentId().hashCode());

		return result;
	}

}
