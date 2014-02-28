package com.antares.slidedrawer.utils;

import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.util.Log;

public class StringUtils {
	private static final String TAG = "StringUtils";

	private static Random r = new Random();

	public static String getFileExtension(String fullFileName) {
		String result = "";
		int dot = fullFileName.lastIndexOf(".");
		result = fullFileName.substring(dot);

		return result;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.trim().equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public static String delimiterChanger(String oldDelimiter,
			String newDelimiter, String msgStr) {
		String result = msgStr.replace(oldDelimiter, newDelimiter);

		return result;
	}

	public static String urlEncode(String value) {
		String encodeResult = "";

		try {
			encodeResult = URLEncoder.encode(value, "utf-8");
		} catch (Exception e) {
			Log.v(TAG, "ERROR ENCODE URL UTILS: " + e.getMessage());
		}

		return encodeResult;
	}

	@SuppressWarnings("deprecation")
	public static String urlDecode(String value) {
		String decodeResult = "";

		try {
			decodeResult = URLDecoder.decode(value);
		} catch (Exception e) {

		}

		return decodeResult;
	}

	public static String adjustmentLine(String msg, int lengthAdjust,
			boolean isPadLeft, String delimiter) {
		String result = msg;

		int lengthWord = msg.length();
		int remainSpace = 0;

		if (lengthWord < lengthAdjust) {
			remainSpace = lengthAdjust - lengthWord;
			String space = "";

			for (int i = 0; i < remainSpace; i++) {
				space = space + delimiter;
			}

			if (isPadLeft == true) {
				result = space + result;
			} else {
				result = result + space;
			}
		}

		return result;

	}

	public static String justifyLine(String msg, int lengthAdjust,
			String delimiter) {
		String result = msg;

		int lengthWord = msg.length();
		int remainSpace = 0;

		if (lengthWord < lengthAdjust) {
			remainSpace = lengthAdjust - lengthWord;
			String space = "";

			int leftPad = remainSpace / 2;
			int rightPad = remainSpace - leftPad;

			for (int i = 0; i < leftPad; i++) {
				space = space + delimiter;
			}

			result = space + result;

			for (int i = 0; i < rightPad; i++) {
				space = space + delimiter;
			}

			result = result + space;

		}

		return result;

	}

	public static boolean regexPasswordChecker(String regex, String password) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public static String formatCurrency(String value, String format) {
		double convert = Double.parseDouble(value);

		NumberFormat formatter = new DecimalFormat(format);
		String resultFormat = formatter.format(convert);

		return resultFormat;
	}

	public static String generateAlphaNumeric(int length) {
		String C = "QWERTYUIOPLKJHGFDAZXCVBNM0987654321";
		StringBuffer sb = new StringBuffer(length);
		for (int i = 0; i < length; i++) {
			int idx = r.nextInt(C.length());
			sb.append(C.substring(idx, idx + 1));
		}
		return sb.toString();
	}

	public static String md5(String input) {

		String md5 = null;

		if (null == input)
			return null;

		try {

			// Create MessageDigest object for MD5
			MessageDigest digest = MessageDigest.getInstance("MD5");

			// Update input string in message digest
			digest.update(input.getBytes(), 0, input.length());

			// Converts message digest value in base 16 (hex)
			md5 = new BigInteger(1, digest.digest()).toString(16);

		} catch (NoSuchAlgorithmException e) {

			e.printStackTrace();
		}
		return md5;
	}

	public static String getBaseHostname(String url) {
		String baseHostname = "";
		baseHostname = url.substring(7, url.length() - 1);
		return baseHostname;
	}
}
