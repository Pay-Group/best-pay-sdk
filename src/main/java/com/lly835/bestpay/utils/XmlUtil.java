package com.lly835.bestpay.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.Writer;

/**
 * Created by 廖师兄
 * 2017-07-02 15:30
 */
public class XmlUtil {

    private static XStream xStream = new XStream(new XppDriver(new NoNameCoder()) {

        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                @SuppressWarnings("rawtypes")
                public void startNode(String name, Class clazz) {
                    super.startNode(name, clazz);
                }

                ////当对象属性带下划线时，XStream会转换成双下划线，
                // 重写这个方法，不再像XppDriver那样调用nameCoder来进行编译，而是直接返回节点名称，避免双下划线出现
                @Override
                public String encodeNode(String name) {
                    return name;
                }


                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });
    /**
     * 对象转xml
     * @param obj
     * @return
     */
    public static String toXMl(Object obj) {
        //使用注解设置别名必须在使用之前加上注解类才有作用
        xStream.processAnnotations(obj.getClass());
        return xStream.toXML(obj);
    }

    public static Object fromXML(String xml, Class objClass) {
        Serializer serializer = new Persister();
        try {
            return serializer.read(objClass, xml);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    public static String toSimpleXML(Object obj) {
//        Serializer serializer = new Persister();
//        OutputElement outputElement = new OutputElement();
//        try {
//            serializer.write(obj, );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return stringWriter.toString();
//    }
}
