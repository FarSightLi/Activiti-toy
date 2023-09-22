package com.farsight.activititoy.service;

import com.farsight.activititoy.entity.Deadman;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author farsight
 * @since 2023-09-22
 */
public interface DeadmanService extends IService<Deadman> {
    public void importFileThread(MultipartFile file);
    public void importFile(MultipartFile file);
}
