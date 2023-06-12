package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat_db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE = "messages";
    public static final String ID = "_id";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";

    // 생성자 함수, 부모 클래스의 생성자를 호출하여 데이터 베이스 생성
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 데이터베이스가 처음 생성될 때 호출, SQL 쿼리를 실행하여 테이블 만들기
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                QUESTION + " TEXT, " +
                ANSWER + " TEXT)";
        db.execSQL(createTableQuery);
    }

    // 데이터베이스 버전이 업그레이드 되면 기존 테이블 삭제 후 새로운 테이블 생성
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // 모든 데이터 삭제
    public void deleteAllMessages() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }
}