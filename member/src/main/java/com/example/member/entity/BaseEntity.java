package com.example.member.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

// Entity Listener는 엔티티의 변화를 감지하고 데이블의 데이터를 조작하는 일을 한다.
@EntityListeners(value = {AuditingEntityListener.class})
// JPA Entity 들이 @MappedSuperClass가 선언된 클래스를 상속할 경우 클래스의 필드들도 컬럼으로 인식하도록 한다.
@MappedSuperclass
@Getter
public abstract class BaseEntity extends BaseTimeEntity{
    @CreatedBy
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;
}

// https://velog.io/@seongwon97/Spring-Boot-Entity-Listener
// https://velog.io/@minji104/CreatedBy-UpdatedBy-%EA%B8%B0%EB%8A%A5-%EA%B5%AC%ED%98%84