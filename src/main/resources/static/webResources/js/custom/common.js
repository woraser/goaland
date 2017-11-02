/**
 * 
 */
function customPost(url, params) {
    var temp = document.createElement("form");
    temp.action = url;
    temp.method = "post";
    temp.style.display = "none";
    if(params!=null){
    	for (var x in params) {
	        var opt = document.createElement("input");
	        opt.name = x;
	        opt.value = params[x];
	        temp.appendChild(opt);
	    }
    }
    document.body.appendChild(temp);
    temp.submit();
    return temp;
}