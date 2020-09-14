package com.wwjjbt.sob_blog_system_mp.utils;



import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class SendMail {
    // 发件人邮箱地址
    private static String from = "2354879324@qq.com";
    // 发件人称号，同邮箱地址
    private static String user = "2354879324@qq.com";
    // 发件人邮箱客户端授权码
    private static String password = "scyvhluwfpvadjgb";
    //发件人的邮箱服务器
    private static String mailHost = "smtp.qq.com";

    /**
     * @param to
     * @param text
     * @param title
     */
    /* 发送验证信息的邮件 */
    public static boolean sendMail(String to, String text, String title) {
        Properties props = new Properties();
// 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）
        props.put("mail.smtp.host", mailHost);
// 需要经过授权，也就是有户名和密码的校验，这样才能通过验证（一定要有这一条）
        props.put("mail.smtp.auth", "true");
// 用刚刚设置好的props对象构建一个session
        Session session = Session.getDefaultInstance(props);
// 有了这句便可以在发送邮件的过程中在console处显示过程信息，供调试使用（你可以在控制台（console)上看到发送邮件的过程）
// session.setDebug(true);
// 用session为参数定义消息对象
        MimeMessage message = new MimeMessage(session);
        try {
// 加载发件人地址
            message.setFrom(new InternetAddress(from));
// 加载收件人地址
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
// 加载标题
            message.setSubject(title);
// 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();
// 设置邮件的文本内容
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(text, "text/html;charset=utf-8");
            multipart.addBodyPart(contentPart);
            message.setContent(multipart);
            message.saveChanges(); // 保存变化
// 连接服务器的邮箱
            Transport transport = session.getTransport("smtp");
// 把邮件发送出去
            transport.connect(mailHost, user, password);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }



        return true;
    }

    public static void sendEmailCode(String emailCode,String emailAddress){
        String toMail = emailAddress;
        String text = "你的邮箱验证码是："+emailCode+"请在有效期【十分钟内】完成注册，非本人操作请忽略此邮件";
        String title = "沙雕博客系统：";
        sendMail(toMail, text, title);
    }
    public static void sendCommendNotify(String commend,String emailAddress){
        String toMail = emailAddress;
        String text = "洋波科技博客系统-评论通知："+commend+"";
        String title = "洋波科技博客系统：";
        sendMail(toMail, text, title);
    }

    public static void main(String[] args) { // 做测试用
//String toMail="xxxxx@fenglinggame.com";
        String toMail = "212508940@qq.com";
        String text = "你好有惊喜噢";
        String title = "测试小通知";
        sendMail(toMail, text, title);
    }

}