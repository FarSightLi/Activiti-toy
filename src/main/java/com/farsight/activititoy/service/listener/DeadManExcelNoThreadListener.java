package com.farsight.activititoy.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.uitl.SpringJobBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DeadManExcelNoThreadListener extends AnalysisEventListener<Deadman> {
    private List<Deadman> list = Collections.synchronizedList(new ArrayList<>());

    public List<Deadman> getData() {
        return list;
    }

    public DeadManExcelNoThreadListener() {
    }


    public void setData(List<Deadman> deadManExcelDataList) {
        this.list = deadManExcelDataList;
    }

    @Override
    public void invoke(Deadman deadManExcelData, AnalysisContext analysisContext) {
        log.info("接收到:" + deadManExcelData);
        if (deadManExcelData != null) {
            list.add(deadManExcelData);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析结束,开始插入数据");
        long startTime = System.currentTimeMillis();

        List<Deadman> deadManList = new ArrayList<>();
        for (Deadman deadManExcelData : list) {
            Deadman deadMan = new Deadman();
            BeanUtils.copyProperties(deadManExcelData, deadMan);
            deadManList.add(deadMan);
            DeadmanDao deadManMapper = SpringJobBeanFactory.getBean(DeadmanDao.class);
            deadManMapper.insertBatch(deadManList);
            long endTime = System.currentTimeMillis();
            System.out.println("总耗时:" + (endTime - startTime));

        }
    }
}
