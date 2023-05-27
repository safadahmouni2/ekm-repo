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

package vwg.vw.km.presentation.client.statistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.Resource;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.ace.component.chart.Axis;
import org.icefaces.ace.component.chart.AxisType;
import org.icefaces.ace.component.chart.HighlighterTooltipAxes;
import org.icefaces.ace.component.chart.Location;
import org.icefaces.ace.model.chart.CartesianSeries;
import org.icefaces.ace.model.chart.SectorSeries;

import vwg.vw.km.application.service.logic.StatisticService;
import vwg.vw.km.application.service.logic.UserService;
import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;
import vwg.vw.km.common.type.BaseDateTime;
import vwg.vw.km.common.type.TableSpaceStatisticModel;
import vwg.vw.km.presentation.client.listener.ActivSessionListener;
import vwg.vw.km.presentation.client.navigation.NavigationBean;
import vwg.vw.km.presentation.client.util.FileResource;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: VW (c) 2011
 * </p>
 * 
 * @author abir changed by $Author: zouhair $
 * @version $Revision: 1.11 $ $Date: 2016/10/17 10:02:59 $
 */

/*
 * PTS requirement 22205: statistic page Responsible ABA
 */
public class StatisticBean {

	private final Log log = LogManager.get().getLog(StatisticBean.class);
	// Server Start Time
	String startTime;

	private FileResource excelResource;

	protected NavigationBean navigationTree;

	private boolean showExcelViewFile = Boolean.FALSE;

	private boolean showExcelExportPopup = Boolean.FALSE;

	@PostConstruct
	public void init() {
		this.startTime = BaseDateTime.getCurrentDateTime().getString(BaseDateTime.DD_MM_YYYY_SPC_HH_COL_MM, ".");
		if (log.isInfoEnabled()) {
			log.info("***Server Start Time:  " + startTime);
		}
	}

	public String getStartTime() {
		return this.startTime;
	}

	// Table Spaces Statistic
	private StatisticService statisticTxService;

	public void setTsService(StatisticService tableSpaceStatisticTxService) {
		this.statisticTxService = tableSpaceStatisticTxService;
	}

	private Map<String, String> tableSpacesMap;

	public void setTableSpacesMap(Map<String, String> tableSpacesMap) {
		this.tableSpacesMap = tableSpacesMap;
	}

	List<TableSpaceStatisticModel> tableSpaceDetailsList;

	public List<TableSpaceStatisticModel> getTableSpaceDetailsList() {
		List<TableSpaceStatisticModel> tableSpaces = statisticTxService.getTableSpacesList();
		return tableSpaces;
	}

	public TableSpaceStatisticModel getTableSpaceDetails(List<TableSpaceStatisticModel> tableSpaces, String tsType) {

		TableSpaceStatisticModel tableSpaceDetails = null;
		if (tableSpaces != null && tableSpaces.size() > 0) {
			for (int i = 0; i < tableSpaces.size(); i++) {
				if (tableSpaces.get(i).getTableSpaceType().equals(tsType)) {
					tableSpaceDetails = new TableSpaceStatisticModel(tableSpaces.get(i));
					break;
				}
			}
		}

		if (tableSpaceDetails == null) {
			tableSpaceDetails = new TableSpaceStatisticModel(tsType, 0d, 0d, 0d);
		}
		return tableSpaceDetails;
	}

	// Users online Statistic
	private UserService userTxService;

	public void setUserService(UserService userTxService) {
		this.userTxService = userTxService;
	}

	public float activeUserRate() {
		int activeUsersNumber = getActiveSessionsNumber();
		int activeUsersTotal = userTxService.getActiveUsersCount();
		float activeUsersRate = (activeUsersNumber * 100) / activeUsersTotal;
		log.info("########## Active users rate: " + activeUsersRate + " %");
		return activeUsersRate;
	}

	public int getActiveSessionsNumber() {
		int activSessions = ActivSessionListener.getSessionCount();
		return activSessions;

	}

	// DB connection Status
	private String dbStatus = new String("inaktiv");

	public String getDbStatus() {
		List<TableSpaceStatisticModel> tableSpaces = getTableSpaceDetailsList();
		this.dbStatus = "inaktiv";
		if (tableSpaces != null && tableSpaces.size() > 0) {
			this.dbStatus = "aktiv";
		}

		return dbStatus;
	}

	// Properties related to charts: Tablespace chart
	/**
	 * Getter of chart Bar Data attribute
	 * 
	 * @return import org.icefaces.ace.model.chart.CartesianSeries;
	 */

	public CartesianSeries getBarData() {
		final List<TableSpaceStatisticModel> tableSpaces = getTableSpaceDetailsList();

		CartesianSeries barData = new CartesianSeries() {

			private static final long serialVersionUID = -7953058053079980593L;

			{
				setType(org.icefaces.ace.model.chart.CartesianSeries.CartesianType.BAR);

				add("TAB", getTableSpaceDetails(tableSpaces, tableSpacesMap.get("TAB")).getUsedAmountRate());
				add("IND", getTableSpaceDetails(tableSpaces, tableSpacesMap.get("IND")).getUsedAmountRate());
				add("LOB", getTableSpaceDetails(tableSpaces, tableSpacesMap.get("LOB")).getUsedAmountRate());

			}
		};
		return barData;
	}

	/**
	 * Getter of BarXAxis attribute
	 * 
	 * @return import org.icefaces.ace.model.chart.Axis
	 */
	public Axis getBarXAxis() {
		Axis barXAxis = new Axis() {

			private static final long serialVersionUID = 9220290121415344282L;

			{
				setType(AxisType.CATEGORY);
				setAutoscale(true);
				setTickAngle(-20);
			}
		};
		return barXAxis;
	}

	/**
	 * Getter of BarYAxis attribute
	 * 
	 * @return import org.icefaces.ace.model.chart.Axis
	 */
	public Axis[] getBarYAxes() {
		Axis[] barYAxes = new Axis[] { new Axis() {

			private static final long serialVersionUID = -2882758479017948533L;

			{
				setAutoscale(true);
				setTickInterval("5");
				setMax(100);

			}
		} };
		return barYAxes;
	}

	/**
	 * Getter of BarYAxis attribute
	 * 
	 * @return import org.icefaces.ace.model.chart.Axis
	 */
	public HighlighterTooltipAxes getHighlighterTooltipAxes() {
		HighlighterTooltipAxes highlighterTooltipAxes = HighlighterTooltipAxes.Y;
		return highlighterTooltipAxes;
	}

	/**
	 * Getter of highlighterLocation attribute
	 * 
	 * @return import org.icefaces.ace.model.chart.Location
	 */
	public Location getHighlighterLocation() {
		Location highlighterLocation = Location.N;
		return highlighterLocation;

	}

	// Properties related to charts: Users online Chart
	public List<SectorSeries> getUsersActivData() {
		final double active = activeUserRate();
		final double inactiv = 100 - active;

		List<SectorSeries> usersActivData = new ArrayList<SectorSeries>() {

			private static final long serialVersionUID = 2928733945701161288L;

			{
				add(new SectorSeries() {

					private static final long serialVersionUID = 2639556822247517541L;

					{
						add("online", active);
						add("offline", inactiv);
						setShowDataLabels(true);
						setSliceMargin(4);
						setFill(true);
						setLabel("Benutzer online");
					}
				});
			}
		};
		return usersActivData;
	}

	/**
	 * PTS change 26656: Änderungen Statisktikseite The excel export is based on a template file.
	 * 
	 * @param event
	 */
	public void excelExport(FacesEvent event) {
		if (log.isInfoEnabled()) {
			log.info("Start Excel Export: ");
		}

		try {
			Object[] fileNameAndContent = statisticTxService.loadExcelExportFile();
			if (fileNameAndContent != null) {
				excelResource = new FileResource((byte[]) fileNameAndContent[1], (String) fileNameAndContent[0]);
				setShowExcelViewFile(Boolean.TRUE);
			} else {

				// navigationTree.openErrorPopup(getMessage("costcomponent.exelexport.noresult.message"));
			}
		} catch (Throwable e) {
			log.error("Error on Statistic Excel Export ==> ", e);
		}
	}

	/**
	 * Show the popup model box used to the Excel export
	 */
	public void showExcelExportModal(ActionEvent event) {
		setShowExcelExportPopup(true);
		setShowExcelViewFile(Boolean.FALSE);
		excelExport(event);
	}

	/**
	 * Cancel Excel export
	 */
	public void cancelExport() {
		setShowExcelExportPopup(false);
	}

	/**
	 * close excel export pop up when resource link is clicked
	 * 
	 * @param vce
	 */
	public void excelResourceClicked(ValueChangeEvent vce) {
		log.info("Excel was download");
		cancelExport();
	}

	/**
	 * Return the download resource of the Excel export file
	 * 
	 * @return
	 */
	public Resource getExcelResource() {
		return excelResource;
	}

	public void setExcelResource(FileResource excelResource) {
		this.excelResource = excelResource;
	}

	/**
	 * Return the value of flag to show the Excel export file
	 * 
	 * @return true/false
	 */
	public boolean isShowExcelViewFile() {
		return showExcelViewFile;
	}

	/**
	 * Set the flag to show the Excel export file
	 * 
	 * @param showExcelViewFile
	 */
	public void setShowExcelViewFile(boolean showExcelViewFile) {
		this.showExcelViewFile = showExcelViewFile;
	}

	public boolean isShowExcelExportPopup() {
		return showExcelExportPopup;
	}

	public void setShowExcelExportPopup(boolean showExcelExportPopup) {
		this.showExcelExportPopup = showExcelExportPopup;
	}

}
