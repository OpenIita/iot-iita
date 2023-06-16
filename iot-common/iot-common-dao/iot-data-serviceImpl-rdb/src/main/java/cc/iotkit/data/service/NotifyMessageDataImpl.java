package cc.iotkit.data.service;

import cc.iotkit.common.api.Paging;
import cc.iotkit.common.utils.MapstructUtils;
import cc.iotkit.data.dao.IJPACommData;
import cc.iotkit.data.dao.NotifyMessageRepository;
import cc.iotkit.data.manager.INotifyMessageData;
import cc.iotkit.data.model.TbNotifyMessage;
import cc.iotkit.data.model.TbOtaPackage;
import cc.iotkit.model.notify.Channel;
import cc.iotkit.model.notify.NotifyMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
public class NotifyMessageDataImpl implements INotifyMessageData, IJPACommData<NotifyMessage, String> {

    @Resource
    private NotifyMessageRepository notifyMessageRepository;


    @Override
    public JpaRepository getBaseRepository() {
        return notifyMessageRepository;
    }

    @Override
    public Class getJpaRepositoryClass() {
        return TbNotifyMessage.class;
    }

    @Override
    public Class getTClass() {
        return NotifyMessage.class;
    }

}
