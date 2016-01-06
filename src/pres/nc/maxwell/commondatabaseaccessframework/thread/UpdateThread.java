package pres.nc.maxwell.commondatabaseaccessframework.thread;

import pres.nc.maxwell.commondatabaseaccessframework.controller.ConnectionPoolController;
import pres.nc.maxwell.commondatabaseaccessframework.db.SQLExecuteHelper;

/**
 * 更新任务线程
 */
public class UpdateThread extends NonTransactionThread implements Runnable {

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
	public UpdateThread(ConnectionPoolController cpController, String sql,
			String[] parameters) {
		super(cpController, sql, parameters);
	}

	@Override
	public void run() {
		conn = cpController.getFromPool();

		try {
			int rowCount = SQLExecuteHelper
					.executeUpdate(conn, sql, parameters);

			// 处理用户操作
			if (listener != null) {
				listener.onGetRowCount(rowCount);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 设置连接为空闲状态
			cpController.setConnectionIdle(conn);
		}

	}

}
