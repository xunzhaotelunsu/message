package com.baosight.message.sender.holder;

import com.baosight.message.persist.mapper.EmailSendServerMapper;
import com.baosight.message.persist.po.EmailSendServer;
import com.baosight.message.sender.extra.ExEmailSender;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yang on 2018/7/11.
 */
@Component
@Slf4j
public class ExEmailSenderHolder implements InitializingBean {

    private List<ExEmailSender> exEmailSenders;

    @Autowired
    EmailSendServerMapper emailSendServerMapper;

    public void refresh() {
        log.info("refreshing email send servers");
        try {
            setExEmailSenders();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<ExEmailSender> getExEmailSenders() {
        return this.exEmailSenders;
    }

    public ExEmailSender getExEmailSender(String serverCode) {
        return this.exEmailSenders.stream().filter(exEmailSender -> serverCode.equals(exEmailSender.getServerCode())).findFirst().get();
    }

    private synchronized void setExEmailSenders() {
        List<EmailSendServer> emailSendServers = emailSendServerMapper.getEmailSendServers();
        this.exEmailSenders = emailSendServers.stream().map(emailSendServer -> convert(emailSendServer)).collect(Collectors.toList());
    }

    private ExEmailSender convert(EmailSendServer emailSendServer) {
        ExEmailSender exEmailSender = new ExEmailSender();
        exEmailSender.setHost(emailSendServer.getHost());
        exEmailSender.setPort(emailSendServer.getPort());
        exEmailSender.setUsername(emailSendServer.getUsername());
        exEmailSender.setPassword(emailSendServer.getPassword());
        exEmailSender.setPersonal(emailSendServer.getPersonal());
        exEmailSender.setPriority(emailSendServer.getPriority());
        exEmailSender.setServerCode(emailSendServer.getServerCode());
        return exEmailSender;
    }

    /**
     * Invoked by a BeanFactory after it has set all bean properties supplied
     * (and satisfied BeanFactoryAware and ApplicationContextAware).
     * <p>This method allows the bean instance to perform initialization only
     * possible when all bean properties have been set and to throw an
     * exception in the event of misconfiguration.
     *
     * @throws Exception in the event of misconfiguration (such
     *                   as failure to set an essential property) or if initialization fails.
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("loading email send servers");
        try {
            setExEmailSenders();
            if(CollectionUtils.isEmpty(exEmailSenders)){
                throw new RuntimeException("no active email send server!");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("no active email send server!");
        }
    }

}
