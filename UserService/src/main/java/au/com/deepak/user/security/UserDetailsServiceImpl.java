package au.com.deepak.user.security;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.com.deepak.user.controller.UserController;
import au.com.deepak.user.security.model.Role;
import au.com.deepak.user.security.model.UserEntity;
import au.com.deepak.user.security.repo.UserEntityRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
 
	private static final Logger logger = LogManager.getLogger(UserDetailsServiceImpl.class);
	
    @Autowired
    private UserEntityRepository repo;
 
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = repo.findByUsername(username);
        if (user != null) {
        	logger.debug("User loaded from DB :: " + user.getUsername());
        	logger.debug("Password loaded from DB :: " + user.getPassword());
            return new User(user.getUsername(), user.getPassword(), buildSimpleGrantedAuthorities(user.getRoles()));
        } else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
 
    private static List<SimpleGrantedAuthority> buildSimpleGrantedAuthorities(final Set<Role> roles) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }
}
