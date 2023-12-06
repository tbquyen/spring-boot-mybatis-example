const token = $("meta[name='_csrf']").attr("content");
const header = $("meta[name='_csrf_header']").attr("content");
const searchs = { categoryId: "0", content: "" };

const dataTable = $('#dataTable').DataTable({
	"orderMulti": true,
	"searching": false,
	"lengthChange": true,
	"processing": true,
	"serverSide": true,
	language: {
		url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json',
	},
	"ajax": {
		"url": "questions/page",
		"type": "POST",
		"data": (params, _settings) => {
			params.categoryId = searchs.categoryId;
			params.content = searchs.content;
		},
		"beforeSend": (jqXHR, settings) => {
			jqXHR.setRequestHeader(header, token);
			$("#txt-search").prop('disabled', true);
			$("#btn-search").prop('disabled', true);
		},
		"complete": (jqXHR, textStatus) => {
			$("#txt-search").prop('disabled', false);
			$("#btn-search").prop('disabled', false);
		},
		"error": (jqXHR, error, thrown) => {
			DialogAlert({ bgcolor: 'bg-danger', message: jqXHR.responseText });
		}
	},
	"columnDefs": [{ targets: [0, 4], width: '10px' },],
	"columns": [
		{ data: "id", name: "id", className: "text-center" },
		{ data: "categoryName", name: "category_id" },
		{ data: "content", name: "content", className: "text-break-down" },
		{ data: "created", name: "created", className: "text-center" },
		{
			data: "id", name: "edit", className: "dropdown text-center", orderable: false,
			render: (data, type, row, meta) => {
				return '<i role="button" class="bi bi-gear-fill" id="dropdown' + data +
					'" data-bs-toggle="dropdown" aria-expanded="false"></i>' +
					'<ul class="dropdown-menu" aria-labelledby="dropdown' + data + '">' +
					'<li><a class="dropdown-item bi bi-arrows-angle-expand" href="#" onclick=loadModel(this,' + data + ')> Xem</a></li>' +
					'<li><a class="dropdown-item bi bi-pencil-fill" href="#" onclick=loadModel(this,' + data + ')> Sửa</a></li>' +
					'<li><a class="dropdown-item bi bi-trash2-fill" href="#" onclick=remove(' + data + ')> Xoá</a></li>' +
					'</ul>';
			}
		},
	],
});

const loadModel = (target, id) => {
	const modal = $('#modal-area');
	const modalContent = modal.find("div.modal-content")[0];

	$.ajax({
		type: "GET",
		url: "questions/" + id,
		beforeSend: (jqXHR, settings) => {
			jqXHR.setRequestHeader(header, token);
			$(target).prop('disabled', true);
			$(modalContent).html('<div class="text-center"><div class="spinner-grow text-danger" role="status" style="width: 3rem; height: 3rem;"></div></div>');
			$(modal).modal({ backdrop: 'static' }).modal("show");
		},
		success: (data, textStatus, jqXHR) => {
			$(modalContent).html(data);
		},
		error: (jqXHR, textStatus, errorThrown) => {
			$(modalContent).html(jqXHR.responseText);
		},
		complete: (jqXHR, textStatus) => {
			$(target).prop('disabled', false);
			const EditForm = $(modalContent).children('form');
			EditForm.submit((event) => { update(event, id); });
		},
	});
};

const update = (event, id) => {
	event.preventDefault();

	const modal = $('#modal-area');
	const modalContent = $(modal).find("div.modal-content")[0];

	const target = event.currentTarget;

	$.ajax({
		url: "questions/" + id,
		type: "POST",
		data: $(target).serialize(),
		beforeSend: (jqXHR, settings) => {
			jqXHR.setRequestHeader(header, token);
			$(target).find(":input").prop('disabled', true);
			$("#form-spinner").removeClass("invisible");
		},
		success: (data, textStatus, jqXHR) => {
			DialogAlert({ message: data.message });
			$(modal).modal("hide");
			dataTable.ajax.reload(null, false);
		},
		error: (jqXHR, textStatus, errorThrown) => {
			if (jqXHR.status === 400) {
				$(modalContent).html(jqXHR.responseText);
			} else {
				$(modalContent).html(jqXHR.responseText);
			}
		},
		complete: (jqXHR, textStatus) => {
			const EditForm = $(modalContent).children('form');
			EditForm.submit((event) => { update(event, id); });
		},
	});
}

const remove = (id) => {
	DialogConfirm(null, () => {
		$.ajax({
			url: "questions/" + id,
			type: "DELETE",
			beforeSend: (jqXHR, settings) => {
				jqXHR.setRequestHeader(header, token);
			},
			success: (data, textStatus, jqXHR) => {
				dataTable.ajax.reload(null, false);
				DialogAlert({ message: data.message });
			},
			error: (jqXHR, textStatus, errorThrown) => {
				DialogAlert({ bgcolor: 'bg-danger', message: data.message });
			},
			complete: (jqXHR, textStatus) => {

			},
		});
	});
}

$('#btn-search').click(() => {
	searchs.categoryId = $('#txt-categoryId').val();
	searchs.content = $('#txt-content').val();
	dataTable.ajax.reload();
});

$('#btn-new').click(function() { loadModel(this, 0); });

$('#btn-import').click(() => {
	const input = $('<input type="file" multiple accept=".csv">');
	input.click();
	input.change(() => {
		const files = input.prop('files');

		const formData = new FormData();
		[...files].forEach(file => {
			formData.append("files", file);
		});

		$.ajax({
			url: "questions/file",
			type: "POST",
			data: formData,
			contentType: false,
			processData: false,
			beforeSend: (jqXHR, settings) => {
				jqXHR.setRequestHeader(header, token);
				$('#btn-import').prop("disabled", true);
			},
			success: (data, textStatus, jqXHR) => {
				DialogAlert({ message: jqXHR.responseText });
			},
			error: (jqXHR, textStatus, errorThrown) => {
				DialogAlert({ bgcolor: 'bg-danger', message: jqXHR.responseText });
			},
			complete: (jqXHR, textStatus) => {
				$('#btn-import').prop("disabled", false);
			},
		});
	});
});