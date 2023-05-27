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
 * @hibernate.class table="T_BIBLIOTHEK"
 * 
 */
@Entity
@Table(name = "T_BIBLIOTHEK")
public class LibraryModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_library")
	@SequenceGenerator(name = "seq_library", sequenceName = "SEQ_LIBRARY", allocationSize = 1)
	private Long libraryId;
	/**
	 * @hibernate.property column="NAME" length="50"
	 * 
	 */

	@Column(name = "NAME", length = 50)
	private String name;
	/**
	 * @hibernate.property column="COLOR" length="50"
	 * 
	 */

	@Column(name = "COLOR", length = 50)
	private String color;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="MARKE_REF_ID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "MARKE_REF_ID", nullable = false)
	private BrandModel brand;

	public LibraryModel() {
	}

	public LibraryModel(String name, String color, BrandModel brand) {
		this.name = name;
		this.color = color;
		this.brand = brand;
	}

	/**
	 * * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getLibraryId() {
		return this.libraryId;
	}

	private void setLibraryId(Long libraryId) {
		this.libraryId = libraryId;
	}

	/**
	 * * @hibernate.property column="NAME" length="50"
	 * 
	 */
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * * @hibernate.property column="COLOR" length="50"
	 * 
	 */
	public String getColor() {
		return this.color;
	}

	public void setColor(String color) {
		this.color = color;
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
		buffer.append("libraryId").append("='").append(getLibraryId()).append("' ");
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
		if (!(other instanceof LibraryModel)) {
			return false;
		}
		LibraryModel castOther = (LibraryModel) other;

		return ((this.getLibraryId() == castOther.getLibraryId()) || (this.getLibraryId() != null
				&& castOther.getLibraryId() != null && this.getLibraryId().equals(castOther.getLibraryId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getLibraryId() == null ? 0 : this.getLibraryId().hashCode());

		return result;
	}

}
