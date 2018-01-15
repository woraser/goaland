/**
 * 
 */
$(document).ready(function() {
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"agreementStatus.beginTime" : {
				required : true,
			},
			"agreementStatus.endTime" : {
				required : true,
                compareDate: "#agreementStatus\\.beginTime"
            },
		},
        messages:{
            "agreementStatus.endTime":{
                compareDate: "结束日期必须大于开始日期!"
            }
        },
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/customerServiceProcess/fillInAgreement',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/customerServiceProcess/allProcesses/view"
						}
						infoAndFunc('操作成功',func);
					}else if(data.result=='error'){
						warning('操作失败:'+data.message);
					}else{
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
	
	$("#agreementStatus\\.beginTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	$("#agreementStatus\\.endTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});

    jQuery.validator.methods.compareDate = function(value, element, param) {
        //var startDate = jQuery(param).val() + ":00";补全yyyy-MM-dd HH:mm:ss格式
        //value = value + ":00";

        var startDate = jQuery(param).val();

        var date1 = new Date(Date.parse(startDate.replace("-", "/")));
        var date2 = new Date(Date.parse(value.replace("-", "/")));
        return date1 < date2;
    };

})
