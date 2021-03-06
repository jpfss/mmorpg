package com.kingston.mmorpg.game.database.user;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 玩家动态表db配置
 * 
 * @author kingston
 *
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "userEntityManagerFactory",
	transactionManagerRef = "userTransactionManager", 
	basePackages = {"com.kingston.mmorpg.game.database.user" })
public class UserDbConfig {

	@Autowired
	@Qualifier("userDataSource")
	private DataSource userDataSource;

	@Bean(name = "userEntityManager")
	public EntityManager entityManager(EntityManagerFactoryBuilder builder) {
		return userEntityManagerFactory(builder).getObject().createEntityManager();
	}

	@Bean(name = "userEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean userEntityManagerFactory(EntityManagerFactoryBuilder builder) {
		return builder.dataSource(userDataSource).properties(getVendorProperties(userDataSource))
				.packages("com.kingston.mmorpg.game.database.user") // 设置实体类所在位置
				.persistenceUnit("userPersistenceUnit").build();
	}

	@Autowired
	private JpaProperties jpaProperties;

	private Map<String, String> getVendorProperties(DataSource dataSource) {
		return jpaProperties.getHibernateProperties(dataSource);
	}

	@Bean(name = "userTransactionManager")
	public PlatformTransactionManager userTransactionManager(EntityManagerFactoryBuilder builder) {
		return new JpaTransactionManager(userEntityManagerFactory(builder).getObject());
	}

}
