package vn.iotstar.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Example;
import vn.iotstar.Entity.ProductEntity;
import java.util.List;
import java.util.Optional;

public interface IProductService {
    <S extends ProductEntity> S save(S entity);
    List<ProductEntity> findAll();
    Page<ProductEntity> findAll(Pageable pageable);
    List<ProductEntity> findAll(Sort sort);
    List<ProductEntity> findAllById(Iterable<Long> ids);
    Optional<ProductEntity> findById(Long id);
    <S extends ProductEntity> Optional<S> findOne(Example<S> example);
    long count();
    void deleteById(Long id);
    void delete(ProductEntity entity);
    void deleteAll();
    List<ProductEntity> findByProductNameContaining(String name);
    Page<ProductEntity> findByProductNameContaining(String name, Pageable pageable);
    List<ProductEntity> findByCategory_CategoryId(Long categoryId);
    List<ProductEntity> findAllByOrderByUnitPriceAsc();
}
