package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.util.HashSet;
import java.util.List;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_ELEMENTVERSION"
 * 
 */
@Entity
@Table(name = "T_ELEMENTVERSION")
public class ElementVersionModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKEVERSION"
	 * 
	 */
	@Id
	@Column(name = "UUIDKEVERSION")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_element_version")
	@SequenceGenerator(name = "seq_element_version", sequenceName = "SEQ_ELEMENT_VERSION", allocationSize = 1)
	private Long elementVersionId;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="1000"
	 * 
	 */
	@Column(name = "BESCHREIBUNG", length = 1000)
	private String description;
	@Column(name = "BESCHREIBUNG", length = 1000, insertable = false, updatable = false)
	private String oldDescription;
	/**
	 * @hibernate.property column="MECHANIKKONSTRUKTION" length="4000"
	 * 
	 */
	@Column(name = "MECHANIK_KONSTRUKTION", length = 4000)
	private String mechanicalConstruction;

	@Column(name = "MECHANIK_KONSTRUKTION", length = 4000, insertable = false, updatable = false)
	private String oldMechanicalConstruction;
	/**
	 * @hibernate.property column="MECHANIKAUSFUEHRUNG" length="4000"
	 * 
	 */
	@Column(name = "MECHANIK_AUSFUEHRUNG", length = 4000)
	private String mechanicalExecution;

	@Column(name = "MECHANIK_AUSFUEHRUNG", length = 4000, insertable = false, updatable = false)
	private String oldMechanicalExecution;
	/**
	 * @hibernate.property column="ELEKTRIK" length="4000"
	 * 
	 */
	@Column(name = "ELEKTRIK", length = 4000)
	private String electric;

	@Column(name = "ELEKTRIK", length = 4000, insertable = false, updatable = false)
	private String oldElectric;
	/**
	 * @hibernate.property column="BEMERKUNGEN" length="4000"
	 * 
	 */
	@Column(name = "BEMERKUNGEN", length = 4000)
	private String comment;

	@Column(name = "BEMERKUNGEN", length = 4000, insertable = false, updatable = false)
	private String oldComment;
	/**
	 * @hibernate.property column="GUELTIGVON" length="7" not-null="true"
	 * 
	 */
	@Column(name = "GUELTIG_VON", columnDefinition = "DATE", nullable = false)
	private BaseDateTime validFrom;
	/**
	 * @hibernate.property column="GUELTIGBIS" length="7" not-null="true"
	 * 
	 */
	@Column(name = "GUELTIG_BIS", columnDefinition = "DATE", nullable = false)
	private BaseDateTime validTo;
	/**
	 * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */
	@Column(name = "ERSTELLT_AM", columnDefinition = "DATE", nullable = false)
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="GEANDERTAM" length="7"
	 * 
	 */
	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	private BaseDateTime modificationDate;
	/**
	 * @hibernate.property column="BEISTELLUNG_AG" length="1" not-null="true"
	 * 
	 */
	@Column(name = "BEISTELLUNG_AG", columnDefinition = "NUMBER", length = 1, nullable = false)
	private Boolean withProvision;
	/**
	 * @hibernate.property column="E_PREISSTAND_DATUM" length="8" not-null="true"
	 * 
	 */
	@Column(name = "E_PREISSTAND_DATUM", columnDefinition = "DATE")
	private BaseDateTime electricPurchasedPartPriceLevelDate;

	@Column(name = "E_PREISSTAND_DATUM", columnDefinition = "DATE", insertable = false, updatable = false)
	private BaseDateTime oldElectricPurchasedPartPriceLevelDate;
	/**
	 * @hibernate.property column="E_PREISSTAND_BEMERKUNG" length="300" not-null="true"
	 * 
	 */
	@Column(name = "E_PREISSTAND_BEMERKUNG", length = 300)
	private String electricPurchasedPartPriceLevelNotice;

	@Column(name = "E_PREISSTAND_BEMERKUNG", length = 300, insertable = false, updatable = false)
	private String oldElectricPPPLNotice;
	/**
	 * @hibernate.property column="M_PREISSTAND_DATUM" length="8" not-null="true"
	 * 
	 */
	@Column(name = "M_PREISSTAND_DATUM", columnDefinition = "DATE")
	private BaseDateTime mecanicPurchasedPartPriceLevelDate;

	@Column(name = "M_PREISSTAND_DATUM", columnDefinition = "DATE", insertable = false, updatable = false)
	private BaseDateTime oldMecanicPPPLDate;
	/**
	 * @hibernate.property column="M_PREISSTAND_BEMERKUNG" length="300" not-null="true"
	 * 
	 */
	@Column(name = "M_PREISSTAND_BEMERKUNG", length = 300)
	private String mecanicPurchasedPartPriceLevelNotice;

	@Column(name = "M_PREISSTAND_BEMERKUNG", length = 300, insertable = false, updatable = false)
	private String oldMecanicPPPLNotice;
	/**
	 * @hibernate.property column="KATALOG_NUMMER" length="300" not-null="true"
	 * 
	 */
	@Column(name = "KATALOG_NUMMER", length = 300)
	private String catalogNumber;

	@Column(name = "KATALOG_NUMMER", length = 300, insertable = false, updatable = false)
	private String oldCatalogNumber;
	/**
	 * @hibernate.property column="HERSTELLER" length="300" not-null="true"
	 * 
	 */
	@Column(name = "HERSTELLER", length = 300)
	private String manufacturer;

	@Column(name = "HERSTELLER", length = 300, insertable = false, updatable = false)
	private String oldManufacturer;
	/**
	 * @hibernate.property column="VERSION_NUMMER" length="9"
	 * 
	 */
	@Column(name = "VERSION_NUMMER", length = 9)
	private Long number;
	/**
	 * @hibernate.property column="GESPERRT" length="1"
	 * 
	 */
	@Column(name = "GESPERRT", length = 1)
	private Boolean isLocked;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ERSTELLER_REF_ID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "ERSTELLER_REF_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "ERSTELLER_REF_ID", nullable = false)
	private UserModel creator;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="GEAENDERT_VON_REF_ID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "GEAENDERT_VON_REF_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "GEAENDERT_VON_REF_ID", nullable = false)
	private UserModel modifier;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KEREFUUID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "KE_REF_UU_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "KE_REF_UU_ID", nullable = false)
	private ElementModel element;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="FREIGABE_DURCH_REF_ID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "FREIGABE_DURCH_REF_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "FREIGABE_DURCH_REF_ID", nullable = false)
	private UserModel releaser;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ENUMSTATUSREFID"
	 * 
	 */
	@ManyToOne
	@JoinColumn(name = "ENUM_STATUS_REF_ID")
	//TODO conflict with DB
	//	@JoinColumn(name = "ENUM_STATUS_REF_ID", nullable = false)
	private EnumStatusModel enumStatus;

	@ManyToMany
	@JoinTable(name = "T_REL_ELEMVERSION_DOKUMENTPOOL", joinColumns = {
			@JoinColumn(name = "UUIDKEVERSION_REF_ID") }, inverseJoinColumns = { @JoinColumn(name = "ID") })
	private Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(0);
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="ELEMVERREFID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Telementwerte"
	 * 
	 */
	@OneToMany(mappedBy = "elementVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ElementValueModel> elementValues = new HashSet<ElementValueModel>(0);

	@OneToMany(mappedBy = "elementVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ElementVersionChangeModel> elementVersionChanges = new HashSet<ElementVersionChangeModel>(0);

	@ManyToOne
	@JoinColumn(name = "KATEGORIE_REF_ID")
	//TODO conflict with DB
	//@JoinColumn(name = "KATEGORIE_REF_ID", nullable = false)
	private ElementCategoryModel elementCategory;

	@ManyToOne
	@JoinColumn(name = "KATEGORIE_REF_ID", insertable = false, updatable = false)
	private ElementCategoryModel oldElementCategory;
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="KE_VERSION_REF_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ElementVersionUsersModel"
	 * 
	 */

	@OneToMany(mappedBy = "elementVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ElementVersionUsersModel> elementVersionUsers = new HashSet<ElementVersionUsersModel>(0);
	/**
	 * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * @hibernate.collection-key column="KEREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ComponentElement"
	 * 
	 */

	@OneToMany(mappedBy = "elementVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentElementModel> componentElements = new HashSet<ComponentElementModel>(0);

	public ElementVersionModel() {
	}

	public ElementVersionModel(BaseDateTime validFrom, BaseDateTime validTo, BaseDateTime creationDate,
			Boolean withProvision) {
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.creationDate = creationDate;
		this.withProvision = withProvision;
	}

	public ElementVersionModel(String description, String oldDescription, String mechanicalConstruction,
			String oldMechanicalConstruction, String mechanicalExecution, String oldMechanicalExecution,
			String electric, String oldElectric, String comment, String oldComment, BaseDateTime validFrom,
			BaseDateTime validTo, BaseDateTime creationDate, BaseDateTime modificationDate, Boolean withProvision,
			BaseDateTime electricPurchasedPartPriceLevelDate, BaseDateTime oldElectricPurchasedPartPriceLevelDate,
			String electricPurchasedPartPriceLevelNotice, String oldElectricPPPLNotice,
			BaseDateTime mecanicPurchasedPartPriceLevelDate, BaseDateTime oldMecanicPPPLDate,
			String mecanicPurchasedPartPriceLevelNotice, String oldMecanicPPPLNotice, String catalogNumber,
			String oldCatalogNumber, String manufacturer, String oldManufacturer, Long number, Boolean isLocked,
			UserModel creator, UserModel modifier, ElementModel element, UserModel releaser, EnumStatusModel enumStatus,
			Set<DocumentPoolModel> documents, Set<ElementValueModel> elementValues,
			Set<ElementVersionChangeModel> elementVersionChanges, ElementCategoryModel elementCategory,
			ElementCategoryModel oldElementCategory, Set<ElementVersionUsersModel> elementVersionUsers,
			Set<ComponentElementModel> componentElements) {
		this.description = description;
		this.oldDescription = oldDescription;
		this.mechanicalConstruction = mechanicalConstruction;
		this.oldMechanicalConstruction = oldMechanicalConstruction;
		this.mechanicalExecution = mechanicalExecution;
		this.oldMechanicalExecution = oldMechanicalExecution;
		this.electric = electric;
		this.oldElectric = oldElectric;
		this.comment = comment;
		this.oldComment = oldComment;
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.withProvision = withProvision;
		this.electricPurchasedPartPriceLevelDate = electricPurchasedPartPriceLevelDate;
		this.oldElectricPurchasedPartPriceLevelDate = oldElectricPurchasedPartPriceLevelDate;
		this.electricPurchasedPartPriceLevelNotice = electricPurchasedPartPriceLevelNotice;
		this.oldElectricPPPLNotice = oldElectricPPPLNotice;
		this.mecanicPurchasedPartPriceLevelDate = mecanicPurchasedPartPriceLevelDate;
		this.oldMecanicPPPLDate = oldMecanicPPPLDate;
		this.mecanicPurchasedPartPriceLevelNotice = mecanicPurchasedPartPriceLevelNotice;
		this.oldMecanicPPPLNotice = oldMecanicPPPLNotice;
		this.catalogNumber = catalogNumber;
		this.oldCatalogNumber = oldCatalogNumber;
		this.manufacturer = manufacturer;
		this.oldManufacturer = oldManufacturer;
		this.number = number;
		this.isLocked = isLocked;
		this.creator = creator;
		this.modifier = modifier;
		this.element = element;
		this.releaser = releaser;
		this.enumStatus = enumStatus;
		this.documents = documents;
		this.elementValues = elementValues;
		this.elementVersionChanges = elementVersionChanges;
		this.elementCategory = elementCategory;
		this.oldElementCategory = oldElementCategory;
		this.elementVersionUsers = elementVersionUsers;
		this.componentElements = componentElements;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.String" column="UUIDKEVERSION"
	 * 
	 */
	public Long getElementVersionId() {
		return this.elementVersionId;
	}

	private void setElementVersionId(Long elementVersionId) {
		this.elementVersionId = elementVersionId;
	}

	/**
	 * * @hibernate.property column="BESCHREIBUNG" length="1000"
	 * 
	 */
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
	 * * @hibernate.property column="MECHANIKKONSTRUKTION" length="4000"
	 * 
	 */
	public String getMechanicalConstruction() {
		return this.mechanicalConstruction;
	}

	public void setMechanicalConstruction(String mechanicalConstruction) {
		this.mechanicalConstruction = mechanicalConstruction;
	}

	public String getOldMechanicalConstruction() {
		return this.oldMechanicalConstruction;
	}

	public void setOldMechanicalConstruction(String oldMechanicalConstruction) {
		this.oldMechanicalConstruction = oldMechanicalConstruction;
	}

	/**
	 * * @hibernate.property column="MECHANIKAUSFUEHRUNG" length="4000"
	 * 
	 */
	public String getMechanicalExecution() {
		return this.mechanicalExecution;
	}

	public void setMechanicalExecution(String mechanicalExecution) {
		this.mechanicalExecution = mechanicalExecution;
	}

	public String getOldMechanicalExecution() {
		return this.oldMechanicalExecution;
	}

	public void setOldMechanicalExecution(String oldMechanicalExecution) {
		this.oldMechanicalExecution = oldMechanicalExecution;
	}

	/**
	 * * @hibernate.property column="ELEKTRIK" length="4000"
	 * 
	 */
	public String getElectric() {
		return this.electric;
	}

	public void setElectric(String electric) {
		this.electric = electric;
	}

	public String getOldElectric() {
		return this.oldElectric;
	}

	public void setOldElectric(String oldElectric) {
		this.oldElectric = oldElectric;
	}

	/**
	 * * @hibernate.property column="BEMERKUNGEN" length="4000"
	 * 
	 */
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOldComment() {
		return this.oldComment;
	}

	public void setOldComment(String oldComment) {
		this.oldComment = oldComment;
	}

	/**
	 * * @hibernate.property column="GUELTIGVON" length="7" not-null="true"
	 * 
	 */
	public BaseDateTime getValidFrom() {
		return this.validFrom;
	}

	public void setValidFrom(BaseDateTime validFrom) {
		this.validFrom = validFrom;
	}

	/**
	 * * @hibernate.property column="GUELTIGBIS" length="7" not-null="true"
	 * 
	 */
	public BaseDateTime getValidTo() {
		return this.validTo;
	}

	public void setValidTo(BaseDateTime validTo) {
		this.validTo = validTo;
	}

	/**
	 * * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */
	public BaseDateTime getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(BaseDateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * * @hibernate.property column="GEANDERTAM" length="7"
	 * 
	 */
	public BaseDateTime getModificationDate() {
		return this.modificationDate;
	}

	public void setModificationDate(BaseDateTime modificationDate) {
		this.modificationDate = modificationDate;
	}

	/**
	 * * @hibernate.property column="BEISTELLUNG_AG" length="1" not-null="true"
	 * 
	 */
	public Boolean getWithProvision() {
		return this.withProvision;
	}

	public void setWithProvision(Boolean withProvision) {
		this.withProvision = withProvision;
	}

	/**
	 * * @hibernate.property column="E_PREISSTAND_DATUM" length="8" not-null="true"
	 * 
	 */
	public BaseDateTime getElectricPurchasedPartPriceLevelDate() {
		return this.electricPurchasedPartPriceLevelDate;
	}

	public void setElectricPurchasedPartPriceLevelDate(BaseDateTime electricPurchasedPartPriceLevelDate) {
		this.electricPurchasedPartPriceLevelDate = electricPurchasedPartPriceLevelDate;
	}

	public BaseDateTime getOldElectricPurchasedPartPriceLevelDate() {
		return this.oldElectricPurchasedPartPriceLevelDate;
	}

	public void setOldElectricPurchasedPartPriceLevelDate(BaseDateTime oldElectricPurchasedPartPriceLevelDate) {
		this.oldElectricPurchasedPartPriceLevelDate = oldElectricPurchasedPartPriceLevelDate;
	}

	/**
	 * * @hibernate.property column="E_PREISSTAND_BEMERKUNG" length="300" not-null="true"
	 * 
	 */
	public String getElectricPurchasedPartPriceLevelNotice() {
		return this.electricPurchasedPartPriceLevelNotice;
	}

	public void setElectricPurchasedPartPriceLevelNotice(String electricPurchasedPartPriceLevelNotice) {
		this.electricPurchasedPartPriceLevelNotice = electricPurchasedPartPriceLevelNotice;
	}

	public String getOldElectricPPPLNotice() {
		return this.oldElectricPPPLNotice;
	}

	public void setOldElectricPPPLNotice(String oldElectricPPPLNotice) {
		this.oldElectricPPPLNotice = oldElectricPPPLNotice;
	}

	/**
	 * * @hibernate.property column="M_PREISSTAND_DATUM" length="8" not-null="true"
	 * 
	 */
	public BaseDateTime getMecanicPurchasedPartPriceLevelDate() {
		return this.mecanicPurchasedPartPriceLevelDate;
	}

	public void setMecanicPurchasedPartPriceLevelDate(BaseDateTime mecanicPurchasedPartPriceLevelDate) {
		this.mecanicPurchasedPartPriceLevelDate = mecanicPurchasedPartPriceLevelDate;
	}

	public BaseDateTime getOldMecanicPPPLDate() {
		return this.oldMecanicPPPLDate;
	}

	public void setOldMecanicPPPLDate(BaseDateTime oldMecanicPPPLDate) {
		this.oldMecanicPPPLDate = oldMecanicPPPLDate;
	}

	/**
	 * * @hibernate.property column="M_PREISSTAND_BEMERKUNG" length="300" not-null="true"
	 * 
	 */
	public String getMecanicPurchasedPartPriceLevelNotice() {
		return this.mecanicPurchasedPartPriceLevelNotice;
	}

	public void setMecanicPurchasedPartPriceLevelNotice(String mecanicPurchasedPartPriceLevelNotice) {
		this.mecanicPurchasedPartPriceLevelNotice = mecanicPurchasedPartPriceLevelNotice;
	}

	public String getOldMecanicPPPLNotice() {
		return this.oldMecanicPPPLNotice;
	}

	public void setOldMecanicPPPLNotice(String oldMecanicPPPLNotice) {
		this.oldMecanicPPPLNotice = oldMecanicPPPLNotice;
	}

	/**
	 * * @hibernate.property column="KATALOG_NUMMER" length="300" not-null="true"
	 * 
	 */
	public String getCatalogNumber() {
		return this.catalogNumber;
	}

	public void setCatalogNumber(String catalogNumber) {
		this.catalogNumber = catalogNumber;
	}

	public String getOldCatalogNumber() {
		return this.oldCatalogNumber;
	}

	public void setOldCatalogNumber(String oldCatalogNumber) {
		this.oldCatalogNumber = oldCatalogNumber;
	}

	/**
	 * * @hibernate.property column="HERSTELLER" length="300" not-null="true"
	 * 
	 */
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getOldManufacturer() {
		return this.oldManufacturer;
	}

	public void setOldManufacturer(String oldManufacturer) {
		this.oldManufacturer = oldManufacturer;
	}

	/**
	 * * @hibernate.property column="VERSION_NUMMER" length="9"
	 * 
	 */
	public Long getNumber() {
		return this.number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	/**
	 * * @hibernate.property column="GESPERRT" length="1"
	 * 
	 */
	public Boolean getIsLocked() {
		return this.isLocked;
	}

	public void setIsLocked(Boolean isLocked) {
		this.isLocked = isLocked;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="ERSTELLER_REF_ID"
	 * 
	 */
	public UserModel getCreator() {
		return this.creator;
	}

	public void setCreator(UserModel creator) {
		this.creator = creator;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="GEAENDERT_VON_REF_ID"
	 * 
	 */
	public UserModel getModifier() {
		return this.modifier;
	}

	public void setModifier(UserModel modifier) {
		this.modifier = modifier;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="KEREFUUID"
	 * 
	 */
	public ElementModel getElement() {
		return this.element;
	}

	public void setElement(ElementModel element) {
		this.element = element;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="FREIGABE_DURCH_REF_ID"
	 * 
	 */
	public UserModel getReleaser() {
		return this.releaser;
	}

	public void setReleaser(UserModel releaser) {
		this.releaser = releaser;
	}

	/**
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="ENUMSTATUSREFID"
	 * 
	 */
	public EnumStatusModel getEnumStatus() {
		return this.enumStatus;
	}

	public void setEnumStatus(EnumStatusModel enumStatus) {
		this.enumStatus = enumStatus;
	}

	public Set<DocumentPoolModel> getDocuments() {
		return this.documents;
	}

	public void setDocuments(Set<DocumentPoolModel> documents) {
		this.documents = documents;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="ELEMVERREFID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.Telementwerte"
	 * 
	 */
	public Set<ElementValueModel> getElementValues() {
		return this.elementValues;
	}

	public void setElementValues(Set<ElementValueModel> elementValues) {
		this.elementValues = elementValues;
	}

	public Set<ElementVersionChangeModel> getElementVersionChanges() {
		return this.elementVersionChanges;
	}

	public void setElementVersionChanges(Set<ElementVersionChangeModel> elementVersionChanges) {
		this.elementVersionChanges = elementVersionChanges;
	}

	public ElementCategoryModel getElementCategory() {
		return this.elementCategory;
	}

	public void setElementCategory(ElementCategoryModel elementCategory) {
		this.elementCategory = elementCategory;
	}

	public ElementCategoryModel getOldElementCategory() {
		return this.oldElementCategory;
	}

	public void setOldElementCategory(ElementCategoryModel oldElementCategory) {
		this.oldElementCategory = oldElementCategory;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="KE_VERSION_REF_ID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ElementVersionUsersModel"
	 * 
	 */
	public Set<ElementVersionUsersModel> getElementVersionUsers() {
		return this.elementVersionUsers;
	}

	public void setElementVersionUsers(Set<ElementVersionUsersModel> elementVersionUsers) {
		this.elementVersionUsers = elementVersionUsers;
	}

	/**
	 * * @hibernate.set lazy="true" inverse="true" cascade="none"
	 * 
	 * @hibernate.collection-key column="KEREFUUID"
	 * @hibernate.collection-one-to-many class="vwg.vw.km.integration.persistence.model.ComponentElement"
	 * 
	 */
	public Set<ComponentElementModel> getComponentElements() {
		return this.componentElements;
	}

	public void setComponentElements(Set<ComponentElementModel> componentElements) {
		this.componentElements = componentElements;
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
		buffer.append("elementVersionId").append("='").append(getElementVersionId()).append("' ");
		buffer.append("number").append("='").append(getNumber()).append("' ");
		buffer.append("element").append("='").append(getElement()).append("' ");
		buffer.append("enumStatus").append("='").append(getEnumStatus()).append("' ");
		buffer.append("elementCategory").append("='").append(getElementCategory()).append("' ");
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
		if (!(other instanceof ElementVersionModel)) {
			return false;
		}
		ElementVersionModel castOther = (ElementVersionModel) other;

		return ((this.getElementVersionId() == castOther.getElementVersionId())
				|| (this.getElementVersionId() != null && castOther.getElementVersionId() != null
						&& this.getElementVersionId().equals(castOther.getElementVersionId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getElementVersionId() == null ? 0 : this.getElementVersionId().hashCode());

		return result;
	}

	// The following is extra code specified in the hbm.xml files

	public String getShortDescription() {
		String shortDescription = description;
		if (shortDescription != null && shortDescription.length() > 25) {
			shortDescription = shortDescription.substring(0, 25) + "...";
		}
		return shortDescription;
	}

	public String getShortComment() {
		String shortComment = comment;
		if (shortComment != null && shortComment.length() > 25) {
			shortComment = shortComment.substring(0, 25) + "...";
		}
		return shortComment;
	}

	@Transient
	private List<ElementValueModel> elementValuesForView;
	@Transient
	private List<CostAttributeCategoryModel> electricalCostAttributeCategories;
	@Transient
	private BaseBigDecimal electricalSum;
	@Transient
	private List<CostAttributeCategoryModel> mecanicsCostAttributeCategories;
	@Transient
	private BaseBigDecimal mecanicsSum;
	@Transient
	private BaseBigDecimal mecanicsProvisionSum;
	@Transient
	private BaseBigDecimal electricalProvisionSum;
	@Transient
	private HourlyRateCatalogModel catalogModel;

	public BaseBigDecimal getTotal() {
		if (electricalSum == null && mecanicsSum == null) {
			return null;
		}
		if (electricalSum == null) {
			return mecanicsSum;
		}
		if (mecanicsSum == null) {
			return electricalSum;
		}
		return electricalSum.add(mecanicsSum);
	}

	public BaseBigDecimal getTotalProvision() {
		if (electricalProvisionSum == null && mecanicsProvisionSum == null) {
			return null;
		}
		if (electricalProvisionSum == null) {
			return mecanicsProvisionSum;
		}
		if (mecanicsProvisionSum == null) {
			return electricalProvisionSum;
		}
		return electricalProvisionSum.add(mecanicsProvisionSum);
	}

	public BaseBigDecimal getSumTotalAndTotalProvision() {
		if (getTotalProvision() == null && getTotal() == null) {
			return null;
		}
		if (getTotalProvision() == null) {
			return getTotal();
		}
		if (getTotal() == null) {
			return getTotalProvision();
		}
		return getTotal().add(getTotalProvision());
	}

	public List<ElementValueModel> getElementValuesForView() {
		return elementValuesForView;
	}

	public void setElementValuesForView(List<ElementValueModel> elementValuesForView) {
		this.elementValuesForView = elementValuesForView;
	}

	public List<CostAttributeCategoryModel> getElectricalCostAttributeCategories() {
		return electricalCostAttributeCategories;
	}

	public void setElectricalCostAttributeCategories(
			List<CostAttributeCategoryModel> electricalCostAttributeCategories) {
		this.electricalCostAttributeCategories = electricalCostAttributeCategories;
	}

	public List<CostAttributeCategoryModel> getMecanicsCostAttributeCategories() {
		return mecanicsCostAttributeCategories;
	}

	public void setMecanicsCostAttributeCategories(List<CostAttributeCategoryModel> mecanicsCostAttributeCategories) {
		this.mecanicsCostAttributeCategories = mecanicsCostAttributeCategories;
	}

	public BaseBigDecimal getElectricalSum() {
		return electricalSum;
	}

	public void setElectricalSum(BaseBigDecimal electricalSum) {
		this.electricalSum = electricalSum;
	}

	public BaseBigDecimal getMecanicsSum() {
		return mecanicsSum;
	}

	public void setMecanicsSum(BaseBigDecimal mecanicsSum) {
		this.mecanicsSum = mecanicsSum;
	}

	public BaseBigDecimal getMecanicsProvisionSum() {
		return mecanicsProvisionSum;
	}

	public void setMecanicsProvisionSum(BaseBigDecimal mecanicsProvisionSum) {
		this.mecanicsProvisionSum = mecanicsProvisionSum;
	}

	public BaseBigDecimal getElectricalProvisionSum() {
		return electricalProvisionSum;
	}

	public void setElectricalProvisionSum(BaseBigDecimal electricalProvisionSum) {
		this.electricalProvisionSum = electricalProvisionSum;
	}

	public HourlyRateCatalogModel getCatalogModel() {
		return catalogModel;
	}

	public void setCatalogModel(HourlyRateCatalogModel catalogModel) {
		this.catalogModel = catalogModel;
	}

	public ElementValueModel getElementValueByCostAttribute(CostAttributeModel cAttributeModel) {
		ElementValueModel eValueModel = null;
		for (ElementValueModel e : getElementValuesForView()) {
			if (e.getCostAttribute().equals(cAttributeModel)) {
				eValueModel = e;
				break;
			}
		}
		return eValueModel;
	}

	public java.util.Date getElectricPurchasedPartPriceLevelDateForView() {
		if (this.electricPurchasedPartPriceLevelDate != null) {
			return this.electricPurchasedPartPriceLevelDate.getDate();
		}
		return null;
	}

	public void setElectricPurchasedPartPriceLevelDateForView(java.util.Date date) {
		setElectricPurchasedPartPriceLevelDate(new BaseDateTime(date));
	}

	public java.util.Date getMecanicPurchasedPartPriceLevelDateForView() {
		if (this.mecanicPurchasedPartPriceLevelDate != null) {
			return this.mecanicPurchasedPartPriceLevelDate.getDate();
		}
		return null;
	}

	public void setMecanicPurchasedPartPriceLevelDateForView(java.util.Date date) {
		setMecanicPurchasedPartPriceLevelDate(new BaseDateTime(date));
	}

	public String getReleaserFullName() {
		if (releaser != null) {
			if (releaser.getFirstName() == null || "".equals(releaser.getFirstName().trim())) {
				return releaser.getLastName();
			}
			if (releaser.getLastName() == null || "".equals(releaser.getLastName().trim())) {
				return releaser.getFirstName();
			}
			return releaser.getLastName() + " " + releaser.getFirstName();
		}
		return null;
	}

	public List<BrandModel> getUsers() {
		if (getElementVersionUsers() != null) {
			List<ElementVersionUsersModel> elementVersionUsers = new java.util.ArrayList<ElementVersionUsersModel>(
					getElementVersionUsers());
			if (elementVersionUsers != null) {
				Set<BrandModel> users = new HashSet<BrandModel>();
				for (ElementVersionUsersModel elementVersionUser : elementVersionUsers) {
					users.add(elementVersionUser.getBrand());
				}
				return new java.util.ArrayList<BrandModel>(users);
			}
		}
		return null;
	}

	public List<String> getUsersImg() {
		List<String> users = new java.util.ArrayList<String>();
		if (getElementVersionUsers() != null) {
			List<ElementVersionUsersModel> usersObject = new java.util.ArrayList<ElementVersionUsersModel>(
					getElementVersionUsers());
			if (usersObject != null) {
				for (ElementVersionUsersModel elementVersionUser : usersObject) {
					if (elementVersionUser.getActive()) {
						users.add(elementVersionUser.getBrand().getUseImg());
					} else {
						users.add(elementVersionUser.getBrand().getUseInactiveImg());
					}
				}
				return users;
			}
		}
		return null;
	}

	public UserModel getProcessor() {
		return (getModifier() != null) ? getModifier() : getCreator();
	}

	// end of extra code specified in the hbm.xml files

}
