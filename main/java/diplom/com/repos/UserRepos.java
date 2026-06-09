package diplom.com.repos;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import diplom.com.domain.User;


public interface UserRepos extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    List<User> findByActivationCodeNotNullAndCodeCreatedAtBefore(LocalDateTime threshold);

    Page<User> findByActiveTrue(Pageable pageable);

    Page<User> findByActiveTrueAndUsernameContainingIgnoreCase(String username, Pageable pageable);

}
