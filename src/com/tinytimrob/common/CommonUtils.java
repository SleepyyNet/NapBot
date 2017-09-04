package com.tinytimrob.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Random utilities
 * @author Robert Dennington
 */
public class CommonUtils
{
	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
	static
	{
		dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
	}
	private static final Logger log = LogWrapper.getLogger();
	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
	public static final Charset charsetUTF8 = Charset.forName("UTF-8");

	/**
	 * Returns the SHA1 hash of a file
	 * @param file The file
	 * @return That file's SHA1 hash
	 */
	public static String getFileHashSHA1(File file)
	{
		FileInputStream stream = null;
		try
		{
			stream = new FileInputStream(file);
			return DigestUtils.sha1Hex(stream);
		}
		catch (Throwable e)
		{
			log.error("Failed to get file hash for " + (file == null ? "null file" : file.getAbsolutePath()), e);
			return null;
		}
		finally
		{
			try
			{
				stream.close();
			}
			catch (IOException e)
			{
				log.warn("Failed to close stream!", e);
			}
		}
	}

	/**
	 * Returns whether or not a string is null or empty
	 * @param s The string
	 * @return whether or not it is null or empty
	 */
	public static boolean isNullOrEmpty(String s)
	{
		return s == null || s.isEmpty();
	}

	/** 
	 * Converts epoch timestamp to human readable format 
	 * @param timestamp The timestamp to convert
	 * @return A human readable version of the timestamp
	 */
	public static String convertTimestamp(long timestamp)
	{
		return dateFormatter.format(new Date(timestamp));
	}

	/** 
	 * Converts database timestamp to human readable format 
	 * @param timestamp The timestamp to convert
	 * @return A human readable version of the timestamp
	 */
	public static String convertTimestamp(Timestamp timestamp)
	{
		return dateFormatter.format(timestamp);
	}

	/**
	 * Format a percentage
	 * @param a Numerator
	 * @param b Denominator
	 * @param digits How many digits after the decimal point to return
	 * @return The formatted percentage
	 */
	public static String formatPercentage(float a, float b, int digits)
	{
		float c = b == 0 ? 0 : a / b;
		return String.format("%." + digits + "f", c * 100) + "%";
	}
}
