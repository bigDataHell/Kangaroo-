package com.hzh.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

import java.util.Arrays;
import java.util.Map;

/**
 * 输入：一行数据
 * 计算：对一行数据进行切割
 * 输出：单词及单词出现的次数
 */
public class SplitBolt extends BaseRichBolt {
    private OutputCollector collector;
    /**
     * 初始化方法，只被运行一次。
     * @param stormConf 配置文件
     * @param context 上下文对象，一般不用
     * @param collector 数据收集器
     */
    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    /**
     * 执行业务逻辑的方法
     * @param input 获取上游的数据
     */
    @Override
    public void execute(Tuple input) {
        // 获取上游的句子dabai
        String juzi = input.getStringByField("dabai");
        // 对句子进行切割
        String[] words = juzi.split(" ");
        // 发送数据
        for (String word : words) {
            // 需要发送单词及单词出现的次数，共两个字段
            collector.emit(Arrays.asList(word,"1"));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("word","num"));
    }
}
