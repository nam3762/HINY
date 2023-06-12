package com.example.hiny;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/* ***********************
 * 클래스명 : DataBaseHelper
 * 이름 : 윤석현
 * 학번 : 2019038011
 * 설명 : 데이터 베이스를 불러오기 위한 기본 설정을 관리하는 클래스
 * ************************/
public class DataBaseHelper extends SQLiteOpenHelper{
    private final static String TAG = "DataBaseHelper";
    // database 의 파일 경로
    private static String DB_PATH = "";
    private static String DB_NAME = "em_store.db";
    private SQLiteDatabase mDataBase;
    private Context mContext;

    /* ***********************
     * 함수명 : DataBaseHelper
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : DataBaseHelper의 생성자
     * ************************/

    public DataBaseHelper(Context context) {
        super(context,DB_NAME,null,1);


        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        this.mContext = context;
        dataBaseCheck();
    }

    /* ***********************
     * 함수명 : dataBaseCheck
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 데이터베이스를 검사하기위한 함수
     * ************************/
    private void dataBaseCheck() {
        File dbFile = new File(DB_PATH + DB_NAME);
        if (!dbFile.exists()) {
            dbCopy();
            Log.d(TAG,"Database is copied.");
        }
    }

    /* ***********************
     * 함수명 : close
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 데이터 베이스를 닫기위한 함수
     * ************************/
    @Override
    public synchronized void close() {
        if (mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 테이블 구조 생성로직
        Log.d(TAG,"onCreate()");
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Toast.makeText(mContext,"onOpen()",Toast.LENGTH_SHORT).show();
        Log.d(TAG,"onOpen() : DB Opening!");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 테이블 삭제하고 onCreate() 다시 로드시킨다.
        Log.d(TAG,"onUpgrade() : DB Schema Modified and Excuting onCreate()");
    }
    /* ***********************
     * 함수명 : dbCopy
     * 이름 : 윤석현
     * 학번 : 2019038011
     * 설명 : 데이터 베이스를 복하하기 위한 함수
     * ************************/
    private void dbCopy() {

        try {
            File folder = new File(DB_PATH);
            if (!folder.exists()) {
                folder.mkdir();
            }

            InputStream inputStream = mContext.getAssets().open(DB_NAME);
            String out_filename = DB_PATH + DB_NAME;
            OutputStream outputStream = new FileOutputStream(out_filename);
            byte[] mBuffer = new byte[1024];
            int mLength;
            while ((mLength = inputStream.read(mBuffer)) > 0) {
                outputStream.write(mBuffer,0,mLength);
            }
            outputStream.flush();;
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dbCopy","IOException 발생함");
        }
    }
}
