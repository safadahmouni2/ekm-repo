package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * @hibernate.class table="T_ROLLEN"
 * 
 */
@Entity
@Table(name = "T_ROLLEN")
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "RoleModel")
public class RoleModel extends vwg.vw.km.integration.persistence.model.base.BaseModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
	@SequenceGenerator(name = "seq_role", sequenceName = "SEQ_ROLE", allocationSize = 1)
	private Long roleId;
	/**
	 * @hibernate.property column="BEZEICHNUNG" length="50" not-null="true"
	 * 
	 */
	@Column(name = "BEZEICHNUNG", length = 50, nullable = false)
	private String designation;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="255"
	 * 
	 */
	@Column(name = "BESCHREIBUNG", length = 255)
	private String description;

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "query.role")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_REL_ROLLEN_RECHT", joinColumns = {
			@JoinColumn(name = "ROLLEN_ID_REF") }, inverseJoinColumns = { @JoinColumn(name = "RECHT_ID_REF") })
	@Fetch(FetchMode.SUBSELECT)
	private Set<RightModel> rights = new HashSet<RightModel>(0);

	@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "query.role")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "T_REL_ROLLEN_ROLLEN", joinColumns = {
			@JoinColumn(name = "ROLLEN_ID_REF") }, inverseJoinColumns = { @JoinColumn(name = "MANAGED_ROLLEN_ID_REF") })
	@Fetch(FetchMode.SUBSELECT)
	private Set<RoleModel> managedRoles = new HashSet<RoleModel>(0);

	public RoleModel() {
	}

	public RoleModel(String designation) {
		this.designation = designation;
	}

	public RoleModel(String designation, String description, Set<RightModel> rights, Set<RoleModel> managedRoles) {
		this.designation = designation;
		this.description = description;
		this.rights = rights;
		this.managedRoles = managedRoles;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getRoleId() {
		return this.roleId;
	}

	private void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/**
	 * * @hibernate.property column="BEZEICHNUNG" length="50" not-null="true"
	 * 
	 */
	public String getDesignation() {
		return this.designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	/**
	 * * @hibernate.property column="BESCHREIBUNG" length="255"
	 * 
	 */
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<RightModel> getRights() {
		return this.rights;
	}

	public void setRights(Set<RightModel> rights) {
		this.rights = rights;
	}

	public Set<RoleModel> getManagedRoles() {
		return this.managedRoles;
	}

	public void setManagedRoles(Set<RoleModel> managedRoles) {
		this.managedRoles = managedRoles;
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
		buffer.append("roleId").append("='").append(getRoleId()).append("' ");
		buffer.append("designation").append("='").append(getDesignation()).append("' ");
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
		if (!(other instanceof RoleModel)) {
			return false;
		}
		RoleModel castOther = (RoleModel) other;

		return ((this.getRoleId() == castOther.getRoleId()) || (this.getRoleId() != null
				&& castOther.getRoleId() != null && this.getRoleId().equals(castOther.getRoleId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getRoleId() == null ? 0 : this.getRoleId().hashCode());

		return result;
	}

}
