package com.zfm.gleaning.util;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.zfm.gleaning.MailConfiguration;

public class MailUtils {
    private static JavaMailSenderImpl mailSender = createMailSender();
    public static final String content3 = "尊敬的用户，您好：本邮件为您在我方网站“拾遗”证件捡拾归还系统中找回密码所用，若不是本人操作，"
    		+ "请忽略本邮件。如果是您本人操作，那么您的验证码为：$1，请前往地址http://localhost:8080/gleaning/forget-new?id=$2";
    public static final String content0 = "尊敬的用户，您好：您在我方网站“拾遗”证件捡拾归还系统中发布的招领证件已经被用户认领，请及时前来查看邮寄信息，能够及时归还对于双方都是一件好事，万分感谢！";
    public static final String content1 = "尊敬的用户，您好：您在我方网站“拾遗”证件捡拾归还系统中认领的证件已经被捡拾者邮寄给您，快递单号是$1，请及时查收，验收成功记得来平台提交完成噢！";
    
    /**
     * 邮件发送器
     *
     * @return 配置好的工具
     */
    private static JavaMailSenderImpl createMailSender() {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(MailConfiguration.getHost());
        sender.setPort(MailConfiguration.getPort());
        sender.setUsername(MailConfiguration.getUsername());
        sender.setPassword(MailConfiguration.getPassword());
        sender.setDefaultEncoding(MailConfiguration.getEncoding());
        Properties p = new Properties();
        p.setProperty("mail.smtp.timeout", MailConfiguration.getTimeout() + "");
        p.setProperty("mail.smtp.auth", MailConfiguration.getAuth() + "");
        p.setProperty("mail.smtp.starttls.enable", MailConfiguration.getStarttlsEnable() + "");
        sender.setJavaMailProperties(p);
        return sender;
    }
    /**
     * 发送邮件
     *
     * @param to 接受人
     * @param subject 主题
     * @param html 发送内容
     * @throws MessagingException 异常
     * @throws UnsupportedEncodingException 异常
     */
    public static void sendHtmlMail(String to, String subject, String html) throws MessagingException,UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        // 设置utf-8或GBK编码，否则邮件会有乱码
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true,MailConfiguration.getEncoding());
        messageHelper.setFrom(MailConfiguration.getFrom(), "系统名称");
        messageHelper.setTo(to);
        messageHelper.setSubject(subject);
        messageHelper.setText(html, true);
        mailSender.send(mimeMessage);
    }

}
