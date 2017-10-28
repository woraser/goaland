/**
 * 
 */
$(document).ready(function() {
	$("#documentForm").validate({
		//debug:true,
		rules : {
			fileUpLoad : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/technologyDocument/upload',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						var func = function(){
							window.location.href="/technologyDocument/manage/view"
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

	$("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		allowedFileExtensions: ["xlsx","xls","pdf","doc","docx","csv","txt","dwg","gif","png","jpeg","bmp","icon"]
	});
	 
})
