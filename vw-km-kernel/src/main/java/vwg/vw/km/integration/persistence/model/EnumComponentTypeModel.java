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
 * @hibernate.class table="T_ENUM_BAUSTEINTYP"
 * 
 */

@Entity
@Table(name = "T_ENUM_BAUSTEINTYP")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumComponentTypeModel")
public class EnumComponentTypeModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component_type")
	@SequenceGenerator(name = "seq_component_type", sequenceName = "SEQ_COMPONENT_TYPE", allocationSize = 1)
	private Long enumComponentTypeId;
	/**
	 * @hibernate.property column="BAUSTEINTYP" length="256" not-null="true"
	 * 
	 */

	@Column(name = "BAUSTEIN_TYP", length = 256, nullable = false)
	private String componentType;
	/**
	 * @hibernate.property column="BAUSTEIN_TYP_EN" length="256" not-null="true"
	 * 
	 */

	@Column(name = "BAUSTEIN_TYP_EN", length = 256, nullable = false)
	private String componentTypeEn;

	public EnumComponentTypeModel() {
	}

	public EnumComponentTypeModel(String componentType, String componentTypeEn) {
		this.componentType = componentType;
		this.componentTypeEn = componentTypeEn;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEnumComponentTypeId() {
		return this.enumComponentTypeId;
	}

	private void setEnumComponentTypeId(Long enumComponentTypeId) {
		this.enumComponentTypeId = enumComponentTypeId;
	}

	/**
	 * * @hibernate.property column="BAUSTEINTYP" length="256" not-null="true"
	 * 
	 */
	public String getComponentType() {
		return this.componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	/**
	 * * @hibernate.property column="BAUSTEIN_TYP_EN" length="256" not-null="true"
	 * 
	 */
	public String getComponentTypeEn() {
		return this.componentTypeEn;
	}

	public void setComponentTypeEn(String componentTypeEn) {
		this.componentTypeEn = componentTypeEn;
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
		buffer.append("enumComponentTypeId").append("='").append(getEnumComponentTypeId()).append("' ");
		buffer.append("componentType").append("='").append(getComponentType()).append("' ");
		buffer.append("componentTypeEn").append("='").append(getComponentTypeEn()).append("' ");
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
		if (!(other instanceof EnumComponentTypeModel)) {
			return false;
		}
		EnumComponentTypeModel castOther = (EnumComponentTypeModel) other;

		return ((this.getEnumComponentTypeId() == castOther.getEnumComponentTypeId())
				|| (this.getEnumComponentTypeId() != null && castOther.getEnumComponentTypeId() != null
						&& this.getEnumComponentTypeId().equals(castOther.getEnumComponentTypeId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEnumComponentTypeId() == null ? 0 : this.getEnumComponentTypeId().hashCode());

		return result;
	}

}
