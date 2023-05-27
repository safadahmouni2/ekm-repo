package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @hibernate.class table="T_NEWS"
 * 
 */
@Entity
@Table(name = "T_NEWS")
public class NewsModel extends vwg.vw.km.integration.persistence.model.base.BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID" not-null="true"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_NEWS")
	@SequenceGenerator(name = "SEQ_NEWS", sequenceName = "SEQ_NEWS", allocationSize = 1)
	private Long newsId;
	/**
	 * @hibernate.property column="UEBERSCHRIFT" length="50"
	 * 
	 */

	@Column(name = "UEBERSCHRIFT", length = 50, nullable = false)
	private String title;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="3000"
	 * 
	 */

	@Column(name = "BESCHREIBUNG", length = 3000)
	private String description;
	/**
	 * @hibernate.property column="GUELTIG_AB" length="10"
	 * 
	 */
	@Column(name = "GUELTIG_AB", length = 10)
	private Date validFrom;
	/**
	 * @hibernate.property column="GUELTIG_BIS" length="10"
	 * 
	 */
	@Column(name = "GUELTIG_BIS", length = 10)
	private Date dateOfExpiry;
	/**
	 * @hibernate.property column="AKTIV" length="1"
	 * 
	 */
	@Column(name = "AKTIV", length = 1)
	private Boolean active;

	public NewsModel() {
	}

	public NewsModel(String title) {
		this.title = title;
	}

	public NewsModel(String title, String description, Date validFrom, Date dateOfExpiry, Boolean active) {
		this.title = title;
		this.description = description;
		this.validFrom = validFrom;
		this.dateOfExpiry = dateOfExpiry;
		this.active = active;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID" not-null="true"
	 * 
	 */
	public Long getNewsId() {
		return this.newsId;
	}

	private void setNewsId(Long newsId) {
		this.newsId = newsId;
	}

	/**
	 * * @hibernate.property column="UEBERSCHRIFT" length="50"
	 * 
	 */
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * * @hibernate.property column="BESCHREIBUNG" length="3000"
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * * @hibernate.property column="GUELTIG_AB" length="10"
	 * 
	 */
	public Date getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * * @hibernate.property column="GUELTIG_BIS" length="10"
	 * 
	 */
	public Date getDateOfExpiry() {
		return this.dateOfExpiry;
	}

	public void setDateOfExpiry(Date dateOfExpiry) {
		this.dateOfExpiry = dateOfExpiry;
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
	 * toString
	 * 
	 * @return String
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
		buffer.append("newsId").append("='").append(getNewsId()).append("' ");
		buffer.append("title").append("='").append(getTitle()).append("' ");
		buffer.append("description").append("='").append(getDescription()).append("' ");
		buffer.append("validFrom").append("='").append(getValidFrom()).append("' ");
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
		if (!(other instanceof NewsModel)) {
			return false;
		}
		NewsModel castOther = (NewsModel) other;

		return ((this.getNewsId() == castOther.getNewsId()) || (this.getNewsId() != null
				&& castOther.getNewsId() != null && this.getNewsId().equals(castOther.getNewsId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getNewsId() == null ? 0 : this.getNewsId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	// end of extra code specified in the hbm.xml files

}
