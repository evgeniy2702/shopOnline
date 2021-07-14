package com.meatshop.shopOnline.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Getter
@Setter
@Entity
@Table(name = "type_product")
public class TypeProduct {

    @Id
    @Column(name = "id_type_product")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int code_type;
    private String type_name;

    @OneToMany(mappedBy = "type_product",
               cascade = CascadeType.ALL,
                fetch = FetchType.LAZY)
    private List<Product> products;
}
