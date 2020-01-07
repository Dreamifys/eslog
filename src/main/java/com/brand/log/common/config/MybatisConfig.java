package com.brand.log.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.util.ClassUtils;

import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@MapperScan(basePackages = "com.brand.log.common.dao")
public class MybatisConfig {

    private static Logger log = LoggerFactory.getLogger(MybatisConfig.class);

    @Value("${datasource.driverClass}")
    private String jdbcDriver;

    @Value("${datasource.user}")
    private String username;

    @Value("${datasource.password}")
    private String password;

    @Value("${datasource.jdbcUrl}")
    private String url;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource logDB() {
        DruidDataSource ds = new DruidDataSource();
        ds.setDriverClassName(this.jdbcDriver);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setUrl(url);
        ds.setMaxWait(10000);
        ds.setMaxActive(30);
        ds.setTestOnBorrow(true);
        ds.setValidationQuery("select 1");
        ds.setValidationQueryTimeout(10);
        return ds;
    }

    private Resource[] getResource(String basePackage, String pattern) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(new StandardEnvironment()
                .resolveRequiredPlaceholders(basePackage)) + "/" + pattern;
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
        return resources;
    }

    private Resource getMybatisConfig(String pattern) throws IOException {
        String packageSearchPath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                 + "/" + pattern;
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources(packageSearchPath);
        return resources[0];
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(@Qualifier("logDB") DataSource ds) throws Exception {
        log.debug("> sqlSessionFactory");
        final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(ds);
        sqlSessionFactory.setMapperLocations(getResource("mapper", "**/*.xml"));
        sqlSessionFactory.setConfigLocation(getMybatisConfig("mybatis-config.xml"));
        return sqlSessionFactory.getObject();
    }

    @Bean
    public DataSourceTransactionManager transactionManager(@Qualifier("logDB") DataSource ds) {
        log.debug("> transactionManager");
        return new DataSourceTransactionManager(ds);
    }
}

