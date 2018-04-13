package com.bonc.channel_edit.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * json解析
 * @author wanghongbian
 *
 */
public class JsonStrUtil {
	private Object resultObject;
	public JsonStrUtil(String jsonString) throws JSONException {
		// TODO Auto-generated constructor stub
		if (jsonString!=null) {
			JSONObject jsonObject=new JSONObject(jsonString);
			resultObject = analysisJsonString(jsonObject);
//			logger.i(resultObject.getClass());
		}
	}
	/**
	 * 获取处理完的json数据
	 */
	public Object getResultObject(){
		return resultObject;
	}
	/**
	 * jsonObject 是json类型的
	 */
	private Object analysisJsonString(Object jsonObject) throws JSONException {
		Object obj="";
		if (!(jsonObject instanceof Object)) {
			return obj;
		}
		if (jsonObject.equals(null)) {
			return obj;
		}
		else if (jsonObject instanceof String) {
			obj=jsonObject;
		}
		else if (jsonObject instanceof Integer) {
			obj=jsonObject+"";
		}
		else if (jsonObject instanceof Double) {
			obj=jsonObject+"";
		}
		else if (jsonObject instanceof JSONArray) {
			JSONArray array=(JSONArray)jsonObject;
			List<Object> list=new ArrayList<Object>();
			for (int i = 0; i < array.length(); i++) {
				list.add(analysisJsonString(array.get(i)));
			}
			obj=list;
		}
		else {
			Map<String, Object> map=new HashMap<String, Object>();
			JSONObject jsonObject2=(JSONObject)jsonObject;
			Iterator<String> iterator = jsonObject2.keys();
			while (iterator.hasNext()) {
				String keyString=iterator.next();
				Object valueObject=jsonObject2.opt(keyString);
				if (valueObject==null) {
					valueObject="";
				}
				map.put(keyString, analysisJsonString(valueObject));
			}
			obj=map;
		}
		return obj;
	}
	/**
	 * mapOrList 是map或list对象，obj按顺序传入对应的key或位置即可拿到对应维度的元素
	 */
	public static Object getItemObject(Object mapOrList, Object... obj){
		Object object=null;
		List<Object> list=new ArrayList<Object>();
		for (int i = 0; i < obj.length ; i++) {
			list.add(obj[i]);
		}
		object=getObjectWithObject(mapOrList, list);
		return object;
	}
	/**
	 * 为上面方法服务的
	 */
	private static Object getObjectWithObject(Object mapOrList, List<Object> obj){
		Object object=null;
		if (mapOrList instanceof Map<?, ?>) {
			Map<?, ?> map=(Map<?, ?>) mapOrList;
			if (obj.size()>1) {
				if (obj.get(0) instanceof String) {
					object=getObjectWithObject(map.get(obj.get(0)), obj.subList(1, obj.size()));
				}else {
					throw new RuntimeException(obj.get(0)+">>>>>>>>>>>>>"+obj.get(0).getClass()+"不是字符串，请检查数据类型");
				}
			}else if (obj.size()==1) {
				object=map.get(obj.get(0));
			}
		}else if (mapOrList instanceof List<?>) {
			List<?> list=(List<?>) mapOrList;
			if (obj.size()>1) {
				if (obj.get(0) instanceof Integer) {
					int num= (Integer) obj.get(0);
					object=getObjectWithObject(list.get(num), obj.subList(1, obj.size()));
				}else {
					throw new RuntimeException(obj.get(0)+">>>>>>>>>>>>>>"+obj.get(0).getClass()+"不是数值，请检查数据类型");
				}
			}else if (obj.size()==1) {
				object=list.get((Integer)obj.get(0));
			}

		}
		return object;
	}
	//一下为多列表数据的处理
	/**
	 * obj是dataDesc对应的value
	 * typeKey是数据中类型对应的key
	 * dataKey是数据中标题对应的key
	 * subMapKey是下一层数据对应的key
	 * 返回的map中有两个Key:type、data.type是类型的数组，data是表头的数据
	 */
	static public Map<String, List<Object>> getHeaderListAndTypeList(Object obj, String typeKey, String dataKey, String subMapKey){
		Map<String, List<Object>> resultMap=new HashMap<String, List<Object>>();
		resultMap.put("data", new ArrayList<Object>());
		resultMap.put("type", new ArrayList<Object>());
		getHeaderListAndTypeListWidthInfor(obj, typeKey, dataKey, subMapKey,resultMap.get("data"),resultMap.get("type"));
		return resultMap;
	}
	//这个方法是为上面的那个方法服务的
	static private void getHeaderListAndTypeListWidthInfor(Object obj, String typeKey, String dataKey, String subMapKey, List<Object> dataList, List<Object> typeList){
		if (obj instanceof Map<?, ?>) {
			Map<?, ?> map=(Map<?, ?>)obj;
			if (map.get(subMapKey)==null) {
				dataList.add(map.get(dataKey));
				typeList.add((String)map.get(typeKey));
			}else {
				Map<Object, Object> newMap=new HashMap<Object, Object>();
				List<Object> newList=new ArrayList<Object>();
				getHeaderListAndTypeListWidthInfor(map.get(subMapKey), typeKey, dataKey, subMapKey, newList, typeList);
				newMap.put(map.get(dataKey), newList);
			}
		}else if (obj instanceof List<?>) {
			for (Object itemObject : (List<?>)obj) {
				getHeaderListAndTypeListWidthInfor(itemObject, typeKey, dataKey, subMapKey, dataList, typeList);
			}
		}
	}
	/**
	 * 获取表个主题内容的方法
	 */
	static public List<List<String>> getBodyListInfor(List<Object> dataSource, List<Object> typeList){
		List<List<String>> resultList=null;
		if (dataSource!=null&&typeList!=null) {
			resultList=new ArrayList<List<String>>();
			for (Object object : dataSource) {
				if (object instanceof Map<?, ?>) {
					@SuppressWarnings("unchecked")
                    Map<String, String> map=(Map<String, String>) object;
					List<String> lineList=new ArrayList<String>();
					for (Object key : typeList) {
						lineList.add(map.get(key));
					}
					resultList.add(lineList);
				}
			}
		}
		return resultList;
	}
	/**
	 * List<Object>转换成JSON
	 */
	public static String listToJson(List<Object> list){
		JSONArray arr = new JSONArray();
		for (int i = 0; i <list.size(); i++) {
			Map<String,Object> map = (Map<String, Object>) list.get(i);
			Set<String> set= map.keySet();
			Iterator<String> iterator = set.iterator();
			JSONObject obj = new JSONObject();
			while (iterator.hasNext()) {
				try {
					String key=iterator.next();
					obj.put(key,map.get(key));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			arr.put(obj);
		}
		Log.i("=====",arr.toString());
		return arr.toString();
	}
}
