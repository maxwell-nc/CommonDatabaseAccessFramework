package pres.nc.maxwell.commondatabaseaccessframework.controller;

/**
 * 日志控制
 */
public class LogController {

	/**
	 * 是否打印日志
	 */
	private static boolean LogOn = true;

	/**
	 * 打印日志到控制台
	 * 
	 * @param msg
	 *            信息
	 */
	public static void log(Object obj, String msg) {

		if (LogOn) {

			if (obj instanceof String) {
				System.out.println(obj + ":" + msg);
			} else {
				System.out.println(getClassName(obj) + ":" + msg);
			}

		}

	}

	/**
	 * 获取对象的类名
	 * 
	 * @param obj
	 *            要获取的对象
	 * @return 对象的类名
	 */
	private static String getClassName(Object obj) {

		String fullPathClassName = obj.getClass().getName();

		String className = fullPathClassName.substring(
				fullPathClassName.lastIndexOf(".") + 1,
				fullPathClassName.length());

		return className;
	}
}
