package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @hibernate.class table="T_ENUM_OBJEKTTYP"
 * 
 */

@Entity
@Table(name = "T_ENUM_OBJEKTTYP")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumObjectTypeModel")
public class EnumObjectTypeModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_object_type")
	@SequenceGenerator(name = "seq_object_type", sequenceName = "SEQ_OBJECT_TYPE", allocationSize = 1)
	private Long enumObjectTypeId;
	/**
	 * @hibernate.property column="OBJEKTTYP" length="150" not-null="true"
	 * 
	 */

	@Column(name = "OBJEKT_TYP", length = 150, nullable = false)
	private String objectType;

	public EnumObjectTypeModel() {
	}

	public EnumObjectTypeModel(String objectType) {
		this.objectType = objectType;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEnumObjectTypeId() {
		return this.enumObjectTypeId;
	}

	private void setEnumObjectTypeId(Long enumObjectTypeId) {
		this.enumObjectTypeId = enumObjectTypeId;
	}

	/**
	 * * @hibernate.property column="OBJEKTTYP" length="150" not-null="true"
	 * 
	 */
	public String getObjectType() {
		return this.objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
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
		buffer.append("enumObjectTypeId").append("='").append(getEnumObjectTypeId()).append("' ");
		buffer.append("objectType").append("='").append(getObjectType()).append("' ");
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
		if (!(other instanceof EnumObjectTypeModel)) {
			return false;
		}
		EnumObjectTypeModel castOther = (EnumObjectTypeModel) other;

		return ((this.getEnumObjectTypeId() == castOther.getEnumObjectTypeId())
				|| (this.getEnumObjectTypeId() != null && castOther.getEnumObjectTypeId() != null
						&& this.getEnumObjectTypeId().equals(castOther.getEnumObjectTypeId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEnumObjectTypeId() == null ? 0 : this.getEnumObjectTypeId().hashCode());

		return result;
	}

}
