package xlo.database.dao;

/**
 * @author XiaoLOrange
 * @time 2020.11.27
 * @title 用于处理字段名的
 */

public interface FieldFilter {

	/**
	 * 处理实体类中的字段名
	 * @param field
	 * @return
	 */
	public String translateFiled(String field);

}
