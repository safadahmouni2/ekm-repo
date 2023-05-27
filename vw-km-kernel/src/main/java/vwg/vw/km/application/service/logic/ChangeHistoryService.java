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
package vwg.vw.km.application.service.logic;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.util.CollectionUtils;

import vwg.vw.km.application.implementation.BrandManager;
import vwg.vw.km.application.implementation.ComponentManager;
import vwg.vw.km.application.implementation.ComponentStandChangeManager;
import vwg.vw.km.application.implementation.ElementManager;
import vwg.vw.km.application.implementation.ElementVersionChangeManager;
import vwg.vw.km.application.implementation.EnumObjectTypeManager;
import vwg.vw.km.application.implementation.EnumStatusManager;
import vwg.vw.km.application.implementation.FolderManager;
import vwg.vw.km.application.service.base.BaseService;
import vwg.vw.km.application.service.dto.ChangeHistoryDTO;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.ChangeType;
import vwg.vw.km.common.type.SearchHistoryObject;
import vwg.vw.km.integration.persistence.model.BrandModel;
import vwg.vw.km.integration.persistence.model.ComponentModel;
import vwg.vw.km.integration.persistence.model.ComponentStandChangeModel;
import vwg.vw.km.integration.persistence.model.ElementModel;
import vwg.vw.km.integration.persistence.model.ElementVersionChangeModel;

/**
 * <p>
 * Title: EKM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * .
 * 
 * @author boubakers changed by $Author: zouhair $
 * @version $Revision: 1.51 $ $Date: 2019/01/22 14:52:52 $
 */
public class ChangeHistoryService extends BaseService<ChangeHistoryDTO> {

	private final Log log = LogManager.get().getLog(ChangeHistoryService.class);

	private static String JA = "ja";

	private static String NEIN = "nein";

	private ElementVersionChangeManager elementVersionChangeManager;

	private ComponentStandChangeManager componentStandChangeManager;

	private ElementManager elementManager;

	private ComponentManager componentManager;

	private BrandManager brandManager;

	private FolderManager folderManager;

	public void search(ChangeHistoryDTO searchDto) {
		SearchHistoryObject searchObject = searchDto.getSearchObject();
		if (log.isInfoEnabled()) {
			log.info("### Search changes History criteria : ");
			log.info("From Date: " + new BaseDateTime(searchObject.getFromDate()) + " ==> To Date: "
					+ new BaseDateTime(searchObject.getToDate()));
			log.info("Besitzer: " + searchObject.getOwner() + " Nutzer: "
					+ ((searchObject.getUsersList() != null) ? searchObject.getUsersList().toString() : null));
			log.info("Wechsel genutzter Stand: " + searchObject.getExchangeUnusedState());
			log.info("Deaktivierung : " + searchObject.getDesactivaton());
			log.info("Aktivierung : " + searchObject.getActivation());
			log.info("In Bearbeitung : " + searchObject.getInProgress());
			log.info("Neuer Stand : " + searchObject.getNewState());
			log.info("Neu : " + searchObject.getNewOne());
			log.info("Status included are : " + searchObject.getStatusList());
			log.info("Changes Types are : " + searchObject.getChangeTypes());
		}
		List<ElementVersionChangeModel> elementsChanges = null;
		List<ComponentStandChangeModel> componentsChanges = null;
		// remove old results
		searchDto.clearOldResultList();
		Long id;
		// only components
		if (searchObject.searchJustComponent()) {
			if (log.isInfoEnabled()) {
				log.info(" ### Search changes History of Components");
			}
			componentsChanges = componentStandChangeManager.getChangesByCriteria(searchObject);
		} else {
			// only elements
			if (searchObject.searchJustElement()) {
				if (log.isInfoEnabled()) {
					log.info(" ### Search changes History of Elements");
				}
				elementsChanges = elementVersionChangeManager.getChangesByCriteria(searchObject);
			} else {
				if (log.isInfoEnabled()) {
					log.info(" ### Search changes History of Elements and Components");
				}
				componentsChanges = componentStandChangeManager.getChangesByCriteria(searchObject);
				elementsChanges = elementVersionChangeManager.getChangesByCriteria(searchObject);
			}
		}
		if (!CollectionUtils.isEmpty(componentsChanges)) {
			searchDto.setComponentChangesResults(componentsChanges);
		}
		if (!CollectionUtils.isEmpty(elementsChanges)) {
			searchDto.setElementChangesResults(elementsChanges);
		}
		if (!CollectionUtils.isEmpty(searchDto.getElementChangesResults())) {
			for (ElementVersionChangeModel elementVersionChangeModel : searchDto.getElementChangesResults()) {
				id = elementVersionChangeModel.getElementVersion().getElement().getElementId();
				if (!searchDto.getSortedElementId().contains(id)) {
					searchDto.getSortedElementId().add(id);
				}
			}
		}
		if (!CollectionUtils.isEmpty(searchDto.getComponentChangesResults())) {
			for (ComponentStandChangeModel componentStandChangeModel : searchDto.getComponentChangesResults()) {
				id = componentStandChangeModel.getComponentStand().getComponentVersion().getComponent()
						.getComponentId();
				if (!searchDto.getSortedComponentId().contains(id)) {
					searchDto.getSortedComponentId().add(id);
				}
			}
		}

		/**
		 * PTS requirement 22211:ABA :Benachrichtigungsfunktion
		 */
		if (searchDto.getComponentChangesResults().isEmpty() && searchDto.getElementChangesResults().isEmpty()) {
			searchDto.setNoSearchResultForLastChanges(Boolean.TRUE);
			if (log.isInfoEnabled()) {
				log.info(" ### No Search Results Found !!! ");
			}
		} else {
			searchDto.setNoSearchResultForLastChanges(Boolean.FALSE);
			if (log.isInfoEnabled()) {
				log.info(" ### Displaying Search Results ... ");
			}
		}
	}

	/**
	 * Get the list of changes for a given objectVersionId 31.10.2013: ZS: PTS_Problem-26241: results displayed outside
	 * the period set filter
	 * 
	 * @param elementVersionId
	 * @param objectType
	 * @param searchDto
	 */
	public void searchDetails(Long objectId, Long objectType, ChangeHistoryDTO searchDto) {

		searchDto.getElementChangesDetails().clear();
		searchDto.getComponentChangesDetails().clear();
		searchDto.getSearchObject().getComponentId().clear();
		searchDto.getSearchObject().getElementId().clear();
		if (EnumObjectTypeManager.ELEMENT_ID.equals(objectType)) {
			searchDto.getSearchObject().getElementId().add(objectId);
			ElementModel element = elementManager.getObject(objectId);
			searchDto.setObjectName(element.getNumberAndDesignation());
			searchDto.setObjectId(objectId.toString());
			searchDto.setObjectFolderId(element.getFolder().getFolderId().toString());
			List<ElementVersionChangeModel> elementsChanges = elementVersionChangeManager
					.getChangesByCriteria(searchDto.getSearchObject());
			searchDto.setElementChangesDetails(elementsChanges);
		} else {
			searchDto.getSearchObject().getComponentId().add(objectId);
			ComponentModel component = componentManager.getObject(objectId);
			searchDto.setObjectName(component.getNumberAndDesignation());
			searchDto.setObjectId(objectId.toString());
			searchDto.setObjectFolderId(component.getFolder().getFolderId().toString());
			List<ComponentStandChangeModel> componentsChanges = componentStandChangeManager
					.getChangesByCriteria(searchDto.getSearchObject());
			Collections.sort(componentsChanges, new Comparator<ComponentStandChangeModel>() {
				@Override
				public int compare(ComponentStandChangeModel o1, ComponentStandChangeModel o2) {
					return o2.getDate().getDate().compareTo(o1.getDate().getDate());
				}
			});
			searchDto.setComponentChangesDetails(componentsChanges);
		}
	}

	/**
	 * Prepare data for the excel file. 31.10.2013: ZS: PTS_Problem-26241: results displayed outside the period set
	 * filter
	 */
	public void loadExcelData(ChangeHistoryDTO searchDto) {
		Set<Long> elementsId = new HashSet<Long>();
		Set<Long> componentsId = new HashSet<Long>();
		searchDto.getComponentChangesDetails().clear();
		searchDto.getElementChangesDetails().clear();
		if (!CollectionUtils.isEmpty(searchDto.getElementChangesResults())) {
			for (ElementVersionChangeModel elementVersionChangeModel : searchDto.getElementChangesResults()) {
				elementsId.add(elementVersionChangeModel.getElementVersion().getElement().getElementId());
			}
		}
		if (!CollectionUtils.isEmpty(searchDto.getComponentChangesResults())) {
			for (ComponentStandChangeModel componentStandChangeModel : searchDto.getComponentChangesResults()) {
				componentsId.add(componentStandChangeModel.getComponentStand().getComponentVersion().getComponent()
						.getComponentId());
			}
		}
		if (!CollectionUtils.isEmpty(elementsId)) {
			searchDto.getSearchObject().setElementId(new ArrayList<Long>(elementsId));
			List<ElementVersionChangeModel> result = elementVersionChangeManager
					.getChangesByCriteria(searchDto.getSearchObject());

			Collections.sort(result, new Comparator<ElementVersionChangeModel>() {
				@Override
				public int compare(ElementVersionChangeModel o1, ElementVersionChangeModel o2) {
					return o2.getElementVersion().getElement().getNumberAndDesignation()
							.compareTo(o1.getElementVersion().getElement().getNumberAndDesignation());
				}
			});

			searchDto.setElementChangesDetails(result);
		}
		if (!CollectionUtils.isEmpty(componentsId)) {
			searchDto.getSearchObject().setComponentId(new ArrayList<Long>(componentsId));
			List<ComponentStandChangeModel> componentsChanges = componentStandChangeManager
					.getChangesByCriteria(searchDto.getSearchObject());
			Collections.sort(componentsChanges, new Comparator<ComponentStandChangeModel>() {
				@Override
				public int compare(ComponentStandChangeModel o1, ComponentStandChangeModel o2) {
					return o2.getDate().getDate().compareTo(o1.getDate().getDate());
				}
			});
			searchDto.setComponentChangesDetails(componentsChanges);
		}
	}

	public Object[] loadExcelExpotedFile(ChangeHistoryDTO dto) throws IOException {
		Object[] fileNameAndContent = new Object[2];
		// launch the search
		search(dto);
		// prepare excel data
		loadExcelData(dto);
		if (CollectionUtils.isEmpty(dto.getComponentChangesDetails())
				&& CollectionUtils.isEmpty(dto.getElementChangesDetails())) {
			log.info("No search results found to be exported.");
		} else {
			File template = new File(getAbsoluteClassDirPath() + File.separator + "templates" + File.separator + "excel"
					+ File.separator + "Export_Aenderungsinformation.xls");
			if (!template.exists()) {
				log.info(template + " File not found");
				template = new File(
						"/appl/webappl/process_designer/appl/wl_config/templates/excel/Export_Aenderungsinformation.xls");
				log.info("Use file: " + template);
			}
			HSSFWorkbook wb = null;
			FileInputStream fis;
			fis = new FileInputStream(template);
			wb = new HSSFWorkbook(fis);
			Sheet sheet = wb.getSheetAt(0);
			wb.setSheetName(0, "Export Aenderungen");
			// build the header
			fillExcelHeader(dto.getSearchObject(), dto.getUserLogged().getSigFullName(), sheet);
			// build the content
			int index = 6;
			index = fillExcelContentWithComponentStandChanges(dto.getComponentChangesDetails(), sheet, index);
			if (dto.getComponentChangesDetails().isEmpty()) {
				index += 2;
			} else {
				index += 1;
			}
			fillExcelContentWithElementVersionChanges(dto.getElementChangesDetails(), sheet, index);
			// create the excel file
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			wb.write(stream);
			fileNameAndContent[1] = stream.toByteArray();
		}
		String dateAsString = BaseDateTime.getCurrentDateTime().getDateSeparatedPt();
		// write the content into xml file
		fileNameAndContent[0] = "Export_Aenderungsinformation_" + dateAsString + ".xls";
		return fileNameAndContent;
	}

	/**
	 * fill the excel export file With Element Version Changes
	 * 
	 * @param componentVersion
	 * @param sheet
	 */
	private int fillExcelContentWithElementVersionChanges(List<ElementVersionChangeModel> exportedList, Sheet sheet,
			int index) {
		Row row;
		Cell cell;
		ElementModel element;
		StringBuilder users;
		for (ElementVersionChangeModel elementVersionChange : exportedList) {
			element = elementVersionChange.getElementVersion().getElement();
			row = sheet.getRow(index);
			if (row == null) {
				row = sheet.createRow((short) sheet.getFirstRowNum() + index);
			}

			// set the values for Ordner Ebene 1 and Ebene 1
			List<Long> parents = new ArrayList<Long>();
			String parentFolderName = "";
			String folderName = "";
			parents = folderManager.getFolderIdsHierarchy(element.getFolder().getFolderId());
			parents.remove(6L);
			if (parents.size() > 0) {
				parentFolderName = folderManager.getObject(parents.get(0)).getDesignation();
			}
			if (parents.size() > 1) {
				folderName = folderManager.getObject(parents.get(1)).getDesignation();
			}

			for (int cellIndex = 0; cellIndex < 24; cellIndex++) {
				cell = row.getCell(cellIndex);
				if (cell == null) {
					cell = row.createCell(cellIndex);
					CellStyle cs = sheet.getRow(index - 1).getCell(cellIndex).getCellStyle();
					cs.setWrapText(true);
					cell.setCellStyle(cs);
				}
				CellStyle cs = cell.getCellStyle();
				switch (cellIndex) {
				case 0:
					cell.setCellValue(parentFolderName);
					break;
				case 1:
					cell.setCellValue(folderName);
					break;
				case 2:
					cell.setCellValue(element.getElementNumber());
					break;
				case 3:
					cell.setCellValue("E");
					break;
				case 4:
					cell.setCellValue(element.getDesignation());
					break;
				case 5:
					if (elementVersionChange.getElementVersion().getNumber() != null) {
						cell.setCellValue(elementVersionChange.getElementVersion().getNumber().doubleValue());
					}
					break;
				case 6:
					if (elementVersionChange.getElementVersion().getNumber() != null) {
						cell.setCellValue(elementVersionChange.getElementVersion().getNumber().doubleValue());
					}
					break;
				case 7:
					cell.setCellValue(element.getOwner().getBrandId());
					break;
				case 8:
					users = new StringBuilder();
					for (BrandModel user : elementVersionChange.getUsers()) {
						users.append(user.getBrandId()).append(" ");
					}
					cell.setCellValue(users.toString());
					break;
				case 9:
					cell.setCellValue("x");
					break;
				case 10:
					cell.setCellValue("");
					break;
				case 11:
					cell.setCellValue(elementVersionChange.getChangeType());
					cs.setWrapText(false);
					cell.setCellStyle(cs);
					break;
				case 14:
					CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
					cs.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
					cell.setCellValue(elementVersionChange.getDate().getDate());
					break;
				case 15:
					cell.setCellValue(elementVersionChange.getChange());
					cs.setWrapText(false);
					cell.setCellStyle(cs);
					break;
				case 17:
					if (ChangeType.ATTRIBUTE_VALUE.value().equals(elementVersionChange.getType())) {
						if (elementVersionChange.getOldValue() != null) {
							cell.setCellValue(
									elementVersionChange.getOldValue() + " " + elementVersionChange.getUnit());
						}
					} else {
						cell.setCellValue(elementVersionChange.getOldValue());
					}
					break;
				case 19:
					if (ChangeType.ATTRIBUTE_VALUE.value().equals(elementVersionChange.getType())) {
						if (elementVersionChange.getNewValue() != null) {
							cell.setCellValue(
									elementVersionChange.getNewValue() + " " + elementVersionChange.getUnit());
						}
					} else {
						cell.setCellValue(elementVersionChange.getNewValue());
					}
					break;
				case 20:
					cell.setCellValue(elementVersionChange.getOldComment());
					break;
				case 21:
					cell.setCellValue(elementVersionChange.getNewComment());
					break;
				case 22:
					cell.setCellValue(elementVersionChange.getUserFullName());
					break;
				case 23:
					cell.setCellValue(elementVersionChange.getRemark());
					break;
				default:
				}
			}
			index++;
		}
		return index;
	}

	/**
	 * fill the excel export file With Component Stand Changes
	 * 
	 * @param componentVersion
	 * @param sheet
	 */
	private int fillExcelContentWithComponentStandChanges(List<ComponentStandChangeModel> exportedList, Sheet sheet,
			int index) {
		int temp = index;
		Row row;

		for (ComponentStandChangeModel standChangeModel : exportedList) {
			ComponentModel component = standChangeModel.getComponentStand().getComponentVersion().getComponent();
			// set the values for Ordner Ebene 1 and Ebene 1
			List<Long> parents = new ArrayList<Long>();
			String parentFolderName = "";
			String folderName = "";
			parents = folderManager.getFolderIdsHierarchy(component.getFolder().getFolderId());
			parents.remove(7L);
			if (parents.size() > 0) {
				parentFolderName = folderManager.getObject(parents.get(0)).getDesignation();
			}
			if (parents.size() > 1) {
				folderName = folderManager.getObject(parents.get(1)).getDesignation();
			}
			row = sheet.getRow(index);
			if (row == null) {
				row = sheet.createRow((short) sheet.getFirstRowNum() + index);
			}
			for (int cellIndex = 0; cellIndex < 24; cellIndex++) {
				prepareComponentCellAndFill(sheet, index, row, cellIndex, standChangeModel, parentFolderName,
						folderName);
			}
			index++;
			if ((index - temp) != exportedList.size()) {
				sheet.shiftRows(index, sheet.getLastRowNum(), 1);
			}
		}
		return index;
	}

	/**
	 * Method to prepare the cell to be filled
	 * 
	 * @param sheet
	 * @param index
	 * @param row
	 * @param cellIndex
	 * @param standChangeModel
	 */
	private void prepareComponentCellAndFill(Sheet sheet, int index, Row row, int cellIndex,
			ComponentStandChangeModel standChangeModel, String parentFolderName, String folderName) {
		Cell cell;
		cell = row.getCell(cellIndex);
		if (cell == null) {
			cell = row.createCell(cellIndex);
			CellStyle cs = sheet.getRow(index - 1).getCell(cellIndex).getCellStyle();
			cs.setWrapText(true);
			cell.setCellStyle(cs);
		}
		fillComponentCell(sheet, cell, cellIndex, index, standChangeModel, parentFolderName, folderName);
	}

	/**
	 * Method to fill one cell by the appropriate value
	 * 
	 * @param cell
	 * @param cellIndex
	 * @param standChangeModel
	 */
	private void fillComponentCell(Sheet sheet, Cell cell, int cellIndex, int index,
			ComponentStandChangeModel standChangeModel, String parentFolderName, String folderName) {
		ComponentModel component = standChangeModel.getComponentStand().getComponentVersion().getComponent();
		StringBuilder users;

		String remark = standChangeModel.getRemark();
		CellStyle cs = cell.getCellStyle();
		switch (cellIndex) {
		case 0:
			cell.setCellValue(parentFolderName);
			break;
		case 1:
			cell.setCellValue(folderName);
			break;
		case 2:
			cell.setCellValue(component.getComponentNumber());
			break;
		case 3:
			cell.setCellValue("B");
			break;
		case 4:
			cell.setCellValue(component.getDesignation());
			break;
		case 5:
			if (standChangeModel.getComponentStand().getComponentVersion().getNumber() != null) {
				cell.setCellValue(standChangeModel.getComponentStand().getComponentVersion().getNumber().doubleValue());
			}
			break;
		case 6:
			if (standChangeModel.getComponentStand().getNumber() != null) {
				cell.setCellValue(standChangeModel.getComponentStand().getNumber().doubleValue());
			}
			break;
		case 7:
			cell.setCellValue(component.getOwner().getBrandId());
			break;
		case 8:
			users = new StringBuilder();
			for (BrandModel standUser : standChangeModel.getUsers()) {
				users.append(standUser.getBrandId()).append(" ");
			}
			cell.setCellValue(users.toString());
			break;
		case 9:
			if (remark == null || (remark.contains("Bausteinänderung")
					|| (!remark.contains("Bausteinänderung") && !remark.contains("Elementänderung")))) {
				cell.setCellValue("x");
			}
			break;
		case 10:
			if (remark != null && remark.contains("Elementänderung")) {
				cell.setCellValue("x");
			}
			break;
		case 11:
			cell.setCellValue(standChangeModel.getChangeType());
			cs.setWrapText(false);
			cell.setCellStyle(cs);
			break;
		case 14:
			CreationHelper createHelper = sheet.getWorkbook().getCreationHelper();
			cs.setDataFormat(createHelper.createDataFormat().getFormat("dd.mm.yyyy"));
			cell.setCellValue(standChangeModel.getDate().getDate());
			break;
		case 15:
			cell.setCellValue(standChangeModel.getChange());
			cs.setWrapText(false);
			cell.setCellStyle(cs);
			break;
		case 17:
			if (ChangeType.ATTRIBUTE_VALUE.value().equals(standChangeModel.getType())) {
				if (standChangeModel.getOldValue() != null) {
					cell.setCellValue(standChangeModel.getOldValue() + " " + standChangeModel.getUnit());
				}
			} else {
				cell.setCellValue(standChangeModel.getOldValue());
			}

			break;
		case 19:
			if (ChangeType.ATTRIBUTE_VALUE.value().equals(standChangeModel.getType())) {
				if (standChangeModel.getNewValue() != null) {
					cell.setCellValue(standChangeModel.getNewValue() + " " + standChangeModel.getUnit());
				}
			} else {
				cell.setCellValue(standChangeModel.getNewValue());
			}
			break;
		case 20:
			cell.setCellValue(standChangeModel.getOldComment());
			break;
		case 21:
			cell.setCellValue(standChangeModel.getNewComment());
			break;
		case 22:
			cell.setCellValue(standChangeModel.getUserFullName());
			break;
		case 23:
			if (remark != null) {
				remark = remark.replaceAll("</br>", "\n");
			}
			cell.setCellValue(remark);
			break;
		default:
		}
	}

	/**
	 * fill the excel export file content
	 * 
	 * @param componentVersion
	 * @param sheet
	 */
	private void fillExcelHeader(SearchHistoryObject searchHistoryObject, String userFullName, Sheet sheet) {
		// Ersteller
		Row row = sheet.getRow(sheet.getFirstRowNum() + 3);
		row.getCell(4).setCellValue(userFullName);
		CellStyle cs = row.getCell(4).getCellStyle();
		cs.setWrapText(true);
		row.getCell(4).setCellStyle(cs);

		// von
		row.getCell(8).setCellValue(searchHistoryObject.getFromDate() == null ? ""
				: new BaseDateTime(searchHistoryObject.getFromDate()).getDateSeparatedPt());
		// änderungsart first row
		row.getCell(11).setCellValue(searchHistoryObject.getNewOne() ? JA : NEIN);
		row.getCell(13).setCellValue(searchHistoryObject.getDesactivaton() ? JA : NEIN);
		row.getCell(15).setCellValue(searchHistoryObject.getInProgress() ? JA : NEIN);
		// Besitzer
		row.getCell(17).setCellValue(searchHistoryObject.getOwner());
		// Nutzer
		row.getCell(18).setCellValue(searchHistoryObject.getUser());
		// nicht genutzte Bausteine ausblenden
		row.getCell(19).setCellValue("");
		row = sheet.getRow(sheet.getFirstRowNum() + 4);
		// Erstelldatum:
		row.getCell(4).setCellValue(BaseDateTime.getCurrentDateTime().getDateSeparatedPt());
		// bis
		row.getCell(8).setCellValue(searchHistoryObject.getToDate() == null ? ""
				: new BaseDateTime(searchHistoryObject.getToDate()).getDateSeparatedPt());
		// änderungsart second row
		row.getCell(11).setCellValue(searchHistoryObject.getExchangeUnusedState() ? JA : NEIN);
		row.getCell(13).setCellValue(searchHistoryObject.getActivation() ? JA : NEIN);
		row.getCell(15).setCellValue(searchHistoryObject.getNewState() ? JA : NEIN);
	}

	public ChangeHistoryDTO loadDTO(ChangeHistoryDTO changeHistoryDTO) {
		changeHistoryDTO.setFromLastChangesPage(Boolean.FALSE);
		// If "Alle" Option is selected in Nutzer dropdown, fill users list
		SearchHistoryObject searchObject = changeHistoryDTO.getSearchObject();
		List<BrandModel> brands = brandManager.getObjects();
		List<String> usersBrands = new ArrayList<String>(brands.size());
		if (searchObject.getUser() == null) {
			if (brands != null) {
				BrandModel brand;
				for (int i = 0; i < brands.size(); i++) {
					brand = brands.get(i);
					usersBrands.add(brand.getBrandId());
				}
			}
		} else {
			usersBrands.add(searchObject.getUser());
		}
		searchObject.setUsersList(usersBrands);
		searchObject.setToDate(new Date());
		changeHistoryDTO.setBrands(brands);
		return changeHistoryDTO;
	}

	/**
	 * PTS 22211: Set Changes types required for Changes Notifications
	 */
	public ChangeHistoryDTO loadDTOForLastChangeNotifications(ChangeHistoryDTO dto) {

		SearchHistoryObject searchObject = dto.getSearchObject();
		searchObject.setJustComponent(Boolean.FALSE);
		searchObject.setJustEelement(Boolean.FALSE);
		searchObject.setActivation(Boolean.TRUE);
		searchObject.setDesactivaton(Boolean.TRUE);
		searchObject.setExchangeUnusedState(Boolean.TRUE);
		searchObject.setNewState(Boolean.TRUE);
		// PTS 34858: Add "New" status to changes notifications
		searchObject.setNewOne(Boolean.TRUE);
		searchObject.setInProgress(Boolean.FALSE);
		// Fill users list
		List<BrandModel> brands = brandManager.getObjects();
		List<String> usersBrands = new ArrayList<String>(brands.size());
		if (brands != null) {
			BrandModel brand;
			for (int i = 0; i < brands.size(); i++) {
				brand = brands.get(i);
				usersBrands.add(brand.getBrandId());
			}
		}
		searchObject.setUsersList(usersBrands);
		// To Date is current date
		searchObject.setToDate(new Date());
		// Set concerned Status : „in Verwendung“ and "Freigegebene"
		List<Long> statusList = new ArrayList<Long>();
		// PTS 34858:"Erstellt" and "In Beabeitung" status in changes notifications
		statusList.add(EnumStatusManager.Status.CREATED.value());
		statusList.add(EnumStatusManager.Status.IN_PROGRESS.value());
		// "Freigegebene" status
		statusList.add(EnumStatusManager.Status.APPROVED.value());
		// "In Verwendung" status
		statusList.add(EnumStatusManager.Status.IN_USE.value());

		searchObject.setStatusList(statusList);
		return dto;
	}

	public void setElementVersionChangeManager(ElementVersionChangeManager elementVersionChangeManager) {
		this.elementVersionChangeManager = elementVersionChangeManager;
	}

	public void setComponentStandChangeManager(ComponentStandChangeManager componentStandChangeManager) {
		this.componentStandChangeManager = componentStandChangeManager;
	}

	public void setBrandManager(BrandManager brandManager) {
		this.brandManager = brandManager;
	}

	public void setElementManager(ElementManager elementManager) {
		this.elementManager = elementManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public void setFolderManager(FolderManager folderManager) {
		this.folderManager = folderManager;
	}

}
