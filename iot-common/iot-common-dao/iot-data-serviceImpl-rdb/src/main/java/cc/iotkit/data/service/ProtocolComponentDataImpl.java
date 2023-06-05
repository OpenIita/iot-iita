package cc.iotkit.data.service;

import cc.iotkit.common.api.PageRequest;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.manager.IProtocolComponentData;
import cc.iotkit.data.dao.ProtocolComponentRepository;
import cc.iotkit.data.model.TbProtocolComponent;
import cc.iotkit.common.api.Paging;
import cc.iotkit.data.util.PageBuilder;
import cc.iotkit.data.util.PredicateBuilder;
import cc.iotkit.model.protocol.ProtocolComponent;
import com.querydsl.core.types.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static cc.iotkit.data.model.QTbSysConfig.tbSysConfig;

@Primary
@Service
public class ProtocolComponentDataImpl implements IProtocolComponentData, IJPACommData<ProtocolComponent, String> {

    @Autowired
    private ProtocolComponentRepository protocolComponentRepository;

    @Override
    public JpaRepository getBaseRepository() {
        return protocolComponentRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbProtocolComponent.class;
    }

    @Override
    public Class getTClass() {
        return ProtocolComponent.class;
    }

    @Override
    public List<ProtocolComponent> findByState(String state) {
        return MapstructUtils.convert(protocolComponentRepository.findByState(state), ProtocolComponent.class);
    }

    @Override
    public List<ProtocolComponent> findByStateAndType(String state, String type) {
        return MapstructUtils.convert(protocolComponentRepository.findByStateAndType(state, type), ProtocolComponent.class);
    }

    @Override
    public List<ProtocolComponent> findByUid(String uid) {
        return MapstructUtils.convert(protocolComponentRepository.findByUid(uid), ProtocolComponent.class);
    }

    @Override
    public Paging<ProtocolComponent> findByUid(String uid, int page, int size) {
        Page<TbProtocolComponent> paged = protocolComponentRepository.findByUid(uid,
                Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(paged.getTotalElements(),
                MapstructUtils.convert(paged.getContent(), ProtocolComponent.class));
    }

    @Override
    public long countByUid(String uid) {
        return protocolComponentRepository.countByUid(uid);
    }

    @Override
    public ProtocolComponent findById(String s) {
        return MapstructUtils.convert(protocolComponentRepository.findById(s).orElse(null), ProtocolComponent.class);
    }

    @Override
    public List<ProtocolComponent> findByIds(Collection<String> id) {
        return null;
    }

    @Override
    public ProtocolComponent save(ProtocolComponent data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
            data.setCreateAt(System.currentTimeMillis());
        }
        protocolComponentRepository.save(MapstructUtils.convert(data, TbProtocolComponent.class));
        return data;
    }

    @Override
    public void deleteById(String s) {
        protocolComponentRepository.deleteById(s);
    }

    @Override
    public List<ProtocolComponent> findAll() {
        return MapstructUtils.convert(protocolComponentRepository.findAll(), ProtocolComponent.class);
    }

    @Override
    public Paging<ProtocolComponent> findAll(PageRequest<ProtocolComponent> pageRequest) {
        ProtocolComponent query = pageRequest.getData();
        Predicate predicate = PredicateBuilder.instance()
                .build();
        Page<TbProtocolComponent> all = protocolComponentRepository.findAll(predicate, PageBuilder.toPageable(pageRequest));
        return new Paging<>(all.getTotalElements(), MapstructUtils.convert(all.getContent(), ProtocolComponent.class));
    }




}
