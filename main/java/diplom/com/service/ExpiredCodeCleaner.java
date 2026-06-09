package diplom.com.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import diplom.com.domain.User;
import diplom.com.repos.UserRepos;


@Component
public class ExpiredCodeCleaner {

    private static final long CODE_LIFETIME_MINUTES = 60;

    @Autowired
    private UserRepos userRepos;

    @Scheduled(fixedRate = 600_000)
    @Transactional
    public void cleanExpiredCodes() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(CODE_LIFETIME_MINUTES);
        List<User> expired = userRepos.findByActivationCodeNotNullAndCodeCreatedAtBefore(threshold);

        if (!expired.isEmpty()) {
            for (User user : expired) {
                user.setActivationCode(null);
                user.setCodeCreatedAt(null);
            }
            userRepos.saveAll(expired);
            System.out.println("Очищено просроченных кодов активации: " + expired.size());
        }
    }
}


