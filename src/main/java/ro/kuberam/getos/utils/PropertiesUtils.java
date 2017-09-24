package ro.kuberam.getos.utils;

import java.io.InputStream;
import java.util.*;

public class PropertiesUtils {

	public static Map<String, String> getAppDetails() {
		Map<String, String> map = new HashMap<String, String>();
		try {
			InputStream in = PropertiesUtils.class.getClassLoader().getResourceAsStream("app.properties");
			Properties p = new Properties();
			p.load(in);
			map.put("name", p.getProperty("name"));
			map.put("version", p.getProperty("version"));
			map.put("link", p.getProperty("link"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

}
