package com.ronustine.splendid.simplify;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.ronustine.splendid.simplify.exception.ServiceException;

public class XmlUtil {
	public static Document parseXml(String xml) throws ServiceException {
		try {
			Document doc = DocumentHelper.parseText(xml);
			return doc;
		} catch (DocumentException e) {
			throw new ServiceException("xml解析失败", e);
		}
	}

	public static String getElemText(Element con, String elName) {
		String text = con.elementText(elName);
		if (text == null) {
			text = "";
		}
		return text;
	}

	public static Node selectSingleNode(Node node, String xpathStr, Map<String, String> map) {
		XPath xpath = node.createXPath(xpathStr);
		if (map != null) {
			xpath.setNamespaceURIs(map);
		}
		return xpath.selectSingleNode(node);
	}

	public static List<?> selectNodes(Node node, String xpathStr, Map<String, String> map) {
		XPath xpath = node.createXPath(xpathStr);
		if (map != null) {
			xpath.setNamespaceURIs(map);
		}
		return xpath.selectNodes(node);
	}

	public static String buildRequest(Map<String, String> map) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("req");

		if (map != null) {
			for (Object key : map.keySet()) {
				String keyStr = (String) key;
				if (keyStr != null) {
					String value = (String) map.get(keyStr);
					if (value == null) {
						value = "";
					}
					root.addElement(keyStr).setText(value);
				}
			}
		}
		prettyPrint(document);
		return document.asXML();
	}

	public static String buildRequestNoPWD(Map map) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("req");
		if (map != null) {
			for (Object key : map.keySet()) {
				String keyStr = (String) key;
				if (keyStr != null) {
					String value = (String) map.get(keyStr);
					if (value == null) {
						value = "";
					}
					root.addElement(keyStr).setText(value);
				}
			}
		}
		prettyPrint(document);
		return document.asXML();
	}

	public static String prettyPrint(Node el) {
		StringWriter writer = new StringWriter();
		OutputFormat format = OutputFormat.createPrettyPrint();
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		try {
			xmlwriter.write(el);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String ret = writer.toString();
		System.out.println("ret" + ret);
		return ret;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> Dom2Map(Document doc) {
		Map<String, String> map = new HashMap<String, String>();
		if (doc == null)
			return map;
		Element root = doc.getRootElement();
		for (Iterator iterator = root.elementIterator(); iterator.hasNext();) {
			Element e = (Element) iterator.next();
			List list = e.elements();
			if (list.size() > 0) {
				// 暂时用不到
				// map.put(e.getName(), Dom2Map(e));
			} else
				map.put(e.getName(), e.getText());
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {
		Map map = new HashMap();
		List list = e.elements();
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				Element iter = (Element) list.get(i);
				List mapList = new ArrayList();

				if (iter.elements().size() > 0) {
					Map m = Dom2Map(iter);
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(m);
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(m);
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), m);
				} else {
					if (map.get(iter.getName()) != null) {
						Object obj = map.get(iter.getName());
						if (!obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = new ArrayList();
							mapList.add(obj);
							mapList.add(iter.getText());
						}
						if (obj.getClass().getName().equals("java.util.ArrayList")) {
							mapList = (List) obj;
							mapList.add(iter.getText());
						}
						map.put(iter.getName(), mapList);
					} else
						map.put(iter.getName(), iter.getText());
				}
			}
		} else
			map.put(e.getName(), e.getText());
		return map;
	}

	/**
	 * Map 转 XML
	 * 
	 * @param map
	 * @return
	 */
	public static String callMapToXML(Map map) {
		System.out.println("将Map转成Xml, Map：" + map.toString());
		StringBuffer sb = new StringBuffer();
		sb.append("<res>");
		mapToXMLTest2(map, sb);
		sb.append("</res>");
		try {
			StringWriter out = null;
			try {
				Document document = DocumentHelper.parseText(sb.toString());
				OutputFormat formate = OutputFormat.createPrettyPrint();
				out = new StringWriter();
				XMLWriter writer = new XMLWriter(out, formate);
				writer.write(document);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				out.close();
			}
			return out.toString();

		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}

	private static void mapToXMLTest2(Map map, StringBuffer sb) {
		Set set = map.keySet();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String key = (String) it.next();
			Object value = map.get(key);
			if (null == value)
				value = "";
			if (value.getClass().getName().equals("java.util.ArrayList")) {
				ArrayList list = (ArrayList) map.get(key);
				sb.append("<" + key + ">");
				for (int i = 0; i < list.size(); i++) {
					HashMap hm = (HashMap) list.get(i);
					mapToXMLTest2(hm, sb);
				}
				sb.append("</" + key + ">");

			} else {
				if (value instanceof HashMap) {
					sb.append("<" + key + ">");
					mapToXMLTest2((HashMap) value, sb);
					sb.append("</" + key + ">");
				} else {
					sb.append("<" + key + ">" + value + "</" + key + ">");
				}

			}

		}
	}
}
