package com.farsight.activititoy.service.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.service.thread.DeadmanThread;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

@Service
@Slf4j
public class DeadmanExcelListener extends AnalysisEventListener<Deadman> {
    private DeadmanDao deadmanDao;

    public DeadmanExcelListener(DeadmanDao deadmanDao) {
        this.deadmanDao = deadmanDao;
    }

    private final List<Deadman> list = Collections.synchronizedList(new ArrayList<>());

    private static final int CORE_POOL_SIZE = 5; // 核心线程数
    private static final int MAX_POOL_SIZE = 10;

    private static final int QUEUE_CAPACITY = 100; //队列大小
    private static final Long KEEP_ALIVE_TIME = 1L; // 存活时间

    public List<Deadman> getData() {
        return list;
    }

    public DeadmanExcelListener() {
    }

    @Override
    public void invoke(Deadman deadManExcelData, AnalysisContext analysisContext) {
        if (deadManExcelData != null) {
            list.add(deadManExcelData);
        }
    }

    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("解析到：" + list.size() + "条数据");
        log.info("解析结束,开始插入数据");
        ExecutorService executor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new ThreadPoolExecutor.CallerRunsPolicy());
        //指定每个线程需要处理的导入数量,假设每个线程处理1000条
        int singleThreadDealCount = 1000;
        //根据假设每个线程需要处理的数量以及总数,计算需要提交到约我程池的线程数量
        int threadSize = (list.size() / singleThreadDealCount) + 1;
        //计算需要导入的数据总数,用于拆分时线程需要处理数据时使用
        int rowSize = list.size() + 1;
        //申明该线程需要处理数据的开始位置
        int startPosition = 0;
        //申明该线程需要处理数据的结束位置
        int endPosition = 0;

        CountDownLatch count = new CountDownLatch(threadSize);
        // 订算母个线程安处理的数据
        for (int i = 0; i < threadSize; i++) {
            //如果是最后一个线程,为保证程序不发生空指针异常,特殊判断结束位置
            if ((i + 1) == threadSize) {
                //计算开始位置
                startPosition = (i * singleThreadDealCount);
                //当前线程为划分的最后一个线程,则取总数据的最后为此线程的结束位置
                endPosition = rowSize - 1;
            } else {
                //计算开始位置
                startPosition = (i * singleThreadDealCount);

                endPosition = (i + 1) * singleThreadDealCount;
            }
            DeadmanThread thread = new DeadmanThread(count, deadmanDao, list, startPosition, endPosition);
            executor.execute(thread);
        }
        try {
            count.await();
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
        executor.shutdown();
    }
}
