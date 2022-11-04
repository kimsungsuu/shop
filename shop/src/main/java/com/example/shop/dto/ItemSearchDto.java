package com.example.shop.dto;

import com.example.shop.constant.ItemSellStatus;
import lombok.Getter;
import lombok.Setter;

/**
 * 상품 조회 조건을 가지고 있는 클래스
 */
@Getter @Setter
public class ItemSearchDto {

    private String searchDataType;

    private ItemSellStatus searchSellStatus;

    private String searchBy;

    private String searchQuery = "";
}
