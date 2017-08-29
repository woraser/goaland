/**
 * 
 */
$(document).ready(function() {
	$("#customerServiceForm").validate({
		//debug:true,
		rules : {
			"repairDetail.problemDescription" : {
				required : true,
			},
			"repairDetail.failureCause" : {
				required : true,
			},
			"repairDetail.processMode" : {
				required : true,
			},
			mandatary : {
				required : true,
			},
			reason : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var url;
			if($('#isEntrust').is(':checked')) {
				url='/customerServiceProcess/entrust'
			}else{
				url='/customerServiceProcess/repair'
			}
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
			
			$.blockUI({message: '<img src="/webResources/img/loading/loading.gif" /> '});
			$(form).ajaxSubmit(options);     
		}  
	});
	
	$('#isEntrust').click(function(){
		if($(this).is(':checked')) {
			$("#entrust").show();
			$("#notEntrust").hide();
		}else{
			$("#entrust").hide();
			$("#notEntrust").show();
		}
	})

})
