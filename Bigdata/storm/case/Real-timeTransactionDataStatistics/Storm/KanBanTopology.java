package cn.itcast.realtime.kanban.Storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.StormSubmitter;
import org.apache.storm.generated.AlreadyAliveException;
import org.apache.storm.generated.AuthorizationException;
import org.apache.storm.generated.InvalidTopologyException;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.kafka.spout.KafkaSpout;
import org.apache.storm.kafka.spout.KafkaSpoutConfig;
import org.apache.storm.topology.TopologyBuilder;

/**
 * 驱动类
 * 提交一个订单实时金额统计的任务
 */
public class KanBanTopology {
    public static void main(String[] args) throws InvalidTopologyException, AuthorizationException, AlreadyAliveException {
        TopologyBuilder topologyBuilder = new TopologyBuilder();

//      1.设置一个kafkaspout消费kafka集群中的数据
        KafkaSpoutConfig.Builder<String, String> builder = KafkaSpoutConfig.builder("hadoop-node-1:9092", "payment");
 //      设置consumer组,名字随意.
        builder.setGroupId("payment_storm_index");
        KafkaSpoutConfig<String, String> kafkaSpoutConfig = builder.build();
        topologyBuilder.setSpout("KafkaSpout", new KafkaSpout<String, String>(kafkaSpoutConfig), 3);

 //     以上相当于一个consumer


//        2.设置解析json串的bolt
        topologyBuilder.setBolt("ParserPaymentBolt", new ParserPaymentBolt(), 3).shuffleGrouping("KafkaSpout");
//         3. 设置计算指标的bolt
        topologyBuilder.setBolt("PaymentIndexProcessBolt", new PaymentIndexProcessBolt(), 3).shuffleGrouping("ParserPaymentBolt");
        // 4.得到一个真正的stormtopology对象，用来提交到集群
        StormTopology topology = topologyBuilder.createTopology();

        // 5.使用本地模式运行任务
        Config config = new Config();
        //不打印日志信息
        config.setDebug(false);
        config.setNumWorkers(1);
        //3、提交job
        //提交由两种方式：一种本地运行模式、一种集群运行模式。
        if (args != null && args.length > 0) {
            //运行集群模式
            StormSubmitter.submitTopology(args[0],config,topologyBuilder.createTopology());
        } else {
            LocalCluster localCluster = new LocalCluster();
            localCluster.submitTopology("KanBanTopology", config, topologyBuilder.createTopology());
        }

    }
}
