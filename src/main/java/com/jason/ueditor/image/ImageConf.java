package com.jason.ueditor.image;

import java.io.Serializable;

/**
 * photo conf
 * 
 * @author Jason
 */
public class ImageConf implements Serializable {
	private static final long serialVersionUID = 1L;

	private ImageConf() {
	}
	
	/**
	 * 资讯缩略图类型
	 * @author Jason
	 *
	 */
	public static enum InfoThumb {

		THUMB_660_1000("660_1000",660,1000)
		;

		private final String name;
		private final int width;
		private final int height;

		private InfoThumb(final String name,int width,int height) {
			this.name = name;
			this.width = width;
			this.height = height;
		}
		public String getName() {
			return name;
		}
		public int getWidth() {
			return width;
		}
		public int getHeight() {
			return height;
		}
	}
	
}
