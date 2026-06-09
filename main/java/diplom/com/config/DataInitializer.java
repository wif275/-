package diplom.com.config;

import diplom.com.domain.Role;
import diplom.com.repos.RoleRepos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepos roleRepos;

    @Override
    public void run(String... args) {
        if (roleRepos.findByName("USER") == null) {
            roleRepos.save(new Role("USER"));
        }
        if (roleRepos.findByName("ADMIN") == null) {
            roleRepos.save(new Role("ADMIN"));
        }
    }
}
