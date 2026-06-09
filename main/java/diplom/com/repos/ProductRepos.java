package diplom.com.repos;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import diplom.com.domain.Product;


public interface ProductRepos extends CrudRepository<Product, Long>{
    Page<Product> findAll(Pageable pageable);
    Page<Product> findByTagContainingIgnoreCase(String tag, Pageable pageable);

}
