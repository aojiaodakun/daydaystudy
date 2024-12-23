package com.hzk.framework.log.jul;

import java.util.logging.Logger;

public class JULTest {

    public static void main(String[] args) throws Exception {
        Logger logger = Logger.getLogger(JULTest.class.getName());
        logger.info("aaa");
    }

}
