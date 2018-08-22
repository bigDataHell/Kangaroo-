package com.hzh.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.TopologyBuilder;

/**
 * wordcount的驱动类，用来提交任务的。
 *
 *
 * 在storm1.0 版本之前，项目的包名都是backtype
 * 在storm1.1.1以后被，正式给apache，所有的包名org.apache
 * 其实类没有变
 */
public class WordCountTopology {

    public static void main(String[] args) {
        // 通过TopologyBuilder来封装任务信息
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        // 设置spout,获取数据 类的id,类实例,并行度 id随意
        topologyBuilder.setSpout("readData",new ReadDataSpout(),1);

        // 设置bolt,对句子进行切割
        topologyBuilder.setBolt("solitBolt",new SplitBolt(),1).shuffleGrouping("readData");
        // 设置bolt,统计单词次数
        topologyBuilder.setBolt("wordcountBolt",new WordCountBolt(),1).shuffleGrouping("solitBolt");

        // 准备一个配置文件
        Config config = new Config();

        // storm中任务提交有两种方式，一种方式是本地模式，另一种是集群模式。
        LocalCluster localCluster = new LocalCluster();
        localCluster.submitTopology("wordcount",config,topologyBuilder.createTopology());



    }
}
