package com.tecsun.sisp.npm.service;

import com.tecsun.sisp.npm.repository.NetPortalRepository;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Path("/npmanagement/web")
public class WebConnectionApplication extends NetSuperApplication {
    private static final Logger LOGGER = Logger.getLogger(WebConnectionApplication.class);
    private static final NetPortalRepository REPOSITORY = new NetPortalRepository();

    public WebConnectionApplication(@Context HttpServletRequest request, @Context UriInfo ui) {
        super(request, ui);
    }

    @GET
    @Path("/getWebInfo")
    public String getWebInfo(@QueryParam("startDate") String startDate,
                             @QueryParam("endDate") String endDate,
                             @QueryParam("searchType") String searchType) {
        try {
            DateRange range = DateRange.parse(startDate, endDate, searchType);
            try (Connection connection = openConnection()) {
                List<Map<String, String>> values = REPOSITORY.connectionStats(
                        connection, new Timestamp(range.start.getTime()), new Timestamp(range.end.getTime()), range.format);
                return ok(values);
            }
        } catch (IllegalArgumentException e) {
            return error(e.getMessage());
        } catch (SQLException e) {
            LOGGER.error("查询网办访问量出错", e);
            return error("查询出错");
        }
    }

    private static final class DateRange {
        private final Date start;
        private final Date end;
        private final String format;

        private DateRange(Date start, Date end, String format) {
            this.start = start;
            this.end = end;
            this.format = format;
        }

        private static DateRange parse(String startValue, String endValue, String searchType) {
            if ("1".equals(searchType)) {
                int startYear = parseInteger(startValue, "开始年份");
                int endYear = parseInteger(endValue, "结束年份");
                if (endYear < startYear) {
                    throw new IllegalArgumentException("结束年份不能早于开始年份");
                }
                return new DateRange(date(startYear, Calendar.JANUARY, 1),
                        date(endYear + 1, Calendar.JANUARY, 1), "yyyy");
            }
            if ("2".equals(searchType)) {
                Date start = parseDate(startValue, "yyyy-MM", "开始月份");
                Date end = parseDate(endValue, "yyyy-MM", "结束月份");
                if (end.before(start)) {
                    throw new IllegalArgumentException("结束月份不能早于开始月份");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.MONTH, 1);
                return new DateRange(start, calendar.getTime(), "yyyy-MM");
            }
            if ("3".equals(searchType)) {
                Date start = parseDate(startValue, "yyyy-MM-dd", "开始日期");
                Date end = parseDate(endValue, "yyyy-MM-dd", "结束日期");
                if (end.before(start)) {
                    throw new IllegalArgumentException("结束日期不能早于开始日期");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(end);
                calendar.add(Calendar.DATE, 1);
                return new DateRange(start, calendar.getTime(), "yyyy-MM-dd");
            }
            throw new IllegalArgumentException("searchType 只支持 1、2、3");
        }

        private static int parseInteger(String value, String label) {
            try {
                return Integer.parseInt(value);
            } catch (Exception e) {
                throw new IllegalArgumentException(label + "格式不正确");
            }
        }

        private static Date parseDate(String value, String pattern, String label) {
            try {
                SimpleDateFormat format = new SimpleDateFormat(pattern);
                format.setLenient(false);
                return format.parse(value);
            } catch (ParseException e) {
                throw new IllegalArgumentException(label + "格式不正确，应为 " + pattern);
            }
        }

        private static Date date(int year, int month, int day) {
            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(year, month, day);
            return calendar.getTime();
        }
    }
}
