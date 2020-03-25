package com.bula.blog;

import com.bula.blog.Util.QiNiuCloud;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class QINiuTest {

    @Test
    public void testAccessToken() {
        QiNiuCloud qiNiuCloud = new QiNiuCloud();
        System.out.println(qiNiuCloud.GetAccessToken());
    }
}
