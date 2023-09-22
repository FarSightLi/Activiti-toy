package com.farsight.activititoy.service.impl;

import com.alibaba.excel.EasyExcel;
import com.farsight.activititoy.entity.Deadman;
import com.farsight.activititoy.dao.DeadmanDao;
import com.farsight.activititoy.service.DeadmanService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsight.activititoy.service.listener.DeadManExcelNoThreadListener;
import com.farsight.activititoy.service.listener.DeadmanExcelListener;
import com.farsight.activititoy.uitl.MultipartFileToFile;
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

    @Override
    public void importFileThread(MultipartFile file) {
        EasyExcel.read(MultipartFileToFile.MultipartFileToFile(file),Deadman.class,new DeadmanExcelListener()).sheet().doRead();
    }

    @Override
    public void importFile(MultipartFile file) {
        EasyExcel.read(MultipartFileToFile.MultipartFileToFile(file),Deadman.class,new DeadManExcelNoThreadListener()).sheet().doRead();
    }
}
