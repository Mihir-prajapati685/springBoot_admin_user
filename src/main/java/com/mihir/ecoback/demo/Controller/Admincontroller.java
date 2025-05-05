package com.mihir.ecoback.demo.Controller;

import com.mihir.ecoback.demo.Models.ProductModel;
import com.mihir.ecoback.demo.Services.AdminProductServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/admin")
public class Admincontroller {
    @Autowired
    AdminProductServices service;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public  String admindashboard(){
        return "Welcome mihir";
    }

    @PostMapping("/postdata")
    public ResponseEntity<ProductModel> postingdata(@RequestBody  ProductModel productModel){
        ProductModel productModel1=service.postindata(productModel);
        return new ResponseEntity<>(productModel1, HttpStatus.CREATED);
    }

    @GetMapping("/getallproduct")
    public ResponseEntity<List<ProductModel>> getallproduct(){
        List<ProductModel> listofproduct=service.getallproduct();
        return new ResponseEntity<>(listofproduct, HttpStatus.OK);
    }

    @GetMapping("/getoneproduct/{id}")
    public ResponseEntity<ProductModel> getoneproduct(Long id){
        ProductModel productModel=service.getoneproduct(id);
        return new ResponseEntity<>(productModel,HttpStatus.OK);
    }

    @GetMapping("/update/{id}")
    public ResponseEntity<ProductModel> updateproduct(@PathVariable  Long id,@RequestBody ProductModel productModel){
        service.updateproduct(id,productModel);
        return new ResponseEntity<>(productModel,HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<String> deleteid(@PathVariable  Long id){
        service.deleteid(id);
        return new ResponseEntity<>("delete successfully",HttpStatus.OK);
    }

}
