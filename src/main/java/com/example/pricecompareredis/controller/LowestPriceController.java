package com.example.pricecompareredis.controller;

import com.example.pricecompareredis.service.LowestPriceService;
import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/")
public class LowestPriceController {

    @Autowired
    private LowestPriceService lowestPriceService;

    // 상품 조회
    @GetMapping("/product")
    public Set GetZsetValue(String key) {
        return lowestPriceService.getZsetValue(key);
    }

    // 상품 추가
    @PutMapping("/product")
    public int SetNewProduct(@RequestBody Product newProduct) {
        return lowestPriceService.setNewProduct(newProduct);
    }

    // 상품 그룹 추가
    @PutMapping("/productGroup")
    public int SetNewProduct(@RequestBody ProductGrp newProductGrp) {
        return lowestPriceService.setNewProductGrp(newProductGrp);
    }

    // 특정 키워드에 대한 상품 그룹의 순위를 반환
    // 특정 키워드와 연관된 상품 그룹을 Redis에 저장하고, 해당 상품 그룹의 순위를 계산하여 반환
    @PutMapping("/productGroupToKeyword")
    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        return lowestPriceService.setNewProductGrpToKeyword(keyword, prodGrpId, score);
    }

    // 특정 키워드를 통해 해당 키워드와 연관된 최저가 상품 정보를 조회하고 반환
    @GetMapping("/productPrice/lowest")
    public Keyword getLowestPriceProductByKeyword(String keyword) {
        return lowestPriceService.getLowestPriceProductByKeyword(keyword);
    }
}
