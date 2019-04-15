const mealAjaxUrl = "ajax/profile/meals/";

function updateFilteredTable() {
    $.ajax({
        type: "GET",
        url: mealAjaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(updateTableByData);
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(mealAjaxUrl, updateTableByData);
}

$(function () {
    makeEditable({
        ajaxUrl: mealAjaxUrl,
        datatableOpts: {
            "columns": [
                {
                    "data": "dateTime",
                    "render": function (date, type, row) {
                        if (type === 'display') {
                            return date.replace('T', ' ').substr(0, 16);
                        }
                        return date;
                    }
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderEditBtn
                },
                {
                    "orderable": false,
                    "defaultContent": "",
                    "render": renderDeleteBtn
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ],
            "createdRow": function (row, data, dataIndex) {
                $(row).attr("data-mealExcess", data.excess);
            }
        },
        updateTable: updateFilteredTable
    });
});

$('#startDate').datetimepicker({
    timepicker:false,
    format:'Y-m-d',
    onShow:function(ct){
        this.setOptions({
            maxDate:$('#endDate').val()?$('#endDate').val():false
        })
    }
});

$('#endDate').datetimepicker({
    timepicker:false,
    format:'Y-m-d',
    onShow:function(ct){
        this.setOptions({
            minDate:$('#startDate').val()?$('#startDate').val():false
        })
    }
});

$('#startTime').datetimepicker({
    datepicker:false,
    format:'H:i',
    onShow:function(ct){
        this.setOptions({
            maxTime:$('#endTime').val()?$('#endTime').val():false
        })
    }
});

$('#endTime').datetimepicker({
    datepicker:false,
    format:'H:i',
    onShow:function(ct){
        this.setOptions({
            minTime:$('#startTime').val()?$('#startTime').val():false
        })
    }
});

$('#dateTime').datetimepicker({
    format: 'Y-m-d H:i'
});