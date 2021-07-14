package com.meatshop.shopOnline.services;

import com.meatshop.shopOnline.models.Product;
import com.meatshop.shopOnline.models.TypeProduct;
import com.meatshop.shopOnline.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Service
public class ProductService {

    private ProductRepo productRepo;
    private TypeProductService typeProductService;

    @Autowired
    public void setProductRepo(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Autowired
    public void setTypeProductService(TypeProductService typeProductService) {
        this.typeProductService = typeProductService;
    }

    public boolean savePruduct(Product product){
        Product productDB = productRepo.getProductByName(product.getName());
        if(productDB != null){
            productRepo.save(product);
            return true;
        } else {
            return false;
        }
    }

    public List<Product> findDiscountProducts(){
        List<Product> prodDiscountList = productRepo.findAll();
        if(!prodDiscountList.isEmpty()){
            List<Product> products = prodDiscountList.stream().filter(product -> product.getDiscount() != 0L).collect(Collectors.toList());
            return products.stream()
                                   .sorted((product1, product2) -> product1.getType_product().getType_name()
                                           .compareToIgnoreCase(product2.getType_product().getType_name()))
                                   .collect(Collectors.toList());
        } else {
            return prodDiscountList;
        }
    }

    public List<Product> findProdBySearch(String search) {
        List<Product> products = productRepo.findAll().stream().filter(product -> findProdByProps(product,search)).collect(Collectors.toList());
        return products.stream().sorted((prod1, prod2)-> prod1.getType_product().getType_name()
                                .compareToIgnoreCase(prod2.getType_product().getType_name()))
                                .collect(Collectors.toList());
    }

    public boolean findProdByProps(Product product, String search){
        Field[] fieldsOfProduct = product.getClass().getDeclaredFields();
        boolean bool = false;
        for (Field fieldOfProd : fieldsOfProduct ) {
            fieldOfProd.setAccessible(true);
            try {
                if (fieldOfProd.get(product) == null ||
                        fieldOfProd.get(product).getClass() == Long.class) {
                    continue;
                }
                if (fieldOfProd.getName().equalsIgnoreCase(search)) {
                    continue;
                }
                if (fieldOfProd.get(product).getClass() == Integer.class) {
                    Integer intField = (Integer) fieldOfProd.get(product);
                    if (intField.toString().toLowerCase().contains(search.toLowerCase())) {
                        bool = true;
                        break;
                    }
                }
                if (fieldOfProd.get(product).getClass() == Float.class) {
                    Float floatField = (Float) fieldOfProd.get(product);
                    if (floatField.toString().toLowerCase().contains(search.toLowerCase())) {
                        bool = true;
                        break;
                    }
                }
                if (fieldOfProd.get(product).getClass() == String.class) {
                    String stringField = (String) fieldOfProd.get(product);
                    if (stringField.toLowerCase().contains(search.toLowerCase())) {
                        bool = true;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return bool;
    }

    public List<TypeProduct> getTypeProdByListOfProd(List<Product> products){

        List<Product> prodList = products.stream().sorted((prod1, prod2)-> prod1.getType_product().getType_name()
                .compareToIgnoreCase(prod2.getType_product().getType_name()))
                .collect(Collectors.toList());
        List<TypeProduct> typeProdList = prodList.stream().map(Product::getType_product)
                                                          .collect(Collectors.toList());
        List<TypeProduct> typeList = new ArrayList<>();
        for ( int i=0; i< prodList.size(); i++ ) {
            if(!typeProdList.get(i).equals(typeProdList.get(i+1)))
                typeList.add(typeProdList.get(i));
        }
        return typeList;
    }
}
