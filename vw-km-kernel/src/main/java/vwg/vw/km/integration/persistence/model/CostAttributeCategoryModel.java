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
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import vwg.vw.km.common.type.BaseBigDecimal;

/**
 * @hibernate.class table="T_PLANKOSTENKATEGORIE"
 * 
 */
@Entity
@Table(name = "T_PLANKOSTENKATEGORIE")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "CostAttributeCategoryModel")
public class CostAttributeCategoryModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cost_plan_category")
	@SequenceGenerator(name = "seq_cost_plan_category", sequenceName = "SEQ_COST_PLAN_CATEGORY", allocationSize = 1)
	private Long costAttributeCategoryId;
	/**
	 * @hibernate.property column="PLANKOSTENKATEGORIE" unique="true" length="255" not-null="true"
	 * 
	 */
	@Column(name = "PLAN_KOSTEN_KATEGORIE", length = 255, nullable = false, unique = true)
	private String category;
	/**
	 * @hibernate.property column="ORDERNR" length="8" not-null="true"
	 * 
	 */
	@Column(name = "ORDER_NR", length = 8)
	private Integer orderNumber;

	@Column(name = "BEISTELLUNG_AG", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean dependFromProvision = Boolean.TRUE;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="T_ENUM_INVESTANTEIL_REF"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "T_ENUM_INVESTANTEIL_REF")
	// TODO conflict with DB
	// @JoinColumn(name = "T_ENUM_INVESTANTEIL_REF", nullable = false)
	private EnumInvestmentPartModel enumInvestmentPart;

	public CostAttributeCategoryModel() {
	}

	public CostAttributeCategoryModel(String category, Boolean dependFromProvision) {
		this.category = category;
		this.dependFromProvision = dependFromProvision;
	}

	public CostAttributeCategoryModel(String category, Integer orderNumber, Boolean dependFromProvision,
			EnumInvestmentPartModel enumInvestmentPart) {
		this.category = category;
		this.orderNumber = orderNumber;
		this.dependFromProvision = dependFromProvision;
		this.enumInvestmentPart = enumInvestmentPart;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getCostAttributeCategoryId() {
		return this.costAttributeCategoryId;
	}

	private void setCostAttributeCategoryId(Long costAttributeCategoryId) {
		this.costAttributeCategoryId = costAttributeCategoryId;
	}

	/**
	 * * @hibernate.property column="PLANKOSTENKATEGORIE" unique="true" length="255" not-null="true"
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

	public Boolean getDependFromProvision() {
		return this.dependFromProvision;
	}

	public void setDependFromProvision(Boolean dependFromProvision) {
		this.dependFromProvision = dependFromProvision;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="T_ENUM_INVESTANTEIL_REF"
	 * 
	 */
	public EnumInvestmentPartModel getEnumInvestmentPart() {
		return this.enumInvestmentPart;
	}

	public void setEnumInvestmentPart(EnumInvestmentPartModel enumInvestmentPart) {
		this.enumInvestmentPart = enumInvestmentPart;
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
		buffer.append("costAttributeCategoryId").append("='").append(getCostAttributeCategoryId()).append("' ");
		buffer.append("category").append("='").append(getCategory()).append("' ");
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
		if (!(other instanceof CostAttributeCategoryModel)) {
			return false;
		}
		CostAttributeCategoryModel castOther = (CostAttributeCategoryModel) other;

		return ((this.getCostAttributeCategoryId() == castOther.getCostAttributeCategoryId())
				|| (this.getCostAttributeCategoryId() != null && castOther.getCostAttributeCategoryId() != null
						&& this.getCostAttributeCategoryId().equals(castOther.getCostAttributeCategoryId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getCostAttributeCategoryId() == null ? 0 : this.getCostAttributeCategoryId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	@Transient
	private BaseBigDecimal amount;

	public BaseBigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BaseBigDecimal amount) {
		this.amount = amount;
	}

	@Transient
	private BaseBigDecimal provisionAmount;

	public BaseBigDecimal getProvisionAmount() {
		return provisionAmount;
	}

	public void setProvisionAmount(BaseBigDecimal provisionAmount) {
		this.provisionAmount = provisionAmount;
	}

	// end of extra code specified in the hbm.xml files

}
