package com.temm.skillify.model.dto.response;

import java.util.List;

public class StudentRankingResponseDTO {
    public String classroomId;
    public String classroomName;
    public List<StudentRankingPositionDTO> ranking;
    public int yourPosition;
}