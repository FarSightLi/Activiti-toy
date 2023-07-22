package ${package.Controller};


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.web.bind.annotation.RequestMapping;
import com.baomidou.mybatisplus.plugins.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.farsight.quickstudy.util.Result
import java.util.Arrays;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${entity};
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.List;

<#if restControllerStyle>
    import org.springframework.web.bind.annotation.RestController;
<#else>
    import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
    import ${superControllerClassPackage};
</#if>

/**
* <p>
    * ${table.comment} 前端控制器
    * </p>
*
* @author ${author}
* @since ${date}
*/
<#if restControllerStyle>
    @RestController
    @Api(tags = { "${table.controllerName}" })
<#else>
    @Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
@Autowired
private ${table.serviceName} ${table.entityPath}Service;

/**
* 分页查询
* @author ${author}
* @since ${date}
*/
@PostMapping("/page")
@ApiOperation(value = "分页")
public ResultInfo
<Page<${entity}>> page(@RequestBody ${entity}DTO dto) {
return ResultInfo.success(${table.entityPath}Service.selectPage(dto.getPage()));
}


/**
* 列表查询
* @author ${author}
* @since ${date}
*/
@PostMapping("/search")
@ApiOperation(value = "列表查询")
public ResultInfo
<List<${entity}>> list(@RequestBody ${entity} entity) {
EntityWrapper<${entity} > wrapper = new EntityWrapper<>();
return ResultInfo.success(${table.entityPath}Service.selectList(wrapper));
}

/**
* 根据id查询
* @author ${author}
* @since ${date}
*/
@GetMapping("/getById/{id}")
@ApiOperation(value = "根据id查询")
public ResultInfo<${entity}> getById(@PathVariable("id") String id) {
return ResultInfo.success(${table.entityPath}Service.selectById(id));
}

/**
* 添加
* @author ${author}
* @since ${date}
*/
@PostMapping("/add")
@ApiOperation(value = "添加")
public ResultInfo
<Boolean> add(@RequestBody ${table.entityName} ${table.entityPath}) {
    return ResultInfo.success(${table.entityPath}Service.insert(${table.entityPath}));
    }

    /**
    * 修改
    * @author ${author}
    * @since ${date}
    */
    @PutMapping("/update")
    @ApiOperation(value = "修改")
    public ResultInfo
    <Boolean> update(@RequestBody ${table.entityName} ${table.entityPath}) {
        return ResultInfo.success(${table.entityPath}Service.updateById(${table.entityPath}));
        }

        /**
        * 批量删除
        * @author ${author}
        * @since ${date}
        */
        @DeleteMapping("/deleteBatch/{id}")
        @ApiOperation(value = "批量删除")
        public ResultInfo
        <Boolean> deleteBatch(@PathVariable("id") String ids) {
            return ResultInfo.success(${table.entityPath}Service.deleteBatchIds(Arrays.asList(ids.split(","))));
            }

            /**
            * 删除
            * @author ${author}
            * @since ${date}
            */
            @DeleteMapping("/delete/{id}")
            @ApiOperation(value = "删除")
            public ResultInfo
            <Boolean> delete(@PathVariable("id") String id) {
                return ResultInfo.success(${table.entityPath}Service.deleteById(id));
                }
                }
                </#if>
                </#if>
