package com.apps.potok.config;

import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;

@ContextConfiguration(classes = {PotokTestConfiguration.class}, initializers = {ConfigDataApplicationContextInitializer.class})
public class BaseTest extends AbstractTestNGSpringContextTests {

}
