package edu.dongguk.complaint.orchestrator.global.util;

import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ComplaintData {
    @ExcelProperty(index = 0)
    private String title;

    @ExcelProperty(index = 1)
    private String content;
}