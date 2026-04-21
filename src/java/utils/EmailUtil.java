package utils;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailUtil {

    private static final String FROM_EMAIL = "dinhtoan204nd@gmail.com";
    private static final String APP_PASSWORD = "vxyiqygwymbqsjwe";

    private EmailUtil() {
    }

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        return Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        });
    }

    public static void sendEmail(String toEmail, String subject, String content) throws Exception {
        Session session = createSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL, "Nha thuoc Online"));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
        message.setSubject(subject);
        message.setText(content);

        Transport.send(message);
    }

    public static void sendRegisterOtp(String toEmail, String otp) throws Exception {
        sendEmail(
                toEmail,
                "Ma xac minh dang ky tai khoan",
                "Ma OTP cua ban la: " + otp + ". Ma co hieu luc trong 5 phut."
        );
    }

    public static void sendChangePasswordOtp(String toEmail, String otp) throws Exception {
        sendEmail(
                toEmail,
                "Ma xac minh doi mat khau",
                "Ban vua yeu cau doi mat khau tai khoan.\n"
                + "Ma OTP cua ban la: " + otp + ". Ma co hieu luc trong 5 phut.\n"
                + "Neu khong phai ban thuc hien, vui long bo qua email nay."
        );
    }

    public static void sendChangeEmailOtp(String toEmail, String otp, String emailMoi) throws Exception {
        sendEmail(
                toEmail,
                "Ma xac minh doi gmail",
                "Ban vua yeu cau doi gmail tai khoan sang: " + emailMoi + "\n"
                + "Ma OTP cua ban la: " + otp + ". Ma co hieu luc trong 5 phut.\n"
                + "Neu khong phai ban thuc hien, vui long bo qua email nay."
        );
    }

    public static void sendEmailChangedNotice(String toEmail) throws Exception {
        sendEmail(
                toEmail,
                "Thong bao doi gmail thanh cong",
                "Gmail tai khoan cua ban vua duoc cap nhat thanh cong.\n"
                + "Neu day khong phai ban, vui long lien he ho tro ngay."
        );
    }

    public static void sendForgotPasswordOtp(String toEmail, String otp) throws Exception {
        sendEmail(
                toEmail,
                "Ma xac minh quen mat khau",
                "Ban vua yeu cau dat lai mat khau tai khoan.\n"
                + "Ma OTP cua ban la: " + otp + ". Ma co hieu luc trong 5 phut.\n"
                + "Neu khong phai ban thuc hien, vui long bo qua email nay."
        );
    }

    public static boolean guiOtpQuenMatKhau(String toEmail, String otp) {
        try {
            sendForgotPasswordOtp(toEmail, otp);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}