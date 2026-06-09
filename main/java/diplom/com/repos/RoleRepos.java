package diplom.com.repos;

import diplom.com.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepos extends JpaRepository<Role, Long> {

    Role findByName(String name);
}
