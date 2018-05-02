package com.compli.util;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.compli.annotation.Table;
import com.compli.db.bean.UserBean;

public class DatabaseUtils {
	public static HashMap<String, List> formInsertStatement(Object dataObject) throws IllegalArgumentException, IllegalAccessException{
		String tableName = getTableName(dataObject);
		HashMap<String, List> objVal = getFieldNameAndValue(dataObject);
		String insertQuery = "INSERT INTO %s(%s) VALUES(%s)";
		
		String keys = objVal.get("keys").toString().replace("[", "").replace("]", "");
		
		String[] values = new String[objVal.get("keys").size()];
		Arrays.fill(values, "?");
		String valueFills = String.join(",", Arrays.asList(values));
		insertQuery = String.format(insertQuery, tableName,keys,valueFills);
		List list = new ArrayList<>();
		list.add(insertQuery);
		objVal.put("query", list);
		return objVal;
	}
	
	private static String getTableName(Object dataObject){
		Table table = dataObject.getClass().getDeclaredAnnotation(Table.class);
		String tableName = table.tableName();
		return tableName;
	}
	
	public static HashMap<String, List> getFieldNameAndValue(final Object dataObject) throws IllegalArgumentException, IllegalAccessException{
		List dataObjectList = new ArrayList();
		List<String> members = new ArrayList<String>();
		List<Integer> types = new ArrayList<Integer>();
		List<Method> methods = Arrays.asList(dataObject.getClass().getDeclaredMethods());
		methods.parallelStream().forEach(e->{
			String methodName = e.getName();
			try {
				if((methodName.startsWith("get") || methodName.startsWith("is")) && (e.invoke(dataObject)!=null)){
					
					int removeIndex = methodName.startsWith("get")?3:0;
					String fieldName = methodName.substring(removeIndex);
					fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);
					types.add(getType(e.getReturnType()));
					dataObjectList.add(e.invoke(dataObject));
					
					members.add(fieldName);
				}
			} catch (Exception e1) {
				e1.printStackTrace();
		}

		});
		HashMap<String, List> map = new HashMap<String, List>();
		map.put("keys", members);
		map.put("value", dataObjectList);
		map.put("type", types);
		return map;
	}
	
	public static int getType(Class<?> cls){
		int type = -1;
		switch (cls.getCanonicalName()) {
		case "java.lang.String":
			type = Types.VARCHAR;
			break;
		case "java.util.Date":
			type = Types.DATE;
			break;
		case "boolean":
			type = Types.BOOLEAN;
			break;
		case "int":
			type = Types.INTEGER;
			break;	
		default:
			break;
		}
		return type;
	}
	
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
		UserBean bean = new UserBean();
		bean.setCreatedBy("Hello");
		bean.setUserId("username");
		bean.setModifiedOn(new Date());
		formInsertStatement(bean);
	}
}
