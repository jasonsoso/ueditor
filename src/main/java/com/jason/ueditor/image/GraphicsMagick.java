package com.jason.ueditor.image;

import java.util.ArrayList;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

import com.jason.ueditor.util.ExceptionUtils;


/**
 * GraphicsMagick 图片处理
 * 
 * @author Jason
 */
public class GraphicsMagick {
	
	/**
	 * window下 安裝路徑
	 */
	private static final String imageMagickPath = "C:/Program Files/GraphicsMagick-1.3.19-Q16";
	
	/**
	 * IM4JAVA是同时支持ImageMagick和GraphicsMagick的 true 則 GraphicsMagick ,
	 * false 則 ImageMagick
	 */
	private static final boolean isGmOrIM = true;
	
	
	/**
	 * 压缩图片 并且裁剪
	 * @param srcPath
	 * @param newPath
	 * @param sw src width
	 * @param sh src hight
	 * @param dw target width
	 * @param dh targer hight
	 * @throws Exception
	 */
	public static void cropImage(String srcPath, String newPath, int sw,
		int sh, int dw, int dh) throws Exception {
		if (sw <= 0 || sh <= 0 || dw <= 0 || dh <= 0){
			return;
		}
		IMOperation op = new IMOperation();
		op.addImage();
		if ((sw <= dw) && (sh <= dh)){	// 如果源图宽度和高度都小于目标宽高，则仅仅压缩图片
			op.resize(sw, sh);
		}
		if ((sw <= dw) && (sh > dh)){	// 如果源图宽度小于目标宽度，并且源图高度大于目标高度
			op.resize(sw, sh); // 压缩图片
			/**
			 * sw：裁剪的宽度  sw：裁剪的高度 x：裁剪的横坐标  y：裁剪的挫坐标
			 */
			op.append().crop(sw, sw, 0, (sh - dh) / 2);// 切割图片
		}
		if ((sw > dw) && (sh <= dh)){	// 如果源宽度大于目标宽度，并且源高度小于目标高度
			op.resize(sw, sh);
			op.append().crop(dw, sh, (sw - dw) / 2, 0);// 切割图片
		}
		if (sw > dw && sh > dh) {	// 如果源图宽、高都大于目标宽高
			float ratiow = (float) dw / sw; // 宽度压缩比
			float ratioh = (float) dh / sh; // 高度压缩比
			if (ratiow >= ratioh) {	// 宽度压缩比小（等）于高度压缩比（是宽小于高的图片）
				int ch = (int) (ratiow * sh); // 压缩后的图片高度
				op.resize(dw, ch); // 按目标宽度压缩图片
				op.append().crop(dw, dh, 0, (ch > dh) ? ((ch - dh) / 2) : 0); // 根据高度居中切割压缩后的图片
			} else {	// （宽大于高的图片）
				int cw = (int) (ratioh * sw); // 压缩后的图片宽度
				op.resize(cw, dh); // 按计算的宽度进行压缩
				op.append().crop(dw, dh, (cw > dw) ? ((cw - dw) / 2) : 0, 0); // 根据宽度居中切割压缩后的图片
			}
		}
		op.addImage();
		ConvertCmd convert = new WindowsConvertCmd(isGmOrIM);
		convert.run(op, srcPath, newPath);
	}

	/**
	 * 先缩放，后居中切割图片
	 * @param srcPath 源图路径
	 * @param desPath  目标图保存路径
	 * @param rectw 待切割在宽度
	 * @param recth 待切割在高度
	 * @throws Exception
	 */
	public static void cropCenter(String srcPath, String newPath,
		int rectw, int recth) throws Exception {
		IMOperation op = new IMOperation();

		op.addImage();
		op.resize(rectw, recth, '^').gravity("center").extent(rectw, recth);
		//op.resize(rectw, recth).gravity("center").extent(rectw, recth);//以空白補
		op.addImage();

		ConvertCmd convert = new WindowsConvertCmd(isGmOrIM);
		convert.run(op, srcPath, newPath);
	}

	/**
	 * 裁剪图片
	 * @param srcPath 源文件路径
	 * @param newPath 新文件路径
	 * @param x 裁剪的横坐标 
	 * @param y 裁剪的挫坐标
	 * @param width 裁剪的宽度 
	 * @param height 裁剪的高度 
	 * @throws Exception
	 */
	public static void crop(String srcPath, String newPath, int x, int y, int width, int height) throws Exception {
		ConvertCmd convert = new WindowsConvertCmd(isGmOrIM);
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		/**
		 * width：裁剪的宽度 
		 * height：裁剪的高度 
		 * x：裁剪的横坐标 
		 * y：裁剪的挫坐标
		 */
		op.crop(width, height, x, y);
		op.addImage(newPath);

		convert.run(op);
	}

	/**
	 * 缩列图
	 * @param srcPath 源文件
	 * @param newPath 新文件
	 * @param width 宽度
	 * @param height 高度
	 * @throws Exception
	 */
	public static void resize(String srcPath, String newPath, int width, int height) throws Exception {
		int imageWidth = getWidth(srcPath);
		int imageHeight = getHeight(srcPath);

		double ratioV = (double) width / (double) imageWidth;
		double ratioH = (double) height / (double) imageHeight;
		
		if (ratioV >= 1.0 && ratioH >= 1.0) {
			createThumbnailInternal(srcPath, newPath, imageWidth, imageHeight);
			return;
		}

		if (ratioV <= 0 || ratioH <= 0) {
			createThumbnailInternal(srcPath, newPath, imageWidth, imageHeight);
			return;
		}

		double ratio = Math.min(ratioH, ratioV);
		int widthInt = (int) (ratio * imageWidth);
		int heightInt = (int) (ratio * imageHeight);
		
		createThumbnailInternal(srcPath, newPath, widthInt, heightInt);
	}
	public static void createThumbnailInternal(String srcPath, String newPath, int width, int height) throws Exception {
		// create command
		ConvertCmd cmd = new WindowsConvertCmd(isGmOrIM);
		// create the operation, add images and operators/options
		IMOperation op = new IMOperation();
		op.addImage(srcPath);
		op.resize(width, height);
		op.addImage(newPath);

		cmd.run(op);
	}

	/**
	 * 获得图片的宽度
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 图片宽度
	 */
	public static int getWidth(String imagePath) {
	    int line = 0;
	    try {
	        IMOperation op = new IMOperation();
	        op.format("%w"); // 设置获取宽度参数
	        op.addImage(1);
	        IdentifyCmd identifyCmd = new WindowsIdentifyCmd(isGmOrIM);

	        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
	        identifyCmd.setOutputConsumer(output);
	        identifyCmd.run(op, imagePath);
	        ArrayList<String> cmdOutput = output.getOutput();
	        assert cmdOutput.size() == 1;
	        line = Integer.parseInt(cmdOutput.get(0));
	    } catch (Exception e) {
	        line = 0;
	        throw ExceptionUtils.toUnchecked(e,"getWidth,运行指令出错!");
	    }
	    return line;
	}
	 
	/**
	 * 获得图片的高度
	 * 
	 * @param imagePath
	 *            文件路径
	 * @return 图片高度
	 */
	public static int getHeight(String imagePath) {
	    int line = 0;
	    try {
	        IMOperation op = new IMOperation();
	 
	        op.format("%h"); // 设置获取高度参数
	        op.addImage(1);
	        IdentifyCmd identifyCmd = new WindowsIdentifyCmd(isGmOrIM);

	        ArrayListOutputConsumer output = new ArrayListOutputConsumer();
	        identifyCmd.setOutputConsumer(output);
	        identifyCmd.run(op, imagePath);
	        ArrayList<String> cmdOutput = output.getOutput();
	        assert cmdOutput.size() == 1;
	        line = Integer.parseInt(cmdOutput.get(0));
	    } catch (Exception e) {
	        line = 0;
	        throw ExceptionUtils.toUnchecked(e,"getHeight,运行指令出错!");
	    }
	    return line;
	}
	 
	
	/**
	 * 
	 * setSearchPath for windows so: ConvertCmd cmd = new ConvertCmd(true);
	 * ConvertCmd cmd = new WindowsConvertCmd(true);
	 * 
	 * @author Jason
	 */
	public static final class WindowsConvertCmd extends ConvertCmd {
		public WindowsConvertCmd() {
			super();
			if (CommandLineHelper.getOS().equals("windows")) {
				super.setSearchPath(imageMagickPath);
			}
		}

		public WindowsConvertCmd(boolean isGmOrIM) {
			super(isGmOrIM);
			if (CommandLineHelper.getOS().equals("windows")) {
				super.setSearchPath(imageMagickPath);
			}
		}
	}
	
	/**
	 * 
	 * setSearchPath for windows so: IdentifyCmd cmd = new IdentifyCmd(true);
	 * IdentifyCmd cmd = new WindowsIdentifyCmd(true);
	 * 
	 * @author Jason
	 */
	public static final class WindowsIdentifyCmd extends IdentifyCmd {
		public WindowsIdentifyCmd() {
			super();
			if (CommandLineHelper.getOS().equals("windows")) {
				super.setSearchPath(imageMagickPath);
			}
		}

		public WindowsIdentifyCmd(boolean isGmOrIM) {
			super(isGmOrIM);
			if (CommandLineHelper.getOS().equals("windows")) {
				super.setSearchPath(imageMagickPath);
			}
		}
	}
	
}
