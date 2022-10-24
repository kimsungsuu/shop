package com.example.shop.repository;

import com.example.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item> {

    List<Item> findByItemNm(String itemNm);
    /**
     * or 조건 처리 메서드
     */
    List<Item> findByItemNmOrItemDetail(String itemNm, String ItemDetail);

    /*
    * LessThan 조건 처리
    * */
    List<Item> findByPriceLessThan(Integer price);

    /*
    * Greater than
    * */
    List<Item> findByPriceGreaterThan(Integer price);

    /*
    * Order By
    * */
    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    /*
    * Order By using JPQL @Query
    * */

    @Query("select i from Item i where i.itemDetail like %:itemDetail% ")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);

    @Query(value="select * from item i where i.item_detail like %:itemDetail% order by i.price desc", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);
}
