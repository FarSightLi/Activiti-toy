package com.farsight.activititoy.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class HeadDTO {
    @ExcelProperty(index = 0)
    private int a1;
    @ExcelProperty(index = 1)
    private int a2;
    @ExcelProperty(index = 2)
    private int a3;
    @ExcelProperty(index = 3)
    private int a4;

}
