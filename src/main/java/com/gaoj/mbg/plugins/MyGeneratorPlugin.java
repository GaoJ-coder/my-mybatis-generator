package com.gaoj.mbg.plugins;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.Plugin;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.Document;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author gaojie
 * @version 1.0
 * @description:
 * @date 2024/8/16 16:37
 */
public class MyGeneratorPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
//        String sqlMapFileName = introspectedTable.getMyBatis3XmlMapperFileName();
//        Pattern pattern = Pattern.compile("(.*?)((Entity)?Mapper)?\\.xml");
//        Matcher matcher = pattern.matcher(sqlMapFileName);
//        if (matcher.matches()) {
//            String matchStr = matcher.group(1);
//            introspectedTable.setMyBatis3XmlMapperFileName(matchStr + "Dao.xml");
//        }



        String javaMapperType = introspectedTable.getMyBatis3JavaMapperType();
        Pattern sqlMapFileNamePattern = Pattern.compile("(.*?)((Entity)?Mapper)");
        Matcher sqlMapFileNameMatcher = sqlMapFileNamePattern.matcher(javaMapperType);
        if (sqlMapFileNameMatcher.matches()) {
            String matchStr = sqlMapFileNameMatcher.group(1);
            introspectedTable.setMyBatis3JavaMapperType(matchStr + "Dao");
        }

        super.initialized(introspectedTable);
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        MyXmlStatementPlugin myXmlStatementPlugin = new MyXmlStatementPlugin();
        myXmlStatementPlugin.setContext(context);
        myXmlStatementPlugin.addElements(document.getRootElement());
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }


    @Override
    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        MyJavaMapperMethodPlugin myJavaMapperMethodPlugin = new MyJavaMapperMethodPlugin();
        myJavaMapperMethodPlugin.setContext(context);
        myJavaMapperMethodPlugin.setIntrospectedTable(introspectedTable);
        myJavaMapperMethodPlugin.addInterfaceElements(interfaze);

        Set<FullyQualifiedJavaType> importedTypes = new TreeSet<FullyQualifiedJavaType>();
        importedTypes.add(new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper"));
        interfaze.addImportedTypes(importedTypes);
        interfaze.addAnnotation("@Mapper");

        return super.clientGenerated(interfaze, topLevelClass, introspectedTable);
    }
}
