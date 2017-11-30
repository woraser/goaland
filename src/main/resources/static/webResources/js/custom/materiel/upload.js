/**
 * 
 */
$(document).ready(function() {
	$("#excelForm").validate({
		//debug:true,
		rules : {
			excel : {
				required : true,
			},
		},
		submitHandler: function(form) {  
			var options = {
				type : "post",
				url : '/materiel/save/batch',
				success : function(data) {
					$.unblockUI();
					if(data.result=='success'){
						info('操作成功');
						jQuery("#materielTable").jqGrid().trigger("reloadGrid");
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

	$("#excel").fileinput({
		language: 'zh', //设置语言
		maxFileCount: 1,
		showUpload: false, //是否显示上传按钮
		maxFileSize : 50000,
		allowedFileExtensions: ["xlsx"]
	});
	 
})
