
package com.goodcol.plugin.activerecord;

import javax.sql.DataSource;

/**
 * IDataSourceProvider
 * <p>
 * ActiveRecordPlugin constructor accept DataSourceProvider and DataSource
 */
public interface IDataSourceProvider {
	DataSource getDataSource();
}


