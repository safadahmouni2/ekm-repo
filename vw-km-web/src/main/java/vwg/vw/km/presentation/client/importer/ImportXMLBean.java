/*
 * Copyright (c) VW All Rights Reserved.
 *
 * This SOURCE CODE FILE, which has been provided by VW Software as part
 * of an VW Software product for use ONLY by licensed users of the product,
 * includes CONFIDENTIAL and PROPRIETARY information of VW Software.
 *
 * USE OF THIS SOFTWARE IS GOVERNED BY THE TERMS AND CONDITIONS
 * OF THE LICENSE STATEMENT AND LIMITED WARRANTY FURNISHED WITH
 * THE PRODUCT.
 *
 * IN PARTICULAR, YOU WILL INDEMNIFY AND HOLD INPRISE, ITS RELATED
 * COMPANIES AND ITS SUPPLIERS, HARMLESS FROM AND AGAINST ANY CLAIMS
 * OR LIABILITIES ARISING OUT OF THE USE, REPRODUCTION, OR DISTRIBUTION
 * OF YOUR PROGRAMS, INCLUDING ANY CLAIMS OR LIABILITIES ARISING OUT OF
 * OR RESULTING FROM THE USE, MODIFICATION, OR DISTRIBUTION OF PROGRAMS
 * OR FILES CREATED FROM, BASED ON, AND/OR DERIVED FROM THIS SOURCE
 * CODE FILE.
 */

package vwg.vw.km.presentation.client.importer;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import vwg.vw.km.application.service.logic.XMLImportService;
import vwg.vw.km.application.util.xml.XmlFolder;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseBigDecimal;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.ChangeTypeValue;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ComponentValueModel;
import vwg.vw.km.integration.persistence.model.ComponentVersionModel;
import vwg.vw.km.integration.persistence.model.CostAttributeModel;
import vwg.vw.km.integration.persistence.model.EnumComponentClassModel;
import vwg.vw.km.integration.persistence.model.EnumComponentTypeModel;
import vwg.vw.km.integration.persistence.model.FolderModel;
import vwg.vw.km.integration.persistence.model.LibraryModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author saidi changed by $Author: saidi $
 * @version $Revision: 1.4 $ $Date: 2014/07/01 07:17:49 $
 */
public class ImportXMLBean {

	private final Log log = LogManager.get().getLog(ImportXMLBean.class);

	private XMLImportService xmlImportService;

	public void setService(XMLImportService xmlImportService) {
		this.xmlImportService = xmlImportService;
	}

	private HashMap<String, XmlFolder> foldersMap = new HashMap<String, XmlFolder>();

	private HashMap<String, EnumComponentTypeModel> componentTypeMap = new HashMap<String, EnumComponentTypeModel>();

	private HashMap<String, EnumComponentClassModel> componentClassMap = new HashMap<String, EnumComponentClassModel>();

	private HashMap<String, CostAttributeModel> costAttributeMap = new HashMap<String, CostAttributeModel>();

	private List<FolderModel> foldersToDelete = new ArrayList<FolderModel>();

	private static final String VW_RESBIB_ROOT_FOLDER = "PP-EMPLNEW-3-3-2004-14-47-51-259629-287867";

	private static final String VW_ERGBIB_ROOT_FOLDER = "PP-EMSDB_VIKAB-23-9-2009-11-0-54-15234313-25609020";

	private static final List<String> NOT_EXPORTED_NODES = Arrays.asList("PP-EMPLNEW-4-3-2004-12-0-15-259629-303644",
			"PP-EMPOWER2_EA-8-4-2008-8-29-30-1240883-57801786", "PP-EMSDB_PPKB-26-9-2012-12-47-20-217611653-306029877");

	private static int savedComponent = 0;

	public BrandModel getOwner() {
		return xmlImportService.getOwner("VW");
	}

	public LibraryModel getLib() {
		return xmlImportService.getLib(2L);
	}

	protected String getAbsoluteClassDirPath() {
		File file = new File(this.getClass().getResource("/uploadRef.properties").getPath());
		return file.getParentFile().getAbsolutePath();
	}

	/**
	 * @param args
	 */
	public String lunchImporter() {

		log.info("START IMPORT");

		log.info("INIT DB DONE");
		fillCostAttributeMap();
		fillComponentTypeMap();
		fillComponentClassMap();
		log.info("FILL MAPS");
		File importFolder = new File(
				getAbsoluteClassDirPath() + File.separator + "templates" + File.separator + "import");
		File[] listOfFiles = importFolder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".xml")) {
				log.info("File " + listOfFiles[i].getName());
				String fileName = importFolder + File.separator + listOfFiles[i].getName();
				try {
					File xmlFile = new File(fileName);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(xmlFile);
					doc.getDocumentElement().normalize();
					// read folderNodes
					readFolderTag(doc);
					removeUnusedFolders();
					updateFolders();
					// read componentNodes
					BrandModel owner = getOwner();
					LibraryModel lib = getLib();

					for (Entry<String, EnumComponentClassModel> entry : componentClassMap.entrySet()) {
						readComponentTag(doc, entry.getKey(), entry.getValue(), owner, lib);
					}

				} catch (Exception e) {
					e.printStackTrace();
					log.error(e);
				}
			}
		}
		log.info(savedComponent);
		return "BS Imported:" + savedComponent;

	}

	private static List<String> getTagValue(String sTag, Element element) {
		List<String> returnList = new ArrayList<String>();
		for (int j = 0; j < element.getElementsByTagName(sTag).getLength(); j++) {
			NodeList nlList = element.getElementsByTagName(sTag).item(j).getChildNodes();
			if (nlList.getLength() > 0) {
				Node nValue = nlList.item(0);
				if (nValue.getNodeValue() != null) {
					String nContent = nValue.getNodeValue().replace("\n", " ");
					if (!nContent.replace(" ", "").equals("")) {
						returnList.add(nContent);
					}
				}
			}
		}
		if (returnList.size() == 0) {
			returnList.add("");
		}
		return returnList;
	}

	private static List<String> getTagNumberValue(String sTag, Element element) {
		List<String> returnList = new ArrayList<String>();
		for (int j = 0; j < element.getElementsByTagName(sTag).getLength(); j++) {
			NodeList nlList = element.getElementsByTagName(sTag).item(j).getChildNodes();
			if (nlList.getLength() > 0) {
				Node nValue = nlList.item(0);
				if (nValue.getNodeValue() != null) {
					String nContent = nValue.getNodeValue().replace("\n", " ");
					if (!nContent.replace(" ", "").equals("")) {
						returnList.add(nContent);
					}
				}
			}
		}
		if (returnList.size() == 0) {
			returnList.add("0");
		}
		return returnList;
	}

	private String emptyToNullConverter(String value) {
		if (value == null || value.trim().length() == 0) {
			return null;
		}
		return value;
	}

	private BaseDateTime exportDateToBaseDateTimeConverter(String dateString) {
		BaseDateTime dateTime = null;
		if (dateString == null || dateString.trim().length() == 0 || "1/1/2000 0:00:00".equals(dateString)) {
			return null;
		}
		if (dateString != null) {
			SimpleDateFormat df = new SimpleDateFormat("d/M/yyyy H:mm:ss");
			SimpleDateFormat df2 = new SimpleDateFormat("dd.MM.yyyy");
			Date date = null;
			try {
				if (dateString.length() == 10) {
					date = df2.parse(dateString);
				} else {
					date = df.parse(dateString);
				}
			} catch (ParseException e) {
				log.info("Enable to parse date: " + dateString);
				return null;
			}
			dateTime = new BaseDateTime(date);
		}
		return dateTime;
	}

	public void readFolderTag(Document doc) {

		NodeList nList = doc.getElementsByTagName("PmResourceLibrary");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			try {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement != null) {
						String folderNodeId = eElement.getAttribute("ExternalId");
						if (!foldersMap.containsKey(folderNodeId)) {
							XmlFolder xmlFolder = new XmlFolder();
							FolderModel e = new FolderModel();
							e.setExternalId(folderNodeId);
							if (VW_RESBIB_ROOT_FOLDER.equals(folderNodeId)
									|| VW_ERGBIB_ROOT_FOLDER.equals(folderNodeId)) {
								e = xmlImportService.getFolder(7L);
							} else {
								FolderModel dbFolder = null;
								if (folderNodeId != null) {
									dbFolder = xmlImportService.getFolderByExternalID(folderNodeId);
								}
								if (dbFolder != null) {
									log.info("--------------------- : Same External Id" + dbFolder.getDesignation());
									e = dbFolder;
									xmlFolder.setAlreadyExist(Boolean.TRUE);
								} else {
									e.setDesignation(getTagValue("name", eElement).get(0).replace("?", "€"));
									xmlImportService.saveImportedFolder(e, true);
								}
							}

							List<String> folderChilds = new ArrayList<String>();
							// case where the children is a single value: no item tag exist
							String childrenValue = getTagValue("children", eElement).get(0);
							if (childrenValue != null && !childrenValue.equals("") && !childrenValue.startsWith("\n")) {
								folderChilds.add(childrenValue);
							}
							// case where the children is a list : list of tag item exist
							else if (childrenValue != null) {
								Node childrenNode = eElement.getElementsByTagName("children").item(0);
								if (childrenNode.getNodeType() == Node.ELEMENT_NODE) {
									Element eChildrenElement = (Element) childrenNode;
									for (String componentId : getTagValue("item", eChildrenElement)) {
										folderChilds.add(componentId);
									}
								}
							}
							xmlFolder.setFolderModel(e);
							xmlFolder.setFolderChilds(folderChilds);
							foldersMap.put(folderNodeId, xmlFolder);
						}
					}
				}
			} catch (Exception e) {
				log.error("Exception", e);
			}

		}
	}

	public void updateFolders() {
		for (Entry<String, XmlFolder> entry : foldersMap.entrySet()) {
			XmlFolder xmlFolder = entry.getValue();
			for (int i = 0; i < xmlFolder.getFolderChilds().size(); i++) {
				String xmlFolderKey = xmlFolder.getFolderChilds().get(i);
				if (foldersMap.containsKey(xmlFolderKey)) {
					XmlFolder childXmlFolder = foldersMap.get(xmlFolderKey);
					FolderModel fModel = childXmlFolder.getFolderModel();
					if (fModel.getFolder() == null) {
						fModel.setFolder(xmlFolder.getFolderModel());
					}
					try {
						if (!childXmlFolder.isAlreadyExist()) {
							xmlImportService.saveImportedFolder(fModel, false);
						}
					} catch (Exception e) {
						log.error("error update folder with nodeId=" + xmlFolderKey, e);
					}
				}
			}
		}
	}

	public void removeUnusedFolders() {
		for (String id : NOT_EXPORTED_NODES) {
			fillUnusedFolder(id);
		}
		for (FolderModel fModel : foldersToDelete) {
			foldersMap.remove(fModel.getExternalFolderId());
			xmlImportService.deleteFolder(fModel.getFolderId(), 2L);
		}
	}

	public void fillUnusedFolder(String externalId) {
		if (foldersMap.containsKey(externalId)) {
			XmlFolder xmlFolder = foldersMap.get(externalId);
			foldersToDelete.add(xmlFolder.getFolderModel());
		}
	}

	public void readComponentTag(Document doc, String nodeName, EnumComponentClassModel enumComponentClassModel,
			BrandModel owner, LibraryModel lib) {
		log.info("---------------------------- reading tag : " + nodeName);
		NodeList nList = doc.getElementsByTagName(nodeName);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			// String componentNumber = "";
			// try {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if (eElement != null) {
					String componentNodeId = eElement.getAttribute("ExternalId");
					FolderModel folderModel = getComponentFolder(componentNodeId);
					if (!getTagValue("Pos", eElement).get(0).equals("") && folderModel != null) {
						List<ComponentStandChangeModel> changesList = new ArrayList<ComponentStandChangeModel>();
						ComponentStandChangeModel changeEntry;
						String user = null;
						String date = null;
						String componentNumber = getTagValue("Pos", eElement).get(0);
						ComponentVersionModel componentVersionModel = new ComponentVersionModel();
						ComponentModel componentModel = new ComponentModel();
						componentModel.setExternalId(componentNodeId);
						componentModel.setComponentNumber(componentNumber);
						componentModel.setDesignation(getTagValue("name", eElement).get(0).replace("?", "€"));
						componentModel.setInactive(Boolean.FALSE);
						componentModel.setManifacturerRefId(1L);
						componentModel.setCreationDate(BaseDateTime.getCurrentDateTime());
						componentModel.setOwner(owner);
						componentModel.setLibrary(lib);
						componentModel.setFolder(folderModel);
						componentVersionModel.setComponent(componentModel);
						String withProvision = getTagValue("bBeistellungenValid", eElement).get(0);
						if (withProvision.equals("1")) {
							componentVersionModel.setWithProvision(Boolean.TRUE);
						} else {
							componentVersionModel.setWithProvision(Boolean.FALSE);
						}

						componentVersionModel.setEnumComponentClass(enumComponentClassModel);
						componentVersionModel.setDescription(getTagValue("comment", eElement).get(0));
						componentVersionModel
								.setMechanicalExecution(getTagValue("Inhalt_Mech_Ausfuerung", eElement).get(0));
						componentVersionModel
								.setMechanicalConstruction(getTagValue("Inhalt_Mech_Konstruktion", eElement).get(0));
						componentVersionModel.setElectric(getTagValue("Inhalt_Elek", eElement).get(0));
						user = emptyToNullConverter(getTagValue("AKEInhaltVLB", eElement).get(0));
						date = emptyToNullConverter(getTagValue("AKEInhaltVLD", eElement).get(0));
						if (user != null && date != null) {
							changeEntry = generateChangeEntry("Leistungsinhalt Elektrik",
									componentVersionModel.getElectric(), user, Boolean.TRUE, ChangeType.ELECTRIC_VALUE,
									date, null);
							changesList.add(changeEntry);
						}

						String componentType = getTagValue("Objekttyp", eElement).get(0);
						if (componentType == null || "".equals(componentType.trim())) {
							componentType = "Vorrichtungstechnik";
						}
						EnumComponentTypeModel objectType = componentTypeMap.get(componentType);
						if (objectType == null) {
							// throw new Exception("Objekttyp : " + componentType + " is not defined in data
							// base");
							return;
						}
						componentVersionModel.setEnumComponentType(objectType);

						// start added attribute by changes
						componentVersionModel
								.setCatalogNumber(emptyToNullConverter(getTagValue("catalogNumber", eElement).get(0)));
						componentVersionModel
								.setManufacturer(emptyToNullConverter(getTagValue("Hersteller", eElement).get(0)));
						componentVersionModel.setMecanicPurchasedPartPriceLevelNotice(
								emptyToNullConverter(getTagValue("NKMKaufteileInfo", eElement).get(0)));
						user = emptyToNullConverter(getTagValue("NKEKaufteileInfoB", eElement).get(0));
						date = emptyToNullConverter(getTagValue("NKEKaufteileInfoD", eElement).get(0));
						if (user != null && date != null) {
							changeEntry = generateChangeEntry("M - Preisstand Bemerkung",
									componentVersionModel.getMecanicPurchasedPartPriceLevelNotice(), user, Boolean.TRUE,
									ChangeType.M_NOTICE_VALUE, date, null);
							changesList.add(changeEntry);
						}
						BaseDateTime mecanicPPPLDate = exportDateToBaseDateTimeConverter(
								getTagValue("NKMKaufteileDatum", eElement).get(0));
						mecanicPPPLDate = (mecanicPPPLDate != null && !mecanicPPPLDate.isNull())
								? new BaseDateTime(mecanicPPPLDate.getDateSeparatedPt())
								: mecanicPPPLDate;
						componentVersionModel.setMecanicPurchasedPartPriceLevelDate(mecanicPPPLDate);
						user = emptyToNullConverter(getTagValue("NKMKaufteileDatumB", eElement).get(0));
						date = emptyToNullConverter(getTagValue("NKMKaufteileDatumD", eElement).get(0));
						if (user != null && date != null) {
							changeEntry = generateChangeEntry("M - Preisstand Datum",
									(mecanicPPPLDate != null && !mecanicPPPLDate.isNull())
											? mecanicPPPLDate.getDateSeparatedPt()
											: null,
									user, Boolean.FALSE, ChangeType.M_DATE_VALUE, date, null);
							changesList.add(changeEntry);
						}
						componentVersionModel.setElectricPurchasedPartPriceLevelNotice(
								emptyToNullConverter(getTagValue("NKEKaufteileInfo", eElement).get(0)));
						user = emptyToNullConverter(getTagValue("NKEKaufteileInfoB", eElement).get(0));
						date = emptyToNullConverter(getTagValue("NKEKaufteileInfoD", eElement).get(0));
						if (user != null && date != null) {
							changeEntry = generateChangeEntry("E - Preisstand Bemerkung",
									componentVersionModel.getElectricPurchasedPartPriceLevelNotice(), user,
									Boolean.TRUE, ChangeType.E_NOTICE_VALUE, date, null);
							changesList.add(changeEntry);
						}

						BaseDateTime electricPPPLDate = exportDateToBaseDateTimeConverter(
								getTagValue("NKEKaufteileDatum", eElement).get(0));
						electricPPPLDate = (electricPPPLDate != null && !electricPPPLDate.isNull())
								? new BaseDateTime(electricPPPLDate.getDateSeparatedPt())
								: electricPPPLDate;
						componentVersionModel.setElectricPurchasedPartPriceLevelDate(electricPPPLDate);
						// end added attribute by changes
						// export xxxxB and xxxxD markup
						user = emptyToNullConverter(getTagValue("NKEKaufteileDatumB", eElement).get(0));
						date = emptyToNullConverter(getTagValue("NKEKaufteileDatumD", eElement).get(0));
						if (user != null && date != null) {
							changeEntry = generateChangeEntry("E - Preisstand Datum",
									(electricPPPLDate != null && !electricPPPLDate.isNull())
											? electricPPPLDate.getDateSeparatedPt()
											: null,
									user, Boolean.FALSE, ChangeType.E_DATE_VALUE, date, null);
							changesList.add(changeEntry);
						}

						boolean isBackup = !withProvision.equals("1");
						List<ComponentValueModel> attributes = readCostAttributes(eElement, componentVersionModel,
								isBackup, changesList);
						try {
							xmlImportService.saveComponentVersion(componentVersionModel, attributes, changesList);
							savedComponent++;
						} catch (Exception e) {
							e.printStackTrace();
							log.info("Same BS Exist: " + componentModel.getNumberAndDesignation());
						}
					} else {
						log.info("---component number is null for: " + componentNodeId);
					}
				}
			}
		}

	}

	private ComponentStandChangeModel generateChangeEntry(String change, String newValue, String userFullName,
			boolean longValue, ChangeType type, String date, String unit) {
		ComponentStandChangeModel changeEntry = new ComponentStandChangeModel();
		changeEntry.setChangeType(ChangeTypeValue.NEW.value());
		changeEntry.setChange(change);
		changeEntry.setOldValue(null);
		changeEntry.setNewValue(newValue);
		changeEntry.setDate(exportDateToBaseDateTimeConverter(date));
		changeEntry.setUserFullName(userFullName);
		changeEntry.setLongValue(longValue);
		changeEntry.setType(type.value());
		changeEntry.setUnit(unit);
		return changeEntry;
	}

	public FolderModel getComponentFolder(String componentNodeId) {
		for (Entry<String, XmlFolder> entry : foldersMap.entrySet()) {
			XmlFolder xmlFolder = entry.getValue();
			for (int i = 0; i < xmlFolder.getFolderChilds().size(); i++) {
				String xmlFolderKey = xmlFolder.getFolderChilds().get(i);
				if (xmlFolderKey.equals(componentNodeId)) {
					return xmlFolder.getFolderModel();
				}
			}
		}
		return null;
	}

	public void fillCostAttributeMap() {
		costAttributeMap.put("NKMPlanMethPlanS", xmlImportService.getCostAttribute(4L));
		costAttributeMap.put("NKMRobSimS", xmlImportService.getCostAttribute(5L));
		costAttributeMap.put("NKMRobUntersS", xmlImportService.getCostAttribute(6L));
		costAttributeMap.put("NKMCADKonS", xmlImportService.getCostAttribute(7L));

		costAttributeMap.put("NKMRobProgS", xmlImportService.getCostAttribute(9L));

		costAttributeMap.put("NKMCADCAMProg2D3DS", xmlImportService.getCostAttribute(10L));
		costAttributeMap.put("NKMBankarbeitS", xmlImportService.getCostAttribute(11L));
		costAttributeMap.put("NKMKleineMaschineS", xmlImportService.getCostAttribute(12L));
		costAttributeMap.put("NKMMittlereMaschineS", xmlImportService.getCostAttribute(13L));
		costAttributeMap.put("NKMGrosseMaschineS", xmlImportService.getCostAttribute(14L));
		costAttributeMap.put("NKMMessmaschineS", xmlImportService.getCostAttribute(15L));
		costAttributeMap.put("NKMFremdanfertigung", xmlImportService.getCostAttribute(16L));
		costAttributeMap.put("NKMBaustahlKg", xmlImportService.getCostAttribute(17L));
		costAttributeMap.put("NKMWkzgstNEMetalleKg", xmlImportService.getCostAttribute(18L));
		costAttributeMap.put("NKMKaufteile", xmlImportService.getCostAttribute(19L));

		costAttributeMap.put("NKMVerpFr", xmlImportService.getCostAttribute(20L));
		costAttributeMap.put("AKMKaufteilePotBeist", xmlImportService.getCostAttribute(21L));
		costAttributeMap.put("AKMKaufteilePotBeistBackup", xmlImportService.getCostAttribute(21L));
		costAttributeMap.put("NKMMonEinarbeitInbANS", xmlImportService.getCostAttribute(22L));
		costAttributeMap.put("NKMMonEinarbeitInbS", xmlImportService.getCostAttribute(23L));
		costAttributeMap.put("NKMSaZS", xmlImportService.getCostAttribute(24L));
		costAttributeMap.put("NKMSoFZS", xmlImportService.getCostAttribute(25L));

		costAttributeMap.put("NKEPlanMethPlanS", xmlImportService.getCostAttribute(29L));
		costAttributeMap.put("NKEHWKonS", xmlImportService.getCostAttribute(30L));
		costAttributeMap.put("NKESWKonLogikS", xmlImportService.getCostAttribute(31L));
		costAttributeMap.put("NKEInstPerS", xmlImportService.getCostAttribute(32L));
		costAttributeMap.put("NKEInbS", xmlImportService.getCostAttribute(33L));
		costAttributeMap.put("NKEInbSSa", xmlImportService.getCostAttribute(34L));
		costAttributeMap.put("NKEInbSSoF", xmlImportService.getCostAttribute(35L));
		costAttributeMap.put("NKERobProgOnLineS", xmlImportService.getCostAttribute(36L));
		costAttributeMap.put("NKEInstMat", xmlImportService.getCostAttribute(37L));
		// E - Kaufteil -> in Excel nicht vorhanden but has related tags to be calculated
		costAttributeMap.put("E-Kaufteil", xmlImportService.getCostAttribute(38L));
		costAttributeMap.put("AKEKaufteilePotBeist", xmlImportService.getCostAttribute(39L));
		costAttributeMap.put("AKEKaufteilePotBeistBackup", xmlImportService.getCostAttribute(39L));

	}

	public void fillComponentTypeMap() {
		for (EnumComponentTypeModel eTypeModel : xmlImportService.getEnumComponentTypeList()) {
			componentTypeMap.put(eTypeModel.getComponentType(), eTypeModel);
		}
	}

	public void fillComponentClassMap() {
		for (EnumComponentClassModel eClassModel : xmlImportService.getEnumComponentClassList()) {
			componentClassMap.put(eClassModel.getComponentClass(), eClassModel);
		}
	}

	public List<ComponentValueModel> readCostAttributes(Element element, ComponentVersionModel componentVersionModel,
			boolean isBackup, List<ComponentStandChangeModel> changesList) {
		List<ComponentValueModel> componentValueList = new ArrayList<ComponentValueModel>();
		ComponentValueModel componentValue = getNewComponentValue("NKMWkzgstNEMetalleKg", element,
				componentVersionModel);
		componentValueList.add(componentValue);

		String user = null;
		String date = null;
		user = emptyToNullConverter(getTagValue("NKMWkzgstNEMetalleB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMWkzgstNEMetalleD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMSoFZS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMSoFZB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMSoFZD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMVerpFr", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMSoFZB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMSoFZD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMSaZS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMSaZB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMSaZD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMRobUntersS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMRobUntersB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMRobUntersD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMRobSimS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMRobSimB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMRobSimD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMRobProgS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMRobProgB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMRobProgD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMMonEinarbeitInbS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMMonEinarbeitInbB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMMonEinarbeitInbD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMMonEinarbeitInbANS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMMonEinarbeitInbANB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMMonEinarbeitInbAND", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMMittlereMaschineS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMMittlereMaschineB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMMittlereMaschineD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMMessmaschineS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMMessmaschineB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMMessmaschineD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMKleineMaschineS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMKleineMaschineB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMKleineMaschineD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMGrosseMaschineS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMGrosseMaschineB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMGrosseMaschineD", element).get(0));
		String user1 = emptyToNullConverter(getTagValue("NKMSoMaB", element).get(0));
		String date1 = emptyToNullConverter(getTagValue("NKMSoMaD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMFremdanfertigung", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMFremdanfertigungB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMFremdanfertigungD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMCADKonS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMCADKonB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMCADKonD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKMCADDetailierungB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKMCADDetailierungD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKMDetailKonKonvB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKMDetailKonKonvD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKMKonKonvB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKMKonKonvD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMCADCAMProg2D3DS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMCADCAMProg2D3DB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMCADCAMProg2D3DD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMBaustahlKg", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMBaustahlB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMBaustahlD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMBankarbeitS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMBankarbeitB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMBankarbeitD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKERobProgOnLineS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKERobProgOnLineB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKERobProgOnLineD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKEInstPerS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEInstPerB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEInstPerD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKEAendAnpassArbeitenB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEAendAnpassArbeitenD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEBedienpulteB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEBedienpulteD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEDemontagenB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEDemontagenD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEElSTSKftgB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEElSTSKftgD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEVerfuegbarkeitB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEVerfuegbarkeitD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEVerlUeberlMonB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEVerlUeberlMonD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKEInstMat", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEInstMatB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEInstMatD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKEUeberleitungenMatB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEUeberleitungenMatD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKEInbS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEInbB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEInbD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKEProdbegleitungB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEProdbegleitungD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKEProdbegleitungNKB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKEProdbegleitungNKD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		user1 = emptyToNullConverter(getTagValue("NKESchulungEinweisungB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKESchulungEinweisungD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKEHWKonS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEHWKonB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEHWKonD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKESWKonLogikS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKESWKonLogikB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKESWKonLogikD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKESWKonKopplungB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKESWKonKopplungD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKMPlanMethPlanS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMPlanMethPlanB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMPlanMethPlanD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		if (isBackup) {
			componentValue = getNewComponentValue("AKMKaufteilePotBeistBackup", element, componentVersionModel);
			componentValueList.add(componentValue);
			user = emptyToNullConverter(getTagValue("AKMKaufteilePotBeistB", element).get(0));
			date = emptyToNullConverter(getTagValue("AKMKaufteilePotBeistD", element).get(0));
			generateAttributeChange(user, date, componentValue, changesList);

			componentValue = getNewComponentValue("AKEKaufteilePotBeistBackup", element, componentVersionModel);
			componentValueList.add(componentValue);
			user = emptyToNullConverter(getTagValue("AKEKaufteilePotBeistB", element).get(0));
			date = emptyToNullConverter(getTagValue("AKEKaufteilePotBeistD", element).get(0));
			generateAttributeChange(user, date, componentValue, changesList);
		} else {
			componentValue = getNewComponentValue("AKMKaufteilePotBeist", element, componentVersionModel);
			componentValueList.add(componentValue);
			user = emptyToNullConverter(getTagValue("AKMKaufteilePotBeistB", element).get(0));
			date = emptyToNullConverter(getTagValue("AKMKaufteilePotBeistD", element).get(0));
			generateAttributeChange(user, date, componentValue, changesList);

			componentValue = getNewComponentValue("AKEKaufteilePotBeist", element, componentVersionModel);
			componentValueList.add(componentValue);
			user = emptyToNullConverter(getTagValue("AKEKaufteilePotBeistB", element).get(0));
			date = emptyToNullConverter(getTagValue("AKEKaufteilePotBeistD", element).get(0));
			generateAttributeChange(user, date, componentValue, changesList);
		}

		componentValue = getNewComponentValue("NKEInbSSa", element, componentVersionModel);
		componentValueList.add(componentValue);

		componentValue = getNewComponentValue("NKEInbSSoF", element, componentVersionModel);
		componentValueList.add(componentValue);

		componentValue = getNewComponentValue("NKMKaufteile", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKMKaufteileB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKMKaufteileD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("NKEPlanMethPlanS", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEPlanMethPlanB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEPlanMethPlanD", element).get(0));
		generateAttributeChange(user, date, componentValue, changesList);

		componentValue = getNewComponentValue("E-Kaufteil", element, componentVersionModel);
		componentValueList.add(componentValue);
		user = emptyToNullConverter(getTagValue("NKEBedienpulteMatB", element).get(0));
		date = emptyToNullConverter(getTagValue("NKEBedienpulteMatD", element).get(0));
		user1 = emptyToNullConverter(getTagValue("NKESchaltschraenkeMatB", element).get(0));
		date1 = emptyToNullConverter(getTagValue("NKESchaltschraenkeMatD", element).get(0));
		if (isDateBefore(date, date1)) {
			date = date1;
			user = user1;
		}
		generateAttributeChange(user, date, componentValue, changesList);

		return componentValueList;
	}

	private boolean isDateBefore(String date, String date1) {
		BaseDateTime dateConverted = exportDateToBaseDateTimeConverter(date);
		BaseDateTime date1Converted = exportDateToBaseDateTimeConverter(date);
		if (((date == null || dateConverted == null) && (date1 != null && date1Converted != null))
				|| (date != null && date1 != null && dateConverted != null && date1Converted != null
						&& dateConverted.getDate().before(date1Converted.getDate()))) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	private void generateAttributeChange(String user, String date, ComponentValueModel componentValue,
			List<ComponentStandChangeModel> changesList) {
		if (user != null && date != null) {
			BaseBigDecimal newValue = new BaseBigDecimal("0");
			CostAttributeModel costAttribute = componentValue.getCostAttribute();
			if (costAttribute.getPricePerUnit()) {
				if (componentValue.getNumber() != null && newValue.compareTo(componentValue.getNumber()) < 0) {
					newValue = componentValue.getNumber();
				}
			} else {
				if (componentValue.getAmount() != null && newValue.compareTo(componentValue.getAmount()) < 0) {
					newValue = componentValue.getAmount();
				}
			}
			ComponentStandChangeModel changeEntry = generateChangeEntry(
					componentValue.getCostAttribute().getDesignation(), newValue.getStringFormatted(), user,
					Boolean.FALSE, ChangeType.ATTRIBUTE_VALUE, date, componentValue.getCostAttribute().getUnit());
			changesList.add(changeEntry);
		}
	}

	public ComponentValueModel getNewComponentValue(String nodeValue, Element element,
			ComponentVersionModel componentVersionModel) {
		ComponentValueModel componentValueModel = new ComponentValueModel();
		CostAttributeModel costAttributeModel = costAttributeMap.get(nodeValue);
		componentValueModel.setCostAttribute(costAttributeModel);
		componentValueModel.setComponentVersion(componentVersionModel);
		BaseBigDecimal value = new BaseBigDecimal("0");
		if (!nodeValue.equals("E-Kaufteil")) {
			value = new BaseBigDecimal(getTagNumberValue(nodeValue, element).get(0));
		}

		if (nodeValue.equals("NKEInstPerS")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEAendAnpassArbeitenS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEBedienpulteS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEDemontagenS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEElSTSKftgS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEVerfuegbarkeitS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEVerlUeberlMonS", element).get(0)));
		}
		if (nodeValue.equals("NKEInbS")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEProdbegleitungS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEProdbegleitungNKS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKESchulungEinweisungS", element).get(0)));
		}
		if (nodeValue.equals("NKESWKonLogikS")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKESWKonKopplungS", element).get(0)));
		}
		if (nodeValue.equals("NKEInstMat")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEUeberleitungenMat", element).get(0)));
		}
		if (nodeValue.equals("NKMGrosseMaschineS")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKMSoMaS", element).get(0)));
		}
		if (nodeValue.equals("NKMCADKonS")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKMCADDetailierungS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKMDetailKonKonvS", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKMKonKonvS", element).get(0)));
		}
		if (nodeValue.equals("E-Kaufteil")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEBedienpulteMat", element).get(0)));
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKESchaltschraenkeMat", element).get(0)));
		}
		if (nodeValue.equals("NKEInbSSoF")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEInstPerSSoF", element).get(0)));
		}
		if (nodeValue.equals("NKEInbSSa")) {
			value = value.add(new BaseBigDecimal(getTagNumberValue("NKEInstPerSSa", element).get(0)));
		}
		if (costAttributeModel.getPricePerUnit()) {
			componentValueModel.setNumber(value);
		} else {
			componentValueModel.setAmount(value);
		}
		if (nodeValue.equals("AKEKaufteilePotBeist") || nodeValue.equals("AKEKaufteilePotBeistBackup")) {
			componentValueModel
					.setDescription(emptyToNullConverter(getTagValue("EL_PreisstandBeistellung", element).get(0)));
		}
		if (nodeValue.equals("AKMKaufteilePotBeist") || nodeValue.equals("AKMKaufteilePotBeistBackup")) {
			componentValueModel
					.setDescription(emptyToNullConverter(getTagValue("ME_PreisstandBeistellung", element).get(0)));
		}
		return componentValueModel;
	}
}
