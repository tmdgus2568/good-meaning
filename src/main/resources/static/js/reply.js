/**
 * 즉시실행함수
 * obj는 변수
 * callback은 함수
 */

var replyManager = (function() {
	var getAll = function(obj, callback) {
		console.log("모든댓글...getAll호출 .....")
		$.getJSON("/myapp/replies/list/" + obj, callback);
	}

	var replyAdd = function(obj, callback) {
		console.log("댓글추가 내용,작성자,bno.....")
		$.ajax({
			url: "/myapp/replies/" + obj.bno,
			type: "post",
			data: JSON.stringify(obj),
			dataType: "json",
			contentType: "application/json",
			success: callback
		});
	}
	var replyUpdate = function(obj, callback) {
		console.log("댓글수정 rno.....")
		$.ajax({
			url: "/myapp/replies/" + obj.bno + "/" + obj.rno,
			type: "put",
			data: JSON.stringify(obj),
			dataType: "json",
			contentType: "application/json",
			success: callback
		});
	}
	var replyDelete = function(obj, callback) {
		console.log("댓글삭제 rno.....")
		$.ajax({
			url: "/myapp/replies/" + obj.bno + "/" + obj.rno,
			type: "delete",
			success: callback
		});
	}

	return {
		"getAll": getAll
		, replyAdd: replyAdd
		, replyUpdate: replyUpdate
		, replyDelete: replyDelete
	};
})();