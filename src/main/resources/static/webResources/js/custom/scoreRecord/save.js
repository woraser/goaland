/**
 * 
 */
$(document).ready(function() {
	$("#recordForm").validate({
		//debug:true,
		rules : {
            score : {
                number : true,
                required : true,
            },
			reason : {
				required : true,
			},
		},
		submitHandler: function(form) {
			var options = {
				type : "post",
				url : '/scoreRecord/save',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						$("#integralTable").trigger("reloadGrid");
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

})