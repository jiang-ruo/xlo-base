package xlo.database.dao;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import xlo.infinite.methods.allocateInstance;
import xlo.util.TypeUtil;
import xlo.util.math.NumUtil;

/**
 * @author XiaoLOrange
 * @time 2020.10.17
 * @title 处理数据库的结果集
 * 		因为用反射调用创建对象，因此需要判断类中是否有嵌套的实体类，数组及非实体类直接抛弃
 *
 * 		优化，采用栈，深度遍历的方式处理，目前采用递归的方式处理，因为采用栈太麻烦了，
 */

public class TypeAdapter {

	/**
	 * 标记该类是否是第一次创建，是否需要设置字段为可编辑
	 */
	private static Map<Class, Field[]> classMap = new HashMap<>();
	//判断create开始的地方，防止环状循环创建
	private Class classOfT;
	//将本次已经创建的类放入该Map中，不环形创建
	private Map<Class, Object> created;
	//字段处理器
	private FieldFilter filter;

	public TypeAdapter(FieldFilter filter){
		this.filter = filter;
	}

	/**
	 * 创建类clazz，并将数据库中的值赋给clazz的属性及其子字段。
	 * @param clazz
	 * @param rs
	 * @return
	 */
	public Object create(Class clazz, ResultSet rs) {
		if(classOfT == null){
			classOfT = clazz;
			created = new HashMap<>();
		}
		//防止环状创建
		created.put(clazz, classOfT);

		Object obj = null;
		try {
			obj = allocateInstance.create().newInstance(clazz);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		Field[] fields = classMap.get(clazz);
		//第一次见到该类，将该类的私有属性设为可写
		if(fields == null){
			fields = clazz.getDeclaredFields();
			setAccessible(fields);
			classMap.put(clazz, fields);
		}

		//数据库中的字段名
		String column;
		//从数据库中取出的值
		Object value = null;
		//字段的类型
		Class type;
		for (Field field: fields) {
			//判断是不是数字类型或者是字符串
			type = field.getType();
			if(TypeUtil.isNum(type) || type == String.class || type == Object.class){
				//是能够直接获取的数据类型
				column = field.getName();
				if(filter != null) {
					column = filter.translateFiled(column);
				}
				try {
					value = rs.getObject(column);
				} catch (SQLException throwables) {
					value = null;
				}
			}else if(isEntity(type)){
				//判断是不是直接继承于object的类，后期可以使用注解，只要有指定注解的类就返回true
				value = created.get(field.getType()) == null ? create(field.getType(), rs) : created.get(field.getType());
			}else{
				//其它类型，以后可以酌情添加
			}

			if(value == null) continue;

			setFieldValue(obj, field, value);
		}

		if(classOfT == clazz){
			classOfT = null;
			created = null;
		}
		return obj;
	}

	/**
	 * 设置允许访问私有属性
	 * @param fields
	 */
	private void setAccessible(Field... fields){
		for (Field field: fields) {
			field.setAccessible(true);
		}
	}

	/**
	 * 判断该字段是不是直接继承与Class的方法
	 * @param clazz
	 * @return
	 */
	private boolean isEntity(Class clazz){
		AnnotatedType pretype = clazz.getAnnotatedSuperclass();
		String type = pretype == null ? null : pretype.getType().getTypeName();
		return type == null ? false : type.toString().equals("java.lang.Object");
	}

	/**
	 * 设置属性的值
	 * @param obj
	 * @param field
	 * @param value
	 */
	private void setFieldValue(Object obj, Field field, Object value)  {
		if(value == null) return;

		Class fieldType = field.getType();
		if(fieldType.isPrimitive() || TypeUtil.isBasePacking(fieldType)){
			//是基本数据类型或者其包装类
			try {
				setValue(obj, field, value);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}else{
			//不是基本数据类型
			if(fieldType.isArray()){
				//是数组
				//getComponentType()方法会去除掉一层数组，如果该属性是一维数组，则isArray为false.
//				field.getType().getComponentType().isArray();
			}else{
				//不是数组
				try {
					analiseFieldType(obj, field, value);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 设置对象基本数据类型的值
	 * @param obj
	 * @param field
	 * @param value
	 */
	private void setValue(Object obj, Field field, Object value) throws IllegalAccessException {
		if(value == null) return;

		Class ft = field.getType();

		if(ft == boolean.class || ft == Boolean.class){
			System.err.println("boolean类型未完善");
			return;
		}else if(ft == char.class || ft == Character.class){
			System.err.println("char类型未完善");
			return;
		}else if(ft == byte.class || ft == Byte.class){
			value = NumUtil.toByte(value);
		}else if(ft == short.class || ft == Short.class){
			value = NumUtil.toShort(value);
		}else if(ft == int.class || ft == Integer.class){
			value = NumUtil.toInt(value);
		}else if(ft == long.class || ft == Long.class){
			value = NumUtil.toLong(value);
		}else if(ft == float.class || ft == Float.class){
			System.err.println("float类型未完善");
			return;
		}else if(ft == double.class || ft == Double.class){
			System.err.println("double类型未完善");
			return;
		}

		if(!TypeUtil.isBasePacking(ft)) if(value == null) value = 0;
		field.set(obj, value);

	}

	/**
	 * 设置类类型的值
	 * @param obj
	 * @param field
	 * @param value
	 * @throws IllegalAccessException
	 */
	private void analiseFieldType(Object obj, Field field, Object value) throws IllegalAccessException {
		if(value == null) return;

		//该语句在低版本jdk中不适用，
		//已知jdk8不行，jdk14可以
//		AnnotatedType type = field.getAnnotatedType();
		String typeInfo = field.getType().getTypeName();
		//分为String和其它类
		switch (typeInfo) {
			case "java.lang.Object":
				field.set(obj, value);
				break;
			case "java.lang.String":
				field.set(obj, value.toString());
				break;
			default:
				if(isEntity(field.getType())){
					field.set(obj, value);
				}
				break;
		}
	}

}
