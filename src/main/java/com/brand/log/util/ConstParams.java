package com.brand.log.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class ConstParams {
    @Value("${enter.ids}")
    String enterIds;

    @Bean
    public Map<String,String> paramKey(){
        Map<String,String> mapping = new HashMap<String,String>();

        try{
            if (enterIds ==null || enterIds.isEmpty()) {
                mapping.put("base", "base");
            }else {

                String [] list = enterIds.split(",");

                for (String item:list) {
                    String [] partSplit = item.split(":");
                    if (partSplit.length == 2){
                        mapping.put(partSplit[0], partSplit[1]);
                    }
                }
            }
            mapping.put("3817848ef191468810fc4b1cfc855ba1", "tp");
            return mapping;
        }catch (Exception e){
            return null;
        }



    }
}
