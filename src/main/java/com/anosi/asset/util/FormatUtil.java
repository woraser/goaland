package com.anosi.asset.util;

public class FormatUtil {

	/***
	 * 返回文件大小 -h
	 * 
	 * @param fileSize
	 * @return
	 */
	public static String getFileSizeH(Long fileSize) {
		// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
		if (fileSize < 1024) {
			return String.valueOf(fileSize) + "B";
		} else {
			fileSize = fileSize / 1024;
		}
		// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
		// 因为还没有到达要使用另一个单位的时候
		// 接下去以此类推
		if (fileSize < 1024) {
			return String.valueOf(fileSize) + "KB";
		} else {
			fileSize = fileSize / 1024;
		}
		if (fileSize < 1024) {
			// 因为如果以MB为单位的话，要保留最后1位小数，
			// 因此，把此数乘以100之后再取余
			fileSize = fileSize * 100;
			return String.valueOf((fileSize / 100)) + "." + String.valueOf((fileSize % 100)) + "MB";
		} else {
			// 否则如果要以GB为单位的，先除于1024再作同样的处理
			fileSize = fileSize * 100 / 1024;
			return String.valueOf((fileSize / 100)) + "." + String.valueOf((fileSize % 100)) + "GB";
		}
	}

	/***
	 * 百分比转换为小数
	 * 
	 * @param percent
	 * @return
	 */
	public static double convertPerCent2Double(String percent) {
		return Double.parseDouble(percent.replace("%", "")) / 100;
	}

}
