package com.jason.ueditor.image;

import java.io.File;

import com.jason.ueditor.util.ExceptionUtils;
import com.jason.ueditor.util.FilesHelper;


/**
 * 图片处理
 * @author Jason
 */
public final class ImageHelper {
	
	private ImageHelper(){}
	
	
	
	/**
	 * 根据文件，创建缩略图，
	 * @param file 源文件
	 * @param suffix 缩略图的后缀eg:200_300
	 * @param width 宽度
	 * @param height 高度
	 */
	public static void createThumbnailSuffix(File file, String suffix, int width, int height) {
		try {
			String srcPath = file.getCanonicalPath();
			String newPath = FilesHelper.insertStringForPathNotSplit(srcPath,suffix);
			
			createThumbnail(file, newPath, width, height);
			//createThumbnail(file, FilesHelper.insertStringForPathNotSplit(file.getCanonicalPath(),suffix), width, height);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e,"IO Exception！");
		}
	}
	
	
	/**
	 * 根据文件，创建缩略图
	 * @param file 文件
	 * @param thumbnail 缩略图路径
	 * @param width 宽度
	 * @param height 高度
	 */
	public static void createThumbnail(File file, String thumbnail, int width, int height) {
		try {
			String srcPath = file.getCanonicalPath();
			GraphicsMagick.resize(srcPath, thumbnail, width, height);
			
			//InputStream in = new FileInputStream(file);
			//createThumbnail(in, thumbnail, width, height);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e,"File is not found！");
		}
	}
	

	
	/*public static void createThumbnail(String file, String thumbnail, int width, int height) {
		Assert.hasText(file, "file must has text");
		File fi =  new File(file);
		createThumbnail(fi, thumbnail, width, height);
	}*/
	/*public static void createThumbnail(InputStream in, String thumbnail, int width, int height) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(in);

			int imageWidth = image.getWidth();
			int imageHeight = image.getHeight();
			double ratioV = (double) width / (double) imageWidth;
			double ratioH = (double) height / (double) imageHeight;

			if (ratioV >= 1.0 && ratioH >= 1.0) {
				createThumbnailInternal(image, thumbnail, imageWidth, imageHeight);
				return;
			}

			if (ratioV <= 0 || ratioH <= 0) {
				createThumbnailInternal(image, thumbnail, imageWidth, imageHeight);
				return;
			}

			double ratio = Math.min(ratioH, ratioV);
			int widthInt = (int) (ratio * imageWidth);
			int heightInt = (int) (ratio * imageHeight);

			createThumbnailInternal(image, thumbnail, widthInt, heightInt);
		} catch (Exception e) {
			throw ExceptionUtils.toUnchecked(e,"createThumbnail error！");
		}finally{
			try {
				if(in!=null){
					in.close();
				}
			} catch (IOException e) {
			}
		}
	}*/

/*	private static void createThumbnailInternal(BufferedImage image, String thumbnail, int imageWidth, int imageHeight) throws IOException {
		Assert.hasText(thumbnail, "thumbnail must has text");
		
		BufferedImage thumb = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = thumb.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.drawImage(image, 0, 0, imageWidth, imageHeight, null);
		g.dispose();

		String format = StringsHelper.suffix(thumbnail);
		ImageIO.write(thumb, StringUtils.isBlank(format) ? "jpg" : format, new File(thumbnail));
	}
	*/

}
