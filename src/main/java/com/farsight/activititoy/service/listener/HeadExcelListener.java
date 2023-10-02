package com.farsight.activititoy.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.farsight.activititoy.dto.HeadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class HeadExcelListener extends AnalysisEventListener<HeadDTO> {

    public static List<HeadDTO> importList = new ArrayList<>();

    @Override
    public void invoke(HeadDTO data, AnalysisContext context) {
        log.info("解析到的一条数据: excelRow = {}", data);
        importList.add(data);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        // 解析完所有excel行, 保存到数据库或进行业务处理
        log.info("解析的所有数据 list = {}", importList);
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        log.info("表头数据 excelHead= {}", headMap);
        int size = headMap.size();
    }
}
