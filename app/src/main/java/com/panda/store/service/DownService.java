package com.panda.store.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.panda.store.MainActivity;
import com.panda.store.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by mrpanda on 11/29/16.
 */

public class DownService extends Service {
    HttpUtils http = new HttpUtils();
    ArrayList<String> downurl;
    ArrayList<String> savepath;
    String[] downurls;
    String[] savepaths;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        downurl=intent.getStringArrayListExtra("down");
        savepath=intent.getStringArrayListExtra("save");
         downurls=new String[downurl.size()];
         savepaths=new String [savepath.size()] ;
        Log.d("down",downurl.toString());
        Log.d("save",savepath.toString());
        for(int i=0;i<downurl.size();i++){
            downurls[i]=downurl.get(i);
            savepaths[i]=savepath.get(i);
            final String fileName= savepaths[i].substring(savepaths[i].lastIndexOf("/")+1);
            final String deletepath=savepaths[i];
            com.lidroid.xutils.http.HttpHandler<File> handler=http.download(downurls[i],savepaths[i], true,true,new RequestCallBack<File>() {
                @Override
                public void onStart() {
                    super.onStart();
                    Log.d("info","开始下载");
                    Intent begin=new Intent(MainActivity.BEGIN);
                    begin.putExtra("name",fileName);
                    sendBroadcast(begin);
                }

                @Override
                public void onSuccess(ResponseInfo<File> arg0) {
                    // TODO Auto-generated method stub
                    Log.d("info","下载成功");
                   // File oldFile=new File(FileUtils.getDownFile().getAbsolutePath()+"/"+fileName);
//                    if(file!=null){
//                     file.renameTo(new File(FileUtils.getPlayFile().getAbsolutePath()+"/"+fileName));
                    //                    }
                    String newFilePath=FileUtils.getPlayFile().getAbsolutePath()+"/"+fileName;
                     String oldPath=FileUtils.getDownFile().getAbsolutePath()+"/"+fileName;
                    FileUtils.copyFile(oldPath,newFilePath);
                    Intent success=new Intent(MainActivity.SUCCESS);
                    success.putExtra("name",fileName);
                    sendBroadcast(success);

                }

                @Override
                public void onFailure(HttpException arg0, String arg1) {
                    // TODO Auto-generated method stub
                    Log.d("info","下载失败");
                    delete(deletepath);
                    Intent fail=new Intent(MainActivity.FAIL);
                    fail.putExtra("name",fileName);
                    sendBroadcast(fail);
                }
            });
        }




     return super.onStartCommand(intent,flags,startId);

    }

    private void delete(String deletepath) {
        File f=new File(deletepath);
        if(!f.exists()){
            f.delete();
        }
    }
}
