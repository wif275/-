package diplom.com.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import diplom.com.domain.News;

public interface NewsRepos extends JpaRepository<News, Long> {

    Page<News> findAll(Pageable pageable);

    List<News> findTop4ByOrderByIdDesc();
}
