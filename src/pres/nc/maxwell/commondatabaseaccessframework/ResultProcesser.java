package pres.nc.maxwell.commondatabaseaccessframework;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 结果处理器，作为父类用于扩展
 */
public class ResultProcesser {

	/**
	 * 获得结果集监听器
	 */
	public interface OnGetResultListener {

		/**
		 * 处理结果集
		 * 
		 * @param rs
		 *            结果集
		 */
		public void onGetResultSet(ResultSet rs);

		/**
		 * 处理影响结果
		 * 
		 * @param rowCount
		 *            影响的行数
		 */
		public void onGetRowCount(int rowCount);

	}

	/**
	 * 空实现的OnGetResultListener
	 * @see ResultProcesser#OnGetResultListener
	 */
	public static class SimpleOnGetResultSetListener implements OnGetResultListener {

		@Override
		public void onGetResultSet(ResultSet rs) {}

		@Override
		public void onGetRowCount(int rowCount) {}

	}

	/**
	 * 默认的打印结果集方法
	 * 
	 * @param rs
	 *            结果集
	 * @param args
	 *            要打印的列名
	 */
	public static void printResultSet(ResultSet rs, String... args) {

		try {
			while (rs.next()) {
				for (int i = 0; i < args.length; i++) {
					System.out.println(args[i] + ":" + rs.getString(args[i]));
				}

				System.out.println();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
