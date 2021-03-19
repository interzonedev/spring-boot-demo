package com.interzonedev.springbootdemo.web;

import com.interzonedev.respondr.response.HttpResponse;
import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class HomeControllerMockIT extends SpringBootDemoMockWebIT {

    private static final Logger log = LoggerFactory.getLogger(HomeControllerMockIT.class);

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHome() throws Exception {
        log.debug("getHome: Start");

        RequestBuilder request = MockMvcRequestBuilders
                .get("/")
                .accept(MediaType.TEXT_HTML);

        mvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(HttpResponse.HTML_CONTENT_TYPE))
                .andExpect(content().string(containsString("Spring Boot Demo - Welcome")));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHome: End");
    }
}
