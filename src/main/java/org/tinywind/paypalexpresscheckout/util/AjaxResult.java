package org.tinywind.paypalexpresscheckout.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.BindingResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class AjaxResult extends HashMap<String, Object> implements Serializable {
    private static String KEY_RESULT = "result";
    private static String KEY_REASON = "reason";
    private static String KEY_PARAM = "param";
    private static String KEY_FIELD_ERRORS = "fieldErrors";
    private static String KEY_GLOBAL_ERRORS = "globalErrors";

    public AjaxResult() {
        this(Result.success);
    }

    public AjaxResult(Result result) {
        this(result, null);
    }

    public AjaxResult(Result result, String reason) {
        this(result, reason, null);
    }

    public AjaxResult(Exception e) {
        this(Result.failure, e.getCause() != null ? e.getCause().getMessage() : e.getMessage());
    }

    public AjaxResult(BindingResult bindingResult) {
        this(bindingResult.hasErrors() ? Result.failure : Result.success, null, bindingResult);
    }

    public AjaxResult(Result result, String reason, BindingResult bindingResult) {
        setFieldErrors(new ArrayList<>());
        setGlobalErrors(new ArrayList<>());
        setResult(result);
        setReason(reason);
        if (bindingResult != null)
            setBindingResult(bindingResult);
    }

    public static AjaxResult create() {
        return new AjaxResult();
    }

    public static AjaxResult create(Long id) {
        AjaxResult result = new AjaxResult();
        result.put("id", id);
        return result;
    }

    public static AjaxResult create(Result result) {
        return new AjaxResult(result);
    }

    public static AjaxResult create(Result result, String reason) {
        return new AjaxResult(result, reason);
    }

    public static AjaxResult create(Result result, String reason, BindingResult bindingResult) {
        return new AjaxResult(result, reason, bindingResult);
    }

    public static AjaxResult create(Exception e) {
        return new AjaxResult(e);
    }

    public static AjaxResult create(BindingResult bindingResult) {
        return new AjaxResult(bindingResult);
    }

    public static AjaxResult create(String reason) {
        return new AjaxResult(Result.failure, reason);
    }

    public Object getParam() {
        return get(KEY_PARAM);
    }

    public void setParam(Object param) {
        put(KEY_PARAM, param);
    }

    public void setBindingResult(BindingResult bindingResult) {
        setResult(bindingResult.hasErrors() ? Result.failure : Result.success);

        List<FieldError> fieldErrors = getFieldErrors();
        if (fieldErrors == null)
            fieldErrors = new ArrayList<>();

        fieldErrors.addAll(bindingResult.getFieldErrors().stream().map(error -> new FieldError(error.getField(), error.isBindingFailure(), error.getObjectName(), error.getCodes(), error.getDefaultMessage())).collect(Collectors.toList()));
        setFieldErrors(fieldErrors);

        List<GlobalError> globalErrors = getGlobalErrors();
        if (globalErrors == null)
            globalErrors = new ArrayList<>();

        globalErrors.addAll(bindingResult.getGlobalErrors().stream().map(error -> new GlobalError(error.getObjectName(), error.getCodes(), error.getDefaultMessage())).collect(Collectors.toList()));
        setGlobalErrors(globalErrors);
    }

    public Result getResult() {
        return (Result) get(KEY_RESULT);
    }

    public void setResult(Result result) {
        put(KEY_RESULT, result);
    }

    public String getReason() {
        return (String) get(KEY_REASON);
    }

    public void setReason(String reason) {
        put(KEY_REASON, reason);
    }

    @SuppressWarnings("unchecked")
    public List<FieldError> getFieldErrors() {
        return (List<FieldError>) get(KEY_FIELD_ERRORS);
    }

    public void setFieldErrors(List<FieldError> fieldErrors) {
        put(KEY_FIELD_ERRORS, fieldErrors);
    }

    @SuppressWarnings("unchecked")
    public List<GlobalError> getGlobalErrors() {
        return (List<GlobalError>) get(KEY_GLOBAL_ERRORS);
    }

    public void setGlobalErrors(List<GlobalError> globalErrors) {
        put(KEY_GLOBAL_ERRORS, globalErrors);
    }

    public enum Result {
        success, failure
    }

    @Data
    @AllArgsConstructor
    class Error {
        protected String objectName;
        protected String[] codes;
        protected String defaultMessage;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private class GlobalError extends Error {
        GlobalError(String objectName, String[] codes, String defaultMessage) {
            super(objectName, codes, defaultMessage);
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    private class FieldError extends Error {
        private String field;
        private Boolean bindingFailure;

        FieldError(String field, Boolean bindingFailure, String objectName, String[] codes, String defaultMessage) {
            super(objectName, codes, defaultMessage);
            this.field = field;
            this.bindingFailure = bindingFailure;
        }
    }
}
