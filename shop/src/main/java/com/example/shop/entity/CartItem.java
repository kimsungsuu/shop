package com.example.shop.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@Table(name="cart_item")
public class CartItem extends BaseEntity{

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 장바구니에는 여러 상품이 담길 수 있으므로
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY) // 하나의 상품은 여러 장바구니에 담길 수 있으므로
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;
}
