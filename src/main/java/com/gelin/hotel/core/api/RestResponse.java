package com.gelin.hotel.core.api;/**
 * Created by vetech on 2018/11/27.
 */

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.UUID;

/**
 * 〈〉
 *
 * @author zoujiming
 * @since 2018/11/27
 */
@ApiModel(description = "响应实体")
public class RestResponse<T> implements Serializable {

    /**
     * id
     */
    private static final long serialVersionUID = 1L;

    /**
     * 响应ID
     */
    @ApiModelProperty(value = "响应ID", required = true, dataType = "string")
    private String id = UUID.randomUUID().toString();

    /**
     * 状态码(业务定义)
     */
    @ApiModelProperty(value = "状态码(业务定义)", required = true, dataType = "string")
    private String status = ResultCode.OK.getCode();

    /**
     * 状态码描述(业务定义)
     */
    @ApiModelProperty(value = "状态码描述(业务定义)", required = true, dataType = "string")
    private String message = ResultCode.OK.getMessage();

    /**
     * 结果集(泛型)
     */
    @ApiModelProperty(value = "结果集(泛型)", required = true, dataType = "object")
    private T result = null; //NOSONAR

    /**
     * 构造函数
     */
    public RestResponse() {
        super();
    }

    /**
     * 构造函数
     *
     * @param result 结果集(泛型)
     */
    public RestResponse(T result) {
        super();
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param httpStatus http状态
     * @param error      错误
     */
    @SuppressWarnings("unchecked")
    public RestResponse(ResultCode httpStatus, String error) {
        super();
        this.status = httpStatus.getCode();
        if (StringUtils.trimToEmpty(httpStatus.getMessage()).indexOf("%s") >= 0) {
            this.message = String.format(httpStatus.getMessage(), error);
        } else {
            this.message = httpStatus.getMessage() + error;
        }
    }

    /**
     * 构造函数
     *
     * @param status  状态码(业务定义)
     * @param message 状态码描述(业务定义)
     */
    public RestResponse(String status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    /**
     * 构造函数
     *
     * @param status  状态码(业务定义)
     * @param message 状态码描述(业务定义)
     * @param result  结果集(泛型)
     */
    public RestResponse(String status, String message, T result) {
        super();
        this.status = status;
        this.message = message;
        this.result = result;
    }

    /**
     * 请求是否成功
     *
     * @return 成功返回true
     */
    public boolean isSuccess() {
        return ResultCode.OK.getCode().equalsIgnoreCase(status);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setResult(T result) {
        this.result = result;
    }
}
