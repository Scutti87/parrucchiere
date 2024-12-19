package it.web.parrucchiere.security;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {

	@Query(value = "SELECT * FROM user u WHERE u.username = :username AND u.password = :password", nativeQuery = true)
	Optional<User> findByUsernameByPassword(@Param("username") String username, @Param("password") String password);
	
	Optional<User> findByUsername(String username);
	
}
