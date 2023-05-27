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
package vwg.vw.km.common.type;

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
 * @author abir changed by $Author: benamora $
 * @version $Revision: 1.4 $ $Date: 2013/11/04 08:52:50 $
 */

/*
 * PTS requirement 22205: statistic page Responsible ABA
 */

public class TableSpaceStatisticModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private String tableSpaceType;

	private Double tableSpaceSize;

	private Double usedAmount;

	private Double usedAmountRate;

	public TableSpaceStatisticModel(String tableSpaceType, Double tableSpaceSize, Double usedAmount,
			Double usedAmountRate) {
		this.tableSpaceType = tableSpaceType;
		this.tableSpaceSize = tableSpaceSize;
		this.usedAmount = usedAmount;
		this.usedAmountRate = usedAmountRate;

	}

	public TableSpaceStatisticModel(TableSpaceStatisticModel tableSpacesStatistic) {
		this.tableSpaceType = tableSpacesStatistic.getTableSpaceType();
		this.tableSpaceSize = tableSpacesStatistic.getTableSpaceSize();
		this.usedAmount = tableSpacesStatistic.getUsedAmount();
		this.usedAmountRate = tableSpacesStatistic.getUsedAmountRate();

	}

	public String getTableSpaceType() {
		return tableSpaceType;
	}

	public void setTableSpaceType(String tableSpaceType) {
		this.tableSpaceType = tableSpaceType;
	}

	public Double getTableSpaceSize() {
		return tableSpaceSize;
	}

	public void setTableSpaceSize(Double tableSpaceSize) {
		this.tableSpaceSize = tableSpaceSize;
	}

	public double getUsedAmount() {
		return usedAmount;
	}

	public void setUsedAmount(Double usedAmount) {
		this.usedAmount = usedAmount;
	}

	public Double getUsedAmountRate() {
		return usedAmountRate;
	}

	public void setUsedAmountRate(Double usedAmountRate) {
		this.usedAmountRate = usedAmountRate;
	}
}
