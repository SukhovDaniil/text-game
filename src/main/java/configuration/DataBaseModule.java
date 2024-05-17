package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import data.GameProvider;
import data.UserProvider;
import data.WorldProvider;
import data.impl.GameProviderImpl;
import data.impl.UserProviderImpl;
import data.impl.WorldProviderImpl;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

public class DataBaseModule extends AbstractModule {

    @Provides
    @Inject
    @Singleton
    public DataSource dataSource(@Named("database.url") String url) {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        return dataSource;
    }

    @Provides
    @Inject
    @Singleton
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Override
    protected void configure() {
        bind(WorldProvider.class).to(WorldProviderImpl.class);
        bind(UserProvider.class).to(UserProviderImpl.class);
        bind(GameProvider.class).to(GameProviderImpl.class);
    }
}
