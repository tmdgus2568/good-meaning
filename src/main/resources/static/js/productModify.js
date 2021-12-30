/**
 * detailModal에 대한 JS
 */
 
//등록시 금액에 , 제거
 
function checkInputData(){
	var productPrice = $("#productPrice").val();
	$("#productPrice").val(productPrice.replace(",",""));
	var optionPrice = document.getElementsByName("optionPrice");
	console.log(optionPrice);
	for(var idx = 0; idx < optionPrice.length; idx++){
		optionPrice[idx].value = optionPrice[idx].value.replace(",","");
	}
	alert("상품을 수정하시겠습니까?");
	//form에 페이징, 검색어, sort부분도 함께 보낸다.
	$("#myfrm").find("input[name='page']").val($("#f1").find("input[name='page']").val());
	$("#myfrm").find("input[name='size']").val($("#f1").find("input[name='size']").val());
	$("#myfrm").find("input[name='colmnName']").val($("#f1").find("input[name='colmnName']").val());
	for(var i = 0; i <5; i++) {
		$("#myfrm").find("input[name='type']").eq(i).val($("#f1").find("input[name='type']").eq(i).val());
		$("#myfrm").find("input[name='keyword']").eq(i).val($("#f1").find("input[name='keyword']").eq(i).val());
	}
	
	 
	
	
	
}

// 옵션 사용안함 선택
$('#optionNo').click(function(){
	$('#demo').css("display", "none")
/*	$("#optionCategory option:eq(0)").prop("selected", true);
	$("#writeOther").val('');
	$("#optionPrice").val('');
	$("#optionName").val('');
	$("#writeOther").attr("type", "hidden");
	
	//$("#addOptions").empty();
	let optionPrice = document.getElementsByName("optionPrice");
	let optionName = document.getElementsByName("optionName");
	for(var idx = 0; idx < optionPrice.length; idx++){
		optionPrice[idx].value = "";
		optionName[idx].value = "";
	}*/
});

// 옵션 사용함 선택
$("#optionYes").click(function(){
	$("#demo").css("display", "block");
});

// 옵션 중 other 선택 시..
$("select[name='optionCategory']").change(function(){ //#optionCategory
	var optionVal = $(this).val();
	if(optionVal == "Other") {
		$("#writeOther").attr("type", "text");
	} else {
		$("#writeOther").attr("type", "hidden");
	}
});

// 파일 삭제시 첨부파일 이름 재설정
$(".btnDelete").click(function () {
	var num = this.id;
	num = num.charAt(num.length-1);
	$(this).parent().parent().find("input[type='file']").val('');
	var a = $(this).parent().parent().find("label").html('Choose File' + num);
	console.log(a);
});


//첨부파일 이름 보이기
$(".custom-file-input").on("change", function() {
	var fileName = $(this).val().split("\\").pop();
	$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});

			
//옵션추가 클릭시 옵션명/추가금액 추가
$(".deleteBtn").css("display", "none");

var cloneCount = 1;
$("#addOptionBtn").click(function(){
	var copyObj = $(this).parent().parent().clone(true);
	copyObj.appendTo("#addOptions");
	$(copyObj).find("#optionName").val("");
	$(copyObj).find("#optionNum").val("");
	$(copyObj).find("#optionPrice").val("");
	cloneCount++
	 //삭제버튼 1번째 x 2번째부터 o
	$(".deleteBtn").css("display", "block");
	$(".deleteBtn").first().css("display", "none");
	 //클론시 옵션라벨 보여주지 않기
	$(".optLable").css("display", "none");
	$(".optLable").slice(0, 2).css("display", "block");
	 //삭제할때 한줄 한번에 없애기
	$('.deleteBtn').on('click', function () { 
/*		var optionNumber = $(this).parent().next().children().val();
        if(optionNumber != null || optionNumber != "") { //옵션번호
        	console.log($(this).parent().next().children().val());
        	console.log($(this).parent().parent().find(".optionPrice").val());
        	console.log($(this).parent().parent().find("#optionName").val());
        	$(this).parent().parent().find(".optionPrice").val("");
        	$(this).parent().parent().find("#optionName").val("");
        } */
		$(this).parent().parent().remove ();
     });
});

// 판매가 , keyup
$("#productPrice").keyup(function(){
	var value = $("#productPrice").val().replace(/[^0-9]/g,"").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	(this).value = value;
});

// 판매가 , keyup
function onKeyup(obj) {
	var value = $(obj).val().replace(/[^0-9]/g,"").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	$(obj).val(value);
}

//상품 삭제 시
$("#delProdBtn").click(function(){
	alert('상품을 삭제하시겠습니까?aaaaaaa');
	var obj = $("#f1");
	
	$("#f1").attr("action", "deleteProduct");
	$("#f1").append("<input type='text' name='productNum' value=" + $("#productNum").val() + ">");
	console.log($("#f1"));
	$("#f1").submit();
	//location.href="deleteProduct?productNum=" + $("#productNum").val();
})

 var uploadFile = $('.fileBox .uploadBtn');
 $(uploadFile).on('change', function(event){
	var filename = $(this)[0].files[0].name;
	$(this).parent().find('.fileName').val(filename);
});  