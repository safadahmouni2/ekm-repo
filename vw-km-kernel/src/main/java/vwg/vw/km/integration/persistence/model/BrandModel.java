package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @hibernate.class table="T_MARKE"
 * 
 */
@Entity
@Table(name = "T_MARKE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class BrandModel extends vwg.vw.km.integration.persistence.model.base.BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	private String brandId;
	/**
	 * @hibernate.property column="NUTZUNG_IMG" length="50"
	 * 
	 */

	@Column(name = "NUTZUNG_IMG", length = 50)
	private String useImg;
	/**
	 * @hibernate.property column="NUTZUNG_IMG" length="50"
	 * 
	 */
	@Column(name = "NUTZUNG_INAKTIV_IMG", length = 50)
	private String useInactiveImg;
	/**
	 * @hibernate.property column="BESITZUNG_IMG" length="50"
	 * 
	 */

	@Column(name = "BESITZUNG_IMG", length = 50)
	private String ownImg;
	/**
	 * @hibernate.set lazy="false" inverse="true" cascade="none"
	 * @hibernate.collection-key column="MARKE_REF_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.LibraryModel"
	 * 
	 */

	@OneToMany(mappedBy = "brand", fetch = FetchType.EAGER)
	@Fetch(FetchMode.SUBSELECT)
	private Set<LibraryModel> libraries = new HashSet<LibraryModel>(0);

	public BrandModel() {
	}

	public BrandModel(String brandId) {
		this.brandId = brandId;
	}

	public BrandModel(String brandId, String useImg, String useInactiveImg, String ownImg,
			Set<LibraryModel> libraries) {
		this.brandId = brandId;
		this.useImg = useImg;
		this.useInactiveImg = useInactiveImg;
		this.ownImg = ownImg;
		this.libraries = libraries;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.String" column="ID"
	 * 
	 */
	public String getBrandId() {
		return this.brandId;
	}

	private void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	/**
	 * * @hibernate.property column="NUTZUNG_IMG" length="50"
	 * 
	 */
	public String getUseImg() {
		return this.useImg;
	}

	public void setUseImg(String useImg) {
		this.useImg = useImg;
	}

	/**
	 * * @hibernate.property column="NUTZUNG_IMG" length="50"
	 * 
	 */
	public String getUseInactiveImg() {
		return this.useInactiveImg;
	}

	public void setUseInactiveImg(String useInactiveImg) {
		this.useInactiveImg = useInactiveImg;
	}

	/**
	 * * @hibernate.property column="BESITZUNG_IMG" length="50"
	 * 
	 */
	public String getOwnImg() {
		return this.ownImg;
	}

	public void setOwnImg(String ownImg) {
		this.ownImg = ownImg;
	}

	/**
	 * * @hibernate.set lazy="false" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="MARKE_REF_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.LibraryModel"
	 * 
	 */
	public Set<LibraryModel> getLibraries() {
		return this.libraries;
	}

	public void setLibraries(Set<LibraryModel> libraries) {
		this.libraries = libraries;
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
		buffer.append("brandId").append("='").append(getBrandId()).append("' ");
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
		if (!(other instanceof BrandModel)) {
			return false;
		}
		BrandModel castOther = (BrandModel) other;

		return ((this.getBrandId() == castOther.getBrandId()) || (this.getBrandId() != null
				&& castOther.getBrandId() != null && this.getBrandId().equals(castOther.getBrandId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getBrandId() == null ? 0 : this.getBrandId().hashCode());

		return result;
	}

}
