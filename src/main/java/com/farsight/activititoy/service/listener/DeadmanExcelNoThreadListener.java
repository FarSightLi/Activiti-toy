package com.farsight.activititoy.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.uitl.SpringJobBeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class DeadmanExcelNoThreadListener extends AnalysisEventListener<Deadman> {
    private Integer maxSize = 1000;
    private List<Deadman> list = Collections.synchronizedList(new ArrayList<>());

    public List<Deadman> getData() {
        return list;
    }

    private DeadmanDao deadmanDao;

    public DeadmanExcelNoThreadListener(DeadmanDao deadmanDao) {
        this.deadmanDao = deadmanDao;
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
        if (list.size() == maxSize){
            save(list);
        }
    }

    private boolean firstSave = true;
    private long startTime;
    private void save(List<Deadman> list){
        if (firstSave){
            log.info("解析结束,开始插入数据");
            startTime = System.currentTimeMillis();
        }
        List<Deadman> deadManList = new ArrayList<>();
        for (Deadman deadManExcelData : list) {
            Deadman deadMan = new Deadman();
            BeanUtils.copyProperties(deadManExcelData, deadMan);
            deadManList.add(deadMan);
        }
        log.info("解析结束,开始插入数据");
        deadmanDao.insertBatch(deadManList);
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析结束,开始插入数据");


        List<Deadman> deadManList = new ArrayList<>();
        for (Deadman deadManExcelData : list) {
            Deadman deadMan = new Deadman();
            BeanUtils.copyProperties(deadManExcelData, deadMan);
            deadManList.add(deadMan);
        }
        deadmanDao.insertBatch(deadManList);
        long endTime = System.currentTimeMillis();
        log.info("总耗时：" + (endTime - startTime) + "ms");
    }
}
