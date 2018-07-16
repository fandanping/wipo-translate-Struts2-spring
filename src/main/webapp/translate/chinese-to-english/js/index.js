

$(function(){
   $.WEB_ROOT = "/translate"
	
	var translate=function(){
		 $.ajax({
                type: "POST",
                dataType: "json",
                url: $.WEB_ROOT + "/translate/translateAC.do" ,
                data: $('#translateForm').serialize(),
                success: function (result) {
			        
                    console.log("success");
                    console.log("耗时"+result.timeConsuming);
                    $("#translateResult").val(result.translateresult);
                },
                error : function(XMLHttpRequest,textStatus,errorThrown) {
                    alert("异常！");
                    alert(XMLHttpRequest.status);//200
                    alert(XMLHttpRequest.readyState);//4
                    alert(textStatus);//error
                }
            });
	}

	
	$("#translateBtn").on("click",translate)
})