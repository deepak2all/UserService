package au.com.deepak.user.cache;

import java.util.List;

import au.com.deepak.user.model.User;

public class UserDataConstants {

	public static List<User> userInfoCache;

	public static List<User> getUserInfoCache() {
		return userInfoCache;
	}

	public static void setUserInfoCache(List<User> userInfoCache) {
		UserDataConstants.userInfoCache = userInfoCache;
	}
	
	public static String JSON_FILE_PATH = "/json/users.json";
	
	public static long dbResponseTime = 5000;
	
}