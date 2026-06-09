package diplom.com.repos;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import diplom.com.domain.Request;

public interface RequestRepos extends CrudRepository<Request, Long>{
    Page<Request> findAll(Pageable pageable);
    Page<Request> findByFinishedFalse(Pageable pageable);

}