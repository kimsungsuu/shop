package com.example.shop.service;

import com.example.shop.dto.ItemFormDto;
import com.example.shop.dto.ItemImgDto;
import com.example.shop.entity.Item;
import com.example.shop.entity.ItemImg;
import com.example.shop.repository.ItemImgRepository;
import com.example.shop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto,
                         List<MultipartFile> itemImgFileList) throws Exception{

        //상품 등록
        Item item = itemFormDto.createItem(); // 상품 등록 폼으로부터 입력 받은 데이터를 이용하여 item 객체를 생성
        itemRepository.save(item); // 상품 데이터를 저장

        //이미지 등록
        for(int i = 0; i<itemImgFileList.size(); i++){
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if(i == 0) // 첫 번째 이미지일 경우 대표 상품 이미지 여부 값을 Y로 세팅, 나머지 상품 이미지는 N으로 설정
                itemImg.setRepimgYn("Y");
            else
                itemImg.setRepimgYn("N");
            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i)); //
        }

        return item.getId();

    }

    @Transactional(readOnly = true) // 상품 데이터를 읽어오는 트랜잭션을 읽기 전용으로 설정. 이럴 경우 JPA가 더티체킹(변경감지)을 수행하지 않아서 성능을 향상 시킬 수 있다.
    public ItemFormDto getItemDtl(Long itemId){

        List<ItemImg> itemImgList =
                itemImgRepository.findByItemIdOrderByIdAsc(itemId); // 해당 상품의 이미지를 조회한다. 등록순으로 가져오기 위해 상품 이미지 아이디 오름차순으로 가지고 온다.
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for(ItemImg itemImg : itemImgList){ // 조회한 ItemImg 엔티티를 ItemImgDto 객체를 만들어서 리스트에 추가
            ItemImgDto itemImgDto = ItemImgDto.of(itemImg);
            itemImgDtoList.add(itemImgDto);
        }

        Item item = itemRepository.findById(itemId) // 상품의 아이디를 통해 상품 엔티티 조회, 존재하지 않을 시에는 EntityNotFoundException을 발생
                .orElseThrow(EntityNotFoundException::new);
        ItemFormDto itemFormDto = ItemFormDto.of(item);
        itemFormDto.setItemImgDtoList(itemImgDtoList);
        return itemFormDto;
    }

    public Long updateItem(ItemFormDto itemFormDto, List<MultipartFile> itemImgFileList) throws Exception{
        // 상품 수정
        Item item = itemRepository.findById(itemFormDto.getId()) // 상품 등록 화면으로부터 전달 받은 상품 아이디를 이용하여 상품 엔티티를 조회한다.
                .orElseThrow(EntityNotFoundException::new);
        item.updateItem(itemFormDto); // 상품 등록 화면으로부터 전달 받은 ItemFormDto를 통해 상품 엔티티를 업데이트 한다.

        List<Long> itemImgIds = itemFormDto.getItemImgIds(); //상품 이미지 아이디 리스트를 조회한다.

        // 이미지 등록
        for(int i=0; i<itemImgFileList.size(); i++){
            itemImgService.updateItemImg(itemImgIds.get(i),
            itemImgFileList.get(i)); // 상품 이미지를 업데이트하기 위해서 updateItemImg() 메소드에 상품 이미지 아이디와, 상품 이미지 파일 정보를 파라미터로 전달한다.
        }

        return item.getId();
    }
}
