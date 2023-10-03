package com.farsight.activititoy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.farsight.activititoy.entity.LabelTest;

import java.util.List;

/**
 * @author 骆灵上
 * @description 针对表【label_test】的数据库操作Service
 * @createDate 2023-10-02 15:02:28
 */
public interface LabelTestService extends IService<LabelTest> {
    void add(List<Integer> list);

    void update(LabelTest labelTest);
}
