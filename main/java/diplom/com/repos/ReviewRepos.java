package diplom.com.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import diplom.com.domain.Review;
import diplom.com.domain.User;

public interface ReviewRepos extends JpaRepository<Review, Long> {

    List<Review> findAllByOrderByCreatedAtDesc();

    boolean existsByAuthor(User author);

    @Query(value = "SELECT * FROM review WHERE rating >= 4 ORDER BY rating DESC, RANDOM() LIMIT 5",
           nativeQuery = true)
    List<Review> findTopRandomReviews();
}
