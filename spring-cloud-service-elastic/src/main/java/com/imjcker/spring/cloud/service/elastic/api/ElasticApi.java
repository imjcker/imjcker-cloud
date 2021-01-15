package com.imjcker.spring.cloud.service.elastic.api;

import com.imjcker.spring.cloud.service.elastic.model.Doc;
import com.imjcker.spring.cloud.service.elastic.model.DocNew;
import com.imjcker.spring.cloud.service.elastic.model.DocOld;
import com.imjcker.spring.cloud.service.elastic.service.ElasticDao;
import com.imjcker.spring.cloud.service.elastic.utils.UuidUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class ElasticApi {

    @Value("${imjcker.old-index:old_index}")
    private String oldIndex;
    @Value("${imjcker.new-index:new_index}")
    private String newIndex;
    @Value("${imjcker.type:doc}")
    private String type;

    private final ElasticDao elasticDao;

    public ElasticApi(ElasticDao elasticDao) {
        this.elasticDao = elasticDao;
    }

    @PostMapping("/saveOld")
    public String saveOld(DocOld docOld) {
        docOld.setUuid(UuidUtils.generateUuid());
        elasticDao.saveDocOld(oldIndex, type, docOld);
        return docOld.getUuid();
    }

    @PostMapping("/saveNew")
    public String saveNew(DocNew docNew) {
        docNew.setUuid(UuidUtils.generateUuid());
        elasticDao.saveDocNew(newIndex, type, docNew);
        return docNew.getUuid();
    }

    @PostMapping("/save")
    public String save(Doc docNew) {
        docNew.setUuid(UuidUtils.generateUuid());
        docNew.setMarried(true);
        elasticDao.saveDoc(docNew);
        return docNew.getUuid();
    }

}
