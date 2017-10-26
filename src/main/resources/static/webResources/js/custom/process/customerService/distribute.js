/**
 * 
 */
$(document).ready(function() {
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"distributeDetail.engineer" : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/customerServiceProcess/distribute',
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
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});

})
