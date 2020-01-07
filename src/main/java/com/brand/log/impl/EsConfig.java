package com.brand.log.impl;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@ComponentScan(basePackageClasses=ESClientSpringFactory.class)
public class EsConfig {

    @Value("${es.cluster.addr}")
    String esClusterAddr;

    @Value("${es.cluster.name}")
    String esClusterName;

    @Value("${es.cluster.host}")
    String esHost;

    @Value("${es.cluster.port}")
    int esPort;

    @Bean
    public HttpHost[] httpHost(){
        //解析hostlist配置信息
        String[] split = esClusterAddr.split(",");
        //创建HttpHost数组，其中存放es主机和端口的配置信息
        HttpHost[] httpHostArray = new HttpHost[split.length];
        for(int i=0;i<split.length;i++){
            String item = split[i];
            httpHostArray[i] = new HttpHost(item.split(":")[0], Integer.parseInt(item.split(":")[1]), "http");
        }
        return httpHostArray;
    }



    @Bean(initMethod="init",destroyMethod="close")
    public ESClientSpringFactory getFactory(){
        return ESClientSpringFactory.
                build(httpHost(), 10, 50);
    }

    @Bean
    @Scope("singleton")
    public RestClient getRestClient(){
        return getFactory().getClient();
    }

    @Bean
    @Scope("singleton")
    public RestHighLevelClient getRHLClient(){
        return getFactory().getRhlClient();
    }


}
