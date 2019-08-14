package com.ef.dao;

import com.ef.entity.BlockedEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data access object for {@link BlockedEntity}
 *
 * @author Alexandra Andrianova
 */
public class BlockedEntityDao extends ParserDao {

    private static final String INSERT_SQL = "insert into blocked_entity (ip_address, reason) values (?, ?);";
    private static final String COUNT_BY_IP_SQL = "select count(id) from blocked_entity where ip_address = ?";

    private static BlockedEntityDao ENTITY_DAO;

    private BlockedEntityDao() {
        super();
    }

    public static BlockedEntityDao getInstance() {
        if (ENTITY_DAO == null) {
            ENTITY_DAO = new BlockedEntityDao();
        }
        return ENTITY_DAO;
    }

    public void save(BlockedEntity entity) throws Exception {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
            statement.setString(1, entity.getIpAddress());
            statement.setString(2, entity.getReason());
            statement.executeUpdate();
        }
    }

    public void saveIfNotExists(BlockedEntity entity) throws Exception {
        try (Connection connection = getConnection()) {
            if (countByIpAddress(connection, entity.getIpAddress()) < 1) {
                PreparedStatement statement = connection.prepareStatement(INSERT_SQL);
                statement.setString(1, entity.getIpAddress());
                statement.setString(2, entity.getReason());
                statement.executeUpdate();
            }
        }
    }

    public int countByIpAddress(Connection connection, String ipAddress) throws Exception {
        PreparedStatement statement = connection.prepareStatement(COUNT_BY_IP_SQL);
        statement.setString(1, ipAddress);
        ResultSet resultSet = statement.executeQuery();
        return resultSet.next() ? resultSet.getInt(1) : -1;
    }

}
