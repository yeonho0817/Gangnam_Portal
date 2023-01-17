package com.gangnam.portal.dto;

import lombok.Getter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class QueryConditionDTO {
    private String sort;
    private String orderBy;
    private Integer pageNumber;
    private Integer pageSize;


    private Date startDate;
    private Date endDate;

    public QueryConditionDTO(String sort, String orderBy, String pageNumber, String pageSize) {
        if (! sort.equals("name") && ! sort.equals("rank")) sort = "name";
        if (! orderBy.equals("ASC") && ! orderBy.equals("DESC")) orderBy = "ASC";

        try {
            Integer.parseInt(pageNumber);
        } catch (NumberFormatException e) {
            pageNumber = "0";
        }

        try {
            Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
            pageSize = "10";
        }

        this.sort = sort;
        this.orderBy = orderBy;
        this.pageNumber = Integer.parseInt(pageNumber)-1;
        this.pageSize = Integer.parseInt(pageSize);
    }

    public QueryConditionDTO(String sort, String orderBy, String pageNumber, String pageSize, String startDate, String endDate) {
        if (! sort.equals("name") && ! sort.equals("rank") && ! sort.equals("date")) sort = "name";
        if (! orderBy.equals("ASC") && ! orderBy.equals("DESC")) orderBy = "ASC";

        try {
            Integer.parseInt(pageNumber);
        } catch (NumberFormatException e) {
            pageNumber = "0";
        }

        try {
            Integer.parseInt(pageSize);
        } catch (NumberFormatException e) {
            pageSize = "10";
        }

        this.sort = sort;
        this.orderBy = orderBy;
        this.pageNumber = Integer.parseInt(pageNumber)-1;
        this.pageSize = Integer.parseInt(pageSize);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (endDate == null) endDate = simpleDateFormat.format(new Date());
        if (startDate == null) startDate = simpleDateFormat.format(new Date());

        try {
            this.startDate = simpleDateFormat.parse(startDate);
            this.endDate = simpleDateFormat.parse(endDate);
        } catch (IllegalArgumentException | ParseException e) {
            this.startDate = new Date();
            this.endDate = new Date();
        }

        if (this.startDate.compareTo(this.endDate) == 1) {
            this.endDate = this.startDate;
        }
        System.out.println(this.startDate + " " + this.endDate);
    }

}
