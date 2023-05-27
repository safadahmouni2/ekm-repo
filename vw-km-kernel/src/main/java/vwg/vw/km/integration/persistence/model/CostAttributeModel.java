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

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_KOSTENATTRIBUT"
 * 
 */
@Entity
@Table(name = "T_KOSTENATTRIBUT")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "CostAttributeModel")
public class CostAttributeModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_cost_attribute")
	@SequenceGenerator(name = "seq_cost_attribute", sequenceName = "SEQ_COST_ATTRIBUTE", allocationSize = 1)
	private Long costAttributeId;

	@Column(name = "BEZEICHNUNG", length = 256)
	private String designation;

	@Column(name = "BESCHREIBUNG", length = 255)
	private String description;
	/**
	 * @hibernate.property column="EINHEIT" length="10" not-null="true"
	 * 
	 */
	@Column(name = "EINHEIT", length = 10, nullable = false)
	private String unit;

	@Column(name = "X_PREIS_PRO_EINHEIT", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean pricePerUnit = Boolean.TRUE;
	/**
	 * @hibernate.property column="ORDERNR" length="8"
	 * 
	 */
	@Column(name = "ORDER_NR", length = 8)
	private Integer orderNumber;

	@Column(name = "BEISTELLUNG_AG", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean dependFromProvision = Boolean.TRUE;
	/**
	 * @hibernate.property column="KA_VERRECHNUNG_REF_ID" length="19"
	 * 
	 */
	@Column(name = "KA_VERRECHNUNG_REF_ID", length = 19)
	private Long costAttributeTransferRefId;

	@Column(name = "KAUFTEIL_FAKTOR", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean dependOnFactor = Boolean.FALSE;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="PLANKOSTENKATEGORIEREF"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "PLAN_KOSTEN_KATEGORIE_REF")
	private CostAttributeCategoryModel costAttributeCategory;

	public CostAttributeModel() {
	}

	public CostAttributeModel(String unit, Boolean pricePerUnit, Boolean dependFromProvision, Boolean dependOnFactor) {
		this.unit = unit;
		this.pricePerUnit = pricePerUnit;
		this.dependFromProvision = dependFromProvision;
		this.dependOnFactor = dependOnFactor;
	}

	public CostAttributeModel(String designation, String description, String unit, Boolean pricePerUnit,
			Integer orderNumber, Boolean dependFromProvision, Long costAttributeTransferRefId, Boolean dependOnFactor,
			CostAttributeCategoryModel costAttributeCategory) {
		this.designation = designation;
		this.description = description;
		this.unit = unit;
		this.pricePerUnit = pricePerUnit;
		this.orderNumber = orderNumber;
		this.dependFromProvision = dependFromProvision;
		this.costAttributeTransferRefId = costAttributeTransferRefId;
		this.dependOnFactor = dependOnFactor;
		this.costAttributeCategory = costAttributeCategory;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getCostAttributeId() {
		return this.costAttributeId;
	}

	private void setCostAttributeId(Long costAttributeId) {
		this.costAttributeId = costAttributeId;
	}

	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * * @hibernate.property column="EINHEIT" length="10" not-null="true"
	 * 
	 */
	public String getUnit() {
		return this.unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getPricePerUnit() {
		return this.pricePerUnit;
	}

	public void setPricePerUnit(Boolean pricePerUnit) {
		this.pricePerUnit = pricePerUnit;
	}

	/**
	 * * @hibernate.property column="ORDERNR" length="8"
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
	 * * @hibernate.property column="KA_VERRECHNUNG_REF_ID" length="19"
	 * 
	 */
	public Long getCostAttributeTransferRefId() {
		return this.costAttributeTransferRefId;
	}

	public void setCostAttributeTransferRefId(Long costAttributeTransferRefId) {
		this.costAttributeTransferRefId = costAttributeTransferRefId;
	}

	public Boolean getDependOnFactor() {
		return this.dependOnFactor;
	}

	public void setDependOnFactor(Boolean dependOnFactor) {
		this.dependOnFactor = dependOnFactor;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="PLANKOSTENKATEGORIEREF"
	 * 
	 */
	public CostAttributeCategoryModel getCostAttributeCategory() {
		return this.costAttributeCategory;
	}

	public void setCostAttributeCategory(CostAttributeCategoryModel costAttributeCategory) {
		this.costAttributeCategory = costAttributeCategory;
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
		buffer.append("costAttributeId").append("='").append(getCostAttributeId()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
		buffer.append("orderNumber").append("='").append(getOrderNumber()).append("' ");
		buffer.append("costAttributeTransferRefId").append("='").append(getCostAttributeTransferRefId()).append("' ");
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
		if (!(other instanceof CostAttributeModel)) {
			return false;
		}
		CostAttributeModel castOther = (CostAttributeModel) other;

		return ((this.getCostAttributeId() == castOther.getCostAttributeId())
				|| (this.getCostAttributeId() != null && castOther.getCostAttributeId() != null
						&& this.getCostAttributeId().equals(castOther.getCostAttributeId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getCostAttributeId() == null ? 0 : this.getCostAttributeId().hashCode());

		return result;
	}

}
