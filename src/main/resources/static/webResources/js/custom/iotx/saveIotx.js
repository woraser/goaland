/**
 * 
 */
$(document).ready(function() {
	$("#iotxForm").validate({
		//debug:true,
		rules : {
			serialNo : {
				required : true,
				checkSerialNo : true,
			},
			installLocation : {
				required : true,
			},
			company : {
				required : true,
			},
			iotxCategory : {
				required : true,
			},
			iotxModel : {
				required : true,
			},
			networkCategory : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/iotx/saveIotx',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						$("#iotxTable").trigger("reloadGrid");
					}else{
						warning('操作失败:'+data.result);
					}
				}
			};
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});

	jQuery.validator.addMethod("checkSerialNo", function(value, element) {
		var flag=true;
		$.ajax({
			url : '/iotx/checkExist',
			data : {
				'serialNo' : $("#serialNo").val(),
			},
			type : 'get',
			dataType : 'json',
			success : function(msg) {
				//如果存在相同序列号，判断id是否相等
				if (msg.result == true) {
					if($("#iotxId").val()!=null&&$("#iotxId").val()!=''){
						
						/*-----检查序列号和id----*/
						$.ajax({
							url : '/iotx/checkExist',
							data : {
								'serialNo' : $("#serialNo").val(),
								'id' : $("#iotxId").val(),
							},
							type : 'get',
							dataType : 'json',
							async : false,
							success : function(msg) {
								//如果序列号和id都相等，那么表明是同一个iotx
								if (msg.result != true) {
									flag=false
								} 
							}
						});
						/*-----检查序列号和id----*/
						
					}else{
						//为null肯定为重复序列号
						flag = false;
					}
				}
			}
		});
		return flag;
	}, "您输入的序列号已存在");

})
