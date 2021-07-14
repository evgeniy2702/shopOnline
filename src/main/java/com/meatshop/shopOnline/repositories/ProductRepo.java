package com.meatshop.shopOnline.repositories;

import com.meatshop.shopOnline.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

public interface ProductRepo extends JpaRepository<Product, Long> {

    Product getProductById(Long id);

    Product getProductByName(String nameProduct);

    void deleteProductById(Long id);

}
