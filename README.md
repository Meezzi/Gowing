# 🪽 Gowing
**지역에 기반하여 익명으로 소통할 수 있는 SNS 앱**

<br>

## ✨ 프로젝트 소개
동네에서 일어나는 일들이 궁금하신가요?
익명으로 자유롭게 대화해봐요 :)

지역 주민들과 함께 다양한 이야기를 나누고, 정보를 공유할 수 있는 익명 커뮤니티 앱!
궁금했던 동네 소식부터 일상 고민까지, 여러분의 이야기를 기다리고 있어요.

<br>

# 🤔 서비스 개요

- **구글 로그인 및 GPS 기반**
    - 구글 로그인 후 사용자의 GPS 위치를 기반으로 지역 정보를 불러옵니다.
    - 해당 지역 게시판이 자동으로 생성되며, 같은 지역 사용자들과 자유롭게 소통할 수 있습니다.
- **게시물 및 채팅 기능**
    - 게시물을 작성하고 필요하면 사진도 업로드 가능하며, 1:1 채팅 기능을 통해 작성자와 직접 대화할 수 있습니다.
- **익명성과 프로필 설정**
    - 익명성을 보장하며, 최초 로그인 시 프로필 사진과 닉네임을 설정할 수 있습니다.
 
<br>

# 사용 기술

**`Kotlin`** **`Compose`** **`MVVM`** **`Hilt`** **`Firebase`** **`Coil` `Jetpack Navigation` `Google Login`** **`Credential Manager`** **`Flow`** **`Rottie` `GitHub`**

<br>

# 🔧 주요 기능

### **1) 구글 로그인 화면**

<img src="https://github.com/user-attachments/assets/9228cdbe-6047-4267-a6e7-0171a610dd02" width="200" height="400"/>

<br>

- **Lottie 애니메이션**
    - Lottie를 활용하여 새가 움직이는 애니메이션 추가
- **구글 로그인**
    - 구글 계정을 통해 앱에 로그인할 수 있음

<br>

### 2) 프로필 설정

<div>
  <img src="https://github.com/user-attachments/assets/c1f72972-9105-434c-afb5-477bf76c8f8a" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/948daaf2-8c89-4a56-82a4-775dca8fc5a9" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/49863c1d-d8df-4c19-bda7-ba3285285894" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/12edd3ad-653f-4011-8277-823e07c4ce3a" width="200" height="400"/>
</div>

- **이미지 설정**
    - 중앙의 기본 프로필 이미지를 클릭하면 갤러리에서 이미지 선택 가능
- **닉네임 설정**
    - 사용자가 원하는 닉네임을 입력할 수 있으며, 2자 이상 15자 이하로 설정해야 함
    - 중복된 닉네임은 허용하지 않음
- **버튼 활성화**
    - 입력값이 조건에 맞지 않으면 저장 버튼이 비활성화 상태를 유지하며, 조건을 충족하면 버튼 활성화
 
<br>

### 3) 필수 권한 안내

<div>
  <img src="https://github.com/user-attachments/assets/8053d1d3-c6ac-484d-bbf4-c008738384bc" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/f42e5a6f-7e2f-4362-98ec-954bf19d74fb" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/4e7fbea5-2bc1-45ca-8f68-73e25c4fedcb" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/57b0f9fc-4c70-4ee8-8ce3-fb7091bd1aae" width="200" height="400"/>
</div>

- **위치 권한 안내**
    - 위치 권한이 필요한 이유를 설명하며, 확인 버튼을 클릭하면 위치 권한 요청 창이 표시됨
    - 만약, 허용 안함을 클릭 시, 요청 문구를 표시하며, 확인 버튼 클릭 유도
    - 취소 클릭 시, 하단의 설정 버튼을 클릭하고, 위치 권한을 허용하면 정상적으로 기능 사용 가능
 
<br>

### **4) 위치 기반 게시판 생성**

<img src="https://github.com/user-attachments/assets/903abd56-a8ca-4e95-9135-2b7beefc9909" width="200" height="400"/>

- **위치 정보 확인**
    - 사용자의 지역 정보를 불러오고, 해당 지역에 맞는 게시판 표시
- **실시간 인기글**
    - 실시간 인기 글 섹션은 최근 일주일 간 공감 수가 가장 많은 게시물을 최대 5개까지 표시
- **최신글**
    - 최신 글 섹션은 가장 최근에 작성된 게시물 5개를 순서대로 표시
 
<br>

### 5) 게시판 화면

<img src="https://github.com/user-attachments/assets/ce42cbd0-92f9-4883-864e-9532b7ef3846" width="200" height="400"/>

- **사용자 활동 관리**
    - 사용자가 작성한 게시글 목록 화면 이동
    - 사용자가 댓글 단 게시글 목록 화면 이동
    - 공감 한 게시글 목록 화면 이동
    - 실시간 인기글 화면 이동
    - 최신 글 화면 이동
- **게시판 이동**
    - 선택한 게시판 화면으로 이동
 
<br>

### 6) 글쓰기 화면

<div>
  <img src="https://github.com/user-attachments/assets/cf487715-f12f-4bb3-8acf-417fd8d6ed0d" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/6478becb-9719-43ea-8eac-8aa46b49b72d" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/f64ce5bd-448b-4f2d-9861-53aeda368741" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/b8f1f922-bfe0-457b-8f62-2c9f824a968b" width="200" height="400"/>
</div>

- **게시판 선택**
    - 게시 글의 주제에 맞는 게시판을 선택할 수 있음
- **이미지 허용**
    - 하단의 이미지 추가 버튼을 클릭하면 권한 허용 창이 표시
    - 허용 안함 클릭 시, 권한을 허용하는 설정 화면으로 이동
- **익명 여부 확인**
    - 익명 여부 체크박스를 활성화 하면 닉네임 대신 익명으로 표시
- **게시물 업로드**
    - 완료 버튼을 클릭하면 작성된 게시 글이 선택된 게시판에 업로드 됨
 
<br>

### 7) 게시물 상세 화면

<div>
  <img src="https://github.com/user-attachments/assets/38470e51-708a-4c12-a40d-ebc266405431" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/9e049d6d-8289-4836-8270-1dc06d04fca7" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/f64ce5bd-448b-4f2d-9861-53aeda368741" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/170ea348-d343-483e-b03c-c1f2d6563475" width="200" height="400"/>
</div>

- **1:1 채팅**
    - 상단 메뉴 클릭 시, 게시물 작성자와 1:1 실시간 채팅
- **공감 기능**
    - 게시물의 공감 수가 표시되고, 공감 버튼을 클릭하면 공감 수 증가
- **댓글**
    - 하단의 입력 칸을 통해 댓글을 작성할 수 있으며, 익명 여부도 체크할 수 있음
 
<br>

### 8) 채팅 화면

<div>
  <img src="https://github.com/user-attachments/assets/be707788-2828-4198-a3d8-694ca3aa633c" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/c6ab130f-bcc4-48e3-acfb-06be15dc7c9d" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/fdcecece-9870-43b2-99ec-fbc26a41b7f7" width="200" height="400"/>
</div>

- **채팅 목록**
    - 채팅 상대방의 프로필 이미지, 닉네임, 최근 메시지 내용, 마지막 메시지 시간 표시
- **채팅방 화면 이동**
    - 채팅 상대를 클릭하면 해당 사용자와의 개별 채팅 화면으로 이동
- **실시간 1:1 채팅**
    - 상대방과 실시간으로 메세지를 주고 받을 수 있음

<br>
 
<div>
  <img src="https://github.com/user-attachments/assets/bd5eedcf-3513-401d-82c4-7ffe1e2e9332" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/4e01346c-535c-4d95-996f-6158dc3301c4" width="200" height="400"/>
</div>

- **채팅방 나가기**
    - 상단의 메뉴 중 채팅방 나가기 옵션 클릭 시, 채팅방을 나가는 팝업 표시
    - 채팅 리스트가 있는 화면에서 나가고자 하는 채팅방을 길게 클릭하면, 채팅방을 나가는 창 표시

<br>

### 9 ) 프로필 화면

<div>
  <img src="https://github.com/user-attachments/assets/3421bccc-3243-4956-a7ff-6858d176ec5b" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/f965c1ad-98a9-469e-a342-0be54efd9bcc" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/9095e730-5a58-4bb4-91d3-fec36d9a7777" width="200" height="400"/>
</div>

- **작성한 글, 공감한 글 표시**
    - 사용자가 작성한 글, 공감한 글 등 내 활동 내역 확인 가능

- **프로필 관리**
    - 사용자의 프로필 사진과 닉네임 변경
    - 로그인 초기의 프로필 설정 화면 재활용
 
<br>

<div>
  <img src="https://github.com/user-attachments/assets/8fac1006-159f-42c0-8a3a-31dfa52cfd23" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/c116aede-9b24-4162-9495-140ccae5c023" width="200" height="400"/>
  <img src="https://github.com/user-attachments/assets/e49014fb-46a0-46dc-856c-5cabd694caa5" width="200" height="400"/>
</div>

- **로그아웃**
    - 프로필 화면 상단의 설정 옵션을 클릭하면 설정 화면으로 이동
    - 설정 화면에서 “내 정보” 클릭 시, 앱에 로그인 한 이메일 주소 표시
    - 로그인된 이메일 클릭 시, 로그아웃 확인 팝업 표시
 
<br>
