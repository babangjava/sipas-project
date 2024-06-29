<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="util" tagdir="/WEB-INF/tags/util" %>
<%@ taglib prefix="input" tagdir="/WEB-INF/tags/input" %>

<div class="container">
	<fieldset class="col-sm-10 bordure">

		<util:message message="${message}" messages="${messages}" />

		<s:url value="${saveAction}" var="url_form_submit" />
		<form:form class="form-horizontal" modelAttribute="uploaddocument" cssClass="well form-horizontal" method="POST" enctype="multipart/form-data" action="${url_form_submit}">
			<form:errors path="*" cssClass="alert alert-danger" element="div" />

			<c:if test="${mode != 'create'}">
				<form:hidden path="id" />
				<form:hidden path="type" />
				<form:hidden path="name" />
				<div class="form-group">
					<label for="id" class="col-sm-2 control-label">Id Menu</label>
					<div class="col-sm-10">
						<form:input id="id" path="id" class="form-control" maxLength="10" disabled="${mode != 'create'}" />
						<form:errors id="id_errors" path="id" cssClass="label label-danger" />
					</div>
				</div>
			</c:if>

			<div class="form-group">
				<label for="unitKerja" class="col-sm-2 control-label">Unit Kerja</label>
				<div class="col-sm-10">
					<form:select id="unitKerja" path="unitKerja" cssClass="form-control" onchange="renderKategori(this)">
						<form:option value="">--Select--</form:option>
						<c:forEach items="${menuitem}" var="item">
							<form:option value="${item.detail}">${item.detail}</form:option>
						</c:forEach>
					</form:select>
					<form:errors id="unitKerja_errors" path="unitKerja" cssClass="label label-danger" />
				</div>
			</div>

			<div class="form-group">
				<label for="kategori" class="col-sm-2 control-label">Kategori</label>
				<div class="col-sm-10">
					<form:select id="kategori" path="kategori" cssClass="form-control" onchange="renderDocument(this)">
						<form:option value="">--Select--</form:option>
					</form:select>
					<form:errors id="kategori_errors" path="kategori" cssClass="label label-danger" />
				</div>
			</div>

			<div class="form-group">
				<label for="namaDocument" class="col-sm-2 control-label">Document</label>
				<div class="col-sm-10">
					<form:select id="namaDocument" path="namaDocument" cssClass="form-control">
						<form:option value="">--Select--</form:option>
					</form:select>
					<form:errors id="namaDocument_errors" path="namaDocument" cssClass="label label-danger" />
				</div>
			</div>

			<div class="form-group">
				<label for="deskripsi" class="col-sm-2 control-label">Nama Document</label>
				<div class="col-sm-10">
					<form:input id="deskripsi" path="deskripsi" class="form-control" maxLength="250"  />
					<form:errors id="deskripsi_errors" path="deskripsi" cssClass="label label-danger" />
				</div>
			</div>

			<div class="form-group">
				<label for="odner" class="col-sm-2 control-label">Odner</label>
				<div class="col-sm-10">
					<form:input id="odner" path="odner" class="form-control" />
					<form:errors id="odner_errors" path="odner" cssClass="label label-danger" />
				</div>
			</div>

			<div class="form-group">
				<!-- The field label is defined in the messages file (for i18n) -->
				<label for="upload_fileUpload" class="col-sm-2 control-label">Upload Document</label>
				<div class="col-sm-10">
					<form:input id="upload_fileUpload" type="file" path="fileUpload" class="form-control"/>
					<form:errors id="upload_fileUpload_errors" path="fileUpload" cssClass="label label-danger"/>
				</div>
			</div>


			<!-- ACTION BUTTONS -->
			<div class="form-group">
				<div class="col-sm-offset-2 col-sm-2">
					<c:if test="${mode != 'create'}">
						<s:url var="deleteButtonURL" value="/uploaddocument/delete/${uploaddocument.id}" />
						<a role="button" class="btn btn-danger btn-block" href="${deleteButtonURL}"><s:message code="delete"/></a>
					</c:if>
				</div>
				<div class="col-sm-offset-4 col-sm-2">
					<s:url var="cancelButtonURL" value="/uploaddocument" />
					<a role="button" class="btn btn-default btn-block" href="${cancelButtonURL}"><s:message code="cancel"/></a>
				</div>
				<div class="col-sm-2">
					<button type="submit" class="btn btn-primary btn-lg btn-block"><s:message code="save"/></button>
				</div>
			</div>
		</form:form>
	</fieldset>
	<script type="text/javascript">
		$('#pageName').text("PPD");
		window.onload = function() {
			var unitKerja = $('#unitKerja').val();
			if(unitKerja!=""){
				renderKategori(unitKerja);
			}

			var kategori = $('#kategori').val();
			if(kategori!=""){
				renderDocument(kategori);
			}
		}

		function renderKategori(obj){
			var unitKerja = $('#unitKerja').val();
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/uploaddocument/list-onchange/'+unitKerja,
				contentType: "application/json",
				async: false,
				dataType: 'json',
				success: function(data) {
					$('#kategori').empty();
					$('#kategori').append('<option value="">--Select--</option>');
					$('#namaDocument').empty();
					$('#namaDocument').append('<option value="">--Select--</option>');
					for (var i = 0; i < data.length; i++) {
						var selected = '${uploaddocument.kategori}';
						if(data[i].detail == selected ){
							$('#kategori').append('<option value="'+data[i].detail+'" selected >'+data[i].detail+'</option>');
						}else{
							$('#kategori').append('<option value="'+data[i].detail+'">'+data[i].detail+'</option>');
						}
					}
				},
				error: function(xhr, textStatus, error) {
					alert("Eror Ajax Loading");
					console.log(xhr.statusText);
					console.log(textStatus);
					console.log(error);
				}
			});
		}

		function renderDocument(obj){
			var kategori = $('#kategori').val();
			$.ajax({
				type: 'POST',
				url: '${pageContext.request.contextPath}/uploaddocument/list-onchange/'+kategori,
				contentType: "application/json",
				async: false,
				dataType: 'json',
				success: function(data) {
					$('#namaDocument').empty();
					$('#namaDocument').append('<option value="">--Select--</option>');
					for (var i = 0; i < data.length; i++) {
						var selected = '${uploaddocument.namaDocument}';
						if(data[i].detail == selected){
							$('#namaDocument').append('<option value="'+data[i].detail+'" selected>'+data[i].detail+'</option>');
						}else{
							$('#namaDocument').append('<option value="'+data[i].detail+'">'+data[i].detail+'</option>');
						}
					}
				},
				error: function(xhr, textStatus, error) {
					alert("Eror Ajax Loading");
					console.log(xhr.statusText);
					console.log(textStatus);
					console.log(error);
				}
			});
		}

	</script>
</div>

