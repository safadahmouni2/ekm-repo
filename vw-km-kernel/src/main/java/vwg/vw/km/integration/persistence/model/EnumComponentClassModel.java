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
 * @hibernate.class table="T_ENUM_BAUSTEINKLASSE"
 * 
 */

@Entity
@Table(name = "T_ENUM_BAUSTEINKLASSE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumComponentClassModel")
public class EnumComponentClassModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component_class")
	@SequenceGenerator(name = "seq_component_class", sequenceName = "SEQ_COMPONENT_CLASS", allocationSize = 1)
	private Long enumComponentClassId;
	/**
	 * @hibernate.property column="BAUSTEINTYP" length="50" not-null="true"
	 * 
	 */

	@Column(name = "BAUSTEIN_KLASSE", length = 50, nullable = false)
	private String componentClass;

	public EnumComponentClassModel() {
	}

	public EnumComponentClassModel(String componentClass) {
		this.componentClass = componentClass;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEnumComponentClassId() {
		return this.enumComponentClassId;
	}

	private void setEnumComponentClassId(Long enumComponentClassId) {
		this.enumComponentClassId = enumComponentClassId;
	}

	/**
	 * * @hibernate.property column="BAUSTEINTYP" length="50" not-null="true"
	 * 
	 */
	public String getComponentClass() {
		return this.componentClass;
	}

	public void setComponentClass(String componentClass) {
		this.componentClass = componentClass;
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
		buffer.append("enumComponentClassId").append("='").append(getEnumComponentClassId()).append("' ");
		buffer.append("componentClass").append("='").append(getComponentClass()).append("' ");
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
		if (!(other instanceof EnumComponentClassModel)) {
			return false;
		}
		EnumComponentClassModel castOther = (EnumComponentClassModel) other;

		return ((this.getEnumComponentClassId() == castOther.getEnumComponentClassId())
				|| (this.getEnumComponentClassId() != null && castOther.getEnumComponentClassId() != null
						&& this.getEnumComponentClassId().equals(castOther.getEnumComponentClassId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEnumComponentClassId() == null ? 0 : this.getEnumComponentClassId().hashCode());

		return result;
	}

}
