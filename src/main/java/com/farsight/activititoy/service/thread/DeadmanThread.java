package com.farsight.activititoy.service.thread;

import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Component
@Slf4j
public class DeadmanThread implements Runnable {

    //当前线程需要处理的总数据中的开始位置
    private int startPosition;
    private int endPosition;
    private List<Deadman> list = Collections.synchronizedList(new ArrayList<>());
    private CountDownLatch count;
    private DeadmanDao deadmanDao;

    public DeadmanThread() {
    }

    public DeadmanThread(CountDownLatch count, DeadmanDao deadmanDao, List<Deadman> list,
                         int startPosition, int endPosition) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.deadmanDao = deadmanDao;
        this.list = list;
        this.count = count;
    }

    @Override
    public void run() {
        try {
            List<Deadman> deadManList = new ArrayList<>();
            List<Deadman> newList = list.subList(startPosition, endPosition);
            //将EasyExcel对象和实体类对象进行一个转换
            for (Deadman deadManExcelData : newList) {
                Deadman deadMan = new Deadman();
                BeanUtils.copyProperties(deadManExcelData, deadMan);
                deadManList.add(deadMan);
            }
            //批量新增
            deadmanDao.insertBatch(deadManList);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            //当一个线程执行完了计数要减一不然这个线程会被一直挂起
            count.countDown();
        }
    }
}
