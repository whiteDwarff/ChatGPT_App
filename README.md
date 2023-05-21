![header](https://capsule-render.vercel.app/api?type=waving&color=timeGradient&text=OpenAPI를%20사용하여%20Android%20App%20개발하기📱&animation=twinkling&fontSize=23&fontAlignY=40&fontAlign=70&height=250&align=center)
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
  - OpenAI에서 개발한 대규모 언어 모델로, GPT-3.5 아키텍쳐를 기반으로 학습되었으며 사람과 자연어로 대화흐는 인공지능 챗봇이다.<br>
  - 다양한 주제와 질문에 대해 이해하고 답변을 생성할 수 있다.<br>
  - 인터넷에서 수집한 대규모의 텍스트 데이터를 기반으로 학습하여, 문법, 문맥, 상식 등 다양한 언어적 특성을 이해하고 다양한 주제에 대해 유창하게 대화할 수 있다.<br>
  - ChatGPT가 나오기 이전에도 자연어로 사람과 대화할 수 있는 AI들이 있었으나 미리 학습된 규칙과 패턴에 의해서만 대화가 가능한 수준이었다.<br>
<br>
 <img width="1329" alt="ppt1" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/061e600c-2216-4247-ae13-55447b3e9c85">
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


<img width="1325" alt="ppt4" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/e9b1ff84-a3e5-4ad5-acef-5a0af5aa1222">
<img width="1328" alt="ppt5" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/756cca97-eb52-4869-b17c-bb6e00df16a0">
<img width="1325" alt="ppt6" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/b1d4443f-98f4-4444-a4c7-5f8e3a42c9d6">
<img width="1326" alt="ppt7" src="https://github.com/whiteDwarff/ChatGPT_App/assets/115057117/a12334e6-bc66-4745-8e40-6c5cff060795">
