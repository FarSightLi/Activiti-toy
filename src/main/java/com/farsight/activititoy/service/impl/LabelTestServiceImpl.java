package com.farsight.activititoy.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.LabelTestDao;
import com.farsight.activititoy.entity.LabelTest;
import com.farsight.activititoy.service.LabelTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 骆灵上
 * @description 针对表【label_test】的数据库操作Service实现
 * @createDate 2023-10-02 15:02:28
 */
@Service
@Slf4j
public class LabelTestServiceImpl extends ServiceImpl<LabelTestDao, LabelTest>
        implements LabelTestService {
    // 定义标签与位掩码的映射关系
    private static final Map<String, Integer> TAGS = new HashMap<>();

    static {
        TAGS.put("A", 1);
        TAGS.put("B", 2);
        TAGS.put("C", 4);
        TAGS.put("D", 8);
        TAGS.put("E", 16);
        TAGS.put("F", 32);
        TAGS.put("G", 64);
        TAGS.put("H", 128);
    }

    private static final Map<Integer, String> NUMS = new HashMap<>();

    static {
        NUMS.put(1, "A");
        NUMS.put(2, "B");
        NUMS.put(3, "C");
        NUMS.put(4, "D");
        NUMS.put(5, "E");
        NUMS.put(6, "F");
        NUMS.put(7, "G");
        NUMS.put(8, "H");
    }

    @Override
    public void add(List<Integer> list) {
        ArrayList<String> labelList = new ArrayList<>();
        // 先对前端传来的list进行转换
        for (Integer integer : list) {
            String s = NUMS.get(integer);
            if (s != null) {
                labelList.add(s);
            } else {
                throw new RuntimeException("标签无法对应");
            }
        }
        ArrayList<Integer> maskList = new ArrayList<>();
        for (String s : labelList) {
            Integer integer = TAGS.get(s);
            if (integer != null) {
                maskList.add(integer);
            } else {
                throw new RuntimeException("标签无法对应");
            }
        }
        int selectedTags = 0;
        for (Integer integer : maskList) {
            selectedTags |= integer;
        }
        LabelTest labelTest = new LabelTest();
        labelTest.setLabel(selectedTags);
        this.save(labelTest);
    }

    @Override
    public void update(LabelTest labelTest) {
        int selectedTags = 0;
        for (Integer integer : labelTest.getLabelList()) {
            selectedTags |= integer;
        }
        labelTest.setLabel(selectedTags);
        this.updateById(labelTest);
    }
}




