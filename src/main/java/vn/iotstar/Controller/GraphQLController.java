package vn.iotstar.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Entity.ProductEntity;
import vn.iotstar.Service.ICategoryService;
import vn.iotstar.Service.IProductService;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class GraphQLController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    // ----- Queries -----

    @QueryMapping
    public List<ProductEntity> getAllProductsSortedByPrice() {
        return productService.findAllByOrderByUnitPriceAsc();
    }

    @QueryMapping
    public List<ProductEntity> getProductsByCategory(@Argument Long categoryId) {
        return productService.findByCategory_CategoryId(categoryId);
    }

    @QueryMapping
    public List<CategoryEntity> getAllCategories() {
        return categoryService.findAll();
    }

    @QueryMapping
    public CategoryEntity getCategoryById(@Argument Long categoryId) {
        return categoryService.findById(categoryId).orElse(null);
    }

    @QueryMapping
    public List<CategoryEntity> searchCategories(@Argument String name, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryService.findByNameContaining(name, pageable).getContent();
    }

    @QueryMapping
    public ProductEntity getProductById(@Argument Long productId) {
        return productService.findById(productId).orElse(null);
    }

    @QueryMapping
    public List<ProductEntity> searchProducts(@Argument String name, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productService.findByProductNameContaining(name, pageable).getContent();
    }

    // ----- Mutations -----

    @MutationMapping
    public CategoryEntity createCategory(@Argument String name) {
        CategoryEntity category = new CategoryEntity();
        category.setName(name);
        return categoryService.save(category);
    }

    @MutationMapping
    public CategoryEntity updateCategory(@Argument Long categoryId, @Argument String name) {
        Optional<CategoryEntity> opt = categoryService.findById(categoryId);
        if (opt.isPresent()) {
            CategoryEntity category = opt.get();
            category.setName(name);
            return categoryService.save(category);
        }
        return null;
    }

    @MutationMapping
    public Boolean deleteCategory(@Argument Long categoryId) {
        try {
            categoryService.deleteById(categoryId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @MutationMapping
    public ProductEntity createProduct(@Argument String productName, @Argument Double unitPrice, 
                                       @Argument Integer quantity, @Argument Double discount, 
                                       @Argument String description, @Argument Integer status, 
                                       @Argument Long categoryId) {
        ProductEntity product = new ProductEntity();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setStatus(status.shortValue());
        product.setCreateDate(new Date());

        Optional<CategoryEntity> catOpt = categoryService.findById(categoryId);
        catOpt.ifPresent(product::setCategory);

        return productService.save(product);
    }

    @MutationMapping
    public ProductEntity updateProduct(@Argument Long productId, @Argument String productName, 
                                       @Argument Double unitPrice, @Argument Integer quantity, 
                                       @Argument Double discount, @Argument String description, 
                                       @Argument Integer status, @Argument Long categoryId) {
        Optional<ProductEntity> opt = productService.findById(productId);
        if (opt.isPresent()) {
            ProductEntity product = opt.get();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setQuantity(quantity);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setStatus(status.shortValue());

            Optional<CategoryEntity> catOpt = categoryService.findById(categoryId);
            catOpt.ifPresent(product::setCategory);

            return productService.save(product);
        }
        return null;
    }

    @MutationMapping
    public Boolean deleteProduct(@Argument Long productId) {
        try {
            productService.deleteById(productId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
