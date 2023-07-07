package edu.birzeit.zamilihotal.controllers;

import android.icu.util.Calendar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DateManager {


    public static String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        month++;
        return makeDateString(day, month, year);
    }
    public static String makeDateString(int day, int month, int year) {

        return getMonth(month) + " " + day + " " + year;
    }
    public static String getMonth(int month) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};

        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Invalid month: " + month);
        }

        return months[month - 1]; // Subtract 1 because array indices are 0-based
    }
    public static List<String> getNextNDays(String startDate, int n) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
        LocalDate start = LocalDate.parse(startDate, formatter);

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String formattedDate = start.plusDays(i).format(formatter);
            dates.add(formattedDate);
        }

        return dates;
    }

    public static String getLastDate(String dateString, int n) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.ENGLISH);
        LocalDate start = LocalDate.parse(dateString, formatter);

        List<String> dates = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String formattedDate = start.plusDays(i).format(formatter);
            dates.add(formattedDate);
        }


        return dates.get(dates.size() - 1);
    }

}
