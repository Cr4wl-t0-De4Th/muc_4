package vn.iotstar.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String homePage() {
        return "home"; // returns home.html
    }

    @GetMapping("/categories")
    public String categoryPage() {
        return "category_crud"; // returns category_crud.html
    }

    @GetMapping("/products")
    public String productPage() {
        return "product_crud"; // returns product_crud.html
    }
}
