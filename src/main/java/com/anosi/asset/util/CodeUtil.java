package com.anosi.asset.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class CodeUtil {
	private static final String CODE = "utf-8";
	private static final int BLACK = 0xff000000;
	private static final int WHITE = 0xFFFFFFFF;

	/**
	 * 二维码宽高度默认200
	 */
	private static final int DEFAULT_IMAGE_WIDTH = 300;
	private static final int DEFAULT_IMAGE_HEIGHT = 300;

	/**
	 * 生成带图片二维码时内部图片大小
	 */
	private static final int INNER_IMAGE_WIDTH = 60;
	private static final int INNER_IMAGE_HEIGHT = 60;
	private static final int IMAGE_HALF_WIDTH = INNER_IMAGE_WIDTH / 2;
	private static final int FRAME_WIDTH = 2;

	/**
	 * 生成RQ二维码
	 * 
	 * @author wuhongbo
	 * @param str
	 *            内容
	 * @param height
	 *            高度（px）
	 * 
	 */
	public static BufferedImage getRQ(String str, Integer height) {
		if (height == null || height < 100) {
			height = 200;
		}

		try {
			// 文字编码
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, CODE);

			BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, height, height, hints);

			return toBufferedImage(bitMatrix);

			// 输出方式
			// 网页
			// ImageIO.write(image, "png", response.getOutputStream());

			// 文件
			// ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成一维码（128）
	 * 
	 * @author wuhongbo
	 * @param str
	 * @param width
	 * @param height
	 * @return
	 */
	public static BufferedImage getBarcode(String str, Integer width, Integer height) {

		if (width == null || width < 200) {
			width = 200;
		}

		if (height == null || height < 50) {
			height = 50;
		}

		try {
			// 文字编码
			Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
			hints.put(EncodeHintType.CHARACTER_SET, CODE);

			BitMatrix bitMatrix = new MultiFormatWriter().encode(str, BarcodeFormat.CODE_128, width, height, hints);

			return toBufferedImage(bitMatrix);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成二维码，写到文件中
	 * 
	 * @author wuhongbo
	 * @param str
	 * @param height
	 * @param file
	 * @throws IOException
	 */
	public static void getRQWriteFile(String str, Integer height, File file) throws IOException {
		BufferedImage image = getRQ(str, height);
		ImageIO.write(image, "png", file);
	}

	/**
	 * 生成一维码，写到文件中
	 * 
	 * @author wuhongbo
	 * @param str
	 * @param height
	 * @param file
	 * @throws IOException
	 */
	public static void getBarcodeWriteFile(String str, Integer width, Integer height, File file) throws IOException {
		BufferedImage image = getBarcode(str, width, height);
		ImageIO.write(image, "png", file);
	}

	/**
	 * 转换成图片
	 * 
	 * @author wuhongbo
	 * @param matrix
	 * @return
	 */
	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}

	/**
	 * 解码(二维、一维均可)
	 */
	public static String read(File file) {

		BufferedImage image;
		try {
			if (file == null || file.exists() == false) {
				throw new Exception(" File not found:" + file.getPath());
			}

			image = ImageIO.read(file);

			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Result result;

			// 解码设置编码方式为：utf-8，
			Map<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");

			result = new MultiFormatReader().decode(bitmap, hints);

			return result.getText();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 生成普通二维码
	 * 
	 * @param contents
	 *            内容
	 * @param width
	 *            二维码宽度，如果小于0，则按默认大小生成
	 * @param height
	 *            二维码高度，如果小于0，则按默认大小生成
	 * @param imgPath
	 *            生成后的文件完整存放路径，包含文件名。形如D:\aa.jpg
	 */
	public static void encodePR(String contents, int width, int height, String imgPath) {
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "GBK");
		if (width <= 0 || height <= 0) {
			width = DEFAULT_IMAGE_WIDTH;
			height = DEFAULT_IMAGE_HEIGHT;
		}
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "jpg", new FileOutputStream(imgPath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 生成带图片的二维码
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @param srcImagePath
	 * @param destImagePath
	 */
	public static void encodePR(String content, int width, int height, String srcImagePath, String destImagePath) {
		try {
			ImageIO.write(genBarcode(content, width, height, srcImagePath), "jpg", new File(destImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 针对二维码进行解析
	 * 
	 * @param imgPath
	 *            二维码文件路径
	 * @return
	 */
	public static String decodePR(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				throw new RuntimeException("the decode image may be not exists.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
			hints.put(DecodeHintType.CHARACTER_SET, "GBK");

			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 创建条形码
	 * 
	 * @param contents
	 * @param width
	 * @param height
	 * @param imgPath
	 */
	public static void encodeBar(String contents, int width, int height, String imgPath) {
		// 条形码的最小宽度
		int codeWidth = 98;
		codeWidth = Math.max(codeWidth, width);
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODABAR, codeWidth, height,
					null);

			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 针对条形码进行解析
	 * 
	 * @param imgPath
	 * @return
	 */
	public static String decodeBar(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 把传入的原始图像按高度和宽度进行缩放，生成符合要求的图标
	 * 
	 * @param srcImageFile
	 *            源文件地址
	 * @param height
	 *            目标高度
	 * @param width
	 *            目标宽度
	 * @param hasFiller
	 *            比例不对时是否需要补白：true为补白; false为不补白;
	 * @throws IOException
	 */
	private static BufferedImage scale(String srcImageFile, int height, int width, boolean hasFiller)
			throws IOException {
		double ratio = 0.0; // 缩放比例
		File file = new File(srcImageFile);
		BufferedImage srcImage = ImageIO.read(file);
		Image destImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
		// 计算比例
		if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
			if (srcImage.getHeight() > srcImage.getWidth()) {
				ratio = (new Integer(height)).doubleValue() / srcImage.getHeight();
			} else {
				ratio = (new Integer(width)).doubleValue() / srcImage.getWidth();
			}
			AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
			destImage = op.filter(srcImage, null);
		}
		if (hasFiller) {// 补白
			BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics2D graphic = image.createGraphics();
			graphic.setColor(Color.white);
			graphic.fillRect(0, 0, width, height);
			if (width == destImage.getWidth(null))
				graphic.drawImage(destImage, 0, (height - destImage.getHeight(null)) / 2, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			else
				graphic.drawImage(destImage, (width - destImage.getWidth(null)) / 2, 0, destImage.getWidth(null),
						destImage.getHeight(null), Color.white, null);
			graphic.dispose();
			destImage = image;
		}
		return (BufferedImage) destImage;
	}

	/**
	 * 产生带有图片的二维码缓冲图像
	 * 
	 * @param content
	 * @param width
	 * @param height
	 * @param srcImagePath
	 * @return
	 * @throws WriterException
	 * @throws IOException
	 */
	private static BufferedImage genBarcode(String content, int width, int height, String srcImagePath)
			throws WriterException, IOException {
		// 读取源图像
		BufferedImage scaleImage = scale(srcImagePath, INNER_IMAGE_WIDTH, INNER_IMAGE_HEIGHT, true);
		int[][] srcPixels = new int[INNER_IMAGE_WIDTH][INNER_IMAGE_HEIGHT];
		for (int i = 0; i < scaleImage.getWidth(); i++) {
			for (int j = 0; j < scaleImage.getHeight(); j++) {
				srcPixels[i][j] = scaleImage.getRGB(i, j);
			}
		}

		Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
		hint.put(EncodeHintType.CHARACTER_SET, "GBK");
		hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		// 生成二维码
		MultiFormatWriter mutiWriter = new MultiFormatWriter();
		BitMatrix matrix = mutiWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hint);

		// 二维矩阵转为一维像素数组
		int halfW = matrix.getWidth() / 2;
		int halfH = matrix.getHeight() / 2;
		int[] pixels = new int[width * height];

		for (int y = 0; y < matrix.getHeight(); y++) {
			for (int x = 0; x < matrix.getWidth(); x++) {
				// 读取图片
				if (x > halfW - IMAGE_HALF_WIDTH && x < halfW + IMAGE_HALF_WIDTH && y > halfH - IMAGE_HALF_WIDTH
						&& y < halfH + IMAGE_HALF_WIDTH) {
					pixels[y * width + x] = srcPixels[x - halfW + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
				}
				// 在图片四周形成边框
				else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
						&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH - IMAGE_HALF_WIDTH + FRAME_WIDTH)
						|| (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
								&& y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH
								&& y < halfH + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
					pixels[y * width + x] = 0xfffffff;
				} else {
					// 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
					pixels[y * width + x] = matrix.get(x, y) ? 0xff000000 : 0xfffffff;
				}
			}
		}

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		image.getRaster().setDataElements(0, 0, width, height, pixels);

		return image;
	}

	// public static void main(String[] args) {
	// String imgPath = "Y:/touchscreen/loginQR.png";
	// String contents = "timestamp="+new Date();
	// // 普通二维码的生成与解析
	// CodeUtil.encodePR(contents, 0, 10, imgPath);
	// System.out.println("生成二维码成功");
	// System.out.println(CodeUtil.decodePR(imgPath));

	// // 带图片的二维的生成与解析
	// imgPath = "d:/pr2.png";
	// String srcPath = "d:/src.jpg";
	// QRCodeUtil.encodePR(contents, 300, 300, srcPath, imgPath);
	// System.out.println("生成带图片的二维码成功");
	// System.out.println(QRCodeUtil.decodePR(imgPath));
	//
	// // 条形码的生成与解析
	// imgPath = "d:/bar.png";
	// QRCodeUtil.encodeBar("6923450657713", 105, 50, imgPath);
	// System.out.println("生成条形码成功");
	// System.out.println(QRCodeUtil.decodeBar(imgPath));
	// }

}
