/*
 * +----------------------------------------------------------------------
 * | Copyright (c) 奇特物联 2021-2022 All rights reserved.
 * +----------------------------------------------------------------------
 * | Licensed 未经许可不能去掉「奇特物联」相关版权
 * +----------------------------------------------------------------------
 * | Author: xw2sy@163.com
 * +----------------------------------------------------------------------
 */
package cc.iotkit.data.service;

import cc.iotkit.data.ICategoryData;
import cc.iotkit.data.dao.CategoryRepository;
import cc.iotkit.data.model.CategoryMapper;
import cc.iotkit.data.model.TbCategory;
import cc.iotkit.model.Paging;
import cc.iotkit.model.product.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Primary
@Service
public class CategoryDataImpl implements ICategoryData {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category findById(String s) {
        return CategoryMapper.M.toDto(categoryRepository.findById(s).orElse(null));
    }

    @Override
    public Category save(Category data) {
        TbCategory tb = categoryRepository.save(CategoryMapper.M.toVo(data));
        data.setId(tb.getId());
        return data;
    }

    @Override
    public Category add(Category data) {
        TbCategory tb = categoryRepository.save(CategoryMapper.M.toVo(data));
        data.setId(tb.getId());
        return data;
    }

    @Override
    public void deleteById(String s) {
        categoryRepository.deleteById(s);
    }

    @Override
    public long count() {
        return categoryRepository.count();
    }

    @Override
    public List<Category> findAll() {
        return categoryRepository.findAll().stream()
                .map(CategoryMapper.M::toDto).collect(Collectors.toList());
    }

    @Override
    public Paging<Category> findAll(int page, int size) {
        return new Paging<>(
                categoryRepository.count(),
                categoryRepository.findAll(Pageable.ofSize(size).withPage(page - 1))
                        .stream().map(CategoryMapper.M::toDto).collect(Collectors.toList())
        );
    }

}
