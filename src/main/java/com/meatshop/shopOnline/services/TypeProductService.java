package com.meatshop.shopOnline.services;

import com.meatshop.shopOnline.models.TypeProduct;
import com.meatshop.shopOnline.repositories.TypeProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Service
public class TypeProductService {

    private final TypeProductRepo typeProductRepo;

    public TypeProductService(TypeProductRepo typeProductRepo) {
        this.typeProductRepo = typeProductRepo;
    }

    public List<TypeProduct> getListOfTypeProduct(){
        return typeProductRepo.findAll();
    }
}
