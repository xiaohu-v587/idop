
package com.goodcol.config;

import java.util.ArrayList;
import java.util.List;
import com.goodcol.plugin.IPlugin;

/**
 * Plugins.
 */
final public class Plugins {
	
	private final List<IPlugin> pluginList = new ArrayList<IPlugin>();
	
	public Plugins add(IPlugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("plugin can not be null");
		}
		pluginList.add(plugin);
		return this;
	}
	
	public List<IPlugin> getPluginList() {
		return pluginList;
	}
}
