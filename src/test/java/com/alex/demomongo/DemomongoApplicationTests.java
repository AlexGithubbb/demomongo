package com.alex.demomongo;

import com.alex.demomongo.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
class DemomongoApplicationTests {

    @Autowired
    MongoTemplate mongoTemplate;

    /**
     *     private String id;
     *     private String name;
     *     private Integer age;
     *     private String email;
     *     private String createDate;
     */
    // insert new document
    @Test
    public void insert(){
        User user = new User();
        user.setName("pangmei");
        user.setAge(35);
        user.setEmail("pm35@gmail.com");
        mongoTemplate.insert(user);
        System.out.println(user);
    }

    // find all documents(records)
    @Test
    public void findUser(){
        List<User> all = mongoTemplate.findAll(User.class);
        for (User user : all) {
            System.out.println(user);
        }
    }

    @Test
    public void findById(){
        User user = mongoTemplate.findById("60a16b76bf0f975c6f7f2489", User.class);
        System.out.println(user);
    }

    @Test
    public void queryByCondition(){
        Query query = new Query(Criteria.where("name").is("coco").and("age").is(25));
        List<User> users = mongoTemplate.find(query, User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }


    // 模糊查询
    @Test
    public void testLike(){
        String name = "ex";
        String regex = String.format("%s%s%s", "^.*", name, ".*$");
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> users = mongoTemplate.find(query, User.class);
        for (User user : users) {
            System.out.println(user);
        }
    }

    // 分页查询
    @Test
    public void queryByPage(){
        Integer pageNo=2;
        Integer pageSize=2;

        Query query = new Query();
        // 分页构建
        // 总记录数
        long count = mongoTemplate.count(query, User.class);
        // 当前页记录
        List<User> users = mongoTemplate.find(query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
        System.out.println(count);
        System.out.println(users);

    }


    @Test
    public void update(){
//        60a170ed2e14fd0faeb4c990

        User user = mongoTemplate.findById("60a170ed2e14fd0faeb4c990", User.class);
        user.setAge(23);
        user.setEmail("rexnew@gmail.com");

        Query query = new Query(Criteria.where("_id").is(user.getId()));

        Update update = new Update();
        update.set("age", user.getAge());
        update.set("email", user.getEmail());

        UpdateResult result = mongoTemplate.upsert(query, update, User.class);
        long modifiedCount = result.getModifiedCount();
        System.out.println("modified count: " + modifiedCount);
    }


    @Test
    public void delete(){
        Query query = new Query(Criteria.where("name").is("pangmei"));
        DeleteResult result = mongoTemplate.remove(query, User.class);
        long deletedCount = result.getDeletedCount();
        System.out.println(deletedCount);
    }










}
