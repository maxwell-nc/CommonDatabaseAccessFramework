package pres.nc.maxwell.commondatabaseaccessframework;

import java.util.HashMap;

import pres.nc.maxwell.commondatabaseaccessframework.ResultProcesser.OnGetResultListener;
import pres.nc.maxwell.commondatabaseaccessframework.controller.ConnectionPoolController;
import pres.nc.maxwell.commondatabaseaccessframework.controller.ThreadPoolController;
import pres.nc.maxwell.commondatabaseaccessframework.prop.PropertiesAnalyzer;
import pres.nc.maxwell.commondatabaseaccessframework.thread.QueryThread;
import pres.nc.maxwell.commondatabaseaccessframework.thread.TransactionThread;
import pres.nc.maxwell.commondatabaseaccessframework.thread.UpdateThread;

/**
 * 通用数据库连接器
 */
public class CommonAccesser {

	/**
	 * 连接池控制器
	 */
	private ConnectionPoolController cpController;

	/**
	 * 线程池控制器
	 */
	private ThreadPoolController tpController;

	/**
	 * 获取线程池控制器，如果没有设置则根据连接池大小来设置
	 * 
	 * @return 线程池控制器
	 * @see #setThreadPool(int, int)
	 */
	private ThreadPoolController getThreadPoolController() {

		// 创建默认的线程池控制器
		if (tpController == null) {
			tpController = new ThreadPoolController(cpController.getCpsize(),
					cpController.getCpsize() * 2, 0L);
		}

		return tpController;
	}

	/**
	 * 创建通用数据库连接器
	 * 
	 * @param propFile
	 *            配置文件
	 */
	public CommonAccesser(String propFile) {

		// 解析配置文件
		String[] propNames = { "datasource", "username", "password", "cpsize" };
		HashMap<String, String> propMap = PropertiesAnalyzer.readProperties(
				propFile, propNames);

		// 创建连接池控制器
		cpController = new ConnectionPoolController(propMap);

	}

	/**
	 * 设置线程池(只能执行一次)，必须在调用实际SQL操作方法前调用，否则无效
	 * 
	 * @param threadPoolCoreSize
	 *            线程池活动线程数
	 * @param maxPoolSize
	 *            线程池最大线程数
	 */
	public void setThreadPool(int threadPoolCoreSize, int maxPoolSize) {
		if (tpController == null) {
			tpController = new ThreadPoolController(threadPoolCoreSize,
					maxPoolSize, 0L);
		}
	}

	/**
	 * 异步查询数据库
	 * 
	 * @param listener
	 *            处理结果的监听器
	 * @param sql
	 *            要执行的SQL语句
	 * @param parameters
	 *            SQL语句的参数
	 */
	public void asyncQuery(OnGetResultListener listener, String sql,
			String... parameters) {

		QueryThread thread = new QueryThread(cpController, sql, parameters);
		thread.setOnGetResultSetListener(listener);

		submitTask(thread);
	}

	/**
	 * 异步更新数据库
	 * 
	 * @param listener
	 *            处理结果的监听器
	 * @param sql
	 *            要执行的SQL语句
	 * @param parameters
	 *            SQL语句的参数
	 */
	public void asyncUpdate(OnGetResultListener listener, String sql,
			String... parameters) {

		UpdateThread thread = new UpdateThread(cpController, sql, parameters);
		thread.setOnGetResultSetListener(listener);

		submitTask(thread);
	}

	/**
	 * 异步提交事务
	 * 
	 * @param sql
	 *            要执行的SQL语句集
	 * @param parameters
	 *            SQL语句的参数集
	 */
	public void asyncTransaction(String[] sql, String[]... parameters) {
		TransactionThread thread = new TransactionThread(cpController, sql,
				parameters);
		submitTask(thread);
	}

	/**
	 * 提交任务到线程池
	 * 
	 * @param runnable
	 *            要执行的任务
	 */
	private void submitTask(Runnable runnable) {

		// 通过getThreadPoolController防止线程池控制器没创建
		ThreadPoolController controller = getThreadPoolController();

		while (controller.isThreadPoolFull() == true) {
			// 队列已满，等待的队列空闲
		}

		controller.getThreadPool().execute(runnable);

	}

	/**
	 * 等待任务完成后，关闭数据库连接器
	 */
	public void close() {

		if (tpController == null) {
			return;
		}

		// 关闭线程池
		tpController.shutdown();

		// 关闭连接池
		cpController.shutdown();

	}

}
