package com.mihir.ecoback.demo.Services;

import com.mihir.ecoback.demo.Models.ProductModel;
import com.mihir.ecoback.demo.Models.UserModel;
import com.mihir.ecoback.demo.Repo.ProductRepo;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    ProductRepo repo;

    public List<ProductModel> getallproduct() {
        List<ProductModel> listofproduct=repo.findAll();
        return listofproduct;
    }

    public ProductModel getidproduct(Long id) {
        ProductModel product=repo.findById(id).orElse(null);
        return product;
    }

}
