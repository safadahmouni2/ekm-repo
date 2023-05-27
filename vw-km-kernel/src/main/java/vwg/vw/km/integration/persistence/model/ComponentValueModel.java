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

import vwg.vw.km.common.type.BaseBigDecimal;

/**
 * @hibernate.class table="T_BAUSTEINKOSTEN"
 * 
 */

@Entity
@Table(name = "T_BAUSTEINKOSTEN")
public class ComponentValueModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component_cost")
	@SequenceGenerator(name = "seq_component_cost", sequenceName = "SEQ_COMPONENT_COST", allocationSize = 1)
	private Long componentValueId;
	/**
	 * @hibernate.property column="ANZAHL" length="8"
	 * 
	 */

	@Column(name = "ANZAHL", length = 8)
	private BaseBigDecimal number;

	@Column(name = "ANZAHL", length = 8, insertable = false, updatable = false)
	private BaseBigDecimal oldNumber;
	/**
	 * @hibernate.property column="BETRAG" length="8"
	 * 
	 */

	@Column(name = "BETRAG", length = 8)
	private BaseBigDecimal amount;

	@Column(name = "BETRAG", length = 8, insertable = false, updatable = false)
	private BaseBigDecimal oldAmount;

	@Column(name = "BESCHREIBUNG", length = 255)
	private String description;

	@Column(name = "BESCHREIBUNG", length = 25, insertable = false, updatable = false)
	private String oldDescription;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KOSTENATTRIBUTREFID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KOSTEN_ATTRIBUT_REF_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "KOSTEN_ATTRIBUT_REF_ID", nullable = false)
	private CostAttributeModel costAttribute;

	@ManyToOne
	@JoinColumn(name = "KB_REF_UU_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "KB_REF_UU_ID", nullable = false)
	private ComponentVersionModel componentVersion;

	public ComponentValueModel() {
	}

	public ComponentValueModel(BaseBigDecimal number, BaseBigDecimal oldNumber, BaseBigDecimal amount,
			BaseBigDecimal oldAmount, String description, String oldDescription, CostAttributeModel costAttribute,
			ComponentVersionModel componentVersion) {
		this.number = number;
		this.oldNumber = oldNumber;
		this.amount = amount;
		this.oldAmount = oldAmount;
		this.description = description;
		this.oldDescription = oldDescription;
		this.costAttribute = costAttribute;
		this.componentVersion = componentVersion;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getComponentValueId() {
		return this.componentValueId;
	}

	private void setComponentValueId(Long componentValueId) {
		this.componentValueId = componentValueId;
	}

	/**
	 * * @hibernate.property column="ANZAHL" length="8"
	 * 
	 */
	public BaseBigDecimal getNumber() {
		return this.number;
	}

	public void setNumber(BaseBigDecimal number) {
		this.number = number;
	}

	public BaseBigDecimal getOldNumber() {
		return this.oldNumber;
	}

	public void setOldNumber(BaseBigDecimal oldNumber) {
		this.oldNumber = oldNumber;
	}

	/**
	 * * @hibernate.property column="BETRAG" length="8"
	 * 
	 */
	public BaseBigDecimal getAmount() {
		return this.amount;
	}

	public void setAmount(BaseBigDecimal amount) {
		this.amount = amount;
	}

	public BaseBigDecimal getOldAmount() {
		return this.oldAmount;
	}

	public void setOldAmount(BaseBigDecimal oldAmount) {
		this.oldAmount = oldAmount;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOldDescription() {
		return this.oldDescription;
	}

	public void setOldDescription(String oldDescription) {
		this.oldDescription = oldDescription;
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

	public ComponentVersionModel getComponentVersion() {
		return this.componentVersion;
	}

	public void setComponentVersion(ComponentVersionModel componentVersion) {
		this.componentVersion = componentVersion;
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
		buffer.append("componentValueId").append("='").append(getComponentValueId()).append("' ");
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
		if (!(other instanceof ComponentValueModel)) {
			return false;
		}
		ComponentValueModel castOther = (ComponentValueModel) other;

		return ((this.getComponentValueId() == castOther.getComponentValueId())
				|| (this.getComponentValueId() != null && castOther.getComponentValueId() != null
						&& this.getComponentValueId().equals(castOther.getComponentValueId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getComponentValueId() == null ? 0 : this.getComponentValueId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	@Transient
	private HourlyRateModel rateModel;

	public HourlyRateModel getRateModel() {
		return rateModel;
	}

	public void setRateModel(HourlyRateModel rateModel) {
		this.rateModel = rateModel;
	}

	@Transient
	private vwg.vw.km.common.type.BaseBigDecimal totalAmount;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(vwg.vw.km.common.type.BaseBigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	@Transient
	private vwg.vw.km.common.type.BaseBigDecimal totalSumAmount;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalSumAmount() {
		return totalSumAmount;
	}

	public void setTotalSumAmount(vwg.vw.km.common.type.BaseBigDecimal totalSumAmount) {
		this.totalSumAmount = totalSumAmount;
	}

	@Transient
	private vwg.vw.km.common.type.BaseBigDecimal totalElemAmount;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalElemAmount() {
		return totalElemAmount;
	}

	public void setTotalElemAmount(vwg.vw.km.common.type.BaseBigDecimal totalElemAmount) {
		this.totalElemAmount = totalElemAmount;
	}

	public String getShortDescription() {
		String shortDescription = description;
		if (shortDescription != null && shortDescription.length() > 25) {
			shortDescription = shortDescription.substring(0, 25) + "...";
		}
		return shortDescription;
	}

	// end of extra code specified in the hbm.xml files

}
