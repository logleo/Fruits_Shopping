package com.example.shop.dao;

import com.example.shop.model.CarOrderBean;
import com.example.shop.model.FinishOrderBean;
import com.example.shop.model.ProductInfoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.List;

@Repository
public class UserShopdao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ProductInfoBean> getallproduct(){
        String sql="select * from productinfo";
        System.out.println(sql);
        return jdbcTemplate.query(sql,new Object[]{},
                new BeanPropertyRowMapper<ProductInfoBean>(ProductInfoBean.class));
    }

    public List<ProductInfoBean> getanyproduct(int min_id,int max_id,String name,String origin,float min_price,float max_price){
        String sql="select * from productinfo where productid>=? and productid<=? and " +
                "name like ? and origin like ? and price>= ? and price<= ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql,new Object[]{min_id,max_id,name,origin,min_price,max_price},
                new BeanPropertyRowMapper<ProductInfoBean>(ProductInfoBean.class));
    }

    public List<CarOrderBean> getallcarorder(String userid){
        String sql="select * from carorder where userid=?";
        return jdbcTemplate.query(sql,new Object[]{userid},
                new BeanPropertyRowMapper<CarOrderBean>(CarOrderBean.class));
    }

    public int deleteorder(int orderid){
        String sql="delete from carorder where orderid = "+orderid;
        System.out.println(sql);
        return jdbcTemplate.update(sql);
    }

    public int addfinishorder(int orderid,String userid){
        String sql="insert into finishorder(finishorderid,productid,userid,name,buynum,allprice) " +
                "values(?,?,?,?,?,?)";
        int productid=jdbcTemplate.queryForObject("select productid from carorder where orderid=?"
                ,new Object[]{orderid},Integer.class);
        String name=jdbcTemplate.queryForObject("select name from carorder where orderid=?"
                ,new Object[]{orderid},String.class);
        int buynum=jdbcTemplate.queryForObject("select buynum from carorder where orderid=?"
                ,new Object[]{orderid},Integer.class);
        float allprice=jdbcTemplate.queryForObject("select allprice from carorder where orderid=?"
                ,new Object[]{orderid},Float.class);
        return jdbcTemplate.update(sql,orderid,productid,userid,name,buynum,allprice);
    }

    public List<FinishOrderBean> getallfinishorder(String userid){
        String sql="select * from finishorder where userid=?";
        return jdbcTemplate.query(sql,new Object[]{userid},
                new BeanPropertyRowMapper<FinishOrderBean>(FinishOrderBean.class));
    }

    public String getnameincarorder(int productid){
        String sql= "select name from productinfo where productid= ?";
        return jdbcTemplate.queryForObject(sql,new Object[]{productid},String.class);
    }

    public float getpriceincarorder(int productid){
        String sql="select price from productinfo where productid= ?";
        return jdbcTemplate.queryForObject(sql,Float.class,new Object[]{productid});
    }

    public int addNewOrder(String userid,int buynum,int productid){
        String sql= "insert into carorder(productid,userid,name,buynum,price,allprice) " +
                "values(?,?,?,?,?,?)";
        System.out.println(sql);
        float p=getpriceincarorder(productid);
        float ap=p*buynum;
        return jdbcTemplate.update(sql,productid,userid,getnameincarorder(productid),
                buynum,p,ap);
    }

}
