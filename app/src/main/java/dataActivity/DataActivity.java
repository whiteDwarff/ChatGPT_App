package dataActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import database.DatabaseHelper;
import com.example.apiproject.R;

import java.util.List;

public class DataActivity extends AppCompatActivity {
    private SQLiteDatabase database;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_table);


        setTitle("Q&A");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0aa379")));

        Button btnDeleteAll = findViewById(R.id.btn_delete_all);
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAllMessages();
                tableLayout.removeAllViews();
            }
        });

        // 데이터베이스 초기화
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getReadableDatabase();

        // 테이블 뷰 찾기
        tableLayout = findViewById(R.id.table_layout);

        // 데이터 가져오기
        Cursor cursor = database.query(DatabaseHelper.TABLE, null, null, null, null, null, null);

        int questionIndex = cursor.getColumnIndex(DatabaseHelper.QUESTION);
        int answerIndex = cursor.getColumnIndex(DatabaseHelper.ANSWER);

        // 헤더 추가
        while (cursor.moveToNext()) {
            // 질문 가져오기
            String question = "";
            if (questionIndex != -1) {
                question = cursor.getString(questionIndex);
            }
            // 답변 가져오기
            String answer = "";
            if (answerIndex != -1) {
                answer = cursor.getString(answerIndex);
            }
            // 데이터 행 추가
            addDataRow(question, answer);
        }
        // 커서 닫기
        cursor.close();
    }

    private List<String> answerList; // 답변 데이터 리스트
    private int currentIndex = 0; // 현재 인덱스

    private void addDataRow(String question, String answer) {

        TableRow questionRow = new TableRow(this);
        TableRow answerRow = new TableRow(this);

        TextView questionTextView = createBorderTextView("Question\n\n" + question);
        TextView answerTextView = createBorderTextView("Answer\n\n" + answer);

        questionRow.setPadding(0, 10, 0, 5); // 왼쪽과 오른쪽에 16dp의 패딩 추가
        answerRow.setPadding(0, 5, 0, 70); // 왼쪽과 오른쪽에 16dp의 패딩 추가

        // questionTextView의 너비 확인 및 조정
        questionTextView.post(new Runnable() {
            @Override
            public void run() {
                int maxWidth = tableLayout.getWidth(); // 테이블 레이아웃의 너비
                int textViewWidth = questionTextView.getWidth(); // questionTextView의 너비

                if (textViewWidth > maxWidth) {
                    // questionTextView의 너비가 화면 너비보다 크면
                    questionTextView.setLayoutParams(new TableRow.LayoutParams(maxWidth, TableRow.LayoutParams.WRAP_CONTENT));
                    questionTextView.setEllipsize(null);
                    questionTextView.setSingleLine(false);
                }
            }
        });
        // answerTextView의 너비 확인 및 조정
        answerTextView.post(new Runnable() {
            @Override
            public void run() {
                int maxWidth = tableLayout.getWidth(); // 테이블 레이아웃의 너비
                int textViewWidth = answerTextView.getWidth(); // answerTextView의 너비

                if (textViewWidth > maxWidth) {
                    // answerTextView의 너비가 화면 너비보다 크면
                    answerTextView.setLayoutParams(new TableRow.LayoutParams(maxWidth, TableRow.LayoutParams.WRAP_CONTENT));
                    answerTextView.setEllipsize(null);
                    answerTextView.setSingleLine(false);
                }
            }
        });
        questionRow.addView(questionTextView);
        answerRow.addView(answerTextView);

        tableLayout.addView(questionRow);
        tableLayout.addView(answerRow);
    }
    private TextView createBorderTextView(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setPadding(8, 8, 8, 8);
        textView.setLayoutParams(new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setEllipsize(TextUtils.TruncateAt.END);
        textView.setSingleLine();
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border)); // 테두리 배경 추가

        return textView;
    }
    // db에 저장된 모든 데이터 삭제
    private void deleteAllMessages() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE, null, null);
        db.close();

        Toast.makeText(this, "질문내역이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }
}
