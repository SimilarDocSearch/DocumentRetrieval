/**
 * 
 */
$body = $("body");

$(document).on({
	ajaxStart : function() {
		$body.addClass("loading");
	},
	ajaxStop : function() {
		$body.removeClass("loading");
	}
});


//function removeSelection(e) {
//	$.ajax({
//		type : "POST",
//		url : "deleteSelection.jsp",
//		data : "category=" + $(e).closest('ul').attr('id') + "&selection="+encodeURIComponent($(e).closest('li').text()),
//		success : function(data) {
//			
//		}
//	});
//	$(e).closest('li').remove();
//}

$( document ).ready(function() {
	
//	$('.itemDelete').on('click', function() {
//		$.ajax({
//			type : "POST",
//			url : "deleteSelection.jsp",
//			data : "category=" + $(this).closest('ul').attr('id') + "&selection="+encodeURIComponent($(this).closest('li').text()),
//			success : function(data) {
//				
//			}
//		});
//		$(this).closest('li').remove();
//	});
//	 $('.checkboxM').change(function() {
//	        if($(this).is(":checked")) {
//	            $(this).attr("checked", true);
//	        }
//	        else{
//	        	 $(this).attr("checked", false);
//	        }
//	        $(this).val($(this).is(':checked'));        
//	    });
//	$('ul.dropdown-menu [data-toggle=dropdown]').on('click', function(event) {
//	    // Avoid following the href location when clicking
//	    event.preventDefault(); 
//	    // Avoid having the menu to close when clicking
//	    event.stopPropagation(); 
//	    // If a menu is already open we close it
//	    //$('ul.dropdown-menu [data-toggle=dropdown]').parent().removeClass('open');
//	    // opening the one you clicked on
//	    $(this).parent().addClass('open');
//
//	    var menu = $(this).parent().find("ul");
//	    var menupos = menu.offset();
//	  
//	    if ((menupos.left + menu.width()) + 30 > $(window).width()) {
//	        var newpos = - menu.width();      
//	    } else {
//	        var newpos = $(this).parent().width();
//	    }
//	    menu.css({ left:newpos });
//
//	});
//	
//	if($('#dataSaved').val()==="true"){
//		$("#showAlert").text("Saved Material Successfully").fadeIn().delay(2000).fadeOut();
//	}
	});

function getSearchResult(){
	var number = $('#numberOfDocs').val();
	var query = $('#query').val();
	var method = $("#method").val();
	
	
	
	
	
	var rankedDBs = $('#rankedDBs').dataTable({
		"ajax": {
			"url": "getRankedDatabases.jsp?number="+ number,
			"dataSrc": "rankedDBs",
			"type": "GET"
			},
		"columns" : [ {
			"title" : "Name"
		}, {
			"title" : "Rank"
		}, {
			"title" : "Path"
		}, {
			"title" : "Representative"
		} ],
		"createdRow": function ( row, data, index ) {
                $('td', row).eq(2).html('<a href="' + data[2] + '"> '+data[2] +'</a>');
        }

	});
	
var docList = $('#docList').dataTable({
	"ajax": {
		"url": "getDocuments.jsp?number="+ number,
		"dataSrc": "docList",
		"type": "GET"
		},
		"columns" : [ {
			"title" : "Name"
		}, {
			"title" : "Normalized weight"
		}, {
			"title" : "Similarity Percentage"
		}, {
			"title" : "Path"
		} ],
		"createdRow": function ( row, data, index ) {
            $('td', row).eq(3).html('<a href="' + data[3] + '"> '+data[3] +' </a>');
    }

	});
}


function changeCheck(){
	
	alert('check called');
}
function positionCodeSel() {
	var selectBox = document.getElementById("posCode");
	var selectedValue = selectBox.options[selectBox.selectedIndex].value;
	var count = parseInt(selectedValue.split(",")[1]);
	$("#pCount").val(count);
	// $("#tCount").val(parseInt(($("#tCount").val()==null ||
	// $("#tCount").val()== "")?"0":$("#tCount").val())+count);
}

function getPatronList() {
	var selectBox = document.getElementById("posCode");
	var selectedValue = selectBox.options[selectBox.selectedIndex].text;
	var tempSelectedValue = selectBox.options[selectBox.selectedIndex].text;
	var selectedValue = tempSelectedValue.split(" -- ")[0];
	if (selectedValue === "Select") {
		$("#showAlert").text("Please select a position code").fadeIn().delay(
				2000).fadeOut();
	} else {
		$.ajax({
			type : "POST",
			url : "getPatronsList.jsp",
			data : "posCodeSelected=" + selectedValue,
			success : function(data) {
				$('#patronList').empty();
				var returnMessage = data;
				var patronNames = data.split("|");
				for (var i = 0; i < patronNames.length; i++) {
					$('#patronList').append('<li>' + patronNames[i] + '</li>');
				}
			}
		});
	}
}

function getLabelUsingMailingIDList() {
	var selectBox = document.getElementById("MailingID");
	var selectedValue = selectBox.options[selectBox.selectedIndex].text;

	if (selectedValue === "Select Mailing ID") {
		$("#showAlert").text("Please select a Mailing ID").fadeIn().delay(2000)
				.fadeOut();
	} else {
		$
				.ajax({
					type : "POST",
					url : "getLabels.jsp",
					data : "mailingID=" + selectedValue, // posCodeSelected
					success : function(data) {
						var dataset = data;
						if (dataset == 0) {
							$("#showAlert").text("No Records found").fadeIn()
									.delay(3000).fadeOut();
								}
						var len = dataset.substring(0, 2);
						console.log(len);
						ptr1 = 2;
						ptr2 = data.indexOf("==");
						
						var myArray = new Array(len);
						for (var i = 0; i < len; i++) {
							myArray[i] = new Array(11); // 12 columns

						}

						for (var i = 0; i < len; i++) {
							
							for (var j = 0; j < myArray[i].length; j++) {

								myArray[i][j] = data.substring(ptr1, ptr2)
										.trim();
								ptr1 = ptr2 + 2;
								ptr2 = data.indexOf("==", ptr2 + 2);
							}

						}

						console.log("myArray", myArray);
						if (myArray[0][0] == 0) {
							console.log("yes");
							myArray = null;

						}

						var mytable = $('#printIDs').dataTable({
							destroy : true,
							"data" : myArray,

							"columns" : [ {
								"title" : "Salutation"
							}, {
								"title" : "Firstname"
							}, {
								"title" : "Lastname"
							}, {
								"title" : "Organization"
							}, {
								"title" : "Address1"
							}, {
								"title" : "Address2"
							}, {
								"title" : "Address3"
							}, {
								"title" : "Address4"
							}, {
								"title" : "City"
							}, {
								"title" : "State"
							}, {
								"title" : "Zip"
							} ]

						});
						$('#export-to-excel')
								.click(
										function() {
		headerArray = [ "Salutation","Firstname", "Lastname","Organization", "Address1",
													"Address2", "Address3",
													"Address4", "City",
													"State", "Zip" ];
											myArray.unshift(headerArray);
											var csvContent = "data:text/csv;charset=utf-8,";
											myArray
													.forEach(function(
															infoArray, index) {
														dataString = infoArray
																.join(",");
														console.log(dataString);
														csvContent += index < infoArray.length ? dataString
																+ "\n"
																: dataString;

													});
											var encodedUri = encodeURI(csvContent);
											var link = document
													.createElement("a");
											link.setAttribute("href",
													encodedUri);
											link.setAttribute("download",
													"MailingID_"
															+ selectedValue
															+ "_data.csv");
											link.click(); 

										});
						

					},
					error : function(response) {
						$("#showAlert").text(
								"Select Mailing ID for Exporting.. ").fadeIn()
								.delay(3000).fadeOut();
					}
				});

	}

}

function addToGrpSel() {

	var selectBox = document.getElementById("posCode");
	var tempSelectedValue = selectBox.options[selectBox.selectedIndex].text;
	var selectedValue = tempSelectedValue.split(" -- ")[0];
	if (selectedValue === "Select") {
		$("#showAlert").text("Please select a position code").fadeIn().delay(
				2000).fadeOut();
		return false;
	}

	var itemFound = false;
	$('#grpSelList li').each(function() {

		if ($(this).text() === selectedValue) {
			itemFound = true;
		}

	});

	if (itemFound == true) {
		$("#showAlert").text("Group already exists in the selection").fadeIn()
				.delay(2000).fadeOut();
	} else {
		$.ajax({
			type : "POST",
			url : "addGroupSelections.jsp",
			data : "posCodeSelected=" + encodeURIComponent(selectedValue),
			success : function(data) {

			}
		});
		var selectedCount = selectBox.options[selectBox.selectedIndex].value;
		var count = parseInt(selectedCount.split(",")[1]);
		$("#tCount")
				.val(
						parseInt(($("#tCount").val() == null || $("#tCount")
								.val() == "") ? "0" : $("#tCount").val())
								+ count);
		$('#grpSelList').append('<li style="margin-bottom: 3px">' + selectedValue + '<button type="button" style="float: right" class="btn btn-xs btn-danger itemDelete" aria-label="Left Align" onclick="removeSelection(this)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button></li>');
	}

}

function setPatronList() {

}

function addMatToSel() {
	var selectBox = document.getElementById("materialSel");
	var selectedValue = selectBox.options[selectBox.selectedIndex].text;
	if (selectedValue === "Select") {
		$("#showAlert").text("Please select a Material").fadeIn().delay(2000)
				.fadeOut();
		return false;
	}

	var itemFound = false;
	$('#matSelList li').each(function() {

		if ($(this).text() === selectedValue) {
			itemFound = true;
		}

	});

	if (itemFound == true) {
		$("#showAlert").text("Material already exists in the selection")
				.fadeIn().delay(2000).fadeOut();
	} else {
		$.ajax({
			type : "POST",
			url : "addMaterialSelections.jsp",
			data : "matSelected=" + encodeURIComponent(selectedValue),
			success : function(data) {

			}
		});
		$('#matSelList').append('<li  style="margin-bottom: 3px">' + selectedValue + '<button type="button" style="float: right" class="btn btn-xs btn-danger itemDelete" aria-label="Left Align" onclick="removeSelection(this)"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></button></li>');
	}

}

var Example = (function() {
	"use strict";

	var elem, hideHandler, that = {};

	that.init = function(options) {
		elem = $(options.selector);
	};

	that.show = function(text) {
		clearTimeout(hideHandler);

		elem.find("span").html(text);
		elem.delay(200).fadeIn().delay(4000).fadeOut();
	};

	return that;
}());

// Mangesh-->
function checkUserLoginedIndex() {
	$.ajax({
		type : "POST",
		url : "chechAccess.do",
		success : function(data) {
			try {
				var returnMessage = data;
				console.log(returnMessage);
				if (returnMessage == 'false') {
					if (!$('#CreateListAnchor').hasClass('disabled')) {
						$('#CreateListAnchor').addClass('disabled');
						$('#CreateListAnchor').addClass('disabled');
						$("#CreateListAnchor").removeAttr('href');
						$('#warningButn').show();
					}
				} else {
					if ($('#CreateListAnchor').hasClass('disabled')) {
						$('#CreateListAnchor').removeClass('disabled');
						$('#CreateListAnchor').attr("href", "init.do")
						$('#warningButn').hide();
					}
				}
			} catch (err) {

				console.error(err);
			}

		}
	});

}
function checkUserLoginedStart() {
	$
			.ajax({
				type : "POST",
				url : "chechAccess.do",
				success : function(data) {
					try {
						var returnMessage = data;
						console.log(returnMessage);
						if (returnMessage == 'false') {
							alert("Your Session has been terminated\n\tTry Again Later!!");
							window.location = "Index.jsp"
						}
					} catch (err) {

						console.error(err);
					}

				}
			});

}
function invalidateSession() {
	$("#resetForm").val("true");
	$
			.ajax({
				type : "POST",
				url : "invalS.do",
				success : function(data) {
					try {
						// var returnMessage = data;
						$("#showAlert")
								.text(
										"Session has been invalidated by You.\n You can access Create Mailing List Page.")
								.fadeIn().delay(2000).fadeOut(5000);

					} catch (err) {
						console.error(err);
					}

				}
			});

}

// -->Mangesh

function resetForm() {
	$.ajax({
		type : "POST",
		url : "resetForm.jsp",
		success : function() {
			$("#resetForm").val("true");
			window.location.reload();
		}
	});

}


function saveForm(){
	if ($('#grpSelList li').length == 0 && $('#matSelList li').length == 0)  {
		$("#showAlert").text("Please add atleast one Position Code and Material to selection").fadeIn().delay(3000)
				.fadeOut();
		return false;
	}
	if ($('#grpSelList li').length == 0)  {
		$("#showAlert").text("Please add atleast one Position Code to selection").fadeIn().delay(2000)
				.fadeOut();
		return false;
	}
	if ($('#matSelList li').length == 0)  {
		$("#showAlert").text("Please add atleast one Material to selection").fadeIn().delay(2000)
				.fadeOut();
		return false;
	}
	// alert($('#curMailId').val());

	$(".loader").fadeIn("fast");
	$.ajax({
		type : "POST",
		url : "saveForm.jsp",
		data : "mailingID=" + $('#curMailId').val()+"&mIDDesc="+$('#mIDDesc').val(),
		success : function() {
			$(".loader").fadeOut("slow");
			$("#showAlert").text("Data Saved successfully").fadeIn()
					.delay(2000).fadeOut();

		}
	});
}

function getMaterialViewList() {

				var mytable = $('#printMaterials').dataTable({
					"ajax": {
						"url": "getMaterials.jsp",
						"dataSrc": "data",
						"type": "GET"
						},
					"columns" : [ {
						"title" : "ID"
					}, {
						"title" : "MaterialName"
					}, {
						"title" : "Format"
					}, {
						"title" : "DefaultDeliveryMethod"
					}, {
						"title" : "Project"
					}, {
						"title" : "Electronic"
					}, {
						"title" : "Active"
					} ],
					"createdRow": function ( row, data, index ) {
			                $('td', row).eq(1).html('<a href="saveMaterial?id=' + data[0] + '">' +
			                        data[1] + '</a>');
			        }

				});
			}

function getDocumentList() {

	var mytable = $('#printMaterials').dataTable({
		"ajax": {
			"url": "getMaterials.jsp",
			"dataSrc": "data",
			"type": "GET"
			},
		"columns" : [ {
			"title" : "ID"
		}, {
			"title" : "MaterialName"
		}, {
			"title" : "Format"
		}, {
			"title" : "DefaultDeliveryMethod"
		}, {
			"title" : "Project"
		}, {
			"title" : "Electronic"
		}, {
			"title" : "Active"
		} ],
		"createdRow": function ( row, data, index ) {
                $('td', row).eq(1).html('<a href="saveMaterial?id=' + data[0] + '">' +
                        data[1] + '</a>');
        }

	});
}
	
//	function saveNewMaterial(){
//		var proceed = true;
//		$('form#newMaterialForm').find('input').each(function(){
//		    if($(this).prop('required')){
//		    	if($(this).val()=='undefined' || $(this).val()===''){
//		    		proceed = false;
//		    	}
//		    }
//		});
//		if(!proceed){
//			$("#showAlert").text("Please fill out all the fields").fadeIn().delay(2000)
//			.fadeOut();
//	    	return false;
//		}
//		else{
//		$(".loader").fadeIn("fast");
//		$.ajax({
//			type : "POST",
//			url : "saveNewMaterial.jsp",
//			data : "matName=" + $('#matName').val()+"&format="+$('#format').val()+"&delMethod="+$('#delMethod').val()+"&project="+$('#project').val()+"&electronic="+$('#electronic').val()+"&active="+$('#active').val(),
//			success : function() {
//				$(".loader").fadeOut("slow");
//				$("#showAlert").text("Data Saved successfully").fadeIn()
//						.delay(2000).fadeOut();
//
//			}
//		});
//		}
//	}
	
