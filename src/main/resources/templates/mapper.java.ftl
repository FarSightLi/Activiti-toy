package ${package.Mapper};

import ${package.Entity}.${entity};
import ${superMapperClassPackage};
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
/**
* <p>
    * ${table.comment} Mapper 接口
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if kotlin>
    interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
</#if>
@Mapper
public interface ${table.mapperName} extends BaseMapper<${entity}> {
    }