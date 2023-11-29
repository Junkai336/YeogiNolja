package com.shop.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class}) //생성일과 수정일 자동관리
@MappedSuperclass // 다른엔티티 클래스에서 상속 받아사용할 수 있습니다.
@Getter
@Setter
public abstract class BaseTimeEntity {
    @CreatedDate // 엔티티가 생성될때 해당 필드에 자동으로 생성일 저장
    @Column(updatable = false)
    private LocalDateTime regTime;
    @LastModifiedDate//: JPA의 Auditing 기능으로, 엔터티가 수정될 때 해당 필드에 자동으로 수정일을 저장
    private LocalDateTime updateTime;
}
