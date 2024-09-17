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

    @Value("${app.security.bcrypt-log-rounds:10}")
    private Integer logRounds;

    /**
     * 经过 BCrypt 加密的密码所匹配的正则表达式，
     * 复制于{@link org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder#BCRYPT_PATTERN BCryptPasswordEncoder 的 BCRYPT_PATTERN}
     */
    private final Pattern BCRYPT_PATTERN = Pattern.compile("\\A\\$2([ayb])?\\$(\\d\\d)\\$[./0-9A-Za-z]{53}");

    @Override
    public String encode(final CharSequence rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(this.checkLogRounds(logRounds)));
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
            return false;
        }
        return BCrypt.checkpw(rawPassword.toString(), encodedPassword);
    }

    private int checkLogRounds(Integer logRounds) {
        return logRounds == null || logRounds < 4 || logRounds > 31 ? 10 : logRounds;
    }
}
