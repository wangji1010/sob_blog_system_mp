/*
 Navicat Premium Data Transfer

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50724
 Source Host           : 127.0.0.1:3306
 Source Schema         : sob_blog_system

 Target Server Type    : MySQL
 Target Server Version : 50724
 File Encoding         : 65001

 Date: 16/10/2020 21:52:39
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_article`;
CREATE TABLE `tb_article`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户昵称',
  `category_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类ID',
  `content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '文章内容',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '类型（0表示富文本，1表示markdown）',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '封面',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（1表示已发布，2表示草稿，3表示删除）',
  `summary` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '摘要',
  `labels` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标签',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '阅读数量',
  `create_time` datetime(0) NOT NULL COMMENT '发布时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_article_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_category_article_on_category_id`(`category_id`) USING BTREE,
  CONSTRAINT `fk_user_article_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_article
-- ----------------------------
INSERT INTO `tb_article` VALUES ('1278908274774638592', '2居然更新了', '1275431122951471104', 'demoData', 'wj', '13456', '2测试更新的接口', '0', '1595772416242_1287388938993926144.png', '2', '2更新测试', '2更新-接口-文章', 0, '2020-07-03 04:27:48', '2020-07-03 08:18:58');
INSERT INTO `tb_article` VALUES ('1279270699466227712', 'java第五条册数', '1275431122951471104', 'demoData', 'wj', '13456', '测试缓存', '0', '1595772416242_1287388938993926144.png', '3', '测试来额', '测试-第五-牛逼', 11, '2020-07-04 04:27:57', '2020-07-07 04:13:02');
INSERT INTO `tb_article` VALUES ('1279584929176354816', '测试3', '1275431122951471104', 'demoData', 'wj', '13456', 'string', '0', '1595772416242_1287388938993926144.png', '2', 'string', '测试-java', 0, '2020-07-05 01:16:35', '2020-07-05 01:16:35');
INSERT INTO `tb_article` VALUES ('1279585060609064960', '测试7', '1275431122951471104', 'demoData', 'wj', '13456', 'string', '0', '1595772416242_1287388938993926144.png', '2', 'string', '测试-miui', 0, '2020-07-05 01:17:07', '2020-07-05 01:17:07');
INSERT INTO `tb_article` VALUES ('1303937385456730112', '第一篇文章，测试1', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1279213430472966144', '第一篇文章，测试1', '1', '1597638939196_1295217703685980160.gif', '1', '第一篇文章，测试1', '测试-es', 2, '2020-09-10 06:04:33', '2020-09-10 06:04:33');
INSERT INTO `tb_article` VALUES ('1303937854509940736', 'vue的秘密', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1287259445914501120', 'vue的秘密', '1', '1596792576929_1291667803044052992.png', '1', 'vue的秘密', 'vue-文章', 11, '2020-09-10 06:06:25', '2020-09-10 06:06:25');
INSERT INTO `tb_article` VALUES ('1303938134890774528', '安卓的密码', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '13456', '安卓的密码', '2', '1599441212673_1302776986539655168.png', '1', '安卓的密码', '安卓-密码', 10, '2020-09-10 06:07:32', '2020-09-10 06:07:32');
INSERT INTO `tb_article` VALUES ('1303938308362993664', 'java的调优', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1287260191263293440', 'java的调优', '1', '1599440482577_1302773924290887680.png', '1', 'java的调优', 'java-调优', 36, '2020-09-10 06:08:13', '2020-09-10 06:08:13');
INSERT INTO `tb_article` VALUES ('1308363749349916672', 'maven静态资源过滤问题', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1287260191263293440', '```basic\n<build>\n        <resources>\n            <resource>\n                <directory>src/main/java</directory>\n                <includes>\n                    <include>**/*.xml</include>\n                </includes>\n                <filtering>true</filtering>\n            </resource>\n        </resources>\n    </build>\n```\n', '1', '1599440985070_1302776031903481856.png', '1', '静态资源过滤问题', 'maven-静态资源-java', 18, '2020-09-22 11:13:21', '2020-09-22 14:07:06');
INSERT INTO `tb_article` VALUES ('1308946294642835456', 'sd', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1279213430472966144', 'sda', '1', '1599441212673_1302776986539655168.png', '1', 'ces', 'ces-的撒', 0, '2020-09-24 01:48:10', '2020-09-24 02:52:19');
INSERT INTO `tb_article` VALUES ('1308967667993935872', '撒旦撒旦', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1279213430472966144', '的撒', '1', '1599440985070_1302776031903481856.png', '1', '大苏打', '的撒-阿斯顿', 0, '2020-09-24 03:13:06', '2020-09-24 03:13:06');
INSERT INTO `tb_article` VALUES ('1308970149998493696', '测试药品', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1279213430472966144', '## 二级标题\n测试', '1', '1600757488089_1308297845777891328.png', '1', '测试的', '说的-药品。', 0, '2020-09-24 03:22:58', '2020-09-24 03:37:46');
INSERT INTO `tb_article` VALUES ('1308977225252470784', 'java中使用elasticsearch实现高亮查询其实很简单', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1287260191263293440', '## 添加高亮查询\n**最后有完整代码奥**\n**红色框的地方不能少**\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200707103906132.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmdqaW5i,size_16,color_FFFFFF,t_70)\n## 核心思想，就是把原来的字段用高亮字段去替换\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200707104356867.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmdqaW5i,size_16,color_FFFFFF,t_70)\n**接下来就是把高亮字段替换掉原来map的字段**\n![在这里插入图片描述](https://img-blog.csdnimg.cn/20200707104548812.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3dhbmdqaW5i,size_16,color_FFFFFF,t_70)\n**直接上完整代码**\n```java\n    //2、从索引库获取数据实现搜索功能\n    public ResponseResult search(String keywords,int pageNo,int pageSize) throws IOException {\n        if (pageNo<=1){\n            pageNo=1;\n        }\n\n        //条件搜索\n        SearchRequest goods = new SearchRequest(\"ceshi\");\n        SearchSourceBuilder builder = new SearchSourceBuilder();\n        //分页\n        builder.from(pageNo);\n        builder.size(pageSize);\n\n        TermQueryBuilder title = QueryBuilders.termQuery(\"content\", keywords);\n        builder.query(title);\n        builder.timeout(new TimeValue(60, TimeUnit.SECONDS));\n\n        HighlightBuilder highlightBuilder = new HighlightBuilder();\n        highlightBuilder.field(\"content\");//高亮的字段\n        highlightBuilder.requireFieldMatch(false);//是否多个字段都高亮\n        highlightBuilder.preTags(\"<span style=\'color:red\'>\");//前缀后缀\n        highlightBuilder.postTags(\"</span>\");\n        builder.highlighter(highlightBuilder);\n\n        //执行搜索\n        goods.source(builder);\n        SearchResponse search = client.search(goods, RequestOptions.DEFAULT);\n        //解析结果\n        ArrayList<Map<String,Object>> list = new ArrayList<>();\n        for (SearchHit hit : search.getHits().getHits()) {\n            //解析高亮的字段\n            //获取高亮字段\n            Map<String, HighlightField> highlightFields = hit.getHighlightFields();\n            System.out.println(\"==========\"+highlightFields);\n            HighlightField content = highlightFields.get(\"content\");\n            System.out.println(\"==content==\"+content);\n            Map<String, Object> sourceAsMap = hit.getSourceAsMap();//原来的结果\n            //将原来的字段替换为高亮字段即可\n            if (content!=null){\n                Text[] fragments = content.fragments();\n                String newTitle = \"\";\n                for (Text text : fragments) {\n                    newTitle +=text;\n                }\n                sourceAsMap.put(\"content\",newTitle);//替换掉原来的内容\n            }\n           list.add(sourceAsMap);\n        }\n        return ResponseResult.success(\"搜索成功\").setData(list);\n\n    }\n```\n', '1', '1600919195758_1308976096905003008.png', '1', 'es实现高亮', 'es-java-高亮', 7, '2020-09-24 03:51:05', '2020-09-24 03:51:05');

-- ----------------------------
-- Table structure for tb_categories
-- ----------------------------
DROP TABLE IF EXISTS `tb_categories`;
CREATE TABLE `tb_categories`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `c_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '分类名称',
  `pinyin` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '拼音',
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '描述',
  `c_order` int(11) NOT NULL COMMENT '顺序',
  `c_status` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_categories
-- ----------------------------
INSERT INTO `tb_categories` VALUES ('1277804485682397184', '安卓1', '鞍座', '安卓宿舍', 1, '0', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1279213430472966144', '测试', 'string', 'string', 1, '1', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287259445914501120', 'vue2', '鞍座', 'vue', 7, '1', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287260191263293440', 'java', '鞍座', 'java的描述', 3, '1', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287260359173865472', '安卓1', '鞍座', '安卓的描述', 100, '0', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287261401965920256', '安卓1', '鞍座', '安卓宿舍', 1, '0', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287261461910913024', '安卓1', '鞍座', '安卓宿舍', 1, '0', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('1287282218040819712', '安卓1', '鞍座', '安卓宿舍', 1, '0', '2020-07-04 00:40:23', '2020-07-04 00:40:23');
INSERT INTO `tb_categories` VALUES ('13456', '安卓1', '鞍座', '安卓宿舍', 1, '1', '2020-07-04 00:40:23', '2020-07-04 00:40:23');

-- ----------------------------
-- Table structure for tb_comment
-- ----------------------------
DROP TABLE IF EXISTS `tb_comment`;
CREATE TABLE `tb_comment`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `parent_content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL COMMENT '父内容',
  `article_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '文章ID',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论内容',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '评论用户的ID',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论用户的头像',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '评论用户的名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表示正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_comment_on_user_id`(`user_id`) USING BTREE,
  INDEX `fk_article_comment_on_article_id`(`article_id`) USING BTREE,
  CONSTRAINT `fk_user_comment_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_comment
-- ----------------------------
INSERT INTO `tb_comment` VALUES ('1279934080636420096', NULL, '1279270699466227712', '文章写的太好了把', '1275431122951471104', 'demoData', 'wj', '1', '2020-07-06 00:24:00', '2020-07-06 00:24:00');
INSERT INTO `tb_comment` VALUES ('1280062339386703872', '这是抚平了', '1279270699466227712', '测试评论', '1275431122951471104', 'demoData', 'wj', '3', '2020-07-06 08:53:39', '2020-07-06 08:53:39');
INSERT INTO `tb_comment` VALUES ('1307475389626449920', NULL, '1306421503339790336', '服务降级可以使用哪些框架啊', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:23:19', '2020-09-20 00:23:19');
INSERT INTO `tb_comment` VALUES ('1307475681663254528', NULL, '1306421503339790336', '服务限流的操作是什么', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:24:29', '2020-09-20 00:24:29');
INSERT INTO `tb_comment` VALUES ('1307475783945551872', NULL, '1306421503339790336', 'springmvc的执行流程了解多少', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:24:53', '2020-09-20 00:24:53');
INSERT INTO `tb_comment` VALUES ('1307475890820612096', NULL, '1306421503339790336', 'mybatis的缓存分多赛季', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:25:19', '2020-09-20 00:25:19');
INSERT INTO `tb_comment` VALUES ('1307475953651286016', NULL, '1306421503339790336', '什么是多态', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:25:34', '2020-09-20 00:25:34');
INSERT INTO `tb_comment` VALUES ('1307475985641242624', NULL, '1306421503339790336', '什么是面向对象', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:25:41', '2020-09-20 00:25:41');
INSERT INTO `tb_comment` VALUES ('1307483249596956672', '1307475681663254528', '1306421503339790336', '测试回复', '1305796843149459456', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lwq', '1', '2020-09-20 00:54:33', '2020-09-20 00:54:33');
INSERT INTO `tb_comment` VALUES ('1308245020863627264', '', '1306421503339790336', '你的文章真的写的可以的', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 03:21:34', '2020-09-22 03:21:34');
INSERT INTO `tb_comment` VALUES ('1308245225616965632', '', '1306421503339790336', '事实上只要努力就可以得到想要的', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 03:22:22', '2020-09-22 03:22:22');
INSERT INTO `tb_comment` VALUES ('1308246719787433984', '', '1303938308362993664', 'java可以的', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 03:28:19', '2020-09-22 03:28:19');
INSERT INTO `tb_comment` VALUES ('1308246798833287168', '', '1303938308362993664', 'java可以的45', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 03:28:38', '2020-09-22 03:28:38');
INSERT INTO `tb_comment` VALUES ('1308247659378638848', '', '1303937854509940736', '123', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 03:32:03', '2020-09-22 03:32:03');
INSERT INTO `tb_comment` VALUES ('1308270711118233600', NULL, '1303937854509940736', '第二条评论', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:03:39', '2020-09-22 05:03:39');
INSERT INTO `tb_comment` VALUES ('1308271185842143232', NULL, '1306422259098845184', '这是始皇的评论', '1306050226351505408', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lcf', '1', '2020-09-22 05:05:32', '2020-09-22 05:05:32');
INSERT INTO `tb_comment` VALUES ('1308271889323393024', NULL, '1306422259098845184', '评论咯', '1306050226351505408', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lcf', '1', '2020-09-22 05:08:20', '2020-09-22 05:08:20');
INSERT INTO `tb_comment` VALUES ('1308274171238678528', NULL, '1303938134890774528', 'sdasd', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:17:24', '2020-09-22 05:17:24');
INSERT INTO `tb_comment` VALUES ('1308277259999641600', NULL, '1303938705240621056', 'asd', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:40', '2020-09-22 05:29:40');
INSERT INTO `tb_comment` VALUES ('1308277278295195648', NULL, '1303938705240621056', 'asdasd', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:44', '2020-09-22 05:29:44');
INSERT INTO `tb_comment` VALUES ('1308277289699508224', NULL, '1303938705240621056', 'asdsada', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:47', '2020-09-22 05:29:47');
INSERT INTO `tb_comment` VALUES ('1308277298511740928', NULL, '1303938705240621056', 'asdsad', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:49', '2020-09-22 05:29:49');
INSERT INTO `tb_comment` VALUES ('1308277316064903168', NULL, '1303938705240621056', 'asdsad', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:53', '2020-09-22 05:29:53');
INSERT INTO `tb_comment` VALUES ('1308277333446098944', NULL, '1303938705240621056', 'sadsa', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:29:58', '2020-09-22 05:29:58');
INSERT INTO `tb_comment` VALUES ('1308277354975461376', NULL, '1303938705240621056', 'asdsadasd', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:30:03', '2020-09-22 05:30:03');
INSERT INTO `tb_comment` VALUES ('1308281510930939904', NULL, '1306421503339790336', '第十条数据', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:46:34', '2020-09-22 05:46:34');
INSERT INTO `tb_comment` VALUES ('1308281557026340864', NULL, '1306421503339790336', '第十一条数据', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 05:46:45', '2020-09-22 05:46:45');
INSERT INTO `tb_comment` VALUES ('1308294694978977792', '事实上只要努力就可以得到想要的', '1306421503339790336', '回复努力就想得到的这个评论', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 06:38:57', '2020-09-22 06:38:57');
INSERT INTO `tb_comment` VALUES ('1308294820178952192', '第十一条数据', '1306421503339790336', '什么是一条数据啊，说些人话？？', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 06:39:27', '2020-09-22 06:39:27');
INSERT INTO `tb_comment` VALUES ('1308296158971428864', 'mybatis的缓存分多赛季', '1306421503339790336', '感谢你的支持', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 06:44:46', '2020-09-22 06:44:46');
INSERT INTO `tb_comment` VALUES ('1308296245097267200', '什么是多态', '1306421503339790336', '多态就是多种态度？？？', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-22 06:45:06', '2020-09-22 06:45:06');
INSERT INTO `tb_comment` VALUES ('1308644239785066496', NULL, '1308363749349916672', '感谢博主的代码没问题解决了\n', '1275431122951471104', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', 'wj', '1', '2020-09-23 05:47:55', '2020-09-23 05:47:55');
INSERT INTO `tb_comment` VALUES ('1308644517477351424', '感谢博主的代码没问题解决了\n', '1308363749349916672', '你是怎么添加的代码啊没我的为什么不行', '1306050226351505408', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lcf', '1', '2020-09-23 05:49:01', '2020-09-23 05:49:01');
INSERT INTO `tb_comment` VALUES ('1308645507047882752', '感谢博主的代码没问题解决了\n', '1308363749349916672', '我的已经可以了自己做好了', '1306050226351505408', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', 'lcf', '1', '2020-09-23 05:52:57', '2020-09-23 05:52:57');

-- ----------------------------
-- Table structure for tb_daily_view_count
-- ----------------------------
DROP TABLE IF EXISTS `tb_daily_view_count`;
CREATE TABLE `tb_daily_view_count`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `view_count` int(11) NOT NULL DEFAULT 0 COMMENT '每天浏览量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_daily_view_count
-- ----------------------------

-- ----------------------------
-- Table structure for tb_friends
-- ----------------------------
DROP TABLE IF EXISTS `tb_friends`;
CREATE TABLE `tb_friends`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `l_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接名称',
  `logo` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接logo',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接',
  `l_order` int(11) NOT NULL DEFAULT 0 COMMENT '顺序',
  `l_state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '友情链接状态:0表示不可用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_friends
-- ----------------------------
INSERT INTO `tb_friends` VALUES ('1302773506890530816', 'gitee', '/admin/images/1599440536766_1302774151580221440.png', 'http://www.gitee.com', 1, '1', '2020-09-07 00:59:43', '2020-09-07 00:59:43');
INSERT INTO `tb_friends` VALUES ('1302776047091056640', '百度', '/admin/images/1599440985070_1302776031903481856.png', 'http://www.baidu.com', 2, '1', '2020-09-07 01:09:49', '2020-09-07 01:09:49');
INSERT INTO `tb_friends` VALUES ('1302776383306465280', 'vue', '/admin/images/1599441065023_1302776367250669568.png', 'https://cn.vuejs.org/', 1, '1', '2020-09-07 01:11:09', '2020-09-07 01:11:09');
INSERT INTO `tb_friends` VALUES ('1302776706959933440', 'nuxt.js', '/admin/images/1599441144045_1302776698688765952.png', 'https://zh.nuxtjs.org/', 1, '1', '2020-09-07 01:12:26', '2020-09-07 01:12:26');
INSERT INTO `tb_friends` VALUES ('1302777005359497216', 'docker', '/admin/images/1599441212673_1302776986539655168.png', 'https://www.docker.com/', 1, '1', '2020-09-07 01:13:37', '2020-09-07 01:13:37');

-- ----------------------------
-- Table structure for tb_images
-- ----------------------------
DROP TABLE IF EXISTS `tb_images`;
CREATE TABLE `tb_images`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '路径',
  `content_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片类型',
  `i_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '原名称',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '存储路径',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态（0表示删除，1表正常）',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_user_images_on_user_id`(`user_id`) USING BTREE,
  CONSTRAINT `fk_user_images_on_user_id` FOREIGN KEY (`user_id`) REFERENCES `tb_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_images
-- ----------------------------
INSERT INTO `tb_images` VALUES ('1280066494998249472', '1275431122951471104', '1594026609534_1280066494998249472.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_06\\png\\1280066494998249472.png', '1', '2020-07-06 09:10:10', '2020-07-06 09:10:10');
INSERT INTO `tb_images` VALUES ('1280066569241624576', '1275431122951471104', '1594026627251_1280066569241624576.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_06\\png\\1280066569241624576.png', '1', '2020-07-06 09:10:27', '2020-07-06 09:10:27');
INSERT INTO `tb_images` VALUES ('1280066592553566208', '1275431122951471104', '1594026632808_1280066592553566208.gif', 'image/gif', 'wwjj.gif', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_06\\gif\\1280066592553566208.gif', '1', '2020-07-06 09:10:33', '2020-07-06 09:10:33');
INSERT INTO `tb_images` VALUES ('1287340303358885888', '1275431122951471104', '1595760820602_1287340303358885888.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287340303358885888.png', '1', '2020-07-26 10:53:41', '2020-07-26 10:53:41');
INSERT INTO `tb_images` VALUES ('1287379253603598336', '1275431122951471104', '1595770107066_1287379253603598336.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287379253603598336.png', '1', '2020-07-26 13:28:27', '2020-07-26 13:28:27');
INSERT INTO `tb_images` VALUES ('1287380948073054208', '1275431122951471104', '1595770511059_1287380948073054208.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287380948073054208.png', '1', '2020-07-26 13:35:11', '2020-07-26 13:35:11');
INSERT INTO `tb_images` VALUES ('1287380999461666816', '1275431122951471104', '1595770523311_1287380999461666816.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287380999461666816.png', '1', '2020-07-26 13:35:23', '2020-07-26 13:35:23');
INSERT INTO `tb_images` VALUES ('1287381184602439680', '1275431122951471104', '1595770567452_1287381184602439680.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287381184602439680.png', '1', '2020-07-26 13:36:07', '2020-07-26 13:36:07');
INSERT INTO `tb_images` VALUES ('1287381255154827264', '1275431122951471104', '1595770584272_1287381255154827264.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287381255154827264.png', '1', '2020-07-26 13:36:24', '2020-07-26 13:36:24');
INSERT INTO `tb_images` VALUES ('1287381349761548288', '1275431122951471104', '1595770606829_1287381349761548288.png', 'image/png', 'favicon.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287381349761548288.png', '1', '2020-07-26 13:36:47', '2020-07-26 13:36:47');
INSERT INTO `tb_images` VALUES ('1287382355132022784', '1275431122951471104', '1595770846528_1287382355132022784.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287382355132022784.png', '1', '2020-07-26 13:40:47', '2020-07-26 13:40:47');
INSERT INTO `tb_images` VALUES ('1287384703745130496', '1275431122951471104', '1595771406481_1287384703745130496.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287384703745130496.png', '1', '2020-07-26 13:50:06', '2020-07-26 13:50:06');
INSERT INTO `tb_images` VALUES ('1287386031154266112', '1275431122951471104', '1595771722959_1287386031154266112.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287386031154266112.png', '1', '2020-07-26 13:55:23', '2020-07-26 13:55:23');
INSERT INTO `tb_images` VALUES ('1287386962210062336', '1275431122951471104', '1595771944940_1287386962210062336.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287386962210062336.png', '1', '2020-07-26 13:59:05', '2020-07-26 13:59:05');
INSERT INTO `tb_images` VALUES ('1287387210739351552', '1275431122951471104', '1595772004195_1287387210739351552.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287387210739351552.png', '1', '2020-07-26 14:00:04', '2020-07-26 14:00:04');
INSERT INTO `tb_images` VALUES ('1287388905162670080', '1275431122951471104', '1595772408176_1287388905162670080.gif', 'image/gif', 'wwjj.gif', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\gif\\1287388905162670080.gif', '1', '2020-07-26 14:06:48', '2020-07-26 14:06:48');
INSERT INTO `tb_images` VALUES ('1287388938993926144', '1275431122951471104', '1595772416242_1287388938993926144.png', 'image/png', 'favicon.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287388938993926144.png', '1', '2020-07-26 14:06:56', '2020-07-26 14:06:56');
INSERT INTO `tb_images` VALUES ('1287390238582243328', '1275431122951471104', '1595772726088_1287390238582243328.png', 'image/png', 'favicon.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_26\\png\\1287390238582243328.png', '1', '2020-07-26 14:12:06', '2020-07-26 14:12:06');
INSERT INTO `tb_images` VALUES ('1287723798342139904', '1275431122951471104', '1595852252932_1287723798342139904.gif', 'image/gif', 'wwjj.gif', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_27\\gif\\1287723798342139904.gif', '1', '2020-07-27 12:17:33', '2020-07-27 12:17:33');
INSERT INTO `tb_images` VALUES ('1287728299946016768', '1275431122951471104', '1595853326198_1287728299946016768.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_07_27\\png\\1287728299946016768.png', '1', '2020-07-27 12:35:26', '2020-07-27 12:35:26');
INSERT INTO `tb_images` VALUES ('1289903022687649792', '1275431122951471104', '1596371820499_1289903022687649792.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289903022687649792.png', '1', '2020-08-02 12:37:01', '2020-08-02 12:37:01');
INSERT INTO `tb_images` VALUES ('1289904057258868736', '1275431122951471104', '1596372067165_1289904057258868736.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289904057258868736.png', '1', '2020-08-02 12:41:07', '2020-08-02 12:41:07');
INSERT INTO `tb_images` VALUES ('1289905081960890368', '1275431122951471104', '1596372311473_1289905081960890368.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289905081960890368.png', '1', '2020-08-02 12:45:11', '2020-08-02 12:45:11');
INSERT INTO `tb_images` VALUES ('1289905581183729664', '1275431122951471104', '1596372430498_1289905581183729664.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289905581183729664.png', '1', '2020-08-02 12:47:11', '2020-08-02 12:47:11');
INSERT INTO `tb_images` VALUES ('1289905726579277824', '1275431122951471104', '1596372465162_1289905726579277824.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289905726579277824.png', '1', '2020-08-02 12:47:45', '2020-08-02 12:47:45');
INSERT INTO `tb_images` VALUES ('1289905907131482112', '1275431122951471104', '1596372508209_1289905907131482112.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289905907131482112.png', '1', '2020-08-02 12:48:28', '2020-08-02 12:48:28');
INSERT INTO `tb_images` VALUES ('1289906512868671488', '1275431122951471104', '1596372652628_1289906512868671488.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289906512868671488.png', '1', '2020-08-02 12:50:53', '2020-08-02 12:50:53');
INSERT INTO `tb_images` VALUES ('1289907139749347328', '1275431122951471104', '1596372802088_1289907139749347328.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289907139749347328.png', '1', '2020-08-02 12:53:22', '2020-08-02 12:53:22');
INSERT INTO `tb_images` VALUES ('1289907754042916864', '1275431122951471104', '1596372948547_1289907754042916864.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289907754042916864.png', '1', '2020-08-02 12:55:49', '2020-08-02 12:55:49');
INSERT INTO `tb_images` VALUES ('1289907795340034048', '1275431122951471104', '1596372958394_1289907795340034048.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289907795340034048.png', '1', '2020-08-02 12:55:58', '2020-08-02 12:55:58');
INSERT INTO `tb_images` VALUES ('1289910442528866304', '1275431122951471104', '1596373589532_1289910442528866304.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289910442528866304.png', '1', '2020-08-02 13:06:30', '2020-08-02 13:06:30');
INSERT INTO `tb_images` VALUES ('1289911486663098368', '1275431122951471104', '1596373838474_1289911486663098368.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289911486663098368.png', '1', '2020-08-02 13:10:38', '2020-08-02 13:10:38');
INSERT INTO `tb_images` VALUES ('1289913055706087424', '1275431122951471104', '1596374212562_1289913055706087424.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289913055706087424.png', '1', '2020-08-02 13:16:53', '2020-08-02 13:16:53');
INSERT INTO `tb_images` VALUES ('1289918502769000448', '1275431122951471104', '1596375511244_1289918502769000448.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289918502769000448.png', '1', '2020-08-02 13:38:31', '2020-08-02 13:38:31');
INSERT INTO `tb_images` VALUES ('1289919939129376768', '1275431122951471104', '1596375853698_1289919939129376768.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289919939129376768.png', '1', '2020-08-02 13:44:14', '2020-08-02 13:44:14');
INSERT INTO `tb_images` VALUES ('1289921422382071808', '1275431122951471104', '1596376207333_1289921422382071808.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_02\\png\\1289921422382071808.png', '1', '2020-08-02 13:50:07', '2020-08-02 13:50:07');
INSERT INTO `tb_images` VALUES ('1290278906447790080', '1275431122951471104', '1596461438173_1290278906447790080.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_03\\png\\1290278906447790080.png', '1', '2020-08-03 13:30:38', '2020-08-03 13:30:38');
INSERT INTO `tb_images` VALUES ('1291660390786138112', '1275431122951471104', '1596790809706_1291660390786138112.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_07\\png\\1291660390786138112.png', '1', '2020-08-07 09:00:10', '2020-08-07 09:00:10');
INSERT INTO `tb_images` VALUES ('1291662329947095040', '1275431122951471104', '1596791272041_1291662329947095040.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_07\\png\\1291662329947095040.png', '1', '2020-08-07 09:07:52', '2020-08-07 09:07:52');
INSERT INTO `tb_images` VALUES ('1291665494142615552', '1275431122951471104', '1596792026445_1291665494142615552.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_07\\png\\1291665494142615552.png', '1', '2020-08-07 09:20:26', '2020-08-07 09:20:26');
INSERT INTO `tb_images` VALUES ('1291667067925168128', '1275431122951471104', '1596792401664_1291667067925168128.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_07\\png\\1291667067925168128.png', '1', '2020-08-07 09:26:42', '2020-08-07 09:26:42');
INSERT INTO `tb_images` VALUES ('1291667803044052992', '1275431122951471104', '1596792576929_1291667803044052992.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_07\\png\\1291667803044052992.png', '1', '2020-08-07 09:29:37', '2020-08-07 09:29:37');
INSERT INTO `tb_images` VALUES ('1293002977061109760', '1275431122951471104', '1597110907219_1293002977061109760.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_11\\png\\1293002977061109760.png', '1', '2020-08-11 01:55:07', '2020-08-11 01:55:07');
INSERT INTO `tb_images` VALUES ('1294241797781848064', '1275431122951471104', '1597406265095_1294241797781848064.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_14\\png\\1294241797781848064.png', '1', '2020-08-14 11:57:45', '2020-08-14 11:57:45');
INSERT INTO `tb_images` VALUES ('1294252185990725632', '1275431122951471104', '1597408741839_1294252185990725632.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_14\\png\\1294252185990725632.png', '1', '2020-08-14 12:39:02', '2020-08-14 12:39:02');
INSERT INTO `tb_images` VALUES ('1294252314550337536', '1275431122951471104', '1597408772490_1294252314550337536.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_14\\png\\1294252314550337536.png', '1', '2020-08-14 12:39:32', '2020-08-14 12:39:32');
INSERT INTO `tb_images` VALUES ('1295211258433241088', '1275431122951471104', '1597637402528_1295211258433241088.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_17\\png\\1295211258433241088.png', '1', '2020-08-17 04:10:03', '2020-08-17 04:10:03');
INSERT INTO `tb_images` VALUES ('1295213589921005568', '1275431122951471104', '1597637958399_1295213589921005568.gif', 'image/gif', 'wwjj.gif', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_17\\gif\\1295213589921005568.gif', '1', '2020-08-17 04:19:18', '2020-08-17 04:19:18');
INSERT INTO `tb_images` VALUES ('1295213615829221376', '1275431122951471104', '1597637964575_1295213615829221376.png', 'image/png', 'log.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_17\\png\\1295213615829221376.png', '1', '2020-08-17 04:19:25', '2020-08-17 04:19:25');
INSERT INTO `tb_images` VALUES ('1295213946260684800', '1275431122951471104', '1597638043356_1295213946260684800.png', 'image/png', 'login.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_17\\png\\1295213946260684800.png', '1', '2020-08-17 04:20:43', '2020-08-17 04:20:43');
INSERT INTO `tb_images` VALUES ('1295217703685980160', '1275431122951471104', '1597638939196_1295217703685980160.gif', 'image/gif', 'wwjj.gif', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_08_17\\gif\\1295217703685980160.gif', '1', '2020-08-17 04:35:39', '2020-08-17 04:35:39');
INSERT INTO `tb_images` VALUES ('1302773490700517376', '1275431122951471104', '1599440379195_1302773490700517376.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302773490700517376.png', '1', '2020-09-07 00:59:39', '2020-09-07 00:59:39');
INSERT INTO `tb_images` VALUES ('1302773924290887680', '1275431122951471104', '1599440482577_1302773924290887680.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302773924290887680.png', '1', '2020-09-07 01:01:23', '2020-09-07 01:01:23');
INSERT INTO `tb_images` VALUES ('1302774151580221440', '1275431122951471104', '1599440536766_1302774151580221440.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302774151580221440.png', '1', '2020-09-07 01:02:17', '2020-09-07 01:02:17');
INSERT INTO `tb_images` VALUES ('1302776031903481856', '1275431122951471104', '1599440985070_1302776031903481856.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302776031903481856.png', '1', '2020-09-07 01:09:45', '2020-09-07 01:09:45');
INSERT INTO `tb_images` VALUES ('1302776367250669568', '1275431122951471104', '1599441065023_1302776367250669568.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302776367250669568.png', '1', '2020-09-07 01:11:05', '2020-09-07 01:11:05');
INSERT INTO `tb_images` VALUES ('1302776698688765952', '1275431122951471104', '1599441144045_1302776698688765952.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302776698688765952.png', '1', '2020-09-07 01:12:24', '2020-09-07 01:12:24');
INSERT INTO `tb_images` VALUES ('1302776986539655168', '1275431122951471104', '1599441212673_1302776986539655168.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_07\\png\\1302776986539655168.png', '1', '2020-09-07 01:13:33', '2020-09-07 01:13:33');
INSERT INTO `tb_images` VALUES ('1308297845777891328', '1275431122951471104', '1600757488089_1308297845777891328.png', 'image/png', 'file.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_22\\png\\1308297845777891328.png', '1', '2020-09-22 06:51:28', '2020-09-22 06:51:28');
INSERT INTO `tb_images` VALUES ('1308975873952579584', '1275431122951471104', '1600919142601_1308975873952579584.png', 'image/png', 'c1a38f11dfc534dee5124d9ea0224eb316093.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_24\\png\\1308975873952579584.png', '1', '2020-09-24 03:45:43', '2020-09-24 03:45:43');
INSERT INTO `tb_images` VALUES ('1308976096905003008', '1275431122951471104', '1600919195758_1308976096905003008.png', 'image/png', 'c1a38f11dfc534dee5124d9ea0224eb316093.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_24\\png\\1308976096905003008.png', '1', '2020-09-24 03:46:36', '2020-09-24 03:46:36');
INSERT INTO `tb_images` VALUES ('1308976560681779200', '1275431122951471104', '1600919306332_1308976560681779200.png', 'image/png', '20170123073446556.png', 'C:\\IDEA\\sob_blog_system_mp\\src\\main\\resources\\static\\images\\2020_09_24\\png\\1308976560681779200.png', '1', '2020-09-24 03:48:26', '2020-09-24 03:48:26');

-- ----------------------------
-- Table structure for tb_labels
-- ----------------------------
DROP TABLE IF EXISTS `tb_labels`;
CREATE TABLE `tb_labels`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标签名称',
  `count` int(11) NOT NULL DEFAULT 0 COMMENT '数量',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_labels
-- ----------------------------
INSERT INTO `tb_labels` VALUES ('1279612282308919296', '沙雕博客', 1, '2020-07-05 03:05:17', '2020-07-05 03:05:17');
INSERT INTO `tb_labels` VALUES ('1279612282459914240', 'java', 18, '2020-07-05 03:05:17', '2020-07-05 03:05:17');
INSERT INTO `tb_labels` VALUES ('1279612282480885760', '高并发', 1, '2020-07-05 03:05:17', '2020-07-05 03:05:17');
INSERT INTO `tb_labels` VALUES ('1279622825451192320', 'java', 17, '2020-07-05 03:47:10', '2020-07-05 03:47:10');
INSERT INTO `tb_labels` VALUES ('1279622825551855616', 'c', 4, '2020-07-05 03:47:11', '2020-07-05 03:47:11');
INSERT INTO `tb_labels` VALUES ('1279622825572827136', 'c++', 2, '2020-07-05 03:47:11', '2020-07-05 03:47:11');
INSERT INTO `tb_labels` VALUES ('1279624757251145728', '博客', 1, '2020-07-05 03:54:51', '2020-07-05 03:54:51');
INSERT INTO `tb_labels` VALUES ('1280383941567578112', 'es', 10, '2020-07-07 06:11:35', '2020-07-07 06:11:35');
INSERT INTO `tb_labels` VALUES ('1280383941609521152', '缓存', 4, '2020-07-07 06:11:35', '2020-07-07 06:11:35');
INSERT INTO `tb_labels` VALUES ('1280383941647269888', '测试', 12, '2020-07-07 06:11:35', '2020-07-07 06:11:35');
INSERT INTO `tb_labels` VALUES ('1280405364608598016', '帅', 10, '2020-07-07 07:36:42', '2020-07-07 07:36:42');
INSERT INTO `tb_labels` VALUES ('1280405364717649920', '王吉', 10, '2020-07-07 07:36:42', '2020-07-07 07:36:42');
INSERT INTO `tb_labels` VALUES ('1280405364738621440', '好看', 10, '2020-07-07 07:36:42', '2020-07-07 07:36:42');
INSERT INTO `tb_labels` VALUES ('1295701816289263616', 'undefinedjava', 7, '2020-08-18 12:39:21', '2020-08-18 12:39:21');
INSERT INTO `tb_labels` VALUES ('1295701816377344000', 'vue', 8, '2020-08-18 12:39:21', '2020-08-18 12:39:21');
INSERT INTO `tb_labels` VALUES ('1295701816423481344', 'html', 6, '2020-08-18 12:39:21', '2020-08-18 12:39:21');
INSERT INTO `tb_labels` VALUES ('1297493034564321280', '', 5, '2020-08-23 11:17:00', '2020-08-23 11:17:00');
INSERT INTO `tb_labels` VALUES ('1303678976882900992', '刷新', 3, '2020-09-09 12:57:44', '2020-09-09 12:57:44');
INSERT INTO `tb_labels` VALUES ('1303678976945815552', '排序', 3, '2020-09-09 12:57:44', '2020-09-09 12:57:44');
INSERT INTO `tb_labels` VALUES ('1303681193564176384', '第三条', 1, '2020-09-09 13:06:32', '2020-09-09 13:06:32');
INSERT INTO `tb_labels` VALUES ('1303681193631285248', '我来了', 1, '2020-09-09 13:06:32', '2020-09-09 13:06:32');
INSERT INTO `tb_labels` VALUES ('1303937854606409728', '文章', 1, '2020-09-10 06:06:25', '2020-09-10 06:06:25');
INSERT INTO `tb_labels` VALUES ('1303938134920134656', '安卓', 1, '2020-09-10 06:07:32', '2020-09-10 06:07:32');
INSERT INTO `tb_labels` VALUES ('1303938134966272000', '密码', 1, '2020-09-10 06:07:32', '2020-09-10 06:07:32');
INSERT INTO `tb_labels` VALUES ('1303938308472045568', '调优', 1, '2020-09-10 06:08:13', '2020-09-10 06:08:13');
INSERT INTO `tb_labels` VALUES ('1306412580616536064', '高亮', 2, '2020-09-17 02:00:06', '2020-09-17 02:00:06');
INSERT INTO `tb_labels` VALUES ('1306412580637507584', '查询', 1, '2020-09-17 02:00:06', '2020-09-17 02:00:06');
INSERT INTO `tb_labels` VALUES ('1306421503402704896', '微服务', 1, '2020-09-17 02:35:33', '2020-09-17 02:35:33');
INSERT INTO `tb_labels` VALUES ('1306421503432065024', 'sentinel', 1, '2020-09-17 02:35:33', '2020-09-17 02:35:33');
INSERT INTO `tb_labels` VALUES ('1306421503448842240', 'spring', 1, '2020-09-17 02:35:33', '2020-09-17 02:35:33');
INSERT INTO `tb_labels` VALUES ('1306422259107233792', '服务降级', 1, '2020-09-17 02:38:33', '2020-09-17 02:38:33');
INSERT INTO `tb_labels` VALUES ('1306422259174342656', '限流', 1, '2020-09-17 02:38:33', '2020-09-17 02:38:33');
INSERT INTO `tb_labels` VALUES ('1308362780293726208', '轮询', 2, '2020-09-22 11:09:30', '2020-09-22 11:09:30');
INSERT INTO `tb_labels` VALUES ('1308362780310503424', 'nacous', 2, '2020-09-22 11:09:30', '2020-09-22 11:09:30');
INSERT INTO `tb_labels` VALUES ('1308363749362499584', 'maven', 6, '2020-09-22 11:13:21', '2020-09-22 11:13:21');
INSERT INTO `tb_labels` VALUES ('1308363749379276800', '静态资源', 5, '2020-09-22 11:13:21', '2020-09-22 11:13:21');
INSERT INTO `tb_labels` VALUES ('1308363749396054016', '过滤', 3, '2020-09-22 11:13:21', '2020-09-22 11:13:21');
INSERT INTO `tb_labels` VALUES ('1308946508594282496', 'mybatis', 1, '2020-09-24 01:49:01', '2020-09-24 01:49:01');
INSERT INTO `tb_labels` VALUES ('1308950632769519616', 'ces', 2, '2020-09-24 02:05:25', '2020-09-24 02:05:25');
INSERT INTO `tb_labels` VALUES ('1308962436363059200', '的撒', 2, '2020-09-24 02:52:19', '2020-09-24 02:52:19');
INSERT INTO `tb_labels` VALUES ('1308967668040073216', '阿斯顿', 1, '2020-09-24 03:13:06', '2020-09-24 03:13:06');
INSERT INTO `tb_labels` VALUES ('1308970150002688000', '说的', 4, '2020-09-24 03:22:58', '2020-09-24 03:22:58');
INSERT INTO `tb_labels` VALUES ('1308970238372478976', '药品。', 2, '2020-09-24 03:23:19', '2020-09-24 03:23:19');

-- ----------------------------
-- Table structure for tb_looper
-- ----------------------------
DROP TABLE IF EXISTS `tb_looper`;
CREATE TABLE `tb_looper`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '轮播图标题',
  `l_order` int(11) NOT NULL DEFAULT 0 COMMENT '顺序',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示不可用，1表示正常',
  `target_url` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '目标URL',
  `image_url` varchar(2014) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '图片地址',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_looper
-- ----------------------------
INSERT INTO `tb_looper` VALUES ('1287390247423836160', '测试四s', 1, '1', 'http://www.wwjjbt.vip/', 'http://localhost:8090/portal/image/1595772726088_1287390238582243328.png', '2020-07-26 14:12:08', '2020-07-27 12:28:07');
INSERT INTO `tb_looper` VALUES ('1287728302236106752', 'ss', 1, '1', 'http://sss', 'http://localhost:8090/portal/image/1595853326198_1287728299946016768.png', '2020-07-27 12:35:27', '2020-07-27 12:35:27');
INSERT INTO `tb_looper` VALUES ('1308976106015031296', '轮播', 1, '1', 'http://www', 'http://localhost:8090/portal/image/1600919195758_1308976096905003008.png', '2020-09-24 03:46:38', '2020-09-24 03:46:38');
INSERT INTO `tb_looper` VALUES ('1308976576108429312', 'ceshi', 1, '1', 'http://www.baidu.com', 'http://localhost:8090/portal/image/1600919306332_1308976560681779200.png', '2020-09-24 03:48:30', '2020-09-24 03:48:30');

-- ----------------------------
-- Table structure for tb_lost_article
-- ----------------------------
DROP TABLE IF EXISTS `tb_lost_article`;
CREATE TABLE `tb_lost_article`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '标题',
  `category_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类id',
  `summary` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `type` int(11) NULL DEFAULT NULL COMMENT '0 全部 1丢失 2拾到',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '失物内容',
  `cover` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '失物图片',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名称',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（1表示已发布，2表示草稿，3表示删除）',
  `view_count` int(11) NULL DEFAULT NULL COMMENT '浏览量',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_lost_article
-- ----------------------------

-- ----------------------------
-- Table structure for tb_lost_categories
-- ----------------------------
DROP TABLE IF EXISTS `tb_lost_categories`;
CREATE TABLE `tb_lost_categories`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `l_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '分类名称',
  `pinyin` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '拼音',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '描述',
  `order` int(11) NULL DEFAULT NULL COMMENT '排序',
  `status` int(11) NULL DEFAULT NULL COMMENT '状态：0表示不使用，1表示正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_lost_categories
-- ----------------------------

-- ----------------------------
-- Table structure for tb_lost_post
-- ----------------------------
DROP TABLE IF EXISTS `tb_lost_post`;
CREATE TABLE `tb_lost_post`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'id',
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子标题',
  `summary` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子描述',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '帖子内容',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户名',
  `user_avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像',
  `view_count` int(11) NULL DEFAULT NULL COMMENT '浏览',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '状态（1表示已发布，2表示草稿，3表示删除）',
  `top` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '0普通  1置顶',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime(0) NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_lost_post
-- ----------------------------

-- ----------------------------
-- Table structure for tb_refresh_token
-- ----------------------------
DROP TABLE IF EXISTS `tb_refresh_token`;
CREATE TABLE `tb_refresh_token`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `refresh_token` text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'refreshToken',
  `user_id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `mobile_token_key` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '移动端的token_key',
  `token_key` varchar(36) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'token_key',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_refresh_token
-- ----------------------------
INSERT INTO `tb_refresh_token` VALUES ('1277616544582467584', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6IjEyNzcyNDQ0MTY0NDgyNjYyNDAiLCJpYXQiOjE1OTM0NDI0OTUsImV4cCI6MTU5MzUyMDI1NX0.jqNkyQQYjpEgusMDe7eCueIVrNR2ip0UPmtHmo-P5uc', '1277244416448266240', NULL, '52776a364ae92d37db90bd9306224b34', '2020-06-29 14:54:56', '2020-06-29 14:54:56');
INSERT INTO `tb_refresh_token` VALUES ('1287011695000027136', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6IjEyNzcyNDQ0MTY0NDgyNjYyNDEiLCJpYXQiOjE1OTU2ODI0NzQsImV4cCI6MTU5NTc2MDIzNH0.tKDBRVpUvT9jttt133gj4_R18pDzMZ1WeNhhmZwz3sI', '1277244416448266241', NULL, '35b01f143b2d19122dcdc1061a4fae0e', '2020-07-25 13:07:54', '2020-07-25 13:07:54');
INSERT INTO `tb_refresh_token` VALUES ('1308644356420272128', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6IjEzMDYwNTAyMjYzNTE1MDU0MDgiLCJpYXQiOjE2MDA4NDAxMDIsImV4cCI6MTYwMDkxNzg2Mn0.H-4t23IjRVpzJHL4imO9548L61Z-32sM3ftbhUtDNd8', '1306050226351505408', NULL, '9d3167d132a80705928b494f7391fa38', '2020-09-23 05:48:23', '2020-09-23 05:48:23');
INSERT INTO `tb_refresh_token` VALUES ('1312271738872004608', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJvbmVoZWUiLCJpZCI6IjEyNzU0MzExMjI5NTE0NzExMDQiLCJpYXQiOjE2MDE3MDQ5MzgsImV4cCI6MTYwMTc4MjY5OH0.4PIYKiVQZWAa1i0Kw5Sm1WC5Oks0DXEn1DxOPNmCAJE', '1275431122951471104', NULL, '7d06bf9b7015fde0a790d08e650455b0', '2020-10-03 06:02:18', '2020-10-03 06:02:18');

-- ----------------------------
-- Table structure for tb_settings
-- ----------------------------
DROP TABLE IF EXISTS `tb_settings`;
CREATE TABLE `tb_settings`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '键',
  `value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '值',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_settings
-- ----------------------------
INSERT INTO `tb_settings` VALUES ('1275084546986999808', 'MANAGER_ACCOUNT_INIT_STATE', '1', '2020-06-22 15:13:41', '2020-06-22 15:13:41');
INSERT INTO `tb_settings` VALUES ('1278849614476214272', 'web_size_title', '欢迎来到王吉的人人博客奥s', '2020-07-03 00:34:43', '2020-07-03 00:34:43');
INSERT INTO `tb_settings` VALUES ('1278852195038527488', 'web_size_description', '很荣幸使用人人博客文件', '2020-07-03 00:44:58', '2020-07-03 00:44:58');
INSERT INTO `tb_settings` VALUES ('1278852195122413568', 'web_size_keywords', 'vue,java,html,php', '2020-07-03 00:44:58', '2020-07-03 00:44:58');
INSERT INTO `tb_settings` VALUES ('1280007153007984640', 'web_size_view_count', '3', '2020-07-06 05:14:21', '2020-07-06 05:14:21');

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user`  (
  `id` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'ID',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `roles` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色',
  `avatar` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '头像地址',
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `sign` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '签名',
  `state` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '状态：0表示删除，1表示正常',
  `reg_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册ip',
  `login_ip` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '登录Ip',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1275431122951471104', 'wj', '$2a$10$.oiBl2bIK.nbhHbuuWe3JO58X8XVQXesuWxz6itqXN/w6/3o/eHS2', 'role_admin', 'http://localhost:8090/admin/images/1596461438173_1290278906447790080.png', '2354879324@qq.com', '我的签名s', '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-06-23 14:10:51', '2020-10-03 07:49:53');
INSERT INTO `tb_user` VALUES ('1277244416448261234', '测试注册2', '$2a$10$wPbLthjzF2.rZkSdDegEpuRHfRglarGnccebDjNQvuUVo5GSnBNaO', 'role_normal', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', '2354879325@qq.com', NULL, '1', '0:0:0:0:0:0:0:1', '0:0:0:0:0:0:0:1', '2020-06-28 14:16:14', '2020-09-16 19:26:20');
INSERT INTO `tb_user` VALUES ('1305796843149459456', 'lwq', '$2a$10$zcnPzzCoxNNg2/W.JMHR2.2pZ9cTF7aEcTdWqDcPFSXso8NZXkrta', 'role_normal', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', '2250990651@qq.com', NULL, '1', '127.0.0.1', '127.0.0.1', '2020-09-15 09:13:23', '2020-09-15 09:13:23');
INSERT INTO `tb_user` VALUES ('1306050226351505408', 'lcf', '$2a$10$450QzCxkZPBNaJmB7JB/juvzJSZdO8cGkGxloZ1/JvXZtxqUvA/ua', 'role_normal', 'https://upload.jianshu.io/users/upload_avatars/8508242/e9dfee8f-0370-499a-acfd-e3b545255c63?imageMogr2/auto-orient/strip|imageView2/1/w/80/h/80/format/webp', '1291062649@qq.com', NULL, '1', '127.0.0.1', '127.0.0.1', '2020-09-16 02:00:14', '2020-09-16 02:00:14');

SET FOREIGN_KEY_CHECKS = 1;
