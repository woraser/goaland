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

	$("#fileUpLoad").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 10,
		showUpload: false, //是否显示上传按钮
		maxFileSize : 2000,
		allowedFileExtensions: ["xlsx","xls","pdf","doc","docx","csv","txt","dwg","gif","png","jpeg","bmp","icon"]
	});
	 
})
