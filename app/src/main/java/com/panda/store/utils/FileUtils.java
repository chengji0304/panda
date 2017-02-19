package com.panda.store.utils;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;

public class FileUtils {
	 /**
	  * 创建文件夹目录
	  */
	public static File getDir(){
		File dir=new File(Environment.getExternalStorageDirectory(), "JustStore");
		if(!dir.exists()){
			dir.mkdirs();
		}
		return dir;
	}
    /**
     *  创建下载目录
     */
    public static File getDownFile(){
        File downdir=new File(getDir().getAbsolutePath()+"/Down");
        if(!downdir.exists()){
            downdir.mkdirs();
        }
        return downdir;
    }
    /**
     *  创建 播放目录
     */
    public static File getPlayFile(){
        File downdir=new File(getDir().getAbsolutePath()+"/Play");
        if(!downdir.exists()){
            downdir.mkdirs();
        }
        return downdir;
    }

    /**
     *
     * @param oldPath
     * @param newPath
     */
    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if(oldfile.exists()){
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                BufferedOutputStream bos=new BufferedOutputStream(fs);
                byte[] buffer = new byte[1024*2];
                // int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    // System.out.println(bytesum);
                    bos.write(buffer, 0, byteread);
                }
                bos.flush();
                inStream.close();
                bos.close();

            }
        }catch (Exception e){
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }

    }
        /**
         * 文件大小
         * @param fileS
         * @return
         */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * 删除单个文件
     * @param   fileName   被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static void deleteFile(String fileName) {
       // flag = false;
       File  file = new File(getPlayFile().getAbsolutePath()+fileName);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
           // flag = true;
        }
       // return flag;
    }
    /**
     * 删除所有文件
     * @param path
     * @return
     */
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
          return flag;
        }
        if (!file.isDirectory()) {
          return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
           if (path.endsWith(File.separator)) {
              temp = new File(path + tempList[i]);
           } else {
               temp = new File(path + File.separator + tempList[i]);
           }
           if (temp.isFile()) {
              temp.delete();
           }
           if (temp.isDirectory()) {
              delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
           //   delFolder(path + "/" + tempList[i]);//再删除空文件夹
              flag = true;
           }
        }
        return flag;
      }

}
