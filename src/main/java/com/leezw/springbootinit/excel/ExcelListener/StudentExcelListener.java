package com.leezw.springbootinit.excel.ExcelListener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.leezw.springbootinit.excel.StudentExcel;
import com.leezw.springbootinit.mapper.StudentMapper;

import java.util.ArrayList;
import java.util.List;


public class StudentExcelListener extends AnalysisEventListener<StudentExcel> {

    private final StudentMapper studentMapper;
    private final List<StudentExcel> batchList = new ArrayList<>();
    private static final int BATCH_COUNT = 100;

    public StudentExcelListener(StudentMapper StudentMapper) {
        this.studentMapper = StudentMapper;
    }

    @Override
    public void invoke(StudentExcel data , AnalysisContext analysisContext) {
        batchList.add(data);
        if (batchList.size() >= BATCH_COUNT) {
            saveData();
            batchList.clear();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        if (!batchList.isEmpty()) {
            studentMapper.batchInsert(batchList);
        }
    }

}

