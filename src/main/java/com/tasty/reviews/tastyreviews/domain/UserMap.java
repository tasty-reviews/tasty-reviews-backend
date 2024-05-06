package com.tasty.reviews.tastyreviews.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
public class UserMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "UserMap_Id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    private String imageurl;

    // 여러 개의 음식점을 저장하기 위한 List
    @OneToMany(mappedBy = "userMap", cascade = CascadeType.ALL)
    private List<Restaurant> restaurants = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "Member_id", nullable = false)
    private Member member;

}
