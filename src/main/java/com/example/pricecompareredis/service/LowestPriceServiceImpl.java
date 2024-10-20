package com.example.pricecompareredis.service;

import com.example.pricecompareredis.vo.Keyword;
import com.example.pricecompareredis.vo.Product;
import com.example.pricecompareredis.vo.ProductGrp;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LowestPriceServiceImpl implements LowestPriceService {

    @Autowired
    private RedisTemplate myProdPriceRedis;

    public Set getZsetValue(String key) {
        Set myTempSet = new HashSet();

        myTempSet = myProdPriceRedis.opsForZSet().rangeWithScores(key, 0, 9);

        return myTempSet;
    }

    public int setNewProduct(Product newProduct) {
        int rank;
        myProdPriceRedis.opsForZSet().add(newProduct.getProdGrpId(), newProduct.getProductId(), newProduct.getPrice());
        // RedisTemplate myProdPriceRedis.opsForZSet() 에 있는 rank메서드 자체가 반환형이 Long이기 때문에 intValue()로 형변환을 해준다.
        rank = myProdPriceRedis.opsForZSet().rank(newProduct.getProdGrpId(), newProduct.getProductId()).intValue();
        return rank;
    }

    public int setNewProductGrp(ProductGrp newProductGrp) {
        // 상품 리스트를 가져온다.
        List<Product> productList = newProductGrp.getProductList();
        // 상품 리스트에서 첫번째 상품의 productId와 price를 가져온다.
        String productId = productList.get(0).getProductId();
        int price = productList.get(0).getPrice();

        // Redis의 ZSet에 새로운 상품 그룹 ID를 키로 사용하여, 상품 ID와 가격을 추가한다.
        myProdPriceRedis.opsForZSet().add(newProductGrp.getProdGrpId(), productId, price); // zadd
        // 추가된 상품 그룹 ID에 해당하는 Redis ZSet의 요소 개수를 가져와서 productCnt로 변환한다.
        int productCnt = myProdPriceRedis.opsForZSet().zCard(newProductGrp.getProdGrpId()).intValue();

        return productCnt;
    }

    // 새로운 productGrp이 기존에 있던 Keyword에 들어갔는지 확인하자
    public int setNewProductGrpToKeyword(String keyword, String prodGrpId, double score) {
        // Redis의 ZSet에 keyword를 키로 사용하여, prodGrpId와 점수를 추가한다.
        myProdPriceRedis.opsForZSet().add(keyword, prodGrpId, score);

        // 추가된 prodGrpId가 해당 keyword에 대해 ZSet에서 몇 번째 순위인지 가져와서 rank로 변환한다.
        int rank = myProdPriceRedis.opsForZSet().rank(keyword, prodGrpId).intValue();
        return rank;
    }

    public Keyword getLowestPriceProductByKeyword(String keyword){
        Keyword returnInfo = new Keyword();
        List<ProductGrp> tempProdGrp = new ArrayList<>();
        // keyword를 통해 ProductGroup 가져오기 (10개)
        tempProdGrp = getProductGrpUsingKeyword(keyword);
        // Loop 타면서 ProductGroup 으로 Product:price 가져오기

        // 가져온 정보들을 Return할 Object에 넣기
        returnInfo.setKeyword(keyword);
        returnInfo.setProductGrpList(tempProdGrp);

        // 해당 Object return
        return returnInfo;
    }

    public List<ProductGrp> getProductGrpUsingKeyword(String keyword){

        List<ProductGrp> returnInfo = new ArrayList<>();
        ProductGrp tempProdGrp = new ProductGrp();
        // input 받은 keyword로 productGroupId를 조회
        // Set을 다루기 귀찮아서 List 다룬다.
        List<String> prodGrpIdList = new ArrayList<>();
        // id만 받으면 되니까 range로 10개만 받아온다.
        prodGrpIdList = List.copyOf(myProdPriceRedis.opsForZSet().range(keyword, 0, 9));
        Product tempProduct = new Product();
        List<Product> tempProdList = new ArrayList<>();

        // 10개 prodGrpId로 loop
        for(final String prodGrpId:prodGrpIdList){
            // Loop 돌면서 ProductGroupId 로 Product:price 가져오기 (10개)
            Set prodAndPriceList = new HashSet();
            prodAndPriceList = myProdPriceRedis.opsForZSet().rangeWithScores(prodGrpId, 0, 9);

            // 코드 작성중
            Iterator<Object> prodPriceObj = prodAndPriceList.iterator();
            // loop 타면서 product obj에 bind (10개)
            while (prodPriceObj.hasNext()){
                ObjectMapper objMapper = new ObjectMapper();
                // ex) {"value":"p0001","score":25000}

                Map<String, String> prodPriceMap = objMapper.convertValue(prodPriceObj.next(), Map.class);

                // Product Obj에 bind
                tempProduct.setProductId(prodPriceMap.get("value"));
                tempProduct.setPrice(Integer.parseInt(prodPriceMap.get("score")));
                tempProdList.add(tempProduct);
            }
            // 10개의 Product price 입력완료
            tempProdGrp.setProdGrpId(prodGrpId);
            tempProdGrp.setProductList(tempProdList);
            returnInfo.add(tempProdGrp);
        }

        return returnInfo;
    }
}
