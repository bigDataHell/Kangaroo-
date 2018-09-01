package com.hzh.mr.weblog.invertedIndex;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

/**
 * Created by Asus on 2018/8/17.
 */
public class InvertedIndex {

    static class InvertedIndexMapper extends Mapper<LongWritable, Text, Text, Text> {

        private static Text keyInfo = new Text();// 存储单词和 URL 组合
        private static final Text valueInfo = new Text("1");// 存储词频,初始化为1

        @Override
        protected void map(LongWritable key, Text value, Context context)
                throws IOException, InterruptedException {

            String line = value.toString();
            String[] fields = line.split(" ");// 得到字段数组

            FileSplit fileSplit = (FileSplit) context.getInputSplit();// 得到这行数据所在的文件切片
            String fileName = fileSplit.getPath().getName();// 根据文件切片得到文件名

            for (String field : fields) {
                // key值由单词和URL组成，如“MapReduce:file1”
                keyInfo.set(field + ":" + fileName);
                context.write(keyInfo, valueInfo);
            }
        }

    }

    static class InvertedIndexCombiner extends Reducer<Text, Text, Text, Text> {

        private static Text info = new Text();

        // 输入： <MapReduce:file3 {1,1,...}>
        // 输出：<MapReduce file3:2>
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;// 统计词频
            for (Text value : values) {
                sum += Integer.parseInt(value.toString());
            }

            int splitIndex = key.toString().indexOf(":");
            // 重新设置 value 值由 URL 和词频组成
            info.set(key.toString().substring(splitIndex + 1) + ":" + sum);
            // 重新设置 key 值为单词
            key.set(key.toString().substring(0, splitIndex));

            context.write(key, info);
        }

    }

    static class InvertedIndexReducer extends Reducer<Text, Text, Text, Text> {
        private static Text result = new Text();

        // 输入：<MapReduce file3:2>
        // 输出：<MapReduce file1:1;file2:1;file3:2;>
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context)
                throws IOException, InterruptedException {
            // 生成文档列表
            String fileList = new String();
            for (Text value : values) {
                fileList += value.toString() + ";";
            }

            result.set(fileList);
            context.write(key, result);
        }

    }

    static class InvertedIndexRunner {
        public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
            Configuration conf = new Configuration();
            Job job = Job.getInstance(conf);

            job.setJarByClass(com.hzh.mr.weblog.invertedIndex.InvertedIndexRunner.class);

            job.setMapperClass(com.hzh.mr.weblog.invertedIndex.InvertedIndexMapper.class);
            job.setCombinerClass(com.hzh.mr.weblog.invertedIndex.InvertedIndexCombiner.class);
            job.setReducerClass(com.hzh.mr.weblog.invertedIndex.InvertedIndexReducer.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);

            FileInputFormat.setInputPaths(job, new Path("D:\\InvertedIndex\\input"));
            // 指定处理完成之后的结果所保存的位置
            FileOutputFormat.setOutputPath(job, new Path("D:\\InvertedIndex\\output"));

            // 向 yarn 集群提交这个 job
            boolean res = job.waitForCompletion(true);
            System.exit(res ? 0 : 1);
        }

    }

}


