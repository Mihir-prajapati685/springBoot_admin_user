package com.mihir.ecoback.demo.Services;

import com.mihir.ecoback.demo.Models.ProductModel;
import com.mihir.ecoback.demo.Repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminProductServices {
    @Autowired
    private ProductRepo repo;

    public ProductModel postindata(ProductModel productModel) {
        ProductModel productModel1=repo.save(productModel);
        return productModel1;
    }

    public List<ProductModel> getallproduct() {
        List<ProductModel> productlist=repo.findAll();
        return productlist;
    }

    public ProductModel getoneproduct(Long id) {
        ProductModel productModel=repo.findById(id).orElse(null);
        return productModel;

    }

    public ProductModel updateproduct(Long id,ProductModel productModel) {
        Optional<ProductModel> existingProduct = repo.findById(id);
        if(existingProduct.isPresent()){
            productModel.setId(id);
           return repo.save(productModel);
        }else{
            return null;
        }

    }

    public void deleteid(Long id) {
        Optional<ProductModel> existingProduct = repo.findById(id);
        if(existingProduct.isPresent()){
            repo.deleteById(id);
        }
    }
}
