package au.com.deepak.user.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.deepak.user.security.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
