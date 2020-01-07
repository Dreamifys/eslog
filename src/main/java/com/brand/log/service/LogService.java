package com.brand.log.service;

import com.brand.log.common.dao.TLogMapper;
import com.brand.log.common.po.TLog;
import com.brand.log.common.po.TLogSaveReq;
import com.brand.log.impl.EsOprationImpl;
import com.brand.log.util.ConstParams;
import com.brand.log.util.DateFormatV1;
import com.brand.log.util.RedisUtil;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Map;

@Service
@EnableAsync
public class LogService {
    @Autowired
    EsOprationImpl es;

    @Autowired
    Map<String,String> paramKey;

    @Autowired
    DateFormatV1 dateFormat;

    @Autowired
    SqlSessionFactory sqlSessionFactory;

    @Autowired
    RedisUtil redisUtil;

    @Async
    public void store(TLogSaveReq logSaveReq) {

        if (!paramKey.isEmpty()){
            String mkey = paramKey.get(logSaveReq.getKey());
            if (mkey != null) {
                String req_id = logSaveReq.getRequest_id()==null ? "@_@" : logSaveReq.getRequest_id();
                logSaveReq.setRequest_id(req_id);
                es.add(logSaveReq.getTag(), logSaveReq.getValue(), mkey, req_id);
                //insertLog(logSaveReq);
                int res = redisUtil.checkCount("req", req_id, 60, 60);
                System.out.println("checkcount:" + res);
            }
        }
    }


    private void insertLog(TLogSaveReq logSaveReq){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        try {
            TLogMapper tLogMapper = sqlSession.getMapper(TLogMapper.class);
            TLog log = new TLog();
            log.setRefer(logSaveReq.getTag());
            log.setData(logSaveReq.getValue());
            log.setUniqueId(logSaveReq.getRequest_id());
            log.setRefer(logSaveReq.getTag());
            int nowDay = Integer.parseInt(dateFormat.getDate("yyyyMMdd", "GMT+8"));
            log.setDate(nowDay);
            int hour = Integer.parseInt(dateFormat.getDate("HH", "GMT+8"));
            log.setHour((byte)hour);
            tLogMapper.insertSelective(log);
            //提交事务
            sqlSession.commit();
            sqlSession.close();
        } catch (Exception e) {
            sqlSession.rollback();
            sqlSession.close();
            e.printStackTrace();
        }
    }

}
