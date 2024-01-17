
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
![js](https://img.shields.io/badge/React-0769Ac?style=for-the-badge&logo=React&logoColor=white)
# E-R 다이어그램


![DebateERD](https://github.com/oals/portfolioDebateBoard/assets/136543676/64a7db24-534f-419c-a654-b51fad2eb7bf)


# 핵심 기능 및 페이지 소개

<details>
 <summary> Member Entity 
 
 </summary> 
 

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    @Entity
    @Table(name="member")
    public class Member {

    @Id
    @Column(nullable = false,length = 50,name="member_id")
    private String memberId;

    private String memberPswd;
    private String memberName;
    private String memberPhone;
    private String memberAddress;

    private int memberDebateWinCount;       //분쟁의 승리를 맞춘 수
    private int memberDebateLoseCount;      //분쟁의 승리를 맞추지 못한 수
    private int memberDebateDrawCount;      //분쟁의 무승부 수

    private Double memberWinningPercent;    //승률
    private String memberDate;

    @Enumerated(EnumType.STRING)
    private Role role;  //권한 설정


    public static Member createMember(MemberDTO memberDTO, PasswordEncoder passwordEncoder){

        Member member = new Member();
        member.setMemberId(memberDTO.getMemberId());
        member.setMemberName(memberDTO.getMemberName());
        member.setMemberPhone(memberDTO.getMemberPhone());
        member.setMemberAddress(memberDTO.getMemberAddress());
        member.setMemberDebateWinCount(memberDTO.getMemberDebateWinCount());
        member.setMemberDebateLoseCount(memberDTO.getMemberDebateLoseCount());
        member.setMemberDebateDrawCount(memberDTO.getMemberDebateDrawCount());
        member.setMemberWinningPercent(memberDTO.getMemberWinningPercent());
        member.setMemberDate(memberDTO.getMemberDate());
        member.setRole(Role.USER);

        // 암호화
        String password = passwordEncoder.encode(memberDTO.getMemberPswd());
        member.setMemberPswd(password);

        return member;
    }



    }


</details>


<details>
 <summary> DebatePost Entity
 
 </summary> 
 

    @Entity
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public class DebatePost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "debate_article_no")
    private Long debateArticleNo;
    private String debateTitle;     //제목

    @Column(length = 1000)
    private String debateSituation; //상황 설명
    private String debateCategory;  //카테고리
    @Column(length = 200)
    private String debateContentTextA;  //A의 논점
    @Column(length = 200)
    private String debateContentTextB;  //B의 논점
    private String debateStartDate;     //상황 발생일

    private String debateSelectTime;    //분쟁 게시글의 선택된 스레드 시간

    private String debateRunningTime;   //현재 스레드의 시간

    private int debatePostView;     //조회수
    private String debatePostDate;      //게시물 자성일
    private String debateResult;        //분쟁 결과
    private Boolean debateState;        //분쟁 상태

    private int aVotes;             //A의 득표수
    private int bVotes;             //B의 득표수
    private String debateWinner;       //승리한 논점

    @OneToOne(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true , fetch = FetchType.LAZY)
    private DebatePostImage debatePostImage;    //포스트 이미지

    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebateComment> debateComments = new ArrayList<>(); //포스트 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //작성자


    @OneToMany(mappedBy = "debatePost", cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    private List<DebatePostProsAndCons> postProsAndCons = new ArrayList<>();    //포스트의 A 혹은 B 투표 현황




    }




</details>



<details>
 <summary> DebatePostImage Entity
 
 </summary> 
 


    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public class DebatePostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debatePostImageNo;

    private String debateContentVideoPath;  //영상 주소
    private String debateContentImagePath;  //이미지 주소
    private String debateContentImagePath2; //이미지 주소2
    private String debateContentImagePath3; //이미지 주소3

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "debate_article_no")
    private DebatePost debatePost;      //해당 포스트



    }



</details>



<details>
 <summary> DebatePostProsAndCons Entity


 
 </summary> 
 

    @Entity
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DebatePostProsAndCons {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pros_and_cons_no")
    private Long prosAndConsNo;


    private String debateVoteState; //사용자가 투표한 A 혹은 B

    private String debateVoteDate;  //투표 버튼을 누른 날짜


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;          //투표를 한 사용자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //투표 된 게시물





    }



</details>




<details>
 <summary> DebateComment Entity
 
 </summary> 



    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @Getter
    @Setter
    public class DebateComment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="debate_comment_no")
    private Long debateCommentNo;
    private String debatePostComment;   //댓글 내용
    @Nullable
    private Long debatePostCommentParent;   //부모 댓글 (사용하지 않았음)

    private String debatePostCommentDate;   //댓글 작성일

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;                  //작성자


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;      //댓글이 작성된 포스트


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "pros_and_cons_no")
    private DebatePostProsAndCons debatePostProsAndCons;    //작성자의 투표 현황

    @OneToMany(mappedBy = "debateComment",cascade=CascadeType.ALL, orphanRemoval=true ,fetch = FetchType.LAZY)
    List<DebateCommentLike> debateCommentLikeList = new ArrayList<>();  //작성자가 받은 댓글 공감 수




    }


 
 

</details>


<details>
 <summary> DebateCommentLike Entity
 
 </summary> 


    
    @Entity
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public class DebateCommentLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateCommentLikeNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;      //공감 버튼을 누른 유저


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="debate_comment_no")
    private DebateComment debateComment;    //해당 공감 버튼의 댓글


    }





 
 

</details>



<details>
 <summary> DebateBookMark Entity
 
 </summary> 



    @Entity
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public class DebateBookMark {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long debateBookMarkNo;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;  //북마크한 회원 정보


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "debate_article_no")
    private DebatePost debatePost;  //북마크한 포스트




    }

 
</details>



<hr>

<h3>메인 페이지</h3>
<br>
<BR>



![메인페이지 (1)](https://github.com/oals/portfolioDebateBoard/assets/136543676/f6b97541-a254-400a-884e-b543b0c28dc8)


<br>
<br>

<details>
 <summary> 메인 페이지 플로우 차트

 </summary> 
 
![메인페이지 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/c202ec69-c219-4e47-b5da-ee431b6883f2)

</details>



<UL>
  <Li>관심 있는 게시판으로 바로 이동할 수 있도록 구현했습니다/ </Li>
  <LI>최신 글들을 카테고리 관계 없이 바로 확인 할 수 있도록 구현 했습니다.</LI>
  <LI>최신 글 클릭 시 해당 포스트 페이지로 이동됩니다.</LI>
</UL>



<br>
<br>






<h3>분쟁 게시판</h3>
<br>


![board페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/0608b9d2-1085-4ec1-9112-561b99fafbb6)



<br>
<br>

<details>
 <summary> 분쟁 게시판 플로우 차트
 
 </summary> 
 
  ![DEBATE게시판 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/3442ed73-e5ea-41c0-847d-21241bca8844)


</details>







<UL>
  <LI>카테고리 탭을 누르면 해당 게시판으로 이동됩니다. </LI>
  <LI>최근 참여한 분쟁 게시글, 인기 게시글, 북마크한 게시글을 확인 할 수 있습니다.  </LI>
  <LI>현재 진행 중인 분쟁 게시글과 종료된 분쟁 게시글을 각각 나눠서 확인 할 수 있습니다. </LI>
  <LI>카테고리 탭에 해당되는 인기 글을 상단에서 확인 할 수 있습니다. </LI>
</UL>


<br>
<br>


<h3>검색</h3>
<br>

![검색](https://github.com/oals/portfolioDebateBoard/assets/136543676/1361ac8c-0274-4acf-a19c-cc19e18aa327)


<br>
<br>

<details>
 <summary> 
 검색 플로우 차트
 </summary> 
  
 ![검색 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/499cf52e-784e-4d11-848f-a9b3678678fe)

</details>


<details>
 <summary> 
 검색 Service
 </summary> 
 
       @Override
        public List<DebatePostDTO> SelectSearchDebatePost(String debateSearchOpt,String debateSearchData,String debateCategoryName, PageRequestDTO pageRequestDTO) {

        Pageable pageable = pageRequestDTO.getPageable();
        BooleanBuilder builder = new BooleanBuilder();


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePost qDebatePost = QDebatePost.debatePost;

        if(debateSearchOpt.equals("전체 검색")){
            builder.and(qDebatePost.debateTitle.contains(debateSearchData)
                    .or(qDebatePost.debateSituation.contains(debateSearchData)));
        }else if(debateSearchOpt.equals("제목")) {
            builder.and(qDebatePost.debateTitle.contains(debateSearchData));
        }else{
            builder.and(qDebatePost.debateSituation.contains(debateSearchData));
        }


        List<DebatePost> query = queryFactory.selectFrom(qDebatePost)
                .where(builder
                        .and(qDebatePost.debateCategory.eq(debateCategoryName)))
                .orderBy(qDebatePost.debatePostDate.desc())
                .offset(pageable.getOffset())   //N 번부터 시작
                .limit(pageable.getPageSize()) //조회 갯수
                .fetch();

        List<DebatePostDTO> debatePostDTOList = new ArrayList<>();



        for(int i = 0; i < query.size(); i++){

            DebatePostDTO debateCommentDTO = DebatePostDTO.builder()
                    .memberId(query.get(i).getMember().getMemberId())
                    .debateArticleNo(query.get(i).getDebateArticleNo())
                    .debateTitle(query.get(i).getDebateTitle())
                    .debateCategory(query.get(i).getDebateCategory())
                    .debateRunningTime(query.get(i).getDebateRunningTime())
                    .debateSituation(query.get(i).getDebateSituation().length() < 100
                            ? query.get(i).getDebateSituation() : query.get(i).getDebateSituation().substring(0,100) + "...")
                    .debatePostView(query.get(i).getDebatePostView())
                    .debatePostDate(query.get(i).getDebatePostDate())
                    .debateResult(query.get(i).getDebateResult())
                    .debateState(query.get(i).getDebateState())
                    .build();

            debatePostDTOList.add(debateCommentDTO);
        }


        return debatePostDTOList;



      }


    
</details>


<UL>
  <LI>전체검색, 제목 검색, 내용 검색으로 각각 검색 할 수 있습니다.</LI>
  <LI>현재 카테고리에 해당 되는 게시글만 검색되도록 구현 했습니다.</LI>
  <LI>검색된 게시글을 클릭 시 해당 게시글 페이지로 이동 할 수 있습니다.</LI>
</UL>

<br>
<br>


<h3>포스트 작성</h3>
<br>


![글 작성](https://github.com/oals/portfolioDebateBoard/assets/136543676/3802ce18-459e-4cc5-bba9-1beb4f98ab9d)


<br>
<br>

<details>
 <summary> 
 포스트 작성 플로우 차트
 </summary> 


 ![포스트 작성 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/4d745013-2a0b-416a-ba74-feb0636006d3)


</details>




<UL>
  <LI>카테고리와 관계 없이 작성 페이지 내에서 카테고리를 변경 할 수 있습니다.</LI>
  <LI>투표 진행 시간을 설정할 수 있도록 구현했습니다.</LI>
  <LI>포스트에 영상과 이미지를 첨부 할 수 있도록 구현했습니다.</LI>
  <LI>포스트 작성 완료 시 다중 스레드가 동작하도록 구현했습니다.</LI>
</UL>

<br>
<br>





<h3>포스트 이미지 저장</h3>
<br>

![Debate이미지저장폴더](https://github.com/oals/portfolioDebateBoard/assets/136543676/5d55d97d-ffc7-4646-ab2d-ae96656b7704)

<br>
<br>

<details>
 <summary> 
 포스트 이미지 저장 플로우 차트
 </summary> 

![이미지 파일 저장 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/3e79e065-23ae-4d7d-86db-5e9ba4858840)

 
</details>


<details> 
 <summary> 
 포스트 이미지 업로드 Service
 </summary> 
 

     @Override
      public String uploadFile(String uploadPath, String originalFileName, byte[] fileDate) {

        UUID uuid = UUID.randomUUID();

        String savedFileName = uuid + originalFileName; // 난수에 확장자 붙이기
        String fileUploadFullUrl = uploadPath + "/"+ savedFileName;

        String fileUrl = fileUploadFullUrl.replace("c:/Debate/Board/","/images/");


        // 결과 : 업로드할 경로 + / + uuid값 + .파일확장자

        try {
            FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
            fos.write(fileDate);
            fos.close();
        }catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //업로드 완료
        return fileUrl;


      }


    
</details>


<UL>
  <LI>생성될 포스트의 articleNo를 폴더명으로 해당 폴더에 영상과 이미지들을 저장합니다</LI>
  <LI>이미지 파일의 이름을 UUID 값으로 변경시켜 이미지 중복 오류를 방지하였습니다.</LI>
  
</UL>

<br>
<br>




<h3>포스트</h3>
<br>

![post페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/594c3148-4681-4753-b72c-e728d1cfefc7)


<br>
<br>

<details>
 <summary> 
 포스트 플로우 차트
 </summary> 
  
 ![포스트 플로우 차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/7850f49a-0e76-43fa-bc6e-2c6bb19d2bc2)

</details>



<UL>
  <LI>현재 남은 투표 가능 시간을 하단에 배치 하였습니다.</LI>
  <LI>A 와 B 버튼을 통해 투표할 수 있습니다</LI>
  <LI>투표 내역 엔티티를 통해 중복 투표를 방지하였습니다.</LI>
  <LI>북마크 버튼을 통해 해당 포스트를 북마크에 추가 할 수 있습니다.</LI>
</UL>

<br>
<br>




<h3>스레드</h3>
<br>

<br>
<br>
<details>
 <summary> 스레드 플로우 차트
 
 </summary> 
 
 ![스레드 플로우차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/2843e7c6-fb36-40b2-92b6-0c84f7f09c5b)


</details>


<details>
 <summary> 스레드 Service 코드
 
 </summary> 
 

    public class MyRunnable implements Runnable {
        private final Long debateArticleNo;
        private boolean threadChk;


        public MyRunnable(Long debateArticleNo) {
            this.debateArticleNo = debateArticleNo;
            this.threadChk = true;
        }

        @Override
        public void run() {
            while(threadChk) {

                try {
                    Thread.sleep(60000);

                    JPAQueryFactory queryFactory = new JPAQueryFactory(em);
                    QDebatePost qDebatePost = QDebatePost.debatePost;

                    DebatePost debatePost = queryFactory.selectFrom(qDebatePost)
                            .leftJoin(qDebatePost.postProsAndCons).fetchJoin()
                            .where(qDebatePost.debateArticleNo.eq(debateArticleNo))
                            .fetchOne();

                    if(debatePost == null){
                        log.info("삭제된 글");
                        threadChk = false;
                        return;
                    }

                    LocalTime time = LocalTime.parse(debatePost.getDebateRunningTime());

                    if(time.toString().equals("00:00")){

                        threadChk = false;
                        log.info("스레드 종료");

                        int a = 0;
                        int b = 0;

                        Map<String,String> memberNameMap = new HashMap<>();


                        for(int i = 0; i < debatePost.getPostProsAndCons().size(); i++ ){
                            if(debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString().equals("A")){
                                a += 1;
                                memberNameMap.put(debatePost.getPostProsAndCons().get(i).getMember().getMemberId(),debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString());
                            }else{
                                b += 1;
                                memberNameMap.put(debatePost.getPostProsAndCons().get(i).getMember().getMemberId(),debatePost.getPostProsAndCons().get(i).getDebateVoteState().toString());
                            }
                        }

                        //투표한 유저들의 승률 작업

                        String result =  a > b ? "A" : a < b ? "B" : "D";
                        memberService.updateMemberDebateWinningPercent(memberNameMap,result);



                        //투표 결과 저장
                        debatePost.setAVotes(a);
                        debatePost.setBVotes(b);
                        debatePost.setDebateWinner(a > b ? "A의 승리 입니다." : a < b ? "B의 승리 입니다." : "무승부 입니다.");
                        debatePost.setDebateResult("종료");
                        debateBoardRepository.save(debatePost);





                    }else {
                        time = time.minusMinutes(1);
                        debatePost.setDebateRunningTime(String.valueOf(time));
                        debateBoardRepository.save(debatePost);
                    }



                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                    }
                }
            }
        }



</details>


<UL>
  <LI>다중 스레드를 사용하여 1분 간격으로 남은 이용 시간이 업데이트 되도록 구현했습니다. </LI>
 <LI>남은 이용 시간이 00:00분이 되었을 때 스레드가 종료되도록 구현했습니다.</LI>
 <LI>스레드가 종료될 때 A와 B의 득표 수를 비교해 승패를 구분 짓고 MEMBER 엔티티와 POST 엔티티의 <BR> 데이터를 업데이트 하도록 구현했습니다. </LI>
</UL>


<br>
<br>


<h3>포스트 (투표 종료)</h3>
<br>

![분쟁종료board페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/cb4d9374-a8de-4d0c-bd26-d0367fc9950c)

<br>
<br>




<UL>
  <LI>A와 B 득표수를 공개 하도록 구현했고 누가 투표를 더 많이 받았는지 공개하도록 하였습니다.</LI>
   <LI>포스트의 진행 시간이 종료된 후에는 포스트 삭제 및 수정, 댓글 작성,투표가 불가능하도록 구현했습니다. </LI>

</UL>

<br>
<br>



<h3>포스트 수정</h3>
<br>

![수정](https://github.com/oals/portfolioDebateBoard/assets/136543676/fccf1946-5b5c-4cb0-99f2-61c0fe510e56)

<br>
<br>
<details>
 <summary> 포스트 수정 플로우 차트

 </summary> 
 
   
 ![포스트 수정 플로우 차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/dff72e41-e2de-495b-8d5a-c7c520369959)

</details>


<details>
 <summary> 포스트 수정 Service 코드
 
 </summary> 
 

       @Override
      public void updateDebatePost(DebatePostDTO debatePostDTO) {


        DebatePost debatePost = debateBoardRepository.findById(debatePostDTO.getDebateArticleNo())
                .orElseThrow(() -> new EntityNotFoundException());

        // 변경된 데이터만 저장
        if (!debatePost.getDebateTitle().equals(debatePostDTO.getDebateTitle())) {
            debatePost.setDebateTitle(debatePostDTO.getDebateTitle());
        }

        if (!debatePost.getDebateSituation().equals(debatePostDTO.getDebateSituation())) {
            debatePost.setDebateSituation(debatePostDTO.getDebateSituation());
        }

        if (!debatePost.getDebateContentTextA().equals(debatePostDTO.getDebateContentTextA())) {
            debatePost.setDebateContentTextA(debatePostDTO.getDebateContentTextA());
        }

        if (!debatePost.getDebateContentTextB().equals(debatePostDTO.getDebateContentTextB())) {
            debatePost.setDebateContentTextB(debatePostDTO.getDebateContentTextB());
        }

        if (!debatePost.getDebateStartDate().equals(debatePostDTO.getDebateStartDate())) {
            debatePost.setDebateStartDate(debatePostDTO.getDebateStartDate());
        }

        if (!debatePost.getDebateCategory().equals(debatePostDTO.getDebateCategory())) {
            debatePost.setDebateCategory(debatePostDTO.getDebateCategory());
        }

        if (!debatePost.getDebateSelectTime().equals(debatePostDTO.getDebateSelectTime())) {
            debatePost.setDebateSelectTime(debatePostDTO.getDebateSelectTime());
        }


        debateBoardRepository.save(debatePost);

      }


</details>


<UL>
  <LI>더티 체킹 기법을 사용해 변경된 필드만 업데이트 되도록 구현했습니다.</LI>
  <LI>첨부파일이 변경 되었을 시 기존의 첨부파일을 삭제하고 새 첨부파일을 저장하도록 구현했습니다.</LI>
  <LI>게시글의 투표 가능 시간은 변경하지 못하도록 구현했습니다.</LI>
</UL>


<br>
<br>



<h3>포스트 삭제</h3>
<br>

![삭제](https://github.com/oals/portfolioDebateBoard/assets/136543676/5d1269dd-436b-485c-bb61-74439573c95c)

<br>
<br>




<UL>
  <LI>포스트 삭제 시 DB의 데이터 삭제 후 해당 포스트의 articleNo를 통해 C드라이브의 이미지 폴더를 삭제 하도록 구현했습니다.</LI>
</UL>


<br>
<br>





<h3>댓글</h3>
<br>

![댓글-작성-공감-삭제](https://github.com/oals/portfolioDebateBoard/assets/136543676/c127058c-29a1-492a-8cd9-1ab6529f4119)

<br>
<br>

<details>
 <summary> 
 댓글 플로우 차트
 </summary> 
  
 ![댓글 플로우 차트](https://github.com/oals/portfolioDebateBoard/assets/136543676/6d326140-2320-4a19-83ef-82d9d4492e54)

</details>


<details>
 <summary> 
 댓글 Service
 </summary> 
 


      @Override
      public void InsertDebateComment(DebateCommentDTO debateCommentDTO) {


        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QDebatePostProsAndCons qDebatePostProsAndCons = QDebatePostProsAndCons.debatePostProsAndCons;


        Member member = Member.builder()
                .memberId(debateCommentDTO.getMemberId())
                .build();

        DebatePost debatePost = DebatePost.builder()
                .debateArticleNo(debateCommentDTO.getDebateArticleNo())
                .build();


        DebatePostProsAndCons debatePostProsAndCons = queryFactory.selectFrom(qDebatePostProsAndCons)
                .where(qDebatePostProsAndCons.debatePost.debateArticleNo.eq(debateCommentDTO.getDebateArticleNo())
                        .and(qDebatePostProsAndCons.member.memberId.eq(debateCommentDTO.getMemberId())))
                .fetchOne();


        DebateComment debateComment = DebateComment.builder()
                .debatePostComment(debateCommentDTO.getDebatePostComment())
                .debatePostCommentDate(debateCommentDTO.getDebatePostCommentDate())
                .debatePostCommentParent(debateCommentDTO.getDebatePostCommentParent())
                .debatePost(debatePost)
                .debatePostProsAndCons(debatePostProsAndCons)
                .member(member)
                .build();




        debateCommentRepository.save(debateComment);


      }  
    
</details>


<UL>
  <LI>투표를 진행 한 유저만 댓글을 작성 할 수 있도록 구현했습니다.</LI>
  <LI>댓글 공감 내역 엔티티를 통해 중복 공감을 방지하였습니다.</LI>
  <LI>자신의 댓글만 삭제 버튼이 보이도록 구현해 댓글을 삭제 할 수 있도록 구현했습니다.</LI>

</UL>

<br>
<br>





<h3>카테고리</h3>
<br>

![카테고리별-게시판](https://github.com/oals/portfolioDebateBoard/assets/136543676/ea345b91-c59d-4be1-af44-71471815cbfb)

<br>
<br>





<UL>
  <LI>카테고리 별 게시글을 나눠 관심사에 맞는 게시글만 볼 수 있도록 구현했습니다.</LI>
</UL>

<br>
<br>



<h3>마이 페이지</h3>
<br>

![마이페이지](https://github.com/oals/portfolioDebateBoard/assets/136543676/fab15567-44f8-4665-8634-83b7fef38ab8)



<br>
<br>




<UL>
  <LI>회원 정보를 확인 할 수 있고 참여한 분쟁의 승패 그리고 승률을 확인 할 수 있습니다. </LI>
  <LI>내가 작성한 글, 참여한 분쟁, 북마크를 확인 할 수 있습니다.</LI>
  <LI>북마크 삭제 버튼과 전체 삭제 버튼을 구현했습니다.</LI>

</UL>

<br>
<br>


<h3>북마크 삭제</h3>
<br>

![마이페이지-북마크-삭제](https://github.com/oals/portfolioDebateBoard/assets/136543676/3dc6fad1-6405-40ce-b4a9-dd498df86c5f)


<br>
<br>




<br>
<br>





# 프로젝트를 통해 느낀 점과 소감
5번 째 웹 프로젝트다. <BR>
이번 프로젝트를 하면서 느낀 점은 비슷비슷한 프로젝트의 코드들이지만 만들 수록 실력이 늘어간다는 것이다.<BR>
익숙해질 부분은 익숙해지고 몰랐던 새로운 것들 또한 배우게되었다.<BR>
그 중 하나는 더티체킹이다  변경된 데이터만 업데이트 하는 것 <BR>
물론 @DynamicUpdate 어노테이션은 프로젝트 완성 후에 알았다.<BR>
난 IF ELSE 문으로 만들었다.<BR>
그리고 이번에 리엑트를 써보기 위해 React.createElement를 사용해 리스트, 페이징 기능을 처리했다 <BR>
지금은 잘 안쓰는 거 같다만 컴포넌트 맛은 대충 봤다.

앞으로는 세번 째 프로젝트 였던 BANK 를 손 볼 생각이다.
빠르게 손 보고 리엑트 JSX 를 공부를 해야겠다.












