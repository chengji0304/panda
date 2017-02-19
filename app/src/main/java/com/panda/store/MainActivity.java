package com.panda.store;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.panda.store.entity.DownInfo;
import com.panda.store.service.DownService;
import com.panda.store.utils.Config;
import com.panda.store.utils.FileUtils;
import com.panda.store.utils.ListDataSave;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.Vitamio;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    public static final String BEGIN="com.panda.store.action.START";
   public static final String SUCCESS="com.panda.store.action.SUCCESS";
    public static final String FAIL="com.panda.store.action.FAIL";
    private ListDataSave downlist;

    List<String> list;
    ArrayList<String> downurl;
    ArrayList<DownInfo> dflist;
    File appStore;
    File down;
    File play;
    int ViewNum=0;
    String[] downurls;
    String[] fileNames;
    String[] savepath;
    ArrayList<String> save;
    String[] playpath;
    VideoView mVideoView;
    ArrayList<String> plays;
   // boolean isDown=false;//不需要下载
    DownReciver receiver;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Vitamio.isInitialized(getApplicationContext());
        setContentView(R.layout.activity_main);
        mVideoView = (VideoView) findViewById(R.id.surface_view);
        appStore= FileUtils.getDir();
        down=FileUtils.getDownFile();
        play=FileUtils.getPlayFile();

        save=new ArrayList<String>();
        downurl=new ArrayList<String>();
        dflist=new ArrayList<DownInfo>();
        downlist=new ListDataSave(this, Config.DOWNLIST);
        list=downlist.getDataList(Config.TAG_DOWN);
        downurls=new String[list.size()];
        fileNames=new String[list.size()];
        savepath=new String[list.size()];
        for(String s:list){
            Log.d("s", s+"");
            DownInfo df= JSON.parseObject(s, DownInfo.class);
            dflist.add(df);
        }
        for(int i=0;i<downurls.length;i++){
            downurls[i]=dflist.get(i).getUrl();
            fileNames[i]=dflist.get(i).getFileName();
            savepath[i]=down.getAbsolutePath()+"/"+fileNames[i];
           // playpath[i]=play.getAbsolutePath()+"/"+fileNames[i];
            File f=new File(savepath[i]);
            if(!f.exists()){
                downurl.add(dflist.get(i).getUrl());
                save.add(down.getAbsolutePath()+"/"+fileNames[i]);
                //isDown=true;//需下载
                Log.d("info",dflist.get(i).getUrl()+"");
            }else{
                //isDown=false;//不要下载
                Log.d("info","无需下载");
            }
        }
        if(downurl.size()>0){
            Log.e("logo", "下载");
            receiver = new DownReciver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(MainActivity.BEGIN);
            filter.addAction(MainActivity.SUCCESS);
            filter.addAction(MainActivity.FAIL);
            registerReceiver(receiver, filter);
            intent = new Intent(MainActivity.this, DownService.class);
            intent.putStringArrayListExtra("down", downurl);
            intent.putStringArrayListExtra("save", save);
            startService(intent);
        }

    }
    class DownReciver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
           String action=intent.getAction();
            String fileName=intent.getStringExtra("name");
            if(action.equals(MainActivity.BEGIN)){
                Toast.makeText(MainActivity.this,fileName+"开始下载",Toast.LENGTH_SHORT).show();
            } else if(action.equals(MainActivity.FAIL)){
                Toast.makeText(MainActivity.this,fileName+"下载失败",Toast.LENGTH_LONG).show();
            }else if(action.equals(MainActivity.SUCCESS)){
                Toast.makeText(MainActivity.this,fileName+"下载成功",Toast.LENGTH_LONG).show();
               // File[] files=play.listFiles();
                //playpath=new String[files.length];

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


         int size=play.listFiles().length;
         if(size>0){
             playLocad();
         }else{
             playNet();

         }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiver!=null){
            unregisterReceiver(receiver);
            receiver=null ;
        }
        stopService(intent);

    }

    private void  playNet(){
        Toast.makeText(MainActivity.this,"播放网络",Toast.LENGTH_SHORT).show();
        mVideoView.setVideoPath(downurls[0]);

        mVideoView.setMediaController(new MediaController(MainActivity.this));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ViewNum++;
                if(ViewNum>=downurls.length){
                    ViewNum=0;
                }
                String path=downurls[ViewNum];
                mVideoView.setVideoPath(path);
            }
        });
    }
    private void playLocad(){
        Toast.makeText(MainActivity.this,"播放本地",Toast.LENGTH_SHORT).show();
        File[] files=play.listFiles();

//        for(int i=0;i<files.length;i++){
//            for(int j=0;j<fileNames.length;j++){
//                if(files[i].getName().equals(fileNames[j])){
//                   Log.e("info","相同文件名不删除");
//                }else{
//                    FileUtils.deleteFile(files[i].getAbsolutePath());
//                }
//            }
//        }
        playpath=new String[files.length];
        plays=new ArrayList<>();
        for(File file:play.listFiles()){
          //  list.add(file.getAbsolutePath());
            plays.add(file.getAbsolutePath());
           // Log.e("palyinfo",file.getAbsolutePath().toString());
        }
        for(int i=0;i<playpath.length;i++){
            playpath[i]=plays.get(i);
        }
        mVideoView.setVideoPath(playpath[0]);

        mVideoView.setMediaController(new MediaController(MainActivity.this));
        mVideoView.requestFocus();
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setPlaybackSpeed(1.0f);
            }
        });
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ViewNum++;
               // Log.e("NUmber",ViewNum+"");
                String[] playpath1=new String[play.listFiles().length];
                ArrayList<String> bofang=new ArrayList<String>();
              //  Log.e("playpa1thlenth",playpath1.length+"");
                for(File file:play.listFiles()){
                    //  list.add(file.getAbsolutePath());
                    bofang.add(file.getAbsolutePath());
                   // Log.e("palyinfo",file.getAbsolutePath().toString());
                }
                for(int i=0;i<playpath1.length;i++){
                    playpath1[i]=bofang.get(i);
                    //Log.e("palypaths",i+":"+playpath1[i].toString());
                }
                if(playpath1.length>playpath.length){
                    ViewNum=0;
                    playpath=new String[playpath1.length];
               //    Log.e("playpath",""+playpath.length);

                    String path=playpath1[ViewNum];
                 //   Log.e("播放path", ViewNum+":"+path);
                    mVideoView.setVideoPath(path);

                }else{

                    if (ViewNum >= playpath1.length) {
                        ViewNum = 0;
                    }
                    String path = playpath1[ViewNum];
                   // Log.e("播放path", ViewNum+":"+path);
                    mVideoView.setVideoPath(path);
                }
            }
        });
    }
}
