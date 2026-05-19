package com.eandrade;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MsCommonsApplication {

    public static void main(String[] args) {
        // Empty main method for library
      }

}
