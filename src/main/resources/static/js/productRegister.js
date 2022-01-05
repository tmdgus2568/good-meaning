/**
 * register,에 대한 JS
 */

//등록시 금액에 , 제거
function checkInputData() {
	if(confirm("상품을 등록하시겠습니까?")) {
		var productPrice = $("#productPrice").val();
		$("#productPrice").val(productPrice.replace(",", ""));
		var optionPrice = document.getElementsByName("optionPrice");
		console.log(optionPrice);
		for (var idx = 0; idx < optionPrice.length; idx++) {
		optionPrice[idx].value = optionPrice[idx].value.replace(",", "");
		}
		alert('상품 1건이 등록되었습니다.');
		return true;
	} 
		alert('상품등록이 취소되었습니다.');
		return false;
	
}


// 옵션 사용/사용안함 선택
$('#optionNo').click(function() {
	$('#demo').css("display", "none")
	$('#optionCategory').find('option:first').attr('selected', true);
	$('#optionName').val('');
	$('#optionPrice').val('');
	$("#writeOther").val('');
	$("#writeOther").attr("type", "hidden");
});

$("#optionYes").click(function() {
	$("#demo").css("display", "block");
});

// 옵션 중 other 선택 시..
$("select[name='optionCategory']").change(function() { //#optionCategory
	var optionVal = $(this).val();
	if (optionVal == "Other") {
		$("#writeOther").attr("type", "text");
	} else {
		$("#writeOther").attr("type", "hidden");
	}
});

// 파일 삭제시 첨부파일 이름 재설정
$(".btnDelete").click(function() {
	var num = this.id;
	num = num.charAt(num.length - 1);
	$(this).parent().parent().find("input[type='file']").val('');
	var a = $(this).parent().parent().find("label").html('Choose File' + num);
	console.log(a);
});

//대표이미지 파일1 requried 
$("#productMainimg1").attr("required", true);

//첨부파일 이름 보이기
$(".custom-file-input").on("change", function() {
	var fileName = $(this).val().split("\\").pop();
	$(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});

//카테고리 checkbox 선택시 required 해제
var tmp = document.getElementsByName('productCategory');
console.log(tmp);
$(".productCategory").change(function() {
	for (var i = 0; i < tmp.length; i++) {
		console.log(tmp[i].checked);
		if (tmp[i].checked == true) {
			$(".productCategory").attr("required", false);
			break;
		} else if (i == tmp.length - 1) {
			$(".productCategory").attr("required", true);
		}
	}
});

//옵션추가 클릭시 옵션명/추가금액 추가
$(".deleteBtn").css("display", "none");

var cloneCount = 1;
$("#addOptionBtn").click(function() {
	var copyObj = $(this).parent().parent().clone(true);
	copyObj.appendTo("#addOptions");
	$(copyObj).find("#optionName").val("");
	$(copyObj).find("#optionPrice").val(""); //attr("id", "objPrice" + cloneCount).
	/* $(objPrice).keyup(function(){
		var value = $(objPrice).val().replace(/[^0-9]/g,"").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    	(this).value = value;
	}); */
	cloneCount++
	//삭제버튼 1번째 x 2번째부터 o
	$(".deleteBtn").css("display", "block");
	$(".deleteBtn").first().css("display", "none");
	//클론시 옵션라벨 보여주지 않기
	$(".optLable").css("display", "none");
	$(".optLable").slice(0, 2).css("display", "block");
	//삭제할때 한줄 한번에 없애기
	$('.deleteBtn').on('click', function() {
		$(this).parent().parent().remove();
	});
});

// 판매가 , keyup
$("#productPrice").keyup(function() {
	var value = $("#productPrice").val().replace(/[^0-9]/g, "").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	(this).value = value;
});

// 판매가 , keyup
function onKeyup(obj) {
	var value = $(obj).val().replace(/[^0-9]/g, "").toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
	$(obj).val(value);
}
