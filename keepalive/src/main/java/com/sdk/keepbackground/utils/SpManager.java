package com.sdk.keepbackground.utils;


/**
 * 
 * SharedPreferences 对象管理类
 *
 */
public class SpManager extends SPUtils {

	private static final String SPNAME = "tourguide_sp";

	private static SpManager instance;

	private SpManager(String spName) {
		super(spName);
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public static SpManager getInstance(){
		if(instance == null) {
			instance = new SpManager(SPNAME);
		}
		return instance;
	}

	/**
	 * 
	 * 存储的SharedPreferences的键
	 * 
	 * @author Administrator
	 *
	 */
	public static final class Keys {

		//搜索记录
		public static final String SEARCH_HISTORY = "search_history";

		//搜索记录
		public static final String SEARCH_CITYSCENIC_HISTORY = "search_cityscenic_history";

		//是否已经签到
		public static final String ISSIGN = "issign";

		//签到时间
		public static final String SIGNTIME = "signtime";

		//用户编号
		public static final String USERNUMBER = "userNumber";

		//用户token
		public static final String USERTOKEN = "userToken";

		//是否已执行过白名单操作
		public static final String  SP_IS_ACTION_WHITE_POWER = "isDoPowerWhite";
		//是否开启过悬浮窗权限
		public static final String  IS_HINT_SYSTEM_WINDOW = "isDoWindowWhite";
		public static final String  WORK_SERVICE = "workService";

	}

}
