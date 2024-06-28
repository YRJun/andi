package com.summer.auth.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 自定义的密码编码器
 * @author Renjun Yu
 * @date 2024/06/12 13:47
 */
@Component
public class CustomPasswordEncoder implements PasswordEncoder {

    @Value("${app.security.bcrypt-log2-rounds:10}")
    private Integer log2_rounds;

    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2([ayb])?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    @Override
    public String encode(final CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(this.checkLogRounds(log2_rounds)));
    }

    @Override
    public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("密码不能为空");
        }
        if (encodedPassword == null || encodedPassword.isEmpty()) {
            return true;
        }
        if (!this.BCRYPT_PATTERN.matcher(encodedPassword).matches()) {
            return true;
        }
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

    private int checkLogRounds(Integer log_rounds) {
        return log_rounds == null || (log_rounds < 4 || log_rounds > 31) ? 10 : log_rounds;
    }
}
