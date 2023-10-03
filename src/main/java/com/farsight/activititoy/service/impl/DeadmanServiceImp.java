package com.farsight.activititoy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.service.DeadmanService;
import com.farsight.activititoy.service.listener.DeadmanExcelListener;
import com.farsight.activititoy.service.listener.DeadmanExcelNoThreadListener;
import com.farsight.activititoy.uitl.MultipartFileToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author farsight
 * @since 2023-09-22
 */
@Service
public class DeadmanServiceImp extends ServiceImpl<DeadmanDao, Deadman> implements DeadmanService {
    @Autowired
    DeadmanDao deadmanDao;

    @Override
    public void importFileThread(MultipartFile file) {
        EasyExcel.read(MultipartFileToFile.MultipartFileToFile(file), Deadman.class, new DeadmanExcelListener(deadmanDao)).sheet().doRead();
    }

    @Override
    public void importFile(MultipartFile file) {
        EasyExcel.read(MultipartFileToFile.MultipartFileToFile(file), Deadman.class, new DeadmanExcelNoThreadListener(deadmanDao)).sheet().doRead();
    }
}
