/**
 * 
 */
var productManager = (function(){
	var getAll = function(obj, callback){ 
		//조회?입력?삭제? 알수없어.. > obj변수로 받음. 수행 수 해야하는 함수들이 다달라 > callback으로 부르기
		console.log("모든댓글..getAll호출");
		$.getJSON( "/myapp/replies/list/"+ obj ,  callback);
	}
	
	var replyAdd = function(obj, callback){ 
		console.log("댓글추가(내용, 작성자, bno)..add호출");
		$.ajax({
			url: "/myapp/replies/" + obj.bno,
			type:"post",
			data: JSON.stringify(obj), //obj를 json으로 바꿔줌: stringify
			dataType: "json",
			contentType: "application/json", //네트워크상에서 주고받는 형태
			success: callback
		});
	}
	var replyUpdate = function(obj, callback){ 
		console.log("댓글수정 rno..update호출");
		$.ajax({
			url: "/myapp/replies/" + obj.bno + "/" + obj.rno,
			type:"put", //restcontroller에서 수정을 put이라고 함
			data: JSON.stringify(obj), //obj를 json으로 바꿔줌: stringify
			dataType: "json",
			contentType: "application/json", //네트워크상에서 data 주고받는 형태
			success: callback
		});
	}
	var replyDelete = function(obj, callback){ 
		console.log("댓글삭제..delete호출");
		$.ajax({
			url: "/myapp/replies/" + obj.bno + "/" + obj.rno,
			type:"delete", //주소가 같아도 타입이 달라서 가능
			//data: url로 보내므로 굳이 data 보내지 않아도 됨(보드번호, 댓글번호만 있어도 삭제가 되니까)
			success: callback
		});
	}
	// 보낼객체(속성: 함수이름)
	return {
		"getAll": getAll, 
		replyAdd:replyAdd, 
		replyUpdate:replyUpdate, 
		replyDelete:replyDelete
	}; 
})();