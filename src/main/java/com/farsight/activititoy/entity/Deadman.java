package com.farsight.activititoy.entity;

    import com.baomidou.mybatisplus.annotation.IdType;
    import com.baomidou.mybatisplus.annotation.TableField;
    import com.baomidou.mybatisplus.annotation.TableId;
    import com.baomidou.mybatisplus.annotation.TableName;
    import com.baomidou.mybatisplus.extension.activerecord.Model;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author farsight
* @since 2023-09-22
*/
    @Data
        @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    @TableName("deadman")
    public class Deadman extends Model<Deadman> {

private static final long serialVersionUID = 1L;

        /**
        * 主键
        */
            @TableId(value = "uid", type = IdType.AUTO)
    private Integer uid;

        /**
        * 身份证号
        */
        @TableField("idCard")
    private String idCard;

        /**
        * 死者姓名
        */
        @TableField("userName")
    private String userName;

        /**
        * 死者性别
        */
        @TableField("sex")
    private String sex;

        /**
        * 死者年龄
        */
        @TableField("age")
    private String age;

        /**
        * 死因
        */
        @TableField("reason")
    private String reason;

        /**
        * 安排地狱层数
        */
        @TableField("house")
    private String house;


    @Override
    public Serializable pkVal() {
        return this.uid;
    }

}
