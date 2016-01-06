package pres.nc.maxwell.commondatabaseaccessframework.thread;

import java.sql.ResultSet;

import pres.nc.maxwell.commondatabaseaccessframework.controller.ConnectionPoolController;
import pres.nc.maxwell.commondatabaseaccessframework.db.SQLExecuteHelper;

/**
 * 查询任务线程
 */
public class QueryThread extends NonTransactionThread implements Runnable {

	/**
	 * 获得要执行的SQL语句和连接池控制器
	 * 
	 * @param cpController
	 *            连接池控制器
	 * @param sql
	 *            执行的SQL语句
	 * @param parameters
	 *            SQL语句的参数
	 */
	public QueryThread(ConnectionPoolController cpController, String sql,
			String[] parameters) {
		super(cpController, sql, parameters);
	}

	@Override
	public void run() {
		conn = cpController.getFromPool();

		try {
			ResultSet rs = SQLExecuteHelper.executeQuery(conn, sql, parameters);

			// 处理用户操作
			if (listener != null) {
				listener.onGetResultSet(rs);
			}

			// 关闭结果集
			rs.close();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 设置连接为空闲状态
			cpController.setConnectionIdle(conn);
		}

	}

}
