package pres.nc.maxwell.commondatabaseaccessframework.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map.Entry;

/**
 * 连接池控制器
 */
public class ConnectionPoolController {

	/**
	 * Connection状态：空闲，可以复用
	 */
	private static final String CONNECTION_STATE_IDLE = "IDLE";

	/**
	 * Connection状态：工作中
	 */
	private static final String CONNECTION_STATE_WORKING = "WORKING";

	/**
	 * 存放连接，值为是否空闲
	 * 
	 * @see #CONNECTION_STATE_IDLE
	 * @see #CONNECTION_STATE_WORKING
	 */
	private Hashtable<Connection, String> connectionPool;

	/**
	 * 配置信息
	 */
	private HashMap<String, String> propMap;

	/**
	 * 数据源地址
	 */
	private String datasource;

	/**
	 * 数据库用户名字
	 */
	private String username;

	/**
	 * 数据库用户密码
	 */
	private String password;

	/**
	 * 连接池大小
	 */
	private int cpsize;

	/**
	 * 获取连接池大小
	 * 
	 * @return 连接池大小
	 */
	public int getCpsize() {
		return cpsize;
	}

	/**
	 * 创建连接池控制器
	 * 
	 * @param propMap
	 *            配置信息
	 */
	public ConnectionPoolController(HashMap<String, String> propMap) {
		this.propMap = propMap;
		init();
	}

	/**
	 * 初始化连接池，获取配置参数
	 */
	private void init() {
		connectionPool = new Hashtable<Connection, String>();

		// 解析配置参数
		datasource = propMap.get("datasource");
		username = propMap.get("username");
		password = propMap.get("password");
		cpsize = Integer.parseInt(propMap.get("cpsize"));

		// 解决某些情况下不能自动设别驱动
		try {
			Class.forName(propMap.get("driver"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 创建Connection
	 * 
	 * @return 新的Connection
	 */
	private Connection createConnecion() {

		Connection conn = null; // 返回null表示创建不成功

		try {
			conn = DriverManager.getConnection(datasource, username, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return conn;

	}

	/**
	 * 从连接池中获取Connection
	 * 
	 * @return Connection
	 */
	public Connection getFromPool() {

		Connection conn = null;

		// 注意防止创建多余的线程
		synchronized (connectionPool) {

			if (connectionPool.size() < cpsize) {// 还没创建足够的连接

				conn = createConnecion();
				if (conn != null) {
					connectionPool.put(conn, CONNECTION_STATE_IDLE);
					LogController.log(this,
							"创建连接" + Integer.toHexString(conn.hashCode()));

					return conn;

				}

			}

		}

		// 已经创建足够的连接，返回第一个空闲的连接
		for (Entry<Connection, String> entry : connectionPool.entrySet()) {
			if (entry.getValue() == CONNECTION_STATE_IDLE) {
				conn = entry.getKey();
				entry.setValue(CONNECTION_STATE_WORKING);
				break;
			}
		}

		// 获取连接失败，重新获取
		while (conn == null) {

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			getFromPool();
		}

		LogController.log(this, "使用连接" + Integer.toHexString(conn.hashCode()));

		return conn;

	}

	/**
	 * 设置连接状态为空闲
	 * 
	 * @param conn
	 *            Connection对象
	 */
	public void setConnectionIdle(Connection conn) {
		if (conn != null) {
			for (Entry<Connection, String> entry : connectionPool.entrySet()) {
				if (entry.getKey() == conn) {
					entry.setValue(CONNECTION_STATE_IDLE);
					break;
				}
			}
			LogController.log(this,
					"空闲连接" + Integer.toHexString(conn.hashCode()));
		}
	}

	/**
	 * 关闭连接池,关闭所有Connection
	 */
	public void shutdown() {

		for (Entry<Connection, String> entry : connectionPool.entrySet()) {

			Connection conn = entry.getKey();
			LogController.log(this,
					"关闭连接" + Integer.toHexString(conn.hashCode()));
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			conn = null;

		}

		connectionPool.clear();

	}

}
