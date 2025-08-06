package com.ptip.auth.email.service;

import com.ptip.auth.entity.User;
import com.ptip.auth.repository.UserRepository;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Properties;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class SchoolEmailVerificationService {

    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;  // 메일 발신 주소

    @Value("${spring.mail.password}")
    private String password;  // 앱 비밀번호 (Gmail/학교 SMTP)

    /**
     * 학교 이메일 인증번호 전송
     */
    public void sendSchoolEmail(String schoolEmail, User user) {
        if (!schoolEmail.endsWith("@ptu.ac.kr")) {
            throw new IllegalArgumentException("학교 이메일(@ptu.ac.kr)만 인증 가능합니다.");
        }

        String code = generateCode();
        user.setSchoolEmail(schoolEmail);
        user.setSchoolEmailVerified(false);
        user.setSchoolEmailVerificationCode(code);
        userRepository.save(user);

        sendEmail(schoolEmail, code);
    }

    /**
     * 학교 이메일 인증번호 검증
     */
    public boolean verifySchoolEmail(String email, String code, User user) {
        if (email.equals(user.getSchoolEmail()) && code.equals(user.getSchoolEmailVerificationCode())) {
            user.setSchoolEmailVerified(true);
            user.setSchoolEmailVerificationCode(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    /**
     * 학교 이메일 인증 여부 조회
     */
    public boolean isSchoolEmailVerified(User user) {
        return user.isSchoolEmailVerified();
    }

    private String generateCode() {
        return String.valueOf(new Random().nextInt(900000) + 100000); // 6자리 숫자
    }

    private void sendEmail(String to, String code) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com"); // 학교 SMTP 사용 시 변경 가능
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("[PTIP 인증] 학교 이메일 인증번호");
            message.setText("아래 인증번호를 입력해주세요\n\n" + code);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 전송 실패: " + e.getMessage());
        }
    }
}
