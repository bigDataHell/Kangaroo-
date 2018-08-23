package com.hzh.storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Asus on 2018/8/22.
 */
public class WordCountBolt extends BaseRichBolt {
    private Map<String, Integer> wordCountMap = new HashMap<String, Integer>();

    @Override
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        // 由于wordcount是最后一个bolt，所有不需要自定义OutputCollector collector，并赋值。
    }

    /**
     * 统计单词出现次数
     * @param input
     */
    @Override
    public void execute(Tuple input) {
        // 接受上游数据   一组数据
        String word = input.getStringByField("word");
        String num = input.getStringByField("num");

        if(wordCountMap.containsKey(word)){
            Integer integer = wordCountMap.get(word);
            wordCountMap.put(word,integer+Integer.parseInt(num));
        }else {
            wordCountMap.put(word,Integer.parseInt(num));
        }
        // 打印整个map
        System.out.println(wordCountMap);
        //如果还有下游,也可以发送出去.
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        // 不发送数据，所以不用实现。
    }
}
