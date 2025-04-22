package com.mihir.ecoback.demo.Repo;

import com.mihir.ecoback.demo.Models.ProductModel;
import com.mihir.ecoback.demo.Models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<ProductModel,Long> {

}
