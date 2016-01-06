package pres.nc.maxwell.commondatabaseaccessframework.prop;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

/**
 * 配置解析器
 */
public class PropertiesAnalyzer {

	/**
	 * 读取配置
	 * @param filePath 配置文件路径
	 * @param propNames 要读取的配置名数组
	 * @return 配置名和值的Map集合
	 */
	public static HashMap<String, String> readProperties(String filePath,String[] propNames) {

		HashMap<String, String> propMap = new HashMap<String, String>();
		
		FileInputStream fis = null;

		try {
			/* 从配置文件中读取配置信息 */
			Properties pp = new Properties();
			fis = new FileInputStream(filePath);
			pp.load(fis);

			// 结果存放在HashMap中
			for (String propName : propNames) {
				propMap.put(propName, pp.getProperty(propName));
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return propMap;
	}

}
