package com.example.apiproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
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

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
     OkHttpClient client;


    private static final String MY_SECRET_KEY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("ChatGPT");
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
                String question = et_msg.getText().toString().trim();
                addToChat(question, Message.SENT_BY_ME);
                et_msg.setText("");
                callAPI(question);
                tv_welcome.setVisibility(View.GONE);
            }
        });
    }
    void addToChat(String message, String sentBy){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageList.add(new Message(message, sentBy));
                messageAdapter.notifyDataSetChanged();
                recycler_view.smoothScrollToPosition(messageAdapter.getItemCount());
            }
        });
    }
    void addResponse(String response){
        messageList.remove(messageList.size()-1);
        addToChat(response, Message.SENT_BY_BOT);
    }
    void callAPI(String question){
        messageList.add(new Message("...", Message.SENT_BY_BOT));

        // 대화 메시지를 담기위한 JSON 배열 생성, GPT와 user의 대화를 담는 객체를 생성
        JSONArray arr = new JSONArray();
        JSONObject baseAi = new JSONObject();
        JSONObject userMsg = new JSONObject();
        try {
            // GPT의 역할 설정, user의 응답을 받는다
            baseAi.put("role", "user");
            // GPT의 대화 모델 설정
            baseAi.put("content",  "안녕하세요! 저는 도와주는 착한 AI Taddy Bear 입니다! 궁금한 거나 도움이 필요하면 언제든지 말해주세요!");
            // user의 질문, GPT에게 전달
            userMsg.put("role", "user");
            // GPT의 응답을 생성하고 반환
            userMsg.put("content", question);
            //array로 담아서 한번에 보낸다
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

        } catch (JSONException e){
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(object.toString(), JSON);
        // reuquest 객체 생성
        Request request = new Request.Builder()
                // 요청이 전송될 url 설정
                .url("https://api.openai.com/v1/chat/completions")
                // Open Api 발급받은 key 넣어주기
                .header("Authorization", "Bearer "+ MY_SECRET_KEY)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Failed to load response due to "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                // 서버의 응답이 성공적으로 도착했는지 확인
                if(response.isSuccessful()){
                    JSONObject jsonObject = null;
                    try {
                        // 서버에서 받은 응답 데이터를 JSON 형식으로 파싱 후 문자열로 받은 후 객체로 변환
                        jsonObject = new JSONObject(response.body().string());
                        // JSON 객체에서 "choice" 키에 해당하는 값을 가져옴(GPT의 답변)
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0).getJSONObject("message").getString("content");
                        // 답변의 앞, 뒤 공백을 제거 후 사용자에게 반환
                        addResponse(result.trim());
                    } catch (JSONException e){
                        e.printStackTrace();
                    }
                } else {
                    addResponse("Failed to load response due to "+response.body().string());
                }
            }
        });

    }
}

