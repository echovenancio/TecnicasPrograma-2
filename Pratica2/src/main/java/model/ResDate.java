package model;

public record ResDate(
        String client_ip,
        String utc_offset,
        String timezone,
        String day_of_week,
        String day_of_year,
        String utc_datetime,
        String datetime) {
}
