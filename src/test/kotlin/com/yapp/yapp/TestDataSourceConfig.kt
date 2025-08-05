package com.yapp.yapp

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.sql.Connection
import java.sql.SQLException
import javax.sql.DataSource

class LoggingConnection(private val delegate: Connection) : Connection by delegate {
    init {
        val connId = Integer.toHexString(System.identityHashCode(delegate))
        println("[CONN-$connId] acquired by thread=${Thread.currentThread().name}")
    }

    override fun close() {
        val connId = Integer.toHexString(System.identityHashCode(delegate))
        println("[CONN-$connId] returned by thread=${Thread.currentThread().name}")
        delegate.close()
    }
}

class CustomLoggingDataSource(private val delegate: DataSource) : DataSource by delegate {
    @Throws(SQLException::class)
    override fun getConnection(): Connection {
        val c = delegate.connection
        return LoggingConnection(c)
    }

    @Throws(SQLException::class)
    override fun getConnection(
        username: String?,
        password: String?,
    ): Connection {
        val c = delegate.getConnection(username, password)
        return LoggingConnection(c)
    }
}

@Configuration
class LoggingDataSourceConfig {
    @Bean
    fun rawDataSource(dataSourceProperties: DataSourceProperties): HikariDataSource {
        val config =
            HikariConfig().apply {
                jdbcUrl = dataSourceProperties.url
                username = dataSourceProperties.username
                password = dataSourceProperties.password
                driverClassName = dataSourceProperties.driverClassName
                maximumPoolSize = 2
                minimumIdle = 2
                connectionTimeout = 30000
            }
        return HikariDataSource(config)
    }

    @Bean
    @Primary
    fun dataSource(rawDataSource: HikariDataSource): DataSource {
        return CustomLoggingDataSource(rawDataSource)
    }
}
