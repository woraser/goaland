/**
 * 
 */
$(document).ready(function() {
	$("#materielForm").validate({
		//debug:true,
		rules : {
			name : {
				required : true,
				checkUniqueName : true,
			},
			beginTime : {
				required : true,
			},
			checkYear : {
				digits:true,                 
			},
			checkMonth : {
				digits:true,
				max: 12,
			},
			checkDay : {
				digits:true,
				max: 31,
			},
			remindYear : {
				checkLegal : true,
				digits:true,
			},
			remindMonth : {
				checkLegal : true,
				digits:true,
				max: 12,
			},
			remindDay : {
				checkLegal : true,
				digits:true,
				max: 31,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/materiel/save',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						$("#materielTable").trigger("reloadGrid");
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
	
	$("#beginTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	
	$("#lastCheckTime").datetimepicker({language: 'zh-CN', format: 'yyyy-mm-dd hh:ii:ss',todayBtn:'true',todayHighlight:'true'});
	
	jQuery.validator.addMethod("checkLegal", function(value, element) {
		if($("#checkYear").val() != 0 || $("#checkMonth").val() != 0 ||$("#checkDay").val() != 0 || 
				$("#remindYear").val() != 0 || $("#remindMonth").val() != 0 || $("#remindDay").val() != 0){
			var checks = parseInt($("#checkYear").val()) * 365 + parseInt($("#checkMonth").val()) * 30 + parseInt($("#checkDay").val());
			console.info(checks)
			var reminds = parseInt($("#remindYear").val()) * 365 + parseInt($("#remindMonth").val()) * 30 + parseInt($("#remindDay").val());
			console.info(reminds)
			if(checks >= reminds){
				return true
			}else{
				return false;
			}
		}else{
			return true;
		}
	}, "维护周期不能小于预警周期");
	
	jQuery.validator.addMethod("checkUniqueName", function(value, element) {
		var flag;
		$.ajax({
			url : '/materiel/checkExist',
			data : {
				'name' : $("#name").val(),
				'device.id' : $("#device\\.id").val(),
			},
			type : 'get',
			dataType : 'json',
			async:false,
			success : function(msg) {
				// true表示已经存在
				if (msg.result == true) {
					if($("#materielId").val()!=null&&$("#materielId").val()!=""){
						// 判断是否是当前materiel
						$.ajax({
							url : '/materiel/checkExist',
							data : {
								'id' : $("#materielId").val(),
								'name' : $("#name").val(),
								'device.id' : $("#device\\.id").val(),
							},
							type : 'get',
							dataType : 'json',
							async:false,
							success : function(msg) {
								// true表示是当前materiel
								if (msg.result == true) {
									flag = true;
								} else{
									flag = false;
								}
							}
						});
					}else{
						flag = false;
					}
					
				} else{
					flag = true;
				}
			}
		});
		return flag;
	}, "您输入的物料名称在当前设备中已存在");
	
})