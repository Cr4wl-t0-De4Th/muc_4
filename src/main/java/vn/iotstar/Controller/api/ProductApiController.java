package vn.iotstar.Controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.Entity.ProductEntity;
import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Model.Response;
import vn.iotstar.Service.IProductService;
import vn.iotstar.Service.ICategoryService;

import java.util.Date;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
public class ProductApiController {

    @Autowired
    private IProductService productService;

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllProduct() {
        return new ResponseEntity<>(new Response(true, "Success", productService.findAll()), HttpStatus.OK);
    }

    @PostMapping("/getProduct")
    public ResponseEntity<?> getProduct(@RequestParam("id") Long id) {
        Optional<ProductEntity> product = productService.findById(id);
        if (product.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Success", product.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Not Found", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> addProduct(@RequestParam("productName") String productName,
                                        @RequestParam("unitPrice") Double unitPrice,
                                        @RequestParam("quantity") Integer quantity,
                                        @RequestParam("discount") Double discount,
                                        @RequestParam("description") String description,
                                        @RequestParam("status") Short status,
                                        @RequestParam("categoryId") Long categoryId) {
        
        ProductEntity product = new ProductEntity();
        product.setProductName(productName);
        product.setUnitPrice(unitPrice);
        product.setQuantity(quantity);
        product.setDiscount(discount);
        product.setDescription(description);
        product.setStatus(status);
        product.setCreateDate(new Date());

        Optional<CategoryEntity> category = categoryService.findById(categoryId);
        category.ifPresent(product::setCategory);

        productService.save(product);
        return new ResponseEntity<>(new Response(true, "Added successfully", product), HttpStatus.OK);
    }

    @PutMapping("/updateProduct")
    public ResponseEntity<?> updateProduct(@RequestParam("productId") Long productId,
                                           @RequestParam("productName") String productName,
                                           @RequestParam("unitPrice") Double unitPrice,
                                           @RequestParam("quantity") Integer quantity,
                                           @RequestParam("discount") Double discount,
                                           @RequestParam("description") String description,
                                           @RequestParam("status") Short status,
                                           @RequestParam("categoryId") Long categoryId) {
        Optional<ProductEntity> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Product not found", null), HttpStatus.BAD_REQUEST);
        } else {
            ProductEntity product = optProduct.get();
            product.setProductName(productName);
            product.setUnitPrice(unitPrice);
            product.setQuantity(quantity);
            product.setDiscount(discount);
            product.setDescription(description);
            product.setStatus(status);

            Optional<CategoryEntity> category = categoryService.findById(categoryId);
            category.ifPresent(product::setCategory);

            productService.save(product);
            return new ResponseEntity<>(new Response(true, "Updated successfully", product), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteProduct")
    public ResponseEntity<?> deleteProduct(@RequestParam("productId") Long productId) {
        Optional<ProductEntity> optProduct = productService.findById(productId);
        if (optProduct.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Product not found", null), HttpStatus.BAD_REQUEST);
        } else {
            productService.delete(optProduct.get());
            return new ResponseEntity<>(new Response(true, "Deleted successfully", optProduct.get()), HttpStatus.OK);
        }
    }
}
