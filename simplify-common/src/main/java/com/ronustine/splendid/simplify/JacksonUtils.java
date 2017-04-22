package com.ronustine.splendid.simplify;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Jackson API 工具类
 * @author ronustine
 */
public final class JacksonUtils {

	private static ObjectMapper objectMapper;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private JacksonUtils(){
		
	}
	
	static {
		objectMapper = new ObjectMapper();
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}
	
	/**
	 * 获取ObjectMapper实例
	 * @return
	 */
	public static synchronized ObjectMapper getMapperInstance(){
		if(objectMapper==null){
			objectMapper = new ObjectMapper();
			objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		}
		return objectMapper;
	}
	
	/**
	 * 把实体 Bean编码成JSON字符串
	 * @param bean		实体Bean
	 * @return			JSON字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static <T> String toJson(T bean) {
		return toJson(bean, null);
	}
	
	/**
	 * 把实体 Bean编码成JSON字符串
	 * @param bean		实体Bean
	 * @param pattern	时间模式，默认 yyyy-MM-dd HH:mm:ss
	 * @return			JSON字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static <T> String toJson(T bean,String pattern) {
		return toJson(bean, pattern, true);
	}
	
	/**
	 * 把实体 Bean编码成JSON字符串
	 * @param bean			实体Bean
	 * @param pattern		时间模式，默认 yyyy-MM-dd HH:mm:ss
	 * @param includeNull	是否序列化值为NULL的属性
	 * @return				JSON字符串
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonGenerationException 
	 */
	public static <T> String toJson(T bean,String pattern, boolean includeNull) {
		StringWriter stringWriter = new StringWriter();
		try {
			if(pattern != null){
				simpleDateFormat.applyPattern(pattern);
			}
			if(!includeNull){
				objectMapper.setSerializationInclusion(Include.NON_NULL);
			}
			objectMapper.setDateFormat(simpleDateFormat);
			objectMapper.writeValue(stringWriter, bean);
			stringWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return stringWriter.toString();
	}
	
	/**
	 * 把JSON字符串编码成实体Bean
	 * @param json		JSON字符串
	 * @param clazz		实体Bean的类型
	 * @return			编码后的实体Bean
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	public static <T> T fromJson(String json, Class<T> clazz) {
		return fromJson(json, clazz, null);
	}
	
	/**
	 * 把JSON字符串编码成实体Bean
	 * @param json		JSON字符串
	 * @param clazz		实体Bean的类型
	 * @param pattern	时间模式，默认 yyyy-MM-dd HH:mm:ss
	 * @return			编码后的实体Bean
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T fromJson(String json, Class<T> clazz, String pattern) {
		Object entity = null;
		try {
			if(pattern != null){
				simpleDateFormat.applyPattern(pattern);
			}
			objectMapper.setDateFormat(simpleDateFormat);
			//设置输入时忽略JSON字符串中存在而Java对象实际没有的属性 
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			entity = objectMapper.readValue(json, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (T) entity;
	}
	
	/**
	 * 把JSON字符串编码成实体Bean
	 * @param json		JSON字符串
	 * @param clazz		实体Bean的类型
	 * @param pattern	时间模式，默认 yyyy-MM-dd HH:mm:ss
	 * @return			编码后的实体Bean
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> fromArrayJson(String json,  Class<T> clazz, String pattern) {
		Object entity = null;
		try {
			if(pattern != null){
				simpleDateFormat.applyPattern(pattern);
			}
			objectMapper.setDateFormat(simpleDateFormat);
			//设置输入时忽略JSON字符串中存在而Java对象实际没有的属性 
			objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			JavaType type = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, clazz);
			entity = objectMapper.readValue(json, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (List<T>)entity;
	}
	
}
