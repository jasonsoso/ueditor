
package com.jason.ueditor.image;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Window/Linux 执行命令行
 * @author Jason
 */
public final class CommandLineHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(CommandLineHelper.class);
	
	private CommandLineHelper(){
	}
	
	private static final String WINDOWS = "windows";
	private static final String LINUX = "linux";
	
	/**
	 * 当前应用系统
	 */
	private static String os;

	static {
		os = System.getProperty("os.name").toLowerCase()
			.startsWith(WINDOWS)?WINDOWS:LINUX;
	}
	/**
	 * 获取应用系统
	 * @return
	 */
	public static String getOS() {
		return os;
	}
	
	/**
	 * 执行cmd
	 * @param cmd
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static boolean exec(String cmd) throws IOException,InterruptedException {
		String[] cmds;
		if (os.equals(WINDOWS)) {
			cmds = new String[] { "cmd.exe", "/c", cmd };
		} else {
			cmds = new String[] { "/bin/sh", "-c", cmd };
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("执行" + os + "系统命令: " + cmd);
		}
		return exec(cmds);
	}

	private static boolean exec(String[] cmds) throws IOException,InterruptedException {
		Process ps = Runtime.getRuntime().exec(cmds);
		String err = loadStream(ps.getErrorStream());
		int r = ps.waitFor();
		if (!err.equalsIgnoreCase("")) {
			throw new IOException(err);
		}
		return (r == 0);
	}

	public static Process execing(String cmd) throws IOException {

		String[] cmds;
		if (os.equals(WINDOWS)) {
			cmds = new String[] { "cmd.exe", "/c", cmd };
		} else {
			cmds = new String[] { "/bin/sh", "-c", cmd };
		}
		return execing(cmds);
	}

	private static Process execing(String[] cmds) throws IOException {
		return Runtime.getRuntime().exec(cmds);
	}

	/**
	 * read an input-stream into a String
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String loadStream(InputStream in) throws IOException {
		InputStream is = in;
		int ptr = 0;
		is = new BufferedInputStream(is);
		StringBuffer buffer = new StringBuffer();
		try {
			while ((ptr = is.read()) != -1) {
				buffer.append((char) ptr);
			}
		} finally {
			is.close();
		}
		return buffer.toString();
	}

}
