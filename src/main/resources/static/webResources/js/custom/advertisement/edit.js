/**
 * 
 */
$(document).ready(function() {
	var E = window.wangEditor
    var editor = new E('#div1', '#div2')
    
	// 加载菜单
    editor.customConfig.menus = [
        'head',  // 标题
        'bold',  // 粗体
        'italic',  // 斜体
        'underline',  // 下划线
        'strikeThrough',  // 删除线
        'foreColor',  // 文字颜色
        'backColor',  // 背景颜色
        'link',  // 插入链接
        'list',  // 列表
        'justify',  // 对齐方式
        'quote',  // 引用
        'emoticon',  // 表情
        'image',  // 插入图片
        'table',  // 表格
        //'video',  // 插入视频
        //'code',  // 插入代码
        'undo',  // 撤销
        'redo'  // 重复
    ]
	
	// 关闭粘贴样式的过滤
    editor.customConfig.pasteFilterStyle = false
    
    // 使用 base64 保存图片
    editor.customConfig.uploadImgShowBase64 = true   
	
	// 自定义 onchange 触发的延迟时间，默认为 200 ms
	editor.customConfig.onchangeTimeout = 1000 // 单位 ms
	
	editor.customConfig.onchange = function (html) {
        //保存到服务器
		console.info("onchange")
		$.ajax({
			url : '/advertisement/save',
			data : {
				"id" : $("#advertisementId").val(),
				"content" : editor.txt.html(),
			},
			type : 'post',
			dataType : 'json',
			success : function( data ) {
				if(data.result == "success"){
					$.toast({ 
					  text : "自动保存成功", 
					  hideAfter : 3000,
					  position : 'mid-center'
					})     
				}
			}
		 })
		
    }
	
	// 配置服务器端地址
    editor.customConfig.uploadImgServer = '/fileUpload/multipartFiles/'+uuid()
	
	// 将图片大小限制为 3M
	editor.customConfig.uploadImgMaxSize = 3 * 1024 * 1024

	// 限制一次最多上传 5 张图片
	editor.customConfig.uploadImgMaxLength = 5

	// 自定义文件参数名
	editor.customConfig.uploadFileName = 'file_upload'
		
	// 自定义图片插入过程
	editor.customConfig.uploadImgHooks = {
        before: function (xhr, editor, files) {
            // 图片上传之前触发
            console.info("upload file")
        },
        success: function (xhr, editor, result) {
            // 图片上传并返回结果，图片插入成功之后触发
        	console.info("upload success")
        },
        fail: function (xhr, editor, result) {
            // 图片上传并返回结果，但图片插入错误时触发
        	console.info("upload success but insert fail")
        	$.toast({ 
    		  text : "upload success but insert fail", 
    		  hideAfter : 3000,
    		  position : 'mid-center'
    		})
        },
        error: function (xhr, editor) {
            // 图片上传出错时触发
        	console.info("upload fail")
        	$.toast({ 
    		  text : "upload fail", 
    		  hideAfter : 3000,
    		  position : 'mid-center'
    		})   
        },
        timeout: function (xhr, editor) {
            // 图片上传超时时触发
        	console.info("time out")
        	$.toast({ 
      		  text : "time out", 
      		  hideAfter : 3000,
      		  position : 'mid-center'
      		})     
        },

        // 如果服务器端返回的不是 {errno:0, data: [...]} 这种格式，可使用该配置
        // （但是，服务器端返回的必须是一个 JSON 格式字符串！！！否则会报错）
        customInsert: function (insertImg, result, editor) {
            // 图片上传并返回结果，自定义插入图片的事件（而不是编辑器自动插入图片！！！）
            // insertImg 是插入图片的函数，editor 是编辑器对象，result 是服务器端返回的结果

            var identification = result.identification
            
            // 获取文件列表
            $.ajax({
				url : '/fileDownload/list/REMOTE',
				data : {
					"identification" : identification,
					"showAttributes" : "stringObjectId",
				},
				type : 'get',
				dataType : 'json',
				success : function( data ) {
					$.each(data.content,function(){
						insertImg("/fileDownload/" + this.stringObjectId)
					})
				}
			 })
            
        }
    }
	
    editor.create()
    
    function checkLastContent(){
    	 if($("#advertisementId").val()!=null && $("#advertisementId").val()!=""){
			// 获取保存的内容
			$.ajax({
				url : '/advertisement/management/data/one',
				data : {
					"id" : $("advertisementId").val(),
					"showAttributes" : "content",
				},
				type : 'get',
				dataType : 'json',
				success : function( data ) {
					editor.txt.html(data.content)
				}
			 })
		}
    }
   
    checkLastContent()
    
})