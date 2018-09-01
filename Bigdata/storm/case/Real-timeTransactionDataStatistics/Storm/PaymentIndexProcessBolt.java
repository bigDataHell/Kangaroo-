package cn.itcast.realtime.kanban.Storm;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import redis.clients.jedis.Jedis;

import java.util.Map;

/**
 * 计算相关的指标
 * 计算那些指标？
 * 计算订单金额（payPrice字段的金额进行累加）、订单数（一条消息就是一个订单）、订单人数（一个消息就是一个人）
 * -------------指标口径的统一，在每个企业中都不一样。作为开发者，一定要找产品经理或者提需求的人讨论明白。
 * 指标数据存放到哪里？
 * --------------存放到redis
 */
public class PaymentIndexProcessBolt extends BaseRichBolt {
    private Jedis jedis = null;

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {
        // 连接redis
        jedis = new Jedis("hadoop-node-3",6379);
    }

    /**
     * 业务处理
     *
     * 把数据缓存大redis
     *
     *   price:shop:shopId 456
     *   这种key的格式会在redis中形成层级目录
     *
     *    -\ price
     *          -\ shop
     *                  -| shopId
     *
     *    方便我们统计分类,查找
     *
     * @param input
     */
    public void execute(Tuple input) {
        // 获取上游发送的值
        Payment value = (Payment) input.getValue(0);
        //  计算订单金额（payPrice字段的金额进行累加）、订单数（一条消息就是一个订单）、订单人数（一个消息就是一个人）
        jedis.incrBy("price", value.getPayPrice());
        jedis.incrBy("orderNum",1);
        jedis.incrBy("orderUser",1);

        System.out.println("===============================");
        System.out.println("订单金额:"+value.getPayPrice());
        System.out.println("订单数:"+"1");
        System.out.println("===============================");

        // 计算每个品类的订单数，订单人数，订单金额
        // 返回的是三品的一级品类，二级品类，三级品类
        String categorys = value.getCatagorys();
        String[] split = categorys.split(",");
        //102,144,114
        for(int i=0;i<split.length;i++){
            //price:1:102
            //price:2:144
            //price:3:144
            jedis.incrBy("price:"+(i+1)+":"+split[i], value.getPayPrice());
            jedis.incrBy("orderNum:"+(i+1)+":"+split[i],1);
            jedis.incrBy("orderUser:"+(i+1)+":"+split[i],1);
        }
        // 计算商品属于哪个产品线
//        jedis.incrBy("price:cpxid:"+split[i], value.getPayPrice());
        //计算每个店铺的订单人数，订单金额，订单数
        jedis.incrBy("price:shop:"+value.getShopId(), value.getPayPrice());
        jedis.incrBy("orderNum:shop:"+value.getShopId(),1);
        jedis.incrBy("orderUser:shop:"+value.getShopId(),1);

        System.out.println("===============================");
        System.out.println("price:shop:"+value.getShopId()+" : "+value.getPayPrice());
        System.out.println("orderNum:shop:"+value.getShopId()+" : "+1);
        System.out.println("orderUser:shop:"+value.getShopId()+" : "+1);
        System.out.println("===============================");

        //计算每个商品的订单人数，订单金额，订单数
        jedis.incrBy("price:pid:"+value.getProductId(), value.getPayPrice());
        jedis.incrBy("orderNum:pid:"+value.getProductId(),1);
        jedis.incrBy("orderUser:pid:"+value.getProductId(),1);

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }
}
