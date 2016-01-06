package pres.nc.maxwell.commondatabaseaccessframework.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池控制器
 */
public class ThreadPoolController {

	/**
	 * 线程池
	 */
	private ThreadPoolExecutor executor;

	/**
	 * 获取线程池
	 * 
	 * @return 线程池
	 */
	public ThreadPoolExecutor getThreadPool() {
		return executor;
	}

	/**
	 * 创建线程池控制器
	 * 
	 * @param threadPoolCoreSize
	 *            线程池活动线程数
	 * @param maxPoolSize
	 *            线程池最大线程数
	 * @param keepAliveTime
	 *            线程保持时间
	 */
	public ThreadPoolController(int threadPoolCoreSize, int maxPoolSize,
			long keepAliveTime) {
		createThreadPool(threadPoolCoreSize, maxPoolSize, keepAliveTime);
	}

	/**
	 * 创建线程池
	 * 
	 * @param threadPoolCoreSize
	 *            线程池活动线程数
	 * @param maxPoolSize
	 *            线程池最大线程数
	 * @param keepAliveTime
	 *            线程保持时间
	 */
	private void createThreadPool(int threadPoolCoreSize, int maxPoolSize,
			long keepAliveTime) {

		if (0 == threadPoolCoreSize) {
			throw new RuntimeException("线程池最小执行线程为1");
		}

		if (maxPoolSize <= threadPoolCoreSize) {
			throw new RuntimeException("线程池最大线程数必须比连接池最大活跃连接数大");
		}

		executor = new ThreadPoolExecutor(threadPoolCoreSize, maxPoolSize,
				keepAliveTime, TimeUnit.MILLISECONDS,
				new ArrayBlockingQueue<Runnable>(maxPoolSize
						- threadPoolCoreSize));

	}

	/**
	 * 判断线程池是否达到最大线程数
	 * 
	 * @return 真表示线程池已满，否则表示线程池未满
	 */
	public boolean isThreadPoolFull() {

		int currentPoolSize = executor.getPoolSize()
				+ executor.getQueue().size();
		if (currentPoolSize < executor.getMaximumPoolSize()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 等待子线程完成操作后关闭线程池
	 */
	public void shutdown() {

		// 等待子线程完成操作
		while (executor.getActiveCount() != 0) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

		// 关闭线程池
		executor.shutdown();
	}

}
