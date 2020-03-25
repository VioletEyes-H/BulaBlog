package com.bula.blog.Util;

import com.alibaba.fastjson.JSONObject;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class QiNiuCloud {
    private static final String ACCESS_KEY = "HzozKa7HXLycV1KtZEYjqmSOXixvo27dr4xB3lWP";
    private static final String SECERT_KEY = "dwrjbiAztHgmkVZL7hp67JORHC6XS71EMUlMfN6z";
    private static final String BUCKET = "bulayu";
    private static String AccessToken;
    private static final String URL = "http://cdn.bulabula.xyz/";

    public String GetAccessToken() {
        if (null == AccessToken) {
            Auth auth = Auth.create(ACCESS_KEY, SECERT_KEY);
            AccessToken = auth.uploadToken(BUCKET);
        }
        return AccessToken;
    }

    /**
     * 上传文件到七牛云
     *
     * @param file
     * @return 返回七牛云路径
     * @throws IOException 保存异常
     */
    public String UpLoadFile(MultipartFile file) throws IOException {
        Configuration cfg = new Configuration(Region.region0());//设置区域为华东区域
        UploadManager uploadManager = new UploadManager(cfg);//生成上传凭证
        String fileName = file.getOriginalFilename();
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        tempName.append("images/");
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        Response response = uploadManager.put(file.getBytes(), newFileName, this.GetAccessToken());
        JSONObject res = JSONObject.parseObject(response.bodyString());
        return URL + res.getString("key");
    }

}
