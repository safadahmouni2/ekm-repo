package vwg.vw.km.integration.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_REL_BAUSVERSION_NUTZER"
 * 
 */

@Entity
@Table(name = "T_REL_BAUSVERSION_NUTZER")
public class ComponentStandUsersModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BAUSVERSION_NUTZER")
	@SequenceGenerator(name = "SEQ_BAUSVERSION_NUTZER", sequenceName = "SEQ_BAUSVERSION_NUTZER", allocationSize = 1)
	private Long id;
	/**
	 * @hibernate.property column="AKTIV" length="1"
	 * 
	 */

	@Column(name = "AKTIV", length = 1)
	private Boolean active;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KB_STAND_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KB_STAND_ID")
	private ComponentStandModel componentStand;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="MARKE_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "MARKE_REF_ID", nullable = false)
	private BrandModel brand;

	public ComponentStandUsersModel() {
	}

	public ComponentStandUsersModel(Boolean active, ComponentStandModel componentStand, BrandModel brand) {
		this.active = active;
		this.componentStand = componentStand;
		this.brand = brand;
	}

	/**
	 * * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getId() {
		return this.id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	/**
	 * * @hibernate.property column="AKTIV" length="1"
	 * 
	 */
	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KB_STAND_ID"
	 * 
	 */
	public ComponentStandModel getComponentStand() {
		return this.componentStand;
	}

	public void setComponentStand(ComponentStandModel componentStand) {
		this.componentStand = componentStand;
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
		buffer.append("id").append("='").append(getId()).append("' ");
		buffer.append("active").append("='").append(getActive()).append("' ");
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
		if (!(other instanceof ComponentStandUsersModel)) {
			return false;
		}
		ComponentStandUsersModel castOther = (ComponentStandUsersModel) other;

		return ((this.getId() == castOther.getId())
				|| (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

}
