package com.example.demo.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.example.demo.pojo.StuVolun;
import com.example.demo.pojo.SupplementStu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SupplementListener extends AnalysisEventListener<SupplementStu> {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpecialityListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    //两个数据库操作对象

    private List<SupplementStu> supplementStus;

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     */
    public  SupplementListener(List<SupplementStu> supplementStus) {
        this.supplementStus = supplementStus;
    }


    //list中最多存放5个数据
    private static final int BATCH_COUNT = 5;

    //需要持久话的数据
    List<SupplementStu> list = new ArrayList<>();





    @Override
    public void invoke(SupplementStu data, AnalysisContext context) {
        list.add(data);
        if (list.size() >= BATCH_COUNT) {
            saveData();
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        if(list.size()>0)
            supplementStus.addAll(list);
    }

}
