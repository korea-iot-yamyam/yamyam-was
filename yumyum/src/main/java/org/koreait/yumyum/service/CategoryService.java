package org.koreait.yumyum.service;


import jakarta.validation.Valid;
import org.koreait.yumyum.dto.ResponseDto;
import org.koreait.yumyum.dto.category.request.CreateCategoryRequestDto;
import org.koreait.yumyum.dto.category.request.UpdateCategoryRequestDto;
import org.koreait.yumyum.dto.category.response.CategoryResponseDto;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface CategoryService {
    ResponseDto<List<CategoryResponseDto>> getCategories(Long id);

    ResponseDto<CategoryResponseDto> createCategory(Long userId, @Valid CreateCategoryRequestDto dto);

    ResponseDto<CategoryResponseDto> updateCategory(@Valid UpdateCategoryRequestDto dto, Long id);

    ResponseDto<Void> deleteCategory(Long id);
}
