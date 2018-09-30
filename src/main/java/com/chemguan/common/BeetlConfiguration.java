package com.chemguan.common;

import com.chemguan.util.Tools;
import org.beetl.core.resource.ClasspathResourceLoader;
import org.beetl.ext.spring.BeetlGroupUtilConfiguration;
import org.beetl.ext.spring.BeetlSpringViewResolver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by ShiWei on 2018-07-17.
 */
@Configuration
public class BeetlConfiguration {
    //模板根目录 ，比如 "templates"
    @Value("${beetl.templatesPath}")
    String templatesPath;

    @Value("${beetl.templatesPropertiesPath}")
    String templatesPropertiesPath;

    @Bean(name = "beetlConfig")
    public BeetlGroupUtilConfiguration getBeetlGroupUtilConfiguration() {
        BeetlGroupUtilConfiguration beetlGroupUtilConfiguration = new BeetlGroupUtilConfiguration();
        //获取Spring Boot 的ClassLoader
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if(loader==null){
            loader = BeetlConfiguration.class.getClassLoader();
        }
        Properties properties = new Properties();
        try {
            Resource res = new ClassPathResource(templatesPropertiesPath);
            InputStream input = res.getInputStream();
            properties.load(input);
            beetlGroupUtilConfiguration.setConfigProperties(properties);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ClasspathResourceLoader cploder = new ClasspathResourceLoader(loader,templatesPath);
        beetlGroupUtilConfiguration.setResourceLoader(cploder);
        //设置自定义函数
        Map map=new HashMap();
        map.put("tools",new Tools());
        beetlGroupUtilConfiguration.setFunctionPackages(map);
        beetlGroupUtilConfiguration.init();
        //如果使用了优化编译器，涉及到字节码操作，需要添加ClassLoader
        beetlGroupUtilConfiguration.getGroupTemplate().setClassLoader(loader);

        return beetlGroupUtilConfiguration;

    }

    @Bean(name = "beetlViewResolver")
    public BeetlSpringViewResolver getBeetlSpringViewResolver(@Qualifier("beetlConfig") BeetlGroupUtilConfiguration beetlGroupUtilConfiguration) {
        BeetlSpringViewResolver beetlSpringViewResolver = new BeetlSpringViewResolver();
        beetlSpringViewResolver.setSuffix(".html");
        beetlSpringViewResolver.setContentType("text/html;charset=UTF-8");
        beetlSpringViewResolver.setOrder(0);
        beetlSpringViewResolver.setConfig(beetlGroupUtilConfiguration);
        return beetlSpringViewResolver;
    }

}
