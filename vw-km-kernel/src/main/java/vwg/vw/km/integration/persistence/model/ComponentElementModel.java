package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

/**
 * @hibernate.class table="T_REL_BAUSTEIN_ELEMENT"
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

@Entity
@Table(name = "T_REL_BAUSTEIN_ELEMENT")
public class ComponentElementModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component_element")
	@SequenceGenerator(name = "seq_component_element", sequenceName = "SEQ_COMPONENT_ELEMENT", allocationSize = 1)
	private Long componentElementId;
	/**
	 * @hibernate.property column="ANZAHL"
	 * 
	 */
	@Column(name = "ANZAHL")
	private Long number;
	/**
	 * @hibernate.property column="ANZEIGE_ORDER"
	 * 
	 */

	@Column(name = "ANZEIGE_ORDER", nullable = false)
	private Long displayOrder;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KEVERSIONREFUUID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KE_VERSION_REF_UUID")
	// TODO conflict with DB
	// @JoinColumn(name="KE_VERSION_REF_UUID", nullable= false)
	private ElementVersionModel elementVersion;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KBVERSIONREFUUID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KB_STAND_REF_UUID")
	// TODO conflict with DB
	// @JoinColumn(name="KB_STAND_REF_UUID", nullable= false)
	private ComponentStandModel componentStand;

	public ComponentElementModel() {
	}

	public ComponentElementModel(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public ComponentElementModel(Long number, Long displayOrder, ElementVersionModel elementVersion,
			ComponentStandModel componentStand) {
		this.number = number;
		this.displayOrder = displayOrder;
		this.elementVersion = elementVersion;
		this.componentStand = componentStand;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getComponentElementId() {
		return this.componentElementId;
	}

	private void setComponentElementId(Long componentElementId) {
		this.componentElementId = componentElementId;
	}

	/**
	 * * @hibernate.property column="ANZAHL"
	 * 
	 */
	public Long getNumber() {
		return this.number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * * @hibernate.property column="ANZEIGE_ORDER"
	 * 
	 */
	public Long getDisplayOrder() {
		return this.displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KEVERSIONREFUUID"
	 * 
	 */
	public ElementVersionModel getElementVersion() {
		return this.elementVersion;
	}

	public void setElementVersion(ElementVersionModel elementVersion) {
		this.elementVersion = elementVersion;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KBVERSIONREFUUID"
	 * 
	 */
	public ComponentStandModel getComponentStand() {
		return this.componentStand;
	}

	public void setComponentStand(ComponentStandModel componentStand) {
		this.componentStand = componentStand;
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
		buffer.append("componentElementId").append("='").append(getComponentElementId()).append("' ");
		buffer.append("number").append("='").append(getNumber()).append("' ");
		buffer.append("displayOrder").append("='").append(getDisplayOrder()).append("' ");
		buffer.append("elementVersion").append("='").append(getElementVersion()).append("' ");
		buffer.append("componentStand").append("='").append(getComponentStand()).append("' ");
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
		if (!(other instanceof ComponentElementModel)) {
			return false;
		}
		ComponentElementModel castOther = (ComponentElementModel) other;

		return ((this.getElementVersion() == castOther.getElementVersion())
				|| (this.getElementVersion() != null && castOther.getElementVersion() != null
						&& this.getElementVersion().equals(castOther.getElementVersion())))
				&& ((this.getComponentStand() == castOther.getComponentStand())
						|| (this.getComponentStand() != null && castOther.getComponentStand() != null
								&& this.getComponentStand().equals(castOther.getComponentStand())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getElementVersion() == null ? 0 : this.getElementVersion().hashCode());
		result = 37 * result + (getComponentStand() == null ? 0 : this.getComponentStand().hashCode());
		return result;
	}

	// The following is extra code specified in the hbm.xml files

	@Transient
	private boolean selected;

	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Transient
	private vwg.vw.km.common.type.BaseBigDecimal totalAmount;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(vwg.vw.km.common.type.BaseBigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	// end of extra code specified in the hbm.xml files

}
