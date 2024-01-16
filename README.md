
# portfolioDebate
분쟁 해결 웹 사이트 개인 프로젝트 포트폴리오

# 소개
여러 문제들의 분쟁을 해결 할 수 있도록 작성자가 분쟁 내용을 정리해 게시하면 <BR>
사이트 이용자들이 판단해 A와 B 둘 중 누가 잘못했는지 투표 할 수 있는 사이트 입니다.


# 제작기간 & 참여 인원
<UL>
  <LI>2023.12.27 ~ 2024.01.15</LI>
  <LI>개인 프로젝트</LI>
</UL>



# 사용기술
![js](https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=SpringBoot&logoColor=white)
![js](https://img.shields.io/badge/Java-FF0000?style=for-the-badge&logo=Java&logoColor=white)
![js](https://img.shields.io/badge/IntelliJ-004088?style=for-the-badge&logo=IntelliJ&logoColor=white)
![js](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=MariaDB&logoColor=white)
![js](https://img.shields.io/badge/security-6DB33F?style=for-the-badge&logo=security&logoColor=white)

![js](https://img.shields.io/badge/jquery-0769AD?style=for-the-badge&logo=jquery&logoColor=white)
![js](https://img.shields.io/badge/bootstrap-7952B3?style=for-the-badge&logo=bootstrap&logoColor=white)
![js](https://img.shields.io/badge/JavaScript-F7DF1E?style=for-the-badge&logo=JavaScript&logoColor=white)

# E-R 다이어그램




# 핵심 기능 및 페이지 소개

<details>
 <summary> Member Entity 
 
 </summary> 
 



</details>


<details>
 <summary> BookRentalHistory Entity
 
 </summary> 
 




</details>



<details>
 <summary> BookRentalReturnHistory Entity
 
 </summary> 
 




</details>


<details>
 <summary> BookLikeHistory Entity
 
 </summary> 


 

</details>


<details>
 <summary> BookHopeHistory Entity


 
 </summary> 
 



</details>




<details>
 <summary> BookInfo Entity
 
 </summary> 


 
 

</details>


<details>
 <summary> StudyRoomHistory Entity
 
 </summary> 







 
 

</details>



<details>
 <summary> StudyRoomState Entity
 
 </summary> 





 
</details>



<hr>

<h3>회원가입&로그인</h3>
<br>



<br>
<br>
<details>
 <summary> 회원가입 플로우 차트
 
 </summary> 
 

</details>


<UL>
  <LI>정규 표현식을 사용하여 생년월일과 전화번호의 잘못된 입력을 방지하였습니다. </LI>
 <LI>우편 번호 API를 사용하여 주소를 검색 할 수 있도록 구현했습니다. </LI>
</UL>


<br>
<br>




<h3>학습실</h3>
<br>


<br>
<br>

<details>
 <summary> 학습실 플로우 차트
 
 </summary> 
 

</details>



<details>
 <summary> 학습실 Service 코드
 
 </summary> 



</details>



<details>

 <summary> 학습실 스레드 코드
 
 </summary> 


   

</details>


<UL>
  <LI>다중 스레드를 사용하여 1분 간격으로 남은 이용 시간이 업데이트 되도록 구현했습니다. </LI>
 <LI>남은 이용 시간이 00:40분 보다 작을 때만 이용 시간 연장 기능이 가능하도록 구현했습니다.</LI>
 <LI>1인 1좌석을 사용 할 수 있도록 현재 사용 중인 좌석 엔티티를 생성하여 중복 좌석을 방지하였습니다.</LI>
</UL>


<br>
<br>


<h3>도서 대여</h3>
<br>

<br>
<br>
<details>
 <summary> 도서 대여 플로우 차트
 
 </summary> 
 

</details>


<details>
 <summary> 도서 대여 Service 코드
 
 </summary> 
 




</details>


<UL>
  <LI>도서 검색 시 알라딘 도서 API를 통해 검색 타입 별로 도서 정보를 받아와 뿌려주도록 구현했습니다.</LI>
 <LI>대여 도서는 마이페이지에서 확인 할 수 있도록 구현했습니다.</LI>
 <LI>최대 8권으로 대여를 제한하고 있고 중복 도서는 대여 불가능 하도록 구현했습니다.</LI>
 <LI>반납 신청 -> 관리자 페이지의 반납 신청 도서로 등록 -> 관리자의 반납 완료 처리 -> 반납 완료 도서로 등록</LI>
 
</UL>

<br>
<br>


<h3>관심 도서</h3>
<br>


<br>
<br>

<details>
 <summary> 관심 도서 플로우 차트
 
 </summary> 
 
</details>


<details>
 <summary> 관심 도서 Service 코드
 
 </summary> 
 



    
</details>


<UL>
  <LI>관심 도서를 중복으로 등록 할 수 없도록 구현 했습니다.</LI>
 <LI>관심 도서로 등록된 도서는 마이페이지에서 확인 할 수 있습니다.</LI>
 <LI>마이페이지에서 관심 도서를 대여 할 시 관심 도서 목록에서 삭제되고 대여 도서 목록에 등록되도록 구현했습니다.</LI>
</UL>

<br>
<br>



<h3>희망 도서 신청</h3>
<br>

<br>
<br>


<details>
 <summary> 희망 도서 신청 플로우 차트
 
 </summary> 
 

</details>


<details>
 <summary> 희망 도서 Service 코드
 
 </summary> 
 


</details>

<UL>
  <LI>희망 도서를 중복으로 신청 할 수 없도록 구현했습니다.</LI>
 <LI>희망 도서 신청 시 관리자 페이지의 희망 도서 신청 목록에서 확인 할 수 있도록 구현했습니다.</LI>
</UL>


<br>
<br>




<h3>관리자 페이지</h3>
<br>


<br>
<br>

<details>
 <summary> 관리자 페이지 플로우 차트
 
 </summary> 
 
</details>

<UL>
  <LI>관리자 페이지에서 반납 신청 된 도서를 확인 할 수 있고 반납 완료 처리를 할 수 있도록 구현했습니다.</LI>
 <LI>희망 신청된 도서를 확인 할 수 있습니다.</LI>
</UL>



# 프로젝트를 통해 느낀 점과 소감














