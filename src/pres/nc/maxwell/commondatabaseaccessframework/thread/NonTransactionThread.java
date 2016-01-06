package pres.nc.maxwell.commondatabaseaccessframework.thread;

import java.sql.Connection;

import pres.nc.maxwell.commondatabaseaccessframework.ResultProcesser.OnGetResultListener;
import pres.nc.maxwell.commondatabaseaccessframework.controller.ConnectionPoolController;

/**
 * 非事务SQL执行线程
 */
public class NonTransactionThread {

	/**
	 * Connection
	 */
	protected Connection conn;
	
	/**
	 * 执行的SQL语句
	 */
	protected String sql;
	
	/**
	 * SQL语句的参数
	 */
	protected String[] parameters;

	/**
	 * 连接池控制器
	 */
	protected ConnectionPoolController cpController;

	/**
	 * 获得要执行的SQL语句和连接池控制器
	 * @param cpController 连接池控制器
	 * @param sql 执行的SQL语句
	 * @param parameters SQL语句的参数
	 */
	public NonTransactionThread(ConnectionPoolController cpController, String sql,
			String... parameters) {
		this.sql = sql;
		this.parameters = parameters;
		this.cpController = cpController;
	}

	/**
	 * 结果监听器
	 */
	protected OnGetResultListener listener;

	/**
	 * 设置结果监听器
	 * @param listener 结果监听器
	 */
	public void setOnGetResultSetListener(OnGetResultListener listener) {
		this.listener = listener;
	}

}
