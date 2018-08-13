package com.gsy.translate.my;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

/**
 * Created by Think on 2018/1/21.
 */

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)){
            DownloadManager manager = (DownloadManager)context.getSystemService(Context.DOWNLOAD_SERVICE);
            long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID,-1);
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(id);
            Cursor c = manager.query(query);
            if(c.moveToNext()){
                String filename = c.getString(c.getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME));
                if(filename != null){
                    //执行安装
                    Intent intent_ins = new Intent(Intent.ACTION_VIEW);
                    intent_ins.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent_ins.setDataAndType(Uri.parse("file://" + filename),"application/vnd.android.package-archive");
                    context.getApplicationContext().startActivity(intent_ins);
                }
            }
        }
    }
}
