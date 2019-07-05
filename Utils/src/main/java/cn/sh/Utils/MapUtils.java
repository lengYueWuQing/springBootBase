package cn.sh.Utils;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MapUtils {

	/**
	 * 按Map的value正序排序
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> sortMapByValue(Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	/**
	 * 按Map的value倒序排序
	 * @param map
	 * @return
	 */
	public static <K, V extends Comparable<? super V>> Map<K, V> reverseSortMapByValue(Map<K, V> map) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
				return (o2.getValue()).compareTo(o1.getValue());
			}
		});

		Map<K, V> result = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : list) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}
	

	 /**
	  *  按Map的kay值正序排序
	  * @param map
	  * @return
	  */
		public static <K, V extends Comparable<? super K>> Map<K, V> sortMapByKey(Map<K, V> map) {
			if (map == null || map.isEmpty()) {
				return null;
			}

			Map<K, V> sortMap = new TreeMap<K, V>(/*new Comparator<String>() {
				public int compare(String obj1, String obj2) {
					// 升序排序
					return obj1.compareTo(obj2);
				}
			}*/);
			sortMap.putAll(map);
			return sortMap;
		}
		
		 /**
		  *  按Map的kay值倒序排序
		  * @param map
		  * @return
		  */
			public static Map<String, String> reverseSortMapByKey(Map<String, String> map) {
				if (map == null || map.isEmpty()) {
					return null;
				}

				Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
					public int compare(String obj1, String obj2) {
						// 升序排序
						return obj2.compareTo(obj1);
					}
				});
				sortMap.putAll(map);
				return sortMap;
			}
}
