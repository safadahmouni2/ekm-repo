package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vwg.vw.km.common.type.BaseBigDecimal;

/**
 * @hibernate.class table="T_STUNDENSAETZE"
 * 
 */
@Entity
@Table(name = "T_STUNDENSAETZE")
public class HourlyRateModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_hourly_rate")
	@SequenceGenerator(name = "seq_hourly_rate", sequenceName = "SEQ_HOURLY_RATE", allocationSize = 1)
	private Long hourlyRateId;
	/**
	 * @hibernate.property column="WERT" length="10"
	 * 
	 */
	@Column(name = "WERT", length = 10)
	private BaseBigDecimal value;
	/**
	 * @hibernate.property column="FAKTOR" length="10"
	 * 
	 */
	@Column(name = "FAKTOR", length = 10)
	private BaseBigDecimal factor;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KOSTENATTRIBUTREFID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "KOSTEN_ATTRIBUT_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "KOSTEN_ATTRIBUT_REF_ID", nullable = false)
	private CostAttributeModel costAttribute;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="STDSATZKATREFID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "STD_SATZ_KAT_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "STD_SATZ_KAT_REF_ID", nullable = false)
	private HourlyRateCatalogModel hourlyRateCatalog;

	public HourlyRateModel() {
	}

	public HourlyRateModel(BaseBigDecimal value, BaseBigDecimal factor, CostAttributeModel costAttribute,
			HourlyRateCatalogModel hourlyRateCatalog) {
		this.value = value;
		this.factor = factor;
		this.costAttribute = costAttribute;
		this.hourlyRateCatalog = hourlyRateCatalog;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getHourlyRateId() {
		return this.hourlyRateId;
	}

	private void setHourlyRateId(Long hourlyRateId) {
		this.hourlyRateId = hourlyRateId;
	}

	/**
	 * * @hibernate.property column="WERT" length="10"
	 * 
	 */
	public BaseBigDecimal getValue() {
		return this.value;
	}

	public void setValue(BaseBigDecimal value) {
		this.value = value;
	}

	/**
	 * * @hibernate.property column="FAKTOR" length="10"
	 * 
	 */
	public BaseBigDecimal getFactor() {
		return this.factor;
	}

	public void setFactor(BaseBigDecimal factor) {
		this.factor = factor;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KOSTENATTRIBUTREFID"
	 * 
	 */
	public CostAttributeModel getCostAttribute() {
		return this.costAttribute;
	}

	public void setCostAttribute(CostAttributeModel costAttribute) {
		this.costAttribute = costAttribute;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="STDSATZKATREFID"
	 * 
	 */
	public HourlyRateCatalogModel getHourlyRateCatalog() {
		return this.hourlyRateCatalog;
	}

	public void setHourlyRateCatalog(HourlyRateCatalogModel hourlyRateCatalog) {
		this.hourlyRateCatalog = hourlyRateCatalog;
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
		buffer.append("hourlyRateId").append("='").append(getHourlyRateId()).append("' ");
		buffer.append("value").append("='").append(getValue()).append("' ");
		buffer.append("factor").append("='").append(getFactor()).append("' ");
		buffer.append("costAttribute").append("='").append(getCostAttribute()).append("' ");
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
		if (!(other instanceof HourlyRateModel)) {
			return false;
		}
		HourlyRateModel castOther = (HourlyRateModel) other;

		return ((this.getHourlyRateId() == castOther.getHourlyRateId()) || (this.getHourlyRateId() != null
				&& castOther.getHourlyRateId() != null && this.getHourlyRateId().equals(castOther.getHourlyRateId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getHourlyRateId() == null ? 0 : this.getHourlyRateId().hashCode());

		return result;
	}

}
