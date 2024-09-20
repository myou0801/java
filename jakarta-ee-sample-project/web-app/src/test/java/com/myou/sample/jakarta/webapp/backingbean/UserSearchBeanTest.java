package com.myou.sample.jakarta.webapp.backingbean;

import jakarta.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.impl.gradle.Gradle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.jboss.shrinkwrap.api.ShrinkWrap.create;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(ArquillianExtension.class)
//@ExtendWith(Arquillian.class)
class UserSearchBeanTest {

    @Inject
    private UserSearchBean userSearchBean;

    @Deployment
    public static WebArchive createDeployment() {
        return create(WebArchive.class)
                .addPackages(true, "com.myou.sample.jakarta.webapp")
                .addAsResource("mybatis-config.xml")
                .addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsLibraries(Gradle.resolver().forProjectDirectory(".").importCompileAndRuntime().resolve(). asList(JavaArchive.class))
                ;

    }

//    @BeforeEach
//    public void setUp() {
//        // 初期データを設定
//        try (Connection conn = DriverManager.getConnection("jdbc:h2:mem:testdb", "sa", "");
//             Statement stmt = conn.createStatement()) {
//            stmt.execute("CREATE TABLE IF NOT EXISTS user_table (id INT PRIMARY KEY, name VARCHAR(255));");
//            stmt.execute("INSERT INTO user_table (id, name) VALUES (1, 'Test Name');");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    @Test
    void search() {
        System.out.println("#########test##############");
        System.out.println("UserSearchBean:" + userSearchBean);
        userSearchBean.search();
        System.out.println("#########test##############");
        fail();
    }
}
