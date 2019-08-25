package com.sdk.keepbackground.utils;

import android.text.TextUtils;

import com.sdk.keepbackground.KeepAliveApplication;
import com.sdk.keepbackground.work.DaemonEnv;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/8/2
 *     desc  : App相关工具类
 * </pre>
 */
public class AppUtils {

//    public static void installApk(Context context, File file) {
//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri data;
//        // 判断版本大于等于7.0
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            // "net.csdn.blog.ruancoder.fileprovider"即是在清单文件中配置的authorities
//            data = FileProvider.getUriForFile(context, "org.sdk.coolfar_sdk.fileprovider", file);
//            // 给目标应用一个临时授权
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        } else {
//            data = Uri.fromFile(file);
//        }
//        intent.setDataAndType(data, "application/vnd.android.package-archive");
//        context.startActivity(intent);
//    }

//    public static void upDateApp(Context context, File file) {
//        installApk(context, file);
//        android.os.Process.killProcess(android.os.Process.myPid());
//    }

    private static final String KEY_SERIALNUMBER = "key_serialnumber";

    /**
     * uuid  获取uuid
     * @return
     */
    public static String getUUID() {
        String uuid = AndroidUtil.getMacAddress(DaemonEnv.app);
        if(TextUtils.isEmpty(uuid) || "02:00:00:00:00:00".equals(uuid)) {
            uuid = AndroidUtil.getSerialNumber();
        }
        if(TextUtils.isEmpty(uuid)) {
            uuid = SpManager.getInstance().getString(KEY_SERIALNUMBER);
            if(TextUtils.isEmpty(uuid)) {
                uuid = RandomUtil.getRandom(RandomUtil.NUMBERS_AND_LETTERS, 32);
                SpManager.getInstance().putString(KEY_SERIALNUMBER, uuid);
            }
        }
        return uuid;
    }

    public static void checkUUID() {
        String localUUID = SpManager.getInstance().getString(KEY_SERIALNUMBER);
        if(!TextUtils.isEmpty(localUUID) && !localUUID.equals(getUUID())) {
            SpManager.getInstance().putString(KEY_SERIALNUMBER, null);
        }
    }

}