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

package vwg.vw.km.integration.persistence.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.orm.hibernate5.support.HibernateDaoSupport;

import vwg.vw.km.common.type.TableSpaceStatisticModel;
import vwg.vw.km.integration.persistence.dao.TableSpaceStatisticDAO;

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
 * @version $Revision: 1.5 $ $Date: 2013/11/07 10:13:58 $
 */

/*
 * PTS requirement 22205: statistic page Responsible ABA
 */

public class TableSpaceStatisticDAOImpl extends HibernateDaoSupport implements TableSpaceStatisticDAO {

	private Map<String, String> tableSpacesMap;

	public void setTableSpacesMap(Map<String, String> tableSpacesMap) {
		this.tableSpacesMap = tableSpacesMap;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TableSpaceStatisticModel> getTableSpaces() {

		Query query = this.currentSession().createSQLQuery("select df.tablespace_name , df.bytes / (1024 * 1024) , "
				+ "(df.bytes - SUM(fs.bytes)) / (1024 * 1024)  , COALESCE (round(sum(fs.bytes) * 100 / df.bytes,2),1) , "
				+ "round((df.bytes - sum(fs.bytes)) * 100 / df.bytes,2) from dba_free_space fs, "
				+ "(select tablespace_name,sum(bytes) bytes from dba_data_files group by tablespace_name) df "
				+ " where fs.tablespace_name  = df.tablespace_name "
				+ " and fs.tablespace_name in (:tablespace)  group by df.tablespace_name,df.bytes  ");

		query.setParameter("tablespace", tableSpacesMap.values());

		List<TableSpaceStatisticModel> returnList = new ArrayList<TableSpaceStatisticModel>();
		List<Object[]> resultFromDB = query.getResultList();

		for (Object[] objects : resultFromDB) {
			TableSpaceStatisticModel ts = new TableSpaceStatisticModel((String) objects[0],
					new Double(objects[1].toString()), new Double(objects[2].toString()),
					new Double(objects[4].toString()));
			returnList.add(ts);
		}

		return returnList;

	}
}
