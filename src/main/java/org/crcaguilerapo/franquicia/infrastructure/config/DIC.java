package org.crcaguilerapo.franquicia.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.crcaguilerapo.franquicia.domain.ports.in.IBranchRepository;
import org.crcaguilerapo.franquicia.domain.ports.in.IFranchiseRepository;
import org.crcaguilerapo.franquicia.domain.ports.in.IProductRepository;
import org.crcaguilerapo.franquicia.domain.usecases.BranchUsecase;
import org.crcaguilerapo.franquicia.domain.usecases.FranchiseUsecase;
import org.crcaguilerapo.franquicia.domain.usecases.ProductUsecase;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.BranchRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.FranchiseRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.ProductRepository;
import org.crcaguilerapo.franquicia.infrastructure.adapters.out.mysql.Seeders;
import org.crcaguilerapo.franquicia.infrastructure.service.Serialization;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DIC {

    @Value("${database.url}")
    private String dataBaseUrl;

    @Value("${database.user}")
    private String dataBaseUser;

    @Value("${database.password}")
    private String dataBasePassword;

    // Here you can change the adapter
    @Bean
    public IBranchRepository branchRepository(DSLContext ctx) {
        return new BranchRepository(ctx);
    }

    // Here you can change the adapter
    @Bean
    public IProductRepository productRepository(DSLContext ctx) {
        return new ProductRepository(ctx);
    }

    // Here you can change the adapter
    @Bean
    public IFranchiseRepository franchiseRepository(DSLContext ctx) {
        return new FranchiseRepository(ctx);
    }

    @Bean
    public FranchiseUsecase franchiseUsecase(IFranchiseRepository franchiseRepository) {
        return new FranchiseUsecase(franchiseRepository);
    }

    @Bean
    public ProductUsecase productUsecase(
            IProductRepository productRepository,
            IBranchRepository branchRepository
    ) {
        return new ProductUsecase(productRepository, branchRepository);
    }

    @Bean
    public BranchUsecase branchUsecase(
            IBranchRepository branchRepository,
            IFranchiseRepository franchiseRepository
    ) {
        return new BranchUsecase(branchRepository, franchiseRepository);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public Serialization serialization(ObjectMapper objectMapper) {
        return new Serialization(objectMapper);
    }

    @Bean
    public DSLContext ctx() {
        try {
            Connection conn = DriverManager
                    .getConnection(
                            this.dataBaseUrl,
                            this.dataBaseUser,
                            this.dataBasePassword
                    );
            return  DSL.using(conn, SQLDialect.MYSQL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    @Profile("local")
    public Seeders seeder(DSLContext ctx) {
        Seeders seeders = new Seeders(ctx);
        seeders.start();
        return seeders;
    }
}
