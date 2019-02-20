package pers.liujunyi.cloud.photo.domain;

import com.alibaba.fastjson.JSONArray;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author ljy
 */
@ApiModel
@Data
@EqualsAndHashCode(callSuper = false)
public class IdParamDto implements Serializable {
    private static final long serialVersionUID = -6603286179991508260L;

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "ids")
    private String ids;

    @ApiModelProperty(value = "idList")
    private List<Long> idList;


    @ApiModelProperty(value = "code")
    private String codes;

    @ApiModelProperty(value = "codes")
    private List<String> codeList;


    @ApiModelProperty(value = "状态")
    private Byte status;

    public void setIds(String ids) {
        if (StringUtils.isNotBlank(ids)) {
            this.setIdList(JSONArray.parseArray(ids, Long.class));
        }
        this.ids = ids;
    }

    public void setCodes(String codes) {
        if (StringUtils.isNotBlank(codes)) {
            this.setCodeList(JSONArray.parseArray(codes, String.class));
        }
        this.codes = codes;
    }

}
