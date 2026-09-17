package vn.iotstar.Controller.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import vn.iotstar.Entity.CategoryEntity;
import vn.iotstar.Model.Response;
import vn.iotstar.Service.ICategoryService;

import java.util.Optional;

@RestController
@RequestMapping("/api/category")
public class CategoryApiController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping
    public ResponseEntity<?> getAllCategory() {
        return new ResponseEntity<>(new Response(true, "Success", categoryService.findAll()), HttpStatus.OK);
    }

    @PostMapping("/getCategory")
    public ResponseEntity<?> getCategory(@RequestParam("id") Long id) {
        Optional<CategoryEntity> category = categoryService.findById(id);
        if (category.isPresent()) {
            return new ResponseEntity<>(new Response(true, "Success", category.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new Response(false, "Not Found", null), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestParam("categoryName") String categoryName) {
        CategoryEntity category = new CategoryEntity();
        category.setName(categoryName);
        categoryService.save(category);
        return new ResponseEntity<>(new Response(true, "Added successfully", category), HttpStatus.OK);
    }

    @PutMapping("/updateCategory")
    public ResponseEntity<?> updateCategory(@RequestParam("categoryId") Long categoryId,
                                            @RequestParam("categoryName") String categoryName) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category not found", null), HttpStatus.BAD_REQUEST);
        } else {
            optCategory.get().setName(categoryName);
            categoryService.save(optCategory.get());
            return new ResponseEntity<>(new Response(true, "Updated successfully", optCategory.get()), HttpStatus.OK);
        }
    }

    @DeleteMapping("/deleteCategory")
    public ResponseEntity<?> deleteCategory(@RequestParam("categoryId") Long categoryId) {
        Optional<CategoryEntity> optCategory = categoryService.findById(categoryId);
        if (optCategory.isEmpty()) {
            return new ResponseEntity<>(new Response(false, "Category not found", null), HttpStatus.BAD_REQUEST);
        } else {
            categoryService.delete(optCategory.get());
            return new ResponseEntity<>(new Response(true, "Deleted successfully", optCategory.get()), HttpStatus.OK);
        }
    }
}
