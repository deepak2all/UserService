package au.com.deepak.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import au.com.deepak.user.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
