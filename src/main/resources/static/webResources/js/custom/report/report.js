/**
 * 
 */
$(document).ready(function(){
	$(".small-charts-left").mouseover(function () {
        $(this).attr('class', 'small-charts-left-shadow');
        $(".small-charts-left-shadow").mouseout(function () {
            $(this).attr('class', 'small-charts-left');
        });
    });
    $(".small-charts-right").mouseover(function () {
        $(this).attr('class', 'small-charts-right-shadow');
        $(".small-charts-right-shadow").mouseout(function () {
            $(this).attr('class', 'small-charts-right');
        });
    });
    
    $("#one").click(function(){
    	window.location.href="/report/management/one/view"
    })
    
    $("#two").click(function(){
    	window.location.href="/report/management/two/view"
    })
    
    $("#three").click(function(){
    	window.location.href="/report/management/three/view"
    })
    
    $("#four").click(function(){
    	window.location.href="/report/management/four/view"
    })
    
})