package com.ky.utills;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;

import com.ky.beaninfo.AppBean;
import com.redbull.log.Logger;

public class LauncherBiz {

	/**
	 * 获取App
	 * 
	 * @param context
	 * @return
	 */
	public static ArrayList<AppBean> getLauncherApps(Context context) {

		PackageManager pm = context.getPackageManager();
		Intent localIntent = new Intent("android.intent.action.MAIN");
		localIntent.addCategory("android.intent.category.LAUNCHER");
		List localList = pm.queryIntentActivities(localIntent,
				PackageManager.GET_INTENT_FILTERS);

		ArrayList localArrayList = new ArrayList();
		if (localList != null) {
			for (Iterator iterator = localList.iterator(); iterator.hasNext();) {
				ResolveInfo localResolveInfo = (ResolveInfo) iterator.next();
				AppBean localAppBean = new AppBean();
				localAppBean
						.setIcon(localResolveInfo.activityInfo.loadIcon(pm));
				localAppBean.setName(localResolveInfo.activityInfo
						.loadLabel(pm).toString());
				// Logger.log("icon:" +
				// localResolveInfo.activityInfo.loadIcon(pm)
				// + "label:"
				// + localResolveInfo.activityInfo.loadLabel(pm)
				// + "packageName:"
				// + localResolveInfo.activityInfo.packageName);
				localAppBean
						.setPackageName(localResolveInfo.activityInfo.packageName);

				localAppBean
						.setDataDir(localResolveInfo.activityInfo.applicationInfo.publicSourceDir);

				localArrayList.add(localAppBean);
			}
		}
		return localArrayList;
	}

	/**
	 * 静默安装
	 * 
	 * @param cmd
	 * @return
	 */
	public static boolean execCommand(String cmd) {
		Process process = null;

		try {
			process = Runtime.getRuntime().exec(cmd);
			process.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
			Logger.log(e.toString() + "<><><><><><>");
			return false;
		} finally {
			process.destroy();
		}
		return true;
	}
	
	/**
	 * 静默卸载
	 */
	private void uninstall2(String packageName) {  
        String[] args = { "pm", "uninstall", packageName};  
        String result = null;  
        ProcessBuilder processBuilder = new ProcessBuilder(args);  
        ;  
        Process process = null;  
        InputStream errIs = null;  
        InputStream inIs = null;  
        try {  
            ByteArrayOutputStream baos = new ByteArrayOutputStream();  
            int read = -1;  
            process = processBuilder.start();  
            errIs = process.getErrorStream();  
            while ((read = errIs.read()) != -1) {  
                baos.write(read);  
            }  
            baos.write('\n');  
            inIs = process.getInputStream();  
            while ((read = inIs.read()) != -1) {  
                baos.write(read);  
            }  
            byte[] data = baos.toByteArray();  
            result = new String(data);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (errIs != null) {  
                    errIs.close();  
                }  
                if (inIs != null) {  
                    inIs.close();  
                }  
            } catch (Exception e) {  
                e.printStackTrace();  
            }  
            if (process != null) {  
                process.destroy();  
            }  
        }  
	}
	

	public static boolean startApk(String packageName, String activityName) {
		boolean isSuccess = false;

		String cmd = "am start -n " + packageName + "/" + activityName + " \n";
		try {
			Process process = Runtime.getRuntime().exec(cmd);

			isSuccess = waitForProcess(process);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return isSuccess;
	}

	private static boolean waitForProcess(Process p) {
		boolean isSuccess = false;
		int returnCode;
		try {
			returnCode = p.waitFor();
			switch (returnCode) {
			case 0:
				isSuccess = true;
				break;

			case 1:
				break;

			default:
				break;
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return isSuccess;
	}

	/**
	 * 安装应用
	 */
	public static void installPackage(Context context, String updateFile) {
		File f = new File(updateFile);
		Uri uri = Uri.fromFile(f); // 获取文件的Uri
		Intent installIntent = new Intent(Intent.ACTION_VIEW);
		installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		installIntent.setDataAndType(uri,
				"application/vnd.android.package-archive");// 设置intent的数据类型
		context.startActivity(installIntent);
	}

}
