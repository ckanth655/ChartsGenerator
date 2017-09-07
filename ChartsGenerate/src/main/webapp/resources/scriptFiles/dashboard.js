var indices = [];
var s = 1;
$(document).ready(function() {

	$.ajax({
		url : "GetIndices",
		dataType : 'json',
		cache : false,
		error : function() {
			alert("Error Occured indices");
		},
		success : function(data) {

			for (var i = 0; i < data.length; i++) {
				indices.push(data[i])
			}
			// console.log(indices);
		}
	});

});

function getFields(index, id) {
	// console.log(index);
	// alert(id);

	var field = [];

	$.ajax({
		url : "GetIndices",
		dataType : 'json',
		async : false,
		data : {
			value : index
		},
		error : function() {
			alert("Error Occured field");
		},
		success : function(data) {

			for (var i = 0; i < data.length; i++) {
				field.push(data[i])
			}
			// console.log(field.length);

			// console.log(data);
		}
	});

	var selectHTML = "<option value='' disabled selected>Select field</option>";
	for (var i = 0; i < field.length; i = i + 1) {

		selectHTML += "<option value='" + field[i] + "'>" + field[i]
				+ "</option>";

	}

	$("#field_" + id).empty();
	$("#field_" + id).append(selectHTML);

}

var chartType = [ "bar", "line", "pie", "polarArea", "horizontalBar",
		"doughnut" ];

(function($) {
	"use strict";

	var itemTemplate = $('.example-template').detach(), editArea = $('.edit-area'), itemNumber = 1;

	$(document).on(
			'click',
			'.edit-area .add',
			function(event) {
				var item = itemTemplate.clone();
				item.find('[id]').attr('id', function() {
					return $(this).attr('id') + '_' + itemNumber;
				});

				item.find('[onchange]').attr(
						'onchange',
						function() {
							return $(this).attr('onchange')
									+ 'getFields(value,' + itemNumber + ')';
						});

				item.appendTo(editArea);
				debugger;
				GetDynamicTextBox(itemNumber);

				++itemNumber;
			});

	$(document).on('click', '.edit-area .rem', function(event) {
		editArea.children('.example-template').last().remove();
	});

	$(document).on(
			'click',
			'.edit-area .del',
			function(event) {
				var target = $(event.target), row = target
						.closest('.example-template');
				row.remove();
			});
}(jQuery));

function GetDynamicTextBox(value) {
	debugger;
	var selectHTML = "";

	selectHTML += "<option value='' disabled selected>Select Index</option>";
	for (i = 0; i < indices.length; i = i + 1) {
		selectHTML += "<option  value='" + indices[i] + "'>" + indices[i]
				+ "</option>";
	}

	$('#index_' + value).empty();
	$('#index_' + value).append(selectHTML);

	var selectTypeHTML = "<option value='' disabled selected>Select chart type</option>";
	for (i = 0; i < chartType.length; i = i + 1) {
		selectTypeHTML += "<option value='" + chartType[i] + "'>"
				+ chartType[i] + "</option>";
	}

	$('#chartType_' + value).empty();
	$('#chartType_' + value).append(selectTypeHTML);

}


function dashBoards(value) {

	s = 1;

	var len = document.getElementsByClassName("index").length;

	var index = [];
	var field = [];
	var chartType = [];
	for (var i = 0; i < len; i = i + 1) {
		index.push(document.getElementsByClassName("index")[i].value);
		field.push(document.getElementsByClassName("field")[i].value);
		chartType.push(document.getElementsByClassName("charttype")[i].value);
	}

	console.log(index);
	console.log(field);
	console.log(chartType);

	table(chartType);

	$.ajax({
		url : "RTIDataAgg",
		dataType : 'json',
		async : false,
		data : {
			indices : index,
			fields : field,
			chartTypes : chartType,
		},
		error : function() {
			alert("Error Occured dashBoards()");
		},
		success : function(data) {
			chartData(data);
		}
	});

}


function DrillDashBoards(value, fld) {
	
	
	s = 1;

	var len = document.getElementsByClassName("index").length;

	var index = [];
	var field = [];
	var chartType = [];
	for (var i = 0; i < len; i = i + 1) {
		index.push(document.getElementsByClassName("index")[i].value);
		field.push(document.getElementsByClassName("field")[i].value);
		chartType.push(document.getElementsByClassName("charttype")[i].value);
	}

	console.log(index);
	console.log(field);
	console.log(chartType);

	table(chartType);

	$.ajax({
		url : "RTIDataAgg",
		dataType : 'json',
		async : false,
		data : {
			indices : index,
			fields : field,
			chartTypes : chartType,
			drill : value,
			field : fld
		},
		error : function() {
			alert("Error Occured drilldashBoards()");
		},
		success : function(data) {
			chartData(data);
		}
	});

}

function chartData(data) {

	for (var i = 0; i < data.length; i++) {
		if (data[i].chartType == 'pie') {

			charts(data[i].agg, data[i].index, 'pie', data[i].field);

		} else if (data[i].chartType == 'bar') {

			charts(data[i].agg, data[i].index, 'bar', data[i].field);

		} else if (data[i].chartType == 'line') {

			charts(data[i].agg, data[i].index, 'line', data[i].field);

		} else if (data[i].chartType == 'polarArea') {

			charts(data[i].agg, data[i].index, 'polarArea', data[i].field);

		} else if (data[i].chartType == 'horizontalBar') {

			charts(data[i].agg, data[i].index, 'horizontalBar', data[i].field);

		} else {
			charts(data[i].agg, data[i].index, 'doughnut', data[i].field);
		}

	}

}

function table(chartType) {

	var selectHTML = "";
	for (var i = 1; i <= chartType.length; i = i + 1) {

		selectHTML += '<tr><td><canvas id="chart_' + i
				+ '" width="800" height="450"></canvas></td></tr>'

	}

	$("#Table").empty();

	$("#Table").append(selectHTML);
}

function charts(agg, index, chartType, field) {

	
	var chartFields = [];
	var chartData = [];

	for (var i = 0; i < agg.length; i++) {
		for (var j = 0; j < agg[i].length; j++) {

			chartFields.push(agg[i][j].key);
			chartData.push(agg[i][j].value);
		}
	}

	var colors = [ "#3e95cd", "#8e5ea2", "#3cba9f", "#e8c3b9", "#c45850",
			"#ee95cd", "#5e5ea2", "#8e95cd", "#3bbea2" ];

	if (chartType == 'line') {
		colors = "#3e95cd"
	}

	field = field.replace(".keyword", "");

	new Chart(document.getElementById("chart_" + s), {
		type : chartType,
		data : {
			labels : chartFields,
			datasets : [ {
				label : field + " wise Aggregation data",
				backgroundColor : colors,
				data : chartData,
			// fill: false
			} ]
		},
		options : {
			tooltips : {

				callbacks : {
					label : function(tooltipItem) {

						return chartFields[tooltipItem.index] + "::"
								+ chartData[tooltipItem.index];
					}
				}
			},
			title : {
				display : true,
				text : index + ' Data'
			},
			onClick : function(evt, item) {
				console.log(item);
				// console.log(chartFields[item[0]._index]);
				DrillDashBoards(chartFields[item[0]._index], field);
				//$("#back").append('<button style="color: white;background-color: teal;">'+field+"::"+chartFields[item[0]._index]+'&nbsp;&nbsp;<span class="closebtn" onclick="this.parentElement.style.display=\'none\';">&times;</span></button>&nbsp;&nbsp;&nbsp;&nbsp;');
			}
		}
	});

	s++;

}
