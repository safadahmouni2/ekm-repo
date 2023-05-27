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

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import vwg.vw.km.common.manager.Log;
import vwg.vw.km.common.manager.LogManager;

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
 * @author Sebri Zouhaier changed by $Author: zouhair $
 * @version $Revision: 1.5 $ $Date: 2011/06/03 11:43:16 $
 */
public abstract class BaseHibernateType extends BaseType implements UserType {
	/** the <code>serialVersionUID</code> instance holder */
	private static final long serialVersionUID = -4577419261417809921L;

	private final Log log = LogManager.get().getLog(BaseHibernateType.class);

	@Override
	public int hashCode(Object x) throws HibernateException {
		if (log.isDebugEnabled()) {
			log.debug("params: " + x);
		}
		throw new RuntimeException("(Hibernate UserType) not yet impemented, params: " + x);
	}

	@Override
	public Object deepCopy(Object value) {
		if (log.isDebugEnabled()) {
			log.debug("params: " + value);
		}
		return value;
	}

	@Override
	public boolean isMutable() {
		if (log.isDebugEnabled()) {
			log.debug("entering ...");
		}
		// if (true) throw new RuntimeException("(Hibernate UserType) not yet impemented");
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		throw new RuntimeException("(Hibernate UserType) not yet impemented, params: " + value);
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		throw new RuntimeException("(Hibernate UserType) not yet impemented, params: " + cached + ", " + owner);
	}

	@Override
	public Object replace(Object original, Object target, Object owner) throws HibernateException {
		throw new RuntimeException(
				"(Hibernate UserType) not yet impemented, params: " + original + ", " + target + ", " + owner);
	}

	/**
	 * Hibernate implementation
	 * 
	 * @param resultSet
	 *            ResultSet
	 * @param names
	 *            String[]
	 * @param owner
	 *            Object
	 * @return Object
	 * @throws HibernateException
	 * @throws SQLException
	 */
	@Override
	public Object nullSafeGet(ResultSet resultSet, String[] names, SharedSessionContractImplementor session,
			Object owner) throws HibernateException, SQLException {

		if (log.isDebugEnabled()) {
			log.debug("entering");
		}
		String name = resultSet.getString(names[0]);
		return resultSet.wasNull() ? null : getNewInstance(name);
	}

	/**
	 * Hibernate implementation
	 * 
	 * @param statement
	 *            PreparedStatement
	 * @param value
	 *            Object
	 * @param index
	 *            int
	 * @throws HibernateException
	 * @throws SQLException
	 */
	@Override
	public void nullSafeSet(PreparedStatement statement, Object value, int index,
			SharedSessionContractImplementor session) throws HibernateException, SQLException {

		if (log.isDebugEnabled()) {
			log.debug("getString(): " + getString() + ", object: " + value + ", index : " + index);
		}
		// value is null or the BaseType is Null (the assigned object is null)
		if (value == null || (value instanceof BaseType && ((BaseType) value).isNull())) {
			statement.setNull(index, sqlTypes()[0]);
		} else {
			statement.setString(index, ((BaseType) value).getString());
		}
	}

	public abstract BaseHibernateType getNewInstance(String value);

	@Override
	public abstract int[] sqlTypes();
}
