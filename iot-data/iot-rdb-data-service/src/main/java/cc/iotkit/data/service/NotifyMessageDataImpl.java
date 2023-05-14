package cc.iotkit.data.service;

import cc.iotkit.data.INotifyMessageData;
import cc.iotkit.data.dao.NotifyMessageRepository;
import cc.iotkit.data.model.NotifyMessageMapper;
import cc.iotkit.data.model.TbNotifyMessage;
import cc.iotkit.model.Paging;
import cc.iotkit.model.notify.NotifyMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Author: 石恒
 * @Date: 2023/5/13 18:35
 * @Description:
 */
@Primary
@Service
public class NotifyMessageDataImpl implements INotifyMessageData {

    @Resource
    private NotifyMessageRepository notifyMessageRepository;


    @Override
    public NotifyMessage findById(String id) {
        return NotifyMessageMapper.M.toDto(notifyMessageRepository.findById(id).orElse(null));
    }

    @Override
    public NotifyMessage save(NotifyMessage data) {
        if (StringUtils.isBlank(data.getId())) {
            data.setId(UUID.randomUUID().toString());
        }
        data.setUpdateAt(System.currentTimeMillis());
        TbNotifyMessage tbNotifyMessage = NotifyMessageMapper.M.toVo(data);
        notifyMessageRepository.save(tbNotifyMessage);
        return data;
    }

    @Override
    public NotifyMessage add(NotifyMessage data) {
        Long current = System.currentTimeMillis();
        data.setCreateAt(current);
        data.setUpdateAt(current);
        return save(data);
    }

    @Override
    public void deleteById(String id) {
        notifyMessageRepository.deleteById(id);
    }

    @Override
    public long count() {
        return notifyMessageRepository.count();
    }

    @Override
    public List<NotifyMessage> findAll() {
        return notifyMessageRepository.findAll().stream().map(NotifyMessageMapper.M::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Paging<NotifyMessage> findAll(int page, int size) {
        Page<TbNotifyMessage> tbNotifyMessages = notifyMessageRepository.findAll(Pageable.ofSize(size).withPage(page - 1));
        return new Paging<>(
                tbNotifyMessages.getTotalElements(),
                tbNotifyMessages.getContent()
                        .stream().map(NotifyMessageMapper.M::toDto)
                        .collect(Collectors.toList())
        );
    }
}
