package com.ade.es;

import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author: shudj
 * @time: 2019/7/29 15:52
 * @description:
 */
public class ES_JAVA_API {

    public static void main(String[] args) {

        try(RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(new HttpHost("192.168.1.130", 9200, "http")))) {

            searchData(client);
        }  catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * @author shudj
     * @date 17:04 2019/7/29
     * @description  添加索引
     *
      * @param client
     * @return void
     **/
    public static void addIndex(RestHighLevelClient client) throws IOException {
        // 1. 创建索引request参数：索引名jd_sn_index_test
        CreateIndexRequest request = new CreateIndexRequest("jd_sn_index");

        // 2. 设置索引的settings
        // 分片数
        request.settings(Settings.builder().put("index.number_of_shards", 3)
                // 副本数
                .put("index.number_of_replicas", 0)
                // 默认分词器
                .put("analysis.analyzer.default.tokenizer", "hanlp").build());

        XContentBuilder mapping = XContentFactory.jsonBuilder()
                //表示开始设置值
                .startObject()
                    //设置只定义字段，不传参
                    .startObject("properties")
                        //定义字段名
                        .startObject("id")
                            .field("type", "integer")
                        .endObject()
                        .startObject("sku_id")
                            .field("type", "text")
                        .endObject()
                        .startObject("sku_name")
                            .field("type", "text")
                        .endObject()
                        .startObject("old_price")
                            .field("type", "double")
                        .endObject()
                        .startObject("sale_price")
                            .field("type", "double")
                        .endObject()
                        .startObject("category")
                            .field("type", "text")
                        .endObject()
                        .startObject("comments")
                            .field("type", "text")
                        .endObject()
                        .startObject("path1")
                            .field("type", "text")
                        .endObject()
                        .startObject("mark")
                            .field("type", "integer")
                        .endObject()
                    .endObject()
                .endObject();


        // 3. 设置索引的mappings
        request.mapping(mapping);

        // 4. 设置索引的别名
        request.alias(new Alias("jd_sn"));

        // 5. 发送请求
        // 5.1 发送异步请求
        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);

        // 6. 处理响应
        boolean acknowledged = response.isAcknowledged();
        boolean shardsAcknowledged = response.isShardsAcknowledged();

        System.out.println("acknowledged = " + acknowledged);
        System.out.println("shardsAcknowledged = " + shardsAcknowledged);
    }

    /**
     * @author shudj
     * @date 9:23 2019/7/30
     * @description 插入数据
     *
      * @param client
     * @return void
     **/
    public static void inserData(RestHighLevelClient client) throws IOException {
        // 1. 创建请求
        IndexRequest request = new IndexRequest("jd_sn_index_test");
        // 2.准备文档数据
        /*// 方式一：直接给JSON串
        String jsonString = "{" +
                "\"sku_id\":\"18318795431\"," +
                "\"sku_name\":\"爱丽小屋（ERISE HOUSE） 【专卖店】双头自动带刷眉笔防水防汗不脱色自然持久初学 眉笔6#黑色," +
                "\"old_price\": " + 59.00 +
                "\"sale_price\":" + 29.00 +
                "\"comments\":\"{\"goodRateShow\":\"99\",\"generalRate\":\"100\",\"tag_极其好用\":\"9\",\"defaultGoodCount\":\"55000\",\"tag_款式时尚\":\"1\",\"averageScore\":\"5\",\"commentCount\":\"65000\",\"videoCount\":\"40\",\"afterCount\":\"60\",\"generalCount\":\"100\",\"tag_颜色纯正\":\"2\",\"goodCount\":\"56000\",\"poorCount\":\"100\"}\"" +
                "\"mark\":" + 1 +
                "}";

        request.source(jsonString, XContentType.JSON);

        // 方式二：以map的对象来表示文档
        HashMap<String, Object> map = new HashMap<>(6);
        map.put("sku_id", "18318795431");
        map.put("sku_name", "爱丽小屋（ERISE HOUSE） 【专卖店】双头自动带刷眉笔防水防汗不脱色自然持久初学 眉笔6#黑色");
        map.put("old_price", 59.00);
        map.put("sale_price", 29.00);
        map.put("comments", "{\"goodRateShow\":\"99\",\"generalRate\":\"100\",\"tag_极其好用\":\"9\",\"defaultGoodCount\":\"55000\",\"tag_款式时尚\":\"1\",\"averageScore\":\"5\",\"commentCount\":\"65000\",\"videoCount\":\"40\",\"afterCount\":\"60\",\"generalCount\":\"100\",\"tag_颜色纯正\":\"2\",\"goodCount\":\"56000\",\"poorCount\":\"100\"}\"");
        map.put("mark", 1);

        request.source(map);*/

        // 方式三：用XContentBuilder来构建文档
        XContentBuilder builder = XContentFactory.jsonBuilder();
        builder.startObject();
        {
            builder.field("sku_id", "18318795431");
            builder.field("sku_name", "爱丽小屋（ERISE HOUSE） 【专卖店】双头自动带刷眉笔防水防汗不脱色自然持久初学 眉笔6#黑色");
            builder.field("old_price", 59.00);
            builder.field("sale_price", 29.00);
            builder.field("comments", "{\"goodRateShow\":\"99\",\"generalRate\":\"100\",\"tag_极其好用\":\"9\",\"defaultGoodCount\":\"55000\",\"tag_款式时尚\":\"1\",\"averageScore\":\"5\",\"commentCount\":\"65000\",\"videoCount\":\"40\",\"afterCount\":\"60\",\"generalCount\":\"100\",\"tag_颜色纯正\":\"2\",\"goodCount\":\"56000\",\"poorCount\":\"100\"}\"");
            builder.field("mark", 1);
        }
        builder.endObject();
        request.source(builder);

        /*// 方式四：直接用key-value对给出
        request.source("sku_id", "18318795431",
        "sku_name", "爱丽小屋（ERISE HOUSE） 【专卖店】双头自动带刷眉笔防水防汗不脱色自然持久初学 眉笔6#黑色",
        "old_price", 59.00,
        "sale_price", 29.00,
        "comments", "{\"goodRateShow\":\"99\",\"generalRate\":\"100\",\"tag_极其好用\":\"9\",\"defaultGoodCount\":\"55000\",\"tag_款式时尚\":\"1\",\"averageScore\":\"5\",\"commentCount\":\"65000\",\"videoCount\":\"40\",\"afterCount\":\"60\",\"generalCount\":\"100\",\"tag_颜色纯正\":\"2\",\"goodCount\":\"56000\",\"poorCount\":\"100\"}\"",
        "mark", 1);
*/
        // 其他一些可选设置
        // 设置routing值
       /* request.routing("routing");

        // 设置主分片等待时长
        request.timeout(TimeValue.timeValueSeconds(7));
        // 设置重新刷新策略
        request.setRefreshPolicy("wait_for");
        // 设置版本号
        request.version(2);
        // 操作类别
        request.opType(DocWriteRequest.OpType.CREATE);*/

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        if (null != response) {
            String index = response.getIndex();
            String id = response.getId();
            long version = response.getVersion();
            if (response.getResult() == DocWriteResponse.Result.CREATED) {
                System.out.println("新增文档成功，处理逻辑代码写到这里。");
            } else if (response.getResult() == DocWriteResponse.Result.UPDATED) {
                System.out.println("修改文档成功，处理逻辑代码写到这里。");
            }

            System.out.println("index = " + index);
            System.out.println("id = " + id);
            System.out.println("version = " + version);
        }
    }

    public static void bulkInsertData(RestHighLevelClient client) throws SQLException, IOException {
        Connection connection = DBConnection.getConnection();
        Statement statement = connection.createStatement();
        String sql = "SELECT * from " +
                "(SELECT sku_id, sku_name, old_price, sale_price, category, comments, mark from jd_sn_skuinfo where mark = 1 LIMIT 50000) a " +
                "UNION " +
                "SELECT sku_id, sku_name, old_price, sale_price, category, comments, mark from jd_sn_skuinfo where mark = 0 LIMIT 100000";
        ResultSet rs = statement.executeQuery(sql);
        BulkRequest request = new BulkRequest();
        int count = 0;
        while (rs.next()) {
            IndexRequest indexRequest = new IndexRequest("jd_sn_index_test");
            XContentBuilder builder = XContentFactory.jsonBuilder();
            builder.startObject();
            {
                builder.field("sku_id", rs.getString("sku_id"));
                builder.field("sku_name", rs.getString("sku_name"));
                builder.field("old_price", rs.getDouble("old_price"));
                builder.field("sale_price", rs.getDouble("sale_price"));
                builder.field("category", rs.getString("category"));
                builder.field("comments", rs.getString("comments"));
                builder.field("mark", rs.getInt("mark"));
            }
            builder.endObject();
            indexRequest.source(builder);
            request.add(indexRequest);
            if (count % 1000 == 0) {
                client.bulk(request, RequestOptions.DEFAULT);
                request = new BulkRequest();
                System.out.println(count);
            }
            count ++;
        }
        rs.close();
        statement.close();
        connection.close();

        BulkResponse bulk = client.bulk(request, RequestOptions.DEFAULT);
        if (null != bulk) {
            System.out.println(bulk.getItems());
            System.out.println(bulk.status());
        }
    }

    public static void searchData(RestHighLevelClient client) throws IOException {
        String skuName = "康师傅小鸡炖蘑菇桶普通超值";
        MultiMatchQueryBuilder analyzer = QueryBuilders.multiMatchQuery(skuName, "sku_name", "category").analyzer("hanlp");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(analyzer).size(100).postFilter(QueryBuilders.matchQuery("mark", 1));
        SearchRequest searchRequest = new SearchRequest("jd_sn_index");
        searchRequest.source(sourceBuilder);
        SearchResponse search = client.search(searchRequest, RequestOptions.DEFAULT);
        if (null != search) {
            System.out.println(search);
            System.out.println("===============================================================");
            SearchHit[] hits = search.getHits().getHits();
            System.out.println(hits.length);
            System.out.println("===============================================================");
            for (int i = 0; i < hits.length; i++) {
                System.out.println(hits[i]);
            }
        }
    }
}
