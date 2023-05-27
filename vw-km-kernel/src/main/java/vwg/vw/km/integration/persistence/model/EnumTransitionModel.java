package vwg.vw.km.integration.persistence.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_ENUM_TRANSITION"
 * 
 */
@Entity
@Table(name = "T_ENUM_TRANSITION")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumTransitionModel")
public class EnumTransitionModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	@EmbeddedId
	private EnumTransitionCompPK comp_id;
	/**
	 * @hibernate.property column="BEZEICHNUNG" length="75"
	 * 
	 */
	@Column(name = "BEZEICHNUNG", length = 25)
	private String designation;

	@Column(name = "BATCH", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean batch = Boolean.TRUE;

	public EnumTransitionModel() {
	}

	public EnumTransitionModel(EnumTransitionCompPK comp_id, Boolean batch) {
		this.comp_id = comp_id;
		this.batch = batch;
	}

	public EnumTransitionModel(EnumTransitionCompPK comp_id, String designation, Boolean batch) {
		this.comp_id = comp_id;
		this.designation = designation;
		this.batch = batch;
	}

	public EnumTransitionCompPK getComp_id() {
		return this.comp_id;
	}

	public void setComp_id(EnumTransitionCompPK comp_id) {
		this.comp_id = comp_id;
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

	public Boolean getBatch() {
		return this.batch;
	}

	public void setBatch(Boolean batch) {
		this.batch = batch;
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
		buffer.append("comp_id").append("='").append(getComp_id()).append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}
