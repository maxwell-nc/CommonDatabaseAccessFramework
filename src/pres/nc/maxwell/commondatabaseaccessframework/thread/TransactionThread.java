package pres.nc.maxwell.commondatabaseaccessframework.thread;

import java.sql.Connection;

import pres.nc.maxwell.commondatabaseaccessframework.controller.ConnectionPoolController;
import pres.nc.maxwell.commondatabaseaccessframework.db.SQLExecuteHelper;

/**
 * 事务任务线程
 */
public class TransactionThread implements Runnable {

	/**
	 * Connection
	 */
	private Connection conn;

	/**
	 * 执行的SQL语句集
	 */
	private String[] sql;
	
	/**
	 * SQL语句的参数集
	 */
	private String[][] parameters;

	/**
	 * 连接池控制器
	 */
	private ConnectionPoolController cpController;

	/**
	 * 获得要执行的SQL语句和连接池控制器
	 * 
	 * @param cpController
	 *            连接池控制器
	 * @param sql
	 *            执行的SQL语句集
	 * @param parameters
	 *            SQL语句的参数集
	 */
	public TransactionThread(ConnectionPoolController cpController,
			String[] sql, String[]... parameters) {
		this.cpController = cpController;
		this.sql = sql;
		this.parameters = parameters;

	}

	@Override
	public void run() {
		conn = cpController.getFromPool();

		try {
			SQLExecuteHelper.executeTransaction(conn, sql, parameters);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 设置连接为空闲状态
			cpController.setConnectionIdle(conn);
		}

	}

}
