package com.english.dto.request;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailRequest {
    private String to;
    private String subject;
    private String body;
}
