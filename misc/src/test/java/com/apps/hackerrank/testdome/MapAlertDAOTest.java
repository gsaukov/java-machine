package com.apps.hackerrank.testdome;

import org.testng.annotations.Test;

import java.util.Date;

public class MapAlertDAOTest {

    @Test
    public void daoTest() {
        MapAlertDAO dao = new MapAlertDAO();
        dao.addAlert(new Date());
        AlertService service = new AlertService(dao);
        service.raiseAlert();
    }

}
