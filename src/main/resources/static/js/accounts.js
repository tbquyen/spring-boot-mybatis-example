const searchs = { username: "", role: "" };

const dataTable = $('#usersTable').DataTable({
	"orderMulti": true,
	"searching": false,
	"lengthChange": false,
	"processing": true,
	"serverSide": true,
	language: {
		url: '//cdn.datatables.net/plug-ins/1.13.7/i18n/vi.json',
	},
	"ajax": {
		"url": "accounts",
		"type": "POST",
		"data": function(params, _settings) {
			params.username = searchs.username;
			params.role = searchs.role;
		},
		"beforeSend": function(jqXHR, settings) {
			$("#txt-search").prop('disabled', true);
			$("#txt-role").prop('disabled', true);
			$("#btn-search").prop('disabled', true);
		},
		"complete": function(jqXHR, textStatus) {
			$("#txt-search").prop('disabled', false);
			$("#txt-role").prop('disabled', false);
			$("#btn-search").prop('disabled', false);
		},
		"error": function(jqXHR, error, thrown) {
			DialogAlert({ bgcolor: 'bg-danger', message: jqXHR.responseText });
		}
	},
	"columnDefs": [{ targets: [0, 5], width: '10px' },],
	"columns": [
		{ data: "id", name: "id", className: "text-center" },
		{ data: "username", name: "username" },
		{ data: "password", name: "password", render: function() { return '************'; } },
		{
			data: "role", name: "role", render: function(data) {
				if (data === 'ROLE_ADMIN') {
					return 'Quản lý';
				}
				return 'Học viên';
			}
		},
		{
			data: "status", name: "status", className: "text-center", render: function(data) {
				if (data === 0) {
					return '<i class="bi bi-shield-fill-exclamation" style="color: brown"></i>';
				}
				return '<i class="bi bi-shield-fill-check" style="color: forestgreen"></i>';
			}
		},
		{
			data: "id", name: "edit", className: "dropdown text-center", orderable: false,
			render: function(data, type, row, meta) {
				return '<i role="button" class="bi bi-gear-fill" id="dropdown' + data +
					'" data-bs-toggle="dropdown" aria-expanded="false"></i>' +
					'<ul class="dropdown-menu" aria-labelledby="dropdown' + data + '">' +
					'<li><a class="dropdown-item bi bi-pencil-fill" href="#" onclick=loadModel(this,' + data + ')> Sửa</a></li>' +
					'<li><a class="dropdown-item bi bi-trash2-fill" href="#" onclick=remove(' + data + ')> Xoá</a></li>' +
					'</ul>';
			}
		},
	],
});

const loadModel = function(target, id) {
	const modal = $('#modal-area');
	const modalContent = modal.find("div.modal-content")[0];

	$(target).prop('disabled', true);

	$.ajax({
		type: "GET",
		url: "accounts/" + id,
		beforeSend: function(jqXHR, settings) {
			$(modalContent).html('<div class="text-center"><div class="spinner-grow text-danger" role="status" style="width: 3rem; height: 3rem;"></div></div>');
			$(modal).modal({ backdrop: 'static' }).modal("show");
		},
		success: function(data, textStatus, jqXHR) {
			$(modalContent).html(data);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			DialogAlert({ bgcolor: 'bg-danger', message: jqXHR.responseText });
		},
		complete: function(jqXHR, textStatus) {
			$(target).prop('disabled', false);
			const UsersForm = $(modalContent).children('form');
			UsersForm.off().submit(function(event) { update(event, id); });
		},
	});
};

const update = function(event, id) {
	event.preventDefault();

	const modal = $('#modal-area');
	const modalContent = $(modal).find("div.modal-content")[0];

	const target = event.currentTarget;

	$.ajax({
		url: "accounts/" + id,
		type: "POST",
		data: $(target).serialize(),
		beforeSend: function(jqXHR, settings) {
			$(target).find(":input").prop('disabled', true);
			$("#form-spinner").removeClass("invisible");
		},
		success: function(data, textStatus, jqXHR) {
			DialogAlert({ message: data.message });
			$(modal).modal("hide");
			dataTable.ajax.reload(null, false);
		},
		error: function(jqXHR, textStatus, errorThrown) {
			if (jqXHR.status === 400) {
				$(modalContent).html(jqXHR.responseText);
			} else {
				DialogAlert({ bgcolor: 'bg-danger', message: jqXHR.responseText });
			}
		},
		complete: function(jqXHR, textStatus) {
			$(target).find(":input").prop('disabled', false);
			$("#form-spinner").addClass("invisible");
			const UsersForm = $(modalContent).children('form');
			UsersForm.off().submit(function(event) { update(event, id); });
		},
	});
}

const remove = function(id) {
	DialogConfirm(null, function() {
		$.ajax({
			url: "accounts/" + id,
			type: "DELETE",
			beforeSend: function(jqXHR, settings) {
			},
			success: function(data, textStatus, jqXHR) {
				dataTable.ajax.reload(null, false);
				DialogAlert({ message: data.message });
			},
			error: function(jqXHR, textStatus, errorThrown) {
				DialogAlert({ bgcolor: 'bg-danger', message: data.message });
			},
			complete: function(jqXHR, textStatus) {

			},
		});
	});
}

$('#btn-new').click(function() { loadModel(this, 0); });
$('#btn-search').click(function() {
	searchs.username = $('#txt-search').val();
	searchs.role = $("#txt-role").val();
	dataTable.ajax.reload();
});
