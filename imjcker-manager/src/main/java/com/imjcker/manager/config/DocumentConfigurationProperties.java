package com.imjcker.manager.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.File;

/**
 * @author ztzh_tanhh 2020/1/3
 **/
@Slf4j
@Data
@ConfigurationProperties(prefix = "docs")
public class DocumentConfigurationProperties {
    private String templatePath = System.getProperty("user.dir") + File.separator + "microservice/scripts/template";//模板地址
    private String temporaryPath = System.getProperty("user.dir") + File.separator + "microservice/scripts/temporary";//临时地址
    private String wordExtension = ".docx";//word文档后缀
}
