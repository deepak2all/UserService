package au.com.deepak.user.security.config;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import au.com.deepak.user.security.model.Role;
import au.com.deepak.user.security.model.UserEntity;
import au.com.deepak.user.security.repo.RoleRepository;
import au.com.deepak.user.security.repo.UserEntityRepository;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private boolean alreadySetup = false;

	@Autowired
	private UserEntityRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Override
	@Transactional
	public void onApplicationEvent(final ContextRefreshedEvent event) {
		if (alreadySetup) {
			return;
		}

		// Create user roles
		var userRole = createRoleIfNotFound(Role.ROLE_USER);
		var adminRole = createRoleIfNotFound(Role.ROLE_ADMIN);

		// Create users
		createUserIfNotFound("genUser", userRole);
		createUserIfNotFound("admin", adminRole);

		alreadySetup = true;
	}

	@Transactional
	private final Role createRoleIfNotFound(final String name) {
		Role role = roleRepository.findByName(name);
		if (role == null) {
			role = new Role(name);
			role = roleRepository.save(role);
		}
		return role;
	}

	@Transactional
	private final UserEntity createUserIfNotFound(final String name, final Role role) {
		UserEntity user = userRepository.findByUsername(name);
		if (user == null) {
			// tmpPwd = "password"
			user = new UserEntity(name, "$2b$10$uQHtH/NrF4XOU5FDE.9vRuYtp/AIR5lgdEzP0kDxpRjN4m75VZL1G");
			user.setRoles(Set.of(role));
			user = userRepository.save(user);
			System.out.println("DEFAULT User " + name + " is created");
		}
		return user;
	}
}