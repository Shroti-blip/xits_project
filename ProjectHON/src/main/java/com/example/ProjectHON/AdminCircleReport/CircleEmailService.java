package com.example.ProjectHON.AdminCircleReport;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class CircleEmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public void sendCircleWarningMail(String email) throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setTo(email);
        helper.setFrom("contactjobsagar@gmail.com");
        helper.setSubject("Warning Mail");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Warning – Group Chat Reported</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #f59e0b; margin-bottom: 20px; }" +
                        "    h2 { color: #b45309; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #fff7ed; border-left: 6px solid #f59e0b; padding: 18px;" +
                        "                   color: #7c2d12; font-size: 16px; border-radius: 6px; margin-top: 25px; text-align:left; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #f59e0b; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>⚠️ HotOrNot</div>" +

                        "    <h2>Group Chat Reported</h2>" +

                        "    <p>Hi <strong>User</strong>,</p>" +
                        "    <p>This email is to inform you that a <strong>group chat you own or participate in</strong> has been reported by one or more users.</p>" +

                        "    <div class='warning-box'>" +
                        "        <strong>Reported Group:</strong> Group Name<br>" +
                        "        <strong>Reason:</strong> Report Reason<br>" +
                        "        <strong>Reported On:</strong> Date & Time<br><br>" +
                        "        Our moderation team is currently reviewing this report." +
                        "    </div>" +

                        "    <p>Please ensure that all conversations within the group comply with our <strong>community guidelines</strong>.</p>" +
                        "    <p>No immediate action has been taken at this stage. However, repeated reports or verified violations may result in restrictions on the group or involved accounts.</p>" +

                        "    <p>If you believe this report was submitted in error, no action is required while the review is ongoing.</p>" +

                        "    <p>For any questions, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot (HON). All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";


        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }



    public void sendWarningMailForGroupAdmin(String email) throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setTo(email);
        helper.setFrom("contactjobsagar@gmail.com");
        helper.setSubject("Warning Mail");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Warning – Your Group Has Been Reported</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #f59e0b; margin-bottom: 20px; }" +
                        "    h2 { color: #b45309; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .warning-box { background-color: #fff7ed; border-left: 6px solid #f59e0b; padding: 18px;" +
                        "                   color: #7c2d12; font-size: 16px; border-radius: 6px; margin-top: 25px; text-align:left; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #f59e0b; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>⚠️ HotOrNot</div>" +

                        "    <h2>Warning: Your Group Has Been Reported</h2>" +

                        "    <p>Hi <strong>Group Owner</strong>,</p>" +
                        "    <p>We are writing to inform you that a <strong>group you own</strong> has been reported by one or more members for violating our community guidelines.</p>" +

                        "    <div class='warning-box'>" +
                        "        <strong>Group Name:</strong> Group Name<br>" +
                        "        <strong>Report Reason:</strong> Report Reason<br>" +
                        "        <strong>Reported On:</strong> Date & Time<br><br>" +
                        "        As the <strong>group owner</strong>, you are responsible for ensuring a safe and respectful environment within your group." +
                        "    </div>" +

                        "    <p>Please review the recent activity in your group and take necessary action if required.</p>" +
                        "    <p><strong>Repeated reports</strong> or confirmed violations may lead to:</p>" +
                        "    <p>• Temporary restrictions on the group<br>" +
                        "       • Removal of group privileges<br>" +
                        "       • Permanent group suspension</p>" +

                        "    <p>No immediate action has been taken at this time. This message serves as an official warning.</p>" +

                        "    <p>If you believe this report was made in error, no action is required while the review is ongoing.</p>" +

                        "    <p>For support, contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot (HON). All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";


        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }


    public void sendAfterDeletingGroup(String email) throws Exception{

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message , true);

        helper.setTo(email);
        helper.setFrom("contactjobsagar@gmail.com");
        helper.setSubject("Warning Mail");

        String htmlBody =
                "<!DOCTYPE html>" +
                        "<html lang='en'>" +
                        "<head>" +
                        "  <meta charset='UTF-8'>" +
                        "  <title>Notice – Your Group Has Been Removed</title>" +
                        "  <style>" +
                        "    body { font-family: 'Segoe UI', Arial, sans-serif; background-color: #f5f6fa; margin: 0; padding: 40px 0; }" +
                        "    .container { max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 12px;" +
                        "                box-shadow: 0 4px 20px rgba(0,0,0,0.08); padding: 40px 30px; text-align: center; }" +
                        "    .logo { font-size: 32px; font-weight: bold; color: #ef4444; margin-bottom: 20px; }" +
                        "    h2 { color: #b91c1c; font-size: 22px; margin-bottom: 10px; }" +
                        "    p { color: #555555; font-size: 16px; line-height: 1.6; margin: 15px 0; }" +
                        "    .alert-box { background-color: #fef2f2; border-left: 6px solid #ef4444; padding: 18px;" +
                        "                 color: #7f1d1d; font-size: 16px; border-radius: 6px; margin-top: 25px; text-align:left; }" +
                        "    .footer { margin-top: 35px; font-size: 12px; color: #999999; border-top: 1px solid #eeeeee; padding-top: 20px; }" +
                        "    a { color: #ef4444; text-decoration: none; }" +
                        "  </style>" +
                        "</head>" +
                        "<body>" +
                        "  <div class='container'>" +
                        "    <div class='logo'>🚫 HotOrNot</div>" +

                        "    <h2>Your Group Has Been Removed</h2>" +

                        "    <p>Hi <strong>Group Owner</strong>,</p>" +
                        "    <p>We regret to inform you that your group has been <strong>permanently removed</strong> from HotOrNot following multiple reports and a moderation review.</p>" +

                        "    <div class='alert-box'>" +
                        "        <strong>Group Name:</strong> Group Name<br>" +
                        "        <strong>Primary Reason:</strong> Policy Violation<br>" +
                        "        <strong>Action Taken On:</strong> Date & Time<br><br>" +
                        "        After careful review, our moderation team determined that the group violated our community guidelines." +
                        "    </div>" +

                        "    <p>As a group owner, you are responsible for ensuring that all group activities comply with our platform rules.</p>" +
                        "    <p>This action is <strong>final</strong>, and the group will no longer be accessible to members.</p>" +

                        "    <p>Further violations associated with your account may result in additional restrictions.</p>" +

                        "    <p>If you believe this action was taken in error, you may contact our support team for clarification.</p>" +

                        "    <p>Contact us at <a href='mailto:contact.hon@gmail.com'>contact.hon@gmail.com</a></p>" +

                        "    <div class='footer'>© 2025 HotOrNot (HON). All rights reserved.</div>" +
                        "  </div>" +
                        "</body>" +
                        "</html>";



        helper.setText(htmlBody, true);
        javaMailSender.send(message);
    }














}
