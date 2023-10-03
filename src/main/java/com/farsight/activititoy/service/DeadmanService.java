package com.farsight.activititoy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.farsight.activititoy.entity.Deadman;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author farsight
 * @since 2023-09-22
 */
public interface DeadmanService extends IService<Deadman> {
    void importFileThread(MultipartFile file);

    void importFile(MultipartFile file);
}
