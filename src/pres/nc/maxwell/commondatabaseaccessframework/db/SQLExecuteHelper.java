package pres.nc.maxwell.commondatabaseaccessframework.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import pres.nc.maxwell.commondatabaseaccessframework.controller.LogController;

/**
 * SQL语句执行帮助类
 */
public class SQLExecuteHelper {

	/**
	 * 执行查询SQL语句
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL语句
	 * @param parameters
	 *            参数
	 * @return 结果集
	 */
	public static ResultSet executeQuery(Connection conn, String sql,
			String... parameters) {

		ResultSet rs = null;
		PreparedStatement ps = null;

		try {
			/* 预编译查询语句 */
			ps = conn.prepareStatement(sql);
			if (parameters != null) {
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}
			}

			rs = ps.executeQuery();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return rs;
	}

	/**
	 * 执行更新操作的SQL语句
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL语句
	 * @param parameters
	 *            参数
	 * @return 影响的行数
	 */
	public static int executeUpdate(Connection conn, String sql,
			String... parameters) {

		PreparedStatement ps = null;
		int ret;

		try {
			ps = conn.prepareStatement(sql);

			/* 预编译SQL语句 */
			if (parameters != null)
				for (int i = 0; i < parameters.length; i++) {
					ps.setString(i + 1, parameters[i]);
				}

			ret = ps.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

		return ret;
	}

	/**
	 * 执行多句更新操作的SQL语句，可用于执行Update/Delete/Insert组合，这个过程是一个事务
	 * 
	 * @param conn
	 *            Connection
	 * @param sql
	 *            SQL语句
	 * @param parameters
	 *            参数
	 */
	public static void executeTransaction(Connection conn, String[] sql,
			String[]... parameters) {

		PreparedStatement ps = null;

		try {
			// 使连接可以执行一个事务
			conn.setAutoCommit(false);

			for (int i = 0; i < sql.length; i++) {

				// 预编译SQL语句
				if (parameters[i] != null) {
					ps = conn.prepareStatement(sql[i]);

					for (int j = 0; j < parameters[i].length; j++)
						ps.setString(j + 1, parameters[i][j]);
				}

				ps.executeUpdate();
			}

			// 提交到连接，此时才开始执行
			conn.commit();

		} catch (Exception e) {

			LogController.log("SQLExecuteHelper", "事务出现异常！");

			try {
				// 出现异常回滚事务
				conn.rollback();
			} catch (SQLException ex) {
				// ex.printStackTrace();
				throw new RuntimeException("事务回滚失败！");
			}

			// e.printStackTrace();
			LogController.log("SQLExecuteHelper", "事务回滚成功！");
			throw new RuntimeException(e.getMessage());

		}

		LogController.log("SQLExecuteHelper", "事务正常执行完毕 ");
	}

}
