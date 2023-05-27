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
import java.util.ArrayList;
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
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.EnumComponentClassModel;

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
 * @version $Revision: 1.1 $ $Date: 2015/12/11 07:59:15 $
 */
public class ImportImageXMLBean {

	private final Log log = LogManager.get().getLog(ImportImageXMLBean.class);

	private XMLImportService xmlImportService;

	public void setService(XMLImportService xmlImportService) {
		this.xmlImportService = xmlImportService;
	}

	private HashMap<String, String> pmImageMap = new HashMap<String, String>();

	private HashMap<String, String> pmReferenceFileMap = new HashMap<String, String>();

	private HashMap<String, EnumComponentClassModel> componentClassMap = new HashMap<String, EnumComponentClassModel>();

	private static int savedComponent = 0;

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
					// Fill PmImageTag
					readPmImageTag(doc);
					// Fill PmReferenceFileTag
					readPmReferenceFileTag(doc);

					// read componentNodes
					for (Entry<String, EnumComponentClassModel> entry : componentClassMap.entrySet()) {
						readComponentTag(doc, entry.getKey());
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
				Node nValue = (Node) nlList.item(0);
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

	/**
	 * 
	 * @param doc
	 */
	private void readPmImageTag(Document doc) {

		NodeList nList = doc.getElementsByTagName("PmImage");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			try {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement != null) {
						String pmImageNodeId = eElement.getAttribute("ExternalId");
						if (!pmImageMap.containsKey(pmImageNodeId)) {
							// case where the children is a single value: no item tag exist
							String fileValue = getTagValue("file", eElement).get(0);
							if (fileValue != null && !fileValue.equals("") && !fileValue.startsWith("\n")) {
								pmImageMap.put(pmImageNodeId, fileValue);
							}

						}
					}
				}
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
	}

	private void readPmReferenceFileTag(Document doc) {
		NodeList nList = doc.getElementsByTagName("PmReferenceFile");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			try {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					if (eElement != null) {
						String pmImageNodeId = eElement.getAttribute("ExternalId");
						if (!pmReferenceFileMap.containsKey(pmImageNodeId)) {
							// case where the children is a single value: no item tag exist
							String fileValue = getTagValue("fileName", eElement).get(0);
							if (fileValue != null && !fileValue.equals("") && !fileValue.startsWith("\n")) {
								pmReferenceFileMap.put(pmImageNodeId, fileValue);
							}

						}
					}
				}
			} catch (Exception e) {
				log.error("Exception", e);
			}
		}
	}

	public void readComponentTag(Document doc, String nodeName) {
		log.info("---------------------------- reading tag : " + nodeName);
		NodeList nList = doc.getElementsByTagName(nodeName);
		for (int temp = 0; temp < nList.getLength(); temp++) {
			// String componentNumber = "";
			// try {
			Node nNode = nList.item(temp);
			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
				Element eElement = (Element) nNode;
				if (eElement != null) {
					String externalId = eElement.getAttribute("ExternalId");
					String brandId = getTagValue("AEKBrand", eElement).get(0);
					log.info("Load component for: " + externalId + " and Brand: " + brandId);
					ComponentModel component = xmlImportService.getComponent(externalId, brandId);
					if (component != null) {
						// get tag image
						String image = getTagValue("image", eElement).get(0);
						if (!"".equals(image)) {
							String pmReferenceFile = pmImageMap.get(image);
							String imagePath = pmReferenceFileMap.get(pmReferenceFile);
							log.info("Component: " + externalId + " ID: " + component.getComponentId() + " imagePath:"
									+ imagePath);
							if (imagePath != null && imagePath.startsWith("#")) {
								imagePath = imagePath.replaceFirst("#", "");
							}
							component.setImagePath(imagePath);
							xmlImportService.saveComponent(component);
						} else {
							log.info("---component not found for: " + externalId);
						}
					}
				}
			}
		}

	}

	public void fillComponentClassMap() {
		for (EnumComponentClassModel eClassModel : xmlImportService.getEnumComponentClassList())
			componentClassMap.put(eClassModel.getComponentClass(), eClassModel);
	}

}
