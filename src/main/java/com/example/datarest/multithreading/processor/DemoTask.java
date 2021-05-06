package com.example.datarest.multithreading.processor;

import lombok.Data;
import lombok.extern.log4j.Log4j2;

import java.io.Serializable;
import java.sql.Date;

@Data
@Log4j2
public class DemoTask implements Serializable {

    private String taskId;

    private Date sentDate;

    // Map this to enum
    private String status;

    private Date receivedDate;
}
