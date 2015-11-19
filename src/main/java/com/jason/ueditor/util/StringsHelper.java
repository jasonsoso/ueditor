package com.jason.ueditor.util;

/**
 * 字符串 辅助类
 * @author Jason
 */
public final class StringsHelper {
	
	private StringsHelper(){}
	
	public static String suffix(String filename) {
		if (isBlank(filename)) {
			return "";
		}
		int dotIndex = filename.lastIndexOf('.');
		if (dotIndex == -1) {
			return "";
		}
		return filename.substring(dotIndex + 1);
	}
	
	public static boolean isBlank(String text) {
		if (isEmpty(text)) {
			return true;
		}
		int length = text.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(text.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * eg : null => true
	 * eg : "" => true
	 * @param text
	 * @return
	 */
	public static boolean isEmpty(String text) {
		if (null == text) {
			return true;
		}
		return text.length() <= 0;
	}
}
