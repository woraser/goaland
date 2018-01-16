$(document).ready(function(){
    var integral = new Vue({
        el: '#integral',
        data: {
            integralData:{},
        },
        methods : {
            viewTasks : function(){
                window.location.href="/customerServiceProcess/runtimeTask/view?menuId=customServiceProcessTaskToDo"
            },
            select : function(operate){
                var param = {
                    "integral.id" : integral.integralData.id,
                    "operate" : operate,
                    "showAttributes":"operate,score,reason,operateTime",
                };
                $.ajax({
                    url : "/scoreRecord/management/data/REMOTE",
                    data : param,
                    type : 'get',
                    dataType : 'json',
                    success : function( data ) {
                        integral.integralData.scoreRecords = data.content
                    }
                });
            }
        },
    })

    var loadIntegralData = function(data){
        $.ajax({
            url : "/integral/management/data/one",
            data : data,
            type : 'get',
            dataType : 'json',
            success : function( data ) {
                integral.integralData = data
            }
        });
    }

    loadIntegralData({
        "account.id" : $("#currentAccountId").val(),
        "showAttributes":"id,total,scoreRecords*.(operate,score,reason,operateTime)",
    });

    $('.test').on('click', function () {
        $(this).toggleClass('active');
    })

})