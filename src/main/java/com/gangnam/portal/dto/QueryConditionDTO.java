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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (startDate != null && endDate != null) {
            try {
                this.startDate = simpleDateFormat.parse(startDate);
            } catch (IllegalArgumentException | ParseException e) {
                this.startDate = new Date();
            }

            try {
                this.endDate = simpleDateFormat.parse(endDate);
            } catch (IllegalArgumentException | ParseException e) {

            }

            if (this.startDate.compareTo(this.endDate) == 1) {
                this.endDate = this.startDate;
            }
        } else {

            try {
                if (startDate == null) this.startDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
                if (endDate == null) this.endDate = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
            } catch (ParseException e) {

            }

        }

    }

}
