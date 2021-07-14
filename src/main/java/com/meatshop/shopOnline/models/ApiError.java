package com.meatshop.shopOnline.models;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 @author Zhurenko Evgeniy 8.06.21
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String message;
    private String debugMessage;
}
