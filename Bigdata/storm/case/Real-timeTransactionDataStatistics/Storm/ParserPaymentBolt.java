package cn.itcast.realtime.kanban.Storm;

import com.google.gson.Gson;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.Map;

/**
 * 获取kafkaspout发送过来的数据,并解析成为一个javabean
 * 输入的json
 * 输出：java对象
 */
public class ParserPaymentBolt extends BaseRichBolt {
    private  OutputCollector collector;
    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        this.collector = collector;
    }

    public void execute(Tuple input) {

        //1.获取从kafka集群中消费数据
        // 从input中获取值，有两种方式。getStringByField 和通过角标获取值
        String json = input.getStringByField("value");
//        Object value1 = input.getValue(4);
//        String value2 = input.getString(4);
        System.out.println("-------------");
        Gson gson = new Gson();
        Payment payment = gson.fromJson(json, Payment.class);
        // 在发送数据的时候，Arrays.asList得到一个list对象
        // 其实storm框架也封装一个类，让开发者快速的得到一个list。就是Values类
        collector.emit(new Values(payment));

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {
        declarer.declare(new Fields("javaBean"));
    }
}
