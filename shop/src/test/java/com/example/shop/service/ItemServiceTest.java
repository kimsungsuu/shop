package com.example.shop.service;

import com.example.shop.constant.ItemSellStatus;
import com.example.shop.dto.ItemFormDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
public class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception{ // MockMultipartFile 클래스를 이용하여 가짜 MultipartFime 리스트를 만들어서 반환해주는 메소드

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile =
                    new MockMultipartFile(path, imageName, "image/jps", new byte[]{1,2,3,4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username="admin", roles = "ADMIN")
    void saveItem() throws Exception{
        ItemFormDto itemFormDto = new ItemFormDto(); // 상품 등록 화면에서 입력 받는 상품 데이터를 세팅해준다.
        itemFormDto.setItemNm("테스트 상품");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품입니다.");
        itemFormDto.setPrice(1000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList); // 상품 데이터와 이미지 정보를 파라미터로 넘겨서 저장 후 저장된 상품의 아이디 값을 반환 값으로 리턴해준다.

        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityExistsException::new);

        assertEquals(itemFormDto.getItemNm(), item.getItemNm()); // 입력한 상품 데이터와 실제로 저장된 상품 데이터가 같은지 확인한다.
        assertEquals(itemFormDto.getItemSellStatus(), item.getItemSellStatus());
        assertEquals(itemFormDto.getItemDetail(), item.getItemDetail());
        assertEquals(itemFormDto.getPrice(), item.getPrice());
        assertEquals(itemFormDto.getStockNumber(), item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(), itemImgList.get(0).getOriImgName()); // 상품 이미지는 첫 번째 파일의 원본 이미지 파일 이름만 같은지 확인한다.
    }
    /**
     * 테스트 실행 시 더미 데이터로 넣은 이미지 데이터가 상품 이미지를 저장하는 폴더에 생성된다.
     */
}
