package com.ronustine.splendid.simplify;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
 * Json API 工具类
 * @author ronustine
 */
public final class JsonUtils {

	private static ObjectMapper objectMapper;
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private JsonUtils(){
		
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

	/**
	 * 递归，复杂json转map
	 * @param jsonStr
	 * @return
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr){
		if(jsonStr!=null&&jsonStr.startsWith("{")&&jsonStr.endsWith("}")){
			Map<String, Object> map = new HashMap();

			JSONObject json = JSONObject.parseObject(jsonStr);
			for(Object k : json.keySet()){
				Object v = json.get(k);
				// 如果内层为数组，继续解析
				if(v instanceof JSONArray){
					List<Map<String, Object>> list = new ArrayList<>();
					Iterator<Object> it = ((JSONArray)v).iterator();
					while(it.hasNext()){
						JSONObject json2 = (JSONObject)it.next();
						list.add(parseJSON2Map(json2.toString()));
					}
					map.put(k.toString(), list);
				} else {
					Map<String, Object> m = parseJSON2Map(v.toString());
					if(m==null)
						map.put(k.toString(), v);
					else
						map.put(k.toString(), m);
				}
			}
			return map;
		}else{
			return null;
		}
	}

	public static void main(String[] args) {
		StringBuilder json = new StringBuilder("{\n" +
				"    \"businessTransaction\": \"67fc211f70064edcb193e4bb9db14d0f\",\n" +
				"    \"medicalInfo\": [\n" +
				"        {\n" +
				"            \"birthday\": \"19700101\",\n" +
				"            \"complication\": \"\",\n" +
				"            \"gender\": \"1\",\n" +
				"            \"linkmanname\": \"\",\n" +
				"            \"linkmanmobile\": \"\",\n" +
				"            \"ageunit\": \"0\",\n" +
				"            \"hospitalizationdays\": \"5\",\n" +
				"            \"guardianbirthday\": \"\",\n" +
				"            \"clinicdate\": \"2018-02-23 11:23:00\",\n" +
				"            \"doctorname\": \"李宇博\",\n" +
				"            \"settlementtype\": \"03\",\n" +
				"            \"certno\": \"JfXOvJ5pu6bfL5lNXxXxD0iyZxSRbUVSJtGwn3ETCCQ=\",\n" +
				"            \"billtype\": \"\",\n" +
				"            \"certtype\": \"01\",\n" +
				"            \"hospitalname\": \"首都医科大学附属北京朝阳医院\",\n" +
				"            \"departmentcode\": \"XGW\",\n" +
				"            \"settledate\": \"2018-02-28 15:00:00\",\n" +
				"            \"dischcause\": \"\",\n" +
				"            \"relationshipofpatient\": \"\",\n" +
				"            \"email\": \"\",\n" +
				"            \"guardiangender\": \"\",\n" +
				"            \"diseaselist\": [\n" +
				"                {\n" +
				"                    \"treatmentoutcome\": \"右下肢大隐静脉曲张\",\n" +
				"                    \"code\": \"I83.901\",\n" +
				"                    \"inhosdiagnosetype\": \"0\",\n" +
				"                    \"name\": \"大隐静脉曲张\",\n" +
				"                    \"description\": \"大隐静脉曲张\",\n" +
				"                    \"icd\": \"I83.901\",\n" +
				"                    \"sort\": \"0\"\n" +
				"                },\n" +
				"                {\n" +
				"                    \"treatmentoutcome\": \"右下肢大隐静脉曲张\",\n" +
				"                    \"code\": \"I83.901\",\n" +
				"                    \"inhosdiagnosetype\": \"1\",\n" +
				"                    \"name\": \"大隐静脉曲张\",\n" +
				"                    \"description\": \"大隐静脉曲张\",\n" +
				"                    \"icd\": \"I83.901\",\n" +
				"                    \"sort\": \"0\"\n" +
				"                }\n" +
				"            ],\n" +
				"            \"invoicelist\": [\n" +
				"                {\n" +
				"                    \"medicalitemcatlist\": [\n" +
				"                        {\n" +
				"                            \"amount\": \"1131.33\",\n" +
				"                            \"feelist\": [\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00344\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9083FA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"盒\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \".7754\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"消脱止-M片\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00344\",\n" +
				"                                    \"size\": \"12\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"消脱止-M片\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00344\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"糖衣片\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"消脱止-M片\",\n" +
				"                                    \"projectspec\": \"0.4g*50片△\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"9.3\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00932\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90843A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"支\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"247\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"东菱迪芙注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00932\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"东菱迪芙注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00932\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"东菱迪芙注射液\",\n" +
				"                                    \"projectspec\": \"5BU &\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"247\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01022\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90846A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"7\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01022\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01022\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"0.9%*500ml#\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"14\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01023\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90847A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"6.59\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01023\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01023\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"0.9%*250ml #\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"6.59\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00805\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9084AA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"支\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"45.78\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"耐乐品注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00805\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"耐乐品注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00805\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"耐乐品注射液\",\n" +
				"                                    \"projectspec\": \"100mg &\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"91.56\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01597\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90845A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"瓶\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"4.3\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"乳酸钠林格注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01597\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"乳酸钠林格注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01597\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"乳酸钠林格注射液\",\n" +
				"                                    \"projectspec\": \"500ml J\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"8.6\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00932\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9083EA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"支\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"247\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"东菱迪芙注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00932\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"东菱迪芙注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00932\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"东菱迪芙注射液\",\n" +
				"                                    \"projectspec\": \"5BU &\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"494\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00344\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90842A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"盒\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"38.77\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"消脱止-M片\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00344\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"消脱止-M片\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00344\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"糖衣片\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"消脱止-M片\",\n" +
				"                                    \"projectspec\": \"0.4g*50片△\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"77.54\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01023\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90844A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"6.59\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01023\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01023\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"0.9%*250ml #\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"6.59\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01043\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9083BA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"7.16\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01043\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01043\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"静脉滴注用注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"500ml#\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"7.16\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED02225\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90841A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"盒\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"19.42\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"蚓激酶肠溶胶囊\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED02225\",\n" +
				"                                    \"size\": \"7\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"蚓激酶肠溶胶囊\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED02225\",\n" +
				"                                    \"chargelevel\": \"2\",\n" +
				"                                    \"selfpayamount\": \"135.94\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"肠溶胶囊\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"蚓激酶肠溶胶囊\",\n" +
				"                                    \"projectspec\": \"30万u*12粒\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"135.94\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01023\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9083DA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"6.59\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01023\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01023\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"0.9%*250ml #\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"6.59\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED01043\",\n" +
				"                                    \"feedetailno\": \"6CEE76D9083CA425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"袋\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"7.16\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED01043\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED01043\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"静脉滴注用注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"葡萄糖氯化钠注射液\",\n" +
				"                                    \"projectspec\": \"500ml#\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"7.16\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED00344\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90840A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"盒\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \".7754\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"消脱止-M片\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED00344\",\n" +
				"                                    \"size\": \"12\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"消脱止-M片\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED00344\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"糖衣片\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"消脱止-M片\",\n" +
				"                                    \"projectspec\": \"0.4g*50片△\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"9.3\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED02126\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90848A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"支\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"4.8\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"盐酸肾上腺素注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED02126\",\n" +
				"                                    \"size\": \"1\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"盐酸肾上腺素注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED02126\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"盐酸肾上腺素注射液\",\n" +
				"                                    \"projectspec\": \"1mg:1ml#☆&△J\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"4.8\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                },\n" +
				"                                {\n" +
				"                                    \"insurancecatadminlevel\": \"2\",\n" +
				"                                    \"insurantinnerout\": \"\",\n" +
				"                                    \"insuritemcode\": \"MED02810\",\n" +
				"                                    \"feedetailno\": \"6CEE76D90849A425E054005056AFDFD4\",\n" +
				"                                    \"listcatname\": \"西药费\",\n" +
				"                                    \"isprescription\": \"\",\n" +
				"                                    \"doctorname\": \"李宇博\",\n" +
				"                                    \"doctorfunction\": \"\",\n" +
				"                                    \"projectunit\": \"支\",\n" +
				"                                    \"everydaysize\": \"\",\n" +
				"                                    \"hosbearmoney\": \"\",\n" +
				"                                    \"price\": \"2.6\",\n" +
				"                                    \"drugspecno\": \"\",\n" +
				"                                    \"brand\": \"\",\n" +
				"                                    \"listcat\": \"1\",\n" +
				"                                    \"projectname\": \"盐酸利多卡因注射液\",\n" +
				"                                    \"chinesemedicineno\": \"\",\n" +
				"                                    \"taboo\": \"\",\n" +
				"                                    \"medlimitedprice\": \"0\",\n" +
				"                                    \"isseriousdrugs\": \"\",\n" +
				"                                    \"examinationresult\": \"\",\n" +
				"                                    \"registerno\": \"\",\n" +
				"                                    \"seriousdrugs\": \"\",\n" +
				"                                    \"projectno\": \"MED02810\",\n" +
				"                                    \"size\": \"2\",\n" +
				"                                    \"herbalsize\": \"\",\n" +
				"                                    \"departmentname\": \"血管外科\",\n" +
				"                                    \"useunit\": \"\",\n" +
				"                                    \"mindosageunit\": \"\",\n" +
				"                                    \"insuritemname\": \"盐酸利多卡因注射液\",\n" +
				"                                    \"examinationhint\": \"\",\n" +
				"                                    \"hospitalchargecode\": \"MED02810\",\n" +
				"                                    \"chargelevel\": \"1\",\n" +
				"                                    \"selfpayamount\": \"0\",\n" +
				"                                    \"prescriptionno\": \"\",\n" +
				"                                    \"projectdosage\": \"注射液\",\n" +
				"                                    \"usedaysize\": \"\",\n" +
				"                                    \"manufacturer\": \"\",\n" +
				"                                    \"projectunitcode\": \"\",\n" +
				"                                    \"constituent\": \"\",\n" +
				"                                    \"selfamount\": \"0\",\n" +
				"                                    \"referenceranges\": \"\",\n" +
				"                                    \"departmentcode\": \"XGW\",\n" +
				"                                    \"selfpayratio\": \"\",\n" +
				"                                    \"hospitalchargename\": \"盐酸利多卡因注射液\",\n" +
				"                                    \"projectspec\": \"5ml：0.1gJ#\",\n" +
				"                                    \"visitdepartment\": \"血管外科\",\n" +
				"                                    \"amount\": \"5.2\",\n" +
				"                                    \"drugspecsize\": \"\",\n" +
				"                                    \"projectenglishname\": \"\",\n" +
				"                                    \"drugdelivertype\": \"\",\n" +
				"                                    \"doctorno\": \"005323\",\n" +
				"                                    \"drugflag\": \"\",\n" +
				"                                    \"packageunit\": \"\",\n" +
				"                                    \"issingleremedy\": \"\",\n" +
				"                                    \"extrarecipeflg\": \"\",\n" +
				"                                    \"medicalnum\": \"1\",\n" +
				"                                    \"drugspeclocal\": \"\",\n" +
				"                                    \"medicalitemcat\": \"17\",\n" +
				"                                    \"packagesize\": \"\"\n" +
				"                                }\n" +
				"                            ],\n" +
				"                            \"selfamount\": \"0\",\n" +
				"                            \"medicalitemcat\": \"17\",\n" +
				"                            \"detailcount\": \"16\",\n" +
				"                            \"selfpayamount\": \"0\"\n" +
				"                        }\n" +
				"                    ],\n" +
				"                    \"updatedby\": \"刘鑫\",\n" +
				"                    \"medicarepayline\": \"1300\",\n" +
				"                    \"previousaccountremainamount\": \"\",\n" +
				"                    \"itemcatcount\": \"10\",\n" +
				"                    \"outsidemedicalinsurancetotalamount\": \"45\",\n" +
				"                    \"selfpaytotalamount\": \"\",\n" +
				"                    \"overcappingpaymoney\": \"\",\n" +
				"                    \"medicalbillingno\": \"57020001333767\",\n" +
				"                    \"settlementseqno\": \"\",\n" +
				"                    \"afterroundingamount\": \"\",\n" +
				"                    \"invoicetotalamount\": \"6749.06\",\n" +
				"                    \"medicallnsuranceno\": \"57020001333767\",\n" +
				"                    \"limitlinetolargepayment\": \"\",\n" +
				"                    \"hospitalcost\": \"\",\n" +
				"                    \"perbearmoney\": \"1551.00\",\n" +
				"                    \"fundpaymentamount\": \"\",\n" +
				"                    \"otherfundpayment\": \"\",\n" +
				"                    \"accountfundmoney\": \"\",\n" +
				"                    \"ininsuremoney\": \"\",\n" +
				"                    \"medicarepaylinetolimitline\": \"\",\n" +
				"                    \"settlementtimes\": \"\",\n" +
				"                    \"notifyinfo\": \"\",\n" +
				"                    \"medicarefundcost\": \"5498\",\n" +
				"                    \"businessno\": \"4006863122018030677A\",\n" +
				"                    \"selffeetotalamount\": \"\",\n" +
				"                    \"individualcashpayment\": \"\",\n" +
				"                    \"largepaymentamount\": \"\",\n" +
				"                    \"yearaccountremainamount\": \"\",\n" +
				"                    \"sectioncoordinatepaymoney\": \"\",\n" +
				"                    \"invoiceno\": \"0001333767\",\n" +
				"                    \"priorburdenmoney\": \"\"\n" +
				"                }\n" +
				"            ],\n" +
				"            \"race\": \"1\",\n" +
				"            \"guardianname\": \"\",\n" +
				"            \"patientid\": \"0001897014\",\n" +
				"            \"homeaddress\": \"\",\n" +
				"            \"phonenum\": \"ojAXSXghB/XejIJKRDh2Rg==\",\n" +
				"            \"medicaltype\": \"01\",\n" +
				"            \"originalsocialinsurtype\": \"医疗保险\",\n" +
				"            \"insurancename\": \"EUdBhmJA4g7vU1m9Z4B5Cw==\",\n" +
				"            \"doctorno\": \"005323\",\n" +
				"            \"hospitalcode\": \"0004454\",\n" +
				"            \"socialinsurtype\": \"05\",\n" +
				"            \"admissiondate\": \"2018-02-23 11:23:00\",\n" +
				"            \"departmentname\": \"血管外科\",\n" +
				"            \"companyname\": \"\",\n" +
				"            \"wardshipenddate\": \"\",\n" +
				"            \"medicalnum\": \"1\",\n" +
				"            \"inhospitalnum\": \"1617989\",\n" +
				"            \"name\": \"EUdBhmJA4g7vU1m9Z4B5Cw==\",\n" +
				"            \"dischdate\": \"2018-02-28 00:00:00\",\n" +
				"            \"wardshipstartdate\": \"\",\n" +
				"            \"age\": \"45\"\n" +
				"        }\n" +
				"    ]\n" +
				"}");

		Map a = parseJSON2Map(json.toString());
		System.out.println(a.size());
	}

}
