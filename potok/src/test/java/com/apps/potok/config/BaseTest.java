package com.apps.potok.config;

import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(classes = {PotokTestConfiguration.class}, initializers = {ConfigFileApplicationContextInitializer.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

}
