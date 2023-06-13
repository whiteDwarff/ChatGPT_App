package com.example.apiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import adapter.MessageAdapter;
import dataActivity.DataActivity;
import database.DatabaseHelper;
import model.Message;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionSpec;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recycler_view;
    TextView tv_welcome;
    EditText et_msg;
    ImageButton btn_send;

    List<Message> messageList;
    MessageAdapter messageAdapter;

    Button btnDataActivity;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client;

    private static final String MY_SECRET_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("ChatGPT");

        btnDataActivity = findViewById(R.id.btn_data_activity);
        btnDataActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DataActivity로 화면 전환
                Intent intent = new Intent(MainActivity.this, DataActivity.class);
                startActivity(intent);
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#0aa379")));

        client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)  // 연결 실패 시 재시도 설정
                .connectionSpecs(Arrays.asList(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))  // 인증서 확인
                .build();

        recycler_view = findViewById(R.id.recycler_view);
        tv_welcome = findViewById(R.id.tv_welcome);
        et_msg = findViewById(R.id.et_msg);
        btn_send = findViewById(R.id.btn_send);

        recycler_view.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        recycler_view.setLayoutManager(manager);

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList);
        recycler_view.setAdapter(messageAdapter);

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String question = et_msg.getText().toString().trim();
                addToChat(question, Message.SENT_BY_ME);
                et_msg.setText("");
                callAPI(question);
                tv_welcome.setVisibility(View.GONE);
            }
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recycler_view.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }

    void addResponse(String response, String question) {
        messageList.remove(messageList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);

        if (response != null && !response.isEmpty()) {
            // SQLite에 질문과 답변 저장
            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
            // 데이터베이스에 쓰기 권한을 추가하여 객체를 반환
            SQLiteDatabase db = databaseHelper.getWritableDatabase();

            // 데이터를 테이블에 삽입 또는 갱신할 때 사용되는 클래스, 해당 객체를 db에 저장
            ContentValues answerValues = new ContentValues();
            answerValues.put("question", question);  // 질문 내용 저장
            answerValues.put("answer", response);  // 답변 저장

            long answerRowId = db.insert("messages", null, answerValues);

            db.close();
        }
    }

    void callAPI(String question) {
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        // 대화 메시지를 담기위한 JSON 배열 생성, GPT와 user의 대화를 담는 객체를 생성
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            // GPT의 역할 설정, user의 응답을 받는다
            baseAi.put("role", "user");
            // GPT의 대화 모델 설정
            baseAi.put("content", "안녕하세요! 저는 도와주는 착한 AI Taddy Bear 입니다! 궁금한 거나 도움이 필요하면 언제든지 말해주세요!");
            // user의 질문, GPT에게 전달
            userMsg.put("role", "user");
            // GPT의 응답을 생성하고 반환
            userMsg.put("content", question);
            // array로 담아서 한번에 보낸다
            arr.put(baseAi);
            arr.put(userMsg);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JSONObject object = new JSONObject();

        try {
            // model을 chatGPT 3.5로 설정
            object.put("model", "gpt-3.5-turbo");
            // 메시지들을 포함하는 JSON 배열을 설정
            object.put("messages", arr);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        // request 객체 생성
        Request request = new Request.Builder()
                // 요청이 전송될 url 설정
                .url("https://api.openai.com/v1/chat/completions")
                // Open Api 발급받은 key 넣어주기
                .header("Authorization", "Bearer " + MY_SECRET_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to " + e.getMessage(), question);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 서버의 응답이 성공적으로 도착했는지 확인
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        // JSON 형태로 받은 응답 데이터를 분석
                        JSONObject json = new JSONObject(responseData);
                        JSONArray choices = json.getJSONArray("choices");
                        JSONObject chatAI = choices.getJSONObject(0);
                        JSONObject message = chatAI.getJSONObject("message");
                        String content = message.getString("content").replaceAll("<.*?>", "");
                        addResponse(content, question);
                    } catch (JSONException e) {
                        addResponse("Failed to parse response due to " + e.getMessage(), question);
                    }
                } else {
                    addResponse("Response was not successful. Error code: " + response.code(), question);
                }
            }
        });
    }
}
