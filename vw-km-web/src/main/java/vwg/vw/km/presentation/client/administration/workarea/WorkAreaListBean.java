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

package vwg.vw.km.presentation.client.administration.workarea;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.faces.event.ActionEvent;

import org.apache.commons.collections4.CollectionUtils;
import org.icefaces.ace.component.datatable.DataTable;

import vwg.vw.km.application.service.dto.WorkAreaDTO;
import vwg.vw.km.application.service.logic.WorkAreaService;
import vwg.vw.km.integration.persistence.model.WorkAreaModel;
import vwg.vw.km.presentation.client.base.BaseBean;

/**
 * <p>
 * Title: VW_KM
 * <p>
 * Description : Class description goes here
 * </p>
 * <p>
 * Copyright: cl (c) 2011
 * </p>
 * 
 * @author zouhairs changed by $Author: mrad $
 * @version $Revision: 1.30 $ $Date: 2016/10/20 14:49:31 $
 */
public class WorkAreaListBean extends BaseBean<WorkAreaDTO, WorkAreaService> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7860391570843510847L;

	/**
	 * (non-Javadoc)
	 * 
	 * @see vwg.vw.km.presentation.client.base.BaseBean#specificSearchMessage()
	 */
	@Override
	public String specificSearchMessage() {
		return getMessage("action.workArea.search");
	}

	/**
	 * PTS-task-id: 22207 Responsible:ZSE Perform ascending alphabetical/descending alphabetical sort on Work area List
	 * DataTable
	 * 
	 * @param event
	 */
	public static final String SORT_COLUMN_DESIGNATION = "designation";
	private String columnName = SORT_COLUMN_DESIGNATION;

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnName() {
		return columnName;
	}

	public String getSortColumnDesignation() {
		return SORT_COLUMN_DESIGNATION;
	}

	public void columnSort(ActionEvent event) {
		List<WorkAreaModel> workAreaModels = dto.getWorkAreas();
		// Ensure we have valid items, a valid column to sort on, and a valid event to work with
		if (!CollectionUtils.isEmpty(workAreaModels) && (columnName != null) && (event != null)
				&& (event.getComponent() != null)) {
			// Get the column name that was clicked to perform the sort
			String newColumnName = ((DataTable) event.getComponent()).getCaption();
			boolean newAscending = true;

			// If the new column was the same as the old column, just toggle the ascending
			if (newColumnName.equals(columnName)) {
				newAscending = !dto.getAscending();
			}

			// Perform the actual sort
			sort(newColumnName, newAscending, workAreaModels);
		}
	}

	public static void sort(final String sortColumn, final boolean sortAscending, final List<WorkAreaModel> data) {
		// Build a comparator that uses compareTo of the proper column
		Comparator<WorkAreaModel> sortComparator = new Comparator<WorkAreaModel>() {

			@Override
			public int compare(WorkAreaModel item1, WorkAreaModel item2) {
				String firstVal = "";
				String secondVal = "";
				if (SORT_COLUMN_DESIGNATION.equals(sortColumn)) {
					if (item1.getDesignation() != null) {
						firstVal = item1.getDesignation();
					}
					if (item2.getDesignation() != null) {
						secondVal = item2.getDesignation();
					}
				}

				return sortAscending ? firstVal.compareToIgnoreCase(secondVal)
						: secondVal.compareToIgnoreCase(firstVal);
			}

		};

		Collections.sort(data, sortComparator);
	}

}
