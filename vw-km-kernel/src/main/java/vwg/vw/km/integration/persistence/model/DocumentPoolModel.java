package vwg.vw.km.integration.persistence.model;
// Generated Jul 15, 2021 12:03:58 PM by Hibernate Tools 3.2.0.b9

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import vwg.vw.km.common.type.BaseDateTime;

/**
 * @hibernate.class table="T_DOKUMENTPOOL"
 * 
 */

@Entity
@Table(name = "T_DOKUMENTPOOL")
public class DocumentPoolModel extends vwg.vw.km.integration.persistence.model.base.BaseModel
		implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	
	/**
	 * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_document_pool")
	@SequenceGenerator(name = "seq_document_pool", sequenceName = "SEQ_DOCUMENT_POOL", allocationSize = 1)
	private Long documentPoolId;
	/**
	 * @hibernate.property column="PFAD" length="255" not-null="true"
	 * 
	 */

	@Column(name = "PFAD", length = 255, nullable = false)
	private String path;

	@Lob
	@Column(name = "CONTENT", columnDefinition = "BLOB", nullable = false)
	private byte[] content;
	/**
	 * @hibernate.property column="BESCHREIBUNG" length="255"
	 * 
	 */
	@Column(name = "BESCHREIBUNG", length = 255)
	private String description;

	@Column(name = "DATEI_TYP", length = 100)
	private String fileType;
	/**
	 * @hibernate.property column="ERSTELLT_AM" length="7" not-null="true"
	 * 
	 */
	@Column(name = "ERSTELLT_AM", length = 8)
	private BaseDateTime creationDate;
	/**
	 * @hibernate.property column="ERSTELLER_REF_ID" length="19"
	 * 
	 */

	@Column(name = "ERSTELLER_REF_ID", length = 19)
	private Long creatorRefId;

	public DocumentPoolModel() {
	}

	public DocumentPoolModel(String path, byte[] content) {
		this.path = path;
		this.content = content;
	}

	public DocumentPoolModel(String path, byte[] content, String description, String fileType,
			BaseDateTime creationDate, Long creatorRefId) {
		this.path = path;
		this.content = content;
		this.description = description;
		this.fileType = fileType;
		this.creationDate = creationDate;
		this.creatorRefId = creatorRefId;
	}

	/**
	 * * @hibernate.id generator-class="assigned" type="java.lang.Long" column="ID"
	 * 
	 */
	public Long getDocumentPoolId() {
		return this.documentPoolId;
	}

	private void setDocumentPoolId(Long documentPoolId) {
		this.documentPoolId = documentPoolId;
	}

	/**
	 * * @hibernate.property column="PFAD" length="255" not-null="true"
	 * 
	 */
	public String getPath() {
		return this.path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getContent() {
		return this.content;
	}

	public void setContent(byte[] content) {
		this.content = content;
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

	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	/**
	 * * @hibernate.property column="ERSTELLT_AM" length="7" not-null="true"
	 * 
	 */
	public BaseDateTime getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(BaseDateTime creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * * @hibernate.property column="ERSTELLER_REF_ID" length="19"
	 * 
	 */
	public Long getCreatorRefId() {
		return this.creatorRefId;
	}

	public void setCreatorRefId(Long creatorRefId) {
		this.creatorRefId = creatorRefId;
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
		buffer.append("documentPoolId").append("='").append(getDocumentPoolId()).append("' ");
		buffer.append("path").append("='").append(getPath()).append("' ");
		buffer.append("description").append("='").append(getDescription()).append("' ");
		buffer.append("fileType").append("='").append(getFileType()).append("' ");
		buffer.append("creatorRefId").append("='").append(getCreatorRefId()).append("' ");
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
		if (!(other instanceof DocumentPoolModel)) {
			return false;
		}
		DocumentPoolModel castOther = (DocumentPoolModel) other;

		return ((this.getDocumentPoolId() == castOther.getDocumentPoolId())
				|| (this.getDocumentPoolId() != null && castOther.getDocumentPoolId() != null
						&& this.getDocumentPoolId().equals(castOther.getDocumentPoolId())));
	}

	@Override
	public int hashCode() {
		int result = 17;

		result = 37 * result + (getDocumentPoolId() == null ? 0 : this.getDocumentPoolId().hashCode());

		return result;
	}

}
