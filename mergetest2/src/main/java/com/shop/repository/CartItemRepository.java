package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.dto.CartItemDto;
import com.shop.entity.Cart;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.Querydsl;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);
    // 붙일 것을 감안해서 " 마지막은 띄우는게 좋다
    @Query("select new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) " +
            "from CartItem ci, ItemImg im " +
            "join ci.item i " +
            "where ci.cart.id = :cartId " +
            "and im.item.id = ci.item.id " +
            "and im.repimgYn = 'Y' " +
            "order by ci.regTime desc"
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
