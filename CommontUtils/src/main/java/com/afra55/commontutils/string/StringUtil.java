package com.afra55.commontutils.string;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class StringUtil {

	/**
	 * 封装链接上的键值对并保存到hashmap
	 *
	 * @param url
	 * @return HashMap<key,value>
	 */
	public static HashMap<String, String> handleUrlParams(String url) {
		HashMap<String, String> hm = new HashMap<String, String>();
		if (url.contains("?") && url.indexOf("?") != url.length() - 1) {
			String params = url.substring(url.indexOf("?") + 1);
			if (params.contains("&")) {
				String[] paramArr = params.split("&");
				for (int i = 0; i < paramArr.length; i++) {
					String str = paramArr[i];
					if ((str.split("=")).length > 1) {
						try {
							hm.put(str.split("=")[0], URLDecoder.decode(str.split("=")[1], "UTF-8"));
						} catch (UnsupportedEncodingException e) {
						}
					}
				}
			} else {
				if (params.contains("=")) {
					try {
						hm.put(params.split("=")[0], URLDecoder.decode(params.split("=")[1], "UTF-8"));
					} catch (UnsupportedEncodingException e) {
					}
				}
			}
		}
		return hm;
	}
	
	public static String getPercentString(float percent) {
		return String.format(Locale.US, "%d%%", (int) (percent * 100));
	}
	/**
	 * 删除字符串中的空白符
	 *
	 * @param content
	 * @return String
	 */
	public static String removeBlanks(String content) {
		if (content == null) {
			return null;
		}
		StringBuilder buff = new StringBuilder();
		buff.append(content);
		for (int i = buff.length() - 1; i >= 0; i--) {
			if (' ' == buff.charAt(i) || ('\n' == buff.charAt(i)) || ('\t' == buff.charAt(i))
					|| ('\r' == buff.charAt(i))) {
				buff.deleteCharAt(i);
			}
		}
		return buff.toString();
	}
	/**
	 * 获取32位uuid
	 *
	 * @return
	 */
	public static String get32UUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static boolean isEmpty(String input) {
		return TextUtils.isEmpty(input);
	}
	
	/**
	 * 生成唯一号
	 *
	 * @return
	 */
	public static String get36UUID() {
		UUID uuid = UUID.randomUUID();
		String uniqueId = uuid.toString();
		return uniqueId;
	}
	
	public static String makeMd5(String source) {
		return MD5.getStringMD5(source);
	}
	
    public static final String filterUCS4(String str) {
		if (TextUtils.isEmpty(str)) {
			return str;
		}

		if (str.codePointCount(0, str.length()) == str.length()) {
			return str;
		}

		StringBuilder sb = new StringBuilder();

		int index = 0;
		while (index < str.length()) {
			int codePoint = str.codePointAt(index);
			index += Character.charCount(codePoint);
			if (Character.isSupplementaryCodePoint(codePoint)) {
				continue;
			}

			sb.appendCodePoint(codePoint);
		}

		return sb.toString();
	}

    /**
     * counter ASCII character as one, otherwise two
     *
     * @param str
     * @return count
     */
    public static int counterChars(String str) {
        // return
        if (TextUtils.isEmpty(str)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            int tmp = (int) str.charAt(i);
            if (tmp > 0 && tmp < 127) {
                count += 1;
            } else {
                count += 2;
            }
        }
        return count;
    }
}
