package com.example.shop.dto;

import com.example.shop.entity.ItemImg;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;

@Getter @Setter
public class ItemImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper(); // 멤버 변수로 ModelMapper 객체를 추가

    public static ItemImgDto of(ItemImg itemImg){
        return modelMapper.map(itemImg, ItemImgDto.class); // itemImg 엔티티 객체를 파라미터로 받아서 itemImg 객체의 자료형과 멤버변수의 이름이 같을 때 itemImgDto로 값을 복사해서 반환한다. static 메소드로 선언해 itemImgDto 객체를 생성하지 않아도 호출할 수 있도록 한다.
    }
}
