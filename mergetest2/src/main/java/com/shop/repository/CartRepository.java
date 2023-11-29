package com.shop.repository;

import com.shop.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByMemberId(Long id);
    // 현재 로그인한 회원의 Cart 엔티티를 찾기 위해 쿼리 메소드를 만든다.
}
