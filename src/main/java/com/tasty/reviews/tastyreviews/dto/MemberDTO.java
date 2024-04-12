//package com.tasty.reviews.tastyreviews.dto;
//
//import com.tasty.reviews.tastyreviews.domain.Gender;
//import com.tasty.reviews.tastyreviews.domain.Member;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//public class MemberDTO { //회원가입에서 클라이언트가 보낸 정보를 전달하는 DTO
//
//    private Long id;
//    private String email;
//    private String password;
//    private int age;
//    private String nickname;
//    private Gender gender;
//
//    public Member toEntity() {
//        return Member.builder()
//                .id(this.id)
//                .email(this.email)
//                .password(this.password)
//                .age(this.age)
//                .nickname(this.nickname)
//                .gender(this.gender)
//                .build();
//    }
//
//    public static MemberDTO fromEntity(Member member) {
//        return MemberDTO.builder()
//                .id(member.getId())
//                .email(member.getEmail())
//                .password(member.getPassword())
//                .age(member.getAge())
//                .nickname(member.getNickname())
//                .gender(member.getGender())
//                .build();
//    }
//}