package uni.social.connect.conf;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import uni.social.connect.Application;

@Configuration
public class PopulatorConfigExecuter {
	
	@Autowired
	private Environment env;
	
	@Bean
	@Conditional(value = RecreatingDatabaseCondition.class)
	public DataSourceInitializer d(final DataSource dataSource) {
		DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(databasePopulator());
		return initializer;
	}
	
	@Bean
	public DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(new ClassPathResource("db/create.sql"));
		populator.setIgnoreFailedDrops(true);
		return populator;
	}
	
	public static class RecreatingDatabaseCondition implements Condition {
		
		
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			String conditionalProperty = System.getProperty("db");
			return conditionalProperty != null && conditionalProperty.equalsIgnoreCase("recreate");
		}
		
	}
	
	public static class PopulateElasticSearchCondition implements Condition {
		
		
		@Override
		public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
			String conditionalProperty = System.getProperty("elastic");
			return conditionalProperty != null && conditionalProperty.equalsIgnoreCase("populate");
		}
		
	}
	
}
