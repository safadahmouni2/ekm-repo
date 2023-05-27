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
 * ElementCategoryModel generated by hbm2java
 */

@Entity
@Table(name = "T_ELEMENTKATEGORIE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "ElementCategoryModel")
public class ElementCategoryModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_element_category")
	@SequenceGenerator(name = "seq_element_category", sequenceName = "SEQ_ELEMENT_CATEGORY", allocationSize = 1)
	private Long elementCategoryId;
	/**
	 * @hibernate.property column="BEZEICHNUNG" unique="true" length="50" not-null="true"
	 * 
	 */

	@Column(name = "BEZEICHNUNG", length = 50, unique = true, nullable = false)
	private String designation;

	public ElementCategoryModel() {
	}

	public ElementCategoryModel(String designation) {
		this.designation = designation;
	}

	public Long getElementCategoryId() {
		return this.elementCategoryId;
	}

	private void setElementCategoryId(Long elementCategoryId) {
		this.elementCategoryId = elementCategoryId;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" unique="true" length="50" not-null="true"
	 * 
	 */
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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
		buffer.append("elementCategoryId").append("='").append(getElementCategoryId()).append("' ");
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
		if (!(other instanceof ElementCategoryModel)) {
			return false;
		}
		ElementCategoryModel castOther = (ElementCategoryModel) other;

		return ((this.getElementCategoryId() == castOther.getElementCategoryId())
				|| (this.getElementCategoryId() != null && castOther.getElementCategoryId() != null
						&& this.getElementCategoryId().equals(castOther.getElementCategoryId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getElementCategoryId() == null ? 0 : this.getElementCategoryId().hashCode());

		return result;
	}

}