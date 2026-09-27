package com.zinhle.cloudfleet.drone.ops;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/ops/database")
public class DatabaseHealthController {

    private final DataSource dataSource;

    public DatabaseHealthController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @GetMapping
    public Map<String, Object> health() throws Exception {
        Map<String, Object> result = new LinkedHashMap<>();
        try (Connection connection = dataSource.getConnection()) {
            result.put("healthy", connection.isValid(2));
            result.put("databaseProduct", connection.getMetaData().getDatabaseProductName());
            result.put("databaseVersion", connection.getMetaData().getDatabaseProductVersion());
        }
        if (dataSource instanceof HikariDataSource hikari && hikari.getHikariPoolMXBean() != null) {
            result.put("activeConnections", hikari.getHikariPoolMXBean().getActiveConnections());
            result.put("idleConnections", hikari.getHikariPoolMXBean().getIdleConnections());
            result.put("totalConnections", hikari.getHikariPoolMXBean().getTotalConnections());
            result.put("threadsAwaitingConnection", hikari.getHikariPoolMXBean().getThreadsAwaitingConnection());
        }
        return result;
    }
}
