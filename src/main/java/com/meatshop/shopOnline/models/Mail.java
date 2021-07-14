package com.meatshop.shopOnline.models;

import lombok.Getter;
import lombok.Setter;


import javax.persistence.*;
import java.time.LocalDateTime;

/**
 @author Zhurenko Evgeniy  08.06.21
 */

@Getter
@Setter
@Entity
@Table(name = "mail")
public class Mail {

    @Id
    @Column(name = "id_mail")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;

    private String message;

    private String email;

    private String telephone;

    private String name;


}
