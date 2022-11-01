package com.example.shop.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter @Setter
@ToString
@Table(name="cart")
public class Cart extends BaseEntity{

    @Id
    @Column(name="cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY) // cart -> member_id (단방향)
    @JoinColumn(name="member_id")
    private Member member;

}
