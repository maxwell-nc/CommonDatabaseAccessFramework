package pres.nc.maxwell.commondatabaseaccessframework.test;

import java.sql.ResultSet;

import org.junit.Test;

import pres.nc.maxwell.commondatabaseaccessframework.CommonAccesser;
import pres.nc.maxwell.commondatabaseaccessframework.ResultProcesser;
import pres.nc.maxwell.commondatabaseaccessframework.ResultProcesser.SimpleOnGetResultSetListener;

/**
 * Junit测试用例
 */
public class JunitTestCase {

	private final String PROP_FILE = "Setting.properties";

	/**
	 * 测试SQL查询任务
	 */
	@Test
	public void testQueryTask() {
		CommonAccesser accesser = new CommonAccesser(PROP_FILE);
		accesser.setThreadPool(1, 5);// 单线程

		// 重复提交次数
		int repeatTimes = 200;

		for (int i = 0; i < repeatTimes; i++) {
			accesser.asyncQuery(new SimpleOnGetResultSetListener() {

				@Override
				public void onGetResultSet(ResultSet rs) {
					ResultProcesser.printResultSet(rs, "username", "pass");
				}

			}, "select * from usertable");
		}

		accesser.close();
	}

	/**
	 * 测试SQL更新任务
	 */
	@Test
	public void testUpdateTask() {
		CommonAccesser accesser = new CommonAccesser(PROP_FILE);
		accesser.setThreadPool(1, 5);// 单线程

		// 重复提交次数
		int repeatTimes = 200;

		for (int i = 0; i < repeatTimes; i++) {
			String[] parameters = { "xiaoming" + i, "123123" };
			accesser.asyncUpdate(new SimpleOnGetResultSetListener() {

				@Override
				public void onGetRowCount(int rowCount) {
					System.out.println("影响了" + rowCount + "行数据");
				}

			}, "insert into usertable(username,pass) values(?,?)", parameters);
		}

		accesser.close();
	}

	/**
	 * 测试事务任务
	 */
	@Test
	public void testTransactionTask() {
		CommonAccesser accesser = new CommonAccesser(PROP_FILE);
		accesser.setThreadPool(1, 5);// 单线程

		String sql1 = "insert into usertable(username,pass) values(?,?)";
		String sql2 = "delete from usertable where username = ?";
		String[] sql = { sql1, sql2 };

		String[] sql1_params = { "xiaoming", "123123" };
		String[] sql2_params = { "xiaoming" };
		String[][] parameters = { sql1_params, sql2_params };

		accesser.asyncTransaction(sql, parameters);

		accesser.close();
	}

}
