package com.ef;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public class SearchParams {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd.HH:mm:ss");
    private static final Pattern ARGUMENT_PATTERN = Pattern.compile("--([a-zA-Z]+)=(.*)");

    private final Date startDate;
    private final Duration duration;
    private final Integer threshold;
    private final String logPath;

    public SearchParams(Date startDate, Duration duration,
                        Integer threshold, String logPath) {
        this.startDate = requireNonNull(startDate, "startDate");
        this.duration = requireNonNull(duration, "duration");
        this.threshold = requireNonNull(threshold, "threshold");
        this.logPath = requireNonNull(logPath, "logPath");
    }

    public Date getStartDate() {
        return startDate;
    }

    public Duration getDuration() {
        return duration;
    }

    public Integer getThreshold() {
        return threshold;
    }

    public String getLogPath() {
        return logPath;
    }

    public static SearchParams parse(String... args) {
        SearchParams.Builder searchParamsBuilder = SearchParams.builder();
        for (String arg : args) {
            Matcher matcher = ARGUMENT_PATTERN.matcher(arg);
            if (matcher.find()) {
                String argumentName = matcher.group(1);
                String value = matcher.group(2);
                switch (argumentName) {
                    case "startDate":
                        try {
                            searchParamsBuilder.withStartDate(DATE_FORMAT.parse(value));
                        } catch (ParseException e) {
                            throw new IllegalArgumentException("Wrong date format: " + value);
                        }
                        break;
                    case "duration":
                        searchParamsBuilder.withDuration(Duration.byCode(value).orElseThrow());
                        break;
                    case "threshold":
                        searchParamsBuilder.withThreshold(Integer.parseInt(value));
                        break;
                    case "accesslog":
                        searchParamsBuilder.withLogPath(value);
                        break;
                    default:
                        throw new IllegalArgumentException("Argument is not supported: " + argumentName);
                }
            } else {
                throw new IllegalArgumentException("Illegal argument format: " + arg);
            }
        }
        return searchParamsBuilder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return "SearchParams{" +
                "startDate=" + startDate +
                ", duration=" + duration +
                ", threshold=" + threshold +
                ", logPath='" + logPath + '\'' +
                '}';
    }

    enum Duration {
        HOURLY("hourly"),
        DAILY("daily");

        private final String code;

        Duration(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public static Optional<Duration> byCode(String code) {
            return Arrays.stream(Duration.values())
                    .filter(value -> value.getCode().equals(code))
                    .findFirst();
        }
    }

    private static class Builder {
        private Date startDate;
        private Duration duration;
        private Integer threshold;
        private String logPath;

        public Builder withStartDate(Date startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withDuration(Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder withThreshold(Integer threshold) {
            this.threshold = threshold;
            return this;
        }

        public Builder withLogPath(String logPath) {
            this.logPath = logPath;
            return this;
        }

        public SearchParams build() {
            return new SearchParams(startDate, duration, threshold, logPath);
        }
    }
}
