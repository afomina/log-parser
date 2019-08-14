package com.ef;

import com.ef.entity.BlockedEntity;
import com.ef.dao.BlockedEntityDao;
import com.ef.entity.LogEntity;
import com.ef.dao.LogEntityDao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final LogEntityDao logEntityDao = LogEntityDao.getInstance();
    private static final BlockedEntityDao blockedEntityDao = BlockedEntityDao.getInstance();

    public static void main(String... args) throws Exception {
        SearchParams searchParams = SearchParams.parse(args);
        if (logEntityDao.count() < 1) {
            System.out.println("Parsing access log file, please wait");
            parseAndSaveAccessLog(searchParams.getLogPath());
        }
        blockIpAddresses(searchParams);
    }

    private static void parseAndSaveAccessLog(String logPath) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(logPath));
        String line = reader.readLine();
        List<LogEntity> logEntities = new LinkedList<>();
        while (line != null) {
            String[] fields = line.split("\\|");
            logEntities.add(new LogEntity(DATE_FORMAT.parse(fields[0]),
                    fields[1], fields[2], Integer.parseInt(fields[3]), fields[4]));
            line = reader.readLine();
        }
        logEntityDao.save(logEntities);
    }

    private static void blockIpAddresses(SearchParams searchParams) {
        try {
            Date endDate = searchParams.getDuration() == SearchParams.Duration.HOURLY ?
                    new Date(searchParams.getStartDate().toInstant().plus(Duration.ofHours(1)).toEpochMilli()) :
                    new Date(searchParams.getStartDate().toInstant().plus(Duration.ofDays(1)).toEpochMilli());

            List<String> ipsToBlock = logEntityDao.findIpsHavingRequestsAtPeriod(searchParams.getStartDate(),
                    endDate, searchParams.getThreshold());
            if (ipsToBlock.size() > 0) {
                System.out.println(String.format("Those ip addresses made more than %d requests from %s to %s:", searchParams.getThreshold(),
                        DATE_FORMAT.format(searchParams.getStartDate()), DATE_FORMAT.format(endDate)));
                String reason = String.format("This ip address made more than %d requests from %s to %s:", searchParams.getThreshold(),
                        DATE_FORMAT.format(searchParams.getStartDate()), DATE_FORMAT.format(endDate));
                for (String ip : ipsToBlock) {
                    System.out.println(ip);
                    blockedEntityDao.saveIfNotExists(new BlockedEntity(ip, reason));
                }
            } else {
                System.out.println("No ip addresses were found with given parameters");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
