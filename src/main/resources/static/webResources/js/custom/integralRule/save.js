/**
 *
 */
$(document).ready(function () {
    $("#ruleForm").validate({
        //debug:true,
        rules: {
            key: {
                checkUniquKey: true,
            },
            express: {
                required: true,
                checkLegal: true,
            },
            note: {
                required: true,
            },
        },
        submitHandler: function (form) {
            var options = {
                type: "post",
                url: '/integralRule/save',
                success: function (data) {
                    $.unblockUI();
                    if (data.result == 'success') {
                        info('操作成功');
                        $("#integralRuleTable").trigger("reloadGrid");
                    } else if (data.result == 'error') {
                        warning('操作失败:' + data.message);
                    } else {
                        warning('操作失败');
                    }
                }
            };

            $.blockUI({
                message: '<div class="lds-css ng-scope"><div class="lds-spinner" style="100%;height:100%"><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div><div></div></div></div>',
                // 指的是提示框的css
                css: {
                    width: "0px",
                    top: "40%",
                    left: "50%"
                },
            });
            $(form).ajaxSubmit(options);
        }
    });

    jQuery.validator.addMethod("checkUniquKey", function (value, element) {
        var flag;
        $.ajax({
            url: '/integralRule/checkExist',
            data: {
                'key': $("#key").val(),
            },
            type: 'get',
            dataType: 'json',
            async: false,
            success: function (msg) {
                // true表示已经存在
                if (msg.result == true) {
                    if ($("#integralRuleId").val() != null && $("#integralRuleId").val() != "") {
                        // 判断是否为当前规则
                        $.ajax({
                            url: '/integralRule/checkExist',
                            data: {
                                'id': $("#integralRuleId").val(),
                                'key': $("#key").val(),
                            },
                            type: 'get',
                            dataType: 'json',
                            async: false,
                            success: function (msg) {
                                // true就是当前规则
                                if (msg.result == true) {
                                    flag = true;
                                } else {
                                    flag = false;
                                }
                            }
                        });
                    } else {
                        flag = false;
                    }

                } else {
                    flag = true;
                }
            }
        });
        return flag;
    }, "您选择的key已存在规则");

    jQuery.validator.addMethod("checkLegal", function (value, element) {
        var flag;
        $.ajax({
            url: '/integralRule/test',
            data: {
                'ruleContext': $("#ruleContext").val(),
                'exp': $("#express").val(),
            },
            type: 'get',
            dataType: 'json',
            async: false,
            success: function (msg) {
                if(msg.result=="success"){
                    flag = true;
                }else{
                    flag = false;
                    expressTestResult = msg.message;
                }
            }
        });
        return flag;
    }, "表达式有错误");

})