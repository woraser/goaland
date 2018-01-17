/**
 * 
 */
function createModalPage(title,url,func) {
	var dialog = new BootstrapDialog({
		title: title,
        message: function(dialogRef){
            var $message =$('<div></div>').load(url);
            return $message;
        },
        buttons: [{
            id: 'btn-1',
            label: '确定',
            cssClass: 'btn-info',
        },{
            id: 'btn-2',
            label: '取消',
            cssClass: 'btn-warning',
        }],
        closable: false,
        draggable: true,
    });
	dialog.realize();
    /*dialog.getModalHeader().css('background-color', '#0088cc');
    dialog.getModalHeader().css('color', '#fff');
    dialog.getModalBody().css('background-color', '#0088cc');
    dialog.getModalBody().css('color', '#fff');
    dialog.getModalFooter().css('background-color', '#0088cc');
    dialog.getModalFooter().css('color', '#fff');*/
	
	var btn1 = dialog.getButton('btn-1');
	var btn2 = dialog.getButton('btn-2');
	
	btn1.click(function(event){
		if(func()){
			dialog.close();
		}
	});
	
	btn2.click(function(event){
		dialog.close();
    });
   
    dialog.open();
}

//展示页面
function createModalPageToView(title,url) {
	var dialog = new BootstrapDialog({
		title: title,
        message: function(dialogRef){
            var $message =$('<div></div>').load(url);
            return $message;
        },
        buttons: [{
            id: 'btn-1',
            label: '确定',
            cssClass: 'btn-info',
        }],
        closable: false,
        draggable: true,
    });
	dialog.realize();
	
	var btn1 = dialog.getButton('btn-1');
	
	btn1.click(function(event){
		dialog.close();
	});
   
    dialog.open();
}

//展示页面
function createModalPageToViewFunc(title,url,func) {
    var dialog = new BootstrapDialog({
        title: title,
        message: function(dialogRef){
            var $message =$('<div></div>').load(url);
            return $message;
        },
        buttons: [{
            id: 'btn-1',
            label: '确定',
            cssClass: 'btn-info',
        }],
        closable: false,
        draggable: true,
    });
    dialog.realize();

    var btn1 = dialog.getButton('btn-1');

    btn1.click(function(event){
    	func()
        dialog.close();
    });

    dialog.open();
}

function warning(message){
	var dialog = new BootstrapDialog({
		title: '错误',
		message: message,
		buttons: [{
			id: 'btn-1',
	        label: '确定'
	    }],
	    closable: false,
	    draggable: true,
	});
	dialog.realize();
	dialog.setType(BootstrapDialog.TYPE_DANGER);
	var btn1 = dialog.getButton('btn-1');
	btn1.click(function(event){
		dialog.close();
	});
	
	dialog.open();
}

function warningAndFunc(message,func){
	var dialog = new BootstrapDialog({
		title: '错误',
		message: message,
		buttons: [{
			id: 'btn-1',
	        label: '确定'
	    }],
	    closable: false,
	    draggable: true,
	});
	dialog.realize();
	dialog.setType(BootstrapDialog.TYPE_DANGER);
	var btn1 = dialog.getButton('btn-1');
	btn1.click(function(event){
		func();
	});
	
	dialog.open();
}

function info(message){
	var dialog = new BootstrapDialog({
		title: '信息',
		message: message,
		buttons: [{
			id: 'btn-1',
	        label: '确定'
	    }],
	    closable: false,
	    draggable: true,
	});
	dialog.realize();
	dialog.setType(BootstrapDialog.TYPE_INFO);
	var btn1 = dialog.getButton('btn-1');
	btn1.click(function(event){
		dialog.close();
	});
	
	dialog.open();
}

function infoAndFunc(message,func){
	var dialog = new BootstrapDialog({
		title: '信息',
		message: message,
		buttons: [{
			id: 'btn-1',
	        label: '确定'
	    }],
	    closable: false,
	    draggable: true,
	});
	dialog.realize();
	dialog.setType(BootstrapDialog.TYPE_INFO);
	var btn1 = dialog.getButton('btn-1');
	btn1.click(function(event){
		func()
	});
	
	dialog.open();
}

function confirm(message,func){
	BootstrapDialog.confirm({
        title: '执行确认',
        message: message,
        type: BootstrapDialog.TYPE_WARNING, // <-- Default value is BootstrapDialog.TYPE_PRIMARY
        closable: true, // <-- Default value is false
        draggable: true, // <-- Default value is false
        btnOKLabel: '确定', // <-- Default value is 'OK',
        btnOKClass: 'btn-warning', // <-- If you didn't specify it, dialog type will be used,
        btnCancelLabel: '取消', // <-- Default value is 'Cancel',
        callback: function(result) {
            // result will be true if button was click, while it will be false if users close the dialog directly.
            if(result) {
                func();
            }
        }
    });
}