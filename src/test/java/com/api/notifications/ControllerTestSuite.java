package com.api.notifications;
import org.junit.platform.suite.api.IncludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;


@Suite
@SelectPackages("com.api.notifications.controllers")
@IncludeClassNamePatterns(".*Test")
public class ControllerTestSuite {
}
