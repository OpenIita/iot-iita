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

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.ITaskInfoData;
import cc.iotkit.data.dao.TaskInfoRepository;
import cc.iotkit.data.model.TbTaskInfo;
import cc.iotkit.data.service.convert.TaskInfoMapper;
import cc.iotkit.common.api.Paging;
import cc.iotkit.model.rule.TaskInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class TaskInfoDataImpl implements ITaskInfoData, IJPACommData<TaskInfo, String> {

    @Autowired
    private TaskInfoRepository taskInfoRepository;

    @Override
    public List<TaskInfo> findByUid(String uid) {
        return TaskInfoMapper.toDto(taskInfoRepository.findByUid(uid));
    }

    @Override
    public Paging<TaskInfo> findByUid(String uid, int page, int size) {
        Page<TbTaskInfo> paged = taskInfoRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                TaskInfoMapper.toDto(paged.getContent()));
    }

    @Override
    public long countByUid(String uid) {
        return 0;
    }

    @Override
    public JpaRepository getBaseRepository() {
        return taskInfoRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbTaskInfo.class;
    }

    @Override
    public TaskInfo findById(String s) {
        return TaskInfoMapper.toDtoFix(taskInfoRepository.findById(s).orElse(null));
    }



    @Override
    public TaskInfo save(TaskInfo data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        taskInfoRepository.save(TaskInfoMapper.toVoFix(data));
        return data;
    }





}
