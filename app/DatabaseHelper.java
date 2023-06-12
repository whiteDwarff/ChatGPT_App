import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "messages_table";
    private static final String COLUMN_QUESTION = "question";
    private static final String COLUMN_ANSWER = "answer";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_QUESTION + " TEXT, " +
                COLUMN_ANSWER + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 데이터베이스 버전이 업그레이드되었을 때 필요한 작업을 수행할 수 있습니다.
        // 예를 들어, 기존 테이블을 삭제하고 새로운 테이블을 생성할 수 있습니다.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}




