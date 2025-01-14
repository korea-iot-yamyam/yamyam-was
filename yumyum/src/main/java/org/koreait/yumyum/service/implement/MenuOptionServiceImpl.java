package org.koreait.yumyum.service.implement;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.bcel.Const;
import org.aspectj.apache.bcel.generic.ClassGen;
import org.koreait.yumyum.common.constant.ResponseMessage;
import org.koreait.yumyum.dto.ResponseDto;
import org.koreait.yumyum.dto.menu.request.MenuOptionDetailRequestDto;
import org.koreait.yumyum.dto.menu.request.MenuOptionDetailUpdateRequestDto;
import org.koreait.yumyum.dto.menu.request.MenuOptionRequestDto;
import org.koreait.yumyum.dto.menu.request.MenuOptionUpdateRequestDto;
import org.koreait.yumyum.dto.menu.response.MenuOptionResponseDto;
import org.koreait.yumyum.entity.Menu;
import org.koreait.yumyum.entity.MenuOption;
import org.koreait.yumyum.entity.MenuOptionDetail;
import org.koreait.yumyum.entity.MenuOptionGroup;
import org.koreait.yumyum.repository.MenuOptionDetailRepository;
import org.koreait.yumyum.repository.MenuOptionGroupRepository;
import org.koreait.yumyum.repository.MenuOptionRepository;
import org.koreait.yumyum.repository.MenuRepository;
import org.koreait.yumyum.service.MenuOptionDetailService;
import org.koreait.yumyum.service.MenuOptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuOptionServiceImpl implements MenuOptionService {

    private final MenuOptionRepository menuOptionRepository;

    private final MenuRepository menuRepository;

    private final MenuOptionGroupRepository menuOptionGroupRepository;

    @Autowired
    private final MenuOptionDetailService menuOptionDetailService;
    @Autowired
    private MenuOptionDetailRepository menuOptionDetailRepository;

    @Override
    public ResponseDto<MenuOptionResponseDto> addMenuOption(MenuOptionRequestDto dto, Long id) {
        MenuOptionResponseDto data = null;

        try {
            Menu menu = menuRepository.findById(dto.getMenuId())
                    .orElseThrow(() -> new Error(ResponseMessage.NOT_EXIST_DATA));

            MenuOption menuOption = MenuOption.builder()
                    .optionName(dto.getOptionName())
                    .build();
            MenuOption savedMenuOption = menuOptionRepository.save(menuOption);
            List<MenuOptionDetailRequestDto> details = dto.getOptionDetails();
            if(details != null) {
                for (MenuOptionDetailRequestDto detailDto : details) {
                    detailDto.setMenuOptionId(savedMenuOption.getId());
                    menuOptionDetailService.addOptionDetail(detailDto, id);
                }
            }
            MenuOptionGroup menuOptionGroup = MenuOptionGroup.builder()
                    .menu(menu)
                    .menuOption(menuOption)
                    .build();
            menuOptionGroupRepository.save(menuOptionGroup);

            data = new MenuOptionResponseDto(savedMenuOption);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }

    @Override
    public ResponseDto<MenuOptionResponseDto> updateMenuOption(MenuOptionUpdateRequestDto dto, Long optionId, Long id) {
        MenuOptionResponseDto data = null;

        try {
            List<MenuOptionDetailUpdateRequestDto> details = dto.getOptionDetails();
            List<Long> menuOptionIds = menuOptionDetailRepository.findIdByMenuOptionId(optionId);
            if(details != null) {
                int i = 0;
                for (MenuOptionDetailUpdateRequestDto detailDto : details) {
//                    System.out.println("이건 나오나: " + );
//                    System.out.println("이건 안나올꺼고: " + detailDto.getDetailName());
                    Long pkId = menuOptionIds.get(i);
                    menuOptionDetailService.updateOptionDetail(detailDto, optionId, pkId, id);
                    i++;
                }


                } else {
                    return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
                }
            } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, data);
    }


    @Override
    public ResponseDto<Void> deleteMenuOption(Long optionId, Long id) {
        try {
            Optional<MenuOption> menuOptionOptional = menuOptionRepository.findById(optionId);

            if (menuOptionOptional.isPresent()) {
                MenuOption menuOption = menuOptionOptional.get();
                menuOptionRepository.delete(menuOption);
            } else {
                return ResponseDto.setFailed(ResponseMessage.NOT_EXIST_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseDto.setFailed(ResponseMessage.DATABASE_ERROR);
        }
        return ResponseDto.setSuccess(ResponseMessage.SUCCESS, null);
    }
}

