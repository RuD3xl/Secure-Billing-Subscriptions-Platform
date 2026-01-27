package com.rud3xl.sbp.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    private int status;
    private String code;
    private String message;
    private List<String> details;
    private String traceId;
    private LocalDateTime timestamp;
    private String path;

}
