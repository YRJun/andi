package com.summer.auth.config.security;

import com.summer.auth.dao.AndiDAO;
import com.summer.common.model.andi.AndiUser;
import jakarta.annotation.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author Renjun Yu
 * @date 2024/06/12 10:56
 */
@Component
public class CustomUserDetailsManager implements UserDetailsManager, UserDetailsPasswordService {

    @Resource
    private AndiDAO andiDAO;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final AndiUser user = andiDAO.queryUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("用户[" + username + "]不存在");
        }
        Collection<? extends GrantedAuthority> authorities = new ArrayList<>();
        return new User(user.getUsername(),
                user.getPassword(),
                user.getIsActive() == 1,
                true,
                true,
                true,
                authorities);
    }

    @Override
    public UserDetails updatePassword(final UserDetails user, final String newPassword) {
        return null;
    }

    @Override
    public void createUser(final UserDetails user) {

    }

    @Override
    public void updateUser(final UserDetails user) {

    }

    @Override
    public void deleteUser(final String username) {

    }

    @Override
    public void changePassword(final String oldPassword, final String newPassword) {

    }

    @Override
    public boolean userExists(final String username) {
        return false;
    }
}
