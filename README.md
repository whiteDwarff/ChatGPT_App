![header](https://capsule-render.vercel.app/api?type=waving&color=timeGradient&text=OpenAPI를%20사용하여%20Android%20App%20개발하기📱&animation=twinkling&fontSize=23&fontAlignY=40&fontAlign=70&height=250&width=1325&align=center)
<br>
<br>
 <div align="center">
  <img src="https://img.shields.io/badge/AndroidStudio-3DDC84?style=flat&logo=android&logoColor=white"/>
  <img src="https://img.shields.io/badge/ChatGpt-412991?style=flat&logo=openai&logoColor=white"/>
  <img src="https://img.shields.io/badge/OpenAPI-6BA539?style=flat&logo=openapiinitiative&logoColor=white"/>
</div>
<br>
<br>
<br>

  > ### 1. Chat GPT란?
  - OpenAI에서 개발한 대규모 언어 모델로, GPT-3.5 아키텍쳐를 기반으로 학습되었으며 사람과 자연어로 대화하는 인공지능 챗봇이다.<br>
  - 다양한 주제와 질문에 대해 이해하고 답변을 생성할 수 있다.<br>
  - 인터넷에서 수집한 대규모의 텍스트 데이터를 기반으로 학습하여, 문법, 문맥, 상식 등 다양한 언어적 특성을 이해하고 다양한 주제에 대해 유창하게 대화할 수 있다.<br>
  - ChatGPT가 나오기 이전에도 자연어로 사람과 대화할 수 있는 AI들이 있었으나 미리 학습된 규칙과 패턴에 의해서만 대화가 가능한 수준이었다.<br>
<br>
 <img width="1325" alt="ppt1" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/061e600c-2216-4247-ae13-55447b3e9c85">
<br><br><br>
<hr>
<br><br>

 > ### 2. ChatGPT API Key 발급받기
 - OpenAI 회원가입 및 로그인
 - API 클릭
 - View API keys 클릭
 - Create new secret key 클릭<br><br>
 OpenAI 홈페이지 : <a href="https://openai.com/">https://openai.com/</a>
<img width="1325" alt="ppt2" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/00a36e9b-0361-4ddf-be50-923e88aa74d7">
<br><br><br> 
<hr>
<br><br>

 > ### 3-1. OkHttp 라이브러리 추가
 ```ruby
 dependencies {
    implementation 'androidx.appcompat:appcompat:1.6.1'
    implementation 'com.google.android.material:material:1.9.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.1.4'
    testImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.5'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'

    implementation 'com.squareup.okhttp3:okhttp:4.10.0'
}
```
<br>

*__Gradle Script > Build.gradle(Module :app) > dependencies > implementation 'com.squareup.okhttp3:okhttp:4.10.0' 추가__*<br>

#### OkHttp란?
- OkHttp는 안드로이드와 자바용으로 개발된 오픈 소스 HTTP 클라이언트 라이브러리.
- Http에 요청을 보내고 응답을 받는 기능을 제공
- 서버와의 통신, API 호출 등 다양한 네트워크 작업을 수행 가능
<br><br>

> ### 3-2. 애플리케이션의 인터넷 연결 권한 설정
```ruby
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />
```
<br>

*__Manifests > AndroidManifest.xml 추가__*<br>

- 애플리케이션이 인터넷에 접근할 수 있는 권한을 얻게 되며, 서버와 통신하거나 데이터를 주고받을 수 있음.<br>
<img width="1324" alt="ppt3" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/7e8430ae-6166-4268-97a5-6ea56eabfe13">
<br><br><br> 
<hr>
<br><br>

> ### 4-1. ChatGPT의 답변시간 동안 다음과 같은 메시지를 출력하여 대화를 자연스럽게 진행할 수 있음.
```ruby
messageList.add(new Message("...", Message.SENT_BY_BOT));
```
<br> 

> ### 4-2. 사용자와 ChatGPT의 대화 메시지를 담기위한 JSON 배열 생성
```ruby
JSONArray arr = new JSONArray();
```
<br>

> ### 4-3. ChatGPT와 user의 대화를 담는 객체를 생성
```ruby
JSONObject baseAi = new JSONObject();
JSONObject userMsg = new JSONObject();
try {
   baseAi.put("role", "user");
   baseAi.put("content",  "안녕하세요! 저는 도와주는 착한 AI Taddy Bear 입니다! 궁금한 거나 도움이 필요하면 언제든지 말해주세요!");
   // user의 질문을 GPT에게 전달 -> GPT의 응답을 
   userMsg.put("role", "user");
   userMsg.put("content", question);
   arr.put(baseAi);
   arr.put(userMsg);
```
<br>
<img width="1325" alt="ppt4" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/e9b1ff84-a3e5-4ad5-acef-5a0af5aa1222">
<br><br><br> 
<hr>
<br><br>

> ### 5. ChatGPT 모델을 사용하여 OpenAI API를 연결하기
- 'model'을 'gpt-3.5-turbo'로 설정
- arr이 object 객체를 문자열로 변환
- 요청이 전송될 url 설정
- API에 요청을 보내기 위해 인증키 'MY_SECRET_KEY'를 사용하여 'Authorization' 헤더를 설정
  *(서버에게 클라이언트의 신원을 인증, 권한을 확인)*
- '.post(body)'로 HTTP POST 메서드를 사용하여 요청의 본문에 body를 포함
- '.build()'를 호출하여 Request 객체를 완성

```ruby
 JSONObject object = new JSONObject();
 try {
      object.put("model", "gpt-3.5-turbo");
      object.put("messages", arr);
 } catch (JSONException e){
      e.printStackTrace();
 }
 RequestBody body = RequestBody.create(object.toString(), JSON);
 Request request = new Request.Builder()
      .url("https://api.openai.com/v1/chat/completions")
      .header("Authorization", "Bearer "+ MY_SECRET_KEY)
      .post(body)
       .build();
```
<br>
<hr>
<br><br>

> ### 6. HTTP 응답받기
- HTTP 응답이 성공적으로 수신되었는지 확인
- HTTP 응답을 문자열로 파싱하여 JSONObject로 변환 후 객체로 생성
- 객체에서 'choice' 키에 해당하는 배열을 가져온다
- 'choice' 배열에서 첫번째 요소인 'message' 객체를 가져온 뒤 'result' 변수에 저장
- 'result'의 앞뒤 공백을 제거 후 반환 
<br>
<img width="1325" alt="ppt5" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/756cca97-eb52-4869-b17c-bb6e00df16a0">
<br><br><br>
<hr>
<br><br>

> ### 7. App 실행하기
<img width="1325" alt="ppt6" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/b1d4443f-98f4-4444-a4c7-5f8e3a42c9d6">
<img width="1325" alt="ppt7" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/a12334e6-bc66-4745-8e40-6c5cff060795">
