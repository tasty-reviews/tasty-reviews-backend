package com.tasty.reviews.tastyreviews.global.common;

// 패키지 선언부: 현재 클래스가 속한 패키지를 정의합니다.
// 패키지 이름은 com.tasty.reviews.tastyreviews.global.common입니다.

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// 필요한 라이브러리와 클래스를 임포트합니다.
// jakarta.persistence 패키지: JPA를 사용하기 위한 클래스들을 포함합니다.
// lombok.Getter: 롬복 라이브러리에서 제공하는 어노테이션으로, 모든 필드에 대한 getter 메서드를 자동으로 생성합니다.
// org.springframework.data.annotation 패키지: 스프링 데이터에서 사용되는 어노테이션들을 포함합니다.
// java.time 패키지: Java 8에서 추가된 날짜와 시간 API를 사용합니다.

@MappedSuperclass
// @MappedSuperclass 어노테이션은 이 클래스를 직접 테이블로 매핑하지 않고, 다른 엔티티 클래스들이
// 이 클래스를 상속받아 사용할 수 있도록 합니다. 상속받은 클래스는 이 클래스의 필드를 자신의
// 테이블에 컬럼으로 포함하게 됩니다.

@Getter
// @Getter 어노테이션은 롬복(Lombok) 라이브러리를 사용하여 모든 필드에 대해 자동으로 getter 메서드를 생성합니다.

@EntityListeners(AbstractMethodError.class)
// @EntityListeners 어노테이션은 JPA 엔티티의 생명주기 이벤트를 리스닝하는 클래스를 지정합니다.
// 여기서는 AbstractMethodError 클래스를 리스너로 지정합니다.

public abstract class BaseTimeEntity {
    // BaseTimeEntity 클래스는 추상 클래스입니다. 이 클래스는 다른 엔티티 클래스들이 상속받아
    // 생성 및 수정 시간 정보를 자동으로 관리할 수 있도록 합니다.

    @Column(name = "created_date", nullable = false)
    @CreatedDate
    // @CreatedDate 어노테이션은 엔티티가 생성될 때 생성 일자를 자동으로 주입받도록 합니다.
    private String createdDate;

    @Column(name = "modified_date", nullable = false)
    @LastModifiedDate
    // @LastModifiedDate 어노테이션은 엔티티가 수정될 때 수정 일자를 자동으로 주입받도록 합니다.
    private String modifiedDate;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    // @PrePersist 어노테이션은 엔티티가 영속성 컨텍스트에 처음 저장되기 전에 실행될 메서드를 지정합니다.
    public void onPrePersist() {
        this.createdDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        // 현재 시간을 "yyyy.MM.dd" 형식으로 포맷하여 createdDate 필드에 설정합니다.
        this.modifiedDate = this.createdDate;
        // createdDate와 동일한 값을 modifiedDate에 설정합니다.
    }

    /* 해당 엔티티를 업데이트 하기 이전에 실행*/
    @PreUpdate
    // @PreUpdate 어노테이션은 엔티티가 수정되기 전에 실행될 메서드를 지정합니다.
    public void onPreUpdate() {
        this.modifiedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
        // 현재 시간을 "yyyy.MM.dd HH:mm" 형식으로 포맷하여 modifiedDate 필드에 설정합니다.
    }
}
