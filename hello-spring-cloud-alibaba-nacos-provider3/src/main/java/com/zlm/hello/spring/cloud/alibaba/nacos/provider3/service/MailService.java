package com.zlm.hello.spring.cloud.alibaba.nacos.provider3.service;

import com.zlm.hello.spring.cloud.alibaba.nacos.provider3.model.Mail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * 邮件业务类 MailService
 */
@Service
public class MailService {

    private Logger logger = LoggerFactory.getLogger(getClass());//提供日志类

    @Autowired
    private JavaMailSenderImpl mailSender;//注入邮件工具类


    /**
     * 发送邮件
     */
    public Mail sendMail(Mail mail) {
        try {
            checkMail(mail); //1.检测邮件
            sendMimeMail(mail); //2.发送邮件
            return saveMail(mail); //3.保存邮件
        } catch (Exception e) {
            logger.error("发送邮件失败:", e);//打印错误信息
            mail.setStatus("fail");
            mail.setError(e.getMessage());
            return mail;
        }

    }


    //检测邮件信息类
    private void checkMail(Mail mail) {
        if (StringUtils.isEmpty(mail.getTo())) {
            throw new RuntimeException("邮件收信人不能为空");
        }
        if (StringUtils.isEmpty(mail.getSubject())) {
            throw new RuntimeException("邮件主题不能为空");
        }
        if (StringUtils.isEmpty(mail.getText())) {
            throw new RuntimeException("邮件内容不能为空");
        }
    }

    //构建复杂邮件信息类
    private void sendMimeMail(Mail mail) {
        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mailSender.createMimeMessage(), true);//true表示支持复杂类型
            mail.setFrom(mailSender.getUsername());//邮件发信人从配置项读取
            messageHelper.setFrom(mail.getFrom());//邮件发信人
            messageHelper.setTo(mail.getTo().split(","));//邮件收信人
            messageHelper.setSubject(mail.getSubject());//邮件主题
            messageHelper.setText(mail.getText(),true);//邮件内容
            if (!StringUtils.isEmpty(mail.getCc())) {//抄送
                messageHelper.setCc(mail.getCc().split(","));
            }
            if (!StringUtils.isEmpty(mail.getBcc())) {//密送
                messageHelper.setBcc(mail.getBcc().split(","));
            }
            if (mail.getMultipartFiles() != null) {//添加邮件附件
                for (MultipartFile multipartFile : mail.getMultipartFiles()) {
                    messageHelper.addAttachment(multipartFile.getOriginalFilename(), multipartFile);
                }
            }
            if (StringUtils.isEmpty(mail.getSentDate())) {//发送时间
                mail.setSentDate(new Date());
                messageHelper.setSentDate(mail.getSentDate());
            }
            mailSender.send(messageHelper.getMimeMessage());//正式发送邮件
            mail.setStatus("ok");
            logger.info("发送邮件成功：{}->{}", mail.getFrom(), mail.getTo());
        } catch (Exception e) {
            throw new RuntimeException(e);//发送失败
        }
    }

    //保存邮件
    private Mail saveMail(Mail mial) {

        //将邮件保存到数据库..

        return mial;
    }

    //获取邮件发信人
    public String getMailSendFrom() {
        return mailSender.getJavaMailProperties().getProperty("from");
    }


}