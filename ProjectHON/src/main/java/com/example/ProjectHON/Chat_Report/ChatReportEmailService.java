package com.example.ProjectHON.Chat_Report;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class ChatReportEmailService {


    @Autowired
    JavaMailSender javaMailSender;

    public void sendAfterGettingReportChat(String email)throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setFrom("muskanshroti01@gmail.com");
        helper.setTo(email);
        helper.setSubject("Your chat get reported");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Chat Report Warning - HotOrNot</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #ffe5e5; border-left: 6px solid #e63946; padding: 15px; " +
                        "                   color: #c53030; font-size: 16px; border-radius: 6px; margin-top: 20px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Warning: Your Chat Was Reported</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>We want to inform you that <strong>one of your messages</strong> has been reported by another user for inappropriate behavior.</p>" +
                        "    <p>Please make sure your conversations follow our community guidelines and remain respectful at all times.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>Important:</strong><br>" +
                        "        You have received a report on your chat.<br>" +
                        "        <strong>3 total reports</strong> will result in your chat feature being <strong>temporarily disabled</strong> or restricted." +
                        "    </div>" +

                        "    <p>If you believe this report was a mistake, you may ignore this message. Our team regularly reviews repeated reports.</p>" +
                        "    <p>For support, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot / HOn. All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";

        helper.setText(htmlBody,true);
        javaMailSender.send(message);
    }


    public void sendAfterGettingThreeReports(String email)throws Exception{
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper =  new MimeMessageHelper(message , true);


        helper.setFrom("muskanshroti01@gmail.com");
        helper.setSubject("Your chat is getting banned");
        helper.setTo(email);

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Chat Report Warning - HotOrNot</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #333333; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #ffe5e5; border-left: 6px solid #e63946; padding: 15px; " +
                        "                   color: #c53030; font-size: 16px; border-radius: 6px; margin-top: 20px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +
                        "    <h2>Warning: Your Chat Was Reported More Then 3 Times</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>We want to inform you that <strong>one of your messages</strong> has been reported by another user for inappropriate behavior.</p>" +
                        "    <p>Please make sure your conversations follow our community guidelines and remain respectful at all times.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>Important:</strong><br>" +
                        "        You have received a report on your chat.<br>" +
                        "        <strong>3 total reports</strong> will result in your chat feature being <strong>temporarily disabled</strong> or restricted." +
                        "    </div>" +

                        "    <p>If you believe this report was a mistake, you may ignore this message. Our team regularly reviews repeated reports.</p>" +
                        "    <p>For support, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot / HOn. All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";



        helper.setText(htmlBody, true);
        javaMailSender.send(message);

    }

    public void sendLastWarningMail(String email) throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setFrom("shrotimuskan@gmail.com");
        helper.setTo(email);
        helper.setSubject("Last Warning.");


        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Final Warning – Reported Chat</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #e63946; margin-bottom: 20px; }" +
                        "    h2 { color: #b91c1c; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #ffe5e5; border-left: 6px solid #e63946; padding: 18px;" +
                        "                   color: #7f1d1d; font-size: 16px; border-radius: 6px; margin-top: 25px; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #e63946; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🔥 HotOrNot</div>" +

                        "    <h2>Final Warning from Admin</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>This email is sent by the <strong>HotOrNot Admin Team</strong> regarding a <strong>reported chat message</strong> from your account.</p>" +
                        "    <p>One or more of your chat messages have been <strong>reported for violating our community guidelines</strong>.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>LAST WARNING</strong><br><br>" +
                        "        Any <strong>future report</strong> against your chat activity will result in:<br>" +
                        "        • <strong>Immediate account review</strong><br>" +
                        "        • <strong>Chat restrictions or permanent chat ban</strong><br>" +
                        "        • <strong>Temporary or permanent account suspension</strong><br>" +
                        "        <br>No further warnings will be issued by the admin team." +
                        "    </div>" +

                        "    <p>You are strictly required to follow our <strong>chat and community guidelines</strong>.</p>" +
                        "    <p>Failure to comply will lead to <strong>automatic administrative action</strong>.</p>" +

                        "    <p>If you believe this report was incorrect, you may contact us at " +
                        "    <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot (HON). All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";


        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }


}
