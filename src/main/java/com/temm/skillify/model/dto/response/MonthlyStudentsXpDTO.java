package com.temm.skillify.model.dto.response;

import lombok.Data;

@Data
public class MonthlyStudentsXpDTO {
    private String month; // Format: "YYYY-MM"
    private int totalXp;
}