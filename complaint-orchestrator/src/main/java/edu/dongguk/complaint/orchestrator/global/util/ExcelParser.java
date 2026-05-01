package edu.dongguk.complaint.orchestrator.global.util;

import cn.idev.excel.FastExcel;
import cn.idev.excel.read.builder.ExcelReaderBuilder;
import cn.idev.excel.read.listener.PageReadListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelParser {

    public List<ComplaintData> parse (MultipartFile file) throws IOException {
        List<ComplaintData> result = new ArrayList<>();

        PageReadListener<ComplaintData> listener = new PageReadListener<>(result::addAll);
        ExcelReaderBuilder reader = FastExcel.read(file.getInputStream(), ComplaintData.class, listener);
        reader.sheet().doRead();

        return result;
    }
}