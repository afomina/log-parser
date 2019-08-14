package com.ef.dao;

import com.ef.entity.LogEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Data access object for {@link LogEntity}
 *
 * @author Alexandra Andrianova
 */
public class LogEntityDao extends ParserDao {

    private static final String INSERT_SQL = "insert into log (date, ip_address, request, status, user_agent) " +
            "values (?, ?, ?, ?, ?);";
    private static final String COUNT_SQL = "select count(*) from log;";
    private static final String SEARCH_SQL = "select ip_address from log " +
            "where date between ? and ? " +
            "group by ip_address " +
            "having count(*) > ?;";

    private static LogEntityDao ENTITY_DAO;

    private LogEntityDao() {
        super();
    }

    public static LogEntityDao getInstance() {
        if (ENTITY_DAO == null) {
            ENTITY_DAO = new LogEntityDao();
        }
        return ENTITY_DAO;
    }

    public void save(LogEntity logEntity) throws Exception {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            statement.setTimestamp(1, new Timestamp(logEntity.getDate().getTime()));
            statement.setString(2, logEntity.getIpAddress());
            statement.setString(3, logEntity.getRequest());
            statement.setInt(4, logEntity.getStatus());
            statement.setString(5, logEntity.getUserAgent());
            statement.executeUpdate();
        }
    }

    public void save(List<LogEntity> logEntities) throws Exception {
        Connection connection = getConnection();
        connection.setAutoCommit(false);
        PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
        for (LogEntity logEntity : logEntities) {
            statement.setTimestamp(1, new Timestamp(logEntity.getDate().getTime()));
            statement.setString(2, logEntity.getIpAddress());
            statement.setString(3, logEntity.getRequest());
            statement.setInt(4, logEntity.getStatus());
            statement.setString(5, logEntity.getUserAgent());
            statement.addBatch();
        }
        statement.executeBatch();
        connection.commit();
    }

    public int count() throws Exception {
        try (Connection connection = getConnection()) {
            ResultSet resultSet = connection.createStatement().executeQuery(COUNT_SQL);
            return resultSet.next() ? resultSet.getInt(1) : -1;
        }
    }

    public List<String> findIpsHavingRequestsAtPeriod(Date start, Date end, Integer threshold) throws Exception {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SEARCH_SQL);
            statement.setTimestamp(1, new Timestamp(start.getTime()));
            statement.setTimestamp(2, new Timestamp(end.getTime()));
            statement.setInt(3, threshold);
            ResultSet resultSet = statement.executeQuery();
            List<String> foundIpAddresses = new ArrayList<>();
            while (resultSet.next()) {
                foundIpAddresses.add(resultSet.getString(1));
            }
            return foundIpAddresses;
        }
    }

}
