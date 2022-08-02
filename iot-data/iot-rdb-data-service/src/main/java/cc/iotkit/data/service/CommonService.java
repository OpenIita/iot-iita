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

import cc.iotkit.data.ICommonData;
import cc.iotkit.model.Id;
import cc.iotkit.model.Paging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public class CommonService<VO extends Id<T>, T> implements ICommonData<VO, T> {

    private final JpaRepository<VO, T> repository;

    public CommonService(JpaRepository<VO, T> repository) {
        this.repository = repository;
    }

    @Override
    public VO findById(T id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public VO save(VO data) {
        return repository.save(data);
    }

    @Override
    public VO add(VO data) {
        return repository.save(data);
    }

    @Override
    public void deleteById(T id) {
        repository.deleteById(id);
    }

    @Override
    public long count() {
        return repository.count();
    }

    @Override
    public List<VO> findAll() {
        return repository.findAll();
    }

    @Override
    public Paging<VO> findAll(int page, int size) {
        Page<VO> rst = repository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(rst.getTotalElements(), rst.getContent());
    }
}
