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
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import vwg.vw.km.application.implementation.ComponentElementManager;
import vwg.vw.km.application.implementation.ComponentManager;
import vwg.vw.km.application.implementation.ComponentVersionManager;
import vwg.vw.km.application.implementation.ElementManager;
import vwg.vw.km.application.implementation.ElementVersionManager;
import vwg.vw.km.application.implementation.TablesSpaceStatisticManager;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.TableSpaceStatisticModel;

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
 * @author abir changed by $Author: zouhair $
 * @version $Revision: 1.13 $ $Date: 2019/01/22 14:52:52 $
 */

/*
 * PTS requirement 22205: statistic page Responsible ABA
 */
public class StatisticService {

	private final Log log = LogManager.get().getLog(StatisticService.class);

	private TablesSpaceStatisticManager tablesSpaceStatisticManager;

	private ElementManager elementManager;

	private ComponentManager componentManager;

	private ElementVersionManager elementVersionManager;

	private ComponentVersionManager componentVersionManager;

	private ComponentElementManager componentElementManager;

	public List<TableSpaceStatisticModel> getTableSpacesList() {
		return tablesSpaceStatisticManager.getTableSpaces();
	}

	/******************** Start Excel export methods **************************/

	/**
	 * PTS change 26656: Änderungen Statisktikseite
	 */

	public Object[] loadExcelExportFile() throws Exception {
		Object[] fileNameAndContent = null;
		File template = new File(getAbsoluteClassDirPath() + File.separator + "templates" + File.separator + "excel"
				+ File.separator + "statistic_reports.xlsx");
		if (!template.exists()) {
			log.info(template + " File not found");
			template = new File("/appl/webappl/process_designer/appl/wl_config/templates/excel/statistic_reports.xlsx");
			log.info("Use file: " + template);
		}
		XSSFWorkbook wb = null;
		FileInputStream fis;
		fis = new FileInputStream(template);
		wb = new XSSFWorkbook(fis);

		// we style the second cell as a date (and time). It is important to
		// create a new cell style from the workbook otherwise you can end up
		// modifying the built in style and effecting not only this cell but other cells.
		CreationHelper createHelper = wb.getCreationHelper();
		CellStyle dateCellStyle = wb.createCellStyle();
		short dateFormat = createHelper.createDataFormat().getFormat("dd.mm.yyyy");
		dateCellStyle.setDataFormat(dateFormat);

		// fill element Sheet
		XSSFSheet sheet = wb.getSheetAt(0);
		List<Object[]> elements = elementManager.getAllElements();
		fillDataSheet(sheet, dateCellStyle, elements);
		// fill component Sheet
		sheet = wb.getSheetAt(1);
		List<Object[]> components = componentManager.getAllComponents();
		fillDataSheet(sheet, dateCellStyle, components);
		// fill element versions Sheet
		sheet = wb.getSheetAt(2);
		List<Object[]> elementsVersions = elementVersionManager.getAllElementsVersions();
		fillDataSheet(sheet, dateCellStyle, elementsVersions);
		// fill component versions Sheet
		sheet = wb.getSheetAt(3);
		List<Object[]> componentsVersions = componentVersionManager.getAllComponentsVersions();
		fillDataSheet(sheet, dateCellStyle, componentsVersions);
		// PTS problem 35867:ABA: 27-01-2014:fill component versions count sheet
		// This sheet is hidden, it is used only for calculation purpose
		sheet = wb.getSheetAt(4);
		List<Object[]> componentsVersionsCount = componentVersionManager.getComponentsCountByVersion();
		fillDataSheet(sheet, dateCellStyle, componentsVersionsCount);
		// fill component element associations Sheet
		sheet = wb.getSheetAt(5);
		List<Object[]> componentsElements = componentElementManager.getAllElementsComponentsAssociations();
		fillDataSheet(sheet, dateCellStyle, componentsElements);
		// re-calculate cells formulas
		XSSFFormulaEvaluator.evaluateAllFormulaCells(wb);
		// create the excel file
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		wb.write(stream);

		fileNameAndContent = new Object[2];
		fileNameAndContent[0] = "Datenbankauszug_" + BaseDateTime.getCurrentDateTime().getDateSeparatedPt() + ".xlsx";
		fileNameAndContent[1] = stream.toByteArray();
		return fileNameAndContent;
	}

	/**
	 * 
	 * @param sheet
	 */
	private void fillDataSheet(XSSFSheet sheet, CellStyle dateCellStyle, List<Object[]> data) {
		int rowIndex = sheet.getFirstRowNum() + 1;
		XSSFRow row = sheet.getRow(rowIndex);
		if (row == null) {
			row = sheet.createRow((short) rowIndex);
		}
		for (Object[] cellValue : data) {
			for (int i = 0; i < cellValue.length; i++) {
				XSSFCell cell = row.getCell(i);
				if (cell == null) {
					cell = row.createCell(i);
				}
				if (cellValue[i] != null) {
					if (cellValue[i] instanceof java.sql.Timestamp) {
						/*
						 * PTS change 35852: If the value to save is a date we must format excel cell, so it will be
						 * displayed according to this format dd.mm.yyyy
						 */
						// Create date format for current cell
						// Insert the date in current cell
						BaseDateTime dateCell = new BaseDateTime(cellValue[i].toString());
						cell.setCellValue(dateCell.getDate());
						cell.setCellStyle(dateCellStyle);
					} else if (isNumeric(cellValue[i] + "")) {
						/*
						 * If the value to save is a number create a numeric cell, so it can be used directly to
						 * evaluate a formula
						 */
						cell.setCellValue(new Double(cellValue[i].toString()));
					} else {
						cell.setCellValue(cellValue[i] + "");
					}
				}
			}
			rowIndex++;
			row = sheet.getRow(rowIndex);
			if (row == null) {
				row = sheet.createRow((short) rowIndex);
			}
		}
	}

	/**
	 * Used to test if the info to be saved is an number
	 * 
	 * @param str
	 * @return
	 */
	public boolean isNumeric(String str) {
		for (char c : str.toCharArray()) {
			if (!Character.isDigit(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * get Absolute ClassDir Path
	 * 
	 * @return
	 */
	private String getAbsoluteClassDirPath() {
		File file = new File(this.getClass().getResource("/uploadRef.properties").getPath());
		return file.getParentFile().getAbsolutePath();
	}

	public void setTablesSpaceStatisticManager(TablesSpaceStatisticManager tablesSpaceStatisticManager) {
		this.tablesSpaceStatisticManager = tablesSpaceStatisticManager;
	}

	public void setElementManager(ElementManager elementManager) {
		this.elementManager = elementManager;
	}

	public void setComponentManager(ComponentManager componentManager) {
		this.componentManager = componentManager;
	}

	public void setElementVersionManager(ElementVersionManager elementVersionManager) {
		this.elementVersionManager = elementVersionManager;
	}

	public void setComponentVersionManager(ComponentVersionManager componentVersionManager) {
		this.componentVersionManager = componentVersionManager;
	}

	public void setComponentElementManager(ComponentElementManager componentElementManager) {
		this.componentElementManager = componentElementManager;
	}

}
