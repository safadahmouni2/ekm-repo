package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_ELEMENTWERTE"
 * 
 */

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

@Entity
@Table(name = "T_ELEMENTWERTE")
public class ElementValueModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_element_value")
	@SequenceGenerator(name = "seq_element_value", sequenceName = "SEQ_ELEMENT_VALUE", allocationSize = 1)
	private Long elementValueId;
	/**
	 * @hibernate.property column="ANZAHL" length="8" not-null="true"
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

	@Column(name = "BESCHREIBUNG", length = 255, insertable = false, updatable = false)
	private String oldDescription;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ELEMVERREFID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "ELEM_VER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name="ELEM_VER_REF_ID", nullable= false)
	private ElementVersionModel elementVersion;

	@ManyToOne
	@JoinColumn(name = "KOSTEN_ATTRIBUT_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name="KOSTEN_ATTRIBUT_REF_ID", nullable= false)
	private CostAttributeModel costAttribute;

	public ElementValueModel() {
	}

	public ElementValueModel(BaseBigDecimal number, BaseBigDecimal oldNumber, BaseBigDecimal amount,
			BaseBigDecimal oldAmount, String description, String oldDescription, ElementVersionModel elementVersion,
			CostAttributeModel costAttribute) {
		this.number = number;
		this.oldNumber = oldNumber;
		this.amount = amount;
		this.oldAmount = oldAmount;
		this.description = description;
		this.oldDescription = oldDescription;
		this.elementVersion = elementVersion;
		this.costAttribute = costAttribute;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getElementValueId() {
		return this.elementValueId;
	}

	private void setElementValueId(Long elementValueId) {
		this.elementValueId = elementValueId;
	}

	/**
	 * * @hibernate.property column="ANZAHL" length="8" not-null="true"
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
	 * @hibernate.column name="ELEMVERREFID"
	 * 
	 */
	public ElementVersionModel getElementVersion() {
		return this.elementVersion;
	}

	public void setElementVersion(ElementVersionModel elementVersion) {
		this.elementVersion = elementVersion;
	}

	public CostAttributeModel getCostAttribute() {
		return this.costAttribute;
	}

	public void setCostAttribute(CostAttributeModel costAttribute) {
		this.costAttribute = costAttribute;
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
		buffer.append("elementValueId").append("='").append(getElementValueId()).append("' ");
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
		if (!(other instanceof ElementValueModel)) {
			return false;
		}
		ElementValueModel castOther = (ElementValueModel) other;

		return ((this.getElementValueId() == castOther.getElementValueId())
				|| (this.getElementValueId() != null && castOther.getElementValueId() != null
						&& this.getElementValueId().equals(castOther.getElementValueId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getElementValueId() == null ? 0 : this.getElementValueId().hashCode());

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

	public String getShortDescription() {
		String shortDescription = description;
		if (shortDescription != null && shortDescription.length() > 20) {
			shortDescription = shortDescription.substring(0, 20) + "...";
		}
		return shortDescription;
	}

	// end of extra code specified in the hbm.xml files

}
