package com.gaoj.mbg.plugins;

import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.AbstractJavaMapperMethodGenerator;

import java.util.List;
import java.util.Objects;

/**
 * @author gaojie
 * @version 1.0
 * @description:
 * @date 2024/8/16 16:31
 */
public class MyJavaMapperMethodPlugin extends AbstractJavaMapperMethodGenerator {

    @Override
    public void addInterfaceElements(Interface interfaze) {
        List<Method> methods = interfaze.getMethods();

        // 更改方法名中的PrimaryKey为Id
        for (Method method : methods) {
            String name = method.getName().replace("PrimaryKey","Id");
            method.setName(name);
        }

        // 移除insertSelective方法
        methods.removeIf(method -> Objects.equals("insertSelective",method.getName()));
    }
}
