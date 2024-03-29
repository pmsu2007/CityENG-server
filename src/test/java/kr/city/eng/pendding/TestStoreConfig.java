package kr.city.eng.pendding;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariDataSource;

@Profile("test")
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories
public class TestStoreConfig {

  public static final String STORE_PACKAGE = "kr.city.eng.pendding.store";

  @Bean
  public DataSource dataSource() {
    DataSourceBuilder<HikariDataSource> dataSourceBuilder = DataSourceBuilder.create().type(HikariDataSource.class);

    dataSourceBuilder.driverClassName("org.h2.Driver");
    dataSourceBuilder.url("jdbc:h2:mem:database");
    // dataSourceBuilder.url("jdbc:h2:./test");
    dataSourceBuilder.username("sa");
    return dataSourceBuilder.build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean entityManagerFactory(ConfigurableEnvironment environment,
      DataSource dataSource) {
    HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
    jpaVendorAdapter.setGenerateDdl(true);
    jpaVendorAdapter.setShowSql(true);

    LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
    Properties jpaProperties = new Properties();
    jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
    jpaProperties.setProperty("hibernate.physical_naming_strategy",
        "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy");
    jpaProperties.setProperty("hibernate.implicit_naming_strategy",
        "org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy");
    factoryBean.setJpaProperties(jpaProperties);
    factoryBean.setPersistenceUnitName("h2Store");

    factoryBean.setDataSource(dataSource);
    factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
    factoryBean.setPackagesToScan(STORE_PACKAGE);

    return factoryBean;
  }

  @Bean
  public PlatformTransactionManager transactionManager(
      EntityManagerFactory entityManagerFactory) {
    return new JpaTransactionManager(entityManagerFactory);
  }

}
