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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_BAUSTEINSTAND"
 * 
 */

@Entity
@Table(name = "T_BAUSTEINSTAND")
public class ComponentStandModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_BAUSTEINSTAND")
	@SequenceGenerator(name = "SEQ_BAUSTEINSTAND", sequenceName = "SEQ_BAUSTEINSTAND", allocationSize = 1)
	private Long id;
	/**
	 * @hibernate.property column="STAND_DATE" length="8"
	 * 
	 */

	@Column(name = "STAND_DATE", columnDefinition = "DATE")
	private BaseDateTime standDate;
	/**
	 * @hibernate.property column="STAND_NUMMER" length="9"
	 * 
	 */

	@Column(name = "STAND_NUMMER", length = 9)
	private Long number;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KB_VERSION_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KB_VERSION_REF_ID")
	private ComponentVersionModel componentVersion;
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="KB_STAND_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ComponentVersionUsersModel"
	 * 
	 */

	@OneToMany(mappedBy = "componentStand", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentStandUsersModel> componentVersionUsers = new HashSet<ComponentStandUsersModel>(0);

	@OneToMany(mappedBy = "componentStand", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentElementModel> componentElements = new HashSet<ComponentElementModel>(0);

	@OneToMany(mappedBy = "componentStand", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentStandChangeModel> componentVersionChanges = new HashSet<ComponentStandChangeModel>(0);

	public ComponentStandModel() {
	}

	public ComponentStandModel(BaseDateTime standDate, Long number, ComponentVersionModel componentVersion,
			Set<ComponentStandUsersModel> componentVersionUsers, Set<ComponentElementModel> componentElements,
			Set<ComponentStandChangeModel> componentVersionChanges) {
		this.standDate = standDate;
		this.number = number;
		this.componentVersion = componentVersion;
		this.componentVersionUsers = componentVersionUsers;
		this.componentElements = componentElements;
		this.componentVersionChanges = componentVersionChanges;
	}

	/**
	 * * @hibernate.id generator-class="sequence" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getId() {
		return this.id;
	}

	private void setId(Long id) {
		this.id = id;
	}

	/**
	 * * @hibernate.property column="STAND_DATE" length="8"
	 * 
	 */
	public BaseDateTime getStandDate() {
		return this.standDate;
	}

	public void setStandDate(BaseDateTime standDate) {
		this.standDate = standDate;
	}

	/**
	 * * @hibernate.property column="STAND_NUMMER" length="9"
	 * 
	 */
	public Long getNumber() {
		return this.number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KB_VERSION_REF_ID"
	 * 
	 */
	public ComponentVersionModel getComponentVersion() {
		return this.componentVersion;
	}

	public void setComponentVersion(ComponentVersionModel componentVersion) {
		this.componentVersion = componentVersion;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="KB_STAND_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ComponentVersionUsersModel"
	 * 
	 */
	public Set<ComponentStandUsersModel> getComponentVersionUsers() {
		return this.componentVersionUsers;
	}

	public void setComponentVersionUsers(Set<ComponentStandUsersModel> componentVersionUsers) {
		this.componentVersionUsers = componentVersionUsers;
	}

	public Set<ComponentElementModel> getComponentElements() {
		return this.componentElements;
	}

	public void setComponentElements(Set<ComponentElementModel> componentElements) {
		this.componentElements = componentElements;
	}

	public Set<ComponentStandChangeModel> getComponentVersionChanges() {
		return this.componentVersionChanges;
	}

	public void setComponentVersionChanges(Set<ComponentStandChangeModel> componentVersionChanges) {
		this.componentVersionChanges = componentVersionChanges;
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
		buffer.append("id").append("='").append(getId()).append("' ");
		buffer.append("standDate").append("='").append(getStandDate()).append("' ");
		buffer.append("number").append("='").append(getNumber()).append("' ");
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
		if (!(other instanceof ComponentStandModel)) {
			return false;
		}
		ComponentStandModel castOther = (ComponentStandModel) other;

		return ((this.getId() == castOther.getId())
				|| (this.getId() != null && castOther.getId() != null && this.getId().equals(castOther.getId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getId() == null ? 0 : this.getId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public java.util.List<ComponentStandUsersModel> getUsers() {
		if (getComponentVersionUsers() != null) {
			java.util.List<ComponentStandUsersModel> componentStandUsers = new java.util.ArrayList<ComponentStandUsersModel>(
					getComponentVersionUsers());
			if (componentStandUsers != null) {
				Set<ComponentStandUsersModel> users = new HashSet<ComponentStandUsersModel>();
				for (ComponentStandUsersModel componentStandUser : componentStandUsers) {
					users.add(componentStandUser);
				}
				return new java.util.ArrayList<ComponentStandUsersModel>(users);
			}
		}
		return null;
	}

	public java.util.List<String> getUsersImg() {
		java.util.List<String> usersImg = new java.util.ArrayList<String>();
		java.util.List<ComponentStandUsersModel> usersObject = getUsers();
		if (usersObject != null) {
			for (ComponentStandUsersModel standUser : usersObject) {
				if (standUser.getActive() != null && standUser.getActive()) {
					usersImg.add(standUser.getBrand().getUseImg());
				} else {
					usersImg.add(standUser.getBrand().getUseInactiveImg());
				}

			}
			return usersImg;
		}
		return null;
	}

	// end of extra code specified in the hbm.xml files

}
