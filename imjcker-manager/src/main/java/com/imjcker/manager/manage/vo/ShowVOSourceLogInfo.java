package com.imjcker.manager.manage.vo;

import com.imjcker.manager.elastic.model.SourceLogInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ShowVOSourceLogInfo extends ShowVO{
    private List<SourceLogInfo> list = new ArrayList<>();
}
