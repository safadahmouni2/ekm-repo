package vwg.vw.km.integration.persistence.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_ENUM_INVESTANTEIL"
 * 
 */

@Entity
@Table(name = "T_ENUM_INVESTANTEIL")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "EnumInvestmentPartModel")
public class EnumInvestmentPartModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_investment_part")
	@SequenceGenerator(name = "seq_investment_part", sequenceName = "SEQ_INVESTMENT_PART", allocationSize = 1)
	private Long enumInvestmentPartId;
	/**
	 * @hibernate.property column="INVESTITIONS_ANTEIL" length="50" not-null="true"
	 * 
	 */

	@Column(name = "INVESTITIONS_ANTEIL", length = 50, nullable = false)
	private String investmentPart;
	/**
	 * @hibernate.property column="KATEGORIE" length="50" not-null="true"
	 * 
	 */

	@Column(name = "KATEGORIE", length = 50, nullable = false)
	private String category;
	/**
	 * @hibernate.property column="ORDERNR" length="8" not-null="true"
	 * 
	 */

	@Column(name = "ORDER_NR", length = 8)
	private Integer orderNumber;

	public EnumInvestmentPartModel() {
	}

	public EnumInvestmentPartModel(String investmentPart, String category) {
		this.investmentPart = investmentPart;
		this.category = category;
	}

	public EnumInvestmentPartModel(String investmentPart, String category, Integer orderNumber) {
		this.investmentPart = investmentPart;
		this.category = category;
		this.orderNumber = orderNumber;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getEnumInvestmentPartId() {
		return this.enumInvestmentPartId;
	}

	private void setEnumInvestmentPartId(Long enumInvestmentPartId) {
		this.enumInvestmentPartId = enumInvestmentPartId;
	}

	/**
	 * * @hibernate.property column="INVESTITIONS_ANTEIL" length="50" not-null="true"
	 * 
	 */
	public String getInvestmentPart() {
		return this.investmentPart;
	}

	public void setInvestmentPart(String investmentPart) {
		this.investmentPart = investmentPart;
	}

	/**
	 * * @hibernate.property column="KATEGORIE" length="50" not-null="true"
	 * 
	 */
	public String getCategory() {
		return this.category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * * @hibernate.property column="ORDERNR" length="8" not-null="true"
	 * 
	 */
	public Integer getOrderNumber() {
		return this.orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
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
		buffer.append("enumInvestmentPartId").append("='").append(getEnumInvestmentPartId()).append("' ");
		buffer.append("investmentPart").append("='").append(getInvestmentPart()).append("' ");
		buffer.append("category").append("='").append(getCategory()).append("' ");
		buffer.append("orderNumber").append("='").append(getOrderNumber()).append("' ");
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
		if (!(other instanceof EnumInvestmentPartModel)) {
			return false;
		}
		EnumInvestmentPartModel castOther = (EnumInvestmentPartModel) other;

		return ((this.getEnumInvestmentPartId() == castOther.getEnumInvestmentPartId())
				|| (this.getEnumInvestmentPartId() != null && castOther.getEnumInvestmentPartId() != null
						&& this.getEnumInvestmentPartId().equals(castOther.getEnumInvestmentPartId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getEnumInvestmentPartId() == null ? 0 : this.getEnumInvestmentPartId().hashCode());

		return result;
	}

}
