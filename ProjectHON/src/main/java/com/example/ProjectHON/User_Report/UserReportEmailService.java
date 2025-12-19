package com.example.ProjectHON.User_Report;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class UserReportEmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendAfterUserGetReported(String email) throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setFrom("muskanshroti01@gmail.com");
        helper.setTo(email);
        helper.setSubject("You get Reported.");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>User Report Warning - HotOrNot</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #ffe5e5; border-left: 6px solid #e63946; padding: 15px;" +
                        "                   color: #c53030; font-size: 16px; border-radius: 6px; margin-top: 20px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Warning: Your Account Was Reported</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>We want to inform you that another user has reported your behavior on our platform.</p>" +
                        "    <p>Please ensure your interactions remain respectful and follow our community guidelines.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>Important Notice:</strong><br>" +
                        "        Your account has received a report.<br>" +
                        "        If your account receives <strong>3 reports in total</strong>, your chat feature will be <strong>temporarily disabled</strong> or restricted for safety reasons." +
                        "    </div>" +

                        "    <p>If you believe this report is false, you may ignore this message. However, repeated reports can impact your account.</p>" +
                        "    <p>For any help, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot / HOn. All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";



        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }

    public void sendAfterUserGettingThreeReports(String email)throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message  , true);

        helper.setFrom("muskanshroti01@gmail.com");
        helper.setTo(email);
        helper.setSubject("You get 3 times Reported.");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>User Report Warning - HotOrNot</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #ffe5e5; border-left: 6px solid #e63946; padding: 15px;" +
                        "                   color: #c53030; font-size: 16px; border-radius: 6px; margin-top: 20px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Warning: Your Account Was Reported 3 Times(Whisper.)</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>We want to inform you that another user has reported your behavior on our platform.</p>" +
                        "    <p>Please ensure your interactions remain respectful and follow our community guidelines.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>Important Notice:</strong><br>" +
                        "        Your account has received a report.<br>" +
                        "        If your account receives <strong>3 reports in total</strong>, your chat feature will be <strong>temporarily disabled</strong> or restricted for safety reasons." +
                        "    </div>" +

                        "    <p>If you believe this report is false, you may ignore this message. However, repeated reports can impact your account.</p>" +
                        "    <p>For any help, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot / HOn. All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";



        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }
}
