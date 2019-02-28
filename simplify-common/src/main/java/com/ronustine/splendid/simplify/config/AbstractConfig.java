package com.ronustine.splendid.simplify.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

import com.ronustine.splendid.simplify.JsonUtils;


public abstract class AbstractConfig {

	// 配置实体 
	protected PropertiesConfiguration props;
	
	/**
	 * 配置文件路径
	 * @return
	 */
	public abstract String getConfigFilePath();
	
	/** 
	 * 加载配置文件
	 */
	public synchronized void load() throws Exception{
		props = new PropertiesConfiguration();
		props.setEncoding("UTF-8");
		props.setFileName(AbstractConfig.class.getResource("/") + getConfigFilePath());
		props.setReloadingStrategy(new FileChangedReloadingStrategy());
		props.setAutoSave(true);
		props.load();
	}
	
	/** 获取配置值 */
	public synchronized String get(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getString(key);
	}
	
	/** 获取布尔类型配置值 */
	public synchronized boolean getBoolean(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getBoolean(key);
	}
	
	/** 获取整型类型配置值 */
	public synchronized int getInt(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getInt(key);
	}
	
	/** 获取长整型类型配置值 */
	public synchronized long getLong(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getLong(key);
	}
	
	/** 获取浮点类型配置值 */
	public synchronized float getFloat(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getFloat(key);
	}
	
	/** 获取浮点类型配置值 */
	public synchronized double getDouble(String key){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return props.getDouble(key);
	}
	
	/** 设置配置值 */
	public synchronized void set(String key, String value){
		if(props==null){
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		props.setProperty(key, value==null?"":value);
	}
	
	/** 转成Map */
	public Map<String, String> toMap(){
		Map<String, String> map = new HashMap<String, String>();
		Iterator<String> it = props.getKeys();
		while(it.hasNext()){
			String key = it.next();
			map.put(key, props.getString(key));
		}
		return map;
	}
	
	/** 转成JSON格式 */
	public String toJSON(){
		return JsonUtils.toJson(toMap());
	}
}
