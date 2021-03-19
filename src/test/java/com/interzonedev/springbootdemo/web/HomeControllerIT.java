package com.interzonedev.springbootdemo.web;

import com.interzonedev.zankou.dataset.DataSet;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class HomeControllerIT extends SpringBootDemoWebIT {

    private static final Logger log = LoggerFactory.getLogger(HomeControllerIT.class);

    @Test
    @DataSet(filename = "dataset/user/before.xml", dataSourceBeanId = "springbootdemo.persistence.dataSource")
    public void getHome() throws Exception {
        log.debug("getHome: Start");

        String homeUrl = getFullTestURL("/");
        ResponseEntity<String> responseEntity = template.getForEntity(homeUrl, String.class);

        assertThat(responseEntity.getStatusCode(), is(HttpStatus.OK));
        assertIsHtmlContentType(responseEntity);
        assertThat(responseEntity.getBody(), containsString("Spring Boot Demo - Welcome"));

        dbUnitDataSetTester.compareDataSetsIgnoreColumns(dataSource, "dataset/user/before.xml",
                "sbd_user", null);

        log.debug("getHome: End");
    }

}
