<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">

    <#if enableCache>
        <!-- 开启二级缓存 -->
        <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

    </#if>
    <#if baseResultMap>
        <!-- 通用查询映射结果 -->
        <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
            <#list table.fields as field>
                <#if field.keyFlag><#--生成主键排在第一位-->
                    <id column="${field.name}" property="${field.propertyName}"/>
                </#if>
            </#list>
            <#list table.commonFields as field><#--生成公共字段 -->
                <result column="${field.name}" property="${field.propertyName}"/>
            </#list>
            <#list table.fields as field>
                <#if !field.keyFlag><#--生成普通字段 -->
                    <result column="${field.name}" property="${field.propertyName}"/>
                </#if>
            </#list>
        </resultMap>

    </#if>
    <#if baseColumnList>
        <!-- 通用查询结果列 -->
        <sql id="Base_Column_List">
            <#list table.commonFields as field>
                <#if field.name == field.propertyName>${field.name}<#else>${field.name} AS ${field.propertyName}</#if>,
            </#list>
            ${table.fieldNames}
        </sql>
    </#if>

    <select id="selectAllAutoPage" resultMap="BaseResultMap" parameterType="${package.Entity}.${entity}">
        select
        <include refid="Base_Column_List"/>
        from ${table.name}
        <where>
            <#list table.fields as field>
                <if test="${field.propertyName} != null">and <#noparse>#</#noparse>{${field.name}
                    }=${field.propertyName}</if>
            </#list>
        </where>
        <if test="sortField != null and sortOrder != null and sortField != '' and sortOrder != '' ">
            order by <#noparse>${sortField} ${sortOrder}</#noparse>
        </if>
    </select>
</mapper>
