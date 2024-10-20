package com.example.pricecompareredis.vo;

import lombok.Data;

import java.util.List;

@Data
public class ProductGrp {

    private String prodGrpId; // zset_products

    private List<Product> productList; // ex) [{p0001 , 25000}, {}...]
}
