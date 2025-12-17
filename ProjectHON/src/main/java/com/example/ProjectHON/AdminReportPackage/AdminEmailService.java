package com.example.ProjectHON.AdminReportPackage;


import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class AdminEmailService {

    @Autowired
    JavaMailSender javaMailSender;

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
                        "  <title>Final Warning – Account Under Review</title>" +
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

                        "    <h2>Final Warning: Account at Risk</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>This email is being sent by the <strong>HotOrNot Admin Team</strong> as a final warning regarding your account activity.</p>" +
                        "    <p>Your account has received <strong>multiple reports</strong> related to inappropriate behavior on the platform.</p>" +

                        "    <div class='warning-box'>" +
                        "        ⚠️ <strong>Final Notice</strong><br><br>" +
                        "        Any <strong>further report</strong> against your account will result in:<br>" +
                        "        • Account being <strong>reported for review</strong><br>" +
                        "        • <strong>Temporary or permanent restriction</strong> of chat features<br>" +
                        "        • Possible <strong>account suspension</strong><br>" +
                        "        <br>This action will be taken <strong>without additional warnings</strong>." +
                        "    </div>" +

                        "    <p>You are expected to strictly follow our <strong>community guidelines</strong> and maintain respectful communication at all times.</p>" +
                        "    <p>If you believe previous reports were incorrect, no action is required from your side. However, continued violations will not be tolerated.</p>" +

                        "    <p>For support, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot (HON). All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";


        helper.setText(htmlBody , true);
        javaMailSender.send(message);

    }

}

