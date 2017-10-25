/**
 * 
 */
$(document).ready(function(){
	var devices = new Vue({
	  el: '#devices',
	  data: {
	    deviceDatas:[],
	  }
	})
	
	 $.ajax({
			url : "/devCategory/count",
			type : 'get',
			dataType : 'json',
			async : false,
			success : function( data ) {
				devices.deviceDatas = data;
			}
	 });
})

