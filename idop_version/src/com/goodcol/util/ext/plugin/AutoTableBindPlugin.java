  package com.goodcol.util.ext.plugin;

import java.util.List;

import javax.sql.DataSource;





import com.goodcol.util.ext.anatation.TableBind;
import com.goodcol.core.kit.StrKit;
import com.goodcol.core.plugin.activerecord.ActiveRecordPlugin;
import com.goodcol.core.plugin.activerecord.IDataSourceProvider;
import com.goodcol.core.plugin.activerecord.Model;
/***
 * 自动绑定model与数据库表

 */
public class AutoTableBindPlugin extends ActiveRecordPlugin {
	private TableNameStyle tableNameStyle;
	public AutoTableBindPlugin(DataSource dataSource) {
		super(dataSource);
	}

	public AutoTableBindPlugin(IDataSourceProvider dataSourceProvider, TableNameStyle tableNameStyle) {
		super(dataSourceProvider);
		this.tableNameStyle = tableNameStyle;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public boolean start() {
		try {
			List<Class> modelClasses = ClassSearcher.findClasses(Model.class);
			TableBind tb = null;
			for (Class modelClass : modelClasses) {
				tb = (TableBind) modelClass.getAnnotation(TableBind.class);
				if (tb == null) {
					this.addMapping(tableName(modelClass), modelClass);
				} else {
					if(StrKit.notBlank(tb.name())){
						if (StrKit.notBlank(tb.pk())) {
							this.addMapping(tb.name(), tb.pk(), modelClass);
						} else {
							this.addMapping(tb.name(), modelClass);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
//			throw new RuntimeException(e);
		}
		return super.start();
	}

	@Override
	public boolean stop() {
		return super.stop();
	}

	private String tableName(Class<?> clazz) {
		String tableName = clazz.getSimpleName();
		if (tableNameStyle == TableNameStyle.UP) {
			tableName = tableName.toUpperCase();
		} else if (tableNameStyle == TableNameStyle.LOWER) {
			tableName = tableName.toLowerCase();
		} else {
			tableName = StrKit.firstCharToLowerCase(tableName);
		}
		return tableName;
	}
}
