package com.meatshop.shopOnline.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.awt.*;
import java.sql.Blob;
import java.util.*;


/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Entity
@Getter
@Setter
@Table(name = "product")
public class Product {

    @Id
    @Column(name = "id_product")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Код не должен быть пустым")
    @Max(value = 6, message = "Внутренний код должен быть не меньше 6 цифр")
    private int inside_code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_type_product", nullable = false)
    private TypeProduct type_product;

    @Min(value = 18, message = "Штрих-код должен состоять из 16 цифр")
    @Max(value = 18, message = "Штрих-код должен состоять из 16 цифр")
    private int bar_code;

    @NotEmpty(message = "Имя не должно быть пустым")
    @Size(min = 2, max = 30, message = "Размер не должен превышать 30 символов и быть меньше 2-х символов")
    private String name;

    @NotNull(message = "Поле цены не должно быть нулевым")
    @NotEmpty(message = "Поле цены не должно быть пустым")
    private float price;

    @Max(value = 100, message = "Скидка не может превышать значения в 100%")
    private int discount;

    @Size(max = 100, message = "Резмер описания продукта не должен превышать 100 символов")
    private String description;
    private long rating;
    private Blob picture_path;
}
