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
 * @hibernate.class table="T_RECHT"
 * 
 */

@Entity
@Table(name = "T_RECHT")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "RightModel")
public class RightModel extends vwg.vw.km.integration.persistence.model.base.BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="RECHT_ID"
	 * 
	 */

	@Id
	@Column(name = "RECHT_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_right")
	@SequenceGenerator(name = "seq_right", sequenceName = "SEQ_RIGHT", allocationSize = 1)
	private Long rightId;

	@Column(name = "KODE", length = 30, nullable = false)
	private String rightCode;

	public RightModel() {
	}

	public RightModel(String rightCode) {
		this.rightCode = rightCode;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="RECHT_ID"
	 * 
	 */
	public Long getRightId() {
		return this.rightId;
	}

	private void setRightId(Long rightId) {
		this.rightId = rightId;
	}

	public String getRightCode() {
		return this.rightCode;
	}

	public void setRightCode(String rightCode) {
		this.rightCode = rightCode;
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
		buffer.append("rightCode").append("='").append(getRightCode()).append("' ");
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
		if (!(other instanceof RightModel)) {
			return false;
		}
		RightModel castOther = (RightModel) other;

		return ((this.getRightId() == castOther.getRightId()) || (this.getRightId() != null
				&& castOther.getRightId() != null && this.getRightId().equals(castOther.getRightId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getRightId() == null ? 0 : this.getRightId().hashCode());

		return result;
	}

}
