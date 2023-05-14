package cc.iotkit.message.listener;

import cc.iotkit.data.IChannelConfigData;
import cc.iotkit.data.IChannelTemplateData;
import cc.iotkit.data.INotifyMessageData;
import cc.iotkit.message.enums.MessageTypeEnum;
import cc.iotkit.message.event.MessageEvent;
import cc.iotkit.message.model.MessageSend;
import cc.iotkit.model.notify.ChannelConfig;
import cc.iotkit.model.notify.NotifyMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * author: 石恒
 * date: 2023-05-08 15:09
 * description:
 **/
@Slf4j
@Component
public class EmailEventListener implements MessageEventListener {

    @Resource
    private IChannelConfigData iChannelConfigData;
    @Resource
    private IChannelTemplateData iChannelTemplateData;

    @Resource
    private INotifyMessageData iNotifyMessageData;

    @Override
    @EventListener
    public void doEvent(MessageEvent messageEvent) {
        MessageSend message = messageEvent.getMessage();
        ChannelConfig channelConfig = getChannelConfig(message.getChannelTemplate().getChannelConfigId());
        ChannelConfig.ChannelParam param = channelConfig.getParam();

        JavaMailSenderImpl jms = new JavaMailSenderImpl();
        jms.setHost(param.getHost());
        jms.setUsername(param.getUserName());
        jms.setPassword(param.getPassWord());
        jms.setDefaultEncoding("utf-8");
        Properties p = new Properties();
        p.setProperty("mail.smtp.auth", String.valueOf(null == param.getMailSmtpAuth() || param.getMailSmtpAuth()));
        jms.setJavaMailProperties(p);
        MimeMessage mimeMessage = jms.createMimeMessage();

        try {
            String content = getContentFormat(message.getParam(), message.getChannelTemplate().getContent());

            NotifyMessage notifyMessage = addNotifyMessage(content, message.getMessageType());

            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            //收件人
            String[] split = param.getTo().split(",");
            messageHelper.setTo(split);
            //标题
            messageHelper.setSubject(getContentFormat(message.getParam(), channelConfig.getTitle()));
            //内容
            messageHelper.setText(content, true);
            //发件人
            messageHelper.setFrom(param.getFrom());
            jms.send(mimeMessage);
            notifyMessage.setStatus(Boolean.TRUE);
            iNotifyMessageData.save(notifyMessage);
        } catch (MessagingException e) {
            log.error("发送邮件失败.", e);
        }
    }

    @Override
    public ChannelConfig getChannelConfig(String channelConfigId) {
        return iChannelConfigData.findById(channelConfigId);
    }

    @Override
    public NotifyMessage addNotifyMessage(String content, MessageTypeEnum messageType) {
        return iNotifyMessageData.add(NotifyMessage.builder()
                .content(content)
                .messageType(messageType.getCode())
                .status(Boolean.FALSE)
                .build());
    }
}
