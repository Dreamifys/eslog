package com.brand.log.controller;

import com.brand.log.common.po.TLogSaveReq;
import com.brand.log.service.LogService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@EnableAutoConfiguration
public class LogController {

    @Autowired
    private LogService Log;

    @RequestMapping("/")
    @ResponseBody
    String index() {
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long timeMills = new Date().getTime();
        return "congratulation！" + date;
    }

    @RequestMapping(value = "/log/save", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> save(TLogSaveReq logSaveReq) throws JSONException {

        Map<String, Object> res = new HashMap<String, Object>();
        res.put("status", 0);
        if(logSaveReq.getTag() == null || logSaveReq.getValue() == null || null == logSaveReq.getKey()) {
            res.put("status", 400);
            res.put("message", "null param");
            return res;
        }
        res.put("message", "");
        Log.store(logSaveReq);
        res.put("status", 200);
        return res;

    }
}
