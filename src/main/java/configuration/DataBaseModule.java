package configuration;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import data.GameController;
import data.UserController;
import data.WorldController;
import data.impl.GameControllerImpl;
import data.impl.UserControllerImpl;
import data.impl.WorldControllerImpl;
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
        bind(WorldController.class).to(WorldControllerImpl.class);
        bind(UserController.class).to(UserControllerImpl.class);
        bind(GameController.class).to(GameControllerImpl.class);
    }
}
