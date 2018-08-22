package com.hzh.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

/**
 * 读取外部的文件，将一行一行的数据发送给下游的bolt
 * 类似于hadoop MapReduce中的inputformat
 */
public class ReadDataSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private String str;

    /**
     * 初始化方法，类似于这个类的构造器，只被运行一次
     * 一般用来打开数据连接，打开网络连接。
     *
     * @param conf      传入的是storm集群的配置文件和用户自定义配置文件，一般不用。
     * @param context   上下文对象，一般不用
     * @param collector 数据输出的收集器，spout类将数据发送给collector，由collector发送给storm框架。
     */
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        String[] arrys ={"a b c","e w d","1 2 3"};

        Random random = new Random();
        int i = random.nextInt(3);

        str = arrys[i];

        System.out.println("====================: "+str);

        this.collector = collector;
    }

    /**
     * 下一个tuple，tuple是数据传送的基本单位。
     * 后台有个while循环一直调用该方法，每调用一次，就发送一个tuple出去
     *
     * storm 框架 会用一个while(true)直调用这个方法,如果数据源一直存在,则他会不断地发送Tuple
     *
     * 可以把 str = null; 注释掉,看看输出,会不断地叠加次数
     */
    public void nextTuple() {
        String line = null;

           // line = bufferedReader.readLine();
            if (str!=null) {
                //collector.emit(Arrays.asList(line));
                collector.emit(Arrays.asList(str));
            }
            str = null;
    }

    /**
     * 声明发出的数据是什么
     *
     * @param declarer
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("dabai"));
    }
}
