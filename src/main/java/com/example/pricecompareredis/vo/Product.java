package com.example.pricecompareredis.vo;

import lombok.Data;

@Data
public class Product {

    private String prodGrpId; // zset_products

    private String productId; // ex) p0001

    private int price; // 25000 (won)

}
