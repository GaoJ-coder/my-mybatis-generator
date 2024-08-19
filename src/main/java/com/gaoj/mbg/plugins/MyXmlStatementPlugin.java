package com.gaoj.mbg.plugins;

import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author gaojie
 * @version 1.0
 * @description:
 * @date 2024/8/16 16:34
 */
public class MyXmlStatementPlugin extends AbstractXmlElementGenerator {
    @Override
    public void addElements(XmlElement parentElement){
        List<Element> elements = parentElement.getElements();


        for (Element element : elements) {
            XmlElement xmlElement = (XmlElement) element;
            List<Attribute> attributes = xmlElement.getAttributes();
            for (Attribute attribute : attributes) {
                modifyStatementId(attribute);
            }

            if (xmlElement.getName().equals("resultMap")){
                removeJdbcTypeAttr(xmlElement);
            }
        }

        elements.removeIf(element -> isNotNeedStatement((XmlElement) element));

    }


    /**
     * 移除不需要的方法
     * @param xmlElement
     */
    private boolean isNotNeedStatement(XmlElement xmlElement){
        boolean flag = false;
        List<Attribute> attributes = xmlElement.getAttributes();
        for (Attribute attribute : attributes) {
            String name = attribute.getName();
            String value = attribute.getValue();
            if ("id".equals(name) && "insertSelective".equals(value)){
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 修改statement中id值
     * @param attribute
     */
    private void modifyStatementId(Attribute attribute) {
        try {
            String name = attribute.getName();
            String value = attribute.getValue();
            if (!"id".equals(name) || !value.contains("PrimaryKey")){
                return;
            }

            Field valueF = attribute.getClass().getDeclaredField("value");
            valueF.setAccessible(true);
            String newVal = value.replace("PrimaryKey", "Id");
            valueF.set(attribute,newVal);
            valueF.setAccessible(false);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    /**
     * 移除resultMap中的jdbc属性
     * @param resultMapEl
     */
    private void removeJdbcTypeAttr(XmlElement resultMapEl){
        List<Element> resultElList = resultMapEl.getElements();
        for (Element resultEl : resultElList) {
            XmlElement resultXEl = (XmlElement) resultEl;
            resultXEl.getAttributes().removeIf(attr -> "jdbcType".equals(attr.getName()));
        }
    }


}
