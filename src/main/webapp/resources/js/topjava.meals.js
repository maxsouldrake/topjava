const ajaxUrl = "ajax/meals/";

function filteredTable() {
    event.preventDefault();
    $.ajax({
        type: "GET",
        url: ajaxUrl + "filter",
        data: $("#filter").serialize()
    }).done(function (data) {
        context.datatableApi.clear().rows.add(data).draw()});;
}

function clearFilter() {
    $("#filter")[0].reset();
    $.get(ajaxUrl, updateTable);
}

$(function () {
    makeEditable({
        ajaxUrl: ajaxUrl,
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "dateTime"
                },
                {
                    "data": "description"
                },
                {
                    "data": "calories"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "desc"
                ]
            ]
        }),
        updateTable: filteredTable
    });
});
