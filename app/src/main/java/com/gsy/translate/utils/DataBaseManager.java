package com.gsy.translate.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Think on 2018/2/8.
 */

public class DataBaseManager {

    static SQLiteDatabase sqLiteDatabase;

    Context context;

    String dbPath;

    static DataBaseManager mInstance;

    DataBaseManager(Context context){
        this.context = context;
        dbPath = context.getFilesDir().getAbsolutePath()+ File.pathSeparatorChar+"sql.db";
    }

    public static DataBaseManager with(Context context){
        if(mInstance==null){
            mInstance = new DataBaseManager(context);
        }
        return mInstance;
    }

    public SQLiteDatabase getDataBase(){
        if(sqLiteDatabase!=null){
            return sqLiteDatabase;
        }
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath,null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            if(sqLiteDatabase!=null)
                return sqLiteDatabase;
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        copyAssetsToFilesystem("sql.db",dbPath);
        try {
            sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath,null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            if(sqLiteDatabase!=null)
                return sqLiteDatabase;
        }catch (SQLiteException e){
            e.printStackTrace();
        }
        return null;
    }

    private boolean copyAssetsToFilesystem(String assetsSrc, String des){
        InputStream istream = null;
        OutputStream ostream = null;
        try{
            AssetManager am = context.getAssets();
            istream = am.open(assetsSrc);
            ostream = new FileOutputStream(des);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = istream.read(buffer))>0){
                ostream.write(buffer, 0, length);
            }
            istream.close();
            ostream.close();
        }
        catch(Exception e){
            e.printStackTrace();
            try{
                if(istream!=null)
                    istream.close();
                if(ostream!=null)
                    ostream.close();
            }
            catch(Exception ee){
                ee.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
