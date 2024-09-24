$.ajaxPrefilter(function(options, originalOptions, _jqXHR) {
	const params = originalOptions.data;

	if (!params || !Array.isArray(params.order)) return;
	params.order.forEach(function(order, index) {
		params['order[' + index + '].name'] = params.columns[order.column].name;
		params['order[' + index + '].column'] = order.column;
		params['order[' + index + '].dir'] = order.dir;
	});
	delete params.order;

	if (!params || !Array.isArray(params.columns)) return;
	params.columns.forEach(function(column, index) {
		params['columns[' + index + '].data'] = column.data;
		params['columns[' + index + '].name'] = column.name;
		params['columns[' + index + '].searchable'] = column.searchable;
		params['columns[' + index + '].orderable'] = column.orderable;
		params['columns[' + index + '].search.regex'] = column.search.regex;
		params['columns[' + index + '].search.value'] = column.search.value;
	});
	delete params.columns;

	if (!params || !params.search) return;
	params['search.regex'] = params.search.regex;
	params['search.value'] = params.search.value;
	delete params.search;

	options.data = $.param(params);
});

const DialogConfirm = function(message, okFunction, noFunction) {
	if (!message) {
		message = 'Bạn thực sự muốn xoá không?';
	}

	const dialog =
		'<div class="modal fade" aria-hidden="true">' +
		'<div class="modal-dialog modal-sm">' +
		'<div class="modal-content">' +
		'<div class="modal-body">' +
		'<h5 class=""modal-title col-12 text-center">' + message + '</h5>' +
		'</div>' +
		'</div>' +
		'</div>' +
		'</div>';

	const modal = $(dialog);
	const modalBody = modal.find("div.modal-body")[0];

	const btnOK = $('<button type="button" class="btn btn-primary btn-sm float-end me-1" data-bs-dismiss="modal">Đồng ý</button>');
	btnOK.appendTo(modalBody);
	if (typeof okFunction === "function") {
		btnOK.click(function() { okFunction(); });
	}

	const btnNo = $('<button type="button" class="btn btn-secondary btn-sm float-end me-1" data-bs-dismiss="modal">Thoát</button>');
	btnNo.appendTo(modalBody);
	if (typeof noFunction === "function") {
		btnNo.click(function() { noFunction(); });
	}

	modal.appendTo('body');

	modal.modal({ backdrop: false }).modal("show");

	modal.off().on("hidden.bs.modal", function() {
		modal.remove();
	});
};

const DialogAlert = function(options) {
	var settings = $.extend({
		bgcolor: "bg-success",
		textcolor: "text-white",
		message: "Hello, world! This is a toast message.",
		animation: true,
		autohide: false,
		delay: 5000,
		icon: "bi bi-check-circle",
	}, options);

	const dom = $(
		'<div class="toast text-white ' + settings.bgcolor + ' border-0" role="alert" aria-live="assertive" aria-atomic="true">' +
		'<div class="d-flex">' +
		'<div class="toast-body"><i class="' + settings.icon + '"></i> ' + settings.message + '</div>' +
		'<button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>' +
		'</div>' +
		'</div>');

	dom.appendTo('#toast-container');

	new bootstrap.Toast(dom, {
		animation: settings.animation,
		autohide: settings.autohide,
		delay: settings.delay
	}).show();

	$(dom).on('hidden.bs.toast', function() {
		dom.remove();
	});
};