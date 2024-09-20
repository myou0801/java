package com.myou.sample.jakarta.webapp;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mybatis.cdi.SessionFactoryProvider;

import java.io.Reader;

@ApplicationScoped
public class MyBatisProducer {

    @Produces
    @SessionFactoryProvider
    public SqlSessionFactory produceSqlSessionFactory() throws Exception {
        try (Reader reader = Resources.getResourceAsReader("mybatis-config.xml")) {
            return new SqlSessionFactoryBuilder().build(reader, "default");
        }
    }

}
