package com.mihir.ecoback.demo.Controller;
import com.mihir.ecoback.demo.Models.ProductModel;
import com.mihir.ecoback.demo.Models.UserModel;
import com.mihir.ecoback.demo.Repo.ProductRepo;
import com.mihir.ecoback.demo.Services.ProductService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/dashboard")
    public String userDashboard() {
        return "Welcome User";
    }
    @GetMapping("/get")
    public ResponseEntity<List<ProductModel>> getallproduct(){
         List<ProductModel> listofproduct=productService.getallproduct();
         return new ResponseEntity<>(listofproduct, HttpStatus.OK);
    }
    @GetMapping("/getidproduct/{id}")
    public  ResponseEntity<ProductModel> getidproduct(@PathVariable  Long id){
        ProductModel productModel=productService.getidproduct(id);
        return ResponseEntity.ok(productModel);
    }
}
