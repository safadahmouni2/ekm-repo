package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import java.sql.Blob;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.CostCategory;

/**
 * @hibernate.class table="T_BAUSTEINVERSION"
 * 
 */

@Entity
@Table(name = "T_BAUSTEINVERSION")
public class ComponentVersionModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.String" column="KBVERSIONUUID"
	 * 
	 */

	@Id
	@Column(name = "KB_VERSION_UU_ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_component_version")
	@SequenceGenerator(name = "seq_component_version", sequenceName = "SEQ_COMPONENT_VERSION", allocationSize = 1)
	private Long componentVersionId;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="2048"
	 * 
	 */

	@Column(name = "BESCHREIBUNG", length = 2048)
	private String description;


	@Column(name = "BESCHREIBUNG", length = 2048, insertable = false, updatable = false)
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

	@Column(name = "GUELTIG_VON", nullable = false, columnDefinition = "DATE")
	private BaseDateTime validFrom;
	/**
	 * @hibernate.property column="GUELTIGBIS" length="7" not-null="true"
	 * 
	 */

	@Column(name = "GUELTIG_BIS", nullable = false, columnDefinition = "DATE")
	private BaseDateTime validTo;
	/**
	 * @hibernate.property column="ABBILDUNG" length="4000"
	 * 
	 */

	@Lob()
	@Column(name = "ABBILDUNG", length = 4000)
	private Blob figure;
	/**
	 * @hibernate.property column="ERSTELLTAM" length="7" not-null="true"
	 * 
	 */

	@Column(name = "ERSTELLT_AM", nullable = false, columnDefinition = "DATE")
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="GEANDERTAM" length="7"
	 * 
	 */

	@Column(name = "GEAENDERT_AM", columnDefinition = "DATE")
	//TODO conflict with DB
	//@Column(name = "GEAENDERT_AM", nullable = false, columnDefinition = "DATE")
	private BaseDateTime modificationDate;
	/**
	 * @hibernate.property column="BEISTELLUNG_AG" length="1" not-null="true"
	 * 
	 */

	@Column(name = "BEISTELLUNG_AG", length = 1, nullable = false)
	private Boolean withProvision;

	@Column(name = "BEISTELLUNG_AG", insertable = false, updatable = false)
	private Boolean oldWithProvision;
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

	@Column(name = "E_PREISSTAND_BEMERKUNG", insertable = false, updatable = false)
	private String oldElectricPPPLNotice;
	/**
	 * @hibernate.property column="M_PREISSTAND_DATUM" length="8" not-null="true"
	 * 
	 */

	@Column(name = "M_PREISSTAND_DATUM", length = 8, columnDefinition = "DATE")
	private BaseDateTime mecanicPurchasedPartPriceLevelDate;

	@Column(name = "M_PREISSTAND_DATUM", insertable = false, updatable = false)
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
	// add KOSTEN_GRUPPE column
	@Column(name = "KOSTEN_GRUPPE", length = 20)
	@Enumerated(EnumType.STRING)
	private EnumCostgroup costgroup;

	@Enumerated(EnumType.STRING)
	@Column(name = "KOSTEN_GRUPPE", length = 20, insertable = false, updatable = false)
	private EnumCostgroup oldCostgroup;

	@Column(name = "GESPERRT", length = 1)
	private Boolean isLocked;

	@Lob
	@Column(name = "VORSCHAUBILD_INHALT", columnDefinition = "BLOB")
	private byte[] imagePreviewContent;

	@Lob
	@Column(name = "VORSCHAUBILD_INHALT", columnDefinition = "BLOB", insertable = false, updatable = false)
	private byte[] oldImagePreviewContent;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ERSTELLER_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "ERSTELLER_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ERSTELLER_REF_ID", nullable = false)
	private UserModel creator;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="GEAENDERT_VON_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "GEAENDERT_VON_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "GEAENDERT_VON_REF_ID", nullable = false)
	private UserModel modifier;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="KBREFUUID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "KB_REF_UU_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "KB_REF_UU_ID", nullable = false)
	private ComponentModel component;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="FREIGABE_DURCH_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "FREIGABE_DURCH_REF_ID")
	private UserModel releaser;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ENUM_BAUSTEIN_KLASSE_REF_ID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "ENUM_BAUSTEIN_KLASSE_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ENUM_BAUSTEIN_KLASSE_REF_ID", nullable = false)
	private EnumComponentClassModel enumComponentClass;
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ENUMSTATUSREFID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "ENUM_STATUS_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ENUM_STATUS_REF_ID", nullable = false)
	private EnumStatusModel enumStatus;

	@ManyToMany
	@JoinTable(name = "T_REL_BAUSVERSION_DOKUMENTPOOL", joinColumns = {
			@JoinColumn(name = "KB_VERSION_UU_ID") }, inverseJoinColumns = { @JoinColumn(name = "ID") })
	private Set<DocumentPoolModel> documents = new HashSet<DocumentPoolModel>(0);
	/**
	 * @hibernate.many-to-one not-null="true"
	 * @hibernate.column name="ENUMBAUSTEINTYPREFID"
	 * 
	 */

	@ManyToOne
	@JoinColumn(name = "ENUM_BAUSTEIN_TYP_REF_ID")
	// TODO conflict with DB
	// @JoinColumn(name = "ENUM_BAUSTEIN_TYP_REF_ID", nullable = false)
	private EnumComponentTypeModel enumComponentType;

	@ManyToOne
	@JoinColumn(name = "ENUM_BAUSTEIN_TYP_REF_ID", insertable = false, updatable = false)
	private EnumComponentTypeModel oldEnumComponentType;

	@OneToMany(mappedBy = "componentVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentValueModel> componentValues = new HashSet<ComponentValueModel>(0);

	@OneToMany(mappedBy = "componentVersion", fetch = FetchType.LAZY, orphanRemoval = true)
	private Set<ComponentStandModel> componentVersionStands = new HashSet<ComponentStandModel>(0);

	public ComponentVersionModel() {
	}

	public ComponentVersionModel(BaseDateTime validFrom, BaseDateTime validTo, BaseDateTime creationDate,
			Boolean withProvision) {
		this.validFrom = validFrom;
		this.validTo = validTo;
		this.creationDate = creationDate;
		this.withProvision = withProvision;
	}

	public ComponentVersionModel(String description, String oldDescription, String mechanicalConstruction,
			String oldMechanicalConstruction, String mechanicalExecution, String oldMechanicalExecution,
			String electric, String oldElectric, String comment, String oldComment, BaseDateTime validFrom,
			BaseDateTime validTo, Blob figure, BaseDateTime creationDate, BaseDateTime modificationDate,
			Boolean withProvision, Boolean oldWithProvision, BaseDateTime electricPurchasedPartPriceLevelDate,
			BaseDateTime oldElectricPurchasedPartPriceLevelDate, String electricPurchasedPartPriceLevelNotice,
			String oldElectricPPPLNotice, BaseDateTime mecanicPurchasedPartPriceLevelDate,
			BaseDateTime oldMecanicPPPLDate, String mecanicPurchasedPartPriceLevelNotice, String oldMecanicPPPLNotice,
			String catalogNumber, String oldCatalogNumber, String manufacturer, String oldManufacturer, Long number,
			Boolean isLocked, UserModel creator, UserModel modifier, ComponentModel component, UserModel releaser,
			EnumComponentClassModel enumComponentClass, EnumStatusModel enumStatus, Set<DocumentPoolModel> documents,
			EnumComponentTypeModel enumComponentType, EnumComponentTypeModel oldEnumComponentType,
			Set<ComponentValueModel> componentValues, Set<ComponentStandModel> componentVersionStands,
			EnumCostgroup costgroup, EnumCostgroup oldCostgroup, byte[] imagePreviewContent,
			byte[] oldImagePreviewContent) {
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
		this.figure = figure;
		this.creationDate = creationDate;
		this.modificationDate = modificationDate;
		this.withProvision = withProvision;
		this.oldWithProvision = oldWithProvision;
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
		this.component = component;
		this.releaser = releaser;
		this.enumComponentClass = enumComponentClass;
		this.enumStatus = enumStatus;
		this.documents = documents;
		this.enumComponentType = enumComponentType;
		this.oldEnumComponentType = oldEnumComponentType;
		this.componentValues = componentValues;
		this.componentVersionStands = componentVersionStands;
		this.costgroup=costgroup;
		this.oldCostgroup=oldCostgroup;
		this.imagePreviewContent = imagePreviewContent;
		this.oldImagePreviewContent = oldImagePreviewContent;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.String" column="KBVERSIONUUID"
	 * 
	 */
	public Long getComponentVersionId() {
		return this.componentVersionId;
	}

	private void setComponentVersionId(Long componentVersionId) {
		this.componentVersionId = componentVersionId;
	}

	/**
	 * * @hibernate.property column="BESCHREIBUNG" length="2048"
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

	public void setCostgroup(EnumCostgroup costgroup) {
		this.costgroup = costgroup;
	}
	public EnumCostgroup getCostgroup() {
		return this.costgroup;
	}

	public EnumCostgroup getOldCostgroup() {
		return oldCostgroup;
	}

	public void setOldCostgroup(EnumCostgroup oldCostgroup) {
		this.oldCostgroup = oldCostgroup;
	}

	public byte[] getOldImagePreviewContent() {
		return oldImagePreviewContent;
	}

	public void setOldImagePreviewContent(byte[] oldImagePreviewContent) {
		this.oldImagePreviewContent = oldImagePreviewContent;
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
	 * * @hibernate.property column="ABBILDUNG" length="4000"
	 * 
	 */
	public Blob getFigure() {
		return this.figure;
	}

	public void setFigure(Blob figure) {
		this.figure = figure;
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

	public Boolean getOldWithProvision() {
		return this.oldWithProvision;
	}

	public void setOldWithProvision(Boolean oldWithProvision) {
		this.oldWithProvision = oldWithProvision;
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
	 * @hibernate.column name="KBREFUUID"
	 * 
	 */
	public ComponentModel getComponent() {
		return this.component;
	}

	public void setComponent(ComponentModel component) {
		this.component = component;
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
	 * @hibernate.column name="ENUM_BAUSTEIN_KLASSE_REF_ID"
	 * 
	 */
	public EnumComponentClassModel getEnumComponentClass() {
		return this.enumComponentClass;
	}

	public void setEnumComponentClass(EnumComponentClassModel enumComponentClass) {
		this.enumComponentClass = enumComponentClass;
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
	 * * @hibernate.many-to-one not-null="true"
	 * 
	 * @hibernate.column name="ENUMBAUSTEINTYPREFID"
	 * 
	 */
	public EnumComponentTypeModel getEnumComponentType() {
		return this.enumComponentType;
	}

	public void setEnumComponentType(EnumComponentTypeModel enumComponentType) {
		this.enumComponentType = enumComponentType;
	}

	public EnumComponentTypeModel getOldEnumComponentType() {
		return this.oldEnumComponentType;
	}

	public void setOldEnumComponentType(EnumComponentTypeModel oldEnumComponentType) {
		this.oldEnumComponentType = oldEnumComponentType;
	}

	public Set<ComponentValueModel> getComponentValues() {
		return this.componentValues;
	}

	public void setComponentValues(Set<ComponentValueModel> componentValues) {
		this.componentValues = componentValues;
	}

	public Set<ComponentStandModel> getComponentVersionStands() {
		return this.componentVersionStands;
	}

	public void setComponentVersionStands(Set<ComponentStandModel> componentVersionStands) {
		this.componentVersionStands = componentVersionStands;
	}

	public byte[] getImagePreviewContent() {
		return imagePreviewContent;
	}

	public void setImagePreviewContent(byte[] imagePreviewContent) {
		this.imagePreviewContent = imagePreviewContent;
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
		buffer.append("componentVersionId").append("='").append(getComponentVersionId()).append("' ");
		buffer.append("validFrom").append("='").append(getValidFrom()).append("' ");
		buffer.append("validTo").append("='").append(getValidTo()).append("' ");
		buffer.append("creationDate").append("='").append(getCreationDate()).append("' ");
		buffer.append("modificationDate").append("='").append(getModificationDate()).append("' ");
		buffer.append("number").append("='").append(getNumber()).append("' ");
		buffer.append("component").append("='").append(getComponent()).append("' ");
		buffer.append("enumComponentClass").append("='").append(getEnumComponentClass()).append("' ");
		buffer.append("enumStatus").append("='").append(getEnumStatus()).append("' ");
		buffer.append("enumComponentType").append("='").append(getEnumComponentType()).append("' ");
		buffer.append("costgroup").append("='").append(getEnumComponentType()).append("' ");

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
		if (!(other instanceof ComponentVersionModel)) {
			return false;
		}
		ComponentVersionModel castOther = (ComponentVersionModel) other;

		return ((this.getComponentVersionId() == castOther.getComponentVersionId())
				|| (this.getComponentVersionId() != null && castOther.getComponentVersionId() != null
						&& this.getComponentVersionId().equals(castOther.getComponentVersionId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getComponentVersionId() == null ? 0 : this.getComponentVersionId().hashCode());

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

	public BaseBigDecimal[] getTotalAndProvisionFromComponentElementsByCategory(
			CostAttributeCategoryModel costAttributeCategory) {
		BaseBigDecimal totalCategory = new BaseBigDecimal("0");
		BaseBigDecimal totalProvision = new BaseBigDecimal("0");
		String category = costAttributeCategory.getEnumInvestmentPart().getCategory();
		for (ComponentElementModel ecModel : getAvailableComponentElementList()) {
			ElementVersionModel ev = ecModel.getElementVersion();
			List<CostAttributeCategoryModel> categories = new ArrayList<CostAttributeCategoryModel>();
			if (CostCategory.MECHANICS.value().equals(category)) {
				categories = ev.getMecanicsCostAttributeCategories();
			} else if (CostCategory.ELECTRICAL.value().equals(category)) {
				categories = ev.getElectricalCostAttributeCategories();
			}
			int categoryIndex = categories.indexOf(costAttributeCategory);
			if (categoryIndex > -1) {
				BaseBigDecimal number = (ecModel.getNumber() != null)
						? new BaseBigDecimal(ecModel.getNumber().toString())
						: new BaseBigDecimal("0");
				CostAttributeCategoryModel caCat = categories.get(categoryIndex);
				totalCategory = totalCategory.add(number.multiply(caCat.getAmount()));
				totalProvision = totalProvision.add(number.multiply(caCat.getProvisionAmount()));
			}
		}
		BaseBigDecimal[] result = new BaseBigDecimal[2];
		result[0] = totalCategory;
		result[1] = totalProvision;
		return result;
	}


	@Transient
	private List<ComponentElementModel> availableComponentElementList;
	@Transient
	private List<ComponentValueModel> componentValuesForView;

	@Transient
	private List<ComponentValueModel> aggregatedComponentValuesForView;
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
	@Transient
	private Boolean statusElementPublish;

	@Transient
	private String imagePreviewName;

	public String getImagePreviewName() {
		return imagePreviewName;
	}

	public void setImagePreviewName(String imagePreviewName) {
		this.imagePreviewName = imagePreviewName;
	}

	public Boolean getStatusElementPublish() {
		return statusElementPublish;
	}

	public void setStatusElementPublish(Boolean statusElementPublish) {
		this.statusElementPublish = statusElementPublish;
	}

	public vwg.vw.km.common.type.BaseBigDecimal getTotal() {
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

	public vwg.vw.km.common.type.BaseBigDecimal getTotalProvision() {
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

	public List<ComponentElementModel> getAvailableComponentElementList() {
		return availableComponentElementList;
	}

	public void setAvailableComponentElementList(List<ComponentElementModel> availableComponentElementList) {
		this.availableComponentElementList = availableComponentElementList;
	}

	public List<ComponentValueModel> getComponentValuesForView() {
		return componentValuesForView;
	}

	public void setComponentValuesForView(List<ComponentValueModel> componentValuesForView) {
		this.componentValuesForView = componentValuesForView;
	}

	public List<ComponentValueModel> getAggregatedComponentValuesForView() {
		return aggregatedComponentValuesForView;
	}

	public void setAggregatedComponentValuesForView(List<ComponentValueModel> aggregatedComponentValuesForView) {
		this.aggregatedComponentValuesForView = aggregatedComponentValuesForView;
	}

	public List<CostAttributeCategoryModel> getElectricalCostAttributeCategories() {
		return electricalCostAttributeCategories;
	}

	public void setElectricalCostAttributeCategories(
			List<CostAttributeCategoryModel> electricalCostAttributeCategories) {
		this.electricalCostAttributeCategories = electricalCostAttributeCategories;
	}

	public java.util.List<CostAttributeCategoryModel> getMecanicsCostAttributeCategories() {
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

	public List<ComponentStandUsersModel> getUsers() {
		List<ComponentStandUsersModel> componentStandUsers = new ArrayList<ComponentStandUsersModel>();
		if (getComponentVersionStands() != null && !getComponentVersionStands().isEmpty()) {
			for (ComponentStandModel componentStand : getComponentVersionStands()) {
				componentStandUsers.addAll(componentStand.getComponentVersionUsers());
			}
		}
		if (componentStandUsers != null) {
			Set<ComponentStandUsersModel> users = new HashSet<ComponentStandUsersModel>();
			for (ComponentStandUsersModel componentStandUser : componentStandUsers) {
				users.add(componentStandUser);
			}
			return new java.util.ArrayList<ComponentStandUsersModel>(users);
		}
		return null;
	}

	public List<String> getUsersImg() {
		List<String> users = new java.util.ArrayList<String>();
		List<ComponentStandUsersModel> usersObject = getUsers();
		if (usersObject != null) {
			for (ComponentStandUsersModel standUser : usersObject) {
				if (standUser.getActive() != null && standUser.getActive()) {
					users.add(standUser.getBrand().getUseImg());
				} else {
					users.add(standUser.getBrand().getUseInactiveImg());
				}
			}
			return users;
		}
		return null;
	}

	public int getStandCount() {
		if (getComponentVersionStands() != null) {
			return getComponentVersionStands().size();
		}
		return 0;
	}

	public UserModel getProcessor() {
		return (getModifier() != null) ? getModifier() : getCreator();
	}

	@Transient
	private BaseBigDecimal electricalComponentSum;
	@Transient
	private BaseBigDecimal electricalComponentProvisionSum;
	@Transient
	private BaseBigDecimal mecanicsComponentSum;
	@Transient
	private BaseBigDecimal mecanicsComponentProvisionSum;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalComponent() {
		if (electricalComponentSum == null && mecanicsComponentSum == null) {
			return null;
		}
		if (electricalComponentSum == null) {
			return mecanicsComponentSum;
		}
		if (mecanicsComponentSum == null) {
			return electricalComponentSum;
		}
		return electricalComponentSum.add(mecanicsComponentSum);
	}

	public vwg.vw.km.common.type.BaseBigDecimal getTotalComponentProvision() {
		if (electricalComponentProvisionSum == null && mecanicsComponentProvisionSum == null) {
			return null;
		}
		if (electricalComponentProvisionSum == null) {
			return mecanicsComponentProvisionSum;
		}
		if (mecanicsComponentProvisionSum == null) {
			return electricalComponentProvisionSum;
		}
		return electricalComponentProvisionSum.add(mecanicsComponentProvisionSum);
	}

	public BaseBigDecimal getSumTotalComponentAndTotalComponentProvision() {
		if (getTotalComponentProvision() == null && getTotalComponent() == null) {
			return null;
		}
		if (getTotalComponentProvision() == null) {
			return getTotalComponent();
		}
		if (getTotalComponent() == null) {
			return getTotalComponentProvision();
		}
		return getTotalComponent().add(getTotalComponentProvision());
	}

	public BaseBigDecimal getElectricalComponentSum() {
		return electricalComponentSum;
	}

	public void setElectricalComponentSum(BaseBigDecimal electricalComponentSum) {
		this.electricalComponentSum = electricalComponentSum;
	}

	public BaseBigDecimal getElectricalComponentProvisionSum() {
		return electricalComponentProvisionSum;
	}

	public void setElectricalComponentProvisionSum(BaseBigDecimal electricalComponentProvisionSum) {
		this.electricalComponentProvisionSum = electricalComponentProvisionSum;
	}

	public BaseBigDecimal getMecanicsComponentSum() {
		return mecanicsComponentSum;
	}

	public void setMecanicsComponentSum(BaseBigDecimal mecanicsComponentSum) {
		this.mecanicsComponentSum = mecanicsComponentSum;
	}

	public BaseBigDecimal getMecanicsComponentProvisionSum() {
		return mecanicsComponentProvisionSum;
	}

	public void setMecanicsComponentProvisionSum(BaseBigDecimal mecanicsComponentProvisionSum) {
		this.mecanicsComponentProvisionSum = mecanicsComponentProvisionSum;
	}

	@Transient
	private BaseBigDecimal electricalComponentElementSum;
	@Transient
	private BaseBigDecimal electricalComponentElementProvisionSum;
	@Transient
	private BaseBigDecimal mecanicsComponentElementSum;
	@Transient
	private BaseBigDecimal mecanicsComponentElementProvisionSum;

	public vwg.vw.km.common.type.BaseBigDecimal getTotalComponentElement() {
		if (electricalComponentElementSum == null && mecanicsComponentElementSum == null) {
			return null;
		}
		if (electricalComponentElementSum == null) {
			return mecanicsComponentElementSum;
		}
		if (mecanicsComponentElementSum == null) {
			return electricalComponentElementSum;
		}
		return electricalComponentElementSum.add(mecanicsComponentElementSum);
	}

	public vwg.vw.km.common.type.BaseBigDecimal getTotalComponentElementProvision() {
		if (electricalComponentElementProvisionSum == null && mecanicsComponentElementProvisionSum == null) {
			return null;
		}
		if (electricalComponentElementProvisionSum == null) {
			return mecanicsComponentElementProvisionSum;
		}
		if (mecanicsComponentElementProvisionSum == null) {
			return electricalComponentElementProvisionSum;
		}
		return electricalComponentElementProvisionSum.add(mecanicsComponentElementProvisionSum);
	}

	public BaseBigDecimal getSumTotalComponentElementAndTotalComponentElementProvision() {
		if (getTotalComponentElementProvision() == null && getTotalComponentElement() == null) {
			return null;
		}
		if (getTotalComponentElementProvision() == null) {
			return getTotalComponentElement();
		}
		if (getTotalComponentElement() == null) {
			return getTotalComponentElementProvision();
		}
		return getTotalComponentElement().add(getTotalComponentElementProvision());
	}

	public BaseBigDecimal getElectricalComponentElementSum() {
		return electricalComponentElementSum;
	}

	public void setElectricalComponentElementSum(BaseBigDecimal electricalComponentElementSum) {
		this.electricalComponentElementSum = electricalComponentElementSum;
	}

	public BaseBigDecimal getElectricalComponentElementProvisionSum() {
		return electricalComponentElementProvisionSum;
	}

	public void setElectricalComponentElementProvisionSum(BaseBigDecimal electricalComponentElementProvisionSum) {
		this.electricalComponentElementProvisionSum = electricalComponentElementProvisionSum;
	}

	public BaseBigDecimal getMecanicsComponentElementSum() {
		return mecanicsComponentElementSum;
	}

	public void setMecanicsComponentElementSum(BaseBigDecimal mecanicsComponentElementSum) {
		this.mecanicsComponentElementSum = mecanicsComponentElementSum;
	}

	public BaseBigDecimal getMecanicsComponentElementProvisionSum() {
		return mecanicsComponentElementProvisionSum;
	}

	public void setMecanicsComponentElementProvisionSum(BaseBigDecimal mecanicsComponentElementProvisionSum) {
		this.mecanicsComponentElementProvisionSum = mecanicsComponentElementProvisionSum;
	}

	// end of extra code specified in the hbm.xml files

}
