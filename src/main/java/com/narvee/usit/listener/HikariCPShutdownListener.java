package com.narvee.usit.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.zaxxer.hikari.HikariDataSource;

public class HikariCPShutdownListener implements ServletContextListener {

	 private HikariDataSource dataSource;

	    public HikariCPShutdownListener(HikariDataSource dataSource) {
	        this.dataSource = dataSource;
	    }

	    @Override
	    public void contextInitialized(ServletContextEvent servletContextEvent) {
	    }

	    @Override
	    public void contextDestroyed(ServletContextEvent servletContextEvent) {
	        if (dataSource != null) {
	            dataSource.close();
	        }
	    }
}
