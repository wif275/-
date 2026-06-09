package diplom.com.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import diplom.com.domain.Role;
import diplom.com.domain.User;
import diplom.com.repos.RoleRepos;
import diplom.com.repos.UserRepos;
import io.micrometer.common.util.StringUtils;


@Service
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepos userRepos;

    @Autowired
    private RoleRepos roleRepos;

    @Autowired
    private MailSender mailSender;

    @Autowired
	private PasswordEncoder passwordEncoder;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepos.findByUsername(username);
    }

    public boolean addUser(User user) {
        User userFromDb = userRepos.findByUsername(user.getUsername());

        if(userFromDb != null)  {
            return false;
        }
 
        user.setActive(false);
        user.setRole(roleRepos.findByName("USER"));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setCodeCreatedAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepos.save(user);

        sendMessage(user);

        return true;
    }
        
        private void sendMessage(User user) {
            if (!StringUtils.isEmpty(user.getEmail())) {
            String message = String.format(
                "Hello, %s! \n" +
                "Welcomo to us. Please, visit next link:http://localhost:8083/activate/%s",
                user.getUsername(),
                user.getActivationCode()
            );

            mailSender.send(user.getEmail(), "Activation Code", message);
        }

    }

    public boolean activateUser(String code){
        User user = userRepos.findByActivationCode(code);

        if (user == null) {
            return false;
        }

        user.setActivationCode(null);
        user.setCodeCreatedAt(null);

        userRepos.save(user);

        return true;
    }

    public List<User> findAll() {
        return userRepos.findAll();
    }

    public void saveUser(Long userId, String username, List<String> roles) {
        User user = userRepos.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(username);

        if (roles != null && !roles.isEmpty()) {
            Role role = roleRepos.findByName(roles.get(0));
            if (role != null) {
                user.setRole(role);
            }
        }

        userRepos.save(user);
    }

    public void updateProfile(User user, String password, String email){
        String userEmail = user.getEmail();

        boolean isEmailChanged = (email != null && !email.equals(userEmail)) || 
        (userEmail !=null && !userEmail.equals(userEmail));

        if (isEmailChanged) {
            user.setEmail(email);

            if (!StringUtils.isEmpty(email)) {
                user.setActivationCode(UUID.randomUUID().toString());
                user.setCodeCreatedAt(LocalDateTime.now());
            }
        }

        if (!StringUtils.isEmpty(password)) {
           user.setPassword(passwordEncoder.encode(password));
        }

        userRepos.save(user);

        if (isEmailChanged) {
        sendMessage(user);
        }
    }
}
