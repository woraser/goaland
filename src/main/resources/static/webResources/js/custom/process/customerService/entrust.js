/**
 * 
 */
$(document).ready(function() {
	$("#customerServiceForm").validate({
		//debug:true,
		ignore: ":hidden:not(select)",
		rules : {
			"entrustDetail.problemDescription" : {
				required : true,
			},
			"entrustDetail.failureCause" : {
				required : true,
			},
			"entrustDetail.processMode" : {
				required : true,
			},
			device : {
				required : true,
			},
			faultCategory : {
				required : true,
			},
			"entrustDetail.repairStartTime" : {
				required : true,
			},
			"entrustDetail.repairEndTime" : {
				required : true,
                compareDate: "#entrustDetail\\.repairStartTime"
			},
		},
		errorPlacement: function(error, element) {
            if(element.parent('#deviceSelect').length || element.parent('#faultCategorySelect').length) {
                error.insertAfter(element.parent());
            } else {
                error.insertAfter(element);
            }
        },
        messages:{
            "entrustDetail.repairEndTime":{
                compareDate: "结束日期必须大于开始日期!"
            }
        },
		submitHandler: function(form) {  
			var url='/customerServiceProcess/entrust'
			var options = {
				type : "post",
				url : url,
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/customerServiceProcess/runtimeTask/view"
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
	
	$("#entrustDetail\\.repairStartTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	$("#entrustDetail\\.repairEndTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	
	$("#faultCategory").chosen();
	$("#device").chosen();
	$("#fellow").chosen();
	
	$.each(eval($("#faultCategoryIds").val()),function(index,item){
		$("#faultCategory option[value='"+ item +"']").attr("selected","selected");
	}); 
	$.each(eval($("#deviceIds").val()),function(index,item){
		$("#device option[value='"+ item +"']").attr("selected","selected");
	}); 
	
	$("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		maxFileSize : 50000,
		allowedFileExtensions: ["jpg","jpeg","png"]
	});

    jQuery.validator.methods.compareDate = function(value, element, param) {
        //var startDate = jQuery(param).val() + ":00";补全yyyy-MM-dd HH:mm:ss格式
        //value = value + ":00";

        var startDate = jQuery(param).val();

        var date1 = new Date(Date.parse(startDate.replace("-", "/")));
        var date2 = new Date(Date.parse(value.replace("-", "/")));
        return date1 < date2;
    };
	
})
