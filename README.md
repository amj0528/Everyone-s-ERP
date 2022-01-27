# Everyone-s-ERP

### STS와 Mybatis, Oracle SQL Developer로 만든 ERP 시스템입니다.
##### 주요 기능 : 프로젝트를 부여받아 팀별로 프로젝트를 수행하고, 그에 따른 프로젝트 보고서를 개인이 올리면, 온라인으로 결재 승인을 받을 수 있는 기능.
##### 관리자 화면과 사용자 화면으로 구성되어 있고, 모든 게시판에는 CRUD 기능이 들어있습니다.

###### 　
초기 시작 URL은 http://localhost:80/ 입니다.



***

   ###### 　
+ 프로젝트 전체 구조
  + 관리자 화면
    + 회원 관리
    + 메인 배너 관리
    + SMS 전송
    + 실적 관리
    + 신고 관리
    + 프로젝트 관리
    + 프로젝트 결재 승인
    + 실적 차트 (조회)

  + 사용자 화면
    + 프로젝트 게시판
    + 스케줄 캘린더
    + 실적 게시판
    + 일반 게시판
    + 회원가입
    + 아이디 / 패스워드 찾기
    + 마이페이지
      + 회원 정보 수정
      + 내 게시글 관리
      + 내 신고글 관리
      + 내 스케줄 관리
      + 내 프로젝트 관리
      + 프로젝트 결재 보고서

   ###### 　
* * *

   ###### 　
+ 테이블 쿼리문

  ◆게시판 관리(일반 게시판을 관리하는 게시판)   
create table tbl_boardadmin(   
code Number primary key	,   
reply varchar2(1)		,--댓글 여부   
answer varchar2(1)		,--답글 여부   
editor varchar2(1)		,--ckeditor적용 여부, 텍스트박스 혹은 이미지.   
attach varchar2(1)		,--첨부파일 여부   
declar varchar2(1)		,--신고 기능   
auth varchar2(1)		,--0이면 개인, 1이면 관리(부서 수정, 삭제)   
title varchar2(100)		,--게시판명   
writer varchar2(20)		,--등록자   
day date			,--등록일   
updater varchar2(20)	,--수정자   
updatedate date		,--수정일   
ip varchar2(15)		--보안 상의 이유로 사용   
);   

  ###### 　
  ◆게시판(일반 게시판)   
create table tbl_board(   
num number primary key	,   
code number		,--fk 게시판관리번호   
parentnum number		,--원글인지 답변글인지   
depth number		,--답변 깊이   
title varchar2(250)		,   
content clob		,   
ip varchar2(15)		,   
day date			,   
writer varchar2(20)		,   
updater varchar2(20)	,   
updatedate date		,   
replycnt number default(0)	,   
notice varchar2(1)		--공지글 여부   
);   

  (+)   
  
  ALTER TABLE tbl_board   
ADD CONSTRAINT board_code_fk FOREIGN KEY(code)   
REFERENCES tbl_boardadmin(code);    

   ###### 　
  ◆파일첨부   
create table tbl_attach(   
idx number primary key	,   
num number		,--fk 게시판 번호   
uploadpath varchar2(200)	,   
uuid varchar2(100)		,   
filename varchar2(50)	,   
ip varchar2(15)		,   
day date			,   
writer varchar2(20)		,   
updater varchar2(20)	,   
updatedate date   
);   

  (+)   

  alter table tbl_attach add constraint fk_board_attach foreign key(num) references tbl_board(num);   

   ###### 　
  ◆댓글   
create table tbl_reply(   
idx number primary key	,   
num number		,--fk 게시판 번호   
content varchar2(4000)	,   
ip varchar2(15)		,   
day date			,   
writer varchar2(20)		,   
updater varchar2(20)	,   
updatedate date   
);   

  (+)   
  
  alter table tbl_reply add foreign key(num) references tbl_board(num);   

  ###### 　
  ◆신고   
create table tbl_declar(   
idx number primary key	,   
num number		,--fk 게시판 번호   
sel varchar2(10)		,--신고항목   
val varchar2(4000)		,--신고 사유   
ip varchar2(15)		,   
day date			,   
writer varchar2(20)		,   
TITLE VARCHAR2(250 BYTE),   
CONTENT CLOB   
);   

  (+)   

  alter table tbl_declar add foreign key(num) references tbl_board(num);   

  ###### 　
  ◆프로젝트 관리   
create table tbl_projectadmin(   
num number primary key	,   
title varchar2(100)		,--프로젝트 게시물 제목   
content clob		,--특이사항,참고사항 등 입력   
ip varchar2(15)		,   
startday varchar2(10)	,--시작 날짜   
endday varchar2(10)	,--끝나는 날짜   
writer varchar2(20)		,--작성자   
day date			,--작성일   
updater varchar2(20)	,--수정자   
updatedate date		,--수정일   
progress varchar2(1)	,--프젝 진행중, 완료 표시   
useridsp varchar2(1000)	,   
member varchar2(1000)	--담당자   
);   

  ###### 　
  ◆회원   
create table tbl_member(   
userid varchar2(20)		,   
userpw varchar2(100)	,   
username varchar2(20)	,   
sex varchar2(1)		,   
birth varchar2(10)		,   
email varchar2(250)	,   
zip varchar2(8)		,--우편번호   
addr1 varchar2(250)	,--주소1   
addr2 varchar2(250)	,   
phone varchar2(13)		,--연락처(-포함)   
hp varchar2(13)		,--내선번호   
job varchar2(20)		,--직책   
dept varchar2(20)		,--부서   
bonus number default(0)	,--실적   
day date			--회원가입일   
);   

  (+)   

  alter table tbl_member add primary key (userid);   

  ###### 　
  ◆실적   
create table tbl_bonus(   
idx number primary key	,   
num number		,--fk 프로젝트 게시글 넘버, 어느 프로젝트에서 실적 추가 받았는지 표시   
userid varchar2(20)		,   
content varchar2(10)	,--실적 사유(셀렉트박스)   
ip varchar2(15)		,   
day Date			,   
writer varchar2(20)		,   
updatedate Date		,   
updater varchar2(20)	,   
bonus number		--실적   
);   

  (+)   

  alter table tbl_bonus add foreign key(num) references tbl_projectadmin(num);   

   ###### 　
  ◆스케줄   
create table tbl_schedule(   
num number primary key		,   
startday VARCHAR2(20 BYTE)	,    
endday VARCHAR2(20 BYTE)		,   
title varchar2(250)			,   
content varchar2(1000)		,   
writer varchar2(20)			,   
day Date				,   
personal varchar2(1)		, --0이면 개인, 1이면 부서 스케줄, 2면 직책    
updatedate Date			,   
ip varchar2(15)    
);   

   ###### 　
  ◆문서관리   
create table tbl_doc(   
idx number primary key	,   
num number		,--fk 스케줄 넘버. 직책에서 온 문서인지, 부서에서 온 문서인지 구별 위해   
uuid varchar2(50)		,   
filetype varchar2(10)	,   
filename varchar2(250)	,   
parentidx number		,   
depth number default(0)	,   
ip varchar2(15)		,   
day date			,   
writer varchar2(20)		,   
updater varchar2(20)	,   
updatedate date   
);   

   ###### 　
  ◆결재용 보고서 쿼리(보고서 목록, 사인 전반으로 다 담는 쿼리)   
Create Table tbl_Report(   
Idx Number Primary  Key				,     
num Number REFERENCES tbl_projectadmin(num)	,    
FileName varchar2(50)				,   
uuid varchar2(50)					,    
writer varchar2(20)					,    
day Date						,    
updater varchar2(20)				,   
updatedate Date					,   
ip varchar2(15)       
);   

   ###### 　
  ◆결재용 보고서 쿼리 보조(결재 보고서 내용 목록 담는 쿼리)   
Create Table tbl_ReportSub(   
Idx Number REFERENCES tbl_Report(idx)	,   
Sel varchar2(100)				,   
Txt varchar2(100)				,   
writer varchar2(20)				,   
day Date					,   
updater varchar2(20)			,   
updatedate Date				,   
ip varchar2(15)   
);   

   ###### 　
  ◆결재 승인자 쿼리   
Create Table tbl_ReportResult(   
Idx Number Primary  Key	,   
num Number		,    
dept varchar2(20)		,   
FileName varchar2(50)	,   
uuid varchar2(100)		,   
writer varchar2(20)		,   
day Date			,   
JOB VARCHAR2(20)	,   
ip varchar2(15)        
);   

  (+)   

  alter table tbl_ReportResult add foreign key(num) references tbl_projectadmin(num);   

   ###### 　
  ◆메인페이지 배너 관리   
create table tbl_mainbanner(   
idx number primary key	,   
num number		,--몇번째 배너로 올릴지   
link varchar2(4000)		,--배너와 이어지게 할 주소 적는 란   
image varchar2(200)	,--배너 이미지 보이기   
ip varchar2(15)		,   
day date			,    
writer varchar2(20)		   
);   

   ###### 　
  ◆회원 권한    
Create Table tbl_memberAuth(   
userid  varchar2(20) 	,   
auth    varchar2(50)   
);   

  (+)   

  alter table tbl_memberauth add foreign key(userid) references tbl_member(userid);   

   ###### 　
  ◆로그인   
create table persistent_logins (   
username varchar2(64) not null	,   
series varchar2(64) primary key	,   
token varchar2(64) not null		,   
last_used timestamp not null   
);   

   ###### 　
  ◆프로젝트 담당자 선택   
create table tbl_memberselect(   
idx number primary key	,   
num number		,--프로젝트 관리 게시물 번호   
userid varchar2(20)		,   
username varchar2(20)   
);   

  (+)   

  alter table tbl_memberselect add foreign key(num) references tbl_projectadmin(num);   

   ###### 　
  ◆회원가입 시 SMS 인증   
create table tbl_certification(   
key varchar2(20)		,   
day varchar2(20)		   
);    

   ###### 　
  ◆시퀀스   
create sequence board_seq;   
create sequence boardadmin_seq;   
create sequence reply_seq;   
create sequence attach_seq;   
create sequence schedule_seq;   
create sequence member_seq;   
create sequence doc_seq;   
create sequence declar_seq;   
create sequence memberauth_seq;   
create sequence projectadmin_seq;   
create SEQUENCE memberselect_seq;   
create SEQUENCE bonus_seq;   
Create SEQUENCE Report_seq;   
Create SEQUENCE ReportResult_seq;   
Create SEQUENCE mainbanner_seq;   

   ###### 　
