package com.code.enhancement.apis;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;


@Component
public class DatabaseManager {
    @Autowired
    private DataSource dataSource;

    private HikariDataSource hikariDataSource;

    private void init() {
        if (dataSource instanceof HikariDataSource) {
            hikariDataSource = (HikariDataSource) dataSource;

            Timer timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    logPoolStatus();
                }
            }, 0, 30000);
        }
    }

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logPoolStatus() {
        if (hikariDataSource != null) {
            int total = hikariDataSource.getMaximumPoolSize();
            int active = hikariDataSource.getHikariPoolMXBean().getActiveConnections();
            int idle = hikariDataSource.getHikariPoolMXBean().getIdleConnections();
            int waiting = hikariDataSource.getHikariPoolMXBean().getThreadsAwaitingConnection();

            System.out.println("[HikariCP] Pool Stats - Active: " + active +
                    ", Idle: " + idle +
                    ", Waiting: " + waiting +
                    ", Total: " + total);

            if (waiting > 3) {
                System.err.println("[HikariCP] Pool Stats - Waiting: " + waiting);
            }

            if (idle > (total * 0.8)) {
                System.err.println("[HikariCP] Pool Stats - Idle: " + idle);
            }
        }
    }


}
