package au.com.deepak.user.security.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.deepak.user.security.model.UserEntity;

public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String userName);
}
